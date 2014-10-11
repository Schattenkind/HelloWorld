package server;

import helper.Config;
import helper.ConsoleWriter;

import java.util.ArrayList;
import java.util.List;

import user.User;
import dbConnect.DBConnection;

public class Server {

	private static DBConnection db;
	private static Thread listener;
	private static List<User> users;
	public static int port = 1234;

	public Server() {
		Config.loadConfig();
		initialize();
		start();
	}

	public static DBConnection getDb() {
		return db;
	}

	public static void setDb(DBConnection db) {
		Server.db = db;
	}

	public static List<User> getUsers() {
		return users;
	}

	public static void addUser(User user) {
		Server.users.add(user);
	}

	private void initialize() {

		try {
			Server.port = Integer.valueOf(Config.getValue("PORT"));
		} catch (NumberFormatException e) {
			ConsoleWriter.write("Server port must be a number, please check the config file!");
			return;
		}

		Server.users = new ArrayList<User>();

		Server.db = new DBConnection(Config.getValue("DBURL"),
				Config.getValue("DBUSER"), Config.getValue("DBPW"));

		if (db != null) {
			ConsoleWriter.write("Connected to the database");
		}
		// TODO

		Server.listener = new Thread(new Listener());
		if (listener != null) {
			ConsoleWriter.write("Server initilized");
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