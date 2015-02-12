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
import java.sql.SQLException;

import org.apache.log4j.Logger;

import corpus.sinhala.crawler.controller.db.DbConnector;

public class Crawl implements Runnable {
	
	static final Logger log = Logger.getLogger(Crawl.class);
	
	private Process p;
	private String jarPath;
	private String start;
	private String end;
	private String host;
	private int port;
	private String savePath;
	private int crawlerId;
	private DbConnector dbConnector;

	public Crawl(int crawlerId, String start, String end, String host,
			int port, String savePath) {
		this.crawlerId = crawlerId;
		this.start = start;
		this.end = end;
		this.host = host;
		this.port = port;
		this.savePath = savePath;
	}

	@Override
	public void run() {
		try {
			dbConnector = DbConnector.getInstance();
			jarPath = dbConnector.getCrawlerPath(crawlerId);
			
			Server server = new Server(crawlerId, port);
			Thread t = new Thread(server);
			t.start();
			String command = "java -jar " + jarPath + " " + start + " " + end
					+ " " + " " + host + " " + port + " " + savePath;

			p = Runtime.getRuntime().exec(command);
			log.info("Crawler at " + jarPath + " started");
			String line;
			BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((line = bri.readLine()) != null) {
				log.info("Message from crawler> " + line);
			}
			bri.close();
			while ((line = bre.readLine()) != null) {
				log.info("Message from crawler> "+line);
			}
			bre.close();
			p.waitFor();
			int exitValue = p.exitValue();
			if(exitValue == 0) {
				log.info("Crawler at " + jarPath + " finished successfully");
			} else {
				log.info("Crawler at " + jarPath + " interrupted");
			}
			
		} catch (IOException e) {
			log.error("IOException", e);
		} catch (InterruptedException e) {
			log.error("InterruptedException", e);
		} catch (SQLException e) {
			log.error("SQLException", e);
		}

	}

}
