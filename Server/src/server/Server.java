package server;

import dbConnect.DBConnection;

public class Server {

	private DBConnection db;

	public static void main(String[] args) {
		Server server = new Server();
		server.startServer();
		server.getDb().closeConnection();

	}

	private void connectDB() {
		setDb(new DBConnection());
	}

	private void startServer() {
		connectDB();
	}

	public DBConnection getDb() {
		return db;
	}

	public void setDb(DBConnection db) {
		this.db = db;
	}
}