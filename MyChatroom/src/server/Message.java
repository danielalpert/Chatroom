package server;

import java.time.LocalDateTime;

public class Message {
	private final String content;
	private final String from;
	private final LocalDateTime datetime;
	
	public Message(String from, String content) {
		this.content = content;
		this.from = from;
		this.datetime = LocalDateTime.now();
	}

	public String getContent() {
		return content;
	}

	public String getFrom() {
		return from;
	}

	public LocalDateTime getDatetime() {
		return datetime;
	}
	
	@Override
	public String toString() {
		return from + ": " + content + " at " + datetime;
	}
}
