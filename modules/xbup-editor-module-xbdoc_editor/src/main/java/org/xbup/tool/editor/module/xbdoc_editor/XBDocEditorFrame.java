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
package org.xbup.tool.editor.module.xbdoc_editor;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JToolBar.Separator;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import org.xbup.lib.catalog.XBAECatalog;
import org.xbup.lib.catalog.entity.XBERoot;
import org.xbup.lib.catalog.entity.service.XBEXDescService;
import org.xbup.lib.catalog.entity.service.XBEXFileService;
import org.xbup.lib.catalog.entity.service.XBEXHDocService;
import org.xbup.lib.catalog.entity.service.XBEXIconService;
import org.xbup.lib.catalog.entity.service.XBEXLangService;
import org.xbup.lib.catalog.entity.service.XBEXLineService;
import org.xbup.lib.catalog.entity.service.XBEXNameService;
import org.xbup.lib.catalog.entity.service.XBEXPaneService;
import org.xbup.lib.catalog.entity.service.XBEXPlugService;
import org.xbup.lib.catalog.entity.service.XBEXStriService;
import org.xbup.lib.catalog.update.XBCUpdateListener;
import org.xbup.lib.catalog.update.XBCUpdatePHPHandler;
import org.xbup.lib.client.XBCatalogNetServiceClient;
import org.xbup.lib.client.catalog.XBARCatalog;
import org.xbup.lib.client.catalog.remote.service.XBRXDescService;
import org.xbup.lib.client.catalog.remote.service.XBRXFileService;
import org.xbup.lib.client.catalog.remote.service.XBRXHDocService;
import org.xbup.lib.client.catalog.remote.service.XBRXIconService;
import org.xbup.lib.client.catalog.remote.service.XBRXLangService;
import org.xbup.lib.client.catalog.remote.service.XBRXLineService;
import org.xbup.lib.client.catalog.remote.service.XBRXNameService;
import org.xbup.lib.client.catalog.remote.service.XBRXPaneService;
import org.xbup.lib.client.catalog.remote.service.XBRXPlugService;
import org.xbup.lib.client.catalog.remote.service.XBRXStriService;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.XBCRoot;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.core.catalog.base.service.XBCXDescService;
import org.xbup.lib.core.catalog.base.service.XBCXFileService;
import org.xbup.lib.core.catalog.base.service.XBCXHDocService;
import org.xbup.lib.core.catalog.base.service.XBCXIconService;
import org.xbup.lib.core.catalog.base.service.XBCXLangService;
import org.xbup.lib.core.catalog.base.service.XBCXLineService;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;
import org.xbup.lib.core.catalog.base.service.XBCXPaneService;
import org.xbup.lib.core.catalog.base.service.XBCXPlugService;
import org.xbup.lib.core.catalog.base.service.XBCXStriService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.operation.undo.XBTLinearUndo;
import org.xbup.lib.parser_tree.XBTTreeNode;
import org.xbup.lib.plugin.XBPluginRepository;
import org.xbup.tool.editor.base.api.ActivePanelActionHandling;
import org.xbup.tool.editor.base.api.ApplicationPanel;
import org.xbup.tool.editor.base.api.BasicMenuType;
import org.xbup.tool.editor.base.api.FileType;
import org.xbup.tool.editor.base.api.MainFrameManagement;
import org.xbup.tool.editor.base.api.MenuManagement;
import org.xbup.tool.editor.base.api.MenuPositionMode;
import org.xbup.tool.editor.base.api.XBEditorApp;
import org.xbup.tool.editor.base.api.XBEditorFrame;
import org.xbup.tool.editor.module.service_manager.catalog.panel.CatalogEditorPanel;
import org.xbup.tool.editor.module.service_manager.panel.CatalogAvailabilityPanel;
import org.xbup.tool.editor.module.service_manager.panel.CatalogManagerPanelable;
import org.xbup.tool.editor.module.text_editor.dialog.FindTextDialog;
import org.xbup.tool.editor.module.text_editor.dialog.FontDialog;
import org.xbup.tool.editor.module.text_editor.dialog.GotoDialog;
import org.xbup.tool.editor.module.text_editor.dialog.TextColorDialog;
import org.xbup.tool.editor.module.text_editor.panel.TextColorPanelFrame;
import org.xbup.tool.editor.module.text_editor.panel.TextFontPanelFrame;
import org.xbup.tool.editor.module.xbdoc_editor.dialog.DocPropertiesDialog;
import org.xbup.tool.editor.module.xbdoc_editor.dialog.BlockPropertiesDialog;
import org.xbup.tool.editor.module.xbdoc_editor.dialog.UndoManagerDialog;
import org.xbup.tool.editor.module.xbdoc_editor.panel.XBDocumentPanel;

