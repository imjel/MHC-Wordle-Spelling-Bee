/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package wordle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KeyboardTest {

	WordleGame game;
	Keyboard test1;

	@BeforeEach
	void setUp() throws Exception {
		game = new WordleGame();
		test1 = new Keyboard(game);
	}

	//test whether after constructing all of the letters have default state
	@Test
	void testConstructer() {
		for (char c = 'A'; c <= 'Z'; c++) {
			assertEquals(Letter.State.NOT_TRIED, test1.getLetters().get(c).getState());
		}
	}
	
	//the letter are all correct
	@Test
	void testUpdateKeyStatesAllCorrect() {
		String guess = "STEIN";
		game.setCurrentGuess(guess);
		game.setTargetWord(guess);

		test1.updateKeyStates(); // Assuming this method correctly sets the state for each letter in `letters`
		for (char c : guess.toCharArray()) {
			assertEquals(Letter.State.CORRECT, test1.getLetters().get(c).getState());
		}
	}

	//There are two same letter in the guess, and one is present.(the other one is absent)
	@Test
	void testUpdateKeyStatesOnePresentOneAbsent() {
		game.setCurrentGuess("AGLEE");
		game.setTargetWord("STEIN");

		test1.updateKeyStates();

		assertEquals(Letter.State.ABSENT, test1.getLetters().get('E').getState());

	}

	//There are two same letter in the guess and targetWord, and one is present, and another is correct
	@Test
	void testUpdateKeyStatesOneCorrectOnePresent() {
		game.setCurrentGuess("ADEEM");
		game.setTargetWord("ACKEE");

		test1.updateKeyStates();

		assertEquals(Letter.State.CORRECT, test1.getLetters().get('E').getState());

	}

	//The case with the mixed states are contained in the guess
	@Test
	void testMixedStates() {
		game.setCurrentGuess("CARTS");
		game.setTargetWord("TRACE");

		test1.updateKeyStates();
		assertEquals(Letter.State.PRESENT, test1.getLetters().get('C').getState());
		assertEquals(Letter.State.PRESENT, test1.getLetters().get('A').getState());
		assertEquals(Letter.State.PRESENT, test1.getLetters().get('R').getState());
		assertEquals(Letter.State.PRESENT, test1.getLetters().get('T').getState());
		assertEquals(Letter.State.ABSENT, test1.getLetters().get('S').getState());

	}
	
	//The case with all letter present
	@Test
	void testAllPresent() {
		game.setCurrentGuess("TRACE");
	    game.setTargetWord("CATER");
	    
	    test1.updateKeyStates();
	    for (char c : game.getCurrentGuess().toCharArray()) {
	        assertEquals(Letter.State.PRESENT, test1.getLetters().get(c).getState());
	    }
	}
	
	
	@Test
	void testEmptyInput() {
		game.setCurrentGuess("");
	    game.setTargetWord("MOUSE");
	    
	    test1.updateKeyStates();
	    for (char c = 'A'; c <= 'Z'; c++) {
			assertEquals(Letter.State.NOT_TRIED, test1.getLetters().get(c).getState());
		}
	}

	//test whether after reset, the state of all letters becomes NOT_TRIED
	@Test
	void testReset() {
		game.setCurrentGuess("CARTS");
		game.setTargetWord("TRACE");
	    
	    test1.updateKeyStates();
	    test1.reset();
	    for (char c = 'A'; c <= 'Z'; c++) {
			assertEquals(Letter.State.NOT_TRIED, test1.getLetters().get(c).getState());
		}
	}
}
