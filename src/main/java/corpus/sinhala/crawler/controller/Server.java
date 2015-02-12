/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
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

	private int port;
	private DbConnector dbConnector;
	private int crawlerId;
	
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
