package models;

public class Commit extends abstract_Commit{
	private String message;
	private String date;
	private String author;

	public String getMessage() {
		return this.message;
	}

	public String getDate() {
		return this.date;
	}

	public String getAuthor() {
		return this.author;
	}
	
	@Override
	public String toString()
	{
		return (" " + super.getSHA() +"\n "
						+ super.split_message(this.message) + "\n " 
							+ this.date + "\n " 
								+ this.author + "\n");
	}
	
	public Commit(String sha, String message, String date, String author)
	{
		super(sha);
		this.message = message;
		this.date = date;
		this.author = author;
	}
}
