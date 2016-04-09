package me.murmurchat.client;

import java.util.ArrayList;
import java.util.Random;

public class Conversation
{	
	static Random random = new Random();
	
	long conversationId;
	
	ArrayList<Contact> contacts;
	ArrayList<Message> messages;

	public Conversation()
	{
		contacts = new ArrayList<Contact>();
		messages = new ArrayList<Message>();
	}
	
	void generateNewConversation()
	{
		conversationId = random.nextLong();
	}
}
