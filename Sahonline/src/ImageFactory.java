import javax.swing.*;

public class ImageFactory {
    public static ImageIcon getImageIcon(String name) {
        switch (name) {
            case "wpion":
                return new ImageIcon("images/wpion.png");
            case "bpion":
                return new ImageIcon("images/bpion.png");
            case "btura":
                return new ImageIcon("images/btura.png");
            case "wture":
                return new ImageIcon("images/vture.png");
            case "wcal":
                return new ImageIcon("images/vcal.png");
            case "bcal":
                return new ImageIcon("images/wpion.png");
            case "wnebun":
                return new ImageIcon("images/vnebun.png");
            case "bnebun":
                return new ImageIcon("images/bnebun.png");
            case "brege":
                return new ImageIcon("images/brege.png");
            case "wrege":
                return new ImageIcon("images/wrege.png");
            case "bregina":
                return new ImageIcon("images/bregina.png");
            case "wregina":
                return new ImageIcon("images/wregina.png");
            default:
                return null;

        }
    }
}
