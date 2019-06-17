/*This is not all my code found the source of it online at http://zetcode.com/tutorials/javagamestutorial/snake/
*Changes made by: Weston & Laci
*2/25/2018

*This program is a game called snake the objective is to collect as many apples as you can without running into yourself and if you are on hard difficulty
*running into the walls. There are 2 difficulties easy and hard. Easy allows you to teleport through 1 wall and out the opposite. Hard kills you if you hit
*any of the walls.

*Enjoy the game feel free to copy the code and make as many changes as you would like.
*/

package SnakePkg;

//This is all of the imports probably should have just imported the entire packages and crap but I decided to import them 1 by 1. Stupid me right?
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.sound.sampled.*;

@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener {
	
	//This is where I declared most of the variables.
    private final int B_WIDTH = 700;
    private final int B_HEIGHT = 700;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 4900;
    private final int RAND_POS = 69;
    public int DELAY = 50;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;
    private int Score = 0;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private boolean flipFlop = true;
    private boolean AudioFlip = true;
    private boolean DINGSOUNDS;

    private Timer timer = new Timer(DELAY, this);
    private Image ball;
    private Image apple;
    private Image head;
    
    private String Difficulty;
    private String diffEasy = "easy";
    private String diffHard = "hard";
    private String diffHyper = "hyper";
    
    private Audio snek;
      
    //This is the first method it sets up the labels and the key listener and also the board size.
    public Board() {
    	//This sets the color for the score label and where it is going it be.
    	setLayout(null);
    	scoreLabel.setForeground(Color.white);
    	add(scoreLabel);
    	scoreLabel.setBounds(625, 665, 100, 50);
    	
    	//This sets the color for the info label and where it is going it be.
    	infoLabel.setForeground(Color.white);
    	add(infoLabel);
    	infoLabel.setBounds(5, 665, 500, 50);
    	
    	difficultyLabel.setForeground(Color.white);
    	add(difficultyLabel);
    	difficultyLabel.setBounds(600,650,100,50);
    	
    	soundLabel.setForeground(Color.white);
    	add(soundLabel);
    	soundLabel.setBounds(5,650,500,50);
    	
    	//This starts the key listener and sets the background color of the JPanel.
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        //This sets the size of the board.
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        
        //Goes the the loadImages method and difficulty method.
        try {
        difficulty();
        loadImages();
        
        } catch (Exception j) {}
    }

    //This is the method to declare the difficulty of the game. Easy lets you teleport through on wall and out the other hard just kills you.
    private void difficulty() {
    	//This loop asks for the user to input either easy or hard.
    	while(true) {
    	Difficulty = JOptionPane.showInputDialog("Enter easy or hard difficulty.");
    	Difficulty.toLowerCase();
    	
    	//Checks if the difficulty equals easy
    	if(Difficulty.equals(diffEasy) || Difficulty.equals(diffHard)) {
    		
    		//Goes to initGame method.
    		initGame();
    		break;
    	}    	
    	
    	if(Difficulty.equals(diffHyper)) {
    		initGame();
    		DELAY = 1;
    		timer = new Timer(DELAY, this);
    		break;
    	}
    	}
    }
    
    //Where I declared the labels not sure why here though?
    JLabel scoreLabel = new JLabel("Score: 0");
    JLabel infoLabel = new JLabel("Press space to pause and again to unpause.");
    JLabel difficultyLabel = new JLabel(Difficulty);
    JLabel soundLabel = new JLabel("Press P to pause and unpause sounds.");
    
    //This is the method that I chose to restart the game.
    private void restart() {
    	//Resets the score-board to 0
    	scoreLabel.setText("Score: 0");
    	Score = 0;
    	
    	//Restarts the timer.
    	timer.restart();
    	timer.stop();
    	
    	//If the player died it makes it true that the game is still playable.
    	inGame = true;
    	
    	//Resets the direction.
    	leftDirection = false;
        rightDirection = true;
        upDirection = false;
        downDirection = false;
    	
        //Goes to the difficulty method so the player can re-enter a new difficulty.
        difficulty();
    }

    //This gets the images from the C: drive since most people have a C: accessible to them.
    private void loadImages() throws Exception {

    	//Loads the dot image.
        ImageIcon iid = new ImageIcon("C:/Snake/Images/dot.png");
        ball = iid.getImage();

        //Loads the apple image.
        ImageIcon iia = new ImageIcon("C:/Snake/Images/apple.png");
        apple = iia.getImage();

        //Loads the head image.
        ImageIcon iih = new ImageIcon("C:/Snake/Images/head.png");
        head = iih.getImage();
        
        snek = new Audio(0);
    }
    

    //This is where the game actually gets initiated
    private void initGame() {

    	//Sets the initial amount of dots the snake has.
    	if (!Difficulty.equals(diffHyper)) {
    		dots = 3;
    	} if (Difficulty.equals(diffHyper)) {
    		dots = 3;
    	}
        //Places the dots 1 after another.
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        //Goes to method locateApple.
        locateApple();

        //Starts the timer.
       	if(Difficulty.equals(diffHyper)) {
    		DELAY = 1;
    		timer = new Timer(DELAY, this);
    	}
        timer.start();
        
        difficultyLabel.setText("Difficulty is: " + Difficulty);
    }

    //This method just paints the graphics into a JPanel.
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    
    private void doDrawing(Graphics g) {
        
    	//Checks to see if the player has died yet.
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                    
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }        
    }

    //This method just sets up the game over graphics.
    private void gameOver(Graphics g) {
        
    	//Sets font of the game over text and restart text.
        String GO = "Game Over";
        String restart = "Press R to restart";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        //Sets color of game over text and restart text also the placement.
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(GO, (B_WIDTH - metr.stringWidth(GO)) / 2, B_HEIGHT / 2);
        g.drawString(restart, (B_WIDTH - metr.stringWidth(restart)) / 2, B_HEIGHT / 2 + 15);
    }
    
    //This method checks if the snake has hit an apple.
    private void checkApple() throws Exception {

    	//Checks to see if the head of the snake has come into contact with an apple
        if ((x[0] == apple_x) && (y[0] == apple_y)) {

        	//Plays apple eating sound.
        	if (DINGSOUNDS == false) {
        	String eat = "C:/Snake/Sounds/ding.wav";
        	File eatSound = new File(eat);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(eatSound);              
            Clip clipEat = AudioSystem.getClip();
            clipEat.open(audioIn);
            clipEat.start();
            }
            
        	//If the snake "ate" an apple increases score and the number of dots.
            dots++; 
            Score++;
            
            //Goes to the locateApple method.
            locateApple();
            
            //Repaints the score-board.
            scoreLabel.setText("Score: " + Score);
        }
    }

    //This method makes the snake move
    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    //This method check to see if the snake has hit itself and if in hard mode if it has hit the walls.
    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
            	if (!Difficulty.equals(diffHyper)) {
                inGame = false;
            	}
            }
        }

        //If the difficulty is easy it teleports the snake through the wall
        if (Difficulty.equals(diffEasy) || (Difficulty.equals(diffHyper))) {
        	if (y[0] >= B_HEIGHT) {
        		y[0] = 0; 
        }

        	if (y[0] < 0) {
        		y[0] = 700;
        }

        	if (x[0] >= B_WIDTH) {
        		x[0] = 0;
        }

        	if (x[0] < 0) {
        		x[0] = 700;
        }
        
        	if(!inGame) {
        		timer.stop();
        }
      }
    
        
        if (Difficulty.equals(diffHard)) {
        	if (y[0] >= B_HEIGHT) {
        		inGame = false;
        }

        	if (y[0] < 0) {
        		inGame = false;
        }

        	if (x[0] >= B_WIDTH) {
        		inGame = false;
        }

        	if (x[0] < 0) {
        		inGame = false;
        }
    }
    }

    //This method finds where the apple is to compare it to where the snakes head is so the program can check to see if the snake has hit an apple.
    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    //This method is the one that repaints the screen.
    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

        	try {
            checkApple();
        	} catch (Exception i){
        	}
        	
        	
            checkCollision();
            move();
        }

        repaint();
    }
    
    

    //This method is where the program checks to see if the movement, pause/unpause, and restart key have been pressed.
    private class TAdapter extends KeyAdapter {
    	
        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (((key == KeyEvent.VK_LEFT) && (!rightDirection)) || ((key == KeyEvent.VK_A) && (!rightDirection))) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if (((key == KeyEvent.VK_RIGHT) && (!leftDirection)) || ((key == KeyEvent.VK_D) && (!leftDirection))) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if (((key == KeyEvent.VK_UP) && (!downDirection)) || ((key == KeyEvent.VK_W) && (!downDirection))) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if (((key == KeyEvent.VK_DOWN) && (!upDirection)) || ((key == KeyEvent.VK_S) && (!upDirection))) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            
            if (key == KeyEvent.VK_R) {
            	restart();
            } 
            
            if (key == KeyEvent.VK_SPACE) {
            	flipFlop = !flipFlop;
            	
            	if (flipFlop == false) {
            		timer.stop();
            	}
            	
            	if (flipFlop == true) {
            		timer.start();
            	}   
            }
            
            if (key == KeyEvent.VK_P) {
            	AudioFlip = !AudioFlip;
            	DINGSOUNDS = !DINGSOUNDS;
            	
            	if (AudioFlip == false) {
            		snek.pause();
            	}
            	
            	if (AudioFlip == true) {
            		try {
            			snek.resumeAudio();
            		} catch (Exception a) {}
            	}
            }
        }
    }
}