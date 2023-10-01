package Wordle;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordleWord {
    /*
    Class for storing attributes of every word (indexes of letters etc.)
    and to check match of guess
    */

    // Attributes
    private String word;
    private Map<Character, List<Integer>> charMap = new HashMap<>();

    // Constructor
    public WordleWord(String word) {
        setWordleWord(word);
    }
    public void setWordleWord(String w) {
        if (w.length() != 5)
            throw new IllegalArgumentException("Only 5 letter words!");
        word = w;
        charMap = getCharIndexes(word);
    }

    // Getters
    public String getWord() { return word; }

    // Methods
    public Map<Character, List<Integer>> getCharIndexes(String w) {
        Map<Character, List<Integer>> map = new HashMap<>();
        // Mapping each character in word to each index it is supposed to be in
        int i = 0;
        for (char c : w.toCharArray()) {
            List<Integer> charIndexes = map.get(c) != null ? map.get(c) : new ArrayList<>();
            charIndexes.add(i);
            map.put(c, charIndexes);
            i++;
        }
        return map;
    }

    public int bestMatchForChar(Character guessChar, String guess) {
        // Calculating the best guess for a character, i.e. could be one orange and one green, green should be best
        // i.e. guessed crack for crane, the first 'c' is green, the last 'c' is grey. The 'c' in the keyboard should be green, not grey. 
        if (word.indexOf(guessChar) == -1) return -1;
        List<Integer> charIndexes = getCharIndexes(guess).get(guessChar);
        for (int i : charIndexes) 
            if (guessChar.equals(word.charAt(i))) return i;
        return -2;
    }

    public List<Integer> checkGuessColorMatch(String guess) {
        /*
        i.e. guess "lippy" for answerword "livid"
        -> returns [0, 1, -1, -1, -1] (= green, green, grey, grey, grey)
        */
        List<Integer> l = new ArrayList<>(0);
        for (int i = 0; i < 5; i++) l.add(-5);

        Map<Character, List<Integer>> cIndexes = getCharIndexes(guess);
        for (Character c : cIndexes.keySet()) {
            if (!word.contains(c.toString())) {
                for (int i : cIndexes.get(c)) {
                    l.set(i, -1);
                }
                continue;
            }
            List<Integer> charIndexMatches = new ArrayList<>();
            List<Integer> actualIndex = charMap.get(c);
            for (Integer i : cIndexes.get(c)) {
                // Firstly iterating to see if correct index ...
                if (actualIndex.contains(i)) {
                    charIndexMatches.add(i);
                    l.set(i, i);
                }
            }
            for (Integer i : cIndexes.get(c)) {
                // ... then iterating to see if any correct letters at wrong index
                if (charIndexMatches.size() < actualIndex.size() && !actualIndex.contains(i)) {
                    charIndexMatches.add(-2);
                    l.set(i, -2);
                }
                else if (charIndexMatches.size() == actualIndex.size() && !actualIndex.contains(i)) l.set(i, -1);
            }
        }
        return l;
    }

}
