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
package org.exbin.framework.editor.xbup;

import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import org.exbin.framework.gui.editor.api.XBEditorProvider;
import org.exbin.framework.gui.file.api.FileType;

/**
 * XBUP editor module.
 *
 * @version 0.2.0 2015/12/20
 * @author ExBin Project (http://exbin.org)
 */
public class XbupEditorHandler implements XBEditorProvider {

    @Override
    public JPanel getPanel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getWindowTitle(String frameTitle) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void loadFromFile() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveToFile() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getFileName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFileName(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFileType(FileType ft) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void newFile() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isModified() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
