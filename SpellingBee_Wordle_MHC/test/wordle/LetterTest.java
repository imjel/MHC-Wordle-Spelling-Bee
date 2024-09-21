/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package wordle;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LetterTest {

	Letter letter1;
	
	@BeforeEach
	void setUp() throws Exception {
		letter1 = new Letter('a');
		
	}

	@Test
	void testGetValue() {
		assertEquals('a', letter1.getValue());
	}
	
	@Test
	void testSetState() {

		assertEquals(Letter.State.NOT_TRIED, letter1.getState());
		
		letter1.setState(Letter.State.CORRECT);
		assertEquals(Letter.State.CORRECT, letter1.getState());
		
		letter1.setState(Letter.State.ABSENT);
		assertEquals(Letter.State.ABSENT, letter1.getState());
		
		letter1.setState(Letter.State.PRESENT);
		assertEquals(Letter.State.PRESENT, letter1.getState());
	}
	
	@Test
	void testGetColor() {
		
		assertEquals(Color.white, letter1.getColor());

		letter1.setState(Letter.State.CORRECT);
		assertEquals(new Color(106, 171, 100), letter1.getColor());
		
		letter1.setState(Letter.State.ABSENT);
		assertEquals(new Color(119, 124, 126), letter1.getColor());
		
		letter1.setState(Letter.State.PRESENT);
		assertEquals(new Color(209, 177, 52), letter1.getColor());
	}
	

}
