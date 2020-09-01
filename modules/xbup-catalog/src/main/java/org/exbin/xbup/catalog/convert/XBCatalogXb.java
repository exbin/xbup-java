/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.catalog.convert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.catalog.entity.service.XBENodeService;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCXDesc;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXHDoc;
import org.exbin.xbup.core.catalog.base.XBCXIcon;
import org.exbin.xbup.core.catalog.base.XBCXName;
import org.exbin.xbup.core.catalog.base.XBCXStri;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;
import org.exbin.xbup.core.catalog.base.service.XBCSpecService;
import org.exbin.xbup.core.catalog.base.service.XBCXDescService;
import org.exbin.xbup.core.catalog.base.service.XBCXFileService;
import org.exbin.xbup.core.catalog.base.service.XBCXHDocService;
import org.exbin.xbup.core.catalog.base.service.XBCXIconService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.core.catalog.base.service.XBCXStriService;
import org.exbin.xbup.core.parser.XBParserMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTokenType;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.event.XBEventWriter;
import org.exbin.xbup.core.parser.token.event.XBTEventFilter;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;
import org.exbin.xbup.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.exbin.xbup.core.parser.token.pull.XBPullReader;
import org.exbin.xbup.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.type.XBString;

/**
 * XB Catalog import and export to XB.
 *
 * TODO: Block types
 *
 * @version 0.2.1 2020/09/01
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBCatalogXb {

    private XBACatalog catalog;

    public XBCatalogXb() {
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    public void exportToXb(OutputStream stream) {
        try {
            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(new TypeDroppingFilter(new XBTToXBEventConvertor(new XBEventWriter(stream, XBParserMode.SINGLE_BLOCK))));

            exportAll(serialInput);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBCatalogXb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void exportAll(XBPListenerSerialHandler serialInput) throws XBProcessingException, IOException {
        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());

        XBCNodeService<XBCNode> nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCNode mainRootNode = nodeService.getMainRootNode().get();
        exportAllNodes(serialInput, mainRootNode);
//        exportAllFiles(serialInput);
//        exportProperties(serialInput, CatalogItemType.NODE);
//
//        exportAllSpecs(serialInput, mainRootNode, CatalogItemType.BLOCK);
//        exportProperties(serialInput, CatalogItemType.BLOCK);
//        exportAllSpecs(serialInput, mainRootNode, CatalogItemType.GROUP);
//        exportProperties(serialInput, CatalogItemType.GROUP);
//        exportAllSpecs(serialInput, mainRootNode, CatalogItemType.FORMAT);
//        exportProperties(serialInput, CatalogItemType.FORMAT);
        serialInput.end();
    }

    private void exportAllNodes(XBPListenerSerialHandler serialInput, XBCNode node) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);

        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());
        serialInput.putAttribute(node.getXBIndex());
        putPath(serialInput, nodeService.getNodeXBPath(node));
        serialInput.end();

        List<XBCNode> subNodes = nodeService.getSubNodes(node);
        for (XBCNode subNode : subNodes) {
            exportAllNodes(serialInput, subNode);
        }
    }

    private void exportAllSpecs(XBPListenerSerialHandler serialInput, XBCNode node, CatalogItemType itemType) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
        XBCRevService revService = catalog.getCatalogService(XBCRevService.class);

        Long[] nodePath = nodeService.getNodeXBPath(node);

        List<XBCItem> items = getItems(serialInput, itemType, node);

        for (XBCItem item : items) {
            serialInput.begin();
            serialInput.putType(new XBFixedBlockType());
            serialInput.putAttribute(item.getXBIndex());
            putPath(serialInput, nodePath);

            List<XBCRev> revs = revService.getRevs((XBCSpec) item);
            for (XBCRev specRev : revs) {
                serialInput.begin();
                serialInput.putType(new XBFixedBlockType());
                serialInput.putAttribute(specRev.getXBIndex());
                serialInput.putAttribute(specRev.getXBLimit());

                // List<XBCSpecDef> specDefs = specService.getSpecDefs((XBCSpec) item);
//                XBSerializable decl = item instanceof XBCFormatSpec ? specService.getFormatDeclAsLocal(new XBCFormatDecl((XBCFormatRev) specRev, catalog))
//                        : item instanceof XBCGroupSpec ? specService.getGroupDeclAsLocal(new XBCGroupDecl((XBCGroupRev) specRev, catalog))
//                                : item instanceof XBCBlockSpec ? specService.getBlockDeclAsLocal(new XBCBlockDecl((XBCBlockRev) specRev, catalog)) : null;
//                serialInput.consist(decl);
                serialInput.end();
            }

            serialInput.end();
        }

        List<XBCNode> subNodes = nodeService.getSubNodes(node);
        for (XBCNode subNode : subNodes) {
            exportAllSpecs(serialInput, subNode, itemType);
        }
    }

    private void exportProperties(XBPListenerSerialHandler serialInput, CatalogItemType itemType) throws XBProcessingException, IOException {
        exportNames(serialInput, itemType);
        exportDescs(serialInput, itemType);
        exportStri(serialInput, itemType);
        exportHDocs(serialInput, itemType);
        exportIcons(serialInput, itemType);
    }

    private void exportAllFiles(XBPListenerSerialHandler serialInput) throws XBProcessingException, IOException {
        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());

        XBCNodeService<XBCNode> nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCNode mainRootNode = nodeService.getMainRootNode().get();
        exportAllFiles(serialInput, mainRootNode);

        serialInput.end();
    }

    private void exportAllFiles(XBPListenerSerialHandler serialInput, XBCNode node) throws XBProcessingException, IOException {
        XBCNodeService<XBCNode> nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCXFileService<XBCXFile> fileService = catalog.getCatalogService(XBCXFileService.class);
        Long[] nodePath = nodeService.getNodeXBPath(node);

        List<XBCXFile> files = fileService.findFilesForNode(node);
        for (XBCXFile file : files) {
            serialInput.begin();
            serialInput.putType(new XBFixedBlockType());
            putPath(serialInput, nodePath);
            serialInput.consist(new XBString(file.getFilename()));

            try (ByteArrayInputStream stream = new ByteArrayInputStream(file.getContent())) {
                serialInput.begin();
                serialInput.putData(stream);
                serialInput.end();
            }

            serialInput.end();
        }

        List<XBCNode> subNodes = nodeService.getSubNodes(node);
        for (XBCNode subNode : subNodes) {
            exportAllFiles(serialInput, subNode);
        }
    }

    private void exportNames(XBPListenerSerialHandler serialInput, CatalogItemType itemType) throws XBProcessingException, IOException {
        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());

        XBCNodeService<XBCNode> nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCNode mainRootNode = nodeService.getMainRootNode().get();
        exportNames(serialInput, itemType, mainRootNode);

        serialInput.end();
    }

    private void exportNames(XBPListenerSerialHandler serialInput, CatalogItemType itemType, XBCNode node) throws XBProcessingException, IOException {
        XBCNodeService<XBCNode> nodeService = catalog.getCatalogService(XBCNodeService.class);
        Long[] nodePath = nodeService.getNodeXBPath(node);

        List<XBCItem> items = getItems(serialInput, itemType, node);

        XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
        for (XBCItem item : items) {
            List<XBCXName> itemNames = nameService.getItemNames(item);
            for (XBCXName itemName : itemNames) {
                serialInput.begin();
                serialInput.putType(new XBFixedBlockType());
                serialInput.putAttribute(item.getXBIndex());
                putPath(serialInput, nodePath);
                serialInput.consist(new XBString(itemName.getLang().getLangCode()));
                serialInput.consist(new XBString(itemName.getText()));
                serialInput.end();
            }
        }

        List<XBCNode> subNodes = nodeService.getSubNodes(node);
        for (XBCNode subNode : subNodes) {
            exportNames(serialInput, itemType, subNode);
        }
    }

    private void exportDescs(XBPListenerSerialHandler serialInput, CatalogItemType itemType) throws XBProcessingException, IOException {
        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());

        XBCNodeService<XBCNode> nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCNode mainRootNode = nodeService.getMainRootNode().get();
        exportDescs(serialInput, itemType, mainRootNode);

        serialInput.end();
    }

    private void exportDescs(XBPListenerSerialHandler serialInput, CatalogItemType itemType, XBCNode node) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        Long[] nodePath = nodeService.getNodeXBPath(node);

        List<XBCItem> items = getItems(serialInput, itemType, node);

        XBCXDescService nameService = catalog.getCatalogService(XBCXDescService.class);
        for (XBCItem item : items) {
            List<XBCXDesc> itemDescs = nameService.getItemDescs(item);
            for (XBCXDesc itemDesc : itemDescs) {
                serialInput.begin();
                serialInput.putType(new XBFixedBlockType());
                serialInput.putAttribute(item.getXBIndex());
                putPath(serialInput, nodePath);
                serialInput.consist(new XBString(itemDesc.getLang().getLangCode()));
                serialInput.consist(new XBString(itemDesc.getText()));
                serialInput.end();
            }
        }

        List<XBCNode> subNodes = nodeService.getSubNodes(node);
        for (XBCNode subNode : subNodes) {
            exportDescs(serialInput, itemType, subNode);
        }
    }

    private void exportStri(XBPListenerSerialHandler serialInput, CatalogItemType itemType) throws XBProcessingException, IOException {
        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());

        XBCNodeService<XBCNode> nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCNode mainRootNode = nodeService.getMainRootNode().get();
        exportStri(serialInput, itemType, mainRootNode);

        serialInput.end();
    }

    private void exportStri(XBPListenerSerialHandler serialInput, CatalogItemType itemType, XBCNode node) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        Long[] nodePath = nodeService.getNodeXBPath(node);

        List<XBCItem> items = getItems(serialInput, itemType, node);

        XBCXStriService striService = catalog.getCatalogService(XBCXStriService.class);
        for (XBCItem item : items) {
            XBCXStri stri = striService.getItemStringId(item);
            if (stri != null) {
                serialInput.begin();
                serialInput.putType(new XBFixedBlockType());
                serialInput.putAttribute(item.getXBIndex());
                putPath(serialInput, nodePath);
                serialInput.consist(new XBString(stri.getText()));
                serialInput.end();
            }
        }

        List<XBCNode> subNodes = nodeService.getSubNodes(node);
        for (XBCNode subNode : subNodes) {
            exportStri(serialInput, itemType, subNode);
        }
    }

    private void exportHDocs(XBPListenerSerialHandler serialInput, CatalogItemType itemType) throws XBProcessingException, IOException {
        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());

        XBCNodeService<XBCNode> nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCNode mainRootNode = nodeService.getMainRootNode().get();
        exportHDocs(serialInput, itemType, mainRootNode);

        serialInput.end();
    }

    private void exportHDocs(XBPListenerSerialHandler serialInput, CatalogItemType itemType, XBCNode node) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        Long[] nodePath = nodeService.getNodeXBPath(node);

        List<XBCItem> items = getItems(serialInput, itemType, node);

        XBCXHDocService docService = catalog.getCatalogService(XBCXHDocService.class);
        for (XBCItem item : items) {
            XBCXHDoc itemHDoc = docService.getDocumentation(item);
            if (itemHDoc != null) {
                serialInput.begin();
                serialInput.putType(new XBFixedBlockType());
                serialInput.putAttribute(item.getXBIndex());
                putPath(serialInput, nodePath);
                serialInput.consist(new XBString(itemHDoc.getLang().getLangCode()));
                putPath(serialInput, nodeService.getNodeXBPath(itemHDoc.getDocFile().getNode()));
                serialInput.consist(new XBString(itemHDoc.getDocFile().getFilename()));
                serialInput.end();
            }
        }

        List<XBCNode> subNodes = nodeService.getSubNodes(node);
        for (XBCNode subNode : subNodes) {
            exportHDocs(serialInput, itemType, subNode);
        }
    }

    private void exportIcons(XBPListenerSerialHandler serialInput, CatalogItemType itemType) throws XBProcessingException, IOException {
        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());

        XBCNodeService<XBCNode> nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCNode mainRootNode = nodeService.getMainRootNode().get();
        exportIcons(serialInput, itemType, mainRootNode);

        serialInput.end();
    }

    private void exportIcons(XBPListenerSerialHandler serialInput, CatalogItemType itemType, XBCNode node) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        Long[] nodePath = nodeService.getNodeXBPath(node);

        List<XBCItem> items = getItems(serialInput, itemType, node);

        XBCXIconService iconService = catalog.getCatalogService(XBCXIconService.class);
        for (XBCItem item : items) {
            List<XBCXIcon> icons = iconService.getItemIcons(item);
            for (XBCXIcon icon : icons) {
                serialInput.begin();
                serialInput.putType(new XBFixedBlockType());
                serialInput.putAttribute(icon.getMode().getType());
                putPath(serialInput, nodePath);
                putPath(serialInput, nodeService.getNodeXBPath(icon.getIconFile().getNode()));
                serialInput.consist(new XBString(icon.getIconFile().getFilename()));

                serialInput.end();
            }
        }

        List<XBCNode> subNodes = nodeService.getSubNodes(node);
        for (XBCNode subNode : subNodes) {
            exportIcons(serialInput, itemType, subNode);
        }
    }

    @Nonnull
    private List<XBCItem> getItems(XBPListenerSerialHandler serialInput, CatalogItemType itemType, XBCNode node) {
        List<XBCItem> result = new ArrayList<>();
        switch (itemType) {
            case NODE: {
                result.add(node);
                break;
            }
            case BLOCK: {
                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                List<XBCBlockSpec> blockSpecs = specService.getBlockSpecs(node);
                result.addAll(blockSpecs);
                break;
            }
            case GROUP: {
                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                List<XBCGroupSpec> groupSpecs = specService.getGroupSpecs(node);
                result.addAll(groupSpecs);
                break;
            }
            case FORMAT: {
                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                List<XBCFormatSpec> formatSpecs = specService.getFormatSpecs(node);
                result.addAll(formatSpecs);
                break;
            }
        }

        return result;
    }

    private void putPath(XBPListenerSerialHandler serialInput, Long[] nodeXBPath) throws XBProcessingException, IOException {
        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());
        serialInput.putAttribute(nodeXBPath.length);
        for (Long value : nodeXBPath) {
            serialInput.putAttribute(value);
        }
        serialInput.end();
    }

    public void importFromXb(InputStream stream) {
        try {
            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(new XBToXBTPullConvertor(new XBPullReader(stream, XBParserMode.SINGLE_BLOCK)));
            
            importAll(serialOutput);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBCatalogXb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void importAll(XBPProviderSerialHandler serialOutput) throws XBProcessingException, IOException {
        serialOutput.begin();
        serialOutput.pullType();

        importAllNodes(serialOutput);
//        importAllFiles(serialOutput);
//        importProperties(serialOutput, CatalogItemType.NODE);
//
//        importAllSpecs(serialOutput, mainRootNode, CatalogItemType.BLOCK);
//        importProperties(serialOutput, CatalogItemType.BLOCK);
//        importAllSpecs(serialOutput, mainRootNode, CatalogItemType.GROUP);
//        importProperties(serialOutput, CatalogItemType.GROUP);
//        importAllSpecs(serialOutput, mainRootNode, CatalogItemType.FORMAT);
//        importProperties(serialOutput, CatalogItemType.FORMAT);
        serialOutput.pullEnd();
    }

    private void importAllNodes(XBPProviderSerialHandler serialOutput) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);

        while (!serialOutput.isFinishedNext()) {
            serialOutput.pullBegin();
            serialOutput.pullType();
            long xbIndex = serialOutput.pullAttribute().getNaturalLong();
            Long[] nodePath = pullPath(serialOutput);
            XBENode parentNode = (XBENode) nodeService.findOwnerByXBPath(nodePath);
            serialOutput.pullEnd();
            XBENode node = (XBENode) nodeService.createItem();
            node.setParent(parentNode);
            node.setXBIndex(xbIndex);
            nodeService.persistItem(node);
        }
    }

    @Nonnull
    private Long[] pullPath(XBPProviderSerialHandler serialOutput) throws XBProcessingException, IOException {
        serialOutput.pullBegin();
        serialOutput.pullType();
        int length = serialOutput.pullAttribute().getNaturalInt();
        Long[] result = new Long[length];
        for (int i = 0; i < length; i++) {
            result[i] = serialOutput.pullAttribute().getNaturalLong();
        }
        serialOutput.pullEnd();
        return result;
    }

    @ParametersAreNonnullByDefault
    private static final class TypeDroppingFilter implements XBTEventFilter {

        private XBTEventListener listener;

        public TypeDroppingFilter(XBTEventListener listener) {
            this.listener = listener;
        }

        @Override
        public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
            if (token.getTokenType() == XBTTokenType.TYPE) {
                listener.putXBTToken(new XBTTypeToken() {
                    @Override
                    public XBBlockType getBlockType() {
                        return new XBFixedBlockType();
                    }
                });
            } else {
                listener.putXBTToken(token);
            }
        }

        @Override
        public void attachXBTEventListener(XBTEventListener eventListener) {
            this.listener = eventListener;
        }
    }
}
