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
package org.exbin.xbup.plugin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.XBCXStri;
import org.exbin.xbup.core.catalog.base.service.XBCXFileService;
import org.exbin.xbup.core.catalog.base.service.XBCXStriService;
import org.exbin.xbup.core.util.StreamUtils;

/**
 * XBUP plugin repository.
 *
 * @version 0.2.0 2015/03/27
 * @author ExBin Project (http://exbin.org)
 */
public class XBPluginRepository {

    private final Map<Long, XBCatalogPlugin> plugins;
    private XBACatalog catalog;

    public XBPluginRepository() {
        plugins = new HashMap<>();
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    public XBCatalogPlugin getPluginHandler(XBCXPlugin plugin) {
        // TODO: Use local files if available
        // TODO: Use repository cache if available
        XBCXFile plugFile = plugin.getPluginFile();
        if (plugFile == null) {
            return null;
        }
        XBCXStriService striService = (XBCXStriService) catalog.getCatalogService(XBCXStriService.class);
        if (plugins.containsKey(plugFile.getId())) {
            return (XBCatalogPlugin) plugins.get(plugFile.getId());
        }
        XBCXFileService fileService = (XBCXFileService) catalog.getCatalogService(XBCXFileService.class);
        XBCXStri stri = striService.getItemStringId(plugFile.getNode());
        String filePath = striService.getFullPath(stri);
        filePath += "/" + plugFile.getFilename();
        InputStream iStream = fileService.getFile(plugFile);
        // TODO: Download file and load plugin
        java.io.File tmpFile = null;
        try {
            tmpFile = java.io.File.createTempFile("jspfplugindownload", ".jar");
            try (FileOutputStream oStream = new FileOutputStream(tmpFile)) {
                StreamUtils.copyInputStreamToOutputStream(iStream, oStream);
                iStream.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(XBPluginRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (tmpFile == null) {
            return null;
        }
//        pluginManager.addPluginsFrom(tmpFile.getAbsoluteFile().toURI());
//        GetPluginOption plugOpt = new OptionPluginSelector<>(new XBPluginSelector(filePath));
//        XBCatalogPlugin processedPlugin = (XBCatalogPlugin) pluginManager.getPlugin(XBCatalogPlugin.class, plugOpt);
//        if (processedPlugin != null) {
//            plugins.put(plugFile.getId(), processedPlugin);
//        }
        return null; //processedPlugin;
    }
}
