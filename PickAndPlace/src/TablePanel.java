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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class TablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTable table;
	private FileSaver saver;
	private String[][] data;

	public TablePanel(FileSaver saver, String[] columnNames, String[][] data, int[] allowableDataTypePerColumn) {
		this.saver = saver;
		this.data = data;
		table = new JTable(this.data, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		this.setLayout(new BorderLayout());
		this.add(table.getTableHeader(), BorderLayout.PAGE_START);
		this.add(scrollPane, BorderLayout.CENTER);
		table.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				try {
					if(GeneralLib.stringIsOnlyNumbersAndSigns(data[e.getLastRow()][e.getColumn()]) && allowableDataTypePerColumn[e.getColumn()] == 0)
						saveDataTableChanges();
					else if(allowableDataTypePerColumn[e.getColumn()] == 1)
						saveDataTableChanges();
					else
						new PopupErrorPanel("Invalid Data Entry \n The Current Cell Data Will Not Be Saved!", "DataType Error!");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	public void saveDataTableChanges() throws IOException {
		saver.saveData(data);
	}
	
	public Object[][] getTableData() {
		return data;
	}
}
