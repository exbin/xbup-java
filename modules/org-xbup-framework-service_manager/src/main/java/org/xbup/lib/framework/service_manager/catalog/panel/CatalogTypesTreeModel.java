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
package org.xbup.lib.framework.service_manager.catalog.panel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.xbup.lib.core.catalog.XBACatalog;

/**
 * Table Model for Catalog Tree
 *
 * @version 0.1.19 2010/05/30
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogTypesTreeModel implements TreeModel {

    private final XBACatalog catalog;
    private final List<TreeModelListener> treeModelListeners = new ArrayList<>();

    public CatalogTypesTreeModel(XBACatalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public Object getRoot() {
        throw new UnsupportedOperationException("Not yet implemented");
//        return catalog.getTypeManager().getRootNode();
    }

    @Override
    public Object getChild(Object parent, int index) {
        throw new UnsupportedOperationException("Not yet implemented");
        /*        if (parent==null) return null;
         return catalog.getTypeManager().getSubNode(((XBCType) parent),index); */
    }

    @Override
    public int getChildCount(Object parent) {
        throw new UnsupportedOperationException("Not yet implemented");
        /*        if (parent==null) throw new NullPointerException("No parent");
         return (int) catalog.getTypeManager().getSubNodesCount(((XBCType) parent)); */
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
        // TODO: optimalization later
        throw new UnsupportedOperationException("Not yet implemented");
//        return catalog.getTypeManager().getSubNodes(((XBCType) parent)).indexOf(child);
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
