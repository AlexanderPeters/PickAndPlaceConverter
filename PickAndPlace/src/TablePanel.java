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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class TablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private DefaultTableModel model;
	private JTable table;
	private FileSaver saver;
	private List<List<String>> data;

	public TablePanel(FileSaver saver, String[] columnNames, List<List<String>> data, int[] allowableDataTypePerColumn) {
		this.saver = saver;
		this.data = data;
		model = createModel(data, columnNames);
		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		
		table.setFillsViewportHeight(true);
		table.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				try {
					if(RegexLib.stringIsBetween0and9999(data.get(e.getLastRow()).get(e.getColumn())) && allowableDataTypePerColumn[e.getColumn()] == 0)
						saveDataTableChanges(e.getLastRow());
					else if(allowableDataTypePerColumn[e.getColumn()] == 1)
						saveDataTableChanges(e.getLastRow());
					else if(RegexLib.stringOnlyContainsOneDigit0To9(data.get(e.getLastRow()).get(e.getColumn())) && allowableDataTypePerColumn[e.getColumn()] == 2)
						saveDataTableChanges(e.getLastRow());
					else if(RegexLib.stringIsBetweenNegative99999andPositive99999(data.get(e.getLastRow()).get(e.getColumn())) && allowableDataTypePerColumn[e.getColumn()] == 3)
						saveDataTableChanges(e.getLastRow());
					else if(RegexLib.stringOnlyContainsOneTwoOrThree(data.get(e.getLastRow()).get(e.getColumn())) && allowableDataTypePerColumn[e.getColumn()] == 4)
						saveDataTableChanges(e.getLastRow());
					else if(RegexLib.stringIsBetween1and100(data.get(e.getLastRow()).get(e.getColumn())) && allowableDataTypePerColumn[e.getColumn()] == 5)
						saveDataTableChanges(e.getLastRow());
					else if(RegexLib.stringIsBetween0and35999(data.get(e.getLastRow()).get(e.getColumn())) && allowableDataTypePerColumn[e.getColumn()] == 6)
						saveDataTableChanges(e.getLastRow());
					else if(RegexLib.stringIsBetweenNegative999andPositive999(data.get(e.getLastRow()).get(e.getColumn())) && allowableDataTypePerColumn[e.getColumn()] == 7)
						saveDataTableChanges(e.getLastRow());
					else if(RegexLib.stringIsLessThanOrEqualTo10CharactersInLength(data.get(e.getLastRow()).get(e.getColumn())) && allowableDataTypePerColumn[e.getColumn()] == 8)
						saveDataTableChanges(e.getLastRow());
					else if(RegexLib.stringIsOnlyYorN(data.get(e.getLastRow()).get(e.getColumn())) && allowableDataTypePerColumn[e.getColumn()] == 9)
						saveDataTableChanges(e.getLastRow());
					else
						new PopupErrorPanel("Invalid Data Entry \n The Current Cell Data Will Not Be Saved!", "DataType Error!");
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
	
	public DefaultTableModel createModel(List<List<String>> list, String[] columnNames) {

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (List<String> row : list) {
            model.addRow(row.toArray());
        }

        return model;
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
