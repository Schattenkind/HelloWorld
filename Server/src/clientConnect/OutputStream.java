package clientConnect;

import helper.ConsoleWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class OutputStream implements Runnable {

	private BufferedWriter out;
	private ClientConnection clientCon;
	private String message;

	public OutputStream(ClientConnection clientCon) {
		this.clientCon = clientCon;
		try {
			this.out = (new BufferedWriter(new OutputStreamWriter(clientCon
					.getClient().getOutputStream())));
		} catch (IOException e) {
			ConsoleWriter.write(e);
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				synchronized (this) {
					this.wait();
					out.write(message);
					out.newLine();
					out.flush();
				}
			}

		} catch (Exception e) {
			if (!clientCon.close()) {
				ConsoleWriter.write(e);
			}
		}
	}

	public boolean close() {
		try {
			this.out.close();
		} catch (IOException e) {
			ConsoleWriter.write(e);
			return false;
		}
		return true;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
