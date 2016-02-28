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
package org.xbup.lib.framework.editor.text;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import org.xbup.lib.framework.editor.text.dialog.ManageEncodingsDialog;
import org.xbup.lib.framework.editor.text.panel.TextEncodingPanel;
import org.xbup.lib.framework.editor.text.panel.TextEncodingPanelApi;
import org.xbup.lib.framework.editor.text.panel.TextPanel;
import org.xbup.lib.framework.editor.text.panel.TextStatusPanel;
import org.xbup.lib.framework.api.XBApplication;
import org.xbup.lib.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;
import org.xbup.lib.framework.gui.utils.ActionUtils;

/**
 * Encodings handler.
 *
 * @version 0.2.0 2016/01/23
 * @author ExBin Project (http://exbin.org)
 */
public class EncodingsHandler implements TextEncodingPanelApi {

    private final XBEditorProvider editorProvider;
    private final XBApplication application;
    private final EditorTextModule textModule;
    private final ResourceBundle resourceBundle;

    private List<String> encodings = null;
    private ActionListener encodingActionListener;
    private ButtonGroup encodingButtonGroup;
    private javax.swing.JMenu toolsEncodingMenu;
    private javax.swing.JRadioButtonMenuItem utfEncodingRadioButtonMenuItem;

    private Action manageEncodingsAction;

    public EncodingsHandler(XBApplication application, XBEditorProvider editorProvider, EditorTextModule textModule) {
        this.application = application;
        this.editorProvider = editorProvider;
        this.textModule = textModule;
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorTextModule.class);
    }

    public void init() {
        encodings = new ArrayList<>();
        encodingButtonGroup = new ButtonGroup();

        encodingActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSelectedEncoding(((JRadioButtonMenuItem) e.getSource()).getText());
            }
        };

        utfEncodingRadioButtonMenuItem = new JRadioButtonMenuItem();
        utfEncodingRadioButtonMenuItem.setSelected(true);
        utfEncodingRadioButtonMenuItem.setText("UTF-8 (default)");
        utfEncodingRadioButtonMenuItem.setToolTipText("Set encoding UTF-8");
        utfEncodingRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setSelectedEncoding(TextEncodingPanel.ENCODING_UTF8);
            }
        });

        encodingButtonGroup.add(utfEncodingRadioButtonMenuItem);
        manageEncodingsAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
                ManageEncodingsDialog dlg = new ManageEncodingsDialog(frameModule.getFrame(), EncodingsHandler.this, true);
                dlg.setIconImage(application.getApplicationIcon());
                TextEncodingPanel panel = dlg.getEncodingPanel();
                panel.setEncodingList(new ArrayList<>(encodings));
                dlg.setLocationRelativeTo(dlg.getParent());
                dlg.setVisible(true);
                if (dlg.getDialogOption() == JOptionPane.OK_OPTION) {
                    encodings = panel.getEncodingList();
                    encodingsRebuild();
                }
            }
        };
        ActionUtils.setupAction(manageEncodingsAction, resourceBundle, "manageEncodingsAction");
        manageEncodingsAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);
        manageEncodingsAction.putValue(Action.NAME, manageEncodingsAction.getValue(Action.NAME) + GuiMenuModuleApi.DIALOG_MENUITEM_EXT);

        toolsEncodingMenu = new JMenu();
        toolsEncodingMenu.add(utfEncodingRadioButtonMenuItem);
        toolsEncodingMenu.addSeparator();
        toolsEncodingMenu.add(manageEncodingsAction);
        toolsEncodingMenu.setText(resourceBundle.getString("toolsEncodingMenu.text"));
        toolsEncodingMenu.setToolTipText(resourceBundle.getString("toolsEncodingMenu.toolTipText"));
    }

    @Override
    public List<String> getEncodings() {
        return encodings;
    }

    @Override
    public void setEncodings(List<String> encodings) {
        this.encodings = encodings;
    }

    @Override
    public String getSelectedEncoding() {
        return ((TextPanel) editorProvider.getPanel()).getCharset().name();
    }

    @Override
    public void setSelectedEncoding(String encoding) {
        if (encoding != null) {
            ((TextPanel) editorProvider.getPanel()).setCharset(Charset.forName(encoding));
            TextStatusPanel textStatusPanel = textModule.getTextStatusPanel();
            textStatusPanel.setEncoding(encoding);
        }
    }

    public JMenu getToolsEncodingMenu() {
        return toolsEncodingMenu;
    }

    public void encodingsRebuild() {
        String encodingToolTip = "Set encoding ";
        for (int i = toolsEncodingMenu.getItemCount() - 2; i > 1; i--) {
            toolsEncodingMenu.remove(i);
        }

        if (encodings.size() > 0) {
            int selectedEncoding = encodings.indexOf(getSelectedEncoding());
            if (selectedEncoding < 0) {
                setSelectedEncoding(TextEncodingPanel.ENCODING_UTF8);
                utfEncodingRadioButtonMenuItem.setSelected(true);
            }
            toolsEncodingMenu.add(new JSeparator(), 1);
            for (int index = 0; index < encodings.size(); index++) {
                String encoding = encodings.get(index);
                JRadioButtonMenuItem item = new JRadioButtonMenuItem(encoding, false);
                item.addActionListener(encodingActionListener);
                item.setToolTipText(encodingToolTip + encoding);
                toolsEncodingMenu.add(item, index + 2);
                encodingButtonGroup.add(item);
                if (index == selectedEncoding) {
                    item.setSelected(true);
                }
            }
        }
    }
}
