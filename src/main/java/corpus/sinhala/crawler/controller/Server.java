package corpus.sinhala.crawler.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import corpus.sinhala.crawler.controller.db.DbConnector;

public class Server implements Runnable {
	int port;
	DbConnector dbconnector;
	int crawlerId;
	
	@Override
	public void run() {
		try {
			receive();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}

	}

	public Server(int crawlerId, int port) {
		this.crawlerId = crawlerId;
		this.port = port;
		dbconnector = DbConnector.getInstance();
	}

	public void receive() throws IOException, SQLException {
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Server> socket created");
		Socket socket = serverSocket.accept();
		System.out.println("Socket> accepted");
		BufferedReader input = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		String date;
		while ((date = input.readLine()) != null
				&& !date.equalsIgnoreCase("close")) {
			if (!date.equalsIgnoreCase("check")) {
				System.out.println("Server> Input: " + date);
				dbconnector.connect();
				dbconnector.saveDate(crawlerId, date);
			}
		}
		System.out.println("Server> Socket closed");
		socket.close();
		serverSocket.close();
	}

}
