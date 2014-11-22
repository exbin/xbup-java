/*
 * SimplyHTML, a word processor based on Java, HTML and CSS
 * Copyright (C) 2006 Ulrich Hilger, Dimitri Polivaev
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

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.undo.*;
import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import com.lightdev.app.shtm.SHTMLEditorKitActions.SetStyleAction;
import com.lightdev.app.shtm.SHTMLEditorKitActions.SetTagAction;
import java.util.prefs.*;

/**
 * Main component of application SimplyHTML.
 *
 * <p>This class constructs the main panel and all of its GUI elements
 * such as menus, etc.</p>
 *
 * <p>It defines a set of inner classes creating actions which can be
 * connected to menus, buttons or instantiated individually.</p>
 *
 * @author Ulrich Hilger
 * @author Dimitri Polivaev
 * @author Light Development
 * @author <a href="http://www.lightdev.com">http://www.lightdev.com</a>
 * @author <a href="mailto:info@lightdev.com">info@lightdev.com</a>
 * @author published under the terms and conditions of the
 *      GNU General Public License,
 *      for details see file gpl.txt in the distribution
 *      package of this software
 *
 *
 */
public class SHTMLPanelImpl extends SHTMLPanel implements CaretListener {
    //private int renderMode = SHTMLEditorKit.RENDER_MODE_JAVA;
    /* some public constants */
    public static final String APP_TEMP_DIR = "temp";
    public static final String IMAGE_DIR = "images";
    public static final String ACTION_SELECTED_KEY = "selected";
    public static final String ACTION_SELECTED = "true";
    public static final String ACTION_UNSELECTED = "false";
    public static final String FILE_LAST_OPEN = "lastOpenFileName";
    public static final String FILE_LAST_SAVE = "lastSaveFileName";
    /** single instance of a dynamic resource for use by all */
    public DynamicResource dynRes = new DynamicResource();
    /** SimplyHTML's main resource bundle (plug-ins use their own) */
    private static TextResources textResources = null;

    public static TextResources getResources() {
        if (textResources == null) {
            textResources = SHTMLPanelImpl.readDefaultResources();
        }
        return textResources;
    }

    /** the plug-in manager of SimplyHTML */
    public static PluginManager pluginManager; // = new PluginManager(mainFrame);
    protected ActionListener openHyperlinkHandler = null;

    public static void setTextResources(final TextResources textResources) {
        if (SHTMLPanelImpl.textResources != null) {
            return;
        }
        SHTMLPanelImpl.textResources = textResources != null ? textResources : SHTMLPanelImpl.readDefaultResources();
    }

    private static TextResources readDefaultResources() {
        try {
            final String propsLoc = "com/lightdev/app/shtm/resources/SimplyHTML_common.properties";
            final URL defaultPropsURL = ClassLoader.getSystemResource(propsLoc);
            final Properties props = new Properties();
            InputStream in = null;
            in = defaultPropsURL.openStream();
            props.load(in);
            in.close();
            final ResourceBundle resourceBundle = ResourceBundle.getBundle(
                "com.lightdev.app.shtm.resources.SimplyHTML", Locale.getDefault());
            return new DefaultTextResources(resourceBundle, props);
        }
        catch (final Exception ex) {
            Util.errMsg(null, "resources not found", ex);
            return null;
        }
    }

    private final SHTMLMenuBar menuBar;
    /** currently active DocumentPane */
    private DocumentPane documentPane;
    /** currently active SHTMLEditorPane */
    private SHTMLEditorPane editorPane;
    /** currently active SHTMLDocument */
    protected SHTMLDocument doc;
    /** tool bar for formatting commands */
    private JToolBar formatToolBar;
    /** tool bar for formatting commands */
    private JToolBar paraToolBar;
    /** plugin menu ID */
    public final String pluginMenuId = "plugin";
    /** help menu ID */
    public final String helpMenuId = "help";
    /** id in TextResources for a relative path to an empty menu icon */
    private final String emptyIcon = "emptyIcon";
    /** watch for repeated key events */
    private final RepeatKeyWatcher rkw = new RepeatKeyWatcher(40);
    /** counter for newly created documents */
    int newDocCounter = 0;
    /** reference to applicatin temp directory */
    private static File appTempDir;
	private static ActionBuilder actionBuilder;
    /** tool bar selector for certain tags */
    private TagSelector tagSelector;
    /** panel for plug-in display */
    SplitPanel splitPanel;
    /** indicates, whether document activation shall be handled */
    boolean ignoreActivateDoc = false;
    private final JPopupMenu editorPopup;
    /**
     * action names
     *
     * these have to correspond with the keys in the
     * resource bundle to allow for dynamic
     * menu creation and control
     */
    public static final String exitAction = "exit";
    public static final String undoAction = "undo";
    public static final String redoAction = "redo";
    public static final String cutAction = "cut";
    public static final String copyAction = "copy";
    public static final String pasteAction = "paste";
    public static final String pasteOtherAction ="pasteOther";
    public static final String selectAllAction = "selectAll";
    public static final String clearFormatAction = "clearFormat";
    public static final String fontAction = "font";
    public static final String fontFamilyAction = "fontFamily";
    public static final String fontSizeAction = "fontSize";
    public static final String fontBoldAction = "fontBold";
    public static final String fontStrikethroughAction = "fontStrikethrough";
    public static final String fontItalicAction = "fontItalic";
    public static final String fontUnderlineAction = "fontUnderline";
    public static final String fontColorAction = "fontColor";
    public static final String helpTopicsAction = "helpTopics";
    public static final String aboutAction = "about";
    public static final String gcAction = "gc";
    public static final String elemTreeAction = "elemTree";
    public static final String testAction = "test";
    public static final String insertTableAction = "insertTable";
    public static final String formatTableAction = "formatTable";
    public static final String toggleTableHeaderCellAction = "toggleTableHeaderCell";
    public static final String insertTableColAction = "insertTableCol";
    public static final String insertTableRowAction = "insertTableRow";
    public static final String insertTableRowHeaderAction = "insertTableRowHeader";
    public static final String appendTableRowAction = "appendTableRow";
    public static final String appendTableColAction = "appendTableCol";
    public static final String deleteTableRowAction = "deleteTableRow";
    public static final String deleteTableColAction = "deleteTableCol";
    public static final String nextTableCellAction = "nextTableCell";
    public static final String prevTableCellAction = "prevTableCell";
    public static final String moveTableRowUpAction = "moveTableRowUp";
    public static final String moveTableColumnLeftAction = "moveTableColumnLeft";
    public static final String moveTableColumnRightAction = "moveTableColumnRight";
    public static final String moveTableRowDownAction = "moveTableRowDown";
    //public static final String nextCellAction = "nextCell";
    //public static final String prevCellAction = "prevCell";
    public static final String toggleBulletsAction = "toggleBullets";
    public static final String toggleNumbersAction = "toggleNumbers";
    public static final String formatListAction = "formatList";
    public static final String editPrefsAction = "editPrefs";
    public static final String insertImageAction = "insertImage";
    public static final String formatImageAction = "formatImage";
    public static final String formatParaAction = "formatPara";
    public static final String editNamedStyleAction = "editNamedStyle";
    public static final String paraAlignLeftAction = "paraAlignLeft";
    public static final String paraAlignCenterAction = "paraAlignCenter";
    public static final String paraAlignRightAction = "paraAlignRight";
    public static final String insertLinkAction = "insertLink";
    public static final String editLinkAction = "editLink";
    public static final String openLinkAction = "openLink";
    public static final String setTagAction = "setTag";
    public static final String editAnchorsAction = "editAnchors";
    public static final String saveAllAction = "saveAll";
    public static final String documentTitleAction = "documentTitle";
    public static final String setDefaultStyleRefAction = "setDefaultStyleRef";
    public static final String findReplaceAction = "findReplace";
    public static final String setStyleAction = "setStyle";
    public static final String formatAsCodeAction = "formatAsCode";
    public static final String printAction = "print";

