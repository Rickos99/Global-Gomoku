package lab4;

import lab4.client.GomokuClient;
import lab4.data.GomokuGameState;
import lab4.gui.GomokuGUI;

public class GomokuMain {

	public static void main(String[] args) {
		int portNumber;
		//Default port.
		if(args.length == 0) {
            GomokuClient gc=new GomokuClient(4001);
            GomokuGameState gs=new GomokuGameState(gc);
            new GomokuGUI(gs,gc);
            //Two clients to test out the game.
            GomokuClient gc2=new GomokuClient(4002);
            GomokuGameState gs2=new GomokuGameState(gc2);
            new GomokuGUI(gs2,gc2);
		}else{
			try {
				if(args.length>1){throw new Exception();}
				portNumber = Integer.parseInt(args[0]);
				GomokuClient gc = new GomokuClient(portNumber);
				GomokuGameState gs = new GomokuGameState(gc);
				new GomokuGUI(gs,gc);
				
			}
			catch(NumberFormatException e) {System.out.println("Not a valid port number.");}
			catch(Exception e) {System.out.println("Only one argument permitted.");}
		}
		


	}

}
