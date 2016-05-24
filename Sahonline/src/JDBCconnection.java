import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCconnection {
	final static public String url = "jdbc:mysql://51.255.99.13:3306/proiect_mds";
	final static public String username = "proiect_mds";
	final static public String password = "parolamea";
	private Connection connection = null;
	private void createConnection() {
		System.out.println("-------- Oracle JDBC Connection Testing ------");
		try {

			Class.forName("com.mysql.jdbc.Driver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			return;

		}

		System.out.println("Oracle JDBC Driver Registered!");

		try {

			connection = DriverManager.getConnection(url, username, password);

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}
	}
	public boolean findUser(String user, String pas) throws SQLException {
		connection = null;
		createConnection();
		PreparedStatement stmt = null;
		System.out.println("Creating statement...");
		String sql;
		sql = "SELECT * FROM users WHERE user_name=? and password=?";

		stmt = connection.prepareStatement(sql);
		stmt.setString(1, user);
		stmt.setString(2, pas);
		ResultSet rs = stmt.executeQuery();
		boolean found = false;
		if (rs.first() == false)
			found = false;
		else
			found = true;
		
		connection.close();
		rs.close();
		stmt.close();
		System.out.println("You made it, take control your database now!");
		return found;
	}
}