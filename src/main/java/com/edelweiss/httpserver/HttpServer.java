package com.edelweiss.httpserver;

import com.edelweiss.httpserver.config.ConfigManager;
import com.edelweiss.httpserver.config.Configuration;
import com.edelweiss.httpserver.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpServer
{
	private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

	public static void main(String[] args)
	{
		LOGGER.info("Server starting....");
		ConfigManager.getInstance().loadConfigFile("src/main/resources/http.json");
		Configuration config = ConfigManager.getInstance().getConfig();


		LOGGER.info("Using Port: " + config.getPort());
		LOGGER.info("Using Webroot: " + config.getWebroot());
		try
		{
			ServerListenerThread listenerThread = new ServerListenerThread(config);
			listenerThread.start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
