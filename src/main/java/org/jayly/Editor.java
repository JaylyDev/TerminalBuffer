package org.jayly;

/**
 * Represents an editor to edit files
 */
public class Editor {
    TerminalBuffer textBuffer;
    public Editor(TerminalBuffer textBuffer) {
        this.textBuffer = textBuffer;
    }
    public void deleteCharacter(Integer row, Integer column) {
        setCursor(row, column);
    }
    public void insertCharacter(Integer row, Integer column) {
        setCursor(row, column);
    }

    public void insertNewLine(Integer row, Integer column) {
        setCursor(row, column);
    }

    public void removeNewLine(Integer row, Integer column) {
        setCursor(row, column);
    }

    public void setCursor(Integer row, Integer column) {}
}
