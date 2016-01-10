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
package org.xbup.tool.xbeditor;

import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.Action;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.xbup.lib.core.parser.basic.XBHead;
import org.xbup.lib.framework.editor.xbup.EditorXbupModule;
import org.xbup.lib.framework.gui.XBBaseApplication;
import org.xbup.lib.framework.gui.about.api.GuiAboutModuleApi;
import org.xbup.lib.framework.gui.api.XBModuleRepository;
import org.xbup.lib.framework.gui.editor.api.GuiEditorModuleApi;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.frame.api.XBApplicationFrameHandler;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;
import org.xbup.lib.framework.gui.menu.api.MenuGroup;
import org.xbup.lib.framework.gui.menu.api.MenuPosition;
import org.xbup.lib.framework.gui.menu.api.MenuPositionMode;
import org.xbup.lib.framework.gui.undo.api.GuiUndoModuleApi;
import org.xbup.lib.framework.gui.menu.api.ClipboardActionsApi;
import org.xbup.lib.framework.gui.file.api.FileHandlingActionsApi;
import org.xbup.lib.framework.gui.file.api.GuiFileModuleApi;
import org.xbup.lib.framework.gui.menu.api.MenuSeparationMode;
import org.xbup.lib.framework.gui.options.api.GuiOptionsModuleApi;

/**
 * The main class of the XBEditor application.
 *
 * @version 0.2.0 2015/11/15
 * @author XBUP Project (http://xbup.org)
 */
public class XBEditor {

    private static Preferences preferences;
    private static boolean verboseMode = false;
    private static boolean devMode = false;
    private static final String APP_BUNDLE_NAME = "org/xbup/tool/xbeditor/resources/XBEditor";
    private static final ResourceBundle bundle = ResourceBundle.getBundle(APP_BUNDLE_NAME);

