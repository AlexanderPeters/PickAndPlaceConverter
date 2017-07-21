/*
 * Copyright 2017 (C) <GP Instruments>
 * 
 * Created on : 20-7-17
 * Author     : Alexander Peters
 *
 *-----------------------------------------------------------------------------
 * Revision History (Release 1.0.0.0)
 */

import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class SliderPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public void addSlider(int start, int end, int tickSpacing, boolean paintTicks, boolean paintLabels) {
		JSlider slider = new JSlider();
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(start), new JLabel(new Integer(start).toString()));
		labelTable.put(new Integer(end), new JLabel(new Integer(end).toString()));

		slider.setMajorTickSpacing(tickSpacing);
		slider.setPaintTicks(paintTicks);
		slider.setLabelTable(labelTable);
		slider.setPaintLabels(paintLabels);
		
		this.add(slider);
		//TODO listeners

	}
}
