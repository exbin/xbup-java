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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.convert.XBTListenerToToken;
import org.exbin.framework.editor.xbup.dialog.ModifyBlockDialog;
import org.exbin.framework.editor.xbup.panel.cell.PropertyTableCellPanel;
import org.exbin.framework.gui.utils.WindowUtils;
import org.exbin.xbup.parser_tree.XBATreeParamExtractor;
import org.exbin.xbup.parser_tree.XBTTreeDocument;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.parser_tree.XBTTreeReader;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * Properties table cell panel.
 *
 * @version 0.1.24 2015/01/16
 * @author ExBin Project (http://exbin.org)
 */
public class XBPropertyTableCellPanel extends PropertyTableCellPanel {

    private XBACatalog catalog;
    private final XBPluginRepository pluginRepository;
    private XBTTreeNode node;
    private final XBTTreeDocument doc;
    private final int row;

    public XBPropertyTableCellPanel(JComponent cellComponent, XBACatalog catalog, XBPluginRepository pluginRepository, XBTTreeNode node, XBTTreeDocument doc, int row) {
        super(cellComponent);

        this.catalog = catalog;
        this.pluginRepository = pluginRepository;
        this.node = node;
        this.doc = doc;
        this.row = row;
        init();
    }

    public XBPropertyTableCellPanel(XBACatalog catalog, XBPluginRepository pluginRepository, XBTTreeNode node, XBTTreeDocument doc, int row) {
        super();

        this.catalog = catalog;
        this.pluginRepository = pluginRepository;
        this.node = node;
        this.doc = doc;
        this.row = row;
        init();
    }

    private void init() {
        setEditorAction(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                performEditorAction();
            }
        });
    }

    public void performEditorAction() {
        ModifyBlockDialog modifyDialog = new ModifyBlockDialog(WindowUtils.getFrame(this), true);
        modifyDialog.setCatalog(catalog);
        modifyDialog.setPluginRepository(pluginRepository);
        modifyDialog.setLocationRelativeTo(modifyDialog.getParent());
        
        // TODO: Subparting instead of copy until modify operation
        XBATreeParamExtractor paramExtractor = new XBATreeParamExtractor(node, catalog);
        paramExtractor.setParameterIndex(row);
        XBTTreeNode paramNode = new XBTTreeNode();
        XBTTreeReader reader = new XBTTreeReader(paramNode);
        int depth = 0;
        try {
            do {
                XBTToken token = paramExtractor.pullXBTToken();
                switch (token.getTokenType()) {
                    case BEGIN: {
                        depth++;
                        break;
                    }
                    case END: {
                        depth--;
                        break;
                    }
                }
                XBTListenerToToken.tokenToListener(token, reader);
            } while (depth > 0);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPropertyTableCellPanel.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        XBTTreeNode newNode = modifyDialog.runDialog(paramNode, doc);
        // TODO save
        if (newNode != null) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    public XBTTreeNode getNode() {
        return node;
    }

    public void setNode(XBTTreeNode node) {
        this.node = node;
    }
}
