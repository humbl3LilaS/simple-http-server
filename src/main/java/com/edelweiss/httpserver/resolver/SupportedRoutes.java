package com.edelweiss.httpserver.resolver;

public enum SupportedRoutes
{
	HOME("/"),ABOUT("/about"), SERVICES("/services");

	private final String ROUTE_PATH;

	SupportedRoutes(String ROUTE_PATH)
	{
		this.ROUTE_PATH = ROUTE_PATH;
	}

	public String getROUTE_PATH()
	{
		return ROUTE_PATH;
	}
}
