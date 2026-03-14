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
    private ArrayList<String> scrollbackBuffer;
    private TerminalBuffer buffer;
    private Document document;

    public Editor(TerminalBuffer buffer, Document document) {
        this.buffer = buffer;
        this.document = document;
    }

    public void deleteCharacter(Integer row, Integer column) {
        String line = document.getLine(row);
        if (line == null || column < 0 || column >= line.length()) {
            return;
        }
        line = line.substring(0, column) + line.substring(column + 1);
        document.setLine(row, line);

        setCursor(row, column);
    }

    public void insertCharacter(Integer row, Integer column) {
        String line = document.getLine(row);
        if (line == null) {
            line = "";
        }
        if (column < 0 || column > line.length()) {
            return;
        }
        line = line.substring(0, column) + " " + line.substring(column);
        document.setLine(row, line);

        setCursor(row, column);
    }

    public void insertNewLine(Integer row) {
        document.insertLine(row, "");
        setCursor(row, 0);
    }

    public void removeNewLine(Integer row) {
        document.deleteLine(row);
        setCursor(row, 0);
    }

    public void setCursor(Integer row, Integer column) {
        Cursor cursor = buffer.getCursor();
        cursor.setRow(row);
        cursor.setColumn(column);
    }

    /**
     * Get character at position from screen and scrollback.
     */
    public String getCharacterAt(Integer row, Integer column) {
        return null;
    }

    /**
     * Get attributes at position from screen and scrollback.
     */
    public String getAttributesAt(Integer row, Integer column) {
        return null;
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
