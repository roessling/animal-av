package interactionsupport.patterns;
/*
 * Created on 19.09.2008 by Bjoern Dasbach <dasbach@rbg.informatik.tu-darmstadt.de>
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class CreateInteractionPatterns extends JPanel {
	private static final long serialVersionUID = 1L;
    
    public CreateInteractionPatterns() {
    	super(new GridLayout(1, 1));
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        PatternUpdate updater = new PatternUpdate("InteractionPatterns.xml");
        XMLPanel XMLPanel = new XMLPanel(updater);
        TFPanel tfPanel = new TFPanel(updater);
        FIBPanel fibPanel = new FIBPanel(updater);
        MCPanel mcPanel = new MCPanel(updater);
        MSPanel msPanel = new MSPanel(updater);
        DocuPanel docuPanel = new DocuPanel(updater);
        
        JComponent panel1 = tfPanel.getPanel();
        tabbedPane.addTab("True/False", panel1);
        JComponent panel2 = fibPanel.getPanel();
        tabbedPane.addTab("Fill in Blanks", panel2);
        JComponent panel3 = mcPanel.getPanel();
        tabbedPane.addTab("Multiple Choice", panel3);
        JComponent panel4 = msPanel.getPanel();
        tabbedPane.addTab("Multiple Selection", panel4);
        JComponent panel5 = docuPanel.getPanel();
        tabbedPane.addTab("Documentation Link", panel5);
        JComponent panel6 = XMLPanel.getPanel();
        tabbedPane.addTab("Pattern File", panel6);
        //Add the tabbed pane to this panel.
        add(tabbedPane);
        
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method is invoked from the
     * event dispatch thread.
     */
    static void createAndShowGUI() {
    	//Create and set up the window.
        JFrame frame = new JFrame("Interaction Pattern Creator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Add content to the window.
        frame.add(new CreateInteractionPatterns(), BorderLayout.CENTER);
        
        frame.setPreferredSize(new Dimension(640,480));
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}