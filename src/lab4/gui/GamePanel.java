package lab4.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import lab4.data.GameGrid;

/**
 * A panel providing a graphical view of the game board
 */

public class GamePanel extends JPanel implements Observer {

	private static final int UNIT_SIZE = 20;
	private GameGrid grid;

	/**
	 * The constructor
	 * 
	 * @param grid The grid that is to be displayed
	 */
	public GamePanel(GameGrid grid) {

		this.grid = grid;
		grid.addObserver(this);
		Dimension d = new Dimension(grid.getSize() * UNIT_SIZE + 1,
				grid.getSize() * UNIT_SIZE + 1);
		this.setMinimumSize(d);
		this.setPreferredSize(d);
		this.setBackground(Color.WHITE);
	}

	/**
	 * Returns a grid position given pixel coordinates of the panel
	 * 
	 * @param x the x coordinates
	 * @param y the y coordinates
	 * @return an integer array containing the [x, y] grid position
	 */
	public int[] getGridPosition(int x, int y) {
		int[] tempGrid = new int[2];
		tempGrid[0] = x * UNIT_SIZE;
		tempGrid[1] = y * UNIT_SIZE;
		return tempGrid;
	}

	public void update(Observable arg0, Object arg1) {
		this.repaint();
	}

	/**
	 * 
	 * Paints each grid square of the game grid and paints an oval in the grid
	 * square if it's occupied.
	 * 
	 * 
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int x = 0; x < grid.getSize(); x++) {
			for (int y = 0; y < grid.getSize(); y++) {
				int sX = getGridPosition(x, y)[0];
				int sY = getGridPosition(x, y)[1];

				g.setColor(Color.BLACK);
				g.drawRect(sX, sY, UNIT_SIZE, UNIT_SIZE);

				if (grid.getLocation(x, y) == GameGrid.ME) {
					g.setColor(Color.RED);
					g.fillOval(sX, sY, UNIT_SIZE, UNIT_SIZE);
					g.setColor(Color.BLACK);
					g.drawOval(sX, sY, UNIT_SIZE, UNIT_SIZE);

				} else if (grid.getLocation(x, y) == GameGrid.OTHER) {
					g.setColor(Color.BLUE);
					g.fillOval(sX, sY, UNIT_SIZE, UNIT_SIZE);
					g.setColor(Color.BLACK);
					g.drawOval(sX, sY, UNIT_SIZE, UNIT_SIZE);
				}
			}
		}
	}
	
	/**
	 * Get size of each cell in the grid.
	 * @return Size of each cell in the grid.
	 */
	public static int getGridSize() {
		return UNIT_SIZE;
	}

}
