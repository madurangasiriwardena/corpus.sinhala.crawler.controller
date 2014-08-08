package corpus.sinhala.crawler.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestClient {
	public static void main(String args[]) throws UnknownHostException, IOException {
		Socket socket = new Socket("127.0.0.1", 11223);
		OutputStreamWriter output = new OutputStreamWriter(
				socket.getOutputStream());

		try {

			output.write("check\n");
			output.flush();

			String s;
			s = "/home/maduranga/workspace/corpus.sinhala.crowler/target/corpus.sinhala.crowler-1.0-SNAPSHOT-jar-with-dependencies.jar"
					+ "|"
					+ "2010/1/1"
					+ "|"
					+ "2010/1/3"
					+ "|"
					+ "127.0.0.1"
					+ "|" + 12345 + "|" + "/home/maduranga/data/";

			output.write(s);
			output.write("\n");
			output.flush();
			
			output.write("check\n");
			output.flush();
			output.write("close\n");
			output.flush();
		} catch (IOException e) {
		}

		output.close();
		socket.close();
	}
}
