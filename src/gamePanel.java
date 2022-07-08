import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
//import javax.swing.border.Border;
//import javax.swing.border.LineBorder;

public class gamePanel extends JPanel implements ActionListener {
     
	static final int SCREEN_WIDTH = 700;
	static final int SCREEN_HEIGHT =700;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
	static final int DELAY = 75;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyparts =6;
	int orangesEaten;
	int orangeX;
	int orangeY;
	char direction = 'D';
	boolean running = false;
	Timer timer;
	Random random;
	
	
	gamePanel(){
		random = new Random();
		this.setBackground(Color.BLACK);
		//Border border = new LineBorder(Color.ORANGE,5,true);
	    //this.setBorder(border);
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setFocusable(true);
		this.addKeyListener(new myKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newOrange();
		running = true; // game is running
		timer = new Timer(DELAY, this);// invokes actionPerformed after every DELAY milliseconds.
		timer.start();
	}
	
	public void newOrange() {
		orangeX = random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;//random.nextInt generates a random integer from 0 to 27 (not 28).
		orangeY = random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
	}
	
	public void paintComponent(Graphics g) {// gets called implicitly.
		super.paintComponent(g); // paints background color.
		draw(g);
	}
	public void draw(Graphics g) {
		if(running) {
		  g.setColor(Color.orange);
		  g.fillOval(orangeX, orangeY, UNIT_SIZE, UNIT_SIZE);
		
		  for(int i=0;i<bodyparts;i++) {
			if(i==0) {
				g.setColor(Color.red);
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
			else if(i==1) {
				g.setColor(Color.BLUE);
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
			else {
				g.setColor(Color.green);
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
		  }
		  g.setColor(Color.red);
	      g.setFont(new Font("Ink Free",Font.PLAIN,40));
		  FontMetrics metrics = getFontMetrics(g.getFont());
		  g.drawString("Score:"+ orangesEaten,(SCREEN_WIDTH- metrics.stringWidth("Score:"+ orangesEaten))/2 ,g.getFont().getSize() );
		}
		else {
			gameOver(g);
		}
	}
	public void gameOver(Graphics g) {
		//score
		g.setColor(Color.red);
	    g.setFont(new Font("Ink Free",Font.PLAIN,40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score:"+ orangesEaten,(SCREEN_WIDTH- metrics1.stringWidth("Score:"+ orangesEaten))/2 ,g.getFont().getSize()+50);
		
		// game over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.PLAIN,100));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over",(SCREEN_WIDTH- metrics2.stringWidth("Game Over"))/2 ,SCREEN_HEIGHT/2 ); //places string in the center of panel.
		
		
	}
	public void move() {
		for(int i =bodyparts; i>0;i--) {// To move the body alongside the head
			x[i]= x[i-1];
			y[i]= y[i-1];
		}
		switch(direction) {
		case 'U':
			y[0]= y[0]-UNIT_SIZE;
			break;
		case 'D':
			y[0]= y[0]+UNIT_SIZE;
			break;
		case 'L':
			x[0]= x[0]-UNIT_SIZE;
			break;
		case 'R':
			x[0]=x[0]+UNIT_SIZE;
		}
	}
	public void checkCollisions() {
		// check if head collides with body
		for(int i =bodyparts;i>0;i--) {
			if(x[0]==x[i] && y[0]==y[i]) {
				running = false;
			}
		}
		// check if head collides with borders
		if(x[0]<0||x[0]>SCREEN_WIDTH -UNIT_SIZE||y[0]<0||y[0]>SCREEN_HEIGHT-UNIT_SIZE) {
			running = false;
		}
		if(!running) {
			timer.stop(); // timer stops invoking actionPerformed
		}	
	}
	public void checkOrange() {
		if(x[0]==orangeX && y[0]==orangeY) {
			bodyparts++; //more pixels will be painted in the for loop inside draw() method.
			orangesEaten++;
			newOrange();
		}
		
	}
	
		public class myKeyAdapter extends KeyAdapter{// abstract class KeyAdapter implements KeyListener interface and gives blank implementation to its
			                                         //three methods.Then KeyPressed() method is overridden in class myKeyAdapter.
		                                             // If we make JPanel directly implement KeyListener then either we'll have to define all 3 methods (or declare JPanel abstract?)
			@Override
			public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case 37: //left. Another option: case KeyEvent.VK_LEFT:
				if(direction!='R')
					direction ='L';
				break;
			case 39: //right
				if(direction!='L')
					direction ='R';
			    break;
			case 38: //up
				if(direction!='D')
					direction ='U';
			    break;
			case 40: //down
				if(direction!='U')
					direction ='D';
			    break;
			
			}
		}
		
	}
         
	@Override
	public void actionPerformed(ActionEvent e) { // invoked by timer after every DELAY milliseconds.
        if(running) {
			move();
			checkOrange();
			checkCollisions();
		}
		repaint(); //after moving snake/eating orange, we must call paint again (using repaint()) to display the changes.
		
	}

}
