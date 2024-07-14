package com.edelweiss.httpserver;

import com.edelweiss.httpserver.config.ConfigManager;

public class HttpServer
{
	public static void main(String[] args)
	{
		System.out.println("Server is starting");
		ConfigManager.getInstance().loadConfigFile("src/main/resources/http.json");
	}
}
