/*
 * Copyright (C) XBUP Project
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
package org.xbup.lib.framework.editor.picture;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.api.XBApplicationModulePlugin;
import org.xbup.lib.framework.gui.api.XBModuleRepositoryUtils;
import org.xbup.lib.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.file.api.GuiFileModuleApi;

/**
 * XBUP picture editor module.
 *
 * @version 0.2.0 2016/01/26
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class EditorPictureModule implements XBApplicationModulePlugin {

    public static final String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(EditorPictureModule.class);

    public static final String TEXT_STATUS_BAR_ID = "textStatusBar";

    private XBApplication application;
    private XBEditorProvider editorProvider;
    private boolean playing = false;

    public EditorPictureModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;

        // Register file types
        GuiFileModuleApi fileModule = application.getModuleRepository().getModuleByInterface(GuiFileModuleApi.class);

        // Register file types
//        String[] formats = new String[]{"wav", "aiff", "au"};
//        for (String ext : formats) {
//            if (ext.toLowerCase().equals(ext)) {
//                fileModule.addFileType(new AudioFileType(ext));
//            }
//        }
//
//        fileModule.addFileType(new XBSFileType());
    }

    @Override
    public void unregisterPlugin(String pluginId) {
    }

    public XBEditorProvider getEditorProvider() {
        if (editorProvider == null) {
//            AudioPanel audioPanel = new AudioPanel();
//
//            GuiUndoModuleApi undoModule = application.getModuleRepository().getModuleByInterface(GuiUndoModuleApi.class);
//            audioPanel.setUndoHandler(undoModule.getUndoHandler());
//
//            editorProvider = audioPanel;
//
//            audioPanel.addStatusChangeListener(new AudioPanel.StatusChangeListener() {
//                @Override
//                public void statusChanged() {
//                    updateStatus();
//                }
//            });
//            audioPanel.addWaveRepaintListener(new AudioPanel.WaveRepaintListener() {
//                @Override
//                public void waveRepaint() {
//                    updatePositionTime();
//                }
//            });
//
//            audioPanel.attachCaretListener(new MouseMotionListener() {
//
//                @Override
//                public void mouseDragged(MouseEvent e) {
//                }
//
//                @Override
//                public void mouseMoved(MouseEvent e) {
//                    if (editorProvider == null) {
//                        return;
//                    }
//
//                    updatePositionTime();
//                }
//            });
//
//            audioPanel.setPopupMenu(createPopupMenu());
        }

        return editorProvider;
    }

    private void updateStatus() {
//        updatePositionTime();
//
//        AudioPanel audioPanel = (AudioPanel) editorProvider;
//        if (audioPanel.getIsPlaying() != playing) {
//            playing = !playing;
//            audioStatusPanel.setPlayButtonIcon(playing
//                    ? new javax.swing.ImageIcon(getClass().getResource("/org/xbup/lib/framework/editor/wave/resources/images/actions/pause16.png"))
//                    : new javax.swing.ImageIcon(getClass().getResource("/org/xbup/lib/framework/editor/wave/resources/images/actions/play16.png"))
//            );
//        }
    }

    public void registerStatusBar() {
//        audioStatusPanel = new AudioStatusPanel(new AudioControlApi() {
//            @Override
//            public void performPlay() {
//                ((AudioPanel) editorProvider).performPlay();
//            }
//
//            @Override
//            public void performStop() {
//                ((AudioPanel) editorProvider).performStop();
//            }
//
//            @Override
//            public void setVolume(int volumeLevel) {
//                ((AudioPanel) editorProvider).setVolume(volumeLevel);
//            }
//        });
//
//        GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
//        frameModule.registerStatusBar(MODULE_ID, TEXT_STATUS_BAR_ID, audioStatusPanel);
//        frameModule.switchStatusBar(TEXT_STATUS_BAR_ID);
//        // ((AudioPanel) getEditorProvider()).registerAudioStatus(audioStatusPanel);
    }

    public void registerOptionsMenuPanels() {
//        getEncodingsHandler();
//        JMenu toolsEncodingMenu = encodingsHandler.getToolsEncodingMenu();
//        encodingsHandler.encodingsRebuild();

//        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
//        menuModule.registerMenuItem(GuiFrameModuleApi.TOOLS_MENU_ID, MODULE_ID, encodingsHandler.getToolsEncodingMenu(), new MenuPosition(PositionMode.TOP_LAST));
    }

    public void registerOptionsPanels() {
//        GuiOptionsModuleApi optionsModule = application.getModuleRepository().getModuleByInterface(GuiOptionsModuleApi.class);
//        WaveColorPanelApi textColorPanelFrame = new WaveColorPanelApi() {
//            @Override
//            public Color[] getCurrentWaveColors() {
//                return ((AudioPanel) getEditorProvider()).getAudioPanelColors();
//            }
//
//            @Override
//            public Color[] getDefaultWaveColors() {
//                return ((AudioPanel) getEditorProvider()).getDefaultColors();
//            }
//
//            @Override
//            public void setCurrentWaveColors(Color[] colors) {
//                ((AudioPanel) getEditorProvider()).setAudioPanelColors(colors);
//            }
//        };
    }

    public void registerToolsOptionsMenuActions() {
//        getToolsOptionsHandler();
//        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
//        menuModule.registerMenuItem(GuiFrameModuleApi.TOOLS_MENU_ID, MODULE_ID, toolsOptionsHandler.getToolsSetColorAction(), new MenuPosition(PositionMode.TOP));
    }
}
