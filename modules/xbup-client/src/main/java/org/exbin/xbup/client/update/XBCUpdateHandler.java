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
package org.exbin.xbup.client.update;

import java.util.Date;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Interface for XB catalog update handler.
 *
 * Provides methods for updating catalog database.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBCUpdateHandler {

    /**
     * Initialize Updater.
     */
    void init();

//    XBContext updateTypeSpecFromWS(Long[] path, long specId);
//
//    XBCFormatSpec updateFormatSpec(Long[] path, Long specId);
//
//    XBCGroupSpec updateGroupSpec(Long[] path, Long specId);
//
//    XBCBlockSpec updateBlockSpec(Long[] path, Long specId);
//
//    XBCFormatSpec addFormatSpecFromWS(XBCNode node, Long specId);
//
//    XBCGroupSpec addGroupSpecFromWS(XBCNode node, Long specId);
//
//    XBCBlockSpec addBlockSpecFromWS(XBCNode node, Long specId);
//
//    XBCNode addNodeFromWS(XBCNode node, Long nodeId);
//
//    /**
//     * Process all catalog data in given path and subnodes.
//     *
//     * @param path path
//     */
//    void processAllData(Long[] path);
//
//    boolean processNodePath(Long[] path);
//
    void performUpdate();

    void performUpdateMain();

    @Nonnull
    Date getMainLastUpdate();

    void addUpdateListener(XBCUpdateListener listener);

    void removeUpdateListener(XBCUpdateListener listener);

    void fireUsageEvent(boolean usage);
}
