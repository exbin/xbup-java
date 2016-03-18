/*
 * SimplyHTML, a word processor based on Java, HTML and CSS
 * Copyright (C) 2002 Ulrich Hilger
 * Copyright (C) 2006 Dimitri Polivaev
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.lightdev.app.shtm;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.event.CaretEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.NavigationFilter;
import javax.swing.text.Position.Bias;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.CSS;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

/**
 * An editor pane for application SimplyHTML.
 *
 * <p>This is extending <code>JEditorPane</code> by cut and paste
 * and drag and drop for HTML text.
 * <code>JEditorPane</code> inherits cut and paste from <code>
 * JTextComponent</code> where handling for plain text is implemented only.
 * <code>JEditorPane</code> has no additional functionality to add cut
 * and paste for the various content types it supports
 * (such as 'text/html').</p>
 *
 * <p>In stage 4 support for caret movement inside tables and
 * table manipulation methods are added.</p>
 *
 * <p>In stage 6 support for list manipulation was added.</p>
 *
 * @author Ulrich Hilger
 * @author Light Development
 * @author <a href="http://www.lightdev.com">http://www.lightdev.com</a>
 * @author <a href="mailto:info@lightdev.com">info@lightdev.com</a>
 * @author published under the terms and conditions of the
 *      GNU General Public License,
 *      for details see file gpl.txt in the distribution
 *      package of this software
 *
 * 
 *
 * @see com.lightdev.app.shtm.HTMLText
 * @see com.lightdev.app.shtm.HTMLTextSelection
 */
public class SHTMLEditorPane extends JEditorPane implements DropTargetListener, DragSourceListener, DragGestureListener {
	
	private static final String TAB = "\t";
	private static final String TAB_REPLACEMENT = "    ";

	public enum PasteMode
	{
		PASTE_HTML("Paste as HTML"), PASTE_PLAIN_TEXT("Paste as plain-text");
		
		private final String displayName;

		private PasteMode(final String displayName)
		{
			this.displayName = displayName;
		}
		
		public String getDisplayName()
		{
			return displayName;
		}
		
		public PasteMode invert()
		{
			if (this == PASTE_HTML)
				return PASTE_PLAIN_TEXT;
			else if (this == PASTE_PLAIN_TEXT)
				return PASTE_HTML;
			else
				throw new RuntimeException("Expected value for SHTMLEditorPane.PasteMode: " + name());
		}
		
		public static PasteMode getValueFromPrefs()
		{
			// try to get freeplane pref value, and use simplyhtml pref value if that fails
			Preferences prefs = Preferences.userNodeForPackage(SHTMLEditorPane.class);
			PasteMode pm = SHTMLEditorPane.PasteMode.valueOf(
					SHTMLEditorPane.PasteMode.class, Util.getPreference(
							"default_paste_mode", prefs.get(
									PrefsDialog.PREFS_DEFAULT_PASTE_MODE,
									SHTMLEditorPane.PasteMode.PASTE_HTML
											.name())));
			return pm;
		}
	}
	
    private static final boolean OLD_JAVA_VERSION = System.getProperty("java.version").compareTo("1.5.0") < 0;
    private JPopupMenu popup;
    private final ListManager listManager = new ListManager();
    private PasteMode pasteMode;
    private boolean forceConstantPasteMode;

