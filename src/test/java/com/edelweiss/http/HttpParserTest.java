package com.edelweiss.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest
{

	private HttpParser parser;


	@BeforeAll
	public void beforeClass()
	{
		parser = new HttpParser();
	}

	@Test
	void parseHttpRequest()
	{
		HttpRequest req = null;
		try
		{
			req = parser.parseHttpRequest(generateValidTestCase());
			assertNotNull(req);
			assertEquals(req.getMethod(), HttpMethod.GET);
			assertEquals(req.getRequestTarget(), "/");
		}
		catch (HttpParsingException e)
		{
			fail(e);
		}
	}

	@Test
	void parseHttpRequestBadMethodName()
	{
		HttpRequest req = null;
		try
		{
			req = parser.parseHttpRequest(generateInvalidTestCase());
			fail();
		}
		catch (HttpParsingException e)
		{
			assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
		}
	}


	@Test
	void parseHttpRequestBadMethodName2()
	{
		HttpRequest req = null;
		try
		{
			req = parser.parseHttpRequest(generateInvalidTestCase2());
			fail();
		}
		catch (HttpParsingException e)
		{
			assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
		}
	}


	@Test
	void parseHttpBadRequestInvalidNumberOfItems()
	{
		HttpRequest req = null;
		try
		{
			req = parser.parseHttpRequest(generateInvalidTestCaseInvalidNumberOfItem());
			fail();
		}
		catch (HttpParsingException e)
		{
			assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
		}
	}

	@Test
	void parseHttpBadRequestEmptyRequestLine() {
		HttpRequest req = null;
		try {
			req = parser.parseHttpRequest(generateInvalidTestCaseEmptyRequestLine());
		}catch (HttpParsingException e) {
			assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
		}
	}

	@Test
	void parseHttpBadRequestNoLF() {
		HttpRequest req = null;
		try {
			req = parser.parseHttpRequest(generateInvalidTestCaseNoLF());
		}catch (HttpParsingException e) {
			assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
		}
	}
	private InputStream generateValidTestCase()
	{
		String rawRequest = "GET / HTTP/1.1\r\n" +
				"Host: localhost:8080\r\n" +
				"Connection: keep-alive\r\n" +
				"sec-ch-ua: \"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";" +
				"v=\"126\"\r\n" +
				"sec-ch-ua-mobile: ?0\r\n" +
				"sec-ch-ua-platform: \"Windows\"\r\n" +
				"Upgrade-Insecure-Requests: 1\r\n" +
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML," +
				" like Gecko) Chrome/126.0.0.0 Safari/537.36\r\n" +
				"Sec-Purpose: prefetch;prerender\r\n" +
				"Purpose: prefetch\r\n" +
				"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif," +
				"image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0" +
				".7\r\n" +
				"Sec-Fetch-Site: none\r\n" +
				"Sec-Fetch-Mode: navigate\r\n" +
				"Sec-Fetch-User: ?1\r\n" +
				"Sec-Fetch-Dest: document\r\n" +
				"Accept-Encoding: gzip, deflate, br, zstd\r\n" +
				"Accept-Language: en-US,en;q=0.9\r\n" +
				"Cookie: Webstorm-d7701941=64e4a356-ba00-4349-bc08-97ec1dac0ef7; " +
				"Phpstorm-e19fb49d=b4c5aa7c-1ad1-40b4-80b1-4618b96bc66b" + "\r\n";

		InputStream request = new ByteArrayInputStream(
				rawRequest.getBytes(StandardCharsets.US_ASCII));

		return request;
	}


	private InputStream generateInvalidTestCase()
	{
		String rawRequest = "GeT / HTTP/1.1\r\n" +
				"Host: localhost:8080\r\n" +
				"Connection: keep-alive\r\n" +
				"sec-ch-ua: \"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";" +
				"v=\"126\"\r\n" +
				"sec-ch-ua-mobile: ?0\r\n" +
				"sec-ch-ua-platform: \"Windows\"\r\n" +
				"Upgrade-Insecure-Requests: 1\r\n" +
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML," +
				" like Gecko) Chrome/126.0.0.0 Safari/537.36\r\n" +
				"Sec-Purpose: prefetch;prerender\r\n" +
				"Purpose: prefetch\r\n" +
				"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif," +
				"image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0" +
				".7\r\n" +
				"Sec-Fetch-Site: none\r\n" +
				"Sec-Fetch-Mode: navigate\r\n" +
				"Sec-Fetch-User: ?1\r\n" +
				"Sec-Fetch-Dest: document\r\n" +
				"Accept-Encoding: gzip, deflate, br, zstd\r\n" +
				"Accept-Language: en-US,en;q=0.9\r\n" +
				"Cookie: Webstorm-d7701941=64e4a356-ba00-4349-bc08-97ec1dac0ef7; " +
				"Phpstorm-e19fb49d=b4c5aa7c-1ad1-40b4-80b1-4618b96bc66b" + "\r\n";

		InputStream request = new ByteArrayInputStream(
				rawRequest.getBytes(StandardCharsets.US_ASCII));

		return request;
	}

	private InputStream generateInvalidTestCase2()
	{
		String rawRequest = "SUPERRRGET / HTTP/1.1\r\n" +
				"Host: localhost:8080\r\n" +
				"Connection: keep-alive\r\n" +
				"sec-ch-ua: \"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";" +
				"v=\"126\"\r\n" +
				"sec-ch-ua-mobile: ?0\r\n" +
				"sec-ch-ua-platform: \"Windows\"\r\n" +
				"Upgrade-Insecure-Requests: 1\r\n" +
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML," +
				" like Gecko) Chrome/126.0.0.0 Safari/537.36\r\n" +
				"Sec-Purpose: prefetch;prerender\r\n" +
				"Purpose: prefetch\r\n" +
				"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif," +
				"image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0" +
				".7\r\n" +
				"Sec-Fetch-Site: none\r\n" +
				"Sec-Fetch-Mode: navigate\r\n" +
				"Sec-Fetch-User: ?1\r\n" +
				"Sec-Fetch-Dest: document\r\n" +
				"Accept-Encoding: gzip, deflate, br, zstd\r\n" +
				"Accept-Language: en-US,en;q=0.9\r\n" +
				"Cookie: Webstorm-d7701941=64e4a356-ba00-4349-bc08-97ec1dac0ef7; " +
				"Phpstorm-e19fb49d=b4c5aa7c-1ad1-40b4-80b1-4618b96bc66b" + "\r\n";

		InputStream request = new ByteArrayInputStream(
				rawRequest.getBytes(StandardCharsets.US_ASCII));

		return request;
	}


	private InputStream generateInvalidTestCaseInvalidNumberOfItem()
	{
		String rawRequest = "GET / superrrrr HTTP/1.1\r\n" +
				"Host: localhost:8080\r\n" +
				"Connection: keep-alive\r\n" +
				"sec-ch-ua: \"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";" +
				"v=\"126\"\r\n" +
				"sec-ch-ua-mobile: ?0\r\n" +
				"sec-ch-ua-platform: \"Windows\"\r\n" +
				"Upgrade-Insecure-Requests: 1\r\n" +
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML," +
				" like Gecko) Chrome/126.0.0.0 Safari/537.36\r\n" +
				"Sec-Purpose: prefetch;prerender\r\n" +
				"Purpose: prefetch\r\n" +
				"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif," +
				"image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0" +
				".7\r\n" +
				"Sec-Fetch-Site: none\r\n" +
				"Sec-Fetch-Mode: navigate\r\n" +
				"Sec-Fetch-User: ?1\r\n" +
				"Sec-Fetch-Dest: document\r\n" +
				"Accept-Encoding: gzip, deflate, br, zstd\r\n" +
				"Accept-Language: en-US,en;q=0.9\r\n" +
				"Cookie: Webstorm-d7701941=64e4a356-ba00-4349-bc08-97ec1dac0ef7; " +
				"Phpstorm-e19fb49d=b4c5aa7c-1ad1-40b4-80b1-4618b96bc66b" + "\r\n";

		InputStream request = new ByteArrayInputStream(
				rawRequest.getBytes(StandardCharsets.US_ASCII));

		return request;
	}

	private InputStream generateInvalidTestCaseEmptyRequestLine()
	{
		String rawRequest = "\r\n" +
				"Host: localhost:8080\r\n" +
				"Connection: keep-alive\r\n" +
				"sec-ch-ua: \"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";" +
				"v=\"126\"\r\n" +
				"sec-ch-ua-mobile: ?0\r\n" +
				"sec-ch-ua-platform: \"Windows\"\r\n" +
				"Upgrade-Insecure-Requests: 1\r\n" +
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML," +
				" like Gecko) Chrome/126.0.0.0 Safari/537.36\r\n" +
				"Sec-Purpose: prefetch;prerender\r\n" +
				"Purpose: prefetch\r\n" +
				"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif," +
				"image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0" +
				".7\r\n" +
				"Sec-Fetch-Site: none\r\n" +
				"Sec-Fetch-Mode: navigate\r\n" +
				"Sec-Fetch-User: ?1\r\n" +
				"Sec-Fetch-Dest: document\r\n" +
				"Accept-Encoding: gzip, deflate, br, zstd\r\n" +
				"Accept-Language: en-US,en;q=0.9\r\n" +
				"Cookie: Webstorm-d7701941=64e4a356-ba00-4349-bc08-97ec1dac0ef7; " +
				"Phpstorm-e19fb49d=b4c5aa7c-1ad1-40b4-80b1-4618b96bc66b" + "\r\n";

		InputStream request = new ByteArrayInputStream(
				rawRequest.getBytes(StandardCharsets.US_ASCII));

		return request;
	}

	private InputStream generateInvalidTestCaseNoLF()
	{
		String rawRequest = "GET / HTTP1.1\r" +
				"Host: localhost:8080\r\n" +
				"Connection: keep-alive\r\n" +
				"sec-ch-ua: \"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";" +
				"v=\"126\"\r\n" +
				"sec-ch-ua-mobile: ?0\r\n" +
				"sec-ch-ua-platform: \"Windows\"\r\n" +
				"Upgrade-Insecure-Requests: 1\r\n" +
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML," +
				" like Gecko) Chrome/126.0.0.0 Safari/537.36\r\n" +
				"Sec-Purpose: prefetch;prerender\r\n" +
				"Purpose: prefetch\r\n" +
				"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif," +
				"image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0" +
				".7\r\n" +
				"Sec-Fetch-Site: none\r\n" +
				"Sec-Fetch-Mode: navigate\r\n" +
				"Sec-Fetch-User: ?1\r\n" +
				"Sec-Fetch-Dest: document\r\n" +
				"Accept-Encoding: gzip, deflate, br, zstd\r\n" +
				"Accept-Language: en-US,en;q=0.9\r\n" +
				"Cookie: Webstorm-d7701941=64e4a356-ba00-4349-bc08-97ec1dac0ef7; " +
				"Phpstorm-e19fb49d=b4c5aa7c-1ad1-40b4-80b1-4618b96bc66b" + "\r\n";

		InputStream request = new ByteArrayInputStream(
				rawRequest.getBytes(StandardCharsets.US_ASCII));

		return request;
	}
}
//
//GET / HTTP/1.1
//		Host: localhost:8080
//		Connection: keep-alive
//		sec-ch-ua: "Not/A)Brand";v="8", "Chromium";v="126", "Google Chrome";v="126"
//		sec-ch-ua-mobile: ?0
//		sec-ch-ua-platform: "Windows"
//		Upgrade-Insecure-Requests: 1
//		User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36
//		Sec-Purpose: prefetch;prerender
//		Purpose: prefetch
//		Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
//Sec-Fetch-Site: none
//Sec-Fetch-Mode: navigate
//Sec-Fetch-User: ?1
//Sec-Fetch-Dest: document
//Accept-Encoding: gzip, deflate, br, zstd
//Accept-Language: en-US,en;q=0.9
//Cookie: Webstorm-d7701941=64e4a356-ba00-4349-bc08-97ec1dac0ef7; Phpstorm-e19fb49d=b4c5aa7c-1ad1-40b4-80b1-4618b96bc66b