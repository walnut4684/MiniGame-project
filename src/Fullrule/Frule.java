package Fullrule;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Frule {
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
            BufferedImage backgroundImage = ImageIO.read(Frule.class.getResourceAsStream("/bgbtn/etrule.png"));
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

        ImageIcon buttonIcon = new ImageIcon(Frule.class.getResource("/bgbtn/skip.gif"));
        JButton imageButton = new JButton(buttonIcon);
        imageButton.setPreferredSize(new Dimension(62,30));
        imageButton.setBorderPainted(false);
        imageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeNextJavaFile(frame);
            }
        });

        // Set the layout to null to enable manual positioning
        frame.setLayout(null);

        // Set the button's position and size
        imageButton.setBounds(650, 460, 62, 30);

        // Add the button to the JFrame
        frame.add(imageButton);

        frame.setVisible(true);
    }

    private static void executeNextJavaFile(JFrame frame) {
        // 패키지 명과 클래스명을 아래와 같이 변경합니다.
        String packageName = "rungame";
        String className = "Main";

        try {
            Class<?> cls = Class.forName(packageName + "." + className);
            Object instance = cls.getDeclaredConstructor().newInstance();
            cls.getMethod("main", String[].class).invoke(instance, new Object[]{new String[]{}});

            // Close the current JFrame
            frame.dispose();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error running the next java file.");
        }
    }
}

