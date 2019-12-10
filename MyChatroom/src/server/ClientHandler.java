package server;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ClientHandler extends Thread {
	private Socket client;
	private BlockingQueue<Message> queue;
	private String username;
	private Chatroom chatroom;
	
	public ClientHandler (Socket client, Chatroom chatroom) {
		this.client = client;
		this.chatroom = chatroom;
		queue = new LinkedBlockingQueue<Message>();
		username = null;
	}
	
	@Override
	public void run() {
		try (
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			OutputThread out = new OutputThread(new PrintWriter(client.getOutputStream(), true), queue);
		) {
			username = in.readLine();
			System.err.println( username);
			chatroom.join(queue);
			out.start();
			String nextLine;
			while ((nextLine = in.readLine()) != null) {
				System.err.println(nextLine);
				Message m = new Message(username, nextLine);
				chatroom.broadcast(m);
			}
			
			chatroom.exit(queue);
			client.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	private class OutputThread extends Thread implements AutoCloseable {
		private PrintWriter out;
		private BlockingQueue<Message> queue;
		
		public OutputThread(PrintWriter out, BlockingQueue<Message> queue) {
			this.out = out;
			this.queue = queue;
		}
		
		@Override
		public void run() {
			try {
				while (true) {
					out.println(queue.take());
				}
			} catch (InterruptedException e) {
				if (out != null)  out.close();
			}
		}
		
		@Override
		public void close() {
			this.interrupt();
		}
	}
}
