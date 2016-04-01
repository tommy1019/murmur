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

	public AccountDatabase(String displayName)
	{
		this.displayName = displayName;
	}
	
	public AccountDatabase(DataInputStream in) throws IOException
	{
		contacts = new ArrayList<Contact>();

		int fileSize = in.readInt();

		if (fileSize == 0)
			return;

		byte[] file = new byte[fileSize];
		in.read(file);

		ByteArrayInputStream bis = new ByteArrayInputStream(Murmur.profile.decrpyt(file));

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

	public void writeToFile(DataOutputStream out)
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
				bos.write(contacts.get(i).contactPublicKey);
			}

			byte[] eArray = Murmur.profile.encrypt(bos.toByteArray());

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
	}

	public ArrayList<Contact> getContacts()
	{
		return contacts;
	}

}