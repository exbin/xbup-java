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
package org.xbup.lib.framework.gui;

import java.net.URI;
import java.util.List;
import javax.swing.JFileChooser;
import net.xeoh.plugins.base.PluginManager;

/**
 * Interface for application module's repository.
 *
 * @version 0.2.0 2015/10/28
 * @author XBUP Project (http://xbup.org)
 */
public interface ModuleRepository {

    /**
     * Scans for all valid modules in give directory.
     *
     * @param uri
     */
    public void addPluginsFrom(URI uri);

    /**
     * Gets active module.
     *
     * @return the activeModule
     */
    public long getActiveModule();

    /**
     * Gets currently active plugin handler.
     *
     * @return application module
     */
    public ApplicationModule getPluginHandler();

    /**
     * Gets plugin manager.
     *
     * @return the pluginManager
     */
    public PluginManager getPluginManager();

    /**
     * Opens given file.
     *
     * @param openFC file chooser
     * @return true if file successfully opened
     */
    public boolean openFile(JFileChooser openFC);

    /**
     * Opens file from given properties.
     *
     * @param path full path
     * @param fileTypeId file type id
     * @return true if file successfully opened
     */
    public boolean openFile(String path, String fileTypeId);

    /**
     * Gets list of modules.
     *
     * @return list of modules
     */
    public List<ApplicationModuleInfo> getModulesList();

    /**
     * Creates new file.
     */
    public void newFile();

    /**
     * Saves file under the given filename.
     *
     * @param saveFC file chooser
     * @return true if file successfully saved
     */
    public boolean saveFile(JFileChooser saveFC);

    /**
     * Saves file under the same filename.
     *
     * @return true if file successfully saved
     */
    public boolean saveFile();
}
