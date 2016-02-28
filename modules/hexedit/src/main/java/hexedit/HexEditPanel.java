/*
 * Copyright (C) ExBin Project
 *
 * GNU General Public License (GPL)
 * --------------------------------
 * HexEditPanel is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License or (at your option) any later
 * version.  This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along this
 * application.  If not, see <http://www.gnu.org/licenses/>.
 */
package hexedit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;

/**
 * Hexadecimal Panel editor based on
 *
 * This panel is based on HexEdit2, see
 * org.xbup.tools.xbeditor.panels.hexedit.hexEdit.java Written by: Keith Fenske,
 * http://www.psc-consulting.ca/fenske/ Released: Monday, 27 October 2008
 * Copyrighted as: GNU General Public License (GPL) version 3 or latter
 *
 * @version 0.1.24 2015/01/16
 * @author ExBin Project (http://exbin.org)
 */
public class HexEditPanel extends javax.swing.JPanel {

    private HexEdit2 hexEdit;

    /**
     * Creates new form HexEditPanel
     */
    public HexEditPanel(JFrame mainFrame) {
        initComponents();

        hexEdit = new HexEdit2();

        String fileName;              // first parameter that isn't an option
        int i;                        // index variable
        boolean maximizeFlag;         // true if we maximize our main window
        int windowHeight, windowLeft, windowTop, windowWidth;
        // position and size for <mainFrame>
        String word;                  // one parameter from command line

        /* Initialize global variables that may be affected by options on the
         command line. */
        hexEdit.buttonFont = null;            // by default, don't use customized font
        hexEdit.clipString = null;            // no string copied to clipboard yet
        hexEdit.dumpWidth = hexEdit.DEFAULT_DUMP;     // default input bytes per dump line
        fileName = "";                // first parameter is name of a file to open
        hexEdit.fontName = "Monospaced";      // default font name for text area
        hexEdit.gotoDialog = null;            // explicitly declare dialog as "not defined"
        maximizeFlag = false;         // true if we maximize our main window
        hexEdit.mswinFlag = System.getProperty("os.name").startsWith("Windows");
        hexEdit.nibbleCount = 0;              // total number of 4-bit data nibbles (none)
        hexEdit.nibbleData = new HexEdit2Data(0); // allocate empty data object for nibbles
        hexEdit.overFlag = false;             // by default, keyboard input has insert mode
        hexEdit.searchDialog = null;          // explicitly declare dialog as "not defined"
        windowHeight = hexEdit.DEFAULT_HEIGHT; // default window position and size
        windowLeft = hexEdit.DEFAULT_LEFT;
        windowTop = hexEdit.DEFAULT_TOP;
        windowWidth = hexEdit.DEFAULT_WIDTH;

        /* Initialize number formatting styles. */
        hexEdit.formatComma = NumberFormat.getInstance(); // current locale
        hexEdit.formatComma.setGroupingUsed(true); // use commas or digit groups

        /* Initialize shared graphical objects. */
        hexEdit.fileChooser = new JFileChooser(); // create our shared file chooser
        hexEdit.fileChooser.resetChoosableFileFilters(); // remove any existing filters
        hexEdit.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        hexEdit.fileChooser.setMultiSelectionEnabled(false); // allow only one file
        hexEdit.userActions = new HexEdit2User(hexEdit); // create our shared action listener

        /* Create the graphical interface as a series of little panels inside
         bigger panels.  The intermediate panel names are of no lasting importance
         and hence are only numbered (panel1, panel2, etc). */

        /* Create a vertical box to stack buttons and options. */
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel1.add(Box.createVerticalStrut(15)); // extra space at panel top

        /* Create a horizontal panel to hold the action buttons. */
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));

        hexEdit.menuButton = new JButton("Edit Menu");
        hexEdit.menuButton.addActionListener(hexEdit.userActions);
        if (hexEdit.buttonFont != null) {
            hexEdit.menuButton.setFont(hexEdit.buttonFont);
        }
        hexEdit.menuButton.setMnemonic(KeyEvent.VK_M);
        hexEdit.menuButton.setToolTipText("Copy, delete, find, paste, replace, etc.");
        panel2.add(hexEdit.menuButton);

        hexEdit.openButton = new JButton("Open File...");
        hexEdit.openButton.addActionListener(hexEdit.userActions);
        if (hexEdit.buttonFont != null) {
            hexEdit.openButton.setFont(hexEdit.buttonFont);
        }
        hexEdit.openButton.setMnemonic(KeyEvent.VK_O);
        hexEdit.openButton.setToolTipText("Read data bytes from a file.");
        panel2.add(hexEdit.openButton);

        hexEdit.saveButton = new JButton("Save File...");
        hexEdit.saveButton.addActionListener(hexEdit.userActions);
        if (hexEdit.buttonFont != null) {
            hexEdit.saveButton.setFont(hexEdit.buttonFont);
        }
        hexEdit.saveButton.setMnemonic(KeyEvent.VK_S);
        hexEdit.saveButton.setToolTipText("Write data bytes to a file.");
        panel2.add(hexEdit.saveButton);

        hexEdit.exitButton = new JButton("Exit");
        hexEdit.exitButton.addActionListener(hexEdit.userActions);
        if (hexEdit.buttonFont != null) {
            hexEdit.exitButton.setFont(hexEdit.buttonFont);
        }
        hexEdit.exitButton.setMnemonic(KeyEvent.VK_X);
        hexEdit.exitButton.setToolTipText("Close this program.");
        panel2.add(hexEdit.exitButton);

        panel1.add(panel2);
        panel1.add(Box.createVerticalStrut(13));

        /* These are the individual menu items for <menuPopup> which is invoked by
         <menuButton> or a right mouse click.  They are assembled into a real menu
         later by the showEditMenu() method. */
        hexEdit.menuCopyCursor = new JMenuItem("Copy Cursor Offset");
        hexEdit.menuCopyCursor.addActionListener(hexEdit.userActions);
        if (hexEdit.buttonFont != null) {
            hexEdit.menuCopyCursor.setFont(hexEdit.buttonFont);
        }

        hexEdit.menuCopyDump = new JMenuItem("Copy Dump");
        hexEdit.menuCopyDump.addActionListener(hexEdit.userActions);
        if (hexEdit.buttonFont != null) {
            hexEdit.menuCopyDump.setFont(hexEdit.buttonFont);
        }

        hexEdit.menuCopyHex = new JMenuItem("Copy Hex");
        hexEdit.menuCopyHex.addActionListener(hexEdit.userActions);
        if (hexEdit.buttonFont != null) {
            hexEdit.menuCopyHex.setFont(hexEdit.buttonFont);
        }

        hexEdit.menuCopyText = new JMenuItem("Copy Text");
        hexEdit.menuCopyText.addActionListener(hexEdit.userActions);
        if (hexEdit.buttonFont != null) {
            hexEdit.menuCopyText.setFont(hexEdit.buttonFont);
        }

        hexEdit.menuDelete = new JMenuItem("Delete");
        hexEdit.menuDelete.addActionListener(hexEdit.userActions);
        if (hexEdit.buttonFont != null) {
            hexEdit.menuDelete.setFont(hexEdit.buttonFont);
        }
        hexEdit.menuDelete.setMnemonic(KeyEvent.VK_D);

        hexEdit.menuFind = new JMenuItem("Find...");
        hexEdit.menuFind.addActionListener(hexEdit.userActions);
        if (hexEdit.buttonFont != null) {
            hexEdit.menuFind.setFont(hexEdit.buttonFont);
        }
        hexEdit.menuFind.setMnemonic(KeyEvent.VK_F);

        hexEdit.menuGotoOffset = new JMenuItem("Go To File Offset...");
        hexEdit.menuGotoOffset.addActionListener(hexEdit.userActions);
        if (hexEdit.buttonFont != null) {
            hexEdit.menuGotoOffset.setFont(hexEdit.buttonFont);
        }
        hexEdit.menuGotoOffset.setMnemonic(KeyEvent.VK_G);

        hexEdit.menuNext = new JMenuItem("Find Next");
        hexEdit.menuNext.addActionListener(hexEdit.userActions);
        if (hexEdit.buttonFont != null) {
            hexEdit.menuNext.setFont(hexEdit.buttonFont);
        }
        hexEdit.menuNext.setMnemonic(KeyEvent.VK_N);

        hexEdit.menuPasteHex = new JMenuItem("Paste Hex");
        hexEdit.menuPasteHex.addActionListener(hexEdit.userActions);
        if (hexEdit.buttonFont != null) {
            hexEdit.menuPasteHex.setFont(hexEdit.buttonFont);
        }

        hexEdit.menuPasteText = new JMenuItem("Paste Text");
        hexEdit.menuPasteText.addActionListener(hexEdit.userActions);
        if (hexEdit.buttonFont != null) {
            hexEdit.menuPasteText.setFont(hexEdit.buttonFont);
        }

        hexEdit.menuReplace = new JMenuItem("Replace");
        hexEdit.menuReplace.addActionListener(hexEdit.userActions);
        if (hexEdit.buttonFont != null) {
            hexEdit.menuReplace.setFont(hexEdit.buttonFont);
        }
        hexEdit.menuReplace.setMnemonic(KeyEvent.VK_R);

        hexEdit.menuSelect = new JMenuItem("Select All");
        hexEdit.menuSelect.addActionListener(hexEdit.userActions);
        if (hexEdit.buttonFont != null) {
            hexEdit.menuSelect.setFont(hexEdit.buttonFont);
        }
        hexEdit.menuSelect.setMnemonic(KeyEvent.VK_A);

        /* Create a horizontal panel for options. */
        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

        hexEdit.fontNameDialog = new JComboBox(GraphicsEnvironment
                .getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        hexEdit.fontNameDialog.setEditable(false); // user must select one of our choices
        if (hexEdit.buttonFont != null) {
            hexEdit.fontNameDialog.setFont(hexEdit.buttonFont);
        }
        hexEdit.fontNameDialog.setSelectedItem(hexEdit.fontName); // select default font name
        hexEdit.fontNameDialog.setToolTipText("Font name for displayed text.");
        hexEdit.fontNameDialog.addActionListener(hexEdit.userActions);
        // do last so this doesn't fire early
        panel3.add(hexEdit.fontNameDialog);

        panel3.add(Box.createHorizontalStrut(30));

        hexEdit.dumpWidthDialog = new JComboBox(hexEdit.DUMP_WIDTHS);
        hexEdit.dumpWidthDialog.setEditable(false); // user must select one of our choices
        if (hexEdit.buttonFont != null) {
            hexEdit.dumpWidthDialog.setFont(hexEdit.buttonFont);
        }
        hexEdit.dumpWidthDialog.setSelectedItem(String.valueOf(hexEdit.dumpWidth));
        // selected item is our default size
        hexEdit.dumpWidthDialog.setToolTipText("Number of input bytes per dump line.");
        hexEdit.dumpWidthDialog.addActionListener(hexEdit.userActions);
        // do last so this doesn't fire early
        panel3.add(hexEdit.dumpWidthDialog);
        JLabel label1 = new JLabel("bytes per line");
        if (hexEdit.buttonFont != null) {
            label1.setFont(hexEdit.buttonFont);
        }
        panel3.add(label1);

        panel3.add(Box.createHorizontalStrut(20));

        hexEdit.overDialog = new JCheckBox("overwrite mode", hexEdit.overFlag);
        hexEdit.overDialog.addActionListener(hexEdit.userActions);
        if (hexEdit.buttonFont != null) {
            hexEdit.overDialog.setFont(hexEdit.buttonFont);
        }
        hexEdit.overDialog.setToolTipText("Select for overwrite, clear for insert mode.");
        panel3.add(hexEdit.overDialog);

        panel1.add(panel3);
        panel1.add(Box.createVerticalStrut(7));

        /* Put above boxed options in a panel that is centered horizontally. */
        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
//        panel4.add(panel1);

        /* Create a text area to display the hex dump and to accept user input.  To
         the right of that will be a vertical scroll bar that we control. */
        hexEdit.textPanel = new HexEdit2Text(hexEdit); // create display as special JPanel
        hexEdit.textPanel.setFocusable(true); // allow keyboard focus for dump display
        hexEdit.textPanel.setPreferredSize(new Dimension(panel4.getPreferredSize().width,
                (4 * panel4.getPreferredSize().height))); // 4 times button/option height

        hexEdit.textScroll = new JScrollBar(JScrollBar.VERTICAL, 0, 1, 0, 1);
        hexEdit.textScroll.addMouseWheelListener((MouseWheelListener) hexEdit.textPanel);
        hexEdit.textScroll.setEnabled(true);  // scroll bar always present, always enabled
        hexEdit.textScroll.setFocusable(true); // allow keyboard focus for scroll bar
        hexEdit.textScroll.getModel().addChangeListener((ChangeListener) hexEdit.textPanel);

        /* Create the main window frame for this application.  Stack buttons and
         options on top of the output text area.  Keep the display text in the
         center so that it expands horizontally and vertically. */
        hexEdit.mainFrame = mainFrame;
        Container panel6 = this; //mainFrame.getContentPane(); // where content meets frame
        panel6.setLayout(new BorderLayout(5, 5));
        panel6.add(panel4, BorderLayout.NORTH); // buttons and options
        panel6.add(hexEdit.textPanel, BorderLayout.CENTER); // our panel for dump display
        panel6.add(hexEdit.textScroll, BorderLayout.EAST); // scroll bar for dump display
/*
         mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         mainFrame.setLocation(windowLeft, windowTop); // normal top-left corner
         if ((windowHeight < MIN_FRAME) || (windowWidth < MIN_FRAME))
         mainFrame.pack();           // do component layout with minimum size
         else                          // the user has given us a window size
         mainFrame.setSize(windowWidth, windowHeight); // size of normal window
         if (maximizeFlag) mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
         mainFrame.validate();         // recheck application window layout
         //  mainFrame.setVisible(true);   // and then show application window
         */
        /* If a file name was given on the command line, then open that file as our
         starting data.  Otherwise, use a text string with copyright information. */

        if (fileName.length() > 0) // was there a file name on the command line?
        {
            openFile(new File(fileName)); // yes, read data bytes from that file
        } else // no file name given
        {
            byte[] array = "".getBytes(); // use copyright string
            hexEdit.nibbleData = new HexEdit2Data(array.length * 2);
            // allocate data object to hold nibbles
            for (i = 0; i < array.length; i++) // do all bytes in copyright string
            {
                hexEdit.nibbleData.append((array[i] >> hexEdit.NIBBLE_SHIFT) & hexEdit.NIBBLE_MASK);
                // high-order nibble in byte
                hexEdit.nibbleData.append(array[i] & hexEdit.NIBBLE_MASK);
                // low-order nibble in byte
            }
            refreshDataSize();          // set to correct number of data nibbles
        }
        hexEdit.textPanel.beginFile();        // display file from the beginning

        /* Let the graphical interface run the application now. */
//        mainFrame.setVisible(true);   // and then show application window
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setName("Form"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
// ------------------------------------------------------------------------- //

    /*
     canWriteFile() method

     The caller gives us a Java File object.  We return true if it seems safe to
     write to this file.  That is, if the file doesn't exist and we can create a
     new file, or if the file exists and the user gives permission to replace it.
     */
    public boolean canWriteFile(File givenFile) {
        boolean result;               // status flag that we return to the caller

        if (givenFile.isDirectory()) // can't write to folders/directories
        {
            JOptionPane.showMessageDialog(hexEdit.mainFrame, (givenFile.getName()
                    + " is a directory or folder.\nPlease select a normal file."));
            result = false;             // don't try to open this "file" for writing
        } else if (givenFile.isHidden()) // won't write to hidden (protected) files
        {
            JOptionPane.showMessageDialog(hexEdit.mainFrame, (givenFile.getName()
                    + " is a hidden or protected file.\nPlease select a normal file."));
            result = false;
        } else if (givenFile.isFile() == false) // are we creating a new file?
        {
            result = true;              // assume we can create new file by this name
        } else if (givenFile.canWrite()) // file exists, but can we write to it?
        {
            result = (JOptionPane.showConfirmDialog(hexEdit.mainFrame, (givenFile.getName()
                    + " already exists.\nDo you want to replace this with a new file?"))
                    == JOptionPane.YES_OPTION);
        } else // if we can't write to an existing file
        {
            JOptionPane.showMessageDialog(hexEdit.mainFrame, (givenFile.getName()
                    + " is locked or write protected.\nCan't write to this file."));
            result = false;
        }
        return (result);               // give caller our best guess about writing

    } // end of canWriteFile() method


    /*
     charHexValue() method

     Return the integer value from 0 to 15 for one character that should be a
     hexadecimal digit.  A negative result means that the character is not a hex
     digit; see the HEX_IGNORE and HEX_INVALID constants.
     */
    public int charHexValue(char ch) {
        int result;                   // integer value of hexadecimal digit

        if ((ch >= '0') && (ch <= '9')) // is this a decimal digit?
        {
            result = ch - '0';
        } else if ((ch >= 'A') && (ch <= 'F')) // uppercase hexadecimal digit?
        {
            result = ch - 'A' + 10;
        } else if ((ch >= 'a') && (ch <= 'f')) // lowercase hexadecimal digit?
        {
            result = ch - 'a' + 10;
        } else if ((ch == ',') || (ch == '.') || (ch == ':')) // punctuation?
        {
            result = hexEdit.HEX_IGNORE;
        } else if (Character.isWhitespace(ch)) // blanks, spaces, tabs, newlines?
        {
            result = hexEdit.HEX_IGNORE;
        } else // illegal character
        {
            result = hexEdit.HEX_INVALID;
        }

        return (result);               // give caller whatever we could find

    } // end of charHexValue() method


    /*
     copyCursor() method

     Copy the current cursor nibble index to the clipboard as a hexadecimal string
     with the equivalent file byte offset.  As always, remember that there are two
     nibbles per byte!  This feature is normally only useful when combined with
     the "Go To File Offset" dialog box.
     */
    public void copyCursor() {
        String text;                  // text string in middle of hex conversion

        text = "00000000" + Integer.toHexString(hexEdit.textPanel.cursorDot / 2)
                .toUpperCase();             // current cursor nibble as hex byte offset
        setClipboard(text.substring(text.length() - hexEdit.OFFSET_DIGITS));
    }


    /*
     copyDump() method

     Copy the currently selected portion of the dump to the clipboard as a string
     with lines delimited by newline characters.  While from the user's point of
     view we are copying text that is already on the screen, we actually have to
     rebuild the dump for each line that has some part selected.
     */
    public void copyDump() {
        int beginIndex, endIndex, thisIndex; // nibble index variables
        int byteValue;                // byte value combined from two nibbles
        int i;                        // index variable
        StringBuffer lineBuffer;      // where we create each dump line
        int lineLength;               // total number of characters each dump line
        int lineNibbles;              // maximum number of hex digits per dump line
        int lineUsed;                 // number of nibbles placed on this line
        int nextHex, nextText;        // indexes for placing next nibble, byte
        StringBuffer result;          // string created from selected dump lines
        int shiftedOffset;            // copy of file offset that we manipulate

        beginIndex = Math.min(hexEdit.textPanel.cursorDot, hexEdit.textPanel.cursorMark);
        endIndex = Math.max(hexEdit.textPanel.cursorDot, hexEdit.textPanel.cursorMark);
        if (beginIndex < endIndex) // only if there is a selection
        {
            lineLength = hexEdit.OFFSET_DIGITS + (4 * hexEdit.dumpWidth) + 5;
            // number of printable chars each dump line
            lineBuffer = new StringBuffer(lineLength + 1);
            // allocate line buffer, ends with newline
            lineBuffer.setLength(lineLength + 1); // force the correct buffer length
            lineBuffer.setCharAt(lineLength, '\n'); // put newline that never changes
            lineNibbles = 2 * hexEdit.dumpWidth; // maximum number of nibbles per dump line
            lineUsed = -1;              // force initialization of a new dump line
            nextHex = nextText = -1;    // just to keep compiler happy
            result = new StringBuffer(); // start with an empty string buffer
            for (thisIndex = beginIndex; thisIndex < endIndex; thisIndex++) {
                /* If the current dump line is full, then copy it to our result. */

                if (lineUsed >= lineNibbles) // is the current dump line full?
                {
                    result.append(lineBuffer); // append each formatted line to result
                    lineUsed = -1;          // force initialization of a new dump line
                }

                /* If necessary, initialize the dump line with the file offset. */
                if (lineUsed < 0) // should we start a new dump line?
                {
                    for (i = 0; i < lineLength; i++) // clear entire line to spaces
                    {
                        lineBuffer.setCharAt(i, ' ');
                    }
                    lineBuffer.setCharAt((lineLength - hexEdit.dumpWidth - 2), hexEdit.MARKER_CHAR);
                    // insert left marker for ASCII text
                    lineBuffer.setCharAt((lineLength - 1), hexEdit.MARKER_CHAR); // right marker
                    shiftedOffset = (thisIndex / lineNibbles) * hexEdit.dumpWidth;
                    for (i = (hexEdit.OFFSET_DIGITS - 1); i >= 0; i--) // extract digits starting with low-order
                    {
                        lineBuffer.setCharAt(i, hexEdit.HEX_DIGITS[shiftedOffset & hexEdit.NIBBLE_MASK]);
                        // convert nibble to hex text digit
                        shiftedOffset = shiftedOffset >> hexEdit.NIBBLE_SHIFT;
                        // shift down next higher-order nibble
                    }
                    lineUsed = thisIndex % lineNibbles; // ignore leading unused digits
                    nextHex = lineUsed + (lineUsed / 2) + hexEdit.OFFSET_DIGITS + 2;
                    // where next hex digit goes
                    nextText = lineLength - hexEdit.dumpWidth - 1 + (lineUsed / 2);
                    // where next ASCII text goes
                }

                /* Place the hexadecimal digit for this nibble. */
                lineBuffer.setCharAt((nextHex++),
                        hexEdit.HEX_DIGITS[hexEdit.nibbleData.get(thisIndex)]);
                nextHex += thisIndex % 2; // insert extra space after second nibble
                lineUsed++;              // one more nibble placed on this dump line

                /* Place the ASCII text for a whole byte (two nibbles). */
                if ((thisIndex % 2) == 1) // for second nibble, construct full byte
                {
                    if ((thisIndex - 1) < beginIndex) // is first nibble in selection?
                    {
                        byteValue = hexEdit.REPLACE_CHAR; // incomplete byte means unprintable
                    } else // we have first and second nibble
                    {
                        byteValue = (hexEdit.nibbleData.get(thisIndex - 1) << hexEdit.NIBBLE_SHIFT)
                                | hexEdit.nibbleData.get(thisIndex); // construct byte from two nibbles
                        if ((byteValue < hexEdit.FIRST_CHAR) || (byteValue > hexEdit.LAST_CHAR)) {
                            byteValue = hexEdit.REPLACE_CHAR; // replace unprintable character
                        }
                    }
                    lineBuffer.setCharAt((nextText++), (char) byteValue); // show text
                } else if ((thisIndex + 1) >= endIndex) // first nibble, but alone?
                {
                    lineBuffer.setCharAt((nextText++), hexEdit.REPLACE_CHAR); // unprintable
                }
            }

            /* There is always a partial line after the loop completes. */
            result.append(lineBuffer);  // append last formatted line to result
            setClipboard(result.toString()); // and buffer becomes clipboard string
        }
    } // end of copyDump() method


    /*
     copyHex() method

     Copy the currently selected portion of our data to the clipboard as
     hexadecimal digits (that is, a string representing the hex values).
     */
    public void copyHex() {
        int beginIndex, endIndex, thisIndex; // nibble index variables
        StringBuffer result;          // string created from selected hex digits

        beginIndex = Math.min(hexEdit.textPanel.cursorDot, hexEdit.textPanel.cursorMark);
        endIndex = Math.max(hexEdit.textPanel.cursorDot, hexEdit.textPanel.cursorMark);
        if (beginIndex < endIndex) // only if there is a selection
        {
            result = new StringBuffer(); // start with an empty string buffer
            for (thisIndex = beginIndex; thisIndex < endIndex; thisIndex++) {
                result.append(hexEdit.HEX_DIGITS[hexEdit.nibbleData.get(thisIndex)]);
            }
            // convert binary nibble to hex character
            setClipboard(result.toString()); // and buffer becomes clipboard string
        }
    } // end of copyHex() method


    /*
     copyText() method

     Copy the currently selected portion of our nibble data to the clipboard as
     text (that is, a string).  To be consistent with pasteText(), first we create
     a byte array, then we convert the bytes to a string using the local system's
     default encoding.  No attempt is made to align the selected nibbles on a byte
     boundary; this may produce unexpected results.
     */
    public void copyText() {
        byte[] array;                 // array of bytes obtained from a string
        int beginIndex, endIndex, thisIndex; // nibble index variables
        int byteValue;                // for creating shifted sum of two nibbles
        char ch;                      // one character from decoded string
        boolean crFound;              // true if previous char was carriage return
        String decoded;               // intermediate copy of decoded string
        int i;                        // index variable
        int length;                   // length of decoded string in characters
        StringBuffer result;          // string created from selected hex digits

        beginIndex = Math.min(hexEdit.textPanel.cursorDot, hexEdit.textPanel.cursorMark);
        endIndex = Math.max(hexEdit.textPanel.cursorDot, hexEdit.textPanel.cursorMark);
        if (beginIndex < endIndex) // only if there is a selection
        {
            array = new byte[(endIndex - beginIndex + 1) / 2]; // round up byte size
            thisIndex = beginIndex;     // first nibble is high-order of first byte
            for (i = 0; i < array.length; i++) // create each byte from two nibbles
            {
                byteValue = hexEdit.nibbleData.get(thisIndex++) << hexEdit.NIBBLE_SHIFT;
                // can always get high-order nibble
                if (thisIndex < endIndex) // there may not be a low-order nibble
                {
                    byteValue |= hexEdit.nibbleData.get(thisIndex++);
                }
                array[i] = (byte) byteValue; // copy summed nibbles to byte array
            }

            /* The Windows clipboard (and possibly others) will truncate a string if
             certain characters such as nulls are found.  Recognize a single carriage
             return (CR), a single line feed (LF), or a CR/LF pair as being equivalent
             to a standard newline character (NL).  Note: the binary value of LF is
             the same as NL. */
            crFound = false;            // previous character was not carriage return
            decoded = new String(array); // decode byte array, default character set
            length = decoded.length();  // get number of characters in decoded string
            result = new StringBuffer(length); // assume maximum capacity for buffer
            for (i = 0; i < length; i++) // check all characters in decoded string
            {
                ch = decoded.charAt(i);   // get one character from decoded string
                if (crFound && (ch != '\n')) // carriage return without line feed?
                {
                    result.append('\n');    // yes, previous CR becomes newline character
                }
                crFound = false;          // previous character is no longer important
                if (ch == '\n') // accept newline character (DOS LF, UNIX NL)
                {
                    result.append(ch);
                } else if (ch == '\r') // delay action for carriage return (CR)
                {
                    crFound = true;
                } else if (ch == '\t') // accept horizontal tab character (HT)
                {
                    result.append(ch);
                } else if ((ch <= 0x1F) || ((ch >= 0x7F) && (ch <= 0x9F))) { /* ignore all other control codes */ } else {
                    result.append(ch);      // otherwise, this character is acceptable
                }
            }
            if (crFound) // if last character was a carriage return
            {
                result.append('\n');      // then end copied string with a newline
            }
            setClipboard(result.toString()); // and buffer becomes clipboard string
        }
    } // end of copyText() method


    /*
     deleteSelected() method

     Delete (remove) hex nibbles or text bytes as selected by the user.  From our
     point of view, we don't need to know what was selected (hex or text), since
     we always delete all nibbles from the start of the selection up to but not
     including the end of the selection.
     */
    public void deleteSelected() {
        int beginIndex, endIndex, thisIndex; // nibble index variables

        beginIndex = Math.min(hexEdit.textPanel.cursorDot, hexEdit.textPanel.cursorMark);
        endIndex = Math.max(hexEdit.textPanel.cursorDot, hexEdit.textPanel.cursorMark);
        if (beginIndex < endIndex) // only if there is a selection
        {
            for (thisIndex = beginIndex; thisIndex < endIndex; thisIndex++) {
                hexEdit.nibbleData.delete(beginIndex); // delete shuffles, always <beginIndex>
            }
            hexEdit.textPanel.cursorDot = hexEdit.textPanel.cursorMark = beginIndex;
            // selection is gone, reset cursor
            hexEdit.textPanel.limitCursorRange(); // refresh data size, enforce cursor range
            hexEdit.textPanel.makeVisible(hexEdit.textPanel.cursorDot);
            // make sure that user can see cursor
            hexEdit.textPanel.adjustScrollBar(); // adjust scroll bar to match new position
            hexEdit.textPanel.repaint();        // redraw text display as necessary
        }
    } // end of deleteSelected() method


    /*
     getClipboard() method

     Get the contents of the clipboard as a string.  For any errors, return an
     empty string.
     */
    public String getClipboard() {
        String result;                // our result as a string

        try // clipboard may not be available
        {
            result = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getContents(null).getTransferData(DataFlavor.stringFlavor);
        } catch (IllegalStateException ise) {
            result = "";
        } catch (IOException ioe) {
            result = "";
        } catch (UnsupportedFlavorException ufe) {
            result = "";
        }
        return (result);               // give caller whatever we could find

    } // end of getClipboard() method


    /*
     gotoFileOffset() method

     Take the user's input from the "Go To File Offset" dialog box and try to
     position the cursor at that byte offset from the beginning of the file.  We
     know the dialog box exists, because we can only get here from buttons and
     fields inside the dialog box.
     */
    public void gotoFileOffset() {
        char ch;                      // one character from input string
        int digits;                   // number of valid hex digits found
        int hexValue;                 // integer value of one hexadecimal digit
        int i;                        // index variable
        String input;                 // offset (input) string typed by user
        int length;                   // size of input string in characters
        long offset;                  // user's offset converted to binary

        /* Try to convert the user's input from a hexadecimal string to a binary
         integer value. */
        refreshDataSize();            // set to correct number of data nibbles
        digits = 0;                   // no valid hex digits found yet
        input = hexEdit.gotoOffsetText.getText(); // get user's string from dialog box
        length = input.length();      // get size of input string in characters
        offset = 0;                   // start offset from zero as we add digits
        for (i = 0; i < length; i++) // do all characters in the string
        {
            ch = input.charAt(i);       // get one character from input string
            hexValue = charHexValue(ch); // convert character to value of hex digit
            if (offset > hexEdit.nibbleCount) // crude (very crude) limit on upper range
            {
                digits = -1;              // our way of saying that hex string is bad
                break;                    // exit early from <for> loop
            } else if (hexValue >= 0) // was it a valid hexadecimal digit?
            {
                digits++;                // one more hexadecimal digit accepted
                offset = (offset << hexEdit.NIBBLE_SHIFT) | hexValue; // append digit to offset
            } else if (hexValue == hexEdit.HEX_IGNORE) // ignore spaces and punctuation?
            { /* do nothing */ } else // illegal character
            {
                digits = -1;              // another way of saying that input is bad
                break;                    // exit early from <for> loop
            }
        }

        /* Check that the resulting binary value is within actual file range. */
        if ((digits < 1) || (offset > (hexEdit.nibbleCount / 2))) {
            showGotoRange(hexEdit.nibbleCount / 2); // repeat message about range allowed
            return;                     // can't jump to an imaginary file offset!
        }

        /* Position the display to the correct file (and dump panel) offset, about
         1/3 of the way down from the top, if possible. */
        hexEdit.gotoStatus.setText(hexEdit.EMPTY_STATUS); // less confusing if clear status message
//    hexEdit.mainFrame.setVisible(true);   // bring main frame in front of dialog box
        hexEdit.textPanel.cursorDot = hexEdit.textPanel.cursorMark = (int) (offset * 2);
        // position cursor at user's exact offset
        hexEdit.textPanel.limitCursorRange(); // refresh data size, enforce cursor range
        hexEdit.textPanel.panelOffset = ((int) offset) - (hexEdit.textPanel.panelDumpWidth
                * (hexEdit.textPanel.panelRows / 3)); // approximate starting panel offset
        hexEdit.textPanel.adjustScrollBar();  // clean up offset and adjust scroll bars
        hexEdit.textPanel.repaint();          // redraw text display as necessary

    } // end of gotoFileOffset() method


    /*
     memoryError() method

     Produce a dialog box with a message when there is insufficient memory to
     complete an operation.
     */
    public void memoryError(String text) {
        JOptionPane.showMessageDialog(hexEdit.mainFrame,
                ("Not enough memory available for " + text + " operation."));
    }


    /*
     openFile() method

     Ask the user for an input file name and copy the contents of that file to our
     nibble data.
     */
    public void openFile(File givenFile) {
        File inputFile;               // user's selected input file
        long inputSize;               // total size of input file in bytes

        /* Clear the nibble counter so that the data looks empty, until after we
         finish opening a file.  This prevents the text display from throwing an
         exception while painting data that isn't there yet.  The nibble counter
         will be reset properly with a call to refreshDataSize(). */
        hexEdit.nibbleCount = 0;              // make data look empty, without losing data

        /* Ask the user for an input file name, if we weren't already given a file
         by our caller. */
        if (givenFile == null) {
            hexEdit.fileChooser.setDialogTitle("Open File...");
            if (hexEdit.fileChooser.showOpenDialog(hexEdit.mainFrame) != JFileChooser.APPROVE_OPTION) {
                refreshDataSize();        // bring back previous nibble data and size
                return;                   // user cancelled file selection dialog box
            }
            inputFile = hexEdit.fileChooser.getSelectedFile(); // get user's input file
        } else {
            inputFile = givenFile;      // caller gave us a file, so use that instead
        }
        /* Warn the user if the file is larger than what we are able to handle. */

        inputSize = inputFile.length(); // get total number of bytes for input file
        if (inputSize > 0x3FFF0000L) // just short of one gigabyte
        {
            JOptionPane.showMessageDialog(hexEdit.mainFrame,
                    ("This program can't open files larger than one gigabyte.\n"
                    + inputFile.getName() + " has " + hexEdit.formatComma.format(inputSize)
                    + " bytes."));
            refreshDataSize();          // bring back previous nibble data and size
            return;                     // we can't open this file, so give up
        } else if (inputSize > 99999999L) // just short of 100 megabytes
        {
            if (JOptionPane.showConfirmDialog(hexEdit.mainFrame,
                    ("Files larger than 100 megabytes may be slow.\n"
                    + inputFile.getName() + " has " + hexEdit.formatComma.format(inputSize)
                    + " bytes.\nDo you want to open this file anyway?"),
                    "Large File Warning", JOptionPane.YES_NO_CANCEL_OPTION)
                    != JOptionPane.YES_OPTION) {
                refreshDataSize();        // bring back previous nibble data and size
                return;                   // user doesn't want to open the new file
            }
        }

        try {
            loadFromStream(new FileInputStream(inputFile), inputSize);
        } catch (IOException ioe) // most likely I/O error is "file not found"
        {
            hexEdit.nibbleData = new HexEdit2Data(0); // substitute an empty data object
            JOptionPane.showMessageDialog(hexEdit.mainFrame,
                    ("Can't read from input file:\n" + ioe.getMessage()));
        } catch (OutOfMemoryError oome) // not enough memory to open the file
        {
            hexEdit.nibbleData = new HexEdit2Data(0); // substitute an empty data object
            JOptionPane.showMessageDialog(hexEdit.mainFrame,
                    ("Not enough memory to open this file.\n"
                    + inputFile.getName() + " has " + hexEdit.formatComma.format(inputSize)
                    + " bytes.\nTry increasing the Java heap size with the -Xmx option."));
        }

    } // end of openFile() method

    public void loadFromStream(InputStream inputStream, long inputSize) {
        byte[] buffer;                // input buffer (faster than byte-by-byte)
        int i;                        // index variable
        int length;                   // actual number of bytes read

        /* Read 8-bit data bytes from the input file and convert to pairs of 4-bit
         nibbles.  Since we are reading whole bytes, there is always an even number
         of nibbles. */
        try // catch file I/O errors, memory allocation
        {
            buffer = new byte[hexEdit.BUFFER_SIZE]; // allocate byte buffer for input
//      inputStream = ;
            // try to open input file
            hexEdit.nibbleData = new HexEdit2Data(2 * (int) inputSize);
            // allocate nibble data object from file size
            while ((length = inputStream.read(buffer, 0, hexEdit.BUFFER_SIZE)) > 0) {
                for (i = 0; i < length; i++) {
                    hexEdit.nibbleData.append((buffer[i] >> hexEdit.NIBBLE_SHIFT) & hexEdit.NIBBLE_MASK);
                    // high-order nibble in byte
                    hexEdit.nibbleData.append(buffer[i] & hexEdit.NIBBLE_MASK);
                    // low-order nibble in byte
                }
            }
            inputStream.close();        // try to close input file
        } catch (IOException ioe) // most likely I/O error is "file not found"
        {
            hexEdit.nibbleData = new HexEdit2Data(0); // substitute an empty data object
            JOptionPane.showMessageDialog(hexEdit.mainFrame,
                    ("Can't read from input file:\n" + ioe.getMessage()));
        }
        refreshDataSize();            // set to correct number of data nibbles
    }
    /*
     pasteHex() method

     Paste the clipboard as hex digits at the cursor location.  Complain to the
     user if there is anything except hex digits, space, or common punctuation.
     */

    public void pasteHex() {
        char ch;                      // one character from clipboard string
        int hexValue;                 // integer value of one hexadecimal digit
        int i;                        // index variable
        int length;                   // length of clipboard string in characters
        int[] nibbles;                // nibble values created from <text>
        String text;                  // string obtained from clipboard
        int used;                     // number of nibbles used in <nibbles>

        text = getClipboard();        // get user's string from clipboard
        length = text.length();       // get number of characters in user's string
        nibbles = new int[length];    // allocate maximum array that may be needed
        used = 0;                     // start placing nibbles at this array index
        for (i = 0; i < length; i++) // do all characters in the string
        {
            ch = text.charAt(i);        // get one character from clipboard string
            hexValue = charHexValue(ch); // convert character to value of hex digit
            if (hexValue >= 0) // was it a valid hexadecimal digit?
            {
                nibbles[used++] = hexValue; // yes, save the nibble value
            } else if (hexValue == hexEdit.HEX_IGNORE) // ignore spaces and punctuation?
            { /* do nothing */ } else // illegal character
            {
                JOptionPane.showMessageDialog(hexEdit.mainFrame,
                        ("Clipboard string must be hexadecimal digits or spaces; found "
                        + (Character.isISOControl(ch) ? "" : ("\"" + ch + "\" or ")) + "0x"
                        + Integer.toHexString(ch).toUpperCase() + "."));
                return;                   // return early: cancel the paste operation
            }
        }
        pasteNibbles(nibbles, used, hexEdit.overFlag); // paste nibbles as file data

    } // end of pasteHex() method


    /*
     pasteNibbles() method

     Called by both pasteHex() and pasteText() to paste an array of nibbles at the
     cursor location.  Handles insert/overwrite modes and selections.
     */
    public void pasteNibbles(
            int[] array, // 4-bit nibble values (may not be full)
            int used, // actual number of nibbles used in array
            boolean localOver) // global <overFlag> or local true/false
    {
        int beginIndex, endIndex;     // nibble index variables
        int i;                        // index variable

        if (used <= 0) // is there any real work to do?
        {
            return;                     // no: clipboard not available, not a string,
        } // ... or an empty string
        else if (localOver) // are we in overwrite mode?
        {
            /* Clipboard replaces or "overwrites" starting at the cursor, or appends
             if there is more clipboard than data after the cursor. */

            beginIndex = Math.min(hexEdit.textPanel.cursorDot, hexEdit.textPanel.cursorMark);
            endIndex = Math.max(hexEdit.textPanel.cursorDot, hexEdit.textPanel.cursorMark);

            if ((beginIndex < endIndex) && (used != (endIndex - beginIndex))) {
                JOptionPane.showMessageDialog(hexEdit.mainFrame, ("Overwrite selection ("
                        + (endIndex - beginIndex) + ") and clipboard (" + used
                        + ") have different sizes."));
                return;                   // return early: cancel the paste operation
            }

            hexEdit.textPanel.cursorDot = beginIndex; // start replacing here
            for (i = 0; i < used; i++) // replace with nibbles given by caller
            {
                hexEdit.nibbleData.put((hexEdit.textPanel.cursorDot++), array[i]);
            }
        } else // must be insert mode
        {
            /* Clipboard inserts starting at the cursor.  Any selection is replaced
             by first deleting the selection, then inserting the clipboard. */

            deleteSelected();           // delete current selection, if any
            for (i = 0; i < used; i++) // insert all nibbles given by caller
            {
                hexEdit.nibbleData.insert((hexEdit.textPanel.cursorDot++), array[i]);
            }
        }

        hexEdit.textPanel.cursorMark = hexEdit.textPanel.cursorDot;
        // position cursor after inserted bytes
        hexEdit.textPanel.limitCursorRange(); // refresh data size, enforce cursor range
        hexEdit.textPanel.makeVisible(hexEdit.textPanel.cursorDot);
        // make sure that user can see cursor
        hexEdit.textPanel.adjustScrollBar();  // adjust scroll bar to match new position
        hexEdit.textPanel.repaint();          // redraw text display as necessary

    } // end of pasteNibbles() method


    /*
     pasteText() method

     Paste the clipboard as ASCII text at the cursor location.  We accept all
     characters, because they are converted to a byte array using the local
     system's default encoding.  We treat the bytes as a string of nibbles; no
     attempt is made to align them on a byte boundary.  This may produce results
     that are unexpected, but which are logically consistent.
     */
    public void pasteText() {
        byte[] bytes;                 // array of bytes obtained from a string
        int i;                        // index variable
        int[] nibbles;                // nibble values created from <bytes>
        int used;                     // number of nibbles used in <nibbles>

        bytes = getClipboard().getBytes(); // convert clipboard to bytes
        nibbles = new int[bytes.length * 2]; // always uses full array
        used = 0;                     // start placing nibbles at this array index
        for (i = 0; i < bytes.length; i++) // do all bytes in the string
        {
            nibbles[used++] = (bytes[i] >> hexEdit.NIBBLE_SHIFT) & hexEdit.NIBBLE_MASK;
            // high-order nibble in byte
            nibbles[used++] = bytes[i] & hexEdit.NIBBLE_MASK;
            // low-order nibble in byte
        }
        pasteNibbles(nibbles, used, hexEdit.overFlag); // paste nibbles as file data

    } // end of pasteText() method


    /*
     refreshDataSize() method

     Recalculate the total number of data nibbles, after insertions and deletions.
     */
    public void refreshDataSize() {
        hexEdit.nibbleCount = hexEdit.nibbleData.size(); // refetch total number of data nibbles
    }


    /*
     saveFile() method

     Ask the user for an output file name, create or replace that file, and copy
     the contents of our nibble data to that file.
     */
    public void saveFile() {
        File outputFile;              // user's selected output file

        /* Ask the user for an output file name. */
        hexEdit.fileChooser.setDialogTitle("Save File...");
        if (hexEdit.fileChooser.showSaveDialog(hexEdit.mainFrame) != JFileChooser.APPROVE_OPTION) {
            return;                     // user cancelled file selection dialog box
        }
        outputFile = hexEdit.fileChooser.getSelectedFile(); // get user's output file

        /* Convert pairs of 4-bit data nibbles to 8-bit bytes and write to the
         output file.  There may be an odd number of nibbles; in which case, assume
         a zero for the final nibble. */
        try // catch file I/O errors
        {
            if (canWriteFile(outputFile)) // if writing this file seems safe
            {
                saveToStream(new FileOutputStream(outputFile));
                // try to open output file
                refreshDataSize();        // refresh total number of nibbles
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(hexEdit.mainFrame,
                    ("Can't write to output file:\n" + ioe.getMessage()));
        }
    } // end of saveFile() method

    public void saveToStream(OutputStream outputStream) throws IOException {
        int i;                        // index variable
        int length;                   // number of bytes in output buffer
        byte[] buffer;                // output buffer (faster than byte-by-byte)

        buffer = new byte[hexEdit.BUFFER_SIZE]; // allocate byte buffer for output

        length = 0;               // nothing in output buffer yet

        i = 0;                    // start with first data nibble
        while (i < hexEdit.nibbleCount) // do all nibbles
        {
            if (length >= hexEdit.BUFFER_SIZE) // time to empty the output buffer?
            {
                outputStream.write(buffer); // yes, write entire buffer
                length = 0;           // and now there is nothing in the buffer
            }
            buffer[length] = (byte) (hexEdit.nibbleData.get(i++) << hexEdit.NIBBLE_SHIFT);
            // can always get high-order nibble
            if (i < hexEdit.nibbleCount) // there may not be a low-order nibble
            {
                buffer[length] |= (byte) hexEdit.nibbleData.get(i++);
                // insert bits for low-order nibble
            }
            length++;              // there is one more byte in output buffer
        }
        if (length > 0) // is there more output to be written?
        {
            outputStream.write(buffer, 0, length); // yes, write partial buffer
        }
        outputStream.close();     // try to close output file
    }

    /*
     searchConvertNibbles() method

     Given a text string, convert it to an integer array of nibbles, using the
     search dialog's options for hex or text conversion.  The given string should
     not be empty, because we use an empty array as a result to indicate an error.
     */
    public int[] searchConvertNibbles(
            String text, // user's string to be converted
            String type) // description like "replacement" or "search"
    {
        byte[] bytes;                 // array of bytes obtained from text string
        char ch;                      // one character from hex string
        boolean error;                // true if error occurs duing hex conversion
        int hexValue;                 // integer value of one hexadecimal digit
        int i;                        // index variable
        int length;                   // length of hex string in characters
        int[] nibbles;                // nibble values created from hex string
        int[] result;                 // nibble array that we return to the caller
        int used;                     // number of nibbles used in <nibbles>

        if (hexEdit.searchIsHex.isSelected()) // is this a hex search?
        {
            error = false;              // assume no errors during conversion
            length = text.length();     // get number of characters in user's string
            nibbles = new int[length];  // allocate maximum array that may be needed
            used = 0;                   // start placing nibbles at this array index
            for (i = 0; i < length; i++) // do all characters in the string
            {
                ch = text.charAt(i);      // get one character from clipboard string
                hexValue = charHexValue(ch); // convert character to value of hex digit
                if (hexValue >= 0) // was it a valid hexadecimal digit?
                {
                    nibbles[used++] = hexValue; // yes, save the nibble value
                } else if (hexValue == hexEdit.HEX_IGNORE) // ignore spaces and punctuation?
                { /* do nothing */ } else // illegal character
                {
                    showSearchMessage("Invalid hex digit in " + type + " string; found "
                            + (Character.isISOControl(ch) ? "" : ("\"" + ch + "\" or ")) + "0x"
                            + Integer.toHexString(ch).toUpperCase() + ".");
                    error = true;           // flag this as an error condition
                    break;                  // escape early from <for> loop
                }
            }
            if (error) // was an invalid hex digit found?
            {
                used = 0;                 // yes, force result to be empty
            } else if (used == 0) // did we find any valid hex digits?
            {
                showSearchMessage("No valid hex digits found in " + type + " string.");
            }
            result = new int[used];     // recopy array with only the portion used
            for (i = 0; i < used; i++) {
                result[i] = nibbles[i];
            }
        } else // must be a text search, which never fails
        {
            bytes = text.getBytes();    // convert string to bytes, default encoding
            result = new int[bytes.length * 2]; // always uses full array
            used = 0;                   // start placing nibbles at this array index
            for (i = 0; i < bytes.length; i++) // do all bytes in the string
            {
                result[used++] = (bytes[i] >> hexEdit.NIBBLE_SHIFT) & hexEdit.NIBBLE_MASK;
                // high-order nibble in byte
                result[used++] = bytes[i] & hexEdit.NIBBLE_MASK;
                // low-order nibble in byte
            }
        }
        return (result);               // give caller whatever we could find

    } // end of searchConvertNibbles() method


    /*
     searchFindFirst() method

     Find the first occurrence of the current search string (if any).
     */
    public void searchFindFirst() {
        searchFindNext(0);            // like "Find Next" but start at beginning
    }


    /*
     searchFindNext() method

     Find the next occurrence of the current search string (if any).  The user can
     and may change options, text fields, etc, between searches.  So this method
     should really be called "find a search string starting from the current
     cursor location or after the current selection if any".  Much easier to say
     "Find Next"!
     */
    public void searchFindNext() {
        searchFindNext(Math.max(hexEdit.textPanel.cursorDot, hexEdit.textPanel.cursorMark));
    }

    public void searchFindNext(
            int givenStart) // data nibble index where search begins
    {
        boolean byteFlag;             // true if start searching on byte boundaries
        int i;                        // index variable
        boolean matchFlag;            // true if we found a match
        int[] nibbles;                // nibble array obtained from <text>
        boolean nullFlag;             // true if null bytes ignored in data
        int start;                    // index that starts current comparison
        String text;                  // search string as typed by user

        if (hexEdit.searchDialog == null) // has the search dialog been created?
        {
            showSearchDialog();         // be nice and start the find/replace dialog
            return;
        }
        text = hexEdit.searchFindText.getText(); // get user's string
        if (text.length() == 0) // did the user type anything?
        {
            showSearchDialog();         // bring up the full find/replace dialog
            showSearchMessage("Empty strings are found everywhere.  (Joke.)");
            hexEdit.searchFindText.requestFocusInWindow(); // we need the search string
            return;
        }
        nibbles = searchConvertNibbles(text, "search");
        if (nibbles.length == 0) // was there an error during conversion?
        {
            return;                     // yes, error message already printed
        }
        /* If we got this far, there is nothing to report and we should cancel any
         previous status message in the search dialog box. */

        hexEdit.searchStatus.setText(hexEdit.EMPTY_STATUS); // clear any previous search status

        /* We start looking at the location given by the caller, which is usually
         after the current selection. */
        byteFlag = hexEdit.searchByteBound.isSelected(); // true if start on byte boundary
        matchFlag = false;            // no match found yet
        nullFlag = hexEdit.searchIgnoreNulls.isSelected() && ((nibbles.length % 2) == 0);
        // true if can ignore nulls, if full bytes
        start = givenStart;           // index that starts current comparison
        if (byteFlag) // does user want searches to be full bytes?
        {
            start += start % 2;         // yes, round up starting nibble index
        }
        /* For each acceptable starting index (byte or nibble boundary), try to
         match the user's string as-is.  If that fails, and the null flag is true,
         try again ignoring null bytes in the data.  Exactly one null byte appears
         when plain text (7-bit ASCII) characters are encoded in Unicode.  Some East
         Asian encodings like inserting null bytes for alignment reasons. */

        while ((hexEdit.nibbleCount - start) >= nibbles.length) // keep looking
        {
            /* First try the search string as-is.  This is a straight nibble-by-
             nibble comparison, no matter what the options may be. */

            boolean differFlag = false; // assume that comparison is successful
            for (i = 0; i < nibbles.length; i++) {
                if (nibbles[i] != hexEdit.nibbleData.get(start + i)) {
                    differFlag = true;      // comparison has failed
                    break;                  // escape early from inner <for> loop
                }
            }
            if (differFlag == false) // was the comparison successful?
            {
                matchFlag = true;         // indicate that we were successful
                hexEdit.textPanel.cursorMark = start; // yes, set start of selection
                hexEdit.textPanel.cursorDot = start + nibbles.length; // set end of selection
                break;                    // escape early from outer <while> loop
            }

            /* Try again starting from the same location.  This time, if a whole byte
             differs, and the nibble data for that byte is null (0x00), advance to the
             next byte of nibble data.  Null search bytes will be matched to the first
             null data byte.  Once a byte is matched, the following does not backtrack
             if a later byte comparison fails. */
            if (nullFlag && ((start % 2) == 0)) // if nulls ignored and start on byte
            {
                int dataIndex = start;    // where we are looking in the nibble data
                differFlag = false;       // assume that comparison is successful
                int findIndex = 0;        // where we are comparing from search data
                while ((dataIndex < (hexEdit.nibbleCount - 1)) // while there are data bytes
                        && (findIndex < (nibbles.length - 1))) // and there are search bytes
                {
                    int dataByte = (hexEdit.nibbleData.get(dataIndex) << hexEdit.NIBBLE_SHIFT)
                            | hexEdit.nibbleData.get(dataIndex + 1);
                    // construct one byte of nibble data
                    int findByte = (nibbles[findIndex] << hexEdit.NIBBLE_SHIFT)
                            | nibbles[findIndex + 1]; // construct one byte of search data
                    if (dataByte == findByte) // does file data match search string?
                    {
                        dataIndex += 2;       // yes, index of next file data byte
                        findIndex += 2;       // index of next search string byte
                    } else if ((dataByte == 0x00) && (findIndex > 0)) // in between null?
                    {
                        dataIndex += 2;       // ignore null data bytes, but after matching
                        // ... at least one byte of search string
                    } else {
                        differFlag = true;    // comparison has failed
                        break;                // escape early from inner <while> loop
                    }
                }
                if ((differFlag == false) && (findIndex == nibbles.length)) // was the comparison successful?
                {
                    matchFlag = true;       // indicate that we were successful
                    hexEdit.textPanel.cursorMark = start; // yes, set start of selection
                    hexEdit.textPanel.cursorDot = dataIndex; // set end of selection
                    break;                  // escape early from outer <while> loop
                }
            }

            /* We were unable to match the search string at this starting index.
             Advance to the next possible starting index. */
            start += byteFlag ? 2 : 1;  // advance by bytes or by nibbles
        }

        /* Show the results of our search. */
        if (matchFlag) // were we successful?
        {
            hexEdit.textPanel.makeVisible(hexEdit.textPanel.cursorMark);
            // do try to show start of selection
            hexEdit.textPanel.makeVisible(hexEdit.textPanel.cursorDot);
            // but end of selection is more important
            hexEdit.textPanel.adjustScrollBar(); // adjust scroll bar to match new position
            hexEdit.textPanel.repaint();        // redraw text display as necessary
        } else // no, search failed
        {
            showSearchMessage("Search string not found.");
        }

    } // end of searchFindNext() method


    /*
     searchReplaceThis() method

     Replace the current selection (if any) with the replacement string (if any).
     Since there is no "Undo" feature yet, we replace only if there is an active
     selection and the replacement string is not empty (no "search-and-delete"
     allowed).  There is no "Replace All" feature in this program (too dangerous).
     */
    public void searchReplaceThis() {
        int[] nibbles;                // nibble array obtained from <text>
        String text;                  // replacement string as typed by user

        if (hexEdit.searchDialog == null) // has the search dialog been created?
        {
            showSearchDialog();         // be nice and start the find/replace dialog
            if (hexEdit.textPanel.cursorDot != hexEdit.textPanel.cursorMark) // if selection exists
            {
                hexEdit.searchReplaceText.requestFocusInWindow(); // only need replace string
            }
            return;
        }
        if (hexEdit.textPanel.cursorDot == hexEdit.textPanel.cursorMark) // is there a selection?
        {
            showSearchMessage("There is no selection to replace.");
            return;                     // nothing to do, so return to caller
        }
        text = hexEdit.searchReplaceText.getText(); // get user's string
        if (text.length() == 0) // did the user type anything?
        {
            showSearchDialog();         // bring up the full find/replace dialog
            showSearchMessage("Replacement with an empty string is not supported.");
            hexEdit.searchReplaceText.requestFocusInWindow(); // we need the replace string
            return;
        }
        nibbles = searchConvertNibbles(text, "replace");
        if (nibbles.length == 0) // was there an error during conversion?
        {
            return;                     // yes, error message already printed
        }
        /* If we got this far, there is nothing to report and we should cancel any
         previous status message in the search dialog box. */

        hexEdit.searchStatus.setText(hexEdit.EMPTY_STATUS); // clear any previous search status

        /* We always insert, never overwrite, so pull the rug out from under the
         pasteNibbles() method, which takes care of deleting the selection. */
        pasteNibbles(nibbles, nibbles.length, false); // paste nibbles as file data

    } // end of searchReplaceThis() method


    /*
     selectAll() method

     Select all data nibbles from the beginning to the end.  Leave the cursor in
     whatever region is active.  (Don't change the <cursorOnText> variable.)
     */
    public void selectAll() {
        hexEdit.textPanel.cursorDot = hexEdit.nibbleCount; // current cursor ends selection
        hexEdit.textPanel.cursorMark = 0;     // select data from beginning of file
        hexEdit.textPanel.repaint();          // redraw text display as necessary
    }


    /*
     setClipboard() method

     Place a string onto the clipboard for some other application to read.
     */
    public void setClipboard(String text) {
        hexEdit.clipString = text;            // save string reference for later
        try // clipboard may not be available
        {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                    (Transferable) hexEdit.userActions, null); // place data notice on clipboard
        } catch (IllegalStateException ise) {
            JOptionPane.showMessageDialog(hexEdit.mainFrame,
                    ("Can't put text on clipboard:\n" + ise.getMessage()));
        }
    } // end of setClipboard() method


    /*
     showEditMenu() method

     Display the pop-up "edit menu" at a location relative to a given component
     and (x,y) co-ordinates.  Items in the menu are enabled or disabled according
     to approximate context.
     */
    public void showEditMenu(Component invoker, int x, int y, boolean mouse) {
        boolean clipboard = getClipboard().length() > 0; // if text on clipboard
        boolean content = hexEdit.nibbleCount > 0; // true if there is any file data
        boolean selection = hexEdit.textPanel.cursorDot != hexEdit.textPanel.cursorMark;
        // true if there is a current selection

        hexEdit.menuPopup = new JPopupMenu();

        hexEdit.menuCopyDump.setEnabled(selection);
        hexEdit.menuPopup.add(hexEdit.menuCopyDump);
        hexEdit.menuCopyHex.setEnabled(selection);
        hexEdit.menuPopup.add(hexEdit.menuCopyHex);
        hexEdit.menuCopyText.setEnabled(selection);
        hexEdit.menuPopup.add(hexEdit.menuCopyText);
        hexEdit.menuPasteHex.setEnabled(clipboard);
        hexEdit.menuPopup.add(hexEdit.menuPasteHex);
        hexEdit.menuPasteText.setEnabled(clipboard);
        hexEdit.menuPopup.add(hexEdit.menuPasteText);

        hexEdit.menuPopup.addSeparator();

        hexEdit.menuFind.setEnabled(content);
        hexEdit.menuPopup.add(hexEdit.menuFind);
        hexEdit.menuNext.setEnabled(content);
        hexEdit.menuPopup.add(hexEdit.menuNext);
        hexEdit.menuReplace.setEnabled(selection);
        hexEdit.menuPopup.add(hexEdit.menuReplace);

        hexEdit.menuPopup.addSeparator();

        hexEdit.menuDelete.setEnabled(selection);
        hexEdit.menuPopup.add(hexEdit.menuDelete);
        hexEdit.menuSelect.setEnabled(content);
        hexEdit.menuPopup.add(hexEdit.menuSelect);

        if (mouse == false) // Edit button only, not right mouse click
        {
            hexEdit.menuPopup.addSeparator();
            hexEdit.menuCopyCursor.setEnabled(true);
            hexEdit.menuPopup.add(hexEdit.menuCopyCursor);
            hexEdit.menuGotoOffset.setEnabled(content);
            hexEdit.menuPopup.add(hexEdit.menuGotoOffset);
        }

        hexEdit.menuPopup.show(invoker, x, y); // show menu with context-sensitive items

    } // end of showEditMenu() method


    /*
     showGotoDialog() method

     Show the "Go To File Offset" dialog box.  We may have to create it first.
     This is a much simpler layout and easier interaction than <searchDialog>.
     */
    public void showGotoDialog() {
        if (hexEdit.gotoDialog == null) // has the dialog box been created yet?
        {
            /* Create a vertical box to stack buttons and options. */

            JPanel panel1 = new JPanel();
            panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));

            /* First layout line has the "go to" address input string. */
            JPanel panel2 = new JPanel(new BorderLayout(10, 0));

            JLabel label1 = new JLabel("Go to file offset:", JLabel.RIGHT);
            if (hexEdit.buttonFont != null) {
                label1.setFont(hexEdit.buttonFont);
            }
            panel2.add(label1, BorderLayout.WEST);

            hexEdit.gotoOffsetText = new JTextField("", 15);
            hexEdit.gotoOffsetText.addActionListener(hexEdit.userActions);
            if (hexEdit.buttonFont != null) {
                hexEdit.gotoOffsetText.setFont(hexEdit.buttonFont);
            }
            hexEdit.gotoOffsetText.setMargin(hexEdit.TEXT_MARGINS);
            panel2.add(hexEdit.gotoOffsetText, BorderLayout.CENTER);
            panel1.add(panel2);
            panel1.add(Box.createVerticalStrut(10)); // vertical space

            /* Second layout line has a message string for the "go to" status. */
            JPanel panel3 = new JPanel(new BorderLayout(0, 0));
            hexEdit.gotoStatus = new JLabel(hexEdit.EMPTY_STATUS, JLabel.CENTER);
            if (hexEdit.buttonFont != null) {
                hexEdit.gotoStatus.setFont(hexEdit.buttonFont);
            }
            showGotoRange(-1);          // set message size with maximum hex digits
            panel3.add(hexEdit.gotoStatus, BorderLayout.CENTER);
            panel1.add(panel3);
            panel1.add(Box.createVerticalStrut(20));

            /* Third and last line has the action buttons. */
            JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));

            hexEdit.gotoJumpButton = new JButton("Go");
            hexEdit.gotoJumpButton.addActionListener(hexEdit.userActions);
            if (hexEdit.buttonFont != null) {
                hexEdit.gotoJumpButton.setFont(hexEdit.buttonFont);
            }
            hexEdit.gotoJumpButton.setMnemonic(KeyEvent.VK_G);
            hexEdit.gotoJumpButton.setToolTipText("Jump to file offset given above.");
            panel4.add(hexEdit.gotoJumpButton);

            hexEdit.gotoCloseButton = new JButton("Close");
            hexEdit.gotoCloseButton.addActionListener(hexEdit.userActions);
            if (hexEdit.buttonFont != null) {
                hexEdit.gotoCloseButton.setFont(hexEdit.buttonFont);
            }
            hexEdit.gotoCloseButton.setMnemonic(KeyEvent.VK_C);
            hexEdit.gotoCloseButton.setToolTipText("Close this dialog box.");
            panel4.add(hexEdit.gotoCloseButton);

            panel1.add(panel4);

            /* Put the vertical box inside a flow layout to center it horizontally
             and stop expansion.  Add left and right margins with the flow layout. */
            JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
            panel5.add(panel1);         // align vertical box inside horizontal box

            /* Put the flow layout in another vertical box to center vertically. */
            Box panel6 = Box.createVerticalBox(); // create a basic vertical box
            panel6.add(Box.createGlue()); // stretch to the top
            panel6.add(Box.createVerticalStrut(20)); // top margin
            panel6.add(panel5);         // horizontal flow layout
            panel6.add(Box.createVerticalStrut(20)); // bottom margin
