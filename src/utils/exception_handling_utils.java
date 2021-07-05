package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class exception_handling_utils {
	final static String current_path = System.getProperty("user.dir");

	public static String getStackTrace(final Throwable throwable) {
	     final StringWriter sw = new StringWriter();
	     final PrintWriter pw = new PrintWriter(sw, true);
	     throwable.printStackTrace(pw);
	     return sw.getBuffer().toString();
	}
	
	public void create_tracelog(Exception e) throws IOException
	{
		String trace = getStackTrace(e);
	    File trace_log = new File(current_path + "/error_logs/" + get_date() + ".txt");
	    trace_log.createNewFile();
		Writer fw;
		fw = new FileWriter(trace_log.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
       bw.write(trace);
       bw.close();
	}
	
	public String get_date()
	{
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	    Date date = new Date();							
	    String good_date = formatter.format(date).replace('/', '-');
	    good_date = good_date.replaceFirst(":", "h");
	    good_date = good_date.replaceFirst(":", "m");
	    good_date = good_date.replaceFirst(":", "s");
	    return good_date;
	}
	
}
