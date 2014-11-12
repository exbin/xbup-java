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
package org.xbup.tool.editor.module.frame;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import javax.swing.ActionMap;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import net.xeoh.plugins.base.PluginManager;
import org.xbup.tool.editor.module.frame.dialog.AboutDialog;
import org.xbup.tool.editor.module.frame.dialog.OptionsDialog;
import org.xbup.tool.editor.base.api.ActivePanelActionHandling;
import org.xbup.tool.editor.base.api.ActivePanelUndoable;
import org.xbup.tool.editor.base.api.ApplicationFilePanel;
import org.xbup.tool.editor.base.api.ApplicationModule;
import org.xbup.tool.editor.base.api.ApplicationModuleInfo;
import org.xbup.tool.editor.base.api.ApplicationPanel;
import org.xbup.tool.editor.base.api.ComponentClipboardHandler;
import org.xbup.tool.editor.base.api.FileType;
import org.xbup.tool.editor.base.api.MainFrameManagement;
import org.xbup.tool.editor.base.api.ModuleRepository;
import org.xbup.tool.editor.base.api.XBEditorApp;
import org.xbup.tool.editor.base.api.XBEditorFrame;

/**
 * XBEditor Main Frame.
 *
 * @version 0.1.24 2014/11/12
 * @author XBUP Project (http://xbup.org)
 */
public class MainFrame extends javax.swing.JFrame implements XBEditorFrame, MainFrameManagement {

    public static final String PREFERENCES_RECENTFILE_PATH_PREFIX = "recentFile.path.";
    public static final String PREFEFRENCES_RECENTFILE_MODULE_PREFIX = "recentFile.module.";
    public static final String PREFERENCES_RECENFILE_MODE_PREFIX = "recentFile.mode.";

    private AboutDialog aboutDialog;
    private OptionsDialog optionsDialog;
    private final String DIALOG_MENU_SUFIX = "...";

    private XBEditorApp appEditor;
    private String activeStatusPanel;
    private JPanel activePanel;
    private List<RecentItem> recentFiles = null;

    private List<String> toolBarButtonText;

    java.util.ResourceBundle resourceBundle;
    private JFileChooser openFC, saveFC;

    public static final String ALLFILESFILTER = "MainFrame.AllFilesFilter";

    private JComponent lastFocusOwner = null;
    private Clipboard clipboard = null;
    private boolean isValidClipboardFlavor = false;
    private CaretListener textComponentCaretListener;
    private PropertyChangeListener textComponentPCL;

    private ActionMap defaultTextActionMap;
    private DefaultPopupClipboardAction defaultCutAction;
    private DefaultPopupClipboardAction defaultCopyAction;
    private DefaultPopupClipboardAction defaultPasteAction;
    private DefaultPopupClipboardAction defaultDeleteAction;
    private DefaultPopupClipboardAction defaultSelectAllAction;
    private DefaultPopupClipboardAction[] defaultTextActions;

    private ActionMap actionMap;
    private Component actionFocusOwner = null;
    private TextAction cutAction;
    private TextAction copyAction;
    private TextAction pasteAction;
    private TextAction deleteAction;
    private Action selectAllAction;

    private Action undoAction;
    private Action redoAction;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        init();
    }

    private void init() {
        resourceBundle = java.util.ResourceBundle.getBundle("org/xbup/tool/editor/module/frame/resources/MainFrame");

        optionsDialog = new OptionsDialog(this, this, true);

        // Create File Choosers
        openFC = new JFileChooser();
        openFC.setAcceptAllFileFilterUsed(false);
        saveFC = new JFileChooser();
        saveFC.setAcceptAllFileFilterUsed(false);

        initActions();

        initComponents();

//        activePanel.addPropertyChangeListener(new PropertyChangePassing(this));
        // Setup proper closing for this frame
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                actionExit();
            }

            @Override
            public void windowActivated(WindowEvent e) {
                isValidClipboardFlavor = getClipboard().isDataFlavorAvailable(DataFlavor.stringFlavor);
                updateFocusOwner(lastFocusOwner, lastFocusOwner);
            }
        });

        activeStatusPanel = "default";
        ((CardLayout) statusPanel.getLayout()).show(statusPanel, activeStatusPanel);

//        mainPanel.add(activePanel, java.awt.BorderLayout.CENTER);
        // Open file from command line
        /*
         String fileName = app.getFileName();
         if (!"".equals(fileName)) {
         setFileName(fileName);
         activePanel.loadFromFile();
         } */
        // Caret position listener
/*        caretChangeListener = new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
         //                Point pos = activePanel.getCaretPosition();
         Point pos = new Point();
         jTextField1.setText(Long.toString((long) pos.getX()) +":"+ Long.toString((long) pos.getY()));
         }
         }; */
        // Actions on change of look&feel
        UIManager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
