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
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public ImagePanel(String imagePath) {
		ImageIcon image = new ImageIcon(imagePath);
		JLabel label = new JLabel("", image, JLabel.CENTER);
		this.setLayout(new BorderLayout());
		this.add( label, BorderLayout.CENTER );
		//TODO image scaling
	}
}
