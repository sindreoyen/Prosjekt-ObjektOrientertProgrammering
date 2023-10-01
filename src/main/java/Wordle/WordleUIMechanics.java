package Wordle;


import java.io.FileNotFoundException;

import Wordle.Filehandling.WordleStats;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class WordleUIMechanics {

    /* Main gameplay mechanics */
    private WordleStats stats = new WordleStats();

    /* Misc. mechanics */
    // Get keyboard letter background colors
    public static Background getLetterBackground(int key) {
        Color c;
        if (key >= 0) c = new Color(0.18, 0.68, 0.38, 1.0); // rgba(39, 174, 96,1.0)
        else if (key == -1) c = new Color(0.5, 0.5, 0.5, 1.0);
        else c = new Color(0.85, 0.647, 0, 1.0);

        BackgroundFill bf = new BackgroundFill(c, new CornerRadii(3), Insets.EMPTY);
        return new Background(bf);
    }

    // Get label text for stats
    public String getGamesWonText() {
        try { stats.fetchData(); }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        int gw = stats.getGamesWon();
        int tg = stats.getTotalGames();
        return "Games won: " + gw + " / " + tg;
    }
    public String getWinPercentText() {
        return "Win percentage: " + stats.getWinPercent() + "%";
    }
    
    // Display alerts

    public static void displayAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
