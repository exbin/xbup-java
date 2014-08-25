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
package org.xbup.lib.core.catalog.update;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.URLDataSource;
import javax.jws.WebParam;
import org.xbup.lib.core.catalog.base.XBCXDesc;
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.catalog.base.XBCXName;
import org.xbup.lib.core.parser.basic.XBHead;

/**
 * Catalog update handler using some primitive PHP interface.
 * 
 * TODO: This is really horrible temporary stub for dummy PHP XBCatalog.
 *
 * @version 0.1.22 2013/08/20
 * @author XBUP Project (http://xbup.org)
 */
public class XBCUpdatePHPPort {

    private String catalogPrefix = "http://catalog-php-dev.xbup.org";
    private String catalogURL = catalogPrefix + "/interface/wr23-0.php";
    private String catalogRepo = catalogPrefix + "/root";

    private BufferedReader compositeReader;
    private BufferedReader compoundCall;

    public XBCUpdatePHPPort() {
        compositeReader = null;
        compoundCall = null;
    }

    public void setItemName(ItemInfo info, List<XBCXName> list, Long langId) {
        String name = null;
        for (XBCXName itemName : list) {
            if (((XBCXLanguage) itemName.getLang()).getId().equals(langId)) {
                name = itemName.getText();
            }
        }
        info.setName(name);
    }

    public void setItemDesc(ItemInfo info, List<XBCXDesc> list, Long langId) {
        String desc = null;
        for (XBCXDesc itemDesc : list) {
            if (((XBCXLanguage) itemDesc.getLang()).getId().equals(langId)) {
                desc = itemDesc.getText();
            }
        }
        info.setDesc(desc);
    }

