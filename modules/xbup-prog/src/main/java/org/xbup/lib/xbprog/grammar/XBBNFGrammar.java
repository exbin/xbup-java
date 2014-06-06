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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xbup.lib.xb.block.declaration.XBDeclaration;
import org.xbup.lib.xb.block.declaration.XBFormatDecl;
import org.xbup.lib.xb.catalog.declaration.XBCDeclaration;
import org.xbup.lib.xb.parser.XBParseException;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.stream.XBTInputTokenStream;
import org.xbup.lib.xb.stream.XBTOutputStream;
import org.xbup.lib.xb.type.XBArrayList;

/**
 * XBUP String-BNF Context-Free Grammar Rule
 *
 * @version 0.1 wr19.0 2010/06/04
 * @author XBUP Project (http://xbup.org)
 */
public class XBBNFGrammar implements XBSerializable {

    public static long[] xbFormatPath = {0, 1, 1, 1}; // Testing only
    public static long[] xbBlockPath = {0, 1, 1, 1}; // Testing only
    private XBArrayList<XBBNFGrammarRule> rules;
    private Map<String,XBBNFGrammarRule> nameMap; // Cache

    /** Creates a new instance of XBRegularGrammar */
    public XBBNFGrammar() {
        rules = new XBArrayList<XBBNFGrammarRule>();
        nameMap = new HashMap<String, XBBNFGrammarRule>();
    }

    public XBDeclaration getXBDeclaration() {
        return new XBCDeclaration(new XBFormatDecl(xbFormatPath));
    }

    private void clear() {
        getRules().clear();
    }

    /**
     * @return the rules
     */
    public XBArrayList<XBBNFGrammarRule> getRules() {
        return rules;
    }

    /**
     * @param rules the rules to set
     */
    public void setRules(XBArrayList<XBBNFGrammarRule> rules) {
        this.rules = rules;
    }

    public String generateGrammarDefinition() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rules.size(); i++) {
            XBBNFGrammarRule rule = rules.get(i);
            builder.append(rule.getRuleName());
            builder.append(" ::= ");
            builder.append(generateGrammarRuleDef(rule));
            builder.append(System.getProperty("line.separator"));
        }
        return builder.toString();
    }

    public String generateGrammarRuleDef(XBBNFGrammarRule rule) {
        StringBuilder builder = new StringBuilder();
        List<String> ruleItems = rule.getRules();
        if (rule.getTerminal() != null) {
            if (rule.isAltMode()) {
                String term = rule.getTerminal();
                for (int j = 0; j < term.length() / 2; j++) {
                    if (j>0) {
                        builder.append(" | ");
                    }
                    char sChar = term.charAt(j*2);
                    char eChar = term.charAt(j*2+1);
                    if (sChar == eChar) {
                        builder.append("\"");
                        builder.append(sChar);
                        builder.append("\"");
                    } else {
                        builder.append("\"");
                        builder.append(sChar);
                        builder.append("\" .. \"");
                        builder.append(eChar);
                        builder.append("\"");
                    }
                }
            } else {
                builder.append("\"").append(rule.getTerminal().replaceAll("\"", "\\\"")).append("\"");
            }
        } else {
            for (int j = 0; j < ruleItems.size(); j++) {
                if (j>0) {
                    if (rule.isAltMode()) {
                        builder.append(" | ");
                    } else {
                        builder.append(" ");
                    }
                }
                String subRule = ruleItems.get(j);
                builder.append(subRule);
            }
        }
        return builder.toString();
    }

    public void parseGrammarDefinition(String grammar) throws XBParseException {
        String[] lines = grammar.split(System.getProperty("line.separator"));
        for (int i = 0; i < lines.length; i++) {
            String[] words = lines[i].split(" ");
            if (words.length < 3) {
                throw new XBParseException("Missing at least one component: "+lines[i]);
            }
            XBBNFGrammarRule rule = new XBBNFGrammarRule();
            rule.setRuleName(words[0]);
            if (!"::=".equals(words[1])) {
                throw new XBParseException("Expected \"::=\" :"+lines[i]);
            }
            int j = 2;
            boolean merge = false;
            while (j < words.length) {

            }
        }
    }

    public void addRule(XBBNFGrammarRule rule) {
        rules.add(rule);
        nameMap.put(rule.getRuleName(), rule);
    }

    public XBBNFGrammarRule findRule(String ruleName) {
        return nameMap.get(ruleName);
    }

    /**
     * @return the nameMap
     */
    public Map<String, XBBNFGrammarRule> getNameMap() {
        return nameMap;
    }

    /**
     * @param nameMap the nameMap to set
     */
    public void setNameMap(Map<String, XBBNFGrammarRule> nameMap) {
        this.nameMap = nameMap;
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
