package org.jayly;

import java.util.HashMap;

/**
 * Core data structure that terminal emulators use to store and manipulate
 * displayed text.
 * When a shell sends output, terminal emulator updates buffer and UI renders
 * it.
 */
public class TerminalBuffer {
    private int width;
    private int height;
    /**
     * Represents configurable max number of lines
     */
    private int scrollbackMaxSize;
    /**
     * Represent text color to be applied using the ANSIColor enum.
     */
    private String foregroundColor;
    /**
     * Represent background color to be applied using the ANSIColor enum.
     */
    private String backgroundColor;
    /**
     * Represents styles enabled for the editor. The styles are applied once a new
     * character is inserted.
     * <p>
     * This styles array is changed once cursor is set to a different location.
     */
    private HashMap<StyleFlag, Boolean> styles;
    private Document document;
    private Cursor cursor;
    private Editor editor;

    public TerminalBuffer(int width, int height, int scrollbackMaxSize, Document document) {
        this.width = width;
        this.height = height;
        this.scrollbackMaxSize = scrollbackMaxSize;
        this.document = document;
        this.cursor = new Cursor(this);

        // Default styles
        this.styles = new HashMap<>();
        this.styles.put(StyleFlag.BOLD, false);
        this.styles.put(StyleFlag.ITALIC, false);
        this.styles.put(StyleFlag.UNDERLINE, false);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return this.width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return this.height;
    }

    public void setScrollbackMaxSize(int scrollbackMaxSize) {
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

    public Cursor getCursor() {
        return this.cursor;
    }

    public Document getDocument() {
        return this.document;
    }

    public Editor getEditor() {
        return this.editor;
    }
}
