package me.murmurchat.client;

public class Contact
{
	String displayName;
	byte[] publicKey;
	
	Contact (String newdisplayName, byte[] newpublicKey)
	{
		displayName = newdisplayName;
		publicKey = newpublicKey;
	}
	
	public String toString()
	{
		return displayName;
	}
}
