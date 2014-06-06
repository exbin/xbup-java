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
package org.xbup.tool.xbeditorbase.base.manager;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import org.xbup.tool.xbeditorbase.base.api.ApplicationPanel;

/**
 * Manager for panels.
 *
 * @version 0.1 wr21.0 2011/06/15
 * @author XBUP Project (http://xbup.org)
 */
class PanelManager {

    private BaseModuleRepository moduleRepository;
    private Map<Long,ApplicationPanel> panels;

    public PanelManager(BaseModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
        panels = new HashMap<Long,ApplicationPanel>();
    }

    void registerPanel(ApplicationPanel panel) {
        panels.put(moduleRepository.getActiveModule(), panel);
        moduleRepository.getMainFrame().setActivePanel((JPanel) panel, panel.getPanelName());
    }

}
