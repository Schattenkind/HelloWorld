package clientConnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import server.Server;

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
				String incoming = in.readLine();

				for (ClientConnection c : Server.getClients()) {
					c.getOut().setMessage(incoming);
					synchronized (c.getOut()) {
						if (c.getIn() != this) {
							c.getOut().notify();
						}
					}
				}
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
