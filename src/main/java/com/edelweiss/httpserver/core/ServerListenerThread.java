package com.edelweiss.httpserver.core;

import com.edelweiss.httpserver.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerListenerThread extends Thread
{
	private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);

	private int port;
	private String webroot;

	private ServerSocket serverSocket;

	public ServerListenerThread(Configuration config) throws IOException
	{
		this.port = config.getPort();
		this.webroot = config.getWebroot();
		this.serverSocket = new ServerSocket(this.port);
	}

	@Override
	public void run()
	{
		try
		{
			while (serverSocket.isBound() && !serverSocket.isClosed())
			{
				Socket socket = serverSocket.accept();

				LOGGER.info("Connection accepted: " + socket.getInetAddress());

				var connectionWorker = new ConnectionWorkThread(socket);
				connectionWorker.setRoot(webroot);
				connectionWorker.start();
			}
		}
		catch (IOException e)
		{
			LOGGER.error("Problem with setting socket", e);
		}
		finally
		{
			if (serverSocket != null)
			{
				try
				{
					serverSocket.close();
				}
				catch (IOException e)
				{
					System.out.println(e.getMessage());
				}
			}
		}
	}
}
