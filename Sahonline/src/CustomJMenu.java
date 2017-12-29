import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.ObjectOutputStream;

public class CustomJMenu extends JMenuBar {
    public CustomJMenu(JFrame checkerBoard, ObjectOutputStream out) {
        JMenu file = new JMenu("Menu");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        JMenuItem eMenuItem2 = new JMenuItem("About");
        eMenuItem2.setMnemonic(KeyEvent.VK_A);
        eMenuItem2.setToolTipText("About");
        eMenuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(checkerBoard, "Joc facut de Sichitiu Marian");
            }
        });

        JMenuItem eMenuItem3 = new JMenuItem("Reset");
        eMenuItem3.setMnemonic(KeyEvent.VK_R);
        eMenuItem3.setToolTipText("Reset");
        eMenuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    out.writeObject("RESET");
                    // display();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                checkerBoard.dispose();
            }
        });

        file.add(eMenuItem);
        file.add(eMenuItem2);
        file.add(eMenuItem3);
        this.add(file);
    }
}