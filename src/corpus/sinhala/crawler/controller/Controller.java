package corpus.sinhala.crawler.controller;

public class Controller {
	public static void main(String args[]){

		String jarPath = "/home/maduranga/workspace/corpus.sinhala.crowler/target/corpus.sinhala.crowler-1.0-SNAPSHOT-jar-with-dependencies.jar";
		String start = "2010/1/1";
		String end = "2010/1/2";
		String host = "127.0.0.1";
		int port = 12345;
		String savePath = "/home/maduranga/data/";
		
		Crawl c= new Crawl(jarPath, start, end, host, port, savePath);
		Thread t = new Thread(c);
		t.start();
	}
}
