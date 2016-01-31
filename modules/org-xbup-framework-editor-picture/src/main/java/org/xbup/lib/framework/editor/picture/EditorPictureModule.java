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

import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.editor.picture.panel.ImagePanel;
import org.xbup.lib.framework.editor.picture.panel.ImageStatusPanel;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.api.XBApplicationModulePlugin;
import org.xbup.lib.framework.gui.api.XBModuleRepositoryUtils;
import org.xbup.lib.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.file.api.FileType;
import org.xbup.lib.framework.gui.file.api.GuiFileModuleApi;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.undo.api.GuiUndoModuleApi;

/**
 * XBUP picture editor module.
 *
 * @version 0.2.0 2016/01/31
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class EditorPictureModule implements XBApplicationModulePlugin {

    public static final String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(EditorPictureModule.class);
    public static final String XBPFILETYPE = "XBPictureEditor.XBPFileType";

    public static final String IMAGE_STATUS_BAR_ID = "imageStatusBar";

    private XBApplication application;
    private XBEditorProvider editorProvider;
    private ImageStatusPanel imageStatusPanel;

    public EditorPictureModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;

        // Register file types
        GuiFileModuleApi fileModule = application.getModuleRepository().getModuleByInterface(GuiFileModuleApi.class);

        // Register file types
        String[] formats = ImageIO.getReaderFormatNames();
        for (String ext : formats) {
            if (ext.toLowerCase().equals(ext)) {
                fileModule.addFileType(new PictureFileType(ext));
            }
        }

        fileModule.addFileType(new XBPFileType());
    }

    @Override
    public void unregisterPlugin(String pluginId) {
    }

    public XBEditorProvider getEditorProvider() {
        if (editorProvider == null) {
            ImagePanel imagePanel = new ImagePanel();

            GuiUndoModuleApi undoModule = application.getModuleRepository().getModuleByInterface(GuiUndoModuleApi.class);
            // imagePanel.setUndoHandler(undoModule.getUndoHandler());

            editorProvider = imagePanel;

//            imagePanel.addStatusChangeListener(new AudioPanel.StatusChangeListener() {
//                @Override
//                public void statusChanged() {
//                    updateStatus();
//                }
//            });
//            imagePanel.addWaveRepaintListener(new AudioPanel.WaveRepaintListener() {
//                @Override
//                public void waveRepaint() {
//                    updatePositionTime();
//                }
//            });
//
//            imagePanel.attachCaretListener(new MouseMotionListener() {
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
//            imagePanel.setPopupMenu(createPopupMenu());
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
        imageStatusPanel = new ImageStatusPanel(new ImageControlApi() {
            @Override
            public void editSelection() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
        frameModule.registerStatusBar(MODULE_ID, IMAGE_STATUS_BAR_ID, imageStatusPanel);
        frameModule.switchStatusBar(IMAGE_STATUS_BAR_ID);
        ((ImagePanel) getEditorProvider()).registerImageStatus(imageStatusPanel);
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

    public class XBPFileType extends FileFilter implements FileType {

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
                return "xbp".contains(extension.substring(0, 3));
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "XBUP Picture Files (*.xbp*)";
        }

        @Override
        public String getFileTypeId() {
            return XBPFILETYPE;
        }
    }
}
