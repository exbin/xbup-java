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
package org.exbin.framework.gui.component;

import javax.swing.JPanel;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.gui.component.api.GuiComponentModuleApi;

/**
 * Implementation of XBUP framework component module.
 *
 * @version 0.2.0 2016/03/20
 * @author ExBin Project (http://exbin.org)
 */
@PluginImplementation
public class GuiComponentModule implements GuiComponentModuleApi {

    private XBApplication application;

    public GuiComponentModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;
    }

    @Override
    public void unregisterPlugin(String pluginId) {
    }
    
    @Override
    public JPanel getTableEditPanel() {
        // return new TableEditPanel();
        return null;
    }
}
