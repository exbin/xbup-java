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
package org.xbup.tool.xbseditor;

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
import org.xbup.tool.editor.module.wave_editor.XBWaveEditorModule;
import org.xbup.tool.editor.base.XBEditorApplication;
import org.xbup.tool.editor.base.XBEditorBase;

/**
 * The main class of the XBSEditor application.
 *
 * @version 0.1.24 2015/01/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBSEditor extends XBEditorBase {

    private static Preferences preferences;
    private static boolean verboseMode = false;
    private static final String APP_BUNDLE_NAME = "org/xbup/tool/xbseditor/resources/XBSEditor";
    private static final ResourceBundle bundle = ResourceBundle.getBundle(APP_BUNDLE_NAME);

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        try {
            preferences = Preferences.userNodeForPackage(XBSEditor.class);
        } catch (SecurityException ex) {
            preferences = null;
        }
        try {
            // Parameters processing
            Options opt = new Options();
            opt.addOption("h", "help", false, bundle.getString("cl_option_help"));
            opt.addOption("port", true, bundle.getString("cl_option_port"));
            opt.addOption("ip", true, bundle.getString("cl_option_ip"));
            opt.addOption("v", false, bundle.getString("cl_option_verbose"));
            BasicParser parser = new BasicParser();
            CommandLine cl = parser.parse(opt, args);
            if (cl.hasOption('h')) {
                HelpFormatter f = new HelpFormatter();
                f.printHelp(bundle.getString("cl_syntax"), opt);
            } else {
                verboseMode = cl.hasOption("v");
                Logger logger = Logger.getLogger("");
                try {
                    logger.setLevel(Level.ALL);
                    logger.addHandler(new XBHead.XBLogHandler(verboseMode));
                } catch (java.security.AccessControlException ex) {
                    // Ignore it in java webstart
                }

                XBEditorApplication app = new XBEditorApplication();
                app.setAppMode(true);
                app.setAppPreferences(preferences);
                app.setAppBundle(bundle, APP_BUNDLE_NAME);

                app.addPlugin(new ClassURI(XBWaveEditorModule.class).toURI());

                String catalogConnection = cl.getOptionValue("ip");
                String port = cl.getOptionValue("port");
                if ((!"".equals(port)) && (port != null)) {
                    catalogConnection += ":" + port;
                }

                app.startup();

                List fileArgs = cl.getArgList();
                if (fileArgs.size() > 0) {
                    app.loadFromFile((String) fileArgs.get(0));
                }

            }
        } catch (ParseException ex) {
            Logger.getLogger(XBSEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
