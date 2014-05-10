package user;

import java.util.List;

import clientConnect.ClientConnection;

public class User {

	private int id;
	private ClientConnection client;
	private List<User> friends;

	public User(int id, ClientConnection client) {
		this.id = id;
		this.client = client;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ClientConnection getClient() {
		return client;
	}

	public void setClient(ClientConnection client) {
		this.client = client;
	}

	public List<User> getFriends() {
		return friends;
	}

	public void setFriend(User friend) {
		this.friends.add(friend);
	}

}
