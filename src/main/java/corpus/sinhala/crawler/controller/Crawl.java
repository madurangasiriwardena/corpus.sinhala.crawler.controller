package corpus.sinhala.crawler.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Crawl implements Runnable {
	Process p;
	String jarPath;
	String start;
	String end;
	String host;
	int port;
	String savePath;
	int crawlerId;

	public Crawl(int crawlerId, String jarPath, String start, String end, String host,
			int port, String savePath) {
		this.crawlerId = crawlerId;
		this.jarPath = jarPath;
		this.start = start;
		this.end = end;
		this.host = host;
		this.port = port;
		this.savePath = savePath;
	}

	@Override
	public void run() {
		try {
			System.out.println("------------" +port);
			Server server = new Server(crawlerId, port);
			Thread t = new Thread(server);
			t.start();
			String command = "java -jar " + jarPath + " " + start + " " + end
					+ " " + " " + host + " " + port + " " + savePath;
			System.out.println(command);
			p = Runtime.getRuntime().exec(command);
//			String line;
//			BufferedReader bri = new BufferedReader(new InputStreamReader(
//					p.getInputStream()));
//			BufferedReader bre = new BufferedReader(new InputStreamReader(
//					p.getErrorStream()));
//			while ((line = bri.readLine()) != null) {
//				System.out.println(line);
//			}
//			bri.close();
//			while ((line = bre.readLine()) != null) {
//				System.out.println(line);
//			}
//			bre.close();
			p.waitFor();
			System.out.println("Crawl>" + p.exitValue());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
