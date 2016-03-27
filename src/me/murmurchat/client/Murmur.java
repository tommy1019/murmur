package me.murmurchat.client;

import javafx.application.Application;
import me.murmurchat.client.GUI.GUI;

public class Murmur
{
	public static void main(String[] args)
	{
		//Crypt crypt = new Crypt();
		//ServerHandler serverHandler = new ServerHandler(crypt);
		
		Application.launch(GUI.class, (java.lang.String[]) null);
	}
}
