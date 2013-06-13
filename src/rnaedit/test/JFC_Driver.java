package rnaedit.test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.logging.*;
import java.util.*;


public class JFC_Driver implements ListSelectionListener {
    
    
    /**
     * Left most list for selection
     */
    private JList list;

    private JSplitPane splitPane;

    private JPanel panelDemo1, panelDemo2;

    private JScrollPane listScrollPane, pictureScrollPane;
    private StateContext stateContext = new StateContext();
    private State[] panelArray = { new StatePanel1() };
    
	
    private static Logger logger = Logger.getLogger("JFC_Driver");

	public JFC_Driver() {

		//Create the list of images and put it in a scroll pane
		String[] items =
			{
				"BorderLayout",
				"BoxLayout",
				"CardLayout",
				"FlowLayout",
				"GridbagLayout",
				"GridLayout",
				"SpringLayout"
			};
 
		/**
                 * use string array to populate the JList
                 */
                list = new JList(items);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);//allow current Listener to listen
		listScrollPane = new JScrollPane(list);
		
		logger.info("[constructor] prior");
		stateContext = new StateContext();
		logger.info("[constructor] after");
		
		pictureScrollPane = new JScrollPane(stateContext.getPanel());

		//Create a split pane with the two scroll panes in it
		splitPane =
			new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,
				listScrollPane,
				pictureScrollPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		//Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(100, 50);
		listScrollPane.setMinimumSize(minimumSize);
		pictureScrollPane.setMinimumSize(minimumSize);

		//Provide a preferred size for the split pane
		splitPane.setPreferredSize(new Dimension(900, 500));
		
	}

	public JSplitPane getSplitPane() {
		return splitPane;
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;

		JList theList = (JList) e.getSource();
		if (theList.isSelectionEmpty()) {
			//
		} else {
			int index = theList.getSelectedIndex();
			logger.info("index=" + index);
			
			stateContext.setState(panelArray[index]);
			
			splitPane.setRightComponent(stateContext.getPanel());
			splitPane.setDividerLocation(150);
		}
	}

	public interface State {
		public JPanel getPanel();
	}

	public class StateContext {

		private State state;
        private JPanel panel;
        
		public StateContext() {
			setState(new StatePanel1()); 
		}
 
		public void setState(State state) {
		    this.state = state;	
		}
		
		public JPanel getPanel() {
			return this.state.getPanel();
		}
		
	}

	public class StatePanel1 extends JPanel implements State {
		JPanel panel1;
		public StatePanel1() {
			logger.info("[StatePanel1()]");
			panel1 = new JPanel();
			panel1.setLayout(new GridLayout(0, 1));
//			panel1.add(new TestPanelBorder("Test"));
		}
		public JPanel getPanel() {
			return panel1;
		}
	}
	
	
	public static void main(String s[]) {
		JFrame frame = new JFrame("JFC_Driver");

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

                
		JFC_Driver JFC_Driver = new JFC_Driver();
                
		frame.getContentPane().add(JFC_Driver.getSplitPane());
		frame.pack();
		frame.setVisible(true);
	}
}