package me.murmurchat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerHandler extends Thread
{
	public static final String IP = "0.0.0.0";
	public static final int PORT = 21212;

	Socket socket;

	DataInputStream in;
	DataOutputStream out;

	Crypt crypt;

	public ServerHandler(Crypt crypt)
	{
		this.crypt = crypt;

		try
		{
			socket = new Socket(IP, PORT);

			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		this.start();
	}

	public void run()
	{
		try
		{
			out.write(crypt.keyPair.getPublic().getEncoded());
			
			System.out.println("Sent public key");
			
			int msgSize = in.readInt();
			byte[] msg = new byte[msgSize];
			in.read(msg);
			
			System.out.println("Got message");
			
			String secretMessage = new String(crypt.decrpyt(msg));
			out.write(secretMessage.getBytes());
			
			System.out.println("Sent decrypted message");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
