/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package spellingBee;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * Main game controller and GUI for Spelling Bee game
 */
public class SpellingBeeGUI extends JFrame {

    private JTextField guessTextField;
    private JButton deleteButton;
    private JButton enterButton;
    private JButton shuffleButton;
    private JList<Word> foundWordsArea;
    private JScrollPane foundWordsScrollPane;
    private JLabel scoreLabel;
    private JLabel performanceLabel;

    private Honeycomb honeycomb;
    private Scoreboard scoreboard;
    private HoneycombView honeycombView;
    private ArrayList<String> dictionary;
    
    public SpellingBeeGUI() throws FileNotFoundException {
        super("Spelling Bee");
        dictionary = setDictionary(); // generate a dictionary to check user guesses
        initializeComponents();
        layoutComponents();
    }
    
    /**
     * initialize all Swing components and action listeners for the game
     */
    private void initializeComponents() {
    	
        guessTextField = new JTextField(20);
        guessTextField.setText("");
        deleteButton = new JButton("Delete");
        enterButton = new JButton("Enter");
        shuffleButton = new JButton("Shuffle");
        
        foundWordsArea = new JList<Word>();
        foundWordsScrollPane = new JScrollPane(foundWordsArea);
        
        scoreLabel = new JLabel("Score: 0");
        performanceLabel = new JLabel("Level: Beginner");
        
        // when user enters their guess from the keyboard, registers guess from text field 
        guessTextField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
        guessTextField.getActionMap().put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processGuess();
            }
        });
        
        // set up listeners for buttons
        
        deleteButton.addActionListener(e -> {
            String currentText = guessTextField.getText();
            if (!currentText.isEmpty()) {
                guessTextField.setText(currentText.substring(0, currentText.length() - 1));
            }
            guessTextField.requestFocus();
        });
        
        enterButton.addActionListener(e -> processGuess());
        
        shuffleButton.addActionListener(e -> {
            honeycomb.shuffle();
            guessTextField.requestFocus(); // Focus back to text field after shuffling
        });

        try {
        	// Pangram is generated with the game's honeycomb
            honeycomb = new Honeycomb("common_words.txt");
            honeycombView = new HoneycombView(honeycomb, this);
            
            scoreboard = new Scoreboard();

            updateDisplay();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to initialize game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    /**
     * Layout the components for the game
     */
    private void layoutComponents() {
    	
        JPanel inputPanel = new JPanel();
        inputPanel.add(guessTextField);
        inputPanel.add(enterButton);
        inputPanel.add(deleteButton);
        inputPanel.add(shuffleButton);

        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.add(foundWordsScrollPane, BorderLayout.CENTER);
        displayPanel.add(scoreLabel, BorderLayout.NORTH);
        displayPanel.add(performanceLabel, BorderLayout.SOUTH);

        JPanel honeycombPanel = new JPanel();
        honeycombPanel.add(honeycombView);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(displayPanel, BorderLayout.CENTER);
        add(honeycombPanel, BorderLayout.WEST);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Error check a user's guess for errors
     * If the guess is valid, add it to the scoreboard
     * Else, give an error message 
     */
    private void processGuess() {
        String text = guessTextField.getText().trim();
        
        if (!text.isEmpty()) {
        	
            Word word = new Word(text, honeycomb);
            if (text.length() < 4) {
            	JOptionPane.showMessageDialog(this, "Too short", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if (!word.containsCenterLetter()) {
            	JOptionPane.showMessageDialog(this, "Missing center letter", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if (!isInDictionary(word)){
            	JOptionPane.showMessageDialog(this, "Not in word list", "Error", JOptionPane.ERROR_MESSAGE);
            }
            // word contains some letter not in the honeycomb
            else if (!containsAllValidLetters(word.getCurrentWord())) {
            	JOptionPane.showMessageDialog(this, "Invalid letters", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if (word.isValid() && isInDictionary(word)) {
                if (scoreboard.addFoundWord(word)) {  // Only add to scoreboard if it's not already found
                    updateDisplay();
                } else {
                    JOptionPane.showMessageDialog(this, "Already found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
            	JOptionPane.showMessageDialog(this, "Invalid word", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            guessTextField.setText("");
            guessTextField.requestFocus(); // Focus back to text field after processing
            
        }
    }

    /**
     * Helper method to check if the guess contains only valid letters from the honeycomb
     * @param guess
     * @return true if valid guess
     */
    private boolean containsAllValidLetters(String guess) {
        List<String> honeycombLetters = honeycomb.getOtherLetters();
        honeycombLetters.add(honeycomb.getCenter());
        for (char c : guess.toCharArray()) {
            if (!honeycombLetters.contains(Character.toString(c))) {
            	honeycombLetters.remove(honeycomb.getCenter());
                return false;
            }
        }
        honeycombLetters.remove(honeycomb.getCenter());
        return true;
    } 
    
    /**
     * update the list of found words every time a new guess is processed
     */
    private void updateDisplay() {
    	
        DefaultListModel<Word> model = new DefaultListModel<>();
        for (Word word : scoreboard.getFoundWords()) {
            model.addElement(word);
        }
        
        foundWordsArea.setModel(model);
        foundWordsArea.setCellRenderer(new WordCellRenderer()); // set the font style of the word
        scoreLabel.setText("Score: " + scoreboard.getTotalPoints());
        performanceLabel.setText("Level: " + scoreboard.getCurrentLabel());
        scoreLabel.setText("Found Words: " + scoreboard.getFoundWords().size());
    }
    

    /**
     * Cell renderer for the JList 
     * Handles font style for each line of the list
     */
    private static class WordCellRenderer extends DefaultListCellRenderer {
        private Font regularFont = getFont();
        private Font boldFont = regularFont.deriveFont(Font.BOLD);

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            
        	Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        	// cells of the list are rendered as JLabels so that can be stylized
            if (renderer instanceof JLabel && value instanceof Word) {
            	
                Word word = (Word) value;
                JLabel label = (JLabel) renderer;
                label.setText(word.getCurrentWord());
                
             // if the word is a pangram, set its font to bold
                // otherwise, its font is plain
                if (word.isPangram()) {  
                	label.setFont(boldFont);
                } else {
                	label.setFont(regularFont);
                }
            }
            return renderer;
        }
    }
    
    /**
     * helper to update the guess field when a user clicks a honeycomb letter
     * @param letter
     */
    public void addLetterToGuessField(String letter) {
    	String currentGuess = guessTextField.getText();
    	currentGuess += letter;
    	guessTextField.setText(currentGuess);
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
	
	public boolean isInDictionary(Word word) {
		return dictionary.contains(word.getCurrentWord());
	}

    public static void main(String[] args) throws FileNotFoundException {
        new SpellingBeeGUI();
    }
}
