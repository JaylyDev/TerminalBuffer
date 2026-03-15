package org.jayly;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditorTest {

    @Test
    void insertText() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 1000, document);
        Editor editor = buffer.getEditor();

        final String text = "Hello, World!";
        editor.insertText(0, 0, text);

        assertTrue(document.getContent().contains(text));
    }
}