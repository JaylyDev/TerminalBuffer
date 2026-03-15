package org.jayly;

import java.util.ArrayList;

public class RowGrid extends LineGrid {
    /**
     * Which line number of a document this screen row came from.
     */
    private int lineOffset;

    /**
     * The offset (start column) within the document line where this row starts.
     * Used for wrapped lines to calculate the actual document column position.
     */
    private int columnOffset;

    public RowGrid(ArrayList<CharacterCell> chars) {
        super(chars);
        this.columnOffset = 0;
    }

    public int getLineOffset() {
        return lineOffset;
    }

    public void setLineOffset(int lineOffset) {
        this.lineOffset = lineOffset;
    }

    public int getColumnOffset() {
        return columnOffset;
    }

    public void setColumnOffset(int columnOffset) {
        this.columnOffset = columnOffset;
    }
}