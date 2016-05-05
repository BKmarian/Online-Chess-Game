import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChessClient {
	private static int PORT = 5002;
	private ClientThread client;
	private JDBCconnection jdbc;
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	public JLabel labelRand;
	public String serverAddress;
	public static final Color col1 = new Color(130, 120, 82);
	public static final int rows = 8;
	public static final int columns = 8;
	public static final Color col2 = Color.white;
	public final int numberOfColums = 20; // for textfield
	public JPanel panelMatrix[][], gridPane, chatPane, sendTextPane;
	public Container panel, cardPanel;
	public JLabel labelChat;
	public JTextField tf;
	public JTextArea ta;
	public JFrame checkerBoard;
	public CardLayout cardLayout;
	// public JFrame loginBoard;
	public SpecialButton label[][];
	public JPanel loginPanel;
	public JButton login;
	public JLabel userLabel;
	public JLabel passwordLabel;
	public JTextField username;
	public JPasswordField password;
	public static final ImageIcon wpion = new ImageIcon("images/wpion.png");
	public static final ImageIcon bpion = new ImageIcon("images/bpion.png");
	public static final ImageIcon wtura = new ImageIcon("images/wtura.png");
	public static final ImageIcon btura = new ImageIcon("images/btura.png");
	public static final ImageIcon wcal = new ImageIcon("images/wcal.png");
	public static final ImageIcon bcal = new ImageIcon("images/bcal.png");
	public static final ImageIcon wnebun = new ImageIcon("images/wnebun.png");
	public static final ImageIcon bnebun = new ImageIcon("images/bnebun.png");
	public static final ImageIcon brege = new ImageIcon("images/brege.png");
	public static final ImageIcon wrege = new ImageIcon("images/wrege.png");
	public static final ImageIcon bregina = new ImageIcon("images/bregina.png");
	public static final ImageIcon wregina = new ImageIcon("images/wregina.png");

	public ChessClient(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public void reseteazaCulori() {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				if ((i + j) % 2 == 0)
					label[i][j].setBackground(col1);
				else
					label[i][j].setBackground(Color.WHITE);
	}

	private void createMenuBar() {

		JMenuBar menubar = new JMenuBar();

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
				checkerBoard.dispose();
				try {
					out.writeObject("RESET");
					// display();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		file.add(eMenuItem);
		file.add(eMenuItem2);
		file.add(eMenuItem3);
		menubar.add(file);

		checkerBoard.setJMenuBar(menubar);
	}

	public void createPanels() {
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
						e1.printStackTrace();
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

		label = new SpecialButton[rows][columns];
		// panel.setSize(1200, 600);
		gridPane.setLayout(new GridLayout(rows, columns));
		panel.add(gridPane, BorderLayout.WEST);
		panel.add(chatPane, BorderLayout.EAST);
		labelRand = new JLabel("");
		panel.add(labelRand, "South");
		panelMatrix = new JPanel[rows][columns];
	}

	public void createConnection() throws UnknownHostException, IOException {
		socket = new Socket(serverAddress, PORT);
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
	}

	public void display() throws Exception {
		int i = 0, j = 0;
		Color temp;
		checkerBoard.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					if (socket != null)
						socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.getWindow().dispose();
			}
		});

		createConnection();
		createPanels();
		createMenuBar();
		for (i = 0; i < rows; i++) {
			if (i % 2 == 0) {
				temp = col1;
			} else {
				temp = col2;
			}
			for (j = 0; j < columns; j++) {

				if (i != 0 && i != 1 && i != 6 && i != 7) {
					label[i][j] = new SpecialButton(temp);
				}
				if (i == 1) {
					label[i][j] = new SpecialButton(temp, wpion);
				}
				if (i == 6) {
					label[i][j] = new SpecialButton(temp, bpion);
				}
				if (i == 7) {
					switch (j) {
					case 0:
					case 7: {
						label[i][j] = new SpecialButton(temp, btura);
						break;
					}
					case 1:
					case 6: {
						label[i][j] = new SpecialButton(temp, bcal);
						break;
					}
					case 2:
					case 5: {
						label[i][j] = new SpecialButton(temp, bnebun);
						break;
					}
					case 3: {
						label[i][j] = new SpecialButton(temp, brege);
						break;
					}
					case 4:
						label[i][j] = new SpecialButton(temp, bregina);
						break;
					}
				}
				if (i == 0) {
					switch (j) {
					case 0:
					case 7: {
						label[i][j] = new SpecialButton(temp, wtura);
						break;
					}
					case 1:
					case 6: {
						label[i][j] = new SpecialButton(temp, wcal);
						break;
					}
					case 2:
					case 5: {
						label[i][j] = new SpecialButton(temp, wnebun);
						break;
					}
					case 3: {
						label[i][j] = new SpecialButton(temp, wrege);
						break;
					}
					case 4: {
						label[i][j] = new SpecialButton(temp, wregina);
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
		reseteazaCulori();

		for (i = 0; i < rows; i++)
			for (j = 0; j < columns; j++)
				addAction(label[i][j]);
		// play();
	}

	public void selecteaza(int i, int j, int[][] matrice) {
		label[i][j].setBackground(Color.DARK_GRAY);
		for (int ii = 0; ii < rows; ii++)
			for (int jj = 0; jj < columns; jj++)
				if (matrice[ii][jj] == 1)
					label[ii][jj].setBackground(Color.LIGHT_GRAY);
	}

	public void muta(int i, int j, int ii, int jj) {
		label[i][j].setIcon(label[ii][jj].getIcon());
		label[ii][jj].setIcon(null);
	}

	class ClientThread extends Thread {
		private String name;

		public ClientThread(String name) {
			this.name = name;
		}

		public void run() {
			try {
				play();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void play() throws Exception {
			String response;
			try {
				while (true) {
					// trimitem numele
					out.writeObject("NUME " + client.name);
					response = (String) in.readObject();
					System.out.println("respones= " + response);
					if (response.startsWith("LEGAL MOVE")) {

						Point punct = (Point) in.readObject();
						Point punctMutat = (Point) in.readObject();
						muta(punct.x, punct.y, punctMutat.x, punctMutat.y);
						reseteazaCulori();
					} else if (response.startsWith("OPPONENT MOVED")) {

						Point punct = (Point) in.readObject();
						Point punctMutat = (Point) in.readObject();
						muta(punct.x, punct.y, punctMutat.x, punctMutat.y);
					} else if (response.startsWith("SELECTING")) {
						Point punct = (Point) in.readObject();
						int[][] matrice = (int[][]) in.readObject();
						selecteaza(punct.x, punct.y, matrice);
						System.out.println("response= " + response);
						System.out.println(punct + "\n");
						for (int i = 0; i < rows; i++) {
							for (int j = 0; j < columns; j++)
								System.out.print(matrice[i][j] + " ");
							System.out.println();
						}
					} else if (response.startsWith("MESSAGE")) {
						labelRand.setText(response.substring(8));
					} else if (response.startsWith("SAH")) {
						labelRand.setText("Sunteti in sah, nu puteti muta in aceasta casuta");
						JOptionPane.showMessageDialog(checkerBoard, "Sunteti in sah :D", "Sah",
								JOptionPane.PLAIN_MESSAGE);
						reseteazaCulori();
					} else if (response.startsWith("DESELECT")) {
						reseteazaCulori();
					} else if (response.startsWith("CHAT")) {
						ta.append(response.substring(5));
						ta.append("\n");
					}
				}
			} finally {
				out.writeObject("QUIT");
				socket.close();
			}
		}
	}

	public void addAction(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int l, ll;
				// Cauta buton
				for (l = 0; l < rows; l++)
					for (ll = 0; ll < columns; ll++)
						if (e.getSource() == label[l][ll]) {
							try {
								out.writeObject("MOVE");
								out.writeObject(new Point(l, ll));
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
			}
		});
	}

	@SuppressWarnings("serial")
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

	public void displayLoginBoard() {
		jdbc = new JDBCconnection();
		jdbc.createConnection();
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
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (jdbc.findUser(username.getText(),String.valueOf(password.getPassword()))) {
						try {
							display();
							cardLayout.show(cardPanel, "2");
							client = new ClientThread(username.getText());
							client.start();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

	}

	public static void main(String args[]) throws Exception {
		String serverAddress = (args.length == 0) ? "localhost" : args[1];
		ChessClient client = new ChessClient(serverAddress);
		client.displayLoginBoard();
	}
}