package lab4.data;

import java.util.Observable;

@SuppressWarnings("deprecation")
/**
 * 
 * The playing field for gomoku.
 * 
 * @author Bernstgunnar
 *
 */

public class GameGrid extends Observable {

	public static final int EMPTY = 0;
	public static final int ME = 1;
	public static final int OTHER = 2;
	private int[][] cord;
	private final int size;
	private final int INROW = 3; // Win condition.

	/**
	 * 
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
	 * 
	 * @param x axis
	 * 
	 * @param y axis
	 * 
	 * @return The current occupant of this grid.
	 */

	public int getLocation(int x, int y) {
		return this.cord[x][y];
	}

	/**
	 * 
	 * @return The total size of the game grid.
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * 
	 * Moves a player to the desired grid position if it's empty.
	 * 
	 * @param x axis
	 * 
	 * @param y axis
	 * 
	 * @param player current player.
	 * 
	 * @return true if desired position was empty else false.
	 */
	public boolean move(int x, int y, int player) {
		if(x < 0 || x >= this.size ||
			y < 0 || y >= this.size){
			return false;
		}else if(this.cord[x][y] == EMPTY) {
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
	 * Checks win condition for player diagonally from west to east.
	 * 
	 * @param player current player
	 * @return true if win condition met for player diagonally from west to east
	 *         else false.
	 */
	private boolean diagonalWestToEast(int player) {
		// Upper half.
		for (int n = 0; n < this.size; n++) {
			int score = 0;
			int i = 0;
			while (n + i < this.size) {
				if (this.cord[n + i][i] == player) {
					score += 1;
				} else {
					score = 0;
				}
				if (score == INROW) {
					return true;
				}
				i += 1;
			}
		}

		// Lower half.
		for (int n = 0; n < this.size; n++) {
			int score = 0;
			int i = 0;
			while (n + i < this.size) {
				if (this.cord[i][n + i] == player) {
					score += 1;
				} else {
					score = 0;
				}
				if (score == INROW) {
					return true;
				}
				i += 1;
			}
		}
		return false;
	}

	/**
	 * Checks win condition for player diagonally from east to west.
	 * 
	 * @param player
	 *            current player
	 * @return true if win condition met for player diagonally from east to west
	 *         else false.
	 */
	private boolean diagonalEastToWest(int player) {

		/// Upper half.
		for (int n = 0; n < this.size; n++) {
			int score = 0;
			int i = 0;
			while (n + i < this.size) {
				if (this.cord[(this.size - 1) - (n + i)][i] == player) {
					score += 1;
				} else {
					score = 0;
				}
				if (score == INROW) {
					return true;
				}
				i += 1;
			}
		}

		// Lower half.
		for (int n = 0; n < this.size; n++) {
			int score = 0;
			int i = 0;
			while (n + i < this.size) {
				if (this.cord[(this.size - 1) - i][n + i] == player) {
					score += 1;
				} else {
					score = 0;
				}
				if (score == INROW) {
					return true;
				}
				i += 1;
			}

		}

		return false;
	}

	/**
	 * Checks win condition for player diagonally.
	 * 
	 * @param player
	 * @return true if win condition met for player diagonally.
	 */
	private boolean checkDiagonal(int player) {
		if (diagonalWestToEast(player) || diagonalEastToWest(player)) {
			return true;
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
		for (int[] x : this.cord) {
			int score = 0;
			for (int i = 0; i < x.length; i++) {
				if (x[i] == player) {
					score += 1;
				} else {
					score = 0; // resets the score.
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
		for (int i = 0; i < this.size; i++) {
			int score = 0;
			for (int q = 0; q < this.size; q++) {
				if (this.cord[q][i] == player) {
					score += 1;
				} else {
					score = 0;
				}
				if (score == INROW) {
					return true;
				}
			}
		}
		return false;

	}

	/*
	 * Checks if players has met any of the win conditions.
	 * 
	 * @param player for this player.
	 */
	public boolean isWinner(int player) {
		if (checkDiagonal(player) || checkVertical(player) || checkHorizontal(player)) {
			return true;
		}
		return false;
	}

}