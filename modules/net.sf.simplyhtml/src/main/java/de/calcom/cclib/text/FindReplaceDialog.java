/*
 * SimplyHTML, a word processor based on Java, HTML and CSS
 * Copyright (C) 2003 Ulrich Hilger
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package de.calcom.cclib.text;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.MutableComboBoxModel;
import javax.swing.RootPaneContainer;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;

import com.lightdev.app.shtm.SHTMLPanel;
import com.lightdev.app.shtm.Util;

import de.calcom.cclib.text.PseudoDamerauLevenshtein.Alignment;

/**
 * Dialog to manage find and replace on a <code>Document</code> shown
 * in a <code>JEditorPane</code>.
 *
 * <p>The component has a 'pluggable' interface about how to deal with the
 * event that one document has been searched to the end. If it is
 * constructed with a FindReplaceListener, it shows an additional
 * checkbox 'Whole project' where the user can select, if a group of
 * documents shall be searched instead of a single document.</p>
 *
 * <p>If only one document is searched, the dialog searches only in the
 * document currently shown in the editor.</p>
 *
 * <p>By adding a FindReplaceListener listening for FindReplaceEvents,
 * the FindReplaceDialog notifies other classes about the fact that a
 * group of documents shall be searched instead of only the current one.</p>
 *
 * <p>Initially FindReplaceDialog notifies the listener that the first document
 * in the group shall be loaded into the editor.</p>
 *
 * <p>After loading the first document and resuming the find or replace
 * operation, the listener gets informed that the end of a document has been
 * reached. A handling method for that event should cause the editor to
 * load the next document in the group before it resumes the find or
 * replace operation.</p>
 *
 * <p><b>Example for an implementation of FindReplaceListener:</b></p>
 * <p><b>IMPORTANT: </b>the methods of the FindReplaceListener need to
 * call either resumeOperation() or terminateOperation() on the
 * FindReplaceDialog, that fired the FindReplaceEvent. Otherwise
 * the FindReplaceDialog could 'hang'.</p>
 * <p>
 * <pre>
 *   FindReplaceDialog frd = new FindReplaceDialog(aFrame,
 *                             myEditorPane, new MyFindReplaceListener());
 *
 *   protected class MyFindReplaceListener implements FindReplaceListener {
 *     public void getNextDocument(FindReplaceEvent e) {
 *       if(documentsLeft()) { // documentsLeft() is a method coded somewhere else
 *         myEditorPane.setDocument(nextDocument()); // nextDocument() is a method coded somewhere else
 *         ((FindReplaceDialog) e.getSource()).resumeOperation();
 *       }
 *       else {
 *         ((FindReplaceDialog) e.getSource()).terminateOperation();
 *       }
 *     }
 *
 *     public void getFirstDocument(FindReplaceEvent e) {
 *       myEditorPane.setDocument(firstDocument()); // firstDocument() is a method coded somewhere else
 *       ((FindReplaceDialog) e.getSource()).resumeOperation();
 *     }
 *   }
 * </pre>
 * </p>
 *
 * <p>Added i18n support for application SimplyHTML in version 1.5</p>
 *
 * @author Ulrich Hilger
 * @author CalCom
 * @author <a href="http://www.calcom.de">http://www.calcom.de</a>
 * @author <a href="mailto:info@calcom.de">info@calcom.de</a>
 *
 * @version 1.5, April 27, 2003
 *
 * @see javax.swing.text.Document
 * @see javax.swing.JEditorPane
 */
