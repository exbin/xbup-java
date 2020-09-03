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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.catalog.entity.XBEBlockSpec;
import org.exbin.xbup.catalog.entity.XBEFormatSpec;
import org.exbin.xbup.catalog.entity.XBEGroupSpec;
import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.catalog.entity.XBEXDesc;
import org.exbin.xbup.catalog.entity.XBEXFile;
import org.exbin.xbup.catalog.entity.XBEXHDoc;
import org.exbin.xbup.catalog.entity.XBEXIcon;
import org.exbin.xbup.catalog.entity.XBEXName;
import org.exbin.xbup.catalog.entity.XBEXPlugin;
import org.exbin.xbup.catalog.entity.XBEXStri;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCSpecDef;
import org.exbin.xbup.core.catalog.base.XBCXBlockUi;
import org.exbin.xbup.core.catalog.base.XBCXDesc;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXHDoc;
import org.exbin.xbup.core.catalog.base.XBCXIcon;
import org.exbin.xbup.core.catalog.base.XBCXIconMode;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.XBCXName;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.XBCXStri;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;
import org.exbin.xbup.core.catalog.base.service.XBCSpecService;
import org.exbin.xbup.core.catalog.base.service.XBCXDescService;
import org.exbin.xbup.core.catalog.base.service.XBCXFileService;
import org.exbin.xbup.core.catalog.base.service.XBCXHDocService;
import org.exbin.xbup.core.catalog.base.service.XBCXIconService;
import org.exbin.xbup.core.catalog.base.service.XBCXLangService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.core.catalog.base.service.XBCXPlugService;
import org.exbin.xbup.core.catalog.base.service.XBCXStriService;
import org.exbin.xbup.core.catalog.base.service.XBCXUiService;
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
import org.exbin.xbup.core.util.StreamUtils;

