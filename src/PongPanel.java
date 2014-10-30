import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class PongPanel extends JPanel implements ActionListener, KeyListener{
	
	private boolean gameover = false;
	private boolean titlescreen = true;
	private boolean playing = false;
	
	private boolean pressedup = false;
	private boolean presseddown = false;
	private boolean pressedw = false;
	private boolean presseds = false;
	private int ballxpos = 300;
	private int ballypos = 300;
	private int balldiameter = 20;
	//Horizontal Ball speed
	private int dx = -4;
	//Vertical Ball speed
	private int dy = 4;
	
	int playeroneX = 25;
	int playeroneY = 300;
	int playertwoX = 550;
	int playertwoY = 300;
	int paddlewidth = 20;
	int paddlelength = 120;
	int paddlespeed = 5;
	
	private int playeronescore = 0;
	private int playertwoscore = 0;
	//creates a black background for the pong game panel
	public PongPanel(){
		setBackground(Color.black);
		
		//PongPanel updates the positions of all actors(balls and paddles) 
		//80 times a second (1000 milliseconds divided by 80)
		//nextFrame() is called 80 times a second
		Timer timer = new Timer(1000/80, this);
		timer.start();
		
		setFocusable(true);
		//Add a keyListener to detect keystrokes for paddle control
		addKeyListener(this);
	}
	@Override
	public void paintComponent(Graphics graphics){
		if(titlescreen){
			graphics.setColor(Color.BLACK);
			graphics.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			graphics.drawString("First player to reach 5 wins!", 100, 250);
			graphics.drawString("Press p to begin.", 150, 300);
		}
		if(playing){
			super.paintComponent(graphics);
			graphics.setColor(Color.green);
			//paint a ball with radius in those coordinates 
			graphics.fillOval(ballxpos, ballypos, balldiameter, balldiameter);
			//paint player paddles
			graphics.fillRect(playeroneX, playeroneY, paddlewidth, paddlelength);
			graphics.fillRect(playertwoX, playertwoY, paddlewidth, paddlelength);
			
			graphics.drawLine(playeroneX+paddlewidth, 0, playeroneX+paddlewidth, getHeight());
		    graphics.drawLine(playertwoX, 0, playertwoX, getHeight());
		    
		    graphics.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
		    graphics.drawString(Integer.toString(playeronescore), 100, 100);
		    graphics.drawString(Integer.toString(playertwoscore), 500, 100);
		}
		if(gameover){
			resetBall();
			resetPaddles();
			resetScore();
			graphics.setColor(Color.WHITE);
			graphics.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			if(playeronescore==5){
				graphics.drawString("Player One Wins!", 150, 250);
				graphics.drawString("Press 1 to restart!", 125, 300);
			}
			else{
				graphics.drawString("Player Two Wins!", 150, 250);
				graphics.drawString("Press 1 to restart!", 125, 300);
			}
			
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		nextFrame();
	}
	
	
	public void nextFrame(){	
		
	if(playing){
			//Calculate the upcoming position of the ball 
			int ballnextleftpos = ballxpos + dx;
			int ballnextrightpos = ballxpos + balldiameter + dx;
			int ballnextTop = ballypos + dy;
			int ballnextBot = ballypos + balldiameter + dy;
			
			//Calculate movement of paddles
			if(pressedup == true){
				//do not move the paddle if it's already at the top boundary
				if(playeroneY-paddlespeed>0){
					playeroneY-=paddlespeed;
				}
			}
			else if(presseddown == true){
				//do not move the paddle if it's already at the bottom boundary
				if(playeroneY+paddlelength+paddlespeed<getHeight()){
					playeroneY+=paddlespeed;
				}
			}
			
			if(pressedw == true){
				//do not move the paddle if it's already at the top boundary
				if(playertwoY-paddlespeed>0){
					playertwoY-=paddlespeed;
				}
			}
			else if(presseds == true){
				//do not move the paddle if it's already at the bottom boundary
				if(playertwoY+paddlelength+paddlespeed<getHeight()){
					playertwoY+=paddlespeed;
				}
			}
			
			//Boundary checking and paddle collision checking
			//Ball is at player 1's goal line
			if(ballnextleftpos<playeroneX+paddlewidth){
				//Player 2 has scored
				if(ballnextBot>playeroneY+paddlelength || ballnextTop<playeroneY){
					playertwoscore++;
					resetBall();
					//player 1 wins
					if(playertwoscore ==5){
						playing = false;
						gameover=true;
					}
						
				}
				else{
					dx = dx*-1;
				}
			}
			//Ball is at player 2's goal line
			if(ballnextrightpos>playertwoX){
				//Player 1 has scored
				if(ballnextBot>playertwoY+paddlelength || ballnextTop<playertwoY){
					playeronescore++;
					resetBall();
					//player 2 wins
					if(playeronescore==5){
						playing = false;
						gameover=true;
					}
				}
				else{
					dx = dx*-1;
				}
			}
			
			//Ball hits top boundary or bottom
			if (ballnextTop <=0 || ballnextBot>=getHeight()){
				//Ball bounces off the boundary
				dy = dy*-1;
			}
			
			//move the ball
			ballxpos +=dx;
			ballypos +=dy;
			
			//Update the panel with the ball's movement
			repaint();
		}	
	}

	@Override
	public void keyPressed(KeyEvent keystroke) {
		if(titlescreen){
			if(keystroke.getKeyCode()==KeyEvent.VK_P){
				titlescreen = false;
				playing = true;
			}
		}
		if(gameover){
			if(keystroke.getKeyCode()==KeyEvent.VK_1 || keystroke.getKeyCode()==KeyEvent.VK_NUMPAD1){
				playing = true;
				gameover = false;
			}
		}
		if(playing)
			switch(keystroke.getKeyCode()){
			case KeyEvent.VK_UP:
				pressedup = true;
				break;
			case KeyEvent.VK_DOWN:
				presseddown = true;
				break;
			case KeyEvent.VK_W:
				pressedw = true;
				break;
			case KeyEvent.VK_S:
				presseds = true;
				break;
			}
	}

	@Override
	public void keyReleased(KeyEvent keystroke) {
		switch(keystroke.getKeyCode()){
		case KeyEvent.VK_UP:
			pressedup = false;
			break;
		case KeyEvent.VK_DOWN:
			presseddown = false;
			break;
		case KeyEvent.VK_W:
			pressedw = false;
			break;
		case KeyEvent.VK_S:
			presseds = false;
			break;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent key) {
	}
	
	private void resetBall(){
		ballxpos=300;
		ballypos=300;
		Random r = new Random();
		dx = r.nextInt(2);
		if(dx==0)
			dx=-4;
		else{
			dx=4;
		}
		dy = r.nextInt(2);
		if(dy==0){
			dy = -4;
		}
		else{
			dy=4;
		}
	}
	
	private void resetPaddles(){
		playeroneX = 25;
		playeroneY = 300;
		playertwoX = 550;
		playertwoY = 300;
	}
	
	private void resetScore(){
		playeronescore = 0;
		playertwoscore = 0;
	}
}
