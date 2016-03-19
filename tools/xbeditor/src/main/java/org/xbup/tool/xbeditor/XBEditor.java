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
package org.xbup.tool.xbeditor;

import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.exbin.framework.XBBaseApplication;
import org.exbin.framework.api.XBModuleRepository;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.parser.basic.XBHead;
import org.exbin.framework.client.api.ClientModuleApi;
import org.exbin.framework.editor.text.EditorTextModule;
import org.exbin.framework.editor.xbup.EditorXbupModule;
import org.exbin.framework.gui.about.api.GuiAboutModuleApi;
import org.exbin.framework.gui.editor.api.GuiEditorModuleApi;
import org.exbin.framework.gui.editor.api.XBEditorProvider;
import org.exbin.framework.gui.file.api.GuiFileModuleApi;
import org.exbin.framework.gui.frame.api.ApplicationFrameHandler;
import org.exbin.framework.gui.frame.api.GuiFrameModuleApi;
import org.exbin.framework.gui.help.api.GuiHelpModuleApi;
import org.exbin.framework.gui.menu.api.GuiMenuModuleApi;
import org.exbin.framework.gui.options.api.GuiOptionsModuleApi;
import org.exbin.framework.gui.undo.api.GuiUndoModuleApi;
import org.exbin.framework.gui.utils.ActionUtils;
import org.xbup.lib.operation.undo.XBTLinearUndo;
import org.xbup.lib.plugin.XBPluginRepository;

/**
 * The main class of the XBEditor application.
 *
 * @version 0.2.0 2016/03/01
 * @author ExBin Project (http://exbin.org)
 */
public class XBEditor {