    public BufferedReader callCatalog(String params) throws IOException {
        Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(XBHead.XB_DEBUG_LEVEL, ("CALL: "+params));
        if (compoundCall != null) {
            return compoundCall;
        }
        try {
            URL myURL;
            myURL = new URL(catalogURL + params);
            URLDataSource dataSource = new URLDataSource(myURL);
            if (compositeReader != null) {
                return compositeReader;
            }
            return new BufferedReader(new InputStreamReader(dataSource.getInputStream(),Charset.forName("UTF-8")));
        } catch (MalformedURLException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public BufferedReader compoundCallCatalog(String params) throws IOException {
        try {
            URL myURL;
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(XBHead.XB_DEBUG_LEVEL, ("CALL: "+params));
            myURL = new URL(catalogURL + params);
            URLDataSource dataSource = new URLDataSource(myURL);
            if (compositeReader != null) {
                return compositeReader;
            }
            compoundCall = new BufferedReader(new InputStreamReader(dataSource.getInputStream(),Charset.forName("UTF-8")));
            return compoundCall;
        } catch (MalformedURLException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void finishCompound() {
        compoundCall = null;
    }

    public InputStream getRepoFile(String path) {
        try {
            URL myURL;
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(XBHead.XB_DEBUG_LEVEL, ("FILE: "+path));
            if (!path.startsWith("/")) {
                path = '/' + path;
            }
            myURL = new URL((catalogRepo + path).replace(" ", "%20")); // TODO: toURI?
            URLDataSource dataSource = new URLDataSource(myURL);
            return dataSource.getInputStream();
        } catch (FileNotFoundException ex) {
            return null;
        } catch (MalformedURLException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getLanguageId(@WebParam(name = "language") String language) {
        try {
            BufferedReader br;
            br = callCatalog("?op=getlang&code=" + language);
            String line;
            line = br.readLine();
            tail();
            if ("id".equals(line)) {
                return new Long(br.readLine());
            } else {
                return null;
            }
        } catch (java.net.UnknownHostException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String getParamPath(Long[] path) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.length; i++) {
            sb.append(path[i]);
            sb.append('/');
        }
        return sb.toString();
    }

    public ItemInfo getCatalogNodeInfo(Long[] path, Long langId) {
        try {
            ItemInfo info = new ItemInfo();
            BufferedReader br = callCatalog("?op=getnode&path="+getParamPath(path));
            String line;
            String itemId = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    itemId = br.readLine();
                } else if ("xbindex".equals(line)) {
                    info.setXbIndex(new Long(br.readLine()));
                } else {
                    br.readLine();
                }
            }
            if (info.getXbIndex() == null) {
                return info;
            }
            br = callCatalog("?op=getname&id="+itemId+"&lang="+langId);
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("text".equals(line)) {
                    info.setName(br.readLine());
                } else {
                    br.readLine();
                }
            }
            br = callCatalog("?op=getdesc&id="+itemId+"&lang="+langId);
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("text".equals(line)) {
                    info.setDesc(br.readLine());
                } else {
                    br.readLine();
                }
            }
            return info;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ItemFile getCatalogNodeFile(Long[] path) {
        try {
            BufferedReader br = callCatalog("?op=getnode&path="+getParamPath(path));
            String line;
            String itemId = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    itemId = br.readLine();
                } else {
                    br.readLine();
                }
            }
            if ("".equals(itemId)) {
                return null;
            }
            ItemFile file = new ItemFile();
            br = callCatalog("?op=getstri&id="+itemId);
            itemId = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    itemId = br.readLine();
                } else if ("text".equals(line)) {
                    file.setFileName(br.readLine());
                } else {
                    br.readLine();
                }
            }
            return file;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getCatalogNodeId(Long[] path) {
        try {
            BufferedReader br = callCatalog("?op=getnode&path="+getParamPath(path));
            String line;
            String itemId = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    itemId = br.readLine();
                } else {
                    br.readLine();
                }
            }
            return itemId.isEmpty() ? null : new Long(itemId);
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getCatalogFormatSpecId(Long[] path, Long xbIndex) {
        try {
            BufferedReader br = callCatalog("?op=getspec&spec="+xbIndex.toString()+"&dtype=0&path="+getParamPath(path));
            String line;
            String itemId = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    itemId = br.readLine();
                } else {
                    br.readLine();
                }
            }
            return new Long(itemId);
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getCatalogGroupSpecId(Long[] path, Long xbIndex) {
        try {
            BufferedReader br = callCatalog("?op=getspec&spec="+xbIndex.toString()+"&dtype=1&path="+getParamPath(path));
            String line;
            String itemId = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    itemId = br.readLine();
                } else {
                    br.readLine();
                }
            }
            return new Long(itemId);
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getCatalogBlockSpecId(Long[] path, Long xbIndex) {
        try {
            BufferedReader br = callCatalog("?op=getspec&spec="+xbIndex.toString()+"&dtype=2&path="+getParamPath(path));
            String line;
            String itemId = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    itemId = br.readLine();
                } else {
                    br.readLine();
                }
            }
            return new Long(itemId);
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getCatalogLimitationSpecId(Long[] path, Long xbIndex) {
        try {
            BufferedReader br = callCatalog("?op=getspec&spec="+xbIndex.toString()+"&dtype=3&path="+getParamPath(path));
            String line;
            String itemId = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    itemId = br.readLine();
                } else {
                    br.readLine();
                }
            }
            return new Long(itemId);
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ItemInfo getFormatCatalogSpecInfo(Long[] path, Long specId, Long langId) {
        try {
            ItemInfo info = new ItemInfo();
            BufferedReader br = callCatalog("?op=getspec&spec="+specId+"&dtype=0&path="+getParamPath(path));
            String line;
            String itemId = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    itemId = br.readLine();
                } else if ("xbindex".equals(line)) {
                    info.setXbIndex(new Long(br.readLine()));
                } else {
                    br.readLine();
                }
            }
            if (!("".equals(itemId))) {
                br = callCatalog("?op=getname&id="+itemId+"&lang="+langId);
                while (!("".equals(line = br.readLine()))&&(line!=null)) {
                    if ("text".equals(line)) {
                        info.setName(br.readLine());
                    } else {
                        br.readLine();
                    }
                }
                br = callCatalog("?op=getdesc&id="+itemId+"&lang="+langId);
                while (!("".equals(line = br.readLine()))&&(line!=null)) {
                    if ("text".equals(line)) {
                        info.setDesc(br.readLine());
                    } else {
                        br.readLine();
                    }
                }
            }
            return info;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ItemInfo getGroupCatalogSpecInfo(Long[] path, Long specId, Long langId) {
        try {
            ItemInfo info = new ItemInfo();
            BufferedReader br = callCatalog("?op=getspec&spec="+specId+"&dtype=1&path="+getParamPath(path));
            String line;
            String itemId = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    itemId = br.readLine();
                } else if ("xbindex".equals(line)) {
                    info.setXbIndex(new Long(br.readLine()));
                } else {
                    br.readLine();
                }
            }
            if (!("".equals(itemId))) {
                br = callCatalog("?op=getname&id="+itemId+"&lang="+langId);
                while (!("".equals(line = br.readLine()))&&(line!=null)) {
                    if ("text".equals(line)) {
                        info.setName(br.readLine());
                    } else {
                        br.readLine();
                    }
                }
                br = callCatalog("?op=getdesc&id="+itemId+"&lang="+langId);
                while (!("".equals(line = br.readLine()))&&(line!=null)) {
                    if ("text".equals(line)) {
                        info.setDesc(br.readLine());
                    } else {
                        br.readLine();
                    }
                }
            }
            return info;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ItemInfo getBlockCatalogSpecInfo(Long[] path, Long specId, Long langId) {
        try {
            ItemInfo info = new ItemInfo();
            BufferedReader br = callCatalog("?op=getspec&spec="+specId+"&dtype=2&path="+getParamPath(path));
            String line;
            String itemId = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    itemId = br.readLine();
                } else if ("xbindex".equals(line)) {
                    info.setXbIndex(new Long(br.readLine()));
                } else {
                    br.readLine();
                }
            }
            if (!("".equals(itemId))) {
                br = callCatalog("?op=getname&id="+itemId+"&lang="+langId);
                while (!("".equals(line = br.readLine()))&&(line!=null)) {
                    if ("text".equals(line)) {
                        info.setName(br.readLine());
                    } else {
                        br.readLine();
                    }
                }
                br = callCatalog("?op=getdesc&id="+itemId+"&lang="+langId);
                while (!("".equals(line = br.readLine()))&&(line!=null)) {
                    if ("text".equals(line)) {
                        info.setDesc(br.readLine());
                    } else {
                        br.readLine();
                    }
                }
            }
            return info;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getSubNodeCatalogMaxIndex(Long[] path) {
        if (path == null) {
            return null;
        }
        try {
            BufferedReader br = callCatalog("?op=getnodemax&dtype=0&path="+getParamPath(path));
            String line;
            Long result = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("max".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getFormatCatalogSpecMaxIndex(Long[] path) {
        try {
            BufferedReader br = callCatalog("?op=getnodemax&dtype=1&path="+getParamPath(path));
            String line;
            Long result = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("max".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getGroupCatalogSpecMaxIndex(Long[] path) {
        try {
            BufferedReader br = callCatalog("?op=getnodemax&dtype=2&path="+getParamPath(path));
            String line;
            Long result = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("max".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getBlockCatalogSpecMaxIndex(Long[] path) {
        try {
            BufferedReader br = callCatalog("?op=getnodemax&dtype=3&path="+getParamPath(path));
            String line;
            Long result = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("max".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getLimitCatalogSpecMaxIndex(Long[] path) {
        throw new UnsupportedOperationException("Not supported yet.");
/*        try {
            BufferedReader br = callCatalog("?op=getnodemax&dtype=4&path="+getParamPath(path));
            String line;
            Long result = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("max".equals(line)) {
                    line = br.readLine();
                    if (!line.equals("")) result = new Long(line);
                } else br.readLine();
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } */
    }

    public Long getFormatCatalogSubMaxIndex(Long[] path) {
        throw new UnsupportedOperationException("Not supported yet.");
/*        try {
            BufferedReader br = callCatalog("?op=getnode&dtype=0&path="+getParamPath(path));
            String line;
            Long result = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("xblimit".equals(line)) {
                    line = br.readLine();
                    if (!line.equals("")) result = new Long(line);
                } else br.readLine();
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } */
    }

    public Long getGroupCatalogSubMaxIndex(Long[] path) {
        throw new UnsupportedOperationException("Not supported yet.");
        /*try {
            BufferedReader br = callCatalog("?op=getnode&dtype=1&path="+getParamPath(path));
            String line;
            Long result = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("xblimit".equals(line)) {
                    line = br.readLine();
                    if (!line.equals("")) result = new Long(line);
                } else br.readLine();
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } */
    }

    public Long getBlockCatalogSubMaxIndex(Long[] path) {
        throw new UnsupportedOperationException("Not supported yet.");
        /*try {
            BufferedReader br = callCatalog("?op=getnode&dtype=2&path="+getParamPath(path));
            String line;
            Long result = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("xblimit".equals(line)) {
                    line = br.readLine();
                    if (!line.equals("")) result = new Long(line);
                } else br.readLine();
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } */
    }
/*
    public Long getAttribCatalogSubMaxIndex(Long[] path) {
        try {
            BufferedReader br = callCatalog("?op=getnode&dtype=3&path="+getParamPath(path));
            String line;
            Long result = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("xblimit".equals(line)) {
                    line = br.readLine();
                    if (!line.equals("")) result = new Long(line);
                } else br.readLine();
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
*/
    public Long getFormatCatalogSpecMaxBindId(Long[] path, Long specId) {
        try {
            BufferedReader br = callCatalog("?op=getspec&spec="+specId+"&dtype=0&path="+getParamPath(path));
            String line;
            Long result = null;
            Long itemId = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        itemId = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            if (itemId != null) {
                br = callCatalog("?op=getdefmax&origin="+itemId.toString());
                while (!("".equals(line = br.readLine()))&&(line!=null)) {
                    if ("max".equals(line)) {
                        line = br.readLine();
                        if (!line.isEmpty()) {
                            result = new Long(line);
                        }
                    } else {
                        br.readLine();
                    }
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getGroupCatalogSpecMaxBindId(Long[] path, Long specId) {
        try {
            BufferedReader br = callCatalog("?op=getspec&spec="+specId+"&dtype=1&path="+getParamPath(path));
            String line;
            Long result = null;
            Long itemId = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        itemId = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            if (itemId != null) {
                br = callCatalog("?op=getdefmax&origin="+itemId.toString());
                while (!("".equals(line = br.readLine()))&&(line!=null)) {
                    if ("max".equals(line)) {
                        line = br.readLine();
                        if (!line.isEmpty()) {
                            result = new Long(line);
                        }
                    } else {
                        br.readLine();
                    }
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getBlockCatalogSpecMaxBindId(Long[] path, Long specId) {
        try {
            BufferedReader br = callCatalog("?op=getspec&spec="+specId+"&dtype=2&path="+getParamPath(path));
            String line;
            Long result = null;
            Long itemId = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        itemId = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            if (itemId != null) {
                br = callCatalog("?op=getdefmax&origin="+itemId.toString());
                while (!("".equals(line = br.readLine()))&&(line!=null)) {
                    if ("max".equals(line)) {
                        line = br.readLine();
                        if (!line.isEmpty()) {
                            result = new Long(line);
                        }
                    } else {
                        br.readLine();
                    }
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getFormatCatalogSpecMaxRevIndex(Long[] path, Long specId) {
        try {
            BufferedReader br = callCatalog("?op=getspec&spec="+specId+"&dtype=0&path="+getParamPath(path));
            String line;
            Long result = null;
            Long itemId = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        itemId = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            if (itemId != null) {
                br = callCatalog("?op=getrevmax&owner="+itemId);
                while (!("".equals(line = br.readLine()))&&(line!=null)) {
                    if ("max".equals(line)) {
                        line = br.readLine();
                        if (!line.isEmpty()) {
                            result = new Long(line);
                        }
                    } else {
                        br.readLine();
                    }
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getGroupCatalogSpecMaxRevIndex(Long[] path, Long specId) {
        try {
            BufferedReader br = callCatalog("?op=getspec&spec="+specId+"&dtype=1&path="+getParamPath(path));
            String line;
            Long result = null;
            Long itemId = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        itemId = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            if (itemId != null) {
                br = callCatalog("?op=getrevmax&owner="+itemId);
                while (!("".equals(line = br.readLine()))&&(line!=null)) {
                    if ("max".equals(line)) {
                        line = br.readLine();
                        if (!line.isEmpty()) {
                            result = new Long(line);
                        }
                    } else {
                        br.readLine();
                    }
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getBlockCatalogSpecMaxRevIndex(Long[] path, Long specId) {
        try {
            BufferedReader br = callCatalog("?op=getspec&spec="+specId+"&dtype=2&path="+getParamPath(path));
            String line;
            Long result = null;
            Long itemId = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        itemId = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            if (itemId != null) {
                br = callCatalog("?op=getrevmax&owner="+itemId);
                while (!("".equals(line = br.readLine()))&&(line!=null)) {
                    if ("max".equals(line)) {
                        line = br.readLine();
                        if (!line.isEmpty()) {
                            result = new Long(line);
                        }
                    } else {
                        br.readLine();
                    }
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ItemRevision getFormatSpecRevision(Long[] path, Long specId, Long revId) {
        try {
            BufferedReader br = callCatalog("?op=getspec&spec="+specId+"&dtype=0&path="+getParamPath(path));
            String line;
            ItemRevision result = null;
            Long itemId = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        itemId = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            if (itemId != null) {
                result = new ItemRevision();
                br = callCatalog("?op=getrev&owner="+itemId+"&xbindex=" + revId);
                while (!("".equals(line = br.readLine()))&&(line!=null)) {
                    if ("id".equals(line)) {
                        line = br.readLine();
                        result.setId(new Long(line));
                    } else if ("xbindex".equals(line)) {
                        line = br.readLine();
                        result.setXBIndex(new Long(line));
                    } else if ("xblimit".equals(line)) {
                        line = br.readLine();
                        result.setXBLimit(new Long(line));
                    } else {
                        br.readLine();
                    }
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ItemRevision getGroupSpecRevision(Long[] path, Long specId, Long revId) {
        try {
            BufferedReader br = callCatalog("?op=getspec&spec="+specId+"&dtype=1&path="+getParamPath(path));
            String line;
            ItemRevision result = null;
            Long itemId = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        itemId = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            if (itemId != null) {
                result = new ItemRevision();
                br = callCatalog("?op=getrev&owner="+itemId+"&xbindex=" + revId);
                while (!("".equals(line = br.readLine()))&&(line!=null)) {
                    if ("xbindex".equals(line)) {
                        line = br.readLine();
                        result.setXBIndex(new Long(line));
                    } else if ("xblimit".equals(line)) {
                        line = br.readLine();
                        result.setXBLimit(new Long(line));
                    } else {
                        br.readLine();
                    }
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ItemRevision getBlockSpecRevision(Long[] path, Long specId, Long revId) {
        try {
            BufferedReader br = callCatalog("?op=getspec&spec="+specId+"&dtype=2&path="+getParamPath(path));
            String line;
            ItemRevision result = null;
            Long itemId = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        itemId = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            if (itemId != null) {
                result = new ItemRevision();
                br = callCatalog("?op=getrev&owner="+itemId+"&xbindex=" + revId);
                while (!("".equals(line = br.readLine()))&&(line!=null)) {
                    if ("id".equals(line)) {
                        line = br.readLine();
                        result.setId(new Long(line));
                    } else if ("xbindex".equals(line)) {
                        line = br.readLine();
                        result.setXBIndex(new Long(line));
                    } else if ("xblimit".equals(line)) {
                        line = br.readLine();
                        result.setXBLimit(new Long(line));
                    } else {
                        br.readLine();
                    }
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public RevisionPath getFormatCatalogBindTargetPath(Long[] path, Long specId, Long bindId) {
        try {
            BufferedReader br = callCatalog("?op=getspec&spec="+specId+"&dtype=0&path="+getParamPath(path));
            String line;
            String itemId = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    itemId = br.readLine();
                } else {
                    br.readLine();
                }
            }
            br = callCatalog("?op=getdef&origin="+itemId+"&xbindex="+bindId);
            String targetSpec = "";
            Long revXB = null;
            Long bindType = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("spec".equals(line)) {
                    targetSpec = br.readLine();
                } else if ("revxb".equals(line)) {
                    revXB = new Long(br.readLine());
                } else if ("btype".equals(line)) {
                    bindType = new Long(br.readLine());
                } else {
                    br.readLine();
                }
            }
            if ("".equals(targetSpec)) {
                return null;
            }
            br = callCatalog("?op=getnodepath&node="+targetSpec);
            String nodePath = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("path".equals(line)) {
                    nodePath = br.readLine();
                } else {
                    br.readLine();
                }
            }
            List<Long> myPath = new ArrayList<Long>();
            int prev = 0;
            for (int i = 0; i < nodePath.length(); i++) {
                if (nodePath.charAt(i)=='/') {
                    myPath.add(new Long(nodePath.substring(prev,i)));
                    prev = i+1;
                }
            }
            Long xbIndex = new Long(nodePath.substring(prev));
            RevisionPath result = new RevisionPath();
            result.setBindType(bindType);
            result.setPath((Long[]) myPath.toArray(new Long[myPath.size()]));
            result.setSpecId(xbIndex);
            result.setRevXBId(revXB);
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public RevisionPath getGroupCatalogBindTargetPath(@WebParam(name = "path") Long[] path, @WebParam(name = "specId") Long specId, @WebParam(name = "bindId") Long bindId) {
        try {
            BufferedReader br = callCatalog("?op=getspec&spec="+specId+"&dtype=1&path="+getParamPath(path));
            String line;
            String itemId = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    itemId = br.readLine();
                } else {
                    br.readLine();
                }
            }
            br = callCatalog("?op=getdef&origin="+itemId+"&xbindex="+bindId);
            String targetSpec = "";
            Long revXB = null;
            Long bindType = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("spec".equals(line)) {
                    targetSpec = br.readLine();
                } else if ("revxb".equals(line)) {
                    revXB = new Long(br.readLine());
                } else if ("btype".equals(line)) {
                    bindType = new Long(br.readLine());
                } else {
                    br.readLine();
                }
            }
            if ("".equals(targetSpec)) {
                return null;
            }
            br = callCatalog("?op=getnodepath&node="+targetSpec);
            String nodePath = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("path".equals(line)) {
                    nodePath = br.readLine();
                } else {
                    br.readLine();
                }
            }
            List<Long> myPath = new ArrayList<Long>();
            int prev = 0;
            for (int i = 0; i < nodePath.length(); i++) {
                if (nodePath.charAt(i)=='/') {
                    myPath.add(new Long(nodePath.substring(prev,i)));
                    prev = i+1;
                }
            }
            Long xbIndex = new Long(nodePath.substring(prev));
            RevisionPath result = new RevisionPath();
            result.setBindType(bindType);
            result.setPath((Long[]) myPath.toArray(new Long[myPath.size()]));
            result.setSpecId(xbIndex);
            result.setRevXBId(revXB);
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public RevisionPath getBlockCatalogBindTargetPath(@WebParam(name = "path") Long[] path, @WebParam(name = "specId") Long specId, @WebParam(name = "bindId") Long bindId) {
        try {
            BufferedReader br = callCatalog("?op=getspec&spec="+specId+"&dtype=2&path="+getParamPath(path));
            String line;
            String itemId = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    itemId = br.readLine();
                } else {
                    br.readLine();
                }
            }
            br = callCatalog("?op=getdef&origin="+itemId+"&xbindex="+bindId);
            String targetSpec = "";
            Long revXB = null;
            Long bindType = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("spec".equals(line)) {
                    targetSpec = br.readLine();
                } else if ("revxb".equals(line)) {
                    revXB = new Long(br.readLine());
                } else if ("btype".equals(line)) {
                    bindType = new Long(br.readLine());
                } else {
                    br.readLine();
                }
            }
            if ("".equals(targetSpec)) {
                return null;
            }
            br = callCatalog("?op=getnodepath&node="+targetSpec);
            String nodePath = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("path".equals(line)) {
                    nodePath = br.readLine();
                } else {
                    br.readLine();
                }
            }
            List<Long> myPath = new ArrayList<Long>();
            int prev = 0;
            for (int i = 0; i < nodePath.length(); i++) {
                if (nodePath.charAt(i)=='/') {
                    myPath.add(new Long(nodePath.substring(prev,i)));
                    prev = i+1;
                }
            }
            Long xbIndex = new Long(nodePath.substring(prev));
            RevisionPath result = new RevisionPath();
            result.setBindType(bindType);
            result.setPath((Long[]) myPath.toArray(new Long[myPath.size()]));
            result.setSpecId(xbIndex);
            result.setRevXBId(revXB);
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ItemFile getCatalogFile(Long[] path, Long prev) {
        try {
            BufferedReader br = callCatalog("?op=getnode&path="+getParamPath(path));
            String line;
            String itemId = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    itemId = br.readLine();
                } else {
                    br.readLine();
                }
            }
            if ("".equals(itemId)) {
                return null;
            }
            ItemFile itemFile = new ItemFile();
            br = callCatalog("?op=getfile&node="+itemId+(prev==null?"":"&prev="+prev.toString()));
            itemId = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    itemId = br.readLine();
                    itemFile.setId(new Long(itemId));
                } else if ("filename".equals(line)) {
                    itemFile.setFileName(br.readLine());
                } else {
                    br.readLine();
                }
            }
            if (itemFile.getId() == null) {
                return null;
            }
            return itemFile;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getItemIconMaxIndex(Long itemId) {
        if (itemId == null) {
            return null;
        }
        try {
            BufferedReader br = callCatalog("?op=geticonmax&owner="+itemId);
            String line;
            Long result = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("max".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ItemFile getItemIcon(Long itemId, Long i) {
        if (itemId == null) {
            return null;
        }
        try {
            BufferedReader br = callCatalog("?op=geticon&owner="+itemId+"&xbindex="+i);
            String line;
            ItemFile result = new ItemFile();
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    result.setId(new Long(br.readLine()));
                } else if ("mode".equals(line)) {
                    result.setMode(new Long(br.readLine()));
//                } else if ("xbindex".equals(line)) {
//                    result.setXBIndex(new Long(br.readLine()));
                } else if ("node".equals(line)) { // TODO: This is ugly
                    result.setXBIndex(new Long(br.readLine()));
                } else if ("filename".equals(line)) {
                    result.setFileName(br.readLine());
                } else {
                    br.readLine();
                }
            }
            if (result.getId() == null) {
                return null;
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long[] getNodePath(Long nodeId) {
        BufferedReader br;
        List<Long> myPath = new ArrayList<Long>();
        try {
            if (nodeId == null) {
                return null;
            }
            br = callCatalog("?op=getnodepath&node=" + nodeId);
            String line;
            String nodePath = "";
            while (!("".equals(line = br.readLine())) && (line != null)) {
                if ("path".equals(line)) {
                    nodePath = br.readLine();
                } else {
                    br.readLine();
                }
            }
            int prev = 0;
            for (int i = 0; i < nodePath.length(); i++) {
                if (nodePath.charAt(i) == '/') {
                    myPath.add(new Long(nodePath.substring(prev, i)));
                    prev = i + 1;
                }
            }
            myPath.add(new Long(nodePath.substring(prev)));
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
 /*           try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            } */
        }
        return myPath.toArray(new Long[0]);
    }

    public Long getBlockCatalogRevMaxLineIndex(Long revId) {
        if (revId == null) {
            return null;
        }
        try {
            BufferedReader br = callCatalog("?op=getlinemax&rev="+revId);
            String line;
            Long result = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("max".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getBlockCatalogRevMaxPaneIndex(Long revId) {
        if (revId == null) {
            return null;
        }
        try {
            BufferedReader br = callCatalog("?op=getpanemax&rev="+revId);
            String line;
            Long result = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("max".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public RevisionPlug getBlockCatalogRevLine(Long revId, Long i) {
        if (revId == null) {
            return null;
        }
        try {
            BufferedReader br = callCatalog("?op=getline&rev="+revId+"&prior="+i);
            String line;
            RevisionPlug result = new RevisionPlug();
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result.setId(new Long(line));
                    }
                } else if ("line".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result.setItemId(new Long(line));
                    }
                } else if ("plug".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result.setPlugId(new Long(line));
                    }
                } else if ("plugnode".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result.setPlugNode(new Long(line));
                    }
                } else if ("plugin".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result.setPlugin(new Long(line));
                    }
                } else {
                    br.readLine();
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public RevisionPlug getBlockCatalogRevPane(Long revId, Long i) {
        if (revId == null) {
            return null;
        }
        try {
            BufferedReader br = callCatalog("?op=getpane&rev="+revId+"&prior="+i);
            String line;
            RevisionPlug result = new RevisionPlug();
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result.setId(new Long(line));
                    }
                } else if ("pane".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result.setItemId(new Long(line));
                    }
                } else if ("plug".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result.setPlugId(new Long(line));
                    }
                } else if ("plugnode".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result.setPlugNode(new Long(line));
                    }
                } else if ("plugin".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result.setPlugin(new Long(line));
                    }
                } else {
                    br.readLine();
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getPluginCatalogNodeMaxPluginIndex(Long nodeId) {
        if (nodeId == null) {
            return null;
        }
        try {
            BufferedReader br = callCatalog("?op=getplugmax&node="+nodeId);
            String line;
            Long result = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("max".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ItemPlugin getPluginCatalogNodePlugin(Long nodeId, Long i) {
        if (nodeId == null) {
            return null;
        }
        if (i == null) {
            return null;
        }
        try {
            BufferedReader br = callCatalog("?op=getplug&node="+nodeId+"&plugin="+i);
            String line;
            ItemPlugin result = new ItemPlugin();
            result.setNodeId(nodeId);
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result.setId(new Long(line));
                    }
                } else if ("file".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result.setFileId(new Long(line));
                    }
                } else if ("plugin".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result.setPlugin(new Long(line));
                    }
                } else if ("filenode".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result.setFileNode(new Long(line));
                    }
                } else if ("filename".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result.setFilename(line);
                    }
                } else {
                    br.readLine();
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getPluginCatalogPlugLineMaxIndex(Long itemId) {
        if (itemId == null) {
            return null;
        }
        try {
            BufferedReader br = callCatalog("?op=getpluglinemax&plug="+itemId);
            String line;
            Long result = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("max".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getPluginCatalogPlugLine(Long itemId, Long plugLine) {
        if (itemId == null) {
            return null;
        }
        try {
            BufferedReader br = callCatalog("?op=getplugline&plug="+itemId+"&line="+plugLine);
            String line;
            Long result = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getPluginCatalogPlugPaneMaxIndex(Long itemId) {
        if (itemId == null) {
            return null;
        }
        try {
            BufferedReader br = callCatalog("?op=getplugpanemax&plug="+itemId);
            String line;
            Long result = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("max".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long getPluginCatalogPlugPane(Long itemId, Long plugPane) {
        if (itemId == null) {
            return null;
        }
        try {
            BufferedReader br = callCatalog("?op=getplugpane&plug="+itemId+"&pane="+plugPane);
            String line;
            Long result = null;
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("id".equals(line)) {
                    line = br.readLine();
                    if (!line.isEmpty()) {
                        result = new Long(line);
                    }
                } else {
                    br.readLine();
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private void tail() {
        if (compoundCall != null) {
            try {
                String line;
                while (!("".equals(line = compoundCall.readLine()))&&(line!=null)) {
                    compoundCall.readLine();
                }
            } catch (IOException ex) {
                Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Date getRootLastUpdate() {
        try {
            BufferedReader br = callCatalog("?op=getroot");
            String line;
            String lastUpdateString = "";
            while (!("".equals(line = br.readLine()))&&(line!=null)) {
                if ("lastupdate".equals(line)) {
                    lastUpdateString = br.readLine();
                } else {
                    br.readLine();
                }
            }

            if (lastUpdateString == null || lastUpdateString.isEmpty()) {
                return null;
            }
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(Integer.valueOf(lastUpdateString.substring(0,4)), Integer.valueOf(lastUpdateString.substring(5,7)), Integer.valueOf(lastUpdateString.substring(8,10)), 0,0,0);
            return cal.getTime();
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ItemInfo getCatalogFullContent(Long[] path, Long lang) {
        try {
            BufferedReader br = compoundCallCatalog("?op=getfull");
            return getCatalogNodeInfo(path, lang);
        } catch (IOException ex) {
            Logger.getLogger(XBCUpdatePHPPort.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
