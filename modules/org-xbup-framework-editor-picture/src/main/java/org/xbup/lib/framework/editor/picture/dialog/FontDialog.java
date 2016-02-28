/*
 * This file is based on the following source:
 * http://www.java2s.com/Code/Java/Tiny-Application/AdvancedFontChooserrevisedbypole.htm
 */
package org.xbup.lib.framework.editor.picture.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import static java.awt.font.TextAttribute.FAMILY;
import static java.awt.font.TextAttribute.POSTURE;
import static java.awt.font.TextAttribute.POSTURE_OBLIQUE;
import static java.awt.font.TextAttribute.SIZE;
import static java.awt.font.TextAttribute.STRIKETHROUGH;
import static java.awt.font.TextAttribute.STRIKETHROUGH_ON;
import static java.awt.font.TextAttribute.SUPERSCRIPT;
import static java.awt.font.TextAttribute.SUPERSCRIPT_SUB;
import static java.awt.font.TextAttribute.SUPERSCRIPT_SUPER;
import static java.awt.font.TextAttribute.UNDERLINE;
import static java.awt.font.TextAttribute.UNDERLINE_LOW_ONE_PIXEL;
import static java.awt.font.TextAttribute.WEIGHT;
import static java.awt.font.TextAttribute.WEIGHT_BOLD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * Font Chooser Dialog.
 *
 * Code is based on the following source:
 * http://www.java2s.com/Code/Java/Tiny-Application/AdvancedFontChooserrevisedbypole.htm
 *
 * @version 0.1.21 2012/04/04
 * @author ExBin Project (http://exbin.org)
 */
public class FontDialog extends JDialog {

    protected int dialogOption = JOptionPane.CLOSED_OPTION;
    private InputList fontNameInputList;
    private InputList fontSizeInputList;
    private MutableAttributeSet attributes;
    protected JCheckBox boldCheckBox = new JCheckBox("Bold");
    protected JCheckBox italicCheckBox = new JCheckBox("Italic");
    protected JCheckBox underlineCheckBox = new JCheckBox("Underline");
    protected JCheckBox strikethroughCheckBox = new JCheckBox("Strikethrough");
    protected JCheckBox subscriptCheckBox = new JCheckBox("Subscript");
    protected JCheckBox superscriptCheckBox = new JCheckBox("Superscript");
//    private ColorComboBox colorComboBox;
    private FontLabel previewLabel;
    public static String[] fontNames;
    public static String[] fontSizes;
    private static final String PREVIEW_TEXT = "Preview Font";

    public FontDialog(java.awt.Frame owner, boolean modal) {
        super(owner, modal);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        fontNames = ge.getAvailableFontFamilyNames();
        fontSizes = new String[]{"8", "9", "10", "11", "12", "14", "16",
            "18", "20", "22", "24", "26", "28", "36", "48", "72"};
        fontNameInputList = new InputList(fontNames, "Name:");
        fontSizeInputList = new InputList(fontSizes, "Size:");

        getContentPane().setLayout(
                new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        setTitle("Select Font");
        setLocationByPlatform(true);
        setModal(true);
        setName("Form");

        JPanel p = new JPanel(new GridLayout(1, 2, 10, 2));
        p.setBorder(new TitledBorder(new EtchedBorder(), "Font"));
        p.add(fontNameInputList);
        fontNameInputList.setDisplayedMnemonic('n');
        fontNameInputList.setToolTipText("Font name");

        p.add(fontSizeInputList);
        fontSizeInputList.setDisplayedMnemonic('s');
        fontSizeInputList.setToolTipText("Font size");
        getContentPane().add(p);

        p = new JPanel(new GridLayout(2, 3, 10, 5));
        p.setBorder(new TitledBorder(new EtchedBorder(), "Effects"));
        boldCheckBox.setMnemonic('b');
        boldCheckBox.setToolTipText("Bold font");
        p.add(boldCheckBox);

        italicCheckBox.setMnemonic('i');
        italicCheckBox.setToolTipText("Italic font");
        p.add(italicCheckBox);

        underlineCheckBox.setMnemonic('u');
        underlineCheckBox.setToolTipText("Underline font");
        p.add(underlineCheckBox);

        strikethroughCheckBox.setMnemonic('r');
        strikethroughCheckBox.setToolTipText("Strikethrough font");
        p.add(strikethroughCheckBox);

        subscriptCheckBox.setMnemonic('t');
        subscriptCheckBox.setToolTipText("Subscript font");
        p.add(subscriptCheckBox);

        superscriptCheckBox.setMnemonic('p');
        superscriptCheckBox.setToolTipText("Superscript font");
        p.add(superscriptCheckBox);
        getContentPane().add(p);

        getContentPane().add(Box.createVerticalStrut(5));
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(Box.createHorizontalStrut(10));
//        JLabel lbl = new JLabel("Color:");
//        lbl.setDisplayedMnemonic('c');
//        p.add(lbl);
        p.add(Box.createHorizontalStrut(20));
        /*
         colorComboBox = new ColorComboBox();
         lbl.setLabelFor(colorComboBox);
         colorComboBox.setToolTipText("Font color");
         ToolTipManager.sharedInstance().registerComponent(colorComboBox);
         p.add(colorComboBox);
         p.add(Box.createHorizontalStrut(10));
         getContentPane().add(p);
         */
        p = new JPanel(new BorderLayout());
        p.setBorder(new TitledBorder(new EtchedBorder(), "Preview"));
        previewLabel = new FontLabel(PREVIEW_TEXT);

        p.add(previewLabel, BorderLayout.CENTER);
        getContentPane().add(p);

        p = new JPanel(new FlowLayout());
        JPanel p1 = new JPanel(new GridLayout(1, 2, 10, 2));
        JButton btOK = new JButton("OK");
        btOK.setToolTipText("Save and exit");
        getRootPane().setDefaultButton(btOK);
        ActionListener actionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dialogOption = JOptionPane.OK_OPTION;
                dispose();
            }
        };
        btOK.addActionListener(actionListener);
        p1.add(btOK);

        JButton btCancel = new JButton("Cancel");
        btCancel.setToolTipText("Exit without save");
        actionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dialogOption = JOptionPane.CANCEL_OPTION;
                dispose();
            }
        };
        btCancel.addActionListener(actionListener);
        p1.add(btCancel);
        p.add(p1);
        getContentPane().add(p);

        pack();
