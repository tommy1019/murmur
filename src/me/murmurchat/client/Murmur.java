package me.murmurchat.client;

import javafx.application.Application;
import me.murmurchat.client.GUI.GUI;
import me.murmurchat.client.GUI.MainWindowController;

public class Murmur
{
	public static AccountDatabase accountDatabase = new AccountDatabase();

	public static ServerHandler serverHandler;
	public static Crypt crypt;

	public static MainWindowController mainWindowController;
	
	public static void main(String[] args)
	{
		serverHandler = new ServerHandler();

		Application.launch(GUI.class, (java.lang.String[]) null);

		serverHandler.disconnect();
	}
}
