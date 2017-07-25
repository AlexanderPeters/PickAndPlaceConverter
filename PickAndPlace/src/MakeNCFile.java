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
	
	public MakeNCFile() throws IOException {
		String filePath = MainGUI.getFilePath();
		newPath = filePath.substring(0, filePath.length() - 4).concat("temp.txt");
		
		//TODO Make everything below this line take less time to execute. (USE ARRAYLISTS)
		
		// Instantiate
		ReadAndWriteTXTLib helper = new ReadAndWriteTXTLib(filePath);
		String[] columnNames = { "Step Number", "Value", "Package", "Skip Step", "X-Orient", "Y-Orient", "Head#", "Feeder#", "Angle-Orient",
				"MountHeight-Comp.", "Comment", "Training Point" };
		FileSaver saver = new FileSaver(newPath, ",", ";");
		int[] allowableDataFormatPerColumn = { 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 2 };// 0-Only Allows Numbers, 1-Doesn't Matter, 2-Only Charachters
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
		// Retrieve Line Data
		List<String[]> data = new ArrayList<String[]>(helper.countLines());
		for (int i = 1; i <= helper.countLines(); i++) {
			data.add(helper.readLine(i, ",", new String[] { ";" }));
		}
		// Correct Data
		for(String[] array: data) {
			int i = 1;
			// Correct Step Numbers
			String lineNum = "000" + String.valueOf(i);
			lineNum = lineNum.substring(lineNum.length() - 4);
			array[0] = lineNum;
			//Correct Number Values
			array[3] = String.valueOf(formatFive.format(Double.parseDouble(array[3]) * 100));
			array[4] = String.valueOf(formatFive.format(Double.parseDouble(array[4]) * 100));
			array[5] = String.valueOf(formatFive.format(Double.parseDouble(array[5]) * 100));
			i++;
		}
		for(String[] i: data)
			for(String b: i)
				System.out.println(b);
		// Insert default values
		
		
		
		
		//TablePanel table = new TablePanel(saver, columnNames, data, allowableDataFormatPerColumn);
		//this.add(table);
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
}
