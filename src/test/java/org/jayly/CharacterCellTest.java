package org.jayly;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterCellTest {
    @Test
    void getStyles() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(2, 2, 2, document);
        Editor editor = buffer.getEditor();

        editor.insertCharacter('a');
        CharacterCell characterCell = editor.getAttributesAt(0, 0);
        assertFalse(characterCell.getStyles().get(StyleFlag.BOLD), "Text should not be bold by default");
        assertFalse(characterCell.getStyles().get(StyleFlag.UNDERLINE), "Text should not be underlined by default");
        assertFalse(characterCell.getStyles().get(StyleFlag.ITALIC), "Text should not be italic by default");
    }

    @Test
    void setStyle() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(2, 2, 2, document);
        Editor editor = buffer.getEditor();
        buffer.setStyle(StyleFlag.BOLD, true);

        editor.insertCharacter('a');
        CharacterCell characterCell = editor.getAttributesAt(0, 0);
        assertTrue(characterCell.getStyles().get(StyleFlag.BOLD), "Failed to set text bold");
    }

    @Test
    void getCharacter() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(2, 2, 2, document);
        Editor editor = buffer.getEditor();

        Character character = 'a';
        editor.insertCharacter(character);
        CharacterCell characterCell = editor.getAttributesAt(0, 0);
        assertEquals(character, characterCell.getCharacter(), "Failed to get the correct character");
    }

    @Test
    void setCharacter() {
        Document document = new Document();
            TerminalBuffer buffer = new TerminalBuffer(2, 2, 2, document);
            Editor editor = buffer.getEditor();
    
            Character character = 'a';
            editor.insertCharacter(character);
            CharacterCell characterCell = editor.getAttributesAt(0, 0);
    
            Character newCharacter = 'b';
            characterCell.setCharacter(newCharacter);
            assertEquals(newCharacter, characterCell.getCharacter(), "Failed to set the correct character");
    }

    @Test
    void getForegroundColor() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(2, 2, 2, document);
        Editor editor = buffer.getEditor();

        buffer.setForegroundColor(ANSIColor.YELLOW);
        editor.insertCharacter('a');
        CharacterCell characterCell = editor.getAttributesAt(0, 0);
        assertEquals(ANSIColor.YELLOW, characterCell.getForegroundColor(), "Failed to get the correct foreground color");
    }

    @Test
    void setForegroundColor() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(2, 2, 2, document);
        Editor editor = buffer.getEditor();

        buffer.setForegroundColor(ANSIColor.YELLOW);
        editor.insertCharacter('a');
        CharacterCell characterCell = editor.getAttributesAt(0, 0);
        characterCell.setForegroundColor(ANSIColor.RED);

        assertEquals(ANSIColor.RED, characterCell.getForegroundColor(), "Failed to set the correct foreground color");
    }

    @Test
    void getBackgroundColor() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(2, 2, 2, document);
        Editor editor = buffer.getEditor();

        buffer.setBackgroundColor(ANSIColor.CYAN_BACKGROUND);
        editor.insertCharacter('a');
        CharacterCell characterCell = editor.getAttributesAt(0, 0);
        assertEquals(ANSIColor.CYAN_BACKGROUND, characterCell.getBackgroundColor(), "Failed to get the correct background color");
    }

    @Test
    void setBackgroundColor() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(2, 2, 2, document);
        Editor editor = buffer.getEditor();

        buffer.setBackgroundColor(ANSIColor.CYAN_BACKGROUND);
        editor.insertCharacter('a');
        CharacterCell characterCell = editor.getAttributesAt(0, 0);

        characterCell.setBackgroundColor(ANSIColor.BLACK_BACKGROUND);
        assertEquals(ANSIColor.BLACK_BACKGROUND, characterCell.getBackgroundColor(), "Failed to set the correct background color");
    }

    @Test
    void isStyleEquals() {
        CharacterCell cell1 = new CharacterCell();
        cell1.setStyle(StyleFlag.BOLD, true);
        cell1.setForegroundColor(ANSIColor.YELLOW);
        cell1.setBackgroundColor(ANSIColor.CYAN_BACKGROUND);

        CharacterCell cell2 = new CharacterCell();
        cell2.setStyle(StyleFlag.BOLD, true);
        cell2.setForegroundColor(ANSIColor.YELLOW);
        cell2.setBackgroundColor(ANSIColor.CYAN_BACKGROUND);
        
        assertTrue(cell1.isStyleEquals(cell2), "Styles should be same for cell1 and cell2");
    }

    @Test
    void getDisplayText() {
        Document document = new Document();
        TerminalBuffer buffer = new TerminalBuffer(2, 2, 2, document);
        Editor editor = buffer.getEditor();

        buffer.setBackgroundColor(ANSIColor.CYAN_BACKGROUND);
        buffer.setForegroundColor(ANSIColor.YELLOW);
        buffer.setStyle(StyleFlag.BOLD, true);
        buffer.setStyle(StyleFlag.ITALIC, true);
        buffer.setStyle(StyleFlag.UNDERLINE, true);

        Character character = 'b';
        editor.insertCharacter(character);

        CharacterCell characterCell = editor.getAttributesAt(0, 0);
        String displayText = characterCell.getDisplayText();

        assertTrue(displayText.contains(character.toString()), "Failed to find character " + character.toString());
        assertTrue(displayText.contains(ANSIColor.CYAN_BACKGROUND), "Failed to set cyan background");
        assertTrue(displayText.contains(ANSIColor.YELLOW), "Failed to set yellow foreground");
        assertTrue(displayText.contains(ANSIColor.BOLD), "Failed to set text bold");
        assertTrue(displayText.contains(ANSIColor.ITALIC), "Failed to set text italic");
        assertTrue(displayText.contains(ANSIColor.UNDERLINE), "Failed to underline text");
    }
}
