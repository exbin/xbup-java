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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.xb.block.declaration.XBDBlockType;
import org.xbup.lib.xb.block.declaration.XBDeclaration;
import org.xbup.lib.xb.block.declaration.XBFormatDecl;
import org.xbup.lib.xb.catalog.declaration.XBCDeclaration;
import org.xbup.lib.xb.catalog.declaration.XBCPBlockDecl;
import org.xbup.lib.xb.parser.XBParseException;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.basic.XBListener;
import org.xbup.lib.xb.parser.basic.XBTListener;
import org.xbup.lib.xb.parser.token.XBAttributeToken;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.parser.token.XBTAttributeToken;
import org.xbup.lib.xb.parser.token.XBTToken;
import org.xbup.lib.xb.parser.token.XBTTokenType;
import org.xbup.lib.xb.parser.token.XBToken;
import org.xbup.lib.xb.parser.token.XBTokenType;
import org.xbup.lib.xb.parser.token.pull.XBPullProvider;
import org.xbup.lib.xb.parser.token.pull.XBTPullProvider;
import org.xbup.lib.xb.parser.token.pull.convert.XBPrefixPullProvider;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.basic.XBListenerSerialMethod;
import org.xbup.lib.xb.serial.basic.XBTListenerSerialMethod;
import org.xbup.lib.xb.serial.token.XBPullProviderSerialHandler;
import org.xbup.lib.xb.serial.token.XBPullProviderSerialMethod;
import org.xbup.lib.xb.serial.token.XBTPullProviderSerialMethod;
import org.xbup.lib.xb.stream.XBTokenInputStream;
import org.xbup.lib.xb.type.XBArrayList;
import org.xbup.lib.xb.ubnumber.type.UBNat32;

/**
 * Simple class for regular grammar.
 *
 * @version 0.1.22 2013/04/05
 * @author XBUP Project (http://xbup.org)
 */
public class XBRegularGrammar implements XBSerializable {

    public static long[] XB_FORMAT_PATH = {0, 1, 1, 1}; // Testing only
    public static long[] XB_BLOCK_PATH = {0, 1, 1, 1}; // Testing only
    private XBArrayList<XBRegularGrammarRule> rules;
    private long nonterminalCount;
    private long terminalCount;

    /** Creates a new instance of XBRegularGrammar */
    public XBRegularGrammar() {
        rules = new XBArrayList<XBRegularGrammarRule>();
    }

    public XBDeclaration getXBDeclaration() {
        return new XBCDeclaration(new XBFormatDecl(xbFormatPath));
    }

    public XBArrayList<XBRegularGrammarRule> getRules() {
        return rules;
    }

    public void setRules(XBArrayList<XBRegularGrammarRule> rules) {
        this.rules = rules;
    }

    private void clear() {
        nonterminalCount = 0;
        terminalCount = 0;
        rules.clear();
    }

