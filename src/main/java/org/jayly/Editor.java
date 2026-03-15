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
    private ArrayList<RowGrid> screenGrid;
    /**
     * Lines that scrolled off the top of the screen, preserved for history and
     * unmodifiable. Users can scroll up to view them.
     */
    private ArrayList<RowGrid> scrollbackBuffer;
    private TerminalBuffer buffer;
    private Document document;

    public Editor(TerminalBuffer buffer, Document document) {
        this.buffer = buffer;
        this.document = document;
        this.screenGrid = new ArrayList<RowGrid>();
        this.scrollbackBuffer = new ArrayList<RowGrid>();

        this.rebuildScreenGridAndScrollBackBuffer();
    }

    /**
     * Rebuild screenGrid and scrollBackBuffer based on document content and buffer
     * width and height.
     */
    private void rebuildScreenGridAndScrollBackBuffer() {
        screenGrid.clear();
        scrollbackBuffer.clear();

        // Collect all rows from all document lines
        ArrayList<RowGrid> allRows = new ArrayList<>();

        int totalLines = document.getLineCount();

        for (int lineNum = 0; lineNum < totalLines; lineNum++) {
            LineGrid line = document.getLine(lineNum);
            if (line != null) {
                ArrayList<LineGrid> Rows = splitLineIntoRows(line, buffer.getWidth());

                // Track which document line each row came from and the offset within that line
                int columnOffset = 0;
                for (LineGrid Row : Rows) {
                    RowGrid rowGrid = new RowGrid(Row.getChars());
                    rowGrid.setLineOffset(lineNum);
                    rowGrid.setColumnOffset(columnOffset);
                    columnOffset += Row.getChars().size();
                    allRows.add(rowGrid);
                }
            }
        }

        // Last `height` rows go to screen; rest go to scrollback
        int screenStartIdx = Math.max(0, allRows.size() - buffer.getHeight());

        for (int i = 0; i < screenStartIdx; i++) {
            scrollbackBuffer.add(allRows.get(i));
        }

        for (int i = screenStartIdx; i < allRows.size(); i++) {
            screenGrid.add(allRows.get(i));
        }

        // Pad screen with empty lines if needed
        while (screenGrid.size() < buffer.getHeight()) {
            RowGrid rowGrid = new RowGrid(new ArrayList<>());
            rowGrid.setLineOffset(-1); // no corresponding document line
            screenGrid.add(rowGrid);
        }
    }

    /**
     * Split a logical line into rows based on width.
     * Each row contains at most `width` characters.
     */
    private ArrayList<LineGrid> splitLineIntoRows(LineGrid line, int width) {
        ArrayList<LineGrid> result = new ArrayList<>();
        ArrayList<CharacterCell> cells = line.getChars();

        if (cells.isEmpty()) {
            result.add(new LineGrid(new ArrayList<>()));
            return result;
        }

        for (int i = 0; i < cells.size(); i += width) {
            int endIdx = Math.min(i + width, cells.size());
            ArrayList<CharacterCell> rowCells = new ArrayList<>(cells.subList(i, endIdx));
            result.add(new LineGrid(rowCells));
        }

        return result;
    }

    private int getLineNumberFromRow(int row) {
        if (row < 0 || row >= screenGrid.size()) {
            return -1;
        }
        return screenGrid.get(row).getLineOffset();
    }

    /**
     * Scroll up the screen grid by one line.
     */
    public void scrollUpScreenGrid(int numberOfRows) {
        // Remove last N rows from scrollBackBuffer and add to the top of screenGrid
        for (int i = 0; i < numberOfRows && !scrollbackBuffer.isEmpty(); i++) {
            RowGrid rowGrid = scrollbackBuffer.remove(scrollbackBuffer.size() - 1);
            screenGrid.add(0, rowGrid);
        }
        // Remove last N rows from screenGrid
        while (screenGrid.size() > buffer.getHeight()) {
            screenGrid.remove(screenGrid.size() - 1);
        }
    }

    /**
     * Scroll down the screen grid by pushing rows to scrollback.
     */
    public void scrollDownScreenGrid(int numberOfRows) {
        // Move top rows from screenGrid to scrollbackBuffer
        for (int i = 0; i < numberOfRows && screenGrid.size() > 0; i++) {
            RowGrid rowGrid = screenGrid.remove(0);
            scrollbackBuffer.add(rowGrid);
        }
        // Trim scrollback if it exceeds max size
        while (scrollbackBuffer.size() > buffer.getScrollbackMaxSize()) {
            scrollbackBuffer.remove(0);
        }
    }

    /**
     * Delete a character at screen position.
     * Modifies the document, then rebuilds the screen to reflect word wrapping.
     */
    public void deleteCharacter() {
        Cursor cursor = buffer.getCursor();
        int row = cursor.getRow();
        int column = cursor.getColumn();
        final int lineNumber = getLineNumberFromRow(row);
        if (lineNumber < 0) {
            return;
        }

        LineGrid line = document.getLine(lineNumber);
        if (line == null) {
            return;
        }

        line.deleteChar(column);
        rebuildScreenGridAndScrollBackBuffer();

        cursor.moveCursorTo(row, column - 1);
    }

    /**
     * Insert a character into the document at the given screen position with
     * current styles.
     * 
     * Converts screen row coordinate to document line, inserts into document,
     * then rebuilds screen to handle word wrapping.
     */
    public void insertCharacter(Character character) {
        Cursor cursor = buffer.getCursor();
        int row = cursor.getRow();
        int lineNumber = getLineNumberFromRow(row);

        // If no document line exists for this screen row, create one
        if (lineNumber < 0) {
            document.insertLine(document.getLineCount(), new LineGrid(new ArrayList<>()));
            rebuildScreenGridAndScrollBackBuffer();
            lineNumber = getLineNumberFromRow(row);
        }

        LineGrid line = document.getLine(lineNumber);
        if (line == null) {
            return;
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

        // Always append character to the end of the line
        // This ensures sequential character insertion works correctly
        line.insertChar(line.length(), newCell);

        // Rebuild screen from document to handle word wrapping
        rebuildScreenGridAndScrollBackBuffer();

        // Update cursor position to reflect the new character and wrapping
        cursor.moveCursorRight();
    }

    /**
     * Insert a string of text into the document at the given screen position with
     * current styles.
     */
    public void insertText(String text) {
        for (int i = 0; i < text.length(); i++) {
            this.insertCharacter(text.charAt(i));
        }
    }

    /**
     * Insert a new line in a specific row
     */
    public void insertEmptyLine() {
        Cursor cursor = buffer.getCursor();
        int row = cursor.getRow();
        int lineNumber = getLineNumberFromRow(row);

        if (lineNumber < 0) {
            lineNumber = document.getLineCount() - 1;
        }

        if (lineNumber < 0) {
            document.insertLine(0, new LineGrid(new ArrayList<>()));
        } else {
            document.insertLine(lineNumber + 1, new LineGrid(new ArrayList<>()));
        }

        rebuildScreenGridAndScrollBackBuffer();

        // Move cursor to the beginning of the next line
        int nextRow = row + 1;
        if (nextRow >= buffer.getHeight()) {
            // If the next row is beyond the screen, clamp to last row
            // The rebuild already moved content to scrollback as needed
            cursor.moveCursorTo(buffer.getHeight() - 1, 0);
        } else {
            cursor.moveCursorTo(nextRow, 0);
        }
    }

    public void removeLine() {
        Cursor cursor = buffer.getCursor();
        int row = cursor.getRow();
        final int lineNumber = getLineNumberFromRow(row);
        if (lineNumber < 0) {
            return;
        }

        document.deleteLine(lineNumber);
        rebuildScreenGridAndScrollBackBuffer();

        // Set cursor to the end of the previous line
        if (row > 0) {
            final int prevLineNumber = getLineNumberFromRow(row - 1);
            if (prevLineNumber >= 0) {
                LineGrid previousLine = document.getLine(prevLineNumber);
                if (previousLine != null) {
                    cursor.moveCursorTo(row - 1, previousLine.length());
                }
            }
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

    public String getScrollbackContent() {
        StringBuilder sb = new StringBuilder();
        for (LineGrid line : scrollbackBuffer) {
            sb.append(line.getDisplayText(buffer.getWidth()));
            sb.append("\n");
        }
        return sb.toString();
    }
}
