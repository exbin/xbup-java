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
package org.xbup.lib.framework.editor.picture;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileFilter;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.editor.picture.panel.ImagePanel;
import org.xbup.lib.framework.editor.picture.panel.ImageStatusPanel;
import org.xbup.lib.framework.api.XBApplication;
import org.xbup.lib.framework.api.XBApplicationModulePlugin;
import org.xbup.lib.framework.api.XBModuleRepositoryUtils;
import org.xbup.lib.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.file.api.FileType;
import org.xbup.lib.framework.gui.file.api.GuiFileModuleApi;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;
import org.xbup.lib.framework.gui.menu.api.MenuPosition;
import org.xbup.lib.framework.gui.menu.api.NextToMode;
import org.xbup.lib.framework.gui.menu.api.PositionMode;
import org.xbup.lib.framework.gui.menu.api.SeparationMode;
import org.xbup.lib.framework.gui.undo.api.GuiUndoModuleApi;

/**
 * XBUP picture editor module.
 *
 * @version 0.2.0 2016/02/09
 * @author ExBin Project (http://exbin.org)
 */
@PluginImplementation
public class EditorPictureModule implements XBApplicationModulePlugin {

    public static final String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(EditorPictureModule.class);
    public static final String XBPFILETYPE = "XBPictureEditor.XBPFileType";
    public static final String ZOOM_MODE_SUBMENU_ID = MODULE_ID + ".zoomSubMenu";
    public static final String PICTURE_MENU_ID = MODULE_ID + ".pictureMenu";
    public static final String PICTURE_OPERATION_MENU_ID = MODULE_ID + ".pictureOperationMenu";
    public static final String PICTURE_POPUP_MENU_ID = MODULE_ID + ".picturePopupMenu";

    public static final String IMAGE_STATUS_BAR_ID = "imageStatusBar";

    private XBApplication application;
    private XBEditorProvider editorProvider;
    private ImageStatusPanel imageStatusPanel;

    private ToolsOptionsHandler toolsOptionsHandler;
    private PropertiesHandler propertiesHandler;
    private PrintHandler printHandler;
    private ZoomControlHandler zoomControlHandler;
    private PictureOperationHandler pictureOperationHandler;

    public EditorPictureModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;
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

            imagePanel.attachCaretListener(new MouseMotionListener() {

                @Override
                public void mouseDragged(MouseEvent e) {
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    if (editorProvider == null) {
                        return;
                    }

                    updateCurrentPosition();
                }
            });
            imagePanel.setPopupMenu(createPopupMenu());
        }

        return editorProvider;
    }

    public void registerFileTypes() {
        GuiFileModuleApi fileModule = application.getModuleRepository().getModuleByInterface(GuiFileModuleApi.class);
        String[] formats = ImageIO.getReaderFormatNames();
        for (String ext : formats) {
            if (ext.toLowerCase().equals(ext)) {
                fileModule.addFileType(new PictureFileType(ext));
            }
        }

        fileModule.addFileType(new XBPFileType());
    }

    private void updateCurrentPosition() {
        Point mousePosition = ((ImagePanel) editorProvider).getMousePosition();
        double scale = ((ImagePanel) editorProvider).getScale();
        imageStatusPanel.setCurrentPosition(new Point((int) (mousePosition.x * scale), (int) (mousePosition.y * scale)));
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

    public void registerPropertiesMenu() {
        getPropertiesHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.FILE_MENU_ID, MODULE_ID, propertiesHandler.getPropertiesAction(), new MenuPosition(PositionMode.BOTTOM));
    }

    public void registerPrintMenu() {
        getPrintHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.FILE_MENU_ID, MODULE_ID, printHandler.getPrintAction(), new MenuPosition(PositionMode.BOTTOM));
    }

    private PictureOperationHandler getPictureOperationHandler() {
        if (pictureOperationHandler == null) {
            pictureOperationHandler = new PictureOperationHandler(application, (ImagePanel) getEditorProvider());
            pictureOperationHandler.init();
        }

        return pictureOperationHandler;
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

    private JPopupMenu createPopupMenu() {
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenu(PICTURE_POPUP_MENU_ID, MODULE_ID);
        menuModule.registerClipboardMenuItems(PICTURE_POPUP_MENU_ID, MODULE_ID, SeparationMode.AROUND);
        JPopupMenu popupMenu = new JPopupMenu();
        menuModule.buildMenu(popupMenu, PICTURE_POPUP_MENU_ID);
        return popupMenu;
    }

    public void registerToolsOptionsMenuActions() {
        getToolsOptionsHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.TOOLS_MENU_ID, MODULE_ID, toolsOptionsHandler.getToolsSetColorAction(), new MenuPosition(PositionMode.TOP));
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

    private PropertiesHandler getPropertiesHandler() {
        if (propertiesHandler == null) {
            propertiesHandler = new PropertiesHandler(application, (ImagePanel) getEditorProvider());
            propertiesHandler.init();
        }

        return propertiesHandler;
    }

    private ToolsOptionsHandler getToolsOptionsHandler() {
        if (toolsOptionsHandler == null) {
            toolsOptionsHandler = new ToolsOptionsHandler(application, (ImagePanel) getEditorProvider());
            toolsOptionsHandler.init();
        }

        return toolsOptionsHandler;
    }

    private PrintHandler getPrintHandler() {
        if (printHandler == null) {
            printHandler = new PrintHandler(application, (ImagePanel) getEditorProvider());
            printHandler.init();
        }

        return printHandler;
    }

    private ZoomControlHandler getZoomControlHandler() {
        if (zoomControlHandler == null) {
            zoomControlHandler = new ZoomControlHandler(application, (ImagePanel) getEditorProvider());
            zoomControlHandler.init();
        }

        return zoomControlHandler;
    }

    public void registerPictureMenu() {
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenu(PICTURE_MENU_ID, MODULE_ID);
        menuModule.registerMenuItem(GuiFrameModuleApi.MAIN_MENU_ID, MODULE_ID, PICTURE_MENU_ID, "Picture", new MenuPosition(NextToMode.AFTER, "View"));
    }

    public void registerPictureOperationMenu() {
        getPictureOperationHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(PICTURE_MENU_ID, MODULE_ID, pictureOperationHandler.getRevertAction(), new MenuPosition(PositionMode.TOP));
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
