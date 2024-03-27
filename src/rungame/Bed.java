package rungame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class Bed extends Obstacle {
	 private Image bedImg;

	    public Bed(int x, int y) {
	        super(x, y, 65, 50);
	        URL imageURL = getClass().getResource("/img/Bed.png");
	        if (imageURL != null) {
	            bedImg = new ImageIcon(imageURL).getImage();
	        } else {
	            System.err.println("Image not found");
	        }
	    }

	    @Override
	    public void draw(Graphics g) {
	        if (bedImg == null) {
	            g.setColor(Color.YELLOW);
	            g.fillRect(x, y, width, height);
	        } else {
	            g.drawImage(bedImg, x, y, width, height, null);
	        }
	    }
	}
