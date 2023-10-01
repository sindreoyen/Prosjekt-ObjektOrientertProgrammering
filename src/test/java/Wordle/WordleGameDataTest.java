package Wordle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WordleGameDataTest {
    
    // Attribute
    private static WordleGameData wData;
    private static String word;

    // Supply method
    private void writeWord(String w) {
        for (char c : w.toCharArray()) {
            wData.letterPressed(Character.toString(c));
        }
    }

    @BeforeAll
    public static void setUp() {
        wData = new WordleGameData();
        word = wData.getCurrWord();
    }

    // Test gameplay
    @Test
    @DisplayName("Testing gameplay")
    void testGameplay() {
        assertThrows(IllegalStateException.class, () -> {
            wData.enterPressed();
        }, "Enter function should not get called when no guess is made");
        assertThrows(IllegalStateException.class, () -> {
            wData.backspacePressed();
        }, "Cannot press backspace when nothing is written");

        String guess = wData.getRandomWord();
        writeWord(guess);

        assertThrows(IllegalStateException.class, () -> {
            wData.letterPressed("k");
        }, "Should not be able to write more when full word is typed in");
        assertThrows(IllegalArgumentException.class, () -> {
            wData.letterPressed("ke");
        }, "Cannot write two letters at the same time");
        assertEquals(guess, wData.getCurrGuess());
        
        wData.backspacePressed();
        assertEquals(guess.substring(0, guess.length() - 1), wData.getCurrGuess());
        assertThrows(IllegalArgumentException.class, () -> {
            wData.addGuess();
        });
        
        wData.letterPressed(Character.toString(guess.charAt(4)));
        wData.addGuess();
        assertEquals(guess, wData.getGuesses().get(0));
        assertEquals("", wData.getCurrGuess());

        assertTrue(wData.getGameActive());
        writeWord(word); // guessing the actual answer
        wData.addGuess();
        assertTrue(wData.getHasWon()); // has won game
    }

    // Testing that getting a random word will work for many repetitions
    // And testing that it will always be a legal guess
    @Test
    @DisplayName("Test for getting random word from dictionary")
    public void testGetRandomWord() {
        for (int i = 0; i < 100; i++) {
            String w = wData.getRandomWord();
            assertEquals(w.length(), 5);
            assertTrue(wData.isLegalGuess(w));
        }
    }
    
}
