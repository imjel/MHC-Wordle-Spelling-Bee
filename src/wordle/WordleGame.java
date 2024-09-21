/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package wordle;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * Main controller and GUI for the Wordle game
 * Game resets when a user wins/loses a game
 */
public class WordleGame {
	
	private boolean isWin = false;
	private String targetWord;
	private int numGuesses = 0;
	private JTextField guessField = new JTextField(25);
	private JFrame frame;
	private Keyboard keyboard;
	private KeyboardView keyboardView = new KeyboardView(this);
	private GridView gridView;
	private Grid grid;

	private final int gridWidth = 600;
	private String currentGuess = "";

	private ArrayList<String> wordleDictionary; // Stores all valid wordle words
	private ArrayList<String> dictionary; // Stores an English dictionary

	public WordleGame() throws FileNotFoundException {
		try {
			// wordle algorithm to get the game's target word
			dictionary = setDictionary();
			wordleDictionary = getFiveLetterWords();
			setTargetWord(getRandomWordle(wordleDictionary));


			// setting up UI and components
			initializeComponents();
			setupUI();

		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to load dictionary.");
			throw new FileNotFoundException("Dictionary file not found.");
		}
	}

	/**
	 * setting up the the User interface
	 */
	private void setupUI() {

		frame = new JFrame("Wordle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);

		JPanel guessPanel = createGuessPanel();
		frame.add(guessPanel, BorderLayout.SOUTH);

		JPanel keyboardPanel = createKeyboardPanel();
		frame.add(keyboardPanel, BorderLayout.CENTER);

		JPanel wordlePanel = createWordlePanel();
		frame.add(wordlePanel, BorderLayout.NORTH);

		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Creates a panel where the user can input their guess
	 * 
	 * @return guessPanel
	 */
	private JPanel createGuessPanel() {
		JPanel guessPanel = new JPanel();
		guessPanel.add(new JLabel("Enter your guess: "));
		guessPanel.add(guessField);
		guessField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				currentGuess = guessField.getText();
				submitGuess();
				guessField.setText(""); 
				// Clear the guess field after processing
			}
		});
		return guessPanel;
	}

	/**
	 * Creates a panel for the keyboard
	 * 
	 * @return keyboardPanel
	 */
	private JPanel createKeyboardPanel() {

		JPanel keyboardPanel = new JPanel();
		// initialize keyboard view and keyboard
		keyboardPanel.add(keyboardView);

		return keyboardPanel;

	}
	
	/**
	 * Creates a panel for the wordle grid
	 * 
	 * @return wordlePanel
	 */
	private JPanel createWordlePanel() {
		JPanel wordlePanel = new JPanel();
		wordlePanel.setBackground(Color.WHITE);
		wordlePanel.add(gridView);
		return wordlePanel;
	}
	
	/**
	 * Initializes the game models 
	 */
	private void initializeComponents() {

		grid = new Grid(getTargetWord());
		gridView = new GridView(grid, gridWidth);
		grid.addListener(gridView);

		keyboard = new Keyboard(this);
		keyboardView = new KeyboardView(this);
		keyboard.addListener(keyboardView);

	}
	
	/**
	 * Updates the guess field based on user input
	 */
	private void updateGuessDisplay() {
		if (guessField != null) {
			guessField.setText(currentGuess); 
		}
	}

	/**
	 * Submits the current guess to the game for evaluation and updates the key
	 * states based on the feedback.
	 */
	public void submitGuess() {
		processGuess(currentGuess);
//		keyboard.submitGuess();
		updateGuessDisplay();
		
	}

	/**
	 * When user enters a guess, error check and update game state
	 * 
	 * @param guess
	 */
	void processGuess(String guess) {

		guess = guess.trim().toLowerCase();

		// guess is too short
		if (guess.length() < 5) {
			displayError("Too short");
			currentGuess = "";
			updateGuessDisplay();
			return;
		}
		// guess is too long
		else if (guess.length() > 5) {
			displayError("Too long");
			currentGuess = "";
			updateGuessDisplay();
			return;
		}
		// word is not in dictionary
		else if (!isInDictionary(guess)) {
			displayError("Not in word list");
			currentGuess = "";
			updateGuessDisplay();
			return;
		}
		
		numGuesses++;

		Letter.State[] feedbacks = grid.getLetterStates(guess);
		currentGuess = guess;
	    keyboardView.keyboardChanged(currentGuess, feedbacks);
	    keyboard.submitGuess();
	  

		try {
			grid.evaluateGuess(guess, numGuesses - 1);		
			updateGuessDisplay();
			

			currentGuess = "";

		} catch (IOException e) {
			e.printStackTrace();
		}

		// win and loss messages
		if (guess.equals(getTargetWord())) {
			JOptionPane.showMessageDialog(frame, "Congratulations! You've guessed the word!");
			isWin = true;
			this.resetGame();
		} else if (numGuesses >= 6) {
			JOptionPane.showMessageDialog(frame, "Game over! The word was: " + getTargetWord());
			isWin = false;
			this.resetGame();
		}
	}


	/**
	 * evaluate the guess by comparing each letter in the guess to the targetWord
	 * @param guess the string being evaluated
	 * @return a Letter.State[] indicating the state of each letter in the guess
	 */
	Letter.State[] evaluateGuess(String guess) {
    char[] guessChars = guess.toCharArray();
    StringBuilder tempString = new StringBuilder(getTargetWord());
    Letter.State[] feedbacks = new Letter.State[5];
    Letter.State[] states = new Letter.State[5];


    // handle the case of reseting
    if(guessChars.length == 0) {
    	for (int i = 0; i < 5; i++) {
    		states[i] = Letter.State.NOT_TRIED;
    		
    	}
    	return states;
    }
    // Check for correct positions
    for (int i = 0; i < 5; i++) {
        if (guessChars[i] == tempString.charAt(i)) {
            states[i] = Letter.State.CORRECT;
            tempString.setCharAt(i, '.');  // Mark this character as correctly used
        } else {
            states[i] = Letter.State.ABSENT;
        }
    }

    // Check for present characters not in correct position
    for (int i = 0; i < 5; i++) {
        if (states[i] != Letter.State.CORRECT) {
            int index = tempString.indexOf(String.valueOf(guessChars[i]));
            if (index != -1 && tempString.charAt(index) != '.' && tempString.charAt(index) != '?') {
                states[i] = Letter.State.PRESENT;
                tempString.setCharAt(index, '?');  // Mark this character as used in wrong place
            }
        }
    }

    for (int i = 0; i < guessChars.length; i++) {
        feedbacks[i]=  states[i];
    }

    return feedbacks;
}

	
	/**
	 * Check if a string is in the game's English dictionary
	 * @param word
	 */
	public boolean isInDictionary(String word) {
		return dictionary.contains(word);
	}

	/**
	 * display the error message
	 * @param message
	 */
	private void displayError(String message) {
		JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Helper for getRandomWordle generates a list of five letter words from the common words dictionary
	 * @return all five letter common words
	 * @throws FileNotFoundException
	 */
	ArrayList<String> getFiveLetterWords() throws FileNotFoundException {

		ArrayList<String> fiveLetterWords = new ArrayList<>();
		String current;
		File file = new File("common_words.txt");

		Scanner in = new Scanner(file);

		while (in.hasNextLine()) {
			current = in.nextLine();
			if (current.length() == 5 && isInDictionary(current)) {
				fiveLetterWords.add(current);
			}
		}
		in.close();

		return fiveLetterWords;
	}

	/**
	 * Randomly select a word for wordle
	 * @param fiveLetterWords
	 * @return random word
	 */
	String getRandomWordle(ArrayList<String> fiveLetterWords) {

		Random rand = new Random();
		return fiveLetterWords.get(rand.nextInt(fiveLetterWords.size()));

	}

	/**
	 * Initialize dictionary for checking user input
	 * @return English dictionary
	 */
	private ArrayList<String> setDictionary() throws FileNotFoundException {

		ArrayList<String> dict = new ArrayList<>();
		File file = new File("EnglishWords.txt");

		Scanner in = new Scanner(file);

		while (in.hasNextLine()) {
			dict.add(in.nextLine());
		}
		in.close();

		return dict;

	}
	
	/**
	 * Check if the user can enter more letters to the guess
	 * @return a boolean indicating whether the user can enter more letters to the guess
	 */
	boolean canEnterMoreLetters() {

		if (!isWin && (currentGuess.length() < 5)) {
			return true;
		}
		return false;
	}

	/**
	 * add a character to the guess and make it displayed in the guessFiled
	 * @param letter: the character that entered by the user and needed to be added to the guess
	 */
	void addLetterToCurrentGuess(char letter) {
		currentGuess += letter;
		updateGuessDisplay();
    
	}

	/**
	 * check whether the guess is complete(whether it has and only has 5 letters)
	 * @return a boolean indicating whether the guess has 5 letters
	 */
	public boolean isGuessComplete() {
	    boolean complete = currentGuess.length() == 5;
	    return complete;
	}

	/**
	 * remove a letter from currentGuess and change the guessFiled display
	 */
	void removeLastLetterFromCurrentGuess() {
		currentGuess = currentGuess.substring(0, currentGuess.length() - 1);
		updateGuessDisplay();
	}
	
	/**
	 * reset the game state once a game is over
	 */
	public void resetGame() {
		
		// reset game state
		numGuesses = 0;
		currentGuess = "";
		setTargetWord(getRandomWordle(wordleDictionary));
		
		// reset views and set grid target to new wordle
		grid.reset();
		grid.setTargetWord(getTargetWord()); 
		keyboard.reset();
		
	}
	
	/**
	 * Return the game's target word/wordle
	 * Exclusively for testing purposes
	 */
	public String getTargetWord() {
		return this.targetWord;
	}
	
	public Keyboard getKeyboard() {
		return this.keyboard;
	}
	
	public void setCurrentGuess(String newGuess) {
		this.currentGuess = newGuess;
	}
	
	String getCurrentGuess() {
		return this.currentGuess;
	}
	

	public static void main(String[] args) {
		try {
			new WordleGame();
		} catch (FileNotFoundException e) {
			System.err.println("Dictionary file not found: " + e.getMessage());
		}
	}

	public void setTargetWord(String targetWord) {
		this.targetWord = targetWord;
	}

	public void setIsWin(boolean b) {
		this.isWin = b;
	}
	
	public Boolean getIsWin() {
		return this.isWin;
	}
}
