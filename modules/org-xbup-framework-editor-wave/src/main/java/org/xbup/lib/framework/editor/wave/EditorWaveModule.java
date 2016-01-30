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
package org.xbup.lib.framework.editor.wave;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileFilter;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.editor.wave.panel.AudioDevicesPanel;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.api.XBApplicationModulePlugin;
import org.xbup.lib.framework.gui.api.XBModuleRepositoryUtils;
import org.xbup.lib.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.file.api.FileType;
import org.xbup.lib.framework.gui.file.api.GuiFileModuleApi;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;
import org.xbup.lib.framework.gui.menu.api.MenuPosition;
import org.xbup.lib.framework.gui.menu.api.PositionMode;
import org.xbup.lib.framework.gui.options.api.GuiOptionsModuleApi;
import org.xbup.lib.framework.editor.wave.panel.AudioPanel;
import org.xbup.lib.framework.editor.wave.panel.AudioStatusPanel;
import org.xbup.lib.framework.editor.wave.panel.WaveColorOptionsPanel;
import org.xbup.lib.framework.editor.wave.panel.WaveColorPanelApi;
import org.xbup.lib.framework.gui.menu.api.MenuGroup;
import org.xbup.lib.framework.gui.menu.api.NextToMode;
import org.xbup.lib.framework.gui.menu.api.SeparationMode;
import org.xbup.lib.framework.gui.undo.api.GuiUndoModuleApi;

