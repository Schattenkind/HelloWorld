package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import clientConnect.ClientConnection;
import dbConnect.DBConnection;

public class Server {

	private static DBConnection db;
	private static Thread listener;
	private static List<ClientConnection> clients;
	private int port = 1234;

	public Server() {
		initialize();
		start();
	}

	public DBConnection getDb() {
		return db;
	}

	public void setDb(DBConnection db) {
		Server.db = db;
	}

	public static List<ClientConnection> getClients() {
		return clients;
	}

	public static void addClients(ClientConnection clients) {
		Server.clients.add(clients);
	}

	private Connection connectToDB() {
		try {
			Class.forName("org.mariadb.jdbc.Driver").newInstance();
			System.out.println("SQL-Driver Loaded....");
			Connection con = (DriverManager.getConnection(
					"jdbc:mysql://localhost:55555/test", "root", "root"));

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

	private void initialize() {
		Server.clients = new ArrayList<ClientConnection>();
		Server.db = new DBConnection(connectToDB());
		if (db != null) {
			System.out.println("Connected to the database....");
		}
		Server.listener = new Thread(new Listener(this.port));
		if (listener != null) {
			System.out.println("Server initilized....");
		}

	}

	private void start() {
		listener.start();
	}

	public static void main(String[] args) {
		new Server();
	}

}