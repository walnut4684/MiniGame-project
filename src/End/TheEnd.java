package End;

import javax.imageio.ImageIO;
import javax.swing.*;

import start.StartGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TheEnd {
     public static void main(String[] args) {
            createAndShowStartGameGUI();
        }

        private static void createAndShowStartGameGUI() {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);

            try {
                BufferedImage backgroundImage = ImageIO.read(TheEnd.class.getResourceAsStream("/bgbtn/end.png"));
                JPanel panel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    }
                };
                frame.setContentPane(panel);
            } catch (IOException e) {
                System.out.println("Cannot read image file");
                e.printStackTrace();
            }

            ImageIcon buttonIcon = new ImageIcon(TheEnd.class.getResource("/bgbtn/gohome.gif"));
            JButton imageButton = new JButton(buttonIcon);
            imageButton.setPreferredSize(new Dimension(120, 36));
            imageButton.setBorderPainted(false);
            imageButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose(); // 여기 수정
                }
            });

            // Set layout to GridBagLayout
            frame.getContentPane().setLayout(new GridBagLayout());

            // Add constraints for button to be placed at the center
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            constraints.gridheight = 1;
            constraints.weightx = 1.0;
            constraints.weighty = 1.0;
            constraints.anchor = GridBagConstraints.CENTER;
            frame.getContentPane().add(imageButton, constraints);

            frame.setVisible(true);
        }

	// 기존 executeNextJavaFile() 메서드 삭제
}
