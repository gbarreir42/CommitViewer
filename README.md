# CommitViewer [JAVA]
Exercise I did as part of the interview process of a company.

# How to run CommitViewer
Simply clone this project and run the CommitViewer.jar file!

# Existing files
* CommitViewer.jar  =>  (This is your runnable file, simply double click it)
* /logs/  =>  (Folder where all your repo command outputs will end up, the name of the file will be "{username}_{repository_name}")
* /error_logs/  =>  (Folder with logs of any error that occurs, the name of the file will be the date and time of the error occurence)

# Instructions
Once you run the .jar file a window that looks like a Console should pop up, after that you can start inputting VALID commands.

# Valid Commands
You have 5 available commands! Here's how each one of them behaves.

Command Input | Behaviour
------------ | -------------
Help | Writes all the available commands and a small explanation for what each of them does!
Clear | Clears the console!
Exit | Closes the console.
Ping | Writes back "Pong!"
Repo | After inputing this command, the console will be waiting for a repository link on the next input.
Cancel | Cancels the wait for a repository link, in case you regret inputting the "repo" command...

# Example on how to run a Repo Command 


<img width="667" alt="CommitViewerGuide" src="https://user-images.githubusercontent.com/79148194/124416460-f3a81a80-dd4e-11eb-99c6-eb35eb34ae2e.png">

* 🟢  =>  "repo" Command input.
* 🔵  =>  Copy&Paste or Type in your github repository link.
* 🟡  =>  Output.

# Error Handling
If you ever run into an error, you will be able to read this on the Console.

<img width="502" alt="ERROR" src="https://user-images.githubusercontent.com/79148194/124417020-2e5e8280-dd50-11eb-8e77-7f641ac77394.png">

For more information on the error type and the reason behind it, please refer to the corresponding error_log file inside the folder.

