package models;

public class Author {
	String name;
	String date;
	
	public Author(String name, String date)
	{
		this.name = name;
		this.date = date;
	}
//good = 19-06-2021 23:44:00
//not good = 2021-06-19T22:44:00Z
	
	private String change_format(String date)
	{
		String result;
		String day = date.substring(8, 10);
		String month = date.substring(5, 7);
		String year = date.substring(0, 4);
		int hour = Integer.parseInt(date.substring(11, 13)) + 1; //To account for Portuguese time zone
		String minute = date.substring(14, 16);
		String second = date.substring(17, 19);
		if (hour >= 24)
			hour -= 24;
		result = day + "-" + month + "-" + year + " " + String.valueOf(hour) + ":" + minute + ":" + second;
		return result;
	}
	
	public String getName() {
		return this.name;
	}

	public String getDate() {
		return change_format(this.date);
	}

}