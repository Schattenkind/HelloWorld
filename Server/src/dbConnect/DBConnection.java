package dbConnect;

import helper.ConsoleWriter;

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
 * Functions as connection to a database.
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
			ConsoleWriter.write("SQL-Driver Loaded");
			Connection con = (DriverManager.getConnection(sever, user, pw));

			return con;

		} catch (Exception e) {
			ConsoleWriter.write(e);
			return null;
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
	 * @throws SQLException
	 * @throws WrongStatementException
	 */
	public List<HashMap<String, String>> selectQuery(String query) {
		if (!query.toLowerCase().startsWith("select")) {
			throw new WrongStatementException();
		}
		Statement st = null;
		ResultSet rs = null;
		List<HashMap<String, String>> result = null;

		try {
			st = con.createStatement();
			rs = st.executeQuery(query);
			result = convertResultSetToList(rs);
			rs.close();
			st.close();
		} catch (SQLException e) {
			ConsoleWriter.write(e);
		}

		return result;
	}

	/**
	 * Use this functions for insert-, update-, create-, ... statements.
	 * 
	 * @return Returns the amount of rows that have been changed.
	 * 
	 * @param query
	 *            The SQL-Query to be executed (except selects).
	 * @throws SQLException
	 * @throws WrongStatementException
	 */
	public int executeUpdate(String query) throws SQLException {
		if (query.toLowerCase().startsWith("select")) {
			throw new WrongStatementException();
		}
		Statement st = null;
		int changed = 0;

		st = con.createStatement();
		changed = st.executeUpdate(query);
		st.close();

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
			ConsoleWriter.write("DB disconnected successful!");
			return true;
		} catch (SQLException e) {
			ConsoleWriter.write(e);
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
	private List<HashMap<String, String>> convertResultSetToList(ResultSet rs)
			throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		while (rs.next()) {
			HashMap<String, String> row = new HashMap<String, String>(columns);
			for (int i = 1; i <= columns; ++i) {
				row.put(md.getColumnName(i), rs.getObject(i).toString());
			}
			list.add(row);
		}

		return list;
	}

	public Connection getCon() {
		return con;
	}
}