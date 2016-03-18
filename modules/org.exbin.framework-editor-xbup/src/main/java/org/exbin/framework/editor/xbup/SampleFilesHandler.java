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

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.exbin.framework.api.XBApplication;
import org.xbup.lib.core.parser.XBProcessingException;
import org.exbin.framework.editor.xbup.panel.XBDocumentPanel;
import org.exbin.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.utils.ActionUtils;

/**
 * Sample files handler.
 *
 * @version 0.2.0 2016/02/09
 * @author ExBin Project (http://exbin.org)
 */
public class SampleFilesHandler {

    private final XBEditorProvider editorProvider;
    private final XBApplication application;
    private final ResourceBundle resourceBundle;

    private int metaMask;

    private Action sampleHtmlFileAction;
    private Action samplePictureFileAction;

    public SampleFilesHandler(XBApplication application, XBEditorProvider editorProvider) {
        this.application = application;
        this.editorProvider = editorProvider;
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorXbupModule.class);
    }

    public void init() {
        metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        sampleHtmlFileAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof XBDocumentPanel) {
                    XBDocumentPanel documentPanel = (XBDocumentPanel) editorProvider;
                    documentPanel.newFile();
                    try {
                        documentPanel.getDoc().fromStreamUB(getClass().getResourceAsStream("/org/xbup/lib/framework/editor/xbup/resources/samples/xhtml_example.xb"));
                        documentPanel.getDoc().processSpec();
                    } catch (XBProcessingException | IOException ex) {
                        Logger.getLogger(SampleFilesHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    documentPanel.reportStructureChange(null);
                    documentPanel.updateItem();
                }
            }
        };
        ActionUtils.setupAction(sampleHtmlFileAction, resourceBundle, "sampleHtmlFileAction");

        samplePictureFileAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof XBDocumentPanel) {
                    XBDocumentPanel documentPanel = (XBDocumentPanel) editorProvider;
                    documentPanel.newFile();
                    try {
                        documentPanel.getDoc().fromStreamUB(getClass().getResourceAsStream("/org/xbup/lib/framework/editor/xbup/resources/samples/xblogo.xbp"));
                        documentPanel.getDoc().processSpec();
                    } catch (XBProcessingException | IOException ex) {
                        Logger.getLogger(SampleFilesHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    documentPanel.reportStructureChange(null);
                    documentPanel.updateItem();
                }
            }
        };
        ActionUtils.setupAction(samplePictureFileAction, resourceBundle, "samplePictureFileAction");
    }

    public Action getSampleHtmlFileAction() {
        return sampleHtmlFileAction;
    }

    public Action getSamplePictureFileAction() {
        return samplePictureFileAction;
    }
}
