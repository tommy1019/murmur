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
	
	public void writeToFile(Crypt crypt)
	{			
		try {
			FileOutputStream fos = new FileOutputStream("keys");
			DataOutputStream dos = new DataOutputStream(fos);
			dos.write(displayName.getBytes());
			dos.write(59);
			for(int i = 0; i < contacts.size(); i++)
			{
				dos.write(contacts.get(i).displayName.getBytes());
				dos.write(59);
				dos.write(contacts.get(i).publicKey);
			}
			dos.close();
		} catch (IOException e) {
			System.out.println("IOException");
		}
	}
	public void readFromFile(Crypt crypt)
	{			
		try
		{
			FileInputStream fis = new FileInputStream("keys");
			DataInputStream dis = new DataInputStream(fis);
			byte[] nameData = new byte[64];
			byte[] keyData = new byte[294];
			
			for(int i = 0; nameData[i] != 59; i++)
			{
				nameData[i] = ((byte) dis.read());
			}
			displayName = new String(nameData);
			
			for(int k = 0; nameData[k] != -1; k++)
			{
				for(int i = 0; nameData[i] != 59; i++)
				{
					nameData[i] = ((byte) dis.read());
				}
				for(int i = 0; i < 294; i++)
				{
					keyData[i] = ((byte) dis.read());
				}
				contacts.add(new Contact(new String(nameData), keyData));
			}
		}
		catch (IOException e)
		{
			System.out.println("IOException");
		}
		
	}
	
	public ArrayList<Contact> getContacts()
	{
		return contacts;
	}
	
}