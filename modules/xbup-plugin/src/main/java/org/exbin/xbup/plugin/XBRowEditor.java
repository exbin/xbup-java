/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.plugin;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.serial.XBSerializable;

/**
 * Row editor plugin interface.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBRowEditor extends XBSerializable {

    /**
     * Returns instance of value representation component.
     *
     * @return component
     */
    @Nonnull
    JComponent getViewer();

    /**
     * Returns instance of value editing component.
     *
     * @return component
     */
    @Nonnull
    public JComponent getEditor();

    /**
     * Sets data to component editor.
     *
     * @param block block
     */
    void setData(XBTBlock block);

    /**
     * Reads data from component editor.
     *
     * @return block
     */
    @Nonnull
    XBTBlock getData();

    /**
     * Loads data from given editor.
     *
     * @return true, if value was changed or cannot compare values
     */
    boolean finishEditor();

    /**
     * Attaches change listener.
     *
     * @param listener change listener
     */
    void attachChangeListener(ChangeListener listener);

    /**
     * Change listener interface.
     */
    public interface ChangeListener {

        void valueChanged();
    }
}
