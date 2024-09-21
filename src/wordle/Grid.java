/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package wordle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Grid object for a Wordle game
 * Represents the current state of a user's guesses
 * in comparison to the game's target word
 */
public class Grid {
	
	private Letter[][] letters = new Letter[6][5];
	private String targetWord;
	private List<GridView> listeners = new ArrayList<>();

	// constructor
	public Grid(String wordle) {
		targetWord = wordle;
	}

	/**
	 * put the word into the grid
	 *
	 * @param wordEntered a letter array which is the word the user entered
	 */
	public void setWord(Letter[] wordEntered) {

		// put the 5-letter word to the grid. Iterate through the grid and put it in the
		// first null row.
		for (int i = 0; i < 5; i++) {
			if (letters[i] == null) {
				letters[i] = wordEntered;
				// notify listeners that the grid has changed
				notifyListeners();
				break;
			}
			
		}
	}

	/**
	 * evaluate the guess by comparing words in each row of the grid to the targetWord
	 * @param wordEntered the string being evaluated
	 * @param row a integer indicating which row is being evaluated
	 */
    public void evaluateGuess(String wordEntered, int row) throws IOException {
        Letter.State[] feedback = getLetterStates(wordEntered);
        Letter[] guessLetters = new Letter[5];

        for (int i = 0; i < 5; i++) {
            char guessChar = wordEntered.charAt(i);
            guessLetters[i] = new Letter(guessChar);
            guessLetters[i].setState(feedback[i]);
        }

        letters[row] = guessLetters;
        notifyListeners();
    }

    /**
	 * get the states of letters in guess by comparing each of them to the targetWord
	 * @param guess the string being evaluated
	 * @return a Letter.State[] indicating the state of each letter in the guess
	 */
     Letter.State[] getLetterStates(String guess) {
        char[] guessChars = guess.toCharArray();
        StringBuilder tempString = new StringBuilder(targetWord);
        Letter.State[] feedbacks = new Letter.State[5];
        Letter.State[] states = new Letter.State[5];

        // First pass: Check for correct positions
        for (int i = 0; i < 5; i++) {
            //System.out.println("Comparing guess: " + guessChars[i] + " with target: " + tempString.charAt(i));
            if (guessChars[i] == tempString.charAt(i)) {
                states[i] = Letter.State.CORRECT;
                //System.out.print("green setted");
                tempString.setCharAt(i, '.');  // Mark this character as correctly used
            } else {
                states[i] = Letter.State.ABSENT;
                //System.out.print("gray seted for: " + guessChars[i] + " ");
            }
        }

        // Second pass: Check for present characters not in correct position
        for (int i = 0; i < 5; i++) {
            if (states[i] != Letter.State.CORRECT) {
                //System.out.println("Comparing guess: " + guessChars[i]);
                int index = tempString.indexOf(String.valueOf(guessChars[i]));
                if (index != -1 && tempString.charAt(index) != '.' && tempString.charAt(index) != '?') {
                    states[i] = Letter.State.PRESENT;
                    //System.out.print("yellow setted");
                    tempString.setCharAt(index, '?');  // Mark this character as used in wrong place
                }
            }
        }

        // Map feedbacks
        for (int i = 0; i < guessChars.length; i++) {
            feedbacks[i]=  states[i];
            //System.out.print(" " + guessChars[i]+ " put in with value " + states[i]);
        }

        return feedbacks;
    }


	
	public Letter[][] getLetters(){
		return this.letters;
	}
	
	public String getTargetWord() {
		return this.targetWord;
	}
	
	public void setTargetWord(String newWordle) {
		this.targetWord = newWordle;
	}
	
	/**
	 * Add a listener to the grid's list of listeners
	 * notified when grid changes
	 * @param listener to add
	 */
	public void addListener(GridView listener) {
		listeners.add(listener);
	}
	
	private void notifyListeners() {
		for(GridView listener : listeners) {
			listener.repaint();
		}
	}
	
	/**
	 * Resets the grid to default state
	 */
	public void reset() {
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				letters[i][j] = null;
				
				notifyListeners();
			}
		}
	}

}
