/*
 * Copyright (C) XBUP Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xbup.lib.xbprog.grammar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.block.XBFixedBlockType;
import org.xbup.lib.xb.block.declaration.XBDBlockType;
import org.xbup.lib.xb.block.declaration.XBDeclaration;
import org.xbup.lib.xb.catalog.XBCatalog;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.stream.XBTInputTokenStream;
import org.xbup.lib.xb.stream.XBTOutputStream;

/**
 * XBUP String-BNF Context-Free Grammar Rule
 * http://en.wikipedia.org/wiki/Backus%E2%80%93Naur_Form
 * TODO: This is simplified version
 *
 * Variant 1: ruleName ::= rule1 rule2 ... ruleN (Rule sequence)
 * Variant 2: ruleName ::= rule1 | rule2 | ... | ruleN (Rule alternatives)
 * Variant 3: ruleName ::= terminal (Terminal string)
 * Variant 4: ruleName ::= charA1 .. charAN | charB1 .. char BN | .. | charZ1 .. char ZN (Char ranges)
 *
 * @version 0.1.19 2010/06/04
 * @author XBUP Project (http://xbup.org)
 */
public class XBBNFGrammarRule implements XBSerializable {

    public static long[] XB_BLOCK_PATH = { 0,1,1,2 }; // Testing only

    private String ruleName;
    private List<String> rules;
    private String terminal;
    // In alternative mode terminal value is interpreted as sequence defined by pair of characters
    private boolean altMode;

    /**
     * Creates a new instance of XBRegularGrammarRule
     */
    public XBBNFGrammarRule() {
        rules = new ArrayList<String>();
        clear();
    }

    public XBDeclaration getXBDeclaration() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public XBBlockType getBlockType(XBCatalog catalog) {
        Long[] path = new Long[xbBlockPath.length];
        for (int i = 0; i < xbBlockPath.length; i++) {
            path[i] = new Long(xbBlockPath[i]);
        }
        XBBlockType context = new XBDBlockType(catalog.findBlockTypeByPath(path));
        if (context==null) {
            context = new XBFixedBlockType();
        } // Empty Context
        return context;
    }

    public void clear() {
        setRuleName(null);
        setRules(null);
        setTerminal(null);
        setAltMode(false);
    }

    /**
     * @return the ruleName
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * @param ruleName the ruleName to set
     */
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    /**
     * @return the rules
     */
    public List<String> getRules() {
        return rules;
    }

    /**
     * @param rules the rules to set
     */
    public void setRules(List<String> rules) {
        this.rules = rules;
    }

    /**
     * @return the terminal
     */
    public String getTerminal() {
        return terminal;
    }

    /**
     * @param terminal the terminal to set
     */
    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    /**
     * @return the rangeMode
     */
    public boolean isAltMode() {
        return altMode;
    }

    /**
     * @param rangeMode the rangeMode to set
     */
    public void setAltMode(boolean rangeMode) {
        this.altMode = rangeMode;
    }

    @Override
    public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
