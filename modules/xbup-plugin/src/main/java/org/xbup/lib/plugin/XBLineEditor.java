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
package org.xbup.lib.plugin;

import javax.swing.JComponent;
import org.xbup.lib.core.serial.XBSerializable;

/**
 * XBUP Line Editor Plugin Base Interface.
 *
 * @version 0.1.24 2015/01/06
 * @author XBUP Project (http://xbup.org)
 */
public interface XBLineEditor extends XBSerializable {

    /**
     * Returns instance of value representation component.
     *
     * @return component
     */
    public JComponent getComponent();

    /**
     * Returns instance of value editing component.
     *
     * @return component
     */
    public JComponent getEditor();

    /**
     * Loads data from given editor.
     *
     * @param editor editor component
     * @return true, if value was changed or cannot compare values
     */
    public boolean finishEditor(JComponent editor);

    /**
     * Attaches change listener.
     *
     * @param listener change listener
     */
    public void attachChangeListener(ChangeListener listener);

    /**
     * Change listener interface.
     */
    public interface ChangeListener {

        void valueChanged();
    }
}
