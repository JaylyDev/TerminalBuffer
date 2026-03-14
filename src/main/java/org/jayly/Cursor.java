package org.jayly;

public class Cursor {
    private Integer column;
    private Integer row;
    private TerminalBuffer buffer;

    public Cursor(TerminalBuffer buffer) {
        this.buffer = buffer;
        this.column = 0;
        this.row = 0;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        if (this.outOfBounds(column, this.getRow())) {
            return;
        }
        this.column = column;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        if (this.outOfBounds(this.getColumn(), row)) {
            return;
        }
        this.row = row;
    }

    public void moveCursorUp() {
        this.setColumn(this.getColumn() - 1);
    }

    public void moveCursorDown() {
        this.setColumn(this.getColumn() + 1);
    }

    public void moveCursorLeft() {
        this.setRow(this.getRow() - 1);
    }

    public void moveCursorRight() {
        this.setRow(this.getRow() + 1);
    }

    public Boolean outOfBounds(Integer column, Integer row) {
        return this.column >= column && this.row >= row;
    }
}
