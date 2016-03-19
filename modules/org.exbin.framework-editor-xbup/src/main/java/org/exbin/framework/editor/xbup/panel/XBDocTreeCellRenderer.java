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
package org.exbin.framework.editor.xbup.panel;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.block.declaration.local.XBLBlockDecl;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.service.XBCXIconService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Tree Cell Renderer for XBUP Document Tree.
 *
 * @version 0.1.24 2014/11/28
 * @author ExBin Project (http://exbin.org)
 */
public class XBDocTreeCellRenderer extends DefaultTreeCellRenderer {

    private XBACatalog catalog;
    private final Map<Long, String> captionCache;
    private final Map<Long, ImageIcon> iconCache;

    public XBDocTreeCellRenderer(XBACatalog catalog) {
        super();
        this.catalog = catalog;
        captionCache = new HashMap<>();
        iconCache = new HashMap<>();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {
//        setLeafIcon(leafIcon);
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (value instanceof XBTTreeNode) {
            XBTTreeNode node = ((XBTTreeNode) value);
            String caption = null;
            if (node.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
                if (node.getDataSize() == 0) {
                    caption = "Empty Block";
                } else {
                    caption = "Data Block";
                }
            } else if (catalog != null) {
                caption = getCaption(node.getBlockDecl());
            }
            if (caption != null) {
                setText(caption);
            } else {
                setText("Undefined");
                setForeground(Color.RED);
            }

            if (node.getDataMode() == XBBlockDataMode.NODE_BLOCK) {
                ImageIcon icon = null;
                if (catalog != null) {
                    icon = getIcon(node.getBlockDecl());
                }
                if (icon != null) {
                    setIcon(icon);
                }
            }
        }
        return this;
    }

    /**
     * @return the catalog
     */
    public XBACatalog getCatalog() {
        return catalog;
    }

    /**
     * @param catalog the catalog to set
     */
    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    /**
     * Gets caption for given block type.
     *
     * Use cache if available.
     *
     * @param blockDecl
     * @return caption
     */
    public String getCaption(XBBlockDecl blockDecl) {
        if (blockDecl instanceof XBCBlockDecl) {
            XBCBlockSpec blockSpec = (XBCBlockSpec) ((XBCBlockDecl) blockDecl).getBlockSpecRev().getParent();
            if (captionCache.containsKey(blockSpec.getId())) {
                return captionCache.get(blockSpec.getId());
            }

            XBCXNameService nameService = (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
            String caption = nameService.getDefaultText(blockSpec);
            captionCache.put(blockSpec.getId(), caption);
            return caption;
        } else if (blockDecl instanceof XBLBlockDecl) {
            // TOOD
            /* XBCBlockDecl blockDecl = (XBCBlockDecl) ((XBLBlockDecl) blockDecl).getBlockDecl();
             if (blockDecl != null) {
             XBCBlockSpec blockSpec = blockDecl.getBlockSpecRev().getParent();
             if (captionCache.containsKey(blockSpec.getId())) {
             return captionCache.get(blockSpec.getId());
             }

             XBCXNameService nameService = (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
             String caption = nameService.getDefaultText(blockSpec);
             captionCache.put(blockSpec.getId(), caption);
             return caption;
             } */
        }

        return null;
    }

    /**
     * Gets icon for given block type. Use cache if available.
     *
     * @param blockDecl
     * @return icon
     */
    public ImageIcon getIcon(XBBlockDecl blockDecl) {
        if (blockDecl instanceof XBCBlockDecl) {
            XBCBlockSpec blockSpec = (XBCBlockSpec) ((XBCBlockDecl) blockDecl).getBlockSpecRev().getParent();
            if (iconCache.containsKey(blockSpec.getId())) {
                return iconCache.get(blockSpec.getId());
            }
            XBCXIconService iconService = (XBCXIconService) catalog.getCatalogService(XBCXIconService.class);
            ImageIcon icon = iconService.getDefaultImageIcon(blockSpec);
            if (icon == null) {
                iconCache.put(blockSpec.getId(), icon);
                return null;
            }
            if (icon.getImage() == null) {
                return null;
            }
            icon = new ImageIcon(icon.getImage().getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));
            iconCache.put(blockSpec.getId(), icon);
            return icon;
        } else if (blockDecl instanceof XBDeclBlockType) {
            // TODO
            /* XBCBlockDecl blockDecl = (XBCBlockDecl) ((XBDBlockType) blockDecl).getBlockDecl();
             if (blockDecl != null) {
             XBCBlockSpec blockSpec = blockDecl.getBlockSpecRev().getParent();
             if (iconCache.containsKey(blockSpec.getId())) {
             return iconCache.get(blockSpec.getId());
             }
             XBCXIconService iconService = (XBCXIconService) catalog.getCatalogService(XBCXIconService.class);
             ImageIcon icon = iconService.getDefaultImageIcon(blockSpec);
             if (icon == null) {
             iconCache.put(blockSpec.getId(), icon);
             return null;
             }
             icon = new ImageIcon(icon.getImage().getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));
             iconCache.put(blockSpec.getId(), icon);
             return icon;
             } */
        }

        return null;
    }
}