//        setResizable(false);

        ListSelectionListener listSelectListener = new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                updatePreview();
            }
        };
        fontNameInputList.addListSelectionListener(listSelectListener);
        fontSizeInputList.addListSelectionListener(listSelectListener);

        actionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                updatePreview();
            }
        };
        boldCheckBox.addActionListener(actionListener);
        italicCheckBox.addActionListener(actionListener);
//        colorComboBox.addActionListener(actionListener);
        underlineCheckBox.addActionListener(actionListener);
        strikethroughCheckBox.addActionListener(actionListener);
        subscriptCheckBox.addActionListener(actionListener);
        superscriptCheckBox.addActionListener(actionListener);
    }

    public FontDialog(JFrame owner) {
        this(owner, false);
    }

    public void setAttributes(AttributeSet a) {
        attributes = new SimpleAttributeSet(a);
        String name = StyleConstants.getFontFamily(a);
        fontNameInputList.setSelected(name);
        int size = StyleConstants.getFontSize(a);
        fontSizeInputList.setSelectedInt(size);
        boldCheckBox.setSelected(StyleConstants.isBold(a));
        italicCheckBox.setSelected(StyleConstants.isItalic(a));
        underlineCheckBox.setSelected(StyleConstants.isUnderline(a));
        strikethroughCheckBox.setSelected(StyleConstants.isStrikeThrough(a));
        subscriptCheckBox.setSelected(StyleConstants.isSubscript(a));
        superscriptCheckBox.setSelected(StyleConstants.isSuperscript(a));
//        colorComboBox.setSelectedItem(StyleConstants.getForeground(a));
        updatePreview();
    }

    public AttributeSet getAttributes() {
        if (attributes == null) {
            return null;
        }
        StyleConstants.setFontFamily(attributes, fontNameInputList.getSelected());
        StyleConstants.setFontSize(attributes, fontSizeInputList.getSelectedInt());
        StyleConstants.setBold(attributes, boldCheckBox.isSelected());
        StyleConstants.setItalic(attributes, italicCheckBox.isSelected());
        StyleConstants.setUnderline(attributes, underlineCheckBox.isSelected());
        StyleConstants.setStrikeThrough(attributes, strikethroughCheckBox.isSelected());
        StyleConstants.setSubscript(attributes, subscriptCheckBox.isSelected());
        StyleConstants.setSuperscript(attributes, superscriptCheckBox.isSelected());
//        StyleConstants.setForeground(attributes, (Color) colorComboBox.getSelectedItem());
        return attributes;
    }

    public int getDialogOption() {
        return dialogOption;
    }

    public Font getStoredFont() {
        String name = fontNameInputList.getSelected();
        int size = fontSizeInputList.getSelectedInt();
        if (size <= 0) {
            return null;
        }

        Map<TextAttribute, Object> attribs = new HashMap<>();

        attribs.put(FAMILY, name);
        attribs.put(SIZE, (float) size);

        if (underlineCheckBox.isSelected()) {
            attribs.put(UNDERLINE, UNDERLINE_LOW_ONE_PIXEL);
        }
        if (strikethroughCheckBox.isSelected()) {
            attribs.put(STRIKETHROUGH, STRIKETHROUGH_ON);
        }

        if (boldCheckBox.isSelected()) {
            attribs.put(WEIGHT, WEIGHT_BOLD);
        }
        if (italicCheckBox.isSelected()) {
            attribs.put(POSTURE, POSTURE_OBLIQUE);
        }

        if (subscriptCheckBox.isSelected()) {
            attribs.put(SUPERSCRIPT, SUPERSCRIPT_SUB);
        }
        if (superscriptCheckBox.isSelected()) {
            attribs.put(SUPERSCRIPT, SUPERSCRIPT_SUPER);
        }

        return new Font(attribs);
    }

    public void setStoredFont(Font font) {
        Map<TextAttribute, ?> attribs = font.getAttributes();

        fontNameInputList.setSelected((String) attribs.get(FAMILY));

        Float fontSize = (Float) attribs.get(SIZE);
        if (fontSize != null) {
            fontSizeInputList.setSelectedInt((int) (float) fontSize);
        } else {
            fontSizeInputList.setSelectedInt(12);
        }

        underlineCheckBox.setSelected(UNDERLINE_LOW_ONE_PIXEL.equals(attribs.get(UNDERLINE)));

        strikethroughCheckBox.setSelected(STRIKETHROUGH_ON.equals(attribs.get(STRIKETHROUGH)));

        boldCheckBox.setSelected(WEIGHT_BOLD.equals(attribs.get(WEIGHT)));

        italicCheckBox.setSelected(POSTURE_OBLIQUE.equals(attribs.get(POSTURE)));

        subscriptCheckBox.setSelected(SUPERSCRIPT_SUB.equals(attribs.get(SUPERSCRIPT)));

        superscriptCheckBox.setSelected(SUPERSCRIPT_SUPER.equals(attribs.get(SUPERSCRIPT)));

        updatePreview();
    }

    protected void updatePreview() {

        StringBuilder previewText = new StringBuilder(PREVIEW_TEXT);

        // Using HTML to force JLabel manage natively unsupported attributes
        if (underlineCheckBox.isSelected() || strikethroughCheckBox.isSelected()) {
            previewText.insert(0, "<html>");
            previewText.append("</html>");
        }

        if (underlineCheckBox.isSelected()) {
            previewText.insert(6, "<u>");
            previewText.insert(previewText.length() - 7, "</u>");
        }
        if (strikethroughCheckBox.isSelected()) {
            previewText.insert(6, "<strike>");
            previewText.insert(previewText.length() - 7, "</strike>");
        }

        superscriptCheckBox.setEnabled(!subscriptCheckBox.isSelected());
        subscriptCheckBox.setEnabled(!superscriptCheckBox.isSelected());

        previewLabel.setText(previewText.toString());
        previewLabel.setFont(getStoredFont());

//        previewLabel.setForeground(getMyColor());
        previewLabel.repaint();
    }

    public static void main(String argv[]) {

        FontDialog dlg = new FontDialog(new JFrame());
        SimpleAttributeSet a = new SimpleAttributeSet();
        StyleConstants.setFontFamily(a, "Monospaced");
        StyleConstants.setFontSize(a, 12);
        dlg.setAttributes(a);
        dlg.setVisible(true);
    }
    /*
     public Color getMyColor() {
     return (Color) colorComboBox.getSelectedItem();
     }
     */
}

