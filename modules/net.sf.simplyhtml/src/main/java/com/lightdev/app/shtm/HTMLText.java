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

import java.io.IOException;
import java.io.StringWriter;
import java.util.Vector;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.html.HTMLDocument;

/**
 * A class to represent a portion of HTML text.
 *
 * <p>In stage 9 copy and paste have been refined to correct bugs that
 * occurred when cut and paste was happening in nested paragraph elements</p>
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
class HTMLText {
    /** the HTML representation of the text */
    private String htmlText;
    /** the plain text representation of the text */
    private String plainText;
    /** holds the copied plain text chunks */
    private final Vector clipText = new Vector(0);
    /** holds the copied character attributes mapping to clipText */
    private final Vector clipAttr = new Vector(0);
    /**
     * indicates whether or not the html represented by this
     * <code>HTMLText</code> instance contains more than one paragraph
     */
    //private boolean multiPara = false;
    private boolean stringRepresentation = false;

    /**
     * construct a new empty <code>HTMLText</code> object
     */
    public HTMLText() {
    }

    public HTMLText(final String htmlText, final String plainText) {
        this.htmlText = htmlText;
        this.plainText = plainText;
        stringRepresentation = true;
    }

    /**
     * copy an HTML string representation of a content portion from the
     * given editor pane.
     *
     * @param  src  the <code>SHTMLEditorPane</code> to copy from
     * @param  start  the position to start copying at
     * @param  length  the length of the content portion to copy
     *
     * @return an HTML string representation of the copied portion of content
     *
     * @see com.lightdev.app.shtm.SHTMLEditorPane
     */
    public void copyHTML(final SHTMLEditorPane src, final int start, final int length) throws BadLocationException,
            IOException {
        final HTMLDocument doc = (HTMLDocument) src.getDocument();
        if (doc.getParagraphElement(start).equals(doc.getParagraphElement(start + length))) {
            stringRepresentation = false;
            clearStyledText();
            copyStyledText(src);
        }
        else {
            stringRepresentation = true;
            final StringWriter sw = new StringWriter();
            final SHTMLWriter w = new SHTMLWriter(sw, doc, start, length);
            final Element first = doc.getParagraphElement(start);
            final Element last = doc.getCharacterElement(start + length);
            w.write(first, last);
            htmlText = sw.getBuffer().toString();
            plainText = doc.getText(start, length);
        }
    }

    /**
     * insert this <code>HTMLText<code> into a <code>Document</code>.
     *
     * @param  document  the document to insert into
     * @param  position  the text position to insert at
     */
    public void pasteHTML(final Document document, int position) throws BadLocationException, IOException {
        /**
         * if only text within one paragraph is to be inserted,
         * iterate over copied text chunks and insert each
         * chunk with its own set of copied attributes. Else
         * simply read copied HTML code back in.
         */
        if (!stringRepresentation) {
            final int nrTextChunks = getClipTextSize();
            String text;
            for (int i = 0; i < nrTextChunks; i++) {
                text = getCharactersAt(i);
                document.insertString(position, text, getCharacterAttributes(i));
                position += text.length();
            }
        }
    }

    /**
     * Determines the HTML string resulting from pasting the given HTML string at the given
     * position within the given paragraph element.
     *
     * @param doc  the document to insert to
     * @param characterElement  the character element to split
     * @param paragraphElement  the paragraph element to split
     * @param targetPosition  the text position inside the document where to split
     * @param pastedHtml  the html text to insert at pos
     */
    public String splitPaste(final SHTMLDocument doc, final Element characterElement, final Element paragraphElement,
                             final int targetPosition, final String pastedHtml, final boolean pastedHTMLHasParagraphTags) {
        // A model for this method: SHTMLEditorPane.InsertLineBreakAction.
        final StringWriter sw = new StringWriter();
        final SHTMLWriter w = new SHTMLWriter(sw, doc);
        String paragraphElementAdjustedName = paragraphElement.getName();
        final boolean impliedParagraph = paragraphElementAdjustedName.equalsIgnoreCase("p-implied");
        if (impliedParagraph) {
            paragraphElementAdjustedName = "p";
        }
        try {
            final int count = paragraphElement.getElementCount();
            if (!impliedParagraph || pastedHTMLHasParagraphTags) {
                w.writeStartTag(paragraphElementAdjustedName, paragraphElement.getAttributes());
            }
            for (int elementIdx = 0; elementIdx < count; elementIdx++) {
                final Element element = paragraphElement.getElement(elementIdx);
                if (element.equals(characterElement)) {
                    // Why the following?
                    //if(elementIdx > 0) 
                    //  w.writeStartTag(paragraphElementAdjustedName, paragraphElement.getAttributes());
                    //Write first part of the splitted text.
                    final SHTMLWriter htmlStartWriter = new SHTMLWriter(sw, doc, element.getStartOffset(),
                        targetPosition - element.getStartOffset());
                    htmlStartWriter.write(element);
                    if (pastedHTMLHasParagraphTags) {
                        w.writeEndTag(paragraphElementAdjustedName);
                    }
                    //Write the pasted text.
                    sw.write(pastedHtml);
                    //Write the second part of the splited text.
                    if (pastedHTMLHasParagraphTags) {
                        w.writeStartTag(paragraphElementAdjustedName, paragraphElement.getAttributes());
                    }
                    final SHTMLWriter htmlEndWriter = new SHTMLWriter(sw, doc, targetPosition, element.getEndOffset()
                            - targetPosition);
                    htmlEndWriter.write(element);
                    // Why the following?
                    //if(elementIdx > 0) 
                    //  w.writeEndTag(paragraphElementAdjustedName);
                }
                else {
                    w.write(element);
                }
            }
            if (!impliedParagraph || pastedHTMLHasParagraphTags) {
                w.writeEndTag(paragraphElementAdjustedName);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return sw.getBuffer().toString();
    }

    /**
     * Copy the selected portion of an SHTMLEditorPane as styled text,
     * i.e. chunks of plain text strings with an AttributeSet associated
     * to each of them.
     *
     * @param src  the SHTMLEditorPane to copy from
     */
    private void copyStyledText(final SHTMLEditorPane src) throws BadLocationException {
        final Document doc = src.getDocument();
        final int selStart = src.getSelectionStart();
        final int selEnd = src.getSelectionEnd();
        int eStart;
        int eEnd;
        final ElementIterator eli = new ElementIterator(doc);
        Element elem = eli.first();
        while (elem != null) {
            eStart = elem.getStartOffset();
            eEnd = elem.getEndOffset();
            if (elem.getName().equalsIgnoreCase(AbstractDocument.ContentElementName)) {
                if ((eEnd >= selStart) && (eStart <= selEnd)) {
                    clipAttr.addElement(elem.getAttributes());
                    if (eStart < selStart) {
                        if (eEnd > selEnd) { // both ends of elem outside selection
                            clipText.addElement(src.getText(selStart, selEnd - selStart));
                        }
                        else { // only first part of elem outside selection
                            clipText.addElement(src.getText(selStart, eEnd - selStart));
                        }
                    }
                    else if (eEnd > selEnd) { // only last part of elem outside selection
                        clipText.addElement(src.getText(eStart, selEnd - eStart));
                    }
                    else { // whole element inside selection
                        clipText.addElement(src.getText(eStart, eEnd - eStart));
                    }
                }
            }
            elem = eli.next();
        }
    }

    /** Gets the number of text chunks in this <code>StyledText</code> object. */
    private int getClipTextSize() {
        return clipText.size();
    }

    /**
     * get the attributes of a certain chunk of styled text
     *
     * @param chunkPos - the number of the chunk to get the attributes for
     * @return the attributes for respective character position
     */
    private AttributeSet getCharacterAttributes(final int chunkNo) {
        return (AttributeSet) clipAttr.elementAt(chunkNo);
    }

    /**
     * get the characters of a certain chunk of styled text
     *
     * @param chunkNo - the number of the chunk to get the characters for
     * @return the characters for respective chunk as String
     */
    private String getCharactersAt(final int chunkNo) {
        return (String) clipText.elementAt(chunkNo);
    }

    /** clear all styled text contents of this <code>HTMLText</code> object */
    private void clearStyledText() {
        clipText.clear();
        clipAttr.clear();
    }

    /**
     * get a String containing all chunks of text contained in this object
     *
     * @return string of all chunks in this object
     */
    public String toString() {
        final StringBuffer text = new StringBuffer();
        if (stringRepresentation) {
            text.append(plainText);
        }
        else {
            int i;
            for (i = 0; i < clipText.size(); i++) {
                text.append((String) clipText.elementAt(i));
            }
        }
        return text.toString();
    }

    /** (See also isParagraphTag.) */
    public static boolean containsParagraphTags(final String htmlText) {
        //An simplistic heuristic. Does not handle tags in comments, for instance.
        return htmlText
            .matches("(?ims).*<(blockquote|dir|div|dl|dt|frameset|h1|h2|h3|h4|h5|h6|hr|li|menu|ol|p|pre|table|td|th|tr|ul).*?>.*");
    }

    /** Determines whether the text has a table with exactly one cell and one row. */
    public boolean isOneCellInOneRow() {
        //return false;
        if (htmlText.matches("(?ims).*</td>.*<td.*")) {
            return false;
        }
        if (htmlText.matches("(?ims).*</tr>.*<tr.*")) {
            return false;
        }
        if (!htmlText.matches("(?ims).*<table.*")) {
            return false;
        }
        return true;
    }

    public boolean usesStringRepresenation() {
        return stringRepresentation;
    }

    public String getHTMLText() {
        return htmlText;
    }
}
