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
package org.xbup.tool.xbeditorbase.base.manager;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.options.getplugin.PluginSelector;
import net.xeoh.plugins.base.util.PluginManagerUtil;
import org.xbup.tool.xbeditor.module.mainframe.MainFrame;
import org.xbup.tool.xbeditor.module.mainframe.dialog.OptionsDialog;
import org.xbup.tool.xbeditorbase.base.api.ApplicationModule;
import org.xbup.tool.xbeditorbase.base.api.ApplicationModuleInfo;
import org.xbup.tool.xbeditorbase.base.api.ModuleRepository;

/**
 * Repository for modules and managers.
 *
 * @version 0.1 wr22.0 2013/03/04
 * @author XBUP Project (http://xbup.org)
 */
public class BaseModuleRepository implements ModuleRepository {

    private long activeModule;

    private MainFrame mainFrame;
    private Map<Long, ApplicationModule> modules;

    private PluginManager pluginManager;
    private ModuleManager moduleManager;

    public BaseModuleRepository() {
        modules = new HashMap<Long, ApplicationModule>();
        pluginManager = PluginManagerFactory.createPluginManager();
        mainFrame = null;

        // fixed load of modules
/*        pluginManager.addPluginsFrom(new ClassURI(ScriptEditorModule.class).toURI());
        pluginManager.addPluginsFrom(new ClassURI(GraphEditorModule.class).toURI());
        pluginManager.addPluginsFrom(new ClassURI(WaveEditorModule.class).toURI());
        pluginManager.addPluginsFrom(new ClassURI(JavaHelpModule.class).toURI());
        pluginManager.addPluginsFrom(new ClassURI(OnlineHelpModule.class).toURI()); */
/*        File directory = new File("./modules");
        if (directory.exists()) {
            System.out.println("Loading plugins from: ./modules");
            pluginManager.addPluginsFrom(directory.toURI());
        } else {
            directory = new File("./../modules");
            if (directory.exists()) {
                System.out.println("Loading plugins from:  ./../modules");
                pluginManager.addPluginsFrom(directory.toURI());
            } else {
                addPlugin("GraphEditor.jar");
                addPlugin("JavaHelp.jar");
                addPlugin("OnlineHelp.jar");
                addPlugin("ScriptEditor.jar");
                addPlugin("WaveEditor.jar");
            }
        } */
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

        for (Iterator<ApplicationModule> module = pluginCol.iterator(); module.hasNext();) {
            ApplicationModule applicationModule = module.next();
            ApplicationModuleInfo info = applicationModule.getInfo();
            activeModule++;
            modules.put(getActiveModule(),applicationModule);
            applicationModule.init(moduleManager);
        }

        // TODO: Finish section
        moduleManager.getFileTypeManagement().finish();
    }

    private void addPlugin(String pluginName) {
/*        try {
            pluginManager.addPluginsFrom(new URI("http:///modules/"+pluginName)); // classpath://*
        } catch (URISyntaxException ex1) {
            Exceptions.printStackTrace(ex1);
        }
        InputStream iStream = null;
        try {
            String pluginPath = "http:///modules/" + pluginName;
            iStream = new URL(pluginPath).openStream();
            java.io.File tmpFile = null;
            try {
                tmpFile = java.io.File.createTempFile("jspfplugindownload", ".jar");
                FileOutputStream oStream = new FileOutputStream(tmpFile);
                while (iStream.available() > 0) {
                    oStream.write(iStream.read()); // TODO: buffer read
                }
                iStream.close();
                oStream.close();
            } catch (IOException ex) {
                Logger.getLogger(BaseModuleRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (tmpFile != null) {
                pluginManager.addPluginsFrom(tmpFile.getAbsoluteFile().toURI());
            }
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                iStream.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
*/    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        moduleManager = new ModuleManager(this);
    }

    /**
     * @return the mainFrame
     */
    public MainFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * @return the activeModule
     */
    @Override
    public long getActiveModule() {
        return activeModule;
    }

    /**
     * @return the pluginManager
     */
    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    /** Open file using appropriate plugin */
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
        for (int i = 0; i < plugins.size(); i++) {
            URI uri = plugins.get(i);
            addPluginsFrom(uri);
        }
    }

    @Override
    public List<ApplicationModuleInfo> getModulesList() {
        List<ApplicationModuleInfo> moduleList = new ArrayList<ApplicationModuleInfo>();
        for (long moduleIndex = 1; moduleIndex <= activeModule; moduleIndex++) {
            ApplicationModule module = modules.get(new Long(moduleIndex));
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

    /**
     * @return the optionsDialog
     */
    public OptionsDialog getOptionsDialog() {
        return mainFrame.getOptionsDialog();
    }

    public class XBPluginSelector implements PluginSelector<ApplicationModule> {

        private String filePath;

        public XBPluginSelector(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public boolean selectPlugin(ApplicationModule plugin) {
            String plugPath = "/"; /* + plugin.getPluginPath();
            if (plugPath == null) {
                return false;
            } */
            return plugPath.equals(filePath);
        }
    }
}
