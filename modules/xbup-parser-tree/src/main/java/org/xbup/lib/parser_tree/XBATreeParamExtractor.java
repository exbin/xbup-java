/*
 * Copyright (C) ExBin Project
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
package org.xbup.lib.parser_tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.block.XBTEditableBlock;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.block.declaration.catalog.XBCBlockDecl;
import org.xbup.lib.core.block.definition.XBBlockDef;
import org.xbup.lib.core.block.definition.XBBlockParam;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.param.XBParamListProcessingState;
import org.xbup.lib.core.parser.param.XBParamProcessingState;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTEndToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.XBTZeroAttributeToken;
import org.xbup.lib.core.parser.token.convert.XBTListenerToToken;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.ubnumber.UBENatural;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBENat32;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Extracting specified parameters from XBUP level 2 blocks.
 *
 * @version 0.1.24 2015/01/18
 * @author ExBin Project (http://exbin.org)
 */
public class XBATreeParamExtractor implements XBTPullProvider, XBTEventListener {

    private final XBTBlock source;

    private final List<ProcessingState> processingStates = new ArrayList<>();
    private XBParamProcessingState currentProcessingState;
    private XBParamListProcessingState currentListProcessingState;
    private int currentParameter;
    private XBBlockParam parameterType = null;
    private ParameterInfo currentParameterInfo;
    private XBTTreeWriter childWriter;
    private XBTTreeReader childReader;
    private final XBTListenerToToken childConvertor = new XBTListenerToToken();

    private final ParameterInfo position;

