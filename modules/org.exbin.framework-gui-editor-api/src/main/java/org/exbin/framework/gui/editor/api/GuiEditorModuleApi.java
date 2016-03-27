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
package org.exbin.framework.gui.editor.api;

import java.awt.Component;
import org.exbin.framework.api.XBModuleRepositoryUtils;
import org.exbin.framework.api.XBApplicationModule;

/**
 * XBUP framework editor module api interface.
 *
 * @version 0.2.0 2016/01/23
 * @author ExBin Project (http://exbin.org)
 */
public interface GuiEditorModuleApi extends XBApplicationModule {

    public static String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(GuiEditorModuleApi.class);

    /**
     * Registers new editor.
     *
     * @param pluginId plugin identifier
     * @param editorProvider editor provider
     */
    public void registerEditor(String pluginId, XBEditorProvider editorProvider);

    /**
     * Returns main component for editors handling.
     *
     * @return panel component
     */
    public Component getEditorPanel();
    
    /**
     * Registers undo handler for undo management to editor.
     */
    public void registerUndoHandler();
}
