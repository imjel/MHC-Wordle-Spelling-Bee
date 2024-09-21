/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package wordle;

public interface KeyboardListener {
   
	/**
     * Called when the keyboard needed to be changes.
     */
	void keyboardChanged(String targetWord, Letter.State[] letters);
}
