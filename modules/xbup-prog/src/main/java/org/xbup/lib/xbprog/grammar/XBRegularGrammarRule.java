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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.block.XBFixedBlockType;
import org.xbup.lib.xb.block.declaration.XBDBlockType;
import org.xbup.lib.xb.block.declaration.XBDeclaration;
import org.xbup.lib.xb.catalog.XBCatalog;
import org.xbup.lib.xb.catalog.declaration.XBCPBlockDecl;
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
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.basic.XBListenerSerialMethod;
import org.xbup.lib.xb.serial.basic.XBTListenerSerialMethod;
import org.xbup.lib.xb.serial.token.XBPullProviderSerialMethod;
import org.xbup.lib.xb.serial.token.XBTPullProviderSerialMethod;
import org.xbup.lib.xb.ubnumber.type.UBNat32;

/**
 * Regular Grammar single rule. A -&gt; bC
 *
 * @version 0.1.22 2013/04/05
 * @author XBUP Project (http://xbup.org)
 */
public class XBRegularGrammarRule implements XBSerializable {

    public static long[] XB_BLOCK_PATH = { 0,1,1,2 }; // Testing only
    public static long xbGroupIndex = 1;
    public static long xbBlockIndex = 2;

    private long leftNonterminal;
    private long rightTerminal;
    private long rightNonterminal;

    /**
     * Creates a new instance of XBRegularGrammarRule
     */
    public XBRegularGrammarRule() {
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
        leftNonterminal = 0;
        rightTerminal = 0;
        rightNonterminal = 0;
    }

    public long getLeftNonterminal() {
        return leftNonterminal;
    }

    public void setLeftNonterminal(long leftNonterminal) {
        this.leftNonterminal = leftNonterminal;
    }

    public long getRightTerminal() {
        return rightTerminal;
    }

    public void setRightTerminal(long rightTerminal) {
        this.rightTerminal = rightTerminal;
    }

    public long getRightNonterminal() {
        return rightNonterminal;
    }

    public void setRightNonterminal(long rightNonterminal) {
        this.rightNonterminal = rightNonterminal;
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
                            // TODO: Checktype
                            int pos = 0;
                            XBToken token = serial.pullXBToken();
                            while (token.getTokenType() == XBTokenType.ATTRIBUTE) {
                                switch (pos) {
                                    case 0: { leftNonterminal = ((XBAttributeToken) token).getAttribute().getLong(); break; }
                                    case 1: { rightTerminal = ((XBAttributeToken) token).getAttribute().getLong(); break; }
                                    case 2: { rightNonterminal = ((XBAttributeToken) token).getAttribute().getLong(); break; }
                                }
                                pos++;
                                token = serial.pullXBToken();
                            }
                            if (token.getTokenType() != XBTokenType.END) {
                                throw new XBProcessingException("Unexpected data order");
                            }
                            break;
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(XBRegularGrammarRule.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    throw new XBProcessingException("Failed to parse Grammar Rule");
                }
                case 1: {
                    XBTPullProvider stream = (XBTPullProvider) serializationHandler;
                    clear();
                    if ((stream.pullXBTToken().getTokenType() == XBTTokenType.BEGIN) && (stream.pullXBTToken().getTokenType() == XBTTokenType.TYPE)) {
                        // TODO: Checktype
                        int pos = 0;
                        XBTToken token = stream.pullXBTToken();
                        while (token.getTokenType() == XBTTokenType.ATTRIBUTE) {
                            switch (pos) {
                                case 0: {
                                    leftNonterminal = ((XBTAttributeToken) token).getAttribute().getLong();
                                    break;
                                }
                                case 1: {
                                    rightTerminal = ((XBTAttributeToken) token).getAttribute().getLong();
                                    break;
                                }
                                case 2: {
                                    rightNonterminal = ((XBTAttributeToken) token).getAttribute().getLong();
                                    break;
                                }
                            }
                            pos++;
                            token = stream.pullXBTToken();
                        }
                    } else {
                        throw new XBProcessingException("Failed to parse Grammar Rule");
                    }

                    break;
                }
            }
        } else {
            switch (methodIndex) {
                case 0: {
                    XBListener serial = (XBListener) serializationHandler;
                    try {
                        serial.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
            //            listener.typeXBT(new XBCPContextFBlock(xbCatalogPath, xbGroupIndex, xbBlockIndex));
                        serial.attribXB(new UBNat32(leftNonterminal)); // TODO: Might spare if zero
                        serial.attribXB(new UBNat32(rightTerminal));
                        serial.attribXB(new UBNat32(rightNonterminal));
                        serial.endXB();
                    } catch (IOException ex) {
                        Logger.getLogger(XBRegularGrammarRule.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                case 1: {
                    XBTListener serial = (XBTListener) serializationHandler;
                    try {
                        serial.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                        serial.typeXBT(new XBDBlockType(new XBCPBlockDecl(XB_BLOCK_PATH)));
                        serial.attribXBT(new UBNat32(leftNonterminal)); // TODO: Might spare if zero
                        serial.attribXBT(new UBNat32(rightTerminal));
                        serial.attribXBT(new UBNat32(rightNonterminal));
                        serial.endXBT();
                    } catch (IOException ex) {
                        Logger.getLogger(XBRegularGrammarRule.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (XBProcessingException ex) {
                        Logger.getLogger(XBRegularGrammarRule.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }
        }
    }

}
