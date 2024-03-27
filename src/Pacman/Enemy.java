package Pacman;
import java.util.ArrayList;
import java.util.Random;




public class Enemy extends Mover {

	private Mover pacman;
	
	public Enemy(int x, int y, Mover pacman) {
		super(x,y);
		this.pacman = pacman;
	}

	@Override
	public void move(PacmanGame.Things board[][], int speed) { 
		super.move(board, speed);
		
		ArrayList<Direction> possibleFollowingDirections = new ArrayList<Direction>();
		
		if (x % PacmanGame.SQUARE_SIZE == 0 && //���� ��ġ�� SQUARE_SIZE�� ��ǥ�� �����ߴ��� Ȯ�� 
			y % PacmanGame.SQUARE_SIZE == 0) {
			if ((Math.abs(pacman.x-this.x)+Math.abs(pacman.y-this.y))<4*PacmanGame.SQUARE_SIZE){ //ĳ���Ϳ� ������� ��ü�� �Ÿ��� 4�� �̳����� Ȯ�� ���� �����ٸ� �߰�
				if (pacman.x>this.x && !isWallAtPoint(board, x + PacmanGame.SQUARE_SIZE, y)){ //ĳ������ x��ǥ�� ���� ��ü�� x��ǥ���� ũ�� ���������� �̵��� �� ������
					possibleFollowingDirections.add(Direction.RIGHT);  //������ ���� �߰�
				}
				if (pacman.x<this.x && !isWallAtPoint(board, x - PacmanGame.SQUARE_SIZE, y)){ //x��ǥ���� �۰� ���� �̵� ���� = ���� ���� �߰�
					possibleFollowingDirections.add(Direction.LEFT);
				}
				if (pacman.y<this.y && !isWallAtPoint(board, x, y - PacmanGame.SQUARE_SIZE)){ //y��ǥ���� �۰� ���� �̵� ���� = ���� ���� �߰�
					possibleFollowingDirections.add(Direction.UP);
				}
				if (pacman.y>this.y && !isWallAtPoint(board, x, y + PacmanGame.SQUARE_SIZE)){ //y��ǥ���� ũ�� �Ʒ��� �̵� ���� = �Ʒ��� ���� �߰�
					possibleFollowingDirections.add(Direction.DOWN);
				}
				Random r = new Random();				
				if (possibleFollowingDirections.size()==1){
					dir = possibleFollowingDirections.get(0);
				}
				else if (possibleFollowingDirections.size()==0){
					random_direction(board); //��ü�� �߰����� ���� �� ������ �������� �̵�
				}
				else {
					int index = r.nextInt(possibleFollowingDirections.size());
					dir = possibleFollowingDirections.get(index);
				}
			}
			else{
				random_direction(board);	
			}
		}
	}
	
	public void random_direction(PacmanGame.Things board[][]){ 
		
			// ������ ���� �߿��� ���� �̵��� �ݴ�Ǵ� ������ ����, ������ ���� ����
			ArrayList<Direction> possibleDirections = new ArrayList<Direction>(); //������ ���� ������ ����Ʈ ����

			if (!isWallAtPoint(board, x - PacmanGame.SQUARE_SIZE, y)) { //���� ������, ��,�Ʒ��� �̵� �������� Ȯ�� /�����Ѱ�� ����Ʈ �߰�
				possibleDirections.add(Direction.LEFT); 
			}
			if (!isWallAtPoint(board, x + PacmanGame.SQUARE_SIZE, y)) {
				possibleDirections.add(Direction.RIGHT);
			}
			if (!isWallAtPoint(board, x, y - PacmanGame.SQUARE_SIZE)) {
				possibleDirections.add(Direction.UP);
			}
			if (!isWallAtPoint(board, x, y + PacmanGame.SQUARE_SIZE)) {
				possibleDirections.add(Direction.DOWN);
			}
			
			if (dir==Direction.LEFT) { //��ü�� �̵� ������ ������ �ƴϰ�, ũ�Ⱑ 1�� �ƴϸ� ���� (���� ����� �ݴ�Ǵ� ���� ����x)
				if (possibleDirections.size()!=1){
					possibleDirections.remove(Direction.RIGHT);
				}
			}
			if (dir==Direction.RIGHT) {
				if (possibleDirections.size()!=1){
					possibleDirections.remove(Direction.LEFT);
				}
			}
			if (dir==Direction.UP) {
				if (possibleDirections.size()!=1){
					possibleDirections.remove(Direction.DOWN);
				}
			}
			if (dir==Direction.DOWN) {
				if (possibleDirections.size()!=1){
					possibleDirections.remove(Direction.UP);
				}
			}
		
			Random r = new Random();			
			int index = r.nextInt(possibleDirections.size()); //0���� ������ ���� ũ�� �������� ������ �ε��� ����
			dir = possibleDirections.get(index); //������ �ε����� dir�� �Ҵ�
			
		
		
	}
}









