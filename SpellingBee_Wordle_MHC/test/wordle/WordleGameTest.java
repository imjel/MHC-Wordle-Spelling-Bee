/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package wordle;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WordleGameTest {

	WordleGame test1;

	@BeforeEach
	void setUp() throws Exception {
		test1 = new WordleGame();
		test1.setCurrentGuess("");
	}

	@Test
	void testWordleIsFiveWords() {
		assertEquals(test1.getTargetWord().length(), 5);
	}

	@Test
	void testWordleIsInDictionary() {
		String wordle = test1.getTargetWord();
		assertTrue(test1.isInDictionary(wordle));
	}

	// The case with letters all correct
	@Test
	void testEvaluateGuessAllCorrect() {
		String guess = "stein";
		test1.setTargetWord("stein");
		// Letter.State[] expectedState = new Letter.State[5];
		for (int i = 0; i < 5; i++) {
			assertEquals(Letter.State.CORRECT, test1.evaluateGuess(guess)[i]);
		}
	}
	
	// The case with letters all correct
		@Test
		void testEvaluateGuessAllAbsent() {
			String guess = "world";
			test1.setTargetWord("stein");
			// Letter.State[] expectedState = new Letter.State[5];
			for (int i = 0; i < 5; i++) {
				assertEquals(Letter.State.ABSENT, test1.evaluateGuess(guess)[i]);
			}
		}

	// The case with the three states are all contained in the guess
	@Test
	void testMixedStates() {
		String guess = "carts";
		test1.setTargetWord("trace");
		assertEquals(Letter.State.PRESENT, test1.evaluateGuess(guess)[0]);
		assertEquals(Letter.State.PRESENT, test1.evaluateGuess(guess)[1]);
		assertEquals(Letter.State.PRESENT, test1.evaluateGuess(guess)[2]);
		assertEquals(Letter.State.PRESENT, test1.evaluateGuess(guess)[3]);
		assertEquals(Letter.State.ABSENT, test1.evaluateGuess(guess)[4]);
	}

	//There are two same letter in the guess, and one is present.(the other one is absent)
	@Test
	void testEvaluateGuessOnePresentOneAbsent() {
		String currentGuess = "AGLEE";
		test1.setTargetWord("STEIN");

		assertEquals(Letter.State.ABSENT, test1.evaluateGuess(currentGuess)[0]);
		assertEquals(Letter.State.ABSENT, test1.evaluateGuess(currentGuess)[1]);
		assertEquals(Letter.State.ABSENT, test1.evaluateGuess(currentGuess)[2]);
		assertEquals(Letter.State.PRESENT, test1.evaluateGuess(currentGuess)[3]);
		assertEquals(Letter.State.ABSENT, test1.evaluateGuess(currentGuess)[4]);

	}

	//There are two same letter in the guess and targetWord, and one is present, and another is correct
	@Test
	void testEvaluateGuessOneCorrectOnePresent() {
		String currentGuess = "ADEEM";
		test1.setTargetWord("ACKEE");

		assertEquals(Letter.State.CORRECT, test1.evaluateGuess(currentGuess)[0]);
		assertEquals(Letter.State.ABSENT, test1.evaluateGuess(currentGuess)[1]);
		assertEquals(Letter.State.PRESENT, test1.evaluateGuess(currentGuess)[2]);
		assertEquals(Letter.State.CORRECT, test1.evaluateGuess(currentGuess)[3]);
		assertEquals(Letter.State.ABSENT, test1.evaluateGuess(currentGuess)[4]);
	}
	
	//test whether the words selected has 5 letters
	@Test
	void testGetFiveLetterWords() throws FileNotFoundException {
		assertEquals(5, test1.getFiveLetterWords().get(0).length());
		assertEquals(5, test1.getFiveLetterWords().get(100).length());
	}
	
	// test whether the wordles are random
	@Test
	void testGetRandomWordle() throws FileNotFoundException {
		String word1 = test1.getRandomWordle(test1.getFiveLetterWords());
		String word2 = test1.getRandomWordle(test1.getFiveLetterWords());
		
		assertFalse(word1.equals(word2));
	}
	
	//test whether the user can enter more letters to currentGuess
	@Test
	void testCanEnterMoreLetters() {
	    test1.setIsWin(false);
	    
	    test1.setCurrentGuess("lend");
	    assertTrue(test1.canEnterMoreLetters());

	    test1.setCurrentGuess("tests");
	    assertFalse(test1.canEnterMoreLetters());

	    test1.setIsWin(true);
	    assertFalse(test1.canEnterMoreLetters());
	}
	
	// test whether the guess is complete
		@Test
		void testIsGuessComplete(){
			test1.setCurrentGuess("test");
			assertFalse(test1.isGuessComplete());
			
			test1.setCurrentGuess("tests");
			assertTrue(test1.isGuessComplete());
			
		}
	
}

