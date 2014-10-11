package helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {
	private static HashMap<String, String> config = new HashMap<String, String>(
			50);

	public static void loadConfig() {
		// set standard values
		config.put("DBURL", "jdbc:mysql://localhost:55555/server");
		config.put("DBUSER", "root");
		config.put("DBPW", "root");
		config.put("PORT", "1234");

		// overwrite them with the values from the config file
		try (BufferedReader br = new BufferedReader(
				new FileReader("config.txt"))) {
			String line = br.readLine();

			while (line != null) {
				String[] in = line.split("=");
				config.put(in[0], in[1]);
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			// write a config file with standard values when no conifg file
			// exists
			File file = new File("config.txt");

			FileWriter writer;
			try {
				writer = new FileWriter(file, true);
				for (Map.Entry<String, String> entry : config.entrySet()) {
					writer.write(entry.getKey() + "=" + entry.getValue());
					writer.write(System.getProperty("line.separator"));
					writer.flush();
				}

				writer.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getValue(String string) {
		return config.get(string);
	}

}
