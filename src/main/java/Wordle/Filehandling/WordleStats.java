package Wordle.Filehandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class WordleStats implements WordleFileSaver {

    public final String filepath = WordleFileReader.getFilePath("WordleSaveData.txt");
    
    // Saved stats attributes
    private int gamesWon;
    private int totalGames;

    // Constructor 
    public WordleStats() {
        try {
            fetchData();
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            gamesWon = -1;
            totalGames = -1;
            try {
                updateSavedStats(true);
                System.out.println("Generated save data file");
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Error generating save data file");
                gamesWon = 0;
                totalGames = 0;
            }
        }
    }

    // Getters
    public int getWinPercent() { 
        if (totalGames == 0) return 0;
        return (gamesWon * 100) / totalGames; 
    }
    public int getGamesWon() { return gamesWon; }
    public int getTotalGames() { return totalGames; }

    // Implementation
    @Override
    public void fetchData() throws FileNotFoundException {
        File wordsFile = new File(filepath);
        Scanner reader = new Scanner(wordsFile);
        int iteration = 0;
        while (reader.hasNextLine()) {
            // First line is games won, second line is total games
            String data = reader.nextLine();
            Integer dataInt = -1;
            try {
                dataInt = Integer.valueOf(data);
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println("Error reading data, corrupt save file. Generating new save.");
                totalGames = -1;
                gamesWon = -1;
                try { updateSavedStats(true); }
                catch (IOException ioe) { e.printStackTrace();}
            }
            if (iteration == 0) gamesWon = dataInt;
            else {
                totalGames = dataInt;
                break;
            }
            iteration++;
        }
        reader.close();
    }

    @Override
    public void updateSavedStats(boolean didWin) throws IOException {
        totalGames++;
        if (didWin) gamesWon++;
        
        FileWriter writer = new FileWriter(filepath);
        
        writer.write(gamesWon + "\n" + totalGames);
        writer.close();
    }
}
