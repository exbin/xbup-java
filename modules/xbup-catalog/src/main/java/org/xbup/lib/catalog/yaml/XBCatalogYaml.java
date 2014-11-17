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
package org.xbup.lib.catalog.yaml;

import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.XBCSpecDefType;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.core.catalog.base.service.XBCRevService;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.core.catalog.base.service.XBCXDescService;
import org.xbup.lib.core.catalog.base.service.XBCXHDocService;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;
import org.xbup.lib.core.catalog.base.service.XBCXStriService;
import org.xbup.lib.catalog.entity.XBEBlockSpec;
import org.xbup.lib.catalog.entity.XBEFormatSpec;
import org.xbup.lib.catalog.entity.XBEGroupSpec;
import org.xbup.lib.catalog.entity.XBEItem;
import org.xbup.lib.catalog.entity.XBENode;
import org.xbup.lib.catalog.entity.XBERev;
import org.xbup.lib.catalog.entity.XBESpec;
import org.xbup.lib.catalog.entity.XBESpecDef;
import org.yaml.snakeyaml.Yaml;

/**
 * XB Catalog import and export to yaml.
 *
 * @version 0.1.24 2014/11/17
 * @author XBUP Project (http://xbup.org)
 */
public class XBCatalogYaml {

    private final XBACatalog catalog;
    private XBCXNameService nameService;
    private XBCXDescService descService;

    public XBCatalogYaml(XBACatalog catalog) {
        this.catalog = catalog;
        if (catalog != null) {
            nameService = (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
            descService = (XBCXDescService) catalog.getCatalogService(XBCXDescService.class);
            // TODO: OnAddExtension
        }
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
        XBCXStriService striService = (XBCXStriService) catalog.getCatalogService(XBCXStriService.class);
        XBCXHDocService hdocService = (XBCXHDocService) catalog.getCatalogService(XBCXHDocService.class);

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
        XBCSpecService specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        XBCXStriService striService = (XBCXStriService) catalog.getCatalogService(XBCXStriService.class);

        List defs = new ArrayList();

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
        XBCNodeService nodeService = (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
        XBCSpecService specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);

        exportItem(node, targetData);

        List blockSpecMaps = new ArrayList();
        List<XBCBlockSpec> blockSpecs = specService.getBlockSpecs(node);
        for (XBCBlockSpec blockSpec : blockSpecs) {
            Map<String, Object> specData = new HashMap<>();
            exportItem(blockSpec, specData);
            exportSpec(blockSpec, specData);
            blockSpecMaps.add(specData);
        }
        targetData.put("block", blockSpecMaps);

        List groupSpecMaps = new ArrayList();
        List<XBCGroupSpec> groupSpecs = specService.getGroupSpecs(node);
        for (XBCGroupSpec groupSpec : groupSpecs) {
            Map<String, Object> specData = new HashMap<>();
            exportItem(groupSpec, specData);
            exportSpec(groupSpec, specData);
            groupSpecMaps.add(specData);
        }
        targetData.put("group", groupSpecMaps);

        List formatSpecMaps = new ArrayList();
        List<XBCFormatSpec> formatSpecs = specService.getFormatSpecs(node);
        for (XBCFormatSpec formatSpec : formatSpecs) {
            Map<String, Object> specData = new HashMap<>();
            exportItem(formatSpec, specData);
            exportSpec(formatSpec, specData);
            formatSpecMaps.add(specData);
        }
        targetData.put("format", formatSpecMaps);

        List nodeSpecMaps = new ArrayList();
        List subNodes = nodeService.getSubNodes(node);
        for (Object subNodeObject : subNodes) {
            XBCNode subNode = (XBCNode) subNodeObject;
            Map<String, Object> subNodeData = new HashMap<>();
            // TODO: Replace recursion with iteration
            exportNode(subNode, subNodeData);
            nodeSpecMaps.add(subNodeData);
        }
        targetData.put("node", nodeSpecMaps);
    }

    public void importCatalogItem(InputStream stream, XBENode parentNode) {

        Yaml yaml = new Yaml();
        Map<String, Object> doc;
        doc = (Map<String, Object>) yaml.load(stream);

        importNode(doc, parentNode);
    }

    public void importItem(Map<String, Object> blockData, XBEItem target) {
        XBCSpecService specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        XBCXStriService striService = (XBCXStriService) catalog.getCatalogService(XBCXStriService.class);
        XBCXHDocService hdocService = (XBCXHDocService) catalog.getCatalogService(XBCXHDocService.class);

        String stringId = (String) blockData.get("id");
        striService.setItemStringIdText(target, stringId);

        String name = (String) blockData.get("name");
        nameService.setDefaultText(target, name);

        String desc = (String) blockData.get("description");
        descService.setDefaultText(target, desc);
    }

    public void importSpec(Map<String, Object> specData, XBESpec target) {
        XBCSpecService specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        XBCRevService revService = (XBCRevService) catalog.getCatalogService(XBCRevService.class);
        XBCXStriService striService = (XBCXStriService) catalog.getCatalogService(XBCXStriService.class);

        XBEBlockSpec blockSpec = (XBEBlockSpec) specService.createBlockSpec();
        //blockSpec.setParent((XBENode) parentNode);
        specService.persistItem(blockSpec);
        importItem(specData, blockSpec);

        List defs = (List) specData.get("def");

        for (int defId = 0; defId < defs.size(); defId++) {
            Map<String, Object> def = (Map<String, Object>) defs.get(defId);

            String bind = (String) def.get("bind");
            XBCSpecDefType bindType = getBindType(bind);

            XBESpecDef specDef = (XBESpecDef) specService.createSpecDef(blockSpec, bindType);
            specService.persistItem(specDef);
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

            specService.persistItem(specDef);
        }

        specService.persistItem(blockSpec);
    }

    public void importNode(Map<String, Object> nodeData, XBENode target) {
        XBCSpecService specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        XBCNodeService nodeService = (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);

        List<Object> nodes = (List<Object>) nodeData.get("node");
        if (nodes != null) {
            for (Object node : nodes) {
                Map<String, Object> subNodeData = (Map<String, Object>) node;
                XBENode subNode = (XBENode) nodeService.createItem();
                subNode.setOwner(target);
                nodeService.persistItem(subNode);
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

                specService.persistItem(blockSpec);

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

                specService.persistItem(groupSpec);

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

                specService.persistItem(formatSpec);

                importItem(formatData, formatSpec);
                importSpec(formatData, formatSpec);
            }
        }
    }

    private String getBindString(XBCSpecDefType type) {
        if (type == null) {
            return null;
        }

        switch (type) {
            case CONS:
                return "cons";
            case JOIN:
                return "join";
            case LIST_CONS:
                return "listcons";
            case LIST_JOIN:
                return "listjoin";
            default:
                return null;
        }
    }

    private XBCSpecDefType getBindType(String bind) {
        if ("join".equals(bind)) {
            return XBCSpecDefType.JOIN;
        }
        if ("cons".equals(bind)) {
            return XBCSpecDefType.CONS;
        }
        if ("listcons".equals(bind)) {
            return XBCSpecDefType.LIST_CONS;
        }
        if ("listjoin".equals(bind)) {
            return XBCSpecDefType.LIST_JOIN;
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
}
