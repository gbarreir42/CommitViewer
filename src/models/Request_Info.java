package models;

public class Request_Info {
	String message;
	Author author;
	public Request_Info(String message, Author author)
	{
		this.message = message;
		this.author = author;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public Author getAuthor() {
		return this.author;
	}
}
