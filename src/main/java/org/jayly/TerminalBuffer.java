package org.jayly;

import java.util.HashMap;

/**
 * Core data structure that terminal emulators use to store and manipulate displayed text.
 * When a shell sends output, terminal emulator updates buffer and UI renders it.
 */
public class TerminalBuffer {
    private Integer width;
    private Integer height;
    /**
     * Represents configurable max number of lines
     */
    private Integer scrollbackMaxSize;
    private String foregroundColor;
    private String backgroundColor;
    /**
     * Represents styles enabled for the editor. The styles are applied once a new character is inserted.
     * <p>
     * This styles array is changed once cursor is set to a different location.
     */
    private HashMap<StyleFlag, Boolean> styles;

    public TerminalBuffer(Integer width, Integer height, Integer scrollbackMaxSize) {
        this.width = width;
        this.height = height;
        this.scrollbackMaxSize = scrollbackMaxSize;

        // Default styles
        this.styles = new HashMap<>();
        this.styles.put(StyleFlag.BOLD, false);
        this.styles.put(StyleFlag.ITALIC, false);
        this.styles.put(StyleFlag.UNDERLINE, false);
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
    public void setHeight(Integer height) {
        this.height = height;
    }
    public void setScrollbackMaxSize(Integer scrollbackMaxSize) {
        this.scrollbackMaxSize = scrollbackMaxSize;
    }
    public void setForegroundColor(String foregroundColor) {
        this.foregroundColor = foregroundColor;
    }
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    public void setStyle(StyleFlag style, boolean enabled) {
        this.styles.put(style, enabled);
    }

    /**
     * Get character at position from screen and scrollback.
     */
    public String getCharacterAt(Integer row, Integer column) {
        return null;
    }

    /**
     * Get attributes at position from screen and scrollback.
     */
    public String getAttributesAt(Integer row, Integer column) {
        return null;
    }
    /**
     * Get line at position from screen and scrollback.
     * @param row The row number of the screen or scrollback, not the line number of the file.
     */
    public String getLine(Integer row) {
        return null;
    }

    public String getScreenContent() {
        return null;
    }


}
