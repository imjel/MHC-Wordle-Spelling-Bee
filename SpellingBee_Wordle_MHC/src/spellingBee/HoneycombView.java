/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package spellingBee;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * Display for the spelling bee game's honeycomb
 */
public class HoneycombView extends JPanel {
    
    private static final Color CELL_COLOR = new Color(228, 228, 228); // Grey color of other letters
    private static final Color CENTER_COLOR = new Color(243, 219, 36); // Yellow color of center letter
    private static final int CELL_WIDTH = 80; // Width of each cell
    private static final int HONEYCOMB_X = 200; 
	private static final int HONEYCOMB_Y = 200;
    private final Font font = new Font("Helvetica Neue", Font.BOLD, 20);
    
    private SpellingBeeGUI game;
    private List <HoneycombCell> cells = new ArrayList<>();
    
    private Honeycomb honeycomb;
    
    public HoneycombView(Honeycomb honeycomb, SpellingBeeGUI game) throws IOException {
        this.honeycomb = honeycomb;
        this.game = game;

        honeycomb.addListener(this);
        
        Dimension preferredSize = new Dimension(400, 400);
        setPreferredSize(preferredSize);
        
        // add mouse listeners to cells in honeycomb
        addMouseListener(new HoneycombMouseListener());
        update();
    }
    
    /**
     * Draw the cells of the honeycomb 
     * so that there is one center cell, and 6 radiating cells
     * in a hexagonal shape
     */
    private void drawCells() {
    	
        int radius = CELL_WIDTH / 2; // Radius of the circumscribed circle
        int sideLength = (int)(radius * Math.cos(Math.PI / 6)); // The length of each side of the hexagon

        // Coordinates for the center cell
        int x = HONEYCOMB_X;
        int y = HONEYCOMB_Y;
        
        // Create and add the center cell
        Polygon centerHexagon = createHexagon(x, y, radius);
        HoneycombCell centerCell = new HoneycombCell(centerHexagon, honeycomb.getCenter(), CENTER_COLOR);
        cells.add(centerCell);
        
        // Calculate positions of the surrounding cells
        for (int i = 0; i < honeycomb.getOtherLetters().size(); i++) {
            double angle = 2 * Math.PI / 6 * i;
            int dx = (int) (Math.sin(angle) * (sideLength + radius));
            int dy = (int) (Math.cos(angle) * (sideLength + radius));

            // The y-coordinates are inverted because the y-axis is flipped
            int cellX = x + dx;
            int cellY = y - dy;

            Polygon hexagon = createHexagon(cellX, cellY, radius);
            HoneycombCell cell = new HoneycombCell(hexagon, honeycomb.getOtherLetters().get(i), CELL_COLOR);
            cells.add(cell);
        }
    }


    
    
    /**
     * Generate a hexagon shape
     * @param center x
     * @param center y
     * @param radius
     */
    private Polygon createHexagon(int centerX, int centerY, int radius) {
        Polygon hexagon = new Polygon();
        for (int i = 0; i < 6; i++) {
            int x = (int) (centerX + radius * Math.cos(i * 2 * Math.PI / 6));
            int y = (int) (centerY + radius * Math.sin(i * 2 * Math.PI / 6));
            hexagon.addPoint(x, y);
        }
        return hexagon;
    }

    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Set the background of the panel to white
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw each cell
        for (HoneycombCell cell : cells) {
        	
        	Polygon p = cell.getPolygon(); // hex shape of cell
        	String cellValue = cell.getCellValue().toUpperCase(); // convert letter to uppercase
        	Color cellColor = cell.getCellColor(); 
        	
        	g.setColor(cellColor);
        	g.fillPolygon(p);
        	
        	// Draw the letter in the cell
        	g.setColor(Color.BLACK);
            FontMetrics metrics = g.getFontMetrics(font);
            cellValue = cellValue.toUpperCase();
            int stringWidth = metrics.stringWidth(cellValue);
            int stringHeight = metrics.getHeight();
            int letterX = p.getBounds().x + (p.getBounds().width - stringWidth) / 2;
            int letterY = p.getBounds().y + (p.getBounds().height + stringHeight) / 2;

            g.setFont(font); // Set the font before drawing the string
            g.drawString(cellValue, letterX, letterY);
        }
    }
    
    /**
     * Mouse listener for the honeycomb
     * detects clicks within the cells and adds the letter to the game's guess field
     */
	class HoneycombMouseListener extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			
			// check if the user's click is in one of the honeycomb's cells
			for (HoneycombCell cell : cells) {
				if (cell.getPolygon().contains(x, y)) {
					game.addLetterToGuessField(cell.getCellValue());
					break;
				}
			}
		}
	}
	
	/**
	 * repaints the honeycomb when it changes (shuffling)
	 */
	public void update() {
		cells.clear();
		drawCells();
		repaint();
	}
	
}

