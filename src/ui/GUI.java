package ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

import com.google.gson.Gson;

import models.CommitList;
import utils.API_request_handler;
import utils.JSON_utils;
import utils.exception_handling_utils;
import utils.git_utils;
import utils.parse_utils;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;
	final static String current_path = System.getProperty("user.dir");
	private int caret_line;
	private int caret_col;
	List<String> valid_commands = new ArrayList<String>();
	String link;
	Font default_font = new Font(Font.MONOSPACED, Font.BOLD, 17);
	String limit = "+----------------------------------------+\n";
	String sep = "     ================================     ";
	String pad = "user@input>";

	public void init() {
		List<Integer> arrows = new ArrayList<Integer>();
		JSON_utils json = new JSON_utils();
		build_arrow_list(arrows);
		GUI console = new GUI();
		console.pack();
		console.setVisible(true);
		console.setVisible(true);
		console.setSize(900, 600);
		console.setResizable(true);
		console.setDefaultCloseOperation(EXIT_ON_CLOSE);
		console.setLocationRelativeTo(null);
		JTextArea textArea = new JTextArea(24, 80);
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.WHITE);
		textArea.setFont(default_font);
		JScrollPane scroll = new JScrollPane(textArea);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setCaretColor(Color.WHITE);
		console.add(scroll);
		console.set_valid_commands();
		console.setVisible(true);
		console.setTitle("Ultimate Github Commit Viewer Console");
		Image icon = Toolkit.getDefaultToolkit().getImage("github_icon.png");
		console.setIconImage(icon);
		textArea.append("####WELCOME TO GITHUB COMMIT VIEWER!####\n");
		textArea.append(pad);
		textArea.addCaretListener(new CaretListener()
		{
			public void caretUpdate(CaretEvent e) {
				int line = 1;
				int col = 1;
				int caretpos = textArea.getCaretPosition();
				try {
					line = textArea.getLineOfOffset(caretpos);
					col = caretpos - textArea.getLineStartOffset(line);
					console.update_caret_pos(line + 1, col);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
		});
		textArea.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e)
			{
				int line = 1;
				int col = 1;
				int caretpos = textArea.getCaretPosition();
				try {
					line = textArea.getLineOfOffset(caretpos);
					col = caretpos - textArea.getLineStartOffset(line);
					console.update_caret_pos(line + 1, col);
					if (col < pad.length())
						textArea.setEditable(false);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
		});
		textArea.addKeyListener(new KeyAdapter()
		{
			String status= "";
			public void keyPressed(KeyEvent e)
			{
				//Allow navigation on the console
				//Doesn't allow editing lines that aren't the current last line
				if (!(arrows.contains(e.getKeyCode()))
						&& (console.get_caret_line() != textArea.getLineCount()))
				{
					textArea.setEditable(false);
					//Allows CTRL+C and ALT+F4
					if (!(check_allowed_combos(e)))
						e.consume();
				}
				if (console.get_caret_line() != textArea.getLineCount() || console.get_caret_col() <= pad.length())
				{
					if(e.getKeyCode() != KeyEvent.VK_KP_RIGHT && e.getKeyCode() != KeyEvent.VK_RIGHT)
						if (!(check_allowed_combos(e)))
							e.consume();
				}
				if (console.get_caret_line() == textArea.getLineCount() && console.get_caret_col() >= pad.length())
				{
					textArea.setEditable(true);	        		
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					String command = "";
					try {
						command = console.get_last_line(textArea).substring(pad.length()).toLowerCase();
						//Waits for one cycle of input and gets the line containing the link
						if (command.equals("cancel"))
						{
							status = "DONE";
							textArea.append("\nREPO INPUT WAIT HAS BEEN CANCELLED\n" + pad);
						}
						if ((!(console.valid_commands.contains(command)) && !(status.equals("WAITING_REPO"))) || (command.equals("")))
						{
							if (command.equals("") && status.equals("WAITING_REPO"))
								textArea.append("\nPLEASE INPUT A VALID GITHUB REPOSITORY LINK!\n" + pad);
							else
								textArea.append("\n\"" + command + "\"" + " IS NOT A RECOGNIZED COMMAND.\n" + pad);
						}
						if (status.equals("WAITING_REPO") && !command.equals(""))
						{
							//https://github.com/gbarreirostester/test01
							parse_utils parse = new parse_utils();
							console.set_link(console.get_last_line(textArea).substring(pad.length()));
							if (!(console.get_repo_link().contains("github.com")))
								textArea.append("\nPLEASE INPUT A VALID GITHUB REPOSITORY LINK!\n" + pad);
							else //Link is a valid Github link
							{
								String good_path = console.get_repo_link().replaceAll("/", "_");
								good_path = good_path.substring(good_path.indexOf("github.com", 0) + 11);
								File f = new File(current_path + "/logs/" + good_path + ".json");
								if (f.exists()) //Data has already been persisted
								{
									Gson gson = new Gson();
									String content = Files.readString(Paths.get(f.getPath()));
									CommitList emp = gson.fromJson(content, CommitList.class);
									emp.process(textArea, limit, sep, pad);
								}
								else //Data hasn't been persisted yet
								{
									CommitList CL;
									try {  //Tries to use Github API
										API_request_handler arh = new API_request_handler(console.get_repo_link());
										arh.connect();
										arh.convert();
										CL = arh.getList();
										CL.setLinkFileName(console.get_repo_link().replaceAll("/", "_"));
										json.writeJSONfromCL(CL);
									} catch (Exception e2) { // If API Fails, falls back to my solution
										String log = new git_utils().log_grabber((console.get_repo_link()));
										if (log.equals("")) //Link is a valid repository link but is empty or doesn't exist
										{
											textArea.append("\nGITHUB REPOSITORY IS EMPTY OR DOESN'T EXIST...\n" + pad);
											CL = null;
										}
										else //Link is a valid repository link and is not empty
										{
											CL = parse.getCommitListOBJ();
											CL.setLinkFileName(console.get_repo_link().replaceAll("/", "_"));
											parse.parse_factory(log);	
											json.writeJSON(parse);
										}
									}
									if (CL != null && CL.get_commitlist().size() != 0)
									{
										CL.process(textArea, limit, sep, pad);
									}
								}
								status = "DONE";
							}
						}						
						//Clears Console
						if (command.equals("clear"))
						{
							textArea.selectAll();
							textArea.replaceSelection(null);
							textArea.append("CONSOLE CLEARED!\n" + pad);
						}
						if (command.equals("exit"))
							console.dispatchEvent(new WindowEvent(console, WindowEvent.WINDOW_CLOSING));
						//Ping Pong = Funny & Used for testing purposes on demand
						if (command.equals(("ping")))
						{
							//API_request_handler abc = new API_request_handler("github.com/gbarreirostester/test01/");
							//throw new IndexOutOfBoundsException(); //Testing exception handling
							textArea.append("\nPong!\n" + pad);
						}
						//Github Repo Link Solicitation
						if (command.equals("repo"))
						{
							textArea.append("\nPLEASE INPUT GITHUB REPOSITORY LINK...\n" + pad);
							status = "WAITING_REPO";
						}
						if (command.equals("help"))
						{
							textArea.append("\n\nHERE ARE THE VALID COMMANDS:\n-CLEAR (CLEARS THE CONSOLE)\n-EXIT (CLOSES THE CONSOLE)\n-PING (PONG!)"
															+ "\n-REPO (STARTS GITHUB COMMIT VIEWER)\n-CANCEL (CANCELS THE WAIT FOR REPO INPUT)\nIF YOU NEED MORE HELP, PLEASE REFER TO THE README FILE.\n\n" + pad);
						}
					} catch (Exception e1) {
						textArea.append("\nTHE PROGRAM HAS ENCOUNTERED AN ERROR...\nPLEASE CHECK THE \"/logs/error_logs.txt\" FILE FOR MORE DETAILS!\n" + pad);
						File error_logs = new File(current_path + "/error_logs");
						if (!error_logs.exists())
							error_logs.mkdir();
						try {
							exception_handling_utils ehu = new exception_handling_utils();
							ehu.create_tracelog(e1);
						} catch (IOException e2) {
							textArea.append("\nSOMETHING WENT WRONG WHILE TRYING TO WRITE A TRACE_LOG FILE!\n" + pad);
							console.dispatchEvent(new WindowEvent(console, WindowEvent.WINDOW_CLOSING));
						}
					}
					e.consume();
				}	        		
			}
			public void keyTyped(KeyEvent e) {
			}
		});
	}

	private void set_link(String repo_link)
	{
		if (repo_link.charAt(repo_link.length() - 1) == '_' || repo_link.charAt(repo_link.length() - 1) == '/')
			repo_link = repo_link.substring(0, repo_link.length() - 1);
		this.link = repo_link;
	}

	public String get_repo_link()
	{
		return this.link;
	}

	private void update_caret_pos(int line_c, int col_c)
	{
		this.caret_line = line_c;
		this.caret_col = col_c;
	}

	private int get_caret_line()
	{
		return this.caret_line;
	}

	private int get_caret_col()
	{
		return this.caret_col;
	}

	private String get_last_line(JTextArea textArea) throws BadLocationException
	{
		Document document = textArea.getDocument();
		Element rootElem = document.getDefaultRootElement();
		int numLines = rootElem.getElementCount();
		Element lineElem = rootElem.getElement(numLines - 1);
		int lineStart = lineElem.getStartOffset();
		int lineEnd = lineElem.getEndOffset();
		String lineText = document.getText(lineStart, lineEnd - lineStart).trim();
		return lineText;
	}

	private List<Integer> build_arrow_list(List <Integer> arrows)
	{
		arrows.add(KeyEvent.VK_KP_DOWN);
		arrows.add(KeyEvent.VK_KP_LEFT);
		arrows.add(KeyEvent.VK_KP_RIGHT);
		arrows.add(KeyEvent.VK_KP_UP);
		arrows.add(KeyEvent.VK_UP);
		arrows.add(KeyEvent.VK_DOWN);
		arrows.add(KeyEvent.VK_LEFT);
		arrows.add(KeyEvent.VK_RIGHT);
		return arrows;
	}

	private boolean check_allowed_combos(KeyEvent e)
	{
		if ((e.getKeyCode() == KeyEvent.VK_C) && (e.isControlDown()))
			return true;
		if((e.getKeyCode() == KeyEvent.VK_V) && (e.isControlDown()))
			return true;
		if ((e.getKeyCode() == KeyEvent.VK_F4) && (e.isAltDown()))
			return true;
		return false;	
	}

	private void set_valid_commands()
	{
		this.valid_commands.add("help");
		this.valid_commands.add("clear");
		this.valid_commands.add("repo");
		this.valid_commands.add("cancel");
		this.valid_commands.add("exit");
		this.valid_commands.add("ping");
	}
}