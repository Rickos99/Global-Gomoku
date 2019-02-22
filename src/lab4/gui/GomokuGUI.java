package lab4.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lab4.client.GomokuClient;
import lab4.data.GomokuGameState;

/*
 * The GUI class
 */

public class GomokuGUI implements Observer{
	
	public static void main(String[] args) {
		GomokuClient gc = new GomokuClient(2000);
		GomokuGameState gs = new GomokuGameState(gc);
		new GomokuGUI(gs, gc).createLayout();
	}

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
	 * @param g   The game state that the GUI will visualize
	 * @param c   The client that is responsible for the communication
	 */
	public GomokuGUI(GomokuGameState g, GomokuClient c){
		this.client = c;
		this.gamestate = g;
		client.addObserver(this);
		gamestate.addObserver(this);
	}
	
	
	public void update(Observable arg0, Object arg1) {
		
		// Update the buttons if the connection status has changed
		if(arg0 == client){
			if(client.getConnectionStatus() == GomokuClient.UNCONNECTED){
				connectButton.setEnabled(true);
				newGameButton.setEnabled(false);
				disconnectButton.setEnabled(false);
			}else{
				connectButton.setEnabled(false);
				newGameButton.setEnabled(true);
				disconnectButton.setEnabled(true);
			}
		}
		
		// Update the status text if the gamestate has changed
		if(arg0 == gamestate){
			messageLabel.setText(gamestate.getMessageString());
		}
		
	}
	
	private void createLayout() {
		gameGridPanel = new GamePanel(gamestate.getGameGrid());
		buttonPanel = new JPanel();
		messagePanel = new JPanel();
		
		buttonPanel.add(new JButton("Connect"));
		
		Dimension dimension = new Dimension(200, 400);
		gameGridPanel.setSize(dimension);
		buttonPanel.setSize(dimension);
		messagePanel.setSize(dimension);
		
		gameGridPanel.setBackground(Color.RED);
		buttonPanel.setBackground(Color.ORANGE);
		messagePanel.setBackground(Color.GREEN);
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		layout.setConstraints(gameGridPanel, c);
		
		c.gridx = 0;
		c.gridy = 1;
		layout.setConstraints(buttonPanel, c);
		
		c.gridx = 0;
		c.gridy = 2;
		layout.setConstraints(messagePanel, c);
		
		JPanel panel = new JPanel();
		panel.setLayout(layout);
		panel.add(gameGridPanel);
		panel.add(buttonPanel);
		panel.add(messagePanel);
		
		JFrame frame = new JFrame("GUI");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
