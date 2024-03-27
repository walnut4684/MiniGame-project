package CardMatchingGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.Icon;

public class Card extends JButton {
	private int cardNumber;
    private Icon frontIcon;
    private Icon backIcon;
    private boolean isMatched;
    private boolean isSelected;

	public Card(int cardNumber, String iconPath) {
		this.cardNumber = cardNumber;
		setPreferredSize(new Dimension(100, 100));
		setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		setBackground(Color.WHITE);

		ImageIcon originalBackIcon = new ImageIcon(getClass().getResource("/cards/backicon.png"));
        Image scaledBackImage = originalBackIcon.getImage().getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
        this.backIcon = new ImageIcon(scaledBackImage);
        
        ImageIcon originalFrontIcon = new ImageIcon(getClass().getResource(iconPath));
        Image scaledFrontImage = originalFrontIcon.getImage().getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
        this.frontIcon = new ImageIcon(scaledFrontImage);

		this.isMatched = false;
		this.isSelected = false;
		setIcon(backIcon);
	}

	public int getCardNumber() {
        return cardNumber;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void showIcon() {
        setIcon(frontIcon);
    }

    public void showBack() {
        setIcon(backIcon);
    }

}
