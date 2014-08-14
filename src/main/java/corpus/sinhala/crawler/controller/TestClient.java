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
			s = "1"
					+ "|"
					+ "2010/1/4"
					+ "|"
					+ "2010/1/6"
					+ "|" + 12346;

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
