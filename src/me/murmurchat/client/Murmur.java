package me.murmurchat.client;

import java.util.prefs.Preferences;

import javafx.application.Application;
import me.murmurchat.client.GUI.GUI;
import me.murmurchat.client.GUI.MainWindow;

public class Murmur
{
	public static Profile profile;
	public static AccountDatabase accountDatabase;

	public static Preferences preferences;
	
	public static ServerHandler serverHandler;

	public static MainWindow mainWindowController;
	
	public static void main(String[] args)
	{
		preferences = Preferences.userNodeForPackage(Murmur.class);
		serverHandler = new ServerHandler();

		Application.launch(GUI.class);

		serverHandler.disconnect();
	}
	
	public static void fatalError(Exception e)
	{
		System.err.println("Encountered a fatal error, program stopping.");
		e.printStackTrace();
		System.exit(1);
	}
}
