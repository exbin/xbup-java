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
package org.xbup.tool.editor.base.manager;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.xbup.tool.editor.module.frame.MainFrame;
import org.xbup.tool.editor.base.api.ApplicationFilePanel;
import org.xbup.tool.editor.base.api.FileType;
import org.xbup.tool.editor.base.api.FileTypeManagement;

/**
 * Manager for file types.
 *
 * @version 0.1 wr22.0 2013/03/04
 * @author XBUP Project (http://xbup.org)
 */
public class FileTypeManager implements FileTypeManagement {

    private BaseModuleRepository moduleRepository;
    private Map<String, FileType> fileTypes;
    private String fileName;

    FileTypeManager(BaseModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
        fileTypes = new HashMap<String, FileType>();
    }

    @Override
    public void addFileType(FileType fileFilter) {
        JFileChooser openFileChooser = moduleRepository.getMainFrame().getOpenFC();
        openFileChooser.addChoosableFileFilter((FileFilter) fileFilter);
        JFileChooser saveFileChooser = moduleRepository.getMainFrame().getSaveFC();
        saveFileChooser.addChoosableFileFilter((FileFilter) fileFilter);
        fileTypes.put(fileFilter.getFileTypeId(), fileFilter);
    }

    @Override
    public boolean openFile(JFileChooser fileChooser) {
        ApplicationFilePanel panel = (ApplicationFilePanel) moduleRepository.getMainFrame().getActivePanel();
        panel.setFileName(fileChooser.getSelectedFile().getAbsolutePath());
        panel.setFileType((FileType) fileChooser.getFileFilter());
        panel.loadFromFile();
        return true;
    }

    @Override
    public boolean openFile(String path, String fileTypeId) {
        ApplicationFilePanel panel = (ApplicationFilePanel) moduleRepository.getMainFrame().getActivePanel();
        panel.setFileName(path);
        FileType fileType = fileTypes.get(fileTypeId);
        if (fileType == null) {
            fileType = fileTypes.get(MainFrame.ALLFILESFILTER);
        }
        panel.setFileType(fileType);
        panel.loadFromFile();
        return true;
    }

    @Override
    public void finish() {
        addFileType(moduleRepository.getMainFrame().newAllFilesFilter());
    }

    @Override
    public boolean saveFile(JFileChooser saveFC) {
        setFileName(saveFC.getSelectedFile().getAbsolutePath());
        ApplicationFilePanel panel = (ApplicationFilePanel) moduleRepository.getMainFrame().getActivePanel();
        panel.setFileName(getFileName());
        panel.setFileType((FileType) saveFC.getFileFilter());
        return saveFile();
    }

    @Override
    public boolean saveFile() {
        if (("".equals(getFileName()))||(getFileName() == null)) {
            return false;
        }
        ApplicationFilePanel panel = (ApplicationFilePanel) moduleRepository.getMainFrame().getActivePanel();
        panel.saveToFile();
        return true;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void newFile() {
        setFileName(null);
        ApplicationFilePanel panel;
        panel = (ApplicationFilePanel) moduleRepository.getMainFrame().getActivePanel();
        panel.newFile();
   }

    @Override
    public String getWindowTitle() {
         ApplicationFilePanel panel = (ApplicationFilePanel) moduleRepository.getMainFrame().getActivePanel();
         return panel.getWindowTitle(moduleRepository.getMainFrame().getFrameTitle());
    }
}
