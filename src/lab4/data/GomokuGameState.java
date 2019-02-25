package lab4.data;

import java.util.Observable;
import java.util.Observer;

import lab4.client.GomokuClient;

/**
 * Represents the state of a game
 * 
 * @author Philip Eriksson
 * @author Rickard Bemm
 */
public class GomokuGameState extends Observable implements Observer {

	// Game variables
	private final int DEFAULT_SIZE = 20;
	private GameGrid gameGrid;
	private GomokuClient client;
	private String message;

	// Possible game states
	private enum GameStates {
		NOT_STARTED, MY_TURN, OTHERS_TURN, FINISHED
	}

	// Current game state
	private GameStates currentState;

	/**
	 * The constructor
	 * 
	 * @param gc The client used to communicate with the other player
	 */
	public GomokuGameState(GomokuClient gc) {
		this.client = gc;
		this.client.addObserver(this);
		this.currentState = GameStates.NOT_STARTED;
		this.gameGrid = new GameGrid(DEFAULT_SIZE);
		gc.setGameState(this);
	}

	/**
	 * Returns the message string
	 * 
	 * @return the message string
	 */
	public String getMessageString() {
		return this.message;
	}

	/**
	 * Returns the game grid
	 * 
	 * @return the game grid
	 */
	public GameGrid getGameGrid() {
		return this.gameGrid;
	}

	/**
	 * This player makes a move at a specified location
	 * 
	 * @param x the x coordinate on game grid
	 * @param y the y coordinate on game grid
	 */
	public void move(int x, int y) {
		if (currentState == GameStates.NOT_STARTED) {
			changeGameMessage("Game not started!");
		} else if (currentState == GameStates.FINISHED) {
			changeGameMessage("The game has ended");
		} else {
			boolean isMyTurn = currentState == GameStates.MY_TURN;
			boolean tileIsEmpty = gameGrid.getLocation(x, y) == GameGrid.EMPTY;

			if (isMyTurn && tileIsEmpty) {
				if (gameGrid.move(x, y, GameGrid.ME)) {
					client.sendMoveMessage(x, y);
					changeGameState(GameStates.OTHERS_TURN,
							"Waiting for the opponent's move");
				}
				if (gameGrid.isWinner(GameGrid.ME)) {
					changeGameState(GameStates.FINISHED,
							"Congratulations, you have won!");
				}
			} else {
				if (!isMyTurn) {
					changeGameMessage("You have to wait for your turn");
				} else if (!tileIsEmpty) {
					changeGameMessage("The tile is already occupied");
				}
			}
		}
	}

	/**
	 * Starts a new game with the current client
	 */
	public void newGame() {
		client.sendNewGameMessage();
		gameGrid.clearGrid();
		changeGameState(GameStates.OTHERS_TURN, "The game has been reset");
	}

	/**
	 * Other player has requested a new game, so the game state is changed
	 * accordingly
	 */
	public void receivedNewGame() {
		gameGrid.clearGrid();
		changeGameState(GameStates.MY_TURN, "A new game has been received");
	}

	/**
	 * The connection to the other player is lost, so the game is interrupted
	 */
	public void otherGuyLeft() {
		gameGrid.clearGrid();
		changeGameState(GameStates.NOT_STARTED, "Your opponent left");
	}

	/**
	 * The player disconnects from the client
	 */
	public void disconnect() {
		client.disconnect();
		gameGrid.clearGrid();
		changeGameState(GameStates.NOT_STARTED, "You are now diconnected");
	}

	/**
	 * The player receives a move from the other player
	 * 
	 * @param x The x coordinate of the move
	 * @param y The y coordinate of the move
	 */
	public void receivedMove(int x, int y) {
		gameGrid.move(x, y, GameGrid.OTHER);

		if (gameGrid.isWinner(GameGrid.OTHER)) {
			changeGameState(GameStates.FINISHED,
					"You lose, better luck next time!");
		} else {
			changeGameState(GameStates.MY_TURN, "It is your turn");
		}
	}

	/**
	 * This method is called whenever the observed object is changed.
	 * Anapplication calls an Observable object's notifyObservers method to have
	 * all the object'sobservers notified of the change.
	 * 
	 * @param o the observable object.
	 * @param arg an argument passed to the notifyObserversmethod.
	 */
	public void update(Observable o, Object arg) {

		switch (client.getConnectionStatus()) {
		case GomokuClient.CLIENT:
			changeGameState(GameStates.MY_TURN,
					"Game started, it is your turn!");
			break;
		case GomokuClient.SERVER:
			changeGameState(GameStates.OTHERS_TURN,
					"Game started, waiting for other player...");
			break;
		}
	}

	/**
	 * Change state of game and change message for user to view.
	 * 
	 * @param state   New state of game
	 * @param message Message for user to view
	 */
	private void changeGameState(GameStates state, String message) {
		this.currentState = state;
		this.message = message;

		setChanged();
		notifyObservers();
	}

	/**
	 * Change message for user to view.
	 * 
	 * @param message Message for user to view
	 */
	private void changeGameMessage(String message) {
		this.message = message;

		setChanged();
		notifyObservers();
	}

}
