/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package wordle;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
/**
 * View for the Keyboard class
 * Sets up the specific Wordle layout
 * Changes the color of the keys based on the relationship to the user's guesses
 */
public class KeyboardView extends JPanel implements KeyboardListener{

	
	public enum LetterState {
	    CORRECT, PRESENT, ABSENT, NOT_TRIED;
	    
	    public Color getColor() {
	        switch (this) {
	            case CORRECT:
	                return new Color(106, 171, 100); // green
	            case PRESENT:
	                return new Color(209, 177, 52); // yellow
	            case ABSENT:
	                return new Color(119, 124, 126); // dark gray
	            default:
	                return Color.WHITE;
	        }
	    }
	}

	private final Map<Character, JButton> keyButtons;
	private WordleGame game;
	private Keyboard keyboard;

	/**
	 * the constructor
	 **/
	public KeyboardView(WordleGame game) {
		this.game = game;
		this.keyButtons = new HashMap<>();
		setKeyboard(game.getKeyboard());
		setLayout(new GridLayout(3, 1)); // 3 rows of keys
		createKeyboard();
	}

	/**
	 * the method to initialize the keyboard and create the keys
	 * 
	 **/
	private void createKeyboard() {

		setLayout(new GridLayout(3, 1)); // Three rows for the keyboard

		add(createKeyRow("QWERTYUIOP"));
		add(createKeyRow(" ASDFGHJKL "));
		add(createKeyRowWithFunctionKeys("ZXCVBNM"));

		setVisible(true);
	}

	/**
	 * the method to create key rows
	 * @param keys the string that contains all the characters that needed to be key
	 * 
	 **/
	private JPanel createKeyRow(String keys) {
		JPanel panel = new JPanel(new GridLayout(1, keys.length()));
		for (char key : keys.toCharArray()) {
			if (Character.isWhitespace(key))
				continue; // Skip whitespace for alignment
			JButton keyButton = createKeyButton(key);
			keyButtons.put(Character.toUpperCase(key), keyButton);
			panel.add(keyButton);
		}
		return panel;
	}

	/**
	 * the method to create key rows with enter and delete buttons
	 * @param keys the string that contains all the characters that are in the same row with the function keys
	 * 
	 **/
	private JPanel createKeyRowWithFunctionKeys(String keys) {
		JPanel panel = new JPanel(new GridLayout(1, keys.length() + 2));
		panel.add(createFunctionKeyButton("ENTER"));
		for (char key : keys.toCharArray()) {
			JButton keyButton = createKeyButton(key);
			keyButtons.put(Character.toUpperCase(key), keyButton);
			panel.add(keyButton);
		}
		panel.add(createFunctionKeyButton("DEL"));
		return panel;
	}

	/**
	 * the method to create keys
	 * @param key the character that shows up on the button
	 * 
	 **/
	private JButton createKeyButton(char key) {
		JButton button = new JButton(String.valueOf(key));
		button.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) { // Only respond to left clicks
					game.addLetterToCurrentGuess(key);
				}
			}
		});
		styleKeyButton(button);
		return button;
	}

	/**
	 * the method to create keys with functions( enter or delete)
	 * @param label the text that shows up on the button
	 * 
	 **/
	private JButton createFunctionKeyButton(String label) {
		JButton button = new JButton(label);
		styleKeyButton(button);
		button.setPreferredSize(new Dimension(100, 60)); // Bigger button for function keys
		if (label.equals("ENTER")) {
			button.addActionListener(event -> {
			game.submitGuess();
}
					
					);
		}else if(label.equals("DEL")) {
			button.addActionListener(event -> game.removeLastLetterFromCurrentGuess());
		}
		return button;
	}

	/**
	 * a helper method to set up the size and font of each botton
	 * @param the button that needed to be set up
	 **/
	private void styleKeyButton(JButton button) {
		button.setFocusPainted(false);
		button.setFocusable(false);
		button.setFont(new Font("Arial", Font.BOLD, 18));
		button.setPreferredSize(new Dimension(60, 60)); // Regular size for letter keys
	}
	
	
	/**
	 * the method to update keys's state in the keyboard.
	 * @param letter the character that needed to be changed
	 * @param the state of that letter
	 **/
	public void updateKeys(char letter, Letter.State state) {
	    JButton button = keyButtons.get(Character.toUpperCase(letter));
	    if (button != null) {
	        Color color = state.getColor();
	        keyboard.getLetters().get(Character.toUpperCase(letter)).setState(state);
	        
	        button.setBackground(color);
	        
	        button.setOpaque(true);
	        button.setBorderPainted(false);
	    }
	}

	/**
	 * the method to reset the keyboard view and make all buttons in the keyboard to the default state.
	 **/
	public void resetKeyboard() {
	    for (JButton button : keyButtons.values()) {
	        button.setBackground(Color.WHITE); // Reset color to default
	        button.setOpaque(true);
	        button.setBorderPainted(true);
	    }
	}
	
	protected void setKeyboard(Keyboard newKeyboard) {
		this.keyboard = newKeyboard;
	}

	/**
	 * the method to change keyboard display by updating the states of the letters in the keyboard.
	 * @param currentGuess a string we get from the WordleGame class indicating the user's guess
	 * @param a Letter.State[] indicating the states of each letters in the guess comparing to the targetWord
	 */
	@Override
	public void keyboardChanged(String currentGuess, Letter.State[] states) {
		
		//handle the case of reseting
		if(currentGuess.length()==0) {
			resetKeyboard();
	    }else {
	    	for (int i = 0; i < 5; i++) {
		        Letter guessLetter = keyboard.getLetters().get(Character.toUpperCase(currentGuess.charAt(i)));
		        // If the letter was not tried before or was absent, update it
		        if (guessLetter.getState() == Letter.State.ABSENT || guessLetter.getState() == Letter.State.NOT_TRIED) {
		            updateKeys(currentGuess.charAt(i), states[i]);
		        }
		        // If the letter is present, only update if it's correct in the new guess
		        else if (guessLetter.getState() == Letter.State.PRESENT) {
		            if (states[i] == Letter.State.CORRECT) {
		                updateKeys(currentGuess.charAt(i), states[i]);
		            }
		            // If the new state is absent, do not update
		        }else {
		        	// If the letter is correct, no update is needed as it should remain correct
		        }
				
			}
		}

	}

}
