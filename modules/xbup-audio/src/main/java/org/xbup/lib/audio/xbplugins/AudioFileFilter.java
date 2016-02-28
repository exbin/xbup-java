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
package org.xbup.lib.audio.xbplugins;

import java.io.File;
import java.util.Locale;
import javax.swing.filechooser.FileFilter;

/**
 * Audio file type.
 *
 * @version 0.1.25 2015/02/20
 * @author ExBin Project (http://exbin.org)
 */
public class AudioFileFilter extends FileFilter {

    public static final String AUDIO_FILE_TYPE = "AudioFileFilter";
    private String ext;

    public AudioFileFilter(String ext) {
        this.ext = ext;
    }

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
        if (extension != null) {
            return extension.toLowerCase(Locale.getDefault()).equals(getExt());
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Audio files " + getExt().toUpperCase(Locale.getDefault()) + " (*." + getExt() + ")";
    }

    public String getFileTypeId() {
        return AUDIO_FILE_TYPE + "." + ext;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public static String getExtension(File f) {
        String ext = null;
        String str = f.getName();
        int extPos = str.lastIndexOf('.');

        if (extPos > 0 && extPos < str.length() - 1) {
            ext = str.substring(extPos + 1).toLowerCase(Locale.getDefault());
        }
        return ext;
    }

}
