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
package org.xbup.tool.xbteditor;

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
import org.xbup.tool.editor.module.text_editor.XBTextEditorModule;
import org.xbup.tool.editor.base.XBEditorApplication;
import org.xbup.tool.editor.base.XBEditorBase;

/**
 * The main class of the XBTEditor application.
 *
 * @version 0.1.24 2015/01/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBTEditor extends XBEditorBase {

    private static Preferences preferences;
    private static boolean verboseMode = false;
    private static final String APP_BUNDLE_NAME = "org/xbup/tool/xbteditor/resources/XBTEditor";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(APP_BUNDLE_NAME);

    public XBTEditor() {
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        try {
            preferences = Preferences.userNodeForPackage(XBTEditor.class);
        } catch (SecurityException ex) {
            preferences = null;
        }
        try {
            // Parameters processing
            Options opt = new Options();
            opt.addOption("h", "help", false, RESOURCE_BUNDLE.getString("cl_option_help"));
            opt.addOption("port", true, RESOURCE_BUNDLE.getString("cl_option_port"));
            opt.addOption("ip", true, RESOURCE_BUNDLE.getString("cl_option_ip"));
            opt.addOption("v", false, RESOURCE_BUNDLE.getString("cl_option_verbose"));
            BasicParser parser = new BasicParser();
            CommandLine cl = parser.parse(opt, args);
            if (cl.hasOption('h')) {
                HelpFormatter f = new HelpFormatter();
                f.printHelp(RESOURCE_BUNDLE.getString("cl_syntax"), opt);
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
                app.setAppBundle(RESOURCE_BUNDLE, APP_BUNDLE_NAME);

                app.addPlugin(new ClassURI(XBTextEditorModule.class).toURI());

                List fileArgs = cl.getArgList();
                if (fileArgs.size() > 0) {
                    app.loadFromFile((String) fileArgs.get(0));
                }

                app.startup();
            }
        } catch (ParseException ex) {
            Logger.getLogger(XBTEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
