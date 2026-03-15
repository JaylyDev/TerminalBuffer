package org.jayly;

import java.util.ArrayList;

/**
 * Represents an editor to edit files
 */
public class Editor {
    /**
     * The last N lines that fit the screen dimensions (e.g., 80×24). This is the
     * editable part and what users see.
     */
    private ArrayList<LineGrid> screenGrid;
    /**
     * Lines that scrolled off the top of the screen, preserved for history and
     * unmodifiable. Users can scroll up to view them.
     */
    private ArrayList<LineGrid> scrollbackBuffer;
    private TerminalBuffer buffer;
    private Document document;
    /**
     * The line number of the document that corresponds to the first row of the
     * screen.
     */
    private int lineOffset;

    public Editor(TerminalBuffer buffer, Document document) {
        this.buffer = buffer;
        this.document = document;
        this.lineOffset = 0;
        this.screenGrid = new ArrayList<LineGrid>();
        this.scrollbackBuffer = new ArrayList<LineGrid>();

        this.rebuildScreenGrid();
    }

    /**
     * Rebuild screenGrid to reflect the current viewport of the document.
     * screenGrid contains references to document lines from lineOffset to
     * lineOffset+height.
     */
    private void rebuildScreenGrid() {
        screenGrid.clear();
        final int height = buffer.getHeight();
        for (int i = 0; i < height; i++) {
            final int lineNumber = lineOffset + i;
            LineGrid line = document.getLine(lineNumber);
            if (line != null) {
                screenGrid.add(line);
            } else {
                // Add empty line if document doesn't have this line yet
                screenGrid.add(new LineGrid(new ArrayList<>()));
            }
        }
    }

    /**
     * Handle automatic scrolling when content exceeds screen height.
     * Moves off-screen lines to scrollbackBuffer and adjusts lineOffset.
     */
    private void handleScrolling() {
        final int height = buffer.getHeight();
        final int totalLines = document.getLineCount();

        // If document exceeds screen, scroll to show the bottom
        if (totalLines > height) {
            final int newOffset = totalLines - height;

            // Move lines that scrolled off the top to scrollbackBuffer
            if (lineOffset < newOffset) {
                for (int i = lineOffset; i < newOffset; i++) {
                    LineGrid offScreenLine = document.getLine(i);
                    if (offScreenLine != null) {
                        scrollbackBuffer.add(offScreenLine);

                        if (scrollbackBuffer.size() > buffer.getScrollbackMaxSize()) {
                            scrollbackBuffer.remove(0);
                        }
                    }
                }
            }

            lineOffset = newOffset;
        }
    }

    public int getLineNumberFromRow(int row) {
        return this.lineOffset + row;
    }

    public void deleteCharacter(int row, int column) {
        final int lineNumber = this.getLineNumberFromRow(row);
        LineGrid line = document.getLine(lineNumber);
        if (line == null || column < 0 || column >= line.length()) {
            return;
        }
        line.deleteChar(column);
        rebuildScreenGrid();
        handleScrolling();

        Cursor cursor = buffer.getCursor();
        cursor.moveCursorTo(row, column - 1);
    }

    /**
     * Insert a character to document with current styles.
     */
    public void insertCharacter(int row, int column, Character character) {
        // Get line grid
        final int lineNumber = this.getLineNumberFromRow(row);
        LineGrid line = document.getLine(lineNumber);
        if (line == null) {
            line = new LineGrid(new ArrayList<CharacterCell>());
            document.insertLine(lineNumber, line);
        }

        // Create new character cell
        CharacterCell newCell = new CharacterCell();
        newCell.setCharacter(character);
        if (this.buffer.getForegroundColor() != null) {
            newCell.setForegroundColor(this.buffer.getForegroundColor());
        }
        if (this.buffer.getBackgroundColor() != null) {
            newCell.setBackgroundColor(this.buffer.getBackgroundColor());
        }
        for (StyleFlag style : StyleFlag.values()) {
            if (this.buffer.isStyleEnabled(style)) {
                newCell.setStyle(style, true);
            }
        }

        // Insert character cell into line grid
        line.insertChar(column, newCell);

        // Update cursor
        Cursor cursor = buffer.getCursor();
        cursor.moveCursorTo(row, column + 1);
    }

    /**
     * Insert a string of text into document with current styles.
     * 
     * @param row
     * @param column
     * @param text
     */
    public void insertText(int row, int column, String text) {
        for (int i = 0; i < text.length(); i++) {
            this.insertCharacter(row, column + i, text.charAt(i));
        }
        rebuildScreenGrid();
        handleScrolling();
    }

    /**
     * Insert a new line in a specific row
     */
    public void insertNewLine(int row) {
        final int lineNumber = this.getLineNumberFromRow(row);
        document.insertLine(lineNumber, new LineGrid(new ArrayList<CharacterCell>()));
        rebuildScreenGrid();
        handleScrolling();

        // Set cursor to the beginning of the new line
        Cursor cursor = buffer.getCursor();
        cursor.moveCursorTo(row, 0);
    }

    public void removeNewLine(int row) {
        final int lineNumber = this.getLineNumberFromRow(row);
        if (lineNumber <= 0) {
            return;
        }
        document.deleteLine(lineNumber);

        // Set cursor to the end of the previous line
        final LineGrid previousLine = document.getLine(lineNumber - 1);
        if (previousLine != null) {
            Cursor cursor = buffer.getCursor();
            cursor.moveCursorTo(row - 1, previousLine.length());
        }
    }

    /**
     * Get character at position from screen and scrollback.
     */
    public Character getCharacterAt(int row, int column) {
        final CharacterCell cell = this.getAttributesAt(row, column);
        if (cell == null) {
            return null;
        }
        return cell.getCharacter();
    }

    /**
     * Get attributes at position from screen and scrollback.
     */
    public CharacterCell getAttributesAt(int row, int column) {
        final LineGrid lineGrid = screenGrid.get(row);
        if (lineGrid == null) {
            return null;
        }
        final ArrayList<CharacterCell> cells = lineGrid.getChars();
        if (cells == null || column < 0 || column >= cells.size()) {
            return null;
        }
        return cells.get(column);
    }

    /**
     * Returns display text of row from screen grid. This represents what the line
     * should be seen by the user in terminal.
     * 
     * @param row The row number of the screen or scrollback, not the line number of
     *            the file.
     */
    public String getLine(int row) {
        if (row < 0 || row >= screenGrid.size()) {
            return "";
        }
        LineGrid line = screenGrid.get(row);
        String text = line.getDisplayText(buffer.getWidth());
        return text;
    }

    /**
     * Returns display text of the entire screen content. This represents what the
     * user should see in terminal.
     */
    public String getScreenContent() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < screenGrid.size(); row++) {
            sb.append(this.getLine(row));
            if (row < screenGrid.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