    @Override
    public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
        return serialType == XBSerializationType.FROM_XB
                ? Arrays.asList(new XBSerialMethod[]{new XBPullProviderSerialMethod(), new XBTPullProviderSerialMethod(1)})
                : Arrays.asList(new XBSerialMethod[]{new XBListenerSerialMethod(), new XBTListenerSerialMethod(1)});
    }

    @Override
    public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
        if (serialType == XBSerializationType.FROM_XB) {
            switch (methodIndex) {
                case 0: {
                    XBPullProvider serial = (XBPullProvider) serializationHandler;
                    try {
                        clear();
                        if ((serial.pullXBToken().getTokenType() == XBTokenType.BEGIN)) {
                            int pos = 0;
                            XBToken token = serial.pullXBToken();
                            while (token.getTokenType() == XBTokenType.ATTRIBUTE) {
                                switch (pos) {
                                    case 0: { nonterminalCount = ((XBAttributeToken) token).getAttribute().getLong(); break; }
                                    case 1: { terminalCount = ((XBAttributeToken) token).getAttribute().getLong(); break; }
                                }
                                token = serial.pullXBToken();
                                pos++;
                            }
                            while (token.getTokenType() == XBTokenType.BEGIN) {
                                XBRegularGrammarRule rule = new XBRegularGrammarRule();
                                XBPullProviderSerialHandler childSerialHandler = new XBPullProviderSerialHandler();
                                childSerialHandler.attachXBPullProvider(new XBPrefixPullProvider(serial, token));
                                rule.serializeXB(XBSerializationType.FROM_XB, 0, childSerialHandler);
                                rules.add(rule);
                                token = serial.pullXBToken();
                            }
                            if (token.getTokenType() != XBTokenType.END) {
                                throw new XBProcessingException("Unexpected data order");
                            }
                            break;
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(XBRegularGrammar.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    throw new XBProcessingException("Failed to parse Grammar Rule");
                }
                case 1: {
                    XBTPullProvider serial = (XBTPullProvider) serializationHandler;
                    try {
                        clear();
                        if ((serial.pullXBTToken().getTokenType() == XBTTokenType.BEGIN)&&(serial.pullXBTToken().getTokenType() == XBTTokenType.TYPE)) {
                            // TODO: Checktype
                            int pos = 0;
                            XBTToken token = serial.pullXBTToken();
                            while (token.getTokenType() == XBTTokenType.ATTRIBUTE) {
                                switch (pos) {
                                    case 0: { nonterminalCount = ((XBTAttributeToken) token).getAttribute().getLong(); break; }
                                    case 1: { terminalCount = ((XBTAttributeToken) token).getAttribute().getLong(); break; }
                                }
                                token = serial.pullXBTToken();
                                pos++;
                            }
                            while (token.getTokenType() == XBTTokenType.BEGIN) {
                                XBRegularGrammarRule rule = new XBRegularGrammarRule();
                                rule.serializeXB(XBSerializationType.FROM_XB, 1, serializationHandler);
                                rules.add(rule);
                                token = serial.pullXBTToken();
                            }
                            if (token.getTokenType() != XBTTokenType.END) {
                                throw new XBProcessingException("Unexpected data order");
                            }
                            break;
                        }
                    } catch (XBParseException ex) {
                        throw new XBProcessingException(ex.getMessage());
                    }
                    throw new XBProcessingException("Failed to parse Grammar Rule");
                }
            }
        } else {
            switch (methodIndex) {
                case 0: {
                    XBListener listener = (XBListener) serializationHandler;
                    try {
                        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
            //            listener.typeXB(new XBCPContextFBlock(xbCatalogPath, xbGroupIndex, xbBlockIndex));
                        listener.attribXB(new UBNat32(nonterminalCount));
                        listener.attribXB(new UBNat32(terminalCount));
                        // TODO: pass UBList
                        if (getRules().size() > 0) {
                            listener.attribXB(new UBNat32(getRules().size()));
                        } // Is L2 UBList
                        for (Iterator it = getRules().iterator(); it.hasNext();) {
                            ((XBSerializable) it.next()).serializeXB(XBSerializationType.TO_XB, 0, serializationHandler);
                        }
                        listener.endXB();
                    } catch (IOException ex) {
                        Logger.getLogger(XBRegularGrammar.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                case 1: {
                    XBTListener listener = (XBTListener) serializationHandler;
                    try {
                        listener.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                        listener.typeXBT(new XBDBlockType(new XBCPBlockDecl(XB_BLOCK_PATH)));
                        listener.attribXBT(new UBNat32(nonterminalCount));
                        listener.attribXBT(new UBNat32(terminalCount));
                        // TODO: pass UBList
                        if (getRules().size() > 0) {
                            listener.attribXBT(new UBNat32(getRules().size()));
                        } // Is L2 UBList
                        for (Iterator it = getRules().iterator(); it.hasNext();) {
                            ((XBSerializable) it.next()).serializeXB(XBSerializationType.TO_XB, 1, serializationHandler);
                        }
                        listener.endXBT();
                    } catch (IOException ex) {
                        Logger.getLogger(XBRegularGrammar.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (XBProcessingException ex) {
                        Logger.getLogger(XBRegularGrammar.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }
        }
    }

    public class XBPrefixInputStream extends XBTokenInputStream {

        private XBToken token;
        private final XBTokenInputStream stream;

        public XBPrefixInputStream(XBToken token, XBTokenInputStream stream) {
            this.token = token;
            this.stream = stream;
        }

        @Override
        public XBToken pullXBToken() throws XBProcessingException, IOException {
            if (token != null) {
                XBToken tokenReturn = token;
                token = null;
                return tokenReturn;
            } else {
                return stream.pullXBToken();
            }
        }

        @Override
        public void reset() throws IOException {
            stream.reset();
        }

        @Override
        public boolean finished() throws IOException {
            return stream.finished();
        }

        @Override
        public void skip(long tokenCount) throws XBProcessingException, IOException {
            stream.skip(tokenCount);
        }

        @Override
        public void close() throws IOException {
            stream.close();
        }
    }
}
