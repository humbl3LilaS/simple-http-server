package com.edelweiss.httpserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;


public class Json
{
	private static ObjectMapper objectMapper = defaultObjectMapper();

	private static ObjectMapper defaultObjectMapper()
	{
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNEXPECTED_VIEW_PROPERTIES, false);
		return om;
	}


	public static JsonNode parse(String jsonSrc) throws IOException
	{
		return objectMapper.readTree(jsonSrc);
	}

	public static <T> T fromJson(JsonNode node, Class<T> clazz) throws JsonProcessingException
	{
		return objectMapper.treeToValue(node, clazz);
	}

	public static JsonNode toJson(Object object)
	{
		return objectMapper.valueToTree(object);
	}

	public static String stringify(JsonNode node) throws JsonProcessingException
	{
		return generateJson(node, false);
	}

	public static String stringifyPretty(JsonNode node) throws JsonProcessingException
	{
		return generateJson(node, true);
	}

	private static String generateJson(Object obj, boolean pretty) throws JsonProcessingException
	{
		ObjectWriter writer = objectMapper.writer();
		if (pretty)
		{
			writer = writer.with(SerializationFeature.INDENT_OUTPUT);
		}
		return writer.writeValueAsString(obj);
	}

}
