package clientConnect;

import java.net.Socket;

public class ClientConnection {

	private Socket client;
	private InputStream in;
	private OutputStream out;

	public ClientConnection(Socket client) {
		this.setClient(client);
		this.setIn(new InputStream(this));
		this.setOut(new OutputStream(this));
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
}
