package lab4.data;

import java.util.ArrayList;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class GameGrid extends Observable {

	public static final int EMPTY = 0;
	public static final int ME = 1;
	public static final int OTHER = 2;
	private ArrayList<int[]> xCord;
	private final int INROW = 5; // Win condition.

	public GameGrid(int n) {
		xCord = new ArrayList<int[]>();
		for (int q = 0; q < n; q++) {
			int[] yCord = new int[n];
			for (int i = 0; i < n; i++) {
				yCord[i] = EMPTY;

			}
			xCord.add(yCord); /// sets every slot of the y axis to the same size
								/// of the x axis. ie a nxn square space.

		}
	}

	public int getLocation(int x, int y) {
		return xCord.get(x)[y];
	}

	public int getSize() {
		return xCord.size() * xCord.size();
	}

	public boolean move(int x, int y, int player) {
		if (xCord.get(x)[y] == EMPTY) {
			xCord.get(x)[y] = player;
			setChanged();
			notifyObservers();
			return true;
		}
		setChanged();
		notifyObservers();
		return false;
	}
	//ska den starta ett nytt spel med samma grid som n x n eller ?
	public void clearGrid() {
		for (int[] x : xCord) {
			for (int i = 0; i < x.length; i++) {
				x[i] = EMPTY;
			}
		}
	}

	// Bˆrjar kolla efter index som ‰r vin fˆrhÂllandet.//mÂste t‰nka om i fall
	// dÂ spelplanen ‰r stor som fan.
	private boolean checkDiagonal(int player) {
		/// kollar diagonal --->>>> (ˆver halva)
		for (int n = 0; n < xCord.size(); n++) {
			int score = 0;
			for (int i = 0; i < xCord.size(); i++) {
				if (n + i < xCord.size()) {
					if (xCord.get(n + i)[n + i] == player) {
						score += 1;
					} else {
						score = 0;
					}
					if (score == INROW) {
						return true;
					}
				} else {
					continue;
				}
			}
		}

		// ->>>> nedre halva.
		for (int n = 0; n < xCord.size(); n++) {
			int score = 0;
			for (int i = 0; i < xCord.size(); i++) {
				if (n + i + 1 < xCord.size()) {
					if (xCord.get(i)[n + i + 1] == player) {
						score += 1;
					} else {
						score = 0;
					}
					if (score == INROW) {
						return true;
					}
				} else {
					continue;
				}
			}
		}
		/// <<<<----ˆvre halvan
		for (int n = 0; n < xCord.size() - (INROW - 1); n++) {
			int score = 0;
			for (int i = 0; i < xCord.size() - (INROW - 1); i++) {
				if (n + i < xCord.size()) {
					if (xCord.get(xCord.size() - (n + i))[xCord.size() - (n + i)] == player) {
						score += 1;
					} else {
						score = 0;
					}
					if (score == INROW) {
						return true;
					}
				}
			}

		}

		/// <<<<---- nedre halvan
		for (int n = 0; n < xCord.size() - INROW; n++) {
			int score = 0;
			for (int i = 0; i < xCord.size() - INROW; i++) {
				if (xCord.get(xCord.size() - (i))[xCord.size() - (n + i + 1)] == player) {
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

	private boolean checkVertical(int player) {
		for (int[] x : xCord) {
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

	private boolean checkHorizontal(int player) {
		for (int i = 0; i < xCord.size(); i++) {
			int score = 0;
			for (int q = 0; q < xCord.size(); q++) {
				if (xCord.get(q)[i] == player) {
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

	public boolean isWinner(int player) {
		if (checkDiagonal(player) || checkVertical(player) || checkHorizontal(player)) {
			return true;
		}
		return false;
	}
	
	
	
	public static void main(String[] args){
		
		GameGrid test = new GameGrid(8);
		System.out.println(test.getSize());
		System.out.println(test.getLocation(4, 4));
		test.move(4, 4, 2);
		System.out.println(test.getLocation(4, 4));
		
		
	}
	
	
	
	
	
	
	
	
	
	
}