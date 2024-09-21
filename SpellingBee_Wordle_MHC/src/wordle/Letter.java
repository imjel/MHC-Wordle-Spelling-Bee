/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package wordle;

import java.awt.Color;
/**
 * Represents one letter of a guess in the Wordle game
 * Each letter has a state of being in the word, not in the word, 
 * 	 not yet guessed, or present but not in the right location
 */
public class Letter {
    enum State {
        CORRECT(new Color(106, 171, 100)),   // Green
        PRESENT(new Color(209, 177, 52)),  // Yellow
        ABSENT(new Color(119, 124, 126)),     // Gray
        NOT_TRIED(Color.WHITE); // (default state)

        private final Color color;

        State(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return this.color;
        }
    }

    private char value;
    private State state;
    private Color color; // Store the color corresponding to the state of the letter

    public Letter(char value) {
        this.value = value;
        this.state = State.NOT_TRIED;
        this.color = State.NOT_TRIED.getColor(); // Initialize color based on the initial state
    }

    public char getValue() {
        return value;
    }

    public void setState(State state) {
        this.state = state;
        this.color = state.getColor(); // Update color when state changes
    }

    public State getState() {
        return state;
    }

    public Color getColor() {
        return color;
    }
}
