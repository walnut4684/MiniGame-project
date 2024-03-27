package Pacman;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JTextField;


public class Pacman extends Mover {
	int mouthAngle=0;
	final int MAX_MOUTH_ANGLE=75;
	boolean isMouthOpening=true;
	
	private Image pacmanImage;
	
	public Pacman(int x, int y) {
		super(x, y);
		this.dir = Direction.LEFT;
		this.pacmanImage = new ImageIcon("./Image/wkstudent.gif").getImage();
	}
	
	
	
	

	@Override
	public void move(PacmanGame.Things board[][], int speed) {
		super.move(board,  speed);
		
		int i = (y+PacmanGame.SQUARE_SIZE/2) / PacmanGame.SQUARE_SIZE;
		int j = (x+PacmanGame.SQUARE_SIZE/2) / PacmanGame.SQUARE_SIZE;
		if (isMouthOpening){
			if (mouthAngle==MAX_MOUTH_ANGLE){
				isMouthOpening=false;
			}
			else
				mouthAngle+= 5;
		}
		if (isMouthOpening==false){
			if (mouthAngle==0){
				isMouthOpening=true;
			}
			else
				mouthAngle-= 5;
		}
		if (board[i][j] == PacmanGame.Things.DOT)
		{
			board[i][j] = PacmanGame.Things.EMPTY;
			PacmanGame.score+=1;
		}
		else if (board[i][j] == PacmanGame.Things.CHERRY)
		{
			board[i][j] = PacmanGame.Things.EMPTY;
			PacmanGame.score+=5;
		}
	}
	public void drawPacman(Graphics2D g){
		g.setColor(new Color (0,200,15));
		if (dir == Direction.RIGHT) {
			g.drawImage(pacmanImage, x, y, null);			
		} else if (dir == Direction.LEFT) {
			g.drawImage(pacmanImage, x, y, null);	
		}else if (dir == Direction.UP) {
			g.drawImage(pacmanImage, x, y, null);
		}else if (dir == Direction.DOWN) {
			g.drawImage(pacmanImage, x, y, null);	
		}else if (dir == Direction.NONE) {
			g.drawImage(pacmanImage, x, y, null);
		}
	}
}
		