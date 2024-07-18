package com.edelweiss.httpserver.resolver;

import com.edelweiss.http.HttpParsingException;
import com.edelweiss.http.HttpStatusCode;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class RequestResolver
{
	public static boolean isSupportedRoute(String providedRoute)
	{
		if (providedRoute == "/favicon.ico")
		{
			return true;
		}

		for (SupportedRoutes supportedRoute : SupportedRoutes.values())
		{
			if (Objects.equals(supportedRoute.getROUTE_PATH(), providedRoute))
			{
				return true;
			}
		}
		return false;
	}

	public static StringBuilder getRelatedFile(String route) throws IOException
	{
		StringBuilder responseBuffer = new StringBuilder();
		FileReader fIn = new FileReader(route, StandardCharsets.US_ASCII);
		int _byte;
		while ((_byte = fIn.read()) >= 0)
		{
			responseBuffer.append((char) _byte);
		}
		fIn.close();
		return responseBuffer;
	}

	public static StringBuilder getFileNotFoundPage() throws IOException
	{
		StringBuilder responseBuffer = new StringBuilder();
		FileReader fIn = new FileReader("src/main/model/not_found.html");
		int _byte;
		while ((_byte = fIn.read()) >= 0)
		{
			responseBuffer.append((char) _byte);
		}
		fIn.close();
		return responseBuffer;
	}

}
