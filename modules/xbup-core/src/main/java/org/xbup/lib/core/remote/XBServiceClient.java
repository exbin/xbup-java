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
package org.xbup.lib.core.remote;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

/**
 * XBService client interface.
 *
 * @version 0.1 wr21.0 2012/04/18
 * @author XBUP Project (http://xbup.org)
 */
public interface XBServiceClient {

    public static long[] XBSERVICE_FORMAT = { 0,2,0,0 };

    public static long[] LOGIN_SERVICE_PROCEDURE = { 0,2,0,0,0 };
    public static long[] STOP_SERVICE_PROCEDURE = { 0,2,0,1,0 };
    public static long[] RESTART_SERVICE_PROCEDURE = { 0,2,0,2,0 };
    public static long[] PING_SERVICE_PROCEDURE = { 0,2,0,3,0 };

    public static long[] VERSION_INFO_PROCEDURE = { 0,2,1,0,0 };

    public static long[] OWNER_ITEM_PROCEDURE = { 0,2,3,0,0 };
    public static long[] XBINDEX_ITEM_PROCEDURE = { 0,2,3,1,0 };
    public static long[] ITEMSCOUNT_ITEM_PROCEDURE = { 0,2,3,2,0 };

    public static long[] ROOT_NODE_PROCEDURE = { 0,2,4,0,0 };
    public static long[] SUBNODE_NODE_PROCEDURE = { 0,2,4,1,0 };
    public static long[] SUBNODES_NODE_PROCEDURE = { 0,2,4,2,0 };
    public static long[] SUBNODESCOUNT_NODE_PROCEDURE = { 0,2,4,3,0 };
    public static long[] SPEC_NODE_PROCEDURE = { 0,2,4,4,0 };
    public static long[] SPECS_NODE_PROCEDURE = { 0,2,4,5,0 };
    public static long[] FORMATSPEC_NODE_PROCEDURE = { 0,2,4,6,0 };
    public static long[] FORMATSPECS_NODE_PROCEDURE = { 0,2,4,7,0 };
    public static long[] GROUPSPEC_NODE_PROCEDURE = { 0,2,4,8,0 };
    public static long[] GROUPSPECS_NODE_PROCEDURE = { 0,2,4,9,0 };
    public static long[] BLOCKSPEC_NODE_PROCEDURE = { 0,2,4,10,0 };
    public static long[] BLOCKSPECS_NODE_PROCEDURE = { 0,2,4,11,0 };
    public static long[] FINDOWNER_NODE_PROCEDURE = { 0,2,4,12,0 };
    public static long[] PATHNODE_NODE_PROCEDURE = { 0,2,4,13,0 };
    public static long[] FINDNODE_NODE_PROCEDURE = { 0,2,4,14,0 };
    public static long[] MAXSUBNODE_NODE_PROCEDURE = { 0,2,4,15,0 };
    public static long[] FINDBLOCKSPEC_NODE_PROCEDURE = { 0,2,4,16,0 };
    public static long[] MAXBLOCKSPEC_NODE_PROCEDURE = { 0,2,4,17,0 };
    public static long[] FINDGROUPSPEC_NODE_PROCEDURE = { 0,2,4,18,0 };
    public static long[] MAXGROUPSPEC_NODE_PROCEDURE = { 0,2,4,19,0 };
    public static long[] FINDFORMATSPEC_NODE_PROCEDURE = { 0,2,4,20,0 };
    public static long[] MAXFORMATSPEC_NODE_PROCEDURE = { 0,2,4,21,0 };
    public static long[] SPECSCOUNT_NODE_PROCEDURE = { 0,2,4,22,0 };
    public static long[] BLOCKSPECSCOUNT_NODE_PROCEDURE = { 0,2,4,23,0 };
    public static long[] GROUPSPECSCOUNT_NODE_PROCEDURE = { 0,2,4,24,0 };
    public static long[] FORMATSPECSCOUNT_NODE_PROCEDURE = { 0,2,4,25,0 };
    public static long[] NODESCOUNT_NODE_PROCEDURE = { 0,2,4,26,0 };
    public static long[] SUBNODESEQ_NODE_PROCEDURE = { 0,2,4,27,0 };
    public static long[] SUBNODESEQCNT_NODE_PROCEDURE = { 0,2,4,28,0 };
    public static long[] ROOT_PROCEDURE = { 0,2,4,29,0 };

