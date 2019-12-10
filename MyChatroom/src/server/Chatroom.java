package server;

import java.util.*;
import java.util.concurrent.*;

public class Chatroom {
	private HashSet<BlockingQueue<Message>> clients;
	
	public Chatroom() {
		clients = new HashSet<BlockingQueue<Message>>();
	}
	
	public synchronized void join(BlockingQueue<Message> userQueue) {
		clients.add(userQueue);
	}
	
	public synchronized boolean exit(BlockingQueue<Message> userQueue) {
		return clients.remove(userQueue);
	}
	
	public synchronized void broadcast(Message m) {
		for (Queue<Message> queue : clients) synchronized (queue) {
			queue.add(m);
		}
	}
}
