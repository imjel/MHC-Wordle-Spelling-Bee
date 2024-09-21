/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package wordle;

/**
 * Keyboard object for the Wordle game
 * Changes based on the state of the user's guesses
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Keyboard {

    private Map<Character, Letter> letters; // Stores letter objects for each alphabet letter
    private WordleGame game;

    private List<KeyboardView> listeners = new ArrayList<>();
    
    public Keyboard(WordleGame game) {
        this.game = game;
        this.setLetters(new HashMap<>());
        initializeLetters();
    }

    /**
     * Initializes each letter from A to Z with a default state of NOT_TRIED.
     */
    void initializeLetters() {
        for (char c = 'A'; c <= 'Z'; c++) {
            getLetters().put(c, new Letter(c));
        }
    }


    /**
     * Submits the current guess to the game for evaluation and updates the key states based on the feedback.
     */
    public void submitGuess() {
        if (game.isGuessComplete()) {
            updateKeyStates();
        }
    }


    /**
     * Removes the last letter from the current guess.
     */
    public void deleteLetter() {
        game.removeLastLetterFromCurrentGuess();
    }

    /**
     * Updates the state of each key based on the latest feedback from the game after a guess is evaluated.
     */
    void updateKeyStates() {
        String currentGuess = game.getCurrentGuess();
        Letter.State[] feedback = game.evaluateGuess(currentGuess);

        for (int i = 0; i < currentGuess.length(); i++) {
            char guessChar = Character.toUpperCase(currentGuess.charAt(i));
            getLetters().get(guessChar).setState(feedback[i]);
        }

        notifyListeners();
    }

    
    /**
     * Resets the keyboard to default state
     */
    public void reset() {
        for (Letter l : getLetters().values()) {
            l.setState(Letter.State.NOT_TRIED); // Reset state to NOT_TRIED
        }
        notifyListeners(); // Notify all listeners to update their view based on the reset state
    }

    

    /**
	 * Add a listener to the Keyboard's list of listeners
	 * notified when keyboard changes
	 * @param listener to add
	 */
	public void addListener(KeyboardView listener) {
		listeners.add(listener);
	}
	
	private void notifyListeners() {
	    for (KeyboardListener listener : listeners) {
	        listener.keyboardChanged(game.getCurrentGuess(), game.evaluateGuess(game.getCurrentGuess()));
	    }
	}

	public Map<Character, Letter> getLetters() {
		return letters;
	}

	public void setLetters(Map<Character, Letter> letters) {
		this.letters = letters;
	}


}
