package corpus.sinhala.crawler.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import corpus.sinhala.crawler.controller.db.DbConnector;

public class Server implements Runnable {
	
	static final Logger log = Logger.getLogger(Server.class);
	
	int port;
	DbConnector dbConnector;
	int crawlerId;
	
	@Override
	public void run() {
		try {
			receive();
		} catch (IOException e) {
			System.out.println(e);
			log.error("IOException",e);
		} catch (SQLException e) {
			System.out.println(e);
			log.error("SQLException",e);
		}

	}

	public Server(int crawlerId, int port) {
		this.crawlerId = crawlerId;
		this.port = port;
		dbConnector = DbConnector.getInstance();
	}

	public void receive() throws IOException, SQLException {
		dbConnector.changeCrawlerState(crawlerId, true, port);
		
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Server> socket created");
		log.debug("Server started at port " + port);
		
		Socket socket = serverSocket.accept();
		System.out.println("Socket> accepted");
		BufferedReader input = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		String date;
		while ((date = input.readLine()) != null
				&& !date.equalsIgnoreCase("close")) {
			if (!date.equalsIgnoreCase("check")) {
				System.out.println("Server> Input: " + date);
				dbConnector.saveDate(crawlerId, date);
			}
		}
		System.out.println("Server> Socket closed");
		socket.close();
		serverSocket.close();
		log.debug("Server at port " + port + " cloased");
		dbConnector.changeCrawlerState(crawlerId, true, port);
	}

}
