package me.murmurchat.client;

import javafx.application.Application;
import me.murmurchat.client.GUI.GUI;
import me.murmurchat.client.GUI.MainWindow;

public class Murmur
{
	public static Profile profile;
	public static AccountDatabase accountDatabase;

	public static ServerHandler serverHandler;

	public static MainWindow mainWindowController;
	
	public static void main(String[] args)
	{
		serverHandler = new ServerHandler();

		Application.launch(GUI.class, (java.lang.String[]) null);

		serverHandler.disconnect();
	}
	
	public static void fatalError(Exception e)
	{
		System.err.println("Encountered a fatal error, program stopping.");
		e.printStackTrace();
		System.exit(1);
	}
}
