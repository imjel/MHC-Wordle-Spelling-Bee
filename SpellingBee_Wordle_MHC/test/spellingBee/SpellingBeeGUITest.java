/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package spellingBee;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpellingBeeGUITest {
	
	SpellingBeeGUI test1;
	Honeycomb honeycomb;

	@BeforeEach
	void setUp() throws Exception {
		test1 = new SpellingBeeGUI();
		honeycomb = new Honeycomb("common_words.txt");
	}
	
	// tests that dictionary functionality works as intended
	@Test
	void testIsInDictionary() {
		Word word1 = new Word("word", honeycomb);
		Word word2 = new Word("fsdkfs", honeycomb);
		
		assertTrue(test1.isInDictionary(word1));
		assertFalse(test1.isInDictionary(word2));
	}

}
