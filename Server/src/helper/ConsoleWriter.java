package helper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

public class ConsoleWriter {

	public static void write(String message, boolean log) {
		// TODO write to log
		Calendar cal = Calendar.getInstance();
		String date = cal.get(Calendar.DAY_OF_MONTH) + "."
				+ (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR);
		String time = cal.get(Calendar.HOUR_OF_DAY) + ":"
				+ cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND)
				+ "." + cal.get(Calendar.MILLISECOND);
		System.out.println("[" + date + "][" + time + "] " + message);
	}

	public static void write(String message) {
		ConsoleWriter.write(message, false);
	}
	
	public static void write(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exception = sw.toString();
		ConsoleWriter.write(exception, true);
	}
}
