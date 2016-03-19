/*
 * Copyright (C) ExBin Project
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
package org.exbin.framework.editor.xbup;

import java.util.ResourceBundle;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.client.api.ClientConnectionEvent;
import org.exbin.framework.client.api.ClientConnectionListener;
import static org.exbin.framework.editor.xbup.EditorXbupModule.DOC_STATUS_BAR_ID;
import static org.exbin.framework.editor.xbup.EditorXbupModule.MODULE_ID;
import org.exbin.framework.editor.xbup.panel.XBDocStatusPanel;
import org.exbin.framework.gui.editor.api.XBEditorProvider;
import org.exbin.framework.gui.frame.api.GuiFrameModuleApi;
import org.exbin.framework.gui.utils.ActionUtils;

/**
 * Status panel handler.
 *
 * @version 0.2.0 2016/02/15
 * @author ExBin Project (http://exbin.org)
 */
public class StatusPanelHandler {

    private final XBEditorProvider editorProvider;
    private final XBApplication application;
    private final ResourceBundle resourceBundle;

    private XBDocStatusPanel docStatusPanel;

    public StatusPanelHandler(XBApplication application, XBEditorProvider editorProvider) {
        this.application = application;
        this.editorProvider = editorProvider;
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorXbupModule.class);
    }

    void init() {
    }

    public XBDocStatusPanel getDocStatusPanel() {
        if (docStatusPanel == null) {
            docStatusPanel = new XBDocStatusPanel();
            GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
            frameModule.registerStatusBar(MODULE_ID, DOC_STATUS_BAR_ID, docStatusPanel);
            frameModule.switchStatusBar(DOC_STATUS_BAR_ID);
            // ((XBDocumentPanel) getEditorProvider()).registerTextStatus(docStatusPanel);
        }

        return docStatusPanel;
    }
    
    public ClientConnectionListener getClientConnectionListener() {
        return new ClientConnectionListener() {
            @Override
            public void connectionChanged(ClientConnectionEvent connectionEvent) {
                docStatusPanel.setConnectionStatus(connectionEvent.getConnectionStatus());
            }
        };
    }
}
