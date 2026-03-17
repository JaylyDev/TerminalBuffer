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
        editor.insertText(text);

        assertTrue(document.getContent().contains(text));
    }

    @Test
    void scrollUpScreenGrid() {
        String[] lines = {
                "Line 1",
                "Line 2",
                "Line 3",
        };
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(20, 2, 100, document);
        Editor editor = buffer.getEditor();
        for (String line : lines) {
            editor.insertText(line);
            editor.insertEmptyLine();
        }

        editor.scrollUpScreenGrid(1);

        // We inserted a new line in line 4, so scroll up should say "line 2" and "line 3"
        assertTrue(editor.getLine(0).contains("Line 2"));
        assertTrue(editor.getLine(1).contains("Line 3"));
    }

    @Test
    void scrollDownScreenGrid() {
        String[] lines = {
                "Line 1",
                "Line 2",
                "Line 3",
        };
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(20, 2, 100, document);
        Editor editor = buffer.getEditor();
        for (String line : lines) {
            editor.insertText(line);
            editor.insertEmptyLine();
        }

        editor.scrollUpScreenGrid(1);
        editor.scrollDownScreenGrid(1);

        assertTrue(editor.getLine(0).contains("Line 3"));
    }

    @Test
    void deleteCharacter() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(20, 2, 100, document);
        Editor editor = buffer.getEditor();

        editor.insertText("Test");
        editor.deleteCharacter();
        assertTrue(editor.getLine(0).contains("Tes"));
    }

    @Test
    void insertCharacter() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(20, 2, 100, document);
        Editor editor = buffer.getEditor();

        editor.insertCharacter('a');
        assertTrue(editor.getLine(0).contains("a"));
    }

    @Test
    void overrideCharacter() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(20, 2, 100, document);
        Editor editor = buffer.getEditor();
        Cursor cursor = buffer.getCursor();

        editor.insertText("Test");
        cursor.moveTo(0, 0);
        editor.overrideCharacter('a');
        assertTrue(editor.getLine(0).contains("aest"));

    }

    @Test
    void overrideText() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(20, 2, 100, document);
        Editor editor = buffer.getEditor();
        Cursor cursor = buffer.getCursor();

        editor.insertText("Testing");
        cursor.moveTo(0, 0);
        editor.overrideText("Work");
        assertTrue(editor.getLine(0).contains("Working"));
    }

    @Test
    void insertEmptyLine() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(20, 5, 100, document);
        Editor editor = buffer.getEditor();

        editor.insertText("Line 1");

        assertEquals(4, editor.getLine(1).length());  // Ensure line 2 on screen only has ANSI characters

        editor.insertEmptyLine();
        editor.insertText("Hello");

        assertTrue(editor.getLine(0).contains("Line 1"));
        assertTrue(editor.getLine(1).contains("Hello"), "The word 'Hello' should be in line 2");
    }

    @Test
    void removeLine() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(20, 5, 100, document);
        Editor editor = buffer.getEditor();
        Cursor cursor = buffer.getCursor();

        editor.insertText("Line 1");
        editor.insertEmptyLine();
        editor.insertText("Line 2");

        cursor.moveTo(0, 0);
        editor.removeLine();

        assertTrue(editor.getLine(0).contains("Line 2"));
    }

    @Test
    void getCharacterAt() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(2, 2, 2, document);
        Editor editor = buffer.getEditor();

         Character character = 'a';
         editor.insertCharacter(character);
         CharacterCell characterCell = editor.getAttributesAt(0, 0);
         assertEquals(character, characterCell.getCharacter(), "Failed to get the correct character");
    }

    @Test
    void getAttributesAt() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(2, 2, 2, document);
        Editor editor = buffer.getEditor();

        buffer.setForegroundColor(ANSIColor.YELLOW);
        buffer.setBackgroundColor(ANSIColor.CYAN_BACKGROUND);
        editor.insertCharacter('a');
        CharacterCell characterCell = editor.getAttributesAt(0, 0);
        assertEquals(ANSIColor.YELLOW, characterCell.getForegroundColor(), "Failed to get the correct foreground color");
        assertEquals(ANSIColor.CYAN_BACKGROUND, characterCell.getBackgroundColor(), "Failed to get the correct background color");
    }

    @Test
    void getLine() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(20, 2, 100, document);
        Editor editor = buffer.getEditor();

        editor.insertText("Line 1");
        editor.insertEmptyLine();
        editor.insertText("Line 2");

        assertTrue(editor.getLine(0).contains("Line 1"));
        assertTrue(editor.getLine(1).contains("Line 2"));
    }

    @Test
    void getScreenContent() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(20, 2, 100, document);
        Editor editor = buffer.getEditor();

        editor.insertText("Line 1");
        editor.insertEmptyLine();
        editor.insertText("Line 2");

        String screenContent = editor.getScreenContent();
        assertTrue(screenContent.contains("Line 1"));
        assertTrue(screenContent.contains("Line 2"));
    }

    @Test
    void getScrollbackContent() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(20, 2, 100, document);
        Editor editor = buffer.getEditor();

        editor.insertText("Line 1");
        editor.insertEmptyLine();
        editor.insertText("Line 2");

        String scrollbackContent = editor.getScrollbackContent();
        assertTrue(scrollbackContent.isEmpty(), "Scrollback should be empty when no lines have been scrolled");

        editor.insertEmptyLine();
        editor.insertText("Line 3");

        scrollbackContent = editor.getScrollbackContent();
        assertTrue(scrollbackContent.contains("Line 1"), "Scrollback should contain Line 1 after scrolling");
    }

    @Test
    void clearScreen() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(20, 2, 100, document);
        Editor editor = buffer.getEditor();

        for (int i = 1; i <= 10; i++) {
            editor.insertText("Line " + i);
            editor.insertEmptyLine();
        }

        editor.clearScreen();
        String scrollbackContent = editor.getScrollbackContent();
        String[] lines = scrollbackContent.split("\n");
        assertTrue(lines.length >= 9, "Scrollback should contain all 9 lines after clearing the screen");
    }

    @Test
    void clearScrollback() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(20, 2, 100, document);
        Editor editor = buffer.getEditor();

        editor.insertText("Line 1");
        editor.insertEmptyLine();
        editor.insertText("Line 2");

        editor.clearScreen(true);

        String scrollbackContent = editor.getScrollbackContent();
        assertTrue(scrollbackContent.isEmpty(), "Scrollback content should be empty after clearing the scrollback");
    }

    @Test
    void getCharacterFromScrollback() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(20, 2, 100, document);
        Editor editor = buffer.getEditor();
        editor.insertText("Line 1");
        editor.insertEmptyLine();
        editor.insertText("Line 2");
        editor.insertEmptyLine();
        editor.insertText("Line 3");

        Character scrollbackContent = editor.getCharacterFromScrollback(0, 5);
        assertEquals('1', scrollbackContent, "Failed to get the correct character from scrollback");
    }

    @Test
    void getLineFromScrollback() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(20, 2, 100, document);
        Editor editor = buffer.getEditor();
        editor.insertText("Line 1");
        editor.insertEmptyLine();
        editor.insertText("Line 2");
        editor.insertEmptyLine();
        editor.insertText("Line 3");

        String scrollbackContent = editor.getLineFromScrollback(0);
        assertTrue(scrollbackContent.contains("Line 1"), "Failed to get the correct line from scrollback");
    }

    @Test
    void getScreenAndScrollbackText() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(20, 2, 100, document);
        Editor editor = buffer.getEditor();

        String[] lines = {
                "Line 1",
                "Line 2",
                "Line 3",
        };
        for (String line : lines) {
            editor.insertText(line);
            editor.insertEmptyLine();
        }

        final String screenAndScrollbackText = editor.getScreenAndScrollbackText();
        for (String line : lines) {
            assertTrue(screenAndScrollbackText.contains(line), "Failed to get the '" + line + "' in the screen and scrollback text");
        }
    }

    @Test
    void fillLine() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(20, 2, 100, document);
        Editor editor = buffer.getEditor();

        editor.insertText("Test");

        CharacterCell characterCell = new CharacterCell();
        characterCell.setCharacter('T');
        characterCell.setForegroundColor(ANSIColor.GREEN);

        editor.fillLine(characterCell);
        String lineContent = editor.getLine(0);
        assertTrue(lineContent.contains("T".repeat(20)), "Failed to fill the line");
    }
}