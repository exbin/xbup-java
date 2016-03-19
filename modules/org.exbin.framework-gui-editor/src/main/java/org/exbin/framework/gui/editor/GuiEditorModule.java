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
package org.exbin.framework.gui.editor;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.gui.editor.api.GuiEditorModuleApi;
import org.exbin.framework.gui.editor.api.XBEditorProvider;
import org.exbin.framework.gui.editor.panel.SingleEditorPanel;
import org.exbin.framework.gui.file.api.FileHandlingActionsApi;
import org.exbin.framework.gui.file.api.GuiFileModuleApi;
import org.exbin.framework.gui.menu.api.ClipboardActionsUpdateListener;
import org.exbin.framework.gui.menu.api.ComponentClipboardHandler;
import org.exbin.framework.gui.menu.api.GuiMenuModuleApi;
import org.exbin.framework.gui.undo.api.ActivePanelUndoable;
import org.exbin.framework.gui.undo.api.GuiUndoModuleApi;
import org.exbin.xbup.operation.Command;
import org.exbin.xbup.operation.undo.UndoUpdateListener;
import org.exbin.xbup.operation.undo.XBUndoHandler;

/**
 * XBUP framework editor module.
 *
 * @version 0.2.0 2016/01/23
 * @author ExBin Project (http://exbin.org)
 */
@PluginImplementation
public class GuiEditorModule implements GuiEditorModuleApi {

    private XBApplication application;
    private final List<XBEditorProvider> editors = new ArrayList<>();
    private final Map<String, List<XBEditorProvider>> pluginEditorsMap = new HashMap<>();
    private SingleEditorPanel editorPanel;
    private XBEditorProvider activeEditor = null;
    private ClipboardActionsUpdateListener clipboardActionsUpdateListener = null;

