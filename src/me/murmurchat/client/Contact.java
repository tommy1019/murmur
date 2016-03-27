package me.murmurchat.client;

public class Contact
{
	String displayName;
	byte[] publicKey;
	String chatHistory = "";
	
	public Contact (String newdisplayName, byte[] newpublicKey)
	{
		displayName = newdisplayName;
		publicKey = newpublicKey;
	}
	
	public String toString()
	{
		return displayName;
	}
	
	public String getChatHistory()
	{
		return chatHistory;
	}
	
	public void setChatHistory(String s)
	{
		chatHistory = s;
	}
	
	public String getDisplayName()
	{
		return displayName;
	}
}
