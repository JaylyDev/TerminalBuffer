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

        // TODO: Find a better way to serialize document content please, this is really bad.
        final String expectedText = "H\u001B[0me\u001B[0ml\u001B[0ml\u001B[0mo\u001B[0m,\u001B[0m \u001B[0mW\u001B[0mo\u001B[0mr\u001B[0ml\u001B[0md\u001B[0m!\u001B[0m\n";
        assertEquals(expectedText, document.getContent());
    }
}