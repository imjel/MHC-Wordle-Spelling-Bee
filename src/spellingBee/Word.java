/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package spellingBee;


/**
 * Represents a word submitted by a user
 */
public class Word {


    private String guess;
    private int points;
    private Honeycomb honeycomb;

    public Word(String guess, Honeycomb honeycomb) {
        this.guess = guess;
        this.honeycomb = honeycomb; // current instance of the honeycomb
        this.points = calculatePoints();
    }
    
    /**
     * Checks if the word contains at least 7 different letters
     */
    public boolean isPangram() {
        return guess.chars().distinct().count() >= 7;
    }
    
    /**
     * Calculate the value of a word, depending on its length and pangram status
     * @return points
     */
    private int calculatePoints() {
        if (guess.length() > 4) {
            points = guess.length();
            if (isPangram()) {
                points *= 2;  // Double the score for pangrams
            }
        } else {
            points = 1; // Minimum points (word is 4 letters)
        }
        return points;
    }

    // Checks if the word is valid based on length and whether it has the center letter
    public boolean isValid() {
        return guess.length() >= 4 && containsCenterLetter();
    }

    // Checks if the pangram has the center letter
    public boolean containsCenterLetter() {
    	return guess.contains(honeycomb.getCenter());
    }
    
    public int getPoints() {
        return this.points;

    }

    public String getCurrentWord() {
        return guess;
    }
   
}

