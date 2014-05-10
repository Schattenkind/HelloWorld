package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import user.User;
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
				System.out.println("Waiting for incoming connections on port "
						+ this.port + "....");
				client = serverSocket.accept();
				System.out.println(client.toString());
				handleConnection(client);

			}
		} catch (IOException e) {
			System.out.println("IOException:\n" + e.toString());
			e.printStackTrace();
		}
	}

	private void handleConnection(Socket client) {
		System.out.println("Client connected!");
		Server.addUser(new User(-1, new ClientConnection(client)));
	}

	private boolean isRunning() {
		return isRunning;
	}

	public void stop() {
		this.isRunning = false;
	}

}