    public static SHTMLPanelImpl getOwnerSHTMLPanel(Component c) {
        for (;;) {
            if (c == null) {
                return null;
            }
            if (c instanceof SHTMLPanelImpl) {
                return (SHTMLPanelImpl) c;
            }
            c = c.getParent();
        }
    }

    /** construct a new main application frame */
    SHTMLPanelImpl() {
        super(new BorderLayout());
        SplashScreen.showInstance();
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        initActions();
        if(actionBuilder != null)
        	actionBuilder.initActions(this);
        menuBar = dynRes.createMenubar(textResources, "menubar");
        editorPopup = dynRes.createPopupMenu(textResources, "popup");
        setJMenuBar(menuBar);
        customizeFrame();
        initAppTempDir();
        initPlugins();
        initDocumentPane();
        updateActions();
        initJavaHelp();
        SplashScreen.hideInstance();
    }

    private void setJMenuBar(final JMenuBar bar) {
        add(bar, BorderLayout.NORTH);
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#processKeyBinding(javax.swing.KeyStroke, java.awt.event.KeyEvent, int, boolean)
     */
    protected boolean processKeyBinding(final KeyStroke ks, final KeyEvent e, final int condition, final boolean pressed) {
        if (super.processKeyBinding(ks, e, condition, pressed)) {
            return true;
        }
        return menuBar.handleKeyBinding(ks, e, condition, pressed);
    }

    public JMenuItem newActionMenuItem(final String actionName) {
        return dynRes.createMenuItem(SHTMLPanelImpl.getResources(), actionName);
    }

    public Action getAction(final String actionName) {
        return dynRes.getAction(actionName);
    }

    /**
       * get the DynamicResource used in this instance of FrmMain
       *
       * @return the DynamicResource
       */
    DynamicResource getDynRes() {
        return dynRes;
    }

    /**
     * get the temporary directory of SimplyHTML
     *
     * @return the temp dir
     */
    static File getAppTempDir() {
        return appTempDir;
    }

    /**
     * get the file object for the document shown in the currently open DocumentPane
     *
     * @return the document file
     */
    File getCurrentFile() {
        File file = null;
        final URL url = getDocumentPane().getSource();
        if (url != null) {
            file = new File(url.getFile());
        }
        return file;
    }

    /**
     * get the name of the file for the document shown in the currently open DocumentPane
     *
     * @return the document name
     */
    String getCurrentDocName() {
        return getDocumentPane().getDocumentName();
    }

    /**
     * Convenience method for obtaining the document text
     * @return returns the document text as string.
     */
    public String getDocumentText() {
        return getDocumentPane().getDocumentText();
    }

    Document getCurrentDocument() {
        return getDocumentPane().getDocument();
    }

    /**
     * indicates whether or not the document needs to be saved.
     *
     * @return  true, if changes need to be saved
     */
    public boolean needsSaving() {
        return getDocumentPane().needsSaving();
    }

    /**
     * Convenience method for clearing out the UndoManager
     */
    void purgeUndos() {
        if (undo != null) {
            undo.discardAllEdits();
            dynRes.getAction(undoAction).putValue("enabled", Boolean.FALSE);
            dynRes.getAction(redoAction).putValue("enabled", Boolean.FALSE);
            updateFormatControls();
        }
    }

    /**
     * Convenience method for setting the document text
     */
    public void setCurrentDocumentContent(final String sText) {
        getDocumentPane().setDocumentText(sText);
        purgeUndos();
    }

    public void setContentPanePreferredSize(final Dimension prefSize) {
        getDocumentPane().setContentPanePreferredSize(prefSize);
    }

    /**
     * @return returns the currently used ExtendedHTMLDocument Object
     */
    public HTMLDocument getDocument() {
        return doc;
    }

    /**
     * get the DocumentPane object that is currently active
     *
     * @return the active DocumentPane
     */
    DocumentPane getCurrentDocumentPane() {
        return getDocumentPane();
    }

    /**
     * add a DocumentPaneListener from the currently active DocumentPane (if any)
     */
    void addDocumentPaneListener(final DocumentPane.DocumentPaneListener listener) {
        if (getDocumentPane() != null) {
            //System.out.println("FrmMain.addDocumentPaneListener documentPane.source=" + documentPane.getSource());
            getDocumentPane().addDocumentPaneListener(listener);
        }
        else {
            //System.out.println("FrmMain.addDocumentPaneListener documentPane is null, did not add");
        }
    }

    /**
     * remove a DocumentPaneListener from the currently active DocumentPane (if any)
     */
    void removeDocumentPaneListener(final DocumentPane.DocumentPaneListener listener) {
        if (getDocumentPane() != null) {
            getDocumentPane().removeDocumentPaneListener(listener);
        }
    }

    /**
     * initialize SimplyHTML's temporary directory
     */
    private void initAppTempDir() {
        appTempDir = new File(System.getProperty("user.home") + File.separator + "." + FrmMain.APP_NAME.toLowerCase()
                + File.separator + APP_TEMP_DIR + File.separator);
    }

    /**
     * find plug-ins and load them accordingly,
     * i.e. display / dock components and add
     * plug-in menus.
     */
    void initPlugins() {
        pluginManager = new PluginManager(this);
        final JMenu pMenu = dynRes.getMenu(pluginMenuId);
        final JMenu hMenu;
        if (pMenu != null) {
            final Container contentPane = SHTMLPanelImpl.this;
            pluginManager.loadPlugins();
            final Enumeration plugins = pluginManager.plugins();
            SHTMLPlugin pi;
            final JComponent pc;
            final JMenuItem pluginMenu;
            final JMenuItem helpMenu;
            while (plugins.hasMoreElements()) {
                pi = (SHTMLPlugin) plugins.nextElement();
                if (pi.isActive()) {
                    refreshPluginDisplay(pi);
                }
            }
        }
        adjustDividers();
    }

    /**
     * adjust the divider sizes of SimplyHTML's SplitPanel
     * according to visibility
     */
    public void adjustDividers() {
        splitPanel.adjustDividerSizes();
    }

    /**
     * watch for key events that are automatically repeated
     * due to the user holding down a key.
     *
     * <p>When a key is held down by the user, every keyPressed
     * event is followed by a keyTyped event and a keyReleased
     * event although the key is actually still down. I.e. it
     * can not be determined by a keyReleased event if a key
     * actually is released, which is why this implementation
     * is necessary.</p>
     */
    class RepeatKeyWatcher implements KeyListener {
        /** timer for handling keyReleased events */
        private final java.util.Timer releaseTimer = new java.util.Timer();
        /** the next scheduled task for a keyReleased event */
        private ReleaseTask nextTask;
        /** time of the last keyPressed event */
        private long lastWhen = 0;
        /** time of the current KeyEvent */
        private long when;
        /** delay to distinguish between single and repeated events */
        private final long delay;
        /** indicates whether or not a KeyEvent currently occurs repeatedly */
        private boolean repeating = false;

        /**
         * construct a <code>RepeatKeyWatcher</code>
         *
         * @param delay  the delay in milliseconds until a
         * keyReleased event should be handled
         */
        RepeatKeyWatcher(final long delay) {
            super();
            this.delay = delay;
        }

        /**
         * handle a keyPressed event by cancelling the previous
         * release task (if any) and indicating repeated key press
         * as applicable.
         */
        public void keyPressed(final KeyEvent e) {
            if (nextTask != null) {
                nextTask.cancel();
            }
            when = e.getWhen();
            if ((when - lastWhen) <= delay) {
                repeating = true;
            }
            else {
                repeating = false;
            }
            lastWhen = when;
        }

        /**
         * handle a keyReleased event by scheduling a
         * <code>ReleaseTask</code>.
         */
        public void keyReleased(final KeyEvent e) {
            nextTask = new ReleaseTask();
            releaseTimer.schedule(nextTask, delay);
        }

        public void keyTyped(final KeyEvent e) {
        }

        /**
         * indicate whether or not a key is being held down
         *
         * @return true if a key is being held down, false if not
         */
        boolean isRepeating() {
            return repeating;
        }

        /**
         * Task to be executed when a key is released
         */
        private class ReleaseTask extends TimerTask implements Runnable {
            public void run() {
                if (EventQueue.isDispatchThread()) {
                    repeating = false;
                    updateFormatControls();
                }
                else {
                    try {
                        EventQueue.invokeAndWait(this);
                    }
                    catch (final InterruptedException e) {
                    }
                    catch (final InvocationTargetException e) {
                    }
                }
            }
        }
    }

    public void clearDockPanels() {
        splitPanel.removeAllOuterPanels();
    }

    /**
     * refresh the display for a given plug-in
     *
     * @param pi  the plug-in to refresh
     */
    public void refreshPluginDisplay(final SHTMLPlugin pi) {
        final JMenu pMenu = dynRes.getMenu(pluginMenuId);
        final JMenu hMenu = dynRes.getMenu(helpMenuId);
        final JMenuItem pluginMenu = pi.getPluginMenu();
        final JMenuItem helpMenu = pi.getHelpMenu();
        JTabbedPane p = null;
        final Preferences prefs;
        if (pi.isActive()) {
            final JComponent pc = pi.getComponent();
            if (pc != null) {
                int panelNo = SplitPanel.WEST;
                double loc = 0.3;
                switch (pi.getDockLocation()) {
                    case SHTMLPlugin.DOCK_LOCATION_LEFT:
                        break;
                    case SHTMLPlugin.DOCK_LOCATION_RIGHT:
                        panelNo = SplitPanel.EAST;
                        loc = 0.7;
                        break;
                    case SHTMLPlugin.DOCK_LOCATION_BOTTOM:
                        panelNo = SplitPanel.SOUTH;
                        loc = 0.7;
                        break;
                    case SHTMLPlugin.DOCK_LOCATION_TOP:
                        panelNo = SplitPanel.NORTH;
                        break;
                }
                p = (JTabbedPane) splitPanel.getPanel(panelNo);
                p.setVisible(true);
                p.add(pi.getGUIName(), pc);
                if (((panelNo == SplitPanel.WEST) && splitPanel.getDivLoc(panelNo) < this.getWidth() / 10)
                        || ((panelNo == SplitPanel.NORTH) && splitPanel.getDivLoc(panelNo) < this.getHeight() / 10)
                        || ((panelNo == SplitPanel.EAST) && splitPanel.getDivLoc(panelNo) > this.getWidth()
                                - (this.getWidth() / 10))
                        || ((panelNo == SplitPanel.SOUTH) && splitPanel.getDivLoc(panelNo) > this.getHeight()
                                - (this.getHeight() / 10))) {
                    splitPanel.setDivLoc(panelNo, loc);
                }
            }
            if (pluginMenu != null) {
                Icon menuIcon = pluginMenu.getIcon();
                if (menuIcon == null) {
                    final URL url = DynamicResource.getResource(textResources, emptyIcon);
                    if (url != null) {
                        menuIcon = new ImageIcon(url);
                        pluginMenu.setIcon(new ImageIcon(url));
                    }
                }
                pMenu.add(pluginMenu);
            }
            if (helpMenu != null) {
                //System.out.println("FrmMain.refreshPluginDisplay insert helpMenu");
                if (helpMenu.getSubElements().length > 0) {
                    Icon menuIcon = helpMenu.getIcon();
                    if (menuIcon == null) {
                        final URL url = DynamicResource.getResource(textResources, emptyIcon);
                        if (url != null) {
                            menuIcon = new ImageIcon(url);
                            helpMenu.setIcon(new ImageIcon(url));
                        }
                    }
                }
                hMenu.insert(helpMenu, hMenu.getItemCount() - 2);
            }
            SwingUtilities.invokeLater(new PluginInfo(pi));
        }
        else {
            if (pluginMenu != null) {
                pMenu.remove(pluginMenu);
            }
            if (helpMenu != null) {
                hMenu.remove(helpMenu);
            }
        }
    }

    class PluginInfo implements Runnable {
        SHTMLPlugin pi;

        PluginInfo(final SHTMLPlugin pi) {
            this.pi = pi;
        }

        public void run() {
            pi.showInitialInfo();
        }
    }

    /**
     * get a <code>HelpBroker</code> for our application,
     * store it for later use and connect it to the help menu.
     */
    private void initJavaHelp() {
        try {
            final JMenuItem mi = dynRes.getMenuItem(helpTopicsAction);
            if (mi == null) {
                return;
            }
            SHTMLHelpBroker.initJavaHelpItem(mi, "item15");
        }
        catch (final Throwable e) {
            System.err.println("Simply HTML : Warning : loading help failed.");
            // --Dan
            //Util.errMsg(this,
            //            Util.getResourceString("helpNotFoundError"),
            //            e);
        }
    }

    protected void initDocumentPane() {
        //TODO
    }

    /**
    * instantiate Actions and put them into the commands
    * Hashtable for later use along with their action commands.
    *
    * This is hard coded as Actions need to be instantiated
    * hard coded anyway, so we do the storage in <code>commands</code>
    * right away.
    */
    protected void initActions() {
        addAction(setDefaultStyleRefAction, new SHTMLEditorKitActions.SetDefaultStyleRefAction(this));
        addAction(documentTitleAction, new SHTMLEditorKitActions.DocumentTitleAction(this));
        addAction(editAnchorsAction, new SHTMLEditorKitActions.EditAnchorsAction(this));
        addAction(setTagAction, new SHTMLEditorKitActions.SetTagAction(this));
        addAction(formatAsCodeAction, new SHTMLEditorKitActions.SetTagAction(this, "code"));
        addAction(editLinkAction, new SHTMLEditorKitActions.EditLinkAction(this));
        addAction(openLinkAction, new SHTMLEditorKitActions.OpenLinkAction(this));
        addAction(prevTableCellAction, new SHTMLEditorKitActions.PrevTableCellAction(this));
        addAction(nextTableCellAction, new SHTMLEditorKitActions.NextTableCellAction(this));
        addAction(editNamedStyleAction, new SHTMLEditorKitActions.EditNamedStyleAction(this));
        addAction(clearFormatAction, new SHTMLEditorKitActions.ClearFormatAction(this));
        addAction(formatParaAction, new SHTMLEditorKitActions.FormatParaAction(this));
        addAction(formatImageAction, new SHTMLEditorKitActions.FormatImageAction(this));
        addAction(insertImageAction, new SHTMLEditorKitActions.InsertImageAction(this));
        addAction(editPrefsAction, new SHTMLEditorKitActions.SHTMLEditPrefsAction(this));
        addAction(toggleBulletsAction, new SHTMLEditorKitActions.ToggleListAction(this, toggleBulletsAction,
            HTML.Tag.UL));
        addAction(toggleNumbersAction, new SHTMLEditorKitActions.ToggleListAction(this, toggleNumbersAction,
            HTML.Tag.OL));
        addAction(formatListAction, new SHTMLEditorKitActions.FormatListAction(this));
        addAction(ManagePluginsAction.managePluginsAction, new ManagePluginsAction());
        addAction(elemTreeAction, new SHTMLEditorKitActions.ShowElementTreeAction(this));
        addAction(gcAction, new SHTMLEditorKitActions.GarbageCollectionAction(this));
        addAction(undoAction, new SHTMLEditorKitActions.UndoAction(this));
        addAction(redoAction, new SHTMLEditorKitActions.RedoAction(this));
        addAction(cutAction, new SHTMLEditorKitActions.SHTMLEditCutAction(this));
        addAction(copyAction, new SHTMLEditorKitActions.SHTMLEditCopyAction(this));
        addAction(pasteAction, new SHTMLEditorKitActions.SHTMLEditPasteAction(this));
        addAction(pasteOtherAction, new SHTMLEditorKitActions.SHTMLEditPasteOtherAction(this));
        addAction(selectAllAction, new SHTMLEditorKitActions.SHTMLEditSelectAllAction(this));
        addAction(aboutAction, new SHTMLEditorKitActions.SHTMLHelpAppInfoAction(this));
        addAction(fontAction, new SHTMLEditorKitActions.FontAction(this));
        addAction(fontFamilyAction, new SHTMLEditorKitActions.FontFamilyAction(this));
        addAction(fontSizeAction, new SHTMLEditorKitActions.FontSizeAction(this));
        addAction(insertTableAction, new SHTMLEditorKitActions.InsertTableAction(this));
        addAction(insertTableRowAction, new SHTMLEditorKitActions.InsertTableRowAction(this, null,
            insertTableRowAction));
        addAction(insertTableRowHeaderAction, new SHTMLEditorKitActions.InsertTableRowAction(this, "th",
            insertTableRowHeaderAction));
        addAction(insertTableColAction, new SHTMLEditorKitActions.InsertTableColAction(this));
        addAction(appendTableColAction, new SHTMLEditorKitActions.AppendTableColAction(this));
        addAction(appendTableRowAction, new SHTMLEditorKitActions.AppendTableRowAction(this));
        addAction(deleteTableRowAction, new SHTMLEditorKitActions.DeleteTableRowAction(this));
        addAction(deleteTableColAction, new SHTMLEditorKitActions.DeleteTableColAction(this));
        addAction(moveTableRowUpAction, new SHTMLEditorKitActions.MoveTableRowUpAction(this));
        addAction(moveTableRowDownAction, new SHTMLEditorKitActions.MoveTableRowDownAction(this));
        addAction(moveTableColumnLeftAction, new SHTMLEditorKitActions.MoveTableColumnLeftAction(this));
        addAction(moveTableColumnRightAction, new SHTMLEditorKitActions.MoveTableColumnRightAction(this));
        addAction(formatTableAction, new SHTMLEditorKitActions.FormatTableAction(this));
        addAction(toggleTableHeaderCellAction, new SHTMLEditorKitActions.ToggleTableHeaderCellAction(this));
        addAction(fontBoldAction, new SHTMLEditorKitActions.BoldAction(this));
        addAction(fontItalicAction, new SHTMLEditorKitActions.ItalicAction(this));
        addAction(fontUnderlineAction, new SHTMLEditorKitActions.UnderlineAction(this));
        addAction(fontColorAction, new SHTMLEditorKitActions.FontColorAction(this));
        addAction(fontStrikethroughAction, new SHTMLEditorKitActions.ApplyCSSAttributeAction(this,
            fontStrikethroughAction, CSS.Attribute.TEXT_DECORATION, "line-through", false));
        addAction(paraAlignLeftAction, new SHTMLEditorKitActions.ApplyCSSAttributeAction(this,
            paraAlignLeftAction, CSS.Attribute.TEXT_ALIGN, Util.CSS_ATTRIBUTE_ALIGN_LEFT, true));
        addAction(paraAlignCenterAction, new SHTMLEditorKitActions.ApplyCSSAttributeAction(this,
            paraAlignCenterAction, CSS.Attribute.TEXT_ALIGN, Util.CSS_ATTRIBUTE_ALIGN_CENTER, true));
        addAction(paraAlignRightAction, new SHTMLEditorKitActions.ApplyCSSAttributeAction(this,
            paraAlignRightAction, CSS.Attribute.TEXT_ALIGN, Util.CSS_ATTRIBUTE_ALIGN_RIGHT, true));
        addAction(testAction, new SHTMLEditorKitActions.SHTMLTestAction(this));
        addAction(printAction, new SHTMLEditorKitActions.PrintAction(this));
    }

    public static void setActionBuilder(final ActionBuilder ab){
    	SHTMLPanelImpl.actionBuilder = ab;
    }

    public void addAction(String text, Action action) {
		dynRes.addAction(text, action);
		
	}

	/**
     * update all actions
     */
    public void updateActions() {
        Action action;
        final Enumeration actions = dynRes.getActions();
        while (actions.hasMoreElements()) {
            action = (Action) actions.nextElement();
            if (action instanceof SHTMLAction) {
                ((SHTMLAction) action).update();
            }
        }
    }

    /** customize the frame to our needs */
    protected void customizeFrame() {
        splitPanel = new SplitPanel();
        for (int i = 0; i < 4; i++) {
            final JTabbedPane p = new JTabbedPane();
            p.setVisible(false);
            splitPanel.addComponent(p, i);
        }
        final JPanel toolBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            public Dimension getPreferredSize() {
                final int maxWidth = splitPanel.getWidth();
                int height = 0;
                int rowHeight = 0;
                int width = 0;
                for (int i = 0; i < getComponentCount(); i++) {
                    final Component component = getComponent(i);
                    final Dimension compPreferredSize = component.getPreferredSize();
                    if (maxWidth < compPreferredSize.width) {
                        height += rowHeight + compPreferredSize.height;
                        rowHeight = 0;
                        width = 0;
                    }
                    else if (maxWidth < width + compPreferredSize.width) {
                        height += rowHeight;
                        rowHeight = compPreferredSize.height;
                        width = compPreferredSize.width;
                    }
                    else {
                        rowHeight = Math.max(rowHeight, compPreferredSize.height);
                        width += compPreferredSize.width;
                    }
                }
                height += rowHeight;
                return new Dimension(maxWidth, height);
            }
        };
        final Container contentPane = new JPanel() {
            /**
            * 
            */
            private static final long serialVersionUID = 1L;

            public Dimension getPreferredSize() {
                final Dimension splitPreferredSize = splitPanel.getPreferredSize();
                final Dimension toolbaPreferredSize = toolBarPanel.getPreferredSize();
                return new Dimension(splitPreferredSize.width, splitPreferredSize.height + toolbaPreferredSize.height);
            }
        };
        contentPane.setLayout(new BorderLayout());
        toolBarPanel.add(createToolBar("toolBar"));
        formatToolBar = createToolBar("formatToolBar");
        paraToolBar = createToolBar("paraToolBar");
        toolBarPanel.add(formatToolBar);
        toolBarPanel.add(paraToolBar);
        if (Util.getPreference("show_toolbars", "true").equalsIgnoreCase("true")) {
            contentPane.add(toolBarPanel, BorderLayout.NORTH);
        }
        //contentPane.add(workPanel, BorderLayout.CENTER);
        contentPane.add(splitPanel, BorderLayout.CENTER);
        //contentPane.add(workPanel);
        add(contentPane, BorderLayout.CENTER);
        splitPanel.addComponentListener(new ComponentListener() {
            public void componentHidden(final ComponentEvent e) {
            }

            public void componentMoved(final ComponentEvent e) {
            }

            public void componentResized(final ComponentEvent e) {
                resizeToolbarPane(toolBarPanel);
            }

            public void componentShown(final ComponentEvent e) {
            }
        });
        toolBarPanel.addContainerListener(new ContainerListener() {
            public void componentAdded(final ContainerEvent e) {
                resizeToolbarPane(toolBarPanel);
            }

            public void componentRemoved(final ContainerEvent e) {
                resizeToolbarPane(toolBarPanel);
            }
        });
    }

