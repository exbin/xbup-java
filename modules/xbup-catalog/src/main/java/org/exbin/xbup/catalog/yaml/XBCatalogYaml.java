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
package org.exbin.xbup.catalog.yaml;

import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.exbin.xbup.catalog.entity.XBEBlockSpec;
import org.exbin.xbup.catalog.entity.XBEFormatSpec;
import org.exbin.xbup.catalog.entity.XBEGroupSpec;
import org.exbin.xbup.catalog.entity.XBEItem;
import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.catalog.entity.XBERev;
import org.exbin.xbup.catalog.entity.XBESpec;
import org.exbin.xbup.catalog.entity.XBESpecDef;
import org.exbin.xbup.catalog.entity.service.XBEXDescService;
import org.exbin.xbup.catalog.entity.service.XBEXNameService;
import org.exbin.xbup.catalog.entity.service.XBEXStriService;
import org.exbin.xbup.core.block.definition.XBParamType;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCSpecDef;
import org.exbin.xbup.core.catalog.base.XBCXHDoc;
import org.exbin.xbup.core.catalog.base.XBCXStri;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;
import org.exbin.xbup.core.catalog.base.service.XBCSpecService;
import org.exbin.xbup.core.catalog.base.service.XBCXDescService;
import org.exbin.xbup.core.catalog.base.service.XBCXHDocService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.core.catalog.base.service.XBCXStriService;
import org.yaml.snakeyaml.Yaml;

/**
 * XB Catalog import and export to yaml.
 *
 * @version 0.2.0 2017/01/07
 * @author ExBin Project (http://exbin.org)
 */
public class XBCatalogYaml {

    private XBACatalog catalog;
    private XBCXNameService nameService;
    private XBCXDescService descService;

    public XBCatalogYaml() {
    }

    public void exportCatalogItem(XBCItem item, Writer writer) {
        Map<String, Object> itemData = new HashMap<>();

        if (item instanceof XBCNode) {
            exportNode((XBCNode) item, itemData);
        } else {
            exportItem(item, itemData);

            if (item instanceof XBCSpec) {
                exportSpec((XBCSpec) item, itemData);
            }
        }

        List<Object> itemMaps = new ArrayList<>();
        itemMaps.add(itemData);

        Map<String, Object> doc = new HashMap<>();
        String typeString = getItemTypeString(item);
        doc.put(typeString, itemMaps);

        Yaml yaml = new Yaml();
        yaml.dump(doc, writer);
    }

    public void exportItem(XBCItem item, Map<String, Object> targetData) {
        XBCXStriService striService = catalog.getCatalogService(XBCXStriService.class);
        XBCXHDocService hdocService = catalog.getCatalogService(XBCXHDocService.class);

        String stringId = striService.getItemStringIdText(item);
        targetData.put("id", stringId);
        /* XBCItem parent = item.getParent();
         if (parent != null) {
         String parentId = striService.getItemStringIdText(parent);
         blockData.put("parent", parentId);
         } */

        String name = nameService.getDefaultText(item);
        if (name != null) {
            targetData.put("name", name);
        }

        String desc = descService.getDefaultText(item);
        if (desc != null) {
            targetData.put("description", desc);
        }

        String hdoc = hdocService.getDocumentationText(item);
        if (hdoc != null) {
            targetData.put("HDoc", hdoc);
        }
    }

    public void exportSpec(XBCItem item, Map<String, Object> targetData) {
        XBCSpecService<? extends XBCSpec> specService = (XBCSpecService<? extends XBCSpec>) catalog.getCatalogService(XBCSpecService.class);
        XBCXStriService<? extends XBCXStri> striService = (XBCXStriService<? extends XBCXStri>) catalog.getCatalogService(XBCXStriService.class);

        List<Map<String, Object>> defs = new ArrayList<>();

        List<XBCSpecDef> specDefs = specService.getSpecDefs((XBCSpec) item);
        for (XBCSpecDef specDef : specDefs) {
            Map<String, Object> def = new HashMap<>();
            String name = striService.getItemStringIdText(specDef);
            def.put("id", name);
            def.put("bind", getBindString(specDef.getType()));

            XBCRev targetSpec = specDef.getTarget();
            if (targetSpec != null) {
                String type = striService.getItemFullPath(targetSpec.getParent());
                def.put("type", type);
                Long rev = targetSpec.getXBIndex();
                def.put("rev", rev);
            }

            Map<String, Object> defData = new HashMap<>();
            exportItem(specDef, defData);
            def.putAll(defData);

            defs.add(def);
        }

        targetData.put("def", defs);
    }

