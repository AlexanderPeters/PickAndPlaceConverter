/*
 * Copyright 2017 (C) <GP Instruments>
 * 
 * Created on : 20-7-17
 * Author     : Alexander Peters
 *
 *-----------------------------------------------------------------------------
 * Revision History (Release 1.0.0.0)
 */

import java.awt.BorderLayout;
import java.io.IOException;
import javax.swing.JFrame;

public class MakeNCFile {
	public static void main(String[] args) throws IOException {
		//Instantiate
		ReadAndWriteTXTLib helper = new ReadAndWriteTXTLib("C:/Users/Alexander the geek/Downloads/Test.pos");
		String[] columnNames = {"Step Number", "Part Name", "Package", "X Orient", "YOrient", "ThetaOrient"};
		FileSaver saver = new FileSaver("C:/Users/Alexander the geek/Downloads/Test.pos", ",", ";");
		int[] allowableDataFormatPerColumn = {1,1,1,0,0,0};//0-Only Allows Numbers, 1-Doesn't Matter
		
		//Trim Document
		for(int i = 0; i < 5; i++) helper.deleteNthLine(1);
		for(int i = helper.countLines(); i > 0; i--)
			if(!helper.getLine(i).isEmpty()) {
				helper.deleteNthLine(i);
				break;
			}
		
		//Condense Spacing
		helper.condenseAllLines();
		
		//Correct Line Endings
		helper.replaceAllInFile(" ", ",");
		helper.replaceAllInFile(",top", ";");
		helper.replaceAllInFile(",bottom", ";");
		
		//Retrieve Line Data
		String[][] data = new String[helper.countLines()][];
		for(int i = 1; i <= helper.countLines(); i++) {
			data[i - 1] = helper.readLine(i, ",", new String[] {";"});
		}
				
		TablePanel table = new TablePanel(saver, columnNames, data, allowableDataFormatPerColumn);
		JFrame window = new JFrame();
		window.setTitle("NC Data Editor");
		window.setLayout(new BorderLayout());
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.add(table, BorderLayout.CENTER);
		window.setSize(800, 400);
		window.setVisible(true);
	}
}