    public XBATreeParamExtractor(XBTBlock source, XBACatalog catalog) {
        this.source = source;
        position = new ParameterInfo();

        XBBlockType blockType = source.getBlockType();
        if (blockType instanceof XBDeclBlockType) {
            position.blockDecl = ((XBDeclBlockType) blockType).getBlockDecl();
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        reset();
    }

    private void reset() {
        currentParameter = 0;
        currentParameterInfo = null;
        position.attributeCount = 0;
        position.childCount = 0;
        currentProcessingState = XBParamProcessingState.BEGIN;
        currentListProcessingState = XBParamListProcessingState.BEGIN;
        childWriter = null;
        childReader = null;
    }

    @Override
    public XBTToken pullXBTToken() throws XBProcessingException, IOException {
        if (childWriter != null) {
            return getChildToken();
        }

        if (parameterType == null) {
            XBBlockDef blockDef = position.blockDecl.getBlockDef();
            if (blockDef != null) {
                parameterType = blockDef.getBlockParam(currentParameter);

                if (parameterType == null) {
                    throw new XBProcessingException("Unable to process parameter " + currentParameter, XBProcessingExceptionType.UNSUPPORTED);
                }
            }
        }

        switch (parameterType.getParamType()) {
            case JOIN: {
                switch (currentProcessingState) {
                    case BEGIN: {
                        if (currentParameterInfo == null) {
                            currentParameterInfo = processParameterInfo();
                            currentProcessingState = XBParamProcessingState.TYPE;
                            return new XBTBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED);
                        } else {
                            throw new XBProcessingException("Parameter already processed", XBProcessingExceptionType.READING_AFTER_END);
                        }
                    }

                    case TYPE: {
                        currentProcessingState = currentParameterInfo.isEmpty() ? XBParamProcessingState.END
                                : (currentParameterInfo.attributeCount > 0 ? XBParamProcessingState.ATTRIBUTES : XBParamProcessingState.CHILDREN);
                        return new XBTTypeToken(new XBDeclBlockType(currentParameterInfo.blockDecl));
                    }

                    case ATTRIBUTES: {
                        if (currentParameterInfo.attributeCount > 0) {
                            currentParameterInfo.attributeCount--;
                            if (currentParameterInfo.attributeCount == 0) {
                                currentProcessingState = currentParameterInfo.childCount > 0 ? XBParamProcessingState.CHILDREN : XBParamProcessingState.END;
                            }

                            return getNextAttributeToken();
                        }

                    }

                    case CHILDREN: {
                        if (currentParameterInfo.childCount > 0) {
                            if (currentParameterInfo.childCount == 1) {
                                currentProcessingState = XBParamProcessingState.END;
                            }

                            childWriter = new XBTTreeWriter(getNextChild());
                            return getChildToken();
                        }

                        throw new IllegalStateException();
                    }

                    case END: {
                        currentProcessingState = XBParamProcessingState.BEGIN;
                        return new XBTEndToken();
                    }
                }

                break;
            }

            case CONSIST: {
                if (currentParameterInfo == null) {
                    currentParameterInfo = processParameterInfo();
                    childWriter = new XBTTreeWriter(getNextChild());
                    return getChildToken();
                } else {
                    throw new XBProcessingException("Parameter already processed", XBProcessingExceptionType.READING_AFTER_END);
                }
            }

            case LIST_JOIN: {
                switch (currentListProcessingState) {
                    case BEGIN: {
                        if (currentParameterInfo == null) {
                            currentParameterInfo = processParameterInfo();
                            currentListProcessingState = XBParamListProcessingState.TYPE;
                            return new XBTBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED);
                        } else {
                            throw new XBProcessingException("Parameter already processed", XBProcessingExceptionType.READING_AFTER_END);
                        }
                    }

                    case TYPE: {
                        currentListProcessingState = XBParamListProcessingState.LIST_SIZE;
                        // TODO some sort of customized type later
                        return new XBTTypeToken(new XBFixedBlockType(XBBasicBlockType.UNKNOWN_BLOCK));
                    }

                    case LIST_SIZE: {
                        UBNat32 listSize = new UBNat32(currentParameterInfo.listSize);
                        currentListProcessingState = currentParameterInfo.listSize == 0
                                ? XBParamListProcessingState.END
                                : XBParamListProcessingState.ITEMS;
                        if (currentParameterInfo.attributeCount == 0) {
                            throw new IllegalStateException();
                        } else {
                            currentParameterInfo.attributeCount--;
                        }
                        return new XBTAttributeToken(listSize);
                    }

                    case ITEMS: {
                        if (currentParameterInfo.listSize >= 0) {
                            switch (currentProcessingState) {
                                case BEGIN: {
                                    currentParameterInfo = processParameterInfo();
                                    currentProcessingState = XBParamProcessingState.TYPE;
                                    return new XBTBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED);
                                }

                                case TYPE: {
                                    currentProcessingState = currentParameterInfo.isEmpty() ? XBParamProcessingState.END
                                            : (currentParameterInfo.attributeCount > 0 ? XBParamProcessingState.ATTRIBUTES : XBParamProcessingState.CHILDREN);
                                    return new XBTTypeToken(new XBDeclBlockType(currentParameterInfo.blockDecl));
                                }

                                case ATTRIBUTES: {
                                    if (currentParameterInfo.attributeCount > 0) {
                                        currentParameterInfo.attributeCount--;
                                        if (currentParameterInfo.attributeCount == 0) {
                                            currentProcessingState = currentParameterInfo.childCount > 0 ? XBParamProcessingState.CHILDREN : XBParamProcessingState.END;
                                        }

                                        return getNextAttributeToken();
                                    }

                                }

                                case CHILDREN: {
                                    if (currentParameterInfo.childCount > 0) {
                                        if (currentParameterInfo.childCount == 1) {
                                            currentProcessingState = XBParamProcessingState.END;
                                        }

                                        childWriter = new XBTTreeWriter(getNextChild());
                                        return getChildToken();
                                    }

                                    throw new IllegalStateException();
                                }

                                case END: {
                                    currentProcessingState = XBParamProcessingState.BEGIN;
                                    currentParameterInfo.listSize--;
                                    if (currentParameterInfo.listSize == 0) {
                                        currentListProcessingState = XBParamListProcessingState.END;
                                    }
                                    return new XBTEndToken();
                                }
                            }
                        }

                    }
                    case END: {
                        currentListProcessingState = XBParamListProcessingState.BEGIN;
                        return new XBTEndToken();
                    }
                }

                break;
            }

