package lab4.gui;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import lab4.client.GomokuClient;
import lab4.data.GomokuGameState;

/*
 * The GUI class
 */

public class GomokuGUI implements Observer{
	
	public static void main(String[] args) {
		GomokuClient gc = new GomokuClient(2222);
		GomokuGameState gs = new GomokuGameState(gc);
		GomokuClient gc2 = new GomokuClient(3333);
		GomokuGameState gs2 = new GomokuGameState(gc);
		GomokuGUI test = new GomokuGUI(gs,gc);
		GomokuGUI testTwo = new GomokuGUI(gs2,gc2);

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


	
	// Frame
	private JFrame frame;
	
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
		
		
		try{

			//Status label.
			messageLabel = new JLabel("Welcome.");
			
			//Creates gomoku field.
			gameGridPanel = new GamePanel(gamestate.getGameGrid());
			gameGridPanel.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e){
					//Ska börja fixa musklickaren en snabbis! Låt den vara åt mig. //pe
					int tempX = (int)Math.floor(e.getX()/GamePanel.UNIT_SIZE);
					int tempY = (int)Math.floor(e.getY()/GamePanel.UNIT_SIZE);
					System.out.println("x value = "+tempX + " y value = "+tempY);
					g.move(tempX, tempY);
				}
				
			});
			//Connect button.
			connectButton = new JButton("Connect");
			connectButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					ConnectionWindow temp = new ConnectionWindow(client);
					
					
					
				}
				
			});
			//New game button.
			newGameButton = new JButton("New Game");
			newGameButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					gamestate.newGame();
					
				}
				
			});
			//Disconnect button.
			disconnectButton = new JButton("Disconnect");
			disconnectButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					gamestate.disconnect();
					
				}
				
			});

			//Layout of the buttons and panels.
			frame = new JFrame("Gomoku");
			
			//Layout for button container.
//			Container buttonContainer = frame.getContentPane();
//			SpringLayout buttonLayout = new SpringLayout();
//			
//			
//			//Adding buttons to container.
//			buttonContainer.add(connectButton);
//			buttonContainer.add(newGameButton);
//			buttonContainer.add(disconnectButton);
//			
//			//Positions the buttons in the container.
//			buttonLayout.putConstraint(SpringLayout.WEST,connectButton, 5, SpringLayout.WEST, buttonContainer);
//			buttonLayout.putConstraint(SpringLayout.EAST,connectButton,5, SpringLayout.WEST,newGameButton);
//			buttonLayout.putConstraint(SpringLayout.EAST,newGameButton,5, SpringLayout.WEST,disconnectButton);
//			buttonLayout.putConstraint(SpringLayout.NORTH, connectButton, 5, SpringLayout.NORTH, buttonContainer);
//
//			buttonContainer.setLayout(buttonLayout);
//			
			//Create the layout for the entire frame.
			SpringLayout layout = new SpringLayout();
			JPanel mainPanel = new JPanel();
			
			mainPanel.add(gameGridPanel);
			mainPanel.add(connectButton);
			mainPanel.add(newGameButton);
			mainPanel.add(disconnectButton);
			mainPanel.add(messageLabel);
			
			
			layout.putConstraint(SpringLayout.NORTH, gameGridPanel, 50, SpringLayout.NORTH, mainPanel);
			layout.putConstraint(SpringLayout.WEST, gameGridPanel, 50, SpringLayout.WEST, mainPanel);
			layout.putConstraint(SpringLayout.WEST, connectButton, 50, SpringLayout.WEST, mainPanel);
			layout.putConstraint(SpringLayout.NORTH, connectButton, 5, SpringLayout.SOUTH, gameGridPanel);
			layout.putConstraint(SpringLayout.NORTH, newGameButton, 5, SpringLayout.SOUTH, gameGridPanel);
			layout.putConstraint(SpringLayout.NORTH, disconnectButton, 5, SpringLayout.SOUTH, gameGridPanel);
			layout.putConstraint(SpringLayout.NORTH, messageLabel, 5, SpringLayout.SOUTH, connectButton);
			layout.putConstraint(SpringLayout.WEST,newGameButton, 5, SpringLayout.EAST,connectButton);
			layout.putConstraint(SpringLayout.WEST,disconnectButton, 5, SpringLayout.EAST, newGameButton);
			
			
			mainPanel.setLayout(layout);
			


			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setContentPane(mainPanel);
			
			frame.setResizable(false);
			frame.setSize(500,500);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
		catch(Exception e){System.out.println("Exception occoured.");}
		
		
		
		
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

	
}






