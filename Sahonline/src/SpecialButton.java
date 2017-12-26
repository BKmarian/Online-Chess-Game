import javax.swing.*;
import java.awt.*;

class SpecialButton extends JButton {

    private boolean opaque;
    private Color color;
    private ImageIcon image;

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

    @Override
    public boolean isOpaque() {
        return opaque;
    }

    @Override
    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ImageIcon getImage() {
        return image;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

}