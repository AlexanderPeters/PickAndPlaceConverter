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
import java.util.List;

public class FileSaver {
	ReadAndWriteTXTLib helper;
	String dataSeperator;
	String dataTerminator;
	public FileSaver(String filePath, String dataSeperator, String dataTerminator) {
		helper = new ReadAndWriteTXTLib(filePath);
		this.dataTerminator = dataTerminator;
		this.dataSeperator = dataSeperator;
	}
	
	
	public void saveData(List<List<String>> data, int rowChanged) throws IOException {
		int i = 1;
		for(List<String> lineList: data) {
			if(i == rowChanged + 1) {
				helper.replaceLine(i, helper.assembleLine(lineList, dataSeperator, dataTerminator));
				break;
			}
			i++;
		}
	}
}
