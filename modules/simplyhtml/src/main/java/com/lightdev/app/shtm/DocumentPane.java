/*
 * SimplyHTML, a word processor based on Java, HTML and CSS
 * Copyright (C) 2002 Ulrich Hilger
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;

import com.sun.demo.ExampleFileFilter;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.plaf.TextUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

/**
 * GUI representation of a document.
 *
 * <p>Swing already uses three types of classes to implement a model, view,
 * controller (MVC) approach for a document:</p>
 *
 * <p>JTextComponent - the view implementation<br>
 * Document - the model implementation<br>
 * EditorKit - the controller implementation</p>
 *
 * <p>For a GUI representation of a document, additional parts are needed, such
 * as a JScrollPane as well as listeners and fields to track the state of
 * the document while it is represented on a GUI.</p>
 *
 * <p><code>DocumentPane</code> wraps all those elements to implement a single
 * document centric external view to all elements.</p>
 *
 * <p>If for instance an application wants to create a new document, it simply
 * creates an instance of this class instead of having to implement own
 * methods for instatiating each element (editor pane, scroll pane, etc.)
 * separately.</p>
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
 */
class DocumentPane extends JPanel implements DocumentListener, ChangeListener {
    /** the editor displaying the document in layout view */
    private final SHTMLEditorPane editorPane;
    /** the editor displaying the document in HTML code view */
    private final SyntaxPane sourceEditorPane;
    /** temporary storage location for this document */
    private File docTempDir = null;
    /** the save thread, if a save operation is in progress */
    public Thread saveThread = null;
    /** indicator if a save operation was succesful */
    public boolean saveSuccessful = false;
    /** indicates if the document text has changed */
    private boolean documentChanged = false;

    /**
     * @param documentChanged The documentChanged to set.
     */
    private void setDocumentChanged(final boolean documentChanged) {
        this.documentChanged = documentChanged;
    }

    /**
     * @return Returns the documentChanged.
     */
    private boolean isDocumentChanged() {
        return documentChanged;
    }

    /** indicates if the document text has changed */
    private boolean htmlChanged = true;

    /**
     * @param htmlChanged The htmlChanged to set.
     */
    private void setHtmlChanged(final boolean htmlChanged) {
        this.htmlChanged = htmlChanged;
    }

    /**
     * @return Returns the htmlChanged.
     */
    private boolean isHtmlChanged() {
        return htmlChanged;
    }

    /** the name of the document */
    private String docName;
    /** the file the current style sheet was loaded from, if any */
    private final File loadedStyleSheet = null;
    /** the URL the document was loaded from (if applicable)*/
    private URL sourceUrl = null;
    /** JTabbedPane for our views */
    private JComponent paneHoldingScrollPanes;
    private final JScrollPane richViewScrollPane;
    private final JScrollPane sourceViewScrollPane;
    public static final int VIEW_TAB_LAYOUT = 0;
    public static final int VIEW_TAB_HTML = 1;
    /**
     * a save place for sourceUrl, when a document is to be saved
     * under a new name and this fails
     */
    private URL savedUrl = null;
    /** indicates if this document was loaded froma file */
    private boolean loadedFromFile = false;
    /** default document name */
    private String DEFAULT_DOC_NAME = "Untitled";
    /** default name for style sheet, when saved */
    public static String DEFAULT_STYLE_SHEET_NAME = "style.css";
    /** number for title of a new document */
    private int newDocNo;
    private int activeView;

