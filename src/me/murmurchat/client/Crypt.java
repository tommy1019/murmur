package me.murmurchat.client;
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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

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
			createNewProfile(filePath);
			keyPair = getKeys();
			cEncrypt = Cipher.getInstance("RSA");
			cDecrypt = Cipher.getInstance("RSA");
			cEncrypt.init(Cipher.ENCRYPT_MODE, keyPair.getPublic()); 
			cDecrypt.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());	
		}
		catch(InvalidKeyException e)
		{
			System.out.println("InvalidKeyException");
		}
		catch(NoSuchAlgorithmException e)
		{
			System.out.println("NoSuchAlgorithmException");
		}
		catch(NoSuchPaddingException e)
		{
			System.out.println("NoSuchPaddingException");
		}
		catch(IOException e)
		{
			System.out.println("IOException");
		}
	}
	
	public byte[] decrpyt(byte[] byteArray)
	{
		try
		{
			return cDecrypt.doFinal(byteArray);
		}
		catch (IllegalBlockSizeException e)
		{
			System.out.println("IllegalBlockSizeException");
		}
		catch (BadPaddingException e)
		{
			System.out.println("BadPaddingException");
		}
		return null;
	}
	
	public byte[] encrypt(byte[] byteArray)
	{	
		try
		{
			return cEncrypt.doFinal(byteArray);
		}
		catch (IllegalBlockSizeException e)
		{
			System.out.println("IllegalBlockSizeException");
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
			Path path = Paths.get(filePath);
			byte[] data = Files.readAllBytes(path);
			byte[] privateKeyArray = new byte[data.length-PUBLIC_KEY_SIZE];
			byte[] publicKeyArray = new byte[PUBLIC_KEY_SIZE];

			for (int i = 0; i < data.length; ++i)
			{
			    if(i < PUBLIC_KEY_SIZE)
			    {
			    	publicKeyArray[i] = data[i];
			    }
			    else
			    {
			    	privateKeyArray[i-PUBLIC_KEY_SIZE] = data[i];
			    }
			}
			
			
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyArray));
			PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyArray));
			
			return new KeyPair(publicKey, privateKey);
		}
		catch(IOException e)
		{
			System.out.println("IOException");
		}
		catch(InvalidKeySpecException e)
		{
			System.out.println("InvalidKeySpecException");
		}
		catch(NoSuchAlgorithmException e)
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
			byte[] combined = new byte[privateKey.length + publicKey.length];

			if(publicKey.length != PUBLIC_KEY_SIZE)
				System.out.println(privateKey.length+"!Unexpected Key Size!"+publicKey.length);

			for (int i = 0; i < combined.length; ++i)
			{
			    if(i < publicKey.length)
			    {
			    	combined[i] = publicKey[i];
			    }
			    else
			    {
			    	combined[i] = privateKey[i-publicKey.length];
			    }
			}
			
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(combined);
			fos.close();
		}
		catch(IOException e)
		{
			System.out.println("IOException");
		}
		catch(NoSuchAlgorithmException e)
		{
			System.out.println("NoSuchAlgorithmException");
		}
	}
	
	public void setFilePath(String path)
	{
		filePath = path;
	}
}
