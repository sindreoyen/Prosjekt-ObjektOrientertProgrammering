package Wordle.Filehandling;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class WordleDataReader implements WordleFileReader {
    
    // Attributes
    private final String answersFileName = "WordleAnswers.txt";
    private final String nonAnswersFileName = "WordleNonAnswers.txt";

    private static List<String> answerWords = new ArrayList<>();
    private static List<String> allWords = new ArrayList<>();

    /* 
    Structure: 
    two lists, one with all possible guesses (incl. answers) and one with only possible answers 
    */

    // Constructor
    public WordleDataReader() {
        answerWords = getAnswerWordleWords();
        allWords = getAllWordleWords();
    }

    // Implementation
    @Override
    public List<String> fetchData(String filepath) throws FileNotFoundException {
        List<String> l = new ArrayList<>();
        // Opening dict file with answer words
        File wordsFile = new File(filepath);
        Scanner reader = new Scanner(wordsFile);
        while (reader.hasNextLine()) {
            // One word for each line
            String data = reader.nextLine();
            l.add(data);
        }
        reader.close();
        return l;
    }

    // Getters
    public List<String> getAllWordleWords() {
        // If the words havent yet been read from file, fetch words
        if (allWords.size() == 0) {
            try {
                allWords = fetchData(WordleFileReader.getFilePath(nonAnswersFileName));
            }
            catch (FileNotFoundException e) {
                handleFileException(e);
            }
            for (String s : getAnswerWordleWords())
                allWords.add(s);
        }
        return new ArrayList<>(allWords);
    }

    
    public List<String> getAnswerWordleWords() {
        // If the words havent yet been read from file, fetch words
        if (answerWords.size() == 0) {
            try {
                answerWords = fetchData(WordleFileReader.getFilePath(answersFileName));
            }
            catch (FileNotFoundException e) {
                handleFileException(e);
            }
        }
        return new ArrayList<>(answerWords);
    }

    private void handleFileException(FileNotFoundException e) {
        System.out.println("An error occured reading a file - error stack:");
        e.printStackTrace();
    }

}
