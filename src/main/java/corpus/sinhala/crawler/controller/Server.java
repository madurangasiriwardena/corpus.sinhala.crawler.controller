package corpus.sinhala.crawler.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	int port;
	
	@Override
	public void run() {
		try {
			receive();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Server(int port) {
		this.port = port;
	}

	public void receive() throws IOException {
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Server> socket created");
		Socket socket = serverSocket.accept();
		System.out.println("Socket> accepted");
//		BufferedReader input = new BufferedReader(new InputStreamReader(
//				socket.getInputStream()));
//		String date;
//		while ((date = input.readLine()) != null
//				&& !date.equalsIgnoreCase("close")) {
//			if (!date.equalsIgnoreCase("check")) {
//				System.out.println("Server> Input: " + date);
//			}
//		}
		System.out.println("Server> Socket closed");
		socket.close();
		serverSocket.close();
	}

}
