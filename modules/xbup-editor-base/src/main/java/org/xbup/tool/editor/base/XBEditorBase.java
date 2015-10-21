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
package org.xbup.tool.editor.base;

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

/**
 * Main class for XBEditors.
 *
 * @version 0.2.0 2015/10/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBEditorBase {

    private static Preferences preferences;
    private static final String APPLICATION_BUNDLE_NAME = "org/xbup/tool/editor/base/resources/XBEditorBase";
    private static final ResourceBundle APPLICATION_BUNDLE = ResourceBundle.getBundle(APPLICATION_BUNDLE_NAME);

    public XBEditorBase() {
    }

    /**
     * Main method launching the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            preferences = Preferences.userNodeForPackage(XBEditorBase.class);
        } catch (SecurityException ex) {
            preferences = null;
        }
        try {
            // Parameters processing
            Options opt = new Options();
            opt.addOption("h", "help", false, APPLICATION_BUNDLE.getString("cl_option_help"));
            opt.addOption("v", false, APPLICATION_BUNDLE.getString("cl_option_verbose"));
            BasicParser parser = new BasicParser();
            CommandLine cl = parser.parse(opt, args);
            if (cl.hasOption('h')) {
                HelpFormatter f = new HelpFormatter();
                f.printHelp(APPLICATION_BUNDLE.getString("cl_syntax"), opt);
                return;
            }

            boolean verboseMode = cl.hasOption("v");
            Logger logger = Logger.getLogger("");
            try {
                logger.setLevel(Level.ALL);
//                    logger.addHandler(new XBHead.XBLogHandler(verboseMode));
            } catch (java.security.AccessControlException ex) {
                // Ignore it in java webstart
            }

            List fileArgs = cl.getArgList();
            if (fileArgs.size() > 0) {
                // TODO fileName = (String) fileArgs.get(0);
            }

            XBEditorApplication app = new XBEditorApplication();
            app.setAppMode(true);
            app.setAppPreferences(preferences);
            app.setAppBundle(APPLICATION_BUNDLE, APPLICATION_BUNDLE_NAME);

            app.startup();
        } catch (ParseException ex) {
            Logger.getLogger(XBEditorBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
