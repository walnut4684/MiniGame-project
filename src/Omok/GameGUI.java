package Omok;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GameGUI extends JFrame {
	private static final int FRAME_WIDTH = 800;
	private static final int FRAME_HEIGHT = 600;
	private static final int BOARD_SIZE = 15;
	private static final Color PLAYER_COLOR = Color.BLACK;
	private static final Color AI_COLOR = Color.WHITE;
	private static final int IMAGE_DELAY = 3000; // 3 seconds

	private Board board;
	private AIPlayer aiPlayer;
	private UserPlayer userPlayer;

	private JButton[][] buttons;

	public GameGUI() {

		setTitle("Concave Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
		setLocationRelativeTo(null);

		initializeBoard();
		initializeButtons();
		setVisible(true);
		showImage();

	}

	private void showImage() {
		ImageIcon imageIcon = new ImageIcon("src\\Omok\\Game Rule.png");
		JLabel imageLabel = new JLabel(imageIcon);

		JDialog dialog = new JDialog(this, "Omok Game Rule", true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.getContentPane().add(imageLabel);
		dialog.pack();
		dialog.setLocationRelativeTo(this);

		Timer timer = new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				startGame();
			}
		});
		timer.setRepeats(false);
		timer.start();

		dialog.setVisible(true);
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

	private void startGame() {
		setSize(FRAME_WIDTH, FRAME_HEIGHT); // Adjust the frame size if necessary

		getContentPane().removeAll(); // Clear the existing content

		// Create a new layout with the desired board size
		setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));

		initializeBoard();
		initializeButtons();

		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				buttons[i][j].setEnabled(true);
			}
		}

		revalidate(); // Revalidate the layout
		repaint(); // Repaint the GUI
	}

	private void initializeBoard() {
		board = new Board();
		aiPlayer = new AIPlayer(Board.WHITE);
		userPlayer = new UserPlayer(Board.BLACK);
	}

	private void initializeButtons() {
		buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				buttons[i][j] = new JButton();
				buttons[i][j].setBackground(Color.GRAY);
				buttons[i][j].addActionListener(new ButtonClickListener(i, j));
				add(buttons[i][j]);
			}
		}

	}

	private class ButtonClickListener implements ActionListener {
		GameGUI frame = GameGUI.this;
		private int row;
		private int col;

		public ButtonClickListener(int row, int col) {
			this.row = row;
			this.col = col;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (board.getCell(row, col) != Board.EMPTY) {
				JOptionPane.showMessageDialog(GameGUI.this, "이미 놓아진 위치입니다. 다른 위치를 선택하세요", "Invalid Move",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			buttons[row][col].setBackground(PLAYER_COLOR);
			board.setCell(row, col, Board.BLACK);

			 if (board.isGameOver()) {
	                int option = JOptionPane.showOptionDialog(GameGUI.this, "모두 클리어했습니다. 종강을 축하합니다♩♪♬", "종강",
	                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"집에 가기"},
	                            "게임 끝내기");
	                if (option == 0) {
	                    executeTheEndClass();
	                    dispose();
	                }
	            }
			aiPlayer.makeMove(board);
			int aiRow = board.getLastMoveRow();
			int aiCol = board.getLastMoveCol();
			buttons[aiRow][aiCol].setBackground(AI_COLOR);

			if (board.isGameOver()) {
				int option = JOptionPane.showOptionDialog(GameGUI.this, "AI 에게 졌습니다. 게임을 재시작하시겠습니까?", "재시작",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[] { "Restart" },
						"Restart");
				if (option == 0) {
					frame.dispose();
					restartProject();
				} else {
					// Perform any other actions or handle the choice accordingly
				}
			}
			
		}
	    private void executeTheEndClass() {
	        try {
	            String packageName = "End";
	            String className = "TheEnd";
	            Class<?> cls = Class.forName(packageName + "." + className);
	            Object instance = cls.getDeclaredConstructor().newInstance();
	            cls.getMethod("main", String[].class).invoke(instance, new Object[]{new String[]{}});
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("Error running the TheEnd class.");
	        }
	    }
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GameGUI();
			}
		});
	}
}
