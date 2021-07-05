package utils;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.CommitList;

public class JSON_utils {
	final static String log_path = System.getProperty("user.dir");
	
	public void writeJSON(parse_utils parse) throws IOException
	{
		Writer writer = new FileWriter("logs/" + parse.getCommitListOBJ().getLinkFileName() + ".json");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    gson.toJson(parse.getCommitListOBJ(), writer);
	    writer.close();
	}
	
	public void writeJSONfromCL(CommitList CL) throws IOException
	{
		Writer writer = new FileWriter("logs/" + CL.getLinkFileName() + ".json");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    gson.toJson(CL, writer);
	    writer.close();
	}
	
}
