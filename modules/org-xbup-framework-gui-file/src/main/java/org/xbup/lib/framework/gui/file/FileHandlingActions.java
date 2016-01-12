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
package org.xbup.lib.framework.gui.file;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.xbup.lib.framework.gui.file.api.FileHandlingActionsApi;
import org.xbup.lib.framework.gui.file.api.FileHandlerApi;
import org.xbup.lib.framework.gui.file.api.FileType;
import org.xbup.lib.framework.gui.utils.ActionUtils;

/**
 * File handling operations.
 *
 * @version 0.2.0 2016/01/12
 * @author XBUP Project (http://xbup.org)
 */
public class FileHandlingActions implements FileHandlingActionsApi {

    public static final String ALL_FILES_FILTER = "AllFilesFilter";

    private ResourceBundle resourceBundle;
    private int metaMask;

    private Action newFileAction;
    private Action openFileAction;
    private Action saveFileAction;
    private Action saveAsFileAction;

    private JFileChooser openFC, saveFC;

    private FileHandlerApi fileHandler = null;
    private final Map<String, FileType> fileTypes = new HashMap<>();

    public FileHandlingActions() {
        resourceBundle = java.util.ResourceBundle.getBundle("org/xbup/lib/framework/gui/file/resources/GuiFileModule");
    }

