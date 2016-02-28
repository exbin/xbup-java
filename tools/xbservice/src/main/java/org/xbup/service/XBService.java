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
package org.xbup.service;

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
import org.xbup.lib.service.XBCatalogNetServiceServer;

/**
 * Console class for XBUP framework service.
 *
 * @version 0.1.25 2015/02/26
 * @author ExBin Project (http://exbin.org)
 */
public class XBService {

    public XBService() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            final ResourceBundle recourceBundle = ResourceBundle.getBundle("org/xbup/service/XBService");
            final Preferences preferences = Preferences.userNodeForPackage(XBService.class);

            // Parameters processing
            Options opt = new Options();
            Logger logger = Logger.getLogger("");
            logger.setLevel(Level.ALL);

            opt.addOption("h", "help", false, recourceBundle.getString("cl_option_help"));
            opt.addOption("port", true, recourceBundle.getString("cl_option_port"));
            opt.addOption("ip", true, recourceBundle.getString("cl_option_ip"));
            opt.addOption("v", false, recourceBundle.getString("cl_option_verbose"));
            opt.addOption("nopref", false, recourceBundle.getString("cl_option_nopref"));
            opt.addOption("stop", false, recourceBundle.getString("cl_option_stop"));
            opt.addOption("log", true, recourceBundle.getString("cl_option_log"));
            opt.addOption("db", true, recourceBundle.getString("cl_option_db"));
            opt.addOption("rootcat", false, recourceBundle.getString("cl_option_rootcat"));
            opt.addOption("dev", false, recourceBundle.getString("cl_option_dev"));
            opt.addOption("update", false, recourceBundle.getString("cl_option_update"));
            //opt.addOption("derby", false, myBundle.getString("cl_option_derby"));
            //opt.addOption("noderby", false, myBundle.getString("cl_option_noderby"));

            BasicParser parser = new BasicParser();
            CommandLine cl = parser.parse(opt, args);

            if (cl.hasOption('h')) {
                logger.addHandler(new XBHead.XBLogHandler(false));
                logger.addHandler(new XBCatalogNetServiceServer.XBServiceLogHandler(true));
                Logger.getLogger(XBService.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, recourceBundle.getString("service_head"));
                Logger.getLogger(XBService.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "");
                HelpFormatter f = new HelpFormatter();
                f.printHelp(recourceBundle.getString("cl_syntax"), opt);
            } else {
                // Preferences processing
                boolean verboseMode = cl.hasOption("v") || Boolean.getBoolean(preferences.get("verbose", Boolean.toString(false)));
                boolean devMode = cl.hasOption("dev");
                boolean forceUpdate = cl.hasOption("update");
                if (devMode) {
                    Logger.getLogger(XBService.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "Set development mode.");
                }
                boolean rootCatalogMode = cl.hasOption("rootcat");
                if (rootCatalogMode) {
                    Logger.getLogger(XBService.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "Set root catalog mode.");
                }
//                if (cl.hasOption("nopref"))
                // 22594 is 0x5842 (XB)
                String tcpipPort = cl.getOptionValue("port", preferences.get("tcpip_port", ((devMode) ? "22595" : "22594")));
                String tcpipInterface = cl.getOptionValue("ip", preferences.get("tcpip_interface", "localhost"));
                logger.addHandler(new XBHead.XBLogHandler(verboseMode));
                logger.addHandler(new XBCatalogNetServiceServer.XBServiceLogHandler(true));

                XBServiceInstance server = new XBServiceInstance();
                server.setResourceBundle(recourceBundle);
                server.setDerbyPath(preferences.absolutePath());
                server.setTcpipInterface(tcpipInterface);
                server.setTcpipPort(tcpipPort);

                server.setDevMode(devMode);
                server.setVerboseMode(verboseMode);
                server.setForceUpdate(forceUpdate);
                server.setRootCatalogMode(rootCatalogMode);

                server.run();
            }
        } catch (ParseException ex) {
            Logger.getLogger(XBService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
