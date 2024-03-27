package rungame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
	private Student player;
	private ArrayList<Obstacle> obstacles;
	private Timer timer;
	private int gametime;
	private int score;
	private boolean gameOver;
	private Random random = new Random();
	private int lastObstaclePosition = 800;
	private int obstacleDelay = 0;
	private int consecutiveObstacles = 0;
	private int obstacleCreatedCount = 0;
	private Image gameEx;
	private JFrame frame;

	public GamePanel(JFrame frame) {
		this.frame = frame;
		player = new Student(100, 300);
		obstacles = new ArrayList<Obstacle>();
		timer = new Timer(1000 / 60, this);
		timer.start();
		setFocusable(true);
		addKeyListener(this);
		gametime = 0;
		score = 0;
		gameOver = false;
		gameEx = new ImageIcon(getClass().getResource("/img/intro.png")).getImage();

		createInitialObstacles();
		setIntroTimer();
	}

	private void setIntroTimer() {
		Timer introTimer = new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameEx = null;
				repaint();
				timer.start();
			}
		});
		introTimer.setRepeats(false);
		introTimer.start();
	}

	private void createInitialObstacles() {
		for (int i = 0; i < 5; i++) {
			createNewObstacle();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!gameOver) {
			player.tick();
			moveObstacles();
			gametime++;
			obstacleDelay--;

			if (lastObstaclePosition >= 200) {
				createNewObstacle();
			}

			if (obstacleDelay == 0) {
				obstacleCreatedCount++;
				if (obstacleCreatedCount > 2) {
					consecutiveObstacles = 0;
					obstacleCreatedCount = 0;
				}
				obstacleDelay = random.nextInt(50) + 40;
			}

			checkCollisions();
			checkGameStatus();
			repaint();
		}
	}

	private void createNewObstacle() {
		int obstacleType = random.nextInt(3);
		int x = 800;
		int y = 300;
		Obstacle newObstacle;

		switch (obstacleType) {
		case 0:
			newObstacle = new Alcohol(x, y);
			break;
		case 1:
			newObstacle = new Bed(x, y);
			break;
		default:
			newObstacle = new Games(x, y);
			break;
		}

		if (obstacles.isEmpty()) {
			obstacles.add(newObstacle);
		} else {
			Obstacle lastObstacle = obstacles.get(obstacles.size() - 1);
			int gap = random.nextInt(100) + 100;
			newObstacle.setX((int) (lastObstacle.getX() + lastObstacle.getWidth() + gap));
			obstacles.add(newObstacle);
		}
		lastObstaclePosition = 0;
	}

	private void moveObstacles() {
		for (int i = 0; i < obstacles.size(); i++) {
			Obstacle obstacle = obstacles.get(i);
			obstacle.x -= 5;
			if (i == obstacles.size() - 1) {
				lastObstaclePosition += 5;
			}
		}
	}

	private void checkCollisions() {
		if (!obstacles.isEmpty()) {
			Obstacle firstObstacle = obstacles.get(0);

			if (player.intersects(firstObstacle)) {
				obstacles.remove(0);
				score -= 5;
			} else if (player.x > firstObstacle.x + firstObstacle.width) {
				obstacles.remove(0);
				score += 5;
			}
		}
	}

	private void checkGameStatus() {
		if ((gametime / 60) >= 20) {
			if (score > 0) {
				gameOver = true;
				String[] options = { "Next Game" };
				int choice = JOptionPane.showOptionDialog(this, "게임 성공! 점수: " + score, "Game Over",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				if (choice == 0) {
					frame.dispose();
					nextProject();
				}

			} else {
				presentRetryDialog("게임 실패! 점수: " + score);
			}
		} else if (score < 0) {
			presentRetryDialog("게임 실패! 점수: " + score);
		}
	}

	private void presentRetryDialog(String message) {
		gameOver = true;
		String[] options = { "Retry" };
		int choice = JOptionPane.showOptionDialog(this, message, "Game Over", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		if (choice == 0) {
			// 게임 재시작
			retryGame();
		}
	}

	private void retryGame() {
		// 게임 관련 변수 및 데이터 초기화
		player = new Student(100, 300); // 플레이어 위치 초기화
		obstacles.clear(); // 장애물 삭제
		createInitialObstacles(); // 초기 장애물 생성
		gametime = 0; // 시간 초기화
		score = 0; // 점수 초기화
		gameOver = false; // 게임오버 상태 초기화
		timer.restart(); // 이머 다시 시작
	}

	private void nextProject() {
		try {
			String basePath = System.getProperty("user.dir") + "\\src";
			String otherPackageName = "Pacman";
			String outputDir = System.getProperty("user.dir") + "\\bin";
			String otherProjectFile = "PacmanGame";
			String gamePackageName = "Pacman";

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
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (gameEx != null) {
			g.drawImage(gameEx, 0, 0, getWidth(), getHeight(), this);
		} else {
			player.draw(g);
			for (Obstacle obstacle : obstacles) {
				obstacle.draw(g);
			}

			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.drawString("time: " + (20 - gametime / 60), 600, 50);
			g.drawString("score: " + score, 600, 80);

			g.setColor(Color.black);
			g.fillRect(0, 353, getWidth(), 355);
			g.setColor(Color.PINK);
			g.fillRect(0, 355, getWidth(), 245);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.jump();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}