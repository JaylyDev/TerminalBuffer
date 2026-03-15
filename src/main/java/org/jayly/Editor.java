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
    private ArrayList<ArrayList<CharacterCell>> screenGrid;
    /**
     * Lines that scrolled off the top of the screen, preserved for history and
     * unmodifiable. Users can scroll up to view them.
     */
    private ArrayList<ArrayList<CharacterCell>> scrollbackBuffer;
    private TerminalBuffer buffer;
    private Document document;
    /**
     * The line number of the top line of the screen.
     */
    private Integer topLineNumber;

    public Editor(TerminalBuffer buffer, Document document) {
        this.buffer = buffer;
        this.document = document;
        this.topLineNumber = 0;
    }

    public Integer getLineNumberFromRow(Integer row) {
        return this.topLineNumber + row;
    }

    public void deleteCharacter(Integer row, Integer column) {
        final Integer lineNumber = this.getLineNumberFromRow(row);
        String line = document.getLine(lineNumber);
        if (line == null || column < 0 || column >= line.length()) {
            return;
        }
        line = line.substring(0, column) + line.substring(column + 1);
        document.setLine(lineNumber, line);

        Cursor cursor = buffer.getCursor();
        cursor.moveCursorTo(row, column - 1);
    }

    public void insertCharacter(Integer row, Integer column, Character character) {
        final Integer lineNumber = this.getLineNumberFromRow(row);
        String line = document.getLine(lineNumber);
        if (line == null) {
            line = "";
        }
        if (column < 0 || column > line.length()) {
            return;
        }
        line = line.substring(0, column) + character + line.substring(column);
        document.setLine(lineNumber, line);

        Cursor cursor = buffer.getCursor();
        cursor.moveCursorTo(row, column + 1);
    }

    public void insertNewLine(Integer row) {
        final Integer lineNumber = this.getLineNumberFromRow(row);
        document.insertLine(lineNumber, "");
        
        // Set cursor to the beginning of the new line
        Cursor cursor = buffer.getCursor();
        cursor.moveCursorTo(row, 0);
    }

    public void removeNewLine(Integer row) {
        final Integer lineNumber = this.getLineNumberFromRow(row);
        if (lineNumber <= 0) {
            return;
        }
        document.deleteLine(lineNumber);

        // Set cursor to the end of the previous line
        final String previousLine = document.getLine(lineNumber - 1);
        if (previousLine != null) {
            Cursor cursor = buffer.getCursor();
            cursor.moveCursorTo(row - 1, previousLine.length());
        }
    }

    /**
     * Get character at position from screen and scrollback.
     */
    public Character getCharacterAt(Integer row, Integer column) {
        final CharacterCell cell = this.getAttributesAt(row, column);
        if (cell == null) {
            return null;
        }
        return cell.getCharacter();
    }

    /**
     * Get attributes at position from screen and scrollback.
     */
    public CharacterCell getAttributesAt(Integer row, Integer column) {
        final ArrayList<CharacterCell> rowGrid = screenGrid.get(row);
        if (rowGrid == null || column < 0 || column >= rowGrid.size()) {
            return null;
        }
        return rowGrid.get(column);
    }

    /**
     * Get line at position from screen and scrollback.
     * 
     * @param row The row number of the screen or scrollback, not the line number of
     *            the file.
     */
    public String getLine(Integer row) {
        return null;
    }

    public String getScreenContent() {
        return null;
    }
}
