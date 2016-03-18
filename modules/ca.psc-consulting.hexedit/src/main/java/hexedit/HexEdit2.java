/*
 Hexadecimal File Editor #2
 Written by: Keith Fenske, http://www.psc-consulting.ca/fenske/
 Monday, 27 October 2008
 Java class name: HexEdit2
 Copyright (c) 2008 by Keith Fenske.  Released under GNU Public License.

 This is a Java 1.4 graphical (GUI) application to edit a file as a stream of
 hexadecimal digits, where each 8-bit byte is represented by two 4-bit
 "nibbles" (the hex digits).  No meaning is attached to the digits, and hence
 to the contents of the file, so this editing is very raw.  You can insert,
 delete, or replace digits or bytes, and you may view an approximate character
 equivalent of the digits in plain text (7-bit ASCII).  The primary purpose of
 a hex editor is to patch or correct specific locations within a file without
 affecting the rest of the file, something that most word processors can't do.
 A secondary purpose is to view the exact content of files.  (To generate a
 hex dump and save this dump to a file, see the DumpFile application.)

 The application window has two portions.  The top portion has standard Java
 buttons and options.  The bottom portion has a hexadecimal dump with three
 regions: file offsets on the left, bytes in hexadecimal in the middle, and
 ASCII text on the right.  The bottom looks like an old video terminal (quite
 deliberately), with a color scheme to match.  The Java buttons and options
 are straightforward after some experimentation.  Don't worry: no files are
 changed unless you click on the "Save File" button.  In fact, files aren't
 saved by default, so if you click on the Exit button without saving, any
 changes will be lost.  This may differ from what you expect with word
 processors or other types of editors.

 File offsets are 8-digit hexadecimal numbers to show you where the start of a
 row (dump line) is located from the beginning of the file.  Bytes are dumped
 (displayed) as two hexadecimal digits.  The first digit is the high-order
 digit in the byte; the second digit is the low-order digit.  All file bytes
 contain two digits, although during editing, you will see an odd number of
 nibbles in the file.  (A zero digit is appended if necessary when writing a
 file.)  Bytes are not grouped in any way, because different computers and
 programs have different ways of deciding which byte is the low-order byte in
 a word.  The text region on the right is only an approximation; most binary
 data is not text.  Bytes that are printable 7-bit ASCII characters are shown
 as text; anything else has a replacement character (".").  This does not mean
 that the program is limited to ASCII text.  You can copy and paste any text
 in the local system's default encoding, even if that encoding uses 8-bit
 bytes or multiple bytes.

 Other than the obvious scroll bar to move through the file, and the fun you
 can have resizing text by changing the number of bytes per line or the window
 size, your interaction is via the mouse and keyboard.  Most features are
 provided by both methods.

 The mouse can be used to position, select, scroll, or to open a context menu.
 To position the cursor (the location where data is inserted or deleted),
 click once with your primary mouse button, which is usually called a "left"
 click.  To select all data from a previous location to a new location, press
 and hold the "Shift" key on your keyboard and then left click on the new
 location.  (Release the Shift key when done.)  You may also select by
 clicking the mouse button, holding the button down, moving the mouse to a new
 location, and releasing the mouse button.  Selections must be entirely within
 the dump region or the text region.  If your mouse has a scroll wheel, you
 can rotate that wheel to move up or down in the file.  The final mouse
 feature is a context menu that will "pop up" if you click the right mouse
 button.  Not all computers have a right button, so any button other than the
 primary button will be accepted, and if your computer has only one mouse
 button, then hold down the "Control" key while clicking the primary mouse
 button.

 Regular text typed on the keyboard will be inserted into the file, or will
 replace a selection if a selection has been made.  For the dump region, only
 the decimal digits "0" to "9" are accepted, along with the uppercase
 hexadecimal digits "A" to "F" and the lowercase hexadecimal digits "a" to "f"
 (and a few punctuation characters are ignored).  For the text region, all
 printable characters are accepted, anything that Java can convert with the
 local system's default encoding.

 Keyboard Shortcuts
 ------------------
 There are numerous keyboard "shortcuts" or control codes.  In the following
 table, "Control-X" means to press and hold the "Control" key, then press and
 release the "X" key, then release the Control key.  Most of these key
 combinations are standard for editing applications, except that the unique
 nature of a hex dump changes the meaning of some keys.

 Alt         Invokes regular menus.  Officially, "Alt" stands for "Alternate"
 but nobody says it that way.

 Alt-M       Shows the "Edit Menu" (copy, paste, find, replace, select, etc).
 This is the same as the pop-up menu for right mouse clicks.

 Alt-O       Shows the "Open File" dialog box.

 Alt-S       Shows the "Save File" dialog box.

 Alt-X       Exits (closes) the program, no questions asked.  Closing the main
 window has the same effect.

 arrow keys  The left arrow moves the cursor one position to the left (one
 nibble for dumps, one byte for text).  The "Shift" key combined
 with the left arrow key (press and hold Shift key, press and
 release left arrow key, release Shift key) selects one position
 to the left, or expands the current selection by one position.
 The right arrow goes one position to the right, the up arrow goes
 one line/row up, and the down arrow goes one line/row down.

 Backspace   If there is a selection, then delete the selection.  Otherwise
 for hex dumps, delete the nibble (digit) before the cursor, and
 for ASCII text, delete the character (byte) before the cursor.
 This key may be "Bksp" on some keyboards, or just a left arrow
 (not to be confused with the real left arrow key).

 Control     Invokes keyboard shortcuts.  May be "Ctrl" on some keyboards.

 Control-A   Selects all data.

 Control-C   Copies selected data to the clipboard.  If the dump region is
 active, then text characters representing the hexadecimal digits
 are copied.  If the text region is active, the bytes are
 converted to text characters using the local system's default
 encoding.

 Control-F   Opens the "find" or "replace" dialog box.

 Control-G   Goes to a specific file byte offset in hexadecimal (dialog box).

 Control-N   Finds the next occurrence of the search string.

 Control-R   Replaces the current selection (if any) with the replacement
 string.

 Control-V   Pastes the clipboard as data.  Similar rules as Control-C.

 Control-X   Similar to Control-C except the selected data is deleted after it
 is copied to the clipboard.  Usually referred to as a "cut"
 operation.

 Control-Z   Reserved for an "undo" feature that hasn't been implemented yet.

 Delete      Same as Backspace, except that the deletion is forward: the
 nibble or byte following the cursor.  May be "Del" on some
 keyboards.

 End         Goes to the end of the current line/row, which (surprise!) turns
 out to be the beginning of the next line/row.  Control-End goes
 to the end of the file.  May be combined with the Shift key to
 select.

 Escape      Cancels any current selection.  May be "Esc" on some keyboards.

 F6          Makes the cursor active in the text region, while Shift-F6 makes
 the dump region active.

 Home        Goes to the beginning of the current line/row, or if already at
 the beginning, to the previous line/row.  Control-Home goes to
 the start of the file.  May be combined with the Shift key to
 select.

 Insert      Toggles between "insert" and "overwrite" mode.  Insert mode adds
 new nibbles/bytes at the cursor location (vertical line).
 Overwrite mode replaces nibbles/bytes at the cursor (box
 outline).  This key may be "Ins" on some keyboards.

 Page Down   Goes down as many lines/rows as there are in the display, less
 one.  May be combined with the Shift key to select.

 Page Up     Opposite of Page Down: goes up.  May be combined with Shift.

 Tab         Used by Java to traverse components (jump from button to button,
 etc).

 GNU General Public License (GPL)
 --------------------------------
 HexEdit2 is free software: you can redistribute it and/or modify it under the
 terms of the GNU General Public License as published by the Free Software
 Foundation, either version 3 of the License or (at your option) any later
 version.  This program is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY or
 FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details.

 You should have received a copy of the GNU General Public License along with
 this program.  If not, see the http://www.gnu.org/licenses/ web page.

 Graphical Versus Console Application
 ------------------------------------
 The Java command line may contain options for the insert/overwrite typing
 mode, the number of input bytes per dump line, and a file name.  See the "-?"
 option for a help summary:

 java  HexEdit2  -?

 The command line has more options than are visible in the graphical
 interface.  An option such as -u14 or -u16 is recommended because the default
 Java font is too small.

 Restrictions and Limitations
 ----------------------------
 There is an unusual circumstance where the mouse may point at the dump region
 but keyboard input is not accepted.  You can force this by clicking anywhere
 on the dump region, then without moving the mouse, open the "Edit Menu" with
 the Alt-M key combination, and cancel the menu with the Esc (escape) key.
 The buttons on top now have keyboard focus, even though the mouse is pointing
 at the dump on the bottom.  The dump region will regain focus as soon as you
 click or move the mouse.

 To handle insertions and deletions, the entire data file is buffered in
 memory as a split array of 4-bit "nibbles" (two data nibbles per 8-bit file
 byte).  To view a file, the Java heap size must be at least twice the size of
 the file.  To edit a file, it must be four times.  The default Java 1.4
 virtual machine on Windows will allow editing of files over 10 megabytes, and
 you may increase the maximum heap size with the "-Xmx" option on the Java
 command line.  This program is not recommended for files larger than 100
 megabytes unless you have a fast computer and disk drive.  The absolute
 maximum file size is one gigabyte, because nibbles in the file are counted
 with a signed 32-bit integer.

 Commentary: There Is A Reason
 -----------------------------
 There is a reason why most graphical applications use Java Swing components
 and let Swing take care of drawing, mouse clicks, etc: the interface is more
 consistent and easier to program.  This application is very close to what the
 JEditorPane and JTextPane classes handle well.  The only unusual features are
 the mirrored or shadowed cursors (one for the hex dump and one for the ASCII
 text) and navigation rules that skip file offsets, white space, and the text
 side markers.  Most of this could be done with JEditorPane/JTextPane, and
 could have more features than are available in this program (such as undo).

 The reason why programmers often avoid the obvious solution and write their
 own code is that it's more fun, you learn more, and you are better prepared
 in the future to decide which way to proceed.  Standard Java components don't
 always achieve the best results, and skill in writing your own "widgets"
 allows you more choice in customization.  Just remember: "The devil is in the
 details."  (Listed as an anonymous variation of "God is in the details" by
 the sixteenth edition of Bartlett's Familiar Quotations, edited by Justin
 Kaplan.)

 Suggestions for New Features
 ----------------------------
 (1) Should have an "undo" feature, and inexperienced users like confirmation
 for destructive changes (large deletions, an offer to save file on exit,
 etc).  KF, 2007-11-07.
 (2) I haven't found a way of allowing "ignore case" in text searches, without
 limiting text to 7-bit ASCII, or making assumptions about character set
 encodings.  KF, 2008-11-02.
 (3) A more general editor would show each byte in binary (8 digits), octal (3
 digits), decimal (3 digits), or hexadecimal (2 digits).  The numeric base
 would be a GUI option.  Data would be stored only as bytes (not nibbles).
 This would completely change the way internal data is accessed in this
 program, and is only suitable for a complete rewrite.  KF, 2008-11-04.

 ExBin Project related changes:
 - Library was modified, so that more instances can be used at the same time.
 */
