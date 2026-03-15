package org.jayly;

import java.util.HashMap;

/**
 * Represent a character cell in a terminal buffer
 */
public class CharacterCell {
    private HashMap<StyleFlag, Boolean> styles;
    private char character;
    private String foregroundColor;
    private String backgroundColor;

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

    public String getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(String foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Returns the display text for this character cell, which may include ANSI escape codes for styles and colors.
     */
    public String getDisplayText() {
        StringBuilder sb = new StringBuilder();
        if (styles.get(StyleFlag.BOLD)) {
            sb.append(ANSIColor.BOLD);
        }
        if (styles.get(StyleFlag.ITALIC)) {
            sb.append(ANSIColor.ITALIC);
        }
        if (styles.get(StyleFlag.UNDERLINE)) {
            sb.append(ANSIColor.UNDERLINE);
        }
        if (foregroundColor != null) {
            sb.append(foregroundColor);
        }
        if (backgroundColor != null) {
            sb.append(backgroundColor);
        }
        sb.append(character);
        sb.append(ANSIColor.RESET);
        return sb.toString();
    }
}
