package Wordle;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class WordleController {

    // General attributes
    private WordleGameData gameDataHandler;
    private final WordleUIMechanics uiMechs = new WordleUIMechanics();

    // FXML Def
    @FXML
    private Pane bgPane;
    @FXML
    private GridPane wordleGridPane;
    
    @FXML
    private Label gamesWonLbl, winPercentLbl;

    @FXML
    private Button 
    qBtn, wBtn, eBtn, rBtn, tBtn, yBtn, uBtn, iBtn, oBtn, pBtn,
    aBtn, sBtn, dBtn, fBtn, gBtn, hBtn, jBtn, kBtn, lBtn,
    zBtn, xBtn, cBtn, vBtn, bBtn, nBtn, mBtn;

    private List<Button> letterButtons = new ArrayList<>();
    private void updateLtrBtnList() {
        if (letterButtons.size() != 0) return;
        Button[] lb = {qBtn, wBtn, eBtn, rBtn, tBtn, yBtn, uBtn, iBtn, oBtn, pBtn,
            aBtn, sBtn, dBtn, fBtn, gBtn, hBtn, jBtn, kBtn, lBtn,
            zBtn, xBtn, cBtn, vBtn, bBtn, nBtn, mBtn};
        
        for (Button btn : lb) {
            letterButtons.add(btn);
            btn.setOnMouseClicked(event -> { 
                String id = btn.getId(); // ie. qBtn
                displayCharInput(id.substring(0, 1));
            });
        }
    }
    private final String keyLetters = "qwertyuiopasdfghjklzxcvbnm"; // for indexing buttons in letterButtons list
    
    // Updating ui panes
    private List<StackPane> guessesPanes = new ArrayList<>(); // Making panes in the wordleGridPane more easily accessible
    private void addGuessPanes() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                StackPane p = new StackPane();
                p.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3, 3, 3, 3))));
                
                wordleGridPane.add(p, j, i);
                guessesPanes.add(p);
            }
        }
    }

    // Initialize controller functions on start
    @FXML
    public void initialize() {
        addGuessPanes();
        gameDataHandler = new WordleGameData();
        updateLtrBtnList();
        updateStatsText();
    }

    private void updateStatsText() {
        gamesWonLbl.setText(uiMechs.getGamesWonText());
        winPercentLbl.setText(uiMechs.getWinPercentText());
    }

    // CONTROL:- UI changing based on game control
    private void displayCharInput(String letter) {
        if (gameDataHandler.getGuesses().size() >= 6 || !gameDataHandler.getGameActive()) return;
        // Letter typed in, displaying in guess grid
        StackPane pane = guessesPanes.get(gameDataHandler.getCurrIndex());

        Text t = new Text(letter);
        t.setStyle("-fx-font: 58 arial;");
        pane.getChildren().add(t);
        StackPane.setAlignment(t, Pos.CENTER);

        gameDataHandler.letterPressed(letter);
    }

    private void removeChar() {
        // Method called when backspace is pressed
        guessesPanes.get(gameDataHandler.getCurrIndex()).getChildren().remove(0);
    }

    @FXML
    private void hardwareKeyboard(KeyEvent k) {
        if (!gameDataHandler.getGameActive()) return;
        KeyCode kCode = k.getCode();
        if (k.getText().matches("[A-Za-z]") && gameDataHandler.getTypingIndex() < 5) {// only english letters, and has to be full word
            displayCharInput(k.getText().toLowerCase());
        }
        else if (kCode.equals(KeyCode.ENTER)) {
            enterPressed();
        }
        else if (kCode.equals(KeyCode.BACK_SPACE)) {
            backSpacePressed();
        }
    }
    @FXML
    private void enterPressed() {
        String w = gameDataHandler.getCurrGuess();
        try {
            if (gameDataHandler.enterPressed().equals(w)) guessLockedIn(w);
        }
        catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    private void backSpacePressed() {
        try {
            gameDataHandler.backspacePressed();
            removeChar();
        }
        catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    // Reset ui for new word
    @FXML
    private void startWithNewWord() {
        // Restarting to new game if pressed "new word btn"
        gameDataHandler.setNewWord();
        for (Pane p : guessesPanes) {
            if (p.getChildren().size() != 0) p.getChildren().remove(0);
            p.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }

        updateLtrBtnList();
        for (Button b : letterButtons) {
            b.setBackground(new Background(new BackgroundFill(new Color(0.85, 0.85, 0.85, 1), new CornerRadii(3), Insets.EMPTY)));
        }
        System.out.println("Current playing word: " + gameDataHandler.getCurrWord());
    }

    // Update letters to give feedback on guess
    private void guessLockedIn(String guess) {
        String currGuess = gameDataHandler.getCurrGuess();
        List<Character> corrCharGuess = gameDataHandler.getCorrectCharGuess();
        // update colors in keyboard
        for (char c : currGuess.toCharArray()) {
            if (corrCharGuess.contains(c)) continue;
            int bestGuess = gameDataHandler.bestMatchForChar(c);
            updateKeyBoardColor(c, bestGuess);
        }
        // update colors in grid
        List<Integer> colorCodes = gameDataHandler.getCurrWordle().checkGuessColorMatch(currGuess);
        int it = 0;
        for (int i : colorCodes) {
            StackPane p = guessesPanes.get(gameDataHandler.getGuesses().size() * 5 + it);
            p.setBackground(WordleUIMechanics.getLetterBackground(i));
            it++;
        }

        // delegating the handling of the guess to the gameDataHandler
        gameDataHandler.addGuess();
        if (gameDataHandler.getHasWon())
            WordleUIMechanics.displayAlert(AlertType.INFORMATION, "Lessgo", "Seier!", 
            "Gratulerer med dagen, du gjettet riktig ord :)");
        updateStatsText();
    }

    private void updateKeyBoardColor(char c, int guessCode) {
        updateLtrBtnList(); // will only run once, adds keyboard btns to the arraylist
        // Guesscode >= 0 = green, -1 = gray, -2 = orange
        Button b = letterButtons.get(keyLetters.indexOf(c));
        b.setBackground(WordleUIMechanics.getLetterBackground(guessCode)); // calling static method from mechanics class
    }
}