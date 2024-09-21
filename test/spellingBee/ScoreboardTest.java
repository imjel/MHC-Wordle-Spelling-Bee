/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package spellingBee;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScoreboardTest {

	Scoreboard test1;
	Honeycomb honeycomb1;
	Honeycomb honeycomb2;
	@BeforeEach
	void setUp() throws Exception {
		test1 = new Scoreboard();
		honeycomb1 = new Honeycomb("common_words.txt");
	}
	
	// test that all states are as expected when initialized 
	@Test
	void testInitializedState() {
		assertEquals(test1.getTotalPoints(), 0);
		assertTrue(test1.getCurrentLabel().equals("Beginner"));
		assertTrue(test1.getFoundWords().isEmpty());
	}
	
	// test that points are updated appropriately for 4 letter word
	@Test
	void testUpdatePointsMin() {
		
		Word word1 = new Word("test", honeycomb1);
		assertEquals(test1.getTotalPoints(), 0);
		
		test1.updatePoints(word1);
		assertEquals(test1.getTotalPoints(), 1);
		
	}
	
	// test that points are updated appropriately for word longer than 4 letters
	@Test
	void testUpdatePoints() {
		
		Word word1 = new Word("tests", honeycomb1);
		assertEquals(test1.getTotalPoints(), 0);
		
		test1.updatePoints(word1);
		assertEquals(test1.getTotalPoints(), 5);
		
	}
	
	// test label is updated appropriately according to number of points
		// test labels and point correspondence are set statically in the Scoreboard class
	@Test
	void testUpdateLabel() {
		
		Word word1 = new Word("honeycomb", honeycomb1);
		assertTrue(test1.getCurrentLabel().equals("Beginner"));
		test1.updatePoints(word1);
		System.out.println(test1.getCurrentLabel());
		assertTrue(test1.getCurrentLabel().equals("Moving Up"));
		
	}
	
	// test that points are updated appropriately for a pangram
	@Test
	void testAddFoundPangram() {
		
		Word word1 = new Word(honeycomb1.getPangram(), honeycomb1);
		test1.addFoundWord(word1);
		assertTrue(test1.getFoundWords().contains(word1));
		assertEquals(test1.getTotalPoints(), word1.getPoints());
		assertTrue(!test1.getCurrentLabel().isEmpty());

	}
	
	// test against duplicate words
	@Test
	void testAddSameWord() {
		
		Word word1 = new Word(honeycomb1.getPangram(), honeycomb1);
		Word word2 = word1;
		assertTrue(word1.getCurrentWord().equals(word2.getCurrentWord()));
		
		assertTrue(test1.addFoundWord(word1));
		assertEquals(test1.getFoundWords().size(), 1);
		
		assertFalse(test1.addFoundWord(word2));
		assertEquals(test1.getFoundWords().size(), 1);
		
	}

	@Test
	void testGetFoundWords() {
		
		Word word1 = new Word(honeycomb1.getPangram(), honeycomb1);
		test1.addFoundWord(word1);
		assertTrue(test1.getFoundWords().contains(word1));
		
	}
	
	@Test
	void testSortWordsAlphabetically() {
		
		List<Word> list = new ArrayList<>();
		
		Word word1 = new Word("aunt", honeycomb1);
		Word word2 = new Word("tuna", honeycomb1);
		
		list.add(word2);
		list.add(word1);
		
		// before sorting, tuna > aunt
		assertTrue(list.get(0).equals(word2));
		assertTrue(list.get(1).equals(word1));
		
		Collections.sort(list, test1.new WordComparator());
		
		// after sorting, aunt > tuna
		assertTrue(list.get(0).equals(word1));
		assertTrue(list.get(1).equals(word2));

	}

}
