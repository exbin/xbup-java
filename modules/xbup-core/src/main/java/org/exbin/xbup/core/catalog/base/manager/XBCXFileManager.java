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
package org.exbin.xbup.core.catalog.base.manager;

import java.io.InputStream;
import java.util.List;
import javax.swing.ImageIcon;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXFile;

/**
 * Interface for XBCXFile catalog manager.
 *
 * @version 0.1.22 2013/07/28
 * @author ExBin Project (http://exbin.org)
 * @param <T> file entity
 */
public interface XBCXFileManager<T extends XBCXFile> extends XBCManager<T>, XBCExtension {

    /**
     * Returns path of XBIndexes for given file.
     *
     * @param file
     * @return array of path indexes
     */
    public Long[] getFileXBPath(XBCXFile file);

    /**
     * Finds file by its node and filename.
     *
     * @param node parent node of the file
     * @param fileName name of the file
     * @return file or null if no such file exist
     */
    public XBCXFile findFile(XBCNode node, String fileName);

    /**
     * Gets file as image.
     *
     * @param file source file
     * @return image representation of the file
     */
    public ImageIcon getFileAsImageIcon(XBCXFile file);

    /**
     * Returns data stream access to file.
     *
     * @param file source file
     * @return data stream
     */
    public InputStream getFile(XBCXFile file);

    /**
     * Finds all files for given node.
     *
     * @param node parent node of the file
     * @return list of files or null if no such file exist
     */
    public List<XBCXFile> findFilesForNode(XBCNode node);
}
