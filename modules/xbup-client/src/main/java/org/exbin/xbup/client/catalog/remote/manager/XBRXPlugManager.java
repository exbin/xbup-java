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
package org.exbin.xbup.client.catalog.remote.manager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXPlugin;
import org.exbin.xbup.client.stub.XBPXPlugStub;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.manager.XBCXPlugManager;

/**
 * Remote manager class for XBRXPlugin catalog items.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRXPlugManager extends XBRDefaultManager<XBCXPlugin> implements XBCXPlugManager {

    private final XBPXPlugStub plugStub;

    public XBRXPlugManager(XBRCatalog catalog) {
        super(catalog);
        plugStub = new XBPXPlugStub(client);
        setManagerStub(plugStub);
    }

    @Override
    public Long getAllPluginCount() {
        throw new UnsupportedOperationException("Not supported yet.");
        /*        try {
         XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SPECSCOUNT_SPEC_PROCEDURE);
         XBListener listener = message.getXBOutput();
         listener.endXB();
         XBStreamChecker checker = message.getXBInput();
         Long index = checker.matchAttribXB().getNaturalLong();
         checker.matchEndXB();
         message.close();
         return index;
         } catch (XBProcessingException ex) {
         Logger.getLogger(XBRItem.class.getExtensionName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
         Logger.getLogger(XBRItem.class.getExtensionName()).log(Level.SEVERE, null, ex);
         }
         return null; */
    }

    @Override
    public Long[] getPluginXBPath(XBCXPlugin plugin) {
        ArrayList<Long> list = new ArrayList<>();
        XBCNode parent = plugin.getOwner();
        while (parent != null) {
            if (parent.getParent().isPresent()) {
                list.add(0, parent.getXBIndex());
            }
            parent = (XBCNode) parent.getParent().orElse(null);
        }
        list.add(plugin.getId());
        return (Long[]) list.toArray(new Long[list.size()]);
    }

    @Override
    public XBRXPlugin findById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InputStream getPlugin(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXPlugin findPlugin(XBCNode node, Long index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBCXPlugin> findPluginsForNode(XBCNode node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void initializeExtension() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getExtensionName() {
        return "Plugin Extension";
    }
}