//    panel6.add(Box.createGlue()); // stretch to bottom (assumed by layout)

            /* Position the dialog box.  Like a JFrame, a JDialog doesn't have an
             initial size.  We "pack" our JDialog layout to the minimum size. */
            hexEdit.gotoDialog = new JDialog((Frame) null, "Go To File Offset");
            hexEdit.gotoDialog.getContentPane().add(panel6, BorderLayout.CENTER);
            hexEdit.gotoDialog.pack();          // lay out components, set preferred size
            hexEdit.gotoDialog.setLocation(hexEdit.mainFrame.getX() + 50, hexEdit.mainFrame.getY() + 50);

            /* The status message controls the layout width, so fix the size, to
             avoid the input field changing size when the message text changes. */
            hexEdit.gotoStatus.setPreferredSize(hexEdit.gotoStatus.getPreferredSize());
        }

        refreshDataSize();            // set to correct number of data nibbles
        showGotoRange(hexEdit.nibbleCount / 2); // default to message about range allowed
        if (hexEdit.gotoDialog.isVisible() == false) // if dialog is closed or hidden
        {
            hexEdit.gotoOffsetText.requestFocusInWindow();
        }
        // assume user wants to edit offset string
        hexEdit.gotoDialog.setVisible(true);  // show "go to" dialog or bring to the front

    } // end of showGotoDialog() method


    /*
     showGotoRange() method

     Set the status in the "Go To File Offset" dialog box to an informational
     message about the hexadecimal range allowed.
     */
    public void showGotoRange(int maxValue) {
        hexEdit.gotoStatus.setText("Enter a byte offset in hexadecimal from 0 to "
                + Integer.toHexString(maxValue).toUpperCase() + " and click \"Go\".");
    }

    /*
     showSearchDialog() method

     Show the "Find or Replace" dialog box.  We may have to create it first.  This
     layout is more complicated than the "Go To File Offset" dialog.  Also, user
     keystrokes (Control-F, Control-N, etc) may invoke methods related to dialog
     components before the dialog box has actually been created.
     */
    public void showSearchDialog() {
        GridBagConstraints gbc;       // reuse the same constraint object

        if (hexEdit.searchDialog == null) // has the dialog box been created yet?
        {
            /* Create the search dialog using a grid bag layout.  Most of this code
             is just plain ugly.  There isn't must chance of understanding it unless
             you read the documentation for GridBagLayout ... if you can understand
             that!  As with our main frame, some intermediate panel names are of no
             lasting importance and hence are only numbered (panel1, panel2, etc). */

            JPanel panel1 = new JPanel(new GridBagLayout()); // use a grid bag layout
            gbc = new GridBagConstraints(); // modify and reuse these constraints

            /* First layout line has the "find" or "search" string. */
            JLabel label1 = new JLabel("Search for:");
            if (hexEdit.buttonFont != null) {
                label1.setFont(hexEdit.buttonFont);
            }
            gbc.anchor = GridBagConstraints.EAST;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridwidth = 1;
            panel1.add(label1, gbc);
            panel1.add(Box.createHorizontalStrut(10), gbc); // horizontal space

            hexEdit.searchFindText = new JTextField("", 20);
            hexEdit.searchFindText.addActionListener(hexEdit.userActions);
            if (hexEdit.buttonFont != null) {
                hexEdit.searchFindText.setFont(hexEdit.buttonFont);
            }
            hexEdit.searchFindText.setMargin(hexEdit.TEXT_MARGINS);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel1.add(hexEdit.searchFindText, gbc);
            panel1.add(Box.createVerticalStrut(10), gbc); // vertical space

            /* Second layout line has the "replace" string. */
            JLabel label2 = new JLabel("Replace with:");
            if (hexEdit.buttonFont != null) {
                label2.setFont(hexEdit.buttonFont);
            }
            gbc.anchor = GridBagConstraints.EAST;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridwidth = 1;
            panel1.add(label2, gbc);
            panel1.add(Box.createHorizontalStrut(10), gbc);

            hexEdit.searchReplaceText = new JTextField("", 20);
            hexEdit.searchReplaceText.addActionListener(hexEdit.userActions);
            if (hexEdit.buttonFont != null) {
                hexEdit.searchReplaceText.setFont(hexEdit.buttonFont);
            }
            hexEdit.searchReplaceText.setMargin(hexEdit.TEXT_MARGINS);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel1.add(hexEdit.searchReplaceText, gbc);
            panel1.add(Box.createVerticalStrut(10), gbc);

            /* Third layout line has the options: radio buttons and check boxes.  Not
             all of these require action listeners. */
            ButtonGroup group1 = new ButtonGroup();
            JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

            hexEdit.searchIsHex = new JRadioButton("hex string", true);
            if (hexEdit.buttonFont != null) {
                hexEdit.searchIsHex.setFont(hexEdit.buttonFont);
            }
            hexEdit.searchIsHex.setMnemonic(KeyEvent.VK_H);
            hexEdit.searchIsHex.setToolTipText(
                    "Search and replace strings are hexadecimal digits.");
            group1.add(hexEdit.searchIsHex);
            panel3.add(hexEdit.searchIsHex);

            hexEdit.searchIsText = new JRadioButton("text string", false);
            if (hexEdit.buttonFont != null) {
                hexEdit.searchIsText.setFont(hexEdit.buttonFont);
            }
            hexEdit.searchIsText.setMnemonic(KeyEvent.VK_T);
            hexEdit.searchIsText.setToolTipText(
                    "Search and replace strings are regular text.");
            hexEdit.searchIsText.addActionListener(hexEdit.userActions);
            // do last so this doesn't fire early
            group1.add(hexEdit.searchIsText);
            panel3.add(hexEdit.searchIsText);

            hexEdit.searchByteBound = new JCheckBox("byte boundary", false);
            if (hexEdit.buttonFont != null) {
                hexEdit.searchByteBound.setFont(hexEdit.buttonFont);
            }
            hexEdit.searchByteBound.setMnemonic(KeyEvent.VK_B);
            hexEdit.searchByteBound.setToolTipText(
                    "Search starts on byte boundary.  Does not apply to replace.");
            panel3.add(hexEdit.searchByteBound);

            hexEdit.searchIgnoreNulls = new JCheckBox("ignore nulls", false);
            if (hexEdit.buttonFont != null) {
                hexEdit.searchIgnoreNulls.setFont(hexEdit.buttonFont);
            }
            hexEdit.searchIgnoreNulls.setMnemonic(KeyEvent.VK_I);
            hexEdit.searchIgnoreNulls.setToolTipText(
                    "Ignore null data bytes between hexEdit.search bytes.");
            panel3.add(hexEdit.searchIgnoreNulls);

            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel1.add(panel3, gbc);
            panel1.add(Box.createVerticalStrut(10), gbc);

            /* Fourth layout line has a message string for the hexEdit.search status. */
            hexEdit.searchStatus = new JLabel(hexEdit.EMPTY_STATUS, JLabel.CENTER);
            if (hexEdit.buttonFont != null) {
                hexEdit.searchStatus.setFont(hexEdit.buttonFont);
            }
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel1.add(hexEdit.searchStatus, gbc);
            panel1.add(Box.createVerticalStrut(20), gbc);

            /* Fifth and last line has the action buttons. */
            JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));

            hexEdit.searchFindButton = new JButton("Find First");
            hexEdit.searchFindButton.addActionListener(hexEdit.userActions);
            if (hexEdit.buttonFont != null) {
                hexEdit.searchFindButton.setFont(hexEdit.buttonFont);
            }
            hexEdit.searchFindButton.setMnemonic(KeyEvent.VK_F);
            hexEdit.searchFindButton.setToolTipText(
                    "Find first occurrence of hexEdit.search string in file.");
            panel4.add(hexEdit.searchFindButton);

            hexEdit.searchNextButton = new JButton("Find Next");
            hexEdit.searchNextButton.addActionListener(hexEdit.userActions);
            if (hexEdit.buttonFont != null) {
                hexEdit.searchNextButton.setFont(hexEdit.buttonFont);
            }
            hexEdit.searchNextButton.setMnemonic(KeyEvent.VK_N);
            hexEdit.searchNextButton.setToolTipText(
                    "Find next occurrence of hexEdit.search string.");
            panel4.add(hexEdit.searchNextButton);

            hexEdit.searchReplaceButton = new JButton("Replace");
            hexEdit.searchReplaceButton.addActionListener(hexEdit.userActions);
            if (hexEdit.buttonFont != null) {
                hexEdit.searchReplaceButton.setFont(hexEdit.buttonFont);
            }
            hexEdit.searchReplaceButton.setMnemonic(KeyEvent.VK_R);
            hexEdit.searchReplaceButton.setToolTipText(
                    "Replace current selection or previously found string.");
            panel4.add(hexEdit.searchReplaceButton);

            hexEdit.searchCloseButton = new JButton("Close");
            hexEdit.searchCloseButton.addActionListener(hexEdit.userActions);
            if (hexEdit.buttonFont != null) {
                hexEdit.searchCloseButton.setFont(hexEdit.buttonFont);
            }
            hexEdit.searchCloseButton.setMnemonic(KeyEvent.VK_C);
            hexEdit.searchCloseButton.setToolTipText("Close this dialog box.");
            panel4.add(hexEdit.searchCloseButton);

            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel1.add(panel4, gbc);

            /* The layout in a grid bag goes strange if there isn't enough space.
             Box the grid bag inside a flow layout to center it horizontally and stop
             expansion, then inside a plain box to center it vertically. */
            JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
            panel5.add(panel1);         // put grid bag in a fancy horizontal box

            Box panel6 = Box.createVerticalBox(); // create a basic vertical box
            panel6.add(Box.createGlue()); // stretch to the top
            panel6.add(Box.createVerticalStrut(20)); // top margin
            panel6.add(panel5);         // put boxed grid bag in center
            panel6.add(Box.createVerticalStrut(20)); // bottom margin
