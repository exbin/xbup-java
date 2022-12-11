/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.plugin;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.XBCXStri;
import org.exbin.xbup.core.catalog.base.service.XBCXFileService;
import org.exbin.xbup.core.catalog.base.service.XBCXStriService;
import org.exbin.xbup.core.util.StreamUtils;
import org.exbin.xbup.core.util.StringUtils;

/**
 * XBUP plugin repository.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPluginRepository {

    private final Map<Long, XBCatalogPlugin> pluginsCache;
    private XBACatalog catalog;

    public XBPluginRepository() {
        pluginsCache = new HashMap<>();
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
        XBCXStriService striService = catalog.getCatalogService(XBCXStriService.class);
        if (pluginsCache.containsKey(plugFile.getId())) {
            return (XBCatalogPlugin) pluginsCache.get(plugFile.getId());
        }
        XBCXFileService fileService = catalog.getCatalogService(XBCXFileService.class);
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
//        return null; //processedPlugin;
        return loadXBPlugin(tmpFile.getAbsoluteFile().toURI());
    }

    public XBCatalogPlugin loadXBPlugin(URI libraryUri) {
        ClassLoader loader;
        try {
            loader = URLClassLoader.newInstance(new URL[]{libraryUri.toURL()}, getClass().getClassLoader());

            URL pluginRecordUrl;
            InputStream pluginRecordStream = null;
            try {
                pluginRecordUrl = new URL("jar:" + libraryUri.toURL().toExternalForm() + "!/META-INF/" + "plugin.txt");
                pluginRecordStream = pluginRecordUrl.openStream();
            } catch (IOException ex) {
                // ignore
            }
            if (pluginRecordStream != null) {
                try {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    StreamUtils.copyInputStreamToOutputStream(pluginRecordStream, outputStream);
                    String className = new String(outputStream.toByteArray(), StringUtils.ENCODING_UTF8);
                    Class<?> clazz = Class.forName(className, true, loader);
                    Constructor<?> ctor = clazz.getConstructor();
                    return (XBCatalogPlugin) ctor.newInstance();
                } catch (IOException ex) {
                    // ignore
                } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(XBPluginRepository.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                throw new IllegalStateException("Unable to read plugin.txt");
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(XBPluginRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new IllegalStateException("Unable to load catalog plugin");
    }
}
