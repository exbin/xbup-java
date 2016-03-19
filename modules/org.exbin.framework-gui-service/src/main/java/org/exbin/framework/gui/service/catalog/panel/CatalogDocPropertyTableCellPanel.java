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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.service.XBCXHDocService;
import org.exbin.framework.gui.service.catalog.dialog.CatalogEditDocumentationDialog;
import org.exbin.framework.gui.utils.WindowUtils;

/**
 * Catalog HDoc Property Cell Panel.
 *
 * @version 0.2.0 2016/02/01
 * @author ExBin Project (http://exbin.org)
 */
public class CatalogDocPropertyTableCellPanel extends PropertyTableCellPanel {

    private XBACatalog catalog;
    private String doc;

    public CatalogDocPropertyTableCellPanel(XBACatalog catalog) {
        super();
        this.catalog = catalog;
        setEditorAction(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                performEditorAction();
            }
        });
    }

    public void performEditorAction() {
        CatalogEditDocumentationDialog docDialog = new CatalogEditDocumentationDialog(WindowUtils.getFrame(this), true, catalog, doc);
        docDialog.setLocationRelativeTo(this);
        docDialog.setVisible(true);

        if (docDialog.getDialogOption() == JOptionPane.OK_OPTION) {
            doc = docDialog.getDocumentation();
            setDocLabel();
        }
    }

    public void setCatalogItem(XBCItem catalogItem) {
        XBCXHDocService hDocService = (XBCXHDocService) catalog.getCatalogService(XBCXHDocService.class);
        doc = hDocService.getDocumentationText(catalogItem);
        setDocLabel();
    }

    private void setDocLabel() {
        setPropertyText(doc == null || doc.isEmpty() ? "" : "[" + doc.length() + " bytes]");
    }

    public String getDocument() {
        return doc;
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }
}
