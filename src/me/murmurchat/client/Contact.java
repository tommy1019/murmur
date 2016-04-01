package me.murmurchat.client;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class Contact
{
	String displayName;
	Cipher contactCipher;
	byte[] contactPublicKey;

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
		return Util.encryptForCipher(contactCipher, bytes);
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
