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
package org.xbup.lib.core.parser.param;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBDBlockDecl;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.block.declaration.XBParamDecl;
import org.xbup.lib.core.block.definition.XBBlockDef;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.declaration.XBCBlockDecl;
import org.xbup.lib.core.catalog.declaration.XBCBlockDef;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBENat32;

/**
 * Basic filter providing parameter sequence from typed tokens.
 *
 * @version 0.1.23 2013/11/25
 * @author XBUP Project (http://xbup.org)
 */
public class XBParamConvertor implements XBTListener, XBParamProducer {

    private XBParamListener paramListener;
    private final XBACatalog catalog;

    private XBBlockDef blockDef;
    private XBParamDecl nextParam;
    private XBParamProcessor paramProcessor;
    private int paramIndex;
    private long depthLevel;

    public XBParamConvertor(XBParamListener paramListener, XBACatalog catalog) {
        this.paramListener = paramListener;
        this.catalog = catalog;
        nextParam = null;
        blockDef = null;
        paramProcessor = null;
        paramIndex = 0;
        depthLevel = 0;
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        depthLevel++;
    }

    @Override
    public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
        if (depthLevel != 1) {
            return;
        }

        if (type instanceof XBDeclBlockType) {
            XBBlockDecl decl = ((XBDeclBlockType) type).getBlockDecl();
            if (decl instanceof XBCBlockDecl) {
                blockDef = new XBCBlockDef(catalog, ((XBCBlockDecl) decl).getBlockSpec(catalog));
            } else if (decl instanceof XBDBlockDecl) {
                blockDef = ((XBDBlockDecl) decl).getBlockDef();
            }

            paramIndex = 0;
            while ((nextParam == null)&&(paramIndex < blockDef.getParamCount())) {
                nextParam = blockDef.getParamDecl(paramIndex);
                paramListener.beginXBParam(nextParam);
                paramProcessor = new XBParamProcessor(nextParam);
                paramProcessor.attachXBParamListener(paramListener);
                if (paramProcessor.paramDecl == null) {
                    nextParam = null;
                }

                if (nextParam == null) {
                    paramIndex++;
                }
            }
        } else {
            blockDef = null;
        }
    }

    @Override
    public void attribXBT(UBNatural value) throws XBProcessingException, IOException {
        if (paramProcessor != null) {
            paramProcessor.attribXBT(value);
        } else {
            while (paramIndex < blockDef.getParamCount()) {
                nextParam = blockDef.getParamDecl(paramIndex);
                paramListener.beginXBParam(nextParam);
                paramProcessor = new XBParamProcessor(nextParam);
                paramProcessor.attachXBParamListener(paramListener);
// TODO                if (paramProcessor.paramDecl == null) nextParam = null;
                paramIndex++;

                if (paramProcessor != null) {
                    paramProcessor.attribXBT(value);
                    break;
                }
            }
        }
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        if (depthLevel == 0) {
            throw new XBProcessingException("Unexpected block end event");
        }

        depthLevel--;
    }

    @Override
    public void attachXBParamListener(XBParamListener listener) {
        paramListener = listener;
    }

    /**
     * Parameter processor accepting attributes and returning parameter events.
     * 
     * It uses depth processing with connected instances.
     */
    public class XBParamProcessor implements XBParamProducer {

        private XBParamDecl paramDecl = null;
        private XBParamListener listener;
        private XBParamProcessor childProcessor;
        private int position;

        private UBENat32 listCount;

        public XBParamProcessor(XBParamDecl paramDecl) throws XBProcessingException, IOException {
            this.paramDecl = paramDecl;
            childProcessor = null;
            position = 0;
            listCount = null;
        }

        public void attribXBT(UBNatural value) throws XBProcessingException, IOException {
            if (paramDecl == null) {
                return;
            } // Ignore throw new XBProcessingException("Attribute after the end in the parameter processing");

            if (childProcessor != null) {
                childProcessor.attribXBT(value);
                return;
            }

            if ((position == 0)&&(paramDecl.isListFlag())) {
                if (paramDecl.isJoinFlag()) {
                    listCount = new UBENat32(value.getLong());
                    while ((childProcessor == null)&&(listCount.getLong() == 0)) {
                        childProcessor = new XBParamProcessor(paramDecl.convertToNonList());
                        childProcessor.attachXBParamListener(new XBParamHandler(this));
                        listCount = new UBENat32(listCount.getLong() - 1);
                    }

                    if (listCount.getLong() == 0) {
                        listener.endXBParam();
                        this.paramDecl = null;
                        return;
                    }

                    position++;
                } else {
                    listCount = UBENat32.ConvertFromNatural(value);
                    if (listCount.isInfinity()) {
                        listener.listXBParam();
                    } else {
                        while (listCount.getLong() != 0) {
                            listener.blockXBParam();
                            listCount = new UBENat32(listCount.getLong() - 1);
                        }
                    }

                    listener.endXBParam();
                    this.paramDecl = null;
                }
                return;
            }

            if (paramDecl.isListFlag()) {
                if (paramDecl.isJoinFlag()) {
                    throw new XBProcessingException("Internal error", XBProcessingExceptionType.UNKNOWN);
                }

                while ((childProcessor == null)&&(listCount.getLong() == 0)) {
                    childProcessor = new XBParamProcessor(paramDecl.convertToNonList());
                    childProcessor.attachXBParamListener(new XBParamHandler(this));
                    listCount = new UBENat32(listCount.getLong() - 1);
                }

                if (listCount.getLong() == 0) {
                    listener.endXBParam();
                    this.paramDecl = null;
                } else {
                    childProcessor.attribXBT(value);
                }

                return;
            }

            if (paramDecl.isJoinFlag()) {
                if (paramDecl.getBlockDecl() == null) {
                    // Single attribute
                    listener.endXBParam();
                    this.paramDecl = null;
                } else {
                    List<XBParamDecl> params = blockDef.getParamDecls();
                    // TODO: Revision processing
                    while ((childProcessor == null)&&(position < params.size())) {
                        nextChildProcessor();
                        if (childProcessor != null) {
                            childProcessor.attribXBT(value);
                            break;
                        }
                    }

                    if (childProcessor == null) {
                        listener.endXBParam();
                        this.paramDecl = null;
                    }
                }
            } else {
                throw new XBProcessingException("Internal error");
            }
        }

        /**
         * Associate next child processor.
         */
        private void nextChildProcessor() throws XBProcessingException, IOException {
            List<XBParamDecl> params = blockDef.getParamDecls();
            XBParamDecl childParam = params.get(position);
            listener.beginXBParam(childParam);
            childProcessor = new XBParamProcessor(childParam);
            childProcessor.attachXBParamListener(new XBParamHandler(this));
            position++;
        }

        @Override
        public void attachXBParamListener(XBParamListener listener) {
            try {
                this.listener = listener;
                if ((paramDecl == null) || ((!paramDecl.isListFlag()) && (!paramDecl.isJoinFlag()))) {
                    // Any block or data block
                    listener.blockXBParam();
                    listener.endXBParam();
                    this.paramDecl = null;
                    return;
                }

                if (!paramDecl.isListFlag()) {
                    nextChildProcessor();
                }
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBParamConvertor.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*try {
                listener.endXBParam();
            } catch (XBProcessingException ex) {
                Logger.getLogger(XBParamConvertor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(XBParamConvertor.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
    }

    /**
     * Handler class for parameter sequence processing.
     */
    public class XBParamHandler implements XBParamListener {

        private final XBParamProcessor processor;

        public XBParamHandler(XBParamProcessor processor) {
            this.processor = processor;
        }

        @Override
        public void beginXBParam(XBParamDecl type) throws XBProcessingException, IOException {
        }

        @Override
        public void blockXBParam() throws XBProcessingException, IOException {
            processor.listener.blockXBParam();
        }

        @Override
        public void listXBParam() throws XBProcessingException, IOException {
            processor.listener.listXBParam();
        }

        @Override
        public void endXBParam() throws XBProcessingException, IOException {
            processor.childProcessor = null;
        }
    }
}
