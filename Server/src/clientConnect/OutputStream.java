package clientConnect;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class OutputStream implements Runnable {

	private BufferedWriter out;
	private String message;

	public OutputStream(Socket clientSocket) {
		try {
			setOut(new BufferedWriter(new OutputStreamWriter(
					clientSocket.getOutputStream())));
		} catch (IOException e) {
			System.out.println("IOException:\n" + e.toString());
			e.printStackTrace();
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

		} catch (IOException e) {
			System.out.println("IOException:\n" + e.toString());
			e.printStackTrace();

		} catch (InterruptedException e) {

			try {
				out.close();
			} catch (IOException e1) {
				System.out.println("IOException:\n" + e.toString());
				e.printStackTrace();
			}
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
