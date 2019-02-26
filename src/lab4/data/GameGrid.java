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
	private final int INROW = 5; // Win condition.
	private int[] lastAdded = new int[2]; // Most recent node placed.

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
		if (this.cord[x][y] == EMPTY) {
			this.cord[x][y] = player;
			lastAdded[0] = x; // Most recent node x position.
			lastAdded[1] = y; // Most recent node y position.
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
	 * Checks win condition for player diagonally.
	 * 
	 * @param player current player
	 * @return true if win condition met for player diagonally else false.
	 */
	private boolean checkDiagonal(int player) {
		int score = 1;
		int tempCord;

		//Diagonal -->. NW -> SE
		for (int y = 1; y < INROW; y++) {
			if (lastAdded[0] + y >= this.size || lastAdded[1] + y >= this.size) {
				break;
			} else {
				tempCord = this.cord[y + lastAdded[0]][y + lastAdded[1]];
				if (tempCord == player) {
					score += 1;
				} else {
					break;
				}
				if (score == INROW) {
					return true;
				}

			}
		}
		//Diagonal <--. SE -> NW
		for (int y2 = 1; y2 < INROW; y2++) {
			if (lastAdded[0] - y2 < 0 || lastAdded[1] - y2 < 0) {
				break;
			} else {
				tempCord = this.cord[lastAdded[0] - y2][lastAdded[1] - y2];
				if (tempCord == player) {
					score += 1;
				} else {
					score = 1;
					break;
				}
				if (score == INROW) {
					return true;
				}

			}
		}
		//Diagonal -->. SW -> NE
		for (int y3 = 1; y3 < INROW; y3++) {
			if (lastAdded[0] + y3 >= this.size || lastAdded[1] - y3 < 0) {
				break;
			} else {
				tempCord = this.cord[lastAdded[0] + y3][lastAdded[1] - y3];
				if (tempCord == player) {
					score += 1;
				} else {
					break;
				}
				if (score == INROW) {
					return true;
				}

			}
		}
		//Diagonal <--. NE -> SW
		for (int y4 = 1; y4 < INROW; y4++) {
			if (lastAdded[0] - y4 < 0 || lastAdded[1] + y4 >= this.size) {
				break;
			} else {
				tempCord = this.cord[lastAdded[0] - y4][lastAdded[1] + y4];
				if (tempCord == player) {
					score += 1;
				} else {
					return false;
				}
				if (score == INROW) {
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
		int score = 1;
		int tempGrid;

		for (int y = 1; y < INROW; y++) {
			if (lastAdded[1] + y >= this.size) {
				break;
			} else {
				tempGrid = this.cord[lastAdded[0]][lastAdded[1] + y];
				if (tempGrid == player) {
					score += 1;
				} else {
					break;
				}
				if (score == INROW) {
					return true;
				}

			}
		}
		for (int y2 = 1; y2 < INROW; y2++) {
			if (lastAdded[1] - y2 < 0) {
				break;
			} else {
				tempGrid = this.cord[lastAdded[0]][lastAdded[1] - y2];
				if (tempGrid == player) {
					score += 1;
				} else {
					return false;
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
		int score = 1;
		int tempGrid;

		for (int x = 1; x < INROW; x++) {
			if (x + lastAdded[0] >= this.size) {
				break;
			} else {
				tempGrid = this.cord[lastAdded[0] + x][lastAdded[1]];
				if (tempGrid == player) {
					score += 1;
				} else {
					break;
				}
				if (score == INROW) {
					return true;
				}

			}
		}

		for (int x2 = 1; x2 < INROW; x2++) {
			if (lastAdded[0] - x2 < 0) {
				break;
			} else {
				tempGrid = this.cord[lastAdded[0] - x2][lastAdded[1]];
				if (tempGrid == player) {
					score += 1;
				} else {
					return false;
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
		return checkVertical(player) || checkHorizontal(player) || checkDiagonal(player);
	}

}