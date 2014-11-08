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
package org.xbup.tool.editor.base.manager;

import java.util.prefs.Preferences;
import javax.swing.JPanel;
import org.xbup.tool.editor.base.api.OptionsManagement;
import org.xbup.tool.editor.base.api.OptionsPanel;

/**
 * Manager for options dialog.
 *
 * @version 0.1.22 2013/03/24
 * @author XBUP Project (http://xbup.org)
 */
public class OptionsManager implements OptionsManagement {

    private final BaseModuleRepository moduleRepository;

    OptionsManager(BaseModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public void addStatusPanel(JPanel panel) {
        moduleRepository.getMainFrame().addStatusPanel(panel, String.valueOf(moduleRepository.getActiveModule()));
    }

    public void setStatusPanel(long moduleId) {
        moduleRepository.getMainFrame().setStatusPanel(String.valueOf(moduleId));
    }

    @Override
    public void addOptionsPanel(OptionsPanel optionsPanel) {
        moduleRepository.getOptionsDialog().addOptionsPanel(optionsPanel);
    }

    @Override
    public Preferences getPreferences() {
        return moduleRepository.getMainFrame().getPreferences();
    }

    @Override
    public void extendMainOptionsPanel(OptionsPanel panel) {
        moduleRepository.getOptionsDialog().extendMainOptionsPanel(panel);
    }

    @Override
    public void extendAppearanceOptionsPanel(OptionsPanel panel) {
        moduleRepository.getOptionsDialog().extendAppearanceOptionsPanel(panel);
    }
}
