package rungame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class Alcohol extends Obstacle {
    private Image alcoholImg;

    public Alcohol(int x, int y) {
        super(x, y, 42, 60);
        URL imageURL = getClass().getResource("/img/Alcohol.png");
        if (imageURL != null) {
            alcoholImg = new ImageIcon(imageURL).getImage();
        } else {
            System.err.println("Image not found");
        }
    }

    @Override
    public void draw(Graphics g) {
        if (alcoholImg == null) {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        } else {
            g.drawImage(alcoholImg, x, y, width, height, null);
        }
    }
}
