package server;

import helper.Config;

import java.util.ArrayList;
import java.util.List;

import user.User;
import dbConnect.DBConnection;

public class Server {

	private static DBConnection db;
	private static Thread listener;
	private static List<User> users;
	private int port = 1234;

	public Server() {
		Config.loadConfig();
		initialize();
		start();
	}

	public DBConnection getDb() {
		return db;
	}

	public void setDb(DBConnection db) {
		Server.db = db;
	}

	public static List<User> getUsers() {
		return users;
	}

	public static void addUser(User user) {
		Server.users.add(user);
	}

	private void initialize() {
		Server.users = new ArrayList<User>();
		Server.db = new DBConnection(Config.getValue("DBURL"),
				Config.getValue("DBUSER"), Config.getValue("DBPW"));
		if (db != null) {
			System.out.println("Connected to the database....");
		}
		// TODO
		Server.listener = new Thread(new Listener(this.port));
		if (listener != null) {
			System.out.println("Server initilized....");
		}
		// TODO

	}

	private void start() {
		listener.start();
	}

	public static void main(String[] args) {
		new Server();
	}

}