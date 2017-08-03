/*
 * Copyright 2017 (C) <GP Instruments>
 * 
 * Created on : 20-7-17
 * Author     : Alexander Peters
 *
 *-----------------------------------------------------------------------------
 * Revision History (Release 1.0.0.0)
 */

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MakeNCFile extends JPanel {
	private static final long serialVersionUID = 1L;
	private String newPath;
	private DecimalFormat formatFive = new DecimalFormat("#####.");// Formats the number to 5 whole digits
	private TablePanel table;
	
	public MakeNCFile() throws IOException {
		String filePath = MainGUI.getFilePath();
		newPath = filePath.substring(0, filePath.length() - 4).concat("temp.txt");
		
		// Check to make sure that a file at new Path does not already exist
		File f = new File(newPath);
		if(f.exists() && !f.isDirectory()) { 
			f.delete();// Delete the file if it does exist to make way for a new working file
		}
		
		//TODO Make everything below this line take less time to execute.
		
		// Instantiate
		ReadAndWriteTXTLib helper = new ReadAndWriteTXTLib(filePath);
		String[] columnNames = { "Step Number", "Value", "Package", "Skip Step", "X-Orient", "Y-Orient", "Head#", "Feeder#", "Angle-Orient",
				"MountHeight-Comp.", "Comment", "Training Point" };
		FileSaver saver = new FileSaver(newPath, ",", ";");
		int[] allowableDataFormatPerColumn = { 0,1,1,2,2,3,4,5,6,7,8,9 };
		List<String> lineData = new ArrayList<String>();
		List<List<String>> seperatedLineData = new ArrayList<List<String>>();
		
		/*
		 *0 - Column Can Not Be Edited
		 *1 - anything goes
		 *2 - stringOnlyContainsOneDigit0To9
		 *3 - stringIsBetweenNegative99999andPositive99999
		 *4 - stringOnlyContainsOneTwoOrThree
		 *5 - stringIsBetween1and100
		 *6 - stringIsBetween0and35999
		 *7 - stringIsBetweenNegative999andPositive999
		 *8 -  stringIsLessThanOrEqualTo10CharactersInLength
		 *9 - stringIsOnlyYorN
		 */
		
		
		// Start of file modifications
		
		// Copy lines from the Original KiCad File
		for (int i = 1; i <= helper.countLines(); i++) {
			lineData.add(helper.getLine(i));
		}
		
		// Change target address
		helper.setFilePath(newPath);
		
		// Paste lines into the working file
		for(int i = 0; i < lineData.size(); i++)
			helper.appendLine(lineData.get(i));
		
		// Trim Document
		for (int i = 0; i < 5; i++)
			helper.deleteNthLine(1);
		for (int i = helper.countLines(); i > 0; i--)
			if (!helper.getLine(i).isEmpty()) {
				helper.deleteNthLine(i);
				break;
			}
		
		// Condense Spacing
		helper.condenseAllLines();
		
		// Correct Line Endings
		helper.replaceAllInFile(" ", ",");
		helper.replaceAllInFile(",top", ";");
		helper.replaceAllInFile(",bottom", ";");
		
		// Correct Data
		for(int i = 1; i <= helper.countLines(); i++) {
			// Correct Step Numbers
			String lineNum = "000" + String.valueOf(i);
			lineNum = lineNum.substring(lineNum.length() - 4);
			List<String> line = helper.readLine(i, ",", new String[]{";"});
			line.set(0, lineNum);
			
			//Correct Number Values
			line.set(3, String.valueOf(formatFive.format(Double.parseDouble(line.get(3)) * 100)));
			line.set(4, String.valueOf(formatFive.format(Double.parseDouble(line.get(4)) * 100)));
			line.set(5, String.valueOf(formatFive.format(Double.parseDouble(line.get(5)) * 100)));
			
			// Insert default values
			line.add(3, "0");
			line.add(6, "0");
			line.add(7, "0");
			line.add(9, "0");
			line.add(10, "Comment");
			line.add(11, "N");
					
			//Save Line To File
			helper.replaceLine(i, helper.assembleLine(line, ",", ";"));
		}
		
		//Remove "."
		for(int i = 1; i <= helper.countLines(); i++)
			helper.replaceLine(i, helper.getLine(i).replace(".", ""));
		
		// Retrieve Separated Line Data
		for (int i = 1; i <= helper.countLines(); i++) {
			seperatedLineData.add(helper.readLine(i, ",", new String[] { ";" }));
		}
		
		//TODO
		//Saving in the correct format instead of the raw data
		//Append Lines of the JTable
		//Comment all the things (getting better)
		
		//Create the table
		table = new TablePanel(saver, columnNames, seperatedLineData, allowableDataFormatPerColumn);
		
		//Add table to panel
		this.add(table);
	}
	
	
	public void closing() {
		String ncFilePath = "";
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter textFilter = new FileNameExtensionFilter("Pick and Place Files", ".NC");

		fc.setCurrentDirectory(new File("C:/Users"));
		fc.addChoosableFileFilter(textFilter);
		fc.setAcceptAllFileFilterUsed(false);
		
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fc.getSelectedFile();
			try {
				ncFilePath = selectedFile.getCanonicalPath();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(!ncFilePath.isEmpty()) {
			File ncFile = new File(ncFilePath);
			File tempFile = new File(newPath);
			tempFile.renameTo(ncFile);
			tempFile.delete();
		}
		else new PopupErrorPanel("Error No File Selected \n Saving of NC File cannot proceed!",
				"Data Select Error!");
	}
	
	public TablePanel getTablePanel() {
		return table;
	}
}
