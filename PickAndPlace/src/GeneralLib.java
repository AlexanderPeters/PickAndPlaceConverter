/*
 * Copyright 2017 (C) <GP Instruments>
 * 
 * Created on : 20-7-17
 * Author     : Alexander Peters
 *
 *-----------------------------------------------------------------------------
 * Revision History (Release 1.0.0.0)
 */

public class GeneralLib {
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
	
}
