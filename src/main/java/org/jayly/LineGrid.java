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
        CharacterCell prevCell = null;
        for (int i = 0; i < chars.size(); i++) {
            CharacterCell cell = chars.get(i);
            if (prevCell == null || !cell.isStyleEquals(prevCell)) {
                sb.append(cell.getDisplayText());
            } else {
                sb.append(cell.getCharacter());
            }
            prevCell = cell;
        }
        return sb.toString() + ANSIColor.RESET;
    }

    /**
     * Returns the display text for this line, which may include ANSI codes and text wrapping.
     */
    public String getDisplayText(int maxWidth) {
        StringBuilder sb = new StringBuilder();
        int lineWidth = 0;
        CharacterCell prevCell = null;
        for (int i = 0; i < chars.size(); i++) {
            CharacterCell cell = chars.get(i);
            String cellText;
            if (prevCell == null || !cell.isStyleEquals(prevCell)) {
                cellText = cell.getDisplayText();
            } else {
                cellText = String.valueOf(cell.getCharacter());
            }
            if (lineWidth + 1 > maxWidth) {
                sb.append("\n");
                lineWidth = 0;
            }
            sb.append(cellText);

            lineWidth += 1; // ANSI codes don't take up width
            prevCell = cell;
        }
        return sb.toString() + ANSIColor.RESET;
    }

    /**
     * @return length of line excluding ANSI codes.
     */
    public int length() {
        return chars.size();
    }
}