    private void resizeToolbarPane(final JComponent toolBarPanel) {
        if (toolBarPanel.getPreferredSize().height != toolBarPanel.getHeight()) {
            toolBarPanel.revalidate();
        }
    }

    /**
     * Create a tool bar.  This reads the definition of a tool bar
     * from the associated resource file.
     *
     * @param nm  the name of the tool bar definition in the resource file
     *
     * @return the created tool bar
     */
    JToolBar createToolBar(final String nm) {
        final String[] itemKeys = Util.tokenize(Util.getResourceString(textResources, nm), " ");
        final JToolBar toolBar = new JToolBar();
        toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        for (int i = 0; i < itemKeys.length; i++) {
            /** special handling for separators */
            final String itemKey = itemKeys[i];
            createToolbarItem(toolBar, itemKey);
        }
        return toolBar;
    }

    protected void createToolbarItem(final JToolBar toolBar, final String itemKey) {
        final ToggleBorderListener tbl = new ToggleBorderListener();
        final Dimension buttonSize = new Dimension(24, 24);
        final Dimension comboBoxSize = new Dimension(300, 24);
        final Dimension separatorSize = new Dimension(3, 24);
        JSeparator separator;
        if (itemKey.equals(DynamicResource.menuSeparatorKey)) {
            separator = new JSeparator(JSeparator.VERTICAL);
            separator.setMaximumSize(separatorSize);
            toolBar.add(separator);
        }
        /**
         * special handling for list elements in the
         * tool bar
         */
        else if (itemKey.equalsIgnoreCase(fontFamilyAction)) {
            final FontFamilyPicker fontFamily = new FontFamilyPicker();
            fontFamily.setPreferredSize(new Dimension(180, 23));
            fontFamily.setAction(dynRes.getAction(fontFamilyAction));
            fontFamily.setMaximumSize(comboBoxSize);
            toolBar.add(fontFamily);
        }
        else if (itemKey.equalsIgnoreCase(fontSizeAction)) {
            final FontSizePicker fontSize = new FontSizePicker();
            fontSize.setPreferredSize(new Dimension(50, 23));
            fontSize.setAction(dynRes.getAction(fontSizeAction));
            fontSize.setMaximumSize(comboBoxSize);
            toolBar.add(fontSize);
        }
        else if (itemKey.equalsIgnoreCase(setTagAction)) {
            tagSelector = new TagSelector();
            tagSelector.setAction(dynRes.getAction(setTagAction));
            /*
             styleSelector = new StyleSelector(HTML.Attribute.CLASS);
             styleSelector.setPreferredSize(new Dimension(110, 23));
             styleSelector.setAction(dynRes.getAction(setStyleAction));
             styleSelector.setMaximumSize(comboBoxSize);
             jtpDocs.addChangeListener(styleSelector);
             */
            toolBar.add(tagSelector);
        }
        else {
            AbstractButton newButton;
            try {
                if (itemKey.equalsIgnoreCase(helpTopicsAction)) {
                    newButton = SHTMLHelpBroker.createHelpButton("item15");
                    final Icon icon = DynamicResource
                        .getIconForCommand(SHTMLPanelImpl.getResources(), helpTopicsAction);
                    newButton.setIcon(icon);
                    newButton.setToolTipText(Util.getResourceString(helpTopicsAction + DynamicResource.toolTipSuffix));
                    toolBar.add(newButton);
                }
                else {
                    /**
                     * special handling for JToggleButtons in the tool bar
                     */
                    final Action action = dynRes.getAction(itemKey);
                    if (action instanceof AttributeComponent) {
                        newButton = new JToggleButton("", (Icon) action.getValue(Action.SMALL_ICON));
                        newButton.addMouseListener(tbl);
                        newButton.setAction(action);
                        newButton.setText("");
                        //newButton.setActionCommand("");
                        newButton.setBorderPainted(false);
                        action.addPropertyChangeListener(new ToggleActionChangedListener((JToggleButton) newButton));
                        final Icon si = DynamicResource.getIconForName(textResources, action.getValue(Action.NAME)
                                + DynamicResource.selectedIconSuffix);
                        if (si != null) {
                            newButton.setSelectedIcon(si);
                        }
                        newButton.setMargin(new Insets(0, 0, 0, 0));
                        newButton.setIconTextGap(0);
                        newButton.setContentAreaFilled(false);
                        newButton.setHorizontalAlignment(SwingConstants.CENTER);
                        newButton.setVerticalAlignment(SwingConstants.CENTER);
                        toolBar.add(newButton);
                    }
                    /**
                     * this is the usual way to add tool bar buttons finally
                     */
                    else {
                        newButton = toolBar.add(action);
                    }
                }
                newButton.setMinimumSize(buttonSize);
                newButton.setPreferredSize(buttonSize);
                newButton.setMaximumSize(buttonSize);
                newButton.setFocusPainted(false);
                newButton.setRequestFocusEnabled(false);
                if (System.getProperty("os.name").equals("Mac OS X")) {
                    newButton.putClientProperty("JButton.buttonType", "segmented");
                    newButton.putClientProperty("JButton.segmentPosition", "middle");
                }
            }
            catch (final Exception ex) {
            }
            catch (final java.lang.NoClassDefFoundError e) {
            } //When one of the help components is not there
        }
    }