class InputList extends JPanel implements ListSelectionListener, ActionListener {

    protected JLabel label = new JLabel();
    protected JTextField textfield;
    protected JList list;
    protected JScrollPane scroll;

    public InputList(String[] data, String title) {
        setLayout(null);

        add(label);
        init(data);
    }

    public InputList(String title, int numCols) {
        setLayout(null);
        label = new OpelListLabel(title, JLabel.LEFT);
        add(label);
        init(numCols);
    }

    private void init(String[] data) {
        textfield = new OpelListText();
        textfield.addActionListener(this);
        label.setLabelFor(textfield);
        add(textfield);
        list = new OpelListList(data);
        list.setVisibleRowCount(4);
        list.addListSelectionListener(this);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scroll = new JScrollPane(list);
        add(scroll);
    }

    private void init(int numCols) {
        textfield = new OpelListText(numCols);
        textfield.addActionListener(this);
        label.setLabelFor(textfield);
        add(textfield);
        list = new OpelListList();
        list.setVisibleRowCount(4);
        list.addListSelectionListener(this);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scroll = new JScrollPane(list);
        add(scroll);
    }

    @Override
    public void setToolTipText(String text) {
        super.setToolTipText(text);
        label.setToolTipText(text);
        textfield.setToolTipText(text);
        list.setToolTipText(text);
    }

