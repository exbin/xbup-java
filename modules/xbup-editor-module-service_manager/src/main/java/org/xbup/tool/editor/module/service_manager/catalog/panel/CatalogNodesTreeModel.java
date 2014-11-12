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
package org.xbup.tool.editor.module.service_manager.catalog.panel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;

/**
 * Table Model for Catalog Tree.
 *
 * @version 0.1.23 2013/09/30
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogNodesTreeModel implements TreeModel {

    private final XBCatalog catalog;
    private final List<TreeModelListener> treeModelListeners = new ArrayList<>();
    private CatalogNodesTreeItem rootItem = null;

    public CatalogNodesTreeModel(XBCatalog catalog) {
        this.catalog = catalog;
        XBCNodeService nodeService = (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
        XBCNode rootNode = nodeService.getRootNode();
        if (rootNode != null) {
            rootItem = new CatalogNodesTreeItem(rootNode);
        }
    }

    @Override
    public Object getRoot() {
        return rootItem;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent == null) {
            return null;
        }

        return ((CatalogNodesTreeItem) parent).getChildren().get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent == null) {
            throw new NullPointerException("No parent");
        }

        return ((CatalogNodesTreeItem) parent).getChildren().size();
    }

    @Override
    public boolean isLeaf(Object node) {
        return false;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return ((CatalogNodesTreeItem) parent).getChildren().indexOf(child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener tml) {
        treeModelListeners.add(tml);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener tml) {
        treeModelListeners.remove(tml);
    }

    public class CatalogNodesTreeItem {

        private XBCNode node;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        private String name;
        private boolean loaded = false;
        private final List<CatalogNodesTreeItem> children;

        public CatalogNodesTreeItem(XBCNode node) {
            this.node = node;
            XBCXNameService nameService = (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
            name = nameService.getItemNameText(node);

            children = new ArrayList<>();
        }

        public XBCNode getNode() {
            return node;
        }

        public void setNode(XBCNode node) {
            this.node = node;
        }

        public List<CatalogNodesTreeItem> getChildren() {
            if (!loaded) {
                XBCNodeService nodeService = (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
                List<XBCNode> subNodes = nodeService.getSubNodes(((XBCNode) node));
                for (XBCNode subNode : subNodes) {
                    children.add(new CatalogNodesTreeItem(subNode));
                }

                loaded = true;
            }

            return children;
        }
    }
}
