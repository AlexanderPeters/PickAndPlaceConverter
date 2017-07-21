/*
 * Copyright 2017 (C) <GP Instruments>
 * 
 * Created on : 20-7-17
 * Author     : Alexander Peters
 *
 *-----------------------------------------------------------------------------
 * Revision History (Release 1.0.0.0)
 */

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class MainGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	private JPanel mainPanel = new JPanel();
	private JMenuItem m_loadKiCadItem;
    private JMenuItem m_loadMachinePropertiesItem;
    private JMenuItem m_saveMachinePropertiesItem;
    private JMenuItem m_exitItem;
    private JMenuItem m_assignFeeders;
    private JMenuItem m_generateFiles;
    private JMenuItem m_printFeederInstructions;
	
	public static void main(String[] args) {
		MainGUI fr = new MainGUI();
		centerFrame(fr);
		fr.setVisible(true);

	}

	//Constructors to set up the options window
	public MainGUI() {		
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		setSize((int) (dim.width/1.25), (int) (dim.height/1.25));
		setTitle("Pick And Place Programmer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(mainPanel);
		
        m_loadKiCadItem = new JMenuItem("Load KiCAD File");
        m_loadMachinePropertiesItem = new JMenuItem("Load Machine Properties");
        m_saveMachinePropertiesItem = new JMenuItem("Save Machine Properties");
        m_exitItem = new JMenuItem("Exit");
        m_assignFeeders = new JMenuItem("Assign Feeders");
        m_generateFiles = new JMenuItem("Generate Files");
        m_printFeederInstructions = new JMenuItem("Print Feeder Instructions");
        
        m_loadKiCadItem.addActionListener(new OpenAction());
        
        JMenuBar menubar = new JMenuBar();  // Create new menu bar
            JMenu fileMenu = new JMenu("File"); // Create new menu
                menubar.add(fileMenu);      // Add menu to the menubar
                fileMenu.add(m_loadKiCadItem);     // Add menu item to the menu
                fileMenu.addSeparator();    // Add separator line to menu
                fileMenu.add(m_loadMachinePropertiesItem);
                fileMenu.addSeparator();
                fileMenu.add(m_saveMachinePropertiesItem);
                fileMenu.addSeparator();
                fileMenu.add(m_exitItem);
            JMenu assignFeedersMenu = new JMenu("Assign Feeders");
                menubar.add(assignFeedersMenu);
                assignFeedersMenu.add(m_assignFeeders);
            JMenu generateFilesMenu = new JMenu("Generate Files");
            	menubar.add(generateFilesMenu);
            	generateFilesMenu.add(m_generateFiles);
            JMenu printFeederInstructions = new JMenu("Print Feeder Instructions");
            	menubar.add(printFeederInstructions);
            	printFeederInstructions.add(m_printFeederInstructions);
           setJMenuBar(menubar);
	}
	//Method to allow the options window to be centered on the screen
	private static void centerFrame(JFrame fr) {
		int w = fr.getSize().width;
		int h = fr.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		fr.setLocation(x, y);
	}
	///////////////////////////////////////////////////////////// OpenAction
	class OpenAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(MainGUI.this, "Can't Open.");
			JOptionPane.showMessageDialog(MainGUI.this, "Are you sure you want to exit?");
			JOptionPane.showConfirmDialog(MainGUI.this, "Are you sure you want to exit?");
		}
	}
	
	///////////////////////////////////////////////////////////// QuitAction
	class QuitAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);  // Terminate the program.
		}
	}


	//Method which defines the functionality of all buttons and text boxes
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_exitItem) {
			dispose();
		}
	}
}
