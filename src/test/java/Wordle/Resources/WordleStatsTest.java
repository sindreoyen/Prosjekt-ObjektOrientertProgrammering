package Wordle.Resources;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Wordle.Filehandling.WordleStats;

public class WordleStatsTest {
    
    private static WordleStats stats;

    @BeforeAll
    public static void setUp() {
        stats = new WordleStats();
    }

    @Test
    @DisplayName("Testing fetching and updating stats: game won")
    public void testGameWon() {
        int gw = stats.getGamesWon();
        int tg = stats.getTotalGames();

        assertDoesNotThrow(() -> {
            stats.updateSavedStats(true);
        });
        assertDoesNotThrow(() -> {
            stats.fetchData();
        });
        
        assertEquals(gw + 1, stats.getGamesWon());
        assertEquals(tg + 1, stats.getTotalGames());
    }

    @Test
    @DisplayName("Testing fetching and updating stats: game lost")
    public void testGameLost() {
        int gw = stats.getGamesWon();
        int tg = stats.getTotalGames();

        assertDoesNotThrow(() -> {
            stats.updateSavedStats(false);
        });
        assertDoesNotThrow(() -> {
            stats.fetchData();
        });
        
        assertEquals(gw, stats.getGamesWon());
        assertEquals(tg + 1, stats.getTotalGames());
    }

}