/**
 * XB Catalog import and export to XB.
 *
 * @version 0.2.1 2020/09/03
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

        exportAllNodes(serialInput);
        exportAllFiles(serialInput);
        exportAllProperties(serialInput, CatalogItemType.NODE);

        exportAllSpecs(serialInput, CatalogItemType.BLOCK);
        exportAllProperties(serialInput, CatalogItemType.BLOCK);
        exportAllSpecs(serialInput, CatalogItemType.GROUP);
        exportAllProperties(serialInput, CatalogItemType.GROUP);
        exportAllSpecs(serialInput, CatalogItemType.FORMAT);
        exportAllProperties(serialInput, CatalogItemType.FORMAT);

        exportAllPlugins(serialInput);
        exportAllUis(serialInput);
        serialInput.end();
    }

    private void exportAllNodes(XBPListenerSerialHandler serialInput) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCNode mainRootNode = nodeService.getMainRootNode().get();

        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());
        exportNodes(serialInput, mainRootNode);
        serialInput.end();
    }

    private void exportNodes(XBPListenerSerialHandler serialInput, XBCNode node) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);

        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());
        serialInput.putAttribute(node.getXBIndex());
        putPath(serialInput, nodeService.getNodeXBPath(node));
        serialInput.end();

        List<XBCNode> subNodes = nodeService.getSubNodes(node);
        for (XBCNode subNode : subNodes) {
            exportNodes(serialInput, subNode);
        }
    }

    private void exportAllFiles(XBPListenerSerialHandler serialInput) throws XBProcessingException, IOException {
        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());

        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCNode mainRootNode = nodeService.getMainRootNode().get();
        exportFiles(serialInput, mainRootNode);

        serialInput.end();
    }

    private void exportFiles(XBPListenerSerialHandler serialInput, XBCNode node) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCXFileService fileService = catalog.getCatalogService(XBCXFileService.class);
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
            exportFiles(serialInput, subNode);
        }
    }

    private void exportAllSpecs(XBPListenerSerialHandler serialInput, CatalogItemType itemType) throws XBProcessingException, IOException {
        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());

        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCNode mainRootNode = nodeService.getMainRootNode().get();
        exportSpecs(serialInput, mainRootNode, itemType);

        serialInput.end();
    }

    private void exportSpecs(XBPListenerSerialHandler serialInput, XBCNode node, CatalogItemType itemType) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
        XBCRevService revService = catalog.getCatalogService(XBCRevService.class);

        Long[] nodePath = nodeService.getNodeXBPath(node);

        List<XBCItem> items = getItems(itemType, node);

        for (XBCItem item : items) {
            serialInput.begin();
            serialInput.putType(new XBFixedBlockType());
            serialInput.putAttribute(item.getXBIndex());
            putPath(serialInput, nodePath);

//            {
//                serialInput.begin();
//                serialInput.putType(new XBFixedBlockType());
//                List<XBCRev> revs = revService.getRevs((XBCSpec) item);
//                for (XBCRev specRev : revs) {
//                    serialInput.begin();
//                    serialInput.putType(new XBFixedBlockType());
//                    serialInput.putAttribute(specRev.getXBIndex());
//                    serialInput.putAttribute(specRev.getXBLimit());
//                    serialInput.end();
//                }
//                serialInput.end();
//            }
//
//            {
//                serialInput.begin();
//                serialInput.putType(new XBFixedBlockType());
//                List<XBCSpecDef> specDefs = specService.getSpecDefs((XBCSpec) item);
//                for (XBCSpecDef specDef : specDefs) {
//                    serialInput.begin();
//                    serialInput.putType(new XBFixedBlockType());
//                    serialInput.putAttribute(specDef.getType().ordinal());
//                    serialInput.putAttribute(specDef.getXBIndex());
//                    XBCRev targetRev = specDef.getTargetRev().orElse(null);
//                    if (targetRev != null) {
//                        XBCSpec targetSpec = targetRev.getParent();
//                        XBCNode targetSpecNode = targetSpec.getParent();
//                        Long[] targetNodePath = nodeService.getNodeXBPath(targetSpecNode);
//                        putPath(serialInput, targetNodePath);
//                        serialInput.putAttribute(targetSpec.getXBIndex());
//                        serialInput.putAttribute(targetRev.getXBIndex());
//                    }
//                    serialInput.end();
//                }
//                serialInput.end();
//            }

            serialInput.end();
        }

        List<XBCNode> subNodes = nodeService.getSubNodes(node);
        for (XBCNode subNode : subNodes) {
            exportSpecs(serialInput, subNode, itemType);
        }
    }

    private void exportAllProperties(XBPListenerSerialHandler serialInput, CatalogItemType itemType) throws XBProcessingException, IOException {
        exportAllNames(serialInput, itemType);
        exportAllDescs(serialInput, itemType);
        exportAllStri(serialInput, itemType);
        exportAllHDocs(serialInput, itemType);
        exportAllIcons(serialInput, itemType);
    }

    private void exportAllNames(XBPListenerSerialHandler serialInput, CatalogItemType itemType) throws XBProcessingException, IOException {
        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());

        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCNode mainRootNode = nodeService.getMainRootNode().get();
        exportNames(serialInput, itemType, mainRootNode);

        serialInput.end();
    }

    private void exportNames(XBPListenerSerialHandler serialInput, CatalogItemType itemType, XBCNode node) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        Long[] nodePath = nodeService.getNodeXBPath(node);

        List<XBCItem> items = getItems(itemType, node);

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

    private void exportAllDescs(XBPListenerSerialHandler serialInput, CatalogItemType itemType) throws XBProcessingException, IOException {
        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());

        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCNode mainRootNode = nodeService.getMainRootNode().get();
        exportDescs(serialInput, itemType, mainRootNode);

        serialInput.end();
    }

    private void exportDescs(XBPListenerSerialHandler serialInput, CatalogItemType itemType, XBCNode node) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        Long[] nodePath = nodeService.getNodeXBPath(node);

        List<XBCItem> items = getItems(itemType, node);

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

    private void exportAllStri(XBPListenerSerialHandler serialInput, CatalogItemType itemType) throws XBProcessingException, IOException {
        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());

        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCNode mainRootNode = nodeService.getMainRootNode().get();
        exportStri(serialInput, itemType, mainRootNode);

        serialInput.end();
    }

    private void exportStri(XBPListenerSerialHandler serialInput, CatalogItemType itemType, XBCNode node) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        Long[] nodePath = nodeService.getNodeXBPath(node);

        List<XBCItem> items = getItems(itemType, node);

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

    private void exportAllHDocs(XBPListenerSerialHandler serialInput, CatalogItemType itemType) throws XBProcessingException, IOException {
        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());

        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCNode mainRootNode = nodeService.getMainRootNode().get();
        exportHDocs(serialInput, itemType, mainRootNode);

        serialInput.end();
    }

    private void exportHDocs(XBPListenerSerialHandler serialInput, CatalogItemType itemType, XBCNode node) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        Long[] nodePath = nodeService.getNodeXBPath(node);

        List<XBCItem> items = getItems(itemType, node);

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

    private void exportAllIcons(XBPListenerSerialHandler serialInput, CatalogItemType itemType) throws XBProcessingException, IOException {
        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());

        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCNode mainRootNode = nodeService.getMainRootNode().get();
        exportIcons(serialInput, itemType, mainRootNode);

        serialInput.end();
    }

    private void exportIcons(XBPListenerSerialHandler serialInput, CatalogItemType itemType, XBCNode node) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        Long[] nodePath = nodeService.getNodeXBPath(node);

        List<XBCItem> items = getItems(itemType, node);

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

    private void exportAllPlugins(XBPListenerSerialHandler serialInput) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCXPlugService plugService = catalog.getCatalogService(XBCXPlugService.class);

        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());

        List<XBCXPlugin> plugins = plugService.getAllItems();
        for (XBCXPlugin plugin : plugins) {
            serialInput.begin();
            serialInput.putType(new XBFixedBlockType());
            XBCNode node = plugin.getOwner();
            Long[] nodePath = nodeService.getNodeXBPath(node);
            putPath(serialInput, nodePath);
            serialInput.putAttribute(plugin.getPluginIndex());
            XBCXFile pluginFile = plugin.getPluginFile();
            XBCNode fileNode = pluginFile.getNode();
            Long[] fileNodePath = nodeService.getNodeXBPath(fileNode);
            putPath(serialInput, fileNodePath);
            XBString fileName = new XBString(pluginFile.getFilename());
            serialInput.putConsist(fileName);
            serialInput.end();
            break;
        }
        serialInput.end();
    }

    private void exportAllUis(XBPListenerSerialHandler serialInput) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCXUiService uiService = catalog.getCatalogService(XBCXUiService.class);
        XBCXPlugService plugService = catalog.getCatalogService(XBCXPlugService.class);

        serialInput.begin();
        serialInput.putType(new XBFixedBlockType());

        List<XBCXPlugin> plugins = plugService.getAllItems();
        for (XBCXPlugin plugin : plugins) {
            List<XBCXPlugUi> plugUis = uiService.getPlugUis(plugin);
            for (XBCXPlugUi plugUi : plugUis) {
                serialInput.begin();
                serialInput.putType(new XBFixedBlockType());
                XBCNode node = plugin.getOwner();
                Long[] nodePath = nodeService.getNodeXBPath(node);
                putPath(serialInput, nodePath);
                serialInput.putAttribute(plugin.getPluginIndex());
                serialInput.putAttribute(plugUi.getUiType().getId());
                serialInput.putAttribute(plugUi.getMethodIndex());
                XBString name = new XBString(plugUi.getName());
                serialInput.putConsist(name);
                serialInput.end();
            }
        }

        List<XBCXBlockUi> blockUis = uiService.getAllItems();
        for (XBCXBlockUi blockUi : blockUis) {
            serialInput.begin();
            serialInput.putType(new XBFixedBlockType());
            XBCXPlugUi plugUi = blockUi.getUi();
            XBCXPlugin plugin = plugUi.getPlugin();
            XBCNode node = plugin.getOwner();
            Long[] nodePath = nodeService.getNodeXBPath(node);
            putPath(serialInput, nodePath);
            serialInput.putAttribute(plugin.getPluginIndex());
            serialInput.putAttribute(plugUi.getMethodIndex());
            XBCBlockRev blockRev = blockUi.getBlockRev();
            XBCBlockSpec blockSpec = blockRev.getParent();
            XBCNode specNode = blockSpec.getParent();
            Long[] specNodePath = nodeService.getNodeXBPath(specNode);
            putPath(serialInput, specNodePath);
            serialInput.putAttribute(blockSpec.getXBIndex());
            serialInput.putAttribute(blockRev.getXBIndex());
            serialInput.putAttribute(blockUi.getPriority());
            XBString name = new XBString(blockUi.getName());
            serialInput.putConsist(name);
            serialInput.end();
        }
        serialInput.end();
    }

    @Nonnull
    private List<XBCItem> getItems(CatalogItemType itemType, XBCNode node) {
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

        importNodes(serialOutput);
        importFiles(serialOutput);
        importProperties(serialOutput, CatalogItemType.NODE);

        importSpecs(serialOutput, CatalogItemType.BLOCK);
        importProperties(serialOutput, CatalogItemType.BLOCK);
        importSpecs(serialOutput, CatalogItemType.GROUP);
        importProperties(serialOutput, CatalogItemType.GROUP);
        importSpecs(serialOutput, CatalogItemType.FORMAT);
        importProperties(serialOutput, CatalogItemType.FORMAT);

        importPlugins(serialOutput);
        importUis(serialOutput);
        serialOutput.pullEnd();
    }

    private void importNodes(XBPProviderSerialHandler serialOutput) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);

        serialOutput.pullBegin();
        serialOutput.pullType();
        while (!serialOutput.isFinishedNext()) {
            serialOutput.pullBegin();
            serialOutput.pullType();
            long xbIndex = serialOutput.pullAttribute().getNaturalLong();
            Long[] nodePath = pullPath(serialOutput);
            XBENode parentNode = (XBENode) nodeService.findOwnerByXBPath(nodePath);
            serialOutput.pullEnd();

            if (parentNode == null) {
                XBCNode mainRootNode = nodeService.getMainRootNode().get();
                XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
                List<XBCXName> itemNames = nameService.getItemNames(mainRootNode);
                itemNames.forEach(name -> {
                    nameService.removeItem(name);
                });
            }
            XBENode node = (XBENode) nodeService.createItem();
            node.setParent(parentNode);
            node.setXBIndex(xbIndex);
            nodeService.persistItem(node);
        }
        serialOutput.pullEnd();
    }

    private void importFiles(XBPProviderSerialHandler serialOutput) throws XBProcessingException, IOException {
        XBCXFileService fileService = catalog.getCatalogService(XBCXFileService.class);
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);

        serialOutput.pullBegin();
        serialOutput.pullType();
        while (!serialOutput.isFinishedNext()) {
            serialOutput.pullBegin();
            serialOutput.pullType();
            Long[] nodePath = pullPath(serialOutput);
            XBENode node = (XBENode) nodeService.findNodeByXBPath(nodePath);
            XBString fileName = new XBString();
            serialOutput.pullConsist(fileName);
            serialOutput.pullBegin();
            InputStream fileDataStream = serialOutput.pullData();
            byte[] fileData;
            try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                StreamUtils.copyInputStreamToOutputStream(fileDataStream, stream);
                fileData = stream.toByteArray();
            }
            serialOutput.pullEnd();
            serialOutput.pullEnd();
            XBEXFile file = (XBEXFile) fileService.createItem();
            file.setNode(node);
            file.setContent(fileData);
            file.setFilename(fileName.getValue());
            fileService.persistItem(file);
        }
        serialOutput.pullEnd();
    }

    private void importSpecs(XBPProviderSerialHandler serialOutput, CatalogItemType itemType) throws XBProcessingException, IOException {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
        XBCRevService revService = catalog.getCatalogService(XBCRevService.class);

        serialOutput.pullBegin();
        serialOutput.pullType();
        while (!serialOutput.isFinishedNext()) {
            serialOutput.pullBegin();
            serialOutput.pullType();
            long xbIndex = serialOutput.pullAttribute().getNaturalLong();
            Long[] nodePath = pullPath(serialOutput);
            XBENode node = (XBENode) nodeService.findNodeByXBPath(nodePath);

            while (!serialOutput.isFinishedNext()) {
                serialOutput.pullBegin();
                serialOutput.pullType();

                serialOutput.pullEnd();
            }
            serialOutput.pullEnd();

            switch (itemType) {
                case BLOCK: {
                    XBEBlockSpec blockSpec = (XBEBlockSpec) specService.createBlockSpec();
                    blockSpec.setParent(node);
                    blockSpec.setXBIndex(xbIndex);
                    specService.persistItem(blockSpec);

                    // TODO rev + specdef
                    break;
                }
                case GROUP: {
                    XBEGroupSpec groupSpec = (XBEGroupSpec) specService.createGroupSpec();
                    groupSpec.setParent(node);
                    groupSpec.setXBIndex(xbIndex);
                    specService.persistItem(groupSpec);
                    break;
                }
                case FORMAT: {
                    XBEFormatSpec formatSpec = (XBEFormatSpec) specService.createFormatSpec();
                    formatSpec.setParent(node);
                    formatSpec.setXBIndex(xbIndex);
                    specService.persistItem(formatSpec);
                    break;
                }
            }
        }
        serialOutput.pullEnd();
    }

    private void importProperties(XBPProviderSerialHandler serialOutput, CatalogItemType itemType) throws XBProcessingException, IOException {
        importNames(serialOutput, itemType);
        importDescs(serialOutput, itemType);
        importStri(serialOutput, itemType);
        importHDocs(serialOutput, itemType);
        importIcons(serialOutput, itemType);
    }

    private void importNames(XBPProviderSerialHandler serialOutput, CatalogItemType itemType) throws XBProcessingException, IOException {
        XBCXLangService langService = catalog.getCatalogService(XBCXLangService.class);
        XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);

        serialOutput.pullBegin();
        serialOutput.pullType();
        while (!serialOutput.isFinishedNext()) {
            serialOutput.pullBegin();
            serialOutput.pullType();
            long xbIndex = serialOutput.pullAttribute().getNaturalLong();
            Long[] nodePath = pullPath(serialOutput);
            XBENode parentNode = (XBENode) nodeService.findOwnerByXBPath(nodePath);
            XBCItem item = findItem(parentNode, xbIndex, itemType);
            XBString langCode = new XBString();
            serialOutput.pullConsist(langCode);
            XBString text = new XBString();
            serialOutput.pullConsist(text);
            serialOutput.pullEnd();

            XBCXLanguage language = langService.findByCode(langCode.getValue()).get();
            XBEXName name = (XBEXName) nameService.createItem();
            name.setItem(item);
            name.setText(text.getValue());
            name.setLang(language);

            nameService.persistItem(name);
        }
        serialOutput.pullEnd();
    }

    private void importDescs(XBPProviderSerialHandler serialOutput, CatalogItemType itemType) throws XBProcessingException, IOException {
        XBCXLangService langService = catalog.getCatalogService(XBCXLangService.class);
        XBCXDescService descService = catalog.getCatalogService(XBCXDescService.class);
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);

        serialOutput.pullBegin();
        serialOutput.pullType();
        while (!serialOutput.isFinishedNext()) {
            serialOutput.pullBegin();
            serialOutput.pullType();
            long xbIndex = serialOutput.pullAttribute().getNaturalLong();
            Long[] nodePath = pullPath(serialOutput);
            XBENode parentNode = (XBENode) nodeService.findOwnerByXBPath(nodePath);
            XBCItem item = findItem(parentNode, xbIndex, itemType);
            XBString langCode = new XBString();
            serialOutput.pullConsist(langCode);
            XBString text = new XBString();
            serialOutput.pullConsist(text);
            serialOutput.pullEnd();

            XBCXLanguage language = langService.findByCode(langCode.getValue()).get();
            XBEXDesc desc = (XBEXDesc) descService.createItem();
            desc.setItem(item);
            desc.setText(text.getValue());
            desc.setLang(language);

            descService.persistItem(desc);
        }
        serialOutput.pullEnd();
    }

    private void importStri(XBPProviderSerialHandler serialOutput, CatalogItemType itemType) throws XBProcessingException, IOException {
        XBCXStriService striService = catalog.getCatalogService(XBCXStriService.class);
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);

        serialOutput.pullBegin();
        serialOutput.pullType();
        while (!serialOutput.isFinishedNext()) {
            serialOutput.pullBegin();
            serialOutput.pullType();
            long xbIndex = serialOutput.pullAttribute().getNaturalLong();
            Long[] nodePath = pullPath(serialOutput);
            XBENode parentNode = (XBENode) nodeService.findOwnerByXBPath(nodePath);
            XBCItem item = findItem(parentNode, xbIndex, itemType);
            XBString text = new XBString();
            serialOutput.pullConsist(text);
            serialOutput.pullEnd();

            XBEXStri stri = (XBEXStri) striService.createItem();
            stri.setItem(item);
            stri.setText(text.getValue());

            striService.persistItem(stri);
        }
        serialOutput.pullEnd();
    }

    private void importHDocs(XBPProviderSerialHandler serialOutput, CatalogItemType itemType) throws XBProcessingException, IOException {
        XBCXFileService fileService = catalog.getCatalogService(XBCXFileService.class);
        XBCXLangService langService = catalog.getCatalogService(XBCXLangService.class);
        XBCXHDocService hdocService = catalog.getCatalogService(XBCXHDocService.class);
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);

        serialOutput.pullBegin();
        serialOutput.pullType();
        while (!serialOutput.isFinishedNext()) {
            serialOutput.pullBegin();
            serialOutput.pullType();
            long xbIndex = serialOutput.pullAttribute().getNaturalLong();
            Long[] nodePath = pullPath(serialOutput);
            XBENode parentNode = (XBENode) nodeService.findNodeByXBPath(nodePath);
            XBCItem item = findItem(parentNode, xbIndex, itemType);
            XBString langCode = new XBString();
            serialOutput.pullConsist(langCode);
            Long[] fileNodePath = pullPath(serialOutput);
            XBENode fileNode = (XBENode) nodeService.findNodeByXBPath(fileNodePath);
            XBString fileName = new XBString();
            serialOutput.pullConsist(fileName);
            serialOutput.pullEnd();

            XBCXLanguage language = langService.findByCode(langCode.getValue()).get();
            XBCXFile file = fileService.findFile(fileNode, fileName.getValue());
            XBEXHDoc hdoc = (XBEXHDoc) hdocService.createItem();
            hdoc.setItem(item);
            hdoc.setLang(language);
            hdoc.setDocFile(file);

            hdocService.persistItem(hdoc);
        }
        serialOutput.pullEnd();
    }

    private void importIcons(XBPProviderSerialHandler serialOutput, CatalogItemType itemType) throws XBProcessingException, IOException {
        XBCXFileService fileService = catalog.getCatalogService(XBCXFileService.class);
        XBCXIconService iconService = catalog.getCatalogService(XBCXIconService.class);
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);

        serialOutput.pullBegin();
        serialOutput.pullType();
        while (!serialOutput.isFinishedNext()) {
            serialOutput.pullBegin();
            serialOutput.pullType();
            long xbIndex = serialOutput.pullAttribute().getNaturalLong();
            Long[] nodePath = pullPath(serialOutput);
            XBENode parentNode = (XBENode) nodeService.findNodeByXBPath(nodePath);
            XBCItem item = findItem(parentNode, xbIndex, itemType);
            long iconModeIndex = serialOutput.pullLongAttribute();
            Long[] fileNodePath = pullPath(serialOutput);
            XBENode fileNode = (XBENode) nodeService.findNodeByXBPath(fileNodePath);
            XBString fileName = new XBString();
            serialOutput.pullConsist(fileName);
            serialOutput.pullEnd();

            XBCXFile file = fileService.findFile(fileNode, fileName.getValue());
            XBCXIconMode mode = iconService.getIconMode(iconModeIndex);
            XBEXIcon icon = (XBEXIcon) iconService.createItem();
            icon.setParent(item);
            icon.setMode(mode);
            icon.setIconFile(file);

            iconService.persistItem(icon);
        }
        serialOutput.pullEnd();
    }

    private void importPlugins(XBPProviderSerialHandler serialOutput) throws XBProcessingException, IOException {
        XBCXPlugService plugService = catalog.getCatalogService(XBCXPlugService.class);
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        XBCXFileService fileService = catalog.getCatalogService(XBCXFileService.class);

        serialOutput.pullBegin();
        serialOutput.pullType();
        while (!serialOutput.isFinishedNext()) {
            serialOutput.pullBegin();
            serialOutput.pullType();
            Long[] nodePath = pullPath(serialOutput);
            XBENode node = (XBENode) nodeService.findNodeByXBPath(nodePath);
            long pluginIndex = serialOutput.pullLongAttribute();
            Long[] fileNodePath = pullPath(serialOutput);
            XBENode fileNode = (XBENode) nodeService.findNodeByXBPath(fileNodePath);
            XBString fileName = new XBString();
            XBEXFile pluginFile = (XBEXFile) fileService.findFile(fileNode, fileName.getValue());
            serialOutput.pullConsist(fileName);
            serialOutput.pullEnd();

            XBEXPlugin plugin = (XBEXPlugin) plugService.createItem();
            plugin.setOwner(node);
            plugin.setPluginFile(pluginFile);
            plugin.setPluginIndex(pluginIndex);
            plugService.persistItem(plugin);
        }
        serialOutput.pullEnd();
    }

    private void importUis(XBPProviderSerialHandler serialOutput) throws XBProcessingException, IOException {
        XBCXFileService fileService = catalog.getCatalogService(XBCXFileService.class);
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);

        serialOutput.pullBegin();
        serialOutput.pullType();
        while (!serialOutput.isFinishedNext()) {
            serialOutput.pullBegin();
            serialOutput.pullType();
            Long[] nodePath = pullPath(serialOutput);
            XBENode node = (XBENode) nodeService.findNodeByXBPath(nodePath);
            XBString fileName = new XBString();
            serialOutput.pullConsist(fileName);
            serialOutput.pullBegin();
            InputStream fileDataStream = serialOutput.pullData();
            byte[] fileData;
            try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                StreamUtils.copyInputStreamToOutputStream(fileDataStream, stream);
                fileData = stream.toByteArray();
            }
            serialOutput.pullEnd();
            serialOutput.pullEnd();
            XBEXFile file = (XBEXFile) fileService.createItem();
            file.setNode(node);
            file.setContent(fileData);
            file.setFilename(fileName.getValue());
            fileService.persistItem(file);
        }
        serialOutput.pullEnd();
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

    @Nonnull
    private XBCItem findItem(XBENode parentNode, long xbIndex, CatalogItemType itemType) {
        switch (itemType) {
            case NODE: {
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                return nodeService.getSubNode(parentNode, xbIndex);
            }
            case BLOCK: {
                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                return specService.findBlockSpecByXB(parentNode, xbIndex);
            }
            case GROUP: {
                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                return specService.findGroupSpecByXB(parentNode, xbIndex);
            }
            case FORMAT: {
                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                return specService.findFormatSpecByXB(parentNode, xbIndex);
            }
        }

        throw new IllegalStateException("Invalid itemtype " + itemType.name());
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