    /**
     * displays or removes an etched border around JToggleButtons
    * this listener is registered with.
    */
    private class ToggleBorderListener implements MouseListener {
        private final EtchedBorder border = new EtchedBorder(EtchedBorder.LOWERED);
        private JToggleButton button;

        public void mouseClicked(final MouseEvent e) {
        }

        public void mouseEntered(final MouseEvent e) {
            final Object src = e.getSource();
            if (src instanceof JToggleButton) {
                button = (JToggleButton) src;
                if (button.isEnabled()) {
                    ((JToggleButton) src).setBorder(border);
                }
            }
        }

        public void mouseExited(final MouseEvent e) {
            final Object src = e.getSource();
            if (src instanceof JToggleButton) {
                ((JToggleButton) src).setBorder(null);
            }
        }

        public void mousePressed(final MouseEvent e) {
        }

        public void mouseReleased(final MouseEvent e) {
        }
    }

    /**
     * register FrmMain as an object which has interest
     * in events from a given document pane
     */
    protected void registerDocument() {
        doc.addUndoableEditListener(undoHandler);
        getSHTMLEditorPane().addCaretListener(this);
        getSHTMLEditorPane().addKeyListener(rkw);
    }

    /**
     * remove FrmMain as a registered object from a given
     * document pane and its components
     *
     * remove all plug-ins owned by this FrmMain from
     * SimplyHTML objects too
     */
    protected void unregisterDocument() {
        getSHTMLEditorPane().removeCaretListener(this);
        getSHTMLEditorPane().removeKeyListener(rkw);
        if (doc != null) {
            doc.removeUndoableEditListener(undoHandler);
        }
        getDocumentPane().removeAllListeners(); // for plug-in removal from any documentPane that is about to close
        //System.out.println("FrmMain unregister document documentPane.name=" + documentPane.getDocumentName());
    }