    public void setDisplayedMnemonic(char ch) {
        label.setDisplayedMnemonic(ch);
    }

    public void setSelected(String sel) {
        list.setSelectedValue(sel, true);
        textfield.setText(sel);
    }

    public String getSelected() {
        return textfield.getText();
    }

    public void setSelectedInt(int value) {
        setSelected(Integer.toString(value));
    }

    public int getSelectedInt() {
        try {
            return Integer.parseInt(getSelected());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        Object obj = list.getSelectedValue();
        if (obj != null) {
            textfield.setText(obj.toString());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ListModel model = list.getModel();
        String key = textfield.getText().toLowerCase();
        for (int k = 0; k < model.getSize(); k++) {
            String data = (String) model.getElementAt(k);
            if (data.toLowerCase().startsWith(key)) {
                list.setSelectedValue(data, true);
                break;
            }
        }
    }

    public void addListSelectionListener(ListSelectionListener lst) {
        list.addListSelectionListener(lst);
    }

    @Override
    public Dimension getPreferredSize() {
        Insets ins = getInsets();
        Dimension labelSize = label.getPreferredSize();
        Dimension textfieldSize = textfield.getPreferredSize();
        Dimension scrollPaneSize = scroll.getPreferredSize();
        int w = Math.max(Math.max(labelSize.width, textfieldSize.width),
                scrollPaneSize.width);
        int h = labelSize.height + textfieldSize.height + scrollPaneSize.height;
        return new Dimension(w + ins.left + ins.right, h + ins.top + ins.bottom);
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public void doLayout() {
        Insets ins = getInsets();
        Dimension size = getSize();
        int x = ins.left;
        int y = ins.top;
        int w = size.width - ins.left - ins.right;
        int h = size.height - ins.top - ins.bottom;

        Dimension labelSize = label.getPreferredSize();
        label.setBounds(x, y, w, labelSize.height);
        y += labelSize.height;
        Dimension textfieldSize = textfield.getPreferredSize();
        textfield.setBounds(x, y, w, textfieldSize.height);
        y += textfieldSize.height;
        scroll.setBounds(x, y, w, h - y);
    }

    public void appendResultSet(ResultSet results, int index,
            boolean toTitleCase) {
        textfield.setText("");
        DefaultListModel model = new DefaultListModel();
        try {
            while (results.next()) {
                String str = results.getString(index);
                if (toTitleCase) {
                    str = Character.toUpperCase(str.charAt(0)) + str.substring(1);
                }

                model.addElement(str);
            }
        } catch (SQLException ex) {
            System.err.println("appendResultSet: " + ex.toString());
        }
        list.setModel(model);
        if (model.getSize() > 0) {
            list.setSelectedIndex(0);
        }
    }

    class OpelListLabel extends JLabel {

        public OpelListLabel(String text, int alignment) {
            super(text, alignment);
        }

        @Override
        public AccessibleContext getAccessibleContext() {
            return InputList.this.getAccessibleContext();
        }
    }

    class OpelListText extends JTextField {

        public OpelListText() {
        }

        public OpelListText(int numCols) {
            super(numCols);
        }

        @Override
        public AccessibleContext getAccessibleContext() {
            return InputList.this.getAccessibleContext();
        }
    }

    class OpelListList extends JList {

        public OpelListList() {
        }

        public OpelListList(String[] data) {
            super(data);
        }

        @Override
        public AccessibleContext getAccessibleContext() {
            return InputList.this.getAccessibleContext();
        }
    }

    // Accessibility Support
    @Override
    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) {
            accessibleContext = new AccessibleOpenList();
        }
        return accessibleContext;
    }

    protected class AccessibleOpenList extends AccessibleJComponent {

        @Override
        public String getAccessibleName() {
            System.out.println("getAccessibleName: " + accessibleName);
            if (accessibleName != null) {
                return accessibleName;
            }
            return label.getText();
        }

        @Override
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.LIST;
        }
    }
}