package hexedit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

public class HexEdit2 {
    /* constants */

    static final int BUFFER_SIZE = 0x10000; // file buffer size in bytes (64 KB)
    static final int BYTE_MASK = 0x000000FF; // gets low-order byte from integer
    static final String COPYRIGHT_NOTICE
            = "Copyright (c) 2008 by Keith Fenske.  Released under GNU Public License.";
    static final int DEFAULT_DUMP = 16; // default input bytes per dump line
    static final int DEFAULT_HEIGHT = -1; // default window height in pixels
    static final int DEFAULT_LEFT = 50; // default window left position ("x")
    static final int DEFAULT_TOP = 50; // default window top position ("y")
    static final int DEFAULT_WIDTH = -1; // default window width in pixels
    static final String[] DUMP_WIDTHS = {"4", "8", "12", "16", "24", "32"};
    // number of input bytes per dump line
    static final String EMPTY_STATUS = " "; // message when no status to display
    static final char FIRST_CHAR = 0x20; // first printable ASCII character
    static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'}; // hexadecimal digits
    static final int HEX_IGNORE = -1; // special hex value for spaces, punctuation
    static final int HEX_INVALID = -2; // special hex value for illegal characters
    static final char LAST_CHAR = 0x7E; // last printable ASCII character
    static final char MARKER_CHAR = '|'; // left and right ASCII text markers
    static final String MARKER_STRING = Character.toString(MARKER_CHAR);
    static final int MIN_FRAME = 200; // minimum window height or width in pixels
    static final int NIBBLE_MASK = 0x0000000F; // low-order four bits from integer
    static final int NIBBLE_SHIFT = 4; // number of bits to shift for one nibble
    static final int OFFSET_DIGITS = 8; // hex digits in file offset (location)
    static final String PROGRAM_TITLE
            = "Hexadecimal File Editor - by: Keith Fenske";
    static final char REPLACE_CHAR = '.'; // replacement character for unprintable
    static final String SYSTEM_FONT = "Dialog"; // this font is always available
    static final Insets TEXT_MARGINS = new Insets(2, 3, 2, 3);
    // default top, left, bottom, right margins

    /* class variables */
    Font buttonFont;         // font for buttons, labels, status, etc
    String clipString;       // string that we put on the clipboard
    int dumpWidth;           // number of input bytes per dump line
    JComboBox dumpWidthDialog; // graphical option for <dumpWidth>
    JButton exitButton;      // "Exit" button for ending this application
    JFileChooser fileChooser; // asks for input and output file names
    String fontName;         // font name for text in text area
    JComboBox fontNameDialog; // graphical option for <fontName>
    NumberFormat formatComma; // formats with commas (digit grouping)
    JButton gotoCloseButton, gotoJumpButton; // buttons in "go to" dialog
    JDialog gotoDialog;      // "Go To File Offset" dialog box
    JTextField gotoOffsetText; // input text string with hex byte offset
    JLabel gotoStatus;       // message string for "go to" information
    JFrame mainFrame;        // this application's window for GUI
    JButton menuButton;      // "Edit Menu" button
    JMenuItem menuCopyCursor, menuCopyDump, menuCopyHex, menuCopyText,
            menuDelete, menuFind, menuGotoOffset, menuNext, menuPasteHex,
            menuPasteText, menuReplace, menuSelect; // menu items for <menuPopup>
    JPopupMenu menuPopup;    // pop-up menu invoked by <menuButton>
    boolean mswinFlag;       // true if running on Microsoft Windows
    int nibbleCount;         // total number of 4-bit data nibbles
    HexEdit2Data nibbleData; // two 4-bit nibbles for each 8-bit file byte
    JButton openButton;      // "Open File" button to read data file
    JCheckBox overDialog;    // graphical option for <overFlag>
    boolean overFlag;        // true for overwrite mode, false for insert
    JButton saveButton;      // "Save File" button to write new file
    JCheckBox searchByteBound, searchIgnoreNulls; // search options
    JButton searchCloseButton, searchFindButton, searchNextButton,
            searchReplaceButton;          // buttons in the search dialog
    JDialog searchDialog;    // "Find or Replace" dialog box
    JTextField searchFindText, searchReplaceText; // input text strings
    JRadioButton searchIsHex, searchIsText; // search options
    JLabel searchStatus;     // message string for search results
    HexEdit2Text textPanel;  // displays hex dump and accepts user input
    JScrollBar textScroll;   // vertical scroll bar beside <textPanel>
    ActionListener userActions; // our shared action listener

    /*
     main() method

     We run as a graphical application only.  Set the window layout and then let
     the graphical interface run the show.
     */
    public static void main(String[] args) {
        HexEdit2 hexEdit = new HexEdit2();
        hexEdit.run(args);
    } // end of main() method

