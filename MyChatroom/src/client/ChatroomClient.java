package client;

import java.io.*;
import java.net.*;

import javax.swing.plaf.synth.SynthSpinnerUI;

public class ChatroomClient {
	public static void main(String args[]) {
		if (args.length != 2) {
			System.err.println("usage: java ChatroomClient [hostname] [portnumber]");
			System.exit(1);
		}
		
		String hostname = args[0];
		int portnumber = Integer.parseInt(args[1]);
		
		String nextLine;
		try (
			Socket socket = new Socket(hostname, portnumber);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
			OutputThread outThread = new OutputThread(in);
		) {
			System.out.println("Enter your username: ");
			out.println(stdin.readLine());
			
			System.out.println("Chatroom: ");
			outThread.start();
			while (true) {
				nextLine = stdin.readLine();
				out.println(nextLine);
			}
		} catch (IOException e) {
			synchronized (System.err) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	public static class OutputThread extends Thread implements AutoCloseable {
		BufferedReader in;
		
		public OutputThread(BufferedReader in) {
			this.in = in;
		}
		
		@Override
		public void run() {
			try {
				while (true) {
					System.out.println("\n" + in.readLine());
					if(interrupted()) break;
				}
			} catch (IOException e) {
				synchronized(System.err) {
					System.err.println(e.getMessage());
				}
			}
		}
		
		@Override
		public void close() {
			this.interrupt();
		}
	}
}
