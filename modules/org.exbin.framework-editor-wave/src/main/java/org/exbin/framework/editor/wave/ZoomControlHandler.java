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
package org.exbin.framework.editor.wave;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.editor.wave.panel.AudioPanel;
import org.exbin.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.utils.ActionUtils;

/**
 * Zoom mode control handler.
 *
 * @version 0.2.0 2016/01/28
 * @author ExBin Project (http://exbin.org)
 */
public class ZoomControlHandler {

    public static String ZOOM_RADIO_GROUP_ID = "zoomRadioGroup";

    private final XBEditorProvider editorProvider;
    private final XBApplication application;
    private final ResourceBundle resourceBundle;

    private int metaMask;

    private Action normalZoomAction;
    private Action zoomUpAction;
    private Action zoomDownAction;

    public ZoomControlHandler(XBApplication application, XBEditorProvider editorProvider) {
        this.application = application;
        this.editorProvider = editorProvider;
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorWaveModule.class);
    }

    public void init() {
        metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        normalZoomAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof AudioPanel) {
                    AudioPanel activePanel = (AudioPanel) editorProvider;
                    activePanel.scaleAndSeek(1);
                }
            }
        };
        ActionUtils.setupAction(normalZoomAction, resourceBundle, "normalZoomAction");

        zoomUpAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof AudioPanel) {
                    AudioPanel activePanel = (AudioPanel) editorProvider;
                    activePanel.scaleAndSeek(activePanel.getScale() / 2);
                }
            }
        };
        ActionUtils.setupAction(zoomUpAction, resourceBundle, "zoomUpAction");

        zoomDownAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof AudioPanel) {
                    AudioPanel activePanel = (AudioPanel) editorProvider;
                    activePanel.scaleAndSeek(activePanel.getScale() * 2);
                }
            }
        };
        ActionUtils.setupAction(zoomDownAction, resourceBundle, "zoomDownAction");
    }

    public Action getNormalZoomAction() {
        return normalZoomAction;
    }

    public Action getZoomUpAction() {
        return zoomUpAction;
    }

    public Action getZoomDownAction() {
        return zoomDownAction;
    }
}
