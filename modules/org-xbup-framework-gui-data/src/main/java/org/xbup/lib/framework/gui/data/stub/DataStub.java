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
package org.xbup.lib.framework.gui.data.stub;

import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.framework.gui.data.TableDataRow;
import org.xbup.lib.framework.gui.data.TableDataSource;
import org.xbup.lib.framework.gui.data.TableDataSource.ColumnDefinition;

/**
 * RPC Stub for data operations.
 *
 * @version 0.2.0 2016/02/27
 * @author XBUP Project (http://xbup.org)
 */
public class DataStub {

    // public static long[] NODE_INFO_PROCEDURE = {0, 2, 11, 0, 0};
    private final XBCatalogServiceClient client;

    public DataStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public TableDataSource getTableDataSource(String tableSourceId) {
        return new RemoteTableDataSource(this, tableSourceId);
    }

    public List<ColumnDefinition> getColumDefinition(String tableSourceId) {
        List<TableDataSource.ColumnDefinition> columns = new ArrayList<>();
        TableDataSource.ColumnDefinition testColumn = new TableDataSource.ColumnDefinition() {
            @Override
            public String getName() {
                return "Test";
            }

            @Override
            public Class<?> getValueClass() {
                return String.class;
            }
        };
        columns.add(testColumn);
        TableDataSource.ColumnDefinition testColumn2 = new TableDataSource.ColumnDefinition() {
            @Override
            public String getName() {
                return "Test2";
            }

            @Override
            public Class<?> getValueClass() {
                return String.class;
            }
        };
        columns.add(testColumn2);

        return columns;
    }

    public int getTableRowCount(String tableSourceId) {
        return 20;
    }

    public List<TableDataRow> getTableRows(String tableSourceId, int startRow, int rowCount) {
        int rowsToLoad = rowCount;
        if (startRow > 20) {
            rowsToLoad = 0;
        } else if (startRow + rowCount > 20) {
            rowsToLoad = 20 - startRow;
        }
        List<TableDataRow> rows = new ArrayList<>();
        for (int i = 0; i < rowsToLoad; i++) {
            final int rowIndex = startRow + i;
            TableDataRow data = new TableDataRow() {
                @Override
                public Object[] getRowData() {
                    return new String[]{"Cell " + rowIndex + ", 0", "Cell " + rowIndex + ", 1"};
                }
            };
            rows.add(data);
        }

        return rows;
    }
}
