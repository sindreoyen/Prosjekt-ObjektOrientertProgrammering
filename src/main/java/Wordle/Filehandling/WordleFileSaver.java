package Wordle.Filehandling;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface WordleFileSaver {
    
    // Fetch stats
    public void fetchData() throws FileNotFoundException;

    // Update stats
    public void updateSavedStats(boolean didWin) throws IOException;


}