    public static long[] BIND_SPEC_PROCEDURE = { 0,2,5,0,0 };
    public static long[] BINDS_SPEC_PROCEDURE = { 0,2,5,1,0 };
    public static long[] BINDSCOUNT_SPEC_PROCEDURE = { 0,2,5,2,0 };
    public static long[] FINDBIND_SPEC_PROCEDURE = { 0,2,5,3,0 };
    public static long[] FINDREV_SPEC_PROCEDURE = { 0,2,5,4,0 };
    public static long[] FORMATSPECSCOUNT_SPEC_PROCEDURE = { 0,2,5,5,0 };
    public static long[] GROUPSPECSCOUNT_SPEC_PROCEDURE = { 0,2,5,6,0 };
    public static long[] BLOCKSPECSCOUNT_SPEC_PROCEDURE = { 0,2,5,7,0 };
    public static long[] SPECSCOUNT_SPEC_PROCEDURE = { 0,2,5,8,0 };
    public static long[] REVSCOUNT_SPEC_PROCEDURE = { 0,2,5,9,0 };
    public static long[] REV_SPEC_PROCEDURE = { 0,2,5,10,0 };

    public static long[] TARGET_BIND_PROCEDURE = { 0,2,6,0,0 };
    public static long[] BINDSCOUNT_BIND_PROCEDURE = { 0,2,6,1,0 };

    public static long[] XBLIMIT_REV_PROCEDURE = { 0,2,7,0,0 };
    public static long[] REVSCOUNT_REV_PROCEDURE = { 0,2,7,1,0 };

    public static long[] CODE_LANG_PROCEDURE = { 0,2,8,0,0 };
    public static long[] DEFAULT_LANG_PROCEDURE = { 0,2,8,1,0 };
    public static long[] LANGS_LANG_PROCEDURE = { 0,2,8,2,0 };
    public static long[] LANGSCOUNT_LANG_PROCEDURE = { 0,2,8,3,0 };

    public static long[] ITEM_NAME_PROCEDURE = { 0,2,9,0,0 };
    public static long[] TEXT_NAME_PROCEDURE = { 0,2,9,1,0 };
    public static long[] LANG_NAME_PROCEDURE = { 0,2,9,2,0 };
    public static long[] ITEMNAME_NAME_PROCEDURE = { 0,2,9,3,0 };
    public static long[] LANGNAME_NAME_PROCEDURE = { 0,2,9,4,0 };
    public static long[] ITEMNAMES_NAME_PROCEDURE = { 0,2,9,5,0 };
    public static long[] NAMESCOUNT_NAME_PROCEDURE = { 0,2,9,6,0 };

    public static long[] ITEM_DESC_PROCEDURE = { 0,2,10,0,0 };
    public static long[] TEXT_DESC_PROCEDURE = { 0,2,10,1,0 };
    public static long[] LANG_DESC_PROCEDURE = { 0,2,10,2,0 };
    public static long[] ITEMDESC_DESC_PROCEDURE = { 0,2,10,3,0 };
    public static long[] LANGNAME_DESC_PROCEDURE = { 0,2,10,4,0 };
    public static long[] ITEMDESCS_DESC_PROCEDURE = { 0,2,10,5,0 };
    public static long[] DESCSCOUNT_DESC_PROCEDURE = { 0,2,10,6,0 };

    public static long[] NODE_INFO_PROCEDURE = { 0,2,11,0,0 };
    public static long[] INFOSCOUNT_INFO_PROCEDURE = { 0,2,11,1,0 };
    public static long[] FILENAME_INFO_PROCEDURE = { 0,2,12,2,0 };
    public static long[] PATH_INFO_PROCEDURE = { 0,2,12,3,0 };

