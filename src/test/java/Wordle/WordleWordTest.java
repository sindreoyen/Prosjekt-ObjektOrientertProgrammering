package Wordle;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



public class WordleWordTest {
    
    private static WordleWord wordleWord;
    private static WordleGameData wData;

    @BeforeAll
    public static void setUp() {
        wordleWord = new WordleWord("crane");
        wData = new WordleGameData();
    }

    @Test
    @DisplayName("Testing WordleWord constructor")
    public void testConstructor() {
        // Testing that constructor only works for full 5 letter word
        String s = wData.getRandomWord();
        for (int i = 1; i < 5; i++) {
            String t = s.substring(0, i);
            assertThrows(IllegalArgumentException.class, () -> {
                new WordleWord(t);
            }, "Should only accept word with len=5 in constructor");
        }
        assertDoesNotThrow(() -> {
            new WordleWord(s);
        });
        // Word longer than 5 letters
        String t = s + "c";
        assertThrows(IllegalArgumentException.class, () -> {
            new WordleWord(t);
        }, "Should only accept word with len=5 in constructor");
    }

    @Test
    @DisplayName("Testing the mapping function to get a list of all the indexes for each char in a word")
    public void testGetCharIndexes() {
        // Testing 10 different random words
        for (int i = 0; i < 10; i++) {
            int n = 0; // keeping track of indexes iterated in map
            String s = wData.getRandomWord();
            Map<Character, List<Integer>> map = wordleWord.getCharIndexes(s);
            for (char c : map.keySet()) {
                for (int j : map.get(c)) {
                    n++;
                    // Checking if each index mapped to char, matches with actual index in the word
                    assertEquals(c, s.charAt(j));
                }
            }
            assertEquals(5, n); // should have checked 5 mapped indexes for each word
        }
    }

    @Test
    @DisplayName("Testing method for getting best match for a given char")
    public void testBestMatchForChar() {
        // Word in game = "crane"
        char c = 'c';
        String[] guesses = {"cxxxc", "xcccc", "xxcxx", "ccccc", "cxxxx"};
        int[] expected = {0, -2, -2, 0, 0};
        int idx = 0;
        for (String s : guesses) {
            assertEquals(expected[idx], wordleWord.bestMatchForChar(c, s));
            idx++;
        }
    }

    @Test
    @DisplayName("Testing method for getting list with color match")
    public void testGetColorMatch() {
        String[] guessWords = {"arose", "crime", "hunts", "money"};
        String[] expected = {
            "[-2, 1, -1, -1, 4]", "[0, 1, -1, -1, 4]",
            "[-1, -1, -2, -1, -1]", "[-1, -1, -2, -2, -1]"
        };
        int i = 0;
        for (String s : guessWords) {
            List<Integer> actual = wordleWord.checkGuessColorMatch(s);
            assertEquals(expected[i], actual.toString());
            i++;
        }
    }

}
