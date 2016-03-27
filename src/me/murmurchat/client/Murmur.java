package me.murmurchat.client;

import javafx.application.Application;
import me.murmurchat.client.GUI.GUI;

public class Murmur
{
	public static AccountDatabase accountDatabase = new AccountDatabase();
	
	public static void main(String[] args)
	{
		accountDatabase.contacts.add(new Contact("Grant", null));
		accountDatabase.contacts.add(new Contact("Tommy", null));
		accountDatabase.contacts.add(new Contact("Bryce", null));
		accountDatabase.contacts.add(new Contact("Sai", null));
		
		Crypt crypt = new Crypt();
		ServerHandler serverHandler = new ServerHandler(crypt);

		Application.launch(GUI.class, (java.lang.String[]) null);

		serverHandler.disconnect();
	}
}