    /**
     * construct a new <code>SHTMLEditorPane</code>
     */
    public SHTMLEditorPane() {
        super();
        setCaretColor(Color.black);
        setNavigationFilter(new MyNavigationFilter());

        int metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        final int metaKey = metaMask == KeyEvent.META_DOWN_MASK ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;
        addMouseListener(new MouseAdapter() {
            public void mousePressed(final MouseEvent e) {
                maybeShowPopup(e);
            }

            public void mouseReleased(final MouseEvent e) {
                maybeShowPopup(e);
            }

            public void mouseClicked(final MouseEvent ev) {
                if ((ev.getModifiers() & metaKey) != 0) {
                    final String linkURL = getURLOfExistingLink();
                    if (linkURL != null) {
                        final SHTMLPanelImpl panel = SHTMLPanelImpl.getOwnerSHTMLPanel((Component) ev.getSource());
                        panel.openHyperlink(linkURL);
                    }
                }
            }

            private void maybeShowPopup(final MouseEvent e) {
                if (popup != null && e.isPopupTrigger()) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        setPasteModeFromPrefs();
        /** implement customized caret movement */
        adjustKeyBindings();
        /** init drag and drop */
        initDnd();
    }
    
    public PasteMode getPasteMode() {
		if (forceConstantPasteMode)
		{ 
			return pasteMode;
		}
		else
		{
			return PasteMode.getValueFromPrefs();
		}
	}

	public void setPasteMode(final PasteMode pasteMode) {
		this.pasteMode = pasteMode;
		this.forceConstantPasteMode = true;
	}
	
	public void setPasteModeFromPrefs()
	{
		this.forceConstantPasteMode = false;
	}

	/**
     * adjust the key bindings of the key map existing for this
     * editor pane to our needs (i.e. add actions to certain keys
     * such as tab/shift tab for caret movement inside tables, etc.)
     *
     * This method had to be redone for using InputMap / ActionMap
     * instead of Keymap.
     */
    private void adjustKeyBindings() {
        final ActionMap myActionMap = new ActionMap();
        final InputMap myInputMap = new InputMap();
        final KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        myActionMap.put(SHTMLPanelImpl.nextTableCellAction, new TabAction(SHTMLPanelImpl.nextTableCellAction));
        myInputMap.put(tab, SHTMLPanelImpl.nextTableCellAction);
        final KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK);
        myActionMap.put(SHTMLPanelImpl.prevTableCellAction, new ShiftTabAction(SHTMLPanelImpl.prevTableCellAction));
        myInputMap.put(shiftTab, SHTMLPanelImpl.prevTableCellAction);
        final KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        myActionMap.put(newListItemAction, new NewParagraphAction());
        myInputMap.put(enter, newListItemAction);
        final KeyStroke lineBreak = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.SHIFT_MASK);
        myActionMap.put(insertLineBreakAction, new InsertLineBreakAction());
        myInputMap.put(lineBreak, insertLineBreakAction);
        final KeyStroke backspace = KeyStroke.getKeyStroke('\b', 0);
        myActionMap.put(deletePrevCharAction, new DeletePrevCharAction());
        myInputMap.put(backspace, deletePrevCharAction);
        final KeyStroke delete = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        myActionMap.put(deleteNextCharAction, new DeleteNextCharAction());
        myInputMap.put(delete, deleteNextCharAction);
        myActionMap.put(moveUpAction, new MoveUpAction());
        myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), moveUpAction);
        myActionMap.put(moveDownAction, new MoveDownAction());
        myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), moveDownAction);
        myActionMap.put(homeAction, new HomeAction());
        myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), homeAction);
        myActionMap.put(endAction, new EndAction());
        myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), endAction);
        myActionMap.put(shiftHomeAction, new ShiftHomeAction());
        myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, KeyEvent.SHIFT_MASK), shiftHomeAction);
        myActionMap.put(shiftEndAction, new ShiftEndAction());
        myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, KeyEvent.SHIFT_MASK), shiftEndAction);
        //myActionMap.put("TTH", getDnew SHTMLEditorKitActions.ToggleTableHeaderCellAction(null));
        //myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, KeyEvent.SHIFT_MASK), "TTH");    
        myActionMap.setParent(getActionMap());
        myInputMap.setParent(getInputMap());
        setActionMap(myActionMap);
        setInputMap(JComponent.WHEN_FOCUSED, myInputMap);
        /*
         implementation before 1.4.1
         -------------------------------------

        Keymap map = getKeymap();
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        map.addActionForKeyStroke(tab, new NextTableCellAction(map.getAction(tab)));
        KeyStroke shiftTab = KeyStroke.getKeyStroke(
                                KeyEvent.VK_TAB, InputEvent.SHIFT_MASK);
        map.addActionForKeyStroke(shiftTab,
                        new PrevTableCellAction(map.getAction(shiftTab)));
        setKeymap(map);
        */
    }

    private static gnu.regexp.RE pattern1 = null;
    private static gnu.regexp.RE pattern2 = null;

    /* (non-Javadoc)
    * @see javax.swing.JComponent#processKeyBinding(javax.swing.KeyStroke, java.awt.event.KeyEvent, int, boolean)
    */
    protected boolean processKeyBinding(final KeyStroke ks, final KeyEvent e, final int condition, final boolean pressed) {
        final int maximumEndSelection = ((SHTMLDocument) getDocument()).getLastDocumentPosition();
        if (getSelectionStart() >= maximumEndSelection
                && !(ks.getKeyCode() == KeyEvent.VK_LEFT || ks.getKeyCode() == KeyEvent.VK_UP || ks.getKeyCode() == KeyEvent.VK_HOME)) {
            return true;
        }
        if (getSelectionEnd() >= maximumEndSelection) {
            setSelectionEnd(maximumEndSelection - 1);
        }
        return super.processKeyBinding(ks, e, condition, pressed);
    }

    /**
       * Convenience method for setting the document text
       * contains hack around JDK bug 4799813
       * see http://developer.java.sun.com/developer/bugParade/bugs/4799813.html
       * regression in 1.4.x, to be fixed in 1.5
       * When setting the text to be "&amp; footext", it becomes "&amp;footext" (space disappears)
       * same ocurrs for "&lt;/a&gt; &amp;amp;", it becomes "&lt;/a&gt;&amp;amp;" (space disappears)
       * with the hack it now does not occur anymore.
       * @param sText the html-text of the document
       */
    public void setText(String sText) {
        try {
            if (System.getProperty("java.version").substring(0, 3).equals("1.4")) {
                if (pattern1 == null) {
                    pattern1 = new gnu.regexp.RE("(&\\w+;|&#\\d+;)(\\s|&#160;|&nbsp;)(?=<|&\\w+;|&#\\d+;)");
                }
                sText = pattern1.substituteAll(sText, "$1&#160;$3");
                if (pattern2 == null) {
                    pattern2 = new gnu.regexp.RE("<(/[^>])>(\\s|&#160;|&nbsp;|\\n\\s+)(?!&#160;)(&\\w+;|&#\\d+;)");
                }
                sText = pattern2.substituteAll(sText, "<$1>&#160;$3$4");
            }
        }
        catch (final gnu.regexp.REException ree) {
            ree.printStackTrace();
        }
        final SHTMLDocument doc = (SHTMLDocument) getDocument();
        doc.startCompoundEdit();
        if (sText == null || sText.equals("")) {
            sText = "<html><body><p></p></body></html>";
        }
        doc.putProperty(SHTMLDocument.AdditionalComments, null);
        super.setText(sText);
        setCaretPosition(0);
        doc.endCompoundEdit();
        if (OLD_JAVA_VERSION) {
            SHTMLPanelImpl.getOwnerSHTMLPanel(this).purgeUndos();
        }
    }

    private class MyNavigationFilter extends NavigationFilter {
        /* (non-Javadoc)
         * @see javax.swing.text.NavigationFilter#moveDot(javax.swing.text.NavigationFilter.FilterBypass, int, javax.swing.text.Position.Bias)
         */
        public void moveDot(final FilterBypass fb, int dot, final Bias bias) {
            dot = getValidPosition(dot);
            super.moveDot(fb, dot, bias);
        }

        /* (non-Javadoc)
         * @see javax.swing.text.NavigationFilter#setDot(javax.swing.text.NavigationFilter.FilterBypass, int, javax.swing.text.Position.Bias)
         */
        public void setDot(final FilterBypass fb, int dot, final Bias bias) {
            dot = getValidPosition(dot);
            super.setDot(fb, dot, bias);
        }
    }

    private int getValidPosition(int position) {
        final SHTMLDocument doc = (SHTMLDocument) getDocument();
        final int lastValidPosition = doc.getLastDocumentPosition() - 1;
        if (position > lastValidPosition) {
            position = lastValidPosition;
        }
        int startPos = 0;
        if (doc.getDefaultRootElement().getElementCount() > 1) {
            startPos = doc.getDefaultRootElement().getElement(1).getStartOffset();
        }
        final int validPosition = Math.max(position, startPos);
        return validPosition;
    }

    private class DeletePrevCharAction extends AbstractAction {
        public void actionPerformed(final ActionEvent actionEvent) {
            final int selectionStart = getSelectionStart();
            final int selectionEnd = getSelectionEnd();
            final SHTMLDocument doc = (SHTMLDocument) getDocument();
            if (selectionEnd >= doc.getLastDocumentPosition()) {
                return;
            }
            if (selectionStart == selectionEnd) {
                final boolean intervention = listManager.deletePrevChar(actionEvent);
                if (intervention) {
                    return;
                }
                // Prevent deletion of table cell.
                final Element tableCell = selectionStart == 0 ? null : getTableCell(selectionStart - 1);
                if (tableCell != null && tableCell.getEndOffset() == selectionStart) {
                    performDefaultKeyStrokeAction(KeyEvent.VK_LEFT, 0, actionEvent);
                    return;
                }
            }
            performDefaultKeyStrokeAction('\b', 0, actionEvent);
        }
    }

    private class DeleteNextCharAction extends AbstractAction {
        public void actionPerformed(final ActionEvent actionEvent) {
            final int selectionStart = getSelectionStart();
            if (selectionStart == getSelectionEnd()) {
                final SHTMLDocument doc = (SHTMLDocument) getDocument();
                if (selectionStart >= doc.getLastDocumentPosition() - 1) {
                    return;
                }
                boolean intervention = treatTables(actionEvent);
                if (intervention) {
                    return;
                }
                intervention = listManager.deleteNextChar(actionEvent);
                if (intervention) {
                    return;
                }
            }
            performDefaultKeyStrokeAction(KeyEvent.VK_DELETE, 0, actionEvent);
        }

        /** Treats tables. Returns true if intervention was necessary. */
        private boolean treatTables(final ActionEvent event) {
            final int selectionStart = getSelectionStart();
            final int nextPosition = selectionStart + 1;
            Element elem = null;
            final SHTMLDocument doc = getSHTMLDocument();
            // Table cell element at the start of the selection
            elem = getCurrentTableCell();
            if (elem != null && elem.getEndOffset() == nextPosition) {
                // Do nothing to avoid deletion of the whole cell.
                //return;
            }
            if (nextPosition < doc.getLength()) {
                // Table cell element at next position
                elem = getTableCell(nextPosition);
                if (elem != null && elem.getStartOffset() == nextPosition) {
                    // In most cases, do nothing to avoid deletion of parts of the following table.
                    final Element paragraphElement = getCurrentParagraphElement();
                    final boolean emptyParagraph = elementIsEmptyParagraph(paragraphElement);
                    if (!caretWithinTableCell() && emptyParagraph) {
                        // Empty paragraph outside a table, before a table.
                        return false;
                    }
                    else if (caretWithinTableCell() && emptyParagraph) {
                        // Remove empty paragraph at the end of the cell. 
                        removeElement(paragraphElement);
                        setCaretPosition(getCaretPosition() - 1);
                        return true;
                    }
                    else {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private class MoveUpAction extends AbstractAction {
        public void actionPerformed(final ActionEvent e) {
            if (caretWithinTableCell()) {
                if (getCaretPosition() == 0) {
                    // The table is at the top of the document.
                    // Insert new paragraph before the table.
                    final Element tableElement = getCurrentTableCell().getParentElement().getParentElement();
                    try {
                        getSHTMLDocument().insertBeforeStart(tableElement, "<p></p>");
                    }
                    catch (final Exception ex) {
                    }
                }
                if (tryDefaultKeyStrokeActionWithinCell(KeyEvent.VK_UP, 0, e)) {
                    return;
                }
                final Element cellElement = getCurrentTableCell();
                final Element rowElement = cellElement.getParentElement();
                final Element tableElement = rowElement.getParentElement();
                final int cellIndexInRow = rowElement.getElementIndex(cellElement.getStartOffset());
                final int rowIndexInTable = tableElement.getElementIndex(rowElement.getStartOffset());
                if (rowIndexInTable > 0) {
                    final Element previousRowElement = tableElement.getElement(rowIndexInTable - 1);
                    final int elementCount = previousRowElement.getElementCount();
                    if (elementCount > 0) {
                        final Element targetCellElement = previousRowElement.getElement(Math.min(elementCount,
                            cellIndexInRow));
                        setCaretPosition(targetCellElement.getEndOffset());
                    }
                    else {
                        setCaretPosition(tableElement.getStartOffset());
                    }
                }
                else {
                    setCaretPosition(tableElement.getStartOffset());
                }
                //}
            }
            performDefaultKeyStrokeAction(KeyEvent.VK_UP, 0, e);
        }
    }

    private class MoveDownAction extends AbstractAction {
        public void actionPerformed(final ActionEvent e) {
            if (caretWithinTableCell()) {
                if (tryDefaultKeyStrokeActionWithinCell(KeyEvent.VK_DOWN, 0, e)) {
                    return;
                }
                final Element cellElement = getCurrentTableCell();
                final Element rowElement = cellElement.getParentElement();
                final Element tableElement = rowElement.getParentElement();
                final int cellIndexInRow = rowElement.getElementIndex(cellElement.getStartOffset());
                final int rowIndexInTable = tableElement.getElementIndex(rowElement.getStartOffset());
                if (rowIndexInTable < tableElement.getElementCount() - 1) {
                    final Element nextRowElement = tableElement.getElement(rowIndexInTable + 1);
                    final int elementCount = nextRowElement.getElementCount();
                    if (elementCount > 0) {
                        final Element targetCellElement = nextRowElement.getElement(Math.min(elementCount,
                            cellIndexInRow));
                        //Element targetInnerElement = targetCellElement.getElement(0); // p or p-implied.
                        setCaretPosition(targetCellElement.getStartOffset() - 1);
                    }
                    else {
                        setCaretPosition(tableElement.getEndOffset() - 1);
                    }
                }
                else {
                    setCaretPosition(tableElement.getEndOffset() - 1);
                }
                //}
            }
            performDefaultKeyStrokeAction(KeyEvent.VK_DOWN, 0, e);
        }
    }

    private class HomeAction extends AbstractAction {
        public void actionPerformed(final ActionEvent e) {
            if (caretWithinTableCell()) {
                if (tryDefaultKeyStrokeActionWithinCell(KeyEvent.VK_HOME, 0, e)) {
                    return;
                }
                setCaretPosition(getCurrentParagraphElement().getStartOffset());
            }
            else {
                performDefaultKeyStrokeAction(KeyEvent.VK_HOME, 0, e);
            }
        }
    }

    private class EndAction extends AbstractAction {
        public void actionPerformed(final ActionEvent e) {
            if (caretWithinTableCell()) {
                if (tryDefaultKeyStrokeActionWithinCell(KeyEvent.VK_END, 0, e)) {
                    return;
                }
                setCaretPosition(getCurrentParagraphElement().getEndOffset() - 1);
            }
            else {
                performDefaultKeyStrokeAction(KeyEvent.VK_END, 0, e);
            }
        }
    }

    private class ShiftHomeAction extends AbstractAction {
        public void actionPerformed(final ActionEvent e) {
            if (caretWithinTableCell()) {
                final int originalCaretPosition = getCaretPosition();
                if (tryDefaultKeyStrokeActionWithinCell(KeyEvent.VK_HOME, KeyEvent.SHIFT_MASK, e)) {
                    return;
                }
                final int newCaretPosition = getCurrentParagraphElement().getStartOffset();
                if (newCaretPosition > originalCaretPosition) {
                    select(originalCaretPosition, newCaretPosition);
                }
                else {
                    select(newCaretPosition, originalCaretPosition);
                }
            }
            else {
                performDefaultKeyStrokeAction(KeyEvent.VK_HOME, KeyEvent.SHIFT_MASK, e);
            }
        }
    }

    private class ShiftEndAction extends AbstractAction {
        public void actionPerformed(final ActionEvent e) {
            if (caretWithinTableCell()) {
                final int originalCaretPosition = getCaretPosition();
                if (tryDefaultKeyStrokeActionWithinCell(KeyEvent.VK_END, KeyEvent.SHIFT_MASK, e)) {
                    return;
                }
                final int newCaretPosition = getCurrentParagraphElement().getEndOffset() - 1;
                if (newCaretPosition > originalCaretPosition) {
                    select(originalCaretPosition, newCaretPosition);
                }
                else {
                    select(newCaretPosition, originalCaretPosition);
                }
            }
            else {
                performDefaultKeyStrokeAction(KeyEvent.VK_END, KeyEvent.SHIFT_MASK, e);
            }
        }
    }

    /* ------- list manipulation start ------------------- */
    /**
     * apply a set of attributes to the list the caret is
     * currently in (if any)
     *
     * @param a  the set of attributes to apply
     */
    public void applyListAttributes(final AttributeSet a) {
        final SHTMLDocument doc = (SHTMLDocument) getDocument();
        final Element list = listManager.getListElement(getSelectionStart());
        if (list != null) {
            if (a.getAttributeCount() > 0) {
                doc.addAttributes(list, a);
                /**
                 * for some reason above code does not show the changed attributes
                 * of the table, although the element really has them (maybe somebody
                 * could let me know why...). Therefore we update the editor pane
                 * contents comparably rude (any other more elegant alternatives
                 * welcome!)
                 *
                 * --> found out why: the swing package does not render short hand
                 *                    properties such as MARGIN or PADDING. When
                 *                    contained in a document inside an AttributeSet
                 *                    they already have to be split into MARGIN-TOP,
                 *                    MARGIN-LEFT, etc.
                 *                    adjusted AttributeComponents accordingly so
                 *                    we don't need refresh anymore
                 */
                //refresh();
            }
        }
    }

    /**
     * <code>Action</code> to create a new paragraph element, which
     * may be a paragraph proper or a list item.
     */
    private class NewParagraphAction extends AbstractAction {
        /** construct a <code>NewParagraphAction</code> */
        public NewParagraphAction() {
        }

        /**
         * create a new list item, when the caret is inside a list
         *
         * <p>The new item is created after the item at the caret position</p>
         */
        public void actionPerformed(final ActionEvent ae) {
            try {
                final int caretPosition = getCaretPosition();
                // if we are in a list, create a new item
                final Element listItemElement = listManager.getListItemElement(caretPosition);
                if (listItemElement != null) {
                    listManager.newListItem();
                    return;
                }
                // we are not in a list, call alternate action
                else {
                    performDefaultKeyStrokeAction(KeyEvent.VK_ENTER, 0, ae);
                }
            }
            catch (final Exception e) {
                Util.errMsg(null, e.getMessage(), e);
            }
        }
    }

    /**
     * toggle list formatting on or off for the currently
     * selected text portion.
     *
     * <p>Switches list display on for the given type, if the selection
     * contains parts not formatted as list or parts formatted as list
     * of another type.</p>
     *
     * <p>Switches list formatting off, if the selection contains
     * only parts formatted as list of the given type.</p>
     *
     * @param listTag  the list tag type to toggle on or off (UL or OL)
     * @param attributeSet  the attributes to use for the list to toggle to
     * @param forceOff  indicator for toggle operation. If true, possibly
     * exisiting list formatting inside the selected parts always is switched
     * off. If false, the method decides, if list formatting for the parts
     * inside the selection needs to be switched on or off.
     */
    public void toggleList(final String listTag, final AttributeSet attributeSet, final boolean forceOff) {
        listManager.toggleList(listTag, attributeSet, forceOff);
    }

    /** range indicator for applying attributes to the current cell only */
    public static final int THIS_CELL = 0;
    /** range indicator for applying attributes to cells of the current column only */
    public static final int THIS_COLUMN = 1;
    /** range indicator for applying attributes to cells of the current row only */
    public static final int THIS_ROW = 2;
    /** range indicator for applying attributes to all cells */
    public static final int ALL_CELLS = 3;
    /** default table width */
    public static final String DEFAULT_TABLE_WIDTH = "80%";
    /** default vertical alignment */
    public static final String DEFAULT_VERTICAL_ALIGN = "top";

    /**
     * Insert a new table.
     *
     * @param colCount the number of columns the new table shall have
     */
    public void insertNewTable(final int colCount) {
        final int selectionStart = getSelectionStart();
        final int start = selectionStart;
        final StringWriter sw = new StringWriter();
        final SHTMLDocument doc = (SHTMLDocument) getDocument();
        final SHTMLWriter w = new SHTMLWriter(sw, doc);
        // some needed constants
        final String table = HTML.Tag.TABLE.toString();
        final String tr = HTML.Tag.TR.toString();
        final String td = HTML.Tag.TD.toString();
        final String th = HTML.Tag.TH.toString();
        final String p = HTML.Tag.P.toString();
        final boolean insertPlainTable = !Util.preferenceIsTrue("table.insertStyle", "true");
        final boolean insertTableHeader = Util.preferenceIsTrue("table.insertHeader");
        try {
            // the attribute set to use for applying attributes to tags
            final SimpleAttributeSet tableAttributeSet = new SimpleAttributeSet();
            // build table attribute
            Util.styleSheet().addCSSAttribute(tableAttributeSet, CSS.Attribute.WIDTH, DEFAULT_TABLE_WIDTH);
            Util.styleSheet().addCSSAttribute(tableAttributeSet, CSS.Attribute.BORDER_STYLE, "solid");
            Util.styleSheet().addCSSAttribute(tableAttributeSet, CSS.Attribute.BORDER_TOP_WIDTH, "0");
            Util.styleSheet().addCSSAttribute(tableAttributeSet, CSS.Attribute.BORDER_RIGHT_WIDTH, "0");
            Util.styleSheet().addCSSAttribute(tableAttributeSet, CSS.Attribute.BORDER_BOTTOM_WIDTH, "0");
            Util.styleSheet().addCSSAttribute(tableAttributeSet, CSS.Attribute.BORDER_LEFT_WIDTH, "0");
            tableAttributeSet.addAttribute(HTML.Attribute.BORDER, "0");
            w.writeStartTag(table, insertPlainTable ? null : tableAttributeSet);
            // get width of each cell according to column count
            // build cell width attribute
            Util.styleSheet().addCSSAttribute(tableAttributeSet, CSS.Attribute.WIDTH,
                Integer.toString(100 / colCount) + Util.pct);
            tableAttributeSet.addAttribute(HTML.Attribute.VALIGN, DEFAULT_VERTICAL_ALIGN);
            Util.styleSheet().addCSSAttribute(tableAttributeSet, CSS.Attribute.BORDER_TOP_WIDTH, "1");
            Util.styleSheet().addCSSAttribute(tableAttributeSet, CSS.Attribute.BORDER_RIGHT_WIDTH, "1");
            Util.styleSheet().addCSSAttribute(tableAttributeSet, CSS.Attribute.BORDER_BOTTOM_WIDTH, "1");
            Util.styleSheet().addCSSAttribute(tableAttributeSet, CSS.Attribute.BORDER_LEFT_WIDTH, "1");
            final SimpleAttributeSet pSet = new SimpleAttributeSet();
            Util.styleSheet().addCSSAttribute(pSet, CSS.Attribute.MARGIN_TOP, "1");
            Util.styleSheet().addCSSAttribute(pSet, CSS.Attribute.MARGIN_RIGHT, "1");
            Util.styleSheet().addCSSAttribute(pSet, CSS.Attribute.MARGIN_BOTTOM, "1");
            Util.styleSheet().addCSSAttribute(pSet, CSS.Attribute.MARGIN_LEFT, "1");
            tableAttributeSet.removeAttribute(HTML.Attribute.BORDER);
            if (insertTableHeader) {
                w.writeStartTag(tr, null);
                for (int i = 0; i < colCount; i++) {
                    w.writeStartTag(th, insertPlainTable ? null : tableAttributeSet);
                    w.writeStartTag(p, insertPlainTable ? null : pSet);
                    w.writeEndTag(p);
                    w.writeEndTag(th);
                }
                w.writeEndTag(tr);
            }
            w.writeStartTag(tr, null);
            for (int i = 0; i < colCount; i++) {
                w.writeStartTag(td, insertPlainTable ? null : tableAttributeSet);
                w.writeStartTag(p, insertPlainTable ? null : pSet);
                w.writeEndTag(p);
                w.writeEndTag(td);
            }
            w.writeEndTag(tr);
            w.writeEndTag(table);
            // read table html into document
            Element para = doc.getParagraphElement(selectionStart);
            if (para == null) {
                throw new Exception("no text selected");
            }
            for (Element parent = para.getParentElement(); !parent.getName().equalsIgnoreCase(HTML.Tag.BODY.toString())
                    && !parent.getName().equalsIgnoreCase(HTML.Tag.TD.toString()); para = parent, parent = parent
                .getParentElement()) {
                ;
            }
            if (para != null) {
                try {
                    doc.startCompoundEdit();
                    doc.insertBeforeStart(para, sw.getBuffer().toString());
                }
                catch (final Exception e) {
                    Util.errMsg(null, e.getMessage(), e);
                }
                finally {
                    doc.endCompoundEdit();
                }
                //      w.write(para);
                //doc.replaceHTML(para, 1, sw.getBuffer().toString());
            }
        }
        catch (final Exception ex) {
            Util.errMsg(null, ex.getMessage(), ex);
        }
        select(start, start);
    }

    /**
     * apply a new anchor to the currently selected text
     *
     * <p>If nothing is selected, this method does nothing</p>
     *
     * @param anchorName  the name of the new anchor
     */
    public void insertAnchor(final String anchorName) {
        if (getSelectionStart() != getSelectionEnd()) {
            final SimpleAttributeSet aSet = new SimpleAttributeSet();
            aSet.addAttribute(HTML.Attribute.NAME, anchorName);
            final SimpleAttributeSet set = new SimpleAttributeSet();
            set.addAttribute(HTML.Tag.A, aSet);
            applyAttributes(set, false);
        }
    }

    /**
     * insert a line break (i.e. a break for which paragraph
     * spacing is not applied)
     */
    public void insertBreak() {
        final int caretPos = getCaretPosition();
        final SHTMLDocument doc = (SHTMLDocument) getDocument();
        try {
            ((SHTMLEditorKit) getEditorKit()).insertHTML(doc, caretPos, "<BR>", 0, 0, HTML.Tag.BR);
        }
        catch (final Exception e) {
        }
        setCaretPosition(caretPos + 1);
    }

    /**
     * set a text link at the current selection replacing the selection
     * with a given text.
     *
     * <p>If nothing is selected, but the caret is inside a link, this will
     * replace the existing link. If nothing is selected and the caret
     * is not inside a link, this method does nothing.</p>
     *
     * @param linkText  the text that shall appear as link at the current selection
     * @param href  the target this link shall refer to
     * @param className  the style class to be used
     */
    public void setLink(final String linkText, final String href, final String className) {
        setLink(linkText, href, className, null, null);
    }

    /**
     * Sets a hyperlink at the current selection, replacing the selection
     * with the given text or image.
     *
     * @param linkText  the text to show as link (or null, if an image shall appear instead)
     * @param href  the link reference
     * @param className  the style name to be used for the link
     * @param linkImage  the file name of the image be used for the link (or null, if a text link is to be set instead)
     * @param size  the size of the image or null
     */
    public void setLink(final String linkText, final String href, final String className, final String linkImage,
                        final Dimension size) {
        if (linkImage == null) {
            setTextLink(getCurrentLinkElement(), href, className, linkText, getSHTMLDocument());
        }
        else {
            setImageLink(getSHTMLDocument(), getCurrentLinkElement(), href, className, linkImage, size);
        }
    }

    /**
     * set an image link replacing the current selection
     *
     * @param doc  the document to apply the link to
     * @param e  the link element found at the selection, or null if none was found
     * @param href  the link reference
     * @param className  the style name to be used for the link
     * @param linkImage  the file name of the image be used for the link
     * @param size  the size of the image
     */
    private void setImageLink(final SHTMLDocument doc, final Element e, final String href, final String className,
                              final String linkImage, final Dimension size) {
        final String a = HTML.Tag.A.toString();
        SimpleAttributeSet set = new SimpleAttributeSet();
        set.addAttribute(HTML.Attribute.HREF, href);
        final String sStyleName = Util.getResourceString("standardStyleName");
        if (className != null && !className.equalsIgnoreCase(sStyleName)) {
            set.addAttribute(HTML.Attribute.CLASS, className);
        }
        final StringWriter sw = new StringWriter();
        final SHTMLWriter w = new SHTMLWriter(sw, doc);
        try {
            w.writeStartTag(a, set);
            set = new SimpleAttributeSet();
            set.addAttribute(HTML.Attribute.SRC,
                Util.getRelativePath(new File(doc.getBase().getFile()), new File(linkImage)));
            set.addAttribute(HTML.Attribute.BORDER, "0");
            if (size != null) {
                set.addAttribute(HTML.Attribute.WIDTH, Integer.toString(new Double(size.getWidth()).intValue()));
                set.addAttribute(HTML.Attribute.HEIGHT, Integer.toString(new Double(size.getHeight()).intValue()));
            }
            w.writeStartTag(HTML.Tag.IMG.toString(), set);
            w.writeEndTag(a);
            if (e != null) {
                System.out.println("SHTMLEditorPane.setImageLink setOuterHTML html='" + sw.getBuffer() + "'");
                doc.setOuterHTML(e, sw.getBuffer().toString());
            }
            else {
                final int start = getSelectionStart();
                if (start < getSelectionEnd()) {
                    replaceSelection("");
                    System.out.println("SHTMLEditorPane.setImageLink insertAfterEnd html='" + sw.getBuffer() + "'");
                    doc.insertAfterEnd(doc.getCharacterElement(start), sw.getBuffer().toString());
                }
            }
        }
        catch (final Exception ex) {
            Util.errMsg(this, ex.getMessage(), ex);
        }
    }

    /**
     * set a text link replacing the current selection
     *
     * @param e  the link element found at the selection, or null if none was found
     * @param href  the link reference
     * @param className  the style name to be used for the link
     * @param linkText  the text to show as link
     * @param doc  the document to apply the link to
     */
    private void setTextLink(final Element e, final String href, final String className, String linkText,
                             final SHTMLDocument doc) {
        final String sStyleName = Util.getResourceString("standardStyleName");
        final SimpleAttributeSet set = new SimpleAttributeSet();
        final SimpleAttributeSet aSet = new SimpleAttributeSet();
        if(href != null){
        	aSet.addAttribute(HTML.Attribute.HREF, href);
        	if (className != null && !className.equalsIgnoreCase(sStyleName)) {
        		aSet.addAttribute(HTML.Attribute.CLASS, className);
        	}
        }
        if (e != null) {
            // replace existing link
        	set.addAttributes(e.getAttributes());
        	if(href != null){
        		set.addAttribute(HTML.Tag.A, aSet);
        	}
        	else{
        		set.removeAttribute(HTML.Tag.A);
        	}
            final int start = e.getStartOffset();
            int length = e.getEndOffset() - start;
            try {
                if(linkText == null)
                	linkText = doc.getText(start, length);
				doc.replace(start, length, linkText, set);
            }
            catch (final BadLocationException ex) {
                Util.errMsg(this, ex.getMessage(), ex);
            }
        }
        else if(href != null){
            // create new link for text selection
            final int start = getSelectionStart();
            if (start < getSelectionEnd()) {
                set.addAttribute(HTML.Tag.A, aSet);
                try {
                	if(linkText == null)
                		linkText = doc.getText(start, getSelectionEnd() - start);
                	replaceSelection(linkText);
                	doc.setCharacterAttributes(start, linkText.length(), set, false);
                }
                catch (final BadLocationException ex) {
                	Util.errMsg(this, ex.getMessage(), ex);
                }
            }
        }
    }

    /**
     * remove an anchor with a given name
     *
     * @param anchorName  the name of the anchor to remove
     */
    public void removeAnchor(final String anchorName) {
        //System.out.println("SHTMLEditorPane removeAnchor");
        AttributeSet attrs;
        Object nameAttr;
        Object link;
        final ElementIterator eli = new ElementIterator(getDocument());
        Element elem = eli.first();
        while (elem != null) {
            attrs = elem.getAttributes();
            link = attrs.getAttribute(HTML.Tag.A);
            if (link != null /*&& link.toString().equalsIgnoreCase(HTML.Tag.A.toString())*/) {
                //System.out.println("found anchor attribute");
                nameAttr = ((AttributeSet) link).getAttribute(HTML.Attribute.NAME);
                if (nameAttr != null && nameAttr.toString().equalsIgnoreCase(anchorName)) {
                    // remove anchor here
                    //System.out.println("removing anchor name=" + nameAttr);
                    final SimpleAttributeSet newSet = new SimpleAttributeSet(attrs);
                    newSet.removeAttribute(HTML.Tag.A);
                    final SHTMLDocument doc = (SHTMLDocument) getDocument();
                    final int start = elem.getStartOffset();
                    doc.setCharacterAttributes(elem.getStartOffset(), elem.getEndOffset() - start, newSet, true);
                }
            }
            elem = eli.next();
        }
    }

    /**
     * insert a table column before the current column
     * (if any)
     */
    public void insertTableColumn() {
        final Element cell = getCurrentTableCell();
        if (cell != null) {
            createTableColumn(cell, Util.getElementIndex(cell)/*getColNumber(cell)*/, true);
        }
    }

    /**
     * append a table column after the last column
     * (if any)
     */
    public void appendTableColumn() {
        final Element cell = getCurrentTableCell();
        if (cell != null) {
            final Element lastCell = getLastTableCell(cell);
            createTableColumn(lastCell, Util.getElementIndex(cell)/*getColNumber(lastCell)*/, false);
        }
    }

    /**
     * create a table column before or after a given column
     *
     * the width of the first cell in the column
     * (if there is a width attribute) is split into
     * half so that the new column and the column
     * inserted before are sharing the space originally
     * taken by the column inserted before.
     *
     * @param cell  the cell to copy from
     * @param cIndex  the number of the column 'cell' is in
     * @param before  true indicates insert before, false append after
     */
    private void createTableColumn(final Element cell, final int cIndex, final boolean before) {
        // get the new width setting for this column and the new column
        final SHTMLDocument doc = (SHTMLDocument) getDocument();
        doc.startCompoundEdit();
        final Element table = cell.getParentElement().getParentElement();
        Element srcCell = table.getElement(0).getElement(cIndex);
        final SimpleAttributeSet set = new SimpleAttributeSet();
        final Object attr = set.getAttribute(CSS.Attribute.WIDTH);
        if (attr != null) {
            //LengthValue lv = new LengthValue(attr);
            //String unit = lv.getUnit();
            //int width = (int) lv.getAttrValue(attr.toString(), unit);
            final int width = (int) Util.getAbsoluteAttrVal(attr); // Util.getAttrValue(attr);
            //System.out.println("SHTMLEditorPane.createTableColumn width=" + width);
            final String unit = Util.getLastAttrUnit();
            //System.out.println("SHTMLEditorPane.createTableColumn unit=" + unit);
            final String widthString = Integer.toString(width / 2) + unit;
            //System.out.println("SHTMLEditorPane.createTableColumn widthString=" + widthString);
            Util.styleSheet().addCSSAttribute(set, CSS.Attribute.WIDTH, widthString);
        }
        // adjust width and insert new column
        //SimpleAttributeSet a = new SimpleAttributeSet(set.copyAttributes());
        //int cellIndex = getCellIndex(srcCell);
        //boolean insertFirst = (before && (cellIndex == 0));
        for (int rIndex = 0; rIndex < table.getElementCount(); rIndex++) {
            srcCell = table.getElement(rIndex).getElement(cIndex);
            /*
            if(rIndex > 0) {
              adjustBorder(a, a, a, CombinedAttribute.ATTR_TOP);
            }
            adjustBorder(a, a, a, CombinedAttribute.ATTR_LEFT);
            */
            doc.addAttributes(srcCell, set);
            try {
                if (before) {
                    doc.insertBeforeStart(srcCell, createTableCellHTML(srcCell));
                }
                else {
                    doc.insertAfterEnd(srcCell, createTableCellHTML(srcCell));
                }
            }
            catch (final IOException ioe) {
                Util.errMsg(null, ioe.getMessage(), ioe);
            }
            catch (final BadLocationException ble) {
                Util.errMsg(null, ble.getMessage(), ble);
            }
        }
        //adjustColumnBorders(table.getElement(0).getElement(cIndex));
        //adjustColumnBorders(table.getElement(0).getElement(++cIndex));
        doc.endCompoundEdit();
    }

    /**
     * Appends a row to a table if the caret is inside a table.
     */
    public void appendTableRow() {
        final Element cell = getCurrentTableCell();
        if (cell != null) {
            final Element table = cell.getParentElement().getParentElement();
            final Element lastRow = table.getElement(table.getElementCount() - 1);
            createTableRow(lastRow, Util.getRowIndex(lastRow.getElement(0)), false, null);
        }
    }

    /**
     * Inserts a row to a table, assuming the caret is currently
     * inside a table.
     */
    public void insertTableRow(final String forcedCellName) {
        final Element cell = getCurrentTableCell();
        if (cell != null) {
            createTableRow(cell.getParentElement(), Util.getRowIndex(cell), true, forcedCellName);
        }
    }

    /**
     * Creates a new table row, inserting it before, or appending after the given
     * row, depending on a parameter.
     *
     * Is shared by appendRow and insertRow actions.
     *
     * @param srcRow  the row element to copy from
     * @param before  true indicates insert before, false append after
     * @param forcedCellName if non-null, that cell name will be used in the new table row. Values: "td", "th".
     */
    private void createTableRow(final Element srcRow, int rowIndex, final boolean before, final String forcedCellName) {
        try {
            if (before) {
                getSHTMLDocument().insertBeforeStart(srcRow, createTableRowHTML(srcRow, forcedCellName));
                if (rowIndex == 0) {
                    rowIndex++;
                }
            }
            else {
                getSHTMLDocument().insertAfterEnd(srcRow, createTableRowHTML(srcRow, forcedCellName));
                rowIndex++;
            }
        }
        catch (final IOException ioe) {
            Util.errMsg(null, ioe.getMessage(), ioe);
        }
        catch (final BadLocationException ble) {
            Util.errMsg(null, ble.getMessage(), ble);
        }
    }

    /**
     * Returns the HTML string of the given table row, without the cell contents, possibly
     * forcing the cell name on "td" or "th", if the parameter for forced cell name is non-null.
     *
     * For each table column found in srcRow, a start and end
     * tag TD is created with the same attributes as in the
     * column found in srcRow. The attributes of srcRow
     * are applied to the newly created row HTML string as well.
     *
     * @param modelRow  the table row Element to copy from
     * @param insert  indicates if a row is inserted before another row
     *
     * @return an HTML string representing the new table row
     * (without cell contents)
     */
    private String createTableRowHTML(final Element modelRow, final String forcedCellName) {
        final StringWriter stringWriter = new StringWriter();
        final SHTMLWriter shtmlWriter = new SHTMLWriter(stringWriter, (SHTMLDocument) getDocument());
        final String tr = HTML.Tag.TR.toString();
        try {
            shtmlWriter.writeStartTag(tr, modelRow.getAttributes());
            for (int i = 0; i < modelRow.getElementCount(); i++) {
                final Element modelCell = modelRow.getElement(i);
                final String cellName = forcedCellName != null ? forcedCellName : modelCell.getName();
                createTableCellHTML(shtmlWriter, modelCell, cellName);
            }
            shtmlWriter.writeEndTag(tr);
        }
        catch (final IOException ex) {
            Util.errMsg(null, ex.getMessage(), ex);
        }
        return stringWriter.getBuffer().toString();
    }

    private void createTableCellHTML(final SHTMLWriter w, final Element cell, final String cellName) throws IOException {
        {
            w.writeStartTag(cellName, cell.getAttributes());
            final Element paragraph = cell.getElement(0);
            final String parName = "p"; //Was: paragraph.getName(); UL and OL are parNames not to be copied. --Dan
            if (!parName.equalsIgnoreCase(HTML.Tag.IMPLIED.toString())) {
                w.writeStartTag(parName, paragraph.getAttributes());
                w.writeEndTag(parName);
            }
            w.writeEndTag(cellName);
        }
    }

    /**
     * build an HTML string copying from an existing table cell
     *
     * @param srcCell the cell to get the HTML for
     * @param a  set of attributes to copy if we are inserting first table column
     * @param insertFirst  indicates if we are inserting first table column
     * @param rNum  number of row a cell is to be inserted to
     *     (can be any value if insertFirst is false)
     *
     * @return the HTML string for the given cell (without cell contents)
     */
    private String createTableCellHTML(final Element srcCell) {
        final StringWriter sw = new StringWriter();
        final SHTMLWriter w = new SHTMLWriter(sw, (SHTMLDocument) getDocument());
        try {
            createTableCellHTML(w, srcCell, srcCell.getName());
        }
        catch (final IOException e) {
            Util.errMsg(null, e.getMessage(), e);
        }
        //System.out.println("getTableCellHTML buffer='" + sw.getBuffer().toString() + "'");
        return sw.getBuffer().toString();
    }

    /**
     * delete the row of the table the caret is currently in (if any)
     */
    public void deleteTableRow() {
        final Element cell = getCurrentTableCell();
        final int finalCaretPosition = cell.getStartOffset();
        if (cell != null) {
            removeElement(cell.getParentElement());
            final int docLength = getDocument().getLength();
            if (docLength >= finalCaretPosition) {
                setCaretPosition(finalCaretPosition);
            }
            else {
                setCaretPosition(docLength);
            }
        }
    }

    /**
     * delete the column of the table the caret is currently in (if any)
     *
     * <p>width of adjacent column is adjusted, if there is more than one
     * column in the table. Width adjustment only works, if width
     * attributes of both the column to remove and its adjacent column
     * have the same unit (pt or %).</p>
     *
     * <p>If there is only one cell or if the caret is not in a table,
     * this method does nothing</p>
     *
     * <p>Smart border handling automatically sets the left border of a cell
     * to zero, if the cell on the left of that cell has a right border and
     * both cells have no margin. In that case removing the first column
     * will cause all cells of the new first column to have no left border.</p>
     */
    public void deleteTableCol() {
        final Element cell = getCurrentTableCell();
        if (cell == null) {
            return;
        }
        Element row = cell.getParentElement();
        final int lastColIndex = row.getElementCount() - 1;
        if (lastColIndex <= 0) {
            return;
        }
        final int cIndex = Util.getElementIndex(cell); //getColNumber(cell);
        int offset = -1; // adjacent cell is left of current cell
        if (cIndex == 0) { // if current cell is in first column...
            offset *= -1; // ...adjacent cell is right of current cell
        }
        final Object attrC = cell.getAttributes().getAttribute(CSS.Attribute.WIDTH);
        final Object attrA = row.getElement(cIndex + offset).getAttributes().getAttribute(CSS.Attribute.WIDTH);
        SimpleAttributeSet set = null;
        if (attrC != null && attrA != null) {
            //LengthValue lvC = new LengthValue(attrC);
            //LengthValue lvA = new LengthValue(attrA);
            final int widthC = (int) Util.getAbsoluteAttrVal(attrC); // Util.getAttrValue(attrC);
            final String cUnit = Util.getLastAttrUnit();
            //String cUnit = lvC.getUnit();
            final int widthA = (int) Util.getAbsoluteAttrVal(attrA); // Util.getAttrValue(attrA);
            final String aUnit = Util.getLastAttrUnit();
            if (aUnit.equalsIgnoreCase(cUnit)) {
                int width = 0;
                width += widthC;
                width += widthA;
                if (width > 0) {
                    final String widthString = Integer.toString(width) + cUnit;
                    set = new SimpleAttributeSet(row.getElement(cIndex + offset).getAttributes());
                    Util.styleSheet().addCSSAttribute(set, CSS.Attribute.WIDTH, widthString);
                }
            }
        }
        final Element table = row.getParentElement();
        final SHTMLDocument doc = (SHTMLDocument) getDocument();
        doc.startCompoundEdit();
        if (cIndex < lastColIndex) {
            offset = 0;
        }
        for (int rIndex = table.getElementCount() - 1; rIndex >= 0; rIndex--) {
            row = table.getElement(rIndex);
            try {
                doc.removeElements(row, cIndex, 1);
                /*
                    the following line does not work for the last column in a table
                    so we use above code instead

                    removeElement(row.getElement(cIndex));
                 */
            }
            catch (final BadLocationException ble) {
                Util.errMsg(null, ble.getMessage(), ble);
            }
            if (set != null) {
                doc.addAttributes(row.getElement(cIndex + offset), set);
            }
            //adjustColumnBorders(table.getElement(0).getElement(cIndex + offset));
        }
        doc.endCompoundEdit();
    }

    /** For each cell within the selection, turns a table data cell into a table header cell or vice
     * versa. */
    public void toggleTableHeaderCell() {
        final int originalCaretPosition = getCaretPosition();
        final int selectionStart = getSelectionStart();
        final int selectionEnd = getSelectionEnd();
        Element tableCell = getTableCell(selectionStart);
        while (tableCell != null && tableCell.getStartOffset() <= selectionEnd) {
            final String content = elementToHTML(tableCell);
            String newContent = content;
            if (content.matches("(?is)\\s*<td.*")) {
                newContent = content.replaceFirst("(?is)^\\s*<td", "<th").replaceFirst("(?is)</td>\\s*$", "</th>");
            }
            else if (content.matches("(?is)\\s*<th.*")) {
                newContent = content.replaceFirst("(?is)^\\s*<th", "<td").replaceFirst("(?is)</th>\\s*$", "</td>");
            }
            else {
                // Unexpected
            }
            final Element row = tableCell.getParentElement();
            final int tableCellIdx = row.getElementIndex(tableCell.getStartOffset());
            try {
                getSHTMLDocument().setOuterHTML(tableCell, newContent);
            }
            catch (final Exception ex) {
            }
            tableCell = row.getElement(tableCellIdx); // Restore the cell reference after the operation.
            tableCell = getNextCell(tableCell);
        }
        setCaretPosition(originalCaretPosition);
    }

    /** Moves the table row up. Does not treat multirow cells. */
    void moveTableRowUp() {
        final Element tableCell = getCurrentTableCell();
        final Element tableRow = tableCell.getParentElement();
        final Element table = tableRow.getParentElement();
        final int indexOfRowInTable = table.getElementIndex(getCaretPosition());
        if (indexOfRowInTable == 0) {
            return;
        }
        try {
            getSHTMLDocument().startCompoundEdit();
            final SHTMLWriter writer = new SHTMLWriter(getSHTMLDocument());
            writer.writeStartTag(table);
            for (int i = 0; i < indexOfRowInTable - 1; i++) {
                writer.write(table.getElement(i));
            }
            writer.write(tableRow);
            writer.write(table.getElement(indexOfRowInTable - 1));
            for (int i = indexOfRowInTable + 1; i < table.getElementCount(); i++) {
                writer.write(table.getElement(i));
            }
            writer.writeEndTag(table);
            final int offsetWithinCurrentRow = getCaretPosition() - tableRow.getStartOffset();
            final int finalCaretPosition = table.getElement(indexOfRowInTable - 1).getStartOffset()
                    + offsetWithinCurrentRow;
            getSHTMLDocument().setOuterHTML(table, writer.getWrittenString());
            setCaretPosition(finalCaretPosition);
        }
        catch (final Exception ex) {
        }
        finally {
            getSHTMLDocument().endCompoundEdit();
        }
    }

    /** Moves the table column left. Does not treat multicolumn cells. */
    void moveTableColumnLeft() {
        final Element tableCell = getCurrentTableCell();
        final Element tableRow = tableCell.getParentElement();
        final Element table = tableRow.getParentElement();
        final int indexOfCellInRow = tableRow.getElementIndex(getCaretPosition());
        if (indexOfCellInRow == 0) {
            return;
        }
        try {
            getSHTMLDocument().startCompoundEdit();
            final SHTMLWriter writer = new SHTMLWriter(getSHTMLDocument());
            writer.writeStartTag(table);
            for (int rowIdx = 0; rowIdx < table.getElementCount(); rowIdx++) {
                final Element row = table.getElement(rowIdx);
                writer.writeStartTag(row);
                for (int i = 0; i < indexOfCellInRow - 1; i++) {
                    writer.write(row.getElement(i));
                }
                writer.write(row.getElement(indexOfCellInRow));
                writer.write(row.getElement(indexOfCellInRow - 1));
                for (int i = indexOfCellInRow + 1; i < row.getElementCount(); i++) {
                    writer.write(row.getElement(i));
                }
                writer.writeEndTag(row);
            }
            writer.writeEndTag(table);
            final int offsetWithinCurrentCell = getCaretPosition() - tableCell.getStartOffset();
            final int finalCaretPosition = tableRow.getElement(indexOfCellInRow - 1).getStartOffset()
                    + offsetWithinCurrentCell;
            getSHTMLDocument().setOuterHTML(table, writer.getWrittenString());
            setCaretPosition(finalCaretPosition);
        }
        catch (final Exception ex) {
        }
        finally {
            getSHTMLDocument().endCompoundEdit();
        }
    }

    /** Moves the table column right. Does not treat multicolumn cells. */
    void moveTableColumnRight() {
        final Element tableCell = getCurrentTableCell();
        final Element tableRow = tableCell.getParentElement();
        final Element table = tableRow.getParentElement();
        final int indexOfCellInRow = tableRow.getElementIndex(getCaretPosition());
        if (indexOfCellInRow == tableRow.getElementCount() - 1) {
            return;
        }
        try {
            getSHTMLDocument().startCompoundEdit();
            final SHTMLWriter writer = new SHTMLWriter(getSHTMLDocument());
            writer.writeStartTag(table);
            for (int rowIdx = 0; rowIdx < table.getElementCount(); rowIdx++) {
                final Element row = table.getElement(rowIdx);
                writer.writeStartTag(row);
                for (int i = 0; i < indexOfCellInRow; i++) {
                    writer.write(row.getElement(i));
                }
                writer.write(row.getElement(indexOfCellInRow + 1));
                writer.write(row.getElement(indexOfCellInRow));
                for (int i = indexOfCellInRow + 2; i < row.getElementCount(); i++) {
                    writer.write(row.getElement(i));
                }
                writer.writeEndTag(row);
            }
            final Element cellToTheRight = tableRow.getElement(indexOfCellInRow + 1);
            final int finalCaretPosition = getCaretPosition() + cellToTheRight.getEndOffset()
                    - cellToTheRight.getStartOffset();
            getSHTMLDocument().setOuterHTML(table, writer.getWrittenString());
            setCaretPosition(finalCaretPosition);
        }
        catch (final Exception ex) {
        }
        finally {
            getSHTMLDocument().endCompoundEdit();
        }
    }

    /** Moves the table row down. Does not treat multirow cells. */
    void moveTableRowDown() {
        final Element tableCell = getCurrentTableCell();
        if (tableCell == null) {
            return;
        }
        final Element tableRow = tableCell.getParentElement();
        final Element table = tableRow.getParentElement();
        final int indexOfRowInTable = table.getElementIndex(getCaretPosition());
        if (indexOfRowInTable == table.getElementCount() - 1) {
            return;
        }
        try {
            getSHTMLDocument().startCompoundEdit();
            final SHTMLWriter writer = new SHTMLWriter(getSHTMLDocument());
            writer.writeStartTag(table);
            for (int i = 0; i < indexOfRowInTable; i++) {
                writer.write(table.getElement(i));
            }
            writer.write(table.getElement(indexOfRowInTable + 1));
            writer.write(tableRow);
            for (int i = indexOfRowInTable + 2; i < table.getElementCount(); i++) {
                writer.write(table.getElement(i));
            }
            writer.writeEndTag(table);
            final Element rowBelow = table.getElement(indexOfRowInTable + 1);
            final int finalCaretPosition = getCaretPosition() + rowBelow.getEndOffset() - rowBelow.getStartOffset();
            getSHTMLDocument().setOuterHTML(table, writer.getWrittenString());
            setCaretPosition(finalCaretPosition);
        }
        catch (final Exception ex) {
        }
        finally {
            getSHTMLDocument().endCompoundEdit();
        }
    }

    /**
     * Removes an element from the document of this editor. Removes it <i>manually</i> if the parent is not body.
     * Leaves it to the caller to set the caret position after the removal.
     */
    private void removeElement(final Element element) {
        try {
            final int start = element.getStartOffset();
            final Element parent = element.getParentElement();
            if (parent.getName().equalsIgnoreCase("body")) {
                getSHTMLDocument().remove(start, element.getEndOffset() - start);
            }
            else {
                // If the parent is not body, remove manually, to avoid quirks, e.g. in tables.
                final int indexInParent = parent.getElementIndex(element.getStartOffset());
                final SHTMLWriter writer = new SHTMLWriter(getSHTMLDocument());
                writer.writeStartTag(parent);
                for (int i = 0; i < indexInParent; i++) {
                    writer.write(parent.getElement(i));
                }
                for (int i = indexInParent + 1; i < parent.getElementCount(); i++) {
                    writer.write(parent.getElement(i));
                }
                writer.writeEndTag(parent);
                getSHTMLDocument().setOuterHTML(parent, writer.getWrittenString());
            }
        }
        catch (final Exception ex) {
            Util.errMsg(null, ex.getMessage(), ex);
        }
    }

    /**
     * apply a set of attributes to the table the caret is
     * currently in (if any)
     *
     * @param a  the set of attributes to apply
     */
    public void applyTableAttributes(final AttributeSet a) {
        final Element cell = getCurrentTableCell();
        if (cell != null) {
            final Element table = cell.getParentElement().getParentElement();
            if (a.getAttributeCount() > 0) {
                //System.out.println("applyTableAttributes count=" + a.getAttributeCount() + " a=" + a);
                ((SHTMLDocument) getDocument()).addAttributes(table, a);
                /**
                 * for some reason above code does not show the changed attributes
                 * of the table, although the element really has them (maybe somebody
                 * could let me know why...). Therefore we update the editor pane
                 * contents comparably rude (any other more elegant alternatives
                 * welcome!)
                 *
                 * --> found out why: the swing package does not render short hand
                 *                    properties such as MARGIN or PADDING. When
                 *                    contained in a document inside an AttributeSet
                 *                    they already have to be split into MARGIN-TOP,
                 *                    MARGIN-LEFT, etc.
                 *                    adjusted AttributeComponents accordingly so
                 *                    we don't need refresh anymore
                 */
                //refresh();
            }
        }
    }

    /**
     * refresh the whole contents of this editor pane with brute force
     */
    private void refresh() {
        final int pos = getCaretPosition();
        final String data = getText();
        setText("");
        setText(data);
        setCaretPosition(pos);
    }

    /**
     * apply a set of attributes to a given range of cells
     * of the table the caret is currently in (if any)
     *
     * @param a  the set of attributes to apply
     * @param range  the range of cells to apply attributes to
     */
    public void applyCellAttributes(final AttributeSet a, final int range) {
        //System.out.println("SHTMLEditorPane applyCellAttributes a=" + a);
        final Element cell = getCurrentTableCell();
        int cIndex = 0;
        int rIndex = 0;
        final SHTMLDocument doc = (SHTMLDocument) getDocument();
        if (cell != null) {
            Element row = cell.getParentElement();
            final Element table = row.getParentElement();
            Element aCell;
            switch (range) {
                case THIS_CELL:
                    doc.addAttributes(cell, a);
                    break;
                case THIS_ROW:
                    for (cIndex = 0; cIndex < row.getElementCount(); cIndex++) {
                        aCell = row.getElement(cIndex);
                        doc.addAttributes(aCell, a);
                    }
                    break;
                case THIS_COLUMN:
                    cIndex = Util.getElementIndex(cell); //getColNumber(cell);
                    for (rIndex = 0; rIndex < table.getElementCount(); rIndex++) {
                        aCell = table.getElement(rIndex).getElement(cIndex);
                        doc.addAttributes(aCell, a);
                    }
                    break;
                case ALL_CELLS:
                    while (rIndex < table.getElementCount()) {
                        row = table.getElement(rIndex);
                        cIndex = 0;
                        while (cIndex < row.getElementCount()) {
                            aCell = row.getElement(cIndex);
                            //System.out.println("applyCellAttributes ALL_CELLS adjusted a=" + adjustCellBorders(aCell, a));
                            doc.addAttributes(aCell, a);
                            cIndex++;
                        }
                        rIndex++;
                    }
                    break;
            }
        }
    }

    public SHTMLDocument getSHTMLDocument() {
        return (SHTMLDocument) getDocument();
    }

    /**
     * Gets the number of the table column in which the given cell is located.
     *
     * @param cell  the cell to get the column number for
     * @return the column number of the given cell
     */
    private int getColNumber(final Element cell) {
        int i = 0;
        final Element thisRow = cell.getParentElement();
        final int last = thisRow.getElementCount() - 1;
        Element aCell = thisRow.getElement(i);
        if (aCell != cell) {
            while ((i < last) && (aCell != cell)) {
                aCell = thisRow.getElement(++i);
            }
        }
        return i;
    }

    /* ------- table manipulation end -------------------- */
    /* ------- table cell navigation start --------------- */
    /**
     * <code>Action</code> to move the caret from the current table cell
     * to the next table cell.
     */
    private class TabAction extends AbstractAction {
        /** action to use when not inside a table */
        /* removed for changes in J2SE 1.4.1
        private Action alternateAction;
         */
        /** construct a <code>NextTableCellAction</code> */
        public TabAction(final String actionName) {
            super(actionName);
        }

        /**
         * construct a <code>NextTableCellAction</code>
         *
         * @param altAction  the action to use when the caret
         *      is not inside a table
         */
        /* removed for changes in J2SE 1.4.1
        public NextTableCellAction(Action altAction) {
          alternateAction = altAction;
        }
         */
        /**
         * move to the previous cell or invoke an alternate action if the
         * caret is not inside a table
         *
         * this will append a new table row when the caret
         * is inside the last table cell
         */
        public void actionPerformed(final ActionEvent ae) {
            if (listManager.caretAtTheBeginningOfListItem()) {
                // Increase indent within list
                listManager.increaseIndent(true);
                return;
            }
            final Element cell = getCurrentTableCell();
            if (cell != null) {
                // Within a table cell.
                goNextCell(cell);
                return;
            }
            if (listManager.caretWithinListItem()) {
                // Increase indent within list
                listManager.increaseIndent(true);
                return;
            }
            // Do nothing; above all, do not enter tab character.
            //performDefaultKeyStrokeAction(KeyEvent.VK_TAB, 0, ae);
            if (Util.preferenceIsTrue("table.insertNewOnTabKey", "false")) {
                insertNewTable(3);
            }
        }
    }

    /**
     * <code>Action</code> to move the caret from the current table cell
     * to the previous table cell.
     */
    private class ShiftTabAction extends AbstractAction {
        /** action to use when not inside a table */
        /* removed for changes in J2SE 1.4.1
        private Action alternateAction;
        */
        /** construct a <code>PrevTableCellAction</code> */
        public ShiftTabAction(final String actionName) {
            super(actionName);
        }

        /**
         * construct a <code>PrevTableCellAction</code>
         *
         * @param altAction  the action to use when the caret
         *      is not inside a table
         */
        /* removed for changes in J2SE 1.4.1
        public PrevTableCellAction(Action altAction) {
          alternateAction = altAction;
        }
        */
        /**
         * Moves to the previous cell or invokes an alternate action if the
         * caret is not inside a table.
         */
        public void actionPerformed(final ActionEvent ae) {
            // Decrease intent within list
            if (listManager.caretAtTheBeginningOfListItem()) {
                listManager.decreaseIndent(true);
                return;
            }
            final Element cell = getCurrentTableCell();
            if (cell != null) {
                goPrevCell(cell);
                return;
            }
            // Decrease intent within list
            if (listManager.caretWithinListItem()) {
                listManager.decreaseIndent(true);
                return;
            }
            performDefaultKeyStrokeAction(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK, ae);
        }
    }

    /**
     * <code>Action</code> to create a new list item.
     */
    private class InsertLineBreakAction extends AbstractAction {
        /** construct a <code>NewListItemAction</code> */
        public InsertLineBreakAction() {
        }

        /**
         * create a new list item, when the caret is inside a list
         *
         * <p>The new item is created after the item at the caret position</p>
         */
        public void actionPerformed(final ActionEvent ae) {
            try {
                final SHTMLDocument doc = (SHTMLDocument) getDocument();
                final int caretPosition = getCaretPosition();
                final Element paragraphElement = doc.getParagraphElement(caretPosition);
                if (paragraphElement != null) {
                    final int so = paragraphElement.getStartOffset();
                    final int eo = paragraphElement.getEndOffset();
                    if (so != eo) {
                        final StringWriter writer = new StringWriter();
                        if (caretPosition > so) {
                            final SHTMLWriter htmlStartWriter = new SHTMLWriter(writer, doc, so, caretPosition - so);
                            htmlStartWriter.writeChildElements(paragraphElement);
                        }
                        // Workaround: <br> is written twice by Java.
                        if (!doc.getCharacterElement(caretPosition).getName().equalsIgnoreCase(HTML.Tag.BR.toString())) {
                            writer.write("<br>");
                        }
                        if (caretPosition < eo - 1) {
                            final SHTMLWriter htmlEndWriter = new SHTMLWriter(writer, doc, caretPosition, eo
                                    - caretPosition);
                            htmlEndWriter.writeChildElements(paragraphElement);
                        }
                        final String text = writer.toString();
                        try {
                            doc.startCompoundEdit();
                            doc.setInnerHTML(paragraphElement, text);
                        }
                        catch (final Exception e) {
                            Util.errMsg(null, e.getMessage(), e);
                        }
                        finally {
                            doc.endCompoundEdit();
                        }
                        setCaretPosition(caretPosition + 1);
                    }
                }
            }
            catch (final Exception e) {
                Util.errMsg(null, e.getMessage(), e);
            }
        }
    }

    public void goNextCell(final Element cell) {
        if (cell == getLastTableCell(cell)) {
            appendTableRow();
            setCaretPosition(getNextCell(cell).getStartOffset());
        }
        else {
            setCaretPosition(getNextCell(cell).getStartOffset());
        }
    }

    public void goPrevCell(Element cell) {
        int newPos;
        if (cell != getFirstTableCell(cell)) {
            cell = getPrevCell(cell);
            newPos = cell.getStartOffset();
            select(newPos, newPos);
        }
    }

    /**
     * Gets the table cell following the given table cell, continuing
     * on the next row if the cell is the last one in the row, returning
     * null if the cell is the last one in the table.
     *
     * @param cell  the cell whose following cell shall be found
     * @return the Element having the cell following the given cell or null
     *    if the given cell is the last cell in the table
     */
    private Element getNextCell(final Element cell) {
        Element nextCell = null;
        final Element row = cell.getParentElement();
        Element nextRow = null;
        final Element table = row.getParentElement();
        final int lastCellIdx = row.getElementCount() - 1;
        final Element lastCellInRow = row.getElement(lastCellIdx);
        if (lastCellInRow != cell) {
            // The cell is not the last one in the row.
            Element runningCell = lastCellInRow;
            int cellIdx = lastCellIdx;
            while ((cellIdx > 0) && (runningCell != cell)) {
                nextCell = runningCell;
                runningCell = row.getElement(--cellIdx);
            }
        }
        else {
            // The cell is the last one in the row.
            int rowIdx = table.getElementCount() - 1;
            Element aRow = table.getElement(rowIdx);
            while (aRow != row) {
                nextRow = aRow;
                aRow = table.getElement(--rowIdx);
            }
            nextCell = nextRow == null || nextRow.getElementCount() == 0 ? null : nextRow.getElement(0);
        }
        return nextCell;
    }

    /**
     * get the table cell preceding a given table cell
     *
     * @param cell  the cell whose preceding cell shall be found
     * @return the Element having the cell preceding the given cell or null
     *    if the given cell is the first cell in the table
     */
    private Element getPrevCell(final Element cell) {
        final Element thisRow = cell.getParentElement();
        final Element table = thisRow.getParentElement();
        Element prevCell = null;
        int i = 0;
        Element aCell = thisRow.getElement(i);
        if (aCell != cell) {
            while (aCell != cell) {
                prevCell = aCell;
                aCell = thisRow.getElement(i++);
            }
        }
        else {
            Element prevRow = null;
            Element aRow = table.getElement(i);
            while (aRow != thisRow) {
                prevRow = aRow;
                aRow = table.getElement(i++);
            }
            prevCell = prevRow.getElement(prevRow.getElementCount() - 1);
        }
        return prevCell;
    }

    /**
     * get the last cell of the table a given table cell belongs to
     *
     * @param cell  a cell of the table to get the last cell of
     * @return the Element having the last table cell
     */
    private Element getLastTableCell(final Element cell) {
        final Element table = cell.getParentElement().getParentElement();
        final Element lastRow = table.getElement(table.getElementCount() - 1);
        final Element lastCell = lastRow.getElement(lastRow.getElementCount() - 1);
        return lastCell;
    }

    /**
     * get the first cell of the table a given table cell belongs to
     *
     * @param cell  a cell of the table to get the first cell of
     * @return the Element having the first table cell
     */
    private Element getFirstTableCell(final Element cell) {
        final Element table = cell.getParentElement().getParentElement();
        final Element firstCell = table.getElement(0).getElement(0);
        return firstCell;
    }

    /**
     * Gets the table cell at the current caret position.
     *
     * @return the Element having the current table cell or null if
     *    the caret is not inside a table cell
     */
    public Element getCurrentTableCell() {
        return getTableCell(getCaretPosition());
    }

    /**
     * 
     */
    public Element getCurrentLinkElement() {
        Element element2 = null;
        Element element = getSHTMLDocument().getCharacterElement(getSelectionStart());
        Object linkAttribute = null; //elem.getAttributes().getAttribute(HTML.Tag.A);
        Object href = null;
        while (element != null && linkAttribute == null) {
            element2 = element;
            linkAttribute = element.getAttributes().getAttribute(HTML.Tag.A);
            if (linkAttribute != null) {
                href = ((AttributeSet) linkAttribute).getAttribute(HTML.Attribute.HREF);
            }
            element = element.getParentElement();
        }
        if (linkAttribute != null && href != null) {
            return element2;
        }
        else {
            return null;
        }
    }

    /**
     * Gets the table cell element at the given position, or null if none.
     */
    public Element getTableCell(final int position) {
        final Element element = getSHTMLDocument().getCharacterElement(position);
        return Util.findElementUp("td", "th", element);
    }

    /** Gets the string URL of an existing link, or null if none. */
    String getURLOfExistingLink() {
        //setIgnoreActions(true);      
        final Element linkElement = getCurrentLinkElement();
        final boolean foundLink = (linkElement != null);
        if (!foundLink) {
            return null;
        }
        final AttributeSet elemAttrs = linkElement.getAttributes();
        final Object linkAttr = elemAttrs.getAttribute(HTML.Tag.A);
        final Object href = ((AttributeSet) linkAttr).getAttribute(HTML.Attribute.HREF);
        return href != null ? href.toString() : null;
    }

    /** Gets the paragraph element in which the caret is located. */
    public Element getCurrentParagraphElement() {
        return getSHTMLDocument().getParagraphElement(getCaretPosition());
    }
    
    

    @Override
    public void replaceSelection(String content) {
    	if(content != null){
    		final String expandedContent = content.replaceAll(TAB, TAB_REPLACEMENT);
    		super.replaceSelection(expandedContent);
    	}
    	else
    		super.replaceSelection(content);
    }

	/* ---------- table cell navigation end --------------*/
    /**
     * Replaces the currently selected content with new content
     * represented by the given <code>HTMLText</code>. If there is no selection
     * this amounts to an insert of the given text.  If there
     * is no replacement text this amounts to a removal of the
     * current selection.
     * This method overrides replaceSelection in <code>JEditorPane</code> for usage
     * of our own HTMLText object.
     *
     * @param replacementHTMLText  the content to replace the selection with
     */
    public void replaceSelection(final HTMLText replacementHTMLText) {
        final SHTMLDocument document = (SHTMLDocument) getDocument();
        final Caret caret = getCaret();
        if (document != null) {
            try {
                final int p0 = Math.min(caret.getDot(), caret.getMark());
                final int p1 = Math.max(caret.getDot(), caret.getMark());
                if (p0 != p1) {
                    document.remove(p0, p1 - p0);
                }
                if (replacementHTMLText != null) {
                    pasteHTML(replacementHTMLText, p0);
                }
            }
            catch (final Exception e) {
                getToolkit().beep();
            }
        }
    }

    /** */
    private void pasteHTML(final HTMLText pastedHTMLText, final int position) throws Exception {
        final SHTMLDocument sDocument = (SHTMLDocument) getDocument();
        if (!pastedHTMLText.usesStringRepresenation()) {
            pastedHTMLText.pasteHTML(sDocument, position);
            return;
        }
        // [ Uses string representation ]
        String pasteHtmlTextModified = pastedHTMLText.getHTMLText();
        final Element characterElement = sDocument.getCharacterElement(position);
        final Element paragraphElement = characterElement.getParentElement();
        if (position == paragraphElement.getStartOffset()) {
            if (caretWithinTableCell() && pastedHTMLText.isOneCellInOneRow()) {
                pasteHtmlTextModified = pasteHtmlTextModified.replaceAll("(?ims).*<td.*?>", "").replaceAll(
                    "(?ims)</td.*?>.*", "");
            }
            // We are at the start of the paragraph to insert at.
            if (!HTMLText.containsParagraphTags(pasteHtmlTextModified)) {
                sDocument.insertAfterStart(paragraphElement, pasteHtmlTextModified);
                // Remove whitespace before the end tag of paragraph element to avoid quircky behavior.       
                final Element newParagraph = getCurrentParagraphElement();
                final String fixedContent = elementToHTML(newParagraph).replaceAll("(?ims)\\s*</p>", "</p>");
                sDocument.setOuterHTML(newParagraph, fixedContent);
                //
                setCaretPosition(newParagraph.getEndOffset() - 1);
                return;
            }
            // Contains paragraph tags.
            if (caretWithinTableCell() && pasteHtmlTextModified.matches("(?ims).*<table.*")) {
                // The condition above is simplistic.
                final String strippedHTMLText = pasteHtmlTextModified.replaceAll("(?ims).*<table.*?>", "").replaceAll(
                    "(?ims)</table.*?>.*", "");
                final Element cellElement = getCurrentTableCell();
                final Element tableRowElement = cellElement.getParentElement();
                sDocument.insertBeforeStart(tableRowElement, strippedHTMLText);
                return;
            }
            // Contains paragraphs tags and
            // (a) is not within a table cell or
            // (b) the pasted content is not a table.
            sDocument.insertBeforeStart(paragraphElement, pasteHtmlTextModified);
            if (caretWithinTableCell()) {
                final Element cellElement = getCurrentTableCell();
                final Element lastElementInCell = cellElement.getElement(cellElement.getElementCount() - 1);
                if (elementIsEmptyParagraph(lastElementInCell)) {
                    // Remove empty paragraph at the end of the cell. A workaround.
                    removeElement(lastElementInCell);
                    setCaretPosition(cellElement.getEndOffset() - 1);
                }
            }
            return;
        }
        if (paragraphElement.getEndOffset() == position + 1) {
            // We are at the end of the paragraph to insert at,
            if (HTMLText.containsParagraphTags(pasteHtmlTextModified)) {
                sDocument.insertAfterEnd(paragraphElement, pasteHtmlTextModified);
            }
            else {
                sDocument.insertBeforeEnd(paragraphElement, pasteHtmlTextModified);
            }
            return;
        }
        // We are somewhere else inside the paragraph to insert at.
        final String newHtml = pastedHTMLText.splitPaste(sDocument, characterElement, paragraphElement, position,
            pasteHtmlTextModified, HTMLText.containsParagraphTags(pasteHtmlTextModified));
        final int paragraphOldEndOffset = paragraphElement.getEndOffset();
        final int oldCaretPosition = getCaretPosition();
        sDocument.setOuterHTML(paragraphElement, newHtml);
        final Element newParagraphElement = getSHTMLDocument().getParagraphElement(oldCaretPosition);
        // Place the caret after the pasted text
        setCaretPosition(oldCaretPosition + newParagraphElement.getEndOffset() - paragraphOldEndOffset);
    }

    /* ------ start of drag and drop implementation -------------------------
                (see also constructor of this class) */
    /** enables this component to be a Drop Target */
    DropTarget dropTarget = null;
    /** enables this component to be a Drag Source */
    DragSource dragSource = null;
    /** the last selection start */
    private int lastSelStart = 0;
    /** the last selection end */
    private int lastSelEnd = 0;
    /** the location of the last event in the text component */
    private int dndEventLocation = 0;
    /**
     * <p>This flag is set by this objects dragGestureRecognizer to indicate that
     * a drag operation has been started from this object. It is cleared once
     * dragDropEnd is captured by this object.</p>
     *
     * <p>If a drop occurs in this object and this object started the drag
     * operation, then the element to be dropped comes from this object and thus
     * has to be removed somewhere else in this object.</p>
     *
     * <p>To the contrary if a drop occurs in this object and the drag operation
     * was not started in this object, then the element to be dropped does not
     * come from this object and has not to be removed here.</p>
     */
    private boolean dragStartedHere = false;

    /**
     * Initialize the drag and drop implementation for this component.
     *
     * <p>DropTarget, DragSource and DragGestureRecognizer are instantiated
     * and a MouseListener is established to track the selection in drag
     * operations</p>
     *
     * <p>this ideally is called in the constructor of a class which
     * would like to implement drag and drop</p>
     */
    public void initDnd() {
        dropTarget = new DropTarget(this, this);
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
        this.addMouseListener(new MouseAdapter() {
            public void mouseReleased(final MouseEvent e) {
                this_mouseReleased(e);
            }

            public void mouseClicked(final MouseEvent e) {
                this_mouseClicked(e);
            }
        });
    }

    /** a drag gesture has been initiated */
    public void dragGestureRecognized(final DragGestureEvent event) {
        final int selStart = getSelectionStart();
        try {
            if ((lastSelEnd > lastSelStart) && (selStart >= lastSelStart) && (selStart < lastSelEnd)) {
                dragStartedHere = true;
                select(lastSelStart, lastSelEnd);
                final HTMLText text = new HTMLText();
                final int start = getSelectionStart();
                text.copyHTML(this, start, getSelectionEnd() - start);
                final HTMLTextSelection trans = new HTMLTextSelection(text);
                dragSource.startDrag(event, DragSource.DefaultMoveDrop, trans, this);
            }
        }
        catch (final Exception e) {
            //getToolkit().beep();
        }
    }

    /**
     * this message goes to DragSourceListener, informing it that the dragging
     * has ended
     */
    public void dragDropEnd(final DragSourceDropEvent event) {
        dragStartedHere = false;
    }

    /** is invoked when a drag operation is going on */
    public void dragOver(final DropTargetDragEvent event) {
        dndEventLocation = viewToModel(event.getLocation());
        try {
            setCaretPosition(dndEventLocation);
        }
        catch (final Exception e) {
            //getToolkit().beep();
        }
    }

    /**
     * a drop has occurred. If the dragged element has a suitable
     * <code>DataFlavor</code>, do the drop.
     *
     * @param event - the event specifiying the drop operation
     * @see java.awt.datatransfer.DataFlavor
     */
    public void drop(final DropTargetDropEvent event) {
        dndEventLocation = viewToModel(event.getLocation());
        if ((dndEventLocation >= lastSelStart) && (dndEventLocation <= lastSelEnd)) {
            event.rejectDrop();
            select(lastSelStart, lastSelEnd);
        }
        else {
            final SHTMLDocument doc = (SHTMLDocument) getDocument();
            doc.startCompoundEdit();
            try {
                final Transferable transferable = event.getTransferable();
                /*
                if (getPasteMode() == PasteMode.PASTE_PLAIN_TEXT)
                {
                    event.acceptDrop(DnDConstants.ACTION_MOVE);
                    final String content = transferable.getTransferData(DataFlavor.stringFlavor).toString();
                    doDrop(event, content);
                }
                else
                */
                 if (transferable.isDataFlavorSupported(htmlTextDataFlavor)) {
                    event.acceptDrop(DnDConstants.ACTION_MOVE);
                    final HTMLText s = (HTMLText) transferable.getTransferData(htmlTextDataFlavor);
                    doDrop(event, s);
                }
                else if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    event.acceptDrop(DnDConstants.ACTION_MOVE);
                    final String s = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                    doDrop(event, s);
                }
                else {
                    event.rejectDrop();
                }
            }
            catch (final Exception exception) {
                exception.printStackTrace();
                event.rejectDrop();
            }
            finally {
                doc.endCompoundEdit();
            }
        }
    }

    /**
     * do the drop operation consisting of adding the dragged element and
     * necessarily removing the dragged element from the original position
     */
    private void doDrop(final DropTargetDropEvent event, final Object s) {
        int removeOffset = 0;
        int moveOffset = 0;
        int newSelStart;
        int newSelEnd;
        setCaretPosition(dndEventLocation);
        if (s instanceof HTMLText) {
            replaceSelection((HTMLText) s);
        }
        else if (s instanceof String) {
            replaceSelection((String) s);
        }
        if (dndEventLocation < lastSelStart) {
            removeOffset = s.toString().length();
        }
        else {
            moveOffset = s.toString().length();
        }
        newSelEnd = dndEventLocation + (lastSelEnd - lastSelStart) - moveOffset;
        newSelStart = dndEventLocation - moveOffset;
        if (dragStartedHere) {
            lastSelStart += removeOffset;
            lastSelEnd += removeOffset;
            select(lastSelStart, lastSelEnd);
            replaceSelection("");
        }
        lastSelEnd = newSelEnd;
        lastSelStart = newSelStart;
        select(lastSelStart, lastSelEnd);
        event.getDropTargetContext().dropComplete(true);
    }

    /** A groupping of list operations, mostly related to toggling of lists. */
    private class ListManager {
        //private Element parentElement;
        class SwitchListException extends Exception {
        }

        private ListManager() {
        }

        private Element getParagraphElement(final int pos) {
            Element paragraphElement = getSHTMLDocument().getParagraphElement(pos);
            if (paragraphElement.getName().equalsIgnoreCase("p-implied")) {
                paragraphElement = paragraphElement.getParentElement();
            }
            return paragraphElement;
        }

        /** */
        private Element getListParent(final Element elem) {
            Element listParent = elem.getParentElement();
            if (elem.getName().equalsIgnoreCase(HTML.Tag.LI.toString())) {
                listParent = listParent.getParentElement();
            }
            return listParent;
        }

        /** Gets the list item element at the caret position, or null if the position
         * is not within a list. */
        private Element getListItemElement(final int caretPosition) {
            final Element paragraphElement = getParagraphElement(caretPosition);
            return Util.findElementUp(HTML.Tag.LI.toString(), paragraphElement);
        }

        /** Gets the innermost list element at the caret position, or null if the position is not
         * within a list.
         */
        private Element getListElement(final int caretPosition) {
            final Element paragraphElement = getParagraphElement(caretPosition);
            final Element listElement = Util.findElementUp("ul", "ol", paragraphElement);
            return listElement;
        }

        private boolean isValidParentElement(final Element e) {
            final String name = e.getName();
            return name.equalsIgnoreCase(HTML.Tag.BODY.toString()) || name.equalsIgnoreCase(HTML.Tag.TD.toString())
                    || name.equalsIgnoreCase(HTML.Tag.LI.toString());
        }

        private boolean isValidElement(final Element e) {
            final String name = e.getName();
            return name.equalsIgnoreCase(HTML.Tag.P.toString()) || name.equalsIgnoreCase(HTML.Tag.UL.toString())
                    || name.equalsIgnoreCase(HTML.Tag.OL.toString()) || name.equalsIgnoreCase(HTML.Tag.H1.toString())
                    || name.equalsIgnoreCase(HTML.Tag.H2.toString()) || name.equalsIgnoreCase(HTML.Tag.H3.toString())
                    || name.equalsIgnoreCase(HTML.Tag.H4.toString()) || name.equalsIgnoreCase(HTML.Tag.H5.toString())
                    || name.equalsIgnoreCase(HTML.Tag.H6.toString());
        }

        private boolean isListRootElement(final Element e) {
            final String name = e.getName();
            return name.equalsIgnoreCase(HTML.Tag.UL.toString()) || name.equalsIgnoreCase(HTML.Tag.OL.toString());
        }

        /**
         * Switches OFF list formatting for a given block of elements.
         *
         * <p>Switches off all list formatting inside the block for the
         * given tag.</p>
         *
         * <p>Splits lists if the selection covers only part of a list.</p>
         * @throws BadLocationException 
         * @throws IOException 
         */
        private void listOff() throws IOException, BadLocationException {
            final int selectionStart = getSelectionStart();
            final int selectionEnd = getSelectionEnd();
            final Element firstParagraphElement = getParagraphElement(selectionStart);
            final int fistParagraphElementStart = firstParagraphElement.getStartOffset();
            final int lastParagraphElementEnd = getParagraphElement(selectionEnd).getEndOffset();
            final Element parentOfTheListElement = getListParent(firstParagraphElement);
            if (isListElement(parentOfTheListElement)) {
                // Currently, no support for nested lists.
                return;
            }
            final SHTMLWriter sHTMLwriter = new SHTMLWriter(getSHTMLDocument());
            int elementIdx;
            Element nextElement = null;
            for (elementIdx = 0; elementIdx < parentOfTheListElement.getElementCount(); elementIdx++) {
                nextElement = parentOfTheListElement.getElement(elementIdx);
                if (nextElement.getEndOffset() > fistParagraphElementStart) {
                    break;
                }
            }
            final Element startElementToBeReplaced = nextElement;
            int nrElementsToBeReplaced = 1;
            int listItemIdx = 0;
            Element listItemElement = null;
            if (nextElement.getStartOffset() < fistParagraphElementStart) {
                sHTMLwriter.writeStartTag(nextElement);
                elementIdx++;
                // Write list item elements before the selection. 
                for (;; listItemIdx++) {
                    listItemElement = nextElement.getElement(listItemIdx);
                    if (listItemElement.getStartOffset() == fistParagraphElementStart) {
                        break;
                    }
                    sHTMLwriter.write(listItemElement);
                }
                sHTMLwriter.writeEndTag(nextElement);
                // Turn the list item elements in the selection into paragraph element.
                for (; listItemIdx < nextElement.getElementCount(); listItemIdx++) {
                    listItemElement = nextElement.getElement(listItemIdx);
                    if (listItemElement.getStartOffset() >= lastParagraphElementEnd) {
                        break;
                    }
                    sHTMLwriter.writeStartTag("p", null);
                    sHTMLwriter.writeChildElements(listItemElement);
                    sHTMLwriter.writeEndTag("p");
                }
            }
            if (nextElement.getEndOffset() <= lastParagraphElementEnd) {
                for (; elementIdx < parentOfTheListElement.getElementCount(); elementIdx++) {
                    nextElement = parentOfTheListElement.getElement(elementIdx);
                    if (nextElement.getEndOffset() > lastParagraphElementEnd) {
                        break;
                    }
                    if (nextElement != startElementToBeReplaced
                            && nextElement.getStartOffset() < lastParagraphElementEnd) {
                        nrElementsToBeReplaced++;
                    }
                    if (isListRootElement(nextElement)) {
                        for (listItemIdx = 0; listItemIdx < nextElement.getElementCount(); listItemIdx++) {
                            sHTMLwriter.writeStartTag("p", null);
                            sHTMLwriter.writeChildElements(nextElement.getElement(listItemIdx));
                            sHTMLwriter.writeEndTag("p");
                        }
                    }
                    else {
                        sHTMLwriter.writeStartTag("p", null);
                        sHTMLwriter.writeChildElements(nextElement);
                        sHTMLwriter.writeEndTag("p");
                    }
                }
            }
            // nextElement is final at this point.
            if (elementIdx <= parentOfTheListElement.getElementCount()
                    && nextElement.getStartOffset() < lastParagraphElementEnd) {
                if (nextElement != startElementToBeReplaced) {
                    nrElementsToBeReplaced++;
                }
                for (; listItemIdx < nextElement.getElementCount(); listItemIdx++) {
                    listItemElement = nextElement.getElement(listItemIdx);
                    if (listItemElement.getStartOffset() >= lastParagraphElementEnd) {
                        break;
                    }
                    sHTMLwriter.writeStartTag("p", null);
                    sHTMLwriter.writeChildElements(listItemElement);
                    sHTMLwriter.writeEndTag("p");
                }
                if (listItemIdx < nextElement.getElementCount()) {
                    sHTMLwriter.writeStartTag(nextElement);
                    for (; listItemIdx < nextElement.getElementCount(); listItemIdx++) {
                        listItemElement = nextElement.getElement(listItemIdx);
                        sHTMLwriter.write(listItemElement);
                    }
                    sHTMLwriter.writeEndTag(nextElement);
                }
            }
            getSHTMLDocument().replaceHTML(startElementToBeReplaced, nrElementsToBeReplaced,
                sHTMLwriter.getWrittenString());
        }

        /**
         * switch ON list formatting for a given block of elements.
         *
         * <p>Takes care of merging existing lists before, after and inside
         * respective element block.</p>
         * @throws BadLocationException 
         * @throws IOException 
         *
         */
        private void listOn(final String listTag, final AttributeSet attributeSet, final boolean forceOff)
                throws IOException, BadLocationException {
            final int selectionStart = getSelectionStart();
            final int selectionEnd = getSelectionEnd();
            final Element firstParagraphElement = getParagraphElement(selectionStart);
            int fistParagraphElementStart = firstParagraphElement.getStartOffset();
            int lastParagraphElementEnd = getParagraphElement(selectionEnd).getEndOffset();
            final Element parentElement = getListParent(firstParagraphElement);
            Element startElementToBeRemoved = null;
            int removeCount = 0;
            final SHTMLWriter writer = new SHTMLWriter(getSHTMLDocument());
            if (fistParagraphElementStart > 0) {
                final Element before = getParagraphElement(fistParagraphElementStart - 1);
                if (before.getName().equalsIgnoreCase(HTML.Tag.LI.toString())) {
                    final Element listRoot = before.getParentElement();
                    if (listRoot.getParentElement() == parentElement && listRoot.getName().equalsIgnoreCase(listTag)) {
                        fistParagraphElementStart = listRoot.getStartOffset();
                    }
                }
            }
            if (lastParagraphElementEnd < getSHTMLDocument().getLength() - 1) {
                final Element after = getParagraphElement(lastParagraphElementEnd);
                if (after.getName().equalsIgnoreCase(HTML.Tag.LI.toString())) {
                    final Element listRoot = after.getParentElement();
                    if (listRoot.getParentElement() == parentElement && listRoot.getName().equalsIgnoreCase(listTag)) {
                        lastParagraphElementEnd = listRoot.getEndOffset();
                    }
                }
            }
            int i;
            Element next = null;
            for (i = 0; i < parentElement.getElementCount(); i++) {
                next = parentElement.getElement(i);
                if (next.getEndOffset() > fistParagraphElementStart) {
                    break;
                }
            }
            startElementToBeRemoved = next;
            removeCount = 1;
            int j = 0;
            Element li = null;
            if (next.getStartOffset() < fistParagraphElementStart) {
                i++;
                writer.writeStartTag(next);
                for (;; j++) {
                    li = next.getElement(j);
                    if (li.getStartOffset() == fistParagraphElementStart) {
                        break;
                    }
                    writer.write(li);
                }
                writer.writeEndTag(next);
                writer.writeStartTag(listTag, attributeSet);
                for (; j < next.getElementCount(); j++) {
                    li = next.getElement(j);
                    if (li.getStartOffset() >= lastParagraphElementEnd) {
                        break;
                    }
                    writer.write(li);
                }
            }
            else {
                writer.writeStartTag(listTag, attributeSet);
            }
            if (next.getEndOffset() <= lastParagraphElementEnd) {
                for (; i < parentElement.getElementCount(); i++) {
                    next = parentElement.getElement(i);
                    if (next.getEndOffset() > lastParagraphElementEnd) {
                        break;
                    }
                    if (startElementToBeRemoved != next && next.getStartOffset() < lastParagraphElementEnd) {
                        removeCount++;
                    }
                    if (isListRootElement(next)) {
                        writer.writeChildElements(next);
                    }
                    else {
                        writer.writeStartTag("li", null);
                        writer.writeChildElements(next);
                        writer.writeEndTag("li");
                    }
                }
            }
            if (i < parentElement.getElementCount() && next.getStartOffset() < lastParagraphElementEnd) {
                if (startElementToBeRemoved != next) {
                    removeCount++;
                }
                for (; j < next.getElementCount(); j++) {
                    li = next.getElement(j);
                    if (li.getStartOffset() >= lastParagraphElementEnd) {
                        break;
                    }
                    writer.write(li);
                }
                writer.writeEndTag(listTag);
                if (j < next.getElementCount()) {
                    writer.writeStartTag(next);
                    for (; j < next.getElementCount(); j++) {
                        li = next.getElement(j);
                        writer.write(li);
                    }
                    writer.writeEndTag(next);
                }
            }
            else {
                writer.writeEndTag(listTag);
            }
            getSHTMLDocument().replaceHTML(startElementToBeRemoved, removeCount, writer.getWrittenString());
        }

        /**
         * decide to switch on or off list formatting
         * @return  true, if list formatting is to be switched on, false if not
         * @throws SwitchListException 
         */
        private boolean switchOn(final String listTag, final AttributeSet attributeSet, final boolean forceOff,
                                 final int start, final int end, final Element parentElement)
                throws SwitchListException {
            boolean listOn = false;
            final int count = parentElement.getElementCount();
            for (int i = 0; i < count && !listOn; i++) {
                final Element elem = parentElement.getElement(i);
                if (elem.getStartOffset() >= start && elem.getEndOffset() <= end && !isValidElement(elem)) {
                    throw new SwitchListException();
                }
                final int eStart = elem.getStartOffset();
                final int eEnd = elem.getEndOffset();
                if (!elem.getName().equalsIgnoreCase(listTag)) {
                    if (((eStart > start) && (eStart < end)) || ((eEnd > start) && (eEnd < end))
                            || ((start >= eStart) && (end <= eEnd))) {
                        listOn = true;
                    }
                }
            }
            return listOn;
        }

        /** */
        private void toggleList(final String listTag, final AttributeSet attributeSet, final boolean forceOff) {
            try {
                final int selectionStart = getSelectionStart();
                final int selectionEnd = getSelectionEnd();
                final Element firstParagraphElement = getParagraphElement(selectionStart);
                final int fistParagraphElementStart = firstParagraphElement.getStartOffset();
                final int lastParagraphElementEnd = getParagraphElement(selectionEnd).getEndOffset();
                final Element parentElement = getListParent(firstParagraphElement);
                getSHTMLDocument().startCompoundEdit();
                // Why is OL not a valid parent element? How could a parent element be invalid? --Dan
                //if (!isValidParentElement(parentElement))
                //  throw new SwitchListException();
                if (selectionStart != selectionEnd) {
                    final Element last = getParagraphElement(lastParagraphElementEnd - 1);
                    if (parentElement != getListParent(last)) {
                        throw new SwitchListException();
                    }
                }
                //
                if (!switchOn(listTag, attributeSet, forceOff, fistParagraphElementStart, lastParagraphElementEnd,
                    parentElement) || forceOff) {
                    listOff();
                }
                else {
                    listOn(listTag, attributeSet, forceOff);
                }
                if (selectionStart == selectionEnd) {
                    setCaretPosition(selectionStart);
                }
                else {
                    select(selectionStart, selectionEnd);
                }
                requestFocus();
            }
            catch (final SwitchListException e) {
            }
            catch (final Exception e) {
                Util.errMsg(null, e.getMessage(), e);
            }
            finally {
                getSHTMLDocument().endCompoundEdit();
            }
        }

        /** */
        private boolean isListElement(final Element element) {
            return "ul".equalsIgnoreCase(element.getName()) || "ol".equalsIgnoreCase(element.getName());
        }

        /** */
        private boolean isListItemElement(final Element element) {
            return "li".equalsIgnoreCase(element.getName());
        }

        /** Determines whether the caret is currently at the beginning of a list item. */
        private boolean caretAtTheBeginningOfListItem() {
            final Element parent = getCurrentParagraphElement().getParentElement();
            return ("li".equalsIgnoreCase(parent.getName()) && getCaretPosition() == parent.getStartOffset());
        }

        /** Determines whether the caret is currently at the beginning of a list item. */
        private boolean caretWithinListItem() {
            final Element currentParagraphElement = getCurrentParagraphElement();
            if(currentParagraphElement == null)
            	return false;
			final Element parent = currentParagraphElement.getParentElement();
            return parent != null && "li".equalsIgnoreCase(parent.getName());
        }

        /** Increases the intent of selected list items. ("Including subitems" should default to "true".) */
        private void increaseIndent(final boolean includingSubitems) {
            final Element paragraphElement = getCurrentParagraphElement();
            final Element listItemElement = paragraphElement.getParentElement();
            if (!isListItemElement(listItemElement)) {
                return;
            }
            // Increase indent
            int selectionStart = getSelectionStart();
            int selectionEnd = getSelectionEnd();
            if (selectionStart != selectionEnd && getListElement(selectionStart) == getListElement(selectionEnd)) {
                // Of block
            }
            else {
                // Of single list item
                selectionStart = getCaretPosition();
                selectionEnd = getCaretPosition();
            }
            // 
            final Element list = getListElement(selectionStart);
            final int indexOfSelectionStart = list.getElementIndex(selectionStart);
            final int indexOfSelectionEnd = list.getElementIndex(selectionEnd);
            try {
                getSHTMLDocument().startCompoundEdit();
                final SHTMLWriter writer = new SHTMLWriter(getSHTMLDocument());
                writer.writeStartTag(list);
                for (int i = 0; i < indexOfSelectionStart - 1; i++) {
                    writer.write(list.getElement(i));
                }
                Element tagModel = null;
                if (indexOfSelectionStart > 0 && isListElement(list.getElement(indexOfSelectionStart - 1))) {
                    tagModel = list.getElement(indexOfSelectionStart - 1);
                }
                else if (indexOfSelectionEnd + 1 < list.getElementCount()
                        && isListElement(list.getElement(indexOfSelectionEnd + 1))) {
                    tagModel = list.getElement(indexOfSelectionEnd + 1);
                }
                else {
                    tagModel = list;
                }
                // The list item before the selection start should be the new parent.
                if (indexOfSelectionStart == 0) {
                    // Cannot increase indent of the first item in a list, unlike in MSO and OOo.
                    return;
                }
                final Element newParentListItem = list.getElement(indexOfSelectionStart - 1);
                writer.writeStartTag(newParentListItem);
                writer.writeChildElements(newParentListItem);
                //
                writer.writeStartTag(tagModel);
                for (int i = indexOfSelectionStart; i <= indexOfSelectionEnd; i++) {
                    if (includingSubitems) {
                        writer.write(list.getElement(i));
                    }
                    else {
                        final Element listItem = list.getElement(i);
                        writer.writeStartTag(listItem);
                        for (int j = 0; j < listItem.getElementCount(); j++) {
                            if (!isListElement(listItem.getElement(j))) {
                                writer.write(listItem.getElement(j));
                            }
                        }
                        writer.writeEndTag(listItem);
                        for (int j = 0; j < listItem.getElementCount(); j++) {
                            if (isListElement(listItem.getElement(j))) {
                                writer.writeChildElements(listItem.getElement(j));
                            }
                        }
                    }
                }
                writer.writeEndTag(tagModel);
                writer.writeEndTag(newParentListItem);
                //
                for (int i = indexOfSelectionEnd + 1; i < list.getElementCount(); i++) {
                    writer.write(list.getElement(i));
                }
                writer.writeEndTag(list);
                final String newContent = writer.getWrittenString().replaceAll("(?ims)</ul>\\s*<ul[^>]*>", "")
                    .replaceAll("(?ims)</ol>\\s*<ol[^>]*>", "");
                getSHTMLDocument().setOuterHTML(list, newContent);
                select(selectionStart, selectionEnd);
            }
            catch (final Exception ex) {
            }
            finally {
                getSHTMLDocument().endCompoundEdit();
            }
        }

        /** Decreases the indent of selected list items. ("Including subitems" should default to "true". */
        private void decreaseIndent(final boolean includingSubitems) {
            // The parameter "includingSubitems should always be set to "true". The value of "false" currently causes
            // problems, in that it leads to the creation of incorrect HTML such as 
            // <ul><ul><li>c</li></ul></ul>; the consecutive ULs are incorrect.
            // Example:
            // * a                   <-- Outer list item, beginning the outer list.
            //   * b                 <-- Inner list. Cursor here, before "b".
            //     * c
            // * d
            // paragraphElement: P or P-implied.
            // parent: LI (b)
            // list: UL (containing LI (b))
            // outerListItem: LI (a)
            // outerList: UL (containing LI (a) and the rest.)
            // Result, with "including subitems" set to "true":
            // * a
            // * b
            //   * c
            // * d
            final Element paragraphElement = getCurrentParagraphElement();
            final Element parent = paragraphElement.getParentElement();
            // Guard 1
            if (!isListItemElement(parent)) {
                return;
            }
            // Guard 2
            final Element list = parent.getParentElement();
            final Element outerListItem = list.getParentElement();
            if (!isListItemElement(outerListItem)) {
                return; // Already the outermost item.
            }
            final Element outerList = outerListItem.getParentElement();
            // Decrease indent
            int selectionStart = getSelectionStart();
            int selectionEnd = getSelectionEnd();
            if (selectionStart != selectionEnd && getListElement(selectionStart) == getListElement(selectionEnd)) {
                // Of block
            }
            else {
                // Of single list item
                selectionStart = getCaretPosition();
                selectionEnd = getCaretPosition();
            }
            final int indexOfSelectionStart = list.getElementIndex(selectionStart);
            final int indexOfSelectionEnd = list.getElementIndex(selectionEnd);
            final int indexOfSelectionInOuterItem = outerListItem.getElementIndex(selectionStart);
            final int indexOfSelectionInOuterList = outerList.getElementIndex(selectionStart);
            try {
                getSHTMLDocument().startCompoundEdit();
                final SHTMLWriter writer = new SHTMLWriter(getSHTMLDocument());
                // Write the beginning of the outer list
                writer.writeStartTag(outerList);
                for (int i = 0; i < indexOfSelectionInOuterList; i++) {
                    writer.write(outerList.getElement(i));
                }
                // Write the beginning of the outer list item
                writer.writeStartTag(outerListItem);
                for (int i = 0; i < indexOfSelectionInOuterItem; i++) {
                    writer.write(outerListItem.getElement(i));
                }
                // Write the beginning of the inner list
                if (indexOfSelectionStart > 0) {
                    writer.writeStartTag(list);
                    for (int i = 0; i < indexOfSelectionStart; i++) {
                        writer.write(list.getElement(i));
                    }
                    writer.writeEndTag(list);
                }
                // Close the outer list item
                writer.writeEndTag(outerListItem);
                // Write the promoted (moved to the left) items except for the last one
                for (int i = indexOfSelectionStart; i <= indexOfSelectionEnd - 1; i++) {
                    if (includingSubitems) {
                        writer.write(list.getElement(i));
                    }
                    else {
                        writeListItemForDecreaseIndent(writer, list.getElement(i), includingSubitems, false);
                    }
                }
                // Write the end of the inner list into the last promoted item
                writeListItemForDecreaseIndent(writer, list.getElement(indexOfSelectionEnd), includingSubitems, /*withoutEndTag=*/
                true);
                if ((indexOfSelectionEnd + 1) < list.getElementCount()) {
                    writer.writeStartTag(list);
                    for (int i = indexOfSelectionEnd + 1; i < list.getElementCount(); i++) {
                        writer.write(list.getElement(i));
                    }
                    writer.writeEndTag(list);
                }
                writer.writeEndTag(list.getElement(indexOfSelectionEnd));
                // Write the end of the outer list item, as another list item
                if (indexOfSelectionInOuterItem + 1 < outerListItem.getElementCount()) {
                    writer.writeStartTag(outerListItem);
                    for (int i = indexOfSelectionInOuterItem + 1; i < outerListItem.getElementCount(); i++) {
                        writer.write(outerListItem.getElement(i));
                    }
                    writer.writeEndTag(outerListItem);
                }
                // Write the end of the outer list
                for (int i = indexOfSelectionInOuterList + 1; i < outerList.getElementCount(); i++) {
                    writer.write(outerList.getElement(i));
                }
                writer.writeEndTag(outerList);
                final String newContent = writer.getWrittenString().replaceAll("(?ims)</ul>\\s*<ul>", "")
                    .replaceAll("(?ims)</ol>\\s*<ol>", "");
                getSHTMLDocument().setOuterHTML(outerList, newContent);
                select(selectionStart, selectionEnd);
            }
            catch (final Exception ex) {
            }
            finally {
                getSHTMLDocument().endCompoundEdit();
            }
        }

        /** Writes the list item for the "descrease indent" action. */
        private void writeListItemForDecreaseIndent(final SHTMLWriter writer, final Element listItem,
                                                    final boolean includingSubitems, final boolean withoutEndTag) {
            try {
                writer.writeStartTag(listItem);
                if (includingSubitems) {
                    writer.writeChildElements(listItem);
                }
                else {
                    boolean childListItemsPresent = false;
                    // Write all child elements that are not list items
                    for (int j = 0; j < listItem.getElementCount(); j++) {
                        if (isListElement(listItem.getElement(j))) {
                            childListItemsPresent = true;
                        }
                        else {
                            writer.write(listItem.getElement(j));
                        }
                    }
                    //
                    if (childListItemsPresent) {
                        writer.writeStartTag(listItem.getParentElement());
                        for (int j = 0; j < listItem.getElementCount(); j++) {
                            if (isListElement(listItem.getElement(j))) {
                                writer.write(listItem.getElement(j));
                            }
                        }
                        writer.writeEndTag(listItem.getParentElement());
                    }
                }
                if (!withoutEndTag) {
                    writer.writeEndTag(listItem);
                }
            }
            catch (final Exception ex) {
            }
        }

        /** Performs the action appropriate on pressing of the key delete, as far as lists
         *  are concerned, treating those cases that Java handles poorly. Returns whether
         *  intervention was necessary. */
        private boolean deleteNextChar(final ActionEvent actionEvent) {
            final int nextPosition = getCaretPosition() + 1;
            final SHTMLDocument doc = getSHTMLDocument();
            final Element listAtNextPosition = getListElement(nextPosition);
            if (listAtNextPosition != null && listAtNextPosition.getStartOffset() == nextPosition) {
                // List element starting at the next position.
                if (!caretWithinListItem()) {
                    if (elementIsEmptyParagraph(getCurrentParagraphElement())) {
                        return false;
                    }
                    mergeSecondElementIntoFirst(getParagraphElement(getCaretPosition()),
                        getListItemElement(nextPosition));
                    return true;
                }
                else {
                    // Caret within list item.
                    final Element listAtCaret = getListElement(getCaretPosition());
                    final boolean isSurroundingList = listAtCaret.getStartOffset() <= listAtNextPosition
                        .getStartOffset() && listAtCaret.getEndOffset() >= listAtNextPosition.getEndOffset();
                    if (isSurroundingList) {
                        mergeNestedListItemIntoParent(getListItemElement(getCaretPosition()),
                            getListItemElement(nextPosition));
                        return true;
                    }
                    else {
                        // Two adjacent lists. To be merged.
                        return false;
                    }
                }
            }
            // Empty paragraph within a list item.
            if (caretWithinListItem()) {
                final Element paragraphElement = getCurrentParagraphElement();
                final boolean emptyParagraph = elementIsEmptyParagraph(paragraphElement);
                if (emptyParagraph) {
                    // Remove the empty paragraph manually, to avoid quircks.          
                    removeElement(paragraphElement);
                    //setCaretPosition(getCaretPosition() - 1);
                    return true;
                }
            }
            // List item element starting at the next position
            final Element listItem = getListItemElement(nextPosition);
            if (listItem != null && listItem.getStartOffset() == nextPosition) {
                mergeSecondElementIntoFirst(getListItemElement(getCaretPosition()), listItem);
                return true;
            }
            final Element listElementAtCurrentPosition = getListElement(getCaretPosition());
            final Element parentElementOfNextPosition = doc.getParagraphElement(nextPosition).getParentElement();
            if (listElementAtCurrentPosition != null && !isListItemElement(parentElementOfNextPosition)) {
                // A non-list after a list.
                // Avoid currently buggy behavior. A workaround.
                if ("body".equalsIgnoreCase(parentElementOfNextPosition.getName())
                        || getTableCell(getCaretPosition()) == getTableCell(nextPosition)) {
                    mergeSecondElementIntoFirst(getListItemElement(getCaretPosition()),
                        getParagraphElement(nextPosition));
                }
                else {
                    performDefaultKeyStrokeAction(KeyEvent.VK_RIGHT, 0, actionEvent);
                }
                return true;
            }
            return false;
        }

        /** Performs the action appropriate on pressing of the key backspace, as far as lists
         *  are concerned, treating those cases that Java's Swing handles poorly. Returns whether
         *  intervention was necessary. */
        private boolean deletePrevChar(final ActionEvent actionEvent) {
            final int selectionStart = getSelectionStart();
            final Element list = getListElement(selectionStart);
            if (list != null && list.getStartOffset() == selectionStart) {
                // A list starts at the caret position.
                final Element listAtPrevPosition = selectionStart == 0 ? null : getListElement(selectionStart - 1);
                if (listAtPrevPosition == null) {
                    performToggleListAction(actionEvent, list.getName());
                    return true;
                }
                final boolean isSurroundingList = listAtPrevPosition.getStartOffset() <= list.getStartOffset()
                        && listAtPrevPosition.getEndOffset() >= list.getEndOffset();
                if (isSurroundingList) {
                    mergeNestedListItemIntoParent(getListItemElement(selectionStart - 1),
                        getListItemElement(selectionStart));
                    return true;
                }
                else {
                    // Two adjacent lists. To be merged.
                    return false;
                }
            }
            // Empty paragraph within a list item.
            if (caretWithinListItem()) {
                final Element paragraphElement = getCurrentParagraphElement();
                final boolean emptyParagraph = elementIsEmptyParagraph(paragraphElement);
                if (emptyParagraph) {
                    // Remove the empty paragraph manually, to avoid quirks.          
                    removeElement(paragraphElement);
                    setCaretPosition(getCaretPosition() - 1);
                    return true;
                }
            }
            final Element listItem = getListItemElement(selectionStart);
            if (listItem != null && listItem.getStartOffset() == selectionStart) {
                // List item starts at the current position.
                final int previousPosition = listItem.getStartOffset() - 1;
                mergeSecondElementIntoFirst(getListItemElement(previousPosition), listItem);
                setCaretPosition(previousPosition);
                return true;
            }
            if (selectionStart > 0) {
                final int previousPosition = selectionStart - 1;
                final Element listElementAtPreviousPosition = getListElement(previousPosition);
                final Element parentElement = getCurrentParagraphElement().getParentElement();
                if (listElementAtPreviousPosition != null && !"li".equalsIgnoreCase(parentElement.getName())) {
                    // A non-list after a list.
                    // Avoid currently buggy behavior. A workaround.
                    if ("body".equalsIgnoreCase(parentElement.getName())
                            || getTableCell(previousPosition) == getTableCell(selectionStart)) {
                        mergeSecondElementIntoFirst(getListItemElement(previousPosition),
                            getParagraphElement(selectionStart));
                    }
                    else {
                        performDefaultKeyStrokeAction(KeyEvent.VK_LEFT, 0, actionEvent);
                    }
                    return true;
                }
            }
            return false;
        }

        /** Determines whether a list item element contains nested list items. */
        private boolean containsNestedListItems(final Element listItem) {
            for (int i = 0; i < listItem.getElementCount(); i++) {
                if (isListElement(listItem.getElement(i))) {
                    return true;
                }
            }
            return false;
        }

        /** Merges the second paragraph element into the first one,
         * setting the caret at the point where the two elements are newly joined. */
        private void mergeSecondElementIntoFirst(final Element first, final Element second) {
            final SHTMLDocument doc = (SHTMLDocument) getDocument();
            final SHTMLWriter writer = new SHTMLWriter(doc);
            doc.startCompoundEdit();
            try {
                int finalCaretPosition = first.getEndOffset() - 1;
                writer.writeStartTag(first);
                writer.writeChildElements(first);
                writer.removeLastWrittenNewline();
                writer.writeChildElements(second);
                writer.writeEndTag(first);
                getSHTMLDocument().setOuterHTML(first, writer.getWrittenString());
                removeElement(second);
                finalCaretPosition = Math.min(finalCaretPosition, first.getEndOffset() - 1);
                setCaretPosition(finalCaretPosition);
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
            catch (final BadLocationException e) {
                e.printStackTrace();
            }
            finally {
                doc.endCompoundEdit();
            }
        }

        /** Merges a nested list item into a list item in the parent list. (Inaccurate description.)
         * See also {@link #mergeSecondElementIntoFirst(Element first, Element second)}. 
         * @param parentListItem a list item
         * @param childListItem a list item 
         */
        private void mergeNestedListItemIntoParent(final Element parentListItem, final Element childListItem) {
            final SHTMLDocument doc = (SHTMLDocument) getDocument();
            final SHTMLWriter writer = new SHTMLWriter(doc);
            doc.startCompoundEdit();
            try {
                if (!isListItemElement(parentListItem)) {
                    return;
                }
                final int finalCaretPosition = parentListItem.getElement(0).getEndOffset() - 1;
                writer.writeStartTag(parentListItem);
                for (int i = 0; i < parentListItem.getElementCount(); i++) {
                    if (!isListElement(parentListItem.getElement(i))) {
                        writer.write(parentListItem.getElement(i));
                    }
                }
                writer.removeLastWrittenNewline();
                writer.writeChildElements(childListItem);
                for (int i = 0; i < parentListItem.getElementCount(); i++) {
                    if (isListElement(parentListItem.getElement(i))) {
                        // Write the nested list except for the merged child.
                        final Element list = parentListItem.getElement(i);
                        if (list.getElementCount() >= 2) {
                            // Write the nested list only if it had at least two elements.
                            writer.writeStartTag(list);
                            for (int j = 0; j < list.getElementCount(); j++) {
                                if (list.getElement(j) != childListItem) {
                                    writer.write(list.getElement(j));
                                }
                            }
                            writer.writeEndTag(list);
                        }
                    }
                }
                writer.writeEndTag(parentListItem);
                getSHTMLDocument().setOuterHTML(parentListItem, writer.getWrittenString());
                setCaretPosition(finalCaretPosition);
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
            catch (final BadLocationException e) {
                e.printStackTrace();
            }
            finally {
                doc.endCompoundEdit();
            }
        }

        /** Inserts a new list item after the current list item, breaking the text in the list
         * item into two parts if the caret is not at the end of the current list item. */
        private void newListItem() {
            final SHTMLDocument doc = getSHTMLDocument();
            final int caretPosition = getCaretPosition();
            final Element listItemElement = listManager.getListItemElement(caretPosition);
            if (listItemElement == null) {
                return;
            }
            try {
                final String listItemContent = elementToHTML(listItemElement);
                if (listItemContent.matches("(?ims)\\s*<li[^>]*>\\s*</li>\\s*")) {
                    // Empty list item, so switch the list off.
                    toggleList("", null, true);
                    return;
                }
                final int so = listItemElement.getStartOffset();
                final int eo = listItemElement.getEndOffset();
                if (so != eo) {
                    final StringWriter stringWriter = new StringWriter();
                    final SHTMLWriter writer = new SHTMLWriter(stringWriter, doc);
                    writer.writeStartTag(listItemElement);
                    if (caretPosition > so) {
                        final SHTMLWriter htmlStartWriter = new SHTMLWriter(stringWriter, doc, so, caretPosition - so);
                        htmlStartWriter.writeChildElements(listItemElement);
                    }
                    writer.writeEndTag(listItemElement);
                    writer.writeStartTag(listItemElement);
                    if (containsNestedListItems(listItemElement)
                            && (listItemElement.getElement(0).getEndOffset() - 1) == caretPosition) {
                        // Contains nested list and the caret is at the end of the list items's paragraph.
                        // Let it be. Workaround for the user: press undo.
                        // ---Old:---
                        // Block the action. User can enter new item by pressing enter when the caret
                        // is not at the end of the list item.
                        //return;
                        //stringWriter.write("<p></p>");
                        // A workaround. Otherwise, the caret cannot reach the position in the new 
                        // list item. Ideally, Java's editor kit would create implied paragraph, which
                        // it does not.
                    }
                    if (caretPosition < eo - 1) {
                        final SHTMLWriter htmlEndWriter = new SHTMLWriter(stringWriter, doc, caretPosition, eo
                                - caretPosition);
                        htmlEndWriter.writeChildElements(listItemElement);
                    }
                    writer.writeEndTag(listItemElement);
                    final String text = stringWriter.toString();
                    try {
                        doc.startCompoundEdit();
                        doc.setOuterHTML(listItemElement, text);
                    }
                    catch (final Exception e) {
                        Util.errMsg(null, e.getMessage(), e);
                    }
                    finally {
                        doc.endCompoundEdit();
                    }
                    setCaretPosition(caretPosition + 1);
                }
            }
            catch (final Exception ex) {
            }
        }
    }

    /** remember current selection when mouse button is released */
    void this_mouseReleased(final MouseEvent e) {
        lastSelStart = getSelectionStart();
        lastSelEnd = getSelectionEnd();
    }

    /** remember current selection when mouse button is double clicked */
    void this_mouseClicked(final MouseEvent e) {
        if (e.getClickCount() > 1) {
            lastSelStart = getSelectionStart();
            lastSelEnd = getSelectionEnd();
        }
    }

    /** is invoked if the user modifies the current drop gesture */
    public void dropActionChanged(final DropTargetDragEvent event) {
    }

    /** is invoked when the user changes the dropAction */
    public void dropActionChanged(final DragSourceDragEvent event) {
    }

    /** is invoked when you are dragging over the DropSite */
    public void dragEnter(final DropTargetDragEvent event) {
    }

    /** is invoked when you are exit the DropSite without dropping */
    public void dragExit(final DropTargetEvent event) {
    }

    /**
     * this message goes to DragSourceListener, informing it that the dragging
     * has entered the DropSite
     */
    public void dragEnter(final DragSourceDragEvent event) {
    }

    /**
     * this message goes to DragSourceListener, informing it that the dragging
     * has exited the DropSite
     */
    public void dragExit(final DragSourceEvent event) {
    }

    /**
     * this message goes to DragSourceListener, informing it that
     * the dragging is currently ocurring over the DropSite
     */
    public void dragOver(final DragSourceDragEvent event) {
    }

    // ------ end of drag and drop implementation ----------------------------
    /* ------ start of cut, copy and paste implementation ------------------- */
    public TransferHandler getTransferHandler() {
        final TransferHandler defaultTransferHandler = super.getTransferHandler();
        if (defaultTransferHandler == null) {
            return null;
        }
        class LocalTransferHandler extends TransferHandler {
            /* (non-Javadoc)
             * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent, java.awt.datatransfer.DataFlavor[])
             */
            public boolean canImport(final JComponent comp, final DataFlavor[] transferFlavors) {
                return defaultTransferHandler.canImport(comp, transferFlavors);
            }

            /* (non-Javadoc)
             * @see javax.swing.TransferHandler#exportAsDrag(javax.swing.JComponent, java.awt.event.InputEvent, int)
             */
            public void exportAsDrag(final JComponent comp, final InputEvent e, final int action) {
                defaultTransferHandler.exportAsDrag(comp, e, action);
            }

            /* (non-Javadoc)
             * @see javax.swing.TransferHandler#exportToClipboard(javax.swing.JComponent, java.awt.datatransfer.Clipboard, int)
             */
            public void exportToClipboard(final JComponent comp, final Clipboard clip, final int action) {
                final SHTMLDocument document = (SHTMLDocument) getDocument();
                if (document.getParagraphElement(getSelectionStart()) != document
                    .getParagraphElement(getSelectionEnd())) {
                    defaultTransferHandler.exportToClipboard(comp, clip, action);
                    return;
                }
                try {
                    final HTMLText htmlText = new HTMLText();
                    final int start = getSelectionStart();
                    htmlText.copyHTML(SHTMLEditorPane.this, start, getSelectionEnd() - start);
                    final Transferable additionalContents = new HTMLTextSelection(htmlText);
                    final Clipboard temp = new Clipboard("");
                    defaultTransferHandler.exportToClipboard(comp, temp, action);
                    final Transferable defaultContents = temp.getContents(this);
                    if (defaultContents == null) {
                        return;
                    }
                    clip.setContents(new Transferable() {
                        public DataFlavor[] getTransferDataFlavors() {
                            final DataFlavor[] defaultFlavors = defaultContents.getTransferDataFlavors();
                            final DataFlavor[] additionalFlavors = additionalContents.getTransferDataFlavors();
                            final DataFlavor[] resultFlavor = new DataFlavor[defaultFlavors.length
                                    + additionalFlavors.length];
                            System.arraycopy(defaultFlavors, 0, resultFlavor, 0, defaultFlavors.length);
                            System.arraycopy(additionalFlavors, 0, resultFlavor, defaultFlavors.length,
                                additionalFlavors.length);
                            return resultFlavor;
                        }

                        public boolean isDataFlavorSupported(final DataFlavor flavor) {
                            return additionalContents.isDataFlavorSupported(flavor)
                                    || defaultContents.isDataFlavorSupported(flavor);
                        }

                        public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException,
                                IOException {
                            if (additionalContents.isDataFlavorSupported(flavor)) {
                                return additionalContents.getTransferData(flavor);
                            }
                            return defaultContents.getTransferData(flavor);
                        }
                    }, null);
                }
                catch (final Exception e) {
                    getToolkit().beep();
                }
            }

            /* (non-Javadoc)
             * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
             */
            public int getSourceActions(final JComponent c) {
                return defaultTransferHandler.getSourceActions(c);
            }

            /* (non-Javadoc)
             * @see javax.swing.TransferHandler#getVisualRepresentation(java.awt.datatransfer.Transferable)
             */
            public Icon getVisualRepresentation(final Transferable t) {
                return defaultTransferHandler.getVisualRepresentation(t);
            }

            /* (non-Javadoc)
             * @see javax.swing.TransferHandler#importData(javax.swing.JComponent, java.awt.datatransfer.Transferable)
             */
            public boolean importData(final JComponent comp, final Transferable transferable) {
            	final SHTMLDocument doc = (SHTMLDocument) getDocument();
                doc.startCompoundEdit();
                boolean result = false;
                try {
                	//System.out.format("getPasteMode()=%s\n", getPasteMode());
                	if (getPasteMode() == PasteMode.PASTE_PLAIN_TEXT)
                	{
                		final String content = transferable.getTransferData(DataFlavor.stringFlavor).toString();
                		if (content != null)
                		{
                			replaceSelection(content);
                		}
                		result = true;           		
                	}
                	else if (transferable.isDataFlavorSupported(htmlTextDataFlavor)) {
                        // This path is taken if:
                        // (a) the copy and paste is internal, from SimplyHTML to SimplyHTML, and
                        // (b) the copied part is not multi paragraph.
                        final HTMLText htmlText = (HTMLText) transferable.getTransferData(htmlTextDataFlavor);
                        replaceSelection(htmlText);
                        result = true;
                    }
                    else {
                        final DataFlavor htmlFlavor = new DataFlavor("text/html; class=java.lang.String");
                        String stringContent = null;
                        String htmlContent = null;
                        if (transferable.isDataFlavorSupported(htmlFlavor)
                                && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                            htmlContent = (String) transferable.getTransferData(htmlFlavor);
                            if (htmlContent.charAt(0) == 65533) {
                                return importDataWithoutHtmlFlavor(comp, transferable);
                            }
                            stringContent = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                            htmlContent = new Remover(htmlContent).removeFirstAndBefore("body").removeLastAndAfter("/body")
                            	.getProcessedText()	
                            	.replaceAll("<!--StartFragment-->", "")
                                .replaceAll("<!--EndFragment-->", "");
                            final HTMLText htmlText = new HTMLText(htmlContent, stringContent);
                            replaceSelection(htmlText);
                            result = true;
                        }
                        else {
                            //result = defaultImportData(comp, transferable);
                            result = importExternalData(comp, transferable);
                        }
                    }
                }
                catch (final Exception e) {
                    getToolkit().beep();
                }
                doc.endCompoundEdit();
                return result;
            }

            private boolean importExternalData(final JComponent comp, final Transferable t)
                    throws ClassNotFoundException, UnsupportedFlavorException, IOException {
                // workaround for java decoding bug 
                // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6740877
                final DataFlavor htmlFlavor = new DataFlavor("text/html; class=java.lang.String");
                if (t.isDataFlavorSupported(htmlFlavor)) {
                    final String s = (String) t.getTransferData(htmlFlavor);
                    if (s.charAt(0) == 65533) {
                        return importDataWithoutHtmlFlavor(comp, t);
                    }
                }
                return defaultImportData(comp, t);
            }

            private boolean importDataWithoutHtmlFlavor(final JComponent comp, final Transferable t) {
                return defaultImportData(comp, new Transferable() {
                    public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException,
                            IOException {
                        if (isValid(flavor)) {
                            return t.getTransferData(flavor);
                        }
                        throw new UnsupportedFlavorException(flavor);
                    }

                    public DataFlavor[] getTransferDataFlavors() {
                        final DataFlavor[] transferDataFlavors = t.getTransferDataFlavors();
                        int counter = 0;
                        for (int i = 0; i < transferDataFlavors.length; i++) {
                            if (isValid(transferDataFlavors[i])) {
                                counter++;
                            }
                        }
                        final DataFlavor[] validDataFlavors = new DataFlavor[counter];
                        int j = 0;
                        for (int i = 0; i < transferDataFlavors.length; i++) {
                            final DataFlavor flavor = transferDataFlavors[i];
                            if (isValid(flavor)) {
                                validDataFlavors[j++] = flavor;
                            }
                        }
                        return validDataFlavors;
                    }

                    public boolean isDataFlavorSupported(final DataFlavor flavor) {
                        return isValid(flavor) && t.isDataFlavorSupported(flavor);
                    }

                    private boolean isValid(final DataFlavor flavor) {
                        return !flavor.isMimeTypeEqual("text/html");
                    }
                });
            }

            private boolean defaultImportData(final JComponent comp, final Transferable t) {
                return defaultTransferHandler.importData(comp, t);
            }
        }
        return new LocalTransferHandler();
    }

    /* ------ end of cut, copy and paste implementation --------------- */
    /* ------ start of font/paragraph manipulation --------------- */
    public void removeCharacterAttributes() {
        final int p0 = getSelectionStart();
        final int p1 = getSelectionEnd();
        if (p0 != p1) {
            final SHTMLDocument doc = (SHTMLDocument) getDocument();
            doc.startCompoundEdit();
            SHTMLEditorKit.removeCharacterAttributes(doc, null, p0, p1 - p0);
            doc.endCompoundEdit();
        }
    }

    public void removeParagraphAttributes() {
        final int p0 = getSelectionStart();
        final int p1 = getSelectionEnd();
        final SHTMLDocument doc = (SHTMLDocument) getDocument();
        doc.removeParagraphAttributes(p0, p1 - p0 + 1);
        select(p0, p1);
    }

    public void applyAttributes(final AttributeSet attributeSet, final boolean applyToCompleteParagraph) {
        applyAttributes(attributeSet, applyToCompleteParagraph, false);
    }

    /**
     * Sets the attributes for a given part of this editor. If a range of
     * text is selected, the attributes are applied to the selection.
     * If nothing is selected, the input attributes of the given
     * editor are set thus applying the given attributes to future
     * inputs.
     *
     * @param attributeSet  the set of attributes to apply
     * @param applyToCompleteParagraph  true, if the attributes shall be applied to the whole
     *     paragraph, false, if only the selected range of characters shall have them
     * @param replace  true, if existing attribtes are to be replaced, false if not
     */
    public void applyAttributes(final AttributeSet attributeSet, final boolean applyToCompleteParagraph,
                                final boolean replace) {
        final SHTMLDocument doc = getSHTMLDocument();
        requestFocus();
        final int selectionStart = getSelectionStart();
        final int selectionEnd = getSelectionEnd();
        if (applyToCompleteParagraph) {
            doc.setParagraphAttributes(selectionStart, selectionEnd - selectionStart, attributeSet, replace);
        }
        else {
            if (selectionEnd != selectionStart) {
                doc.setCharacterAttributes(selectionStart, selectionEnd - selectionStart, attributeSet, replace);
            }
            else {
                final MutableAttributeSet inputAttributes = ((SHTMLEditorKit) getEditorKit()).getInputAttributes();
                inputAttributes.addAttributes(attributeSet);
            }
        }
    }

    /** (Unfinished.)*/
    public void applyCharacterTag(final String tag) {
        final int selectionStart = getSelectionStart();
        final int selectionEnd = getSelectionEnd();
        final SHTMLDocument doc = (SHTMLDocument) getDocument();
        doc.startCompoundEdit();
        doc.endCompoundEdit();
    }

    /**
     * Switches the elements in the current selection to the given tag. If allowedTags
     * is non-null, applies the tag only if it is contained in allowedTags.
     * 
     * TODO: The new parameter does not work. So the method only works for paragraph tags,
     * like H1, H2 etc. --Dan
     *
     * @param tag  the tag name to switche elements to
     * @param overwritableTags  Tags that may be overwritten by the new tag.
     */
    public void applyParagraphTag(final String tag, final Vector overwritableTags) {
        final int selectionStart = getSelectionStart();
        final int selectionEnd = getSelectionEnd();
        final StringWriter stringWriter = new StringWriter();
        final SHTMLDocument doc = (SHTMLDocument) getDocument();
        try {
            doc.startCompoundEdit();
            final SHTMLWriter writer = new SHTMLWriter(stringWriter, doc);
            final Element paragraphElement = doc.getParagraphElement(selectionStart);
            final int regionStart = paragraphElement.getStartOffset();
            final int regionEnd = Math.max(paragraphElement.getEndOffset(), selectionEnd);
            //System.out.println("SHTMLEditorPane applyTag start=" + start + ", end=" + end);
            final Element parentOfparagraphElement = paragraphElement.getParentElement();
            int replaceStart = -1;
            int replaceEnd = -1;
            int index = -1;
            int elementsToRemoveCount = 0;
            final int elementCount = parentOfparagraphElement.getElementCount();
            //System.out.println("SHTMLEditorPane applyTag parent elem=" + elem.getName() + ", eCount=" + eCount);
            for (int i = 0; i < elementCount; i++) {
                final Element child = parentOfparagraphElement.getElement(i);
                //System.out.println("SHTMLEditorPane applyTag child elem=" + child.getName() + ", eCount=" + eCount);
                final int elementStart = child.getStartOffset();
                final int elementEnd = child.getEndOffset();
                //System.out.println("SHTMLEditorPane applyTag eStart=" + eStart + ", eEnd=" + eEnd);
                if ((elementStart >= regionStart && elementStart < regionEnd)
                        || (elementEnd > regionEnd && elementEnd <= regionEnd)) {
                    elementsToRemoveCount++;
                    if (overwritableTags.contains(child.getName())) {
                        //System.out.println("SHTMLEditorPane applyTag element is in selection");
                        writer.writeStartTag(tag.toString(), child.getAttributes());
                        writer.writeChildElements(child);
                        writer.writeEndTag(tag.toString());
                        if (index < 0) {
                            index = i;
                        }
                        if (replaceStart < 0 || replaceStart > elementStart) {
                            replaceStart = elementStart;
                        }
                        if (replaceEnd < 0 || replaceEnd < elementEnd) {
                            replaceEnd = elementEnd;
                        }
                    }
                    else {
                        writer.write(child);
                    }
                }
            }
            //System.out.println("SHTMLEditorPane applyTag remove index=" + index + ", removeCount=" + removeCount);
            if (index > -1) {
                doc.insertAfterEnd(parentOfparagraphElement.getElement(index), stringWriter.getBuffer().toString());
                doc.removeElements(parentOfparagraphElement, index, elementsToRemoveCount);
            }
            //SHTMLEditorKit kit = (SHTMLEditorKit) getEditorKit();
            //System.out.println("SHTMLEditorPane applyTag new HTML=\r\n" + sw.getBuffer().toString() );
            //kit.read(new StringReader(sw.getBuffer().toString()), doc, getCaretPosition());
        }
        catch (final Exception e) {
            Util.errMsg(this, e.getMessage(), e);
        }
        finally {
            doc.endCompoundEdit();
        }
    }

    /* ------ end of font/paragraph manipulation --------------- */
    /* ---------- class fields start -------------- */
    static Action toggleBulletListAction = null;
    static Action toggleNumberListAction = null;

    private void performToggleListAction(final ActionEvent e, final String elemName) {
        if (elemName.equalsIgnoreCase(HTML.Tag.UL.toString())) {
            if (toggleBulletListAction == null) {
                final Component c = (Component) e.getSource();
                final SHTMLPanelImpl panel = SHTMLPanelImpl.getOwnerSHTMLPanel(c);
                toggleBulletListAction = panel.dynRes.getAction(SHTMLPanelImpl.toggleBulletsAction);
            }
            toggleBulletListAction.actionPerformed(e);
        }
        else if (elemName.equalsIgnoreCase(HTML.Tag.OL.toString())) {
            if (toggleNumberListAction == null) {
                final Component c = (Component) e.getSource();
                final SHTMLPanelImpl panel = SHTMLPanelImpl.getOwnerSHTMLPanel(c);
                toggleNumberListAction = panel.dynRes.getAction(SHTMLPanelImpl.toggleNumbersAction);
            }
            toggleNumberListAction.actionPerformed(e);
        }
    }

    public static final String newListItemAction = "newListItem";
    public static final String insertLineBreakAction = "insertLineBreak";
    public static final String deletePrevCharAction = "deletePrevChar";
    public static final String deleteNextCharAction = "deleteNextChar";
    public static final String moveUpAction = "moveUp";
    public static final String homeAction = "home";
    public static final String shiftHomeAction = "shiftHome";
    public static final String shiftEndAction = "shiftEnd";
    public static final String endAction = "end";
    public static final String moveDownAction = "moveDown";
    /** a data flavor for transferables processed by this component */
    private final DataFlavor htmlTextDataFlavor = new DataFlavor(com.lightdev.app.shtm.HTMLText.class, "HTMLText");

    /* Cursors for mouseovers in the editor */
    void updateInputAttributes() {
        ((SHTMLEditorKit) getEditorKit()).updateInputAttributes(this);
        fireCaretUpdate(new CaretEvent(this) {
            public int getDot() {
                return getSelectionStart();
            }

            public int getMark() {
                return getSelectionEnd();
            }
        });
    }

    public JPopupMenu getPopup() {
        return popup;
    }

    public void setPopup(final JPopupMenu popup) {
        this.popup = popup;
    }

    /** Determines whether the caret is currently within a table cell. */
    private boolean caretWithinTableCell() {
        final Element tableCell = getCurrentTableCell();
        return tableCell != null;
    }

    /** Returns the string HTML representation of the element. */
    public String elementToHTML(final Element element) {
        final HTMLDocument document = (HTMLDocument) getDocument();
        final StringWriter stringWriter = new StringWriter();
        final SHTMLWriter shtmlWriter = new SHTMLWriter(stringWriter, document);
        try {
            shtmlWriter.write(element);
        }
        catch (final Exception ex) {
        }
        return stringWriter.getBuffer().toString();
    }

    /** Determines whether an element is an empty paragraph. */
    private boolean elementIsEmptyParagraph(final Element element) {
        final String elementContent = elementToHTML(element);
        return elementContent.matches("(?ims)\\s*<p[^>]*>\\s*</p>\\s*");
    }

    private void performDefaultKeyStrokeAction(final int keyCode, final int modifiers, final ActionEvent event) {
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, modifiers);
        final Object key = getInputMap().getParent().get(keyStroke);
        if (key != null) {
            getActionMap().getParent().get(key).actionPerformed(event);
        }
    }

    /** Performs the default key stroke action, assuming that the caret is within
     * a table cell and that the action is a cursor move; if the cursor leaves
     * the current table cell, undoes the action.
     * Returns true if the cursor stayed within the table cell. */
    public boolean tryDefaultKeyStrokeActionWithinCell(final int keyCode, final int modifiers, final ActionEvent event) {
        final int originalCaretPosition = getCaretPosition();
        final Element cellElement = getCurrentTableCell();
        performDefaultKeyStrokeAction(keyCode, modifiers, event);
        final Element cellElementAfter = getCurrentTableCell();
        if (cellElement == cellElementAfter) {
            return true;
        }
        setCaretPosition(originalCaretPosition);
        return false;
    }
    /* (non-Javadoc)
     * @see javax.swing.JComponent#getTransferHandler()
     */
    /* ---------- class fields end -------------- */
}
