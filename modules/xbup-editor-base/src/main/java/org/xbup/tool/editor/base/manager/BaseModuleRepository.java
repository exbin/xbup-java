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
package org.xbup.tool.editor.base.manager;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.options.getplugin.PluginSelector;
import net.xeoh.plugins.base.util.PluginManagerUtil;
import org.xbup.tool.editor.module.frame.MainFrame;
import org.xbup.tool.editor.module.frame.dialog.OptionsDialog;
import org.xbup.tool.editor.base.api.ApplicationModule;
import org.xbup.tool.editor.base.api.ApplicationModuleInfo;
import org.xbup.tool.editor.base.api.ModuleRepository;

/**
 * Repository for modules and managers.
 *
 * @version 0.2.0 2015/10/21
 * @author XBUP Project (http://xbup.org)
 */
public class BaseModuleRepository implements ModuleRepository {

    private long activeModule;

    private MainFrame mainFrame;
    private final Map<Long, ApplicationModule> modules;

    private final PluginManager pluginManager;
    private ModuleManager moduleManager;

    public BaseModuleRepository() {
        modules = new HashMap<>();
        pluginManager = PluginManagerFactory.createPluginManager();
        mainFrame = null;

        moduleManager = null;
        activeModule = 0;
    }

    @Override
    public void addPluginsFrom(URI uri) {
        pluginManager.addPluginsFrom(uri);
    }

    public void processModules() {
        PluginManagerUtil pmu = new PluginManagerUtil(pluginManager);
        final Collection<ApplicationModule> pluginCol = pmu.getPlugins(ApplicationModule.class);

        for (ApplicationModule applicationModule : pluginCol) {
            ApplicationModuleInfo info = applicationModule.getInfo();
            activeModule++;
            modules.put(getActiveModule(), applicationModule);
            applicationModule.init(moduleManager);
        }

        moduleManager.getFileTypeManagement().finish();
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        moduleManager = new ModuleManager(this);
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    @Override
    public long getActiveModule() {
        return activeModule;
    }

    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    /**
     * Opens file using appropriate plugin.
     */
    @Override
    public boolean openFile(JFileChooser openFC) {
        boolean fileOpened = moduleManager.getFileTypeManagement().openFile(openFC);
        mainFrame.setTitle(moduleManager.getFileTypeManagement().getWindowTitle());
        return fileOpened;
    }

    @Override
    public boolean openFile(String path, String fileTypeId) {
        return moduleManager.getFileTypeManagement().openFile(path, fileTypeId);
    }

    @Override
    public ApplicationModule getPluginHandler() {
        return modules.get(activeModule);
    }

    public ApplicationModule getPluginHandler(Class moduleClass) {
        for (long i = 0; i <= activeModule; i++) {
            ApplicationModule module = modules.get(i);
            if (moduleClass.isInstance(module)) {
                return module;
            }
        }
        return null;
    }

    public void loadPreferences(Preferences preferences) {
        mainFrame.loadPreferences();
    }

    public Preferences getPreferences() {
        return mainFrame.getPreferences();
    }

    public void addPluginsFrom(List<URI> plugins) {
        for (URI uri : plugins) {
            addPluginsFrom(uri);
        }
    }

    @Override
    public List<ApplicationModuleInfo> getModulesList() {
        List<ApplicationModuleInfo> moduleList = new ArrayList<>();
        for (long moduleIndex = 1; moduleIndex <= activeModule; moduleIndex++) {
            ApplicationModule module = modules.get(moduleIndex);
            if (module != null) {
                moduleList.add(module.getInfo());
            }
        }
        return moduleList;
    }

    @Override
    public void newFile() {
        moduleManager.getFileTypeManagement().newFile();
    }

    @Override
    public boolean saveFile(JFileChooser saveFC) {
        return moduleManager.getFileTypeManagement().saveFile(saveFC);
    }

    @Override
    public boolean saveFile() {
        return moduleManager.getFileTypeManagement().saveFile();
    }

    public OptionsDialog getOptionsDialog() {
        return mainFrame.getOptionsDialog();
    }

    public class XBPluginSelector implements PluginSelector<ApplicationModule> {

        private final String filePath;

        public XBPluginSelector(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public boolean selectPlugin(ApplicationModule plugin) {
            String plugPath = "/";
            /* + plugin.getPluginPath();
             if (plugPath == null) {
             return false;
             } */

            return plugPath.equals(filePath);
        }
    }
}
