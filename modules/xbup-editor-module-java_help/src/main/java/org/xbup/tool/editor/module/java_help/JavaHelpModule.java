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
package org.xbup.tool.editor.module.java_help;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JMenuItem;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.tool.editor.base.api.ApplicationModule;
import org.xbup.tool.editor.base.api.ApplicationModuleInfo;
import org.xbup.tool.editor.base.api.BasicMenuType;
import org.xbup.tool.editor.base.api.MenuPositionMode;
import org.xbup.tool.editor.base.api.ModuleManagement;

/**
 * JavaHelp module.
 *
 * @version 0.1.22 2013/03/10
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class JavaHelpModule implements ApplicationModule {

    private HelpSet helpSet;
    private HelpBroker helpBroker;
    private ActionListener helpActionLisneter;
    private final String DIALOG_MENU_SUFIX = "...";
    private JMenuItem helpContextMenuItem;
    private ResourceBundle resourceBundle;

    /** Constructor */
    public JavaHelpModule() {
        resourceBundle = ResourceBundle.getBundle("org/xbup/tool/editor/module/java_help/resources/JavaHelpModule");
    }

    @Override
    public ApplicationModuleInfo getInfo() {
        return new ApplicationModuleInfo() {

            @Override
            public String getPluginId() {
                return "javahelp";
            }

            @Override
            public String getPluginName() {
                return "JavaHelp";
            }

            @Override
            public String getPluginDescription() {
                return "Context Help using JavaHelp2.0";
            }

            @Override
            public String[] pluginDependency() {
                return null;
            }

            @Override
            public String[] pluginOptional() {
                return null;
            }
        };
    }

    @Override
    public void init(ModuleManagement management) {
        helpContextMenuItem = new JMenuItem(resourceBundle.getString("JavaHelpModule.helpContextMenuItem.text")+DIALOG_MENU_SUFIX);
        helpContextMenuItem.setToolTipText(resourceBundle.getString("JavaHelpModule.helpContextMenuItem.toolTipText"));
        helpContextMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));;
        String path = ".";
        try {
            path = (new File(".")).getCanonicalPath();
        } catch (IOException ex) {
            Logger.getLogger(JavaHelpModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        helpSet = getHelpSet(path + "/help/Sample.hs");
        if (helpSet != null) { // Temporary for Java webstart, include help in jar later
            helpBroker = helpSet.createHelpBroker();
            CSH.setHelpIDString(helpContextMenuItem, "top");
            helpActionLisneter = new CSH.DisplayHelpFromSource(helpBroker);
            helpContextMenuItem.addActionListener(helpActionLisneter);
        } else {
            helpContextMenuItem.setEnabled(false);
        }
        management.getMenuManagement().addMenuItem(helpContextMenuItem, BasicMenuType.HELP, MenuPositionMode.BEFORE_PANEL);
    }

    /**
     * find the helpset file and create a HelpSet object
     */
    private HelpSet getHelpSet(String helpsetfile) {
        HelpSet hs = null;
        ClassLoader cl = getClass().getClassLoader();
        try {
            URL hsURL = HelpSet.findHelpSet(cl, helpsetfile);
            File file = new File("./help/Sample.hs");
            if (!file.exists()) {
                file = new File("./../help/Sample.hs");
            }
            if (hsURL == null) {
                hsURL = (file.toURI()).toURL();
            }
            hs = new HelpSet(null, hsURL);
        } catch (Exception ee) {
            System.out.println("HelpSet: " + ee.getMessage());
            System.out.println("HelpSet: " + helpsetfile + " not found");
        }
        return hs;
    }
}
