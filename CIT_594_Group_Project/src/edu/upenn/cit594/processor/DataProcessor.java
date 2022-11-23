package edu.upenn.cit594.processor;

import java.util.regex.Pattern;

public class DataProcessor {
	
	public static boolean checkValidZip(int zipcode, boolean property) {
		boolean valid = false;

		// check zipcode in property data (as long as zipcode has more than 5 digits it's valid)
		if(property) {
			if(zipcode >= 10000) {
				valid = true;
			}
		}
		// check zipcode in covid or population data (zipcode needs to be exactly 5 digits to be valid)
		else {
			if(zipcode >= 10000 && zipcode <= 99999) {
				valid = true;
			}
		}
		return valid;
	}
	
	public static int returnValidZip(int zipcode) {
		// when zipcode has more than 5 digits
		if(zipcode > 99999) {
			// cast zipcode to string type
			String zipcodeString = Integer.toString(zipcode);
			// only keep the first 5 characters
			zipcode = Integer.parseInt(zipcodeString.substring(0, 5)); 
			return zipcode;
		}
		else {
			return zipcode;
		}
	}
	
	// check “YYYY-MM-DD hh:mm:ss” format;
	public static boolean checkValidTimestamp(String timestamp) {
		boolean valid = false;
		String timestampRegex = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]$";
		// create pattern
		Pattern pattern = Pattern.compile(timestampRegex);
		if(pattern.matcher(timestamp).find()) {
			valid = true;
		}
		return valid;
	}
	
}
