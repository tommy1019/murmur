package me.murmurchat.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

public class AccountDatabase
{
	String displayName;

	ArrayList<Contact> contacts;

	public AccountDatabase()
	{
		displayName = "";
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
			out.write(bos.size());
			out.write(crypt.encrypt(bos.toByteArray()));
			
		}
		catch (IOException e)
		{
			System.out.println("IOException");
		}
	}

	public void readFromFile(Crypt crypt, DataInputStream in)
	{
		try
		{
			int fileSize = in.readInt();
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

			readFileLoop:
			while (true)
			{
				ArrayList<Byte> contactName = new ArrayList<Byte>();
				
				curByte = -1;
				while ((curByte = bis.read()) != 59)
				{
					if (curByte == -1)
						break readFileLoop;
					nameBytes.add((byte) curByte);
				}
				
				byte[] keyData = new byte[294];
				bis.read(keyData);
				
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

	public void addContact(byte[] publicKey){
		contacts.add(new Contact("Grant",publicKey));
	}
	
	public ArrayList<Contact> getContacts()
	{
		return contacts;
	}

}