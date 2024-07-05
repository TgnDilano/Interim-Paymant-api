/*
 * Smobilpay S3P API STANDARD
 * Smobilpay Third Party STANDARD API FOR PAYMENT COLLECTIONS
 *
 * OpenAPI spec version: 3.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.iwomi.maviance;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-03-20T16:37:47.602Z")
public class StringUtil {
	/**
	 * Check if the given array contains the given value (with case-insensitive
	 * comparison).
	 *
	 * @param array The array
	 * @param value The value to search
	 * @return true if the array contains the value
	 */
	public static boolean containsIgnoreCase(String[] array, String value) {
		for (String str : array) {
			if (value == null && str == null)
				return true;
			if (value != null && value.equalsIgnoreCase(str))
				return true;
		}
		return false;
	}

	/**
	 * Join an array of strings with the given separator.
	 * <p>
	 * Note: This might be replaced by utility method from commons-lang or guava
	 * someday if one of those libraries is added as dependency.
	 * </p>
	 *
	 * @param array     The array of strings
	 * @param separator The separator
	 * @return the resulting string
	 */
	public static String join(String[] array, String separator) {
		int len = array.length;
		if (len == 0)
			return "";

		StringBuilder out = new StringBuilder();
		out.append(array[0]);
		for (int i = 1; i < len; i++) {
			out.append(separator).append(array[i]);
		}
		return out.toString();
	}

	/**
	 * Generates a random string
	 * 
	 * @return the result string
	 */
	public static String stringGenerator() {
		long min = new Long("111111111"), max = new Long("99999999999");
		long v = (long) (Math.random() * (max - min + 1)) + min;
		return String.valueOf(v);
	}

	/**
	 * Converts a float to string and remove the decimal part if it's zero. 145.00
	 * => 145 78.36 => 78.36 0.4878 => 0.4878 82.0 => 82
	 * 
	 * @param ft the float to convert
	 * @return the string result
	 */
	public static String floatConverter(Float ft) {
		if (ft == null)
			return null;
		if (ft % 1.0 != 0) {
			return String.format("%s", ft);
		} else {
			return String.format("%.0f", ft);
		}
	}

	public static String floatConverter(String s) {
		return s != null ? floatConverter(Float.valueOf(s)) : s;
	}

}
