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
	public AbstractLogger logger;
	public List<Game> games;

	public void createServer() throws IOException, ClassNotFoundException, SQLException {
		logger = Logger.getChainOfLoggers();
		logger.logMessage(AbstractLogger.INFO, "Chess Server is Running");
		ServerSocket listener = new ServerSocket(PORT);
		games = new ArrayList<Game>();
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
}
