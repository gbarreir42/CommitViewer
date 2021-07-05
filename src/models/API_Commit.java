package models;


public class API_Commit extends abstract_Commit {
	Request_Info commit;
	
	public API_Commit(String sha, Request_Info commit){
		super(sha);
		this.commit = commit;
	}

	public Request_Info getInfo() {
		return this.commit;
	}
	
	@Override
	public String toString() {
		return (" " + super.getSHA() +"\n "
				+ super.split_message(this.commit.getMessage()) + "\n " 
					+ this.commit.getAuthor().getDate() + "\n " 
						+ this.getInfo().getAuthor().getName() + "\n");
	}
}
