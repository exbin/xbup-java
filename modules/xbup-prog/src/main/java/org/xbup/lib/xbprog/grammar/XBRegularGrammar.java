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
import org.xbup.lib.core.block.declaration.XBDeclaration;
import org.xbup.lib.core.block.declaration.local.XBLFormatDecl;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.stream.XBTokenInputStream;

/**
 * Simple class for regular grammar.
 *
 * @version 0.1.22 2013/04/05
 * @author XBUP Project (http://xbup.org)
 */
public class XBRegularGrammar implements XBSerializable {

    public static long[] XBUP_FORMATREV_CATALOGPATH = {0, 1, 1, 1};
    public static long[] XBUP_BLOCKREV_CATALOGPATH = {0, 1, 1, 1};
    private List<XBRegularGrammarRule> rules;
    private long nonterminalCount;
    private long terminalCount;

    public XBRegularGrammar() {
        rules = new ArrayList<>();
    }

    public XBDeclaration getXBDeclaration() {
        return new XBDeclaration(new XBLFormatDecl(XBUP_FORMATREV_CATALOGPATH));
    }

    public List<XBRegularGrammarRule> getRules() {
        return rules;
    }

    public void setRules(List<XBRegularGrammarRule> rules) {
        this.rules = rules;
    }

    private void clear() {
        nonterminalCount = 0;
        terminalCount = 0;
        rules.clear();
    }
    /*
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
     case 0: {
     nonterminalCount = ((XBAttributeToken) token).getAttribute().getLong();
     break;
     }
     case 1: {
     terminalCount = ((XBAttributeToken) token).getAttribute().getLong();
     break;
     }
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
     if ((serial.pullXBTToken().getTokenType() == XBTTokenType.BEGIN) && (serial.pullXBTToken().getTokenType() == XBTTokenType.TYPE)) {
     // TODO: Checktype
     int pos = 0;
     XBTToken token = serial.pullXBTToken();
     while (token.getTokenType() == XBTTokenType.ATTRIBUTE) {
     switch (pos) {
     case 0: {
     nonterminalCount = ((XBTAttributeToken) token).getAttribute().getLong();
     break;
     }
     case 1: {
     terminalCount = ((XBTAttributeToken) token).getAttribute().getLong();
     break;
     }
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
     listener.typeXBT(new XBDBlockType(new XBCPBlockDecl(XBUP_BLOCKREV_CATALOGPATH)));
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
     */

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
