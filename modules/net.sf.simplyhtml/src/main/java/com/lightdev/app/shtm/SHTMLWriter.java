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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.CSS;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLWriter;

/**
 * FixedHTMLWriter
 *
 * 
 */
public class SHTMLWriter extends HTMLWriter {
    private Element element;
    private Writer writer = null;
    private boolean replaceEntities;
    private boolean inTextArea;
    //final private MutableAttributeSet oConvAttr  = new SimpleAttributeSet();
    //final private MutableAttributeSet convertedAttributeSet  = new SimpleAttributeSet();
    private boolean inPre;

    public SHTMLWriter(final Writer w, final HTMLDocument doc, final int pos, final int len) {
        super(w, doc, pos, len);
        writer = w;
    }

    /** Constructs the SHTMLWriter with a new StringWriter. See also the method
     * getWrittenString. */
    public SHTMLWriter(final HTMLDocument doc) {
        this(new StringWriter(), doc, 0, doc.getLength());
    }

    public SHTMLWriter(final Writer w, final HTMLDocument doc) {
        this(w, doc, 0, doc.getLength());
    }

    protected ElementIterator getElementIterator() {
        if (element == null) {
            return super.getElementIterator();
        }
        return new ElementIterator(element);
    }

    protected void output(final char[] chars, final int start, final int length) throws IOException {
        if (replaceEntities) {
            if (chars[start] == ' ') {
                chars[start] = '\u00A0';
            }
            final int last = start + length - 1;
            for (int i = start + 1; i < last; i++) {
                if (chars[i] == ' ' && (chars[i - 1] == '\u00A0' || chars[i + 1] == ' ')) {
                    chars[i] = '\u00A0';
                }
            }
            //			if(chars[last] == ' '){
            //				chars[last] = '\u00A0';
            //			}
        }
        super.output(chars, start, length);
    }

    protected void startTag(final Element elem) throws IOException, BadLocationException {
        if (matchNameAttribute(elem.getAttributes(), HTML.Tag.PRE)) {
            inPre = true;
        }
        super.startTag(elem);
    }

    protected void endTag(final Element elem) throws IOException {
        if (matchNameAttribute(elem.getAttributes(), HTML.Tag.PRE)) {
            inPre = false;
        }
        super.endTag(elem);
    }

    protected void text(final Element elem) throws BadLocationException, IOException {
        replaceEntities = !inPre;
        super.text(elem);
        replaceEntities = false;
    }

    protected void textAreaContent(final AttributeSet attr) throws BadLocationException, IOException {
        inTextArea = true;
        super.textAreaContent(attr);
        inTextArea = false;
    }

    public void write() throws IOException, BadLocationException {
        replaceEntities = false;
        super.write();
    }

    protected void writeLineSeparator() throws IOException {
        final boolean pre = replaceEntities;
        replaceEntities = false;
        super.writeLineSeparator();
        replaceEntities = pre;
    }

    protected void indent() throws IOException {
        if (inTextArea) {
            return;
        }
        final boolean pre = replaceEntities;
        replaceEntities = false;
        super.indent();
        replaceEntities = pre;
    }

    /**
     * Iterates over the
     * Element tree and controls the writing out of
     * all the tags and its attributes.
     *
     * @exception IOException on any I/O error
     * @exception BadLocationException if pos represents an invalid
     *            location within the document.
     *
     */
    synchronized void write(Element element) throws IOException, BadLocationException {
        this.element = element;
        try {
            write();
        }
        catch (final BadLocationException e) {
            element = null;
            throw e;
        }
        catch (final IOException e) {
            element = null;
            throw e;
        }
    }

    /**
     * invoke HTML creation for all children of a given element.
     *
     * @param elem  the element which children are to be written as HTML
     */
    public void writeChildElements(final Element parentElement) throws IOException, BadLocationException {
        Element childElement; //Not necessarily a paragraph element.
        for (int i = 0; i < parentElement.getElementCount(); i++) {
            childElement = parentElement.getElement(i);
            write(childElement);
        }
    }

    protected boolean inRange(final Element next) {
        final Document document = next.getDocument();
        if (document instanceof SHTMLDocument
                && next.getStartOffset() >= ((SHTMLDocument) document).getLastDocumentPosition()) {
            return false;
        }
        final int startOffset = getStartOffset();
        final int endOffset = getEndOffset();
        if ((next.getStartOffset() >= startOffset && (next.getStartOffset() < endOffset) || next.getEndOffset() - 1 == endOffset)
                || (startOffset >= next.getStartOffset() && startOffset < next.getEndOffset())) {
            return true;
        }
        return false;
    }

