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
package org.xbup.tool.xbshell;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * XBUP Protocol Shell Browser Tool
 *
 * @version 0.1.18 2009/08/02
 * @author ExBin Project (http://exbin.org)
 */
public class XBShell {

    /** Creates a new instance of Main */
    public XBShell() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Options opt = new Options();

            opt.addOption("h", false, "Print help for this application");
            opt.addOption("u", true, "The username to use");
            opt.addOption("dsn", true, "The data source to use");

            BasicParser parser = new BasicParser();
            CommandLine cl = parser.parse(opt, args);

            if ( cl.hasOption('h') ) {
                HelpFormatter f = new HelpFormatter();
                f.printHelp("xbsh", opt);
            } else {
//                System.out.println(cl.getOptionValue("u"));
//                System.out.println(cl.getOptionValue("dsn"));
                Prompt prompt = new Prompt();
                prompt.run(null);
            }
        }
        catch (ParseException ex) {
            Logger.getLogger(XBShell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
