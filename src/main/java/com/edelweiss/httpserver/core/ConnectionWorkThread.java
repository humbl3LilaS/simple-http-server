package com.edelweiss.httpserver.core;

import com.edelweiss.http.HttpParser;
import com.edelweiss.http.HttpParsingException;
import com.edelweiss.http.HttpStatusCode;
import com.edelweiss.httpserver.config.Configuration;
import com.edelweiss.httpserver.resolver.RequestResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class ConnectionWorkThread extends Thread
{
	private Socket socket;
	private final Logger LOGGER = LoggerFactory.getLogger(ConnectionWorkThread.class);

	private String ROOT_PATH = "/";

	public ConnectionWorkThread(Socket socket)
	{
		this.socket = socket;
	}

	void setRoot(String root)
	{
		this.ROOT_PATH = root;
	}

	private String responseFilePathResolver(String path)
	{
		return Objects.equals(path, "/") ? ROOT_PATH + "/home.html" : ROOT_PATH + path + ".html";
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

			var parser = new HttpParser();

			var httpRequest = parser.parseHttpRequest(requestStream);
			StringBuilder resBuf = new StringBuilder();

			try
			{
				if (RequestResolver.isSupportedRoute(httpRequest.getRequestTarget())) {
					String filePath = responseFilePathResolver(httpRequest.getRequestTarget());
					StringBuilder content = RequestResolver.getRelatedFile(filePath);
					resBuf.append(content);
				} else {
					throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
				}
			}
			catch (IOException e)
			{
				LOGGER.error("Problem with file reading: ", e);
			} catch (HttpParsingException e) {
				LOGGER.error("Problem with request path: ", e);
			}
			String response = "HTTP/1.1 200 0K\r\n" + "Content-Length: " + resBuf.length() + "\r" +
					"\n" +
					"\r\n" + resBuf + "\r\n\r\n";

			responseStream.write(response.getBytes());
			LOGGER.info("Connection processing finished");
		}
		catch (IOException e)
		{
			LOGGER.error("Problem with communication: ", e);
		}
		catch (HttpParsingException e)
		{
			LOGGER.error("Problem with Http parsing: ", e);
		}
		finally
		{
			try
			{
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
