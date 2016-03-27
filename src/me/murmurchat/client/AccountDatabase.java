package me.murmurchat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AccountDatabase
{
	String displayName;

	ArrayList<Contact> contacts;

	public AccountDatabase()
	{
		displayName = "";
		contacts = new ArrayList<Contact>();
	}

	public void writeToFile(Crypt crypt)
	{
		try
		{
			FileOutputStream fos = new FileOutputStream("keys");
			DataOutputStream dos = new DataOutputStream(fos);
			dos.write(displayName.getBytes());
			dos.write(59);
			for (int i = 0; i < contacts.size(); i++)
			{
				dos.write(contacts.get(i).displayName.getBytes());
				dos.write(59);
				dos.write(contacts.get(i).publicKey);
			}
			dos.close();
		}
		catch (IOException e)
		{
			System.out.println("IOException");
		}
	}

	public void readFromFile(Crypt crypt)
	{
		try
		{
			@SuppressWarnings("resource")
			DataInputStream dis = new DataInputStream(new FileInputStream("keys"));

			int curByte = -1;

			ArrayList<Byte> nameBytes = new ArrayList<Byte>();
			while ((curByte = dis.read()) != 59)
			{
				if (curByte == -1)
					throw new IOException("End of stream");
				nameBytes.add((byte) curByte);
			}
			displayName = new String(Util.toByteArray(nameBytes));

			readFileLoop:
			while (true)
			{
				ArrayList<Byte> contactName = new ArrayList<Byte>();
				
				curByte = -1;
				while ((curByte = dis.read()) != 59)
				{
					if (curByte == -1)
						break readFileLoop;
					nameBytes.add((byte) curByte);
				}
				
				byte[] keyData = new byte[294];
				dis.read(keyData);
				
				contacts.add(new Contact(new String(Util.toByteArray(contactName)), keyData));
			}
			
			dis.close();
		}
		catch (IOException e)
		{
			System.out.println("IOException - Error reading user database.");
			System.exit(1);
		}

	}

	public ArrayList<Contact> getContacts()
	{
		return contacts;
	}

}