import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChessServer {
	private static int PORT = 5002;
	public static final int rows = 8;
	public static final int columns = 8;
	public AbstractLogger logger;

	public void createServer() throws IOException {
		logger = Logger.getChainOfLoggers();
		logger.logMessage(AbstractLogger.INFO, "Chess Server is Running");
		ServerSocket listener = new ServerSocket(PORT);
		// listener.setSoTimeout(10000);
		try {
			while (true) {
				Game game = new Game();
				Game.Player player1, player2;
				player1 = game.new Player(listener.accept(), 1);
				player2 = game.new Player(listener.accept(), -1);
				player1.setOpponent(player2);
				player2.setOpponent(player1);
				game.currentPlayer = player1;
				player1.start();
				player2.start();
			}
		} finally {
			listener.close();
			logger.close();
		}
	}

	public static void main(String[] args) throws Exception {
		new ChessServer().createServer();
	}

	class Game {
		public int selected = 0; // exista piesa selectata
		public int piespoz[][]; // 1 pentru piesa alba -1 pt piesa neagra
		public int check1[][] = new int[rows][columns];
		public int legalMoves[][];
		public Player currentPlayer;
		public String matrix[][];
		public int piesaselectatax, piesaselectatay;
		
		public Game() throws FileNotFoundException, UnsupportedEncodingException {
			init();
		}
		public void init() throws FileNotFoundException, UnsupportedEncodingException {
			piesaselectatax = piesaselectatay = 0;
			legalMoves = new int[rows][columns];
			piespoz = new int[rows][columns];
			for (int i = 0; i < columns; i++) {
				piespoz[0][i] = piespoz[1][i] = 1;
				piespoz[6][i] = piespoz[7][i] = -1;
			}
			matrix = new String[][] { { "wtura", "wcal", "wnebun", "wrege", "wregina", "wnebun", "wcal", "wtura" },
					{ "wpion", "wpion", "wpion", "wpion", "wpion", "wpion", "wpion", "wpion" },
					{ "0", "0", "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0", "0", "0" },
					{ "0", "0", "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0", "0", "0" },
					{ "bpion", "bpion", "bpion", "bpion", "bpion", "bpion", "bpion", "bpion" },
					{ "btura", "bcal", "bnebun", "brege", "bregina", "bnebun", "bcal", "btura" } };
		}

		public int cauta(String s, String matrix[][]) {
			for (int i = 0; i < rows; i++)
				for (int j = 0; j < columns; j++) {
					if (matrix[i][j].equals(s))
						return i * 10 + j;
				}
			return 0;
		}

		public int[][] mutariPosibile(int rand, String[][] matrice, int piespozitii[][]) {
			int i, j, ii, jj;
			int tabla[][] = new int[rows][columns];
			int b[][] = new int[rows][columns];
			for (i = 0; i < rows; i++) {
				for (j = 0; j < columns; j++) {
					if (piespozitii[i][j] == rand) {
						b = Management.nextmove(matrice[i][j], i, j, rand, piespozitii);

						for (ii = 0; ii < rows; ii++)
							for (jj = 0; jj < columns; jj++)
								tabla[ii][jj] = tabla[ii][jj] | b[ii][jj];
					}
				}
			}
			return tabla;
		}

		public int[][] moves(int rand, String piesa, int x, int y) {
			int b[][] = new int[rows][columns];
			b = Management.nextmove(piesa, x, y, rand, piespoz);
			return b;
		}

		public void afisareTabla(String[][] matrix) {
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

			if (checkSah(matrix, piespoz) && verificaSahMat(currentPlayer.rand) == 1)
				currentPlayer.output
						.writeObject("MESSAGE SAH MAT ,Jucatorul " + currentPlayer.opponent.nume + "a castigat");
		}

		public int getregepoz(int rand, String matrix[][]) {
			int i, j, nr = 0;
			char culoare = (rand == 1) ? 'w' : 'b';
			for (i = 0; i < rows; i++)
				for (j = 0; j < columns; j++)
					if (matrix[i][j].substring(1).equals("rege") && matrix[i][j].charAt(0) == culoare)
						nr = i * 10 + j;
			return nr;
		}

		public int verificaSahMat(int rand) {
			int i, j, ii = 0, jj = 0, pozrege;
			String aux;
			int a[][] = new int[rows][columns];
			String b[][] = new String[rows][columns];
			int c[][] = new int[rows][columns];
			pozrege = getregepoz(rand, matrix);
			logger.logMessage(AbstractLogger.INFO, "pozrege=" + pozrege);
			for (ii = 0; ii < rows; ii++)
				for (jj = 0; jj < columns; jj++) {
					if (piespoz[ii][jj] == rand) {
						a = moves(rand, matrix[ii][jj], ii, jj);
						for (i = 0; i < rows; i++)
							for (j = 0; j < columns; j++) {
								if (a[i][j] == 0)
									continue;
								for (int l = 0; l < rows; l++)
									for (int ll = 0; ll < columns; ll++)
										b[l][ll] = matrix[l][ll];
								aux = b[ii][jj];
								b[ii][jj] = b[i][j];
								b[i][j] = aux;
								pozrege = getregepoz(rand, b);
								c = mutariPosibile(rand * (-1), b, piespoz);
								if (c[pozrege / 10][pozrege % 10] == 0)
									return 0;
							}
					}
				}
			return 1;
		}

		public synchronized void sah(int i, int j, String ii, int jj) throws IOException {
			selected = 0;
			currentPlayer.output.writeObject("SAH");

			logger.logMessage(AbstractLogger.INFO, "MAAAAATRRIX");
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < columns; c++) {
					logger.print(piespoz[r][c] + " ");
				}
				logger.logMessage(AbstractLogger.INFO, "");
			}
		}

		// copiem masa cu numele pieselor
		public synchronized String[][] copyMatrix(String matrix[][]) {
			String matrixTemp[][] = new String[8][8];
			for (int i = 0; i < rows; i++)
				System.arraycopy(matrix[i], 0, matrixTemp[i], 0, 8);
			logger.logMessage(AbstractLogger.INFO, "copyMatrix");
			afisareTabla(matrixTemp);
			return matrixTemp;
		}

		// copiem masa care retine culoarea peiselor
		public synchronized int[][] copyPiesPoz(int piespoz[][]) {
			int piespozTemp[][] = new int[8][8];
			for (int i = 0; i < rows; i++)
				System.arraycopy(piespoz[i], 0, piespozTemp[i], 0, 8);
			return piespozTemp;
		}

		public boolean checkLegalMove(Point punct, Player player) {
			return player.rand == currentPlayer.rand;
		}

		public boolean checkLegalSelect(Point punct, Player player) {
			return currentPlayer.culoare == matrix[punct.x][punct.y].charAt(0);
		}

		public synchronized void nuSah(int i, int j) throws IOException {
			selected = 0;
			currentPlayer.output.writeObject("LEGAL MOVE");
			currentPlayer.output.writeObject(new Point(i, j));
			currentPlayer.output.writeObject(new Point(piesaselectatax, piesaselectatay));
			setRand();
			currentPlayer.output.writeObject("OPPONENT MOVED");
			currentPlayer.output.writeObject(new Point(i, j));
			currentPlayer.output.writeObject(new Point(piesaselectatax, piesaselectatay));
		}

		public boolean checkSah(String[][] matrixTemp, int[][] piespozTemp) {
			int pozitierege = 0;
			if (currentPlayer.rand == 1)
				pozitierege = cauta("wrege", matrixTemp);
			else
				pozitierege = cauta("brege", matrixTemp);
			check1 = mutariPosibile(currentPlayer.rand * (-1), matrixTemp, piespozTemp);
			if (check1[pozitierege / 10][pozitierege % 10] == 1)
				return true;
			return false;
		}

		public synchronized void muta(int placeToMoveX, int placeToMoveY, int moves[][]) throws IOException {
			String matrixTemp[][];
			int piespozTemp[][];
			// a mutat pe o noua casuta
			if ((placeToMoveX != piesaselectatax || placeToMoveY != piesaselectatay)) {
				if (moves[placeToMoveX][placeToMoveY] == 1) { // legal move
					logger.logMessage(AbstractLogger.INFO, "legalmove");
					logger.logMessage(AbstractLogger.INFO, "");
					afisareTabla(matrix);
					logger.logMessage(AbstractLogger.INFO, "");
					matrixTemp = copyMatrix(matrix);
					piespozTemp = copyPiesPoz(piespoz);
					matrixTemp[placeToMoveX][placeToMoveY] = matrixTemp[piesaselectatax][piesaselectatay];
					matrixTemp[piesaselectatax][piesaselectatay] = "0";
					piespozTemp[placeToMoveX][placeToMoveY] = piespozTemp[piesaselectatax][piesaselectatay];
					piespozTemp[piesaselectatax][piesaselectatay] = 0;
					afisareTabla(matrixTemp);
					afisareTabla(matrix);
					logger.logMessage(AbstractLogger.INFO, "");
					if (checkSah(matrixTemp, piespozTemp) == true) {
						logger.logMessage(AbstractLogger.INFO, "SAH");
						sah(placeToMoveX, placeToMoveY, matrixTemp[placeToMoveX][placeToMoveY],
								piespozTemp[placeToMoveX][placeToMoveY]);
					} else {
						logger.logMessage(AbstractLogger.INFO, "NUSAH");
						matrix = copyMatrix(matrixTemp);
						piespoz = copyPiesPoz(piespozTemp);
						nuSah(placeToMoveX, placeToMoveY);
					}
				}
				// a deselectat casuta
			} else if (placeToMoveY == piesaselectatax || placeToMoveY == piesaselectatay) {
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
				piesaselectatax = i;
				piesaselectatay = j;
				selected = 1;
				currentPlayer.output.writeObject("SELECTING");
				currentPlayer.output.writeObject(new Point(i, j));
				int[][] matrice = moves(currentPlayer.rand, matrix[i][j], i, j);
				// afisareTabla(matrice);
				currentPlayer.output.writeObject(matrice);
				// check1 = mutariPosibile(currentPlayer.rand * (-1), matrix,
				// piespoz);
				if (verificaSahMat(currentPlayer.rand) == 1)
					currentPlayer.output
							.writeObject("MESSAGE SAH MAT ,Player " + currentPlayer.opponent.nume + "a castigat");
			} else {
				// afisareMatrix();
				// logger.println();
				int[][] matrice = moves(currentPlayer.rand, matrix[piesaselectatax][piesaselectatay], piesaselectatax,
						piesaselectatay);
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

			public Player(Socket socket, int rand) {
				this.socket = socket;
				this.culoare = (rand == 1 ? 'w' : 'b');
				this.rand = rand;
				try {
					output = new ObjectOutputStream(socket.getOutputStream());
					input = new ObjectInputStream(socket.getInputStream());
				} catch (IOException e) {
					logger.logMessage(AbstractLogger.INFO, "Player died: ");
					logger.print(e.getMessage());
				}
			}

			public void setOpponent(Player player) {
				opponent = player;
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
						}
					}
				} catch (IOException | ClassNotFoundException e) {
					logger.logMessage(AbstractLogger.ERROR, "Player died: " + e);
				} catch (Exception e) {
					logger.logMessage(AbstractLogger.ERROR, "eroare");
					logger.logMessage(AbstractLogger.ERROR, e.getMessage());
				} finally {
					try {
						socket.close();
						opponent.socket.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}
}