//    panel6.add(Box.createGlue()); // stretch to bottom (assumed by layout)

            /* Position the dialog box.  Like a JFrame, a JDialog doesn't have an
             initial size.  We "pack" our JDialog layout to the minimum size. */
            hexEdit.searchDialog = new JDialog((Frame) null, "Find or Replace");
            hexEdit.searchDialog.getContentPane().add(panel6, BorderLayout.CENTER);
            hexEdit.searchDialog.pack();        // lay out components, set preferred size
            hexEdit.searchDialog.setLocation(hexEdit.mainFrame.getX() + 50, hexEdit.mainFrame.getY() + 50);
        }

        hexEdit.searchStatus.setText(hexEdit.EMPTY_STATUS); // clear any previous search status
        if (hexEdit.searchDialog.isVisible() == false) // if dialog is closed or hidden
        {
            hexEdit.searchFindText.requestFocusInWindow();
        }
        // assume user wants to edit search string
        hexEdit.searchDialog.setVisible(true); // show search dialog or bring to the front

    } // end of showSearchDialog() method


    /*
     showSearchMessage() method

     Display a text string given by the caller.  If the search dialog is open,
     then use the message field in the dialog.  Otherwise, use a pop-up dialog.
     */
    public void showSearchMessage(String text) {
        if ((hexEdit.searchDialog != null) && hexEdit.searchDialog.isVisible()) {
            hexEdit.searchStatus.setText(text); // place search result in message field
            hexEdit.searchDialog.setVisible(true); // bring search dialog to the front
        } else {
            JOptionPane.showMessageDialog(hexEdit.mainFrame, text); // simpler pop-up dialog
        }
    }


    /*
     userButton() method

     This method is called by our action listener actionPerformed() to process
     buttons, in the context of the main HexEdit2 class.
     */
    public void userButton(ActionEvent event) {
        try // "out of memory" errors are likely
        {
            Object source = event.getSource(); // where the event came from
            if (source == hexEdit.dumpWidthDialog) // number of input bytes per dump line
            {
                /* We can safely parse the dump width as an integer, because we supply
                 the only choices allowed, and the user can't edit this dialog field.
                 Calling adjustScrollBar() or makeVisible() is not possible here,
                 because display sizes are unknown until after the first redraw. */

                hexEdit.dumpWidth = Integer.parseInt((String) hexEdit.dumpWidthDialog
                        .getSelectedItem());    // convert text width to integer
                hexEdit.textPanel.repaint();      // redraw text display as necessary
            } else if (source == hexEdit.exitButton) // "Exit" button
            {
                System.exit(0);           // always exit with zero status from GUI
            } else if (source == hexEdit.fontNameDialog) // font name for display text area
            {
                /* We can safely assume that the font name is valid, because we
                 obtained the names from getAvailableFontFamilyNames(), and the user
                 can't edit this dialog field.  Calling adjustScrollBar() or
                 makeVisible() is not possible here, because display sizes are unknown
                 until after the first redraw. */

                hexEdit.fontName = (String) hexEdit.fontNameDialog.getSelectedItem();
                hexEdit.textPanel.repaint();      // redraw text display as necessary
            } else if (source == hexEdit.gotoCloseButton) // "Close" button on "go to" dialog
            {
                hexEdit.gotoDialog.setVisible(false); // hide "Go To File Offset" dialog box
            } else if (source == hexEdit.gotoJumpButton) // "Go" button on "Go To File Offset"
            {
                gotoFileOffset();         // call common method for this operation
            } else if (source == hexEdit.gotoOffsetText) // press Enter on "go to" offset text
            {
                gotoFileOffset();         // call common method for this operation
            } else if (source == hexEdit.menuButton) // "Edit Menu" button
            {
                showEditMenu(hexEdit.menuButton, 0, hexEdit.menuButton.getHeight(), false);
            } else if (source == hexEdit.menuCopyCursor) // "Copy Cursor Offset" menu item
            {
                copyCursor();             // call common method for this operation
            } else if (source == hexEdit.menuCopyDump) // "Copy Dump" menu item (selection)
            {
                copyDump();               // call common method for this operation
            } else if (source == hexEdit.menuCopyHex) // "Copy Hex" menu item (selection)
            {
                copyHex();                // call common method for this operation
            } else if (source == hexEdit.menuCopyText) // "Copy Text" menu item (selection)
            {
                copyText();               // call common method for this operation
            } else if (source == hexEdit.menuDelete) // "Delete" menu item (selection)
            {
                deleteSelected();         // call common method for this operation
            } else if (source == hexEdit.menuFind) // "Find" menu item
            {
                showSearchDialog();       // call common method for this operation
            } else if (source == hexEdit.menuGotoOffset) // "Go To File Offset" menu item
            {
                showGotoDialog();         // call common method for this operation
            } else if (source == hexEdit.menuNext) // "Find Next" menu item
            {
                searchFindNext();         // call common method for this operation
            } else if (source == hexEdit.menuPasteHex) // "Paste Hex" menu item
            {
                pasteHex();               // call common method for this operation
            } else if (source == hexEdit.menuPasteText) // "Paste Text" menu item
            {
                pasteText();              // call common method for this operation
            } else if (source == hexEdit.menuReplace) // "Replace" menu item (selection)
            {
                searchReplaceThis();      // call common method for this operation
            } else if (source == hexEdit.menuSelect) // "Select All" menu item
            {
                selectAll();              // call common method for this operation
            } else if (source == hexEdit.openButton) // "Open File" button
            {
                openFile(null);           // ask for file name, read data from file
                hexEdit.textPanel.beginFile();    // display file from the beginning
            } else if (source == hexEdit.overDialog) // insert versus overwrite input mode
            {
                hexEdit.overFlag = hexEdit.overDialog.isSelected(); // transfer GUI to faster boolean
                hexEdit.textPanel.repaint();      // redraw text display as necessary
            } else if (source == hexEdit.saveButton) // "Save File" button
            {
                saveFile();               // ask for file name, write data to file
            } else if (source == hexEdit.searchCloseButton) // "Close" button on search dialog
            {
                hexEdit.searchDialog.setVisible(false); // hide search dialog box
            } else if (source == hexEdit.searchFindButton) // "Find First" button on search
            {
                searchFindFirst();        // call common method for this operation
            } else if (source == hexEdit.searchFindText) // press Enter on search text
            {
                searchFindNext();         // call common method for this operation
            } else if (source == hexEdit.searchIsText) // user wants text search, not hex
            {
                if (hexEdit.searchIsText.isSelected()) // selecting text search turns on the
                {
                    hexEdit.searchByteBound.setSelected(true); // ... byte boundary by default
                }
            } else if (source == hexEdit.searchNextButton) // "Find Next" button on search
            {
                hexEdit.searchFindNext();         // call common method for this operation
            } else if (source == hexEdit.searchReplaceButton) // "Replace" button on search
            {
                searchReplaceThis();      // call common method for this operation
            } else if (source == hexEdit.searchReplaceText) // press Enter on replacement text
            {
                searchReplaceThis();      // call common method for this operation
            } else {
                System.err.println(
                        "Error in HexEdit2 userButton(): ActionEvent not recognized: "
                        + event);
            }
        } catch (OutOfMemoryError oome) {
            memoryError("userButton");  // nicely tell user that we failed
        }
    } // end of userButton() method

}
