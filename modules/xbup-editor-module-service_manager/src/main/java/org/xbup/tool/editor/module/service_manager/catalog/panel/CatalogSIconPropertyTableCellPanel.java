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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.service.XBCXIconService;
import org.xbup.tool.editor.base.api.utils.WindowUtils;
import org.xbup.tool.editor.module.service_manager.catalog.dialog.CatalogEditIconDialog;

/**
 * Catalog Big Icon Property Cell Panel.
 *
 * @version 0.1.24 2014/11/26
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogSIconPropertyTableCellPanel extends PropertyTableCellPanel {

    private XBACatalog catalog;
    private byte[] icon;

    public CatalogSIconPropertyTableCellPanel(XBACatalog catalog) {
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
        CatalogEditIconDialog iconDialog = new CatalogEditIconDialog(WindowUtils.getFrame(this), true, catalog, icon);
        iconDialog.setLocationRelativeTo(this);
        iconDialog.setVisible(true);

        if (iconDialog.getDialogOption() == JOptionPane.OK_OPTION) {
            icon = iconDialog.getIcon();
            setDocLabel();
        }
    }

    public void setCatalogItem(XBCItem catalogItem) {
        XBCXIconService iconService = (XBCXIconService) catalog.getCatalogService(XBCXIconService.class);
        icon = iconService.getDefaultSmallIconData(catalogItem);
        setDocLabel();
    }

    private void setDocLabel() {
        setPropertyText(icon == null || icon.length == 0 ? "" : "[" + icon.length + " bytes]");
    }

    public byte[] getIcon() {
        return icon;
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }
}