public class FindReplaceDialog extends JDialog {
    /* ---- Constructor(s) start -----------*/
    /**
     * Construct a <code>FindReplaceDialog</code>.
     *
     * <p>Does not show the dialog window, as fields 'editor' and 'doc'
     * have to be set separately before the dialog is operable.</p>
     *
     * @see javax.swing.JEditorPane
     * @see javax.swing.text.Document
     * @see java.awt.Frame
     */
    public FindReplaceDialog() {
        try {
            jbInit();
            initDialogContents();
            rememberSearchTermFromSelection();
            pack();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Construct a <code>FindReplaceDialog</code>.
     *
     * <p>Shows the dialog window modal, packed and centered over the owning
     * <code>Frame</code> after construction.</p>
     *
     * <p>Using this constructor implies the dialog shall be used in mode
     * MODE_DOCUMENT</p>
     *
     * @param owner  the <code>Frame</code> that owns this dialog
     * @param editor  <code>JEditorPane</code> displaying the
     *                    <code>Document</code> to seach in
     *
     * @see javax.swing.JEditorPane
     * @see javax.swing.text.Document
     * @see java.awt.Frame
     */
    public FindReplaceDialog(final Frame owner, final JEditorPane editor) {
        setEditor(editor);
        setMode(MODE_DOCUMENT);
        try {
            jbInit();
            initDialogContents();
            centerDialog(owner);
            rememberSearchTermFromSelection();
            pack();
            setVisible(true);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Construct a <code>FindReplaceDialog</code>.
     *
     * <p>Shows the dialog window modal, packed and centered over the owning
     * <code>Frame</code> after construction.</p>
     *
     * <p>Using this constructor implies the dialog shall be used in mode
     * MODE_PROJECT</p>
     *
     * @param owner  the <code>Frame</code> that owns this dialog
     * @param editor  <code>JEditorPane</code> displaying the
     *                    <code>Document</code> to seach in
     * @param listener  listener for handling FindReplaceEvents
     *
     * @see javax.swing.JEditorPane
     * @see javax.swing.text.Document
     * @see java.awt.Frame
     */
    public FindReplaceDialog(final Frame owner, final JEditorPane editor, final FindReplaceListener listener) {
        setEditor(editor);
        setMode(MODE_PROJECT);
        addFindReplaceListener(listener);
        try {
            jbInit();
            initDialogContents();
            centerDialog(owner);
            rememberSearchTermFromSelection();
            pack();
            setVisible(true);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /* --------- Constructor(s) end ------------- */
    
    private void rememberSearchTermFromSelection()
    {
    	if (editor.getSelectedText() != null)
    	{
    		rememberSearchTerm(editor.getSelectedText(), jcomboSearchTerm);
    	}
    }
    
    /* --------- Event handling start ------------- */
    /**
     * add an event listener to this dialog.
     *
     * @param  listener  the event listener to add
     */
    public void addFindReplaceListener(final FindReplaceListener listener) {
        listeners.addElement(listener);
    }

    /**
     * remove an event listener from this dialog.
     *
     * @param  listener  the event listener to remove
     */
    public void removeFindReplaceListener(final FindReplaceListener listener) {
        listeners.removeElement(listener);
    }

    /**
     * notify listeners interested in this event that it occurred
     *
     * @param  node  the node, that is affected by the event
     */
    private void fireGetNextDocument() {
        final Enumeration listenerList = listeners.elements();
        while (listenerList.hasMoreElements()) {
            ((FindReplaceListener) listenerList.nextElement()).getNextDocument(new FindReplaceEvent(this));
        }
    }

    /**
     * notify listeners interested in this event that it occurred
     *
     * @param  node  the node, that is affected by the event
     */
    private void fireGetFirstDocument() {
        final Enumeration listenerList = listeners.elements();
        while (listenerList.hasMoreElements()) {
            ((FindReplaceListener) listenerList.nextElement()).getFirstDocument(new FindReplaceEvent(this));
        }
    }

    /**
     * notify listeners interested in this event that it occurred
     *
     * @param  node  the node, that is affected by the event
     */
    private void fireFindReplaceTerminated() {
        final Enumeration listenerList = listeners.elements();
        while (listenerList.hasMoreElements()) {
            ((FindReplaceListener) listenerList.nextElement()).findReplaceTerminated(new FindReplaceEvent(this));
        }
    }

    /**
     * Resume the current operation after a getFirstDocument or getNextDocument
     * event was fired
     */
    public void resumeOperation() {
        doc = editor.getDocument();
        findInProgress = false;
        initFind();
        switch (operation) {
            case OP_FIND:
                find();
                break;
            case OP_REPLACE:
                replace();
                break;
        }
    }

    /**
     * Terminate the current operation
     */
    public void terminateOperation() {
        switch (operation) {
            case OP_FIND:
                message(Util.getResourceString(SHTMLPanel.getResources(), "noMoreOccurrencesFound"));
                toggleState(STATE_UNLOCKED);
                jbtnReplace.setEnabled(true);
                break;
            case OP_REPLACE:
                switch (replaceChoice) {
                    case RO_YES:
                    case RO_NO:
                        message(Util.getResourceString(SHTMLPanel.getResources(), "noMoreOccurrencesFound"));
                        break;
                    case RO_ALL:
                        message(Util.getResourceString(SHTMLPanel.getResources(), "allOccurrencesReplaced"));
                        break;
                }
                toggleState(STATE_UNLOCKED);
                setVisible(true);
                break;
        }
        operation = OP_NONE;
        fireFindReplaceTerminated();
    }

    /**
     * perform a find, when button 'find next' is pressed
     */
    private void jbtnFindNext_actionPerformed(final ActionEvent e) {
        operation = OP_FIND;
        jbtnReplace.setEnabled(false);
        if (mode == MODE_PROJECT && jcbProject.isSelected() && listeners.size() > 0 && !findInProgress) {
            fireGetFirstDocument();
        }
        else {
            initFind();
            find();
        }
    }

    /**
     * perform a replace, when button 'replace' is pressed
     */
    private void jbtnReplace_actionPerformed(final ActionEvent e) {
        operation = OP_REPLACE;
        replaceChoice = RO_YES;
        setVisible(false);
        if (mode == MODE_PROJECT && jcbProject.isSelected() && listeners.size() > 0 && !findInProgress) {
            fireGetFirstDocument();
        }
        else {
            initFind();
            replace();
        }
    }

    /**
     * Cancels the current find operation and switches the dialog back to
     * normal.
     */
    private void jbtnCancel_actionPerformed(final ActionEvent e) {
        toggleState(STATE_UNLOCKED);
        jbtnReplace.setEnabled(true);
    }

    /**
     * When Close is pressed, store the pressed button in the result
     * of this dialog and dispose the dialog.
     */
    private void jbtnClose_actionPerformed(final ActionEvent e) {
        result = JOptionPane.OK_OPTION;
        dispose();
    }

    /* --------- Event handling end ------------- */
    /* --------- Getters and setters start ------------- */
    /**
     * Set the JEditorPane holding the document to be searched
     *
     * @param editor  the JEditorPane holding the document to be searched
     *
     * @see javax.swing.JEditorPane
     */
    public void setEditor(final JEditorPane editor) {
        this.editor = editor;
        doc = editor.getDocument();
    }

    /**
     * Set the mode.
     *
     * <p>Switches between</p>
     * <ul>
     * <li>MODE_DOCUMENT: only the document currently viewed in the editor can be
     *    searched</li>
     * <li>MODE_PROJECT: An additional check box allows to choose, whether or not
     *    the user likes to search a whole group of documents.</li>
     * </ul>
     *
     * @param mode  one of MODE_DOCUMENT and MODE_PROJECT
     */
    public void setMode(final int mode) {
        this.mode = mode;
        if (mode == MODE_PROJECT) {
            jcbProject.setVisible(true);
        }
        else {
            jcbProject.setVisible(false);
        }
    }
    
	public void setSearchingBusyCursor()
	{
		//RootPaneContainer root = (RootPaneContainer)getTopLevelAncestor();
		getRootPane().getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		getRootPane().getGlassPane().setVisible(true);
	}
	
	public void setSearchingDefaultCursor()
	{
		//RootPaneContainer root = (RootPaneContainer)getTopLevelAncestor();
		getRootPane().getGlassPane().setCursor(Cursor.getDefaultCursor());
		getRootPane().getGlassPane().setVisible(false);
	}


    /* --------- Getters and setters end ------------- */
    /* --------- Find implementation start ------ */
    /**
     * Initialize the find operation by reading all relevant settings and
     * locking the dialog window.
     */
    private void initFind() {
    	//System.out.format("initFind(findInProgress=%s)\n", findInProgress);
        if (!findInProgress) {
            try {
                searchText = doc.getText(0, doc.getLength());
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
            searchTerm = (String)jcomboSearchTerm.getEditor().getItem();
            //System.out.format("initFind(): searchTerm='%s'\n", searchTerm);
            
            rememberSearchTerm(searchTerm, jcomboSearchTerm);
            matchCaseSetting.getAndSet(jcbMatchCase.isSelected());
            matchApproxSetting.getAndSet(jcbMatchApprox.isSelected());
            
            replacementText = jtfReplace.getText();
            replaceDiff = replacementText.length() - searchTerm.length();
            offset = 0;
            if (!jcbMatchCase.isSelected()) {
                searchTerm = searchTerm.toLowerCase();
                searchText = searchText.toLowerCase();
            }
            
            if (jcbMatchApprox.isSelected())
            {
            	initApproximateSearch();
            }
            else
            {
	            initExactSearch();
            }
            toggleState(STATE_LOCKED);
        }
    }

	private void initExactSearch() {
	    if (jcbStartOnTop.isSelected()) {
	        if (jrbUp.isSelected()) {
	            lastPosition = doc.getLength();
	        }
	        else {
	            lastPosition = -1;
	        }
	    }
	    else {
	        lastPosition = editor.getSelectionStart();
	    }
    }

	private void initApproximateSearch() {
	    PseudoDamerauLevenshtein PDL = new PseudoDamerauLevenshtein();
	    PDL.init(searchTerm, searchText, true, jcbMatchCase.isSelected());
	    
	    // get the approximate search threshold parameter (0.65 by default)
	    // (see http://freeplane.sourceforge.net/wiki/index.php/Approximate_search)
	    double threshold = Double.parseDouble(Util.getPreference("approximate_search_threshold", null));
	    //System.out.format("simplyhtml: approximate_search_threshold=%.2f\n", threshold);
	    
	    try
	    {
	    	setSearchingBusyCursor();
	    	currentApproximateMatches = PDL.computeAlignments(threshold);
	    }
	    finally
	    {
	    	setSearchingDefaultCursor();
	    }
	    if (jcbStartOnTop.isSelected())
	    {
	    	if (jrbUp.isSelected()) // search bottom-up
	    	{
	    		currentApproximateMatchIndex = currentApproximateMatches.size();
	    	}
	    	else // search top-down
	    	{
	    		currentApproximateMatchIndex = -1;
	    	}
	    }
	    else
	    {
	    	int start = editor.getSelectionStart();
	    	// get first match after 'start'
	    	int i = 0;
	    	currentApproximateMatchIndex = 0;
	    	for (Alignment ali: currentApproximateMatches)
	    	{
	    		if (ali.getMatchStart() >= start)
	    		{
	    			currentApproximateMatchIndex = i - 1;
	    			break;
	    		}
	    		i++;
	    	}
	    }
    }

    /**
     * Initiate a find or find next operation, whatever applies. If no (more)
     * hits are found, a message is displayed and the dialog is unlocked for a
     * new search operation.
     */
    private void find() {
        if (!doFind()) {
            if (mode == MODE_PROJECT && jcbProject.isSelected() && listeners.size() > 0) {
                fireGetNextDocument();
            }
            else {
                terminateOperation();
            }
        }
    }

    /**
     * Look for the next occurrence of the search phrase either as whole word
     * or as part of a word, depending on the current dialog setting.
     *
     * <p>If the phrase is found (again), its position is 'remembered' for a
     * possible findNext and its postion is highlighted in the underlying
     * <code>JEditorPane</code>.
     *
     * @return  true, if the phrase was found (again), false if not
     *
     * @see javax.swing.JEditorPane
     */
    private boolean doFind() {
        boolean found = false;
        int start;
        if (jcbMatchApprox.isSelected())
        	start = findNextApproximately();
        else
        {
        	start = findNext();
        	if (jcbWholeWords.isSelected()) {
        		start = findWholeWords(start);
        	}
        }
        if (start >= 0) { // we found a match!
            lastPosition = start;
            if (jrbDown.isSelected()) {
                start += offset;
            }
            editor.setCaretPosition(start);
            editor.moveCaretPosition(start + getMatchLength());
            editor.getCaret().setSelectionVisible(true);
            found = true;
        }
//        System.out.format("doFind() => start=%d/found=%s\n",
//        		start, found);
        return found;
    }
    
    private int getMatchLength()
    {
    	if (jcbMatchApprox.isSelected())
    		return currentApproximateMatches.get(currentApproximateMatchIndex).getMatch().length();
    	else
    		return searchTerm.length();
    }
    
    private int findNextApproximately()
    { 
    	int nextIndex = currentApproximateMatchIndex + (jrbUp.isSelected() ? -1 : +1);
    	if (nextIndex < 0 || nextIndex >= currentApproximateMatches.size())
    	{
    		return -1;
    	}
    	else
    	{
    		currentApproximateMatchIndex = nextIndex;
    		return currentApproximateMatches.get(nextIndex).getMatchStart();
    	}
    }

    /**
     * Finds the next occurrence of the search term, starting from the last position,
     * either in the direction up or down, returning the position of the found occurrence.
     *
     * @return the start position of the next occurrence or
     *                          0 if no more hits were found
     */
    private int findNext() {
    	// avoid endless loop due to "" always being found
    	if (searchTerm.length() == 0)
    		return -1;
    	
        int start = -1; // -1 means not found.
        if (jrbUp.isSelected()) {
            if (lastPosition < doc.getLength()) {
                start = searchText.lastIndexOf(searchTerm, lastPosition - 1);
            }
            else {
                start = searchText.lastIndexOf(searchTerm, lastPosition);
            }
        }
        else {
            if (lastPosition >= 0) {
                start = searchText.indexOf(searchTerm, lastPosition + searchTerm.length());
            }
            else {
                start = searchText.indexOf(searchTerm, lastPosition);
            }
        }
        return start;
    }

    /**
     * Find the next whole word occurrence of the searched phrase from
     * a given position.
     *
     * @param start  the position to start the search at
     *
     * @return the start position of the next occurrence or
     *                          0 if no more hits were found
     */
    private int findWholeWords(int start) {
        while ((start > 0)
                && ((!isSeparator(searchText.charAt(start - 1))) || (!isSeparator(searchText.charAt(start
                        + searchTerm.length()))))) {
            lastPosition = start;
            start = findNext();
        }
        return start;
    }

    /* ----------- Find implementation end ------- */
    /* ----------- Replace implementation start ------- */
    /**
     * Initiate a replace operation. If no (more)
     * hits are found, a message is displayed and the dialog is unlocked for a
     * new search operation.
     */
    private void replace() {
        while (replaceChoice != RO_DONE && doFind()) {
            if (replaceChoice != RO_ALL) {
                replaceChoice = getReplaceChoice();
            }
            switch (replaceChoice) {
                case RO_YES:
                    replaceOne();
                    break;
                case RO_ALL:
                    replaceOne();
                    while (doFind()) {
                        replaceOne();
                    }
                    break;
            }
        }
        if (mode == MODE_PROJECT && jcbProject.isSelected() && listeners.size() > 0) {
            switch (replaceChoice) {
                case RO_YES:
                case RO_NO:
                case RO_ALL:
                    fireGetNextDocument();
                    break;
                case RO_DONE:
                    terminateOperation();
                    break;
            }
        }
        else {
            terminateOperation();
        }
    }

    /**
     * Show an option window asking the user for a decision about what to
     * do with the found occurrence during a replace operation.
     *
     * @return the chosen option, one of RO_YES, RO_NO, RO_DONE and RO_ALL
     */
    private int getReplaceChoice() {
        final String msg = Util.getResourceString(SHTMLPanel.getResources(), "replaceThisQuery") + " '" + searchTerm
                + "'?";
        return JOptionPane.showOptionDialog(this, msg,
            Util.getResourceString(SHTMLPanel.getResources(), "findReplaceDialogTitle"),
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, replaceOptions, null);
    }

    /**
     * Replace the currently selected occurrence of the search phrase
     */
    private void replaceOne() {
        editor.replaceSelection(replacementText);
        offset += replaceDiff;
    }

    /* ----------- Replace implementation end ------- */
    /* ----------- Helper methods start ------- */
    /**
     * Set dialog components to their inital state
     */
    private void initDialogContents() {
        jbtnCancel.setEnabled(false);
        jrbUp.setSelected(false);
        jrbDown.setSelected(true);
        jcbWholeWords.setSelected(false);
        jcbMatchCase.setSelected(matchCaseSetting.get());
        jcbMatchApprox.setSelected(matchApproxSetting.get());
        jcbStartOnTop.setSelected(true);
        jcbProject.setSelected(false);
        
        MutableComboBoxModel searchTermComboModel = (MutableComboBoxModel)jcomboSearchTerm.getModel();
        while (searchTermComboModel.getSize() > 0)
        {
        	searchTermComboModel.removeElementAt(0);
        }
        for (final String searchTerm: searchTermHistory)
        {
        	searchTermComboModel.addElement(searchTerm);
        }
        jcomboSearchTerm.setEditable(true);
        
        jtfReplace.setText("");
    }

    /**
     * Center this dialog window relative to its owning <code>Frame</code>.
     *
     * @param owner  <code>Frame</code> owning this dialog
     *
     * @see java.awt.Frame
     */
    public void centerDialog(final Frame owner) {
        final Dimension dlgSize = getPreferredSize();
        final Dimension frmSize = owner.getSize();
        final Point loc = owner.getLocation();
        setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
    }

    /**
     * Toggle the state of the dialog window.
     *
     * <p>The state of the dialog is either unlocked (no find in progress) or
     * locked (find in progress).</p>
     *
     * @param unlocked  one of FindReplaceDialog.STATE_LOCKED and
     *      FindReplaceDialog.STATE_UNLOCKED
     */
    private void toggleState(final boolean unlocked) {
        jbtnCancel.setEnabled(!unlocked);
        jbtnClose.setEnabled(unlocked);
        jcomboSearchTerm.setEnabled(unlocked);
        jtfReplace.setEnabled(unlocked);
        jLabel3.setEnabled(unlocked);
        jLabel4.setEnabled(unlocked);
        jcbWholeWords.setEnabled(unlocked);
        jcbMatchCase.setEnabled(unlocked);
        jcbMatchApprox.setEnabled(unlocked);
        jcbStartOnTop.setEnabled(unlocked);
        jrbUp.setEnabled(unlocked);
        jrbDown.setEnabled(unlocked);
        jcbProject.setEnabled(unlocked);
        findInProgress = !unlocked;
    }

    /**
     * method for determining whether or not a character is a
     * word separator.
     */
    private boolean isSeparator(final char ch) {
        int i = 0;
        while ((i < WORD_SEPARATORS.length) && (ch != WORD_SEPARATORS[i])) {
            i++;
        }
        return (i < WORD_SEPARATORS.length);
    }

    /**
     * Show an information message
     */
    private void message(final String msgText) {
        JOptionPane.showMessageDialog(this, msgText,
            Util.getResourceString(SHTMLPanel.getResources(), "findReplaceDialogTitle"),
            JOptionPane.INFORMATION_MESSAGE);
    }

    /* ----------- Helper methods end ------- */
    /** GUI builder init */
    private void jbInit() throws Exception {
        final KeyListener escapeKeyListender = new KeyAdapter() {
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    e.consume();
                    jbtnClose_actionPerformed(null);
                }
            }
        };
        titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(142, 142, 142)),
            "Options");
        final ButtonGroup bgSearchDirection = new ButtonGroup();
        jbtnFindNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                jbtnFindNext_actionPerformed(e);
            }
        });
        jbtnFindNext.setText(Util.getResourceString(SHTMLPanel.getResources(), "findNext"));
        jbtnFindNext.setPreferredSize(new Dimension(100, 27));
        jbtnFindNext.setMinimumSize(new Dimension(100, 27));
        jbtnFindNext.setMaximumSize(new Dimension(100, 27));
        jbtnFindNext.addKeyListener(escapeKeyListender);
        jcbStartOnTop.setText(Util.getResourceString(SHTMLPanel.getResources(), "searchFromStart"));
        jcbStartOnTop.setToolTipText(Util.getResourceString(SHTMLPanel.getResources(), "searchFromStart.tooltip"));
        jrbDown.setText(Util.getResourceString(SHTMLPanel.getResources(), "searchDown"));
        
        jcbWholeWords.setText(Util.getResourceString(SHTMLPanel.getResources(), "wholeWordsOnly"));
        jcbWholeWords.setToolTipText(Util.getResourceString(SHTMLPanel.getResources(), "wholeWordsOnly.tooltip"));
        jrbDown.setToolTipText(Util.getResourceString(SHTMLPanel.getResources(), "searchDown.tooltip"));
        jrbUp.setToolTipText(Util.getResourceString(SHTMLPanel.getResources(), "searchUp.tooltip"));
        
        jpnlBtn.setLayout(gridBagLayout4);
        jpnlOptions.setBorder(titledBorder1);
        jpnlOptions.setLayout(gridLayout2);
        jpnlFind.setLayout(gridBagLayout5);
        jtfReplace.setMinimumSize(new Dimension(4, 12));
        jtfReplace.setPreferredSize(new Dimension(59, 12));
        jtfReplace.setText("jtfReplace");
        jtfReplace.addKeyListener(new KeyAdapter() {
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    e.consume();
                    jbtnClose_actionPerformed(null);
                }
                else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    jbtnReplace_actionPerformed(null);
                    jbtnReplace.requestFocus();
                }
            }
        });
        jpnlMain.setLayout(gridBagLayout6);
        jrbUp.setText(Util.getResourceString(SHTMLPanel.getResources(), "searchUp"));
        jcomboSearchTerm.setMinimumSize(new Dimension(4, 12));
        jcomboSearchTerm.setPreferredSize(new Dimension(63, 12));
        //jcomboSearchTerm.setText("jtfPhrase");
        jcomboSearchTerm.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    e.consume();
                    jbtnClose_actionPerformed(null);
                }
                else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    jbtnFindNext_actionPerformed(null);
                    jbtnFindNext.requestFocus();
                }
            }
        });
        
        jcbMatchCase.setText(Util.getResourceString(SHTMLPanel.getResources(), "matchCase"));
        jcbMatchCase.setToolTipText(Util.getResourceString(SHTMLPanel.getResources(), "matchCase.tooltip"));
        jcbMatchApprox.setText(Util.getResourceString(SHTMLPanel.getResources(), "matchApproximately"));
        jcbMatchApprox.setToolTipText(Util.getResourceString(SHTMLPanel.getResources(), "matchApproximately.tooltip"));
        
        jLabel3.setText(Util.getResourceString(SHTMLPanel.getResources(), "replaceWith"));
        jLabel4.setText(Util.getResourceString(SHTMLPanel.getResources(), "textToFind"));
        jbtnClose.setMaximumSize(new Dimension(100, 27));
        jbtnClose.setMinimumSize(new Dimension(100, 27));
        jbtnClose.setPreferredSize(new Dimension(100, 27));
        jbtnClose.setText(Util.getResourceString(SHTMLPanel.getResources(), "closeBtnName"));
        jbtnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                jbtnClose_actionPerformed(e);
            }
        });
        gridLayout2.setRows(4);
        gridLayout2.setColumns(2);
        this.setModal(true);
        this.setTitle(Util.getResourceString(SHTMLPanel.getResources(), "findReplaceDialogTitle"));
        jbtnReplace.setMaximumSize(new Dimension(100, 27));
        jbtnReplace.setMinimumSize(new Dimension(100, 27));
        jbtnReplace.setPreferredSize(new Dimension(100, 27));
        jbtnReplace.setText(Util.getResourceString(SHTMLPanel.getResources(), "replace"));
        jbtnReplace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                jbtnReplace_actionPerformed(e);
            }
        });
        jbtnReplace.addKeyListener(escapeKeyListender);
        jbtnCancel.setMaximumSize(new Dimension(100, 27));
        jbtnCancel.setMinimumSize(new Dimension(100, 27));
        jbtnCancel.setPreferredSize(new Dimension(100, 27));
        jbtnCancel.setText(Util.getResourceString(SHTMLPanel.getResources(), "cancelBtnName"));
        jbtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                jbtnCancel_actionPerformed(e);
            }
        });
        jcbProject.setText(Util.getResourceString(SHTMLPanel.getResources(), "searchWholeProject"));
        this.getContentPane().add(jpnlMain, BorderLayout.NORTH);
        jpnlBtn.add(jbtnFindNext, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
            GridBagConstraints.NONE, new Insets(4, 4, 0, 4), 0, 0));
        jpnlBtn.add(jbtnClose, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
            GridBagConstraints.NONE, new Insets(0, 4, 4, 4), 0, 0));
        jpnlBtn.add(jbtnReplace, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
            GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        jpnlBtn.add(jbtnCancel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
            GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        jpnlMain.add(jpnlFind, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0));
        jpnlMain.add(jpnlBtn, new GridBagConstraints(1, 0, 1, 2, 1.0, 1.0, GridBagConstraints.NORTHEAST,
            GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        jpnlFind.add(jcomboSearchTerm, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 12));
        jpnlFind.add(jLabel4, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
            GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        jpnlFind.add(jtfReplace, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 12));
        jpnlFind.add(jLabel3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
            GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        jpnlMain.add(jpnlOptions, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHWEST,
            GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

        jpnlOptions.add(jcbMatchCase, null);
        jpnlOptions.add(jcbMatchApprox, null);
        jpnlOptions.add(jcbWholeWords, null);
        jpnlOptions.add(jrbUp, null);
        jpnlOptions.add(jcbStartOnTop, null);
        jpnlOptions.add(jrbDown, null);
        jpnlOptions.add(jcbProject, null);
        bgSearchDirection.add(jrbUp);
        bgSearchDirection.add(jrbDown);
        
        // this is necessary so that the button fires on enter key press
        // (for continuing a search)ok, 
        getRootPane().setDefaultButton(jbtnFindNext);
    }

    /* ----------------- class fields ------------------------------ */
    /** result value for this dialog */
    private int result;
    /** mode of dialog */
    private int mode;
    /** JEditorPane containing the document to search in */
    private JEditorPane editor;
    /** Document to search in */
    private Document doc;
    /** search text from the document */
    private String searchText;
    /** search phrase to find */
    private String searchTerm;
    /** new phrase to replace the searched phrase with */
    
    /* search results for approximate matching */
    private List<PseudoDamerauLevenshtein.Alignment> currentApproximateMatches;
    private int currentApproximateMatchIndex;
    
    private String replacementText;
    /** last start position, the search phrase was found at in the document */
    private int lastPosition;
    /** two fields to correct position differences during replace operations */
    private int offset;
    private int replaceDiff;
    /** indicates if a find is already in progress */
    private boolean findInProgress = false;
    /** indicates the current operation */
    private int operation;
    /** choice of replace operation */
    private int replaceChoice;
    /** the listeners for FindReplaceEvents */
    private final Vector listeners = new Vector(0);
    /** separators for whole words only search */
    private static final char[] WORD_SEPARATORS = { ' ', '\t', '\n', '\r', '\f', '.', ',', ':', '-', '(', ')', '[',
            ']', '{', '}', '<', '>', '/', '|', '\\', '\'', '\"' };
    /** options for replacing */
    private static final Object[] replaceOptions = { Util.getResourceString(SHTMLPanel.getResources(), "replaceYes"),
            Util.getResourceString(SHTMLPanel.getResources(), "replaceNo"),
            Util.getResourceString(SHTMLPanel.getResources(), "replaceAll"),
            Util.getResourceString(SHTMLPanel.getResources(), "replaceDone") };
    /* Constants for method toggleState */
    public static final boolean STATE_LOCKED = false;
    public static final boolean STATE_UNLOCKED = true;
    /* Constants for replaceOptions */
    public static final int RO_YES = 0;
    public static final int RO_NO = 1;
    public static final int RO_ALL = 2;
    public static final int RO_DONE = 3;
    /* Constants for dialog mode */
    public static final int MODE_DOCUMENT = 1;
    public static final int MODE_PROJECT = 2;
    /* Constants for operation */
    public static final int OP_NONE = 0;
    public static final int OP_FIND = 1;
    public static final int OP_REPLACE = 2;
    /* ---- GUI elements start ---------*/
    private TitledBorder titledBorder1;
    private final JButton jbtnFindNext = new JButton();
    private final JCheckBox jcbStartOnTop = new JCheckBox();
    private final JRadioButton jrbDown = new JRadioButton();
    private final JCheckBox jcbWholeWords = new JCheckBox();
    private final JPanel jpnlBtn = new JPanel();
    private final JPanel jpnlOptions = new JPanel();
    private final JPanel jpnlFind = new JPanel();
    private final JTextField jtfReplace = new JTextField();
    private final JPanel jpnlMain = new JPanel();
    private final JRadioButton jrbUp = new JRadioButton();
    private final JComboBox jcomboSearchTerm = new JComboBox();
    private final JCheckBox jcbMatchCase = new JCheckBox();
    private final JCheckBox jcbMatchApprox = new JCheckBox();
    private final JLabel jLabel3 = new JLabel();
    private final JLabel jLabel4 = new JLabel();
    private final GridBagLayout gridBagLayout4 = new GridBagLayout();
    private final GridBagLayout gridBagLayout5 = new GridBagLayout();
    private final JButton jbtnClose = new JButton();
    private final GridBagLayout gridBagLayout6 = new GridBagLayout();
    private final GridLayout gridLayout2 = new GridLayout();
    private final JButton jbtnReplace = new JButton();
    private final JButton jbtnCancel = new JButton();
    private final JCheckBox jcbProject = new JCheckBox();
    /* ---- GUI elements end ---------*/
    
    public static synchronized void rememberSearchTerm(final String searchTerm, final JComboBox searchTermCombo)
    {
    	//System.out.format("rememberSearchTerm(%s)\n", searchTerm);
    	if (searchTerm.equals(""))
    		return;
    	
    	MutableComboBoxModel searchTermComboModel = (MutableComboBoxModel)searchTermCombo.getModel();
		
    	// remove this term from the history
    	if (searchTermHistory.contains(searchTerm))
    	{
    		searchTermHistory.remove(searchTerm);
    		searchTermComboModel.removeElement(searchTerm);
    	}
    	
    	// (re)insert at top of list
    	searchTermHistory.add(0, searchTerm);
    	searchTermComboModel.insertElementAt(searchTerm, 0);
		
    	searchTermCombo.setSelectedItem(searchTerm);
    }
    
    private final static List<String> searchTermHistory = new LinkedList<String>();
    private final static AtomicBoolean matchCaseSetting = new AtomicBoolean(false);
    private final static AtomicBoolean matchApproxSetting = new AtomicBoolean(false);
}
