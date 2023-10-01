package Wordle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import Wordle.Filehandling.WordleDataReader;
import Wordle.Filehandling.WordleStats;
import javafx.scene.control.Alert.AlertType;


public class WordleGameData {
    
    // Delegation attributes
    private final WordleStats stats = new WordleStats();
    private final WordleDataReader dataReader = new WordleDataReader();

    private WordleWord currWordle;

    // Attributes
    private boolean gameActive = true;
    private boolean hasWon = false;

    private int currentIndex = 0; // index of total letters wrote
    
    private List<String> guesses; // guesses (words) that have been locked in
    private List<String> lettersWrote; // every letter written
    private List<Character> correctCharGuess; // correct characters guessed (green)

    // Constructor
    public WordleGameData() {
        setNewWord();
    }

    // Getters
    public String getCurrWord() { return currWordle.getWord(); }
    public WordleWord getCurrWordle() { return currWordle; }
    public int getTypingIndex() { return currentIndex - guesses.size() * 5; } // typing index on current guess
    public int getCurrIndex() { return currentIndex; }
    public boolean getGameActive() { return gameActive; }
    public boolean getHasWon() { return hasWon; }
    public List<String> getGuesses() { return new ArrayList<>(guesses); } // only returning a copy bc shouldn't be able to alter the list
    public List<Character> getCorrectCharGuess() { return new ArrayList<>(correctCharGuess); }

    public String getCurrGuess() {
        // returns current word typed in
        String s = "";
        for (int i = guesses.size() * 5; i < currentIndex; i++) {
            s += lettersWrote.get(i);
        }
        return s.toLowerCase();
    }
    
    // Setters
    public void setNewWord() {
        String w = getRandomWord();
        if (!isLegalGuess(w)) // checking if word is part of dataset
            throw new IllegalStateException("Startword not valid!");
        gameActive = true;
        hasWon = false;
        if (currWordle == null) currWordle = new WordleWord(w);
        else currWordle.setWordleWord(w);
        System.out.println("New word: " + getCurrWord());
        
        lettersWrote = new ArrayList<>();
        correctCharGuess = new ArrayList<>();
        guesses = new ArrayList<>();

        currentIndex = 0;
    }
    void addGuess() {
        if (getCurrGuess().length() != 5) 
            throw new IllegalArgumentException("Cannot add a guess with length != 5");

        guesses.add(getCurrGuess());
        if (guesses.get(guesses.size() - 1).equals(getCurrWord())) { // if correct guess
            gameActive = false;
            hasWon = true;
            // Spill vunnet -> lagre
            try { stats.updateSavedStats(true); } 
            catch (IOException e) { System.out.println(e.getMessage()); }
            return;
        }
        if (guesses.size() == 6) { // hvis tapt
            gameActive = false;
            WordleUIMechanics.displayAlert(AlertType.INFORMATION, "Spill tapt!", "Ord ikke gjettet", 
            "Du har brukt opp alle forsøkene på å gjette! Trykk på 'New word' for nytt spill.");
            // Spill tapt -> lagre
            try { stats.updateSavedStats(false); } 
            catch (IOException e) { System.out.println(e.getMessage()); }
        }
    }

    // Methods:- Mechanics
    int bestMatchForChar(char c) {
        if (correctCharGuess.contains(c)) return 1;

        int best = currWordle.bestMatchForChar(c, getCurrGuess());
        if (best >= 0) correctCharGuess.add(c);

        return best;
    }

    public String getRandomWord() {
        int max = dataReader.getAnswerWordleWords().size();
        int randomIndex = ThreadLocalRandom.current().nextInt(0, max);

        return dataReader.getAnswerWordleWords().get(randomIndex);
    }


    public boolean isLegalGuess(String word) {
        return dataReader.getAllWordleWords().contains(word);
    }

    // Methods:- Key pressed handling
    void letterPressed(String letter) {
        if (letter.length() != 1) 
            throw new IllegalArgumentException("Can only write one letter at a time");
        if (getTypingIndex() >= 5)
            throw new IllegalStateException("Cannot write more than 5 characters");
        currentIndex++;
        lettersWrote.add(letter);
    }
    String enterPressed() {
        if (getCurrGuess().length() != 5) 
            throw new IllegalStateException("Guess must be 5 characters"); // not enough letters
            
        if (isLegalGuess(getCurrGuess())) return getCurrGuess();
            
        // Error feedback, guess is not a word in the dictionary
        WordleUIMechanics.displayAlert(AlertType.ERROR, "Feil ord", "Gjett et nytt ord", 
        "Ordet du gjettet finnes ikke i spillets ordliste. Obs: kun engelske ord.");
        return "-1";
    }
    void backspacePressed() {
        if (getTypingIndex() <= 0)
            throw new IllegalStateException("No characters to remove");
        currentIndex--;
        lettersWrote.remove(lettersWrote.size() - 1);        
    }
}
