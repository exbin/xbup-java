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

import javax.annotation.Nullable;

/**
 * Abstract panel viewer plugin.
 *
 * @version 0.2.1 2020/09/25
 * @author ExBin Project (http://exbin.org)
 */
public abstract class XBAbstractPanelViewer implements XBPanelViewer {

    @Nullable
    private ChangeListener changeListener = null;

    public void fireValueChange() {
        if (changeListener != null) {
            changeListener.valueChanged();
        }
    }

    @Override
    public void attachChangeListener(@Nullable ChangeListener listener) {
        changeListener = listener;
    }
}
