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
package org.exbin.xbup.core.catalog.base.manager;

import java.util.List;
import javax.swing.ImageIcon;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXIcon;
import org.exbin.xbup.core.catalog.base.XBCXIconMode;

/**
 * Interface for XBCXIcon catalog manager.
 *
 * @version 0.1.24 2014/11/26
 * @author ExBin Project (http://exbin.org)
 * @param <T> icon entity
 */
public interface XBCXIconManager<T extends XBCXIcon> extends XBCManager<T>, XBCExtension {

    /**
     * Gets icon mode by unique index.
     *
     * @param iconModeId icon mode id
     * @return icon mode
     */
    XBCXIconMode getIconMode(Long iconModeId);

    /**
     * Gets list of icons for block specification.
     *
     * @param blockSpec block specification
     * @return list of icons
     */
    List<XBCXIcon> getBlockSpecIcons(XBCBlockSpec blockSpec);

    /**
     * Gets default icon for item.
     *
     * @param item item
     * @return icon
     */
    XBCXIcon getDefaultIcon(XBCItem item);

    /**
     * Gets default icon for given item.
     *
     * @param item item we want to get icon of
     * @return icon instance or null
     */
    ImageIcon getDefaultImageIcon(XBCItem item);

    /**
     * Gets default big icon.
     *
     * @param item catalog item
     * @return icon data or null
     */
    XBCXIcon getDefaultBigIcon(XBCItem item);

    /**
     * Gets default small icon.
     *
     * @param item catalog item
     * @return icon data or null
     */
    XBCXIcon getDefaultSmallIcon(XBCItem item);

    /**
     * Gets default big icon.
     *
     * @param item catalog item
     * @return icon data or null
     */
    byte[] getDefaultBigIconData(XBCItem item);

    /**
     * Gets default small icon.
     *
     * @param item catalog item
     * @return icon data or null
     */
    byte[] getDefaultSmallIconData(XBCItem item);
}
