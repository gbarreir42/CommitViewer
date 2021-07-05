package models;

abstract class abstract_Commit {
	String sha;
	
	public abstract String toString();
	
	public abstract_Commit (String sha)
	{
		this.sha = sha;
	}
	
	public String getSHA()
	{
		return this.sha;
	}
	
	public String split_message(String message)
	{
		String good_message = "";
		for (int i = 0,  j = 1; i < message.length() && j <= message.length(); i++, j++)
		{
			good_message += message.charAt(i);
			if ( j % 40 == 0)
				good_message += "\n ";
		}
		return good_message;
	}
	
}
