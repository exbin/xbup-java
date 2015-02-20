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

import java.beans.PropertyChangeListener;

/**
 * Interface for application's panel.
 *
 * @version 0.1.22 2013/03/04
 * @author XBUP Project (http://xbup.org)
 */
public interface ApplicationFilePanel extends ApplicationPanel {

    /**
     * Loads file from given filename.
     */
    public void loadFromFile();

    /**
     * Saves file to given filename.
     */
    public void saveToFile();

    /**
     * Gets current filename.
     *
     * @return filename
     */
    public String getFileName();

    /**
     * Sets current filename.
     *
     * @param fileName
     */
    public void setFileName(String fileName);

    /**
     * Sets current filetype.
     *
     * @param fileType
     */
    public void setFileType(FileType fileType);

    /**
     * Creates new file.
     */
    public void newFile();

    /**
     * Returns flag if file in this panel was modified since last saving.
     *
     * @return true if file was modified
     */
    public boolean isModified();

    /**
     * Changes passing listener.
     *
     * @param propertyChangeListener Change listener.
     */
    public void setPropertyChangeListener(PropertyChangeListener propertyChangeListener);

    /**
     * Gets window title related to last opened or saved file.
     *
     * @param frameTitle title of frame
     * @return title related to last opened file
     */
    public String getWindowTitle(String frameTitle);
}
