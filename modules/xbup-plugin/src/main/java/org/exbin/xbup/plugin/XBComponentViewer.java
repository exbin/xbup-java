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

/**
 * Component viewer plugin interface.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBComponentViewer {

    /**
     * Returns instance of value representation panel component.
     *
     * @return component
     */
    @Nonnull
    JComponent getViewer();

    /**
     * Attaches change listener.
     *
     * @param listener change listener
     */
    void attachChangeListener(ChangeListener listener);

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
