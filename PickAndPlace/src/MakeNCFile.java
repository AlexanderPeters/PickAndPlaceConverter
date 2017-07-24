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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MakeNCFile extends JPanel {
	private static final long serialVersionUID = 1L;
	private String newPath;
	
	public MakeNCFile() throws IOException {
		String filePath = MainGUI.getFilePath();
		newPath = filePath.substring(0, filePath.length() - 4).concat("temp.txt");
		
		// Instantiate
		ReadAndWriteTXTLib helper = new ReadAndWriteTXTLib(filePath);
		String[] columnNames = { "Step Number", "Part Name", "Package", "X Orient", "YOrient", "ThetaOrient" };
		FileSaver saver = new FileSaver(newPath, ",", ";");
		int[] allowableDataFormatPerColumn = { 1, 1, 1, 0, 0, 0 };// 0-Only Allows Numbers, 1-Doesn't Matter
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
		String[][] data = new String[helper.countLines()][];
		for (int i = 1; i <= helper.countLines(); i++) {
			data[i - 1] = helper.readLine(i, ",", new String[] { ";" });
		}
			TablePanel table = new TablePanel(saver, columnNames, data, allowableDataFormatPerColumn);
		this.add(table);
	}
	
	
	public void closing() {
		String ncFilePath = "";
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter textFilter = new FileNameExtensionFilter("Pick and Place Files", "nc");

		fc.setCurrentDirectory(new File("C:/Users"));
		fc.addChoosableFileFilter(textFilter);
		fc.setAcceptAllFileFilterUsed(false);
		JFrame container = new JFrame();
		container.setTitle("Please select a NC file to save to or create a new one.");
		container.setVisible(true);
		
		if (fc.showOpenDialog(container) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fc.getSelectedFile();
			try {
				ncFilePath = selectedFile.getCanonicalPath();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(!ncFilePath.isEmpty()) {
			container.dispose();
			File ncFile = new File(ncFilePath);
			File tempFile = new File(newPath);
			tempFile.renameTo(ncFile);
			tempFile.delete();
		}
		else new PopupErrorPanel("Error No File Selected \n Saving of NC File cannot proceed!",
				"Data Select Error!");
	}
}
