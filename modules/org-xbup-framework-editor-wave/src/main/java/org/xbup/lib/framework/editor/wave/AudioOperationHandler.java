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
package org.xbup.lib.framework.editor.wave;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.xbup.lib.framework.api.XBApplication;
import org.xbup.lib.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.utils.ActionUtils;
import org.xbup.lib.framework.editor.wave.panel.AudioPanel;

/**
 * Audio operation handler.
 *
 * @version 0.2.0 2016/01/24
 * @author ExBin Project (http://exbin.org)
 */
public class AudioOperationHandler {

    private final XBEditorProvider editorProvider;
    private final XBApplication application;
    private final ResourceBundle resourceBundle;

    private int metaMask;

    private Action audioReverseAction;

    public AudioOperationHandler(XBApplication application, XBEditorProvider editorProvider) {
        this.application = application;
        this.editorProvider = editorProvider;
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorWaveModule.class);
    }

    public void init() {
        metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        audioReverseAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof AudioPanel) {
                    AudioPanel activePanel = (AudioPanel) editorProvider;
                    activePanel.performTransformReverse();
                }
            }
        };
        ActionUtils.setupAction(audioReverseAction, resourceBundle, "audioReverseAction");
    }

    public Action getRevertAction() {
        return audioReverseAction;
    }
}
