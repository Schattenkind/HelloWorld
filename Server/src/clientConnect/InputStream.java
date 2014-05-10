package clientConnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import server.Server;
import user.User;

public class InputStream implements Runnable {

	private BufferedReader in;

	public InputStream(Socket clientSocket) {
		try {
			setIn(new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream())));
		} catch (IOException e) {
			System.out.println("IOException:\n" + e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				processInput(in.readLine());
			}
		} catch (IOException e) {
			System.out.println("IOException:\n" + e.toString());
			e.printStackTrace();
		}
	}

	private void processInput(String incoming) {
		for (User c : Server.getUsers()) {
			ClientConnection out = c.getClient();
			if (c.getClient().getIn() != this) {
				out.sendMessage(incoming);
			}
		}
	}

	public BufferedReader getIn() {
		return in;
	}

	public void setIn(BufferedReader in) {
		this.in = in;
	}

}
