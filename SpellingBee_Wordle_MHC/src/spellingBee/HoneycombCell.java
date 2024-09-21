/**
 * MHC CS-225 - Final Project
 * @author Anna Dai, Max Endieveri, Nancy Jie, Tristy Huang
 * @version 5/2024
 */
package spellingBee;

import java.awt.Color;
import java.awt.Polygon;

/**
 * Cell class for building each cell in the honeycomb view
 * Each cell has a shape, letter value, and color
 */
public class HoneycombCell {
	
	private Polygon polygon;
	private String cellValue;
	private Color cellColor;
	
	public HoneycombCell(Polygon polygon, String cellValue, Color cellColor) {
		this.polygon = polygon;
		this.cellValue = cellValue;
		this.cellColor = cellColor;
	}
	
	public Polygon getPolygon() {
		return this.polygon;
	}
	
	public Color getCellColor() {
		return this.cellColor;
	}
	
	public String getCellValue() {
		return this.cellValue;
	}

}
