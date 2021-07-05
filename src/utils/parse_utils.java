package utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import models.Commit;
import models.CommitList;

public class parse_utils {
	public String[] months = { "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};  
	CommitList list = new CommitList();

	public CommitList getCommitListOBJ() {
		return list;
	}

	public String find_month(String month)
	{
		int m = 0;
		String result = "";
		for (int i = 0; i < 12; i++)
		{
			if (this.months[i].startsWith(month.toLowerCase()))
				m = i + 1;
		}
		if (m < 10)
			result = "0" + String.valueOf(m);
		else
			result = String.valueOf(m);
		return result;
	}

	public String date_format(String date)
	{
		//before => Thu Jun 17 20:07:51 2021 +0100
		String result = "";
		String [] aux = date.split(" ");
		String new_date = aux[2] + "-" + find_month(aux[1]) + "-" + aux[4];
		result = new_date + " " + aux[3];
		return result;
	}

	public void parse_factory(String log)
	{
		try (BufferedReader reader = new BufferedReader(new StringReader(log))) {
			String line = reader.readLine();
			while (line != null) {
				String message = "";
				if (line.startsWith("commit"))
				{
					String sha = line.split(" ")[1];
					line = reader.readLine();
					String author = line.substring(line.indexOf(':') + 1, line.indexOf('<')).trim();
					String date = date_format(reader.readLine().substring(6).trim());
					line = reader.readLine();
					while ((line = reader.readLine()) != null)
					{
						if (line.startsWith("commit "))
							break;
						else
							message += line.trim() + '\n';
					}
					message = message.trim();
					Commit c = new Commit(sha, message, date, author);
					list.add(c);
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
