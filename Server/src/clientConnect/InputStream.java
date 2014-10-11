package clientConnect;

import helper.ConsoleWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;

public class InputStream implements Runnable {

	private BufferedReader in;
	private ClientConnection clientCon;

	public InputStream(ClientConnection clientCon) {
		this.clientCon = clientCon;
		try {
			this.in = (new BufferedReader(new InputStreamReader(clientCon
					.getClient().getInputStream())));
		} catch (IOException e) {
			ConsoleWriter.write(e);
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				try {
					processInput(in.readLine());
				} catch (SocketException e2) {
					try {
						in.close();
					} catch (IOException e1) {
						ConsoleWriter.write(e1);
					}
				}

			}
		} catch (IOException e) {
			if (!clientCon.close()) {
				ConsoleWriter.write(e);
			}
		}

	}

	private void processInput(String incoming) {
		String[] split = incoming.split(";");
		String command = split[0].toUpperCase();
		if (command.equals("NEWUSER")) {
			this.clientCon.getUser().addUser(split);
		} else if (command.equals("MESSAGE")) {

		} else if (command.equals("LOGIN")) {
			this.clientCon.getUser().verifyPassword(split[1], split[2]);
		} else if (command.equals("PING")) {

		} else if (command.equals("SEARCHUSER")) {

		} else if (command.equals("ADDFRIEND")) {

		}
		/*
		 * for (User c : Server.getUsers()) { ClientConnection out =
		 * c.getClient(); if (c.getClient().getIn() != this) {
		 * out.sendMessage(incoming); } }
		 */
	}

	public boolean close() {
		try {
			this.in.close();
		} catch (IOException e) {
			ConsoleWriter.write(e);
			return false;
		}
		return true;
	}
}
