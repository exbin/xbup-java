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
package org.xbup.tool.editor.module.xbdoc_editor.panel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.block.XBTDefaultBlock;
import org.xbup.lib.parser_tree.XBTTreeDocument;

/**
 * Document Tree Model for XBUP Document Tree.
 *
 * @version 0.2.0 2015/09/19
 * @author XBUP Project (http://xbup.org)
 */
public class XBDocTreeModel implements TreeModel {

    private XBTTreeDocument treeDoc;
    private final List<TreeModelListener> treeModelListeners = new ArrayList<>();

    public XBDocTreeModel() {
        super();
    }

    public XBDocTreeModel(XBTTreeDocument treeDoc) {
        super();
        this.treeDoc = treeDoc;
    }

    @Override
    public Object getRoot() {
        return treeDoc.getRootBlock();
    }

    @Override
    public Object getChild(Object parent, int index) {
        return ((XBTBlock) parent).getChildAt(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return ((XBTBlock) parent).getChildrenCount();
    }

    @Override
    public boolean isLeaf(Object node) {
        return ((XBTBlock) node).getChildAt(0) == null;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return XBTDefaultBlock.getChildIndexOf((XBTBlock) parent, (XBTBlock) child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener tml) {
        treeModelListeners.add(tml);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener tml) {
        treeModelListeners.remove(tml);
    }

    /**
     * Performs structure change event.
     *
     * The only event raised by this model is TreeStructureChanged with the root
     * as path, i.e. the whole tree has changed.
     *
     * @param oldRoot old root node
     */
    public void fireTreeStructureChanged(XBTBlock oldRoot) {
        int len = treeModelListeners.size();
        TreeModelEvent e = new TreeModelEvent(this, new Object[]{oldRoot});
        for (int i = 0; i < len; i++) {
            ((TreeModelListener) treeModelListeners.get(i)).treeStructureChanged(e);
        }
    }

    public void fireTreeChanged() {
        int len = treeModelListeners.size();
        TreeModelEvent e = new TreeModelEvent(this, new Object[]{this});
        for (int i = 0; i < len; i++) {
            ((TreeModelListener) treeModelListeners.get(i)).treeStructureChanged(e);
        }
    }

}
