package com.edelweiss.httpserver.core;

import com.edelweiss.httpserver.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionWorkThread extends Thread
{
	private Socket socket;
	private final Logger LOGGER = LoggerFactory.getLogger(ConnectionWorkThread.class);

	public ConnectionWorkThread(Socket socket)
	{
		this.socket = socket;
	}

	@Override
	public void run()
	{
		InputStream requestStream = null;
		OutputStream responseStream = null;

		try
		{
			requestStream = socket.getInputStream();
			responseStream = socket.getOutputStream();

			String output = "<html><head><title>Simple Java Http " +
					"Server</title></head><body><h1>This is my http server</h1></body></html>";

			String response = "HTTP/1.1 200 0K\r\n" + "Content-Length: " + output.length() + "\r" +
					"\n" +
					"\r\n" + output + "\r\n\r\n";

			responseStream.write(response.getBytes());


			LOGGER.info("Connection processing finished");
		}
		catch (IOException e)
		{
			LOGGER.error("Problem with communication: ", e);
		}
		finally
		{
			try
			{
				if (requestStream != null)
				{
					requestStream.close();
				}
				if (responseStream != null)
				{
					responseStream.close();
				}
				if (socket != null)
				{
					socket.close();
				}
			}
			catch (IOException e)
			{
				System.out.println(e.getMessage());
			}
		}
	}
}