/**
 * XBDocEditor Main Frame.
 *
 * @version 0.1.25 2015/04/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBDocEditorFrame extends javax.swing.JFrame implements XBEditorFrame, TextColorPanelFrame, TextFontPanelFrame {

    private static final Preferences preferences = Preferences.userNodeForPackage(XBDocEditorFrame.class);
    private JFileChooser openFC, saveFC;

    private FindTextDialog findDialog = null;
    private GotoDialog gotoDialog = null;
    private BlockPropertiesDialog propertiesDialog = null;
    private CatalogEditorDialog catalogEditorDialog = null;

    private final String DIALOG_MENU_SUFIX = "...";

    private String catalogConnection;
    private XBPluginRepository pluginRepository;
    private XBCUpdatePHPHandler wsHandler;
    private XBACatalog catalog;
    private ResourceBundle resourceBundle;
    private XBEditorApp editorApp;
    private MainFrameManagement mainFrameManagement;
    private boolean devMode = false;

    private XBDocumentPanel activePanel;

    private Thread catInitThread = null;

    public static final String XBFILETYPE = "XBDocEditorModule.XBFileType";

    private Action itemAddAction;
    private Action itemModifyAction;
    private Action itemPropertiesAction;
    private Action editFindAction;
    private Action editFindAgainAction;
    private Action editReplaceAction;
    private Action editGotoAction;
    private MenuManagement menuManagement;

    public XBDocEditorFrame() {
        resourceBundle = ResourceBundle.getBundle("org/xbup/tool/editor/module/xbdoc_editor/resources/XBDocEditorFrame");

        catalog = null;
        activePanel = new XBDocumentPanel(this, catalog);

        // Create File Choosers
        openFC = new JFileChooser();
        openFC.setAcceptAllFileFilterUsed(false);
        saveFC = new JFileChooser();
        saveFC.setAcceptAllFileFilterUsed(false);
        javax.swing.filechooser.FileFilter filter;
        filter = new AllFilesFilter();
        openFC.addChoosableFileFilter(filter);
        saveFC.addChoosableFileFilter(filter);
        filter = new XBFileFilter();
        openFC.addChoosableFileFilter(filter);
        saveFC.addChoosableFileFilter(filter);
        openFC.setFileFilter(filter);
        openFC.setFileFilter(filter);

        initActions();

        initComponents();

        activePanel.addPropertyChangeListener(new PropertyChangePassing(this));

        ((CardLayout) statusPanel.getLayout()).show(statusPanel, "default");

        mainPanel.add(activePanel, java.awt.BorderLayout.CENTER);

        // TODO: Open file from command line
/*        String fileName = ((XBDocEditor) app).getFileName();
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
                // SwingUtilities.updateComponentTreeUI(this);
                SwingUtilities.updateComponentTreeUI(mainPopupMenu);
                SwingUtilities.updateComponentTreeUI(openFC);
                SwingUtilities.updateComponentTreeUI(saveFC);
                if (findDialog != null) {
                    SwingUtilities.updateComponentTreeUI(findDialog);
                }
                if (gotoDialog != null) {
                    SwingUtilities.updateComponentTreeUI(gotoDialog);
                }
            }
        });

// TODO        activePanel.attachCaretListener(caretChangeListener);
        String path = ".";
        try {
            path = (new File(".")).getCanonicalPath();
        } catch (IOException ex) {
            Logger.getLogger(XBDocEditorFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        activePanel.newFile();
        activePanel.setPopupMenu(mainPopupMenu);

        // TODO: Migrate to properties loading
    }

    private void setFileName(String fileName) {
        activePanel.setFileName(fileName);
        if (!"".equals(fileName)) {
            File file = new File(fileName);
        }
    }

    private void initService() {
        // Catalog initialization Thread
        catInitThread = new Thread(new Runnable() {
            @Override
            public void run() {
//                boolean panelStatus = jPanel1.isVisible();
                boolean panelStatus = false;
                getStatusBar().setVisible(true);

                // is 0x5842 (XB)
                int defaultPort = ("DEV".equals(editorApp.getAppBundle().getString("Application.mode"))) ? 22595 : 22594;
                ((CardLayout) statusPanel.getLayout()).show(statusPanel, "initCat");
                operationProgressBar.setString(resourceBundle.getString("main_initlocal") + "...");

                Boolean serviceConnectionAllowed = Boolean.valueOf(preferences.get("serviceConnectionAllowed", Boolean.toString(true)));
                if (serviceConnectionAllowed) {
                    catalogConnection = preferences.get("serviceConnectionURL", null);
                    if (catalogConnection == null || "".equals(catalogConnection)) {
                        catalogConnection = "localhost:" + defaultPort;
                    }
                    String catalogHost;
                    int catalogPort = defaultPort;
                    int pos = catalogConnection.indexOf(":");
                    if (pos >= 0) {
                        catalogHost = catalogConnection.substring(0, pos);
                        catalogPort = Integer.valueOf(catalogConnection.substring(pos + 1));
                    } else {
                        catalogHost = catalogConnection;
                    }

                    catalog = null;

                    // Attempt to connect to catalog service
                    if ((catalogConnection != null) && (!"".equals(catalogConnection))) {
                        XBCatalogNetServiceClient serviceClient = new XBCatalogNetServiceClient(catalogHost, catalogPort);
                        setConnectionStatus(ConnectionStatus.CONNECTING);
                        if (serviceClient.validate()) {
                            catalog = new XBARCatalog(serviceClient);

                            ((XBARCatalog) catalog).addCatalogService(XBCXLangService.class, new XBRXLangService((XBARCatalog) catalog));
                            ((XBARCatalog) catalog).addCatalogService(XBCXStriService.class, new XBRXStriService((XBARCatalog) catalog));
                            ((XBARCatalog) catalog).addCatalogService(XBCXNameService.class, new XBRXNameService((XBARCatalog) catalog));
                            ((XBARCatalog) catalog).addCatalogService(XBCXDescService.class, new XBRXDescService((XBARCatalog) catalog));
                            ((XBARCatalog) catalog).addCatalogService(XBCXFileService.class, new XBRXFileService((XBARCatalog) catalog));
                            ((XBARCatalog) catalog).addCatalogService(XBCXIconService.class, new XBRXIconService((XBARCatalog) catalog));
                            ((XBARCatalog) catalog).addCatalogService(XBCXPlugService.class, new XBRXPlugService((XBARCatalog) catalog));
                            ((XBARCatalog) catalog).addCatalogService(XBCXLineService.class, new XBRXLineService((XBARCatalog) catalog));
                            ((XBARCatalog) catalog).addCatalogService(XBCXPaneService.class, new XBRXPaneService((XBARCatalog) catalog));
                            ((XBARCatalog) catalog).addCatalogService(XBCXHDocService.class, new XBRXHDocService((XBARCatalog) catalog));

                            if ("localhost".equals(serviceClient.getHost()) || "127.0.0.1".equals(serviceClient.getHost())) {
                                setConnectionStatus(ConnectionStatus.LOCAL);
                            } else {
                                setConnectionStatus(ConnectionStatus.NETWORK);
                            }
                            setCatalog(catalog);
                        }
                    }
                }

                // Create internal catalog instead
                if (catalog == null) {
                    setConnectionStatus(ConnectionStatus.DISCONNECTED);
                    operationProgressBar.setString(resourceBundle.getString("main_initcat") + "...");
                    try {
                        // Database Initialization
                        String derbyHome = System.getProperty("user.home") + "/.java/.userPrefs/" + preferences.absolutePath();
                        if (devMode) {
                            derbyHome += "-dev";
                        }
                        System.setProperty("derby.system.home", derbyHome);
                        EntityManagerFactory emf = Persistence.createEntityManagerFactory("XBEditorPU");
                        EntityManager em = emf.createEntityManager();
                        em.setFlushMode(FlushModeType.AUTO);

                        catalog = createCatalog(em);

                        if (((XBAECatalog) catalog).isShallInit()) {
                            operationProgressBar.setString(resourceBundle.getString("main_defaultcat") + "...");
                            ((XBAECatalog) catalog).initCatalog();
                        }

                        Boolean catalogUpdateAllowed = Boolean.valueOf(preferences.get("catalogUpdateAllowed", Boolean.toString(true)));
                        // Update internal catalog
                        if (catalogUpdateAllowed && catalog instanceof XBAECatalog) {
                            // String catalogUpdate = preferences.get("catalogUpdateURL", null); // TODO

                            try {
                                operationProgressBar.setString(resourceBundle.getString("main_initws") + "...");
                                wsHandler = new XBCUpdatePHPHandler((XBAECatalog) catalog);
                                wsHandler.init();
                                wsHandler.getPort().getLanguageId("en");
                                ((XBAECatalog) catalog).setUpdateHandler(wsHandler);
                                XBCNodeService nodeService = (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
                                XBCRoot catalogRoot = nodeService.getRoot();
                                if (catalogRoot != null) {
                                    Date localLastUpdate = catalogRoot.getLastUpdate();
                                    Date lastUpdate = wsHandler.getPort().getRootLastUpdate();
                                    if (localLastUpdate == null || localLastUpdate.before(lastUpdate)) {
                                        // TODO: As there is currently no diff update available - wipe out entire database instead
                                        em.close();
                                        EntityManagerFactory emfDrop = Persistence.createEntityManagerFactory("XBEditorPU-drop");
                                        EntityManager emDrop = emfDrop.createEntityManager();
                                        emDrop.setFlushMode(FlushModeType.AUTO);
                                        catalog = createCatalog(emDrop);
                                        ((XBAECatalog) catalog).initCatalog();
                                        nodeService = (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
                                        performUpdate((XBERoot) nodeService.getRoot(), lastUpdate);

                                    }
                                    setConnectionStatus(ConnectionStatus.INTERNET);
                                }
                            } catch (Exception ex) {
                                Logger.getLogger(XBDocEditorFrame.class.getName()).log(Level.SEVERE, null, ex);
                                wsHandler = null;
                            }
                        }

                        setCatalog(catalog);
                    } catch (Exception ex) {
                        Logger.getLogger(XBDocEditorFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                // Initialize plugins
                if (catalog != null) {
                    pluginRepository = new XBPluginRepository();
                    pluginRepository.addPluginsFrom(new File("plugins/").toURI());
                    pluginRepository.processPlugins();
                    pluginRepository.setCatalog(catalog);
                    activePanel.setPluginRepository(pluginRepository);
                }

                ((CardLayout) statusPanel.getLayout()).first(statusPanel);
            }

            private XBACatalog createCatalog(EntityManager em) {
                XBACatalog createdCatalog = new XBAECatalog(em);

                ((XBAECatalog) createdCatalog).addCatalogService(XBCXLangService.class, new XBEXLangService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXStriService.class, new XBEXStriService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXNameService.class, new XBEXNameService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXDescService.class, new XBEXDescService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXFileService.class, new XBEXFileService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXIconService.class, new XBEXIconService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXPlugService.class, new XBEXPlugService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXLineService.class, new XBEXLineService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXPaneService.class, new XBEXPaneService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXHDocService.class, new XBEXHDocService((XBAECatalog) createdCatalog));
                return createdCatalog;
            }

            private void performUpdate(XBERoot catalogRoot, Date lastUpdate) {
                XBCUpdatePHPHandler wsHandler = new XBCUpdatePHPHandler((XBAECatalog) catalog);
                wsHandler.init();
                wsHandler.getPort().getLanguageId("en");

                wsHandler.fireUsageEvent(false);
                wsHandler.addWSListener(new XBCUpdateListener() {
                    private boolean toolBarVisibleTemp;

                    @Override
                    public void webServiceUsage(boolean status) {
                        if (status == true) {
                            toolBarVisibleTemp = getStatusBar().isVisible();
                            ((CardLayout) statusPanel.getLayout()).show(statusPanel, "updateCat");
                            activityProgressBar.setString(resourceBundle.getString("main_updatecat") + "...");
                            getStatusBar().setVisible(true);
                        } else {
                            ((CardLayout) statusPanel.getLayout()).first(statusPanel);
                            //                                statusBar.setVisible(toolBarVisibleTemp);
                        }
                    }
                });
                wsHandler.updateCatalog(catalogRoot, lastUpdate);
            }
        });
        catInitThread.start();
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;

        activePanel.setCatalog(catalog);
        activePanel.reportStructureChange((XBTTreeNode) activePanel.getDoc().getRootBlock());

        if (propertiesDialog != null) {
            propertiesDialog.setCatalog(catalog);
        }

        if (catalogEditorDialog != null) {
            catalogEditorDialog.setCatalog(catalog);
        }
    }

    public void updateItem() {
        activePanel.updateItem();
    }

    public JToolBar getToolBar() {
        return toolBar;
    }

    public void setToolBar(JToolBar toolBar) {
        this.toolBar = toolBar;
    }

    public JPanel getStatusBar() {
        return statusBar;
    }

    public void setStatusBar(JPanel statusBar) {
        this.statusBar = statusBar;
    }

    public JPopupMenu getPopupMenu() {
        return mainPopupMenu;
    }

    private void initActions() {
        itemAddAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionItemAdd();
            }
        };
        itemAddAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/xbdoc_editor/resources/images/actions/Add16.gif")));
        itemAddAction.putValue(Action.NAME, resourceBundle.getString("actionItemAdd.Action.text"));
        itemAddAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionItemAdd.Action.shortDescription"));
        itemAddAction.setEnabled(false);

        itemModifyAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionItemModify();
            }
        };
        itemModifyAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/xbdoc_editor/resources/images/actions/Modify16.gif")));
        itemModifyAction.putValue(Action.NAME, resourceBundle.getString("actionItemModify.Action.text"));
        itemModifyAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionItemModify.Action.shortDescription"));
        itemModifyAction.setEnabled(false);

        itemPropertiesAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionItemProperties();
            }
        };
        itemPropertiesAction.putValue(Action.NAME, resourceBundle.getString("actionItemProperties.Action.text"));
        itemPropertiesAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionItemProperties.Action.shortDescription"));
        itemPropertiesAction.setEnabled(false);

        editFindAction = new AbstractAction("editFind") {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionEditFind();
            }
        };
        editFindAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/xbdoc_editor/resources/images/actions/Find16.gif")));
        editFindAction.putValue(Action.NAME, resourceBundle.getString("actionItemModify.Action.text"));
        editFindAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionItemModify.Action.shortDescription"));
        editFindAction.setEnabled(false);

        editFindAgainAction = new AbstractAction("editFindAgain") {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionEditFindAgain();
            }
        };
        editFindAgainAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/xbdoc_editor/resources/images/actions/FindAgain16.gif")));
        editFindAgainAction.putValue(Action.NAME, resourceBundle.getString("actionEditFindAgain.Action.text"));
        editFindAgainAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditFindAgain.Action.shortDescription"));
        editFindAgainAction.setEnabled(false);

        editReplaceAction = new AbstractAction("editReplace") {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionEditReplace();
            }
        };
        editReplaceAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/xbdoc_editor/resources/images/actions/Replace16.gif")));
        editReplaceAction.putValue(Action.NAME, resourceBundle.getString("actionEditReplace.Action.text"));
        editReplaceAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditReplace.Action.shortDescription"));
        editReplaceAction.setEnabled(false);

        editGotoAction = new AbstractAction("editGoto") {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionEditGoto();
            }
        };
        editGotoAction.putValue(Action.NAME, resourceBundle.getString("actionEditGoto.Action.text"));
        editGotoAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditGoto.Action.shortDescription"));
        editGotoAction.setEnabled(false);
    }

    public Action getItemAddAction() {
        return itemAddAction;
    }

    public Action getItemModifyAction() {
        return itemModifyAction;
    }

    public Action getEditFindAction() {
        return editFindAction;
    }

    public Action getEditFindAgainAction() {
        return editFindAgainAction;
    }

    public Action getEditReplaceAction() {
        return editReplaceAction;
    }

    public Action getEditGotoAction() {
        return editGotoAction;
    }

    public Action getItemPropertiesAction() {
        return itemPropertiesAction;
    }

    @Override
    public Color[] getCurrentTextColors() {
        return activePanel.getCurrentColors();
    }

    @Override
    public Color[] getDefaultTextColors() {
        return activePanel.getDefaultColors();
    }

    @Override
    public void setCurrentTextColors(Color[] colors) {
        activePanel.setCurrentColors(colors);
    }

    @Override
    public Font getCurrentFont() {
        return activePanel.getCurrentFont();
    }

    @Override
    public Font getDefaultFont() {
        return activePanel.getDefaultFont();
    }

    @Override
    public void setCurrentFont(Font font) {
        activePanel.setCurrentFont(font);
    }

    @Override
    public MainFrameManagement getMainFrameManagement() {
        return mainFrameManagement;
    }

    void setMainFrameManagement(MainFrameManagement mainFrameManagement) {
        this.mainFrameManagement = mainFrameManagement;

        ActionMap actionMap = mainFrameManagement.getActionMap();
        actionMap.put("itemAdd", itemAddAction);
        actionMap.put("itemModify", itemModifyAction);
        actionMap.put("itemProperties", itemPropertiesAction);
    }

    void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public ApplicationPanel getActivePanel() {
        return activePanel;
    }

    private class PropertyChangePassing implements PropertyChangeListener {

        private final XBDocEditorFrame target;

        public PropertyChangePassing(XBDocEditorFrame target) {
            this.target = target;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            // target.firePropertyChange(evt);
        }
    };

    public boolean isEditEnabled() {
        if (activePanel == null) {
            return false;
        }
        return activePanel.isEditEnabled() && (activePanel.getMode() == XBDocumentPanel.PanelMode.TREE);
    }

    public boolean isPasteEnabled() {
        if (activePanel == null) {
            return false;
        }
        return activePanel.isPasteEnabled();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPopupMenu = new JPopupMenu();
        popupItemOpenMenuItem = new JMenuItem();
        popupItemAddMenuItem = new JMenuItem();
        popupItemModifyMenuItem = new JMenuItem();
        jSeparator14 = new JPopupMenu.Separator();
        jSeparator22 = new JPopupMenu.Separator();
        popupItemImportMenuItem = new JMenuItem();
        popupItemExportMenuItem = new JMenuItem();
        jSeparator16 = new JPopupMenu.Separator();
        popupItemPropertiesMenuItem = new JMenuItem();
        viewAsbuttonGroup = new ButtonGroup();
        mainPanel = new JPanel();
        toolBar = new JToolBar();
        findToolButton = new JButton();
        jSeparator20 = new Separator();
        addToolButton = new JButton();
        modifyToolButton = new JButton();
        statusBar = new JPanel();
        statusPanel = new JPanel();
        emptyPanel = new JPanel();
        textEditingPanel = new JPanel();
        textPositionTextField = new JTextField();
        textStatusLabel = new JLabel();
        currentEncodingTextField = new JTextField();
        labelPanel = new JPanel();
        statusLabel = new JLabel();
        operationPanel = new JPanel();
        operationProgressBar = new JProgressBar();
        operationStopButton = new JButton();
        activityPanel = new JPanel();
        activityProgressBar = new JProgressBar();
        connectionStatusPanel = new JPanel();
        connectionStatusLabel = new JLabel();
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        fileNewCustomMenuItem = new JMenuItem();
        fileOpenSampleMenu = new JMenu();
        fileOpenXHTMLSampleMenuItem = new JMenuItem();
        fileOpenPicSampleMenuItem = new JMenuItem();
        filePropertiesMenuItem = new JMenuItem();
        editMenu = new JMenu();
        undoManagerMenuItem = new JMenuItem();
        jSeparator3 = new JSeparator();
        editGotoMenuItem = new JMenuItem();
        jSeparator12 = new JSeparator();
        editFindMenuItem = new JMenuItem();
        editFindAgainMenuItem = new JMenuItem();
        editFindReplaceMenuItem = new JMenuItem();
        jSeparator15 = new JPopupMenu.Separator();
        editItemAddMenuItem = new JMenuItem();
        editItemModifyMenuItem = new JMenuItem();
        viewMenu = new JMenu();
        jSeparator21 = new JPopupMenu.Separator();
        viewPropertiesCheckBoxMenuItem = new JCheckBoxMenuItem();
        jSeparator6 = new JSeparator();
        viewAsTreeMenuItem = new JRadioButtonMenuItem();
        viewAsTextMenuItem = new JRadioButtonMenuItem();
        viewAsHexMenuItem = new JRadioButtonMenuItem();
        jSeparator17 = new JPopupMenu.Separator();
        viewWordWrapCheckBoxMenuItem = new JCheckBoxMenuItem();
        toolsMenu = new JMenu();
        toolsCatalogBrowserMenuItem = new JMenuItem();
        optionsMenu = new JMenu();
        optionsFontMenuItem = new JMenuItem();
        optionsColorsMenuItem = new JMenuItem();

        mainPopupMenu.setName("mainPopupMenu"); // NOI18N

        ResourceBundle bundle = ResourceBundle.getBundle("org/xbup/tool/editor/module/xbdoc_editor/resources/XBDocEditorFrame"); // NOI18N
        popupItemOpenMenuItem.setText(bundle.getString("popupItemOpenMenuItem.text")); // NOI18N
        popupItemOpenMenuItem.setToolTipText(bundle.getString("popupItemOpenMenuItem.toolTipText")); // NOI18N
        popupItemOpenMenuItem.setEnabled(false);
        popupItemOpenMenuItem.setName("popupItemOpenMenuItem"); // NOI18N
        mainPopupMenu.add(popupItemOpenMenuItem);

        popupItemAddMenuItem.setAction(itemAddAction);
        popupItemAddMenuItem.setIcon(new ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/xbdoc_editor/resources/images/actions/Add16.gif"))); // NOI18N
        popupItemAddMenuItem.setText(bundle.getString("popupItemAddMenuItem.text")); // NOI18N
        popupItemAddMenuItem.setName("popupItemAddMenuItem"); // NOI18N
        mainPopupMenu.add(popupItemAddMenuItem);

        popupItemModifyMenuItem.setAction(itemModifyAction);
        popupItemModifyMenuItem.setIcon(new ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/xbdoc_editor/resources/images/actions/Modify16.gif"))); // NOI18N
        popupItemModifyMenuItem.setText(bundle.getString("popupItemModifyMenuItem.text")); // NOI18N
        popupItemModifyMenuItem.setName("popupItemModifyMenuItem"); // NOI18N
        mainPopupMenu.add(popupItemModifyMenuItem);

        jSeparator14.setName("jSeparator14"); // NOI18N
        mainPopupMenu.add(jSeparator14);

        jSeparator22.setName("jSeparator22"); // NOI18N
        mainPopupMenu.add(jSeparator22);

        popupItemImportMenuItem.setText(bundle.getString("popupItemImportMenuItem.text")); // NOI18N
        popupItemImportMenuItem.setToolTipText(bundle.getString("popupItemImportMenuItem.toolTipText")); // NOI18N
        popupItemImportMenuItem.setName("popupItemImportMenuItem"); // NOI18N
        mainPopupMenu.add(popupItemImportMenuItem);

        popupItemExportMenuItem.setText(bundle.getString("popupItemExportMenuItem.text")); // NOI18N
        popupItemExportMenuItem.setToolTipText(bundle.getString("popupItemExportMenuItem.toolTipText")); // NOI18N
        popupItemExportMenuItem.setName("popupItemExportMenuItem"); // NOI18N
        mainPopupMenu.add(popupItemExportMenuItem);

        jSeparator16.setName("jSeparator16"); // NOI18N
        mainPopupMenu.add(jSeparator16);

        popupItemPropertiesMenuItem.setAction(itemPropertiesAction);
        popupItemPropertiesMenuItem.setText(bundle.getString("actionItemProperties.Action.text")); // NOI18N
        popupItemPropertiesMenuItem.setName("popupItemPropertiesMenuItem"); // NOI18N
        mainPopupMenu.add(popupItemPropertiesMenuItem);

        for (JMenuItem item : Arrays.asList(popupItemAddMenuItem, popupItemModifyMenuItem, popupItemPropertiesMenuItem, popupItemOpenMenuItem, popupItemExportMenuItem, popupItemImportMenuItem)) {
            item.setText(item.getText()+DIALOG_MENU_SUFIX);
        }

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setLayout(new BorderLayout());

        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setName("toolBar"); // NOI18N

        findToolButton.setAction(editFindAction);
        findToolButton.setIcon(new ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/xbdoc_editor/resources/images/actions/Find16.gif"))); // NOI18N
        findToolButton.setText(bundle.getString("actionEditFind.Action.text")); // NOI18N
        findToolButton.setFocusable(false);
        findToolButton.setHorizontalTextPosition(SwingConstants.CENTER);
        findToolButton.setName("findToolButton"); // NOI18N
        findToolButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolBar.add(findToolButton);

        jSeparator20.setName("jSeparator20"); // NOI18N
        toolBar.add(jSeparator20);

        addToolButton.setAction(itemAddAction);
        addToolButton.setIcon(new ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/xbdoc_editor/resources/images/actions/Add16.gif"))); // NOI18N
        addToolButton.setText(bundle.getString("actionItemAdd.Action.text")); // NOI18N
        addToolButton.setFocusable(false);
        addToolButton.setHorizontalTextPosition(SwingConstants.CENTER);
        addToolButton.setName("addToolButton"); // NOI18N
        addToolButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolBar.add(addToolButton);

        modifyToolButton.setAction(itemModifyAction);
        modifyToolButton.setIcon(new ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/xbdoc_editor/resources/images/actions/Modify16.gif"))); // NOI18N
        modifyToolButton.setText(bundle.getString("actionItemModify.Action.text")); // NOI18N
        modifyToolButton.setFocusable(false);
        modifyToolButton.setHorizontalTextPosition(SwingConstants.CENTER);
        modifyToolButton.setName("modifyToolButton"); // NOI18N
        modifyToolButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolBar.add(modifyToolButton);

        mainPanel.add(toolBar, BorderLayout.NORTH);

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        statusBar.setName("statusBar"); // NOI18N
        statusBar.setLayout(new BorderLayout());

        statusPanel.setName("statusPanel"); // NOI18N
        statusPanel.setPreferredSize(new Dimension(649, 26));
        statusPanel.setRequestFocusEnabled(false);
        statusPanel.setLayout(new CardLayout());

        emptyPanel.setName("emptyPanel"); // NOI18N

        GroupLayout emptyPanelLayout = new GroupLayout(emptyPanel);
        emptyPanel.setLayout(emptyPanelLayout);
        emptyPanelLayout.setHorizontalGroup(emptyPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGap(0, 640, Short.MAX_VALUE)
        );
        emptyPanelLayout.setVerticalGroup(emptyPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGap(0, 27, Short.MAX_VALUE)
        );

        statusPanel.add(emptyPanel, "default");

        textEditingPanel.setName("textEditingPanel"); // NOI18N

        textPositionTextField.setEditable(false);
        textPositionTextField.setHorizontalAlignment(JTextField.CENTER);
        textPositionTextField.setText("1:1"); // NOI18N
        textPositionTextField.setToolTipText(bundle.getString("textPositionTextField.toolTipText")); // NOI18N
        textPositionTextField.setName("textPositionTextField"); // NOI18N

        textStatusLabel.setName("textStatusLabel"); // NOI18N

        currentEncodingTextField.setEditable(false);
        currentEncodingTextField.setHorizontalAlignment(JTextField.CENTER);
        currentEncodingTextField.setText("UTF-8"); // NOI18N
        currentEncodingTextField.setToolTipText(bundle.getString("currentEncodingTextField.toolTipText")); // NOI18N
        currentEncodingTextField.setName("currentEncodingTextField"); // NOI18N

        GroupLayout textEditingPanelLayout = new GroupLayout(textEditingPanel);
        textEditingPanel.setLayout(textEditingPanelLayout);
        textEditingPanelLayout.setHorizontalGroup(textEditingPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(textEditingPanelLayout.createSequentialGroup()
                .addComponent(textPositionTextField, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED, 427, Short.MAX_VALUE)
                .addComponent(textStatusLabel)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(currentEncodingTextField, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        textEditingPanelLayout.setVerticalGroup(textEditingPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(textEditingPanelLayout.createParallelGroup(Alignment.BASELINE)
                .addComponent(textPositionTextField, GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addComponent(currentEncodingTextField, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                .addComponent(textStatusLabel))
        );

        statusPanel.add(textEditingPanel, "text");

        labelPanel.setName("labelPanel"); // NOI18N
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        statusLabel.setName("statusLabel"); // NOI18N
        labelPanel.add(statusLabel);

        statusPanel.add(labelPanel, "main");

        operationPanel.setName("operationPanel"); // NOI18N

        operationProgressBar.setIndeterminate(true);
        operationProgressBar.setMinimumSize(new Dimension(10, 10));
        operationProgressBar.setName("operationProgressBar"); // NOI18N
        operationProgressBar.setRequestFocusEnabled(false);
        operationProgressBar.setStringPainted(true);

        operationStopButton.setText("Stop"); // NOI18N
        operationStopButton.setEnabled(false);
        operationStopButton.setMinimumSize(new Dimension(67, 15));
        operationStopButton.setName("operationStopButton"); // NOI18N
        operationStopButton.setPreferredSize(new Dimension(75, 20));

        GroupLayout operationPanelLayout = new GroupLayout(operationPanel);
        operationPanel.setLayout(operationPanelLayout);
        operationPanelLayout.setHorizontalGroup(operationPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(Alignment.TRAILING, operationPanelLayout.createSequentialGroup()
                .addComponent(operationProgressBar, GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(operationStopButton, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
        );
        operationPanelLayout.setVerticalGroup(operationPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(operationPanelLayout.createParallelGroup(Alignment.BASELINE)
                .addComponent(operationStopButton, GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addComponent(operationProgressBar, GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE))
        );

        statusPanel.add(operationPanel, "initCat");

        activityPanel.setName("activityPanel"); // NOI18N

        activityProgressBar.setIndeterminate(true);
        activityProgressBar.setName("activityProgressBar"); // NOI18N
        activityProgressBar.setRequestFocusEnabled(false);
        activityProgressBar.setStringPainted(true);

        GroupLayout activityPanelLayout = new GroupLayout(activityPanel);
        activityPanel.setLayout(activityPanelLayout);
        activityPanelLayout.setHorizontalGroup(activityPanelLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(activityProgressBar, GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
        );
        activityPanelLayout.setVerticalGroup(activityPanelLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(activityProgressBar, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
        );

        statusPanel.add(activityPanel, "updateCat");

        statusBar.add(statusPanel, BorderLayout.CENTER);

        connectionStatusPanel.setName("connectionStatusPanel"); // NOI18N

        connectionStatusLabel.setIcon(new ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/xbdoc_editor/resources/images/status/network-offline.png"))); // NOI18N
        connectionStatusLabel.setToolTipText(bundle.getString("connectionStatusLabel.toolTipText")); // NOI18N
        connectionStatusLabel.setName("connectionStatusLabel"); // NOI18N

        GroupLayout connectionStatusPanelLayout = new GroupLayout(connectionStatusPanel);
        connectionStatusPanel.setLayout(connectionStatusPanelLayout);
        connectionStatusPanelLayout.setHorizontalGroup(connectionStatusPanelLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(connectionStatusLabel)
        );
        connectionStatusPanelLayout.setVerticalGroup(connectionStatusPanelLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(connectionStatusLabel, GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
        );

        statusBar.add(connectionStatusPanel, BorderLayout.EAST);

        getContentPane().add(statusBar, BorderLayout.SOUTH);

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(bundle.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        fileNewCustomMenuItem.setIcon(new ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/xbdoc_editor/resources/images/actions/New16.gif"))); // NOI18N
        fileNewCustomMenuItem.setText(bundle.getString("fileNewCustomMenuItem.text")); // NOI18N
        fileNewCustomMenuItem.setToolTipText(bundle.getString("fileNewCustomMenuItem.toolTipText")); // NOI18N
        fileNewCustomMenuItem.setEnabled(false);
        fileNewCustomMenuItem.setName("fileNewCustomMenuItem"); // NOI18N
        fileNewCustomMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                fileNewCustomMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(fileNewCustomMenuItem);

        fileOpenSampleMenu.setText(bundle.getString("fileOpenSampleMenu.text")); // NOI18N
        fileOpenSampleMenu.setToolTipText(bundle.getString("fileOpenSampleMenu.toolTipText")); // NOI18N
        fileOpenSampleMenu.setName("fileOpenSampleMenu"); // NOI18N

        fileOpenXHTMLSampleMenuItem.setText(bundle.getString("fileOpenXHTMLSampleMenuItem.text")); // NOI18N
        fileOpenXHTMLSampleMenuItem.setName("fileOpenXHTMLSampleMenuItem"); // NOI18N
        fileOpenXHTMLSampleMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                fileOpenXHTMLSampleMenuItemActionPerformed(evt);
            }
        });
        fileOpenSampleMenu.add(fileOpenXHTMLSampleMenuItem);

        fileOpenPicSampleMenuItem.setText(bundle.getString("fileOpenPicSampleMenuItem.text")); // NOI18N
        fileOpenPicSampleMenuItem.setName("fileOpenPicSampleMenuItem"); // NOI18N
        fileOpenPicSampleMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                fileOpenPicSampleMenuItemActionPerformed(evt);
            }
        });
        fileOpenSampleMenu.add(fileOpenPicSampleMenuItem);

        fileMenu.add(fileOpenSampleMenu);

        filePropertiesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.ALT_MASK));
        filePropertiesMenuItem.setText(bundle.getString("filePropertiesMenuItem.text")); // NOI18N
        filePropertiesMenuItem.setToolTipText(bundle.getString("filePropertiesMenuItem.toolTipText")); // NOI18N
        filePropertiesMenuItem.setName("filePropertiesMenuItem"); // NOI18N
        filePropertiesMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                filePropertiesMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(filePropertiesMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText(bundle.getString("editMenu.text")); // NOI18N
        editMenu.setName("editMenu"); // NOI18N
        editMenu.setRolloverEnabled(true);

        undoManagerMenuItem.setText(bundle.getString("undoManagerMenuItem.text")); // NOI18N
        undoManagerMenuItem.setName("undoManagerMenuItem"); // NOI18N
        undoManagerMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                undoManagerMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(undoManagerMenuItem);

        jSeparator3.setName("jSeparator3"); // NOI18N
        editMenu.add(jSeparator3);

        editGotoMenuItem.setAction(editGotoAction);
        editGotoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
        editGotoMenuItem.setText(bundle.getString("actionEditGoto.Action.text")); // NOI18N
        editGotoMenuItem.setName("editGotoMenuItem"); // NOI18N
        editMenu.add(editGotoMenuItem);

        jSeparator12.setName("jSeparator12"); // NOI18N
        editMenu.add(jSeparator12);

        editFindMenuItem.setAction(editFindAction);
        editFindMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        editFindMenuItem.setIcon(new ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/xbdoc_editor/resources/images/actions/Find16.gif"))); // NOI18N
        editFindMenuItem.setText(bundle.getString("actionEditFind.Action.text")); // NOI18N
        editFindMenuItem.setName("editFindMenuItem"); // NOI18N
        editMenu.add(editFindMenuItem);

        editFindAgainMenuItem.setAction(editFindAgainAction);
        editFindAgainMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        editFindAgainMenuItem.setIcon(new ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/xbdoc_editor/resources/images/actions/FindAgain16.gif"))); // NOI18N
        editFindAgainMenuItem.setText(bundle.getString("actionEditFindAgain.Action.text")); // NOI18N
        editFindAgainMenuItem.setName("editFindAgainMenuItem"); // NOI18N
        editMenu.add(editFindAgainMenuItem);

        editFindReplaceMenuItem.setAction(editReplaceAction);
        editFindReplaceMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
        editFindReplaceMenuItem.setIcon(new ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/xbdoc_editor/resources/images/actions/Replace16.gif"))); // NOI18N
        editFindReplaceMenuItem.setText(bundle.getString("actionEditReplace.Action.text")); // NOI18N
        editFindReplaceMenuItem.setName("editFindReplaceMenuItem"); // NOI18N
        editMenu.add(editFindReplaceMenuItem);

        jSeparator15.setName("jSeparator15"); // NOI18N
        editMenu.add(jSeparator15);

        editItemAddMenuItem.setAction(itemAddAction);
        editItemAddMenuItem.setIcon(new ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/xbdoc_editor/resources/images/actions/Add16.gif"))); // NOI18N
        editItemAddMenuItem.setText(bundle.getString("actionItemAdd.Action.text")); // NOI18N
        editItemAddMenuItem.setName("editItemAddMenuItem"); // NOI18N
        editMenu.add(editItemAddMenuItem);

        editItemModifyMenuItem.setAction(itemModifyAction);
        editItemModifyMenuItem.setIcon(new ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/xbdoc_editor/resources/images/actions/Modify16.gif"))); // NOI18N
        editItemModifyMenuItem.setText(bundle.getString("actionItemModify.Action.text")); // NOI18N
        editItemModifyMenuItem.setName("editItemModifyMenuItem"); // NOI18N
        editMenu.add(editItemModifyMenuItem);

        menuBar.add(editMenu);

        viewMenu.setText(bundle.getString("viewMenu.text")); // NOI18N

        jSeparator21.setName("jSeparator21"); // NOI18N
        viewMenu.add(jSeparator21);

        viewPropertiesCheckBoxMenuItem.setSelected(true);
        viewPropertiesCheckBoxMenuItem.setText(bundle.getString("viewPropertiesCheckBoxMenuItem.text")); // NOI18N
        viewPropertiesCheckBoxMenuItem.setToolTipText(bundle.getString("viewPropertiesCheckBoxMenuItem.toolTipText")); // NOI18N
        viewPropertiesCheckBoxMenuItem.setName("viewPropertiesCheckBoxMenuItem"); // NOI18N
        viewPropertiesCheckBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                viewPropertiesCheckBoxMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(viewPropertiesCheckBoxMenuItem);

        jSeparator6.setName("jSeparator6"); // NOI18N
        viewMenu.add(jSeparator6);

        viewAsbuttonGroup.add(viewAsTreeMenuItem);
        viewAsTreeMenuItem.setSelected(true);
        viewAsTreeMenuItem.setText(bundle.getString("viewAsTreeMenuItem.text")); // NOI18N
        viewAsTreeMenuItem.setToolTipText(bundle.getString("viewAsTreeMenuItem.toolTipText")); // NOI18N
        viewAsTreeMenuItem.setName("viewAsTreeMenuItem"); // NOI18N
        viewAsTreeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                viewAsTreeMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(viewAsTreeMenuItem);

        viewAsbuttonGroup.add(viewAsTextMenuItem);
        viewAsTextMenuItem.setText(bundle.getString("viewAsTextMenuItem.text")); // NOI18N
        viewAsTextMenuItem.setToolTipText(bundle.getString("viewAsTextMenuItem.toolTipText")); // NOI18N
        viewAsTextMenuItem.setName("viewAsTextMenuItem"); // NOI18N
        viewAsTextMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                viewAsTextMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(viewAsTextMenuItem);

        viewAsbuttonGroup.add(viewAsHexMenuItem);
        viewAsHexMenuItem.setText(bundle.getString("viewAsHexMenuItem.text")); // NOI18N
        viewAsHexMenuItem.setToolTipText(bundle.getString("viewAsHexMenuItem.toolTipText")); // NOI18N
        viewAsHexMenuItem.setName("viewAsHexMenuItem"); // NOI18N
        viewAsHexMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                viewAsHexMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(viewAsHexMenuItem);

        jSeparator17.setName("jSeparator17"); // NOI18N
        viewMenu.add(jSeparator17);

        viewWordWrapCheckBoxMenuItem.setText(bundle.getString("viewWordWrapCheckBoxMenuItem.text")); // NOI18N
        viewWordWrapCheckBoxMenuItem.setName("viewWordWrapCheckBoxMenuItem"); // NOI18N
        viewWordWrapCheckBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                viewWordWrapCheckBoxMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(viewWordWrapCheckBoxMenuItem);

        menuBar.add(viewMenu);

        toolsMenu.setText(bundle.getString("toolsMenu.text")); // NOI18N
        toolsMenu.setName("toolsMenu"); // NOI18N

        toolsCatalogBrowserMenuItem.setText(bundle.getString("toolsCatalogBrowserMenuItem.text")); // NOI18N
        toolsCatalogBrowserMenuItem.setToolTipText(bundle.getString("toolsCatalogBrowserMenuItem.toolTipText")); // NOI18N
        toolsCatalogBrowserMenuItem.setName("toolsCatalogBrowserMenuItem"); // NOI18N
        toolsCatalogBrowserMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                toolsCatalogBrowserMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(toolsCatalogBrowserMenuItem);

        menuBar.add(toolsMenu);

        optionsMenu.setText(bundle.getString("optionsMenu.text")); // NOI18N
        optionsMenu.setName("optionsMenu"); // NOI18N

        optionsFontMenuItem.setText(bundle.getString("XBDocEditorFrame.optionsFontMenuItem.text")); // NOI18N
        optionsFontMenuItem.setToolTipText(bundle.getString("XBDocEditorFrame.optionsFontMenuItem.toolTipText")); // NOI18N
        optionsFontMenuItem.setName("optionsFontMenuItem"); // NOI18N
        optionsFontMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                optionsFontMenuItemActionPerformed(evt);
            }
        });
        optionsMenu.add(optionsFontMenuItem);

        optionsColorsMenuItem.setText(bundle.getString("XBDocEditorFrame.optionsColorsMenuItem.text")); // NOI18N
        optionsColorsMenuItem.setToolTipText(bundle.getString("XBDocEditorFrame.optionsColorsMenuItem.toolTipText")); // NOI18N
        optionsColorsMenuItem.setName("optionsColorsMenuItem"); // NOI18N
        optionsColorsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                optionsColorsMenuItemActionPerformed(evt);
            }
        });
        optionsMenu.add(optionsColorsMenuItem);

        menuBar.add(optionsMenu);

        for (JMenuItem item : Arrays.asList(fileNewCustomMenuItem, filePropertiesMenuItem, editFindMenuItem, editFindReplaceMenuItem, editGotoMenuItem, editItemAddMenuItem, editItemModifyMenuItem, toolsCatalogBrowserMenuItem, optionsFontMenuItem, optionsColorsMenuItem, undoManagerMenuItem)) {
            item.setText(item.getText()+DIALOG_MENU_SUFIX);
        }

        setJMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

    private void viewWordWrapCheckBoxMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_viewWordWrapCheckBoxMenuItemActionPerformed
        viewWordWrapCheckBoxMenuItem.setSelected(activePanel.changeLineWrap());
    }//GEN-LAST:event_viewWordWrapCheckBoxMenuItemActionPerformed

    private void fileOpenXHTMLSampleMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_fileOpenXHTMLSampleMenuItemActionPerformed
        activePanel.newFile();
        try {
            activePanel.getDoc().fromStreamUB(getClass().getResourceAsStream("/org/xbup/tool/editor/module/xbdoc_editor/resources/samples/xhtml_example.xb"));
            activePanel.getDoc().processSpec();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBDocEditorFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        activePanel.reportStructureChange(null);
        activePanel.updateItem();
    }//GEN-LAST:event_fileOpenXHTMLSampleMenuItemActionPerformed

    private void fileOpenPicSampleMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_fileOpenPicSampleMenuItemActionPerformed
        activePanel.newFile();
        try {
            activePanel.getDoc().fromStreamUB(getClass().getResourceAsStream("/org/xbup/tool/editor/module/xbdoc_editor/resources/samples/xblogo.xbp"));
            activePanel.getDoc().processSpec();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBDocEditorFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        activePanel.reportStructureChange(null);
        activePanel.updateItem();
    }//GEN-LAST:event_fileOpenPicSampleMenuItemActionPerformed

    private void viewAsTreeMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_viewAsTreeMenuItemActionPerformed
        actionViewAsTree();
    }//GEN-LAST:event_viewAsTreeMenuItemActionPerformed

    private void viewAsTextMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_viewAsTextMenuItemActionPerformed
        actionViewAsText();
    }//GEN-LAST:event_viewAsTextMenuItemActionPerformed

    private void viewAsHexMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_viewAsHexMenuItemActionPerformed
        actionViewAsHex();
    }//GEN-LAST:event_viewAsHexMenuItemActionPerformed

    private void toolsCatalogBrowserMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_toolsCatalogBrowserMenuItemActionPerformed
        actionToolsCatalogBrowser();
    }//GEN-LAST:event_toolsCatalogBrowserMenuItemActionPerformed

    private void filePropertiesMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_filePropertiesMenuItemActionPerformed
        actionFileProperties();
    }//GEN-LAST:event_filePropertiesMenuItemActionPerformed

    private void fileNewCustomMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_fileNewCustomMenuItemActionPerformed

    }//GEN-LAST:event_fileNewCustomMenuItemActionPerformed

    private void viewPropertiesCheckBoxMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_viewPropertiesCheckBoxMenuItemActionPerformed
        actionViewProperties();
    }//GEN-LAST:event_viewPropertiesCheckBoxMenuItemActionPerformed

    private void optionsFontMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_optionsFontMenuItemActionPerformed
        FontDialog fontDialog = new FontDialog(mainFrameManagement.getFrame(), true);
        fontDialog.setIconImage(mainFrameManagement.getFrameIcon());
        fontDialog.setLocationRelativeTo(fontDialog.getParent());
        activePanel.showFontDialog(fontDialog);
    }//GEN-LAST:event_optionsFontMenuItemActionPerformed

    private void optionsColorsMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_optionsColorsMenuItemActionPerformed
        TextColorDialog textColorDialog = new TextColorDialog(mainFrameManagement.getFrame(), this, true);
        textColorDialog.setIconImage(mainFrameManagement.getFrameIcon());
        textColorDialog.setLocationRelativeTo(textColorDialog.getParent());
        textColorDialog.showDialog();
    }//GEN-LAST:event_optionsColorsMenuItemActionPerformed

    private void undoManagerMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_undoManagerMenuItemActionPerformed
        UndoManagerDialog undoManagerDialog = new UndoManagerDialog(mainFrameManagement.getFrame(), true, catalog);
        undoManagerDialog.setIconImage(mainFrameManagement.getFrameIcon());
        undoManagerDialog.setLocationRelativeTo(undoManagerDialog.getParent());

        XBTLinearUndo undoHandle = null;
        if (activePanel instanceof ActivePanelActionHandling) {
            Object objectUndoHandle = ((ActivePanelActionHandling) activePanel).getUndoHandle();
            if (objectUndoHandle instanceof XBTLinearUndo) {
                undoHandle = (XBTLinearUndo) objectUndoHandle;
                undoManagerDialog.setUndoHandle(undoHandle);
            }
        }

        undoManagerDialog.setVisible(true);
        if (undoManagerDialog.getDialogOption() == JOptionPane.OK_OPTION && undoHandle != null) {
            try {
                undoHandle.setCommandPosition(undoManagerDialog.getCommandPosition());
            } catch (Exception ex) {
                Logger.getLogger(XBDocEditorFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_undoManagerMenuItemActionPerformed

    public void actionFileOpen() {
        //if (!releaseFile()) return;
        if (openFC.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (openFC.getFileFilter().getDescription().equals(resourceBundle.getString("filter_file_xb"))) {
                activePanel.setFileMode(2);
            } else {
                activePanel.setFileMode(1);
            }
            loadFromFile(openFC.getSelectedFile().getAbsolutePath());
        }
    }

    private void loadFromFile(String fileName) {
        setFileName(fileName);
        activePanel.loadFromFile();
        activePanel.updateItem();
    }

    public void actionFileSave() {
        // TODO: Set button grayed when saved and no changes were made.
        // TODO: jButton3.setIcon(jButton3.getDisabledIcon());
        if ((!"".equals(activePanel.getFileName())) && (activePanel.getFileName() != null)) {
            activePanel.saveToFile();
        } else {
            actionFileSaveAs();
        }
    }

    public void actionFileSaveAs() {
        if (saveFC.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (new File(saveFC.getSelectedFile().getAbsolutePath()).exists()) {
                //if (!overwriteFile()) return;
            }
            setFileName(saveFC.getSelectedFile().getAbsolutePath());
            if (saveFC.getFileFilter().getDescription().equals(resourceBundle.getString("filter_file_xbt"))) {
                activePanel.setFileMode(2);
            } else {
                activePanel.setFileMode(1);
            }
            actionFileSave();
        }
    }

    public void actionEditFind() {
        initFindDialog();
        findDialog.setShallReplace(false);
        findDialog.setSelected();
        findDialog.setLocationRelativeTo(findDialog.getParent());
        findDialog.setVisible(true);
        if (findDialog.getDialogOption() == JOptionPane.OK_OPTION) {
            activePanel.findText(findDialog);
        }
    }

    public void actionEditReplace() {
        initFindDialog();
        findDialog.setShallReplace(true);
        findDialog.setSelected();
        findDialog.setLocationRelativeTo(findDialog.getParent());
        findDialog.setVisible(true);
        if (findDialog.getDialogOption() == JOptionPane.OK_OPTION) {
            activePanel.findText(findDialog);
        }
    }

    public void actionFilePrint() {
        activePanel.printFile();
    }

    public void actionEditGoto() {
        initGotoDialog();
        gotoDialog.setMaxLine(activePanel.getLineCount());
        gotoDialog.setCharPos(1);
        gotoDialog.setLocationRelativeTo(gotoDialog.getParent());
        gotoDialog.setVisible(true);
        if (gotoDialog.getDialogOption() == JOptionPane.OK_OPTION) {
            activePanel.gotoLine(gotoDialog.getLine());
            activePanel.gotoRelative(gotoDialog.getCharPos());
        }
    }

    public void actionEditFindAgain() {
        initFindDialog();
        findDialog.setShallReplace(false);
        activePanel.findText(findDialog);
    }

    public void initFindDialog() {
        if (findDialog == null) {
            findDialog = new FindTextDialog(this, true);
        }
    }

    public void initGotoDialog() {
        if (gotoDialog == null) {
            gotoDialog = new GotoDialog(this, true);
        }
    }

    public void actionFilePrinterSetup() {
        /*        PrinterJob job = PrinterJob.getPrinterJob();
         PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
         PageFormat pf = job.pageDialog(aset);
         job.setPrintable(new PrintDialogExample(), pf);
         boolean ok = job.printDialog(aset);
         if (ok) {
         try {
         job.print(aset);
         } catch (PrinterException ex) {
         Logger.getLogger(XBDocEditor.class.getExtensionName()).log(Level.SEVERE, null, ex);
         }
         } */
    }

    public void actionFileProperties() {
        DocPropertiesDialog filePropertiesDialog = new DocPropertiesDialog(this, true);
        filePropertiesDialog.setLocationRelativeTo(filePropertiesDialog.getParent());
        filePropertiesDialog.runDialog(activePanel.getDoc(), activePanel.getFileName());
    }

    public void setDocumentCharset(Charset charset) {
        activePanel.setCharset(charset);
        currentEncodingTextField.setText(charset.name());
    }

    public boolean isPropertyPanelEnabled() {
        return activePanel.isSplitMode();
    }

    public void setPropertyPanelEnabled(boolean b) {
        boolean old = isPropertyPanelEnabled();
        activePanel.setSplitMode(b);
        firePropertyChange("propertyPanelEnabled", old, isPropertyPanelEnabled());
    }

    public JPanel getStatusPanel() {
        return getStatusBar();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel activityPanel;
    private JProgressBar activityProgressBar;
    private JButton addToolButton;
    private JLabel connectionStatusLabel;
    private JPanel connectionStatusPanel;
    private JTextField currentEncodingTextField;
    private JMenuItem editFindAgainMenuItem;
    private JMenuItem editFindMenuItem;
    private JMenuItem editFindReplaceMenuItem;
    private JMenuItem editGotoMenuItem;
    private JMenuItem editItemAddMenuItem;
    private JMenuItem editItemModifyMenuItem;
    private JMenu editMenu;
    private JPanel emptyPanel;
    private JMenu fileMenu;
    private JMenuItem fileNewCustomMenuItem;
    private JMenuItem fileOpenPicSampleMenuItem;
    private JMenu fileOpenSampleMenu;
    private JMenuItem fileOpenXHTMLSampleMenuItem;
    private JMenuItem filePropertiesMenuItem;
    private JButton findToolButton;
    private JSeparator jSeparator12;
    private JPopupMenu.Separator jSeparator14;
    private JPopupMenu.Separator jSeparator15;
    private JPopupMenu.Separator jSeparator16;
    private JPopupMenu.Separator jSeparator17;
    private Separator jSeparator20;
    private JPopupMenu.Separator jSeparator21;
    private JPopupMenu.Separator jSeparator22;
    private JSeparator jSeparator3;
    private JSeparator jSeparator6;
    private JPanel labelPanel;
    private JPanel mainPanel;
    private JPopupMenu mainPopupMenu;
    private JMenuBar menuBar;
    private JButton modifyToolButton;
    private JPanel operationPanel;
    private JProgressBar operationProgressBar;
    private JButton operationStopButton;
    private JMenuItem optionsColorsMenuItem;
    private JMenuItem optionsFontMenuItem;
    private JMenu optionsMenu;
    private JMenuItem popupItemAddMenuItem;
    private JMenuItem popupItemExportMenuItem;
    private JMenuItem popupItemImportMenuItem;
    private JMenuItem popupItemModifyMenuItem;
    private JMenuItem popupItemOpenMenuItem;
    private JMenuItem popupItemPropertiesMenuItem;
    private JPanel statusBar;
    private JLabel statusLabel;
    private JPanel statusPanel;
    private JPanel textEditingPanel;
    private JTextField textPositionTextField;
    private JLabel textStatusLabel;
    private JToolBar toolBar;
    private JMenuItem toolsCatalogBrowserMenuItem;
    private JMenu toolsMenu;
    private JMenuItem undoManagerMenuItem;
    private JRadioButtonMenuItem viewAsHexMenuItem;
    private JRadioButtonMenuItem viewAsTextMenuItem;
    private JRadioButtonMenuItem viewAsTreeMenuItem;
    private ButtonGroup viewAsbuttonGroup;
    private JMenu viewMenu;
    private JCheckBoxMenuItem viewPropertiesCheckBoxMenuItem;
    private JCheckBoxMenuItem viewWordWrapCheckBoxMenuItem;
    // End of variables declaration//GEN-END:variables

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

    public class XBTFilesFilter extends FileFilter {

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
                return "xbt".contains(extension.substring(0, 3));
            }
            return false;
        }

        @Override
        public String getDescription() {
            return resourceBundle.getString("filter_file_xbt");
        }
    }

    public class TXTFilesFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = getExtension(f);
            if (extension != null) {
                return "txt".equals(extension);
            }
            return false;
        }

        @Override
        public String getDescription() {
            return resourceBundle.getString("filter_file_txt");
        }
    }

    /**
     * FileFilter for *.xb* files.
     */
    public class XBFileFilter extends FileFilter implements FileType {

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            String extension = getExtension(file);
            if (extension != null) {
                if (extension.length() >= 2) {
                    return extension.substring(0, 2).equals("xb");
                }
            }

            return false;
        }

        @Override
        public String getDescription() {
            return resourceBundle.getString("filter_file_xb");
        }

        @Override
        public String getFileTypeId() {
            return XBFILETYPE;
        }
    }

    public FileType newXBFileType() {
        return (FileType) new XBFileFilter();
    }

    public class AllFilesFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            return true;
        }

        @Override
        public String getDescription() {
            return resourceBundle.getString("filter_file_all");
        }
    }

    public boolean isAddEnabled() {
        if (activePanel == null) {
            return true;
        }
        return activePanel.isAddEnabled() && (activePanel.getMode() == XBDocumentPanel.PanelMode.TREE);
    }

    public void setEditorApp(XBEditorApp editorApp) {
        this.editorApp = editorApp;
    }

    public void postWindowOpened() {
        activePanel.postWindowOpened();
        initService();
    }

    public void actionItemAdd() {
        activePanel.performAdd();
    }

    public void actionItemModify() {
        activePanel.performModify();
    }

    public void actionItemProperties() {
        propertiesDialog = new BlockPropertiesDialog(this, true);
        propertiesDialog.setCatalog(catalog);
        propertiesDialog.setDevMode(devMode);
        propertiesDialog.runDialog(activePanel.getSelectedItem());
        propertiesDialog = null;
    }

    public void actionViewAsTree() {
        activePanel.setMode(XBDocumentPanel.PanelMode.TREE);
    }

    public void actionViewAsText() {
        activePanel.setMode(XBDocumentPanel.PanelMode.TEXT);
    }

    public void actionViewAsHex() {
        activePanel.setMode(XBDocumentPanel.PanelMode.HEX);
    }

    public void actionToolsCatalogBrowser() {
        catalogEditorDialog = new CatalogEditorDialog(mainFrameManagement.getFrame(), true);
        catalogEditorDialog.setMenuManagement(menuManagement);
        catalogEditorDialog.setMainFrameManagement(mainFrameManagement);
        catalogEditorDialog.setCatalog(catalog);
        catalogEditorDialog.setVisible(true);

        catalogEditorDialog = null;
    }

    public MenuManagement getMenuManagement() {
        return menuManagement;
    }

    public void setMenuManagement(MenuManagement menuManagement) {
        this.menuManagement = menuManagement;

        menuManagement.extendMenu(fileMenu, BasicMenuType.FILE, MenuPositionMode.PANEL);
        JMenu undoManagerMenu = new JMenu();
        undoManagerMenu.add(undoManagerMenuItem);
        menuManagement.extendMenu(undoManagerMenu, BasicMenuType.EDIT, MenuPositionMode.TOP);
        menuManagement.extendMenu(editMenu, BasicMenuType.EDIT, MenuPositionMode.PANEL);
        menuManagement.extendMenu(viewMenu, BasicMenuType.VIEW, MenuPositionMode.PANEL);
        menuManagement.extendMenu(optionsMenu, BasicMenuType.OPTIONS, MenuPositionMode.PANEL);
        menuManagement.extendMenu(toolsMenu, BasicMenuType.TOOLS, MenuPositionMode.PANEL);
        menuManagement.extendToolBar(toolBar);
        menuManagement.insertMainPopupMenu(mainPopupMenu, 4);
    }

    public void actionViewProperties() {
        setPropertyPanelEnabled(!isPropertyPanelEnabled());
    }

    private enum ConnectionStatus {

        DISCONNECTED,
        LOCAL,
        CONNECTING,
        NETWORK,
        INTERNET
    };

    private static final String[] connectionStatusIcons = {
        "/org/xbup/tool/editor/module/xbdoc_editor/resources/images/status/network-offline.png",
        "/org/xbup/tool/editor/module/xbdoc_editor/resources/images/status/computer.png",
        "/org/xbup/tool/editor/module/xbdoc_editor/resources/images/status/network-idle.png",
        "/org/xbup/tool/editor/module/xbdoc_editor/resources/images/status/network-server.png",
        "/org/xbup/tool/editor/module/xbdoc_editor/resources/images/status/internet-web-browser.png"
    };

    private void setConnectionStatus(ConnectionStatus status) {
        connectionStatusLabel.setIcon(new ImageIcon(getClass().getResource(connectionStatusIcons[status.ordinal()])));
        connectionStatusLabel.setToolTipText(resourceBundle.getString("connectionStatusLabel.toolTipText" + String.valueOf(status.ordinal())));
    }

    public class CatalogEditorDialog extends JDialog implements CatalogManagerPanelable {

        private final CatalogEditorPanel catalogEditorPanel;
        private final CatalogAvailabilityPanel catalogAvailabilityPanel;
        private XBACatalog catalog = null;

        public CatalogEditorDialog(Frame owner, boolean modal) {
            super(owner, modal);
            setTitle("Catalog Browser");
            setSize(900, 600);
            setIconImage(mainFrameManagement.getFrameIcon());
            setLocationRelativeTo(mainFrameManagement.getFrame());
            catalogEditorPanel = new CatalogEditorPanel();
            catalogAvailabilityPanel = new CatalogAvailabilityPanel();

            getContentPane().add(catalogAvailabilityPanel, BorderLayout.CENTER);
        }

        @Override
        public void setCatalog(XBACatalog catalog) {
            if (this.catalog == null && catalog != null) {
                catalogAvailabilityPanel.setCatalog(catalog);
                catalogEditorPanel.setCatalog(catalog);
                getContentPane().removeAll();
                getContentPane().add(catalogEditorPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
            } else if (this.catalog != null && catalog == null) {
                catalogAvailabilityPanel.setCatalog(catalog);
                getContentPane().removeAll();
                getContentPane().add(catalogAvailabilityPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
                catalogEditorPanel.setCatalog(catalog);
            } else {
                catalogEditorPanel.setCatalog(catalog);
            }

            this.catalog = catalog;
        }

        @Override
        public void setMenuManagement(MenuManagement menuManagement) {
            catalogEditorPanel.setMenuManagement(menuManagement);
        }

        @Override
        public void setMainFrameManagement(MainFrameManagement mainFrameManagement) {
            catalogEditorPanel.setMainFrameManagement(mainFrameManagement);
        }
    }
}
