/*
  HexEdit2Text class
  Written by: Keith Fenske, http://www.psc-consulting.ca/fenske/
  Monday, 27 October 2008
  Java class name: HexEdit2Text.java
  This class is part of HexEdit2
  Copyright (c) 2008 by Keith Fenske.  Released under GNU Public License.

  This class draws the display text area and listens to input from the user.
  The text is monospaced (fixed width) like an old video terminal, as are the
  colors.

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
*/

package hexedit;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class HexEdit2Text extends JPanel
  implements ChangeListener, KeyListener, MouseListener, MouseMotionListener,
    MouseWheelListener
{
  /* constants */

//  static final Color ACTIVE_CURSOR = Color.WHITE; // color for active cursor
//  static final Color ACTIVE_SELECT = new Color(51, 51, 153); // active selection
  static final int MAX_FONT_SIZE = 73; // maximum font size in points (plus one)
  static final int MIN_FONT_SIZE = 10; // minimum font size in points
//  static final Color PANEL_COLOR = Color.BLACK; // panel background color
  static final int PANEL_MARGIN = 10; // outside margin of panel in pixels
//  static final Color SHADOW_CURSOR = new Color(102, 102, 102);
                                  // color for mirrored or shadow cursor
//  static final Color SHADOW_SELECT = new Color(51, 51, 51);
                                  // color for mirrored or shadow selection
//  static final Color TEXT_COLOR = new Color(51, 255, 51); // all displayed text

  static final Color ACTIVE_CURSOR = Color.BLACK; // color for active cursor
  static final Color ACTIVE_SELECT = new Color(255, 255, 51); // active selection
  static final Color PANEL_COLOR = Color.WHITE; // panel background color
  static final Color SHADOW_CURSOR = new Color(182, 182, 182);
  static final Color SHADOW_SELECT = new Color(192, 192, 192);
  static final Color TEXT_COLOR = Color.BLACK; // all displayed text

  /* instance variables */

  int charShifts[];               // pixel offset to center ASCII characters
  int charWidths[];               // pixel width of each ASCII character
  int cursorDot;                  // nibble index for current cursor position
  int cursorMark;                 // nibble index of starting cursor selection
  boolean cursorOnText;           // active cursor: false hex dump, true text
  int lineAscent;                 // number of pixels above baseline
  int lineHeight;                 // height of each display line in pixels
  int maxWidth;                   // maximum pixel width of ASCII characters
  int mousePressNibble;           // mouse pressed: converted nibble index
  boolean mousePressOnText;       // mouse pressed: false hex dump, true text
  int mouseTempNibble;            // mouse temporary: converted nibble index
  boolean mouseTempOnText;        // mouse temporary: false hex dump, true text
  int panelColumns;               // number of complete text columns displayed
  int panelDumpWidth;             // number of input bytes per dump line
  Font panelFont;                 // saved font for drawing text on this panel
  String panelFontName;           // saved font name for <panelFont>
  int panelFontSize;              // saved font size for <panelFont>
  int panelHeight, panelWidth;    // saved panel height and width in pixels
  int panelOffset;                // file offset in bytes for first display row
  int panelRows;                  // number of complete lines (rows) displayed

  /* class constructor */

  public HexEdit2Text()
  {
    super();                      // initialize our superclass first (JPanel)

    /* Set class instance variables to undefined values that we will recognize
    if we are called before the layout and first "paint" is complete. */

    cursorDot = 0;                // nibble index for current cursor position
    cursorMark = 0;               // nibble index of starting cursor selection
    cursorOnText = false;         // assume cursor is active on hex dump
    lineAscent = -1;              // number of pixels above baseline
    lineHeight = -1;              // height of each display line in pixels
    maxWidth = -1;                // maximum pixel width of ASCII characters
    panelColumns = -1;            // number of complete text columns displayed
    panelDumpWidth = -1;          // number of input bytes per dump line
    panelFont = null;             // saved font for drawing text on this panel
    panelFontName = "";           // saved font name for <panelFont>
    panelFontSize = -1;           // saved font size for <panelFont>
    panelHeight = -1;             // saved panel height in pixels
    panelOffset = 0;              // file offset in bytes for first display row
    panelRows = -1;               // number of complete lines (rows) displayed
    panelWidth = -1;              // saved panel width in pixels

    /* Allocate instance arrays.  There is no need to assign initial values. */

    charShifts = new int[HexEdit2.LAST_CHAR + 1]; // use same arrays all fonts
    charWidths = new int[HexEdit2.LAST_CHAR + 1];

    /* Install our keyboard and mouse listeners. */

    this.addKeyListener((KeyListener) this);
    this.addMouseListener((MouseListener) this);
    this.addMouseMotionListener((MouseMotionListener) this);
    this.addMouseWheelListener((MouseWheelListener) this);
//  this.setFocusable(true);      // focus is enabled by default for JPanel
//  this.setFocusTraversalKeysEnabled(false); // we want all keys, even "tab"

  } // end of HexEdit2Text() constructor


/*
  adjustScrollBar() method

  When the window size changes, we need to recalculate several settings for the
  vertical scroll bar.  These are otherwise static except the current position
  and the maximum size (which is subject to insertions and deletions by the
  user).  As a programming note, please call setValues() when setting more than
  one of the parameters, otherwise the change listener may fire between calls
  to the individual methods for setting parameters.
*/
  void adjustScrollBar()
  {
    /* If the dump width increases, our current panel offset can sometimes be
    too large with too much empty space in the display. */

    panelOffset = Math.max(0, Math.min(panelOffset, (((HexEdit2.nibbleCount
      / (panelDumpWidth * 2)) - panelRows + 1) * panelDumpWidth)));
                                  // limit range of panel offset
    panelOffset -= panelOffset % panelDumpWidth;
                                  // truncate or round down to full row

    /* Set values for the scroll bar. */

    HexEdit2.textScroll.setValues((panelOffset / panelDumpWidth), // value
      panelRows,                  // extent (visible amount)
      0,                          // minimum: always zero
      (HexEdit2.nibbleCount / (panelDumpWidth * 2) + 1));
                                  // maximum: round up to next full row

    HexEdit2.textScroll.setBlockIncrement(Math.max(1, (panelRows - 1)));
                                  // dump lines/rows per "scroll one page"
    HexEdit2.textScroll.setUnitIncrement(1); // rows per "scroll one line"

  } // end of adjustScrollBar() method


/*
  beginFile() method

  Display this data file from the beginning, by invalidating any position
  information that we currently have.
*/
  void beginFile()
  {
    cursorDot = 0;                // nibble index for current cursor position
    cursorMark = 0;               // nibble index of starting cursor selection
    cursorOnText = false;         // assume cursor is active on hex dump
    panelDumpWidth = -1;          // forces a complete resize and redraw
    panelOffset = 0;              // file offset of first row in panel display
    repaint();                    // redraw text display as necessary
  }


/*
  convertMouse() method

  Convert a mouse co-ordinate to a data nibble index, along with a flag to say
  if this is on the hex dump region or the ASCII text region.  A negative index
  is an error, although this is not currently used.  Our result has two parts,
  which are saved in temporary class variables.

  We are relaxed about what constitutes a valid mouse location, except for the
  imaginary dividing line between the hex dump and the ASCII text.  Floating-
  point numbers in the code below give some "fuzz" to mouse locations, because
  exact pixel boundaries aren't the best place to accept mouse clicks for text.
*/
  void convertMouse(MouseEvent event)
  {
    int column, row;              // index variables
    int x;                        // adjusted horizontal co-ordinate

    mouseTempOnText = event.getX() > (PANEL_MARGIN + (int)
      ((HexEdit2.OFFSET_DIGITS + (panelDumpWidth * 3) + 2.8) * maxWidth));
                                  // dividing line, with fuzz

    row = (event.getY() - PANEL_MARGIN) / lineHeight; // no fuzzy select for row
    row = Math.max(0, Math.min(row, panelRows)); // allow incomplete final row

    if (mouseTempOnText)          // if mouse is pointing at ASCII text region
    {
      x = event.getX() - PANEL_MARGIN - (int) ((HexEdit2.OFFSET_DIGITS
        + (panelDumpWidth * 3) + 3.8) * maxWidth);
                                  // shifted horizontal for text, with fuzz
      column = (x / maxWidth) * 2; // nibble offset for text
    }
    else                          // mouse is pointing at hex dump or offset
    {
      /* Spaces between pairs of hex digits cause us to first calculate which
      pair we are pointing at in a three-character width, then which nibble
      inside the pair. */

      x = event.getX() - PANEL_MARGIN - (int) ((HexEdit2.OFFSET_DIGITS + 1.5)
        * maxWidth);              // shifted horizontal for pairs, with fuzz
      column = (x / (maxWidth * 3)) * 2;
                                  // nibble offset for high-order digit
      column += ((x % (maxWidth * 3) > (maxWidth * 1.3))) ? 1 : 0;
                                  // increment for low-order digit, some fuzz
    }
    column = Math.max(0, Math.min(column, (panelDumpWidth * 2)));
                                  // limit range of column
    mouseTempNibble = (panelOffset + (row * panelDumpWidth)) * 2 + column;
    mouseTempNibble = Math.min(mouseTempNibble, HexEdit2.nibbleCount);
                                  // selection may include end of nibble data
  } // end of convertMouse() method


/*
  finishArrowKey() method

  All arrow keys (including Page Down and Page Up) have some common processing
  at the end involving selection and making the new cursor visible.  Unlike
  deletions and insertions, we don't need to update the scroll bar sizes.
*/
  void finishArrowKey(KeyEvent event)
  {
    if (event.isShiftDown() == false) // Shift + arrow means extend selection
      cursorMark = cursorDot;     // no selection, so keep cursors together
    limitCursorRange();           // refresh data size, enforce cursor range
    makeVisible(cursorDot);       // make sure that user can see cursor
//  adjustScrollBar();            // adjust scroll bar to match new position
    repaint();                    // redraw text display as necessary
  }


/*
  keyPressed(), keyReleased(), and keyTyped() methods

  These are the keyboard listeners.  They will receive key events as long as
  this component has the focus.  Something that should be noted is that since
  the data bytes/nibbles are contiguous, the end of one line is no different
  than the beginning of the next line.  Do not be surprised when the "End" key
  jumps down one row.  This is not a word processor with cute little paragraph
  markers!
*/
  public void keyPressed(KeyEvent event) { /* not used */ }

  public void keyReleased(KeyEvent event)
  {
    /* Use this method for control keys, that is, keys that don't have a value
    in the Unicode standard.  Ignore anything that we don't want, to avoid
    conflict with the keyTyped() method or other key listeners. */

    try                           // "out of memory" errors are likely
    {
      int key = event.getKeyCode(); // get numeric key code
      switch (key)
      {
        case (KeyEvent.VK_DOWN):  // down arrow
        case (KeyEvent.VK_KP_DOWN): // numeric keypad, down arrow
          if (cursorOnText)       // is active cursor on ASCII text region?
          {
            cursorDot += cursorDot % 2; // force cursor to end of byte
            cursorMark -= cursorMark % 2; // force selection to start of byte
          }
          cursorDot += panelDumpWidth * 2; // go forward one row
          finishArrowKey(event);  // limit cursor, make visible, repaint
          break;

        case (KeyEvent.VK_END):   // "End" key
          if (cursorOnText)       // is active cursor on ASCII text region?
            cursorMark -= cursorMark % 2; // force selection to start of byte
          if (event.isControlDown()) // Control-End means end of the file
            cursorDot = HexEdit2.nibbleCount; // go forward to end-of-file
          else                    // plain End means end of this line/row
          {
            cursorDot += panelDumpWidth * 2; // first go forward one row
            cursorDot -= cursorDot % (panelDumpWidth * 2);
                                  // then go to the beginning of that row
          }
          finishArrowKey(event);  // limit cursor, make visible, repaint
          break;

        case (KeyEvent.VK_F3):    // F3 key for "Find Next" (typical Windows)
          HexEdit2.searchFindNext(); // same as main menu (but not documented)
          break;

        case (KeyEvent.VK_F6):    // F6 key to switch between dump/text regions
          if (event.isShiftDown()) // Shift-F6 is previous panel for Windows
            cursorOnText = false; // here previous is always hex dump region
          else                    // plain F6 is next panel for Windows
            cursorOnText = true;  // here next is always ASCII text region
          limitCursorRange();     // refresh data size, enforce cursor range
          repaint();              // redraw text display as necessary
          break;

        case (KeyEvent.VK_HOME):  // "Home" key
          if (cursorOnText)       // is active cursor on ASCII text region?
            cursorMark += cursorMark % 2; // force selection to end of byte
          if (event.isControlDown()) // Control-Home means begnning of the file
            cursorDot = 0;        // go backward to start-of-file
          else                    // plain Home means start of this line/row
          {
            cursorDot --;         // first go backward one nibble
            cursorDot -= cursorDot % (panelDumpWidth * 2);
                                  // then go to the beginning of that row
          }
          finishArrowKey(event);  // limit cursor, make visible, repaint
          break;

        case (KeyEvent.VK_INSERT): // insert toggles with overwrite
          HexEdit2.overFlag = ! HexEdit2.overFlag; // invert current flag
          HexEdit2.overDialog.setSelected(HexEdit2.overFlag);
                                  // pass change on to GUI dialog box
          repaint();              // redraw text display as necessary
          break;

        case (KeyEvent.VK_LEFT):  // left arrow key
        case (KeyEvent.VK_KP_LEFT): // numeric keypad, left arrow
          if (cursorOnText)       // is active cursor on ASCII text region?
          {
            cursorDot --;         // first go backward one nibble
            cursorDot -= cursorDot % 2; // force cursor to start of byte
            cursorMark += cursorMark % 2; // force selection to end of byte
          }
          else                    // no, must be on hex dump region
            cursorDot --;         // go backward one nibble for hex dump
          finishArrowKey(event);  // limit cursor, make visible, repaint
          break;

        case (KeyEvent.VK_PAGE_DOWN): // "Page Down" key
          if (cursorOnText)       // is active cursor on ASCII text region?
          {
            cursorDot += cursorDot % 2; // force cursor to end of byte
            cursorMark -= cursorMark % 2; // force selection to start of byte
          }
          cursorDot += panelDumpWidth * 2 * Math.max(1, (panelRows - 1));
                                  // go forward one page
          finishArrowKey(event);  // limit cursor, make visible, repaint
          break;

        case (KeyEvent.VK_PAGE_UP): // "Page Up" key
          if (cursorOnText)       // is active cursor on ASCII text region?
          {
            cursorDot -= cursorDot % 2; // force cursor to start of byte
            cursorMark += cursorMark % 2; // force selection to end of byte
          }
          cursorDot -= panelDumpWidth * 2 * Math.max(1, (panelRows - 1));
                                  // go backward one page
          finishArrowKey(event);  // limit cursor, make visible, repaint
          break;

        case (KeyEvent.VK_RIGHT): // right arrow
        case (KeyEvent.VK_KP_RIGHT): // numeric keypad, right arrow
          if (cursorOnText)       // is active cursor on ASCII text region?
          {
            cursorDot ++;         // first go forward one nibble
            cursorDot += cursorDot % 2; // force cursor to end of byte
            cursorMark -= cursorMark % 2; // force selection to start of byte
          }
          else                    // no, must be on hex dump region
            cursorDot ++;         // go forward one nibble for hex dump
          finishArrowKey(event);  // limit cursor, make visible, repaint
          break;

        case (KeyEvent.VK_UP):    // up arrow
        case (KeyEvent.VK_KP_UP): // numeric keypad, up arrow
          if (cursorOnText)       // is active cursor on ASCII text region?
          {
            cursorDot -= cursorDot % 2; // force cursor to start of byte
            cursorMark += cursorMark % 2; // force selection to end of byte
          }
          cursorDot -= panelDumpWidth * 2; // go backward one row
          finishArrowKey(event);  // limit cursor, make visible, repaint
          break;

        default:                  // ignore anything we don't recognize
          break;
      }
    }
    catch (OutOfMemoryError oome)
    {
      HexEdit2.memoryError("keyReleased"); // nicely tell user that we failed
    }
  } // end of keyReleased() method

  public void keyTyped(KeyEvent event)
  {
    /* Accept keys with Unicode values, possibly modified with Alt or Ctrl key
    combinations.  Ignore anything that we don't want, to avoid conflict with
    the keyReleased() method, which does the special "named" keys. */

    try                           // "out of memory" errors are likely
    {
      char ch = event.getKeyChar(); // get Unicode character from keyboard
      if (event.isAltDown())      // menu shortcuts are Alt plus key
      {
        /* Ignore Alt combinations so that we don't interfere with menus. */
      }
      else if (event.isControlDown()) // keyboard shortcuts are Control + key
      {
        switch (ch)
        {
          case (0x01):            // Control-A for "Select All"
            HexEdit2.selectAll(); // same as main menu for "select all"
            break;

          case (0x03):            // Control-C for "Copy"
            if (cursorOnText)     // is active cursor on ASCII text region?
              HexEdit2.copyText(); // yes, same as main menu for "Copy Text"
            else                  // no, must be on hex dump region
              HexEdit2.copyHex(); // same as main menu for "Copy Hex"
            break;

          case (0x06):            // Control-F for "Find"
            HexEdit2.showSearchDialog(); // same as main menu
            break;

          case (0x07):            // Control-G for "Go To File Offset"
            HexEdit2.showGotoDialog(); // same as main menu
            break;

          case (0x0E):            // Control-N for "Find Next"
            HexEdit2.searchFindNext(); // same as main menu
            break;

          case (0x12):            // Control-R for "Replace" (more mnemonic)
            HexEdit2.searchReplaceThis(); // same as main menu
            break;

          case (0x16):            // Control-V for "Paste"
            if (cursorOnText)     // is active cursor on ASCII text region?
              HexEdit2.pasteText(); // yes, same as main menu for "Paste Text"
            else                  // no, must be on hex dump region
              HexEdit2.pasteHex(); // same as main menu for "Paste Hex"
            break;

          case (0x18):            // Control-X for "Cut"
            if (cursorOnText)     // is active cursor on ASCII text region?
              HexEdit2.copyText(); // yes, same as main menu for "Copy Text"
            else                  // no, must be on hex dump region
              HexEdit2.copyHex(); // same as main menu for "Copy Hex"
            HexEdit2.deleteSelected(); // same as main menu for "Delete"
            break;

          case (0x1A):            // Control-Z for "Undo" (not implemented)
            break;

          default:                // ignore anything we don't recognize
            break;
        }
      }
      else if (ch == 0x08)        // Backspace key (separate from Control-H)
      {
        if (cursorDot != cursorMark) // is there a selection?
          HexEdit2.deleteSelected(); // delete current selection and be done
        else if (cursorDot > 0)   // no selection, delete previous byte/nibble
        {
          if (cursorOnText)       // is active cursor on ASCII text (bytes)?
          {
            cursorDot --;         // move backward one nibble
            cursorDot -= cursorDot % 2; // then go backward to start of byte
            if (cursorDot < (HexEdit2.nibbleCount - 1)) // are there 2 nibbles?
              HexEdit2.nibbleData.delete(cursorDot + 1);
                                  // backspace always deletes low-order first
            HexEdit2.nibbleData.delete(cursorDot);
                                  // backspace then deletes high-order nibble
          }
          else                    // no, must be on hex dump region (nibbles)
          {
            cursorDot --;         // move backward one nibble
            HexEdit2.nibbleData.delete(cursorDot); // delete one nibble only
          }
          cursorMark = cursorDot; // both cursors are the same after deletion
          limitCursorRange();     // refresh data size, enforce cursor range
          makeVisible(cursorDot); // make sure that user can see cursor
          adjustScrollBar();      // adjust scroll bar to match new position
          repaint();              // redraw text display as necessary
        }
      }
      else if (ch == 0x1B)        // Escape key
      {
        cursorDot = cursorMark;   // restore original cursor (cancel selection)
        repaint();                // redraw text display as necessary
      }
      else if (ch == 0x7F)        // Delete key
      {
        if (cursorDot != cursorMark) // is there a selection?
          HexEdit2.deleteSelected(); // delete current selection and be done
        else if (cursorDot < HexEdit2.nibbleCount)
                                  // no selection, delete next byte/nibble
        {
          if (cursorOnText)       // is active cursor on ASCII text (bytes)?
          {
            cursorDot -= cursorDot % 2; // go backward to start of byte
            if (cursorDot < (HexEdit2.nibbleCount - 1)) // are there 2 nibbles?
              HexEdit2.nibbleData.delete(cursorDot);
                                  // forward always deletes high-order first
            HexEdit2.nibbleData.delete(cursorDot);
                                  // then delete low-order using same index
          }
          else                    // no, must be on hex dump region (nibbles)
          {
            HexEdit2.nibbleData.delete(cursorDot); // delete one nibble only
          }
          cursorMark = cursorDot; // both cursors are the same after deletion
          limitCursorRange();     // refresh data size, enforce cursor range
          makeVisible(cursorDot); // make sure that user can see cursor
          adjustScrollBar();      // adjust scroll bar to match new position
          repaint();              // redraw text display as necessary
        }
      }
      else if (Character.isISOControl(ch)) // all other control codes
      {
        /* Ignore all control codes that we haven't already recognized. */
      }
      else if (cursorOnText)      // is active cursor on ASCII text region?
      {
        /* Accept all characters that aren't identified as control codes.  To
        be consistent with copy and paste operations, the character is
        converted to a string, the string to a byte array using the system's
        default encoding, and the byte array to nibbles.  As with copy and
        paste, we don't enforce byte boundaries for inserted text. */

        if (HexEdit2.overFlag)    // cancel selection if in overwrite mode
          cursorDot = cursorMark = Math.min(cursorDot, cursorMark);

        byte[] bytes = String.valueOf(ch).getBytes(); // convert to byte array
        int[] nibbles = new int[bytes.length * 2]; // always uses full array
        int used = 0;             // start placing nibbles at this array index
        for (int i = 0; i < bytes.length; i ++) // do all bytes in the string
        {
          nibbles[used ++] = (bytes[i] >> HexEdit2.NIBBLE_SHIFT)
            & HexEdit2.NIBBLE_MASK; // high-order nibble in byte
          nibbles[used ++] = bytes[i] & HexEdit2.NIBBLE_MASK;
                                  // low-order nibble in byte
        }
        HexEdit2.pasteNibbles(nibbles, used, HexEdit2.overFlag);
                                  // paste nibbles as file data
      }
      else                        // no, must be on hex dump region
      {
        /* Accept only hexadecimal digits.  We are called for one character at
        a time, and hence only one digit.  Each digit is one data nibble.  To
        make use of the existing paste methods, we create an array of one
        nibble. */

        if (HexEdit2.overFlag)    // cancel selection if in overwrite mode
          cursorDot = cursorMark = Math.min(cursorDot, cursorMark);

        int hexValue = HexEdit2.charHexValue(ch);
                                  // convert character to value of hex digit
        int[] nibbles = new int[1]; // one nibble if valid input, else zero
        int used = 0;             // will get incremented for valid input

        if (hexValue >= 0)        // was it a valid hexadecimal digit?
          nibbles[used ++] = hexValue; // yes, save the nibble value
        else if (hexValue == HexEdit2.HEX_IGNORE) // ignore space, punctuation?
          { /* do nothing */ }
        else                      // illegal character
          Toolkit.getDefaultToolkit().beep(); // warning sound (may not work)

        if (used > 0)             // was there a valid hexadecimal digit?
          HexEdit2.pasteNibbles(nibbles, used, HexEdit2.overFlag);
                                  // paste nibbles as file data
      }
    }
    catch (OutOfMemoryError oome)
    {
      HexEdit2.memoryError("keyTyped"); // nicely tell user that we failed
    }
  } // end of keyTyped() method


/*
  limitCursorRange() method

  Adjust the cursor "dot" and "mark" to be within the legal range, which is
  from zero to the number of data nibbles.  (The cursor can be after the last
  data nibble.)

  A previous version of this method checked if the cursor was in the ASCII text
  region, and if so, truncated it to a byte boundary, that is, an even number
  of nibbles.  This was not helpful, because the text region couldn't select
  the last nibble if there was an odd number of nibbles after editing.
*/
  void limitCursorRange()
  {
    HexEdit2.refreshDataSize();   // refresh total number of nibbles
    cursorDot = Math.max(0, Math.min(cursorDot, HexEdit2.nibbleCount));
    cursorMark = Math.max(0, Math.min(cursorMark, HexEdit2.nibbleCount));
//  if (cursorOnText)             // is active cursor on ASCII text region?
//  {
//    cursorDot -= cursorDot % 2; // yes, go backward to start of byte
//    cursorMark -= cursorMark % 2;
//  }
  }


/*
  makeVisible() method

  Given a nibble index into the data, check if this nibble is visible in the
  current display.  If not, then adjust the scroll and redraw the display so
  that the nibble appears on the top or bottom row (as appropriate).  This is
  used by all methods that delete, insert, or paste data, or move the cursor
  with keyboard commands.  (The mouse can only select visible positions, except
  in one unusual circumstance.)
*/
  void makeVisible(int nibbleIndex)
  {
    if (nibbleIndex < (panelOffset * 2)) // is nibble before start of display?
    {
      panelOffset = (nibbleIndex / (2 * panelDumpWidth)) * panelDumpWidth;
                                  // put nibble on first display line
      adjustScrollBar();          // adjust scroll bar to match new position
      repaint();                  // redraw text display as necessary
    }
    else if (nibbleIndex >= ((panelOffset + (panelRows * panelDumpWidth)) * 2))
    {
      panelOffset = ((nibbleIndex / (2 * panelDumpWidth)) - panelRows + 1)
        * panelDumpWidth;         // put nibble on last display line
      adjustScrollBar();          // adjust scroll bar to match new position
      repaint();                  // redraw text display as necessary
    }
    else
    {
      /* Nibble is visible.  Do nothing. */
    }
  } // end of makeVisible() method


/*
  mouseClicked() ... mouseReleased() methods

  These are the mouse click and movement listeners.  A plain click sets the
  cursor to the location of the click.  A shift click selects from the cursor
  to the location of the click.  A right click (or control click) shows a menu
  for copy and paste.  A click-and-drag selects from the click to where the
  mouse button is released.

  Mouse clicks are always on the visible display, so we rarely need to scroll.
  We do need to remind Java often that we want the keyboard focus.  Otherwise,
  a menu can open up over this panel, steal the focus, and not return it,
  because the mouse never leaves this panel's region, and Java doesn't call
  mouseEntered() when the menu closes!
*/
  public void mouseClicked(MouseEvent event)
  {
    /* Called when a mouse button has been pressed and released.  Not called at
    the end of a click-and-drag.  Mouse clicks are much less frequent than drag
    events, so we always update the display after a mouse click. */

    requestFocusInWindow();       // request focus, listen for keyboard input
    convertMouse(event);          // convert mouse event to nibble index, type
    if (mouseTempNibble < 0)      // is the mouse position valid for us?
    {
      cursorDot = cursorMark;     // restore original cursor (cancel selection)
      repaint();                  // redraw text display as necessary
    }
    else if (event.isControlDown() // control click means show pop-up menu
      || (event.getButton() != MouseEvent.BUTTON1)) // as does right click
    {
      HexEdit2.showEditMenu(this, event.getX(), event.getY(), true);
    }
    else if (event.isShiftDown()) // shift click means "select to here"
    {
      cursorDot = mouseTempNibble; // select to current mouse position
      cursorOnText = mouseTempOnText; // cursor type: false hex dump, true text
      limitCursorRange();         // refresh data size, enforce cursor range
      makeVisible(cursorDot);     // user may have clicked after last row, last
                                  // ... column, which sets cursor to next page
      repaint();                  // redraw text display as necessary
    }
    else                          // plain mouse click-and-release
    {
      cursorDot = cursorMark = mouseTempNibble; // move cursor to this position
      cursorOnText = mouseTempOnText; // cursor type: false hex dump, true text
      limitCursorRange();         // refresh data size, enforce cursor range
      makeVisible(cursorDot);     // user may have clicked at end of display
      repaint();                  // redraw text display as necessary
    }
  } // end of mouseClicked() method

  public void mouseDragged(MouseEvent event)
  {
    /* Called after a mouse button has been pressed and when the mouse is moved
    before the button is released.  Unlike mouse clicks, mouse drags are called
    many times, so avoid updating the display if nothing has changed. */

    requestFocusInWindow();       // request focus, listen for keyboard input
    convertMouse(event);          // convert mouse event to nibble index, type
    if (mousePressNibble < 0)     // do we have a starting point for drag?
    {
      /* Ignore mouse drag if not starting from valid left button press. */
    }
    else if ((mouseTempNibble < 0) || (mousePressOnText != mouseTempOnText))
    {
      /* Current mouse position is invalid, or the mouse was dragged outside of
      the starting dump or text region. */

      if (cursorDot != cursorMark) // do we really need to update cursor?
      {
        cursorDot = cursorMark;   // restore original cursor (cancel selection)
        repaint();                // redraw text display as necessary
      }
    }
    else                          // update cursor only if it's changed
    {
      boolean updateFlag = false; // true if we need to redraw display
      if (cursorDot != mouseTempNibble) // has cursor position changed?
      {
        cursorDot = mouseTempNibble; // selection now goes to mouse position
        updateFlag = true;        // must redraw display
      }
      if (cursorMark != mousePressNibble) // have we set the selection start?
      {
        cursorMark = mousePressNibble; // button press is now selection start
        updateFlag = true;        // must redraw display
      }
      if (cursorOnText != mouseTempOnText) // where is mouse pointing?
      {
        cursorOnText = mouseTempOnText; // update cursor region from mouse
        updateFlag = true;        // must redraw display
      }
      if (updateFlag)             // do we need to redraw the display?
      {
        /* Don't makeVisible(cursorDot), or else the display will scroll down
        too quickly when the selection ends after the last row and column ...
        and the display never scrolls up for a selection before the first row
        and column, which would be inconsistent behavior. */

        repaint();                // redraw text display as necessary
      }
    }
  } // end of mouseDragged() method

  public void mouseEntered(MouseEvent event)
  {
    requestFocusInWindow();       // request focus, listen for keyboard input
  }

  public void mouseExited(MouseEvent event)
  {
//  transferFocus();              // cancel focus, don't want keyboard input
  }

  public void mouseMoved(MouseEvent event)
  {
    requestFocusInWindow();       // request focus, listen for keyboard input
  }

  public void mousePressed(MouseEvent event)
  {
    /* Called when a mouse button is first pressed.  We don't know if this will
    be a press-and-release or a press-and-drag, so all we can do is to record
    the mouse position as a possible starting point. */

    requestFocusInWindow();       // request focus, listen for keyboard input
    if (event.getButton() == MouseEvent.BUTTON1) // only left click starts drag
    {
      convertMouse(event);        // convert mouse event to nibble index, type
      mousePressNibble = mouseTempNibble; // save converted nibble index
      mousePressOnText = mouseTempOnText; // save region: hex dump, ASCII text
    }
    else                          // ignore drag on all other mouse buttons
      mousePressNibble = -1;      // by refusing to recognize start of drag
  }

  public void mouseReleased(MouseEvent event)
  {
    requestFocusInWindow();       // request focus, listen for keyboard input
  }


/*
  mouseWheelMoved() method

  This is the mouse wheel listener, for the scroll wheel on some mice.  The
  "unit" scroll uses the local system preferences for how many lines/rows per
  click of the mouse.  The "unit" scroll may be too big if there are only one
  or two lines in the display.

  The mouse wheel listener has no interaction with the other mouse listeners
  above.
*/
  public void mouseWheelMoved(MouseWheelEvent event)
  {
    switch (event.getScrollType()) // different mice scroll differently
    {
      case (MouseWheelEvent.WHEEL_BLOCK_SCROLL):
        HexEdit2.textScroll.setValue(HexEdit2.textScroll.getValue()
          + (event.getWheelRotation() * Math.max(1, (panelRows - 1))));
        break;

      case (MouseWheelEvent.WHEEL_UNIT_SCROLL):
        int i = Math.max(1, (panelRows - 1)); // maximum rows to scroll
        i = Math.max((-i), Math.min(i, event.getUnitsToScroll())); // limits
        HexEdit2.textScroll.setValue(HexEdit2.textScroll.getValue() + i);
                                  // scroll using limited local preferences
        break;

      default:                    // ignore anything that we don't recognize
        break;
    }
  } // end of mouseWheelMoved() method


/*
  paintComponent() method

  This is the "paint" method for a Java Swing component.  We have to worry
  about the window size changing, new options chosen by the user, etc.  There
  are many temporary variables in this method, because some calculations are
  difficult and declaring all variables at the beginning would be worse than
  declaring them when they are first used.

  To avoid conflict with the openFile() method, this method always uses the
  current value of <nibbleCount> for the total data size; it *never* calls the
  refreshDataSize() method.  Otherwise this method might try to paint with data
  that has disappeared.  Other GUI events like insert and delete don't have
  the same problem, because they complete before repaint() gets invoked.
*/
  protected void paintComponent(Graphics context)
  {
    int column, row;              // index variables
    int i;                        // index variable

    /* Erase the entire panel region using our choice of background colors. */

    context.setColor(PANEL_COLOR); // flood fill with background color
    context.fillRect(0, 0, this.getWidth(), this.getHeight());

    /* Recalculate panel sizes if any of the following have changed: font name,
    input bytes per dump line, panel height, panel width. */

    if ((HexEdit2.dumpWidth != panelDumpWidth)
      || (HexEdit2.fontName.equals(panelFontName) == false)
      || (this.getWidth() != panelWidth))
    {
      /* We need to find a good font size whenever there is a change to the
      dump width (input bytes per line), font name, or panel width. */

      panelDumpWidth = HexEdit2.dumpWidth; // save current input bytes per line
      panelFontName = HexEdit2.fontName; // save name of current font
      panelHeight = this.getHeight(); // save current panel height in pixels
      panelWidth = this.getWidth(); // save current panel width in pixels

      /* Our search for a font size is intended to shrink or expand lines in
      the dump output so that they fill the width of the panel.  To do this, we
      first need to know how many monospaced text positions are required for
      one complete dump line. */

      panelColumns = HexEdit2.OFFSET_DIGITS // digits in file offset
        + 1                       // space between offset and hex bytes
        + (panelDumpWidth * 3)    // two hex digits per input byte, one space
        + 2                       // two spaces between hex and ASCII text
        + 1                       // left marker for ASCII text
        + panelDumpWidth          // ASCII text corresponding to hex bytes
        + 1;                      // right marker for ASCII text

      /* Look for the biggest font size that doesn't overflow the panel width.
      We do this with a binary search starting at the mid-range of our allowed
      font sizes.  There is not always a smooth progression of pixel widths as
      you increase the point size, and due to rounding errors, we never use the
      supposed maximum size (only the maximum minus one). */

      int fontSizeLow = MIN_FONT_SIZE; // current low end of search range
      int fontSizeHigh = MAX_FONT_SIZE; // current high end of search range
      panelFontSize = -1;         // start without knowing a font size
      while (true)                // this <while> loop ends with a <break>
      {
        int testSize = (fontSizeLow + fontSizeHigh) / 2; // middle of range
        if (panelFontSize == testSize) // has range reduced to one size?
          break;                  // yes, stop looking
        panelFontSize = testSize; // no, calculate widths for this size

        /* Get an instance of the current font in the current test size, and
        grab the widths of all printable ASCII characters. */

        panelFont = new Font(panelFontName, Font.PLAIN, panelFontSize);
        FontMetrics fm = context.getFontMetrics(panelFont);
        lineAscent = fm.getAscent(); // number of pixels above baseline
        lineHeight = fm.getHeight(); // height of each display line in pixels
        maxWidth = -1;            // makes everything else look bigger
        for (i = HexEdit2.FIRST_CHAR; i <= HexEdit2.LAST_CHAR; i ++)
                                  // get widths for printable ASCII characters
        {
          charWidths[i] = fm.charWidth(i); // remember width of each character
          maxWidth = Math.max(charWidths[i], maxWidth); // remember maximum
        }

        /* Adjust search range to increase or decrease the font size.  We stop
        when the search range reduces to one point or less. */

        if ((panelColumns * maxWidth) < (panelWidth - (2 * PANEL_MARGIN)))
          fontSizeLow = panelFontSize; // search same size or larger only
        else
          fontSizeHigh = panelFontSize; // search same size or smaller only
      }

      /* Since not all fonts will be monospaced, calculate how many pixels to
      shift each character right so that it will be centered in the width of
      the widest character. */

      for (i = HexEdit2.FIRST_CHAR; i <= HexEdit2.LAST_CHAR; i ++)
        charShifts[i] = (maxWidth - charWidths[i]) / 2;

      /* Recalculate how many complete rows (lines) of text can be displayed
      inside this panel with the specified margins. */

      panelRows = Math.max(1, ((panelHeight - (2 * PANEL_MARGIN)) / lineHeight));
      adjustScrollBar();          // adjust scroll bar to match new sizes
    }
    else if (this.getHeight() != panelHeight)
    {
      /* If only the panel height has changed, then all we need to do is to
      recalculate how many complete rows (lines) of text can be displayed. */

      panelHeight = this.getHeight(); // save current panel height in pixels
      panelRows = Math.max(1, ((panelHeight - (2 * PANEL_MARGIN)) / lineHeight));
      adjustScrollBar();          // adjust scroll bar to match new sizes
    }

    /* Draw the panel.  All cached screen size, position, and font information
    is correct or has been updated. */

    /* Highlight where selected text will go by redrawing the background color.
    The cursor "dot" and "mark" positions may be in forward or reverse order.
    The region that will be highlighted is irregular (described by up to three
    joined rectangles), so it is easier to highlight pieces individually.  As
    always, avoid repeating the same calculations, in an effort to speed up the
    display. */

    int panelNibbleBegin = panelOffset * 2; // nibble index starting first row
    int panelNibbleEnd = panelNibbleBegin + ((panelRows + 1) * panelDumpWidth
      * 2);                       // after last digit on incomplete final row

    int selectBegin = Math.max(panelNibbleBegin, Math.min(cursorDot,
      cursorMark));               // intersect with display
    int selectEnd = Math.min(panelNibbleEnd, Math.max(cursorDot, cursorMark));
    if (selectBegin < selectEnd)  // only work hard if selection is visible
    {
      /* First draw the selection background for the hex dump. */

      context.setColor(cursorOnText ? SHADOW_SELECT : ACTIVE_SELECT);
                                  // set correct color, if cursor active here

      row = ((selectBegin / 2) - panelOffset) / panelDumpWidth;
                                  // calculate starting row index
      int thisRowY = PANEL_MARGIN + (row * lineHeight);
                                  // convert index to vertical co-ordinate

      int rowNibbleCount = panelDumpWidth * 2; // repeatedly used inside loop
      int nibble = selectBegin % rowNibbleCount;
                                  // calculate nibble index (half byte)
      int rowFirstDumpX = PANEL_MARGIN + maxWidth * (HexEdit2.OFFSET_DIGITS + 2);
                                  // horizontal position first nibble, each row
      int thisColumnX = rowFirstDumpX + (maxWidth * (nibble + (nibble / 2)));
                                  // starting horizontal for first selected

      int nibbleIndex = selectBegin; // start nibbling away at this index
      while (nibbleIndex < selectEnd) // draw one background per each nibble
      {
        context.fillRect(thisColumnX, thisRowY, maxWidth, lineHeight);
        nibble ++;                // move right one nibble on this row
        nibbleIndex ++;           // data index of next nibble
        thisColumnX += maxWidth;  // horizontal co-ordinate for next dump

        if (nibble >= rowNibbleCount) // have we filled one row?
        {
          nibble = 0;             // yes, reset back to beginning of row
          thisColumnX = rowFirstDumpX; // reset horizontal co-ordinate
          thisRowY += lineHeight; // and drop down one row
        }
        else if ((nibbleIndex < selectEnd) && ((nibbleIndex % 2) == 0))
        {
          /* Highlight the space between pairs of nibbles. */

          context.fillRect(thisColumnX, thisRowY, maxWidth, lineHeight);
          thisColumnX += maxWidth; // horizontal co-ordinate for next dump
        }
      }

      /* Second, draw the selection background for the ASCII text.  We don't
      try to be fancy and indicate half a byte if the active cursor is between
      two hex digits in a byte pair (above). */

      context.setColor(cursorOnText ? ACTIVE_SELECT : SHADOW_SELECT);
                                  // set correct color, if cursor active here

      row = ((selectBegin / 2) - panelOffset) / panelDumpWidth;
                                  // calculate starting row index
      thisRowY = PANEL_MARGIN + (row * lineHeight);
                                  // convert index to vertical co-ordinate

      column = (selectBegin % (panelDumpWidth * 2)) / 2;
                                  // calculate column index (full byte)
      int rowFirstTextX = PANEL_MARGIN + maxWidth * (HexEdit2.OFFSET_DIGITS + (3
        * panelDumpWidth) + 4);   // horizontal for first column on each row
      thisColumnX = rowFirstTextX + (maxWidth * column);
                                  // starting horizontal for first selected

      nibbleIndex = selectBegin - (selectBegin % 2);
                                  // round nibble index down to byte boundary

      while (nibbleIndex < selectEnd) // draw one background per two nibbles
      {
        context.fillRect(thisColumnX, thisRowY, maxWidth, lineHeight);
        column ++;                // move right one position
        nibbleIndex += 2;         // next text byte is two nibbles away
        thisColumnX += maxWidth;  // horizontal co-ordinate for next text

        if (column >= panelDumpWidth) // have we filled one row?
        {
          column = 0;             // yes, reset back to beginning of row
          thisColumnX = rowFirstTextX; // reset horizontal co-ordinate
          thisRowY += lineHeight; // and drop down one row
        }
      }
    }

    /* Draw the active cursor (either hex dump or ASCII text) and a mirrored
    shadow cursor (on the opposite side), if visible. */

    if ((cursorDot >= panelNibbleBegin) && (cursorDot < panelNibbleEnd))
    {
      /* First draw the cursor for the hex dump. */

      int cursorY = (cursorDot - panelNibbleBegin) / (panelDumpWidth * 2);
                                  // calculate row index
      cursorY = PANEL_MARGIN + (cursorY * lineHeight);
                                  // convert index to vertical co-ordinate

      int cursorX = (cursorDot % (panelDumpWidth * 2)) / 2;
                                  // calculate column index (full byte)
      cursorX = PANEL_MARGIN + maxWidth * (HexEdit2.OFFSET_DIGITS + (3 * cursorX)
        + 2);                     // convert index to horizontal co-ordinate

      if ((cursorDot % 2) > 0)    // shift right if second nibble in byte
        cursorX += maxWidth;

      context.setColor(cursorOnText ? SHADOW_CURSOR : ACTIVE_CURSOR); // active?
      if (HexEdit2.overFlag)      // is this an overwrite cursor (box)?
      {
        context.drawRect((cursorX - 1), cursorY, (maxWidth + 1),
          (lineHeight - 1));      // regular thin cursor (one pixel width)
        if (panelFontSize > 24)   // big fonts need a fatter cursor
          context.drawRect(cursorX, (cursorY + 1), (maxWidth - 1),
            (lineHeight - 3));    // additional one-pixel box inside first
      }
      else                        // must be an insert cursor (vertical line)
        context.fillRect((cursorX - 1), cursorY,
          ((panelFontSize > 24) ? 3 : 2), lineHeight);

      /* Second, draw the cursor for the ASCII text.  Use the same <cursorY> as
      above.  We don't try to be fancy and indicate half a byte if the active
      cursor is between two hex digits in a byte pair (above). */

      cursorX = (cursorDot % (panelDumpWidth * 2)) / 2;
                                  // calculate column index (full byte)
      cursorX = PANEL_MARGIN + maxWidth * (HexEdit2.OFFSET_DIGITS + (3
        * panelDumpWidth) + cursorX + 4);
                                  // convert index to horizontal co-ordinate

      context.setColor(cursorOnText ? ACTIVE_CURSOR : SHADOW_CURSOR); // active?
      if (HexEdit2.overFlag)      // is this an overwrite cursor (box)?
      {
        context.drawRect((cursorX - 1), cursorY, (maxWidth + 1),
          (lineHeight - 1));      // regular thin cursor (one pixel width)
        if (panelFontSize > 24)   // big fonts need a fatter cursor
          context.drawRect(cursorX, (cursorY + 1), (maxWidth - 1),
            (lineHeight - 3));    // additional one-pixel box inside first
      }
      else                        // must be an insert cursor (vertical line)
        context.fillRect((cursorX - 1), cursorY,
          ((panelFontSize > 24) ? 3 : 2), lineHeight);
    }

    /* All text on the panel has the same color and font.  Only the background
    color changes, or the addition of a cursor (above). */

    context.setColor(TEXT_COLOR);
    context.setFont(panelFont);

    /* Draw the file byte offsets on the left side and the ASCII text markers
    on the right side.  Although the overall difference in speed may be small,
    avoid recalculating screen positions, such as multiplications and
    especially divisions.  Allow for one more nibble than is in the file, so
    the user has somewhere to insert text at the end of the file.  There is
    always at least one file offset, even if the file is empty. */

    int maxOffset = HexEdit2.nibbleCount / 2;
                                  // don't display offsets past this value
    int rowLastDigitX = PANEL_MARGIN + (HexEdit2.OFFSET_DIGITS - 1) * maxWidth;
                                  // all rows put low-order offset digit here
    int rowLeftMarkerX = PANEL_MARGIN + maxWidth * ((HexEdit2.OFFSET_DIGITS + 3
      + (3 * panelDumpWidth))) + charShifts[HexEdit2.MARKER_CHAR];
                                  // all rows put left text marker here
    int rowRightMarkerX = rowLeftMarkerX + maxWidth * (panelDumpWidth + 1);
                                  // all rows put right text marker here
    int rowY = PANEL_MARGIN + lineAscent; // vertical baseline for first row
    int thisOffset = panelOffset; // byte offset for first row

    for (row = 0; row <= panelRows; row ++) // allow incomplete final row
    {
      if (thisOffset > maxOffset) // have we gone too far? (">" is correct)
        break;                    // yes, escape early from <for> loop
      int shiftedOffset = thisOffset; // copy offset so as to extract digits
      int thisDigitX = rowLastDigitX; // horizontal start for low-order digit
      for (i = HexEdit2.OFFSET_DIGITS; i > 0; i --)
                                  // extract digits from low-order end
      {
        char ch = HexEdit2.HEX_DIGITS[shiftedOffset & HexEdit2.NIBBLE_MASK];
                                  // convert nibble to hex text digit
        context.drawString(Character.toString(ch), (thisDigitX
          + charShifts[ch]), rowY); // center this character onto the screen
        shiftedOffset = shiftedOffset >> HexEdit2.NIBBLE_SHIFT;
                                  // shift down next higher-order nibble
        thisDigitX -= maxWidth;   // back up for next higher-order digit
      }
      context.drawString(HexEdit2.MARKER_STRING, rowLeftMarkerX, rowY);
                                  // left text marker with centering shift
      context.drawString(HexEdit2.MARKER_STRING, rowRightMarkerX, rowY);
                                  // right text marker with centering shift
      rowY += lineHeight;         // vertical position for next row
      thisOffset += panelDumpWidth; // byte offset for next row
    }

    /* Draw the hex dump and plain text ASCII characters.  As before, avoid
    unnecessary calculations.  This can be entirely empty if the file is empty,
    or if the display is only big enough for one row and we are at the end of a
    file that exactly filled the previous row. */

    int nibbleIndex = panelOffset * 2; // index of first data nibble, first row
    int rowFirstDumpX = PANEL_MARGIN + maxWidth * (HexEdit2.OFFSET_DIGITS + 2);
                                  // horizontal position first nibble, each row
    int rowFirstTextX = rowFirstDumpX + maxWidth * ((3 * panelDumpWidth) + 2);
                                  // horizontal position first text, each row
    rowY = PANEL_MARGIN + lineAscent; // vertical baseline for first row

    for (row = 0; row <= panelRows; row ++) // allow incomplete final row
    {
      int thisDumpX = rowFirstDumpX; // horizontal dump start for this row
      int thisTextX = rowFirstTextX; // horizontal text start for this row
      for (column = 0; column < panelDumpWidth; column ++)
      {
        /* first nibble of two */

        if (nibbleIndex >= HexEdit2.nibbleCount) // have we gone too far?
          break;                  // yes, escape early from <for> loop
        int thisNibble = HexEdit2.nibbleData.get(nibbleIndex ++);
                                  // get value of this nibble as integer
        char ch = HexEdit2.HEX_DIGITS[thisNibble & HexEdit2.NIBBLE_MASK];
                                  // convert nibble to hex text digit
        context.drawString(Character.toString(ch), (thisDumpX
          + charShifts[ch]), rowY); // center this hex digit onto screen
        thisDumpX += maxWidth;    // horizontal position for next hex digit

        int byteValue = thisNibble << HexEdit2.NIBBLE_SHIFT;
                                  // first nibble is high-order part of byte

        /* second nibble of two (may be missing) */

        if (nibbleIndex < HexEdit2.nibbleCount) // is there a second nibble?
        {
          thisNibble = HexEdit2.nibbleData.get(nibbleIndex ++);
                                  // get value of this nibble as integer
          ch = HexEdit2.HEX_DIGITS[thisNibble & HexEdit2.NIBBLE_MASK];
                                  // convert nibble to hex text digit
          context.drawString(Character.toString(ch), (thisDumpX
            + charShifts[ch]), rowY); // center this hex digit onto screen
          thisDumpX += maxWidth;  // horizontal position for next hex digit

          byteValue |= thisNibble; // insert second nibble into byte value
        }
        else
          byteValue = -1;         // don't try to display incomplete bytes

        thisDumpX += maxWidth;    // skip space between pairs of hex digits

        /* text character (if printable) */

        if ((byteValue < HexEdit2.FIRST_CHAR) || (byteValue > HexEdit2.LAST_CHAR))
          byteValue = HexEdit2.REPLACE_CHAR; // replace unprintable character
        context.drawString(Character.toString((char) byteValue), (thisTextX
          + charShifts[byteValue]), rowY); // draw character onto screen
        thisTextX += maxWidth;    // horizontal position for next text char
      }
      rowY += lineHeight;         // vertical position for next row
    }
  } // end of paintComponent() method


/*
  stateChanged() method

  Currently only used for the vertical scroll bar.  This method gets called
  often, perhaps too often.  Try to invoke other methods only if something
  important has changed.
*/
  public void stateChanged(ChangeEvent event)
  {
    if (panelDumpWidth > 1)       // are we ready to handle this yet?
    {
      int scroll = HexEdit2.textScroll.getValue(); // scroll bar row position
      int newOffset = scroll * panelDumpWidth; // convert rows to input bytes
      if (newOffset != panelOffset) // has drawing position truly changed?
      {
        panelOffset = newOffset;  // yes, remember new starting offset
        repaint();                // redraw text display as necessary
      }
    }
  } // end of stateChanged() method

} // end of HexEdit2Text class
