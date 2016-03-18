/*
 * SimplyHTML, a word processor based on Java, HTML and CSS
 * Created on 10.09.2006
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

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.text.html.HTMLDocument;

/**
 * Class for using SimplyHTML as as component  
 * 
 * @author Dimitri Polivaev
 * 14.01.2007
 */
public abstract class SHTMLPanel extends JPanel implements SHTMLPrefsChangeListener {
    SHTMLPanel(final LayoutManager layout) {
        super(layout);
    }
    
    public void shtmlPrefChanged(String propertyName, String newValue, String oldValue)
    {
    	//System.out.format("SHTMLPanel.shtmlPrefChanged(%s, %s, %s)\n",
    	// propertyName, newValue, oldValue);
    	if (propertyName.equals("default_paste_mode"))
    	{
    		((SHTMLEditorKitActions.SHTMLEditPasteOtherAction)getAction("pasteOther"))
    		.updateActionName(SHTMLEditorPane.PasteMode.valueOf(newValue).invert());
    	}
    }

    public static SHTMLPanel createSHTMLPanel() {
        return new SHTMLPanelSingleDocImpl();
    }

    public abstract String getDocumentText();

    public abstract boolean needsSaving();

    public abstract void setContentPanePreferredSize(Dimension dimension);

    public abstract void setCurrentDocumentContent(String sText);

    public static void setResources(final TextResources resources) {
        SHTMLPanelImpl.setTextResources(resources);
    }
    
    public static void setActionBuilder(final ActionBuilder ab){
    	SHTMLPanelImpl.setActionBuilder(ab);
    }

    public abstract HTMLDocument getDocument();

    public abstract JEditorPane getEditorPane();

    public abstract JEditorPane getSourceEditorPane();

    public static TextResources getResources() {
        return SHTMLPanelImpl.getResources();
    }

    abstract public int getCaretPosition();

    public abstract JMenuBar getMenuBar();

    public abstract JEditorPane getMostRecentFocusOwner();

    public abstract Action getAction(String actionName);
    public abstract void addAction(String text, Action action);

    /**
     * Returns a new menu item for a named action of SimplyHTML. (Can be used for building custom
     * popup menu, or for invoking the action externally in another way.)
     */
    public abstract JMenuItem newActionMenuItem(String actionName);

    /**
     * Switches between the rich text view and the source view, given
     * tabbed panes are not used. Has no corresponding action; calling
     * this method is up to the caller application of SimplyHTML.
     */
    public abstract void switchViews();

    /**
     * Sets the handler for the Open Hyperlink action. SimplyHTML itself has
     * no ability to open hyperlinks, so it forwards the action to the caller
     * application.
     */
    public abstract void setOpenHyperlinkHandler(ActionListener openHyperlinkHandler);
}
