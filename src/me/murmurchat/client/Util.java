package me.murmurchat.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class Util
{
	public static byte[] encryptForCipher(Cipher cipher, byte[] bytes)
	{
		try
		{
			if (bytes.length < 245)
				return cipher.doFinal(bytes);
			
			ArrayList<Byte> res = new ArrayList<Byte>();
			
			int i;
			for (i = 0; i < bytes.length / 245; i++)
			{
				byte[] curArray = new byte[245];
				for (int j = 245 * i; j < 245 * (i + 1); j++)
					curArray[j % 245] = bytes[j];
				
				curArray = cipher.doFinal(curArray);
				for (byte b : curArray)
					res.add(b);
			}
			
			byte[] finalArray = new byte[bytes.length % 245];
			for (int j = 245 * (i + 1); j < bytes.length; j++)
				finalArray[j % 245] = bytes[j];
			
			finalArray = cipher.doFinal(finalArray);
			for (byte b : finalArray)
				res.add(b);
			
			return toByteArray(res);
		}
		catch (IllegalBlockSizeException e)
		{
			e.printStackTrace();
		}
		catch (BadPaddingException e)
		{
			e.printStackTrace();
		}
		
		return new byte[0];
	}
	
	public static byte[] toByteArray(ArrayList<Byte> list)
	{
		byte[] data = new byte[list.size()];
		for (int i = 0; i < list.size(); i++)
			data[i] = list.get(i);
		return data;
	}

	public static byte[] readPublicKey(DataInputStream in)
	{
		byte[] key = new byte[294];
		try
		{
			in.read(key);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return key;
	}

	public static byte[] readPrefixedBytes(DataInputStream in)
	{
		try
		{
			int numBytes;
			numBytes = in.readInt();

			byte[] bytes = new byte[numBytes];
			in.read(bytes);

			return bytes;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return new byte[0];
	}
}