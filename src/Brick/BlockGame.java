package Brick;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

import Brick.BlockGame.MyFrame.Block;

public class BlockGame {

	private JFrame frame;
	public int startX;

	static class MyFrame extends JFrame {

		// constant
		static int BALL_WIDTH = 20;
		static int BALL_HEIGHT = 20;
		static int BLOCK_ROWS = 5; // 수정: 블록의 행 수
		static int BLOCK_COLUMNS = 9; // 수정: 블록의 열 수
		static int BLOCK_WIDTH = 40;
		static int BLOCK_HEIGHT = 20;
		static int BLOCK_GAP = 3;
		static int BAR_WIDTH = 80;
		static int BAR_HEIGHT = 20;
		static int CANVAS_WIDTH = 400; // 수정: 블록과 간격을 고려한 너비
		static int CANVAS_HEIGHT = 600;
		// variable
		static MyPanel myPanel = null;
		static int score = 0;
		static Timer timer = null;
		static Block[][] blocks = new Block[BLOCK_ROWS][BLOCK_COLUMNS];
		static Bar bar = new Bar();
		static Ball ball = new Ball();
		static int barXTarget = bar.x; // Target Value - interpolation
		static int dir = 0; // 0 : Up-Right 1 : Down-Rigth 2 : Up-Left 3 : Down-Left
		static int ballSpeed = 5;
		static boolean isGameFinish = false;

		static class Ball {
			int x = CANVAS_WIDTH / 2 - BALL_WIDTH / 2;
			int y = CANVAS_HEIGHT / 2 - BALL_HEIGHT / 2;
			int width = BALL_WIDTH;
			int height = BALL_HEIGHT;

			Point getCenter() {
				return new Point(x + (BALL_WIDTH / 2), y + (BALL_HEIGHT / 2));
			}

			Point getBottomCenter() {
				return new Point(x + (BALL_WIDTH / 2), y + (BALL_HEIGHT));
			}

			Point getTopCenter() {
				return new Point(x + (BALL_WIDTH / 2), y);
			}

			Point getLeftCenter() {
				return new Point(x, y + (BALL_HEIGHT / 2));
			}

			Point getRightCenter() {
				return new Point(x + (BALL_WIDTH), y + (BALL_HEIGHT / 2));
			}
		}

		static class Bar {
			int x = CANVAS_WIDTH / 2 - BAR_WIDTH / 2;
			int y = CANVAS_HEIGHT - 100;
			int width = BAR_WIDTH;
			int height = BAR_HEIGHT;
		}

		static class Block {
			int x = 0;
			int y = 0;
			int width = BLOCK_WIDTH;
			int height = BLOCK_HEIGHT;
			int color = 0; // 0:white 1:yellow 2:blue 3:mazanta 4:red
			boolean isHidden = false; // after collision, block will be hidden.
		}

		static class MyPanel extends JPanel { // VANVAS for Draw!
			public MyPanel() {

				this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
				this.setBackground(Color.BLACK);
			}

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2d = (Graphics2D) g;

				drawUI(g2d);
			}

			private void drawUI(Graphics2D g2d) {
				// draw Blocks
				for (int i = 0; i < BLOCK_ROWS; i++) {
					for (int j = 0; j < BLOCK_COLUMNS; j++) {
						if (blocks[i][j].isHidden) {
							continue;
						}
						if (blocks[i][j].color == 0) {
							g2d.setColor(Color.WHITE);
						} else if (blocks[i][j].color == 1) {
							g2d.setColor(Color.YELLOW);
						} else if (blocks[i][j].color == 2) {
							g2d.setColor(Color.BLUE);
						} else if (blocks[i][j].color == 3) {
							g2d.setColor(Color.MAGENTA);
						} else if (blocks[i][j].color == 4) {
							g2d.setColor(Color.RED);
						}
						g2d.fillRect(blocks[i][j].x, blocks[i][j].y, blocks[i][j].width, blocks[i][j].height);
					}

					// draw score
					g2d.setColor(Color.WHITE);
					g2d.setFont(new Font("TimesRoman", Font.BOLD, 20));
					g2d.drawString("score : " + score, CANVAS_WIDTH / 2 - 30, 20);
					if (isGameFinish) {
						g2d.setColor(Color.RED);
						g2d.drawString("Game Finished!", CANVAS_WIDTH / 2 - 55, 50);
					}

					// draw Ball
					g2d.setColor(Color.WHITE);
					g2d.fillOval(ball.x, ball.y, BALL_WIDTH, BALL_HEIGHT);

					// draw Bar
					g2d.setColor(Color.WHITE);
					g2d.fillRect(bar.x, bar.y, bar.width, bar.height);
				}
			}
		}

