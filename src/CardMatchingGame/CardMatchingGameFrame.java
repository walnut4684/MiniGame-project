package CardMatchingGame;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardMatchingGameFrame extends JFrame {
	private JPanel gamePanel;
	private Card[] cards;
	private Card firstCard;
	private Card secondCard;
	private Timer timer;
	private int timeLeft;
	private JTextField timeField;

	private class TimerUpdateListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			if (!gamePanel.isVisible()) {
				return;
			}

			timeLeft--;
			timeField.setText("Time: " + timeLeft);

			if (timeLeft == 0) {
				timer.stop();
				for (int i = 0; i < cards.length; i++) {
					cards[i].setEnabled(false);
				}
				showGameOverPanel();
			}
		}

		private void showGameOverPanel() {
			CardMatchingGameFrame frame = CardMatchingGameFrame.this;

			JDialog gameOverDialog = new JDialog(frame, "Game Over", true);
			gameOverDialog.setSize(300, 150);
			gameOverDialog.setLocationRelativeTo(frame);

			JPanel messageAndButtonPanel = new JPanel(new BorderLayout());

			JLabel gameOverLabel = new JLabel("<html>시간 초과!<br/>F학점으로 재수강을 시작합니다.</html>");
			gameOverLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 18));
			gameOverLabel.setHorizontalAlignment(JLabel.CENTER);

			JPanel buttonPanel = new JPanel(); // 버튼 패널 추가
			JButton restartButton = new JButton("재수강");
			restartButton.setPreferredSize(new Dimension(100, 30)); // 버튼 크기 변경
			restartButton.addActionListener(evt -> {
				gameOverDialog.dispose();
				frame.dispose();
				frame.restartProject();
			});

			buttonPanel.add(restartButton); // 버튼 패널에 버튼 추가

			messageAndButtonPanel.add(gameOverLabel, BorderLayout.CENTER);
			messageAndButtonPanel.add(buttonPanel, BorderLayout.SOUTH);

			gameOverDialog.getContentPane().add(messageAndButtonPanel, BorderLayout.CENTER);

			gameOverDialog.setVisible(true);
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

	private TimerUpdateListener timerUpdateListener = new TimerUpdateListener();

	private class CardClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Card selectedCard = (Card) e.getSource();

			if (selectedCard.isMatched() || selectedCard.isSelected()) {
				return; // 이미 선택된 카드를 클릭한 경우 무시
			}

			selectedCard.showIcon();
			selectedCard.setSelected(true);

			if (firstCard == null) {
				firstCard = selectedCard;
			} else {
				secondCard = selectedCard;

				if (firstCard.getCardNumber() == secondCard.getCardNumber()) {
					// 카드 일치할 때
					firstCard.setMatched(true);
					secondCard.setMatched(true);

					firstCard = null;
					secondCard = null;

				} else {
					// 카드 일치하지 않을 떄
					Timer timer = new Timer(200, e1 -> flipCardsBack());
					timer.setRepeats(false);
					timer.start();

				}

				boolean allMatched = true;
				for (Card c : cards) {
					if (!c.isMatched()) {
						allMatched = false;
						break;
					}
				}

				if (allMatched) {
					timer.stop();

					JDialog congratsDialog = new JDialog();
					congratsDialog.setSize(300, 150);
					congratsDialog.setLocationRelativeTo(null);
					congratsDialog.setLayout(new BorderLayout());

					JPanel congratsPanel = new JPanel();
					congratsPanel.setLayout(new BoxLayout(congratsPanel, BoxLayout.Y_AXIS));

					JLabel congratsLabel = new JLabel("<html>축하합니다!<br/>다음 게임으로 넘어값니다.</html>");
					congratsLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
					congratsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
					congratsLabel.setHorizontalAlignment(JLabel.CENTER);

					JToggleButton playAgainButton = new JToggleButton("다음 게임");
					playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);

					playAgainButton.addActionListener(evt -> {
						if (playAgainButton.isSelected()) {
							congratsDialog.dispose();
							dispose();
							runOtherProject();
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
		}

		private void runOtherProject() {
			try {
				String basePath = System.getProperty("user.dir") + "\\src";
				String otherPackageName = "Omok";
				String outputDir = System.getProperty("user.dir") + "\\bin";
				String otherProjectFile = "GameGUI";
				String gamePackageName = "Omok";

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

		private void flipCardsBack() {
			firstCard.showBack();
			secondCard.showBack();

			firstCard.setSelected(false);
			secondCard.setSelected(false);

			firstCard = null;
			secondCard = null;
		}

	}

	public CardMatchingGameFrame() {

		setTitle("CardMatchingGame");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		timeLeft = GameConstants.GAME_DURATION;
		timeField = new JTextField("Time: " + timeLeft);
		timeField.setEditable(false);
		timeField.setVisible(false);

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		topPanel.add(timeField, BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);

		List<Integer> indexes = new ArrayList<>();

		for (int i = 0; i < GameConstants.GRID_SIZE * GameConstants.GRID_SIZE; i++) {
			indexes.add(i / 2);
		}

		Collections.shuffle(indexes);

		gamePanel = new JPanel(new GridLayout(GameConstants.GRID_SIZE, GameConstants.GRID_SIZE));

		// cardIcons 배열 선언
		String[] cardIcons = { "/cards/icon1.png", "/cards/icon2.png", "/cards/icon3.png", "/cards/icon4.png",
				"/cards/icon5.png", "/cards/icon6.png", "/cards/icon7.png", "/cards/icon8.png" };

		cards = new Card[GameConstants.GRID_SIZE * GameConstants.GRID_SIZE];

		for (int i = 0; i < GameConstants.GRID_SIZE * GameConstants.GRID_SIZE; i++) {
			int index = indexes.get(i);
			cards[i] = new Card(index, cardIcons[index]);
			gamePanel.add(cards[i]);
			cards[i].addActionListener(new CardClickListener());
		}

		JLabel rulesLabel = new JLabel();
		gamePanel.setVisible(false);
		add(gamePanel, BorderLayout.CENTER);

		// 이미지 로딩 및 크기 조절
		ImageIcon originalRulesImage = new ImageIcon(getClass().getResource("/cards/CardMatchingGameRule.png"));
		Image scaledRulesImage = originalRulesImage.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
		ImageIcon rulesImage = new ImageIcon(scaledRulesImage);

		rulesLabel.setIcon(rulesImage);
		rulesLabel.setOpaque(true);
		rulesLabel.setBackground(Color.WHITE);
		rulesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rulesLabel.setVerticalAlignment(SwingConstants.CENTER);
		add(rulesLabel, BorderLayout.CENTER);
		setVisible(true);

		Timer showRulesTimer = new Timer(3000, e -> {
			remove(rulesLabel);
			add(gamePanel);

			timeField.setVisible(true);
			gamePanel.setVisible(true);

			revalidate();
			repaint();

			timer = new Timer(GameConstants.TIMER_DELAY, timerUpdateListener);
			timer.start();
		});
		showRulesTimer.setRepeats(false);
		showRulesTimer.start();

		setVisible(true);
	}
}