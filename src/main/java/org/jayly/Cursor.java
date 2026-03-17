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

    public void moveUp() {
        this.setRow(this.getRow() - 1);
    }

    public void moveUp(int cells) {
        for (int i = 0; i < cells; i++) {
            this.moveUp();
        }
    }

    public void moveDown() {
        this.setRow(this.getRow() + 1);
    }

    public void moveDown(int cells) {
        for (int i = 0; i < cells; i++) {
            this.moveDown();
        }
    }

    public void moveLeft() {
        final int nextColumn = this.getColumn() - 1;
        if (nextColumn < 0) {
            int nextRow = this.getRow() - 1;
            // Only wrap if there's a previous row available
            if (nextRow >= 0) {
                this.row = nextRow;
                this.column = buffer.getWidth() - 1; // Move to the end of the previous line
            }
        } else {
            this.column = nextColumn;
        }
    }

    public void moveLeft(int cells) {
        for (int i = 0; i < cells; i++) {
            this.moveLeft();
        }
    }

    public void moveRight() {
        final int nextColumn = this.getColumn() + 1;
        // If cursor reaches the right edge, wrap to next line at column 0
        if (nextColumn >= buffer.getWidth()) {
            int nextRow = this.getRow() + 1;
            // Only wrap if there's a next row available
            if (nextRow < buffer.getHeight()) {
                this.row = nextRow;
                this.column = 0;
            }
        } else {
            this.column = nextColumn;
        }
    }

    public void moveRight(int cells) {
        for (int i = 0; i < cells; i++) {
            this.moveRight();
        }
    }

    public void moveTo(int row, int column) {
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
