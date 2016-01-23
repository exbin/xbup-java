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
package org.xbup.lib.framework.gui.editor.api;

import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import org.xbup.lib.framework.gui.file.api.FileHandlerApi;

/**
 * XBUP framework editor api module.
 *
 * @version 0.2.0 2016/01/09
 * @author XBUP Project (http://xbup.org)
 */
public interface XBEditorProvider extends FileHandlerApi {

    /**
     * Returns currently active panel.
     *
     * @return panel
     */
    public JPanel getPanel();

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