    /**
     * Main method launching the application.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        Preferences preferences;
        boolean verboseMode;
        boolean devMode;
        ResourceBundle bundle = ActionUtils.getResourceBundleByClass(XBEditor.class);

        try {
            preferences = Preferences.userNodeForPackage(XBEditor.class);
        } catch (SecurityException ex) {
            preferences = null;
        }
        try {
            // Parameters processing
            Options opt = new Options();
            opt.addOption("h", "help", false, bundle.getString("cl_option_help"));
            opt.addOption("v", false, bundle.getString("cl_option_verbose"));
            opt.addOption("dev", false, bundle.getString("cl_option_dev"));
            BasicParser parser = new BasicParser();
            CommandLine cl = parser.parse(opt, args);
            if (cl.hasOption('h')) {
                HelpFormatter f = new HelpFormatter();
                f.printHelp(bundle.getString("cl_syntax"), opt);
            } else {
                verboseMode = cl.hasOption("v");
                devMode = cl.hasOption("dev");
                Logger logger = Logger.getLogger("");
                try {
                    logger.setLevel(Level.ALL);
                    logger.addHandler(new XBHead.XBLogHandler(verboseMode));
                } catch (java.security.AccessControlException ex) {
                    // Ignore it in java webstart
                }

                XBBaseApplication app = new XBBaseApplication();
                app.setAppPreferences(preferences);
                app.setAppBundle(bundle, ActionUtils.getResourceBaseNameBundleByClass(XBEditor.class));
                app.init();

                XBModuleRepository moduleRepository = app.getModuleRepository();
                moduleRepository.addClassPathPlugins();
                moduleRepository.addPluginsFromManifest(XBEditor.class);
                moduleRepository.initModules();

                GuiFrameModuleApi frameModule = moduleRepository.getModuleByInterface(GuiFrameModuleApi.class);
                GuiEditorModuleApi editorModule = moduleRepository.getModuleByInterface(GuiEditorModuleApi.class);
                GuiMenuModuleApi menuModule = moduleRepository.getModuleByInterface(GuiMenuModuleApi.class);
                GuiAboutModuleApi aboutModule = moduleRepository.getModuleByInterface(GuiAboutModuleApi.class);
                GuiHelpModuleApi helpModule = moduleRepository.getModuleByInterface(GuiHelpModuleApi.class);
                GuiUndoModuleApi undoModule = moduleRepository.getModuleByInterface(GuiUndoModuleApi.class);
                GuiFileModuleApi fileModule = moduleRepository.getModuleByInterface(GuiFileModuleApi.class);
                final ClientModuleApi clientModule = moduleRepository.getModuleByInterface(ClientModuleApi.class);
                GuiOptionsModuleApi optionsModule = moduleRepository.getModuleByInterface(GuiOptionsModuleApi.class);
                final EditorXbupModule xbupEditorModule = moduleRepository.getModuleByInterface(EditorXbupModule.class);
                final EditorTextModule textEditorModule = moduleRepository.getModuleByInterface(EditorTextModule.class);

                xbupEditorModule.setDevMode(devMode);
                aboutModule.registerDefaultMenuItem();
                try {
                    helpModule.setHelpUrl(new URL(bundle.getString("online_help_url")));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(XBEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
                helpModule.registerMainMenu();
                helpModule.registerOnlineHelpMenu();

                frameModule.registerExitAction();
                frameModule.registerBarsVisibilityActions();

                // Register clipboard editing actions
                fileModule.registerMenuFileHandlingActions();
                fileModule.registerToolBarFileHandlingActions();
                fileModule.registerCloseListener();
                fileModule.registerLastOpenedMenuActions();

                undoModule.registerMainMenu();
                undoModule.registerMainToolBar();
                undoModule.registerUndoManagerInMainMenu();
                XBTLinearUndo linearUndo = new XBTLinearUndo(null);
//                linearUndo.addUndoUpdateListener(new UndoUpdateListener() {
//                    @Override
//                    public void undoChanged() {
//                        ((AudioPanel) waveEditorModule.getEditorProvider()).repaint();
//                    }
//                });
                undoModule.setUndoHandler(linearUndo);
                xbupEditorModule.setUndoHandler(linearUndo);

                // Register clipboard editing actions
                menuModule.registerMenuClipboardActions();
                menuModule.registerToolBarClipboardActions();

                optionsModule.registerMenuAction();

                textEditorModule.registerEditFindMenuActions();
                textEditorModule.registerWordWrapping();
                textEditorModule.registerGoToLine();
                textEditorModule.registerPrintMenu();

                XBEditorProvider editorProvider = xbupEditorModule.getEditorProvider();

                xbupEditorModule.setDevMode(devMode);
                xbupEditorModule.registerFileTypes();
                xbupEditorModule.registerCatalogBrowserMenu();
                xbupEditorModule.registerDocEditingMenuActions();
                xbupEditorModule.registerDocEditingToolBarActions();
                xbupEditorModule.registerViewModeMenu();
                xbupEditorModule.registerSampleFilesSubMenuActions();
                xbupEditorModule.registerPropertiesMenuAction();
                xbupEditorModule.registerPropertyPanelMenuAction();

                textEditorModule.registerToolsOptionsMenuActions();
                textEditorModule.registerOptionsPanels();
                xbupEditorModule.registerOptionsPanels();

                ApplicationFrameHandler frameHandler = frameModule.getFrameHandler();
                editorModule.registerEditor("xbup", editorProvider);

                xbupEditorModule.registerStatusBar();

                frameHandler.setMainPanel(editorModule.getEditorPanel());
                frameHandler.setDefaultSize(new Dimension(600, 400));
                frameHandler.show();

                clientModule.addClientConnectionListener(xbupEditorModule.getClientConnectionListener());
                Thread connectionThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!clientModule.connectToService()) {
                            if (!clientModule.connectToFallbackService()) {
                                clientModule.useBuildInService();
                            }
                        }

                        XBACatalog catalog = clientModule.getCatalog();
                        XBPluginRepository pluginRepository = clientModule.getPluginRepository();
                        if (catalog != null) {
                            xbupEditorModule.setCatalog(catalog);
                            xbupEditorModule.setPluginRepository(pluginRepository);
                        }
                    }
                });

                connectionThread.start();

                List fileArgs = cl.getArgList();
                if (fileArgs.size() > 0) {
                    fileModule.loadFromFile((String) fileArgs.get(0));
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(XBEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
