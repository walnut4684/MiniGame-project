package rungame;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Student extends Rectangle {
	
	  private int yVelocity;
	  private int jumpCounter;
	  private static final int JUMP_GRAVITY = 2;
	  private ImageIcon studentIcon;
	  private Image studentImage;

	  public Student(int x, int y) {
		    super(x, y, 30, 45);
		    studentIcon = new ImageIcon(getClass().getResource("/img/wkstudent.gif"));
		    studentImage = studentIcon.getImage();
		  }
	   public void setY(int y) {
	        this.y = y;
	    }

	  public void tick() {
	    y += yVelocity;
	    yVelocity += JUMP_GRAVITY;
	    if (getY() >= 300) {
	      yVelocity = 0;
	      setY(300);
	      jumpCounter = 0;
	    }
	  }

	  public void jump() {
	    if (jumpCounter < 2) {
	      yVelocity -= 30;
	      jumpCounter++;
	    }
	  }

	  public void draw(Graphics g) {
		  g.drawImage(studentImage, x, y, width, height, null);
		}

}
