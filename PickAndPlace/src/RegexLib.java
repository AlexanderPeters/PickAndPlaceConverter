/*
 * Copyright 2017 (C) <GP Instruments>
 * 
 * Created on : 20-7-17
 * Author     : Alexander Peters
 *
 *-----------------------------------------------------------------------------
 * Revision History (Release 1.0.0.0)
 */

public class RegexLib {
	//Returns if the specified string only contains numbers
	public static boolean stringIsOnlyNumbers(String text) {
		return text.matches("^[0-9]*$");
	}
	
	//Returns if the specified string only consists of numbers or symbols
	public static boolean stringIsOnlyNumbersAndSigns(String text) {
		return text.matches("^-?(0|[1-9]\\d*)?(\\.\\d+)?(?<=\\d)$");
	}
	
	//Returns if the specified string is only consists of characters
	public static boolean stringIsOnlyCharachters(String text) {
		return text.matches("[a-zA-Z]+");
	}
	
	public static boolean stringOnlyContainsOneTwoOrThree(String text) {
		return text.matches("^[(1,2,3)]$");		
	}
	
	public static boolean stringOnlyContainsOneDigit0To9(String text) {
		return text.matches("(?<!\\S)\\d(?!\\S)");
	}
	
	//TODO The Regex statement methods below this line.
	public static boolean stringIsBetween0and9999(String text) {
		
	}
	
	public static boolean stringIsBetweenNegative999andPositive999(String text) {
		
	}
	
	public static boolean stringIsBetweenNegative99999andPositive99999(String text) {
		
	}
	
	public static boolean stringIsBetween0and35999(String text) {
		
	}
	
	public static boolean stringIsLessThanOrEqualTo10CharactersInLength(String text) {
		return text.length() <= 10;
	}
	
	
	
}