class FontLabel extends JLabel {

    public FontLabel(String text) {
        super(text, JLabel.CENTER);
        setBackground(Color.white);
        setForeground(Color.black);
        setOpaque(true);
        setBorder(new LineBorder(Color.black));
        setPreferredSize(new Dimension(120, 40));
    }
}
/*
 class ColorComboBox extends JComboBox {

 public ColorComboBox() {
 int[] values = new int[]{0, 128, 192, 255};
 for (int r = 0; r < values.length; r++) {
 for (int g = 0; g < values.length; g++) {
 for (int b = 0; b < values.length; b++) {
 Color c = new Color(values[r], values[g], values[b]);
 addItem(c);
 }
 }
 }
 setRenderer(new ColorComboRenderer1());

 }

 class ColorComboRenderer1 extends JPanel implements ListCellRenderer {

 protected Color m_c = Color.black;

 public ColorComboRenderer1() {
 super();
 setBorder(new CompoundBorder(new MatteBorder(2, 10, 2, 10,
 Color.white), new LineBorder(Color.black)));
 }

 public Component getListCellRendererComponent(JList list, Object obj,
 int row, boolean sel, boolean hasFocus) {
 if (obj instanceof Color) {
 m_c = (Color) obj;
 }
 return this;
 }

 @Override
 public void paint(Graphics g) {
 setBackground(m_c);
 super.paint(g);
 }
 }

 }
 */
