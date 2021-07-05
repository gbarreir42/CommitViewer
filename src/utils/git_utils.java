package utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class git_utils {

	//Returns output of variable "command" typed in CMD
	public static String git_cmd(String command) throws IOException
	{
		BufferedReader in = null;
		String result = "";
		try
		{
			Process p = Runtime.getRuntime().exec(command);
			InputStream s = p.getInputStream();
			in = new BufferedReader(new InputStreamReader(s));
			String temp;

			while ((temp = in.readLine()) != null)
			{
				result += (temp + '\n');
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (in != null)
			{
				in.close();
			}    
		}
		return result;
	}

	//Recursively deletes all files in a named directory
	public static boolean deleteDirectory(File directoryToBeDeleted) throws IOException {
		final String current_path = System.getProperty("user.dir");
		File current_directory = new File(current_path);
		boolean areRelated = directoryToBeDeleted.getCanonicalPath().contains(current_directory.getCanonicalPath());
		//Check if directory is valid so I dont delete all my files accidentally
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null && areRelated) {
			for (File file : allContents) {
				deleteDirectory(file);
			}
		}
		return directoryToBeDeleted.delete();
	}

	public String get_filename(String repo)
	{
		if (!(repo.contains("github.com/")))
		{
			System.out.println("Invalid Repository"); 
		}
		String filename = repo.substring(repo.lastIndexOf('/') + 1);
		return filename;
	}

	//Clones repository, copies "git log" output to String variable and deletes cloned repository
	public String log_grabber(String repo) throws IOException
	{
		//filename could be "example" or "example.git"
		String filename = get_filename(repo);
		String log = git_cmd("cmd.exe /c git clone " + repo + " " + filename + " && git -C " + filename + " log");
		deleteDirectory(new File(filename));
		return log;
	}
}