    /**
     * save a document and catch possible errors
     *
     * this is shared by save and saveAs so we put it here to avoid redundancy
     *
     * @param documentPane  the document pane containing the document to save
     */
    void doSave(final DocumentPane documentPane) {
    try {
      documentPane.saveDocument();
    }
    /**
     * this exception should never happen as the menu allows to save a
     * document only if a name has been set. For new documents, whose
     * name is not set, only save as is enabled anyway.
     *
     * Just in case this is changed without remembering why it was designed
     * that way, we catch the exception here.
     */
    catch(DocNameMissingException e) {
      Util.errMsg(this, Util.getResourceString(textResources, "docNameMissingError"), e);
    }
  }

    public boolean isHtmlEditorActive() {
        return getDocumentPane() != null && getDocumentPane().getSelectedTab() == DocumentPane.VIEW_TAB_HTML;
    }

    /**
     * get action properties from the associated resource bundle
     *
     * @param action the action to apply properties to
     * @param cmd the name of the action to get properties for
     */
    public static void getActionProperties(final Action action, final String cmd) {
        final Icon icon = DynamicResource.getIconForCommand(textResources, cmd);
        if (icon != null) {
            action.putValue(Action.SMALL_ICON, icon);
        }
        /*else {
          action.putValue(Action.SMALL_ICON, emptyIcon);
        }*/
        final String name = Util.getResourceString(textResources, cmd + DynamicResource.labelSuffix);
        if (name != null) {
            action.putValue(Action.NAME, name);
        }
        final String toolTip = Util.getResourceString(textResources, cmd + DynamicResource.toolTipSuffix);
        if (toolTip != null) {
            action.putValue(Action.SHORT_DESCRIPTION, toolTip);
        }
        
    }

