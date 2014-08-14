package corpus.sinhala.crawler.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import corpus.sinhala.crawler.controller.db.DbConnector;

public class Controller {
	public static void main(String args[]) throws IOException{
		
		int controllerPort = 11223;
		DbConnector dbconnector = new DbConnector();
		
		String saveBasePath = "/home/maduranga/data/";
		String host = "127.0.0.1";
		
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(controllerPort);
		System.out.println("Server> socket created");
		
		while(true){
			Socket socket = serverSocket.accept();
			System.out.println("Socket> accepted");
			BufferedReader input = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String msg;
			String data = "";
			while ((msg = input.readLine()) != null
					&& !msg.equalsIgnoreCase("close")) {
				if (!msg.equalsIgnoreCase("check")) {
					System.out.println("Server> Input: " + msg);
					data = msg;
				}
			}
			System.out.println("Server> Socket closed");
			socket.close();
			
			String[] temp = data.split("\\|");
			
			int crawlerId = Integer.parseInt(temp[0]);
			String start = temp[1];
			String end = temp[2];
			
			int port = Integer.parseInt(temp[3]);
			String savePath = saveBasePath + "/" + crawlerId;
			
			dbconnector.connect();
			String jarPath = dbconnector.getCrawlerPath(crawlerId);
			
			Crawl c= new Crawl(jarPath, start, end, host, port, savePath);
			Thread t = new Thread(c);
			t.start();
		}
		

		
	}
}