    public void run(String[] args) {
        String fileName;              // first parameter that isn't an option
        int i;                        // index variable
        boolean maximizeFlag;         // true if we maximize our main window
        int windowHeight, windowLeft, windowTop, windowWidth;
        // position and size for <mainFrame>
        String word;                  // one parameter from command line

        /* Initialize global variables that may be affected by options on the
         command line. */
        buttonFont = null;            // by default, don't use customized font
        clipString = null;            // no string copied to clipboard yet
        dumpWidth = DEFAULT_DUMP;     // default input bytes per dump line
        fileName = "";                // first parameter is name of a file to open
        fontName = "Monospaced";      // default font name for text area
        gotoDialog = null;            // explicitly declare dialog as "not defined"
        maximizeFlag = false;         // true if we maximize our main window
        mswinFlag = System.getProperty("os.name").startsWith("Windows");
        nibbleCount = 0;              // total number of 4-bit data nibbles (none)
        nibbleData = new HexEdit2Data(0); // allocate empty data object for nibbles
        overFlag = false;             // by default, keyboard input has insert mode
        searchDialog = null;          // explicitly declare dialog as "not defined"
        windowHeight = DEFAULT_HEIGHT; // default window position and size
        windowLeft = DEFAULT_LEFT;
        windowTop = DEFAULT_TOP;
        windowWidth = DEFAULT_WIDTH;

        /* Initialize number formatting styles. */
        formatComma = NumberFormat.getInstance(); // current locale
        formatComma.setGroupingUsed(true); // use commas or digit groups

        /* We allow one argument (parameter) on the command line for the name of a
         file, and we do check for some options, but otherwise reject anything more
         on the command line. */
        for (i = 0; i < args.length; i++) {
            word = args[i].toLowerCase(); // easier to process if consistent case
            if (word.length() == 0) {
                /* Ignore null parameters, which are more common that you might think,
                 when programs are being run from inside scripts (command files). */
            } else if (word.equals("?") || word.equals("-?") || word.equals("/?")
                    || word.equals("-h") || (mswinFlag && word.equals("/h"))
                    || word.equals("-help") || (mswinFlag && word.equals("/help"))) {
                showHelp();               // show help summary
                System.exit(0);           // exit application after printing help
            } else if (word.equals("-d4") || (mswinFlag && word.equals("/d4"))) {
                dumpWidth = 4;            // user wants 4 input bytes per dump line
            } else if (word.equals("-d8") || (mswinFlag && word.equals("/d8"))) {
                dumpWidth = 8;
            } else if (word.equals("-d12") || (mswinFlag && word.equals("/d12"))) {
                dumpWidth = 12;
            } else if (word.equals("-d16") || (mswinFlag && word.equals("/d16"))) {
                dumpWidth = 16;
            } else if (word.equals("-d24") || (mswinFlag && word.equals("/d24"))) {
                dumpWidth = 24;
            } else if (word.equals("-d32") || (mswinFlag && word.equals("/d32"))) {
                dumpWidth = 32;
            } else if (word.equals("-ins") || (mswinFlag && word.equals("/ins"))) {
                overFlag = false;         // input starts in insert mode
            } else if (word.equals("-over") || (mswinFlag && word.equals("/over"))) {
                overFlag = true;          // input starts in overwrite mode
            } else if (word.startsWith("-u") || (mswinFlag && word.startsWith("/u"))) {
                /* This option is followed by a font point size that will be used for
                 buttons, dialogs, labels, etc. */

                int size = -1;            // default value for font point size
                try // try to parse remainder as unsigned integer
                {
                    size = Integer.parseInt(word.substring(2));
                } catch (NumberFormatException nfe) // if not a number or bad syntax
                {
                    size = -1;              // set result to an illegal value
                }

                if ((size < 10) || (size > 99)) {
                    System.err.println("Dialog font size must be from 10 to 99: "
                            + args[i]);           // notify user of our arbitrary limits
                    showHelp();             // show help summary
                    System.exit(-1);        // exit application after printing help
                }
                buttonFont = new Font(SYSTEM_FONT, Font.PLAIN, size); // for big sizes
//      buttonFont = new Font(SYSTEM_FONT, Font.BOLD, size); // for small sizes
            } else if (word.startsWith("-w") || (mswinFlag && word.startsWith("/w"))) {
                /* This option is followed by a list of four numbers for the initial
                 window position and size.  All values are accepted, but small heights
                 or widths will later force the minimum packed size for the layout. */

                Pattern pattern = Pattern.compile(
                        "\\s*\\(\\s*(\\d{1,5})\\s*,\\s*(\\d{1,5})\\s*,\\s*(\\d{1,5})\\s*,\\s*(\\d{1,5})\\s*\\)\\s*");
                Matcher matcher = pattern.matcher(word.substring(2)); // parse option
                if (matcher.matches()) // if option has proper syntax
                {
                    windowLeft = Integer.parseInt(matcher.group(1));
                    windowTop = Integer.parseInt(matcher.group(2));
                    windowWidth = Integer.parseInt(matcher.group(3));
                    windowHeight = Integer.parseInt(matcher.group(4));
                } else // bad syntax or too many digits
                {
                    System.err.println("Invalid window position or size: " + args[i]);
                    showHelp();             // show help summary
                    System.exit(-1);        // exit application after printing help
                }
            } else if (word.equals("-x") || (mswinFlag && word.equals("/x"))) {
                maximizeFlag = true;      // true if we maximize our main window
            } else if (word.startsWith("-") || (mswinFlag && word.startsWith("/"))) {
                System.err.println("Option not recognized: " + args[i]);
                showHelp();               // show help summary
                System.exit(-1);          // exit application after printing help
            } else {
                /* Parameter does not look like an option.  Assume that this is a file
                 name. */

                if (fileName.length() == 0) // do we already have a file name?
                {
                    fileName = args[i];     // no, use original parameter (not lowercase)
                } else {
                    System.err.println("Only one file name accepted: " + args[i]);
                    showHelp();             // show help summary
                    System.exit(-1);        // exit application after printing help
                }
            }
        }

        /* Open the graphical user interface (GUI).  The standard Java style is the
         most reliable, but you can switch to something closer to the local system,
         if you want. */
//  try
//  {
//    UIManager.setLookAndFeel(
//      UIManager.getCrossPlatformLookAndFeelClassName());
////    UIManager.getSystemLookAndFeelClassName());
//  }
//  catch (Exception ulafe)
//  {
//    System.err.println("Unsupported Java look-and-feel: " + ulafe);
//  }

        /* Initialize shared graphical objects. */
        fileChooser = new JFileChooser(); // create our shared file chooser
        fileChooser.resetChoosableFileFilters(); // remove any existing filters
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false); // allow only one file
        userActions = new HexEdit2User(this); // create our shared action listener

        /* Create the graphical interface as a series of little panels inside
         bigger panels.  The intermediate panel names are of no lasting importance
         and hence are only numbered (panel1, panel2, etc). */

        /* Create a vertical box to stack buttons and options. */
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel1.add(Box.createVerticalStrut(15)); // extra space at panel top

