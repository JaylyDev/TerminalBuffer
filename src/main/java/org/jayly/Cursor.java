package org.jayly;

public class Cursor {
    private int column;
    private int row;
    private TerminalBuffer buffer;

    public Cursor(TerminalBuffer buffer) {
        this.buffer = buffer;
        this.column = 0;
        this.row = 0;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        if (this.outOfBounds(column, this.getRow())) {
            return;
        }
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        if (this.outOfBounds(this.getColumn(), row)) {
            return;
        }
        this.row = row;
    }

    public void moveCursorUp() {
        this.setRow(this.getRow() - 1);
    }

    public void moveCursorDown() {
        this.setRow(this.getRow() + 1);
    }

    public void moveCursorLeft() {
        this.setColumn(this.getColumn() - 1);
    }

    public void moveCursorRight() {
        this.setColumn(this.getColumn() + 1);
    }

    public void moveCursorTo(int row, int column) {
        this.setRow(row);
        this.setColumn(column);
    }

    /**
     * Check if the cursor is out of bounds of the terminal buffer
     */
    public Boolean outOfBounds(int column, int row) {
        return column < 0 || column >= buffer.getWidth() || row < 0 || row >= buffer.getHeight();
    }
}
