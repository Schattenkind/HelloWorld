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

/**
 * Immutable class, functions as connection to a database.
 * 
 * @author Tobias Arndt
 * 
 */
public final class DBConnection {

	private final Connection con;

	public DBConnection(String sever, String user, String pw) {
		this.con = connectToDB(sever, user, pw);
	}

	/**
	 * Establishes a connection to a database.
	 * 
	 * @param sever
	 *            Server IP to connect to.
	 * @param user
	 *            User name.
	 * @param pw
	 *            Password.
	 * @return The established connection.
	 */
	private Connection connectToDB(String sever, String user, String pw) {
		try {
			Class.forName("org.mariadb.jdbc.Driver").newInstance();
			System.out.println("SQL-Driver Loaded....");
			Connection con = (DriverManager.getConnection(sever, user, pw));

			return con;

		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException:\n" + e.toString());
			e.printStackTrace();

		} catch (SQLException e) {
			System.out.println("SQLException:\n" + e.toString());
			e.printStackTrace();

		} catch (InstantiationException e) {
			System.out.println("InstantiationException:\n" + e.toString());
			e.printStackTrace();

		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException:\n" + e.toString());
			e.printStackTrace();
		}
		return null;
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
			rs.close();
			st.close();

		} catch (SQLException e) {
			System.out.println("SQLException:\n" + e.toString());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Use this functions for insert-, update-, create-, ... statements.
	 * 
	 * @param query
	 *            The SQL-Query to be executed (except selects).
	 */
	public int executeUpdate(String query) {
		if (query.toLowerCase().startsWith("select")) {
			throw new WrongStatementException();
		}
		Statement st = null;
		int changed = 0;
		try {
			st = con.createStatement();
			changed = st.executeUpdate(query);
			st.close();

		} catch (SQLException e) {
			System.out.println("SQLException:\n" + e.toString());
			e.printStackTrace();
		}
		return changed;
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
}