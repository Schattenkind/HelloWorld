package dbConnect;

public class WrongStatementException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WrongStatementException() {
		super();
	}

	public WrongStatementException(String message) {
		super(message);
	}

	public WrongStatementException(String message, Throwable cause) {
		super(message, cause);
	}

	public WrongStatementException(Throwable cause) {
		super(cause);
	}
}