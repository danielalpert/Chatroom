package server;

import java.net.*;
import java.io.*;
//import java.util.*;
//import java.util.concurrent.*;

public class ChatroomServer {
	private static int portNumber;
	private static Chatroom chatroom;
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("usage: java ChatroomServer portnumber");
			System.exit(1);
		}
		
		portNumber = Integer.parseInt(args[0]);
		chatroom = new Chatroom();
		
		Socket client;
		
		try (ServerSocket server = new ServerSocket(portNumber)) {
			while (true) {
				client = server.accept();
				ClientHandler handler = new ClientHandler(client, chatroom);
				handler.start();
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
}
