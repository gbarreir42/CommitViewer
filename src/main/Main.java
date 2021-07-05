package main;
import java.io.File;
import java.io.IOException;

import ui.GUI;

public class Main {
	final static String current_path = System.getProperty("user.dir");
	//repo = https://github.com/gbarreirostester/test01
	
	private static void create_logs() throws IOException
	{
		File logs = new File(current_path + "/logs");
		if (!logs.exists()){
			logs.mkdirs();
		}
	}
	
	public static void main(String[] args) throws IOException {
		create_logs();
		GUI gui = new GUI();
		gui.init();
	}
}
