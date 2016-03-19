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
package org.exbin.xbup.tool.xbshell;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * XBUP Protocol Shell Browser Tool
 *
 * @version 0.2.0 2016/03/12
 * @author ExBin Project (http://exbin.org)
 */
public class Prompt {

    private File file;
    private String path;

    /**
     * Run prompt on given file.
     *
     * @param file
     */
    public void run(File file) {
        this.file = file;
        path = "";

        System.out.println("XBShell interface (version 0.2.0 DEV)");
        String command = "";
        byte[] input = new byte[30];
        do {
            try {
                System.out.print("xbup:" + path + "$ ");
                System.in.read(input);
                command = (new String(input)).trim();
                if (command.equals("help")) {
                    System.out.println("XBShell 0.2.0 DEV (C) ExBin Project http://exbin.org");
                    System.out.println("Usage: xbshell [options] [path]filename");
                    System.out.println("Commands: help exit ls cp mv");
                } else if (command.equals("help")) {

                }
            } catch (IOException ex) {
                Logger.getLogger(Prompt.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (!command.equals("exit"));
    }
}
