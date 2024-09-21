/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package spellingBee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * Scoreboard object that keeps track of user's points, currently found words, and score label
 */
public class Scoreboard {

	// label and scoring logic:
    private static final String[] LABELS = {"Beginner", "Good Start", "Moving Up", "Good", "Solid", "Nice", "Great", "Amazing", "Genius"};
    private static final int MAX_SCORE = 300; // Set the max score at 300 -- not dynamic (unlike NYT)
    private static final int[] SCORE_THRESHOLDS = {
        (int) (MAX_SCORE * 0.00), // 0% for Beginner
        (int) (MAX_SCORE * 0.02), // 2% for Good Start
        (int) (MAX_SCORE * 0.05), // 5% for Moving Up
        (int) (MAX_SCORE * 0.08), // 8% for Good
        (int) (MAX_SCORE * 0.15), // 15% for Solid
        (int) (MAX_SCORE * 0.25), // 25% for Nice
        (int) (MAX_SCORE * 0.40), // 40% for Great
        (int) (MAX_SCORE * 0.50), // 50% for Amazing
        (int) (MAX_SCORE * 0.70), // 70% for Genius
        MAX_SCORE                // 100% for Queen Bee
    };
    
    private int totalPoints;
    private String currentLabel;
    private List<Word> foundWords;
    

    public Scoreboard() {
        totalPoints = 0;
        currentLabel = LABELS[0];
        foundWords = new ArrayList<>();
    }
    
    /**
     * update the total points count
     * @param word
     */
    public void updatePoints(Word word) {
        totalPoints += word.getPoints();
        updateLabel();
    }
    
    /**
     * update the score label 
     * based on the total number of points
     */
    private void updateLabel() {
    	for (int i = SCORE_THRESHOLDS.length - 1; i >= 0; i--) {
            if (totalPoints >= SCORE_THRESHOLDS[i]) {
                currentLabel = LABELS[i];
                break;
            }
        }
    }

    /**
     * Add a new word to the list of found words in a game
     * @param newWord
     * @return true if word is added successfully
     */
    public boolean addFoundWord(Word newWord) {
        if (!foundWords.contains(newWord) && newWord.isValid()) { // Check if the list already contains the word
            foundWords.add(newWord);
            Collections.sort(foundWords, new WordComparator()); // sort alphabetically        
            totalPoints += newWord.getPoints(); // Only add points if the word is new
            updateLabel();
            return true; // Word was successfully added
        }
        return false; // Word was not added because it's a repeat
    }
    
    /**
     * Custom comparator to sort words alphabetically
     */
    class WordComparator implements Comparator<Word>{
    	public int compare(Word w1, Word w2) {
            return w1.getCurrentWord().compareTo(w2.getCurrentWord());
        }
    }

    public String getCurrentLabel() {
        return currentLabel;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public List<Word> getFoundWords() {
        return this.foundWords;
    }
    
}