    public void exportNode(XBCNode node, Map<String, Object> targetData) {
        XBCNodeService<? extends XBCNode> nodeService = (XBCNodeService<? extends XBCNode>) catalog.getCatalogService(XBCNodeService.class);
        XBCSpecService<? extends XBCSpec> specService = (XBCSpecService<? extends XBCSpec>) catalog.getCatalogService(XBCSpecService.class);

        exportItem(node, targetData);

        List<Map<String, Object>> blockSpecMaps = new ArrayList<>();
        List<XBCBlockSpec> blockSpecs = specService.getBlockSpecs(node);
        for (XBCBlockSpec blockSpec : blockSpecs) {
            Map<String, Object> specData = new HashMap<>();
            exportItem(blockSpec, specData);
            exportSpec(blockSpec, specData);
            blockSpecMaps.add(specData);
        }
        targetData.put("block", blockSpecMaps);

        List<Map<String, Object>> groupSpecMaps = new ArrayList<>();
        List<XBCGroupSpec> groupSpecs = specService.getGroupSpecs(node);
        for (XBCGroupSpec groupSpec : groupSpecs) {
            Map<String, Object> specData = new HashMap<>();
            exportItem(groupSpec, specData);
            exportSpec(groupSpec, specData);
            groupSpecMaps.add(specData);
        }
        targetData.put("group", groupSpecMaps);

        List<Map<String, Object>> formatSpecMaps = new ArrayList<>();
        List<XBCFormatSpec> formatSpecs = specService.getFormatSpecs(node);
        for (XBCFormatSpec formatSpec : formatSpecs) {
            Map<String, Object> specData = new HashMap<>();
            exportItem(formatSpec, specData);
            exportSpec(formatSpec, specData);
            formatSpecMaps.add(specData);
        }
        targetData.put("format", formatSpecMaps);

        List<Map<String, Object>> nodeSpecMaps = new ArrayList<>();
        List<XBCNode> subNodes = nodeService.getSubNodes(node);
        for (XBCNode subNode : subNodes) {
            Map<String, Object> subNodeData = new HashMap<>();
            // TODO: Replace recursion with iteration
            exportNode(subNode, subNodeData);
            nodeSpecMaps.add(subNodeData);
        }
        targetData.put("node", nodeSpecMaps);
    }

    public void importCatalogItem(InputStream stream, XBENode parentNode) {

        Yaml yaml = new Yaml();
        Object doc = yaml.load(stream);
        importNode((Map<String, Object>) doc, parentNode);
    }

    public void importItem(Map<String, Object> blockData, XBEItem target) {
        XBCSpecService<? extends XBCSpec> specService = (XBCSpecService<? extends XBCSpec>) catalog.getCatalogService(XBCSpecService.class);
        XBCXStriService<? extends XBCXStri> striService = (XBCXStriService<? extends XBCXStri>) catalog.getCatalogService(XBCXStriService.class);
        XBCXHDocService<? extends XBCXHDoc> hdocService = (XBCXHDocService<? extends XBCXHDoc>) catalog.getCatalogService(XBCXHDocService.class);

        String stringId = (String) blockData.get("id");
        ((XBEXStriService) striService).setItemStringIdText(target, stringId);

        String name = (String) blockData.get("name");
        ((XBEXNameService) nameService).setDefaultText(target, name);

        String desc = (String) blockData.get("description");
        ((XBEXDescService) descService).setDefaultText(target, desc);
    }

    public void importSpec(Map<String, Object> specData, XBESpec target) {
        XBCSpecService<? extends XBCSpec> specService = (XBCSpecService<? extends XBCSpec>) catalog.getCatalogService(XBCSpecService.class);
        XBCRevService<? extends XBCRev> revService = (XBCRevService<? extends XBCRev>) catalog.getCatalogService(XBCRevService.class);
        XBCXStriService<? extends XBCXStri> striService = (XBCXStriService<? extends XBCXStri>) catalog.getCatalogService(XBCXStriService.class);

        XBEBlockSpec blockSpec = (XBEBlockSpec) specService.createBlockSpec();
        //blockSpec.setParent((XBENode) parentNode);
        ((XBCSpecService<XBCSpec>) specService).persistItem(blockSpec);
        importItem(specData, blockSpec);

        List defs = (List) specData.get("def");

        for (int defId = 0; defId < defs.size(); defId++) {
            Map<String, Object> def = (Map<String, Object>) defs.get(defId);

            String bind = (String) def.get("bind");
            XBParamType bindType = getBindType(bind);

            XBESpecDef specDef = (XBESpecDef) specService.createSpecDef(blockSpec, bindType);
            ((XBCSpecService) specService).persistItem(specDef);
            importItem(def, specDef);

            specDef.setCatalogItem(blockSpec);
            specDef.setXBIndex(new Long(defId));

            String type = (String) def.get("type");
            XBCSpec targetSpec = striService.getSpecByFullPath(type);
            if (targetSpec != null) {
                Integer targetRevIndex = (Integer) def.get("rev");
                XBERev targetRev = (XBERev) revService.findRevByXB(targetSpec, targetRevIndex);
                specDef.setTarget(targetRev);
            }

            ((XBCSpecService) specService).persistItem(specDef);
        }

        ((XBCSpecService) specService).persistItem(blockSpec);
    }

