package dbConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBConnection {

	private static final String SQL_DRIVER = "org.mariadb.jdbc.Driver";
	private static final String MYSQL_URL = "jdbc:mysql://localhost:55555/test";
	private static final String USER = "root";
	private static final String PASSWORD = "root";
	private Connection con;

	public DBConnection() {
		getConnection();
	}

	/**
	 * Establishes a new connection to a DB.
	 * 
	 * @return true if successful and false if failed.
	 */
	public boolean getConnection() {
		try {
			Class.forName(SQL_DRIVER).newInstance();
			System.out.println("SQL-Driver Loaded....");
			setCon(DriverManager.getConnection(MYSQL_URL, USER, PASSWORD));
			System.out.println("Connected to the database....");
			return true;
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException:\n" + e.toString());
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			System.out.println("SQLException:\n" + e.toString());
			e.printStackTrace();
			return false;
		} catch (InstantiationException e) {
			System.out.println("InstantiationException:\n" + e.toString());
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException:\n" + e.toString());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Executes a SELECT-Query.
	 * 
	 * Return a List containing a List of rows for a select statement. The List
	 * of rows contains a List of Strings. So the return has the following
	 * structure: (rowA(ColumnA,ColumnB,...),rowB((ColumnA,ColumnB,...),...).
	 * 
	 * @param query
	 *            The SQL-Query to be executed (must be a select statement!).
	 * @return Return a List containing a HashMap of rows for a select
	 *         statement.
	 */
	public List<HashMap<String, Object>> selectQuery(String query) {
		if (!query.toLowerCase().startsWith("select")) {
			throw new WrongStatementException();
		}
		Statement st = null;
		ResultSet rs = null;
		List<HashMap<String, Object>> result = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery(query);
			result = convertResultSetToList(rs);

		} catch (SQLException e) {
			System.out.println("SQLException:\n" + e.toString());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Closes the connection to the DB.
	 * 
	 * @return true if successful or false if it failed.
	 */
	public boolean closeConnection() {
		try {
			con.close();
			System.out.println("DB disconnected successful!");
			return true;
		} catch (SQLException e) {
			System.out.println("SQLException:\n" + e.toString());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Converts a ResultSet into a List.
	 * 
	 * @param rs
	 *            The ResultSet to Convert.
	 * @return The converted List.
	 * @throws SQLException
	 */
	private List<HashMap<String, Object>> convertResultSetToList(ResultSet rs)
			throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		while (rs.next()) {
			HashMap<String, Object> row = new HashMap<String, Object>(columns);
			for (int i = 1; i <= columns; ++i) {
				row.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(row);
		}

		return list;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}
}