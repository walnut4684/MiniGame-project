package rungame;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Obstacle extends Rectangle {
  public Obstacle(int x, int y, int width, int height) {
    super(x, y, width, height);
  }
  public void setX(int x) {
      this.x = x;
  }

  public abstract void draw(Graphics g);
}