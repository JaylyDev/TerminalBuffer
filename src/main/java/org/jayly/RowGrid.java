package org.jayly;

import java.util.ArrayList;

public class RowGrid extends LineGrid {
    /**
     * Which line number of a document this screen row came from.
     */
    private int lineNumber;

    public RowGrid(ArrayList<CharacterCell> chars) {
        super(chars);
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}