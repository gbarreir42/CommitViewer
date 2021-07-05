package models;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;


public class CommitList implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<Commit> commit_list;
	private String link = "";


	public CommitList()
	{
		this.commit_list = new ArrayList<Commit>();
	}

	public void add(Commit c)
	{
		this.commit_list.add(c);
	}

	public List<Commit> get_commitlist()
	{
		return this.commit_list;
	}

	public String getLink()
	{
		String link2 = this.link;
		link2.replaceAll("_", "/");
		return link2;
	}

	public String getLinkFileName()
	{
		//https://github.com/gbarreirostester/test01 => turns into => gbarreirostester_test01
		String link2 = this.link;
		int index = link2.indexOf("github.com", 0);
		link2 = link2.substring(index + 11);
		return link2;
	}

	public void setLinkFileName(String link)
	{
		this.link = link;
	}

	public void process(JTextArea textArea, String limit, String sep, String pad)
	{
		textArea.append("\n\n" + "                " +"COMMIT LOG");
		textArea.append("\n" + limit);
		for (int i = 0; i < this.commit_list.size(); i++)
		{
			textArea.append("\n" + this.commit_list.get(i).toString());
			if (i != this.commit_list.size() - 1)
				textArea.append("\n" + sep + "\n");
		}
		textArea.append("\n" + limit + "\n" + pad);
	}

}

