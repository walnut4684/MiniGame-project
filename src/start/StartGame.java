package start;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class StartGame {
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
            BufferedImage backgroundImage = ImageIO.read(StartGame.class.getResourceAsStream("/bgbtn/startbg.png"));
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

        ImageIcon buttonIcon = new ImageIcon(StartGame.class.getResource("/bgbtn/start.gif"));
        JButton imageButton = new JButton(buttonIcon);
        imageButton.setPreferredSize(new Dimension(70, 30));
        imageButton.setBorderPainted(false);
        imageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeNextJavaFile(frame);
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

    private static void executeNextJavaFile(JFrame frame) {
        // 패키지 명과 클래스명을 아래와 같이 변경합니다.
        String packageName = "Fullrule";
        String className = "Frule";
        
        try {
            Class<?> cls = Class.forName(packageName + "." + className);
            Object instance = cls.getDeclaredConstructor().newInstance();
            cls.getMethod("main", String[].class).invoke(instance, new Object[] {new String[] {}});
            
            // Close the current JFrame
            frame.dispose();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error running the next java file.");
        }
    }
}
