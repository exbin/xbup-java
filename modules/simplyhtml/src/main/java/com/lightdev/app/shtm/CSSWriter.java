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
import java.io.Writer;
import java.util.Enumeration;

import javax.swing.text.AttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.html.StyleSheet;

/**
 * A writer for writing a <code>StyleSheet</code> into a CSS file.
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
 */
class CSSWriter {
    /** spaces for indent */
    private char[] indentChars;
    /** new line character sequence */
    private final String newLine = System.getProperty("line.separator");
    /** the writer to write to */
    private final Writer writer;
    /** the style sheet to write */
    private final StyleSheet styleSheet;
    /** indent length */
    private int indentLen;

    /**
     * construct a new CSSWriter
     *
     * @param writer  the writer to write to
     * @param styleSheet  the StyleSheet to write
     */
    public CSSWriter(final Writer writer, final StyleSheet styleSheet) {
        this.writer = writer;
        this.styleSheet = styleSheet;
    }

    /** write the style sheet to the given writer */
    public void write() throws IOException {
        final Enumeration rules = styleSheet.getStyleNames();
        while (rules.hasMoreElements()) {
            writeRule((String) rules.nextElement());
            try {
                Thread.currentThread();
                Thread.sleep(0, 1);
            }
            catch (final Exception e) {
            }
        }
    }

    /**
     * write out a rule with a given name
     *
     * <p>Takes the style with the given name from the style sheet passed in the
     * constructor and writes it to the writer passed in the constructor.</p>.
     *
     * @param ruleName  the name of the rule to write out
     *
     * @exception IOException if i/o fails
     */
    private void writeRule(final String ruleName) throws IOException {
        writeRule(ruleName, styleSheet.getStyle(ruleName));
    }

    /**
     * write out a rule with a given name and style
     *
     * <p>Takes the style passed in paramter 'rule' and writes it under
     * the given name to the writer passed in the constructor.</p>.

     * @param ruleName  the name of the rule to write out
     * @apram rule  the style to write out
     *
     * @exception IOException if i/o fails
     */
    public void writeRule(final String ruleName, final AttributeSet rule) throws IOException {
        //System.out.println("CSSWriter writeRule ruleName=" + ruleName);
        indentLen = ruleName.length() + 3;
        if (!ruleName.equalsIgnoreCase(StyleContext.DEFAULT_STYLE)) {
            writer.write(ruleName);
            writer.write(" { ");
            writeStyle(rule);
            writer.write(newLine);
        }
    }

    /**
     * write a given style
     *
     * <p>A style is an AttributeSet which can have other
     * AttributeSets in the value field of one of its Attributes.
     * Therefore this is recursively called whenever an Attribute
     * contains another AttributeSet.</p>
     *
     * @param style the <code>Style</code> to write
     *
     * @return true, if the style was closed in this run of recursion,
     *      false if not
     */
    private boolean writeStyle(final AttributeSet style) throws IOException {
        boolean closed = false;
        final Enumeration names = style.getAttributeNames();
        Object value;
        Object key;
        int count = 0;
        while (names.hasMoreElements()) {
            key = names.nextElement();
            value = style.getAttribute(key);
            if ((!key.equals(StyleConstants.NameAttribute)) && (!key.equals(StyleConstants.ResolveAttribute))) {
                if (count > 0) {
                    writer.write(newLine);
                    indent(indentLen);
                }
                else {
                    count++;
                }
                writer.write(key.toString());
                writer.write(":");
                writer.write(value.toString());
                writer.write(";");
            }
            else {
                if (key.equals(StyleConstants.ResolveAttribute)) {
                    closed = writeStyle((Style) value);
                }
            }
        }
        if (!closed) {
            writer.write(" }");
            writer.write(newLine);
            closed = true;
        }
        return closed;
    }

    /**
     * indent by a given number of characters
     *
     * @param len the number of characters to indent
     */
    private void indent(final int len) throws IOException {
        if (indentChars == null || len > indentChars.length) {
            indentChars = new char[len];
            for (int i = 0; i < len; i++) {
                indentChars[i] = ' ';
            }
        }
        writer.write(indentChars, 0, len);
    }
}
