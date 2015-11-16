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
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.xbup.lib.core.parser.basic.XBHead;
import org.xbup.lib.framework.gui.XBBaseApplication;

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
                app.setAppMode(true);
                app.setAppPreferences(preferences);
                app.setAppBundle(bundle, APP_BUNDLE_NAME);
//                app.setFirstCommand(new XBEditorFirstCommand(app));

//                app.addPlugin(new ClassURI(XBDocEditorModule.class).toURI());
//                app.addPlugin(new ClassURI(JavaHelpModule.class).toURI());
//                app.addPlugin(new ClassURI(OnlineHelpModule.class).toURI());
                List fileArgs = cl.getArgList();
                if (fileArgs.size() > 0) {
                    app.loadFromFile((String) fileArgs.get(0));
                }

                app.init();

//                ApplicationModule module = app.getModuleRepository().getPluginHandler(XBDocEditorModule.class);
//                ((XBDocEditorModule) module).setEditorApp(app);
//                ((XBDocEditorModule) module).setDevMode(devMode);
                app.run();
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
