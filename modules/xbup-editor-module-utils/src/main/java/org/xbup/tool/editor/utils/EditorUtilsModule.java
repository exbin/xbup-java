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
package org.xbup.tool.editor.utils;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.tool.editor.base.api.ApplicationModule;
import org.xbup.tool.editor.base.api.ApplicationModuleInfo;
import org.xbup.tool.editor.base.api.ModuleManagement;

/**
 * Editor utilities module.
 *
 * @version 0.1.25 2015/04/09
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class EditorUtilsModule implements ApplicationModule {

    public EditorUtilsModule() {
    }

    @Override
    public ApplicationModuleInfo getInfo() {
        return new ApplicationModuleInfo() {

            @Override
            public String getPluginId() {
                return "editorutils";
            }

            @Override
            public String getPluginName() {
                return "EditorUtils";
            }

            @Override
            public String getPluginDescription() {
                return "Basic editor utilities";
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
    }
}
