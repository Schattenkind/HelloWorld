package user;

import helper.ConsoleWriter;

import java.net.Socket;
import java.sql.SQLException;

import secure.Password;
import server.Server;
import clientConnect.ClientConnection;

public class User {

	private String id;
	private ClientConnection client;

	public User(String id, Socket client) {
		this.id = id;
		this.client = new ClientConnection(client, this);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ClientConnection getClient() {
		return client;
	}

	public void setClient(ClientConnection client) {
		this.client = client;
	}

	public void addUser(String[] info) {
		String surname = info[1];
		String name = info[2];
		String birthdate = info[3];
		String email = info[4];
		String nickname = info[5];
		String pw = info[6];
		if (isValidName(surname) && isValidName(name) && isValidName(nickname)
				&& isValidMail(email) && isValidPW(pw)) {
			try {
				Server.getDb()
						.executeUpdate(
								"INSERT INTO `user` (`PW`, `SURNAME`, `NAME`, `BIRTHDATE`, `EMAIL`, `NICKNAME`) VALUES ('"
										+ Password.hashPassword(pw)
										+ "', '"
										+ surname
										+ "','"
										+ name
										+ "', Cast('"
										+ birthdate
										+ "' as datetime), '"
										+ email + "','" + nickname + "');");
			} catch (SQLException e) {
				ConsoleWriter.write(e);
			}
		}
	}
	
	public void verifyPassword(String id, String pw){
		String password = Server.getDb().selectQuery("select PW from user where ID = " + id).get(0).get("PW");
		if (Password.checkPassword(pw, password)){
			this.id = id;
			this.client.sendMessage("LOGIN;TRUE");
		}
		this.client.sendMessage("LOGIN;FALSE");
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