    public void importNode(Map<String, Object> nodeData, XBENode target) {
        XBCSpecService<? extends XBCSpec> specService = (XBCSpecService<? extends XBCSpec>) catalog.getCatalogService(XBCSpecService.class);
        XBCNodeService<? extends XBCNode> nodeService = (XBCNodeService<? extends XBCNode>) catalog.getCatalogService(XBCNodeService.class);

        List<Object> nodes = (List<Object>) nodeData.get("node");
        if (nodes != null) {
            for (Object node : nodes) {
                Map<String, Object> subNodeData = (Map<String, Object>) node;
                XBENode subNode = (XBENode) nodeService.createItem();
                subNode.setOwner(target);
                ((XBCNodeService) nodeService).persistItem(subNode);
                importItem(subNodeData, subNode);
                // TODO: Replace recursion with iteration
                importNode(subNodeData, subNode);
            }
        }

        List<Object> blocks = (List<Object>) nodeData.get("block");
        if (blocks != null && nodes != null) {
            for (int blockId = 0; blockId < blocks.size(); blockId++) {
                Map<String, Object> blockData = (Map<String, Object>) nodes.get(blockId);
                XBEBlockSpec blockSpec = (XBEBlockSpec) specService.createBlockSpec();
                blockSpec.setParent(target);
                long blockSpecIndex = specService.findMaxBlockSpecXB(target);
                blockSpecIndex++;
                blockSpec.setXBIndex(blockSpecIndex);

                ((XBCSpecService) specService).persistItem(blockSpec);

                importItem(blockData, blockSpec);
                importSpec(blockData, blockSpec);
            }
        }

        List<Object> groups = (List<Object>) nodeData.get("group");
        if (groups != null && nodes != null) {
            for (int groupId = 0; groupId < groups.size(); groupId++) {
                Map<String, Object> groupData = (Map<String, Object>) nodes.get(groupId);
                XBEGroupSpec groupSpec = (XBEGroupSpec) specService.createGroupSpec();
                groupSpec.setParent(target);
                long groupSpecIndex = specService.findMaxGroupSpecXB(target);
                groupSpecIndex++;
                groupSpec.setXBIndex(groupSpecIndex);

                ((XBCSpecService) specService).persistItem(groupSpec);

                importItem(groupData, groupSpec);
                importSpec(groupData, groupSpec);
            }
        }

        List<Object> formats = (List<Object>) nodeData.get("format");
        if (formats != null && nodes != null) {
            for (int formatId = 0; formatId < formats.size(); formatId++) {
                Map<String, Object> formatData = (Map<String, Object>) nodes.get(formatId);
                XBEFormatSpec formatSpec = (XBEFormatSpec) specService.createFormatSpec();
                formatSpec.setParent(target);
                long formatSpecIndex = specService.findMaxFormatSpecXB(target);
                formatSpecIndex++;
                formatSpec.setXBIndex(formatSpecIndex);

                ((XBCSpecService) specService).persistItem(formatSpec);

                importItem(formatData, formatSpec);
                importSpec(formatData, formatSpec);
            }
        }
    }

    private String getBindString(XBParamType type) {
        if (type == null) {
            return null;
        }

        switch (type) {
            case CONSIST:
                return "cons";
            case JOIN:
                return "join";
            case LIST_CONSIST:
                return "listcons";
            case LIST_JOIN:
                return "listjoin";
            default:
                return null;
        }
    }

    private XBParamType getBindType(String bind) {
        if ("join".equals(bind)) {
            return XBParamType.JOIN;
        }
        if ("cons".equals(bind)) {
            return XBParamType.CONSIST;
        }
        if ("listcons".equals(bind)) {
            return XBParamType.LIST_CONSIST;
        }
        if ("listjoin".equals(bind)) {
            return XBParamType.LIST_JOIN;
        }
        return null;
    }

    private String getItemTypeString(XBCItem item) {
        if (item instanceof XBCNode) {
            return "node";
        }
        if (item instanceof XBCBlockSpec) {
            return "block";
        }
        if (item instanceof XBCGroupSpec) {
            return "group";
        }
        if (item instanceof XBCFormatSpec) {
            return "format";
        }

        return null;
    }

    private Class getItemStringType(String itemType) {
        if ("node".equals(itemType)) {
            return XBCNode.class;
        }
        if ("block".equals(itemType)) {
            return XBCBlockSpec.class;
        }
        if ("group".equals(itemType)) {
            return XBCGroupSpec.class;
        }
        if ("format".equals(itemType)) {
            return XBCFormatSpec.class;
        }

        return null;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;

        nameService = catalog == null ? null : catalog.getCatalogService(XBCXNameService.class);
        descService = catalog == null ? null : catalog.getCatalogService(XBCXDescService.class);
    }
}
