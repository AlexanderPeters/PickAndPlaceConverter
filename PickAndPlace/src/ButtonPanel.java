/*
 * Copyright 2017 (C) <GP Instruments>
 * 
 * Created on : 20-7-17
 * Author     : Alexander Peters
 *
 *-----------------------------------------------------------------------------
 * Revision History (Release 1.0.0.0)
 */

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JPanel;

// Creates a JPanel capable of holding up to 5 buttons
public class ButtonPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private String[] names;

	public ButtonPanel(String[] names, String[] locations) {
		this.names = names;
		if (this.names.length == locations.length && locations.length < 6)
			for (int i = 0; i < this.names.length; i++)
				this.add(new JButton(names[i]), locations[i]);
		else
			System.out.println("Error Array Lengths Do Not Match! Or Too many buttons added!" + Thread.currentThread().getStackTrace());
	}
	
	public Component getButtonByName(String name) {
		for(int i = 0; i < names.length; i++)
			if(names[i].equals(name))
				return this.getComponent(i);
		return null;
	}
}
