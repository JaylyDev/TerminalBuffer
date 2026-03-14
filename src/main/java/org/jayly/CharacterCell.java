package org.jayly;

import java.util.HashMap;

/**
 * Represent a character cell in a terminal buffer
 */
public class CharacterCell {
    private HashMap<StyleFlag, Boolean> styles;
    private char character;
    private int foregroundColor;
    private int backgroundColor;

    public CharacterCell() {
        this.styles = new HashMap<>();
        this.styles.put(StyleFlag.BOLD, false);
        this.styles.put(StyleFlag.ITALIC, false);
        this.styles.put(StyleFlag.UNDERLINE, false);
    }

    public HashMap<StyleFlag, Boolean> getStyles() {
        return styles;
    }

    public void setStyle(StyleFlag style, boolean enabled) {
        this.styles.put(style, enabled);
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