    public GuiEditorModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;

        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerClipboardHandler(new ComponentClipboardHandler() {
            @Override
            public void performCut() {
                if (activeEditor instanceof ComponentClipboardHandler) {
                    ((ComponentClipboardHandler) activeEditor).performCut();
                }
            }

            @Override
            public void performCopy() {
                if (activeEditor instanceof ComponentClipboardHandler) {
                    ((ComponentClipboardHandler) activeEditor).performCopy();
                }
            }

            @Override
            public void performPaste() {
                if (activeEditor instanceof ComponentClipboardHandler) {
                    ((ComponentClipboardHandler) activeEditor).performPaste();
                }
            }

            @Override
            public void performDelete() {
                if (activeEditor instanceof ComponentClipboardHandler) {
                    ((ComponentClipboardHandler) activeEditor).performDelete();
                }
            }

            @Override
            public void performSelectAll() {
                if (activeEditor instanceof ComponentClipboardHandler) {
                    ((ComponentClipboardHandler) activeEditor).performSelectAll();
                }
            }

            @Override
            public boolean isSelection() {
                if (activeEditor instanceof ComponentClipboardHandler) {
                    return ((ComponentClipboardHandler) activeEditor).isSelection();
                }

                return false;
            }

            @Override
            public boolean isEditable() {
                if (activeEditor instanceof ComponentClipboardHandler) {
                    return ((ComponentClipboardHandler) activeEditor).isEditable();
                }

                return false;
            }

            @Override
            public boolean canSelectAll() {
                if (activeEditor instanceof ComponentClipboardHandler) {
                    return ((ComponentClipboardHandler) activeEditor).canSelectAll();
                }

                return false;
            }

            @Override
            public void setUpdateListener(ClipboardActionsUpdateListener updateListener) {
                clipboardActionsUpdateListener = updateListener;
            }

            @Override
            public boolean canPaste() {
                if (activeEditor instanceof ComponentClipboardHandler) {
                    return ((ComponentClipboardHandler) activeEditor).canPaste();
                }

                return true;
            }
        });
    }

    @Override
    public void unregisterPlugin(String pluginId) {
        List<XBEditorProvider> pluginEditors = pluginEditorsMap.get(pluginId);
        if (pluginEditors != null) {
            for (XBEditorProvider editor : pluginEditors) {
                editors.remove(editor);
            }
            pluginEditorsMap.remove(pluginId);
        }
    }

    @Override
    public void registerEditor(String pluginId, final XBEditorProvider editorProvider) {
        if (editorProvider instanceof ActivePanelUndoable) {
            ((ActivePanelUndoable) editorProvider).setUndoUpdateListener(new UndoUpdateListener() {
                @Override
                public void undoChanged() {
                    GuiUndoModuleApi undoModule = application.getModuleRepository().getModuleByInterface(GuiUndoModuleApi.class);
                    undoModule.updateUndoStatus();
                }
            });
        }
        if (editorProvider instanceof ComponentClipboardHandler) {
            ((ComponentClipboardHandler) editorProvider).setUpdateListener(new ClipboardActionsUpdateListener() {
                @Override
                public void stateChanged() {
                    if (editorProvider == activeEditor) {
                        clipboardActionsUpdateListener.stateChanged();
                    }
                }
            });
        }
        editors.add(editorProvider);
        List<XBEditorProvider> pluginEditors = pluginEditorsMap.get(pluginId);
        if (pluginEditors == null) {
            pluginEditors = new ArrayList<>();
            pluginEditorsMap.put(pluginId, pluginEditors);
        }

        pluginEditors.add(editorProvider);
    }

    @Override
    public Component getEditorPanel() {
        if (editorPanel == null) {
            activeEditor = editors.get(0);
            editorPanel = new SingleEditorPanel(activeEditor.getPanel());
            editorPanel.init();
        }

        GuiFileModuleApi fileModule = application.getModuleRepository().getModuleByInterface(GuiFileModuleApi.class);
        FileHandlingActionsApi fileHandlingActions = fileModule.getFileHandlingActions();
        fileHandlingActions.setFileHandler(activeEditor);

        return editorPanel;
    }

    @Override
    public void registerUndoHandler() {
        GuiUndoModuleApi undoModule = application.getModuleRepository().getModuleByInterface(GuiUndoModuleApi.class);
        undoModule.setUndoHandler(new XBUndoHandler() {
            @Override
            public boolean canRedo() {
                if (activeEditor != null && activeEditor.getPanel() instanceof ActivePanelUndoable) {
                    return ((ActivePanelUndoable) activeEditor.getPanel()).canRedo();
                }

                return false;
            }

            @Override
            public boolean canUndo() {
                if (activeEditor != null && activeEditor.getPanel() instanceof ActivePanelUndoable) {
                    return ((ActivePanelUndoable) activeEditor.getPanel()).canUndo();
                }

                return false;
            }

            @Override
            public void clear() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void doSync() throws Exception {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void execute(Command command) throws Exception {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public List<Command> getCommandList() {
                return new ArrayList<>();
            }

            @Override
            public long getCommandPosition() {
                return 0;
            }

            @Override
            public long getMaximumUndo() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public long getSyncPoint() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public long getUndoMaximumSize() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public long getUsedSize() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void performRedo() throws Exception {
                if (activeEditor != null && activeEditor.getPanel() instanceof ActivePanelUndoable) {
                    ((ActivePanelUndoable) activeEditor.getPanel()).performRedo();
                    GuiUndoModuleApi undoModule = application.getModuleRepository().getModuleByInterface(GuiUndoModuleApi.class);
                    undoModule.updateUndoStatus();
                }
            }

            @Override
            public void performRedo(int count) throws Exception {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void performUndo() throws Exception {
                if (activeEditor != null && activeEditor.getPanel() instanceof ActivePanelUndoable) {
                    ((ActivePanelUndoable) activeEditor.getPanel()).performUndo();
                    GuiUndoModuleApi undoModule = application.getModuleRepository().getModuleByInterface(GuiUndoModuleApi.class);
                    undoModule.updateUndoStatus();
                }
            }

            @Override
            public void performUndo(int count) throws Exception {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setCommandPosition(long targetPosition) throws Exception {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setSyncPoint(long syncPoint) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setSyncPoint() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void addUndoUpdateListener(UndoUpdateListener listener) {
            }

            @Override
            public void removeUndoUpdateListener(UndoUpdateListener listener) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

    }
}
