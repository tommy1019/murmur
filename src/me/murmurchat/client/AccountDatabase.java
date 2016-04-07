package me.murmurchat.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

public class AccountDatabase
{
	public String displayName;
	public ArrayList<Contact> contacts;

	public AccountDatabase(String displayName)
	{
		this.displayName = displayName;
		contacts = new ArrayList<Contact>();
	}

	public AccountDatabase(DataInputStream in) throws IOException
	{
		contacts = new ArrayList<Contact>();

		int fileSize = in.readInt();

		if (fileSize == 0)
			return;

		byte[] file = new byte[fileSize];
		in.read(file);

		DataInputStream bIn = new DataInputStream(new ByteArrayInputStream(Murmur.profile.decrpyt(file)));

		int displayLength = bIn.readInt();
		byte[] displayNameBytes = new byte[displayLength];
		bIn.read(displayNameBytes);

		displayName = new String(displayNameBytes);

		System.out.println("Display name - " + displayName);

		int numContacts = bIn.readInt();

		for (int i = 0; i < numContacts; i++)
		{
			String contactName = new String(Util.readPrefixedBytes(bIn));
			System.out.println("Contact name - " + contactName);
			contacts.add(new Contact(contactName, Util.readPublicKey(bIn)));
		}

		bIn.close();
	}

	public void writeToFile(DataOutputStream out)
	{
		try
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream bOut = new DataOutputStream(stream);
			bOut.writeInt(displayName.getBytes().length);
			bOut.write(displayName.getBytes());

			bOut.writeInt(contacts.size());
			for (Contact c : contacts)
			{
				bOut.writeInt(c.displayName.getBytes().length);
				bOut.write(c.displayName.getBytes());
				System.out.println(new BigInteger(c.contactPublicKey).toString(36));
				bOut.write(c.contactPublicKey);
			}

			byte[] eArray = Murmur.profile.encrypt(stream.toByteArray());

			out.writeInt(eArray.length);
			out.write(eArray);

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void addContact(byte[] publicKey, String name)
	{
		contacts.add(new Contact(name, publicKey));

		Murmur.serverHandler.updateServerProfile();
	}

	public ArrayList<Contact> getContacts()
	{
		return contacts;
	}

}