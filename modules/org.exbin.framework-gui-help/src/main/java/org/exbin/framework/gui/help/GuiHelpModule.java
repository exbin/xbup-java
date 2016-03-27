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
package org.exbin.framework.gui.help;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.gui.frame.api.GuiFrameModuleApi;
import org.exbin.framework.gui.help.api.GuiHelpModuleApi;
import org.exbin.framework.gui.menu.api.GuiMenuModuleApi;
import org.exbin.framework.gui.menu.api.MenuPosition;
import org.exbin.framework.gui.menu.api.PositionMode;
import org.exbin.framework.gui.utils.ActionUtils;
import org.exbin.framework.gui.utils.BareBonesBrowserLaunch;
import org.exbin.xbup.plugin.XBModuleHandler;

/**
 * Implementation of XBUP framework help module.
 *
 * @version 0.2.0 2016/02/07
 * @author ExBin Project (http://exbin.org)
 */
public class GuiHelpModule implements GuiHelpModuleApi {

    private XBApplication application;
    private final java.util.ResourceBundle bundle = ActionUtils.getResourceBundleByClass(GuiHelpModule.class);
    private HelpSet helpSet;
    private HelpBroker helpBroker;
    private ActionListener helpActionLisneter;
    private URL helpUrl;

    private Action helpAction;
    private Action onlineHelpAction;

    public GuiHelpModule() {
    }

    @Override
    public void init(XBModuleHandler moduleHandler) {
        this.application = (XBApplication) moduleHandler;

        String path = ".";
        try {
            path = (new File(".")).getCanonicalPath();
        } catch (IOException ex) {
            Logger.getLogger(GuiHelpModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        helpSet = getHelpSet(path + "/help/help.hs");
        if (helpSet != null) {
            // Temporary for Java webstart, include help in jar later
            helpBroker = helpSet.createHelpBroker();
            // CSH.setHelpIDString(helpContextMenuItem, "top");
            helpActionLisneter = new CSH.DisplayHelpFromSource(helpBroker);
            // helpContextMenuItem.addActionListener(helpActionLisneter);
        }
    }

    @Override
    public void unregisterModule(String moduleId) {
    }

    @Override
    public Action getHelpAction() {
        if (helpAction == null) {
            helpAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    helpActionLisneter.actionPerformed(e);
                }
            };
            ActionUtils.setupAction(helpAction, bundle, "helpAction");
            helpAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);
            helpAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        }

        return helpAction;
    }

    @Override
    public Action getOnlineHelpAction() {
        if (onlineHelpAction == null) {
            onlineHelpAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    BareBonesBrowserLaunch.openURL(helpUrl.toExternalForm());
                }
            };
            ActionUtils.setupAction(onlineHelpAction, bundle, "onlineHelpAction");
            onlineHelpAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);
        }

        return onlineHelpAction;
    }

    @Override
    public void registerMainMenu() {
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.HELP_MENU_ID, MODULE_ID, getHelpAction(), new MenuPosition(PositionMode.TOP));
    }

    @Override
    public void registerOnlineHelpMenu() {
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.HELP_MENU_ID, MODULE_ID, getOnlineHelpAction(), new MenuPosition(PositionMode.TOP));
    }

    /**
     * Finds the helpset file and create a HelpSet object.
     */
    private HelpSet getHelpSet(String helpsetfile) {
        HelpSet hs = null;
        ClassLoader cl = getClass().getClassLoader();
        try {
            URL hsURL = HelpSet.findHelpSet(cl, helpsetfile);
            File file = new File("./help/help.hs");
            if (!file.exists()) {
                file = new File("./../help/help.hs");
            }
            if (hsURL == null) {
                hsURL = (file.toURI()).toURL();
            }
            hs = new HelpSet(null, hsURL);
        } catch (MalformedURLException | HelpSetException ex) {
            System.out.println("HelpSet: " + ex.getMessage());
            System.out.println("HelpSet: " + helpsetfile + " not found");
        }
        return hs;
    }

    @Override
    public void setHelpUrl(URL helpUrl) {
        this.helpUrl = helpUrl;
    }
}
