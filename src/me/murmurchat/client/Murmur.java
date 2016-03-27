package me.murmurchat.client;

import javafx.application.Application;
import me.murmurchat.client.GUI.GUI;

public class Murmur
{
	public static AccountDatabase accountDatabase = new AccountDatabase();

	public static ServerHandler serverHandler;
	public static Crypt crypt;

	public static void main(String[] args)
	{
		accountDatabase.contacts.add(new Contact("Grant", null));
		accountDatabase.contacts.add(new Contact("Tommy", null));
		accountDatabase.contacts.add(new Contact("Bryce", null));
		accountDatabase.contacts.add(new Contact("Sai", null));
		
		serverHandler = new ServerHandler();

		Application.launch(GUI.class, (java.lang.String[]) null);

		serverHandler.disconnect();
	}
}
