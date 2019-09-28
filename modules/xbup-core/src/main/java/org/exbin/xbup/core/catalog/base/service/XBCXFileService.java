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
package org.exbin.xbup.core.catalog.base.service;

import java.io.InputStream;
import java.util.List;
import javax.swing.ImageIcon;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXFile;

/**
 * Interface for XBCXFile items service.
 *
 * @version 0.1.21 2011/12/31
 * @author ExBin Project (http://exbin.org)
 * @param <T> file entity
 */
public interface XBCXFileService<T extends XBCXFile> extends XBCService<T>, XBCExtension {

    /**
     * Returns path of XBIndexes for given node.
     *
     * @param file file
     * @return array of path indexes
     */
    Long[] getFileXBPath(XBCXFile file);

    /**
     * Finds file by its node and filename.
     *
     * @param node parent node of the file
     * @param fileName name of the file
     * @return file or null if no such file exist
     */
    XBCXFile findFile(XBCNode node, String fileName);

    /**
     * Gets file as image.
     *
     * @param file source file
     * @return image representation of the file
     */
    ImageIcon getFileAsImageIcon(XBCXFile file);

    /**
     * Accesses file as data stream.
     *
     * @param file source file
     * @return data stream
     */
    InputStream getFile(XBCXFile file);

    /**
     * Finds all file for given node.
     *
     * @param node parent node of the file
     * @return list of files or null if no such file exist
     */
    List<XBCXFile> findFilesForNode(XBCNode node);
}
