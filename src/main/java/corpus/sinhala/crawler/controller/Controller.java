package corpus.sinhala.crawler.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Controller {
	public static void main(String args[]) throws IOException{
		
		int controllerPort = 11223;
		
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
			
			String jarPath = temp[0];
			String start = temp[1];
			String end = temp[2];
			String host = temp[3];
			int port = Integer.parseInt(temp[4]);
			String savePath = temp[5];
			
//			String jarPath = "/home/maduranga/workspace/corpus.sinhala.crowler/target/corpus.sinhala.crowler-1.0-SNAPSHOT-jar-with-dependencies.jar";
//			String start = "2010/1/1";
//			String end = "2010/1/2";
//			String host = "127.0.0.1";
//			int port = 12345;
//			String savePath = "/home/maduranga/data/";
			
			Crawl c= new Crawl(jarPath, start, end, host, port, savePath);
			Thread t = new Thread(c);
			t.start();
		}
		

		
	}
}
