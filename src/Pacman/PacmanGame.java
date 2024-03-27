package Pacman;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


import java.io.*;

import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class PacmanGame extends JPanel implements MouseMotionListener, MouseListener, KeyListener {

	int mouse_x; // ���콺�� ���� ��ġ ����
	int mouse_y;
	int Pmouse_x; // ���� ���콺�� ��ġ ����
	int Pmouse_y;

	private boolean displayGameRule;
	
	private boolean gamePaused = true;
	private long gameStartTime;

	static int PAUSE_BUTTON_X = 630; // �Ͻ� ���� ��ư ��ġ
	static int PAUSE_BUTTON_Y = 5;
	static int PAUSE_SIZE = 30; // �Ͻ� ���� ��ư ũ��

	boolean is_first_draw = true; // �׸��� ���� ����

	boolean mouse_is_down = false; // ���콺 ��ư�� ���ȴ��� ����

	static int SQUARE_SIZE = 40; // �簢�� ũ��
	static int PACMAN_SPEED = 8; // ĳ������ �̵� �ӵ�
	static int ENEMY_SPEED = 4; // ������� ��ü �ӵ�
	static int score = 0; // ���� ���� �ʱ�ȭ

	Pacman pacman;

	Mover enemy1;
	Mover enemy2;

	int pendingKeyCode = -1; // Ű �̺�Ʈ ���� �ʱ�ȭ

	boolean paused = false; // ���� �Ͻ�����

	enum Things { // ���� ���
		CHERRY, DOT, WALL, DESTINATION_DOT, ENEMY_SPAWNING_PLACE, EMPTY, ENEMY_SPAWNING_GATE,
	}

	static Things original_board[][];
	static Things[][] board;

	public class MyRunnable implements Runnable {

		public void run() {
			while (true) {
				try {
					Thread.sleep(1000 / 60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!gamePaused) {
					repaint();
				}
			}
		}
	}

	MyRunnable repaint_thread = new MyRunnable();
	private BufferedImage dotImage;
	private BufferedImage enemyImage1;
	private BufferedImage enemyImage2;
	private BufferedImage pauseImage;
	private BufferedImage CherryImage;
	private BufferedImage gameRuleImage;

	public PacmanGame() {

		restart();
		try {
			// �̹��� ������ �о��
			dotImage = ImageIO.read(new File("./Image/grape.png"));
			enemyImage1 = ImageIO.read(new File("./Image/F.jpg"));
			enemyImage2 = ImageIO.read(new File("./Image/F1.jpg"));
			pauseImage = ImageIO.read(new File("./Image/pause.png"));
			CherryImage = ImageIO.read(new File("./Image/Cherry.png"));
			gameRuleImage = ImageIO.read(new File("./Image/GameRule.png"));
		} catch (IOException e) {
			System.out.println("�̹��� �ε� ���� :" + e.getMessage());
			e.printStackTrace();
		}
	}

	public void restart() { // ���� �����
		gamePaused = false;
		displayGameRule = true; // ���� ���� �� GameRule �̹��� ǥ��
		pacman = new Pacman(7 * SQUARE_SIZE, 6 * SQUARE_SIZE);
		enemy1 = new Enemy(-1, -1, pacman);
		enemy2 = new Enemy(-1, -1, pacman);
		score = 0;
		pendingKeyCode = -1;
		gameStartTime = System.currentTimeMillis();

		board = original_board.clone();
		for (int i = 0; i < board.length; i++) {
			board[i] = original_board[i].clone();
		}
	}

	public static void loadBoard() {
		InputStream fis;
		BufferedReader br;
		String line;

		Map<Character, Things> decode_map = new HashMap<Character, Things>();
		decode_map.put('#', Things.WALL); // map.txt���� '#'�� ���� �ǹ�
		decode_map.put(' ', Things.EMPTY); // map.txt���� �ƹ��͵� ������ ���
		decode_map.put('C', Things.CHERRY); // 'c'�� ����(5��¥��)
		decode_map.put('.', Things.DOT); // '.'�� �Դ� ������ �ǹ�
		decode_map.put('-', Things.ENEMY_SPAWNING_PLACE); // '-'�� �� ���� ��Ҹ� �ǹ�
		decode_map.put('=', Things.ENEMY_SPAWNING_GATE); // '='�� �� ���� �Ա��� �ǹ�

		try {
			fis = new FileInputStream("map.txt");
			br = new BufferedReader(new InputStreamReader(fis));

			int num_rows = -1;
			int num_cols = -1;
			int i = 0;
			while ((line = br.readLine()) != null) {
				if (num_rows < 0) {
					num_rows = Integer.parseInt(line);
				} else if (num_cols < 0) {
					num_cols = Integer.parseInt(line);
					original_board = new Things[num_rows][num_cols];
				} else {
					for (int j = 0; j < line.length(); j++) {
						original_board[i][j] = decode_map.get(line.charAt(j));
					}
					i++;
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		br = null;
		fis = null;
	}

	public static void main(String[] args) {

		loadBoard(); // map.txt���Ͽ��� ���带 �о��

		JFrame frame = new JFrame();
		frame.setSize(800, 600); // ������ ũ��
		frame.setLocationRelativeTo(null);

		PacmanGame m = new PacmanGame();
		m.addMouseMotionListener(m);
		m.addMouseListener(m);
		frame.addKeyListener(m);
		frame.add(m);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel myOutput = new JLabel("Score:" + score); // Score ǥ��
		frame.add(myOutput);
		myOutput.setForeground(Color.white);

		new Thread(m.repaint_thread).run();
	}

	public void mouseMovedOrDragged(MouseEvent e) // ���콺 �̵��� �巡�� �̺�Ʈ ó��
	{
		mouse_x = e.getX();
		mouse_y = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		mouseMovedOrDragged(e);
	}

	public void mouseDragged(MouseEvent e) {
		mouseMovedOrDragged(e);
	}

	@Override
	public void paintComponent(Graphics oldg) {
		Graphics2D g = (Graphics2D) oldg; // oldg�� Graphics2D ��ü�� ��ȯ-> g�� ����
		if (is_first_draw) // true�� ��� setup �޼ҵ� ȣ��
		{
			setup();
			is_first_draw = false;
		} else {
			if (!paused) // �Ͻ������� �ƴҶ�
			{
				// �Ѹǹ���
				if (pendingKeyCode != -1) { // pendingkeyCode�� -1�� �ƴ� ���
					tryToSwitchDirection(); // �޼ҵ� ȣ���Ͽ� ĳ���� ���� ����
				}

				pacman.move(board, PACMAN_SPEED); // ĳ���͸� ���ǵ常ŭ ���忡 �̵���Ŵ
				
				if (score >= 132) {
					gamePaused = true;
					Object[] options = { "Next Game" };
					int choice = JOptionPane.showOptionDialog(null, "Next game", "Next Game", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					if (choice == JOptionPane.YES_OPTION) {
						// 현재 게임 창 닫기
						Window window = SwingUtilities.getWindowAncestor(this);
						window.dispose();
						// CardMatching 게임 실행
						nextProject();
					} else {
						// 게임 일시 정지 상태 해제
						gamePaused = false;
					}
					
				}

				// ������� ��ü�� ������ ���� ��ҿ� ����
				if (enemy1.x < 0) {
					for (int i = 0; i < board.length; i++) {
						for (int j = 0; j < board[i].length; j++) {
							if (board[i][j] == Things.ENEMY_SPAWNING_PLACE) { // ������� ��ü�� ��ġ�� ���� ����
								enemy1.x = j * SQUARE_SIZE;
								enemy1.y = i * SQUARE_SIZE;
								enemy1.dir = Mover.Direction.UP;
							}
						}
					}
				} else {
					enemy1.move(board, ENEMY_SPEED);
				}
				if ((pacman.x == enemy1.x && Math.abs(pacman.y - enemy1.y) < SQUARE_SIZE) || // �Ѹǰ� ���� ��ġ�� �浹�Ѱ��
						(pacman.y == enemy1.y && Math.abs(pacman.x - enemy1.x) < SQUARE_SIZE)) { // �����
					Object[] options = { "Retry Game" };

					gamePaused = true;
					int choice = JOptionPane.showOptionDialog(null, "적에게 잡혔습니다", "Retry Game",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					if (choice == JOptionPane.YES_OPTION) {
						// 현재 게임 창 닫기
						Window window = SwingUtilities.getWindowAncestor(this);
						window.dispose();
						// 처음 게임 실행
						restartProject();
					} else {
						// 게임 일시 정지 상태 해제
						gamePaused = false;
					}
				}

				{
					if (enemy2.x < 0) {
						for (int i = 0; i < board.length; i++) {
							for (int j = 0; j < board[i].length; j++) {
								if (board[i][j] == Things.ENEMY_SPAWNING_PLACE) {
									enemy2.x = j * SQUARE_SIZE;
									enemy2.y = i * SQUARE_SIZE;
									enemy2.dir = Mover.Direction.UP;
								}
							}
						}
					} else {
						enemy2.move(board, ENEMY_SPEED);
					}
					if ((pacman.x == enemy2.x && Math.abs(pacman.y - enemy2.y) < SQUARE_SIZE)
							|| (pacman.y == enemy2.y && Math.abs(pacman.x - enemy2.x) < SQUARE_SIZE)) {
						Object[] options = { "Retry Game" };

						gamePaused = true;
						int choice = JOptionPane.showOptionDialog(null, "적에게 잡혔습니다", "Retry Game",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
						if (choice == JOptionPane.YES_OPTION) {
							// 현재 게임 창 닫기
							Window window = SwingUtilities.getWindowAncestor(this);
							window.dispose();
							// 처음 게임 실행
							restartProject();
						} else {
							// 게임 일시 정지 상태 해제
							gamePaused = false;
						}
					}
				}
				if (displayGameRule) { 
					long currentTime = System.currentTimeMillis();
					long elapsedTime = currentTime - gameStartTime;

					if (elapsedTime < 3000) { 
						g.drawImage(gameRuleImage, 0, 0, getWidth(), getHeight(), null);
						return;
					} else {
						displayGameRule = false; 
					}
				}
			}
		}

		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, 2000, 2000);

		// ������ ��ҵ� �׷��� ����
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == Things.WALL) {
					g.setColor(Color.gray);
					g.fillRect(j * SQUARE_SIZE, i * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
				} else if (board[i][j] == Things.DOT) {
					g.drawImage(dotImage, j * SQUARE_SIZE + 5, i * SQUARE_SIZE + 5, SQUARE_SIZE - 10, SQUARE_SIZE - 10,
							null);
				} else if (board[i][j] == Things.CHERRY) {
					g.setColor(new Color(255, 0, 0));
					g.drawImage(CherryImage, j * SQUARE_SIZE + 5, i * SQUARE_SIZE + 5, SQUARE_SIZE - 10,
							SQUARE_SIZE - 10, null);
				} else if (board[i][j] == Things.ENEMY_SPAWNING_GATE) {
					g.setColor(Color.black);
					g.fillRect(j * SQUARE_SIZE, i * SQUARE_SIZE + 10, SQUARE_SIZE, SQUARE_SIZE - 20);
				}
			}
		}

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 20)); // score ��Ʈ ũ��
		g.drawString("Score:" + score, getWidth() - 100, 20); // score ��ġ

		pacman.drawPacman(g);
		// enemy ������
		if (enemy1.x >= 0) {
			try {
				BufferedImage enemyImage1 = ImageIO.read(new File("./Image/F.jpg")); // enemy1�� �̹��� ���� ���
				g.drawImage(enemyImage1, enemy1.x, enemy1.y, SQUARE_SIZE, SQUARE_SIZE, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (enemy2.x >= 0) {
			try {
				BufferedImage enemyImage2 = ImageIO.read(new File("./Image/F1.jpg")); // enemy2�� �̹��� ���� ���
				g.drawImage(enemyImage2, enemy2.x, enemy2.y, SQUARE_SIZE, SQUARE_SIZE, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// �Ͻ�������ư

		g.setColor(new Color(255, 0, 0));
		g.drawImage(pauseImage, PAUSE_BUTTON_X, PAUSE_BUTTON_Y, PAUSE_SIZE, PAUSE_SIZE, null);
	}

	private void nextProject() {
		try {
			String basePath = System.getProperty("user.dir") + "\\src";
			String otherPackageName = "Brick";
			String outputDir = System.getProperty("user.dir") + "\\bin";
			String otherProjectFile = "BlockGame";
			String gamePackageName = "Brick";

			ProcessBuilder pbCompileGame = new ProcessBuilder("javac", "-encoding", "UTF-8", "-d", outputDir,
					basePath + "\\" + gamePackageName + "\\*.java");
			pbCompileGame.inheritIO();
			Process processCompileGame = pbCompileGame.start();
			processCompileGame.waitFor();

			ProcessBuilder pbRun = new ProcessBuilder("java", "-cp", outputDir,
					otherPackageName + "." + otherProjectFile);
			pbRun.inheritIO();
			Process processRun = pbRun.start();
			processRun.waitFor();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void restartProject() {
		try {
			String basePath = System.getProperty("user.dir") + "\\src";
			String otherPackageName = "rungame";
			String outputDir = System.getProperty("user.dir") + "\\bin";
			String otherProjectFile = "Main";
			String gamePackageName = "rungame";

			ProcessBuilder pbCompileGame = new ProcessBuilder("javac", "-encoding", "UTF-8", "-d", outputDir,
					basePath + "\\" + gamePackageName + "\\*.java");
			pbCompileGame.inheritIO();
			Process processCompileGame = pbCompileGame.start();
			processCompileGame.waitFor();

			ProcessBuilder pbRun = new ProcessBuilder("java", "-cp", outputDir,
					otherPackageName + "." + otherProjectFile);
			pbRun.inheritIO();
			Process processRun = pbRun.start();
			processRun.waitFor();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (mouse_x >= PAUSE_BUTTON_X && mouse_x <= PAUSE_BUTTON_X + PAUSE_SIZE && mouse_y >= PAUSE_BUTTON_Y
				&& mouse_y <= PAUSE_BUTTON_Y + PAUSE_SIZE) {
			// 게임 일시 정지 상태로 변경

		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent event) {
		// TODO Auto-generated method stub
		if (event.getButton() == 1) {
			mouse_is_down = true;
			Pmouse_x = mouse_x;
			Pmouse_y = mouse_y;
		} else {

		}

		mouseClicked();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mouse_is_down = false;
	}

	public void setup() {
		for (int i = 0; i < 16; i++)
			;
	}

	public void mouseClicked() { // ���콺 Ŭ�� �̺�Ʈ�� �߻����� ��
		if (mouse_x > PAUSE_BUTTON_X && mouse_x < PAUSE_BUTTON_X + PAUSE_SIZE && mouse_y > PAUSE_BUTTON_Y
				&& mouse_y < PAUSE_BUTTON_Y + PAUSE_SIZE) { // ���콺�� pause ��ư ������ �ִ��� Ȯ��

			paused = !paused;
		}
	}

	public void tryToSwitchDirection() // ĳ������ �̵� ������ �����ϴ� �޼ҵ�
	{
		if (pendingKeyCode == KeyEvent.VK_UP && (pacman.x % SQUARE_SIZE == 0)
				&& !Mover.isWallAtPoint(board, pacman.x, pacman.y - SQUARE_SIZE)) {
			pacman.dir = Mover.Direction.UP;
			pendingKeyCode = -1;

		}
		if (pendingKeyCode == KeyEvent.VK_DOWN && (pacman.x % SQUARE_SIZE == 0)
				&& !Mover.isWallAtPoint(board, pacman.x, pacman.y + SQUARE_SIZE)) {
			pacman.dir = Mover.Direction.DOWN;
			pendingKeyCode = -1;
		}
		if (pendingKeyCode == KeyEvent.VK_LEFT && (pacman.y % SQUARE_SIZE == 0)
				&& !Mover.isWallAtPoint(board, pacman.x - SQUARE_SIZE, pacman.y)) {
			pacman.dir = Mover.Direction.LEFT;
			pendingKeyCode = -1;
		}
		if (pendingKeyCode == KeyEvent.VK_RIGHT && (pacman.y % SQUARE_SIZE == 0)
				&& !Mover.isWallAtPoint(board, pacman.x + SQUARE_SIZE, pacman.y)) {
			pacman.dir = Mover.Direction.RIGHT;
			pendingKeyCode = -1;
		}
	}

	@Override
	public void keyPressed(KeyEvent key) {
		pendingKeyCode = key.getKeyCode();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}