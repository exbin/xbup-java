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
package org.xbup.tool.editor.base.api;

/**
 * Interface for application module management.
 *
 * @version 0.1.22 2013/03/01
 * @author XBUP Project (http://xbup.org)
 */
public interface ModuleManagement {

    /**
     * Registers basic application panel for this module.
     *
     * @param panel
     */
    public void registerPanel(ApplicationPanel panel);

    /**
     * Gets menu management instance.
     *
     * @return menu management
     */
    public MenuManagement getMenuManagement();

    /**
     * Gets file type management instance.
     *
     * @return file type management
     */
    public FileTypeManagement getFileTypeManagement();

    /**
     * Gets status bars mamagement instance.
     *
     * @return status management
     */
    public StatusManagement getStatusManagement();

    /**
     * Gets instance of options management.
     *
     * @return options management
     */
    public OptionsManagement getOptionsManagement();

    /**
     * Gets main frame management.
     *
     * @return main frame management
     */
    public MainFrameManagement getMainFrameManagement();
}
