package me.murmurchat.client;

import java.security.NoSuchAlgorithmException;

import javafx.application.Application;
import me.murmurchat.client.GUI.GUI;

public class Murmur
{
	public static AccountDatabase accountDatabase = new AccountDatabase();

	public static ServerHandler serverHandler;
	public static Crypt crypt;

	public static void main(String[] args) throws NoSuchAlgorithmException
	{
		serverHandler = new ServerHandler();

		Application.launch(GUI.class, (java.lang.String[]) null);

		serverHandler.disconnect();
	}
}
