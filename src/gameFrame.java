import javax.swing.*;

public class gameFrame extends JFrame{
    
	
	gameFrame(){
		gamePanel panel = new gamePanel();
		this.add(panel);
		this.setTitle("Snake Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null); // puts the frame in the middle of screen.
		
	}
}
