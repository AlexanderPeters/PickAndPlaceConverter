/*
 * Copyright 2017 (C) <GP Instruments>
 * 
 * Created on : 20-7-17
 * Author     : Alexander Peters
 *
 *-----------------------------------------------------------------------------
 * Revision History (Release 1.0.0.0)
 */

import java.io.IOException;

public class FileSaver {
	ReadAndWriteTXTLib helper;
	String dataSeperator;
	String dataTerminator;
	public FileSaver(String filePath, String dataSeperator, String dataTerminator) {
		helper = new ReadAndWriteTXTLib(filePath);
		this.dataTerminator = dataTerminator;
		this.dataSeperator = dataSeperator;
	}
	
	
	public void saveData(String[][] data, int rowChanged) throws IOException {
		for(int i = 1; i <= helper.countLines(); i++)
			if(i == rowChanged + 1) {
				helper.replaceLine(i, helper.assembleLine(data[i - 1], dataSeperator, dataTerminator));
				break;
			}
	}
}
