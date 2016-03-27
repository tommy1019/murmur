package me.murmurchat.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AccountDatabase
{
	public String displayName;
	public ArrayList<Contact> contacts;

	public AccountDatabase()
	{
		displayName = "Tommy";
		contacts = new ArrayList<Contact>();
	}

	public void writeToFile(Crypt crypt, DataOutputStream out)
	{
		try
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bos.write(displayName.getBytes());
			bos.write(59);
			for (int i = 0; i < contacts.size(); i++)
			{
				bos.write(contacts.get(i).displayName.getBytes());
				bos.write(59);
				bos.write(contacts.get(i).publicKey);
			}

			byte[] eArray = crypt.encrypt(bos.toByteArray());

			out.writeInt(eArray.length);
			out.write(eArray);

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void readFromFile(Crypt crypt, DataInputStream in)
	{
		try
		{
			int fileSize = in.readInt();

			if (fileSize == 0)
				return;

			byte[] file = new byte[fileSize];
			in.read(file);

			ByteArrayInputStream bis = new ByteArrayInputStream(crypt.decrpyt(file));

			int curByte = -1;

			ArrayList<Byte> nameBytes = new ArrayList<Byte>();
			while ((curByte = bis.read()) != 59)
			{
				if (curByte == -1)
					throw new IOException("End of stream");
				nameBytes.add((byte) curByte);
			}
			displayName = new String(Util.toByteArray(nameBytes));

			System.out.println("Display name - " + displayName);

			contacts.clear();

			readFileLoop: while (true)
			{
				ArrayList<Byte> contactName = new ArrayList<Byte>();

				curByte = -1;
				while ((curByte = bis.read()) != 59)
				{
					if (curByte == -1)
						break readFileLoop;
					contactName.add((byte) curByte);
				}

				byte[] keyData = new byte[294];
				bis.read(keyData);

				System.out.println("Concact name - " + new String(Util.toByteArray(contactName)));

				contacts.add(new Contact(new String(Util.toByteArray(contactName)), keyData));

			}

			bis.close();
		}
		catch (IOException e)
		{
			System.out.println("IOException - Error reading user database.");
			System.exit(1);
		}

	}

	public void addContact(byte[] publicKey, String name)
	{
		contacts.add(new Contact(name, publicKey));
		//Murmur.serverHandler.updateClientList();

		try
		{
			System.out.println("Requesting data");
			Murmur.serverHandler.out.write(3);
			Murmur.serverHandler.out.write(publicKey);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public ArrayList<Contact> getContacts()
	{
		return contacts;
	}

}