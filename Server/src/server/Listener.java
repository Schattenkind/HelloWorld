package server;

import helper.ConsoleWriter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import user.User;

public class Listener implements Runnable {

	public Listener() {
	}
	@Override
	public void run() {
		Socket client = null;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(Server.port);

			while (true) {
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
		Server.addUser(new User(null , client));
	}
}
