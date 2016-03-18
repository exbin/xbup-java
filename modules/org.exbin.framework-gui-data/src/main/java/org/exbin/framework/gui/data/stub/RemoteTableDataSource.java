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
package org.exbin.framework.gui.data.stub;

import java.util.ArrayList;
import java.util.List;
import org.exbin.framework.gui.data.TableDataRow;
import org.exbin.framework.gui.data.TableDataSource;

/**
 * Remote table model data source.
 *
 * @version 0.2.0 2016/02/27
 * @author ExBin Project (http://exbin.org)
 */
public class RemoteTableDataSource implements TableDataSource {

    public static final int ROWS_PER_REQUEST = 7;

    private final String tableSourceId;
    private final DataStub dataStub;
    private List<ColumnDefinition> columDefinition = null;
    private final List<RowsPageCache> pageCache = new ArrayList<>();

    public RemoteTableDataSource(DataStub dataStub, String tableSourceId) {
        this.dataStub = dataStub;
        this.tableSourceId = tableSourceId;
    }

    @Override
    public List<ColumnDefinition> getColumns() {
        if (columDefinition == null) {
            columDefinition = dataStub.getColumDefinition(tableSourceId);
        }

        return columDefinition;
    }

    @Override
    public int getRowCount() {
        return dataStub.getTableRowCount(tableSourceId);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        int pageIndex = rowIndex / ROWS_PER_REQUEST;
        int pageOffset = rowIndex % ROWS_PER_REQUEST;

        List<TableDataRow> tableRows = dataStub.getTableRows(tableSourceId, rowIndex, 1);
        TableDataRow tableRow = tableRows.get(0);
        return tableRow.getRowData()[columnIndex];
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private RowsPageCache getPageCache(int pageIndex) {
        if (pageIndex < pageCache.size()) {
            return pageCache.get(pageIndex);
        }

        return null;
    }

    private RowsPageCache getOrLoadPage(int pageIndex) {
        RowsPageCache page = getPageCache(pageIndex);
        if (page == null) {
            List<TableDataRow> tableRows = dataStub.getTableRows(tableSourceId, pageIndex * ROWS_PER_REQUEST, ROWS_PER_REQUEST);
            page = new RowsPageCache();
            page.pageData = tableRows;
            if (pageCache.size() < pageIndex) {
                int cacheSize = pageCache.size();
                for (int i = cacheSize; i < pageIndex; i++) {
                    pageCache.add(null);
                }
                pageCache.add(page);
            }
        }

        return page;
    }

    private class RowsPageCache {

        List<TableDataRow> pageData;
    }
}
