package lab4.data;

import java.util.Observable;



/**
 * 
 * The playing field for Gomoku.
 * 
 * @author Philip Eriksson
 * @author Rickard Bemm
 *
 */
public class GameGrid extends Observable {

	public static final int EMPTY = 0;
	public static final int ME = 1;
	public static final int OTHER = 2;

	/**
	 * Grid data with [x, y] layout
	 */
	private int[][] cord;
	private final int size;
	private final int INROW = 3; // Win condition.

	/**
	 * Construct a new instance of a {@code GameGrid} object
	 * 
	 * @param n size for the game grid in x axis and y axis
	 */
	public GameGrid(int n) {
		this.size = n;
		this.cord = new int[n][];
		for (int q = 0; q < n; q++) {
			int[] tempCord = new int[n];
			for (int i = 0; i < n; i++) {
				tempCord[i] = EMPTY;

			}
			this.cord[q] = tempCord;

		}
	}

	/**
	 * Get the current occupant in grid by {@code x} and {@code y} coordinate
	 * 
	 * @param x x coordinate in grid
	 * @param y y coordinate in grid
	 * 
	 * @return The current occupant of this grid.
	 */

	public int getLocation(int x, int y) {
		return this.cord[x][y];
	}

	/**
	 * Get length and width of the game grid
	 * 
	 * @return The length and width of the game grid.
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * 
	 * Moves a player to the desired grid position if it's empty.
	 * 
	 * @param x      x coordinate in grid
	 * @param y      y coordinate in grid
	 * @param player player to move
	 * 
	 * @return true if desired position was empty else false.
	 */
	public boolean move(int x, int y, int player) {
		if (x < 0 || x >= this.size || y < 0 || y >= this.size) {
			return false;
		} else if (this.cord[x][y] == EMPTY) {
			this.cord[x][y] = player;
			setChanged();
			notifyObservers();
			return true;
		}
		return false;
	}

	/**
	 * Resets the game grid when new game is called.
	 * 
	 */

	public void clearGrid() {
		for (int[] x : this.cord) {
			for (int i = 0; i < this.size; i++) {
				x[i] = EMPTY;
			}
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * Checks win condition for player diagonally from east to west.
	 * 
	 * @param player current player
	 * @return true if win condition met for player diagonally from east to west
	 *         else false.
	 */
	private boolean checkDiagonal(int player) {
		for (int x = 0; x < cord.length; x++) {
			for (int y = 0; y < cord.length - INROW + 1; y++) {
				int scoreSE = 0;
				int scoreSW = 0;
				for (int i = 0; i < INROW; i++) {
					if (x + i < cord.length) {
						if (cord[x + i][y + i] == player) {
							scoreSE++;
						}
					}
					if (0 <= x - i) {
						if (cord[x - i][y + i] == player) {
							scoreSW++;
						}
					}
				}
				if (scoreSE == INROW || scoreSW == INROW) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks win condition for player vertically.
	 * 
	 * @param player
	 * @return true if win condition met vertically, else false.
	 */
	private boolean checkVertical(int player) {
		for (int[] coloumns : cord) {
			int score = 0;
			for (int cell : coloumns) {
				if (cell == player) {
					score++;
				} else {
					score = 0; // Resets the score
				}
				if (score == INROW) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks win condition for player horizontally.
	 * 
	 * @param player
	 * @return true if win condition met horizontally, else false.
	 */
	private boolean checkHorizontal(int player) {
		for (int y = 0; y < this.size; y++) {
			int score = 0;
			for (int x = 0; x < this.size; x++) {
				if (this.cord[x][y] == player) {
					score += 1;
				} else {
					score = 0; // Resets the score
				}
				if (score == INROW) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if players has met any of the win conditions.
	 * 
	 * @param player for this player.
	 */
	public boolean isWinner(int player) {
		return checkVertical(player) || checkHorizontal(player)
				|| checkDiagonal(player);
	}

}