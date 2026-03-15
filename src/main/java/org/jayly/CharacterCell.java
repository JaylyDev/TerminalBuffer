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
     * Checks if this character cell equals to another character cell, based on
     * styles and colors.
     */
    public boolean isStyleEquals(CharacterCell other) {
        final boolean stylesEqual = this.styles.equals(other.styles);
        boolean foregroundEqual = false;
        if (this.foregroundColor == null) {
            foregroundEqual = other.foregroundColor == null;
        } else {
            foregroundEqual = this.foregroundColor.equals(other.foregroundColor);
        }

        boolean backgroundEqual = false;
        if (this.backgroundColor == null) {
            backgroundEqual = other.backgroundColor == null;
        } else {
            backgroundEqual = this.backgroundColor.equals(other.backgroundColor);
        }

        return stylesEqual && foregroundEqual && backgroundEqual;
    }

    /**
     * Returns the display text for this character cell, which may include ANSI
     * escape codes for styles and colors.
     */
    public String getDisplayText() {
        StringBuilder sb = new StringBuilder();
        sb.append(ANSIColor.RESET);
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
        return sb.toString();
    }
}
