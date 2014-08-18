package corpus.sinhala.crawler.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Controller {
	static final Logger log = Logger.getLogger(Controller.class);
	
	public static void main(String args[]) throws IOException {
		try {
			String log4jConfPath = SysProperty.getProperty("logPath") + "/log4j.properties";
			PropertyConfigurator.configure(log4jConfPath);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		int controllerPort = 11223;

		String saveBasePath = SysProperty.getProperty("savePath");
		String host = SysProperty.getProperty("dbHost");

		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(controllerPort);
		System.out.println("Server> socket created");

		while (true) {
			Socket socket = serverSocket.accept();
			System.out.println("Socket> accepted");
			log.debug("Job received");
			BufferedReader input = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String msg;
			String data = "";
			while ((msg = input.readLine()) != null
					&& !msg.equalsIgnoreCase("close")) {
				System.out.println("Server> Input: " + msg);
				data = msg;
			}
			System.out.println("Server> Socket closed");
			socket.close();

			String[] temp = data.split("\\|");

			int crawlerId = Integer.parseInt(temp[0]);
			String start = temp[1];
			String end = temp[2];

			int port = Integer.parseInt(temp[3]);
			String savePath = saveBasePath + "/" + crawlerId;

			Crawl c = new Crawl(crawlerId, start, end, host, port, savePath);
			Thread t = new Thread(c);
			t.start();

		}

	}
}
