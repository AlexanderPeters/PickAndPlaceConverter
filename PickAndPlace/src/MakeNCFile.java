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
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MakeNCFile extends JFrame {
	private static final long serialVersionUID = 1L;

	public MakeNCFile() throws IOException {
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter textFilter = new FileNameExtensionFilter("KICAD files", "pos");
		String filePath = "";

		fc.setCurrentDirectory(new File("C:/Users"));
		fc.addChoosableFileFilter(textFilter);
		fc.setAcceptAllFileFilterUsed(false);

		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fc.getSelectedFile();
			filePath = selectedFile.getCanonicalPath();
		
			//Instantiate
			ReadAndWriteTXTLib helper = new ReadAndWriteTXTLib(filePath);
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
			this.setTitle("NC Data Editor");
			this.setLayout(new BorderLayout());
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.add(table, BorderLayout.CENTER);
			//window.add(new ImagePanel("C:/Users/Alexander the geek/Desktop/5e5d0e54ce306241f988890057e88c90.jpg"), BorderLayout.EAST);
			this.setSize(800, 400);
			this.setVisible(true);
		}
		else {
			new PopupErrorPanel("Error No File Selected \n Generation of NC File cannot proceed!", "Data Select Error!");
		}
	}
}

