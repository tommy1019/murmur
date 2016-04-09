package me.murmurchat.client;

import java.util.ArrayList;

public class Conversation
{
	ArrayList<Contact> contacts;
	ArrayList<Message> messages;

	public Conversation()
	{
		contacts = new ArrayList<Contact>();
		messages = new ArrayList<Message>();
	}
}
