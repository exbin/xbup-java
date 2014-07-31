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
import org.xbup.lib.parser_tree.XBTTreeDocument;
import org.xbup.lib.parser_tree.XBTTreeNode;

/**
 * Document Tree Model for XBUP Document Tree.
 *
 * @version 0.1 wr20.0 2010/09/15
 * @author XBUP Project (http://xbup.org)
 */
public class XBDocTreeModel implements TreeModel {

    private XBTTreeDocument treeDoc;
    private List<TreeModelListener> treeModelListeners = new ArrayList<TreeModelListener>();

    /** Creates a new instance of XBDocTreeModel */
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
        return ((XBTTreeNode) parent).getChildAt(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return ((XBTTreeNode) parent).getChildCount();
    }

    @Override
    public boolean isLeaf(Object node) {
        return ((XBTTreeNode) node).isLeaf();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return ((XBTTreeNode) parent).getIndex((XBTTreeNode) child);
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
     * The only event raised by this model is TreeStructureChanged with the
     * root as path, i.e. the whole tree has changed.
     */
    public void fireTreeStructureChanged(XBTTreeNode oldRoot) {
        int len = treeModelListeners.size();
        TreeModelEvent e = new TreeModelEvent(this, new Object[] {oldRoot});
        for (int i = 0; i < len; i++) {
            ((TreeModelListener)treeModelListeners.get(i)).treeStructureChanged(e);
        }
    }

    public void fireTreeChanged() {
        int len = treeModelListeners.size();
        TreeModelEvent e = new TreeModelEvent(this, new Object[] {this});
        for (int i = 0; i < len; i++) {
            ((TreeModelListener)treeModelListeners.get(i)).treeStructureChanged(e);
        }
    }

}
