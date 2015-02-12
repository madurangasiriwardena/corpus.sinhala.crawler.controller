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
			log.error("IOException",e);
		} catch (SQLException e) {
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
		log.info("Server started at port " + port);
		
		Socket socket = serverSocket.accept();
		log.info("Server accepted at port " + port);
		BufferedReader input = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		String date;
		while ((date = input.readLine()) != null
				&& !date.equalsIgnoreCase("close")) {
			if (!date.equalsIgnoreCase("check")) {
				log.info("Crawler " + crawlerId + " crawled " + date);
				dbConnector.saveDate(crawlerId, date);
			}
		}

		socket.close();
		serverSocket.close();
		log.debug("Server at port " + port + " closed");
		dbConnector.changeCrawlerState(crawlerId, true, port);
	}

}
