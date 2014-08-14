package corpus.sinhala.crawler.controller.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnector {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String driverName;
	String url;
	String uname;
	String pwd;
	
	public DbConnector(){
		driverName = "com.mysql.jdbc.Driver";
		url = "jdbc:mysql://localhost:3306/crawler_data";
		uname = "root";
		pwd = "";
	}

	public static void main(String[] args) {
		DbConnector loadDriver = new DbConnector();
		loadDriver.connect();
	}

	public void connect() {
		String driverName = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/crawler_data";

		 try{
		 conn = getConnection(driverName, url, "root", "");
		 }catch(DataSourceException ex){
		
		 System.out.println(ex.getMessage());
		 }

//		try {
//			 boolean valiedConnection = testConnection(driverName, url, "root", "");
//			if (valiedConnection) {
//				System.out.println("Connection is Healthy");
//			}
//		} catch (DataSourceException e) {
//			System.out.println(e.getMessage());
//		}

		// testConnection();

		// selectStatement();
		// insertWithAutoIncrement();
		// insertWithLastInsertedId();
		// insertWithUpdatableResult();
	}
	
	public boolean testConnection() throws DataSourceException{
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
			conn = DriverManager.getConnection(url + "?user=" + uname
					+ "&password=" + pwd);
			conn = DriverManager.getConnection(url, uname, pwd);

		} catch (SQLException ex) {
			// handle any errors
			// System.out.println("SQLException: " + ex.getMessage());
			// System.out.println("SQLState: " + ex.getSQLState());
			// System.out.println("VendorError: " + ex.getErrorCode());
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
			// e.printStackTrace();
		}

		return conn;
	}
	
	public boolean saveDate(int crawlerId, String date){
		String query = "INSERT INTO completed (CRAWLERID, DATE) values (?,?)";

		try {
			PreparedStatement stmt3 = conn.prepareStatement(query);
			stmt3.setInt(1, crawlerId);
			stmt3.setString(2, date);
			stmt3.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	
	public boolean changeCrawlerState(int crawlerId, boolean state, int port){
		String query = "UPDATE crawler SET ACTIVE=?, PORT=? WHERE ID=?";

		try {
			PreparedStatement stmt3 = conn.prepareStatement(query);
			stmt3.setBoolean(1, state);
			stmt3.setInt(2, port);
			stmt3.setInt(3, crawlerId);
			stmt3.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public String getCrawlerPath(int crawlerId){
		String query = "SELECT * FROM crawler WHERE ID = ?";
		ResultSet rs;
		try {
			PreparedStatement stmt3 = conn.prepareStatement(query);
			stmt3.setInt(1, crawlerId);
			rs = stmt3.executeQuery();
			if(rs.next()){
				String location = rs.getString("LOCATION");
				return location;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
		
	}


}