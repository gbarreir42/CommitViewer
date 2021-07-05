package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

import models.API_Commit;
import models.Commit;
import models.CommitList;

public class API_request_handler {
	private String output;
	API_Commit[] commits;
	CommitList list;
	String link;

	public API_request_handler(String link) throws FileNotFoundException, NullPointerException, IOException
	{
		this.link = link;
	}
	
	public void connect() throws IOException, FileNotFoundException, NullPointerException
	{
		URL url;
		url = new URL("https://api.github.com/repos/"+ get_missing(this.link) +"/commits");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);
		con.setRequestMethod("GET");
		save_output(con);
		Gson gson = new Gson();	    
		this.commits = gson.fromJson(output, API_Commit[].class);
		con.disconnect();
	}

	public void convert()
	{
		CommitList CL = new CommitList();
		if (commits.length > 0) {
			for (int i = 0; i < commits.length; i++)
			{
				Commit c = new Commit(this.commits[i].getSHA(), this.commits[i].getInfo().getMessage(), this.commits[i].getInfo().getAuthor().getDate(), this.commits[i].getInfo().getAuthor().getName());
				CL.add(c);
			}
		}
		this.list = CL;
	}
	
	public CommitList getList() {
		return this.list;
	}

	public API_Commit[] getCommits() {
		return commits;
	}

	public String getOutput() {
		return this.output;
	}

	public void save_output(HttpURLConnection con) throws IOException
	{
		StringBuilder result = new StringBuilder();
		try (var reader = new BufferedReader(
				new InputStreamReader(con.getInputStream()))) {
			for (String line; (line = reader.readLine()) != null; ) {
				result.append(line);
			}
			this.output = result.toString();
		}
	}
	
	public String get_missing(String repo_link)
	{
		int index = repo_link.indexOf("github.com", 0);
		if (repo_link.charAt(repo_link.length() - 1) == '/')
			repo_link = repo_link.substring(index + 11, repo_link.length() - 1);
		else
			repo_link = repo_link.substring(index + 11);
		return repo_link;
	}

}
