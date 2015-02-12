package corpus.sinhala.crawler.controller.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import corpus.sinhala.crawler.controller.ConfigManager;

public class DbConnector {

	private static DbConnector instance = null;

	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String driverName;
	String url;
	String uname;
	String pwd;

	private int saveDateCounter = 0;
	private int changeCrawlerStateCounter = 0;
	private int getCrawlerPathCounter = 0;

	protected DbConnector() {
		
		driverName = "com.mysql.jdbc.Driver";
		url = ConfigManager.getProperty(ConfigManager.MYSQL_DB_HOST);
		uname = ConfigManager.getProperty(ConfigManager.MYSQL_DB_USER);
		pwd = ConfigManager.getProperty(ConfigManager.MYSQL_DB_PASSWORD);
		
		connect();
	}

	public static DbConnector getInstance() {
		if (instance == null) {
			instance = new DbConnector();
		}
		return instance;
	}

	public static void main(String[] args) {
		DbConnector loadDriver = new DbConnector();
		loadDriver.connect();
	}

	private void connect() {
		try {
			if (conn == null)
				conn = getConnection(driverName, url, uname, pwd);
		} catch (DataSourceException e) {
			e.printStackTrace();
		}
	}

	public boolean testConnection() throws DataSourceException {
		return testConnection(driverName, url, uname, pwd);
	}

	public boolean testConnection(String driver, String url, String uname,
			String pwd) throws DataSourceException {
		try {
			getConnection(driver, url, uname, pwd);
		} catch (DataSourceException e) {
			throw e;
		}

		return true;
	}

	public Connection getConnection(String driver, String url, String uname,
			String pwd) throws DataSourceException {
		Connection conn = null;

		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url, uname, pwd);

		} catch (SQLException ex) {
			throw new DataSourceException(
					"Error establishing data source connection: "
							+ ex.getMessage(), ex);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new DataSourceException(
					"Error establishing data source connection: "
							+ e.getMessage(), e);
		}

		return conn;
	}

	public boolean saveDate(int crawlerId, String date) throws SQLException {

		try {
			String query = "INSERT INTO completed (CRAWLERID, DATE) values (?,?)";

			PreparedStatement stmt3 = conn.prepareStatement(query);
			stmt3.setInt(1, crawlerId);
			stmt3.setString(2, date);
			stmt3.execute();
		}catch (SQLException e){
			if (saveDateCounter == 0){
				saveDateCounter = 1;
				connect();
				saveDate(crawlerId, date);
			}
			saveDateCounter = 0;
			return false;
		}

		return true;

	}

	public boolean changeCrawlerState(int crawlerId, boolean state, int port)
			throws SQLException {

		try{
			String query = "UPDATE crawler SET ACTIVE=?, PORT=? WHERE ID=?";

			PreparedStatement stmt3 = conn.prepareStatement(query);
			stmt3.setBoolean(1, state);
			stmt3.setInt(2, port);
			stmt3.setInt(3, crawlerId);
			stmt3.executeUpdate();
		}catch (SQLException e){
			if (changeCrawlerStateCounter == 0){
				changeCrawlerStateCounter = 1;
				connect();
				changeCrawlerState(crawlerId, state, port);
			}
			changeCrawlerStateCounter = 0;
			return false;
		}

		return true;
	}

	public String getCrawlerPath(int crawlerId) throws SQLException {
		try {
			String query = "SELECT * FROM crawler WHERE ID = ?";
			ResultSet rs;

			PreparedStatement stmt3 = conn.prepareStatement(query);
			stmt3.setInt(1, crawlerId);
			rs = stmt3.executeQuery();
			if (rs.next()) {
				String location = rs.getString("LOCATION");
				return location;
			}

		}catch (SQLException e){
			if (getCrawlerPathCounter == 0){
				getCrawlerPathCounter = 1;
				connect();
				getCrawlerPath(crawlerId);
			}
			getCrawlerPathCounter = 0;
			return null;
		}

		return null;

	}

}
