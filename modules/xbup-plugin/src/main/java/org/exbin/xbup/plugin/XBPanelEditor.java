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
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import org.exbin.xbup.core.block.XBTBlock;

/**
 * Panel editor plugin interface.
 *
 * @version 0.2.1 2020/09/25
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBPanelEditor {

    /**
     * Returns instance of value representation panel component.
     *
     * @return component
     */
    @Nonnull
    JComponent getEditor();

    /**
     * Attaches change listener.
     *
     * @param listener change listener
     */
    void attachChangeListener(ChangeListener listener);

    /**
     * Loads data from given editor.
     *
     * @return true, if value was changed or cannot compare values
     */
    boolean finishEditor();

    /**
     * Sets data to component editor.
     *
     * @param block block
     */
    void setData(XBTBlock block);

    /**
     * Change listener interface.
     */
    public interface ChangeListener {

        void valueChanged();
    }
}