		public MyFrame(String title) {

			super(title);

			JFrame frame = new JFrame("");

			// 이미지 아이콘 생성
			ImageIcon imageIcon = new ImageIcon("images/게임시작화면.jpg"); // 이미지 파일 경로로 변경

			// JLabel에 이미지 아이콘 설정
			JLabel label = new JLabel(imageIcon);

			// JLabel을 JFrame에 추가
			frame.getContentPane().add(label);

			// JFrame 크기 설정
			frame.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());

			// JFrame을 화면 중앙에 배치
			frame.setLocationRelativeTo(null);

			// JFrame을 표시
			frame.setVisible(true);

			// 3초 대기
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// JFrame 종료
			frame.dispose();

			this.setVisible(true);
			this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
			this.setLocationRelativeTo(null); // 프레임을 화면 중앙에 배치
			// this.setLayout(new BorderLayout());
			// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			initData();

			myPanel = new MyPanel();
			this.add("Center", myPanel);

			setKeyListener();
			startTimer();
		}

		public void initData() {
			for (int i = 0; i < BLOCK_ROWS; i++) {
				for (int j = 0; j < BLOCK_COLUMNS; j++) {
					blocks[i][j] = new Block();
					blocks[i][j].x = BLOCK_WIDTH * j + BLOCK_GAP * j;
					blocks[i][j].y = 100 + BLOCK_HEIGHT * i + BLOCK_GAP * i;
					blocks[i][j].width = BLOCK_WIDTH;
					blocks[i][j].height = BLOCK_HEIGHT;
					blocks[i][j].color = 4 - i; // 0:white 1:yellow 2:blue 3:mazanta 4:red
					blocks[i][j].isHidden = false;
				}
			}
		}

		public void setKeyListener() {
			this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) { // Key Event
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						System.out.println("Pressed Left Key");
						barXTarget -= 20;
						if (bar.x < barXTarget) { // repeate key pressed...
							barXTarget = bar.x;
						}
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						System.out.println("Pressed Right Key");
						barXTarget += 20;
						if (bar.x > barXTarget) { // repeate key pressed...
							barXTarget = bar.x;
						}
					}
				}
			});
		}

		public void startTimer() {
			timer = new Timer(20, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) { // Timer Event
					movement();
					checkCollision(); // Wall, Bar
					checkCollisionBlock(); // Blocks 50
					myPanel.repaint(); // Redraw!
					isGameFinish();

				}
			});
			timer.start(); // Start Timer!
		}

		public void isGameFinish() {
			// Game Success!
			int count = 0;
			for (int i = 0; i < BLOCK_ROWS; i++) {
				for (int j = 0; j < BLOCK_COLUMNS; j++) {
					Block block = blocks[i][j];
					if (block.isHidden)
						count++;
				}
			}
			if (count == BLOCK_ROWS * BLOCK_COLUMNS) {
				// Game Finished!
				timer.stop();
				isGameFinish = true;

				JDialog congratsDialog = new JDialog();
				congratsDialog.setSize(300, 150);
				congratsDialog.setLocationRelativeTo(null);
				congratsDialog.setLayout(new BorderLayout());

				JPanel congratsPanel = new JPanel();
				congratsPanel.setLayout(new BoxLayout(congratsPanel, BoxLayout.Y_AXIS));

				JLabel congratsLabel = new JLabel("<html>성   공<br/>다음 게임으로 넘어갑니다.</html>");
				congratsLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
				congratsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				congratsLabel.setHorizontalAlignment(JLabel.CENTER);

				JToggleButton playAgainButton = new JToggleButton("다음 게임");
				playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);

				playAgainButton.addActionListener(evt -> {
					if (playAgainButton.isSelected()) {
						congratsDialog.dispose();
						dispose();
						nextProject();
					} else {
						System.exit(0);
					}
				});

				congratsPanel.add(congratsLabel);
				congratsPanel.add(Box.createVerticalStrut(20));
				congratsPanel.add(playAgainButton);

				congratsDialog.add(congratsPanel, BorderLayout.CENTER);
				congratsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				congratsDialog.setVisible(true);
			}
		}

		private void nextProject() {
			try {
				String basePath = System.getProperty("user.dir") + "\\src";
				String otherPackageName = "CardMatchingGame";
				String outputDir = System.getProperty("user.dir") + "\\bin";
				String otherProjectFile = "CardMatchingGame";
				String gamePackageName = "CardMatchingGame";

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

		public void movement() {
			if (bar.x < barXTarget) {
				bar.x += 5;
			} else if (bar.x > barXTarget) {
				bar.x -= 5;
			}

			if (dir == 0) { // 0 : Up-Right
				ball.x += ballSpeed;
				ball.y -= ballSpeed;
			} else if (dir == 1) { // 1 : Down-Right
				ball.x += ballSpeed;
				ball.y += ballSpeed;
			} else if (dir == 2) { // 2 : Up-Left
				ball.x -= ballSpeed;
				ball.y -= ballSpeed;
			} else if (dir == 3) { // 3 : Down-Left
				ball.x -= ballSpeed;
				ball.y += ballSpeed;
			}

		}

		public boolean duplRect(Rectangle rect1, Rectangle rect2) {
			return rect1.intersects(rect2); // check two Rect is Duplicated!
		}

		public void checkCollision() {
			if (dir == 0) { // 0 : Up-Right
				// Wall
				if (ball.y < 0) { // wall upper
					dir = 1;
				}
				if (ball.x > CANVAS_WIDTH - BALL_WIDTH) { // wall right
					dir = 2;
				}
				// Bar - none

			} else if (dir == 1) { // 1 : Down-Right
				// Wall
				if (ball.y > CANVAS_HEIGHT - BALL_HEIGHT - BALL_HEIGHT) { // wall bottom

					// frame.dispose();

					showGameOverPanel();
					// game reset
				}
				if (ball.x > CANVAS_WIDTH - BALL_WIDTH) { // wall right
					dir = 3;
				}
				// Bar
				if (ball.getBottomCenter().y >= bar.y) {
					if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
							new Rectangle(bar.x, bar.y, bar.width, bar.height))) {
						dir = 0;
					}
				}
			} else if (dir == 2) { // 2 : Up-Left
				// Wall
				if (ball.y < 0) { // wall upper
					dir = 3;
				}
				if (ball.x < 0) { // wall left
					dir = 0;
				}
				// Bar - none

			} else if (dir == 3) { // 3 : Down-Left
				// Wall
				if (ball.y > CANVAS_HEIGHT - BALL_HEIGHT - BALL_HEIGHT) { // wall bottom
					dir = 2;
					showGameOverPanel();

				}
				if (ball.x < 0) { // wall left
					dir = 1;
				}
				// Bar
				if (ball.getBottomCenter().y >= bar.y) {
					if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
							new Rectangle(bar.x, bar.y, bar.width, bar.height))) {
						dir = 2;
					}
				}
			}
		}

		public void showGameOverPanel() {
			JDialog gameOverDialog = new JDialog();
			gameOverDialog.setSize(300, 150);
			gameOverDialog.setLocationRelativeTo(null);
			gameOverDialog.setLayout(new BorderLayout());

			JPanel gameOverPanel = new JPanel();
			gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));

			JLabel gameOverLabel = new JLabel("<html>실   패<br/>다시 도전해주세요.</html>");
			gameOverLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
			gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			gameOverLabel.setHorizontalAlignment(JLabel.CENTER);

			JButton playAgainButton = new JButton("Restart");
			playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);

			playAgainButton.addActionListener(evt -> {
				gameOverDialog.dispose();
				dispose();
				restartProject();
			});

			gameOverPanel.add(gameOverLabel);
			gameOverPanel.add(Box.createVerticalStrut(20));
			gameOverPanel.add(playAgainButton);
			gameOverDialog.add(gameOverPanel, BorderLayout.CENTER);
			gameOverDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			gameOverDialog.setVisible(true);
		}

		public void checkCollisionBlock() {
			// 0 : Up-Right 1 : Down-Rigth 2 : Up-Left 3 : Down-Left
			for (int i = 0; i < BLOCK_ROWS; i++) {
				for (int j = 0; j < BLOCK_COLUMNS; j++) {
					Block block = blocks[i][j];
					if (block.isHidden == false) {
						if (dir == 0) { // 0 : Up-Right
							if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									new Rectangle(block.x, block.y, block.width, block.height))) {
								if (ball.x > block.x + 2 && ball.getRightCenter().x <= block.x + block.width - 2) {
									// block bottom collision
									dir = 1;
								} else {
									// block left collision
									dir = 2;
								}
								block.isHidden = true;
								if (block.color == 0) {
									score += 10;
								} else if (block.color == 1) {
									score += 20;
								} else if (block.color == 2) {
									score += 30;
								} else if (block.color == 3) {
									score += 40;
								} else if (block.color == 4) {
									score += 50;
								}
							}
						} else if (dir == 1) { // 1 : Down-Rigth
							if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									new Rectangle(block.x, block.y, block.width, block.height))) {
								if (ball.x > block.x + 2 && ball.getRightCenter().x <= block.x + block.width - 2) {
									// block top collision
									dir = 0;
								} else {
									// block left collision
									dir = 3;
								}
								block.isHidden = true;
								if (block.color == 0) {
									score += 10;
								} else if (block.color == 1) {
									score += 20;
								} else if (block.color == 2) {
									score += 30;
								} else if (block.color == 3) {
									score += 40;
								} else if (block.color == 4) {
									score += 50;
								}
							}
						} else if (dir == 2) { // 2 : Up-Left
							if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									new Rectangle(block.x, block.y, block.width, block.height))) {
								if (ball.x > block.x + 2 && ball.getRightCenter().x <= block.x + block.width - 2) {
									// block bottom collision
									dir = 3;
								} else {
									// block right collision
									dir = 0;
								}
								block.isHidden = true;
								if (block.color == 0) {
									score += 10;
								} else if (block.color == 1) {
									score += 20;
								} else if (block.color == 2) {
									score += 30;
								} else if (block.color == 3) {
									score += 40;
								} else if (block.color == 4) {
									score += 50;
								}
							}
						} else if (dir == 3) { // 3 : Down-Left
							if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									new Rectangle(block.x, block.y, block.width, block.height))) {
								if (ball.x > block.x + 2 && ball.getRightCenter().x <= block.x + block.width - 2) {
									// block top collision
									dir = 2;
								} else {
									// block right collision
									dir = 1;
								}
								block.isHidden = true;
								if (block.color == 0) {
									score += 10;
								} else if (block.color == 1) {
									score += 20;
								} else if (block.color == 2) {
									score += 30;
								} else if (block.color == 3) {
									score += 40;
								} else if (block.color == 4) {
									score += 50;
								}
							}
						}
					}
				}
			}
		}

	}

	public static void main(String[] args) {
		new MyFrame("Block Game");
	}

}
