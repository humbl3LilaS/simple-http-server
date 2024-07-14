package com.edelweiss.httpserver.config;

import com.edelweiss.httpserver.util.Json;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.FileReader;
import java.io.IOException;

public class ConfigManager
{
	private static ConfigManager configManager;

	private static Configuration config;

	private ConfigManager()
	{

	}

	public static ConfigManager getInstance()
	{
		if (configManager == null)
		{
			configManager = new ConfigManager();
		}
		return configManager;
	}

	/**
	 * load a configuration file by the path provided
	 */
	public void loadConfigFile(String path)
	{
		StringBuffer strBuffer = null;
		try
		{
			FileReader reader = new FileReader(path);
			strBuffer = new StringBuffer();
			int i;
			while ((i = reader.read()) != -1)
			{
				strBuffer.append((char) i);
			}
		}
		catch (IOException e)
		{
			throw new HttpConfigurationException(e);
		}
		JsonNode conf = null;
		try
		{
			conf = Json.parse(strBuffer.toString());
		}
		catch (IOException e)
		{
			throw new HttpConfigurationException("Error Parsing the Configuration file");
		}
		try
		{
			config = Json.fromJson(conf, Configuration.class);
		}
		catch (JsonProcessingException e)
		{
			throw new HttpConfigurationException("Error Parsing the Configuration File: Internal");
		}
	}


	public Configuration getConfig()
	{
		if (config == null)
		{
			throw new HttpConfigurationException("No current Configuration Set.");
		}
		return config;
	}

}
