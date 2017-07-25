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
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	private JPanel titleBar = new JPanel();
    private JPanel mainPanel = new JPanel();
	private JMenuItem m_loadKiCadItem;
    private JMenuItem m_loadMachinePropertiesItem;
    private JMenuItem m_saveMachinePropertiesItem;
    private JMenuItem m_exitItem;
    private JMenuItem m_assignFeeders;
    private JMenuItem m_generateNCFile;
    private JMenuItem m_printFeederInstructions;
    private static String filePath = "";

	public static void main(String[] args) {
		MainGUI fr = new MainGUI();
		centerFrame(fr);
		fr.setVisible(true);

	}

	// Constructors to set up the options window
	public MainGUI() {		
		BorderLayout bLayout = new BorderLayout();// Main Panel
		mainPanel.setLayout(bLayout);
		GroupLayout layout = new GroupLayout(titleBar);// Title Panel
		titleBar.setLayout(layout);
		mainPanel.add(titleBar, BorderLayout.NORTH);//Add Title Panel to Main Panel
		
		setSize((int) (dim.width/1.25), (int) (dim.height/1.25));
		setTitle("Pick And Place Programmer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(mainPanel);
		
        m_loadKiCadItem = new JMenuItem("Load KiCAD File");
        m_loadMachinePropertiesItem = new JMenuItem("Load Machine Properties");
        m_saveMachinePropertiesItem = new JMenuItem("Save Machine Properties");
        m_exitItem = new JMenuItem("Exit");
        m_assignFeeders = new JMenuItem("Assign Feeders");
        m_generateNCFile = new JMenuItem("Generate_NCFile");
        m_printFeederInstructions = new JMenuItem("Print Feeder Instructions");
        
        m_loadKiCadItem.addActionListener(new OpenAction());
        m_generateNCFile.addActionListener(new OpenAction());
        
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
            JButton assignFeedersMenu = new JButton("Assign Feeders");
                menubar.add(assignFeedersMenu);
                assignFeedersMenu.add(m_assignFeeders);
            JButton generateFilesMenu = new JButton("Generate Files");
            	menubar.add(generateFilesMenu);
            	generateFilesMenu.add(m_generateNCFile);
            JButton printFeederInstructions = new JButton("Print Feeder Instructions");
            	menubar.add(printFeederInstructions);
            	printFeederInstructions.add(m_printFeederInstructions);
           setJMenuBar(menubar);
	}
	
	// Adds a Component to the main panel.
	private void addComponent(Component comp, String l) {
		if(!l.equals(BorderLayout.NORTH)) {
			mainPanel.add(comp, l);
			this.setVisible(false);
			this.setVisible(true);
		}
		
		else System.out.println("Illegal use of this method!");		
	}
	
	// Method to allow the options window to be centered on the screen
	private static void centerFrame(JFrame fr) {
		int w = fr.getSize().width;
		int h = fr.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		fr.setLocation(x, y);
	}
	
	///////////////////////////////////////////////////////////// OpenAction
	private class OpenAction implements ActionListener {
		JButton b;
		MakeNCFile maker;
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == m_exitItem) {
				dispose();
			}
			else if(e.getSource() == m_loadKiCadItem) {
				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter textFilter = new FileNameExtensionFilter("KICAD files", "pos");

				fc.setCurrentDirectory(new File("C:/Users"));
				fc.addChoosableFileFilter(textFilter);
				fc.setAcceptAllFileFilterUsed(false);
				JFrame container = new JFrame();
				container.setTitle("Please select a KiCad file.");
				container.setVisible(false);
				
				if (fc.showOpenDialog(container) == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fc.getSelectedFile();
					container.dispose();
					try {
						filePath = selectedFile.getCanonicalPath();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			else if(e.getSource() == m_generateNCFile) {
				try {
					if (filePath.isEmpty())
						new PopupErrorPanel("Error No File Selected \n Creation of NC File cannot proceed!",
								"Data Select Error!");
					else {
						addComponent((maker = new MakeNCFile()), BorderLayout.CENTER);						
						ButtonPanel bp;
						addComponent((bp = new ButtonPanel(new String[] {"save"}, new String[] {BorderLayout.CENTER})), BorderLayout.EAST);
						b = (JButton) bp.getButtonByName("save");
						b.addActionListener(this);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else if(e.getSource() == b)
				maker.closing();
		}
	}
	
	///////////////////////////////////////////////////////////// QuitAction
	private class QuitAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);  // Terminate the program.
		}
	}
	
	public static String getFilePath() {
		return filePath;
	}
}
