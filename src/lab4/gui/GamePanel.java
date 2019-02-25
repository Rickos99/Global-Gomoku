package lab4.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import lab4.data.GameGrid;

/**
 * A {@code JPanel} providing a graphical view of the game board.
 * 
 * @author Bernstgunnar
 * @author Rickard Bemm
 * 
 */
public class GamePanel extends JPanel implements Observer {

	private final int UNIT_SIZE = 20;
	private GameGrid grid;

	/**
	 * Constructs a {@code GamePanel} and initializes it to use
	 * {@code GameGrid grid}
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
	 * @param x The x coordinates on screen
	 * @param y The y coordinates on screen
	 * @return An integer array containing the [x, y] grid position
	 */
	public int[] getGridPosition(int x, int y) {
		int[] tempGrid = { (int) Math.floor(x / UNIT_SIZE),
				(int) Math.floor(y / UNIT_SIZE) };
		return tempGrid;
	}

	/**
	 * This method is called whenever the observed object is changed.
	 * Anapplication calls an Observable object's notifyObservers method to have
	 * all the object'sobservers notified of the change.
	 * 
	 * @param o the observable object.
	 * @param arg an argument passed to the notifyObserversmethod.
	 */
	public void update(Observable arg0, Object arg1) {
		this.repaint();
	}

	/**
	 * Paints each grid square of the game grid and paints an oval in the grid
	 * square if it's occupied.
	 * 
	 * @param g graphics object to paint on
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int x = 0; x < grid.getSize(); x++) {
			for (int y = 0; y < grid.getSize(); y++) {

				g.setColor(Color.BLACK);
				g.drawRect(x * UNIT_SIZE, y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);

				if (grid.getLocation(x, y) == GameGrid.ME) {
					g.setColor(Color.RED);
					g.fillOval(x * UNIT_SIZE, y * UNIT_SIZE, UNIT_SIZE,
							UNIT_SIZE);
					g.setColor(Color.BLACK);
					g.drawOval(x * UNIT_SIZE, UNIT_SIZE * y, UNIT_SIZE,
							UNIT_SIZE);

				} else if (grid.getLocation(x, y) == GameGrid.OTHER) {
					g.setColor(Color.BLUE);
					g.fillOval(x * UNIT_SIZE, y * UNIT_SIZE, UNIT_SIZE,
							UNIT_SIZE);
					g.setColor(Color.BLACK);
					g.drawOval(x * UNIT_SIZE, y * UNIT_SIZE, UNIT_SIZE,
							UNIT_SIZE);
				}
			}
		}
	}

}
