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
package org.xbup.lib.framework.gui.service;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.xbup.lib.framework.gui.file.api.FileType;

/**
 * XB file type.
 *
 * @version 0.1.25 2015/02/20
 * @author ExBin Project (http://exbin.org)
 */
public class XBFileType extends FileFilter implements FileType {

    public static final String XB_FILE_TYPE = "XBFileType";

    public XBFileType() {
    }

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        String extension = getExtension(file);
        if (extension != null) {
            return "xb".equals(extension);
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "XB File (*.xb)";
    }

    @Override
    public String getFileTypeId() {
        return XB_FILE_TYPE;
    }

    /**
     * Gets the extension part of file name.
     *
     * @param file Source file
     * @return extension part of file name
     */
    public static String getExtension(File file) {
        String ext = null;
        String str = file.getName();
        int extPos = str.lastIndexOf('.');

        if (extPos > 0 && extPos < str.length() - 1) {
            ext = str.substring(extPos + 1).toLowerCase();
        }
        return ext;
    }
}
