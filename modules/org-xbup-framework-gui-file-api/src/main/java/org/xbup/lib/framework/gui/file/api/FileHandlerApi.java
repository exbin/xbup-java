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
package org.xbup.lib.framework.gui.file.api;

/**
 * Interface for file handling actions.
 *
 * @version 0.2.0 2016/01/09
 * @author ExBin Project (http://exbin.org)
 */
public interface FileHandlerApi {

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
}
