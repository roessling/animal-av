/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generators.framework.wizard;

//import generators.framework.PropertiesGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.FileAlreadyExistsException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Jerome, Dominik Fischer
 */
public class AdditionalDataPanel extends JPanel implements ActionListener {

    private JButton jButton1;
    private JComboBox<String> jComboBox1 = new JComboBox<String>();
    private JComboBox<String> jComboBox2 = new JComboBox<String>();
    private JComboBox<String> jComboBox3;
    private JLabel jLabel1 = new JLabel("Content Locale");
    private JLabel jLabel2 = new JLabel("Output Language");
    private JLabel jLabel3 = new JLabel("File Extension");
    public String buttonText = "Export";
    public WizardPanel wizPanel;

    private JCheckBox jCheckBox1 = new JCheckBox("Extract primitive variables");
    private JCheckBox jCheckBox2 = new JCheckBox("Extract property variables");
    private JCheckBox jCheckBox3 = new JCheckBox("Permit source code disclosure", true);

    public AdditionalDataPanel(WizardPanel wizPanel) {
        super(false);
        this.wizPanel = wizPanel;
        jButton1 = new JButton(buttonText);
        jButton1.addActionListener(this);
        init();
        InitLayout();
    }

    public void init() {
        String[] contentOptions = {"ENGLISH", "GERMAN"}; // TODO
        jComboBox1 = new JComboBox<String>(contentOptions);

        String[] outputOptions = {"JAVA", "PSEUDO_CODE"}; // TODO
        jComboBox2 = new JComboBox<String>(outputOptions);

        String[] extensionOptions = {"asu", "aml", "asc", "ama"}; // TODO
        jComboBox3 = new JComboBox<String>(extensionOptions);
    }

    public String getContentLocale() {
        return jComboBox1.getSelectedItem().toString();
    }

    public String getOutput() {
        return jComboBox2.getSelectedItem().toString();
    }

    public String getFileExtension() {
        return jComboBox3.getSelectedItem().toString();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(buttonText)) {
          try {
            wizPanel.Export(false);
          } catch (FileAlreadyExistsException exception) {
            int ret = JOptionPane.showConfirmDialog(this,
                exception.getFile() + "\nalready exists. Are you "
                    + "sure that you want to overwrite it?", WizardFrame.APP_NAME,
                JOptionPane.YES_NO_OPTION);
            if (ret == JOptionPane.YES_OPTION)
              try {
                wizPanel.Export(true);
              } catch (FileAlreadyExistsException neverThrown) {
                // this never happens when Export is
                // invoked with a true overwrite parameter.
              }
          } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this,
                exception.getMessage(), WizardFrame.APP_NAME,
                JOptionPane.ERROR_MESSAGE);
          }
        }
    }

    public boolean ExtractPrims(){
        return  jCheckBox1.isSelected();
    }
    public boolean ExtractProps(){
      return  jCheckBox2.isSelected();
  }
    public boolean permitDisclosure(){
      return  jCheckBox3.isSelected();
  }

    public void InitLayout() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
            layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox3)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox2)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox3, 0, 297, Short.MAX_VALUE)
                            .addComponent(jComboBox2, 0, 297, Short.MAX_VALUE)
                            .addComponent(jComboBox1, 0, 297, Short.MAX_VALUE))
                        .addGap(41, 41, 41))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addContainerGap(317, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(129, Short.MAX_VALUE))
        );
    }
}
