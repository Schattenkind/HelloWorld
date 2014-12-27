package user;

import helper.ConsoleWriter;

import java.net.Socket;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;

import secure.Password;
import server.Server;
import clientConnect.ClientConnection;

public class User {

	private static final int DBCOLUMNS = 7;
	private String username;
	private ClientConnection client;

	public User(String username, Socket client) {
		this.username = username;
		this.client = new ClientConnection(client, this);
	}

	public String getId() {
		return username;
	}

	public void setId(String id) {
		this.username = id;
	}

	public ClientConnection getClient() {
		return client;
	}

	public void setClient(ClientConnection client) {
		this.client = client;
	}

	public void addUser(String[] info) {
		if (info.length != User.DBCOLUMNS) {
			this.client.sendMessage("NEWUSER;FALSE");
			return;
		}
		String pw = info[1];
		String username = info[2];
		String email = info[3];
		String birthdate = info[4];
		String name = info[5];
		String surname = info[6];
		if (isValidName(surname) && isValidName(name)
				&& isValidUserName(username) && isValidMail(email)
				&& isValidPW(pw)) {
			try {
				int a = Server
						.getDb()
						.executeUpdate(
								"INSERT INTO `user` (`PASSWORD`, `USERNAME`, `EMAIL`, `BIRTHDATE`, `NAME`, `SURNAME`) VALUES ('"
										+ Password.hashPassword(pw)
										+ "', '"
										+ username
										+ "','"
										+ email
										+ "', Cast('"
										+ birthdate
										+ "' as date), '"
										+ name
										+ "','"
										+ surname + "');");
				if (a == 1) {
					this.client.sendMessage("NEWUSER;TRUE");
				} else {
					this.client.sendMessage("NEWUSER;FALSE");
				}
			} catch (SQLIntegrityConstraintViolationException e) {
				this.client.sendMessage("NEWUSER;EXISTS");
			} catch (SQLDataException e) {
				this.client.sendMessage("NEWUSER;DATE");
			} catch (SQLException e) {
				ConsoleWriter.write(e);
			}
		}
	}

	public void sendUserInfo() {
		HashMap<String, String> userinfo = Server
				.getDb()
				.selectQuery(
						"select * from user where USERNAME = '" + username
								+ "'").get(0);
		this.client.sendMessage("USERINFO;" + userinfo.get("USERNAME") + ";"
				+ userinfo.get("EMAIL") + ";" + userinfo.get("BIRTHDATE") + ";"
				+ userinfo.get("NAME") + "," + userinfo.get("SURNAME") + ";"
				+ userinfo.get("ID"));
	}

	private boolean isValidUserName(String username) {
		// TODO Auto-generated method stub
		return true;
	}

	public void verifyPassword(String username, String pw) {
		String password = Server
				.getDb()
				.selectQuery(
						"select PASSWORD from user where USERNAME = '"
								+ username + "'").get(0).get("PASSWORD");
		if (Password.checkPassword(pw, password)) {
			this.username = username;
			this.client.sendMessage("LOGIN;TRUE;" + username);
		} else {
			this.client.sendMessage("LOGIN;FALSE");
		}
	}

	private boolean isValidName(String name) {
		if (name.length() > 3 && name.length() < 20) {
			return true;
		}
		return false;

	}

	// TODO
	private boolean isValidMail(String mail) {
		return true;
	}

	// TODO
	private boolean isValidPW(String pw) {
		return true;
	}
}
