import javax.swing.*;
import java.awt.*;

class SpecialButton extends JButton {
    boolean opaque;
    Color color;
    ImageIcon image;

    public SpecialButton(Color color, ImageIcon image) {
        super();
        super.setOpaque(true);
        super.setBackground(color);
        super.setIcon(image);
    }

    public SpecialButton(Color color) {
        super();
        super.setBackground(color);
        super.setOpaque(true);
    }
}