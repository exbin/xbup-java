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
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import org.xbup.lib.core.catalog.base.XBCRev;

/**
 * Catalog Revisions Combo Box Model.
 *
 * @version 0.1.22 2013/05/24
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogRevsComboBoxModel extends AbstractListModel<XBCRev> implements ComboBoxModel<XBCRev> {

    private List<XBCRev> revs;
    private XBCRev selectedRev;

    public CatalogRevsComboBoxModel() {
        revs = new ArrayList<>();
    }

    @Override
    public int getSize() {
        return revs.size();
    }

    @Override
    public XBCRev getElementAt(int index) {
        return revs.get(index);
    }

    public List<XBCRev> getRevs() {
        return revs;
    }

    public void setRevs(List<XBCRev> revs) {
        this.revs = revs;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        setSelectedRev((XBCRev) anItem);
    }

    @Override
    public Object getSelectedItem() {
        return getSelectedRev();
    }

    public XBCRev getSelectedRev() {
        return selectedRev;
    }

    public void setSelectedRev(XBCRev selectedRev) {
        this.selectedRev = selectedRev;
    }
}
