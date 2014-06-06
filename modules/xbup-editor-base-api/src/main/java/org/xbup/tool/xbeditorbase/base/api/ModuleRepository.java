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
package org.xbup.tool.xbeditorbase.base.api;

import java.net.URI;
import java.util.List;
import javax.swing.JFileChooser;
import net.xeoh.plugins.base.PluginManager;

/**
 * Interface for application module's repository.
 *
 * @version 0.1 wr21.0 2012/05/11
 * @author XBUP Project (http://xbup.org)
 */
public interface ModuleRepository {

    /**
     * Scan for all valid modules in give directory.
     *
     * @param uri
     */
    public void addPluginsFrom(URI uri);

    /**
     * Get active module.
     *
     * @return the activeModule
     */
    public long getActiveModule();

    /**
     * Get currently active plugin handler.
     *
     * @return application module
     */
    public ApplicationModule getPluginHandler();

    /**
     * Get plugin manager.
     *
     * @return the pluginManager
     */
    public PluginManager getPluginManager();

    /**
     * Open given file.
     *
     * @param openFC file chooser
     * @return true if file successfully opened
     */
    public boolean openFile(JFileChooser openFC);

    /**
     * Open file from given properties.
     *
     * @param path full path
     * @param fileTypeId file type id
     * @return true if file successfully opened
     */
    public boolean openFile(String path, String fileTypeId);

    /**
     * Get list of modules.
     *
     * @return list of modules
     */
    public List<ApplicationModuleInfo> getModulesList();

    /**
     * Create new file.
     */
    public void newFile();

    /**
     * Save file under the given filename.
     *
     * @param saveFC file chooser
     * @return true if file successfully saved
     */
    public boolean saveFile(JFileChooser saveFC);

    /**
     * Save file under the same filename.
     *
     * @return true if file successfully saved
     */
    public boolean saveFile();
}
