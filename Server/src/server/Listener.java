package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import clientConnect.ClientConnection;

public class Listener implements Runnable {

	private boolean isRunning;
	private int port;

	public Listener(int port) {
		this.port = port;
		this.isRunning = false;
	}

	@Override
	public void run() {
		this.isRunning = true;
		Socket client = null;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(this.port);
			while (isRunning()) {
				System.out.println("Waiting for incoming connections on port: "
						+ this.port + "....");
				client = serverSocket.accept();
				handleConnection(client);

			}
		} catch (IOException e) {
			System.out.println("IOException:\n" + e.toString());
			e.printStackTrace();
		} finally {
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					System.out.println("IOException:\n" + e.toString());
					e.printStackTrace();
				}
			}
		}
	}

	private void handleConnection(Socket client) throws IOException {
		Server.addClients(new ClientConnection(client));
	}

	private boolean isRunning() {
		return isRunning;
	}

	public void stop() {
		this.isRunning = false;
	}

}
