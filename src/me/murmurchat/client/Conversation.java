package me.murmurchat.client;

import java.util.ArrayList;
import java.util.Random;

public class Conversation
{	
	static Random random = new Random();
	
	long conversationId;
	
	public ArrayList<Contact> contacts;
	public ArrayList<Message> messages;

	public Conversation()
	{
		contacts = new ArrayList<Contact>();
		messages = new ArrayList<Message>();
	}
	
	public void generateNewConversation()
	{
		conversationId = random.nextLong();
	}
}