    /* ---------- undo/redo implementation ----------------------- */
    /** Listener for edits on a document. */
    private final UndoableEditListener undoHandler = new UndoHandler();
    /** UndoManager that we add edits to. */
    private UndoManager undo = new UndoManager();

    /** inner class for handling undoable edit events */
    class UndoHandler implements UndoableEditListener {
        /**
         * Messaged when the Document has created an edit, the edit is
         * added to <code>undo</code>, an instance of UndoManager.
         */
        public void undoableEditHappened(final UndoableEditEvent e) {
            // ignore all events happened when the html source code pane is open
            if (getCurrentDocumentPane().getSelectedTab() != DocumentPane.VIEW_TAB_LAYOUT) {
                return;
            }
            getUndo().addEdit(e.getEdit());
        }
    }

    /**
     * caret listener implementation to track format changes
     */
    public void caretUpdate(final CaretEvent e) {
        if (!rkw.isRepeating()) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    updateFormatControls();
                }
            });
        }
    }

    /**
     * update any controls that relate to formats at the
     * current caret position
     */
    void updateFormatControls() {
        updateAToolBar(formatToolBar);
        updateAToolBar(paraToolBar);
        if (tagSelector != null) {
            final SetTagAction sta = (SetTagAction) tagSelector.getAction();
            sta.setIgnoreActions(true);
            final Element e = doc.getParagraphElement(getSHTMLEditorPane().getCaretPosition());
            tagSelector.setSelectedTag(e.getName());
            sta.setIgnoreActions(false);
        }
    }

    private void updateAToolBar(final JToolBar bar) {
        Component c;
        Action action;
        final int count = bar.getComponentCount();
        final AttributeSet a = getMaxAttributes(getSHTMLEditorPane(), null);
        for (int i = 0; i < count; i++) {
            c = bar.getComponentAtIndex(i);
            if (c instanceof AttributeComponent) {
                if (c instanceof StyleSelector) {
                    final SetStyleAction ssa = (SetStyleAction) ((StyleSelector) c).getAction();
                    final AttributeSet oldAttibuteSet = ((AttributeComponent) c).getValue();
                    if (!a.isEqual(oldAttibuteSet)) {
                        ssa.setIgnoreActions(true);
                        ((AttributeComponent) c).setValue(a);
                        ssa.setIgnoreActions(false);
                    }
                }
                else {
                    ((AttributeComponent) c).setValue(a);
                }
            }
            else if (c instanceof AbstractButton) {
                action = ((AbstractButton) c).getAction();
                if ((action != null) && (action instanceof AttributeComponent)) {
                    ((AttributeComponent) action).setValue(a);
                }
            }
        }
    }

    /**
    * a JComboBox for selecting a font family names
    * from those available in the system.
    */
    class FontFamilyPicker extends JComboBox implements AttributeComponent {
        /** switch for the action listener */
        private boolean ignoreActions = false;

        FontFamilyPicker() {
            /**
             * add the font family names available in the system
             * to the combo box
             */
            super(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
            setSelectedItem("SansSerif");
        }

        boolean ignore() {
            return ignoreActions;
        }

        /**
         * set the value of this <code>AttributeComponent</code>
         *
         * @param a  the set of attributes possibly having an
         *          attribute this component can display
         *
         * @return true, if the set of attributes had a matching attribute,
         *            false if not
         */
        public boolean setValue(final AttributeSet a) {
            ignoreActions = true;
            final String newSelection = Util.styleSheet().getFont(a).getFamily();
            setSelectedItem(newSelection);
            ignoreActions = false;
            return true;
        }

        /**
         * get the value of this <code>AttributeComponent</code>
         *
         * @return the value selected from this component
         */
        public AttributeSet getValue() {
            final SimpleAttributeSet set = new SimpleAttributeSet();
            Util.styleSheet().addCSSAttribute(set, CSS.Attribute.FONT_FAMILY, (String) getSelectedItem());
            set.addAttribute(HTML.Attribute.FACE, (String) getSelectedItem());
            return set;
        }

        public AttributeSet getValue(final boolean includeUnchanged) {
            return getValue();
        }
    }

    /**
    * a JComboBox for selecting a font size
    */
    static final String[] FONT_SIZES = new String[] { "8", "10", "12", "14", "18", "24" };

    class FontSizePicker extends JComboBox implements AttributeComponent {
        private boolean ignoreActions = false;
        final private Object key;

        FontSizePicker() {
            /**
             * add font sizes to the combo box
             */
            super(FONT_SIZES);
            key = CSS.Attribute.FONT_SIZE;
        }

        boolean ignore() {
            return ignoreActions;
        }

        /**
         * set the value of this combo box
         *
         * @param a  the set of attributes possibly having a
         *          font size attribute this pick list could display
         *
         * @return true, if the set of attributes had a font size attribute,
         *            false if not
         */
        public boolean setValue(final AttributeSet a) {
            ignoreActions = true;
            final int size = Util.styleSheet().getFont(a).getSize();
            final String newSelection = Integer.toString(size);
            setEditable(true);
            setSelectedItem(newSelection);
            setEditable(false);
            ignoreActions = false;
            return true;
        }

        /**
         * get the value of this <code>AttributeComponent</code>
         *
         * @return the value selected from this component
         */
        public AttributeSet getValue() {
            final SimpleAttributeSet set = new SimpleAttributeSet();
            final String relativeSize = Integer.toString(getSelectedIndex() + 1);
            set.addAttribute(HTML.Attribute.SIZE, relativeSize);
            Util.styleSheet().addCSSAttributeFromHTML(set, CSS.Attribute.FONT_SIZE, relativeSize /*+ "pt"*/);
            return set;
        }

        public AttributeSet getValue(final boolean includeUnchanged) {
            return getValue();
        }
    }

    /**
     * a listener for property change events on ToggleFontActions
     */
    private class ToggleActionChangedListener implements PropertyChangeListener {
        JToggleButton button;

        ToggleActionChangedListener(final JToggleButton button) {
            super();
            this.button = button;
        }

        public void propertyChange(final PropertyChangeEvent e) {
            final String propertyName = e.getPropertyName();
            if (e.getPropertyName().equals(SHTMLPanelImpl.ACTION_SELECTED_KEY)) {
                //System.out.println("propertyName=" + propertyName + " newValue=" + e.getNewValue());
                if (e.getNewValue().toString().equals(SHTMLPanelImpl.ACTION_SELECTED)) {
                    button.setSelected(true);
                }
                else {
                    button.setSelected(false);
                }
            }
        }
    }

    public AttributeSet getMaxAttributes(final int caretPosition) {
        final Element paragraphElement = getSHTMLDocument().getParagraphElement(caretPosition);
        final StyleSheet styleSheet = getSHTMLDocument().getStyleSheet();
        return SHTMLPanelImpl.getMaxAttributes(paragraphElement, styleSheet);
    }

    /**
     * Gets all the attributes that can be found in the element tree
     * starting at the highest parent down to the character element
     * at the current position in the document. Combine element
     * attributes with attributes from the style sheet.
     *
     * @param editorPane  the editor pane to combine attributes from
     *
     * @return the resulting set of combined attributes
     */
    AttributeSet getMaxAttributes(final SHTMLEditorPane editorPane, final String elemName) {
        Element element = doc.getCharacterElement(editorPane.getSelectionStart());
        final StyleSheet styleSheet = doc.getStyleSheet();
        if (elemName != null && elemName.length() > 0) {
            element = Util.findElementUp(elemName, element);
            return SHTMLPanelImpl.getMaxAttributes(element, styleSheet);
        }
        final MutableAttributeSet maxAttributes = (MutableAttributeSet) SHTMLPanelImpl.getMaxAttributes(element,
            styleSheet);
        final StyledEditorKit editorKit = (StyledEditorKit) editorPane.getEditorKit();
        final MutableAttributeSet inputAttributes = editorKit.getInputAttributes();
        maxAttributes.addAttributes(inputAttributes);
        return maxAttributes;
    }

    Frame getMainFrame() {
        return JOptionPane.getFrameForComponent(SHTMLPanelImpl.this);
    }

    static AttributeSet getMaxAttributes(Element e, final StyleSheet s) {
        final SimpleAttributeSet a = new SimpleAttributeSet();
        final Element cElem = e;
        AttributeSet attrs;
        final Vector elements = new Vector();
        Object classAttr;
        String styleName;
        String elemName;
        while (e != null) {
            elements.insertElementAt(e, 0);
            e = e.getParentElement();
        }
        for (int i = 0; i < elements.size(); i++) {
            e = (Element) elements.elementAt(i);
            classAttr = e.getAttributes().getAttribute(HTML.Attribute.CLASS);
            elemName = e.getName();
            styleName = elemName;
            if (classAttr != null) {
                styleName = elemName + "." + classAttr.toString();
                a.addAttribute(HTML.Attribute.CLASS, classAttr);
            }
            //System.out.println("getMaxAttributes name=" + styleName);
            attrs = s.getStyle(styleName);
            if (attrs != null) {
                a.addAttributes(Util.resolveAttributes(attrs));
            }
            else {
                attrs = s.getStyle(elemName);
                if (attrs != null) {
                    a.addAttributes(Util.resolveAttributes(attrs));
                }
            }
            a.addAttributes(Util.resolveAttributes(e.getAttributes()));
        }
        if (cElem != null) {
            //System.out.println("getMaxAttributes cElem.name=" + cElem.getName());
            a.addAttributes(cElem.getAttributes());
        }
        //System.out.println(" ");
        //de.calcom.cclib.html.HTMLDiag hd = new de.calcom.cclib.html.HTMLDiag();
        //hd.listAttributes(a, 4);
        return new AttributeMapper(a).getMappedAttributes(AttributeMapper.toJava);
    }

    /**
     * @param documentPane The documentPane to set.
     */
    void setDocumentPane(final DocumentPane documentPane) {
        this.documentPane = documentPane;
    }

    /**
     * @return Returns the documentPane.
     */
    public DocumentPane getDocumentPane() {
        return documentPane;
    }

    protected void setEditorPane(final SHTMLEditorPane editorPane) {
        if (editorPane != null) {
            editorPane.setPopup(editorPopup);
        }
        this.editorPane = editorPane;
    }

    /**
     * @return Returns the editorPane.
     */
    public SHTMLEditorPane getSHTMLEditorPane() {
        return (SHTMLEditorPane) getEditorPane();
    }

    public JEditorPane getEditorPane() {
        return editorPane;
    }

    public JEditorPane getSourceEditorPane() {
        return (JEditorPane) getDocumentPane().getHtmlEditor();
    }

    /**
     * @return Returns the doc.
     */
    SHTMLDocument getSHTMLDocument() {
        return doc;
    }

    /**
     * @param undo The undo to set.
     */
    void setUndo(final UndoManager undo) {
        this.undo = undo;
    }

    /**
     * @return Returns the undo.
     */
    UndoManager getUndo() {
        return undo;
    }

    /**
     * @param tagSelector The tagSelector to set.
     */
    void setTagSelector(final TagSelector tagSelector) {
        this.tagSelector = tagSelector;
    }

    /**
     * @return Returns the tagSelector.
     */
    TagSelector getTagSelector() {
        return tagSelector;
    }

    void savePrefs() {
        splitPanel.savePrefs();
    }

    boolean close() {
        return true;
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#requestFocus()
     */
    public JEditorPane getMostRecentFocusOwner() {
        if (getDocumentPane() != null) {
            return getDocumentPane().getMostRecentFocusOwner();
        }
        return null;
    }

    /* ---------- font manipulation code end ------------------ */
    public int getCaretPosition() {
        return getSHTMLEditorPane().getCaretPosition();
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public void switchViews() {
        getDocumentPane().switchViews();
    }

    public void setOpenHyperlinkHandler(final ActionListener openHyperlinkHandler) {
        this.openHyperlinkHandler = openHyperlinkHandler;
    }

    public void openHyperlink(final String linkURL) {
        if (openHyperlinkHandler != null) {
            openHyperlinkHandler.actionPerformed(new ActionEvent(this, 0, linkURL));
        }
    }
}
