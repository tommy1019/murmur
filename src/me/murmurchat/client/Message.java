package me.murmurchat.client;

public class Message 
{
	String text;
	
	public Message()
	{
		this("");
	}
	
	public Message(String t)
	{
		text = t;
	}
	
	public byte[] getBytes()
	{
		return text.getBytes();
	}
	
	public String getText()
	{
		return text;
	}
}
