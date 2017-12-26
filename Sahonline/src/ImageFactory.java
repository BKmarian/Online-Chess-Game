import javax.swing.*;

public class ImageFactory {
    public static ImageIcon getImageIcon(String name) {
        switch (name) {
            case "wpion":
                return new ImageIcon("Sahonline/images/wpion.png");
            case "bpion":
                return new ImageIcon("Sahonline/images/bpion.png");
            case "btura":
                return new ImageIcon("Sahonline/images/btura.png");
            case "wtura":
                return new ImageIcon("Sahonline/images/wtura.png");
            case "wcal":
                return new ImageIcon("Sahonline/images/wcal.png");
            case "bcal":
                return new ImageIcon("Sahonline/images/bcal.png");
            case "wnebun":
                return new ImageIcon("Sahonline/images/wnebun.png");
            case "bnebun":
                return new ImageIcon("Sahonline/images/bnebun.png");
            case "brege":
                return new ImageIcon("Sahonline/images/brege.png");
            case "wrege":
                return new ImageIcon("Sahonline/images/wrege.png");
            case "bregina":
                return new ImageIcon("Sahonline/images/bregina.png");
            case "wregina":
                return new ImageIcon("Sahonline/images/wregina.png");
            default:
                return null;

        }
    }
}