    /**
     * Main method launching the application.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
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
                app.setAppBundle(bundle, APP_BUNDLE_NAME);
                app.init();
//                app.setFirstCommand(new XBEditorFirstCommand(app));

//                app.loadPlugin(new ClassURI(GuiFrameModule.class).toURI());
//                app.loadPlugin(new ClassURI(GuiFrameModule.class).toURI());
//                app.loadPlugin(new ClassURI(JavaHelpModule.class).toURI());
//                app.loadPlugin(new ClassURI(OnlineHelpModule.class).toURI());
                XBModuleRepository moduleRepository = app.getModuleRepository();
                moduleRepository.addClassPathPlugins();
                moduleRepository.initModules();

                GuiFrameModuleApi frameModule = moduleRepository.getModuleByInterface(GuiFrameModuleApi.class);
                GuiEditorModuleApi editorModule = moduleRepository.getModuleByInterface(GuiEditorModuleApi.class);
                GuiMenuModuleApi menuModule = moduleRepository.getModuleByInterface(GuiMenuModuleApi.class);
                GuiAboutModuleApi aboutModule = moduleRepository.getModuleByInterface(GuiAboutModuleApi.class);
                GuiUndoModuleApi undoModule = moduleRepository.getModuleByInterface(GuiUndoModuleApi.class);
                GuiFileModuleApi fileModule = moduleRepository.getModuleByInterface(GuiFileModuleApi.class);
                GuiOptionsModuleApi optionsModule = moduleRepository.getModuleByInterface(GuiOptionsModuleApi.class);

                // Test menu registration
                Action aboutAction = aboutModule.getAboutAction();
                menuModule.registerMenuItem(GuiFrameModuleApi.HELP_MENU_ID, GuiAboutModuleApi.MODULE_ID, aboutAction, new MenuPosition(MenuPositionMode.BOTTOM_LAST));

                String appClosingActionsGroup = "ApplicationClosingActionsGroup";
                menuModule.registerMenuGroup(GuiFrameModuleApi.FILE_MENU_ID, new MenuGroup(appClosingActionsGroup, new MenuPosition(MenuPositionMode.BOTTOM_LAST), MenuSeparationMode.ABOVE));
                menuModule.registerMenuItem(GuiFrameModuleApi.FILE_MENU_ID, GuiAboutModuleApi.MODULE_ID, frameModule.getExitAction(), new MenuPosition(appClosingActionsGroup));

                // Register clipboard editing actions
                String fileHandlingActionsGroup = "FileHandlingActionsGroup";
                FileHandlingActionsApi fileActions = fileModule.getFileHandlingActions();
                menuModule.registerMenuGroup(GuiFrameModuleApi.FILE_MENU_ID, new MenuGroup(fileHandlingActionsGroup, new MenuPosition(MenuPositionMode.TOP)));
                menuModule.registerMenuItem(GuiFrameModuleApi.FILE_MENU_ID, GuiAboutModuleApi.MODULE_ID, fileActions.getNewFileAction(), new MenuPosition(fileHandlingActionsGroup));
                menuModule.registerMenuItem(GuiFrameModuleApi.FILE_MENU_ID, GuiAboutModuleApi.MODULE_ID, fileActions.getOpenFileAction(), new MenuPosition(fileHandlingActionsGroup));
                menuModule.registerMenuItem(GuiFrameModuleApi.FILE_MENU_ID, GuiAboutModuleApi.MODULE_ID, fileActions.getSaveFileAction(), new MenuPosition(fileHandlingActionsGroup));
                menuModule.registerMenuItem(GuiFrameModuleApi.FILE_MENU_ID, GuiAboutModuleApi.MODULE_ID, fileActions.getSaveAsFileAction(), new MenuPosition(fileHandlingActionsGroup));

                undoModule.registerMainMenu();

                // Register clipboard editing actions
                String clipboardActionsGroup = "ClipboardActionsGroup";
                ClipboardActionsApi clipboardActions = menuModule.getClipboardActions();
                menuModule.registerMenuGroup(GuiFrameModuleApi.EDIT_MENU_ID, new MenuGroup(clipboardActionsGroup, new MenuPosition(MenuPositionMode.TOP)));
                menuModule.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, GuiAboutModuleApi.MODULE_ID, clipboardActions.getCutAction(), new MenuPosition(clipboardActionsGroup));
                menuModule.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, GuiAboutModuleApi.MODULE_ID, clipboardActions.getCopyAction(), new MenuPosition(clipboardActionsGroup));
                menuModule.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, GuiAboutModuleApi.MODULE_ID, clipboardActions.getPasteAction(), new MenuPosition(clipboardActionsGroup));
                menuModule.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, GuiAboutModuleApi.MODULE_ID, clipboardActions.getDeleteAction(), new MenuPosition(clipboardActionsGroup));
                menuModule.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, GuiAboutModuleApi.MODULE_ID, clipboardActions.getSelectAllAction(), new MenuPosition(clipboardActionsGroup));

                Action optionsAction = optionsModule.getOptionsAction();
                menuModule.registerMenuItem(GuiFrameModuleApi.TOOLS_MENU_ID, GuiAboutModuleApi.MODULE_ID, optionsAction, new MenuPosition(MenuPositionMode.BOTTOM_LAST));

                XBApplicationFrameHandler frameHandler = frameModule.getFrameHandler();

                EditorXbupModule xbupEditorModule = new EditorXbupModule();
                xbupEditorModule.init(app);
                editorModule.registerEditor("xbup", xbupEditorModule.getEditorProvider());

                frameHandler.setMainPanel(editorModule.getEditorPanel());
                frameHandler.show();

                List fileArgs = cl.getArgList();
                if (fileArgs.size() > 0) {
                    // TODO app.loadFromFile((String) fileArgs.get(0));
                }

                // editorModule.run();
//                ApplicationModule module = app.getModuleRepository().getPluginHandler(XBDocEditorModule.class);
//                ((XBDocEditorModule) module).setEditorApp(app);
//                ((XBDocEditorModule) module).setDevMode(devMode);
            }
        } catch (ParseException ex) {
            Logger.getLogger(XBEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    private static class XBEditorFirstCommand implements XBAppCommand {
//
//        private final XBEditorApplication app;
//
//        public XBEditorFirstCommand(XBEditorApplication app) {
//            this.app = app;
//        }
//
//        @Override
//        public void execute() {
//            ApplicationModule module = app.getModuleRepository().getPluginHandler(XBDocEditorModule.class);
//            ((XBDocEditorModule) module).postWindowOpened();
//
//            module = app.getModuleRepository().getPluginHandler(OnlineHelpModule.class);
//            try {
//                if (module instanceof OnlineHelpModule) {
//                    ((OnlineHelpModule) module).setHelpUrl(new URL(bundle.getString("online_help_url")));
//                }
//            } catch (MalformedURLException ex) {
//                Logger.getLogger(XBEditor.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
}
