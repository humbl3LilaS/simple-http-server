package com.edelweiss.http;

public class HttpRequest extends HttpMessage
{
	private HttpMethod method;
	private String requestTarget;
	private String originalHttpVersion;

	private HttpVersion bestCompatibleVersion;

	HttpRequest()
	{

	}

	public HttpMethod getMethod()
	{
		return method;
	}

	void setMethod(String methodName) throws HttpParsingException
	{
		for (HttpMethod method : HttpMethod.values())
		{
			if (methodName.equals(method.name()))
			{
				this.method = method;
				return;
			}
		}

		throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
	}


	public String getRequestTarget()
	{
		return requestTarget;
	}

	void setRequestTarget(String requestTarget) throws HttpParsingException
	{
		if (requestTarget == null || requestTarget.length() == 0)
		{
			throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERR0R);
		}
		this.requestTarget = requestTarget;
	}

	void setHttpVersion(String originalVersion) throws UnsupportedHttpVersionException, HttpParsingException
	{
		this.originalHttpVersion = originalVersion;
		this.bestCompatibleVersion = HttpVersion.getBestCompatibleVersion(originalVersion);
		if (this.bestCompatibleVersion == null) {
			throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
		}
	}


}
