/*
 * Copyright 2017 (C) <GP Instruments>
 * 
 * Created on : 20-7-17
 * Author     : Alexander Peters
 *
 *-----------------------------------------------------------------------------
 * Revision History (Release 1.0.0.0)
 */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

// Public class which defines functions for reading to, writing to, creating, and deleting text files.
public class ReadAndWriteTXTLib {
	private String filePath = "C:/";
	
	public ReadAndWriteTXTLib(String filePath) {
		this.filePath = filePath;
	}
	
	// Returns the current file location that has been set.
	public String getFilePath() {
		return (filePath);
	}
	
	// Sets the file location to be used.
	public void setFilePath(String e) {
		filePath = e;
	}
	
	//Creates a new text file at filePath with the provided data within it.
	public void createFile(String[] data) throws IOException {
        File file = new File(filePath);
		PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        
        for(int i = 0; i < data.length; i++) output.println(data[i]);
        if (output != null) output.close();          
    }
	
	// Deletes files located at filePath.
	public boolean deleteFile() {
		File f = new File(filePath);
		return f.delete();
	}
	
	// Returns the number of lines within the file located at filePath.
	public int countLines() throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filePath));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i)
	                if (c[i] == '\n') ++count;
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	
	// Returns the specified line of the file located at filePath as a string.
	public String getLine(int lineNum) {
		BufferedReader br = null;
		String chosenLine = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			List<String> lines = new ArrayList<>();
			String line = null;
			int i = 0;
			while (i < 5) { // Leave some empty lines at the end of the list which may be utilized as place-holders
				line = br.readLine();
				if(line == null) {
					i++;
					lines.add("");
					continue;
				}
				lines.add(line);
			}
			chosenLine = lines.get(lineNum - 1); // First line is # 1, but first item in array is #0
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return chosenLine;
	}
	
	// Returns the number of characters present within the specified line of the file located at filePath.
	public int getLineLength(int lineNum) {
		return getLine(lineNum).length();	
	}

	// Appends a line of a specified value to the end of the text of the file located at filePath.
	public void appendLineWithoutWhiteSpaceBuffer(String lineToBeAppended) throws IOException {
		for(int i = countLines(); i > 0; i--) {
			if(!getLine(i).isEmpty()) {
				replaceLine(i + 1, lineToBeAppended);
				break;
			}
		}
	}
		
	// Appends a line of a specified value to the end of the file 
	// (last length of the file including excess whitespace at the end) located at filePath.
	public void appendLine(String lineToBeAppended) throws IOException {
		PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)));
		output.println(lineToBeAppended);
		output.close();
	}
	
	// Replaces the given line number with lineReplacement.
	public void replaceLine(int lineNum, String lineReplacement) throws IOException {
		Path replacePath = Paths.get(filePath);
		List<String> fileContent = new ArrayList<>(Files.readAllLines(replacePath, StandardCharsets.UTF_8));
		
		if(fileContent.size() != countLines())
			for(int i = 0; i <= countLines() -  fileContent.size(); i++) fileContent.add("");
		for (int i = 0; i < fileContent.size(); i++) {
		    if (i == lineNum - 1) {
		    	fileContent.set(i, lineReplacement);
		        break;
		    }
		}
		Files.write(replacePath, fileContent, StandardCharsets.UTF_8);
	}
	
	// Inserts a line of a specified value at the line number of the file located at filePath.
	public void insertLine(int lineNum, String lineToBeInserted) throws IOException {
		double rand = Math.random();
		File outputFile = new File("tempFile" + rand + ".txt");
		BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
		PrintWriter output = new PrintWriter(new FileOutputStream(outputFile));
		String thisLine = "";
		int i = 1;
		while ((thisLine = input.readLine()) != null) {
			if(i == lineNum && i <= countLines()) output.println(lineToBeInserted);
			else if(i == lineNum) appendLineWithoutWhiteSpaceBuffer(lineToBeInserted);
			output.println(thisLine);
			i++;
		}
		output.flush();
		output.close();
		input.close();
		deleteFile();
		outputFile.renameTo(new File(filePath));
	}
	
	// Removes the line of a specified value located within the file at filePath.
	public void deleteLineOfValueX(String lineOfValueX) throws IOException {
		for(int i = 1; i <= countLines(); i++)
			if(getLine(i).equals(lineOfValueX)) {
				deleteNthLine(i);
				break;
			}
	}
	
	// Removes the Nth line located within the file at filePath.
	public void deleteNthLine(int NthLineNum) throws IOException {
	    RandomAccessFile raf = new RandomAccessFile(filePath, "rw");

	    // Leave the n first lines unchanged.
	    for (int i = 1; i < NthLineNum; i++) raf.readLine();

	    // Shift remaining lines upwards.
	    long writePos = raf.getFilePointer();
	    raf.readLine();
	    long readPos = raf.getFilePointer();

	    byte[] buf = new byte[1024];
	    int n;
	    while (-1 != (n = raf.read(buf))) {
	        raf.seek(writePos);
	        raf.write(buf, 0, n);
	        readPos += n;
	        writePos += n;
	        raf.seek(readPos);
	    }

	    raf.setLength(writePos);
	    raf.close();
	}
	
	// Reduces Whitespace From a Given Line To One Space Wherever There Were Multiple Before
	public String condense(String line) {
		String newLine  = "";
		if(line != " " && line != null) {
			int i = 0;
			while(i < line.length()) {
				if(line.charAt(i) != ' ' || line.charAt(i - 1) != ' ') newLine = newLine.concat(line.charAt(i) + "");
				i++;
			}
		}
		return newLine;
	}
	
	// Condenses empty space within the specified line of the file located at filePath down to one space and returns the condensed result.
	public void condenseLine(int lineNum) throws IOException {
		String line = getLine(lineNum), newLine = "";
		if(line != " " && line != null) {
			int i = 0;
			while(i < line.length()) {
				if(line.charAt(i) != ' ' || line.charAt(i - 1) != ' ') newLine = newLine.concat(line.charAt(i) + "");
				i++;
			}
			replaceLine(lineNum, newLine);
		}
	}
		
	// Condenses each line within the file located at filePath
	public void condenseAllLines() throws IOException {
		for(int i = 1; i <= countLines(); i++) {
			condenseLine(i);
		}
	}
	
	// Assembles a CSV format line from the given items, and defined and terminator characters.
	public String assembleLine(List<String> items, String seperator, String terminator) {
		String a = "", b  = "";
		for(String item: items) {
			b = condense(item.replaceAll(terminator, " "));
			a += b += seperator;
		}
		a = a.substring(0, a.length()-1);
		return  a += terminator;
	}
	
	// Reads in a line from the file located at filePath and returns a list of important and separated values.
	public List<String> readLine (int lineNum, String lineSplitString, String[] possibleEndOfLineStrings) {
		String line = getLine(lineNum);
		String[] parts = line.substring(0, indexOfPartOfAnArray(line, possibleEndOfLineStrings)).split(lineSplitString);
		List<String> values = new ArrayList<String>(0);
		for(String part: parts)
			values.add(part);
		return values;
	}
	
	// Replace all instances of String a located within the file with instances of String b
	public void replaceAllInFile(String a, String b) throws IOException {
		for(int i = 1; i <= countLines(); i++) {
			replaceLine(i, getLine(i).replaceAll(a, b));
		}
	}
	
	// Finds the first index of any word found in the array within the given string.
	// Returns -1 if none of the strings are found.
	private int indexOfPartOfAnArray(String stringToBeSearched, String[] stringsToFind) {
		int a  = -1, b;
		for(int i = 0; i < stringsToFind.length; i++)
			if((b = stringToBeSearched.indexOf(stringsToFind[i])) > a) a = b;
		return a;
	}
		
	// Determines whether a string can be found in a specified string array and returns the result.
	private boolean foundInArray(String a, String[] b) {
		for(int i = 0; i < b.length; i++) 
			if(a.equals(b[i])) return true;
		return false;
	}
}