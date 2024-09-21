/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package spellingBee;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
/**
 * Honeycomb object for the Spelling Bee game
 */
public class Honeycomb {

	private String center; // immutable center letter of the honeycomb
	private List<String> otherLetters = new ArrayList<String>(); // other 6 letters of the honeycomb
	private List <HoneycombView> listeners = new ArrayList<>();
	private String pangram;

	/**
	 * initialize the honeycomb with a pangram
	 * @param filename -- should be common_words.txt included in the src folder
	 * @throws IOException
	 */
	public Honeycomb(String filename) throws IOException {
		
		List<String> commonWords = readFile(filename); // generate a list of common english words 
		List<String> pangrams = new ArrayList<String>(); // generate a list to store all pangrams
		pangram = ""; 
		
		// generate a list of pangrams from the list of common words
		for (String i : commonWords) {
			if (checkUniqueLetters(i)) {
				pangrams.add(i);
			}
		}
		
		// pick a random pangram for this instance of the game
		this.pangram = pickRandomElement(pangrams);
		
		Set<Character> uniqueCharacters = new HashSet<>();
		for (char c : this.pangram.toCharArray()) {
			uniqueCharacters.add(c);
		}
		
		// pick some letter in the pangram to be the center letter
		center = pickRandomElement(new ArrayList<>(uniqueCharacters)).toString(); 
		
		// the rest of the letters in the pangram to go around the honeycomb
		for (Character i : uniqueCharacters) {
			if (!i.toString().equals(center)) {
				otherLetters.add(i.toString());
			}
		}

	}

	/**
	 * help method: read a file and store the words from the file to a string list
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	private List<String> readFile(String filename) throws IOException {
		List<String> wordList = new ArrayList<String>();

		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			try {
				String line;
				while ((line = br.readLine()) != null) {
					wordList.add(line);
				}
			} finally {
				br.close();
			}

		} catch (FileNotFoundException e) {
			System.out.println("File " + filename + " was not found.");
		}
		return wordList;
	}


	/**
	 * helper method: check whether the word is a pangram
	 * @param word
	 * @return true if pangram
	 */
	public boolean checkUniqueLetters(String word) {


		Set<Character> uniqueCharacters = new HashSet<>();

		// Iterate over each character in the string
		for (char c : word.toCharArray()) {
			uniqueCharacters.add(c);
		}

		// Check if the set size is exactly 7
		return uniqueCharacters.size() == 7;
	}


	/**
	 * helper method: generate a random element from a list
	 * @param <T>
	 * @param list
	 * @return
	 */
	private <T> T pickRandomElement(List<T> list) {
		Random random = new Random();
		int index = random.nextInt(list.size());
		return list.get(index);
	}

	/**
	 * change the order of elements in otherLetters
	 */
	public void shuffle() {
		Collections.shuffle(otherLetters);
		notifyListeners();
	}
	
	public List<String> getOtherLetters(){
		return this.otherLetters;
	}
	
	public String getCenter() {
		return this.center;
	}
	
	public String getPangram() {
		return this.pangram;
	}
	
	// EXCLUSIVELY FOR TESTING PURPOSES!
	public void setPangram(String p) {
		this.pangram = p;
	}
	
	public void addListener(HoneycombView listener) {
		listeners.add(listener);
	}
	
	private void notifyListeners() {
		for (HoneycombView listener : listeners) {
			listener.update();
		}
	}
	
	
}
