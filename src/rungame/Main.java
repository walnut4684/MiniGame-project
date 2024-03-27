package rungame;

import javax.swing.*;

public class Main {
	 public static void main(String[] args) {
	        JFrame frame = new JFrame("run jump run");
	        GamePanel gamePanel = new GamePanel(frame);
	        
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.add(gamePanel);
	        frame.pack();
	        frame.setSize(800, 600);
	        frame.setResizable(false);
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	    }

}
