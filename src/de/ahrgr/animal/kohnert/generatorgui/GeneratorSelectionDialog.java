/*
 * Created on 21.04.2005
 */
package de.ahrgr.animal.kohnert.generatorgui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;


/**
 * @author ek
 */
public class GeneratorSelectionDialog extends JDialog 
    implements ActionListener{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8184930958671834952L;
		protected JList<String> generatorList;
    protected int result = 0;
    
    public GeneratorSelectionDialog(String[] generators) {
        generatorList = new JList<String>(generators);
        Container p = getContentPane();
        p.setLayout(new BorderLayout());
        p.add(generatorList, BorderLayout.CENTER);
        p.add(new JLabel("Which animation type do you want to generate?"),
                BorderLayout.NORTH);
        JButton btn_ok = new JButton("Okay");
        btn_ok.setMnemonic(KeyEvent.VK_O);
        btn_ok.setActionCommand("ok");
        btn_ok.addActionListener(this);
        JButton btn_cancel = new JButton("Cancel");
        btn_cancel.setMnemonic(KeyEvent.VK_C);
        btn_cancel.setActionCommand("cancel");
        btn_cancel.addActionListener(this);
        JPanel pn = new JPanel();
        pn.setLayout(new GridLayout(1, 2));
        pn.add(btn_cancel);
        pn.add(btn_ok);
        p.add(pn, BorderLayout.SOUTH);
        generatorList.setSelectedIndex(0);
        setModal(true);
        setResizable(false);
        setTitle("New Animation");        
        pack();
    }
    
    public int showModal() {
        setVisible(true);        
        return result;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        result = generatorList.getSelectedIndex();
        if(arg0.getActionCommand().equals("cancel")) result = -1;
        dispose();
    }

}
