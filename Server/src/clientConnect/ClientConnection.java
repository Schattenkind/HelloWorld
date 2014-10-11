package clientConnect;

import helper.ConsoleWriter;

import java.io.IOException;
import java.net.Socket;

import user.User;

public class ClientConnection {

	private Socket client;
	private InputStream in;
	private OutputStream out;
	private User user;

	public ClientConnection(Socket client, User user) {
		this.setClient(client);
		this.user = user;
		this.setIn(new InputStream(this));
		this.setOut(new OutputStream(this));
		new Thread(in).start();
		new Thread(out).start();
	}

	public OutputStream getOut() {
		return out;
	}

	public void setOut(OutputStream out) {
		this.out = out;
	}

	public InputStream getIn() {
		return in;
	}

	public void setIn(InputStream inputStream) {
		this.in = inputStream;
	}

	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void sendMessage(String message) {
		this.getOut().setMessage(message);
		synchronized (this.getOut()) {
			this.getOut().notify();
		}
	}

	public boolean close() {
		if (in.close() && out.close()) {
			try {
				client.close();
			} catch (IOException e) {
				ConsoleWriter.write(
						"Failed to close client socket: " + client.toString(),
						true);
				ConsoleWriter.write(e);
				return false;
			}
			ConsoleWriter.write("Connection closed: " + client.toString());
			return true;
		}
		return false;
	}
}
