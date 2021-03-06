package com.library.app.commontests.utils;

import java.io.InputStream;
import java.util.Scanner;

import org.json.JSONException;
import org.junit.Ignore;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

@Ignore
public class JsonTestUtils {
	public static final String BASE_JSON_DIR = "json/";

	private JsonTestUtils() {
	}

	public static String readJsonFile(String relativePath) {
		final InputStream is = JsonTestUtils.class.getClassLoader().getResourceAsStream(BASE_JSON_DIR + relativePath);
		try (Scanner s = new Scanner(is)) {
			return s.useDelimiter("\\A").hasNext() ? s.next() : "";
		}
	}

	public static void assertJsonMatchesExpectedJson(String actualJson, String expectedJson) {
		try {
			JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.NON_EXTENSIBLE);
		} catch (final JSONException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static void assertJsonMatchesFileContent(String actualJson, String fileNameWithExpectedJson) {
		assertJsonMatchesExpectedJson(actualJson, readJsonFile(fileNameWithExpectedJson));
	}
}
