package org.jayly;

import java.util.ArrayList;

public class LineGrid {
    private ArrayList<CharacterCell> chars;

    public LineGrid(ArrayList<CharacterCell> chars) {
        this.chars = chars;
    }

    public void setChars(ArrayList<CharacterCell> chars) {
        this.chars = chars;
    }

    public ArrayList<CharacterCell> getChars() {
        return chars;
    }

    public void insertChar(int column, CharacterCell newChar) {
        if (column < 0 || column > chars.size()) {
            return;
        }
        chars.add(column, newChar);
    }

    public void deleteChar(int column) {
        if (column < 0 || column >= chars.size()) {
            return;
        }
        chars.remove(column);
    }

    /**
     * Returns display text without line wrapping. May include ANSI codes.
     */
    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (CharacterCell cell : chars) {
            sb.append(cell.getDisplayText());
        }
        return sb.toString();
    }

    /**
     * Returns the display text for this line, which may include ANSI codes and text wrapping. 
     */
    public String getDisplayText(int maxWidth) {
        StringBuilder sb = new StringBuilder();
        int lineWidth = 0;
        for (CharacterCell cell : chars) {
            String cellText = cell.getDisplayText();
            if (lineWidth + cellText.length() > maxWidth) {
                sb.append("\n");
                lineWidth = 0;
            }
            sb.append(cellText);

            lineWidth += 1; // ANSI codes don't take up width
        }
        return sb.toString();
    }

    /**
     * @return length of line excluding ANSI codes.
     */
    public int length() {
        return chars.size();
    }
}