    /**
     * Create an older style of HTML attributes.  This will 
     * convert character level attributes that have a StyleConstants
     * mapping over to an HTML tag/attribute.  Other CSS attributes
     * will be placed in an HTML style attribute.
     */
    private static void convertStyleToHTMLStyle(final AttributeSet source, final MutableAttributeSet target) {
        if (source == null) {
            return;
        }
        final Enumeration sourceAttributeNames = source.getAttributeNames();
        String value = "";
        while (sourceAttributeNames.hasMoreElements()) {
            final Object sourceAttributeName = sourceAttributeNames.nextElement();
            if (sourceAttributeName instanceof CSS.Attribute) {
                // default is to store in a HTML style attribute
                if (value.length() > 0) {
                    value += "; ";
                }
                value += sourceAttributeName + ": " + source.getAttribute(sourceAttributeName);
            }
            else {
                target.addAttribute(sourceAttributeName, source.getAttribute(sourceAttributeName));
            }
        }
        if (value.length() > 0) {
            target.addAttribute(HTML.Attribute.STYLE, value);
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.text.html.HTMLWriter#writeAttributes(javax.swing.text.AttributeSet)
     */
    protected void writeAttributes(final AttributeSet attributeSet) throws IOException {
        final Object nameTag = (attributeSet != null) ? attributeSet.getAttribute(StyleConstants.NameAttribute) : null;
        final Object endTag = (attributeSet != null) ? attributeSet.getAttribute(HTML.Attribute.ENDTAG) : null;
        // write no attributes for end tags
        if (nameTag != null && endTag != null && (endTag instanceof String) && ((String) endTag).equals("true")) {
            return;
        }
        if (attributeSet instanceof Element) {
            final Element element = (Element) attributeSet;
            if (element.isLeaf() || element.getName().equalsIgnoreCase("p-implied")) {
                super.writeAttributes(attributeSet);
                return;
            }
        }
        //convertedAttributeSet.removeAttributes(convertedAttributeSet);
        final MutableAttributeSet convertedAttributeSet = new SimpleAttributeSet();
        SHTMLWriter.convertStyleToHTMLStyle(attributeSet, convertedAttributeSet);
        final Enumeration attributeNames = convertedAttributeSet.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            final Object attributeName = attributeNames.nextElement();
            if (attributeName instanceof HTML.Tag || attributeName instanceof StyleConstants
                    || attributeName == HTML.Attribute.ENDTAG) {
                continue;
            }
            write(" " + attributeName + "=\"" + convertedAttributeSet.getAttribute(attributeName) + "\"");
        }
    }

    /**
     * write an element and all its children. If a given element is reached,
     * writing stops with this element. If the end element is a leaf,
     * it is written as the last element, otherwise it is not written.
     *
     * @param e  the element to write including its children (if any)
     * @param end  the last leaf element to write or the branch element
     * to stop writing at (whatever applies)
     */
    private void writeElementsUntil(final Element e, final Element end) throws IOException, BadLocationException {
        if (e.isLeaf()) {
            write(e);
        }
        else {
            if (e != end) {
                startTag(e);
                final int childCount = e.getElementCount();
                int index = 0;
                while (index < childCount) {
                    writeElementsUntil(e.getElement(index), end); // drill down in recursion
                    index++;
                }
                endTag(e);
            }
        }
    }

    /**
     * write elements and their children starting at a
     * given element until a given element is reached.
     * The end element is written as the last element,
     * if it is a leaf element.
     *
     * @param startElement  the element to start writing with
     * @param endElement  the last element to write
     */
    void write(final Element startElement, final Element endElement) throws IOException, BadLocationException {
        final Element parentElement = startElement.getParentElement();
        final int count = parentElement.getElementCount();
        int i = 0;
        Element e = parentElement.getElement(i);
        while (i < count && e != startElement) {
            e = parentElement.getElement(i++);
        }
        while (i < count) {
            writeElementsUntil(e, endElement);
            e = parentElement.getElement(i++);
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.text.html.HTMLWriter#startTag(javax.swing.text.Element)
     */
    void writeStartTag(final Element elem) throws IOException, BadLocationException {
        // TODO Auto-generated method stub
        super.startTag(elem);
    }

    /* (non-Javadoc)
     * @see javax.swing.text.html.HTMLWriter#endTag(javax.swing.text.Element)
     */
    void writeEndTag(final Element elem) throws IOException {
        // TODO Auto-generated method stub
        super.endTag(elem);
    }

    void writeEndTag(final String elementName) throws IOException {
        indent();
        write('<');
        write('/');
        write(elementName);
        write('>');
        writeLineSeparator();
    }

    void writeStartTag(final String elementName, final AttributeSet attributes) throws IOException {
        indent();
        write('<');
        write(elementName);
        if (attributes != null) {
            writeAttributes(attributes);
        }
        write('>');
        writeLineSeparator();
    }

    public void write(final String string) {
        try {
            writer.write(string);
        }
        catch (final IOException ex) {
        }
    }

    public String toString() {
        if (writer instanceof StringWriter) {
            final StringWriter stringWriter = (StringWriter) writer;
            return stringWriter.getBuffer().toString();
        }
        return super.toString();
    }

    /** Gets the written string if the writer is a StringWriter, null otherwise. */
    String getWrittenString() {
        if (writer instanceof StringWriter) {
            final StringWriter stringWriter = (StringWriter) writer;
            return stringWriter.getBuffer().toString();
        }
        return null;
    }

    void removeLastWrittenNewline() {
        if (writer instanceof StringWriter) {
            final StringWriter stringWriter = (StringWriter) writer;
            int charIdx = stringWriter.getBuffer().length();
            while (stringWriter.getBuffer().charAt(--charIdx) <= 13) {
                stringWriter.getBuffer().deleteCharAt(charIdx);
            }
        }
    }
}
