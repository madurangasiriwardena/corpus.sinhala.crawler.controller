package corpus.sinhala.crawler.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import corpus.sinhala.crawler.controller.db.DbConnector;

public class Crawl implements Runnable {
	
	static final Logger log = Logger.getLogger(Crawl.class);
	
	Process p;
	String jarPath;
	String start;
	String end;
	String host;
	int port;
	String savePath;
	int crawlerId;
	DbConnector dbConnector;

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
			System.out.println(command);
			p = Runtime.getRuntime().exec(command);
			log.debug("Crawler at " + jarPath + " started");
			String line;
			BufferedReader bri = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			BufferedReader bre = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));
			while ((line = bri.readLine()) != null) {
				System.out.println(line);
			}
			bri.close();
			while ((line = bre.readLine()) != null) {
				System.out.println(line);
			}
			bre.close();
			p.waitFor();
			int exitValue = p.exitValue();
			System.out.println("Crawl>" + exitValue);
			if(exitValue == 0){
				log.debug("Crawler at " + jarPath + " finished successfully");
			}else{
				log.debug("Crawler at " + jarPath + " interrupted");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			log.error("IOException", e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("InterruptedException", e);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("SQLException", e);
		}

	}

}
