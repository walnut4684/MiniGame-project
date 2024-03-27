package rungame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class Games extends Obstacle {
	 private Image GameImg;

	    public Games(int x, int y) {
	        super(x, y, 42, 60);
	        URL imageURL = getClass().getResource("/img/Games.png");
	        if (imageURL != null) {
	            GameImg = new ImageIcon(imageURL).getImage();
	        } else {
	            System.err.println("Image not found");
	        }
	    }

	    @Override
	    public void draw(Graphics g) {
	        if (GameImg == null) {
	            g.setColor(Color.GREEN);
	            g.fillRect(x, y, width, height);
	        } else {
	            g.drawImage(GameImg, x, y, width, height, null);
	        }
	    }
	}