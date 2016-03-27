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

public class Crypt
{
	public static final int PUBLIC_KEY_SIZE = 294;

	String filePath;

	KeyPair keyPair;
	Cipher cEncrypt;
	Cipher cDecrypt;

	public Crypt(String path)
	{
		filePath = path;

		try
		{
			keyPair = getKeys();
			cEncrypt = Cipher.getInstance("RSA");
			cDecrypt = Cipher.getInstance("RSA");
			cEncrypt.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
			cDecrypt.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
		}
		catch (InvalidKeyException e)
		{
			System.out.println("InvalidKeyException");
		}
		catch (NoSuchAlgorithmException e)
		{
			System.out.println("NoSuchAlgorithmException");
		}
		catch (NoSuchPaddingException e)
		{
			System.out.println("NoSuchPaddingException");
		}
	}

	public byte[] decrpyt(byte[] byteArray)
	{
		try
		{
			ArrayList<Byte> res = new ArrayList<Byte>();
			System.out.println(byteArray.length / 256);
			for (int i = 0; i < byteArray.length / 256; i++)
			{
				byte[] curArray = new byte[256];
				for (int j = 256 * i; j < 256 * (i + 1); j++)
					curArray[j % 256] = byteArray[j];
				
				curArray = cDecrypt.doFinal(curArray);
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
		try
		{
			if (byteArray.length < 245)
				return cEncrypt.doFinal(byteArray);
			else
			{
				ArrayList<Byte> res = new ArrayList<Byte>();
				for (int i = 0; i < byteArray.length / 245 + 1; i++)
				{
					byte[] curArray = new byte[245];
					for (int j = 245 * i; j < Math.min(245 * (i + 1), byteArray.length); j++)
						curArray[j % 245] = byteArray[j];
					
					curArray = cEncrypt.doFinal(curArray);
					for (byte b : curArray)
						res.add(b);
				}
				
				return Util.toByteArray(res);
			}
		}
		catch (IllegalBlockSizeException e)
		{
			e.printStackTrace();
		}
		catch (BadPaddingException e)
		{
			System.out.println("BadPaddingException");
		}
		return null;
	}

	private KeyPair getKeys()
	{
		try
		{
			DataInputStream in = new DataInputStream(new FileInputStream(filePath));

			byte[] publicKeyBytes = new byte[PUBLIC_KEY_SIZE];
			in.read(publicKeyBytes);

			ArrayList<Byte> privateKeyBytes = new ArrayList<Byte>();
			int curByte = -1;
			while ((curByte = in.read()) != -1)
				privateKeyBytes.add((byte) curByte);

			in.close();

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Util.toByteArray(privateKeyBytes)));
			PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

			System.out.println("Public key is: ");
			System.out.println(new BigInteger(publicKey.getEncoded()).toString(36));
			
			return new KeyPair(publicKey, privateKey);
		}
		catch (IOException e)
		{
			System.out.println("IOException");
		}
		catch (InvalidKeySpecException e)
		{
			System.out.println("InvalidKeySpecException");
		}
		catch (NoSuchAlgorithmException e)
		{
			System.out.println("NoSuchAlgorithmException");
		}
		return null;
	}

	public static void createNewProfile(String path) throws IOException, NoSuchAlgorithmException
	{
		try
		{
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KeyPair newPair = kpg.generateKeyPair();
			byte[] privateKey = newPair.getPrivate().getEncoded();
			byte[] publicKey = newPair.getPublic().getEncoded();

			FileOutputStream fos = new FileOutputStream(path);
			fos.write(publicKey);
			fos.write(privateKey);
			fos.close();
		}
		catch (IOException e)
		{
			System.out.println("IOException");
		}
		catch (NoSuchAlgorithmException e)
		{
			System.out.println("NoSuchAlgorithmException");
		}
	}

	public void setFilePath(String path)
	{
		filePath = path;
	}
}
