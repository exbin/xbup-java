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
package org.xbup.lib.plugin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.options.GetPluginOption;
import net.xeoh.plugins.base.options.getplugin.OptionPluginSelector;
import net.xeoh.plugins.base.options.getplugin.PluginSelector;
import net.xeoh.plugins.base.util.PluginManagerUtil;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.catalog.base.XBCXPlugin;
import org.xbup.lib.core.catalog.base.XBCXStri;
import org.xbup.lib.core.catalog.base.service.XBCXFileService;
import org.xbup.lib.core.catalog.base.service.XBCXStriService;
import org.xbup.lib.core.util.StreamUtils;

/**
 * XBUP Transformation Plugin Base Class.
 *
 * @version 0.1.24 2015/01/09
 * @author XBUP Project (http://xbup.org)
 */
public class XBPluginRepository {

    private final PluginManager pluginManager;
    private final Map<Long, XBPlugin> plugins;
    private XBACatalog catalog;

    public XBPluginRepository() {
        plugins = new HashMap<>();
        pluginManager = PluginManagerFactory.createPluginManager();
    }

    public void addPluginsFrom(URI uri) {
        pluginManager.addPluginsFrom(uri);

    }

    public void processPlugins() {
        PluginManagerUtil pmu = new PluginManagerUtil(pluginManager);
        final Collection<XBPlugin> pluginCol = pmu.getPlugins(XBPlugin.class);

        if (catalog != null) {
//            for (final XBPlugin plugin : pluginCol) {
//                catalog.ge
//                plugins.put(new Long(0), plugin);
//            }
        }
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    public XBPlugin getPluginHandler(XBCXPlugin plugin) {
        // TODO: Use local files if available
        // TODO: Use repository cache if available
        XBCXFile plugFile = plugin.getPluginFile();
        if (plugFile == null) {
            return null;
        }
        XBCXStriService striService = (XBCXStriService) catalog.getCatalogService(XBCXStriService.class);
        if (plugins.containsKey(plugFile.getId())) {
            return (XBPlugin) plugins.get(plugFile.getId());
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
        pluginManager.addPluginsFrom(tmpFile.getAbsoluteFile().toURI());
        GetPluginOption plugOpt = new OptionPluginSelector<>(new XBPluginSelector(filePath));
        XBPlugin processedPlugin = (XBPlugin) pluginManager.getPlugin(XBPlugin.class, plugOpt);
        if (processedPlugin != null) {
            plugins.put(plugFile.getId(), processedPlugin);
        }
        return processedPlugin;
    }

    public class XBPluginSelector implements PluginSelector<XBPlugin> {

        private final String filePath;

        public XBPluginSelector(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public boolean selectPlugin(XBPlugin plugin) {
            // System.out.println("Plugin filename:" + filePath);
            String plugPath = "/" + plugin.getPluginPath();
            if (plugPath == null) {
                return false;
            }
            return plugPath.equals(filePath);
        }
    }
}
