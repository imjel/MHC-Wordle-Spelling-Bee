/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package wordle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GridTest {
	

	Grid test1;
	Grid test2;

	
	@BeforeEach
	void setUp() throws Exception {
		
		test1 = new Grid("stein");
		test2 = new Grid("apple");
	}
	
	@Test
	void testStatesChange() {
		String guess = "elect";
		Letter.State[] states = test1.getLetterStates(guess);

		for(int i=0; i<5; i++) {
			assertFalse(states[i].equals(null));
		}
	}
	
	@Test
	void testAddGuessToGrid() throws IOException {
		String guess = "elect";
		Letter[][] letters = test1.getLetters();
		
		test1.evaluateGuess(guess, 0);
		
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 5; j++) {
				assertTrue(letters[i][j] != null);
			}
		}
	}
	
	@Test
	void testResetGrid() throws IOException {
		String guess = "elect";
		Letter[][] letters = test1.getLetters();
		
		test1.evaluateGuess(guess, 0);
		//before reset ; first row of grid object should be populated 
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 5; j++) {
				assertTrue(letters[i][j] != null);
			}
		}
		
		test1.reset();
		
		//after reset; grid object should be empty
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 5; j++) {
				assertFalse(letters[i][j] != null);
			}
		}
	}
	
	// tests that 2 repeated letters in the guess, one absent, and one in the right spot 
		// show up with the correct state
	@Test
	void testRepeatLetterInGuess() {
		String guess = "elect";
		char[] guessChar = guess.toCharArray();
		Letter.State[] states = test1.getLetterStates(guess);

		for(int i=0; i<5; i++) {
			System.out.print("color for " + guessChar[i] + ": " + states[i]);
		}

		assertEquals(states[0], Letter.State.ABSENT);
		assertEquals(states[1], Letter.State.ABSENT);
		assertEquals(states[2], Letter.State.CORRECT);
		assertEquals(states[3], Letter.State.ABSENT);
		assertEquals(states[4], Letter.State.PRESENT);
	}
	

	// guess = the wordle
	@Test
	void testCorrectWordle() {
		String guess = "stein";
		Letter.State[] states = test1.getLetterStates(guess);
		
		for(int i=0; i<5; i++) {
			assertEquals(states[i], Letter.State.CORRECT);
		}
	}
	

	// guess has none of the letters in the wordle
	@Test
	void testAllAbsentLetters() {
		String guess = "alarm";
		Letter.State[] states = test1.getLetterStates(guess);
		
		for(int i=0; i<5; i++) {
			assertEquals(states[i], Letter.State.ABSENT);
		}
	}
	
	// tests for guess correctly getting the repeat letters, 
		//but one of the letters is in the wrong position
	@Test
	void testRepeatLettersInWordleNotAllGuess() {
		String guess = "happy";
		Letter.State[] states = test2.getLetterStates(guess);
		
		assertEquals(states[2], Letter.State.CORRECT);
		assertEquals(states[3], Letter.State.PRESENT);
	}
	
	// tests for guess correctly getting the repeat letters, 
			//but all of the repeat letters are in the wrong position
	@Test
	void testRepeatLettersInWordleWrongPosInGuess() {
		String guess = "polyp";
		Letter.State[] states = test2.getLetterStates(guess);
		
		assertEquals(states[0], Letter.State.PRESENT);
		assertEquals(states[4], Letter.State.PRESENT);
	}
	
	// tests for guess correctly getting the repeat letters in the wordle
	@Test
	void testRepeatLettersInWordle() {
		String guess = "apply";
		Letter.State[] states = test2.getLetterStates(guess);
		
		assertEquals(states[1], Letter.State.CORRECT);
		assertEquals(states[2], Letter.State.CORRECT);
	}
	
	@Test
	void testFullGrid() throws IOException {
		String guess = "space";
		Letter[][] letters = test1.getLetters();
		
		// add 6 guesses to each row of the grid
		for (int i = 0; i < 6; i++) {
			test1.evaluateGuess(guess, i);		
		}
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				assertFalse(letters[i][j] == null);
			}
		}
	}


}