//                activePanel.attachCaretListener(caretChangeListener);
                SwingUtilities.updateComponentTreeUI(getFrame());
                SwingUtilities.updateComponentTreeUI(mainPopupMenu);
                SwingUtilities.updateComponentTreeUI(openFC);
                SwingUtilities.updateComponentTreeUI(saveFC);
                if (aboutDialog != null) {
                    SwingUtilities.updateComponentTreeUI(aboutDialog);
                }
                if (optionsDialog != null) {
                    SwingUtilities.updateComponentTreeUI(optionsDialog);
                }
                repaint();
            }
        });
        initMenuBar();
        initMenu(toolsMenu);
        initPopupMenu(mainPopupMenu);
        initPopupMenu(defaultPopupMenu);

        recentFiles = new ArrayList<>();

        mainTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                ApplicationPanel panel = (ApplicationPanel) mainTabbedPane.getSelectedComponent();
                setStatusPanel(String.valueOf(tabModules.get(panel.getPanelName())));
            }
        });
    }

    public void applySetting(String lookAndFeel) {
        if (!lookAndFeel.isEmpty()) {
            try {
                UIManager.setLookAndFeel(lookAndFeel);

            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void loadState(Preferences preferences) {
        recentFiles.clear();
        int recent = 1;
        while (recent < 14) {
            String filePath = preferences.get(PREFERENCES_RECENTFILE_PATH_PREFIX + String.valueOf(recent), null);
            String moduleName = preferences.get(PREFEFRENCES_RECENTFILE_MODULE_PREFIX + String.valueOf(recent), null);
            String fileMode = preferences.get(PREFERENCES_RECENFILE_MODE_PREFIX + String.valueOf(recent), null);
            if (filePath == null) {
                break;
            }
            recentFiles.add(new RecentItem(filePath, moduleName, fileMode));
            recent++;
        }
        rebuildRecentFilesMenu();
    }

    private void saveState(Preferences prefs) {
        for (int i = 0; i < recentFiles.size(); i++) {
            prefs.put(PREFERENCES_RECENTFILE_PATH_PREFIX + String.valueOf(i + 1), recentFiles.get(i).getPath());
            prefs.put(PREFEFRENCES_RECENTFILE_MODULE_PREFIX + String.valueOf(i + 1), recentFiles.get(i).getModuleName());
            prefs.put(PREFERENCES_RECENFILE_MODE_PREFIX + String.valueOf(i + 1), recentFiles.get(i).getFileMode());
        }
        prefs.remove(PREFERENCES_RECENTFILE_PATH_PREFIX + String.valueOf(recentFiles.size() + 1));
        try {
            prefs.flush();
        } catch (BackingStoreException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void rebuildRecentFilesMenu() {
        fileOpenRecentMenu.removeAll();
        for (int recentFileIndex = 0; recentFileIndex < recentFiles.size(); recentFileIndex++) {
            File file = new File(recentFiles.get(recentFileIndex).getPath());
            JMenuItem menuItem = new JMenuItem(file.getName());
            menuItem.setToolTipText(recentFiles.get(recentFileIndex).getPath());
            menuItem.setIcon(FileSystemView.getFileSystemView().getSystemIcon(file));
            menuItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() instanceof JMenuItem) {
                        if (!releaseFile()) {
                            return;
                        }

                        JMenuItem menuItem = (JMenuItem) e.getSource();
                        for (int itemIndex = 0; itemIndex < fileOpenRecentMenu.getItemCount(); itemIndex++) {
                            if (menuItem.equals(fileOpenRecentMenu.getItem(itemIndex))) {
                                getAppEditor().getModuleRepository().openFile(recentFiles.get(itemIndex).getPath(), recentFiles.get(itemIndex).getFileMode());
                            }
                        }
                    }
                }
            });

            fileOpenRecentMenu.add(menuItem);
        }
        initMenuItem(fileOpenRecentMenu);
        fileOpenRecentMenu.setEnabled(recentFiles.size() > 0);
    }

    @Override
    public void initMenu(JMenu menu) {
        menu.addMenuListener(new MenuListener() {

            @Override
            public void menuSelected(MenuEvent e) {
                ((CardLayout) statusPanel.getLayout()).show(statusPanel, "main");
                statusLabel.setText(((JMenu) e.getSource()).getToolTipText());
                ToolTipManager.sharedInstance().setEnabled(false);
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                ((CardLayout) statusPanel.getLayout()).show(statusPanel, activeStatusPanel);
                ToolTipManager.sharedInstance().setEnabled(true);
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });

        initSubmenu(menu.getPopupMenu());
    }

    public void initSubmenu(JPopupMenu menu) {
        int j = 0;
        while (j < menu.getComponentCount()) {
            initMenuItem(menu.getComponent(j));
            j++;
        }
    }

    @Override
    public void initMenuItem(Object menuItem) {
        if (menuItem instanceof JMenuItem) {
            ((JMenuItem) menuItem).addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    JMenuItem menuItem = (JMenuItem) e.getSource();
                    if (!menuItem.isEnabled()) {
                        statusLabel.setText(menuItem.getToolTipText());
                        statusLabel.setForeground(Color.GRAY);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    JMenuItem menuItem = (JMenuItem) e.getSource();
                    if (!menuItem.isEnabled()) {
                        statusLabel.setText("");
                        statusLabel.setForeground(Color.BLACK);
                    }
                }
            });
            ((JMenuItem) menuItem).addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    JMenuItem menuItem = (JMenuItem) e.getSource();
                    statusLabel.setText(menuItem.getToolTipText());
                    statusLabel.setForeground(Color.BLACK);
                }
            });
            if (menuItem instanceof JMenu) {
                initSubmenu(((JMenu) menuItem).getPopupMenu());
            }
        } else if (menuItem instanceof JSeparator) {
            ((JSeparator) menuItem).addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    statusLabel.setText("");
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
        }
    }

    // TODO: Ordering
    private final Map<String, JPanel> tabs = new HashMap<>();
    private final Map<String, Long> tabModules = new HashMap<>();

    public void setActivePanel(JPanel panel, String panelName) {
        activePanel = panel;
        tabs.put(panelName, panel);
        tabModules.put(panelName, getAppEditor().getModuleRepository().getActiveModule());
//        mainTabbedPane.addTab(panelName, tabs.get(panelName));
//        activePanel = tabs.get(panelName);
//        mainTabbedPane.setSelectedComponent(activePanel);
        // Temporary modification
        mainPanel.add(panel, BorderLayout.CENTER);

        refreshUndo();
    }

    public JFileChooser getOpenFC() {
        return openFC;
    }

    public JFileChooser getSaveFC() {
        return saveFC;
    }

    public OptionsDialog getOptionsDialog() {
        return optionsDialog;
    }

    public void loadPreferences() {
        Preferences preferences = appEditor.getAppPreferences();
        optionsDialog.setPreferences(preferences);
        optionsDialog.loadPreferences(preferences);
        optionsDialog.applyPreferencesChanges();

        loadState(preferences);
    }

    @Override
    public Preferences getPreferences() {
        return appEditor.getAppPreferences();
    }

    /**
     * @return the appEditor
     */
    public XBEditorApp getAppEditor() {
        return appEditor;
    }

    /**
     * @param appEditor the appEditor to set
     */
    public void setAppEditor(XBEditorApp appEditor) {
        this.appEditor = appEditor;
        setTitle(appEditor.getAppBundle().getString("Application.name"));
        setIconImage(new ImageIcon(getClass().getResource(appEditor.getAppBundle().getString("Application.icon"))).getImage());

        if (optionsDialog != null) {
            optionsDialog.setAppEditor(appEditor);
        }
    }

    private void initActions() {
        // Note: There is probably better way for clipboard action's handling
        actionMap = new ActionMap();
        cutAction = new PassingTextAction(new DefaultEditorKit.CutAction());
        cutAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/cut.png")));
        cutAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        cutAction.putValue(Action.NAME, resourceBundle.getString("actionEditCut.Action.text"));
        cutAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditCut.Action.shortDescription"));
        cutAction.setEnabled(false);
        actionMap.put(TransferHandler.getCutAction().getValue(Action.NAME), cutAction);

        copyAction = new PassingTextAction(new DefaultEditorKit.CopyAction());
        copyAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/copy.png")));
        copyAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copyAction.putValue(Action.NAME, resourceBundle.getString("actionEditCopy.Action.text"));
        copyAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditCopy.Action.shortDescription"));
        copyAction.setEnabled(false);
        actionMap.put(TransferHandler.getCopyAction().getValue(Action.NAME), copyAction);

        pasteAction = new PassingTextAction(new DefaultEditorKit.PasteAction());
        pasteAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/paste.png")));
        pasteAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        pasteAction.putValue(Action.NAME, resourceBundle.getString("actionEditPaste.Action.text"));
        pasteAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditPaste.Action.shortDescription"));
        pasteAction.setEnabled(false);
        actionMap.put(TransferHandler.getPasteAction().getValue(Action.NAME), pasteAction);

        deleteAction = new PassingTextAction(new TextAction("delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object src = actionFocusOwner;

                if (src instanceof JTextComponent) {
                    invokeTextAction((JTextComponent) src, DefaultEditorKit.deleteNextCharAction);
                }
            }
        });
        deleteAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/delete.png")));
        deleteAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        deleteAction.putValue(Action.NAME, resourceBundle.getString("actionEditDelete.Action.text"));
        deleteAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditDelete.Action.shortDescription"));
        deleteAction.setEnabled(false);
        actionMap.put("delete", deleteAction);

        selectAllAction = new PassingTextAction(new TextAction("selectAll") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object src = actionFocusOwner;

                if (src instanceof JTextComponent) {
                    invokeTextAction((JTextComponent) src, DefaultEditorKit.selectAllAction);
                }
            }
        });
        selectAllAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        selectAllAction.putValue(Action.NAME, resourceBundle.getString("actionEditSelectAll.Action.text"));
        selectAllAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditSelectAll.Action.shortDescription"));
        actionMap.put("selectAll", selectAllAction);

        undoAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionEditUndo();
            }
        };
        undoAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/Undo16.gif")));
        undoAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        undoAction.putValue(Action.NAME, resourceBundle.getString("actionEditUndo.Action.text"));
        undoAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditUndo.Action.shortDescription"));
        undoAction.setEnabled(false);
        actionMap.put("undo", undoAction);

        redoAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionEditRedo();
            }
        };
        redoAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/Redo16.gif")));
        redoAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        redoAction.putValue(Action.NAME, resourceBundle.getString("actionEditRedo.Action.text"));
        redoAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditRedo.Action.shortDescription"));
        redoAction.setEnabled(false);
        actionMap.put("redo", redoAction);

        textComponentCaretListener = new TextComponentCaretListener();
        textComponentPCL = new TextComponentPCL();
        getClipboard().addFlavorListener(new ClipboardListener());

        // Note: It would be better to mix together both approaches
        KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kfm.addPropertyChangeListener("permanentFocusOwner", new KeyboardFocusPCL());

        initDefaultPopupMenu();
    }

    public void initDefaultPopupMenu() {
        defaultTextActionMap = new ActionMap();
        defaultCutAction = new DefaultPopupClipboardAction(DefaultEditorKit.cutAction) {
            @Override
            public void actionPerformed(ActionEvent e) {
                clipboardHandler.cut();
            }

            @Override
            protected void postTextComponentInitialize() {
                setEnabled(clipboardHandler.isEditable() && clipboardHandler.isSelection());
            }
        };
        defaultCutAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/cut.png")));
        defaultCutAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        defaultCutAction.putValue(Action.NAME, resourceBundle.getString("actionEditCut.Action.text"));
        defaultCutAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditCut.Action.shortDescription"));
        defaultCutAction.setEnabled(false);
        defaultTextActionMap.put(TransferHandler.getCutAction().getValue(Action.NAME), defaultCutAction);

        defaultCopyAction = new DefaultPopupClipboardAction(DefaultEditorKit.copyAction) {
            @Override
            public void actionPerformed(ActionEvent e) {
                clipboardHandler.copy();
            }

            @Override
            protected void postTextComponentInitialize() {
                setEnabled(clipboardHandler.isSelection());
            }
        };
        defaultCopyAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/copy.png")));
        defaultCopyAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        defaultCopyAction.putValue(Action.NAME, resourceBundle.getString("actionEditCopy.Action.text"));
        defaultCopyAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditCopy.Action.shortDescription"));
        defaultCopyAction.setEnabled(false);
        defaultTextActionMap.put(TransferHandler.getCopyAction().getValue(Action.NAME), defaultCopyAction);

        defaultPasteAction = new DefaultPopupClipboardAction(DefaultEditorKit.pasteAction) {
            @Override
            public void actionPerformed(ActionEvent e) {
                clipboardHandler.paste();
            }

            @Override
            protected void postTextComponentInitialize() {
                setEnabled(clipboardHandler.isEditable());
            }
        };
        defaultPasteAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/paste.png")));
        defaultPasteAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        defaultPasteAction.putValue(Action.NAME, resourceBundle.getString("actionEditPaste.Action.text"));
        defaultPasteAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditPaste.Action.shortDescription"));
        defaultPasteAction.setEnabled(false);
        defaultTextActionMap.put(TransferHandler.getPasteAction().getValue(Action.NAME), defaultPasteAction);

        defaultDeleteAction = new DefaultPopupClipboardAction(DefaultEditorKit.deleteNextCharAction) {
            @Override
            public void actionPerformed(ActionEvent e) {
                clipboardHandler.delete();
            }

            @Override
            protected void postTextComponentInitialize() {
                setEnabled(clipboardHandler.isEditable() && clipboardHandler.isSelection());
            }
        };
        defaultDeleteAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/delete.png")));
        defaultDeleteAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        defaultDeleteAction.putValue(Action.NAME, resourceBundle.getString("actionEditDelete.Action.text"));
        defaultDeleteAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditDelete.Action.shortDescription"));
        defaultDeleteAction.setEnabled(false);
        defaultTextActionMap.put("delete", defaultDeleteAction);

        defaultSelectAllAction = new DefaultPopupClipboardAction("Select all") {
            @Override
            public void actionPerformed(ActionEvent e) {
                clipboardHandler.selectAll();
            }

            @Override
            protected void postTextComponentInitialize() {
                setEnabled(clipboardHandler.canSelectAll());
            }
        };
        defaultSelectAllAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        defaultSelectAllAction.putValue(Action.NAME, resourceBundle.getString("actionEditSelectAll.Action.text"));
        defaultSelectAllAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditSelectAll.Action.shortDescription"));
        defaultTextActionMap.put("selectAll", defaultSelectAllAction);

        DefaultPopupClipboardAction[] actions = {defaultCutAction, defaultCopyAction, defaultPasteAction, defaultDeleteAction, defaultSelectAllAction};
        defaultTextActions = actions;

        Toolkit.getDefaultToolkit().getSystemEventQueue().push(new PopupEventQueue());
    }

    @Override
    public Image getFrameIcon() {
        return getIconImage();
    }

    public int initToolsMenu() {
        int position = menuBar.getComponentCount() - 2;
        menuBar.add(toolsMenu, position);
        return position;
    }

    public void setToolBarsVisibility(boolean toolBarVisible, boolean captionsVisible, boolean statusBarVisible) {
        if (viewToolBarCheckBoxMenuItem.isSelected() != toolBarVisible) {
            viewToolBarCheckBoxMenuItem.doClick();
        }

        if (viewCaptionsCheckBoxMenuItem.isSelected() != captionsVisible) {
            viewCaptionsCheckBoxMenuItem.doClick();
        }

        if (viewStatusBarCheckBoxMenuItem.isSelected() != statusBarVisible) {
            viewStatusBarCheckBoxMenuItem.doClick();
        }
    }

    @Override
    public Component getLastFocusOwner() {
        return lastFocusOwner;
    }

    @Override
    public MainFrameManagement getMainFrameManagement() {
        return this;
    }

    public class PassingTextAction extends TextAction {

        private final TextAction parentAction;

        public PassingTextAction(TextAction parentAction) {
            super((String) parentAction.getValue(NAME));
            this.parentAction = parentAction;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (activePanel instanceof ActivePanelActionHandling) {
                ActivePanelActionHandling childHandling = (ActivePanelActionHandling) activePanel;
                if (childHandling.performAction((String) parentAction.getValue(NAME), actionEvent)) {
                    return;
                }
            }

            parentAction.actionPerformed(actionEvent);
        }
    }

    public String getFrameTitle() {
        return appEditor.getAppBundle().getString("Application.name");
    }

    public class PopupEventQueue extends EventQueue {

        @Override
        protected void dispatchEvent(AWTEvent event) {
            if (event.getID() == MouseEvent.MOUSE_RELEASED) {
                MouseEvent e = (MouseEvent) event;
                Component c = getSource(e);

                if (c instanceof JTextComponent) {
                    if ((SwingUtilities.isRightMouseButton(e)) && (((JTextComponent) c).getComponentPopupMenu() == null)) {
                        TextComponentClipboardHandler clipboardHandler = new TextComponentClipboardHandler((JTextComponent) c);
                        for (Object action : defaultTextActions) {
                            ((DefaultPopupClipboardAction) action).setClipboardHandler(clipboardHandler);
                        }

                        defaultPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                } else if (c instanceof JList) {
                    if ((SwingUtilities.isRightMouseButton(e)) && (((JList) c).getComponentPopupMenu() == null)) {
                        ListClipboardHandler clipboardHandler = new ListClipboardHandler((JList) c);
                        for (Object action : defaultTextActions) {
                            ((DefaultPopupClipboardAction) action).setClipboardHandler(clipboardHandler);
                        }

                        defaultPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                } else if (c instanceof JTable) {
                    if ((SwingUtilities.isRightMouseButton(e)) && (((JTable) c).getComponentPopupMenu() == null)) {
                        TableClipboardHandler clipboardHandler = new TableClipboardHandler((JTable) c);
                        for (Object action : defaultTextActions) {
                            ((DefaultPopupClipboardAction) action).setClipboardHandler(clipboardHandler);
                        }

                        defaultPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }

            }

            super.dispatchEvent(event);
        }

        private Component getSource(MouseEvent e) {
            return SwingUtilities.getDeepestComponentAt(e.getComponent(), e.getX(), e.getY());
        }

        private class TextComponentClipboardHandler implements ComponentClipboardHandler {

            private final JTextComponent txtComp;

            public TextComponentClipboardHandler(JTextComponent txtComp) {
                this.txtComp = txtComp;
            }

            @Override
            public void cut() {
                txtComp.cut();
            }

            @Override
            public void copy() {
                txtComp.copy();
            }

            @Override
            public void paste() {
                txtComp.paste();
            }

            @Override
            public void delete() {
                invokeTextAction(txtComp, DefaultEditorKit.deleteNextCharAction);
            }

            @Override
            public void selectAll() {
                txtComp.requestFocus();
                txtComp.selectAll();
            }

            @Override
            public boolean isSelection() {
                return txtComp.isEnabled() && txtComp.getSelectionStart() != txtComp.getSelectionEnd();
            }

            @Override
            public boolean isEditable() {
                return txtComp.isEnabled() && txtComp.isEditable();
            }

            @Override
            public boolean canSelectAll() {
                return txtComp.isEnabled() && !txtComp.getText().isEmpty();
            }
        }

        private class ListClipboardHandler implements ComponentClipboardHandler {

            private final JList listComp;

            public ListClipboardHandler(JList listComp) {
                this.listComp = listComp;
            }

            @Override
            public void cut() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void copy() {
                StringBuilder builder = new StringBuilder();
                List<String> rows = listComp.getSelectedValuesList();
                boolean empty = true;
                for (String row : rows) {
                    builder.append(empty ? row : System.getProperty("line.separator") + row);

                    if (empty) {
                        empty = false;
                    }
                }

                getClipboard().setContents(new StringSelection(builder.toString()), null);
            }

            @Override
            public void paste() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void delete() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void selectAll() {
                if (listComp.getModel().getSize() > 0) {
                    listComp.setSelectionInterval(0, listComp.getModel().getSize() - 1);
                }
            }

            @Override
            public boolean isSelection() {
                return listComp.isEnabled() && !listComp.isSelectionEmpty();
            }

            @Override
            public boolean isEditable() {
                return false;
            }

            @Override
            public boolean canSelectAll() {
                return listComp.isEnabled() && listComp.getSelectionMode() != DefaultListSelectionModel.SINGLE_SELECTION;
            }
        }

        private class TableClipboardHandler implements ComponentClipboardHandler {

            private final JTable tableComp;

            public TableClipboardHandler(JTable tableComp) {
                this.tableComp = tableComp;
            }

            @Override
            public void cut() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void copy() {
                StringBuilder builder = new StringBuilder();
                int[] rows = tableComp.getSelectedRows();
                int[] columns;
                if (tableComp.getSelectionModel().getSelectionMode() == ListSelectionModel.SINGLE_SELECTION) {
                    columns = new int[tableComp.getColumnCount()];
                    for (int i = 0; i < tableComp.getColumnCount(); i++) {
                        columns[i] = i;
                    }
                } else {
                    columns = tableComp.getSelectedColumns();
                }

                boolean empty = true;
                for (int rowIndex : rows) {
                    if (!empty) {
                        builder.append(System.getProperty("line.separator"));
                    } else {
                        empty = false;
                    }

                    boolean columnEmpty = true;
                    for (int columnIndex : columns) {
                        if (!columnEmpty) {
                            builder.append("\t");
                        } else {
                            columnEmpty = false;
                        }

                        Object value = tableComp.getModel().getValueAt(rowIndex, columnIndex);
                        if (value != null) {
                            builder.append(value.toString());
                        }
                    }
                }

                getClipboard().setContents(new StringSelection(builder.toString()), null);
            }

            @Override
            public void paste() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void delete() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void selectAll() {
                tableComp.selectAll();
            }

            @Override
            public boolean isSelection() {
                return tableComp.isEnabled() && (tableComp.getSelectedColumn() >= 0 || tableComp.getSelectedRow() >= 0);
            }

            @Override
            public boolean isEditable() {
                return false;
            }

            @Override
            public boolean canSelectAll() {
                return tableComp.isEnabled();
            }
        }
    }

    /**
     * Clipboard action for default popup menu.
     */
    private static abstract class DefaultPopupClipboardAction extends AbstractAction {

        protected ComponentClipboardHandler clipboardHandler;

        public DefaultPopupClipboardAction(String name) {
            super(name);
        }

        public void setClipboardHandler(ComponentClipboardHandler clipboardHandler) {
            this.clipboardHandler = clipboardHandler;
            postTextComponentInitialize();
        }

        protected abstract void postTextComponentInitialize();
    }

    /**
     * @return the actionMap
     */
    @Override
    public ActionMap getActionMap() {
        return actionMap;
    }

    /**
     * @return the cutAction
     */
    @Override
    public Action getCutAction() {
        return cutAction;
    }

    /**
     * @return the copyAction
     */
    @Override
    public Action getCopyAction() {
        return copyAction;
    }

    /**
     * @return the pasteAction
     */
    @Override
    public Action getPasteAction() {
        return pasteAction;
    }

    /**
     * @return the deleteAction
     */
    @Override
    public Action getDeleteAction() {
        return deleteAction;
    }

    /**
     * @return the undoAction
     */
    @Override
    public Action getUndoAction() {
        return undoAction;
    }

    /**
     * @return the redoAction
     */
    @Override
    public Action getRedoAction() {
        return redoAction;
    }

    public JPopupMenu getMainPopupMenu() {
        return mainPopupMenu;
    }

    /**
     * @return the selectAllAction
     */
    @Override
    public Action getSelectAllAction() {
        return selectAllAction;
    }

    /**
     * @param selectAllAction the selectAllAction to set
     */
    public void setSelectAllAction(Action selectAllAction) {
        this.selectAllAction = selectAllAction;
    }

    private void initMenuBar() {
        // Show hints in status bar for main menu and popup menu
        for (int i = 0; i < menuBar.getComponentCount(); i++) {
            JMenu menu = (JMenu) menuBar.getComponent(i);
            initMenu(menu);
        }
    }

    public void initPopupMenu(JPopupMenu popupMenu) {
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                ((CardLayout) statusPanel.getLayout()).show(statusPanel, "main");
                statusLabel.setText(((JPopupMenu) e.getSource()).getToolTipText());
                statusLabel.setForeground(Color.BLACK);
                ToolTipManager.sharedInstance().setEnabled(false);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                ((CardLayout) statusPanel.getLayout()).show(statusPanel, activeStatusPanel);
                ToolTipManager.sharedInstance().setEnabled(true);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        initSubmenu(popupMenu);
    }

    public class AllFilesFilter extends FileFilter implements FileType {

        @Override
        public boolean accept(File f) {
            return true;
        }

        @Override
        public String getDescription() {
            return resourceBundle.getString("filter_file_all");
        }

        @Override
        public String getFileTypeId() {
            return ALLFILESFILTER;
        }
    }

    /**
     * @return the activePanel
     */
    public JPanel getActivePanel() {
        return activePanel;
    }

    public void addStatusPanel(JPanel panel, String panelName) {
        statusPanel.add(panel, "_" + panelName);
        setStatusPanel(panelName);
    }

    public void setStatusPanel(String panelName) {
        ((CardLayout) statusPanel.getLayout()).show(statusPanel, "_" + panelName);
        activeStatusPanel = "_" + panelName;
    }

    public ApplicationPanel getSpecificPanel(int i) {
        // TODO: This is temporary stub
        return (ApplicationPanel) mainTabbedPane.getComponentAt(i);
    }

    /**
     * Try to release and warn if document was modified
     *
     * @return true if successfull
     */
    private boolean releaseFile() {
        if (!(activePanel instanceof ApplicationFilePanel)) {
            return true;
        }
        while (((ApplicationFilePanel) activePanel).isModified()) {
            Object[] options = {
                resourceBundle.getString("Question.modified_save"),
                resourceBundle.getString("Question.modified_discard"),
                resourceBundle.getString("Question.modified_cancel")
            };
            int result = JOptionPane.showOptionDialog(this,
                    resourceBundle.getString("Question.modified"),
                    resourceBundle.getString("Question.modified_title"),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);
            if (result == JOptionPane.NO_OPTION) {
                return true;
            }
            if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                return false;
            }

            actionFileSave();
        }
        return true;
    }

    /**
     * Ask whether it's allows to overwrite file.
     *
     * @return true if allowed
     */
    private boolean overwriteFile() {
        Object[] options = {
            resourceBundle.getString("Question.overwrite_save"),
            resourceBundle.getString("Question.modified_cancel")
        };
        int result = JOptionPane.showOptionDialog(
                this,
                resourceBundle.getString("Question.overwrite"),
                resourceBundle.getString("Question.overwrite_title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        if (result == JOptionPane.YES_OPTION) {
            return true;
        }
        if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION) {
            return false;
        }
        return false;
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

    public FileType newAllFilesFilter() {
        return (FileType) new AllFilesFilter();
    }

    /**
     * Class for representation of recently opened or saved files.
     */
    public class RecentItem {

        private String path;
        private String moduleName;
        private String fileMode;

        public RecentItem(String path, String moduleName, String fileMode) {
            this.path = path;
            this.moduleName = moduleName;
            this.fileMode = fileMode;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getFileMode() {
            return fileMode;
        }

        public void setFileMode(String fileMode) {
            this.fileMode = fileMode;
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPopupMenu = new javax.swing.JPopupMenu();
        popupUndoMenuItem = new javax.swing.JMenuItem();
        popupRedoMenuItem = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JSeparator();
        popupCutMenuItem = new javax.swing.JMenuItem();
        popupCopyMenuItem = new javax.swing.JMenuItem();
        popupPasteMenuItem = new javax.swing.JMenuItem();
        popupDeleteMenuItem = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        popupSelectAllMenuItem = new javax.swing.JMenuItem();
        mainTabbedPane = new javax.swing.JTabbedPane();
        defaultPopupMenu = new javax.swing.JPopupMenu();
        basicPopupCutMenuItem = new javax.swing.JMenuItem();
        basicPopupCopyMenuItem = new javax.swing.JMenuItem();
        basicPopupPasteMenuItem = new javax.swing.JMenuItem();
        basicPopupDeleteMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        basicPopupSelectAllMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        toolBar = new javax.swing.JToolBar();
        toolbarFileNewButton = new javax.swing.JButton();
        toolbarFileOpenButton = new javax.swing.JButton();
        toolbarFileSaveButton = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        toolbarEditUndoButton = new javax.swing.JButton();
        toolbarEditRedoButton = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        toolbarEditCutButton = new javax.swing.JButton();
        toolbarEditCopyButton = new javax.swing.JButton();
        toolbarEditPasteButton = new javax.swing.JButton();
        toolbarEditDeleteButton = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JToolBar.Separator();
        statusBar = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusPanel = new javax.swing.JPanel();
        emptyStatusPanel = new javax.swing.JPanel();
        mainStatusPanel = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();
        progressStatusPanel = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton4 = new javax.swing.JButton();
        busyStatusPanel = new javax.swing.JPanel();
        jProgressBar2 = new javax.swing.JProgressBar();
        mainPanel = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        fileNewMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem fileOpenMenuItem = new javax.swing.JMenuItem();
        fileOpenRecentMenu = new javax.swing.JMenu();
        fileSaveMenuItem = new javax.swing.JMenuItem();
        fileSaveAsMenuItem = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        editUndoMenuItem = new javax.swing.JMenuItem();
        editRedoMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        editCutMenuItem = new javax.swing.JMenuItem();
        editCopyMenuItem = new javax.swing.JMenuItem();
        editPasteMenuItem = new javax.swing.JMenuItem();
        editDeleteMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        editSelectAllMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        viewToolBarCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        viewCaptionsCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        viewStatusBarCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        optionsMenu = new javax.swing.JMenu();
        toolsOptionsMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        mainPopupMenu.setName("mainPopupMenu"); // NOI18N

        popupUndoMenuItem.setAction(getUndoAction());
        popupUndoMenuItem.setName("popupUndoMenuItem"); // NOI18N
        mainPopupMenu.add(popupUndoMenuItem);

        popupRedoMenuItem.setAction(getRedoAction());
        popupRedoMenuItem.setName("popupRedoMenuItem"); // NOI18N
        mainPopupMenu.add(popupRedoMenuItem);

        jSeparator10.setName("jSeparator10"); // NOI18N
        mainPopupMenu.add(jSeparator10);

        popupCutMenuItem.setAction(getCutAction());
        popupCutMenuItem.setName("popupCutMenuItem"); // NOI18N
        mainPopupMenu.add(popupCutMenuItem);

        popupCopyMenuItem.setAction(getCopyAction());
        popupCopyMenuItem.setName("popupCopyMenuItem"); // NOI18N
        mainPopupMenu.add(popupCopyMenuItem);

        popupPasteMenuItem.setAction(getPasteAction());
        popupPasteMenuItem.setName("popupPasteMenuItem"); // NOI18N
        mainPopupMenu.add(popupPasteMenuItem);

        popupDeleteMenuItem.setAction(getDeleteAction());
        popupDeleteMenuItem.setName("popupDeleteMenuItem"); // NOI18N
        mainPopupMenu.add(popupDeleteMenuItem);

        jSeparator6.setName("jSeparator6"); // NOI18N
        mainPopupMenu.add(jSeparator6);

        popupSelectAllMenuItem.setAction(getSelectAllAction());
        popupSelectAllMenuItem.setName("popupSelectAllMenuItem"); // NOI18N
        mainPopupMenu.add(popupSelectAllMenuItem);

        JMenuItem[] popupItems = { };
        for (JMenuItem item : popupItems) {
            item.setText(item.getText()+DIALOG_MENU_SUFIX);
        }

        mainTabbedPane.setName("mainTabbedPane"); // NOI18N

        defaultPopupMenu.setName("defaultPopupMenu"); // NOI18N

        basicPopupCutMenuItem.setAction(defaultCutAction);
        basicPopupCutMenuItem.setName("basicPopupCutMenuItem"); // NOI18N
        defaultPopupMenu.add(basicPopupCutMenuItem);

        basicPopupCopyMenuItem.setAction(defaultCopyAction);
        basicPopupCopyMenuItem.setName("basicPopupCopyMenuItem"); // NOI18N
        defaultPopupMenu.add(basicPopupCopyMenuItem);

        basicPopupPasteMenuItem.setAction(defaultPasteAction);
        basicPopupPasteMenuItem.setName("basicPopupPasteMenuItem"); // NOI18N
        defaultPopupMenu.add(basicPopupPasteMenuItem);

        basicPopupDeleteMenuItem.setAction(defaultDeleteAction);
        basicPopupDeleteMenuItem.setName("basicPopupDeleteMenuItem"); // NOI18N
        defaultPopupMenu.add(basicPopupDeleteMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        defaultPopupMenu.add(jSeparator1);

        basicPopupSelectAllMenuItem.setAction(defaultSelectAllAction);
        basicPopupSelectAllMenuItem.setName("basicPopupSelectAllMenuItem"); // NOI18N
        defaultPopupMenu.add(basicPopupSelectAllMenuItem);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/tool/editor/module/frame/resources/MainFrame"); // NOI18N
        toolsMenu.setText(bundle.getString("toolsMenu.text")); // NOI18N
        toolsMenu.setName("toolsMenu"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setEnabled(false);
        toolBar.setName("toolBar"); // NOI18N

        toolbarFileNewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/New16.gif"))); // NOI18N
        toolbarFileNewButton.setText(bundle.getString("actionFileNew.Action.text")); // NOI18N
        toolbarFileNewButton.setToolTipText(bundle.getString("actionFileNew.Action.shortDescription")); // NOI18N
        toolbarFileNewButton.setFocusable(false);
        toolbarFileNewButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolbarFileNewButton.setName("toolbarFileNewButton"); // NOI18N
        toolbarFileNewButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbarFileNewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolbarFileNewButtonActionPerformed(evt);
            }
        });
        toolBar.add(toolbarFileNewButton);

        toolbarFileOpenButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/Open16.gif"))); // NOI18N
        toolbarFileOpenButton.setText(bundle.getString("actionFileOpen.Action.text")); // NOI18N
        toolbarFileOpenButton.setToolTipText(bundle.getString("actionFileOpen.Action.shortDescription")); // NOI18N
        toolbarFileOpenButton.setFocusable(false);
        toolbarFileOpenButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolbarFileOpenButton.setName("toolbarFileOpenButton"); // NOI18N
        toolbarFileOpenButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbarFileOpenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolbarFileOpenButtonActionPerformed(evt);
            }
        });
        toolBar.add(toolbarFileOpenButton);

        toolbarFileSaveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/Save16.gif"))); // NOI18N
        toolbarFileSaveButton.setText(bundle.getString("actionFileSave.Action.text")); // NOI18N
        toolbarFileSaveButton.setToolTipText(bundle.getString("actionFileSave.Action.shortDescription")); // NOI18N
        toolbarFileSaveButton.setFocusable(false);
        toolbarFileSaveButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolbarFileSaveButton.setName("toolbarFileSaveButton"); // NOI18N
        toolbarFileSaveButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbarFileSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolbarFileSaveButtonActionPerformed(evt);
            }
        });
        toolBar.add(toolbarFileSaveButton);

        jSeparator5.setName("jSeparator5"); // NOI18N
        toolBar.add(jSeparator5);

        toolbarEditUndoButton.setAction(getUndoAction());
        toolbarEditUndoButton.setFocusable(false);
        toolbarEditUndoButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolbarEditUndoButton.setName("toolbarEditUndoButton"); // NOI18N
        toolbarEditUndoButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(toolbarEditUndoButton);

        toolbarEditRedoButton.setAction(getRedoAction());
        toolbarEditRedoButton.setFocusable(false);
        toolbarEditRedoButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolbarEditRedoButton.setName("toolbarEditRedoButton"); // NOI18N
        toolbarEditRedoButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(toolbarEditRedoButton);

        jSeparator4.setName("jSeparator4"); // NOI18N
        toolBar.add(jSeparator4);

        toolbarEditCutButton.setAction(getCutAction());
        toolbarEditCutButton.setFocusable(false);
        toolbarEditCutButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolbarEditCutButton.setName("toolbarEditCutButton"); // NOI18N
        toolbarEditCutButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(toolbarEditCutButton);

        toolbarEditCopyButton.setAction(getCopyAction());
        toolbarEditCopyButton.setFocusable(false);
        toolbarEditCopyButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolbarEditCopyButton.setName("toolbarEditCopyButton"); // NOI18N
        toolbarEditCopyButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(toolbarEditCopyButton);

        toolbarEditPasteButton.setAction(getPasteAction());
        toolbarEditPasteButton.setFocusable(false);
        toolbarEditPasteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolbarEditPasteButton.setName("toolbarEditPasteButton"); // NOI18N
        toolbarEditPasteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(toolbarEditPasteButton);

        toolbarEditDeleteButton.setAction(getDeleteAction());
        toolbarEditDeleteButton.setFocusable(false);
        toolbarEditDeleteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolbarEditDeleteButton.setName("toolbarEditDeleteButton"); // NOI18N
        toolbarEditDeleteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(toolbarEditDeleteButton);

        jSeparator11.setName("jSeparator11"); // NOI18N
        toolBar.add(jSeparator11);

        getContentPane().add(toolBar, java.awt.BorderLayout.NORTH);

        statusBar.setName("statusBar"); // NOI18N
        statusBar.setLayout(new java.awt.BorderLayout());

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N
        statusBar.add(statusPanelSeparator, java.awt.BorderLayout.NORTH);

        statusPanel.setName("statusPanel"); // NOI18N
        statusPanel.setPreferredSize(new java.awt.Dimension(649, 26));
        statusPanel.setRequestFocusEnabled(false);
        statusPanel.setLayout(new java.awt.CardLayout());

        emptyStatusPanel.setName("emptyStatusPanel"); // NOI18N

        javax.swing.GroupLayout emptyStatusPanelLayout = new javax.swing.GroupLayout(emptyStatusPanel);
        emptyStatusPanel.setLayout(emptyStatusPanelLayout);
        emptyStatusPanelLayout.setHorizontalGroup(
            emptyStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 675, Short.MAX_VALUE)
        );
        emptyStatusPanelLayout.setVerticalGroup(
            emptyStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 26, Short.MAX_VALUE)
        );

        statusPanel.add(emptyStatusPanel, "default");

        mainStatusPanel.setName("mainStatusPanel"); // NOI18N
        mainStatusPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        statusLabel.setName("statusLabel"); // NOI18N
        mainStatusPanel.add(statusLabel);

        statusPanel.add(mainStatusPanel, "main");

        progressStatusPanel.setName("progressStatusPanel"); // NOI18N

        jProgressBar1.setIndeterminate(true);
        jProgressBar1.setMinimumSize(new java.awt.Dimension(10, 10));
        jProgressBar1.setName("jProgressBar1"); // NOI18N
        jProgressBar1.setRequestFocusEnabled(false);
        jProgressBar1.setStringPainted(true);

        jButton4.setText("Stop");
        jButton4.setEnabled(false);
        jButton4.setMinimumSize(new java.awt.Dimension(67, 15));
        jButton4.setName("jButton4"); // NOI18N
        jButton4.setPreferredSize(new java.awt.Dimension(75, 20));

        javax.swing.GroupLayout progressStatusPanelLayout = new javax.swing.GroupLayout(progressStatusPanel);
        progressStatusPanel.setLayout(progressStatusPanelLayout);
        progressStatusPanelLayout.setHorizontalGroup(
            progressStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, progressStatusPanelLayout.createSequentialGroup()
                .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        progressStatusPanelLayout.setVerticalGroup(
            progressStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progressStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE))
        );

        statusPanel.add(progressStatusPanel, "initCat");

        busyStatusPanel.setName("busyStatusPanel"); // NOI18N

        jProgressBar2.setIndeterminate(true);
        jProgressBar2.setName("jProgressBar2"); // NOI18N
        jProgressBar2.setRequestFocusEnabled(false);
        jProgressBar2.setStringPainted(true);

        javax.swing.GroupLayout busyStatusPanelLayout = new javax.swing.GroupLayout(busyStatusPanel);
        busyStatusPanel.setLayout(busyStatusPanelLayout);
        busyStatusPanelLayout.setHorizontalGroup(
            busyStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jProgressBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
        );
        busyStatusPanelLayout.setVerticalGroup(
            busyStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jProgressBar2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        statusPanel.add(busyStatusPanel, "busy");

        statusBar.add(statusPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setLayout(new java.awt.BorderLayout());
        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(bundle.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        fileNewMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/New16.gif"))); // NOI18N
        fileNewMenuItem.setText(bundle.getString("actionFileNew.Action.text")); // NOI18N
        fileNewMenuItem.setToolTipText(bundle.getString("actionFileNew.Action.shortDescription")); // NOI18N
        fileNewMenuItem.setName("fileNewMenuItem"); // NOI18N
        fileNewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileNewMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(fileNewMenuItem);

        fileOpenMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        fileOpenMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/Open16.gif"))); // NOI18N
        fileOpenMenuItem.setText(bundle.getString("actionFileOpen.Action.text")); // NOI18N
        fileOpenMenuItem.setToolTipText(bundle.getString("actionFileOpen.Action.shortDescription")); // NOI18N
        fileOpenMenuItem.setName("fileOpenMenuItem"); // NOI18N
        fileOpenMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileOpenMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(fileOpenMenuItem);

        fileOpenRecentMenu.setText(bundle.getString("fileOpenRecentMenu.text")); // NOI18N
        fileOpenRecentMenu.setToolTipText(bundle.getString("fileOpenRecentMenu.toolTipText")); // NOI18N
        fileOpenRecentMenu.setEnabled(false);
        fileOpenRecentMenu.setName("fileOpenRecentMenu"); // NOI18N
        fileMenu.add(fileOpenRecentMenu);

        fileSaveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        fileSaveMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/Save16.gif"))); // NOI18N
        fileSaveMenuItem.setText(bundle.getString("actionFileSave.Action.text")); // NOI18N
        fileSaveMenuItem.setToolTipText(bundle.getString("actionFileSave.Action.shortDescription")); // NOI18N
        fileSaveMenuItem.setName("fileSaveMenuItem"); // NOI18N
        fileSaveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileSaveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(fileSaveMenuItem);

        fileSaveAsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        fileSaveAsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/SaveAs16.gif"))); // NOI18N
        fileSaveAsMenuItem.setText(bundle.getString("actionFileSaveAs.Action.text")); // NOI18N
        fileSaveAsMenuItem.setToolTipText(bundle.getString("actionHelpAbout.Action.shortDescription")); // NOI18N
        fileSaveAsMenuItem.setName("fileSaveAsMenuItem"); // NOI18N
        fileSaveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileSaveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(fileSaveAsMenuItem);

        jSeparator9.setName("jSeparator9"); // NOI18N
        fileMenu.add(jSeparator9);

        exitMenuItem.setText(bundle.getString("actionExit.Action.text")); // NOI18N
        exitMenuItem.setToolTipText(bundle.getString("actionExit.Action.shortDescription")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText(bundle.getString("editMenu.text")); // NOI18N
        editMenu.setName("editMenu"); // NOI18N

        editUndoMenuItem.setAction(getUndoAction());
        editUndoMenuItem.setName("editUndoMenuItem"); // NOI18N
        editMenu.add(editUndoMenuItem);

        editRedoMenuItem.setAction(getRedoAction());
        editRedoMenuItem.setName("editRedoMenuItem"); // NOI18N
        editMenu.add(editRedoMenuItem);

        jSeparator2.setName("jSeparator2"); // NOI18N
        editMenu.add(jSeparator2);

        editCutMenuItem.setAction(getCutAction());
        editCutMenuItem.setName("editCutMenuItem"); // NOI18N
        editMenu.add(editCutMenuItem);

        editCopyMenuItem.setAction(getCopyAction());
        editCopyMenuItem.setName("editCopyMenuItem"); // NOI18N
        editMenu.add(editCopyMenuItem);

        editPasteMenuItem.setAction(getPasteAction());
        editPasteMenuItem.setName("editPasteMenuItem"); // NOI18N
        editMenu.add(editPasteMenuItem);

        editDeleteMenuItem.setAction(getDeleteAction());
        editDeleteMenuItem.setName("editDeleteMenuItem"); // NOI18N
        editMenu.add(editDeleteMenuItem);

        jSeparator3.setName("jSeparator3"); // NOI18N
        editMenu.add(jSeparator3);

        editSelectAllMenuItem.setAction(getSelectAllAction());
        editSelectAllMenuItem.setName("editSelectAllMenuItem"); // NOI18N
        editMenu.add(editSelectAllMenuItem);

        menuBar.add(editMenu);

        viewMenu.setText(bundle.getString("viewMenu.text")); // NOI18N
        viewMenu.setName("viewMenu"); // NOI18N

        viewToolBarCheckBoxMenuItem.setSelected(true);
        viewToolBarCheckBoxMenuItem.setText(bundle.getString("actionViewToolbar.Action.text")); // NOI18N
        viewToolBarCheckBoxMenuItem.setToolTipText(bundle.getString("actionViewToolbar.Action.shortDescription")); // NOI18N
        viewToolBarCheckBoxMenuItem.setName("viewToolBarCheckBoxMenuItem"); // NOI18N
        viewToolBarCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewToolBarCheckBoxMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(viewToolBarCheckBoxMenuItem);

        viewCaptionsCheckBoxMenuItem.setSelected(true);
        viewCaptionsCheckBoxMenuItem.setText(bundle.getString("actionViewCaptions.Action.text")); // NOI18N
        viewCaptionsCheckBoxMenuItem.setToolTipText(bundle.getString("actionViewStatusBar.Action.shortDescription")); // NOI18N
        viewCaptionsCheckBoxMenuItem.setName("viewCaptionsCheckBoxMenuItem"); // NOI18N
        viewCaptionsCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewCaptionsCheckBoxMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(viewCaptionsCheckBoxMenuItem);

        viewStatusBarCheckBoxMenuItem.setSelected(true);
        viewStatusBarCheckBoxMenuItem.setText(bundle.getString("actionViewStatusBar.Action.text")); // NOI18N
        viewStatusBarCheckBoxMenuItem.setToolTipText(bundle.getString("actionViewStatusBar.Action.shortDescription")); // NOI18N
        viewStatusBarCheckBoxMenuItem.setName("viewStatusBarCheckBoxMenuItem"); // NOI18N
        viewStatusBarCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewStatusBarCheckBoxMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(viewStatusBarCheckBoxMenuItem);

        menuBar.add(viewMenu);

        optionsMenu.setText(bundle.getString("optionMenu.text")); // NOI18N
        optionsMenu.setName("optionsMenu"); // NOI18N

        toolsOptionsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/frame/resources/images/actions/Preferences16.gif"))); // NOI18N
        toolsOptionsMenuItem.setText(bundle.getString("actionToolsOptions.Action.text")); // NOI18N
        toolsOptionsMenuItem.setToolTipText(bundle.getString("actionToolsOptions.Action.shortDescription")); // NOI18N
        toolsOptionsMenuItem.setName("toolsOptionsMenuItem"); // NOI18N
        toolsOptionsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolsOptionsMenuItemActionPerformed(evt);
            }
        });
        optionsMenu.add(toolsOptionsMenuItem);

        menuBar.add(optionsMenu);

        helpMenu.setText(bundle.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setText(bundle.getString("actionHelpAbout.Action.text")); // NOI18N
        aboutMenuItem.setToolTipText(bundle.getString("actionHelpAbout.Action.shortDescription")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        for (JMenuItem item : Arrays.asList(aboutMenuItem, fileOpenMenuItem, fileSaveAsMenuItem, toolsOptionsMenuItem) ) {
            item.setText(item.getText()+DIALOG_MENU_SUFIX);
        }

        setJMenuBar(menuBar);

        setSize(new java.awt.Dimension(685, 524));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void fileNewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileNewMenuItemActionPerformed
        actionFileNew();
    }//GEN-LAST:event_fileNewMenuItemActionPerformed

    private void fileOpenMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileOpenMenuItemActionPerformed
        actionFileOpen();
    }//GEN-LAST:event_fileOpenMenuItemActionPerformed

    private void fileSaveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileSaveMenuItemActionPerformed
        actionFileSave();
    }//GEN-LAST:event_fileSaveMenuItemActionPerformed

    private void fileSaveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileSaveAsMenuItemActionPerformed
        actionFileSaveAs();
    }//GEN-LAST:event_fileSaveAsMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        actionExit();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void toolbarFileNewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolbarFileNewButtonActionPerformed
        actionFileNew();
    }//GEN-LAST:event_toolbarFileNewButtonActionPerformed

    private void toolbarFileOpenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolbarFileOpenButtonActionPerformed
        actionFileOpen();
    }//GEN-LAST:event_toolbarFileOpenButtonActionPerformed

    private void toolbarFileSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolbarFileSaveButtonActionPerformed
        actionFileSave();
    }//GEN-LAST:event_toolbarFileSaveButtonActionPerformed

    private void viewToolBarCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewToolBarCheckBoxMenuItemActionPerformed
        actionViewToolbar();
    }//GEN-LAST:event_viewToolBarCheckBoxMenuItemActionPerformed

    private void viewCaptionsCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewCaptionsCheckBoxMenuItemActionPerformed
        actionViewCaptions();
    }//GEN-LAST:event_viewCaptionsCheckBoxMenuItemActionPerformed

    private void viewStatusBarCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewStatusBarCheckBoxMenuItemActionPerformed
        actionViewStatusBar();
    }//GEN-LAST:event_viewStatusBarCheckBoxMenuItemActionPerformed

    private void toolsOptionsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolsOptionsMenuItemActionPerformed
        actionToolsOptions();
    }//GEN-LAST:event_toolsOptionsMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        actionHelpAbout();
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                XBEditorApp appEditor = new XBEditorApp() {

                    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("org/xbup/tool/editor/module/frame/resources/XBEditorMainFrame");
                    private Preferences preferences = null;
                    private ModuleRepository moduleRepository = null;

                    @Override
                    public ResourceBundle getAppBundle() {
                        return resourceBundle;
                    }

                    @Override
                    public Preferences getAppPreferences() {
                        if (preferences == null) {
                            preferences = Preferences.userNodeForPackage(MainFrame.class);
                        }

                        return preferences;
                    }

                    @Override
                    public ModuleRepository getModuleRepository() {
                        if (moduleRepository == null) {
                            moduleRepository = new ModuleRepository() {

                                @Override
                                public void addPluginsFrom(URI uri) {
                                }

                                @Override
                                public long getActiveModule() {
                                    return 0;
                                }

                                @Override
                                public ApplicationModule getPluginHandler() {
                                    return null;
                                }

                                @Override
                                public PluginManager getPluginManager() {
                                    return null;
                                }

                                @Override
                                public boolean openFile(JFileChooser openFC) {
                                    return false;
                                }

                                @Override
                                public boolean openFile(String path, String fileTypeId) {
                                    return false;
                                }

                                @Override
                                public List<ApplicationModuleInfo> getModulesList() {
                                    return new ArrayList<>();
                                }

                                @Override
                                public void newFile() {
                                }

                                @Override
                                public boolean saveFile(JFileChooser saveFC) {
                                    return false;
                                }

                                @Override
                                public boolean saveFile() {
                                    return false;
                                }
                            };
                        }

                        return moduleRepository;
                    }

                    @Override
                    public boolean isAppMode() {
                        return true;
                    }

                    @Override
                    public String preferencesGet(String key, String def) {
                        return getAppPreferences().get(key, def);
                    }

                    @Override
                    public Image getApplicationIcon() {
                        return new ImageIcon(getClass().getResource(resourceBundle.getString("Application.icon"))).getImage();
                    }
                };

                MainFrame mainFrame = new MainFrame();
                mainFrame.setAppEditor(appEditor);
                mainFrame.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem basicPopupCopyMenuItem;
    private javax.swing.JMenuItem basicPopupCutMenuItem;
    private javax.swing.JMenuItem basicPopupDeleteMenuItem;
    private javax.swing.JMenuItem basicPopupPasteMenuItem;
    private javax.swing.JMenuItem basicPopupSelectAllMenuItem;
    private javax.swing.JPanel busyStatusPanel;
    private javax.swing.JPopupMenu defaultPopupMenu;
    private javax.swing.JMenuItem editCopyMenuItem;
    private javax.swing.JMenuItem editCutMenuItem;
    private javax.swing.JMenuItem editDeleteMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem editPasteMenuItem;
    private javax.swing.JMenuItem editRedoMenuItem;
    private javax.swing.JMenuItem editSelectAllMenuItem;
    private javax.swing.JMenuItem editUndoMenuItem;
    private javax.swing.JPanel emptyStatusPanel;
    private javax.swing.JMenuItem fileNewMenuItem;
    private javax.swing.JMenu fileOpenRecentMenu;
    private javax.swing.JMenuItem fileSaveAsMenuItem;
    private javax.swing.JMenuItem fileSaveMenuItem;
    private javax.swing.JButton jButton4;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JToolBar.Separator jSeparator11;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPopupMenu mainPopupMenu;
    private javax.swing.JPanel mainStatusPanel;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu optionsMenu;
    private javax.swing.JMenuItem popupCopyMenuItem;
    private javax.swing.JMenuItem popupCutMenuItem;
    private javax.swing.JMenuItem popupDeleteMenuItem;
    private javax.swing.JMenuItem popupPasteMenuItem;
    private javax.swing.JMenuItem popupRedoMenuItem;
    private javax.swing.JMenuItem popupSelectAllMenuItem;
    private javax.swing.JMenuItem popupUndoMenuItem;
    private javax.swing.JPanel progressStatusPanel;
    private javax.swing.JPanel statusBar;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JButton toolbarEditCopyButton;
    private javax.swing.JButton toolbarEditCutButton;
    private javax.swing.JButton toolbarEditDeleteButton;
    private javax.swing.JButton toolbarEditPasteButton;
    private javax.swing.JButton toolbarEditRedoButton;
    private javax.swing.JButton toolbarEditUndoButton;
    private javax.swing.JButton toolbarFileNewButton;
    private javax.swing.JButton toolbarFileOpenButton;
    private javax.swing.JButton toolbarFileSaveButton;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JMenuItem toolsOptionsMenuItem;
    private javax.swing.JCheckBoxMenuItem viewCaptionsCheckBoxMenuItem;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JCheckBoxMenuItem viewStatusBarCheckBoxMenuItem;
    private javax.swing.JCheckBoxMenuItem viewToolBarCheckBoxMenuItem;
    // End of variables declaration//GEN-END:variables

    public void actionFileSave() {
        // TODO: Set button grayed when saved and no changes were made.
        // TODO: jButton3.setIcon(jButton3.getDisabledIcon());
        if (!appEditor.getModuleRepository().saveFile()) {
            actionFileSaveAs();
        }
    }

    public void actionFileNew() {
        if (releaseFile()) {
            appEditor.getModuleRepository().newFile();
        }
    }

    public void actionFileOpen() {
        if (!releaseFile()) {
            return;
        }

        if (openFC.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            ((CardLayout) statusPanel.getLayout()).show(statusPanel, "busy");
            statusPanel.repaint();
            if (appEditor.getModuleRepository().openFile(openFC)) {
                String fileName = openFC.getSelectedFile().getAbsolutePath();

                // Update recent files list
                int i = 0;
                while (i < recentFiles.size()) {
                    RecentItem recentItem = recentFiles.get(i);
                    if (recentItem.getPath().equals(fileName)) {
                        recentFiles.remove(i);
                    }
                    i++;
                }

                recentFiles.add(new RecentItem(fileName, "", ((FileType) openFC.getFileFilter()).getFileTypeId()));
                if (recentFiles.size() > 15) {
                    recentFiles.remove(14);
                }
                rebuildRecentFilesMenu();
            }
            ((CardLayout) statusPanel.getLayout()).show(statusPanel, activeStatusPanel);
            statusPanel.repaint();
        }
    }

    public void actionFileSaveAs() {
        if (saveFC.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (new File(saveFC.getSelectedFile().getAbsolutePath()).exists()) {
                if (!overwriteFile()) {
                    return;
                }
            }
            // TODO setFileName(saveFC.getSelectedFile().getAbsolutePath());
/*            if (saveFC.getFileFilter().getDescription().equals(resourceBundle.getString("filter_file_xbt"))) {
             activePanel.setFileMode(2);
             } else activePanel.setFileMode(1); */
            appEditor.getModuleRepository().saveFile(saveFC);
        }
    }

    private void actionExit() {
        if (releaseFile()) {
            Preferences preferences = appEditor.getAppPreferences();
            if (preferences != null) {
                saveState(preferences);
            }
            System.exit(0);
        }
    }

    /**
     * A shared {@code Clipboard}.
     *
     * @return clipboard
     */
    public Clipboard getClipboard() {
        if (clipboard == null) {
            try {
                clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            } catch (SecurityException e) {
                clipboard = new Clipboard("sandbox");
            }
        }

        return clipboard;
    }

    public void actionEditUndo() {
        if (activePanel instanceof ActivePanelUndoable) {
            ((ActivePanelUndoable) activePanel).performUndo();
        }

        refreshUndo();
    }

    public void actionEditRedo() {
        if (activePanel instanceof ActivePanelUndoable) {
            ((ActivePanelUndoable) activePanel).performRedo();
        }

        refreshUndo();
    }

    @Override
    public void refreshUndo() {
        if (activePanel instanceof ActivePanelUndoable) {
            undoAction.setEnabled(((ActivePanelUndoable) activePanel).canUndo());
            redoAction.setEnabled(((ActivePanelUndoable) activePanel).canRedo());
        } else {
            undoAction.setEnabled(false);
            redoAction.setEnabled(false);
        }
    }

    public void actionViewToolbar() {
        toolBar.setVisible(!toolBar.isVisible());
        viewCaptionsCheckBoxMenuItem.setEnabled(toolBar.isVisible());
    }

    public void actionViewStatusBar() {
        statusBar.setVisible(!statusBar.isVisible());
    }

    public void actionViewCaptions() {
        boolean flag = !viewCaptionsCheckBoxMenuItem.isSelected();
        viewCaptionsCheckBoxMenuItem.setSelected(!flag);
        Component[] comps = toolBar.getComponents();

        for (Component comp : comps) {
            if (comp instanceof JButton) {
                ((JButton) comp).setHideActionText(flag);
            }
        }
    }

    public void actionToolsOptions() {
        optionsDialog.setLocationRelativeTo(optionsDialog.getParent());
        optionsDialog.setPreferences(appEditor.getAppPreferences());
        optionsDialog.setVisible(true);
    }

    public void actionHelpAbout() {
        if (aboutDialog == null) {
            aboutDialog = new AboutDialog(null, true, appEditor);
        }

        aboutDialog.setLocationRelativeTo(aboutDialog.getParent());
        aboutDialog.setVisible(true);
    }

    public JToolBar getToolBar() {
        return toolBar;
    }

    /*
     * Called by the KeyboardFocus PropertyChangeListener,
     * before any other focus-change related work is done.
     */
    void updateFocusOwner(JComponent oldOwner, JComponent newOwner) {
        if (oldOwner instanceof JTextComponent) {
            JTextComponent text = (JTextComponent) oldOwner;
            text.removeCaretListener(textComponentCaretListener);
            text.removePropertyChangeListener(textComponentPCL);
        }
        if (newOwner instanceof JTextComponent) {
            JTextComponent text = (JTextComponent) newOwner;
            // maybeInstallTextActions(text);

            text.addCaretListener(textComponentCaretListener);
            text.addPropertyChangeListener(textComponentPCL);
        } else if (newOwner == null) {
            copyAction.setEnabled(false);
            cutAction.setEnabled(false);
            pasteAction.setEnabled(false);
            deleteAction.setEnabled(false);
        }

        lastFocusOwner = newOwner;

        if (activePanel instanceof ActivePanelActionHandling) {
            if (((ActivePanelActionHandling) activePanel).updateActionStatus(newOwner)) {
                return;
            }
        }
        if (newOwner instanceof JTextComponent) {
            isValidClipboardFlavor = getClipboard().isDataFlavorAvailable(DataFlavor.stringFlavor);
            updateTextActions((JTextComponent) newOwner);
        }
    }

    private final class KeyboardFocusPCL implements PropertyChangeListener {

        KeyboardFocusPCL() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            Component oldOwner = getFocusOwner();
            Object newValue = e.getNewValue();
            JComponent newOwner = (newValue instanceof JComponent) ? (JComponent) newValue : null;
            if (oldOwner instanceof JComponent) {
                updateFocusOwner((JComponent) oldOwner, newOwner);
            }

            if (newOwner != null) {
                actionFocusOwner = newOwner;
                /* ActionMap textActionMap = newOwner.getActionMap();
                 if (textActionMap != null) {
                 if (actionMap.get(markerActionKey) == null) {
                 actionFocusOwner = newOwner;
                 }
                 } */

                /*if (newOwner instanceof JTextComponent) {
                 if (((JTextComponent) newOwner).getComponentPopupMenu() == null) {
                 ((JTextComponent) newOwner).setComponentPopupMenu(defaultPopupMenu);
                 }
                 } */
            }
        }
    }

    private final class ClipboardListener implements FlavorListener {

        @Override
        public void flavorsChanged(FlavorEvent e) {
            JComponent c = (JComponent) getFocusOwner();
            if (c instanceof JTextComponent) {
                isValidClipboardFlavor = getClipboard().isDataFlavorAvailable(DataFlavor.stringFlavor);
                updateTextActions((JTextComponent) c);
            }
        }
    }

    private final class TextComponentCaretListener implements CaretListener {

        @Override
        public void caretUpdate(CaretEvent e) {
            updateTextActions((JTextComponent) (e.getSource()));
        }
    }

    private final class TextComponentPCL implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            if ((propertyName == null) || "editable".equals(propertyName)) {
                updateTextActions((JTextComponent) (e.getSource()));
            }
        }
    }

    private void updateTextActions(JTextComponent text) {
        Caret caret = text.getCaret();
        boolean selection = (caret.getDot() != caret.getMark());
        // text.getSelectionEnd() > text.getSelectionStart();
        boolean editable = text.isEditable();
        boolean data = isValidClipboardFlavor;
        copyAction.setEnabled(selection);
        cutAction.setEnabled(editable && selection);
        deleteAction.setEnabled(editable && selection);
        pasteAction.setEnabled(editable && data);
    }

    /* This method lifted from JTextComponent.java */
    private int getCurrentEventModifiers() {
        int modifiers = 0;
        AWTEvent currentEvent = EventQueue.getCurrentEvent();
        if (currentEvent instanceof InputEvent) {
            modifiers = ((InputEvent) currentEvent).getModifiers();
        } else if (currentEvent instanceof ActionEvent) {
            modifiers = ((ActionEvent) currentEvent).getModifiers();
        }
        return modifiers;
    }

    private void invokeTextAction(JTextComponent text, String actionName) {
        ActionMap textActionMap = text.getActionMap().getParent();
        long eventTime = EventQueue.getMostRecentEventTime();
        int eventMods = getCurrentEventModifiers();
        ActionEvent actionEvent
                = new ActionEvent(text, ActionEvent.ACTION_PERFORMED, actionName, eventTime, eventMods);
        textActionMap.get(actionName).actionPerformed(actionEvent);
    }

    public void performCut(ActionEvent e) {
        Object src = e.getSource();
        if (src instanceof JTextComponent) {
            invokeTextAction((JTextComponent) src, DefaultEditorKit.cutAction);
        }
    }

    public void performCopy(ActionEvent e) {
        Object src = e.getSource();
        if (src instanceof JTextComponent) {
            invokeTextAction((JTextComponent) src, DefaultEditorKit.copyAction);
        }
    }

    public void performPaste(ActionEvent e) {
        Object src = e.getSource();
        if (src instanceof JTextComponent) {
            invokeTextAction((JTextComponent) src, DefaultEditorKit.pasteAction);
        }
    }

    @Override
    public Frame getFrame() {
        return this;
    }
}
