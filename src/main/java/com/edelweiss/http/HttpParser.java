package com.edelweiss.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser
{
	private final Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

	private static final int SP = 0x20;
	private static final int CR = 0x0D;
	private static final int LF = 0x0A;

	public HttpRequest parseHttpRequest(InputStream requestStream) throws HttpParsingException
	{
		InputStreamReader reader = new InputStreamReader(requestStream, StandardCharsets.US_ASCII);

		HttpRequest request = new HttpRequest();

		try
		{
			parseRequestLine(reader, request);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		parseHeader(reader, request);
		parseBody(reader, request);


		return request;
	}

	private void parseBody(InputStreamReader reader, HttpRequest request)
	{

	}

	private void parseHeader(InputStreamReader requestStream, HttpRequest request)
	{

	}

	private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException
	{
		StringBuilder dataBuffer = new StringBuilder();

		boolean methodParsed = false;
		boolean reqTargetParsed = false;

		int _byte;
		while ((_byte = reader.read()) >= 0)
		{
			if (_byte == CR)
			{
				_byte = reader.read();
				if (_byte == LF)
				{
					LOGGER.debug("Request Line Version to Process: {}", dataBuffer.toString());
					if (!methodParsed || !reqTargetParsed)
					{
						throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
					}
					try
					{
						request.setHttpVersion(dataBuffer.toString());
					}
					catch (UnsupportedHttpVersionException e)
					{
						throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
					}
					return;
				} else
				{
					throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
				}
			}

			if (_byte == SP)
			{
				if (!methodParsed)
				{
					LOGGER.debug("Request Line Method to Process: {}", dataBuffer.toString());
					request.setMethod(dataBuffer.toString());
					methodParsed = true;
				} else if (!reqTargetParsed)
				{
					LOGGER.debug("Request Line Request Target to Process: {}",
								 dataBuffer.toString());
					request.setRequestTarget(dataBuffer.toString());
					reqTargetParsed = true;
				} else
				{
					throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
				}
				dataBuffer.delete(0, dataBuffer.length());
			} else
			{
				dataBuffer.append((char) _byte);
				if (!methodParsed)
				{
					if (dataBuffer.length() > HttpMethod.MAX_LENGTH)
					{
						throw new HttpParsingException(
								HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
					}
				}
			}
		}
	}
}
