import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;

public class GamePanel extends JPanel {
    private JLabel labelRand;
    private final int numberOfColums = 20; // for textfield
    private JPanel panelMatrix[][], gridPane, chatPane, sendTextPane;
    private Container panel, cardPanel;
    private JLabel labelChat;
    private JTextField tf;
    private JTextArea ta;
    private JFrame checkerBoard;
    private CardLayout cardLayout;
    private SpecialButton label[][];
    private JPanel loginPanel;
    private JButton login;
    private JLabel userLabel;
    private JLabel passwordLabel;
    private JTextField username;
    private JPasswordField password;
    public static final Color col1 = new Color(130, 120, 82);
    public static final Color col2 = new Color(255,255,255);

    public void createPanels(ObjectOutputStream out) {
        chatPane = new JPanel();
        gridPane = new JPanel();
        sendTextPane = new JPanel();
        gridPane.setSize(300, 300);
        chatPane.setSize(200, 300);
        sendTextPane.setSize(100, 100);
        labelChat = new JLabel("Enter");
        labelChat.setSize(50, 50);
        sendTextPane.setLayout(new FlowLayout());
        sendTextPane.add(labelChat);
        tf = new JTextField("");
        tf.setColumns(numberOfColums);
        tf.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    String outcome = tf.getText();
                    try {
                        out.writeObject("CHAT " + outcome);
                    } catch (IOException e1) {
                        // e1.printStackTrace();
                    }
                    tf.setText("");
                    ta.append("Eu: " + outcome);
                    ta.append("\n");
                }
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
            }

            @Override
            public void keyTyped(KeyEvent arg0) {

            }
        });
        sendTextPane.add(tf);
        tf.setBackground(Color.WHITE);
        chatPane.add(sendTextPane, BorderLayout.SOUTH);

        ta = new JTextArea("Bine ati venit! Bafta la joc !\n", 20, 30);
        chatPane.add(new JScrollPane(ta), BorderLayout.NORTH);
        ta.setEditable(false);

        label = new SpecialButton[ChessClient.rows][ChessClient.columns];
        // panel.setSize(1200, 600);
        gridPane.setLayout(new GridLayout(ChessClient.rows, ChessClient.columns));
        panel.add(gridPane, BorderLayout.WEST);
        panel.add(chatPane, BorderLayout.EAST);
        labelRand = new JLabel("Asteptati un oponent");
        panel.add(labelRand, "South");
        panelMatrix = new JPanel[ChessClient.rows][ChessClient.columns];
    }

    public void addButtons() {
        int i, j;
        Color temp;
        for (i = 0; i < ChessClient.rows; i++) {
            if (i % 2 == 0) {
                temp = col1;
            } else {
                temp = col2;
            }
            for (j = 0; j < ChessClient.columns; j++) {

                if (i != 0 && i != 1 && i != 6 && i != 7) {
                    label[i][j] = new SpecialButton(temp);
                }
                if (i == 1) {
                    label[i][j] = new SpecialButton(temp, ImageFactory.getImageIcon("wpion"));
                }
                if (i == 6) {
                    label[i][j] = new SpecialButton(temp, ImageFactory.getImageIcon("bpion"));
                }
                if (i == 7) {
                    switch (j) {
                        case 0:
                        case 7: {
                            label[i][j] = new SpecialButton(temp, ImageFactory.getImageIcon("btura"));
                            break;
                        }
                        case 1:
                        case 6: {
                            label[i][j] = new SpecialButton(temp, ImageFactory.getImageIcon("bcal"));
                            break;
                        }
                        case 2:
                        case 5: {
                            label[i][j] = new SpecialButton(temp, ImageFactory.getImageIcon("bnebun"));
                            break;
                        }
                        case 3: {
                            label[i][j] = new SpecialButton(temp, ImageFactory.getImageIcon("brege"));
                            break;
                        }
                        case 4:
                            label[i][j] = new SpecialButton(temp, ImageFactory.getImageIcon("bregina"));
                            break;
                    }
                }
                if (i == 0) {
                    switch (j) {
                        case 0:
                        case 7: {
                            label[i][j] = new SpecialButton(temp, ImageFactory.getImageIcon("wtura"));
                            break;
                        }
                        case 1:
                        case 6: {
                            label[i][j] = new SpecialButton(temp, ImageFactory.getImageIcon("wcal"));
                            break;
                        }
                        case 2:
                        case 5: {
                            label[i][j] = new SpecialButton(temp, ImageFactory.getImageIcon("wnebun"));
                            break;
                        }
                        case 3: {
                            label[i][j] = new SpecialButton(temp, ImageFactory.getImageIcon("wrege"));
                            break;
                        }
                        case 4: {
                            label[i][j] = new SpecialButton(temp, ImageFactory.getImageIcon("wregina"));
                            break;
                        }
                    }
                }
                if (temp.equals(col1)) {
                    temp = col2;

                } else {
                    temp = col1;
                }
                panelMatrix[i][j] = new JPanel(new BorderLayout());
                gridPane.add(panelMatrix[i][j]);
                panelMatrix[i][j].add(label[i][j]);
            }
        }
    }

    public void displayLoginBoard() throws UnknownHostException, IOException {

        cardLayout = new CardLayout();
        checkerBoard = new JFrame();
        checkerBoard.setResizable(false);
        cardPanel = new JPanel();
        checkerBoard.getContentPane().add(cardPanel);
        loginPanel = new JPanel();
        panel = new JPanel();
        checkerBoard.setVisible(true);
        checkerBoard.setSize(1200, 500);
        checkerBoard.setTitle("CheckerBoard");
        checkerBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardPanel.setLayout(cardLayout);
        login = new JButton("Login");
        userLabel = new JLabel("Username");
        passwordLabel = new JLabel("Password");
        username = new JTextField();
        password = new JPasswordField(10);
        username.setColumns(numberOfColums);
        password.setColumns(numberOfColums);
        loginPanel.add(userLabel);
        loginPanel.add(username);
        loginPanel.add(passwordLabel);
        loginPanel.add(password);
        loginPanel.add(login);
        cardPanel.add(loginPanel, "1");
        cardPanel.add(panel, "2");

    }
    public JButton getLogin() {
        return login;
    }

    public void setLogin(JButton login) {
        this.login = login;
    }

    public int getNumberOfColums() {
        return numberOfColums;
    }

    public JPanel[][] getPanelMatrix() {
        return panelMatrix;
    }

    public void setPanelMatrix(JPanel[][] panelMatrix) {
        this.panelMatrix = panelMatrix;
    }

    public JPanel getGridPane() {
        return gridPane;
    }

    public void setGridPane(JPanel gridPane) {
        this.gridPane = gridPane;
    }

    public JPanel getChatPane() {
        return chatPane;
    }

    public void setChatPane(JPanel chatPane) {
        this.chatPane = chatPane;
    }

    public JPanel getSendTextPane() {
        return sendTextPane;
    }

    public void setSendTextPane(JPanel sendTextPane) {
        this.sendTextPane = sendTextPane;
    }

    public Container getPanel() {
        return panel;
    }

    public void setPanel(Container panel) {
        this.panel = panel;
    }

    public Container getCardPanel() {
        return cardPanel;
    }

    public void setCardPanel(Container cardPanel) {
        this.cardPanel = cardPanel;
    }

    public JLabel getLabelChat() {
        return labelChat;
    }

    public void setLabelChat(JLabel labelChat) {
        this.labelChat = labelChat;
    }

    public JTextField getTf() {
        return tf;
    }

    public void setTf(JTextField tf) {
        this.tf = tf;
    }

    public JTextArea getTa() {
        return ta;
    }

    public void setTa(JTextArea ta) {
        this.ta = ta;
    }

    public JFrame getCheckerBoard() {
        return checkerBoard;
    }

    public void setCheckerBoard(JFrame checkerBoard) {
        this.checkerBoard = checkerBoard;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public void setCardLayout(CardLayout cardLayout) {
        this.cardLayout = cardLayout;
    }

    public SpecialButton[][] getLabel() {
        return label;
    }

    public void setLabel(SpecialButton[][] label) {
        this.label = label;
    }

    public JPanel getLoginPanel() {
        return loginPanel;
    }

    public void setLoginPanel(JPanel loginPanel) {
        this.loginPanel = loginPanel;
    }

    public JLabel getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(JLabel userLabel) {
        this.userLabel = userLabel;
    }

    public JLabel getPasswordLabel() {
        return passwordLabel;
    }

    public void setPasswordLabel(JLabel passwordLabel) {
        this.passwordLabel = passwordLabel;
    }

    public JTextField getUsername() {
        return username;
    }

    public void setUsername(JTextField username) {
        this.username = username;
    }

    public JPasswordField getPassword() {
        return password;
    }

    public void setPassword(JPasswordField password) {
        this.password = password;
    }

    public JLabel getLabelRand() {
        return labelRand;
    }

    public void setLabelRand(JLabel labelRand) {
        this.labelRand = labelRand;
    }

}
