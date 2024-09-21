/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package spellingBee;


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class honeycombTest {

	Honeycomb test1;
	@BeforeEach
	void setUp() throws IOException  {
		test1 = new Honeycomb("common_words.txt");
	}
	
	// test that all letters in the honeycomb are distinct
	@Test
	void testCheckUniqueLetters() {
		
		List<String> allLetters = new ArrayList<String>();
		allLetters.add(test1.getCenter());
		allLetters.addAll(test1.getOtherLetters());
		String letters = "";
		for(String i:allLetters) {
			letters+=i;
		}
		
		assertTrue(test1.checkUniqueLetters(letters));

	}
	
	@Test
	void testShuffleLetters() {
		
		List<String> beforeShuffle = new ArrayList<String>();
		beforeShuffle.addAll(test1.getOtherLetters());
		test1.shuffle();
		
		assertTrue(!beforeShuffle.equals(test1.getOtherLetters()));
		
	}
	
	// test when a pangram is created, it is >= 7 letters and is made up of unique chars
	@Test
	void testPangramConditions() {
		String pangram = test1.getPangram();
		assertTrue(pangram.length() >= 7);
		assertTrue(test1.checkUniqueLetters(pangram));
	}
	
	@Test
	void testBadFileInput() {
		assertThrows(IllegalArgumentException.class, 
				() -> new Honeycomb("blah"),
				"Expected IllegalArgumentException"
		);
	}
	
}
