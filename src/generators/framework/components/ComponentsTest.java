/*
 * Created on 05.11.2004 by T. Ackermann
 */

//Package name added by Eike Kohnert
package generators.framework.components;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * ComponentsTest is a test Application for the new Components
 *
 * @author T. Ackermann
 */
public class ComponentsTest {

    /** DOCUMENT ME! */
    static MatrixInputTable matrix;

    /** DOCUMENT ME! */
    static MatrixInputTable matrix2;

    /** DOCUMENT ME! */
    static ColorChooserComboBox colorChooser;

    /** DOCUMENT ME! */
    static FontChooserComboBox fontChooser;

    /** DOCUMENT ME! */
    static JButton button1;

    /** DOCUMENT ME! */
    static JButton button2;

    /** DOCUMENT ME! */
    static JButton button3;

    /** DOCUMENT ME! */
    static JButton button4;

    /**
     * main shows the Test Frame
     *
     * @param args Shell-Arguments
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test the Components");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel listPane = new JPanel();

        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));

        int[][] newValues = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 },
        		{ 13, 14, 15, -123 } };

        matrix = new MatrixInputTable(newValues);

        int[][] newValues2 = { { 28, 7, 19, 82 }, { 0, 8, 15, -99999 },
				{ 4, 7, 1, 1 }, { 17, -123, -12321, -234 } };

        matrix2 = new MatrixInputTable(newValues2);

        colorChooser = new ColorChooserComboBox("gold");
        fontChooser = new FontChooserComboBox("Monospaced");

        listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        listPane.add(new JLabel("MatrixInputTable:"));
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));
        listPane.add(matrix);

        /*
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));

        listPane.add(new JLabel("MatrixInputTable (2):"));
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));
        listPane.add(matrix2);
        */

        listPane.add(Box.createRigidArea(new Dimension(0, 5)));

        JPanel comboPane = new JPanel();
        GridLayout gridLayout = new GridLayout(2, 2);

        gridLayout.setHgap(10);
        gridLayout.setVgap(10);
        comboPane.setLayout(gridLayout);
        comboPane.add(new JLabel("ColorChooserComboBox:"));
        comboPane.add(colorChooser);
        comboPane.add(new JLabel("FontChooserComboBox:"));
        comboPane.add(fontChooser);

        listPane.add(comboPane);

        listPane.add(Box.createRigidArea(new Dimension(0, 5)));

        JPanel buttonPane = new JPanel();

        buttonPane.setLayout(gridLayout);
        button1 = new JButton("MatrixInputTable.getValues");
        //button2 = new JButton("ArrayInputTable.getValues");
        button3 = new JButton("ColorChooser.getColorSelectedAsString");
        button4 = new JButton("FontChooser.getFontSelected");

        button1.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int[][] data = matrix.getIntMatrixValues();
                    for (int i=0; i < matrix.getNumberOfRows(); i++) {
                        for (int j=0; j < matrix.getNumberOfColumns(); j++) {
                            System.out.print(data[i][j] + "\t");
                        }
                        System.out.print("\n");
                    }
                }
            });
        
        /*
        button2.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("not implemented yet");
                    }
                });
        */
        
        button3.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(colorChooser.getColorSelectedAsString());
                    }
                });
        
        button4.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(fontChooser.getFontSelected());
                    }
                });

        buttonPane.add(button1);
        //buttonPane.add(button2);
        buttonPane.add(button3);
        buttonPane.add(button4);

        listPane.add(buttonPane);

        frame.getContentPane().add(listPane);

        frame.pack();
        frame.setVisible(true);
    }
}
