package com.jpmc.theater;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PrintUtility {
	private static Logger logger = LogManager.getLogger(PrintUtility.class);
	
	public static Gson GSONBuilder() {
		Gson gson = new GsonBuilder().registerTypeAdapter(Duration.class, new JsonSerializer<Duration>() {
			@Override
			public JsonElement serialize(Duration duration, Type typeOfSrc, JsonSerializationContext context) {
				return new JsonPrimitive(humanReadableFormat(duration));
			}
		}).registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
			@Override
			public JsonElement serialize(LocalDateTime localDateTime, Type type,
					JsonSerializationContext jsonSerializationContext) {
				return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
			}
		}).registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
			@Override
			public JsonElement serialize(LocalDate localDate, Type typeOfSrc, JsonSerializationContext context) {
				return new JsonPrimitive(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
			}
		}).setPrettyPrinting().create();
		return gson;
	}

	public static String humanReadableFormat(Duration duration) {
		long hour = duration.toHours();
		long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());

		return String.format("(%s hour%s %s minute%s)", hour, handlePlural(hour), remainingMin,
				handlePlural(remainingMin));
	}

	 // (s) postfix should be added to handle plural correctly
	private static String handlePlural(long value) {
		if (value == 1) {
			return "";
		} else {
			return "s";
		}
	}

}