    public static long[] OWNER_FILE_PROCEDURE = { 0,2,12,0,0 };
    public static long[] FILENAME_FILE_PROCEDURE = { 0,2,12,1,0 };
    public static long[] DATA_FILE_PROCEDURE = { 0,2,12,2,0 };

    public static long[] OWNER_ICON_PROCEDURE = { 0,2,13,0,0 };
    public static long[] MODE_ICON_PROCEDURE = { 0,2,13,1,0 };
    public static long[] XBINDEX_ICON_PROCEDURE = { 0,2,13,2,0 };
    public static long[] DEFAULTITEM_ICON_PROCEDURE = { 0,2,13,3,0 };
    public static long[] FILE_ICON_PROCEDURE = { 0,2,13,4,0 };

    public static long[] OWNER_PLUGIN_PROCEDURE = { 0,2,14,0,0 };
    public static long[] FILE_PLUGIN_PROCEDURE = { 0,2,14,1,0 };
    public static long[] INDEX_PLUGIN_PROCEDURE = { 0,2,14,2,0 };
    public static long[] LINEPLUGIN_PLUGIN_PROCEDURE = { 0,2,14,3,0 };
    public static long[] LINEINDEX_PLUGIN_PROCEDURE = { 0,2,14,4,0 };
    public static long[] PANEPLUGIN_PLUGIN_PROCEDURE = { 0,2,14,5,0 };
    public static long[] PANEINDEX_PLUGIN_PROCEDURE = { 0,2,14,6,0 };

    public static long[] REV_LINE_PROCEDURE = { 0,2,15,0,0 };
    public static long[] PLUGIN_LINE_PROCEDURE = { 0,2,15,1,0 };
    public static long[] PRIORITY_LINE_PROCEDURE = { 0,2,15,2,0 };
    public static long[] LINESCOUNT_LINE_PROCEDURE = { 0,2,15,3,0 };
    public static long[] REVLINE_LINE_PROCEDURE = { 0,2,15,4,0 };
    public static long[] PLUGLINESCOUNT_LINE_PROCEDURE = { 0,2,15,5,0 };
    public static long[] PLUGLINE_LINE_PROCEDURE = { 0,2,15,6,0 };

    public static long[] REV_PANE_PROCEDURE = { 0,2,16,0,0 };
    public static long[] PLUGIN_PANE_PROCEDURE = { 0,2,16,1,0 };
    public static long[] PRIORITY_PANE_PROCEDURE = { 0,2,16,2,0 };
    public static long[] PANESCOUNT_PANE_PROCEDURE = { 0,2,16,3,0 };
    public static long[] REVPANE_PANE_PROCEDURE = { 0,2,16,4,0 };
    public static long[] PLUGPANESCOUNT_PANE_PROCEDURE = { 0,2,16,5,0 };
    public static long[] PLUGPANE_PANE_PROCEDURE = { 0,2,16,6,0 };

    public static long[] OWNER_HDOC_PROCEDURE = { 0,2,17,0,0 };
    public static long[] XBINDEX_HDOC_PROCEDURE = { 0,2,17,1,0 };
    public static long[] ITEM_HDOC_PROCEDURE = { 0,2,17,2,0 };
    public static long[] FILE_HDOC_PROCEDURE = { 0,2,17,3,0 };

    public static long[] ITEM_STRI_PROCEDURE = { 0,2,18,0,0 };
    public static long[] TEXT_STRI_PROCEDURE = { 0,2,18,1,0 };
    public static long[] NODEPATH_STRI_PROCEDURE = { 0,2,18,2,0 };
    public static long[] ITEMSTRI_STRI_PROCEDURE = { 0,2,18,3,0 };
    public static long[] ITEMSTRIS_STRI_PROCEDURE = { 0,2,18,4,0 };
    public static long[] STRISCOUNT_STRI_PROCEDURE = { 0,2,18,5,0 };

    /** Perform login to the server */
    public int login(String user, char[] password) throws IOException;

    public void init() throws IOException, ConnectException;

    public String getVersion();

    public void close();

    public void ping();

    public String getHost();

    public int getPort();

    public String getLocalAddress();

    public String getHostAddress();

    public boolean validate();

    public Socket getSocket();
}
