package me.murmurchat.client;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Contact
{
	String displayName;
	Cipher contactCipher;

	public Contact(String displayName, byte[] publicKey)
	{
		this.displayName = displayName;

		try
		{
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey key = keyFactory.generatePublic(new X509EncodedKeySpec(publicKey));

			contactCipher = Cipher.getInstance("RSA");
			contactCipher.init(Cipher.ENCRYPT_MODE, key);
		}
		catch (NoSuchAlgorithmException | NoSuchPaddingException e)
		{
			Murmur.fatalError(e);
		}
		catch (InvalidKeySpecException | InvalidKeyException e)
		{
			System.out.println("Encountered invalid key for concact with display name " + displayName);
			Murmur.fatalError(e);
		}
	}

	public byte[] encryptForConcact(byte[] bytes)
	{
		try
		{
			if (bytes.length < 245)
				return contactCipher.doFinal(bytes);
			
			ArrayList<Byte> res = new ArrayList<Byte>();
			
			int i;
			for (i = 0; i < bytes.length / 245; i++)
			{
				byte[] curArray = new byte[245];
				for (int j = 245 * i; j < 245 * (i + 1); j++)
					curArray[j % 245] = bytes[j];
				
				curArray = contactCipher.doFinal(curArray);
				for (byte b : curArray)
					res.add(b);
			}
			
			byte[] finalArray = new byte[bytes.length % 245];
			for (int j = 245 * (i + 1); j < bytes.length; j++)
				finalArray[j % 245] = bytes[j];
			
			finalArray = contactCipher.doFinal(finalArray);
			for (byte b : finalArray)
				res.add(b);
			
			return Util.toByteArray(res);
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

	public String toString()
	{
		return displayName;
	}

	public String getDisplayName()
	{
		return displayName;
	}
}