    //private int renderMode;
    /**
     * construct a new <code>DocumentPane</code>.
     *
     * <p>A document still has to be either created or loaded after using
     * this constructor, so it is better to use the constructor doing this
     * right away instead.</p>
     */
    public DocumentPane(/*int renderMode*/) {
        super();
        // EditorPane and ScrollPane for layout view
        editorPane = new SHTMLEditorPane();
        final SHTMLEditorKit kit = new SHTMLEditorKit(/*renderMode*/);
        //kit.resetStyleSheet();
        editorPane.setEditorKit(kit);

        LinkController linkController = new LinkController();
        editorPane.addMouseMotionListener(linkController);
        editorPane.addMouseListener(linkController);
        editorPane.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                    if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                        try {
                            java.net.URI uri = new java.net.URI(e.getURL().toExternalForm());
                            desktop.browse(uri);
                        } catch (IOException ex) {
                            Util.errMsg((Component) e.getSource(), ex.getMessage(), ex);
                        } catch (URISyntaxException ex) {
                            Util.errMsg((Component) e.getSource(), ex.getMessage(), ex);
                        }
                    }
                }
            }
        });

        int metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        int metaKey = metaMask == KeyEvent.META_DOWN_MASK ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;
        editorPane.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(metaKey, metaMask), "meta-change");
        editorPane.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(metaKey, 0, true), "meta-change");
        editorPane.getActionMap().put("meta-change", linkController.getMetaChangeAction());

        richViewScrollPane = new JScrollPane(); // create a new JScrollPane,
        richViewScrollPane.getViewport().setView(editorPane); // ..add the editor pane to it
        // EditorPane and ScrollPane for html view
        sourceEditorPane = new SyntaxPane();
        sourceEditorPane.setFont(new Font("Monospaced", Font.PLAIN, 12));
        //sourceEditorPane.addKeyListener(rkw);
        sourceViewScrollPane = new JScrollPane();
        sourceViewScrollPane.getViewport().setView(sourceEditorPane);
        // Tabbed pane for HTML and layout views
        if (Util.showViewsInTabs()) {
            paneHoldingScrollPanes = new JTabbedPane();
            paneHoldingScrollPanes.add(richViewScrollPane, VIEW_TAB_LAYOUT);
            paneHoldingScrollPanes.add(sourceViewScrollPane, VIEW_TAB_HTML);
            final JTabbedPane tabbedPane = (JTabbedPane) paneHoldingScrollPanes;
            tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
            tabbedPane.setTitleAt(VIEW_TAB_LAYOUT, Util.getResourceString("layoutTabTitle"));
            tabbedPane.setTitleAt(VIEW_TAB_HTML, Util.getResourceString("htmlTabTitle"));
            tabbedPane.addChangeListener(this);
            setLayout(new BorderLayout());
            add(paneHoldingScrollPanes, BorderLayout.CENTER);
        }
        else {
            paneHoldingScrollPanes = new JPanel(new BorderLayout());
            paneHoldingScrollPanes.add(richViewScrollPane, BorderLayout.CENTER);
            activeView = VIEW_TAB_LAYOUT;
            //BorderLayout DOES NOT allow two parts with ..CENTER.
            //paneHoldingScrollPanes.add(sourceViewScrollPane, BorderLayout.CENTER);
            //sourceViewScrollPane.setVisible(false);
            //paneHoldingScrollPanes.addChangeListener(this);
            setLayout(new BorderLayout());
            add(paneHoldingScrollPanes, BorderLayout.CENTER);
        }
        setDocumentChanged(false); // no changes so far
        setPreferredSize(new Dimension(550, 550));
    }

    /**
     * construct a new DocumentPane with either a new Document or an exisiting
     * Document that is to be loaded into the DocumentPane upon construction.
     *
     * @param docToLoad the document to be loaded. If this is null, a new
     *      Document is created upon construction of the DocumentPane
     * @param newDocNo  the number a new document shall have in the
     * title as long as it is not saved (such as in 'Untitled1'). If an
     * existing document shall be loaded, this number is ignored
     */
    public DocumentPane(final URL docToLoad, final int newDocNo/*, int renderMode*/) {
        this(/*renderMode*/);
        DEFAULT_DOC_NAME = Util.getResourceString("defaultDocName");
        if (docToLoad != null) {
            loadDocument(docToLoad);
        }
        else {
            this.newDocNo = newDocNo;
            createNewDocument();
        }
    }

    /**
     * get the <code>JEditorPane</code> of this <code>DocumentPane</code>
     *
     * @return the JEditorPane of this DocumentPane
     */
    public SHTMLEditorPane getEditor() {
        return editorPane;
    }

    /**
     * get the <code>SyntaxPane</code> of this <code>DocumentPane</code>
     *
     * @return the SyntaxPane of this DocumentPane
     */
    public SyntaxPane getHtmlEditor() {
        return sourceEditorPane;
    }

    /**
     * @return the selected tab index
     */
    public int getSelectedTab() {
        if (paneHoldingScrollPanes instanceof JTabbedPane) {
            return ((JTabbedPane) paneHoldingScrollPanes).getSelectedIndex();
        }
        return activeView;
    }

    /**
     * create a new HTMLDocument and attach it to the editor
     */
    public void createNewDocument() {
        try {
            final SHTMLEditorKit kit = (SHTMLEditorKit) editorPane.getEditorKit();
            final SHTMLDocument doc = (SHTMLDocument) kit.createDefaultDocument();
            //insertStyleRef(doc); // create style sheet reference in HTML header tag
            //styles = kit.getStyleSheet();
            doc.addDocumentListener(this); // listen to changes
            doc.setBase(createTempDir());
            editorPane.setDocument(doc); // let the document be edited in our editor
            //doc.putProperty(Document.TitleProperty, getDocumentName());
            final boolean useStyle = Util.useSteStyleSheet();
            if (useStyle) {
                doc.insertStyleRef();
            }
        }
        catch (final Exception e) {
            Util.errMsg(this, e.getMessage(), e);
        }
    }

    public void setDocument(final Document docToSet) {
        try {
            editorPane.getEditorKit();
            final HTMLDocument doc = (HTMLDocument) getDocument();
            if (doc != null) {
                doc.removeDocumentListener(this);
            }
            docToSet.addDocumentListener(this); // listen to changes
            editorPane.setDocument(docToSet); // let the document be edited in our editor
        }
        catch (final Exception e) {
            Util.errMsg(this, e.getMessage(), e);
        }
    }

    /**
     * create temporary directory for a newly created document
     * so that images can be stored and referenced until the document
     * is saved.
     *
     * @return URL of created temporary document directoy
     */
    private URL createTempDir() throws MalformedURLException {
        docTempDir = new File(SHTMLPanelImpl.getAppTempDir().getAbsolutePath() + File.separator + getDocumentName()
                + File.separator);
        return docTempDir.toURI().toURL();
    }

    /**
     * remove the temporary storage created for this <code>DocumentPane</code>
     */
    public void deleteTempDir() {
        if (docTempDir != null) {
            Util.deleteDir(docTempDir);
            docTempDir = null;
        }
    }

    /**
     * load a document found at a certain URL.
     *
     * @param url the URL to look for the document
     */
    public void loadDocument(final URL url) {
        try {
            final SHTMLEditorKit kit = (SHTMLEditorKit) editorPane.getEditorKit();
            final SHTMLDocument doc = (SHTMLDocument) kit.createEmptyDocument();
            doc.putProperty("IgnoreCharsetDirective", new Boolean(true));
            doc.setBase(new File(url.getPath()).getParentFile().toURI().toURL()); // set the doc base
            final InputStream in = url.openStream(); // get an input stream
            kit.read(in, doc, 0); // ..and read the document contents from it
            in.close(); // .. then properly close the stream
            doc.addDocumentListener(this); // listen to changes
            editorPane.setDocument(doc); // let the document be edited in our editor
            setSource(url); // remember where the document came from
            loadedFromFile = true;
        }
        catch (final Exception ex) {
            Util.errMsg(this, "An exception occurred while loading the file", ex);
            ex.printStackTrace();
        }
    }

    /**
     * load the rules from a given style sheet file into a new <code>StyleSheet</code> object.
     *
     * @param  cssFile  the file object referring to the style sheet to load from
     *
     * @return the style sheet with rules loaded
     */
    private StyleSheet loadStyleSheet(final File cssFile) throws MalformedURLException, IOException {
        final StyleSheet s = new StyleSheet();
        s.importStyleSheet(cssFile.toURI().toURL());
        return s;
    }

    /**
     * saves the document to the file specified in the source of the
     * <code>DocumentPane</code> and creates the associated style sheet.
     *
     * The actual save process only is done, when there is a name to save
     * to. The class(es) calling this method have to make sure that a
     * name for new documents is requested from the user, for instance.
     *
     * The desired name and location for the save need then to be set using method
     * setSource prior to a call to this method
     *
     * @throws DocNameMissingException to ensure the caller gets notified
     *        that a save did not take place because of a missing name
     *        and location
     */
    public void saveDocument() throws DocNameMissingException {
        if (!saveInProgress()) {
            saveThread = Thread.currentThread(); // store thread for saveInProgress
            saveSuccessful = false; // if something goes wrong, this remains false
            File file = null;
            try {
                if (sourceUrl != null) {
                    /* write the HTML document */
                    if (getSelectedTab() == VIEW_TAB_HTML) {
                        editorPane.setText(sourceEditorPane.getText());
                    }
                    final SHTMLDocument doc = (SHTMLDocument) getDocument();
                    final OutputStream os = new FileOutputStream(sourceUrl.getPath());
                    final OutputStreamWriter osw = new OutputStreamWriter(os);
                    final SHTMLWriter hw = new SHTMLWriter(osw, doc);
                    hw.write();
                    osw.flush();
                    osw.close();
                    os.flush();
                    os.close();
                    /* write the style sheet */
                    if (doc.hasStyleRef()) {
                        saveStyleSheet();
                    }
                    /*
                      copy image directory,
                      if new document or saved from different location
                    */
                    saveImages();
                    /* clean up */
                    //System.out.println("DocumentPane textChanged = false");
                    setDocumentChanged(false); // indicate no changes pending anymore after the save
                    file = new File(sourceUrl.getPath()).getParentFile();
                    ((HTMLDocument) getDocument()).setBase(file.toURI().toURL()); // set the doc base
                    deleteTempDir();
                    //System.out.println("DocumentPane saveSuccessful = true");
                    saveSuccessful = true; // signal that saving was successful
                }
                else {
                    saveThread = null;
                    throw new DocNameMissingException();
                }
            }
            catch (final MalformedURLException mue) {
                if (file != null) {
                    Util.errMsg(this, "Can not create a valid URL for\n" + file.getAbsolutePath(), mue);
                }
                else {
                    Util.errMsg(this, mue.getMessage(), mue);
                }
            }
            catch (final Exception e) {
                if (savedUrl != null) {
                    sourceUrl = savedUrl;
                }
                Util.errMsg(this, "An exception occurred while saving the file", e);
            }
            saveThread = null;
            savedUrl = sourceUrl;
        }
    }

    /**
     * determine the directory this <code>DocumentPane</code> references image
     * files from
     *
     * @return the directory image files referenced by this
     * <code>DocumentPane</code> are found
     */
    public File getImageDir() {
        File srcDir = null;
        if (savedUrl == null && newDocNo > 0) {
            // new Document: use temp dir as source
            srcDir = new File(docTempDir + File.separator + SHTMLPanelImpl.IMAGE_DIR + File.separator);
        }
        else {
            if (savedUrl == null) {
                // document has been saved before: source is 'sourceUrl'
                srcDir = new File(new File(sourceUrl.getPath()).getParent() + File.separator + SHTMLPanelImpl.IMAGE_DIR
                        + File.separator);
            }
            else {
                /*
                   document has been saved before but now is
                   to be saved under new name: source is 'old' url
                */
                srcDir = new File(new File(savedUrl.getPath()).getParent() + File.separator + SHTMLPanelImpl.IMAGE_DIR
                        + File.separator);
            }
        }
        return srcDir;
    }

    /**
     * save image files
     */
    private void saveImages() {
        final File srcDir = getImageDir();
        final File destDir = new File(new File(sourceUrl.getPath()).getParent() + File.separator
                + SHTMLPanelImpl.IMAGE_DIR + File.separator);
        try {
            if (srcDir.exists()) {
                final ExampleFileFilter filter = new ExampleFileFilter();
                filter.addExtension("gif");
                filter.addExtension("jpg");
                filter.addExtension("jpeg");
                final File[] imgFiles = srcDir.listFiles();
                for (int i = 0; i < imgFiles.length; i++) {
                    Util.copyFile(imgFiles[i],
                        new File(destDir.getAbsolutePath() + File.separator + imgFiles[i].getName()));
                }
            }
        }
        catch (final Exception e) {
            Util.errMsg(this, e.getMessage(), e);
        }
    }

    /**
     * indicates whether or not a save process is in progress
     *
     * @return true, if a save process is going on, else false
     */
    public boolean saveInProgress() {
        //System.out.println("DocumentPane.saveInProgress=" + (saveThread != null) + " for document " + getDocumentName());
        return saveThread != null;
    }

    /**
     * Saves the style sheet of this document to a CSS file.
     *
     * <p>With stage 8, saves a style sheet by merging it with the existing
     * one with the same name/location. Styles in this style sheet overwrite
     * styles in the already existing style sheet.</p>
     */
    public void saveStyleSheet() throws IOException {
        final SHTMLDocument doc = (SHTMLDocument) getDocument();
        final StyleSheet styles = doc.getStyleSheet();
        final String styleSheetName = getStyleSheetName();
        if (styleSheetName != null) {
            final File styleSheetFile = new File(new URL(styleSheetName).getFile());
            if (!styleSheetFile.exists()) {
                // no styles present at save location, create new style sheet
                styleSheetFile.createNewFile();
            }
            else {
                if (loadedFromFile) {
                    if ((savedUrl == null) || (!savedUrl.getPath().equals(sourceUrl.getPath()))) {
                        /*
                            this style sheet was loaded from somewhere else and now is
                            being saved at a new location where a style sheet exists
                            havig the same name --> merge
                        */
                        mergeStyleSheets(loadStyleSheet(styleSheetFile), styles);
                    }
                    else {
                        /*
                            same location where styles originally came
                            from, overwrite existing styles with new version
                        */
                        styleSheetFile.delete();
                        styleSheetFile.createNewFile();
                    }
                }
                else {
                    /*
                        this style sheet was newly created and now is
                        being saved at a location where a style sheet exists
                        havig the same name --> merge
                    */
                    mergeStyleSheets(loadStyleSheet(styleSheetFile), styles);
                }
            }
            final OutputStream os = new FileOutputStream(styleSheetFile);
            final OutputStreamWriter osw = new OutputStreamWriter(os);
            CSSWriter cssWriter;
            cssWriter = new CSSWriter(osw, styles);
            cssWriter.write();
            osw.close();
            os.close();
        }
    }

    /**
     * Merges two style sheets by adding all the rules found
     * in the source style sheet that are not contained
     * in the destination style sheet. Assumes the rules
     * of src and dest are already loaded.
     *
     * @param sourceStyleSheet  the source StyleSheet
     * @param destinationStyleSheet  the destination StyleSheet
     */
    private void mergeStyleSheets(final StyleSheet sourceStyleSheet, final StyleSheet destinationStyleSheet)
            throws IOException {
        String name;
        Object elem;
        final Vector srcNames = Util.getStyleNames(sourceStyleSheet);
        final Vector destNames = Util.getStyleNames(destinationStyleSheet);
        final StringWriter sw = new StringWriter();
        final StringBuffer buf = sw.getBuffer();
        final CSSWriter cssWriter = new CSSWriter(sw, null);
        for (int i = 0; i < srcNames.size(); i++) {
            elem = srcNames.get(i);
            name = elem.toString();
            if (destNames.indexOf(elem) < 0) {
                buf.delete(0, buf.length());
                cssWriter.writeRule(name, sourceStyleSheet.getStyle(name));
                destinationStyleSheet.removeStyle(name);
                destinationStyleSheet.addRule(buf.toString());
            }
        }
    }

    /**
     * get the URL of the style sheet of this document
     *
     * <p>The name is built by<ol>
     * <li>get the style sheet reference, if none, use default style
     * sheet name</li>
     * <li>get the document base</li>
     * <li>if the style sheet reference is a relative path, resolve base
     * and relative path</li>
     * <li>else simply concatenate doc base and style sheet reference</li>
     * </ol></p>
     *
     * @return the URL of the style sheet
     */
    private String getStyleSheetName() throws MalformedURLException {
        String name = DEFAULT_STYLE_SHEET_NAME; //SHTMLEditorKit.DEFAULT_CSS;
        final SHTMLDocument doc = (SHTMLDocument) getDocument();
        final String styleRef = doc.getStyleRef();
        final File file = new File(sourceUrl.getPath()).getParentFile();
        String newDocBase = null;
        try {
            newDocBase = file.toURI().toURL().toString();
        }
        catch (final Exception e) {
            if (file != null) {
                Util.errMsg(this, "Can not create a valid URL for\n" + file.getAbsolutePath(), e);
            }
            else {
                Util.errMsg(this, e.getMessage(), e);
            }
        }
        if (styleRef != null) {
            name = Util.resolveRelativePath(styleRef, newDocBase);
        }
        else {
            name = null; // Util.resolveRelativePath(name, newDocBase);
        }
        //System.out.println("DocumentPane.getStyleSheetName=" + name);
        return name;
    }

    /**
     * get the name of the document of this pane.
     *
     * @return  the name of the document
     */
    public String getDocumentName() {
        String theName;
        if (docName == null || docName.length() < 1) {
            theName = DEFAULT_DOC_NAME + " " + Integer.toString(newDocNo);
        }
        else {
            theName = docName;
        }
        return theName;
    }

    /**
     * indicates whether or not the document needs to be saved.
     *
     * @return  true, if changes need to be saved
     */
    public boolean needsSaving() {
        //System.out.println("DocumentPane.needsSaving=" + textChanged + " for document " + getDocumentName());
        return isDocumentChanged();
    }

    /**
     * set the source this document is to be loaded from
     *
     * <p>This is only to be used when it is made sure,
     * that the document is saved at the location specified
     * by 'source'.</p>
     *
     * @param the URL of the source this document is to be loaded from
     */
    public void setSource(final URL source) {
        savedUrl = sourceUrl;
        sourceUrl = source;
        final String fName = source.getFile();
        docName = fName.substring(fName.lastIndexOf("/") + 1);
        fireNameChanged();
    }

    /**
     * get the source, this document was having before its current sourceUrl
     * was set.
     *
     * @return the source URL before a name change
     */
    public URL getOldSource() {
        if (savedUrl == null) {
            return sourceUrl;
        }
        else {
            return savedUrl;
        }
    }

    /**
     * get the source this document can be loaded from
     *
     * @return the URL this document can be loaded from
     */
    public URL getSource() {
        return sourceUrl;
    }

    /**
     * indicates whether or not this document was newly created and not saved so
     * far.
     *
     * @return true, if this is a new document that has not been saved so far
     */
    public boolean isNewDoc() {
        return sourceUrl == null;
    }

    /**
     * get the document of this <code>DocumentPane</code>
     *
     * @return the <code>Document</code> of this <code>DocumentPane</code>
     */
    public Document getDocument() {
        return editorPane.getDocument();
    }

    HTMLDocument getHTMLDocument() {
        return (HTMLDocument) sourceEditorPane.getDocument();
    }

    /**
     * Switches between the rich text view and the source view, given
     * tabbed panes are not used. Has no corresponding action; calling
     * this method is up to the caller application of SimplyHTML; the
     * application should call the method of the same name available at
     * SHTMLPanel.
     */
    public void switchViews() {
        if (paneHoldingScrollPanes instanceof JTabbedPane) {
            return;
        }
        // [ Tabbed pane not used ]
        if (activeView == VIEW_TAB_LAYOUT) {
            setHTMLView();
            paneHoldingScrollPanes.remove(richViewScrollPane);
            paneHoldingScrollPanes.add(sourceViewScrollPane);
            activeView = VIEW_TAB_HTML;
        }
        else {
            setLayoutView();
            paneHoldingScrollPanes.remove(sourceViewScrollPane);
            paneHoldingScrollPanes.add(richViewScrollPane);
            activeView = VIEW_TAB_LAYOUT;
        }
    }

    /**
     * Switches the DocumentPane to HTML view.
     */
    private void setHTMLView() {
        try {
            editorPane.getDocument().removeDocumentListener(this);
            final StringWriter stringWriter = new StringWriter();
            if (isHtmlChanged()) {
                editorPane.getEditorKit().write(stringWriter, editorPane.getDocument(), 0,
                    editorPane.getDocument().getLength());
                stringWriter.close();
                String newText = stringWriter.toString();
                if (!Util.preferenceIsTrue("writeHead", "true")) {
                    newText = newText.replaceAll("(?ims)<head>.*?(<body)", "$1");
                }
                sourceEditorPane.setText(newText);
                setHtmlChanged(false);
            }
            sourceEditorPane.getDocument().addDocumentListener(this);
            sourceEditorPane.addCaretListener(sourceEditorPane);
            setHtmlChanged(false);
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Switches the DocumentPane to layout view.
     */
    private void setLayoutView() {
        sourceEditorPane.getDocument().removeDocumentListener(this);
        sourceEditorPane.removeCaretListener(sourceEditorPane);
        if (isHtmlChanged()) {
            editorPane.setText(sourceEditorPane.getText());
            setHtmlChanged(false);
        }
        editorPane.setCaretPosition(0);
        editorPane.getDocument().addDocumentListener(this);
        editorPane.requestFocus();
    }

    /**
     * Convenience method for obtaining the document text
     * @return returns the document text as string.
     */
    String getDocumentText() {
        if (getSelectedTab() == VIEW_TAB_HTML) {
            editorPane.setText(sourceEditorPane.getText());
        }
        return editorPane.getText();
    }

    /**
     * Convenience method for setting the document text
     */
    void setDocumentText(final String sText) {
        switch (getSelectedTab()) {
            case VIEW_TAB_LAYOUT:
                editorPane.setText(sText);
                break;
            case VIEW_TAB_HTML:
                sourceEditorPane.setText(sText);
                break;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                setHtmlChanged(true);
                setDocumentChanged(false);
            }
        });
    }

    /* ----------------- changeListener implementation start ---------------------- */
    public void stateChanged(final ChangeEvent e) {
        final Object src = e.getSource();
        if (src.equals(paneHoldingScrollPanes)) {
            switch (getSelectedTab()) {
                case VIEW_TAB_LAYOUT:
                    setLayoutView();
                    break;
                case VIEW_TAB_HTML:
                    setHTMLView();
                    break;
            }
        }
        SHTMLPanelImpl.getOwnerSHTMLPanel(this).updateActions();
    }

    /* ----------------- changeListener implementation end ------------------------ */
    /* -------- DocumentListener implementation start ------------*/
    /**
     * listens to inserts into the document to track whether or not the document
     * needs to be saved.
     */
    public void insertUpdate(final DocumentEvent e) {
        setHtmlChanged(true);
        setDocumentChanged(true);
    }

    /**
     * listens to removes from the document to track whether or not the document
     * needs to be saved.
     */
    public void removeUpdate(final DocumentEvent e) {
        setHtmlChanged(true);
        setDocumentChanged(true);
    }

    /**
     * listens to changes on the document to track whether or not the document
     * needs to be saved.
     */
    public void changedUpdate(final DocumentEvent e) {
        //System.out.println("changedUpdate setting textChanged=true for " + getDocumentName());
        if (getSelectedTab() == VIEW_TAB_LAYOUT) {
            editorPane.updateInputAttributes();
            setDocumentChanged(true);
        }
    }

    /* -------- DocumentListener implementation end ------------*/
    /* -------- DocumentPaneListener definition start --------------- */
    /**
     * interface to be implemented for being notified of
     * changes to the name of this document
     */
    public interface DocumentPaneListener {
        public void nameChanged(DocumentPaneEvent e);

        public void activated(DocumentPaneEvent e);
    }

    /** the event object definition for DocumentPaneEvents */
    class DocumentPaneEvent extends EventObject {
        public DocumentPaneEvent(final Object source) {
            super(source);
        }
    }

    /** listeners for DocumentPaneEvents */
    private final Vector dpListeners = new Vector();

    /**
     * add a DocumentPaneListener to this Document
     *
     * @param listener the listener object to add
     */
    public void addDocumentPaneListener(final DocumentPaneListener listener) {
        if (!dpListeners.contains(listener)) {
            dpListeners.addElement(listener);
        }
        //System.out.println("DocumentPane.addDocumentPaneListener docName=" + getDocumentName() + ", listener.count=" + dpListeners.size());
    }

    /**
     * remove a DocumentPaneListener from this Document
     *
     * @param listener  the listener object to remove
     */
    public void removeDocumentPaneListener(final DocumentPaneListener listener) {
        dpListeners.remove(listener);
    }

    /**
     * fire a DocumentPaneEvent to all registered DocumentPaneListeners
     */
    public void fireNameChanged() {
        final Enumeration listenerList = dpListeners.elements();
        while (listenerList.hasMoreElements()) {
            ((DocumentPaneListener) listenerList.nextElement()).nameChanged(new DocumentPaneEvent(this));
        }
    }

    /**
     * fire a DocumentPaneEvent to all registered DocumentPaneListeners
     */
    public void fireActivated() {
        final Enumeration listenerList = dpListeners.elements();
        while (listenerList.hasMoreElements()) {
            ((DocumentPaneListener) listenerList.nextElement()).activated(new DocumentPaneEvent(this));
        }
    }

    /**
     * remove all listeners
     */
    public void removeAllListeners() {
        dpListeners.clear();
    }

    public JEditorPane getMostRecentFocusOwner() {
        switch (getSelectedTab()) {
            case VIEW_TAB_LAYOUT:
                return editorPane;
            case VIEW_TAB_HTML:
                return sourceEditorPane;
        }
        return null;
    }

    public void setContentPanePreferredSize(final Dimension prefSize) {
        setPreferredSize(null);
        paneHoldingScrollPanes.setPreferredSize(null);
        for (int i = 0; i < paneHoldingScrollPanes.getComponentCount(); i++) {
            final JScrollPane scrollPane = (JScrollPane) paneHoldingScrollPanes.getComponent(i);
            scrollPane.setPreferredSize(prefSize);
            scrollPane.invalidate();
        }
    }
    /* -------- DocumentPaneListener definition end --------------- */

    /* -------- LinkController definition start --------------- */
    public class LinkController extends MouseAdapter implements MouseMotionListener, Serializable {

        private final transient Position.Bias[] bias = new Position.Bias[1];
        private Element curElem = null;
        private int metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        public void mouseClicked(MouseEvent e) {
            JEditorPane editor = (JEditorPane) e.getSource();
            HTMLDocument hdoc = (HTMLDocument) editor.getDocument();

            if (editor.isEditable() && editor.isEnabled()
                    && SwingUtilities.isLeftMouseButton(e)
                    && (e.getModifiers() & metaMask) == metaMask) {
                Point pt = e.getPoint();
                int pos = editor.viewToModel(pt);
                if (pos >= 0) {
                    Element element = hdoc.getCharacterElement(pos);
                    AttributeSet aTag = element.getAttributes();
                    AttributeSet anchor = (AttributeSet) aTag.getAttribute(HTML.Tag.A);
                    String href = (String) anchor.getAttribute(HTML.Attribute.HREF);
                    try {
                        editor.fireHyperlinkUpdate(new HyperlinkEvent(editor, HyperlinkEvent.EventType.ACTIVATED, new URL(href)));
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(DocumentPane.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        public void mouseMoved(MouseEvent e) {
            updateCursor((JEditorPane) e.getSource(), (e.getModifiers() & metaMask) == metaMask, e.getPoint());
        }

        private void updateCursor(JEditorPane editor, boolean metaPressed, Point mousePosition) {
            if (!editor.isEnabled() || !editor.isEditable()) {
                return;
            }

            HTMLEditorKit kit = (HTMLEditorKit) editor.getEditorKit();
            Cursor newCursor = kit.getDefaultCursor();
            if (metaPressed) {
                Point pt = mousePosition;
                int pos = editor.getUI().viewToModel(editor, pt, bias);
                if (bias[0] == Position.Bias.Backward && pos > 0) {
                    pos--;
                }
                if (pos >= 0 && (editor.getDocument() instanceof HTMLDocument)) {
                    HTMLDocument hdoc = (HTMLDocument) editor.getDocument();
                    Element elem = hdoc.getCharacterElement(pos);
                    if (!doesElementContainLocation(editor, elem, pos,
                            (int) mousePosition.getX(), (int) mousePosition.getY())) {
                        elem = null;
                    }

                    String href = null;
                    if (elem != null) {
                        AttributeSet a = elem.getAttributes();
                        AttributeSet anchor = (AttributeSet) a.
                                getAttribute(HTML.Tag.A);
                        if (anchor != null) {
                            href = (String) anchor.getAttribute(HTML.Attribute.HREF);
                            if (href != null) {
                                newCursor = kit.getLinkCursor();
                            }
                        }
                    }
                }
            }

            if (editor.getCursor() != newCursor) {
                editor.setCursor(newCursor);
            }
        }

        /**
         * Returns true if the View representing <code>e</code> contains the
         * location <code>x</code>, <code>y</code>. <code>offset</code> gives
         * the offset into the Document to check for. Copied HTMLEditorKit.
         */
        private boolean doesElementContainLocation(JEditorPane editor,
                Element e, int offset,
                int x, int y) {
            if (e != null && offset > 0 && e.getStartOffset() == offset) {
                try {
                    TextUI ui = editor.getUI();
                    Shape s1 = ui.modelToView(editor, offset,
                            Position.Bias.Forward);
                    if (s1 == null) {
                        return false;
                    }
                    Rectangle r1 = (s1 instanceof Rectangle) ? (Rectangle) s1
                            : s1.getBounds();
                    Shape s2 = ui.modelToView(editor, e.getEndOffset(),
                            Position.Bias.Backward);
                    if (s2 != null) {
                        Rectangle r2 = (s2 instanceof Rectangle) ? (Rectangle) s2
                                : s2.getBounds();
                        r1.add(r2);
                    }
                    return r1.contains(x, y);
                } catch (BadLocationException ble) {
                }
            }
            return true;
        }

        public Action getMetaChangeAction() {
            return new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JEditorPane editor = (JEditorPane) e.getSource();
                    Point point = editor.getMousePosition();
                    if (point != null) {
                        updateCursor(editor, (e.getModifiers() & metaMask) == metaMask, point);
                    }
                }
            };
        }
    }
    /* -------- LinkController definition end --------------- */
}
