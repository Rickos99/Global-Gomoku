package lab4.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import lab4.client.GomokuClient;
import lab4.data.GomokuGameState;

/**
 * The GUI class
 * 
 * @author Philip Eriksson
 * @author Rickard Bemm
 *
 * @see lab4.client.GomokuClient
 * @see lab4.client.Listener
 */
public class GomokuGUI implements Observer {

	private GomokuClient client;
	private GomokuGameState gamestate;

	// Buttons
	private JButton connectButton;
	private JButton newGameButton;
	private JButton disconnectButton;

	// Labels
	private JLabel messageLabel;

	// Panels
	private JPanel gameGridPanel;
	private JPanel buttonPanel;
	private JPanel messagePanel;

	/**
	 * The constructor
	 * 
	 * @param g The game state that the GUI will visualize
	 * @param c The client that is responsible for the communication
	 * 
	 * @see lab4.client.GomokuClient
	 * @see lab4.client.Listener
	 */
	public GomokuGUI(GomokuGameState g, GomokuClient c) {
		this.client = c;
		this.gamestate = g;
		client.addObserver(this);
		gamestate.addObserver(this);

		this.createButtons();
		this.createLabels();
		this.createPanels();
		this.createLayout();
	}

	/**
	 * This method is called whenever the observed object is changed.
	 * Anapplication calls an Observable object's notifyObservers method to have
	 * all the object'sobservers notified of the change.
	 * 
	 * @param o   the observable object.
	 * @param arg an argument passed to the notifyObserversmethod.
	 */
	public void update(Observable arg0, Object arg1) {

		// Update the buttons if the connection status has changed
		if (arg0 == client) {
			if (client.getConnectionStatus() == GomokuClient.UNCONNECTED) {
				connectButton.setEnabled(true);
				newGameButton.setEnabled(false);
				disconnectButton.setEnabled(false);
			} else {
				connectButton.setEnabled(false);
				newGameButton.setEnabled(true);
				disconnectButton.setEnabled(true);
			}
		}

		// Update the status text if the gamestate has changed
		if (arg0 == gamestate) {
			messageLabel.setText(gamestate.getMessageString());
		}

	}

	private void createButtons() {
		connectButton = new JButton("Connect");
		newGameButton = new JButton("New game");
		disconnectButton = new JButton("Disconnect");

		connectButton.setEnabled(true);
		newGameButton.setEnabled(false);
		disconnectButton.setEnabled(false);

		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ConnectionWindow(client);
			}
		});
		newGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gamestate.newGame();
			}
		});
		disconnectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gamestate.disconnect();
			}
		});
	}

	private void createLabels() {
		messageLabel = new JLabel("Welcome to Gomoku!");
	}

	private void createPanels() {
		gameGridPanel = new GamePanel(gamestate.getGameGrid());
		buttonPanel = new JPanel();
		messagePanel = new JPanel();

		gameGridPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				int[] coords = new GamePanel(gamestate.getGameGrid())
						.getGridPosition(e.getX(), e.getY());
				gamestate.move(coords[0], coords[1]);
			}
		});

		buttonPanel.add(connectButton);
		buttonPanel.add(newGameButton);
		buttonPanel.add(disconnectButton);

		messagePanel.add(messageLabel);
	}

	private void createLayout() {
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		c.gridy = 0;
		layout.setConstraints(gameGridPanel, c);

		c.gridy = 1;
		layout.setConstraints(buttonPanel, c);

		c.gridy = 2;
		layout.setConstraints(messagePanel, c);

		JPanel panel = new JPanel();
		panel.setLayout(layout);
		panel.add(gameGridPanel);
		panel.add(buttonPanel);
		panel.add(messagePanel);
		panel.setBorder(new EmptyBorder(20, 20, 20, 20));

		JFrame frame = new JFrame("Gomoku");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panel);
		
		frame.pack();
		frame.setVisible(true);
		frame.setMinimumSize(panel.getSize());
	
	}
}
