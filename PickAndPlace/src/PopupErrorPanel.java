/*
 * Copyright 2017 (C) <GP Instruments>
 * 
 * Created on : 20-7-17
 * Author     : Alexander Peters
 *
 *-----------------------------------------------------------------------------
 * Revision History (Release 1.0.0.0)
 */

import javax.swing.JOptionPane;

public class PopupErrorPanel extends JOptionPane{
	private static final long serialVersionUID = 1L;

	public PopupErrorPanel(String errorMessage, String errorType) {
		JOptionPane.showMessageDialog(this, errorMessage, errorType, JOptionPane.ERROR_MESSAGE);
	}
}
