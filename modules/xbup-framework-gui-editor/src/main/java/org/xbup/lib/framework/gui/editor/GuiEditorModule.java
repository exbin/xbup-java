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
package org.xbup.lib.framework.gui.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.editor.api.GuiEditorApiModule;
import org.xbup.lib.framework.gui.editor.api.XBEditorProvider;

/**
 * XBUP framework editor api module.
 *
 * @version 0.2.0 2015/12/20
 * @author XBUP Project (http://xbup.org)
 */
public class GuiEditorModule implements GuiEditorApiModule {

    private XBApplication application;
    private final List<XBEditorProvider> editors = new ArrayList<>();
    private final Map<String, List<XBEditorProvider>> pluginEditorsMap = new HashMap<>();

    public GuiEditorModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;
    }

    @Override
    public void unregisterPlugin(String pluginId) {
        List<XBEditorProvider> pluginEditors = pluginEditorsMap.get(pluginId);
        if (pluginEditors != null) {
            for (XBEditorProvider editor : pluginEditors) {
                editors.remove(editor);
            }
            pluginEditorsMap.remove(pluginId);
        }
    }

    @Override
    public void registerEditor(String pluginId, XBEditorProvider editorProvider) {
        editors.add(editorProvider);
        List<XBEditorProvider> pluginEditors = pluginEditorsMap.get(pluginId);
        if (pluginEditors == null) {
            pluginEditors = new ArrayList<>();
            pluginEditorsMap.put(pluginId, pluginEditors);
        }

        pluginEditors.add(editorProvider);
    }
}
