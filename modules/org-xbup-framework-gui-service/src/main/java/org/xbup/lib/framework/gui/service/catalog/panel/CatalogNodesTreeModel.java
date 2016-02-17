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
package org.xbup.lib.framework.gui.service.catalog.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;

/**
 * Table Model for Catalog Tree.
 *
 * @version 0.1.24 2014/12/09
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogNodesTreeModel implements TreeModel {

    private XBCatalog catalog;
    private final List<TreeModelListener> treeModelListeners = new ArrayList<>();
    private CatalogNodesTreeItem rootItem = null;

    public CatalogNodesTreeModel() {
        this(null);
    }

    public CatalogNodesTreeModel(XBCNode rootNode) {
        rootItem = rootNode == null ? null : new CatalogNodesTreeItem(rootNode);
    }

    public void setCatalog(XBCatalog catalog) {
        this.catalog = catalog;
        if (rootItem != null) {
            rootItem.updateNode();
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

    public TreePath findPathForSpec(XBCSpec spec) {
        Long[] specPath = catalog.getSpecPath(spec);
        CatalogNodesTreeItem[] nodePath = new CatalogNodesTreeItem[specPath.length - 1];
        CatalogNodesTreeItem node = rootItem;
        for (int specPathDepth = 0; specPathDepth < specPath.length - 1; specPathDepth++) {
            Long specPathIndex = specPath[specPathDepth];
            List<CatalogNodesTreeItem> children = node.getChildren();
            for (CatalogNodesTreeItem child : children) {
                if (child.getNode().getXBIndex().equals(specPathIndex)) {
                    nodePath[specPathDepth] = child;
                    node = child;
                    break;
                }
            }
        }

        return new TreePath(nodePath);
    }

    public class CatalogNodesTreeItem {

        private XBCNode node;

        private String name;
        private boolean loaded = false;
        private final List<CatalogNodesTreeItem> children = new ArrayList<>();

        public CatalogNodesTreeItem(XBCNode node) {
            this.node = node;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public XBCNode getNode() {
            return node;
        }

        public void setNode(XBCNode node) {
            this.node = node;
            updateNode();
        }

        private void updateNode() {
            XBCXNameService nameService = (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
            name = nameService.getDefaultText(node);
        }

        public List<CatalogNodesTreeItem> getChildren() {
            if (!loaded) {
                XBCNodeService nodeService = (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
                List<XBCNode> subNodes = nodeService.getSubNodes(((XBCNode) node));
                for (XBCNode subNode : subNodes) {
                    CatalogNodesTreeItem subItem = new CatalogNodesTreeItem(subNode);
                    subItem.updateNode();
                    children.add(subItem);
                }

                loaded = true;
            }

            return children;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 71 * hash + Objects.hashCode(this.node);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CatalogNodesTreeItem other = (CatalogNodesTreeItem) obj;
            return Objects.equals(this.node, other.node);
        }
    }
}
