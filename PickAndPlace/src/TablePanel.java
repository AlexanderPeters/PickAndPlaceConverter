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
import java.awt.Component;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class TablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private myModel model;
	private JTable table;
	private FileSaver saver;
	private List<List<String>> data;

	public TablePanel(FileSaver saver, String[] columnNames, List<List<String>> data, int[] allowableDataTypePerColumn) {
		this.saver = saver;
		this.data = data;
		model = new myModel(this.data, columnNames, allowableDataTypePerColumn);
		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		
		table.setFillsViewportHeight(true);
		table.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				// The data currently located in the cell which just changed.
				String cellChanged = ((String) table.getModel().getValueAt(e.getLastRow(), e.getColumn()));
				
				try {
					if(allowableDataTypePerColumn[e.getColumn()] == 1)
						saveDataTableChanges(e.getLastRow());
					else if(RegexLib.stringOnlyContainsOneDigit0To9(cellChanged) && allowableDataTypePerColumn[e.getColumn()] == 2)
						saveDataTableChanges(e.getLastRow());
					else if(RegexLib.stringIsBetweenNegative99999andPositive99999(cellChanged) && allowableDataTypePerColumn[e.getColumn()] == 3)
						saveDataTableChanges(e.getLastRow());
					else if(RegexLib.stringOnlyContainsOneTwoOrThree(cellChanged) && allowableDataTypePerColumn[e.getColumn()] == 4)
						saveDataTableChanges(e.getLastRow());
					else if(RegexLib.stringIsBetween1and100(cellChanged) && allowableDataTypePerColumn[e.getColumn()] == 5)
						saveDataTableChanges(e.getLastRow());
					else if(RegexLib.stringIsBetween0and35999(cellChanged) && allowableDataTypePerColumn[e.getColumn()] == 6)
						saveDataTableChanges(e.getLastRow());
					else if(RegexLib.stringIsBetweenNegative999andPositive999(cellChanged) && allowableDataTypePerColumn[e.getColumn()] == 7)
						saveDataTableChanges(e.getLastRow());
					else if(RegexLib.stringIsLessThanOrEqualTo10CharactersInLength(cellChanged) && allowableDataTypePerColumn[e.getColumn()] == 8)
						saveDataTableChanges(e.getLastRow());
					else if(RegexLib.stringIsOnlyYorN(cellChanged) && allowableDataTypePerColumn[e.getColumn()] == 9)
						saveDataTableChanges(e.getLastRow());
					//else
						//new PopupErrorPanel("Invalid Data Entry \n The Current Cell Data Will Not Be Saved!", "DataType Error!");
				} catch (IOException e1) {
					e1.printStackTrace(); 
				}
			}
		});
		 
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		resizeColumnWidth(table);
		
		this.setLayout(new BorderLayout());
		this.add(table.getTableHeader(), BorderLayout.PAGE_START);
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	public void saveDataTableChanges(int rowChanged) throws IOException {
		saver.saveData(data, rowChanged);
	}
	
	public List<List<String>> getTableData() {
		return data;
	}
	
	public void insertRow(int row) {
		//Update Data
		data.add(row, data.get(row-1));
		model.updateRow(row, data.get(row));
				
		//Update Line Numbers
		for(int i = row; i < data.size(); i++) {
			String lineNum = "000" + String.valueOf(Integer.parseInt(data.get(i).get(0)) + 1);
			lineNum = lineNum.substring(lineNum.length() - 4);
			data.get(i).set(0, lineNum); 
			model.updateRow(i, data.get(i));
		}
		table.setModel(model);
	}
	
	public void deleteRow(int row) {
		//Delete data
		data.remove(row);
		
		//Update Line Numbers
		for(int i = row + 1; i < data.size(); i++) {
			data.get(i).set(0, String.valueOf(Integer.parseInt(data.get(i).get(0)) - 1)); 
			model.updateRow(row, data.get(row));
		}
		table.setModel(model);
	}
	
	private class myModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		int[] nonEditableColumns;
		
		myModel(List<List<String>> list, String[] columnNames, int[] nonEditableColumns) {
			this.nonEditableColumns = nonEditableColumns;
			this.setColumnIdentifiers(columnNames);
	        for (List<String> row : list) {
	            this.addRow(row.toArray());
	        }       
	    }
		
		@Override 
        public boolean isCellEditable(int row, int column) {
			if(nonEditableColumns[column] == 0) return false;
			return true;
        }
		
		public void updateRow(int row, List<String> rowData) {
			for(int i = 0; i < this.getColumnCount(); i++) {
				if(row ==  this.getRowCount()) {
					this.addRow(data.get(this.getRowCount()).toArray());// Add a buffer row
					this.setValueAt(rowData.get(i), row, i);// Update the last row in the table
					this.removeRow(this.getRowCount());// Remove the buffer row
				}
				else
					this.setValueAt(rowData.get(i), row, i);
			}
		}
    }

	public void resizeColumnWidth(JTable table) {
	    final TableColumnModel columnModel = table.getColumnModel();
	    for (int column = 0; column < table.getColumnCount(); column++) {
	        int width = 15; // Min width
	        for (int row = 0; row < table.getRowCount(); row++) {
	            TableCellRenderer renderer = table.getCellRenderer(row, column);
	            Component comp = table.prepareRenderer(renderer, row, column);
	            width = Math.max(comp.getPreferredSize().width + 10, width);
	        }
	        if(width > 300)
	            width = 300;
	        columnModel.getColumn(column).setPreferredWidth(width);
	    }
	}
}
