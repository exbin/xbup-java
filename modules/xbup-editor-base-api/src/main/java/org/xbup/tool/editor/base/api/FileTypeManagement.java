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

import javax.swing.JFileChooser;

/**
 * Interface for file types management.
 *
 * @version 0.1.22 2013/03/04
 * @author XBUP Project (http://xbup.org)
 */
public interface FileTypeManagement {

    /**
     * Adds file type into manager.
     *
     * FileType should extends javax.swing.filechooser.FileFilter.
     *
     * @param fileType file type
     */
    public void addFileType(FileType fileType);

    /**
     * Opens file from given fileChooser with the respect to used file type.
     *
     * @param fileChooser
     * @return true if file opened successfuly
     */
    public boolean openFile(JFileChooser fileChooser);

    /**
     * Open file from given properties.
     *
     * @param path full path to file
     * @param fileTypeId file type ID
     * @return true if file opened successfuly
     */
    public boolean openFile(String path, String fileTypeId);

    /**
     * Finishes last file operation.
     */
    public void finish();

    /**
     * Saves file using given file chooser.
     *
     * @param saveFC file chooser
     * @return true if file saved successfuly
     */
    public boolean saveFile(JFileChooser saveFC);

    /**
     * Saves file using last used filename.
     *
     * @return true if file saved successfuly
     */
    public boolean saveFile();

    /**
     * Creates new file.
     */
    public void newFile();

    /**
     * Gets window title.
     *
     * @return window title
     */
    public String getWindowTitle();

}