/**
 * XBUP audio editor module.
 *
 * @version 0.2.0 2016/01/30
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class EditorWaveModule implements XBApplicationModulePlugin {

    public static final String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(EditorWaveModule.class);
    public static final String AUDIO_MENU_ID = MODULE_ID + ".audioMenu";
    public static final String AUDIO_OPERATION_MENU_ID = MODULE_ID + ".audioOperationMenu";
    public static final String AUDIO_POPUP_MENU_ID = MODULE_ID + ".audioPopupMenu";
    public static final String DRAW_MODE_SUBMENU_ID = MODULE_ID + ".drawSubMenu";
    public static final String ZOOM_MODE_SUBMENU_ID = MODULE_ID + ".zoomSubMenu";
    public static final String TOOLS_SELECTION_MENU_GROUP_ID = MODULE_ID + ".toolsSelectionMenuGroup";

    public static final String XBS_FILE_TYPE = "XBWaveEditor.XBSFileFilter";

    public static final String TEXT_STATUS_BAR_ID = "textStatusBar";

    private XBApplication application;
    private XBEditorProvider editorProvider;
    private AudioStatusPanel audioStatusPanel;
    private boolean playing = false;

    private ToolsOptionsHandler toolsOptionsHandler;
    private PropertiesHandler propertiesHandler;
    private AudioControlHandler audioControlHandler;
    private DrawingControlHandler drawingControlHandler;
    private ToolsSelectionHandler toolsSelectionHandler;
    private ZoomControlHandler zoomControlHandler;
    private AudioOperationHandler audioOperationHandler;

    public EditorWaveModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;

        // Register file types
        GuiFileModuleApi fileModule = application.getModuleRepository().getModuleByInterface(GuiFileModuleApi.class);

        // Register file types
        String[] formats = new String[]{"wav", "aiff", "au"};
        for (String ext : formats) {
            if (ext.toLowerCase().equals(ext)) {
                fileModule.addFileType(new AudioFileType(ext));
            }
        }

        fileModule.addFileType(new XBSFileType());
    }

    @Override
    public void unregisterPlugin(String pluginId) {
    }

    public XBEditorProvider getEditorProvider() {
        if (editorProvider == null) {
            AudioPanel audioPanel = new AudioPanel();

            GuiUndoModuleApi undoModule = application.getModuleRepository().getModuleByInterface(GuiUndoModuleApi.class);
            audioPanel.setUndoHandler(undoModule.getUndoHandler());

            editorProvider = audioPanel;

            audioPanel.addStatusChangeListener(new AudioPanel.StatusChangeListener() {
                @Override
                public void statusChanged() {
                    updateStatus();
                }
            });
            audioPanel.addWaveRepaintListener(new AudioPanel.WaveRepaintListener() {
                @Override
                public void waveRepaint() {
                    updatePositionTime();
                }
            });

            audioPanel.attachCaretListener(new MouseMotionListener() {

                @Override
                public void mouseDragged(MouseEvent e) {
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    if (editorProvider == null) {
                        return;
                    }

                    updatePositionTime();
                }
            });

            audioPanel.setPopupMenu(createPopupMenu());
        }

        return editorProvider;
    }

    private void updatePositionTime() {
        audioStatusPanel.setCurrentTime(((AudioPanel) editorProvider).getPositionTime());
    }

    private void updateStatus() {
        updatePositionTime();

        AudioPanel audioPanel = (AudioPanel) editorProvider;
        if (audioPanel.getIsPlaying() != playing) {
            playing = !playing;
            audioStatusPanel.setPlayButtonIcon(playing
                    ? new javax.swing.ImageIcon(getClass().getResource("/org/xbup/lib/framework/editor/wave/resources/images/actions/pause16.png"))
                    : new javax.swing.ImageIcon(getClass().getResource("/org/xbup/lib/framework/editor/wave/resources/images/actions/play16.png"))
            );
        }
    }

    public void registerStatusBar() {
        audioStatusPanel = new AudioStatusPanel(new AudioControlApi() {
            @Override
            public void performPlay() {
                ((AudioPanel) editorProvider).performPlay();
            }

            @Override
            public void performStop() {
                ((AudioPanel) editorProvider).performStop();
            }

            @Override
            public void setVolume(int volumeLevel) {
                ((AudioPanel) editorProvider).setVolume(volumeLevel);
            }
        });

        GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
        frameModule.registerStatusBar(MODULE_ID, TEXT_STATUS_BAR_ID, audioStatusPanel);
        frameModule.switchStatusBar(TEXT_STATUS_BAR_ID);
        // ((AudioPanel) getEditorProvider()).registerAudioStatus(audioStatusPanel);
    }

    public void registerOptionsPanels() {
        GuiOptionsModuleApi optionsModule = application.getModuleRepository().getModuleByInterface(GuiOptionsModuleApi.class);
        WaveColorPanelApi waveColorPanelFrame = new WaveColorPanelApi() {
            @Override
            public Color[] getCurrentWaveColors() {
                return ((AudioPanel) getEditorProvider()).getAudioPanelColors();
            }

            @Override
            public Color[] getDefaultWaveColors() {
                return ((AudioPanel) getEditorProvider()).getDefaultColors();
            }

            @Override
            public void setCurrentWaveColors(Color[] colors) {
                ((AudioPanel) getEditorProvider()).setAudioPanelColors(colors);
            }
        };

        optionsModule.addOptionsPanel(new WaveColorOptionsPanel(waveColorPanelFrame));
        optionsModule.addOptionsPanel(new AudioDevicesPanel());
    }

    public void registerToolsOptionsMenuActions() {
        getToolsOptionsHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.TOOLS_MENU_ID, MODULE_ID, toolsOptionsHandler.getToolsSetColorAction(), new MenuPosition(PositionMode.MIDDLE));
    }

    public void registerToolsMenuActions() {
        getToolsSelectionHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuGroup(GuiFrameModuleApi.TOOLS_MENU_ID, new MenuGroup(TOOLS_SELECTION_MENU_GROUP_ID, new MenuPosition(PositionMode.TOP), SeparationMode.AROUND));
        menuModule.registerMenuItem(GuiFrameModuleApi.TOOLS_MENU_ID, MODULE_ID, toolsSelectionHandler.getSelectionToolAction(), new MenuPosition(TOOLS_SELECTION_MENU_GROUP_ID));
        menuModule.registerMenuItem(GuiFrameModuleApi.TOOLS_MENU_ID, MODULE_ID, toolsSelectionHandler.getPencilToolAction(), new MenuPosition(TOOLS_SELECTION_MENU_GROUP_ID));
    }

    public AudioStatusPanel getAudioStatusPanel() {
        return audioStatusPanel;
    }

    private PropertiesHandler getPropertiesHandler() {
        if (propertiesHandler == null) {
            propertiesHandler = new PropertiesHandler(application, (AudioPanel) getEditorProvider());
            propertiesHandler.init();
        }

        return propertiesHandler;
    }

    private AudioControlHandler getAudioControlHandler() {
        if (audioControlHandler == null) {
            audioControlHandler = new AudioControlHandler(application, (AudioPanel) getEditorProvider());
            audioControlHandler.init();
        }

        return audioControlHandler;
    }

    private AudioOperationHandler getAudioOperationHandler() {
        if (audioOperationHandler == null) {
            audioOperationHandler = new AudioOperationHandler(application, (AudioPanel) getEditorProvider());
            audioOperationHandler.init();
        }

        return audioOperationHandler;
    }

    private DrawingControlHandler getDrawingControlHandler() {
        if (drawingControlHandler == null) {
            drawingControlHandler = new DrawingControlHandler(application, (AudioPanel) getEditorProvider());
            drawingControlHandler.init();
        }

        return drawingControlHandler;
    }

    private ToolsSelectionHandler getToolsSelectionHandler() {
        if (toolsSelectionHandler == null) {
            toolsSelectionHandler = new ToolsSelectionHandler(application, (AudioPanel) getEditorProvider());
            toolsSelectionHandler.init();
        }

        return toolsSelectionHandler;
    }

    private ZoomControlHandler getZoomControlHandler() {
        if (zoomControlHandler == null) {
            zoomControlHandler = new ZoomControlHandler(application, (AudioPanel) getEditorProvider());
            zoomControlHandler.init();
        }

        return zoomControlHandler;
    }

    private ToolsOptionsHandler getToolsOptionsHandler() {
        if (toolsOptionsHandler == null) {
            toolsOptionsHandler = new ToolsOptionsHandler(application, (AudioPanel) getEditorProvider());
            toolsOptionsHandler.init();
        }

        return toolsOptionsHandler;
    }

    public void registerPropertiesMenu() {
        getPropertiesHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.FILE_MENU_ID, MODULE_ID, propertiesHandler.getPropertiesAction(), new MenuPosition(PositionMode.BOTTOM));
    }

    public void registerAudioMenu() {
        getAudioControlHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenu(AUDIO_MENU_ID, MODULE_ID);
        menuModule.registerMenuItem(GuiFrameModuleApi.MAIN_MENU_ID, MODULE_ID, AUDIO_MENU_ID, "Audio", new MenuPosition(NextToMode.AFTER, "View"));
        menuModule.registerMenuItem(AUDIO_MENU_ID, MODULE_ID, audioControlHandler.getPlayAction(), new MenuPosition(PositionMode.TOP));
        menuModule.registerMenuItem(AUDIO_MENU_ID, MODULE_ID, audioControlHandler.getStopAction(), new MenuPosition(PositionMode.TOP));
    }

    public void registerAudioOperationMenu() {
        getAudioOperationHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenu(AUDIO_OPERATION_MENU_ID, MODULE_ID);
        menuModule.registerMenuItem(AUDIO_MENU_ID, MODULE_ID, AUDIO_OPERATION_MENU_ID, "Operation", new MenuPosition(PositionMode.BOTTOM));
        menuModule.registerMenuItem(AUDIO_OPERATION_MENU_ID, MODULE_ID, audioOperationHandler.getRevertAction(), new MenuPosition(PositionMode.TOP));
    }

    public void registerDrawingModeMenu() {
        getDrawingControlHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.VIEW_MENU_ID, MODULE_ID, DRAW_MODE_SUBMENU_ID, "Draw Mode", new MenuPosition(PositionMode.BOTTOM));
    }

    public void registerZoomModeMenu() {
        getZoomControlHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.VIEW_MENU_ID, MODULE_ID, ZOOM_MODE_SUBMENU_ID, "Zoom", new MenuPosition(PositionMode.BOTTOM));
        menuModule.registerMenu(ZOOM_MODE_SUBMENU_ID, MODULE_ID);
        menuModule.registerMenuItem(ZOOM_MODE_SUBMENU_ID, MODULE_ID, zoomControlHandler.getZoomUpAction(), new MenuPosition(PositionMode.TOP));
        menuModule.registerMenuItem(ZOOM_MODE_SUBMENU_ID, MODULE_ID, zoomControlHandler.getNormalZoomAction(), new MenuPosition(PositionMode.TOP));
        menuModule.registerMenuItem(ZOOM_MODE_SUBMENU_ID, MODULE_ID, zoomControlHandler.getZoomDownAction(), new MenuPosition(PositionMode.TOP));
    }

    public void bindZoomScrollWheel() {
        // ((AudioPanel) getEditorProvider()).
    }

    private JPopupMenu createPopupMenu() {
        getAudioControlHandler();
        getDrawingControlHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenu(AUDIO_POPUP_MENU_ID, MODULE_ID);
        menuModule.registerMenuItem(AUDIO_POPUP_MENU_ID, MODULE_ID, audioControlHandler.getPlayAction(), new MenuPosition(PositionMode.TOP));
        menuModule.registerMenuItem(AUDIO_POPUP_MENU_ID, MODULE_ID, audioControlHandler.getStopAction(), new MenuPosition(PositionMode.TOP));

        menuModule.registerClipboardMenuItems(AUDIO_POPUP_MENU_ID, MODULE_ID, SeparationMode.AROUND);
        menuModule.registerMenu(DRAW_MODE_SUBMENU_ID, MODULE_ID);
        menuModule.registerMenuItem(DRAW_MODE_SUBMENU_ID, MODULE_ID, drawingControlHandler.getDotsModeAction(), new MenuPosition(PositionMode.TOP));
        menuModule.registerMenuItem(DRAW_MODE_SUBMENU_ID, MODULE_ID, drawingControlHandler.getLineModeAction(), new MenuPosition(PositionMode.TOP));
        menuModule.registerMenuItem(DRAW_MODE_SUBMENU_ID, MODULE_ID, drawingControlHandler.getIntegralModeAction(), new MenuPosition(PositionMode.TOP));

        menuModule.registerMenuItem(AUDIO_POPUP_MENU_ID, MODULE_ID, DRAW_MODE_SUBMENU_ID, "Draw Mode", new MenuPosition(PositionMode.BOTTOM));
        JPopupMenu popupMenu = new JPopupMenu();
        menuModule.buildMenu(popupMenu, AUDIO_POPUP_MENU_ID);
        return popupMenu;
    }

    public class XBSFileType extends FileFilter implements FileType {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = getExtension(f);
            if (extension != null) {
                if (extension.length() < 3) {
                    return false;
                }
                return "xbs".contains(extension.substring(0, 3));
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "XBUP Text Files (*.xbs*)";
        }

        @Override
        public String getFileTypeId() {
            return XBS_FILE_TYPE;
        }
    }

    /**
     * Gets the extension part of file name.
     *
     * @param file Source file
     * @return extension part of file name
     */
    public static String getExtension(File file) {
        String ext = null;
        String str = file.getName();
        int i = str.lastIndexOf('.');

        if (i > 0 && i < str.length() - 1) {
            ext = str.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
