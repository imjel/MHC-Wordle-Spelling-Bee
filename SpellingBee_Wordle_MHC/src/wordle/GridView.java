/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package wordle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JPanel;

/**
 * View for the Grid class visually represents a 6x5 grid with individual cells
 * Cells filled with a letter corresponding with the guess
 * And colored based on the letter's relationship to the target word
 */
public class GridView extends JPanel {

	private final int LEFT_MARGIN;
	private final int TOP_MARGIN;
	private final int LETTER_WIDTH;
	private final int GRID_ROWS;
	private final int GRID_COLS;
	private final Insets INSETS;
	private final Font font = new Font("Helvetica Neue", Font.BOLD, 30);
	
	private Grid gridModel;
	private Rectangle[][] grid;
	private int width;
	
	public GridView (Grid gridModel, int width) {
		
		this.width = width;
		this.gridModel = gridModel;
		this.INSETS = new Insets(0,3,3,3);
		this.GRID_COLS = 5;
		this.GRID_ROWS = 6;
	
		this.LETTER_WIDTH = 80;
		// left margin is determined by finding the size of the entire word, 
			// plus inset spacing, from the width of the whole grid
		this.LEFT_MARGIN = width - ((LETTER_WIDTH + INSETS.right) * GRID_COLS) / 2;
		this.TOP_MARGIN = 0;
		
		int height = (LETTER_WIDTH + INSETS.bottom) * GRID_ROWS + 2 * TOP_MARGIN;
		
		this.grid = initializeGrid();
		this.setPreferredSize(new Dimension(width, height));
	}
	
	/**
	 * create a display for the Grid class
	 */
	private Rectangle[][] initializeGrid() {
		
		grid = new Rectangle[GRID_ROWS][GRID_COLS];
		
		int x = LEFT_MARGIN;
		int y = TOP_MARGIN;
		
		// initialize each cell of the grid as a rectangle
		for (int i = 0; i < GRID_ROWS; i++) {
			for (int j = 0; j < GRID_COLS; j++) {
				grid[i][j] = new Rectangle(x,y, LETTER_WIDTH, LETTER_WIDTH);
			}
			// when we go to a lower row, update the y axis
			x = LEFT_MARGIN;
			y += LETTER_WIDTH + INSETS.bottom;

		}
		
		return grid;
	}
	
	/**
	 * Paint the wordle grid
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        
		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;
		
		Letter[][] letters = gridModel.getLetters();
		
		g.translate(centerX - (GRID_COLS * LETTER_WIDTH) / 2, centerY - (GRID_ROWS * LETTER_WIDTH) / 2);
		
		// iterate through each cell and row
		for(int i = 0; i < GRID_ROWS; i++) {
			for (int j = 0; j < GRID_COLS; j++) {
				// get the component at the specific cell
				Rectangle cell = grid[i][j];
				Letter letter = letters[i][j];
				drawCell(g, cell, letter, i, j);
			}
		}
		
	}
	
	/**
	 * Draw an individual cell of the wordle grid
	 * @param g
	 * @param cell
	 * @param letter
	 */
	private void drawCell(Graphics g, Rectangle cell, Letter letter, int row, int col) {
		
		// drawing the border around the cell
		int w = cell.width - 2;
		int h = cell.height - 2;
		
		// offset adjusts the outlines so that each rectangle appears for each col
		int xOffset = (col * w) + 1;
		int yOffset = (row * h) + 1;
		
		g.setColor(new Color (211, 214, 218));
		g.drawRect(xOffset, yOffset, w, h);
		
		// drawing the letter inside the cell
		if (letter != null) {
			// fill in the letter's background according to its state
			g.setColor(letter.getColor());
			g.fillRect(xOffset, yOffset, w, h);
			
			g.setColor(Color.WHITE);
			FontMetrics metrics = g.getFontMetrics(font);
			
			// centering the string in its cell
			String letterString = Character.toString(letter.getValue()).toUpperCase();
	        int stringWidth = metrics.stringWidth(letterString);
	        int stringHeight = metrics.getHeight();
	        int letterX = xOffset + (w - stringWidth) / 2;
	        int letterY = yOffset + (h + stringHeight) / 2 - metrics.getDescent();
			
			g.setFont(font);
			g.drawString(letterString, letterX, letterY);
		}
	}
	
}
