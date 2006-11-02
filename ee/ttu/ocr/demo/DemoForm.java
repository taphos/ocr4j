package ee.ttu.ocr.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DemoForm {

    private static final String[] fontNames = new String[]{"Tahoma", "TimesRoman"};
    private static final int[] fontStyles = new int[]{Font.PLAIN, Font.ITALIC};

    private JPanel fontChoosePannel;
    private JComboBox fontNameComboBox;
    private JComboBox fontSizeComboBox;
    private JComboBox fontStyleComboBox;

    private ActionListener comboBoxListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            updateFont();
        }
    };

    private Font currentFont;

    public DemoForm() {
        updateFont();
        fontNameComboBox.addActionListener(comboBoxListener);
        fontStyleComboBox.addActionListener(comboBoxListener);
        fontSizeComboBox.addActionListener(comboBoxListener);
    }

    private void updateFont() {
        String fontName = fontNames[fontNameComboBox.getSelectedIndex()];
        int fontSize = Integer.parseInt(fontSizeComboBox.getSelectedItem().toString());
        int fontStyle = fontStyles[fontStyleComboBox.getSelectedIndex()];
        currentFont = new Font(fontName, fontStyle, fontSize);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("DemoForm");
        frame.setContentPane(new DemoForm().fontChoosePannel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
