package lab4.data;

import java.util.Observable;
import java.util.Observer;

import lab4.client.GomokuClient;

/**
 * Represents the state of a game
 * 
 * @author Rickard Bemm
 */
public class GomokuGameState extends Observable implements Observer {

	// Game variables
	private final int DEFAULT_SIZE = 15;
	private GameGrid gameGrid;
	private GomokuClient client;
	private String message;

	// Possible game states
	private enum GameStates {
		NOT_STARTED, MY_TURN, OTHERS_TURN, FINISHED
	}

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
		boolean isMyTurn = currentState == GameStates.MY_TURN;
		boolean tileIsEmpty = gameGrid.getLocation(x, y) == GameGrid.EMPTY;

		if (isMyTurn && tileIsEmpty) {
			client.sendMoveMessage(x, y);
			gameGrid.move(x, y, GameGrid.ME);
			if (gameGrid.isWinner(GameGrid.ME)) {
				currentState = GameStates.FINISHED;
				message = "Congratulations, you have won!";
			}
		} else {
			if (!isMyTurn) {
				message = "You have to wait for your turn";
			} else if (!tileIsEmpty) {
				message = "Sorry, but the tile is already occupied";
			}
		}

		setChanged();
		notifyObservers();
	}

	/**
	 * Starts a new game with the current client
	 */
	public void newGame() {
		client.sendNewGameMessage();
		gameGrid.clearGrid();
		currentState = GameStates.OTHERS_TURN;
		message = "The game has been reset";

		setChanged();
		notifyObservers();
	}

	/**
	 * Other player has requested a new game, so the game state is changed
	 * accordingly
	 */
	public void receivedNewGame() {
		gameGrid.clearGrid();
		currentState = GameStates.MY_TURN;
		message = "A new game has been received";

		setChanged();
		notifyObservers();
	}

	/**
	 * The connection to the other player is lost, so the game is interrupted
	 */
	public void otherGuyLeft() {
		gameGrid.clearGrid();
		currentState = GameStates.NOT_STARTED;
		message = "Your opponent left";

		setChanged();
		notifyObservers();
	}

	/**
	 * The player disconnects from the client
	 */
	public void disconnect() {
		client.disconnect();
		gameGrid.clearGrid();
		currentState = GameStates.NOT_STARTED;
		message = "You are now diconnected";

		setChanged();
		notifyObservers();
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
			currentState = GameStates.FINISHED;
			message = "You lose, better luck next time!";
		} else {
			currentState = GameStates.MY_TURN;
			message = "It is you turn";
		}
	}
	
	public void update(Observable o, Object arg) {

		switch (client.getConnectionStatus()) {
		case GomokuClient.CLIENT:
			message = "Game started, it is your turn!";
			currentState = GameStates.MY_TURN;
			break;
		case GomokuClient.SERVER:
			message = "Game started, waiting for other player...";
			currentState = GameStates.OTHERS_TURN;
			break;
		}

		setChanged();
		notifyObservers();
	}

}
