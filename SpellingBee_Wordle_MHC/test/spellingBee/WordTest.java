/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package spellingBee;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WordTest {

	Honeycomb test1;
	
	
	@BeforeEach
	void setUp() throws Exception {
		test1 = new Honeycomb("common_words.txt");

	}

	@Test
	void testIsPangram() {
		Word word1= new Word("journey", test1); // actual pangram
		Word word2= new Word("fuzzled", test1); // 7 letters, not all unique
		Word word3= new Word("letters", test1); // 7 letters, not all unique
		Word word4 = new Word("user", test1); // <7 letters, all unique
		Word word5 = new Word("test", test1); // <7 letters, not unique
		
		
		assertTrue(word1.isPangram());
		assertTrue(!word2.isPangram());
		assertTrue(!word3.isPangram());
		assertTrue(!word4.isPangram());
		assertTrue(!word5.isPangram());
	}
	
	@Test
	void testCalculatePoints() {
		Word word1 = new Word("four", test1);
		Word word2 = new Word("letters", test1);
		Word word3 = new Word("journey", test1);
		
		assertEquals(1, word1.getPoints());
		assertEquals(7, word2.getPoints());
		assertEquals(14, word3.getPoints());
	}
	
	// test that words < 4 letters long are invalid
	@Test
	void testInvalidLength() {
		Word word1 = new Word("a", test1);
		assertFalse(word1.isValid());
	}
	
	@Test
	void testDoesNotContainCenterLetter() {
		test1.setPangram("journey");
		Word word1 = new Word("a", test1);
		assertFalse(word1.containsCenterLetter());
	}
	
	@Test
	void testValidWord() {
		Word word1 = new Word(test1.getPangram(), test1);
		assertTrue(word1.isValid());
		
	}
	
	@Test
	void testWordNotInDictionary() throws FileNotFoundException {
		SpellingBeeGUI gui = new SpellingBeeGUI();
		Word word1 = new Word("zxcvb", test1);
		assertFalse(gui.isInDictionary(word1));	
	}
	
	@Test
	void testWordInDictionary() throws FileNotFoundException{
		SpellingBeeGUI gui = new SpellingBeeGUI();
		Word word1 = new Word("journey", test1);
		assertTrue(gui.isInDictionary(word1));	
	}

}
