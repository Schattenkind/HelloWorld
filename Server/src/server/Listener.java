package server;

import helper.ConsoleWriter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import user.User;

public class Listener implements Runnable {

	private boolean isRunning;

	public Listener() {
		this.isRunning = false;
	}

	// TODO listener outside of a thread
	@Override
	public void run() {
		this.isRunning = true;
		Socket client = null;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(Server.port);
			// TODO BAD WAY TO INTERRUPT, do i even need this or is it enough to
			// stop the thread?
			while (isRunning()) {
				ConsoleWriter.write("Waiting for incoming connections on port "
						+ Server.port);
				client = serverSocket.accept();
				ConsoleWriter.write("Connection established: "
						+ client.toString());
				handleConnection(client);

			}
		} catch (IOException e) {
			try {
				serverSocket.close();
			} catch (IOException e1) {
				ConsoleWriter.write(e1);
			}
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e1) {
				ConsoleWriter.write(e1);
			}
		}
	}

	private void handleConnection(Socket client) {
		Server.addUser(new User("-1", client));
	}

	private boolean isRunning() {
		return isRunning;
	}

	public void stop() {
		this.isRunning = false;
	}

}
