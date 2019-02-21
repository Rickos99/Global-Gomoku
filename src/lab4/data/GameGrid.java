package lab4.data;

import java.util.Observable;

@SuppressWarnings("deprecation")
public class GameGrid extends Observable {

	public static final int EMPTY = 0;
	public static final int ME = 1;
	public static final int OTHER = 2;
	private int[][] cord;
	private final int size;
	private final int INROW = 3; // Win condition.

	public GameGrid(int n) {
		this.size = n;
		this.cord = new int[n][];
		for (int q = 0; q < n; q++) {
			int[] tempCord = new int[n];
			for (int i = 0; i < n; i++) {
				tempCord[i] = EMPTY;

			}
			this.cord[q] = tempCord; /// sets every slot of the y axis to the
										/// same
			/// size
			/// of the x axis. ie a nxn square space.

		}
	}

	public int getLocation(int x, int y) {
		return this.cord[x][y];
	}

	public int getSize() {
		return this.size * this.size;
	}

	public boolean move(int x, int y, int player) {
		try {
			if (this.cord[x][y] == EMPTY) {
				this.cord[x][y] = player;
				setChanged();
				notifyObservers();
				return true;
			}
			setChanged();
			notifyObservers();
			return false;
		} catch (ArrayIndexOutOfBoundsException e) {
			//
			setChanged();
			notifyObservers();
			return false;
		}
	}

	public void clearGrid() {
		for (int[] x : this.cord) {
			for (int i = 0; i < this.size; i++) {
				x[i] = EMPTY;
			}
		}
	}

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
			System.out.println("C");
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

	private boolean checkDiagonal(int player) {
		if (diagonalWestToEast(player) || diagonalEastToWest(player)) {
			return true;
		}
		return false;
	}

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
					System.out.println("vertical");
					return true;
				}
			}
		}
		return false;
	}

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
					System.out.println("horizontal");
					return true;
				}
			}
		}
		return false;

	}

	public boolean isWinner(int player) {
		if (checkDiagonal(player) || checkVertical(player) || checkHorizontal(player)) {
			return true;
		}
		return false;
	}

}