package Wordle.Filehandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;


public interface WordleFileReader {

    // General
    public static String getFilePath(String filename) {
        String sep = File.separator;
        String originPath = System.getProperty("user.dir") + sep;
        String endPath = "src" + sep + "main" + sep + "resources" + sep + "Wordle" + sep + "Filedata" + sep;
        String path = originPath + endPath + filename;
        return path;
    }

    // Read file
    public List<String> fetchData(String filepath) throws FileNotFoundException;

}
