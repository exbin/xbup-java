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
package org.exbin.framework.gui.service.catalog.panel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import static org.exbin.framework.gui.service.catalog.panel.CatalogSpecItemType.BLOCK;
import static org.exbin.framework.gui.service.catalog.panel.CatalogSpecItemType.FORMAT;
import static org.exbin.framework.gui.service.catalog.panel.CatalogSpecItemType.GROUP;

/**
 * Table Model for Catalog Tree.
 *
 * @version 0.1.24 2014/12/12
 * @author ExBin Project (http://exbin.org)
 */
public class CatalogSelectSpecTreeModel implements TreeModel {

    private XBCNodeService nodeService = null;
    private XBCSpecService specService = null;
    private final List<TreeModelListener> treeModelListeners = new ArrayList<>();
    private final CatalogSpecItemType specType;

    public CatalogSelectSpecTreeModel(XBACatalog catalog, CatalogSpecItemType specType) {
        this.specType = specType;

        nodeService = catalog == null ? null : (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
        specService = catalog == null ? null : (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
    }

    @Override
    public Object getRoot() {
        return nodeService == null ? null : nodeService.getRootNode();
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent == null) {
            return null;
        }
        long subNodesCount = nodeService.getSubNodesCount((XBCNode) parent);
        if (index < subNodesCount) {
            return nodeService.getSubNodeSeq(((XBCNode) parent), index);
        }

        switch (specType) {
            case BLOCK: {
                return specService.getBlockSpec((XBCNode) parent, index - subNodesCount);
            }
            case GROUP: {
                return specService.getGroupSpec((XBCNode) parent, index - subNodesCount);
            }
            case FORMAT: {
                return specService.getFormatSpec((XBCNode) parent, index - subNodesCount);
            }
        }

        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent == null) {
            throw new NullPointerException("No parent");
        }
        int childrenCount = (int) nodeService.getSubNodesSeq(((XBCNode) parent));
        switch (specType) {
            case BLOCK: {
                childrenCount += specService.getBlockSpecsCount((XBCNode) parent);
                break;
            }
            case GROUP: {
                childrenCount += specService.getGroupSpecsCount((XBCNode) parent);
                break;
            }
            case FORMAT: {
                childrenCount += specService.getFormatSpecsCount((XBCNode) parent);
                break;
            }
        }
        return childrenCount;
    }

    @Override
    public boolean isLeaf(Object node) {
        return !(node instanceof XBCNode);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        // TODO: optimalization later
        int subNodeIndex = nodeService.getSubNodes(((XBCNode) parent)).indexOf(child);

        if (subNodeIndex >= 0) {
            return subNodeIndex;
        }

        int childrenCount = (int) nodeService.getSubNodesSeq(((XBCNode) parent));
        switch (specType) {
            case BLOCK: {
                return specService.getBlockSpecs((XBCNode) parent).indexOf(child) + childrenCount;
            }
            case GROUP: {
                return specService.getGroupSpecs((XBCNode) parent).indexOf(child) + childrenCount;
            }
            case FORMAT: {
                return specService.getFormatSpecs((XBCNode) parent).indexOf(child) + childrenCount;
            }
        }

        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener tml) {
        treeModelListeners.add(tml);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener tml) {
        treeModelListeners.remove(tml);
    }
}
