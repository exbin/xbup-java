/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
     * Finds all files for given node.
     *
     * @param node parent node of the file
     * @return list of files or null if no such file exist
     */
    List<XBCXFile> findFilesForNode(XBCNode node);
}
