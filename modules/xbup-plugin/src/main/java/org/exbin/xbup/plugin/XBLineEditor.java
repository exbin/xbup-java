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
package org.exbin.xbup.plugin;

import javax.annotation.Nonnull;
import javax.swing.JComponent;
import org.exbin.xbup.core.serial.XBSerializable;

/**
 * XBUP Line Editor Plugin Base Interface.
 *
 * @version 0.2.1 2017/05/25
 * @author ExBin Project (http://exbin.org)
 */
public interface XBLineEditor extends XBSerializable {

    /**
     * Returns instance of value representation component.
     *
     * @return component
     */
    @Nonnull
    JComponent getComponent();

    /**
     * Returns instance of value editing component.
     *
     * @return component
     */
    @Nonnull
    public JComponent getEditor();

    /**
     * Loads data from given editor.
     *
     * @param editor editor component
     * @return true, if value was changed or cannot compare values
     */
    public boolean finishEditor(@Nonnull JComponent editor);

    /**
     * Attaches change listener.
     *
     * @param listener change listener
     */
    public void attachChangeListener(@Nonnull ChangeListener listener);

    /**
     * Change listener interface.
     */
    public interface ChangeListener {

        void valueChanged();
    }
}
