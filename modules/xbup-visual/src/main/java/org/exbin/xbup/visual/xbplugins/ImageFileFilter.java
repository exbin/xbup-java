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
package org.exbin.xbup.visual.xbplugins;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Image file filter supporting single filename extension.
 *
 * @version 0.1.25 2015/02/03
 * @author ExBin Project (http://exbin.org)
 */
public class ImageFileFilter extends FileFilter {

    private String ext;

    public ImageFileFilter(String ext) {
        this.ext = ext;
    }

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
        if (extension != null) {
            return extension.toLowerCase().equals(getExt());
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Images " + getExt().toUpperCase() + " (*." + getExt() + ")";
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
