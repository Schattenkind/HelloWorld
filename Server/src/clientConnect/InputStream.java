package clientConnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import server.Server;

public class InputStream implements Runnable {

	private ClientConnection client;
	private BufferedReader in;
	private Socket clientSocket;
	private Thread listen;

	public InputStream(ClientConnection clientConnection) {
		this.client = clientConnection;
		this.clientSocket = clientConnection.getClient();
		this.listen = new Thread(this);
		this.listen.start();
	}

	@Override
	public void run() {
		try {
			setIn(new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream())));
			String incoming = in.readLine();
			for (ClientConnection c : Server.getClients()) {
				c.getOut().setMessage(incoming);
				new Thread(c.getOut()).start();
			}

		} catch (IOException e) {
			System.out.println("IOException:\n" + e.toString());
			e.printStackTrace();
		}

	}

	public BufferedReader getIn() {
		return in;
	}

	public void setIn(BufferedReader in) {
		this.in = in;
	}

}
