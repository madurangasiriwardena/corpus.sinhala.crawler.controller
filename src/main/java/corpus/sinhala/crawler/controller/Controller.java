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

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Controller {
	static final Logger log = Logger.getLogger(Controller.class);
	
	public static void main(String args[]) throws IOException {
		
		int controllerPort = 11223;

		String saveBasePath = ConfigManager.getProperty(ConfigManager.SAVE_PATH);
		String host = ConfigManager.getProperty(ConfigManager.SERVER_HOST);

		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(controllerPort);
		log.info("Server socket created at port " + controllerPort);

		while (true) {

			Socket socket = serverSocket.accept();
			log.info("Job received");
			BufferedReader input = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String msg;
			String data = "";
			while ((msg = input.readLine()) != null
					&& !msg.equalsIgnoreCase("close")) {
				data = msg;
			}
			log.info("Connection Closed");
			socket.close();

			String[] temp = data.split("\\|");

			int crawlerId = Integer.parseInt(temp[0]);
			String start = temp[1];
			String end = temp[2];

			int port = Integer.parseInt(temp[3]);
			String savePath = saveBasePath + "/" + crawlerId;
			log.info("Starting crawler with id " + crawlerId);
			Crawl c = new Crawl(crawlerId, start, end, host, port, savePath);
			Thread t = new Thread(c);
			t.start();

		}

	}
}
