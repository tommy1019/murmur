package me.murmurchat.client;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.sun.media.sound.InvalidFormatException;

public class Profile
{
	public static final int RSA_ENCRYPTION_BITS = 2048;
	public static final int PUBLIC_KEY_SIZE = 294;

	PublicKey publicKey;
	Cipher profileCipherEncrypt;
	Cipher profileCipherDecrypt;

	public Profile(String profilePath) throws IOException
	{
		DataInputStream in = new DataInputStream(new FileInputStream(profilePath));

		byte[] publicKeyBytes = new byte[PUBLIC_KEY_SIZE];
		in.read(publicKeyBytes);

		ArrayList<Byte> privateKeyBytes = new ArrayList<Byte>();
		int curByte = -1;
		while ((curByte = in.read()) != -1)
			privateKeyBytes.add((byte) curByte);

		in.close();

		try
		{
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Util.toByteArray(privateKeyBytes)));
			publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
			
			profileCipherEncrypt = Cipher.getInstance("RSA");
			profileCipherEncrypt.init(Cipher.ENCRYPT_MODE, publicKey);
			
			profileCipherDecrypt = Cipher.getInstance("RSA");
			profileCipherDecrypt.init(Cipher.DECRYPT_MODE, privateKey);
			
			
		}
		catch (NoSuchAlgorithmException | NoSuchPaddingException e)
		{
			Murmur.fatalError(e);
		}
		catch (InvalidKeySpecException | InvalidKeyException e)
		{
			throw new InvalidFormatException(".profile file has invalid format.");
		}
	}

	public byte[] decrpyt(byte[] byteArray)
	{
		try
		{
			ArrayList<Byte> res = new ArrayList<Byte>();
			for (int i = 0; i < byteArray.length / 256; i++)
			{
				byte[] curArray = new byte[256];
				for (int j = 256 * i; j < 256 * (i + 1); j++)
					curArray[j % 256] = byteArray[j];

				curArray = profileCipherDecrypt.doFinal(curArray);
				for (byte b : curArray)
					res.add(b);
			}

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
		return null;
	}
	
	public byte[] encrypt(byte[] byteArray)
	{
		return Util.encryptForCipher(profileCipherEncrypt, byteArray);
	}
	
	public String getPublicKey()
	{
		return new BigInteger(publicKey.getEncoded()).toString(36);
	}

	public static void createNewProfile(String path) throws IOException
	{
		try
		{
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(RSA_ENCRYPTION_BITS);
			KeyPair newPair = kpg.generateKeyPair();
			byte[] privateKey = newPair.getPrivate().getEncoded();
			byte[] publicKey = newPair.getPublic().getEncoded();

			FileOutputStream fos = new FileOutputStream(path);
			fos.write(publicKey);
			fos.write(privateKey);
			fos.close();
		}
		catch (NoSuchAlgorithmException e)
		{
			Murmur.fatalError(e);
		}
	}
}
