import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;

public class ChessClient {
	private static int PORT = 5002;
	private ClientThread client;
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
    private String serverAddress;
	private CustomJMenu menubar;
	private GamePanel gamePanel;

	public ChessClient(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public void resetColors() {
		for (int i = 0; i < Table.rows; i++)
			for (int j = 0; j < Table.columns; j++)
				if ((i + j) % 2 == 0)
					gamePanel.getLabel()[i][j].setBackground(GamePanel.col1);
				else
					gamePanel.getLabel()[i][j].setBackground(GamePanel.col2);
	}

	private void createMenuBar() {
		menubar = new CustomJMenu(gamePanel.getCheckerBoard(),out);
		gamePanel.getCheckerBoard().setJMenuBar(menubar);
	}

	public void createConnection() throws UnknownHostException, IOException {
		try {
			socket = new Socket(serverAddress, PORT);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (java.net.ConnectException exc) {
			JOptionPane.showMessageDialog(gamePanel.getCheckerBoard(), "Problema conexiune server", "SERVER PICAT",
					JOptionPane.PLAIN_MESSAGE);
		}
	}

	public void addButtons() {
		int i,j;
		gamePanel.addButtons();
		for (i = 0; i < Table.rows; i++)
			for (j = 0; j < Table.columns; j++)
				addAction(gamePanel.getLabel()[i][j]);
	}

	public void display() throws Exception {
		gamePanel.getCheckerBoard().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					out.writeObject("DISCONNECTED OPPONENT DISCONNECTED");
					if (socket != null)
						socket.close();
				} catch (IOException e1) {
					 e1.printStackTrace();
				}
				e.getWindow().dispose();
			}
		});

		gamePanel.createPanels(out);
		this.createMenuBar();
		this.addButtons();
		this.resetColors();

		// play();
	}

	public void select(int i, int j, int[][] matrice) {
		gamePanel.getLabel()[i][j].setBackground(Color.DARK_GRAY);
		for (int ii = 0; ii < Table.rows; ii++)
			for (int jj = 0; jj < Table.columns; jj++)
				if (matrice[ii][jj] == 1)
					gamePanel.getLabel()[ii][jj].setBackground(Color.LIGHT_GRAY);
	}

	public void move(int i, int j, int ii, int jj) {
		gamePanel.getLabel()[i][j].setIcon(gamePanel.getLabel()[ii][jj].getIcon());
		gamePanel.getLabel()[ii][jj].setIcon(null);
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
					// trimitem si citim numele
					out.writeObject("NUME " + client.name);
					response = (String) in.readObject();
					System.out.println("respones= " + response);
					process(response);
				}
			} catch (SocketException exc) {
				JOptionPane.showMessageDialog(gamePanel.getCheckerBoard(), "SERVER PICAT", "SERVER PICAT", JOptionPane.PLAIN_MESSAGE);
				gamePanel.getCheckerBoard().dispose();
			} catch (Exception e) {
				 e.printStackTrace();
			} finally {
				socket.close();
			}
		}
	}
	public void process(String response) throws IOException, ClassNotFoundException {
		if (response.startsWith("LEGAL MOVE")) {

			Point punct = (Point) in.readObject();
			Point punctMutat = (Point) in.readObject();
			move(punct.x, punct.y, punctMutat.x, punctMutat.y);
			resetColors();
		} else if (response.startsWith("OPPONENT MOVED")) {

			Point punct = (Point) in.readObject();
			Point punctMutat = (Point) in.readObject();
			move(punct.x, punct.y, punctMutat.x, punctMutat.y);
		} else if (response.startsWith("SELECTING")) {
			Point punct = (Point) in.readObject();
			int[][] matrice = (int[][]) in.readObject();
			select(punct.x, punct.y, matrice);
			System.out.println("response= " + response);
			System.out.println(punct + "\n");
			for (int i = 0; i < Table.rows; i++) {
				for (int j = 0; j < Table.columns; j++)
					System.out.print(matrice[i][j] + " ");
				System.out.println();
			}
		} else if (response.startsWith("MESSAGE")) {
			gamePanel.getLabelRand().setText(response.substring(8));
		}
		else if(response.startsWith("SAHMAT")) {
			JOptionPane.showMessageDialog(gamePanel.getCheckerBoard(), response, "SAH-MAT",
					JOptionPane.PLAIN_MESSAGE);
			gamePanel.getCheckerBoard().dispose();
		}
		else if (response.startsWith("SAH")) {
			gamePanel.getLabelRand().setText("Sunteti in sah, nu puteti muta in aceasta casuta");
			JOptionPane.showMessageDialog(gamePanel.getCheckerBoard(), "Sunteti in sah :D", "Sah",
					JOptionPane.PLAIN_MESSAGE);
			resetColors();
		} else if (response.startsWith("DESELECT")) {
			resetColors();
		} else if (response.startsWith("CHAT")) {
			gamePanel.getTa().append(response.substring(5));
			gamePanel.getTa().append("\n");
		} else if (response.startsWith("DISCONNECTED")) {
			JOptionPane.showMessageDialog(gamePanel.getCheckerBoard(), "OPPONENT DECONECTAT", "OPPONENT DECONECTAT",
					JOptionPane.PLAIN_MESSAGE);
			gamePanel.getLabelRand().setText(response.substring(13));
			gamePanel.getCheckerBoard().dispose();
		} else if (response.startsWith("PICAT")) {
			JOptionPane.showMessageDialog(gamePanel.getCheckerBoard(), "SERVER PICAT", "SERVER PICAT",
					JOptionPane.PLAIN_MESSAGE);
			gamePanel.getLabelRand().setText(response.substring(5));
			gamePanel.getCheckerBoard().dispose();
		}
	}
	public void addAction(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int l, ll;
				// Cauta buton
				for (l = 0; l < Table.rows; l++)
					for (ll = 0; ll < Table.columns; ll++)
						if (e.getSource() == gamePanel.getLabel()[l][ll]) {
							try {
								out.writeObject("MOVE");
								out.writeObject(new Point(l, ll));
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(gamePanel.getCheckerBoard(), "SERVER PICAT", "SERVER PICAT", JOptionPane.PLAIN_MESSAGE);
								gamePanel.getCheckerBoard().dispose();
								e1.printStackTrace();
							}
						}
			}
		});
	}


	public void displayLoginBoard() throws UnknownHostException, IOException {
		gamePanel = new GamePanel();
		gamePanel.displayLoginBoard();
		createConnection();
		gamePanel.getLogin().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					out.writeObject("CONNECT " + gamePanel.getUsername().getText() + " " + String.valueOf(gamePanel.getPassword().getPassword()));
					String response = (String) in.readObject();
					System.out.println("raspuns conectare=" + response);
					if (response.equals("CONNECTED")) {
						display();
						gamePanel.getCardLayout().show(gamePanel.getCardPanel(), "2");
						client = new ClientThread(gamePanel.getUsername().getText());
						client.start();
					} else {
						JOptionPane.showMessageDialog(gamePanel.getCheckerBoard(), "DATE GRESITE", "Eroare conexiune",
								JOptionPane.PLAIN_MESSAGE);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
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