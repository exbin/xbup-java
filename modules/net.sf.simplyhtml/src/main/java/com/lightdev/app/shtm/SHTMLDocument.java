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
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.GapContent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;

/**
 * Extends <code>HTMLDocument</code> by a custom reader which supports
 * the SPAN tag.
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
public class SHTMLDocument extends HTMLDocument {
    public static final String SUFFIX = "&nbsp;";
    private static Set paragraphElements;
    private CompoundEdit compoundEdit;
    private int compoundEditDepth;
    private boolean inSetParagraphAttributes = false;
    private boolean baseDirChecked = false;
    private final boolean keepSpanTag = Util.preferenceIsTrue("keepSpanTag");

    /**
     * Constructs an SHTMLDocument.
     */
    public SHTMLDocument() {
        this(new GapContent(BUFFER_SIZE_DEFAULT), new StyleSheet());
    }

    /**
     * Constructs an SHTMLDocument with the default content
     * storage implementation and the given style/attribute
     * storage mechanism.
     *
     * @param styles  the styles
     */
    public SHTMLDocument(final StyleSheet styles) {
        this(new GapContent(BUFFER_SIZE_DEFAULT), styles);
    }

    /**
     * Constructs an SHTMLDocument with the given content
     * storage implementation and the given style/attribute
     * storage mechanism.
     *
     * @param c  the container for the content
     * @param styles the styles
     */
    public SHTMLDocument(final Content c, final StyleSheet styles) {
        super(c, styles);
        compoundEdit = null;
    }

    /**
     * apply a set of attributes to a given document element
     *
     * @param e  the element to apply attributes to
     * @param a  the set of attributes to apply
     */
    public void addAttributes(final Element e, final AttributeSet a) {
        if ((e != null) && (a != null)) {
            try {
                writeLock();
                //System.out.println("SHTMLDocument addAttributes e=" + e);
                //System.out.println("SHTMLDocument addAttributes a=" + a);
                final int start = e.getStartOffset();
                final DefaultDocumentEvent changes = new DefaultDocumentEvent(start, e.getEndOffset() - start,
                    DocumentEvent.EventType.CHANGE);
                final AttributeSet sCopy = a.copyAttributes();
                final MutableAttributeSet attr = (MutableAttributeSet) e.getAttributes();
                changes.addEdit(new AttributeUndoableEdit(e, sCopy, false));
                attr.addAttributes(a);
                changes.end();
                fireChangedUpdate(changes);
                fireUndoableEditUpdate(new UndoableEditEvent(this, changes));
            }
            finally {
                writeUnlock();
            }
        }
    }

    /**
     * Removes a consecutive group of child elements.
     *
     * @param element  the parent element to remove child elements from
     * @param index  the index of the first child element to remove
     * @param count  the number of child elements to remove
     */
    public void removeElements(final Element element, final int index, final int count) throws BadLocationException {
        writeLock();
        final int start = element.getElement(index).getStartOffset();
        final int end = element.getElement(index + count - 1).getEndOffset();
        try {
            final Element[] removed = new Element[count];
            final Element[] added = new Element[0];
            for (int counter = 0; counter < count; counter++) {
                removed[counter] = element.getElement(counter + index);
            }
            final DefaultDocumentEvent defaultDocumentEvent = new DefaultDocumentEvent(start, end - start,
                DocumentEvent.EventType.REMOVE);
            ((AbstractDocument.BranchElement) element).replace(index, removed.length, added);
            defaultDocumentEvent.addEdit(new ElementEdit(element, index, removed, added));
            final UndoableEdit undoableEdit = getContent().remove(start, end - start);
            if (undoableEdit != null) {
                defaultDocumentEvent.addEdit(undoableEdit);
            }
            postRemoveUpdate(defaultDocumentEvent);
            defaultDocumentEvent.end();
            fireRemoveUpdate(defaultDocumentEvent);
            if (undoableEdit != null) {
                fireUndoableEditUpdate(new UndoableEditEvent(this, defaultDocumentEvent));
            }
        }
        finally {
            writeUnlock();
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.text.html.HTMLDocument#setOuterHTML(javax.swing.text.Element, java.lang.String)
     */
    public void setOuterHTML(final Element paragraphElement, final String htmlText) throws BadLocationException,
            IOException {
        try {
            startCompoundEdit();
            if (paragraphElement.getName().equalsIgnoreCase("p-implied")) {
                //What has to be replaced is the HTML of the parent of this implied element.
                final Element parentElement = paragraphElement.getParentElement();
                final SHTMLWriter writer = new SHTMLWriter(this);
                final int indexOfElement = parentElement.getElementIndex(paragraphElement.getStartOffset());
                writer.writeStartTag(parentElement);
                for (int i = 0; i < indexOfElement; i++) {
                    writer.write(parentElement.getElement(i));
                }
                writer.write(htmlText);
                for (int i = indexOfElement + 1; i < parentElement.getElementCount(); i++) {
                    writer.write(parentElement.getElement(i));
                }
                writer.writeEndTag(parentElement);
                super.setOuterHTML(parentElement, writer.toString());
            }
            else {
                super.setOuterHTML(paragraphElement, htmlText);
            }
        }
        finally {
            endCompoundEdit();
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.text.html.HTMLDocument#insertAfterEnd(javax.swing.text.Element, java.lang.String)
     */
    public void insertAfterEnd(final Element elem, final String htmlText) throws BadLocationException, IOException {
        try {
            startCompoundEdit();
            super.insertAfterEnd(elem, htmlText);
        }
        finally {
            endCompoundEdit();
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.text.html.HTMLDocument#insertAfterStart(javax.swing.text.Element, java.lang.String)
     */
    public void insertAfterStart(final Element elem, final String htmlText) throws BadLocationException, IOException {
        try {
            startCompoundEdit();
            super.insertAfterStart(elem, htmlText);
        }
        finally {
            endCompoundEdit();
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.text.html.HTMLDocument#insertBeforeEnd(javax.swing.text.Element, java.lang.String)
     */
    public void insertBeforeEnd(final Element elem, final String htmlText) throws BadLocationException, IOException {
        try {
            startCompoundEdit();
            super.insertBeforeEnd(elem, htmlText);
        }
        finally {
            endCompoundEdit();
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.text.html.HTMLDocument#insertBeforeStart(javax.swing.text.Element, java.lang.String)
     */
    public void insertBeforeStart(final Element elem, final String htmlText) throws BadLocationException, IOException {
        try {
            startCompoundEdit();
            super.insertBeforeStart(elem, htmlText);
        }
        finally {
            endCompoundEdit();
        }
    }

    /** */
    public void replaceHTML(final Element firstElement, final int number, final String htmlText)
            throws BadLocationException, IOException {
        if (number > 1) {
            if (firstElement != null && firstElement.getParentElement() != null && htmlText != null) {
                final int start = firstElement.getStartOffset();
                final Element parent = firstElement.getParentElement();
                final int removeIndex = parent.getElementIndex(start);
                try {
                    startCompoundEdit();
                    removeElements(parent, removeIndex, number - 1);
                    setOuterHTML(parent.getElement(removeIndex), htmlText);
                }
                finally {
                    endCompoundEdit();
                }
            }
        }
        else if (number == 1) {
            setOuterHTML(firstElement, htmlText);
        }
    }

    public void startCompoundEdit() {
        compoundEditDepth++;
    }

    public void endCompoundEdit() {
        if (compoundEditDepth != 0) {
            compoundEditDepth--;
            if (compoundEditDepth == 0 && compoundEdit != null) {
                compoundEdit.end();
                super.fireUndoableEditUpdate(new UndoableEditEvent(this, compoundEdit));
                compoundEdit = null;
            }
        }
    }

    protected void fireUndoableEditUpdate(final UndoableEditEvent e) {
        if (compoundEditDepth == 0) {
            super.fireUndoableEditUpdate(e);
        }
        else {
            if (compoundEdit == null) {
                compoundEdit = new CompoundEdit();;
            }
            compoundEdit.addEdit(e.getEdit());
        }
    }

    /* ------------------ custom document title handling start -------------------- */
    /**
     * set the title of this SHTMLDocument
     *
     * @param title  the title this document shall have
     */
    public void setDocumentTitle(final String title) {
        try {
            final String titleHTML = "<title></title>";
            final Element defaultRoot = getDefaultRootElement();
            final Element head = Util.findElementDown(HTML.Tag.HEAD.toString(), defaultRoot);
            if (head != null) {
                final Element pImpl = Util.findElementDown(HTML.Tag.IMPLIED.toString(), head);
                if (pImpl != null) {
                    final Element tElem = Util.findElementDown(HTML.Tag.TITLE.toString(), pImpl);
                    if (tElem == null) {
                        insertBeforeEnd(pImpl, titleHTML);
                    }
                }
            }
            else {
                final Element body = Util.findElementDown(HTML.Tag.BODY.toString(), defaultRoot);
                insertBeforeStart(body, "<head>" + titleHTML + "</head>");
            }
            putProperty(Document.TitleProperty, title);
        }
        catch (final Exception e) {
            Util.errMsg(null, "An exception occurred while trying to insert the title", e);
        }
    }

    /**
     * get the title of this SHTMLDocument
     *
     * @return  the title of this document or null if none was set so far
     */
    public String getDocumentTitle() {
        final Object title = getProperty(Document.TitleProperty);
        if (title != null) {
            return title.toString();
        }
        else {
            return null;
        }
    }

    /* ------------------ custom document title handling end -------------------- */
    /* ------------------ custom style sheet reference handling start -------------------- */
    /**
     * insert a style sheet reference into the head of this SHTMLDocument
     */
    public void insertStyleRef() {
        try {
            final String styleRef = "  <link rel=stylesheet type=\"text/css\" href=\""
                    + DocumentPane.DEFAULT_STYLE_SHEET_NAME + "\">";
            final Element defaultRoot = getDefaultRootElement();
            final Element head = Util.findElementDown(HTML.Tag.HEAD.toString(), defaultRoot);
            if (head != null) {
                final Element pImpl = Util.findElementDown(HTML.Tag.IMPLIED.toString(), head);
                if (pImpl != null) {
                    final Element link = Util.findElementDown(HTML.Tag.LINK.toString(), pImpl);
                    if (link != null) {
                        setOuterHTML(link, styleRef);
                    }
                    else {
                        insertBeforeEnd(pImpl, styleRef);
                    }
                }
            }
            else {
                final Element body = Util.findElementDown(HTML.Tag.BODY.toString(), defaultRoot);
                insertBeforeStart(body, "<head>" + styleRef + "</head>");
            }
        }
        catch (final Exception e) {
            Util.errMsg(null, "An exception occurred while trying to insert the style sheet reference link", e);
        }
    }

    /**
     * check whether or not this SHTMLDocument has an explicit style sheet reference
     *
     * @return true, if a style sheet reference was found, false if not
     */
    public boolean hasStyleRef() {
        return (getStyleRef() != null);
    }

    /**
     * get the style sheet reference of the document in this
     * <code>DocumentPane</code>.
     *
     * @return the reference to this document's style sheet or
     *    null if none is found
     */
    public String getStyleRef() {
        String linkName = null;
        final Element link = Util.findElementDown(HTML.Tag.LINK.toString(), getDefaultRootElement());
        if (link != null) {
            final Object href = link.getAttributes().getAttribute(HTML.Attribute.HREF);
            if (href != null) {
                linkName = href.toString();
            }
        }
        return linkName;
    }

    /* ------------------ custom style sheet reference handling end -------------------- */
    /* -------- custom reader implementation start ------ */
    /**
     * Fetches the reader for the parser to use to load the document
     * with HTML.  This is implemented to return an instance of
     * SHTMLDocument.SHTMLReader.
     */
    public HTMLEditorKit.ParserCallback getReader(final int pos) {
        final Object desc = getProperty(Document.StreamDescriptionProperty);
        if (desc instanceof URL) {
            setBase((URL) desc);
        }
        final SHTMLReader reader = new SHTMLReader(pos, getLength() == 0);
        return reader;
    }

    /**
     * This reader extends HTMLDocument.HTMLReader by the capability
     * to handle SPAN tags
     */
    public class SHTMLReader extends HTMLDocument.HTMLReader {
        /** action needed to handle SPAN tags */
        SHTMLCharacterAction characterAction = new SHTMLCharacterAction();
        /** the attributes found in a STYLE attribute */
        AttributeSet styleAttributes;
        /** indicates whether we're inside a SPAN tag */
        boolean inSpan = false;
        boolean emptyDocument;
        private boolean paragraphInserted;
        private boolean inBody;
        private boolean paragraphCreated;
        private boolean isParagraphTag;

        /**
         * Constructor
         * 
         */
        public SHTMLReader(final int offset, final boolean emptyDocument) {
            super(offset, 0, 0, null);
            this.emptyDocument = emptyDocument;
            inBody = false;
            paragraphInserted = false;
            paragraphCreated = false;
        }

        /**
         * Handles the start tag received by the parser.
         *
         * If it is a SPAN tag, converts the contents of the STYLE
         * attribute to an AttributeSet, and adds it to the contents
         * of this tag.
         *
         * Otherwise lets HTMLDocument.HTMLReader do the work.
         */
        public void handleStartTag(final HTML.Tag tag, final MutableAttributeSet attributeSet, final int pos) {
            if (tag == HTML.Tag.BODY) {
                inBody = true;
            }
            else if (inBody) {
                isParagraphTag = isParagraphTag(tag);
                if (isParagraphTag) {
                    if (paragraphCreated && !paragraphInserted) {
                        insertParagraphEndTag(pos);
                    }
                    paragraphInserted = true;
                }
                else if (!paragraphCreated && !paragraphInserted) {
                    insertParagraphStartTag(pos);
                }
            }
            if (tag == HTML.Tag.SPAN && !keepSpanTag) {
                handleStartSpan(attributeSet);
            }
            else {
                super.handleStartTag(tag, attributeSet, pos);
                if (tag == HTML.Tag.FONT) {
                    charAttr.removeAttribute(tag);
                }
            }
        }

        private void insertParagraphStartTag(final int pos) {
            super.handleStartTag(HTML.Tag.P, new SimpleAttributeSet(), pos);
            paragraphCreated = true;
            paragraphInserted = true;
        }

        private void insertParagraphEndTag(final int pos) {
            super.handleEndTag(HTML.Tag.P, pos);
            paragraphCreated = false;
        }

        private boolean isParagraphTag(final Tag t) {
            if (paragraphElements == null) {
                paragraphElements = new HashSet();
                final Object[] elementList = new Object[] { HTML.Tag.BLOCKQUOTE, HTML.Tag.DIR, HTML.Tag.DIV,
                        HTML.Tag.DL, HTML.Tag.DT, HTML.Tag.FRAMESET, HTML.Tag.H1, HTML.Tag.H2, HTML.Tag.H3,
                        HTML.Tag.H4, HTML.Tag.H5, HTML.Tag.H6, HTML.Tag.HR, HTML.Tag.LI, HTML.Tag.MENU, HTML.Tag.OL,
                        HTML.Tag.P, HTML.Tag.PRE, HTML.Tag.TABLE, HTML.Tag.TD, HTML.Tag.TH, HTML.Tag.TR, HTML.Tag.UL };
                for (int i = 0; i < elementList.length; i++) {
                    paragraphElements.add(elementList[i]);
                }
            }
            return paragraphElements.contains(t);
        }

        private void handleStartSpan(final MutableAttributeSet attributeSet) {
            if (attributeSet.isDefined(HTML.Attribute.STYLE)) {
                final String styleAttributeValue = (String) attributeSet.getAttribute(HTML.Attribute.STYLE);
                attributeSet.removeAttribute(HTML.Attribute.STYLE);
                styleAttributes = getStyleSheet().getDeclaration(styleAttributeValue);
                attributeSet.addAttributes(styleAttributes);
            }
            else {
                styleAttributes = null;
            }
            final TagAction action = characterAction;
            if (action != null) {
                /** Remembers which part we're in for handleSimpleTag. */
                inSpan = true;
                action.start(HTML.Tag.SPAN, attributeSet);
            }
        }

        /**
         * SPAN tags are directed to handleSimpleTag by the parser.
         * If a SPAN tag is detected in this method, it gets redirected
         * to handleStartTag and handleEndTag respectively.
         */
        public void handleSimpleTag(final HTML.Tag t, final MutableAttributeSet a, final int pos) {
            if (inBody && !paragraphCreated && !paragraphInserted) {
                insertParagraphStartTag(pos);
            }
            if (t == HTML.Tag.SPAN && !keepSpanTag) {
                if (inSpan) {
                    handleEndTag(t, pos);
                }
                else {
                    handleStartTag(t, a, pos);
                }
            }
            else {
                super.handleSimpleTag(t, a, pos);
            }
        }

        /**
         * Handles end tag. If a SPAN tag is directed to this method, end its action,
         * otherwise, let HTMLDocument.HTMLReader do the work
         */
        public void handleEndTag(final HTML.Tag t, final int pos) {
            if (t == HTML.Tag.BODY) {
                if (paragraphCreated) {
                    insertParagraphEndTag(pos);
                }
                inBody = false;
                if (emptyDocument) {
                    if (!paragraphInserted) {
                        super.handleStartTag(HTML.Tag.P, getEndingAttributeSet(), pos);
                        super.handleText("\n".toCharArray(), pos);
                        super.handleEndTag(HTML.Tag.P, pos);
                    }
                    super.handleStartTag(HTML.Tag.P, getEndingAttributeSet(), pos);
                    super.handleText(" ".toCharArray(), pos);
                    super.handleEndTag(HTML.Tag.P, pos);
                }
                super.handleEndTag(t, pos);
            }
            else if (t == HTML.Tag.SPAN && !keepSpanTag) {
                handleEndSpan();
            }
            else {
                super.handleEndTag(t, pos);
            }
        }

        /* (non-Javadoc)
         * @see javax.swing.text.html.HTMLDocument.HTMLReader#handleComment(char[], int)
         */
        public void handleComment(final char[] data, final int pos) {
            if (emptyDocument) {
                super.handleComment(data, pos);
            }
        }

        /* (non-Javadoc)
         * @see javax.swing.text.html.HTMLDocument.HTMLReader#handleText(char[], int)
         */
        private void handleEndSpan() {
            final TagAction action = characterAction;
            if (action != null) {
                /**
                 * remember which part we're in for handleSimpleTag
                 */
                inSpan = false;
                action.end(HTML.Tag.SPAN);
            }
        }

        /**
         * Is used to read the style attribute from
         * a SPAN tag and to map from HTML to Java attributes.
         */
        class SHTMLCharacterAction extends HTMLDocument.HTMLReader.CharacterAction {
            public void start(final HTML.Tag tag, final MutableAttributeSet attr) {
                pushCharacterStyle();
                if (attr.isDefined(IMPLIED)) {
                    attr.removeAttribute(IMPLIED);
                }
                charAttr.addAttribute(tag, attr.copyAttributes());
                if (styleAttributes != null) {
                    charAttr.addAttributes(styleAttributes);
                }
                if (charAttr.isDefined(HTML.Tag.SPAN)) {
                    charAttr.removeAttribute(HTML.Tag.SPAN);
                }
                //System.out.println("mapping attributes");
                charAttr = (MutableAttributeSet) new AttributeMapper(charAttr)
                    .getMappedAttributes(AttributeMapper.toJava);
            }

            public void end(final HTML.Tag t) {
                popCharacterStyle();
            }
        }
    }

    /* -------- custom reader implementation end -------- */
    public Element getParagraphElement(final int pos) {
        return getParagraphElement(pos, inSetParagraphAttributes);
    }

    /** Gets the current paragraph element, retracing out of p-implied if the parameter
     * noImplied is true.
     * @see javax.swing.text.DefaultStyledDocument#getParagraphElement(int)
     */
    public Element getParagraphElement(final int pos, final boolean noPImplied) {
        Element element = super.getParagraphElement(pos);
        if (noPImplied) {
            while (element != null && element.getName().equalsIgnoreCase("p-implied")) {
                element = element.getParentElement();
            }
        }
        return element;
    }

    public int getLastDocumentPosition() {
        final int length = getLength();
        final int suffixLength = 1;
        return length > suffixLength ? length - suffixLength : length;
    }

    /* (non-Javadoc)
     * @see javax.swing.text.html.HTMLDocument#setParagraphAttributes(int, int, javax.swing.text.AttributeSet, boolean)
     */
    public void setParagraphAttributes(final int offset, final int length, final AttributeSet s, final boolean replace) {
        startCompoundEdit();
        super.setParagraphAttributes(offset, length, s, replace);
        inSetParagraphAttributes = true;
        super.setParagraphAttributes(offset, length, s, replace);
        inSetParagraphAttributes = false;
        endCompoundEdit();
    }

    public void removeParagraphAttributes(final int offset, final int length) {
        startCompoundEdit();
        // clear all paragraph attributes in selection
        for (int i = offset; i < offset + length;) {
            final Element paragraphElement = super.getParagraphElement(i);
            removeParagraphAtributes(paragraphElement);
            final int endOffset = paragraphElement.getEndOffset();
            i = endOffset;
        }
        endCompoundEdit();
    }

    private void removeParagraphAtributes(final Element paragraphElement) {
        if (paragraphElement != null && paragraphElement.getName().equalsIgnoreCase("p-implied")) {
            removeParagraphAtributes(paragraphElement.getParentElement());
            return;
        }
        final StringWriter writer = new StringWriter();
        final SHTMLWriter htmlStartWriter = new SHTMLWriter(writer, this);
        try {
            htmlStartWriter.writeStartTag(paragraphElement.getName(), null);
            htmlStartWriter.writeChildElements(paragraphElement);
            htmlStartWriter.writeEndTag(paragraphElement.getName());
            setOuterHTML(paragraphElement, writer.toString());
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        catch (final BadLocationException e) {
            e.printStackTrace();
        }
    }

    private SimpleAttributeSet getEndingAttributeSet() {
        final SimpleAttributeSet set = new SimpleAttributeSet();
        if (Util.preferenceIsTrue("gray_row_below_end")) {
            StyleConstants.setBackground(set, Color.GRAY);
        }
        return set;
    }

    /* (non-Javadoc)
     * @see javax.swing.text.html.HTMLDocument#getBase()
     */
    public URL getBase() {
        URL url = super.getBase();
        if (false == baseDirChecked) {
            baseDirChecked = true;
            final File docDir = new File(url.getFile());
            if (!docDir.exists()) {
                docDir.mkdirs();
            }
            try {
                url = docDir.toURI().toURL();
                super.setBase(url);
                return url;
            }
            catch (final MalformedURLException e) {
            }
        }
        return url;
    }

    /* (non-Javadoc)
     * @see javax.swing.text.html.HTMLDocument#setBase(java.net.URL)
     */
    public void setBase(final URL u) {
        baseDirChecked = false;
        super.setBase(u);
    }
}
