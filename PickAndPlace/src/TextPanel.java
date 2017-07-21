/*
 * Copyright 2017 (C) <GP Instruments>
 * 
 * Created on : 20-7-17
 * Author     : Alexander Peters
 *
 *-----------------------------------------------------------------------------
 * Revision History (Release 1.0.0.0)
 */

import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public TextPanel(int textBoxLength, String defaultValue) {
		JTextField textField = new JTextField(textBoxLength);
		textField.setText(defaultValue);
		//TODO listeners
	}
}
