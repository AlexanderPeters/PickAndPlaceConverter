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
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

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
		this.setLayout(new BorderLayout());
		this.add(table.getTableHeader(), BorderLayout.PAGE_START);
		this.add(scrollPane, BorderLayout.CENTER);
		table.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				try {
					if(RegexLib.stringIsOnlyNumbersAndSigns(data.get(e.getLastRow()).get(e.getColumn())) && allowableDataTypePerColumn[e.getColumn()] == 0)
						saveDataTableChanges(e.getLastRow());
					else if(RegexLib.stringIsOnlyCharachters(data.get(e.getLastRow()).get(e.getColumn())) && allowableDataTypePerColumn[e.getColumn()] == 2)
						saveDataTableChanges(e.getLastRow());
					else if(allowableDataTypePerColumn[e.getColumn()] == 1)
						saveDataTableChanges(e.getLastRow());
					else
						new PopupErrorPanel("Invalid Data Entry \n The Current Cell Data Will Not Be Saved!", "DataType Error!");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
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

}
