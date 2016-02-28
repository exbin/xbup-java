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
package org.xbup.lib.framework.gui.data.panel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.framework.gui.data.TableDataSource;
import org.xbup.lib.framework.gui.data.stub.DataStub;

/**
 * Table model.
 *
 * @version 0.2.0 2016/02/27
 * @author ExBin Project (http://exbin.org)
 */
public class TableModelSource implements TableModel {

    private final List<TableModelListener> listeners = new ArrayList<>();
    private final TableDataSource dataSource;
    private final String tableSourceId;
    private final XBServiceClient service;

    public TableModelSource(String tableSourceId, XBServiceClient service) {
        this.service = service;
        this.tableSourceId = tableSourceId;

        DataStub dataStub = new DataStub((XBCatalogServiceClient) service);
        dataSource = dataStub.getTableDataSource(tableSourceId);
    }

    @Override
    public int getRowCount() {
        return dataSource.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return dataSource.getColumns().size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return dataSource.getColumns().get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return dataSource.getColumns().get(columnIndex).getValueClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return dataSource.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        dataSource.setValueAt(value, rowIndex, columnIndex);
    }

    @Override
    public void addTableModelListener(TableModelListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }
}