            case LIST_CONSIST: {
                switch (currentListProcessingState) {
                    case BEGIN: {
                        if (currentParameterInfo == null) {
                            currentParameterInfo = processParameterInfo();
                            currentListProcessingState = XBParamListProcessingState.TYPE;
                            return new XBTBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED);
                        } else {
                            throw new XBProcessingException("Parameter already processed", XBProcessingExceptionType.READING_AFTER_END);
                        }
                    }

                    case TYPE: {
                        currentListProcessingState = XBParamListProcessingState.LIST_SIZE;
                        return new XBTTypeToken(new XBFixedBlockType(XBBasicBlockType.UNKNOWN_BLOCK));
                    }

                    case LIST_SIZE: {
                        UBENat32 listSize = new UBENat32();
                        if (currentParameterInfo.listSize >= 0) {
                            listSize.setValue(currentParameterInfo.listSize);
                        } else {
                            listSize.setInfinity();
                        }
                        currentListProcessingState = currentParameterInfo.listSize == 0
                                ? XBParamListProcessingState.END
                                : XBParamListProcessingState.ITEMS;
                        if (currentParameterInfo.attributeCount == 0) {
                            throw new IllegalStateException();
                        } else {
                            currentParameterInfo.attributeCount--;
                        }
                        return new XBTAttributeToken(listSize.convertToNatural());
                    }

                    case ITEMS: {
                        if (currentParameterInfo.listSize >= 0) {
                            childWriter = new XBTTreeWriter(getNextChild());
                            currentParameterInfo.listSize--;
                            if (currentParameterInfo.listSize == 0) {
                                currentListProcessingState = XBParamListProcessingState.END;
                            }
                            return getChildToken();
                        } else {
                            // TODO process infinite list
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                    }
                    case END: {
                        currentListProcessingState = XBParamListProcessingState.BEGIN;
                        return new XBTEndToken();
                    }
                }

                break;
            }
        }

        throw new XBProcessingException("Unexpected processing state", XBProcessingExceptionType.UNKNOWN);
    }

    @Override
    public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
        if (childReader != null) {
            XBTListenerToToken.tokenToListener(token, childReader);
            if (childReader.isClosed()) {
                childReader = null;
                currentParameterInfo.childCount--;
            }

            return;
        }

        if (parameterType == null) {
            XBBlockDef blockDef = position.blockDecl.getBlockDef();
            if (blockDef != null) {
                parameterType = blockDef.getBlockParam(currentParameter);
                if (parameterType == null) {
                    throw new XBProcessingException("Unable to process parameter " + currentParameter, XBProcessingExceptionType.UNSUPPORTED);
                }
            }
        }

        switch (parameterType.getParamType()) {
            case JOIN: {
                switch (token.getTokenType()) {
                    case BEGIN: {
                        if (currentProcessingState == XBParamProcessingState.BEGIN) {
                            if (currentParameterInfo == null) {
                                currentParameterInfo = processParameterInfo();
                                currentProcessingState = XBParamProcessingState.TYPE;
                                return;
                            } else {
                                throw new XBProcessingException("Parameter already processed", XBProcessingExceptionType.READING_AFTER_END);
                            }
                        }

                        if (currentProcessingState == XBParamProcessingState.ATTRIBUTES) {
                            if (currentParameterInfo == null || currentParameterInfo.attributeCount == 0) {
                                throw new IllegalStateException();
                            }

                            currentProcessingState = XBParamProcessingState.CHILDREN;
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        if (currentProcessingState == XBParamProcessingState.CHILDREN) {
                            childReader = new XBTTreeReader(getNextChild());
                            childReader.beginXBT(((XBTBeginToken) token).getTerminationMode());
                            return;
                        }

                        throw new XBProcessingException("Unexpected join processing order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }

                    case TYPE: {
                        if (currentProcessingState == XBParamProcessingState.TYPE) {
                            currentProcessingState = currentParameterInfo.isEmpty() ? XBParamProcessingState.END
                                    : (currentParameterInfo.attributeCount > 0 ? XBParamProcessingState.ATTRIBUTES : XBParamProcessingState.CHILDREN);

                            return;
                        }

                        throw new XBProcessingException("Unexpected join processing order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }

                    case ATTRIBUTE: {
                        if (currentProcessingState == XBParamProcessingState.ATTRIBUTES) {
                            setNextAttributeToken(((XBTAttributeToken) token).getAttribute());
                            currentParameterInfo.attributeCount--;
                            return;
                        }

                        break;
                    }

                    case DATA: {
                        throw new IllegalStateException("Unexpected data node");
                    }

                    case END: {
                        if (currentProcessingState == XBParamProcessingState.ATTRIBUTES || currentProcessingState == XBParamProcessingState.CHILDREN) {
                            while (currentParameterInfo.attributeCount > 0) {
                                UBNatural zeroAttribute = new UBNat32();
                                setNextAttributeToken(zeroAttribute);
                                currentParameterInfo.attributeCount--;
                            }
                            while (currentParameterInfo.childCount > 0) {
                                throw new UnsupportedOperationException("Not supported yet.");
                            }

                            currentProcessingState = XBParamProcessingState.END;
                            return;
                        }

                        throw new XBProcessingException("Unexpected join processing order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }
                }

                throw new IllegalStateException();
            }

            case CONSIST: {
                if (currentParameterInfo == null) {
                    currentParameterInfo = processParameterInfo();
                    childReader = new XBTTreeReader(getNextChild());
                    XBTListenerToToken.tokenToListener(token, childReader);
                    return;
                } else {
                    throw new XBProcessingException("Parameter already processed", XBProcessingExceptionType.READING_AFTER_END);
                }
            }

            case LIST_JOIN: {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            case LIST_CONSIST: {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }

        throw new XBProcessingException("Unexpected processing state", XBProcessingExceptionType.UNKNOWN);
    }

    private XBTToken getChildToken() throws XBProcessingException, IOException {
        childWriter.produceXBT(childConvertor);
        if (childWriter.isFinished()) {
            childWriter = null;
            currentParameterInfo.childCount--;
        }

        return childConvertor.getToken();
    }

    private ParameterInfo processParameterInfo() {
        processingStates.clear();

        ParameterInfo parameterInfo = new ParameterInfo();

        ProcessingState processingState = new ProcessingState(null);
        XBBlockDef blockDef = position.blockDecl.getBlockDef();

        if (blockDef != null) {
            XBBlockParam paramDecl = blockDef.getBlockParam(currentParameter);
            processingState.blockDecl = (XBCBlockDecl) paramDecl.getBlockDecl();
            parameterInfo.blockDecl = (XBCBlockDecl) paramDecl.getBlockDecl();
            long revision = processingState.blockDecl.getRevision();
            processingState.parametersCount = processingState.blockDecl.getBlockDef().getRevisionDef().getRevisionLimit(revision);
            processingStates.add(processingState);
            processState(parameterInfo);
        }

        return parameterInfo;
    }

    private XBTAttributeToken getNextAttributeToken() {
        if (position.attributeCount >= source.getAttributesCount()) {
            position.attributeCount++;
            return new XBTZeroAttributeToken();
        }

        XBTAttributeToken attributeToken = new XBTAttributeToken(source.getAttributeAt(position.attributeCount));
        position.attributeCount++;
        return attributeToken;
    }

    private void setNextAttributeToken(XBAttribute attribute) {
        ((XBTEditableBlock) source).setAttributeAt(attribute, position.attributeCount);
        position.attributeCount++;
    }

    private XBTTreeNode getNextChild() {
        if (position.childCount >= source.getChildrenCount()) {
            XBTTreeNode emptyNode = new XBTTreeNode();
            emptyNode.setDataMode(XBBlockDataMode.DATA_BLOCK);
            position.childCount++;
            return emptyNode;
        }

        XBTTreeNode childNode = (XBTTreeNode) source.getChildAt(position.childCount);
        position.childCount++;
        return childNode;
    }

    public void setParameterIndex(int targetParameter) throws XBProcessingException {
        if (targetParameter <= currentParameter) {
            reset();
        }

        if (targetParameter > currentParameter) {
            // Finish currently processed parameter
            if (currentParameterInfo != null) {
                position.attributeCount += currentParameterInfo.attributeCount;
                position.childCount += currentParameterInfo.childCount;
                currentParameter++;
                currentParameterInfo = null;
            }

            while (targetParameter > currentParameter) {
                ParameterInfo parameterInfo = processParameterInfo();
                position.attributeCount += parameterInfo.attributeCount;
                position.childCount += parameterInfo.childCount;
                currentParameter++;
            }

            currentProcessingState = XBParamProcessingState.BEGIN;
            currentListProcessingState = XBParamListProcessingState.BEGIN;
        }

        parameterType = null;
        currentParameterInfo = null;
    }

    private void processState(ParameterInfo parameterInfo) {
        do {
            ProcessingState processingState = processingStates.get(processingStates.size() - 1);
            if (processingState.processingParameter < processingState.parametersCount) {
                XBBlockDef blockDef = processingState.blockDecl.getBlockDef();
                XBBlockParam blockParam = blockDef.getBlockParam(processingState.processingParameter);
                if (blockParam != null) {
                    switch (blockParam.getParamType()) {
                        case CONSIST: {
                            parameterInfo.childCount++;
                            break;
                        }

                        case JOIN: {
                            ProcessingState joinState = new ProcessingState((XBCBlockDecl) blockParam.getBlockDecl());
                            if (joinState.blockDecl.getBlockSpecRev() == null) {
                                parameterInfo.attributeCount++;
                            } else {
                                long revision = joinState.blockDecl.getRevision();
                                joinState.parametersCount = joinState.blockDecl.getBlockDef().getRevisionDef().getRevisionLimit(revision);
                                processingStates.add(joinState);
                            }

                            break;
                        }

                        case LIST_CONSIST: {
                            XBAttribute attribute = source.getAttributeAt(position.attributeCount + parameterInfo.attributeCount);
                            UBENatural listSizeNat;
                            if (attribute instanceof UBENatural) {
                                listSizeNat = (UBENatural) attribute;
                            } else {
                                listSizeNat = new UBENat32();
                                listSizeNat.convertFromNatural(attribute.convertToNatural());
                            }
                            parameterInfo.attributeCount++;
                            if (!listSizeNat.isInfinity()) {
                                parameterInfo.childCount += listSizeNat.getInt();
                            } else {
                                throw new UnsupportedOperationException("Not supported yet.");
                            }
                        }

                        case LIST_JOIN: {
                            int listSize = source.getAttributeAt(position.attributeCount + parameterInfo.attributeCount).getNaturalInt();
                            parameterInfo.attributeCount++;
                            while (listSize > 0) {
                                ProcessingState joinState = new ProcessingState((XBCBlockDecl) blockParam.getBlockDecl());
                                if (joinState.blockDecl.getBlockSpecRev() == null) {
                                    parameterInfo.attributeCount++;
                                } else {
                                    long revision = joinState.blockDecl.getRevision();
                                    joinState.parametersCount = joinState.blockDecl.getBlockDef().getRevisionDef().getRevisionLimit(revision);
                                    processingStates.add(joinState);
                                }

                                listSize--;
                            }
                        }
                    }
                }
            }

            if (processingState.processingParameter >= processingState.parametersCount) {
                if (processingStates.size() == 1) {
                    break;
                } else {
                    processingStates.remove(processingStates.size() - 1);
                }
            }

            processingState.processingParameter++;
        } while (true);
    }

    private class ProcessingState {

        public XBCBlockDecl blockDecl = null;
        public int processingParameter = 0;
        public int parametersCount = 0;

        public ProcessingState(XBCBlockDecl blockDecl) {
            this.blockDecl = blockDecl;
        }
    }

    private class ParameterInfo {

        public XBBlockDecl blockDecl = null;
        public int attributeCount = 0;
        public int childCount = 0;
        public int listSize;

        public boolean isEmpty() {
            return attributeCount == 0 && childCount == 0;
        }
    }
}
