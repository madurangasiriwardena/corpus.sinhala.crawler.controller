package corpus.sinhala.crawler.controller;

import java.io.IOException;


public class Crawl implements Runnable{
	Process p;
	String jarPath;
	String start;
	String end;
	String host;
	int port;
	String savePath;
	
	public Crawl(String jarPath, String start, String end, String host, int port, String savePath){
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
			Server server = new Server(port);
			Thread t = new Thread(server);
			t.start();
			p = Runtime.getRuntime().exec("java -jar "+jarPath+" "+start+" "+end+" "+" "+host+" "+port+" "+savePath);
			p.waitFor();
			System.out.println("Crawl>"+p.exitValue());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
