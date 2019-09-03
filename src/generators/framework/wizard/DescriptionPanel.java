/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package generators.framework.wizard;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 *
 * @author Jerome
 */
public class DescriptionPanel extends JPanel{
    JLabel lblInstructions = new JLabel("Enter a description of the algorithm");
    JTextArea txtArea = new JTextArea();

    public DescriptionPanel(){
        super(false);
        InitLayout();
        txtArea.setColumns(30);
        txtArea.setRows(10);
        txtArea.setMaximumSize(txtArea.getSize());
    }

    public String getDescription(){
        return txtArea.getText();
    }

    public void InitLayout(){
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(lblInstructions)
                    .addComponent(txtArea, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInstructions)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(txtArea, GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                .addGap(61, 61, 61))
        );
    }
}
