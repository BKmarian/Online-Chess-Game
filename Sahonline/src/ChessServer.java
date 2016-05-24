import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChessServer {
	private static int PORT = 5002;
	public static final int rows = 8;
	public static final int columns = 8;
	public AbstractLogger logger;
	public List<Game> games;
	public JDBCconnection jdbc;

	public void createServer() throws IOException, ClassNotFoundException, SQLException {
		logger = Logger.getChainOfLoggers();
		logger.logMessage(AbstractLogger.INFO, "Chess Server is Running");
		ServerSocket listener = new ServerSocket(PORT);
		games = new ArrayList<Game>();
		jdbc = new JDBCconnection();
		// listener.setSoTimeout(10000);
		try {
			while (true) {
				Game game = new Game();
				Game.Player player1, player2;
				player1 = game.new Player(listener.accept(), 1);
				while (player1.waitConnection() == 0) {
				}

				player2 = game.new Player(listener.accept(), -1);
				while (player2.waitConnection() == 0) {
				}

				player1.setOpponent(player2);
				player2.setOpponent(player1);
				game.currentPlayer = player1;
				games.add(game);
				player1.start();
				player2.start();
			}
		} catch (Exception e) {
			logger.logMessage(AbstractLogger.ERROR, e.getMessage());
		} finally {
			for (Game game : games) {
				game.currentPlayer.output.writeObject("PICAT SERVERUL A PICAT");
				game.currentPlayer.opponent.output.writeObject("PICAT SERVERUL A PICAT");
			}
			listener.close();
			logger.close();
		}
	}

	public static void main(String[] args) throws Exception {
		new ChessServer().createServer();
	}

	class Game {
		public int selected = 0; // exista piesa selectata
		public int piecePoz[][]; // 1 pentru piesa alba -1 pt piesa neagra
		public int check1[][] = new int[rows][columns];
		public int legalMoves[][];
		public Player currentPlayer;
		public String matrix[][];
		public int selectedX, selectedY;

		public Game() throws FileNotFoundException, UnsupportedEncodingException {
			init();
		}

		public void init() throws FileNotFoundException, UnsupportedEncodingException {
			selectedX = selectedY = 0;
			legalMoves = new int[rows][columns];
			piecePoz = new int[rows][columns];
			for (int i = 0; i < columns; i++) {
				piecePoz[0][i] = piecePoz[1][i] = 1;
				piecePoz[6][i] = piecePoz[7][i] = -1;
			}
			matrix = new String[][] { { "wtura", "wcal", "wnebun", "wrege", "wregina", "wnebun", "wcal", "wtura" },
					{ "wpion", "wpion", "wpion", "wpion", "wpion", "wpion", "wpion", "wpion" },
					{ "0", "0", "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0", "0", "0" },
					{ "0", "0", "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0", "0", "0" },
					{ "bpion", "bpion", "bpion", "bpion", "bpion", "bpion", "bpion", "bpion" },
					{ "btura", "bcal", "bnebun", "brege", "bregina", "bnebun", "bcal", "btura" } };
		}

		public int search(String s, String matrix[][]) {
			for (int i = 0; i < rows; i++)
				for (int j = 0; j < columns; j++) {
					if (matrix[i][j].equals(s))
						return i * 10 + j;
				}
			return 0;
		}

		public int[][] mutariPosibile(int rand, String[][] matrice, int piecePoz[][]) {
			int i, j, ii, jj;
			int table[][] = new int[rows][columns];
			int b[][] = new int[rows][columns];
			for (i = 0; i < rows; i++) {
				for (j = 0; j < columns; j++) {
					if (piecePoz[i][j] == rand) {
						b = Management.nextmove(matrice[i][j], i, j, rand, piecePoz);

						for (ii = 0; ii < rows; ii++)
							for (jj = 0; jj < columns; jj++)
								table[ii][jj] = table[ii][jj] | b[ii][jj];
					}
				}
			}
			return table;
		}

		public int[][] moves(int rand, String piesa, int x, int y) {
			int b[][] = new int[rows][columns];
			b = Management.nextmove(piesa, x, y, rand, piecePoz);
			return b;
		}

		public void showTable(String[][] matrix) {
			int r, c;
			for (r = 0; r < rows; r++) {
				for (c = 0; c < columns; c++) {
					logger.print(matrix[r][c] + " ");
				}
				logger.logMessage(AbstractLogger.INFO, "");
			}
		}

		public synchronized void setRand() throws IOException {
			currentPlayer = currentPlayer.opponent;
			currentPlayer.output.writeObject("MESSAGE Este randul lui " + currentPlayer.nume);
			currentPlayer.opponent.output.writeObject("MESSAGE Este randul lui " + currentPlayer.nume);

			if (check(matrix, piecePoz, currentPlayer.rand) && verificaSahMat(currentPlayer.rand) == 1) {
				currentPlayer.output
						.writeObject("SAHMAT ,Jucatorul " + currentPlayer.opponent.nume + " a castigat");
				currentPlayer.opponent.output
						.writeObject("SAHMAT ,Jucatorul " + currentPlayer.opponent.nume + " a castigat");
			}
		}

		public int getkingPos(int rand, String matrix[][]) {
			int i, j, poz = 0;
			char culoare = (rand == 1) ? 'w' : 'b';
			for (i = 0; i < rows; i++)
				for (j = 0; j < columns; j++)
					if (matrix[i][j].substring(1).equals("rege") && matrix[i][j].charAt(0) == culoare)
						poz = i * 10 + j;
			return poz;
		}

		public int verificaSahMat(int rand) {
			int i, j, ii = 0, jj = 0, kingPos;
			String aux;
			int aux2;
			int a[][] = new int[rows][columns];
			String tempPoz[][] = new String[rows][columns];
			int tempPiecePoz[][] = new int[rows][columns];
			// int c[][] = new int[rows][columns];
			kingPos = getkingPos(rand, matrix);
			logger.logMessage(AbstractLogger.INFO, "kingPos=" + kingPos);
			for (ii = 0; ii < rows; ii++)
				for (jj = 0; jj < columns; jj++) {
					if (piecePoz[ii][jj] == rand) {
						a = moves(rand, matrix[ii][jj], ii, jj);
						for (i = 0; i < rows; i++)
							for (j = 0; j < columns; j++) {
								if (a[i][j] == 0)
									continue;
								for (int l = 0; l < rows; l++)
									for (int ll = 0; ll < columns; ll++) {
										tempPoz[l][ll] = matrix[l][ll];
										tempPiecePoz[l][ll] = piecePoz[l][ll];
									}
								aux = tempPoz[ii][jj];
								tempPoz[ii][jj] = tempPoz[i][j];
								tempPoz[i][j] = aux;

								aux2 = tempPiecePoz[ii][jj];
								tempPiecePoz[ii][jj] = tempPiecePoz[i][j];
								tempPiecePoz[i][j] = aux2;

								if (check(tempPoz, tempPiecePoz, rand ) == false)
									return 0;

								/*
								 * kingPos = getkingPos(rand, tempPoz); c =
								 * mutariPosibile(rand * (-1), tempPoz,
								 * piecePoz); if (c[kingPos / 10][kingPos % 10]
								 * == 0) return 0;
								 */
							}
					}
				}
			return 1;
		}

		public synchronized void check() throws IOException {
			selected = 0;
			currentPlayer.output.writeObject("SAH");

			logger.logMessage(AbstractLogger.INFO, "MAAAAATRRIX");
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < columns; c++) {
					logger.print(piecePoz[r][c] + " ");
				}
				logger.logMessage(AbstractLogger.INFO, "");
			}
		}

		// copiem masa cu numele pieselor
		public synchronized String[][] copyMatrix(String matrix[][]) {
			String matrixTemp[][] = new String[rows][columns];
			for (int i = 0; i < rows; i++)
				System.arraycopy(matrix[i], 0, matrixTemp[i], 0, columns);
			logger.logMessage(AbstractLogger.INFO, "copyMatrix");
			// showTable(matrixTemp);
			return matrixTemp;
		}

		// copiem masa care retine culoarea peiselor
		public synchronized int[][] copypiecePoz(int piecePoz[][]) {
			int piecePozTemp[][] = new int[rows][columns];
			for (int i = 0; i < rows; i++)
				System.arraycopy(piecePoz[i], 0, piecePozTemp[i], 0, columns);
			return piecePozTemp;
		}

		public boolean checkLegalMove(Point punct, Player player) {
			return player.rand == currentPlayer.rand;
		}

		public boolean checkLegalSelect(Point punct, Player player) {
			return currentPlayer.culoare == matrix[punct.x][punct.y].charAt(0);
		}

		public synchronized void noCheck(int i, int j) throws IOException {
			selected = 0;
			currentPlayer.output.writeObject("LEGAL MOVE");
			currentPlayer.output.writeObject(new Point(i, j));
			currentPlayer.output.writeObject(new Point(selectedX, selectedY));
			setRand();
			currentPlayer.output.writeObject("OPPONENT MOVED");
			currentPlayer.output.writeObject(new Point(i, j));
			currentPlayer.output.writeObject(new Point(selectedX, selectedY));
		}

		public boolean check(String[][] matrixTemp, int[][] piecePozTemp, int rand) {
			int kingPos = 0;
			if (rand == 1)
				kingPos = search("wrege", matrixTemp);
			else
				kingPos = search("brege", matrixTemp);
			check1 = mutariPosibile(currentPlayer.rand * (-1), matrixTemp, piecePozTemp);
			if (check1[kingPos / 10][kingPos % 10] == 1)
				return true;
			return false;
		}

		public synchronized void muta(int placeToMoveX, int placeToMoveY, int moves[][]) throws IOException {
			String matrixTemp[][];
			int piecePozTemp[][];
			// a mutat pe o noua casuta
			if ((placeToMoveX != selectedX || placeToMoveY != selectedY)) {
				if (moves[placeToMoveX][placeToMoveY] == 1) { // legal move
					logger.logMessage(AbstractLogger.INFO, "legalmove");
					logger.logMessage(AbstractLogger.INFO, "");
					// showTable(matrix);
					logger.logMessage(AbstractLogger.INFO, "");
					matrixTemp = copyMatrix(matrix);
					piecePozTemp = copypiecePoz(piecePoz);
					matrixTemp[placeToMoveX][placeToMoveY] = matrixTemp[selectedX][selectedY];
					matrixTemp[selectedX][selectedY] = "0";
					piecePozTemp[placeToMoveX][placeToMoveY] = piecePozTemp[selectedX][selectedY];
					piecePozTemp[selectedX][selectedY] = 0;
					// showTable(matrixTemp);
					// showTable(matrix);
					logger.logMessage(AbstractLogger.INFO, "");
					if (check(matrixTemp, piecePozTemp, currentPlayer.rand) == true) {
						logger.logMessage(AbstractLogger.INFO, "SAH");
						check();
					} else {
						logger.logMessage(AbstractLogger.INFO, "NUSAH");
						matrix = copyMatrix(matrixTemp);
						piecePoz = copypiecePoz(piecePozTemp);
						noCheck(placeToMoveX, placeToMoveY);
					}
				}
				// a deselectat casuta
			} else if (placeToMoveY == selectedX || placeToMoveY == selectedY) {
				selected = 0;
				currentPlayer.output.writeObject("DESELECT");
			} else {
				currentPlayer.output.writeObject("MESSAGE NOT LEGAL MOVE!");
			}
		}

		public synchronized void checkSelected(int i, int j) throws IOException {
			if (selected == 0) {
				if (checkLegalSelect(new Point(i, j), currentPlayer) == false)
					return;
				selectedX = i;
				selectedY = j;
				selected = 1;
				currentPlayer.output.writeObject("SELECTING");
				currentPlayer.output.writeObject(new Point(i, j));
				int[][] matrice = moves(currentPlayer.rand, matrix[i][j], i, j);
				currentPlayer.output.writeObject(matrice);

			} else {
				int[][] matrice = moves(currentPlayer.rand, matrix[selectedX][selectedY], selectedX, selectedY);
				muta(i, j, matrice);
			}
		}

		class Player extends Thread {
			public Socket socket;
			public int rand;
			public String nume;
			public char culoare;
			public ObjectInputStream input;
			public ObjectOutputStream output;
			public Player opponent;

			public Player(Socket socket, int rand) throws ClassNotFoundException, SQLException, IOException {
				this.socket = socket;
				this.culoare = (rand == 1 ? 'w' : 'b');
				this.rand = rand;
				output = new ObjectOutputStream(socket.getOutputStream());
				input = new ObjectInputStream(socket.getInputStream());
			}

			public void setOpponent(Player player) {
				opponent = player;
			}

			public int waitConnection() throws ClassNotFoundException, SQLException {
				try {
					String command = (String) input.readObject();
					logger.logMessage(AbstractLogger.INFO, "salam" + command);
					String name = command.split(" ")[1];
					String pas = command.split(" ")[2];
					if (jdbc.findUser(name, pas) == true) {
						output.writeObject("CONNECTED");
						return 1;
					} else {
						output.writeObject("NOTCONNECTED");
					}
				} catch (IOException e) {
					logger.logMessage(AbstractLogger.INFO, "Player died: ");
					logger.print(e.getMessage());
				}
				return 0;
			}

			public void run() {
				try {
					// The thread is only started after everyone connects.
					output.writeObject("MESSAGE All players connected");

					// Repeatedly get commands from the client and process them.
					while (true) {
						String command = (String) input.readObject();
						logger.logMessage(AbstractLogger.INFO, "command= " + command);
						if (command.startsWith("MOVE")) {
							Point punct = (Point) input.readObject();
							logger.logMessage(AbstractLogger.INFO, punct.toString());
							if (checkLegalMove(punct, this) == true)
								checkSelected(punct.x, punct.y);
						} else if (command.startsWith("RESET")) {
							init();
						} else if (command.startsWith("QUIT")) {
							socket.close();
						} else if (command.startsWith("CHAT")) {
							logger.logMessage(AbstractLogger.INFO, command.substring(5));
							this.opponent.output
									.writeObject(new String("CHAT " + this.nume + ": " + command.substring(5)));
						} else if (command.startsWith("NUME")) {
							logger.logMessage(AbstractLogger.INFO, command.substring(5));
							this.nume = command.substring(5);
						} else if (command.startsWith("DISCONNECTED")) {
							// currentPlayer.output.writeObject(command);
							this.opponent.output.writeObject(command);
						}
					}
				} catch (IOException | ClassNotFoundException e) {
					logger.logMessage(AbstractLogger.ERROR, "Player died: " + e);
				} catch (Exception e) {
					logger.logMessage(AbstractLogger.ERROR, e.getMessage());
				} finally {
					try {
						socket.close();
						opponent.socket.close();
					} catch (IOException e) {
						logger.logMessage(AbstractLogger.ERROR, e.getMessage());
					}
				}
			}
		}
	}
}
