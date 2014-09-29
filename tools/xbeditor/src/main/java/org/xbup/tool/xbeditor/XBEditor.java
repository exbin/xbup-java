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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import net.xeoh.plugins.base.util.uri.ClassURI;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.xbup.lib.core.parser.basic.XBHead;
import org.xbup.tool.editor.module.java_help.JavaHelpModule;
import org.xbup.tool.editor.module.online_help.OnlineHelpModule;
import org.xbup.tool.editor.module.xbdoc_editor.XBDocEditorModule;
import org.xbup.tool.editor.base.XBEditorApplication;
import org.xbup.tool.editor.base.XBEditorApplication.XBAppCommand;
import org.xbup.tool.editor.base.XBEditorBase;
import org.xbup.tool.editor.base.api.ApplicationModule;

/**
 * The main class of the XBEditor application.
 *
 * @version 0.1.22 2013/03/10
 * @author XBUP Project (http://xbup.org)
 */
public class XBEditor extends XBEditorBase {

    private static Preferences preferences;
    private static boolean verboseMode = false;
    private static boolean devMode = false;

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        ResourceBundle bundle = ResourceBundle.getBundle("org/xbup/tool/xbeditor/resources/XBEditor");
        try {
            preferences = Preferences.userNodeForPackage(XBEditor.class);
        } catch (SecurityException ex) {
            preferences = null;
        }
        try {
            // Parameters processing
            Options opt = new Options();
            opt.addOption("h", "help", false, bundle.getString("cl_option_help"));
            // opt.addOption("port", true, resourceMap.getString("cl_option_port"));
            // opt.addOption("ip", true, resourceMap.getString("cl_option_ip"));
            opt.addOption("v", false, bundle.getString("cl_option_verbose"));
            // TODO: More options
//            opt.addOption("nopref", false, resourceMap.getString("cl_option_nopref"));
//            opt.addOption("stop", false, resourceMap.getString("cl_option_stop"));
//            opt.addOption("log", true, resourceMap.getString("cl_option_log"));
//            opt.addOption("db", true, resourceMap.getString("cl_option_db"));
//            opt.addOption("derby", false, resourceMap.getString("cl_option_derby"));
//            opt.addOption("noderby", false, resourceMap.getString("cl_option_noderby"));
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

                XBEditorApplication  app = new XBEditorApplication();
                app.setAppMode(true);
                app.setAppPreferences(preferences);
                app.setAppBundle(bundle);
                app.setFirstCommand(new XBEditorFirstCommand(app));

                app.addPlugin(new ClassURI(XBDocEditorModule.class).toURI());
                app.addPlugin(new ClassURI(JavaHelpModule.class).toURI());
                app.addPlugin(new ClassURI(OnlineHelpModule.class).toURI());

                String catalogConnection = cl.getOptionValue("ip");
                String port = cl.getOptionValue("port");
                if ((!"".equals(port))&&(port != null)) {
                    catalogConnection += ":" + port;
                }

                List fileArgs = cl.getArgList();
                if (fileArgs.size()>0) {
                    app.loadFromFile((String) fileArgs.get(0));
                }

                app.prepare();

                ApplicationModule module = app.getModuleRepository().getPluginHandler(XBDocEditorModule.class);
                ((XBDocEditorModule) module).setEditorApp(app);
                ((XBDocEditorModule) module).setDevMode(devMode);

                app.run();
            }
        } catch (ParseException ex) {
            Logger.getLogger(XBEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static class XBEditorFirstCommand implements XBAppCommand {

        private final XBEditorApplication app;

        public XBEditorFirstCommand(XBEditorApplication app) {
            this.app = app;
        }

        @Override
        public void execute() {
            ApplicationModule module = app.getModuleRepository().getPluginHandler(OnlineHelpModule.class);
            try {
                if (module instanceof OnlineHelpModule) {
                    ((OnlineHelpModule) module).setHelpUrl(new URL("http://www.xbup.org/?devel/editor"));
                }
            } catch (MalformedURLException ex) {
                Logger.getLogger(XBEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