        /* Create a horizontal panel to hold the action buttons. */
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));

        menuButton = new JButton("Edit Menu");
        menuButton.addActionListener(userActions);
        if (buttonFont != null) {
            menuButton.setFont(buttonFont);
        }
        menuButton.setMnemonic(KeyEvent.VK_M);
        menuButton.setToolTipText("Copy, delete, find, paste, replace, etc.");
        panel2.add(menuButton);

        openButton = new JButton("Open File...");
        openButton.addActionListener(userActions);
        if (buttonFont != null) {
            openButton.setFont(buttonFont);
        }
        openButton.setMnemonic(KeyEvent.VK_O);
        openButton.setToolTipText("Read data bytes from a file.");
        panel2.add(openButton);

        saveButton = new JButton("Save File...");
        saveButton.addActionListener(userActions);
        if (buttonFont != null) {
            saveButton.setFont(buttonFont);
        }
        saveButton.setMnemonic(KeyEvent.VK_S);
        saveButton.setToolTipText("Write data bytes to a file.");
        panel2.add(saveButton);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(userActions);
        if (buttonFont != null) {
            exitButton.setFont(buttonFont);
        }
        exitButton.setMnemonic(KeyEvent.VK_X);
        exitButton.setToolTipText("Close this program.");
        panel2.add(exitButton);

        panel1.add(panel2);
        panel1.add(Box.createVerticalStrut(13));

        /* These are the individual menu items for <menuPopup> which is invoked by
         <menuButton> or a right mouse click.  They are assembled into a real menu
         later by the showEditMenu() method. */
        menuCopyCursor = new JMenuItem("Copy Cursor Offset");
        menuCopyCursor.addActionListener(userActions);
        if (buttonFont != null) {
            menuCopyCursor.setFont(buttonFont);
        }

        menuCopyDump = new JMenuItem("Copy Dump");
        menuCopyDump.addActionListener(userActions);
        if (buttonFont != null) {
            menuCopyDump.setFont(buttonFont);
        }

        menuCopyHex = new JMenuItem("Copy Hex");
        menuCopyHex.addActionListener(userActions);
        if (buttonFont != null) {
            menuCopyHex.setFont(buttonFont);
        }

        menuCopyText = new JMenuItem("Copy Text");
        menuCopyText.addActionListener(userActions);
        if (buttonFont != null) {
            menuCopyText.setFont(buttonFont);
        }

        menuDelete = new JMenuItem("Delete");
        menuDelete.addActionListener(userActions);
        if (buttonFont != null) {
            menuDelete.setFont(buttonFont);
        }
        menuDelete.setMnemonic(KeyEvent.VK_D);

        menuFind = new JMenuItem("Find...");
        menuFind.addActionListener(userActions);
        if (buttonFont != null) {
            menuFind.setFont(buttonFont);
        }
        menuFind.setMnemonic(KeyEvent.VK_F);

        menuGotoOffset = new JMenuItem("Go To File Offset...");
        menuGotoOffset.addActionListener(userActions);
        if (buttonFont != null) {
            menuGotoOffset.setFont(buttonFont);
        }
        menuGotoOffset.setMnemonic(KeyEvent.VK_G);

        menuNext = new JMenuItem("Find Next");
        menuNext.addActionListener(userActions);
        if (buttonFont != null) {
            menuNext.setFont(buttonFont);
        }
        menuNext.setMnemonic(KeyEvent.VK_N);

        menuPasteHex = new JMenuItem("Paste Hex");
        menuPasteHex.addActionListener(userActions);
        if (buttonFont != null) {
            menuPasteHex.setFont(buttonFont);
        }

        menuPasteText = new JMenuItem("Paste Text");
        menuPasteText.addActionListener(userActions);
        if (buttonFont != null) {
            menuPasteText.setFont(buttonFont);
        }

        menuReplace = new JMenuItem("Replace");
        menuReplace.addActionListener(userActions);
        if (buttonFont != null) {
            menuReplace.setFont(buttonFont);
        }
        menuReplace.setMnemonic(KeyEvent.VK_R);

        menuSelect = new JMenuItem("Select All");
        menuSelect.addActionListener(userActions);
        if (buttonFont != null) {
            menuSelect.setFont(buttonFont);
        }
        menuSelect.setMnemonic(KeyEvent.VK_A);

        /* Create a horizontal panel for options. */
        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

        fontNameDialog = new JComboBox(GraphicsEnvironment
                .getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontNameDialog.setEditable(false); // user must select one of our choices
        if (buttonFont != null) {
            fontNameDialog.setFont(buttonFont);
        }
        fontNameDialog.setSelectedItem(fontName); // select default font name
        fontNameDialog.setToolTipText("Font name for displayed text.");
        fontNameDialog.addActionListener(userActions);
        // do last so this doesn't fire early
        panel3.add(fontNameDialog);

        panel3.add(Box.createHorizontalStrut(30));

        dumpWidthDialog = new JComboBox(DUMP_WIDTHS);
        dumpWidthDialog.setEditable(false); // user must select one of our choices
        if (buttonFont != null) {
            dumpWidthDialog.setFont(buttonFont);
        }
        dumpWidthDialog.setSelectedItem(String.valueOf(dumpWidth));
        // selected item is our default size
        dumpWidthDialog.setToolTipText("Number of input bytes per dump line.");
        dumpWidthDialog.addActionListener(userActions);
        // do last so this doesn't fire early
        panel3.add(dumpWidthDialog);
        JLabel label1 = new JLabel("bytes per line");
        if (buttonFont != null) {
            label1.setFont(buttonFont);
        }
        panel3.add(label1);

        panel3.add(Box.createHorizontalStrut(20));

        overDialog = new JCheckBox("overwrite mode", overFlag);
        overDialog.addActionListener(userActions);
        if (buttonFont != null) {
            overDialog.setFont(buttonFont);
        }
        overDialog.setToolTipText("Select for overwrite, clear for insert mode.");
        panel3.add(overDialog);

        panel1.add(panel3);
        panel1.add(Box.createVerticalStrut(7));

        /* Put above boxed options in a panel that is centered horizontally. */
        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel4.add(panel1);

        /* Create a text area to display the hex dump and to accept user input.  To
         the right of that will be a vertical scroll bar that we control. */
        textPanel = new HexEdit2Text(this); // create display as special JPanel
        textPanel.setFocusable(true); // allow keyboard focus for dump display
        textPanel.setPreferredSize(new Dimension(panel4.getPreferredSize().width,
                (4 * panel4.getPreferredSize().height))); // 4 times button/option height

        textScroll = new JScrollBar(JScrollBar.VERTICAL, 0, 1, 0, 1);
        textScroll.addMouseWheelListener((MouseWheelListener) textPanel);
        textScroll.setEnabled(true);  // scroll bar always present, always enabled
        textScroll.setFocusable(true); // allow keyboard focus for scroll bar
        textScroll.getModel().addChangeListener((ChangeListener) textPanel);

        /* Create the main window frame for this application.  Stack buttons and
         options on top of the output text area.  Keep the display text in the
         center so that it expands horizontally and vertically. */
        mainFrame = new JFrame(PROGRAM_TITLE);
        Container panel6 = mainFrame.getContentPane(); // where content meets frame
        panel6.setLayout(new BorderLayout(5, 5));
        panel6.add(panel4, BorderLayout.NORTH); // buttons and options
        panel6.add(textPanel, BorderLayout.CENTER); // our panel for dump display
        panel6.add(textScroll, BorderLayout.EAST); // scroll bar for dump display

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocation(windowLeft, windowTop); // normal top-left corner
        if ((windowHeight < MIN_FRAME) || (windowWidth < MIN_FRAME)) {
            mainFrame.pack();           // do component layout with minimum size
        } else // the user has given us a window size
        {
            mainFrame.setSize(windowWidth, windowHeight); // size of normal window
        }
        if (maximizeFlag) {
            mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        mainFrame.validate();         // recheck application window layout
//  mainFrame.setVisible(true);   // and then show application window

        /* If a file name was given on the command line, then open that file as our
         starting data.  Otherwise, use a text string with copyright information. */
        if (fileName.length() > 0) // was there a file name on the command line?
        {
            openFile(new File(fileName)); // yes, read data bytes from that file
        } else // no file name given
        {
            byte[] array = COPYRIGHT_NOTICE.getBytes(); // use copyright string
            nibbleData = new HexEdit2Data(array.length * 2);
            // allocate data object to hold nibbles
            for (i = 0; i < array.length; i++) // do all bytes in copyright string
            {
                nibbleData.append((array[i] >> NIBBLE_SHIFT) & NIBBLE_MASK);
                // high-order nibble in byte
                nibbleData.append(array[i] & NIBBLE_MASK);
                // low-order nibble in byte
            }
            refreshDataSize();          // set to correct number of data nibbles
        }
        textPanel.beginFile();        // display file from the beginning

        /* Let the graphical interface run the application now. */
        mainFrame.setVisible(true);   // and then show application window        
    }
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
            JOptionPane.showMessageDialog(mainFrame, (givenFile.getName()
                    + " is a directory or folder.\nPlease select a normal file."));
            result = false;             // don't try to open this "file" for writing
        } else if (givenFile.isHidden()) // won't write to hidden (protected) files
        {
            JOptionPane.showMessageDialog(mainFrame, (givenFile.getName()
                    + " is a hidden or protected file.\nPlease select a normal file."));
            result = false;
        } else if (givenFile.isFile() == false) // are we creating a new file?
        {
            result = true;              // assume we can create new file by this name
        } else if (givenFile.canWrite()) // file exists, but can we write to it?
        {
            result = (JOptionPane.showConfirmDialog(mainFrame, (givenFile.getName()
                    + " already exists.\nDo you want to replace this with a new file?"))
                    == JOptionPane.YES_OPTION);
        } else // if we can't write to an existing file
        {
            JOptionPane.showMessageDialog(mainFrame, (givenFile.getName()
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
    static int charHexValue(char ch) {
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
            result = HEX_IGNORE;
        } else if (Character.isWhitespace(ch)) // blanks, spaces, tabs, newlines?
        {
            result = HEX_IGNORE;
        } else // illegal character
        {
            result = HEX_INVALID;
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

        text = "00000000" + Integer.toHexString(textPanel.cursorDot / 2)
                .toUpperCase();             // current cursor nibble as hex byte offset
        setClipboard(text.substring(text.length() - OFFSET_DIGITS));
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

        beginIndex = Math.min(textPanel.cursorDot, textPanel.cursorMark);
        endIndex = Math.max(textPanel.cursorDot, textPanel.cursorMark);
        if (beginIndex < endIndex) // only if there is a selection
        {
            lineLength = OFFSET_DIGITS + (4 * dumpWidth) + 5;
            // number of printable chars each dump line
            lineBuffer = new StringBuffer(lineLength + 1);
            // allocate line buffer, ends with newline
            lineBuffer.setLength(lineLength + 1); // force the correct buffer length
            lineBuffer.setCharAt(lineLength, '\n'); // put newline that never changes
            lineNibbles = 2 * dumpWidth; // maximum number of nibbles per dump line
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
                    lineBuffer.setCharAt((lineLength - dumpWidth - 2), MARKER_CHAR);
                    // insert left marker for ASCII text
                    lineBuffer.setCharAt((lineLength - 1), MARKER_CHAR); // right marker
                    shiftedOffset = (thisIndex / lineNibbles) * dumpWidth;
                    for (i = (OFFSET_DIGITS - 1); i >= 0; i--) // extract digits starting with low-order
                    {
                        lineBuffer.setCharAt(i, HEX_DIGITS[shiftedOffset & NIBBLE_MASK]);
                        // convert nibble to hex text digit
                        shiftedOffset = shiftedOffset >> NIBBLE_SHIFT;
                        // shift down next higher-order nibble
                    }
                    lineUsed = thisIndex % lineNibbles; // ignore leading unused digits
                    nextHex = lineUsed + (lineUsed / 2) + OFFSET_DIGITS + 2;
                    // where next hex digit goes
                    nextText = lineLength - dumpWidth - 1 + (lineUsed / 2);
                    // where next ASCII text goes
                }

                /* Place the hexadecimal digit for this nibble. */
                lineBuffer.setCharAt((nextHex++),
                        HEX_DIGITS[nibbleData.get(thisIndex)]);
                nextHex += thisIndex % 2; // insert extra space after second nibble
                lineUsed++;              // one more nibble placed on this dump line

                /* Place the ASCII text for a whole byte (two nibbles). */
                if ((thisIndex % 2) == 1) // for second nibble, construct full byte
                {
                    if ((thisIndex - 1) < beginIndex) // is first nibble in selection?
                    {
                        byteValue = REPLACE_CHAR; // incomplete byte means unprintable
                    } else // we have first and second nibble
                    {
                        byteValue = (nibbleData.get(thisIndex - 1) << NIBBLE_SHIFT)
                                | nibbleData.get(thisIndex); // construct byte from two nibbles
                        if ((byteValue < FIRST_CHAR) || (byteValue > LAST_CHAR)) {
                            byteValue = REPLACE_CHAR; // replace unprintable character
                        }
                    }
                    lineBuffer.setCharAt((nextText++), (char) byteValue); // show text
                } else if ((thisIndex + 1) >= endIndex) // first nibble, but alone?
                {
                    lineBuffer.setCharAt((nextText++), REPLACE_CHAR); // unprintable
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

        beginIndex = Math.min(textPanel.cursorDot, textPanel.cursorMark);
        endIndex = Math.max(textPanel.cursorDot, textPanel.cursorMark);
        if (beginIndex < endIndex) // only if there is a selection
        {
            result = new StringBuffer(); // start with an empty string buffer
            for (thisIndex = beginIndex; thisIndex < endIndex; thisIndex++) {
                result.append(HEX_DIGITS[nibbleData.get(thisIndex)]);
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

        beginIndex = Math.min(textPanel.cursorDot, textPanel.cursorMark);
        endIndex = Math.max(textPanel.cursorDot, textPanel.cursorMark);
        if (beginIndex < endIndex) // only if there is a selection
        {
            array = new byte[(endIndex - beginIndex + 1) / 2]; // round up byte size
            thisIndex = beginIndex;     // first nibble is high-order of first byte
            for (i = 0; i < array.length; i++) // create each byte from two nibbles
            {
                byteValue = nibbleData.get(thisIndex++) << NIBBLE_SHIFT;
                // can always get high-order nibble
                if (thisIndex < endIndex) // there may not be a low-order nibble
                {
                    byteValue |= nibbleData.get(thisIndex++);
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

        beginIndex = Math.min(textPanel.cursorDot, textPanel.cursorMark);
        endIndex = Math.max(textPanel.cursorDot, textPanel.cursorMark);
        if (beginIndex < endIndex) // only if there is a selection
        {
            for (thisIndex = beginIndex; thisIndex < endIndex; thisIndex++) {
                nibbleData.delete(beginIndex); // delete shuffles, always <beginIndex>
            }
            textPanel.cursorDot = textPanel.cursorMark = beginIndex;
            // selection is gone, reset cursor
            textPanel.limitCursorRange(); // refresh data size, enforce cursor range
            textPanel.makeVisible(textPanel.cursorDot);
            // make sure that user can see cursor
            textPanel.adjustScrollBar(); // adjust scroll bar to match new position
            textPanel.repaint();        // redraw text display as necessary
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
        input = gotoOffsetText.getText(); // get user's string from dialog box
        length = input.length();      // get size of input string in characters
        offset = 0;                   // start offset from zero as we add digits
        for (i = 0; i < length; i++) // do all characters in the string
        {
            ch = input.charAt(i);       // get one character from input string
            hexValue = charHexValue(ch); // convert character to value of hex digit
            if (offset > nibbleCount) // crude (very crude) limit on upper range
            {
                digits = -1;              // our way of saying that hex string is bad
                break;                    // exit early from <for> loop
            } else if (hexValue >= 0) // was it a valid hexadecimal digit?
            {
                digits++;                // one more hexadecimal digit accepted
                offset = (offset << NIBBLE_SHIFT) | hexValue; // append digit to offset
            } else if (hexValue == HEX_IGNORE) // ignore spaces and punctuation?
            { /* do nothing */ } else // illegal character
            {
                digits = -1;              // another way of saying that input is bad
                break;                    // exit early from <for> loop
            }
        }

        /* Check that the resulting binary value is within actual file range. */
        if ((digits < 1) || (offset > (nibbleCount / 2))) {
            showGotoRange(nibbleCount / 2); // repeat message about range allowed
            return;                     // can't jump to an imaginary file offset!
        }

        /* Position the display to the correct file (and dump panel) offset, about
         1/3 of the way down from the top, if possible. */
        gotoStatus.setText(EMPTY_STATUS); // less confusing if clear status message
        mainFrame.setVisible(true);   // bring main frame in front of dialog box
        textPanel.cursorDot = textPanel.cursorMark = (int) (offset * 2);
        // position cursor at user's exact offset
        textPanel.limitCursorRange(); // refresh data size, enforce cursor range
        textPanel.panelOffset = ((int) offset) - (textPanel.panelDumpWidth
                * (textPanel.panelRows / 3)); // approximate starting panel offset
        textPanel.adjustScrollBar();  // clean up offset and adjust scroll bars
        textPanel.repaint();          // redraw text display as necessary

    } // end of gotoFileOffset() method


    /*
     memoryError() method

     Produce a dialog box with a message when there is insufficient memory to
     complete an operation.
     */
    public void memoryError(String text) {
        JOptionPane.showMessageDialog(mainFrame,
                ("Not enough memory available for " + text + " operation."));
    }


    /*
     openFile() method

     Ask the user for an input file name and copy the contents of that file to our
     nibble data.
     */
    public void openFile(File givenFile) {
        byte[] buffer;                // input buffer (faster than byte-by-byte)
        int i;                        // index variable
        File inputFile;               // user's selected input file
        long inputSize;               // total size of input file in bytes
        FileInputStream inputStream;  // input file stream
        int length;                   // actual number of bytes read

        /* Clear the nibble counter so that the data looks empty, until after we
         finish opening a file.  This prevents the text display from throwing an
         exception while painting data that isn't there yet.  The nibble counter
         will be reset properly with a call to refreshDataSize(). */
        nibbleCount = 0;              // make data look empty, without losing data

        /* Ask the user for an input file name, if we weren't already given a file
         by our caller. */
        if (givenFile == null) {
            fileChooser.setDialogTitle("Open File...");
            if (fileChooser.showOpenDialog(mainFrame) != JFileChooser.APPROVE_OPTION) {
                refreshDataSize();        // bring back previous nibble data and size
                return;                   // user cancelled file selection dialog box
            }
            inputFile = fileChooser.getSelectedFile(); // get user's input file
        } else {
            inputFile = givenFile;      // caller gave us a file, so use that instead
        }
        /* Warn the user if the file is larger than what we are able to handle. */

        inputSize = inputFile.length(); // get total number of bytes for input file
        if (inputSize > 0x3FFF0000L) // just short of one gigabyte
        {
            JOptionPane.showMessageDialog(mainFrame,
                    ("This program can't open files larger than one gigabyte.\n"
                    + inputFile.getName() + " has " + formatComma.format(inputSize)
                    + " bytes."));
            refreshDataSize();          // bring back previous nibble data and size
            return;                     // we can't open this file, so give up
        } else if (inputSize > 99999999L) // just short of 100 megabytes
        {
            if (JOptionPane.showConfirmDialog(mainFrame,
                    ("Files larger than 100 megabytes may be slow.\n"
                    + inputFile.getName() + " has " + formatComma.format(inputSize)
                    + " bytes.\nDo you want to open this file anyway?"),
                    "Large File Warning", JOptionPane.YES_NO_CANCEL_OPTION)
                    != JOptionPane.YES_OPTION) {
                refreshDataSize();        // bring back previous nibble data and size
                return;                   // user doesn't want to open the new file
            }
        }

        /* Read 8-bit data bytes from the input file and convert to pairs of 4-bit
         nibbles.  Since we are reading whole bytes, there is always an even number
         of nibbles. */
        try // catch file I/O errors, memory allocation
        {
            buffer = new byte[BUFFER_SIZE]; // allocate byte buffer for input
            inputStream = new FileInputStream(inputFile);
            // try to open input file
            nibbleData = new HexEdit2Data(2 * (int) inputSize);
            // allocate nibble data object from file size
            while ((length = inputStream.read(buffer, 0, BUFFER_SIZE)) > 0) {
                for (i = 0; i < length; i++) {
                    nibbleData.append((buffer[i] >> NIBBLE_SHIFT) & NIBBLE_MASK);
                    // high-order nibble in byte
                    nibbleData.append(buffer[i] & NIBBLE_MASK);
                    // low-order nibble in byte
                }
            }
            inputStream.close();        // try to close input file
        } catch (IOException ioe) // most likely I/O error is "file not found"
        {
            nibbleData = new HexEdit2Data(0); // substitute an empty data object
            JOptionPane.showMessageDialog(mainFrame,
                    ("Can't read from input file:\n" + ioe.getMessage()));
        } catch (OutOfMemoryError oome) // not enough memory to open the file
        {
            nibbleData = new HexEdit2Data(0); // substitute an empty data object
            JOptionPane.showMessageDialog(mainFrame,
                    ("Not enough memory to open this file.\n"
                    + inputFile.getName() + " has " + formatComma.format(inputSize)
                    + " bytes.\nTry increasing the Java heap size with the -Xmx option."));
        }
        refreshDataSize();            // set to correct number of data nibbles

    } // end of openFile() method


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
            } else if (hexValue == HEX_IGNORE) // ignore spaces and punctuation?
            { /* do nothing */ } else // illegal character
            {
                JOptionPane.showMessageDialog(mainFrame,
                        ("Clipboard string must be hexadecimal digits or spaces; found "
                        + (Character.isISOControl(ch) ? "" : ("\"" + ch + "\" or ")) + "0x"
                        + Integer.toHexString(ch).toUpperCase() + "."));
                return;                   // return early: cancel the paste operation
            }
        }
        pasteNibbles(nibbles, used, overFlag); // paste nibbles as file data

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

            beginIndex = Math.min(textPanel.cursorDot, textPanel.cursorMark);
            endIndex = Math.max(textPanel.cursorDot, textPanel.cursorMark);

            if ((beginIndex < endIndex) && (used != (endIndex - beginIndex))) {
                JOptionPane.showMessageDialog(mainFrame, ("Overwrite selection ("
                        + (endIndex - beginIndex) + ") and clipboard (" + used
                        + ") have different sizes."));
                return;                   // return early: cancel the paste operation
            }

            textPanel.cursorDot = beginIndex; // start replacing here
            for (i = 0; i < used; i++) // replace with nibbles given by caller
            {
                nibbleData.put((textPanel.cursorDot++), array[i]);
            }
        } else // must be insert mode
        {
            /* Clipboard inserts starting at the cursor.  Any selection is replaced
             by first deleting the selection, then inserting the clipboard. */

            deleteSelected();           // delete current selection, if any
            for (i = 0; i < used; i++) // insert all nibbles given by caller
            {
                nibbleData.insert((textPanel.cursorDot++), array[i]);
            }
        }

        textPanel.cursorMark = textPanel.cursorDot;
        // position cursor after inserted bytes
        textPanel.limitCursorRange(); // refresh data size, enforce cursor range
        textPanel.makeVisible(textPanel.cursorDot);
        // make sure that user can see cursor
        textPanel.adjustScrollBar();  // adjust scroll bar to match new position
        textPanel.repaint();          // redraw text display as necessary

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
            nibbles[used++] = (bytes[i] >> NIBBLE_SHIFT) & NIBBLE_MASK;
            // high-order nibble in byte
            nibbles[used++] = bytes[i] & NIBBLE_MASK;
            // low-order nibble in byte
        }
        pasteNibbles(nibbles, used, overFlag); // paste nibbles as file data

    } // end of pasteText() method


    /*
     refreshDataSize() method

     Recalculate the total number of data nibbles, after insertions and deletions.
     */
    public void refreshDataSize() {
        nibbleCount = nibbleData.size(); // refetch total number of data nibbles
    }


    /*
     saveFile() method

     Ask the user for an output file name, create or replace that file, and copy
     the contents of our nibble data to that file.
     */
    public void saveFile() {
        byte[] buffer;                // output buffer (faster than byte-by-byte)
        int i;                        // index variable
        int length;                   // number of bytes in output buffer
        File outputFile;              // user's selected output file
        FileOutputStream outputStream; // output file stream

        /* Ask the user for an output file name. */
        fileChooser.setDialogTitle("Save File...");
        if (fileChooser.showSaveDialog(mainFrame) != JFileChooser.APPROVE_OPTION) {
            return;                     // user cancelled file selection dialog box
        }
        outputFile = fileChooser.getSelectedFile(); // get user's output file

        /* Convert pairs of 4-bit data nibbles to 8-bit bytes and write to the
         output file.  There may be an odd number of nibbles; in which case, assume
         a zero for the final nibble. */
        try // catch file I/O errors
        {
            if (canWriteFile(outputFile)) // if writing this file seems safe
            {
                buffer = new byte[BUFFER_SIZE]; // allocate byte buffer for output
                length = 0;               // nothing in output buffer yet
                outputStream = new FileOutputStream(outputFile);
                // try to open output file
                refreshDataSize();        // refresh total number of nibbles

                i = 0;                    // start with first data nibble
                while (i < nibbleCount) // do all nibbles
                {
                    if (length >= BUFFER_SIZE) // time to empty the output buffer?
                    {
                        outputStream.write(buffer); // yes, write entire buffer
                        length = 0;           // and now there is nothing in the buffer
                    }
                    buffer[length] = (byte) (nibbleData.get(i++) << NIBBLE_SHIFT);
                    // can always get high-order nibble
                    if (i < nibbleCount) // there may not be a low-order nibble
                    {
                        buffer[length] |= (byte) nibbleData.get(i++);
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
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(mainFrame,
                    ("Can't write to output file:\n" + ioe.getMessage()));
        }
    } // end of saveFile() method


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

        if (searchIsHex.isSelected()) // is this a hex search?
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
                } else if (hexValue == HEX_IGNORE) // ignore spaces and punctuation?
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
                result[used++] = (bytes[i] >> NIBBLE_SHIFT) & NIBBLE_MASK;
                // high-order nibble in byte
                result[used++] = bytes[i] & NIBBLE_MASK;
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
        searchFindNext(Math.max(textPanel.cursorDot, textPanel.cursorMark));
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

        if (searchDialog == null) // has the search dialog been created?
        {
            showSearchDialog();         // be nice and start the find/replace dialog
            return;
        }
        text = searchFindText.getText(); // get user's string
        if (text.length() == 0) // did the user type anything?
        {
            showSearchDialog();         // bring up the full find/replace dialog
            showSearchMessage("Empty strings are found everywhere.  (Joke.)");
            searchFindText.requestFocusInWindow(); // we need the search string
            return;
        }
        nibbles = searchConvertNibbles(text, "search");
        if (nibbles.length == 0) // was there an error during conversion?
        {
            return;                     // yes, error message already printed
        }
        /* If we got this far, there is nothing to report and we should cancel any
         previous status message in the search dialog box. */

        searchStatus.setText(EMPTY_STATUS); // clear any previous search status

        /* We start looking at the location given by the caller, which is usually
         after the current selection. */
        byteFlag = searchByteBound.isSelected(); // true if start on byte boundary
        matchFlag = false;            // no match found yet
        nullFlag = searchIgnoreNulls.isSelected() && ((nibbles.length % 2) == 0);
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

        while ((nibbleCount - start) >= nibbles.length) // keep looking
        {
            /* First try the search string as-is.  This is a straight nibble-by-
             nibble comparison, no matter what the options may be. */

            boolean differFlag = false; // assume that comparison is successful
            for (i = 0; i < nibbles.length; i++) {
                if (nibbles[i] != nibbleData.get(start + i)) {
                    differFlag = true;      // comparison has failed
                    break;                  // escape early from inner <for> loop
                }
            }
            if (differFlag == false) // was the comparison successful?
            {
                matchFlag = true;         // indicate that we were successful
                textPanel.cursorMark = start; // yes, set start of selection
                textPanel.cursorDot = start + nibbles.length; // set end of selection
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
                while ((dataIndex < (nibbleCount - 1)) // while there are data bytes
                        && (findIndex < (nibbles.length - 1))) // and there are search bytes
                {
                    int dataByte = (nibbleData.get(dataIndex) << NIBBLE_SHIFT)
                            | nibbleData.get(dataIndex + 1);
                    // construct one byte of nibble data
                    int findByte = (nibbles[findIndex] << NIBBLE_SHIFT)
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
                    textPanel.cursorMark = start; // yes, set start of selection
                    textPanel.cursorDot = dataIndex; // set end of selection
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
            textPanel.makeVisible(textPanel.cursorMark);
            // do try to show start of selection
            textPanel.makeVisible(textPanel.cursorDot);
            // but end of selection is more important
            textPanel.adjustScrollBar(); // adjust scroll bar to match new position
            textPanel.repaint();        // redraw text display as necessary
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

        if (searchDialog == null) // has the search dialog been created?
        {
            showSearchDialog();         // be nice and start the find/replace dialog
            if (textPanel.cursorDot != textPanel.cursorMark) // if selection exists
            {
                searchReplaceText.requestFocusInWindow(); // only need replace string
            }
            return;
        }
        if (textPanel.cursorDot == textPanel.cursorMark) // is there a selection?
        {
            showSearchMessage("There is no selection to replace.");
            return;                     // nothing to do, so return to caller
        }
        text = searchReplaceText.getText(); // get user's string
        if (text.length() == 0) // did the user type anything?
        {
            showSearchDialog();         // bring up the full find/replace dialog
            showSearchMessage("Replacement with an empty string is not supported.");
            searchReplaceText.requestFocusInWindow(); // we need the replace string
            return;
        }
        nibbles = searchConvertNibbles(text, "replace");
        if (nibbles.length == 0) // was there an error during conversion?
        {
            return;                     // yes, error message already printed
        }
        /* If we got this far, there is nothing to report and we should cancel any
         previous status message in the search dialog box. */

        searchStatus.setText(EMPTY_STATUS); // clear any previous search status

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
        textPanel.cursorDot = nibbleCount; // current cursor ends selection
        textPanel.cursorMark = 0;     // select data from beginning of file
        textPanel.repaint();          // redraw text display as necessary
    }


    /*
     setClipboard() method

     Place a string onto the clipboard for some other application to read.
     */
    public void setClipboard(String text) {
        clipString = text;            // save string reference for later
        try // clipboard may not be available
        {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                    (Transferable) userActions, null); // place data notice on clipboard
        } catch (IllegalStateException ise) {
            JOptionPane.showMessageDialog(mainFrame,
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
        boolean content = nibbleCount > 0; // true if there is any file data
        boolean selection = textPanel.cursorDot != textPanel.cursorMark;
        // true if there is a current selection

        menuPopup = new JPopupMenu();

        menuCopyDump.setEnabled(selection);
        menuPopup.add(menuCopyDump);
        menuCopyHex.setEnabled(selection);
        menuPopup.add(menuCopyHex);
        menuCopyText.setEnabled(selection);
        menuPopup.add(menuCopyText);
        menuPasteHex.setEnabled(clipboard);
        menuPopup.add(menuPasteHex);
        menuPasteText.setEnabled(clipboard);
        menuPopup.add(menuPasteText);

        menuPopup.addSeparator();

        menuFind.setEnabled(content);
        menuPopup.add(menuFind);
        menuNext.setEnabled(content);
        menuPopup.add(menuNext);
        menuReplace.setEnabled(selection);
        menuPopup.add(menuReplace);

        menuPopup.addSeparator();

        menuDelete.setEnabled(selection);
        menuPopup.add(menuDelete);
        menuSelect.setEnabled(content);
        menuPopup.add(menuSelect);

        if (mouse == false) // Edit button only, not right mouse click
        {
            menuPopup.addSeparator();
            menuCopyCursor.setEnabled(true);
            menuPopup.add(menuCopyCursor);
            menuGotoOffset.setEnabled(content);
            menuPopup.add(menuGotoOffset);
        }

        menuPopup.show(invoker, x, y); // show menu with context-sensitive items

    } // end of showEditMenu() method


    /*
     showGotoDialog() method

     Show the "Go To File Offset" dialog box.  We may have to create it first.
     This is a much simpler layout and easier interaction than <searchDialog>.
     */
    public void showGotoDialog() {
        if (gotoDialog == null) // has the dialog box been created yet?
        {
            /* Create a vertical box to stack buttons and options. */

            JPanel panel1 = new JPanel();
            panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));

            /* First layout line has the "go to" address input string. */
            JPanel panel2 = new JPanel(new BorderLayout(10, 0));

            JLabel label1 = new JLabel("Go to file offset:", JLabel.RIGHT);
            if (buttonFont != null) {
                label1.setFont(buttonFont);
            }
            panel2.add(label1, BorderLayout.WEST);

            gotoOffsetText = new JTextField("", 15);
            gotoOffsetText.addActionListener(userActions);
            if (buttonFont != null) {
                gotoOffsetText.setFont(buttonFont);
            }
            gotoOffsetText.setMargin(TEXT_MARGINS);
            panel2.add(gotoOffsetText, BorderLayout.CENTER);
            panel1.add(panel2);
            panel1.add(Box.createVerticalStrut(10)); // vertical space

            /* Second layout line has a message string for the "go to" status. */
            JPanel panel3 = new JPanel(new BorderLayout(0, 0));
            gotoStatus = new JLabel(EMPTY_STATUS, JLabel.CENTER);
            if (buttonFont != null) {
                gotoStatus.setFont(buttonFont);
            }
            showGotoRange(-1);          // set message size with maximum hex digits
            panel3.add(gotoStatus, BorderLayout.CENTER);
            panel1.add(panel3);
            panel1.add(Box.createVerticalStrut(20));

            /* Third and last line has the action buttons. */
            JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));

            gotoJumpButton = new JButton("Go");
            gotoJumpButton.addActionListener(userActions);
            if (buttonFont != null) {
                gotoJumpButton.setFont(buttonFont);
            }
            gotoJumpButton.setMnemonic(KeyEvent.VK_G);
            gotoJumpButton.setToolTipText("Jump to file offset given above.");
            panel4.add(gotoJumpButton);

            gotoCloseButton = new JButton("Close");
            gotoCloseButton.addActionListener(userActions);
            if (buttonFont != null) {
                gotoCloseButton.setFont(buttonFont);
            }
            gotoCloseButton.setMnemonic(KeyEvent.VK_C);
            gotoCloseButton.setToolTipText("Close this dialog box.");
            panel4.add(gotoCloseButton);

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
            gotoDialog = new JDialog((Frame) null, "Go To File Offset");
            gotoDialog.getContentPane().add(panel6, BorderLayout.CENTER);
            gotoDialog.pack();          // lay out components, set preferred size
            gotoDialog.setLocation(mainFrame.getX() + 50, mainFrame.getY() + 50);

            /* The status message controls the layout width, so fix the size, to
             avoid the input field changing size when the message text changes. */
            gotoStatus.setPreferredSize(gotoStatus.getPreferredSize());
        }

        refreshDataSize();            // set to correct number of data nibbles
        showGotoRange(nibbleCount / 2); // default to message about range allowed
        if (gotoDialog.isVisible() == false) // if dialog is closed or hidden
        {
            gotoOffsetText.requestFocusInWindow();
        }
        // assume user wants to edit offset string
        gotoDialog.setVisible(true);  // show "go to" dialog or bring to the front

    } // end of showGotoDialog() method


    /*
     showGotoRange() method

     Set the status in the "Go To File Offset" dialog box to an informational
     message about the hexadecimal range allowed.
     */
    public void showGotoRange(int maxValue) {
        gotoStatus.setText("Enter a byte offset in hexadecimal from 0 to "
                + Integer.toHexString(maxValue).toUpperCase() + " and click \"Go\".");
    }


    /*
     showHelp() method

     Show the help summary.  This is a UNIX standard and is expected for all
     console applications, even very simple ones.
     */
    static void showHelp() {
        System.err.println();
        System.err.println(PROGRAM_TITLE);
        System.err.println();
        System.err.println("This is a graphical application.  You may give options and/or one file name on");
        System.err.println("the command line.  Options are:");
        System.err.println();
        System.err.println("  -? = print this help summary");
        System.err.println("  -d4 -d8 -d12 -d16 -d24 -d32 = input bytes per dump line (default: "
                + DEFAULT_DUMP + ")");
        System.err.println("  -ins = keyboard input starts with insert mode (default)");
        System.err.println("  -over = keyboard input starts with overwrite mode");
        System.err.println("  -u# = font size for buttons, dialogs, etc; default is local system;");
        System.err.println("      example: -u16");
        System.err.println("  -w(#,#,#,#) = normal window position: left, top, width, height;");
        System.err.println("      example: -w(50,50,700,500)");
        System.err.println("  -x = maximize application window; default is normal window");
        System.err.println();
        System.err.println(COPYRIGHT_NOTICE);
//  System.err.println();

    } // end of showHelp() method


    /*
     showSearchDialog() method

     Show the "Find or Replace" dialog box.  We may have to create it first.  This
     layout is more complicated than the "Go To File Offset" dialog.  Also, user
     keystrokes (Control-F, Control-N, etc) may invoke methods related to dialog
     components before the dialog box has actually been created.
     */
    public void showSearchDialog() {
        GridBagConstraints gbc;       // reuse the same constraint object

        if (searchDialog == null) // has the dialog box been created yet?
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
            if (buttonFont != null) {
                label1.setFont(buttonFont);
            }
            gbc.anchor = GridBagConstraints.EAST;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridwidth = 1;
            panel1.add(label1, gbc);
            panel1.add(Box.createHorizontalStrut(10), gbc); // horizontal space

            searchFindText = new JTextField("", 20);
            searchFindText.addActionListener(userActions);
            if (buttonFont != null) {
                searchFindText.setFont(buttonFont);
            }
            searchFindText.setMargin(TEXT_MARGINS);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel1.add(searchFindText, gbc);
            panel1.add(Box.createVerticalStrut(10), gbc); // vertical space

            /* Second layout line has the "replace" string. */
            JLabel label2 = new JLabel("Replace with:");
            if (buttonFont != null) {
                label2.setFont(buttonFont);
            }
            gbc.anchor = GridBagConstraints.EAST;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridwidth = 1;
            panel1.add(label2, gbc);
            panel1.add(Box.createHorizontalStrut(10), gbc);

            searchReplaceText = new JTextField("", 20);
            searchReplaceText.addActionListener(userActions);
            if (buttonFont != null) {
                searchReplaceText.setFont(buttonFont);
            }
            searchReplaceText.setMargin(TEXT_MARGINS);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel1.add(searchReplaceText, gbc);
            panel1.add(Box.createVerticalStrut(10), gbc);

            /* Third layout line has the options: radio buttons and check boxes.  Not
             all of these require action listeners. */
            ButtonGroup group1 = new ButtonGroup();
            JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

            searchIsHex = new JRadioButton("hex string", true);
            if (buttonFont != null) {
                searchIsHex.setFont(buttonFont);
            }
            searchIsHex.setMnemonic(KeyEvent.VK_H);
            searchIsHex.setToolTipText(
                    "Search and replace strings are hexadecimal digits.");
            group1.add(searchIsHex);
            panel3.add(searchIsHex);

            searchIsText = new JRadioButton("text string", false);
            if (buttonFont != null) {
                searchIsText.setFont(buttonFont);
            }
            searchIsText.setMnemonic(KeyEvent.VK_T);
            searchIsText.setToolTipText(
                    "Search and replace strings are regular text.");
            searchIsText.addActionListener(userActions);
            // do last so this doesn't fire early
            group1.add(searchIsText);
            panel3.add(searchIsText);

            searchByteBound = new JCheckBox("byte boundary", false);
            if (buttonFont != null) {
                searchByteBound.setFont(buttonFont);
            }
            searchByteBound.setMnemonic(KeyEvent.VK_B);
            searchByteBound.setToolTipText(
                    "Search starts on byte boundary.  Does not apply to replace.");
            panel3.add(searchByteBound);

            searchIgnoreNulls = new JCheckBox("ignore nulls", false);
            if (buttonFont != null) {
                searchIgnoreNulls.setFont(buttonFont);
            }
            searchIgnoreNulls.setMnemonic(KeyEvent.VK_I);
            searchIgnoreNulls.setToolTipText(
                    "Ignore null data bytes between search bytes.");
            panel3.add(searchIgnoreNulls);

            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel1.add(panel3, gbc);
            panel1.add(Box.createVerticalStrut(10), gbc);

            /* Fourth layout line has a message string for the search status. */
            searchStatus = new JLabel(EMPTY_STATUS, JLabel.CENTER);
            if (buttonFont != null) {
                searchStatus.setFont(buttonFont);
            }
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            panel1.add(searchStatus, gbc);
            panel1.add(Box.createVerticalStrut(20), gbc);

            /* Fifth and last line has the action buttons. */
            JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));

            searchFindButton = new JButton("Find First");
            searchFindButton.addActionListener(userActions);
            if (buttonFont != null) {
                searchFindButton.setFont(buttonFont);
            }
            searchFindButton.setMnemonic(KeyEvent.VK_F);
            searchFindButton.setToolTipText(
                    "Find first occurrence of search string in file.");
            panel4.add(searchFindButton);

            searchNextButton = new JButton("Find Next");
            searchNextButton.addActionListener(userActions);
            if (buttonFont != null) {
                searchNextButton.setFont(buttonFont);
            }
            searchNextButton.setMnemonic(KeyEvent.VK_N);
            searchNextButton.setToolTipText(
                    "Find next occurrence of search string.");
            panel4.add(searchNextButton);

            searchReplaceButton = new JButton("Replace");
            searchReplaceButton.addActionListener(userActions);
            if (buttonFont != null) {
                searchReplaceButton.setFont(buttonFont);
            }
            searchReplaceButton.setMnemonic(KeyEvent.VK_R);
            searchReplaceButton.setToolTipText(
                    "Replace current selection or previously found string.");
            panel4.add(searchReplaceButton);

            searchCloseButton = new JButton("Close");
            searchCloseButton.addActionListener(userActions);
            if (buttonFont != null) {
                searchCloseButton.setFont(buttonFont);
            }
            searchCloseButton.setMnemonic(KeyEvent.VK_C);
            searchCloseButton.setToolTipText("Close this dialog box.");
            panel4.add(searchCloseButton);

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
            searchDialog = new JDialog((Frame) null, "Find or Replace");
            searchDialog.getContentPane().add(panel6, BorderLayout.CENTER);
            searchDialog.pack();        // lay out components, set preferred size
            if (mainFrame != null) {
                searchDialog.setLocation(mainFrame.getX() + 50, mainFrame.getY() + 50);
            }
        }

        searchStatus.setText(EMPTY_STATUS); // clear any previous search status
        if (searchDialog.isVisible() == false) // if dialog is closed or hidden
        {
            searchFindText.requestFocusInWindow();
        }
        // assume user wants to edit search string
        searchDialog.setVisible(true); // show search dialog or bring to the front

    } // end of showSearchDialog() method


    /*
     showSearchMessage() method

     Display a text string given by the caller.  If the search dialog is open,
     then use the message field in the dialog.  Otherwise, use a pop-up dialog.
     */
    public void showSearchMessage(String text) {
        if ((searchDialog != null) && searchDialog.isVisible()) {
            searchStatus.setText(text); // place search result in message field
            searchDialog.setVisible(true); // bring search dialog to the front
        } else {
            JOptionPane.showMessageDialog(mainFrame, text); // simpler pop-up dialog
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
            if (source == dumpWidthDialog) // number of input bytes per dump line
            {
                /* We can safely parse the dump width as an integer, because we supply
                 the only choices allowed, and the user can't edit this dialog field.
                 Calling adjustScrollBar() or makeVisible() is not possible here,
                 because display sizes are unknown until after the first redraw. */

                dumpWidth = Integer.parseInt((String) dumpWidthDialog
                        .getSelectedItem());    // convert text width to integer
                textPanel.repaint();      // redraw text display as necessary
            } else if (source == exitButton) // "Exit" button
            {
                System.exit(0);           // always exit with zero status from GUI
            } else if (source == fontNameDialog) // font name for display text area
            {
                /* We can safely assume that the font name is valid, because we
                 obtained the names from getAvailableFontFamilyNames(), and the user
                 can't edit this dialog field.  Calling adjustScrollBar() or
                 makeVisible() is not possible here, because display sizes are unknown
                 until after the first redraw. */

                fontName = (String) fontNameDialog.getSelectedItem();
                textPanel.repaint();      // redraw text display as necessary
            } else if (source == gotoCloseButton) // "Close" button on "go to" dialog
            {
                gotoDialog.setVisible(false); // hide "Go To File Offset" dialog box
            } else if (source == gotoJumpButton) // "Go" button on "Go To File Offset"
            {
                gotoFileOffset();         // call common method for this operation
            } else if (source == gotoOffsetText) // press Enter on "go to" offset text
            {
                gotoFileOffset();         // call common method for this operation
            } else if (source == menuButton) // "Edit Menu" button
            {
                showEditMenu(menuButton, 0, menuButton.getHeight(), false);
            } else if (source == menuCopyCursor) // "Copy Cursor Offset" menu item
            {
                copyCursor();             // call common method for this operation
            } else if (source == menuCopyDump) // "Copy Dump" menu item (selection)
            {
                copyDump();               // call common method for this operation
            } else if (source == menuCopyHex) // "Copy Hex" menu item (selection)
            {
                copyHex();                // call common method for this operation
            } else if (source == menuCopyText) // "Copy Text" menu item (selection)
            {
                copyText();               // call common method for this operation
            } else if (source == menuDelete) // "Delete" menu item (selection)
            {
                deleteSelected();         // call common method for this operation
            } else if (source == menuFind) // "Find" menu item
            {
                showSearchDialog();       // call common method for this operation
            } else if (source == menuGotoOffset) // "Go To File Offset" menu item
            {
                showGotoDialog();         // call common method for this operation
            } else if (source == menuNext) // "Find Next" menu item
            {
                searchFindNext();         // call common method for this operation
            } else if (source == menuPasteHex) // "Paste Hex" menu item
            {
                pasteHex();               // call common method for this operation
            } else if (source == menuPasteText) // "Paste Text" menu item
            {
                pasteText();              // call common method for this operation
            } else if (source == menuReplace) // "Replace" menu item (selection)
            {
                searchReplaceThis();      // call common method for this operation
            } else if (source == menuSelect) // "Select All" menu item
            {
                selectAll();              // call common method for this operation
            } else if (source == openButton) // "Open File" button
            {
                openFile(null);           // ask for file name, read data from file
                textPanel.beginFile();    // display file from the beginning
            } else if (source == overDialog) // insert versus overwrite input mode
            {
                overFlag = overDialog.isSelected(); // transfer GUI to faster boolean
                textPanel.repaint();      // redraw text display as necessary
            } else if (source == saveButton) // "Save File" button
            {
                saveFile();               // ask for file name, write data to file
            } else if (source == searchCloseButton) // "Close" button on search dialog
            {
                searchDialog.setVisible(false); // hide search dialog box
            } else if (source == searchFindButton) // "Find First" button on search
            {
                searchFindFirst();        // call common method for this operation
            } else if (source == searchFindText) // press Enter on search text
            {
                searchFindNext();         // call common method for this operation
            } else if (source == searchIsText) // user wants text search, not hex
            {
                if (searchIsText.isSelected()) // selecting text search turns on the
                {
                    searchByteBound.setSelected(true); // ... byte boundary by default
                }
            } else if (source == searchNextButton) // "Find Next" button on search
            {
                searchFindNext();         // call common method for this operation
            } else if (source == searchReplaceButton) // "Replace" button on search
            {
                searchReplaceThis();      // call common method for this operation
            } else if (source == searchReplaceText) // press Enter on replacement text
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

} // end of HexEdit2 class

/* Copyright (c) 2008 by Keith Fenske.  Released under GNU Public License. */
