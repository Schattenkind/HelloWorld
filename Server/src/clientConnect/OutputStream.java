package clientConnect;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class OutputStream implements Runnable {

	private ClientConnection client;
	private BufferedWriter out;
	private Socket clientSocket;
	private Thread outstream;
	private String message;

	public OutputStream(ClientConnection clientConnection) {
		this.client = clientConnection;
		this.clientSocket = clientConnection.getClient();
		this.outstream = new Thread(this);
		this.outstream.start();
	}

	@Override
	public void run() {
		try {
			setOut(new BufferedWriter(new OutputStreamWriter(
					clientSocket.getOutputStream())));
			out.write(message);
			out.newLine();
			out.flush();
			out.close();

		} catch (IOException e) {
			System.out.println("IOException:\n" + e.toString());
			e.printStackTrace();
		}

	}

	public BufferedWriter getOut() {
		return out;
	}

	public void setOut(BufferedWriter out) {
		this.out = out;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