    public void init() {
        metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        openFC = new JFileChooser();
        openFC.setAcceptAllFileFilterUsed(false);
        saveFC = new JFileChooser();
        saveFC.setAcceptAllFileFilterUsed(false);

        newFileAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionFileNew();
            }
        };
        ActionUtils.setupAction(newFileAction, resourceBundle, "fileNewAction");
        newFileAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, metaMask));

        openFileAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionFileOpen();
            }
        };
        ActionUtils.setupAction(openFileAction, resourceBundle, "fileOpenAction");
        openFileAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, metaMask));
        openFileAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);

        saveFileAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionFileSave();
            }
        };
        ActionUtils.setupAction(saveFileAction, resourceBundle, "fileSaveAction");
        saveFileAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, metaMask));

        saveAsFileAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                actionFileSaveAs();
            }
        };
        ActionUtils.setupAction(saveAsFileAction, resourceBundle, "fileSaveAsAction");
        saveAsFileAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, metaMask));
        saveAsFileAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);

        addFileType(new AllFilesFilter());
    }

    /**
     * Try to release current file and warn if document was modified.
     *
     * @return true if successfull
     */
    private boolean releaseFile() {
//        if (!(activePanel instanceof ApplicationFilePanel)) {
//            return true;
//        }
//        while (((ApplicationFilePanel) activePanel).isModified()) {
//            Object[] options = {
//                resourceBundle.getString("Question.modified_save"),
//                resourceBundle.getString("Question.modified_discard"),
//                resourceBundle.getString("Question.modified_cancel")
//            };
//            int result = JOptionPane.showOptionDialog(this,
//                    resourceBundle.getString("Question.modified"),
//                    resourceBundle.getString("Question.modified_title"),
//                    JOptionPane.YES_NO_CANCEL_OPTION,
//                    JOptionPane.QUESTION_MESSAGE,
//                    null, options, options[0]);
//            if (result == JOptionPane.NO_OPTION) {
//                return true;
//            }
//            if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
//                return false;
//            }
//
//            actionFileSave();
//        }

        return true;
    }

    /**
     * Asks whether it's allowed to overwrite file.
     *
     * @return true if allowed
     */
    private boolean overwriteFile() {
        Object[] options = {
            resourceBundle.getString("Question.overwrite_save"),
            resourceBundle.getString("Question.modified_cancel")
        };
//        int result = JOptionPane.showOptionDialog(
//                this,
//                resourceBundle.getString("Question.overwrite"),
//                resourceBundle.getString("Question.overwrite_title"),
//                JOptionPane.YES_NO_OPTION,
//                JOptionPane.QUESTION_MESSAGE,
//                null, options, options[0]);
//        if (result == JOptionPane.YES_OPTION) {
//            return true;
//        }
//        if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION) {
//            return false;
//        }

        return false;
    }

    @Override
    public Action getNewFileAction() {
        return newFileAction;
    }

    @Override
    public Action getOpenFileAction() {
        return openFileAction;
    }

    @Override
    public Action getSaveFileAction() {
        return saveFileAction;
    }

    @Override
    public Action getSaveAsFileAction() {
        return saveAsFileAction;
    }

    public void actionFileNew() {
        if (fileHandler != null) {
            fileHandler.newFile();
        }
    }

    public void actionFileOpen() {
        if (fileHandler != null) {
            if (!releaseFile()) {
                return;
            }

            if (openFC.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//                ((CardLayout) statusPanel.getLayout()).show(statusPanel, "busy");
//                statusPanel.repaint();
                String fileName = openFC.getSelectedFile().getAbsolutePath();
                fileHandler.setFileName(fileName);
                FileType fileType = fileTypes.get(null);
                if (fileType == null) {
                    fileType = fileTypes.get("XBTextEditor.TXTFileType"); // ALL_FILES_FILTER
                }
                fileHandler.setFileType(fileType);
                fileHandler.loadFromFile();

//                // Update recent files list
//                int i = 0;
//                while (i < recentFiles.size()) {
//                    RecentItem recentItem = recentFiles.get(i);
//                    if (recentItem.getPath().equals(fileName)) {
//                        recentFiles.remove(i);
//                    }
//                    i++;
//                }
//
//                recentFiles.add(new RecentItem(fileName, "", ((FileType) openFC.getFileFilter()).getFileTypeId()));
//                if (recentFiles.size() > 15) {
//                    recentFiles.remove(14);
//                }
//                rebuildRecentFilesMenu();
//                ((CardLayout) statusPanel.getLayout()).show(statusPanel, activeStatusPanel);
//                statusPanel.repaint();
            }
        }
    }

    public void actionFileSave() {
        if (fileHandler != null) {
            if (("".equals(fileHandler.getFileName())) || (fileHandler.getFileName() == null)) {
                actionFileSaveAs();
            } else {
                fileHandler.saveToFile();
            }
        }
    }

    public void actionFileSaveAs() {
//        if (fileHandler != null) {
//            if (saveFC.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
//                if (new File(saveFC.getSelectedFile().getAbsolutePath()).exists()) {
//                    if (!overwriteFile()) {
//                        return;
//                    }
//                }
//
//                try {
//                    fileHandler.saveFile(saveFC);
//                } catch (Exception ex) {
//                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
//                    String errorMessage = ex.getLocalizedMessage();
//                    JOptionPane.showMessageDialog(this, "Unable to save file: " + ex.getClass().getCanonicalName() + (errorMessage == null || errorMessage.isEmpty() ? "" : errorMessage), "Unable to save file", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        }
//
//            setFileName(saveFC.getSelectedFile().getAbsolutePath());
//            ApplicationFilePanel panel = (ApplicationFilePanel) moduleRepository.getMainFrame().getActivePanel();
//            panel.setFileName(getFileName());
//            panel.setFileType((FileType) saveFC.getFileFilter());
//            return saveFile();
//        }
//
//        @Override
//        public boolean saveFile() {
//            if (("".equals(getFileName())) || (getFileName() == null)) {
//                return false;
//            }
//            ApplicationFilePanel panel = (ApplicationFilePanel) moduleRepository.getMainFrame().getActivePanel();
//            panel.saveToFile();
//            return true;
//        }
    }

    void addFileType(FileType fileType) {
        openFC.addChoosableFileFilter((FileFilter) fileType);
        saveFC.addChoosableFileFilter((FileFilter) fileType);
        fileTypes.put(fileType.getFileTypeId(), fileType);
    }

    public class AllFilesFilter extends FileFilter implements FileType {

        @Override
        public boolean accept(File f) {
            return true;
        }

        @Override
        public String getDescription() {
            return "All files (*)";
        }

        @Override
        public String getFileTypeId() {
            return ALL_FILES_FILTER;
        }
    }

    @Override
    public FileHandlerApi getFileHandler() {
        return fileHandler;
    }

    @Override
    public void setFileHandler(FileHandlerApi fileHandler) {
        this.fileHandler = fileHandler;
    }
}
