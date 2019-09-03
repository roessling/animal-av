/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package generators.framework.wizard;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 *
 * @author Jerome
 */
public class BasicPropertiesPanel extends JPanel {

    private JLabel lblTitle = new JLabel("Title");
    private JLabel lblAlgorithmName = new JLabel("Algorithm Name");
    private JLabel lblAuthorName = new JLabel("Author Name");
    private JLabel lblGeneratorType = new JLabel("Generator Type");
    private JTextField jTextField1 = new JTextField();
    private JTextField jTextField2 = new JTextField();
    private JTextField jTextField3 = new JTextField();
    private JComboBox<String> cmboType = new JComboBox<String>();

     public BasicPropertiesPanel(){
        super(false);
        init();
        InitLayout();
    }

    public void init(){
        String[] typeOptions = { "BACKTRACKING", "COMPRESSION", "CRYPT",
        "DATA_STRUCTURE", "GRAPH", "GRAPHICS", "HARDWARE", "HASHING", 
        "MATHS", "MORE", "NETWORK", "SEARCH", "SORT", "TREE"};
        cmboType = new JComboBox<String>(typeOptions);
     }

    public String GetTitle(){
        return jTextField1.getText();
    }

    public String GetAlgorithmName(){
        return jTextField2.getText();
    }

    public String GetAuthorName(){
        return jTextField3.getText();
    }

    public String GetAlgorithmType(){
        return cmboType.getSelectedItem().toString();
    }

    public void InitLayout(){
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                    .addGroup(Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                            .addComponent(lblTitle)
                            .addComponent(lblAuthorName)
                            .addComponent(lblGeneratorType)
                            .addComponent(lblAlgorithmName))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                            .addComponent(jTextField1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                            .addComponent(jTextField3, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                            .addComponent(jTextField2, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                            .addComponent(cmboType, 0, 278, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                  .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitle))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAlgorithmName))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(lblAuthorName)
                    .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(cmboType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGeneratorType))
                .addContainerGap(176, Short.MAX_VALUE))
        );
    }
}
