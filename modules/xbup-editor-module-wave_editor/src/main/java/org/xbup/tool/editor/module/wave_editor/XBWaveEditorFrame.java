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
package org.xbup.tool.editor.module.wave_editor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.Arrays;
import java.util.ResourceBundle;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import org.xbup.lib.audio.swing.XBWavePanel;
import org.xbup.tool.editor.module.wave_editor.dialog.WaveColorDialog;
import org.xbup.tool.editor.module.wave_editor.panel.AudioPanel;
import org.xbup.tool.editor.module.wave_editor.panel.WaveColorPanelFrame;
import org.xbup.tool.editor.base.api.FileType;
import org.xbup.tool.editor.base.api.utils.WindowUtils;

/**
 * XBSEditor Main Frame.
 *
 * @version 0.1.24 2014/11/08
 * @author XBUP Project (http://xbup.org)
 */
public class XBWaveEditorFrame extends javax.swing.JFrame implements WaveColorPanelFrame {

    private AudioPanel activePanel;
    private final MouseMotionListener mouseMotionListener;
    private ResourceBundle resourceBundle;
    private final String DIALOG_MENU_SUFIX = "...";
    private boolean playing = false;

    public static final String XBSFILETYPE = "XBWaveEditor.XBSFileFilter";

    public XBWaveEditorFrame() {
        resourceBundle = ResourceBundle.getBundle("org/xbup/tool/editor/module/wave_editor/resources/XBWaveEditorFrame");

        initComponents();
        activePanel = new AudioPanel();
        activePanel.addStatusChangeListener(new AudioPanel.StatusChangeListener() {
            @Override
            public void statusChanged() {
                updateStatus();
            }
        });
        activePanel.addWaveRepaintListener(new AudioPanel.WaveRepaintListener() {
            @Override
            public void waveRepaint() {
                currentTimeTextField.setText(activePanel.getPositionTime());
            }
        });

        // ((CardLayout) statusPanel.getLayout()).show(statusPanel,"default");
        mainPanel.add(activePanel, java.awt.BorderLayout.CENTER);

        /*        // Open file from command line
         String fileName = ((XBSEditor) app).getFileName();
         if (!"".equals(fileName)) {
         setFileName(fileName);
         activePanel.loadFromFile();
         } */
        // Caret position listener
        mouseMotionListener = new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (activePanel == null) {
                    return;
                }

                currentTimeTextField.setText(activePanel.getPositionTime());
            }
        };

        activePanel.attachCaretListener(mouseMotionListener);
        activePanel.setPopupMenu(mainPopupMenu);
    }

    /**
     * @return the activePanel
     */
    public AudioPanel getActivePanel() {
        return activePanel;
    }

    /**
     * @param activePanel the activePanel to set
     */
    public void setActivePanel(AudioPanel activePanel) {
        this.activePanel = activePanel;
    }

    @Override
    public Color[] getCurrentWaveColors() {
        return activePanel.getAudioPanelColors();
    }

    @Override
    public Color[] getDefaultWaveColors() {
        return activePanel.getDefaultColors();
    }

    @Override
    public void setCurrentWaveColors(Color[] colors) {
        activePanel.setAudioPanelColors(colors);
    }

    /**
     * @return the mainPopupMenu
     */
    public javax.swing.JPopupMenu getMainPopupMenu() {
        return mainPopupMenu;
    }

    private void updateStatus() {
        currentTimeTextField.setText(activePanel.getPositionTime());

        if (activePanel.getIsPlaying() != playing) {
            playing = !playing;
            playButton.setIcon(playing
                    ? new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/wave_editor/resources/images/actions/pause16.png"))
                    : new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/wave_editor/resources/images/actions/play16.png"))
            );
        }
    }

    public boolean isEditEnabled() {
        if (activePanel == null) {
            return false;
        }

        return activePanel.isEditEnabled();
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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        mainPanel = new javax.swing.JPanel();
        toolBarPanel = new javax.swing.JPanel();
        statusBar = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusPanel = new javax.swing.JPanel();
        waveStatusPanel = new javax.swing.JPanel();
        currentTimeTextField = new javax.swing.JTextField();
        playButton = new javax.swing.JButton();
        volumeSlider = new javax.swing.JSlider();
        stopButton = new javax.swing.JButton();
        mainStatusPanel = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();
        operationStatusPanel = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton4 = new javax.swing.JButton();
        activityStatusPanel = new javax.swing.JPanel();
        jProgressBar2 = new javax.swing.JProgressBar();
        mainPopupMenu = new javax.swing.JPopupMenu();
        jSeparator7 = new javax.swing.JSeparator();
        viewDrawModePopupMenu = new javax.swing.JMenu();
        viewDrawModeDotsPopupMenuItem = new javax.swing.JRadioButtonMenuItem();
        viewDrawModeLinesPopupMenuItem = new javax.swing.JRadioButtonMenuItem();
        viewDrawModeIntegralPopupMenuItem = new javax.swing.JRadioButtonMenuItem();
        zoomButtonGroup = new javax.swing.ButtonGroup();
        toolsModeButtonGroup = new javax.swing.ButtonGroup();
        drawModeButtonGroup = new javax.swing.ButtonGroup();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JSeparator();
        filePrintMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        viewDrawModeMenu = new javax.swing.JMenu();
        viewDrawModeDotsMenuItem = new javax.swing.JRadioButtonMenuItem();
        viewDrawModeLinesMenuItem = new javax.swing.JRadioButtonMenuItem();
        viewDrawModeIntegralMenuItem = new javax.swing.JRadioButtonMenuItem();
        viewZoomMenu = new javax.swing.JMenu();
        viewZoom25MenuItem = new javax.swing.JRadioButtonMenuItem();
        viewZoom50MenuItem = new javax.swing.JRadioButtonMenuItem();
        viewZoom100MenuItem = new javax.swing.JRadioButtonMenuItem();
        viewZoom200MenuItem = new javax.swing.JRadioButtonMenuItem();
        viewZoom400MenuItem = new javax.swing.JRadioButtonMenuItem();
        audioMenu = new javax.swing.JMenu();
        audioPlayMenuItem = new javax.swing.JMenuItem();
        audioStopMenuItem = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        audioResizeMenuItem = new javax.swing.JMenuItem();
        audioTransformMenu = new javax.swing.JMenu();
        audioTransformMirrorMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        toolSelectionMenuItem = new javax.swing.JRadioButtonMenuItem();
        toolPencilMenuItem = new javax.swing.JRadioButtonMenuItem();
        optionsMenu = new javax.swing.JMenu();
        optionsColorsMenuItem = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setLayout(new java.awt.BorderLayout());

        toolBarPanel.setName("toolBarPanel"); // NOI18N
        toolBarPanel.setLayout(new java.awt.BorderLayout());
        mainPanel.add(toolBarPanel, java.awt.BorderLayout.NORTH);

        statusBar.setName("statusBar"); // NOI18N
        statusBar.setLayout(new java.awt.BorderLayout());

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N
        statusBar.add(statusPanelSeparator, java.awt.BorderLayout.NORTH);

        statusPanel.setName("statusPanel"); // NOI18N
        statusPanel.setPreferredSize(new java.awt.Dimension(649, 26));
        statusPanel.setRequestFocusEnabled(false);
        statusPanel.setLayout(new java.awt.CardLayout());

        waveStatusPanel.setName("waveStatusPanel"); // NOI18N

        currentTimeTextField.setEditable(false);
        currentTimeTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        currentTimeTextField.setText("0:00.00"); // NOI18N
        currentTimeTextField.setToolTipText("Current position of cursor MIN:SEC.DEC");
        currentTimeTextField.setName("currentTimeTextField"); // NOI18N

        playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/wave_editor/resources/images/actions/play16.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/tool/editor/module/wave_editor/resources/XBWaveEditorFrame"); // NOI18N
        playButton.setToolTipText(bundle.getString("playButton.toolTipText")); // NOI18N
        playButton.setFocusable(false);
        playButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        playButton.setName("playButton"); // NOI18N
        playButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        volumeSlider.setToolTipText("Volume control");
        volumeSlider.setValue(100);
        volumeSlider.setFocusable(false);
        volumeSlider.setName("volumeSlider"); // NOI18N
        volumeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                volumeSliderStateChanged(evt);
            }
        });

        stopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/wave_editor/resources/images/actions/stop16.png"))); // NOI18N
        stopButton.setToolTipText(bundle.getString("stopButton.toolTipText")); // NOI18N
        stopButton.setFocusable(false);
        stopButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stopButton.setName("stopButton"); // NOI18N
        stopButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout waveStatusPanelLayout = new javax.swing.GroupLayout(waveStatusPanel);
        waveStatusPanel.setLayout(waveStatusPanelLayout);
        waveStatusPanelLayout.setHorizontalGroup(
            waveStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(waveStatusPanelLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentTimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 338, Short.MAX_VALUE)
                .addComponent(volumeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        waveStatusPanelLayout.setVerticalGroup(
            waveStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(playButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(currentTimeTextField)
            .addComponent(volumeSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(stopButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        statusPanel.add(waveStatusPanel, "default");

        mainStatusPanel.setName("mainStatusPanel"); // NOI18N
        mainStatusPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        statusLabel.setName("statusLabel"); // NOI18N
        mainStatusPanel.add(statusLabel);

        statusPanel.add(mainStatusPanel, "main");

        operationStatusPanel.setName("operationStatusPanel"); // NOI18N

        jProgressBar1.setIndeterminate(true);
        jProgressBar1.setMinimumSize(new java.awt.Dimension(10, 10));
        jProgressBar1.setName("jProgressBar1"); // NOI18N
        jProgressBar1.setRequestFocusEnabled(false);
        jProgressBar1.setStringPainted(true);

        jButton4.setText("Cancel");
        jButton4.setEnabled(false);
        jButton4.setMinimumSize(new java.awt.Dimension(67, 15));
        jButton4.setName("jButton4"); // NOI18N
        jButton4.setPreferredSize(new java.awt.Dimension(75, 20));

        javax.swing.GroupLayout operationStatusPanelLayout = new javax.swing.GroupLayout(operationStatusPanel);
        operationStatusPanel.setLayout(operationStatusPanelLayout);
        operationStatusPanelLayout.setHorizontalGroup(
            operationStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, operationStatusPanelLayout.createSequentialGroup()
                .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        operationStatusPanelLayout.setVerticalGroup(
            operationStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(operationStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE))
        );

        statusPanel.add(operationStatusPanel, "initCat");

        activityStatusPanel.setName("activityStatusPanel"); // NOI18N

        jProgressBar2.setIndeterminate(true);
        jProgressBar2.setName("jProgressBar2"); // NOI18N
        jProgressBar2.setRequestFocusEnabled(false);
        jProgressBar2.setString("Updating catalog");
        jProgressBar2.setStringPainted(true);

        javax.swing.GroupLayout activityStatusPanelLayout = new javax.swing.GroupLayout(activityStatusPanel);
        activityStatusPanel.setLayout(activityStatusPanelLayout);
        activityStatusPanelLayout.setHorizontalGroup(
            activityStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jProgressBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
        );
        activityStatusPanelLayout.setVerticalGroup(
            activityStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jProgressBar2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        statusPanel.add(activityStatusPanel, "updateCat");

        statusBar.add(statusPanel, java.awt.BorderLayout.SOUTH);

        mainPopupMenu.setName("mainPopupMenu"); // NOI18N

        jSeparator7.setName("jSeparator7"); // NOI18N
        mainPopupMenu.add(jSeparator7);

        viewDrawModePopupMenu.setText(bundle.getString("viewDrawModePopupMenu.text")); // NOI18N
        viewDrawModePopupMenu.setName("viewDrawModePopupMenu"); // NOI18N

        viewDrawModeDotsPopupMenuItem.setText(bundle.getString("actionDrawModeDots.Action.text")); // NOI18N
        viewDrawModeDotsPopupMenuItem.setToolTipText(bundle.getString("actionDrawModeDots.Action.shortDescription")); // NOI18N
        viewDrawModeDotsPopupMenuItem.setName("viewDrawModeDotsPopupMenuItem"); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, viewDrawModeDotsMenuItem, org.jdesktop.beansbinding.ELProperty.create("${selected}"), viewDrawModeDotsPopupMenuItem, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        viewDrawModeDotsPopupMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewDrawModeDotsMenuItemActionPerformed(evt);
            }
        });
        viewDrawModePopupMenu.add(viewDrawModeDotsPopupMenuItem);

        viewDrawModeLinesPopupMenuItem.setText(bundle.getString("actionDrawModeLines.Action.text")); // NOI18N
        viewDrawModeLinesPopupMenuItem.setToolTipText(bundle.getString("actionDrawModeLines.Action.shortDescription")); // NOI18N
        viewDrawModeLinesPopupMenuItem.setName("viewDrawModeLinesPopupMenuItem"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, viewDrawModeLinesMenuItem, org.jdesktop.beansbinding.ELProperty.create("${selected}"), viewDrawModeLinesPopupMenuItem, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        viewDrawModeLinesPopupMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewDrawModeLinesMenuItemActionPerformed(evt);
            }
        });
        viewDrawModePopupMenu.add(viewDrawModeLinesPopupMenuItem);

        viewDrawModeIntegralPopupMenuItem.setText(bundle.getString("actionDrawModeIntegral.Action.text")); // NOI18N
        viewDrawModeIntegralPopupMenuItem.setToolTipText(bundle.getString("actionDrawModeIntegral.Action.shortDescription")); // NOI18N
        viewDrawModeIntegralPopupMenuItem.setName("viewDrawModeIntegralPopupMenuItem"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, viewDrawModeIntegralMenuItem, org.jdesktop.beansbinding.ELProperty.create("${selected}"), viewDrawModeIntegralPopupMenuItem, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        viewDrawModeIntegralPopupMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewDrawModeIntegralMenuItemActionPerformed(evt);
            }
        });
        viewDrawModePopupMenu.add(viewDrawModeIntegralPopupMenuItem);

        mainPopupMenu.add(viewDrawModePopupMenu);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText("File"); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        filePrintMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        filePrintMenuItem.setText(bundle.getString("filePrintMenuItem.text")); // NOI18N
        filePrintMenuItem.setToolTipText(bundle.getString("filePrintMenuItem.toolTipText")); // NOI18N
        filePrintMenuItem.setName("filePrintMenuItem"); // NOI18N
        filePrintMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filePrintMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(filePrintMenuItem);

        menuBar.add(fileMenu);

        viewMenu.setText("View");
        viewMenu.setName("viewMenu"); // NOI18N

        jSeparator3.setName("jSeparator3"); // NOI18N
        viewMenu.add(jSeparator3);

        viewDrawModeMenu.setText(bundle.getString("viewDrawModeMenu.text")); // NOI18N
        viewDrawModeMenu.setName("viewDrawModeMenu"); // NOI18N

        drawModeButtonGroup.add(viewDrawModeDotsMenuItem);
        viewDrawModeDotsMenuItem.setText(bundle.getString("actionDrawModeDots.Action.text")); // NOI18N
        viewDrawModeDotsMenuItem.setToolTipText(bundle.getString("actionDrawModeDots.Action.shortDescription")); // NOI18N
        viewDrawModeDotsMenuItem.setName("viewDrawModeDotsMenuItem"); // NOI18N
        viewDrawModeDotsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewDrawModeDotsMenuItemActionPerformed(evt);
            }
        });
        viewDrawModeMenu.add(viewDrawModeDotsMenuItem);

        drawModeButtonGroup.add(viewDrawModeLinesMenuItem);
        viewDrawModeLinesMenuItem.setSelected(true);
        viewDrawModeLinesMenuItem.setText(bundle.getString("actionDrawModeLines.Action.text")); // NOI18N
        viewDrawModeLinesMenuItem.setToolTipText(bundle.getString("actionDrawModeLines.Action.shortDescription")); // NOI18N
        viewDrawModeLinesMenuItem.setName("viewDrawModeLinesMenuItem"); // NOI18N
        viewDrawModeLinesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewDrawModeLinesMenuItemActionPerformed(evt);
            }
        });
        viewDrawModeMenu.add(viewDrawModeLinesMenuItem);

        drawModeButtonGroup.add(viewDrawModeIntegralMenuItem);
        viewDrawModeIntegralMenuItem.setText(bundle.getString("actionDrawModeIntegral.Action.text")); // NOI18N
        viewDrawModeIntegralMenuItem.setToolTipText(bundle.getString("actionDrawModeIntegral.Action.shortDescription")); // NOI18N
        viewDrawModeIntegralMenuItem.setName("viewDrawModeIntegralMenuItem"); // NOI18N
        viewDrawModeIntegralMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewDrawModeIntegralMenuItemActionPerformed(evt);
            }
        });
        viewDrawModeMenu.add(viewDrawModeIntegralMenuItem);

        viewMenu.add(viewDrawModeMenu);

        viewZoomMenu.setText(bundle.getString("viewZoomMenu.text")); // NOI18N
        viewZoomMenu.setName("viewZoomMenu"); // NOI18N

        zoomButtonGroup.add(viewZoom25MenuItem);
        viewZoom25MenuItem.setText(bundle.getString("viewZoom25.Action.text")); // NOI18N
        viewZoom25MenuItem.setToolTipText(bundle.getString("viewZoom25.Action.shortDescription")); // NOI18N
        viewZoom25MenuItem.setName("viewZoom25MenuItem"); // NOI18N
        viewZoom25MenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewZoom25MenuItemActionPerformed(evt);
            }
        });
        viewZoomMenu.add(viewZoom25MenuItem);

        zoomButtonGroup.add(viewZoom50MenuItem);
        viewZoom50MenuItem.setText(bundle.getString("viewZoom50.Action.text")); // NOI18N
        viewZoom50MenuItem.setToolTipText(bundle.getString("viewZoom50.Action.shortDescription")); // NOI18N
        viewZoom50MenuItem.setName("viewZoom50MenuItem"); // NOI18N
        viewZoom50MenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewZoom50MenuItemActionPerformed(evt);
            }
        });
        viewZoomMenu.add(viewZoom50MenuItem);

        zoomButtonGroup.add(viewZoom100MenuItem);
        viewZoom100MenuItem.setSelected(true);
        viewZoom100MenuItem.setText(bundle.getString("viewZoom100.Action.text")); // NOI18N
        viewZoom100MenuItem.setToolTipText(bundle.getString("viewZoom200.Action.shortDescription")); // NOI18N
        viewZoom100MenuItem.setName("viewZoom100MenuItem"); // NOI18N
        viewZoom100MenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewZoom100MenuItemActionPerformed(evt);
            }
        });
        viewZoomMenu.add(viewZoom100MenuItem);

        zoomButtonGroup.add(viewZoom200MenuItem);
        viewZoom200MenuItem.setText(bundle.getString("viewZoom200.Action.text")); // NOI18N
        viewZoom200MenuItem.setToolTipText(bundle.getString("viewZoom200.Action.shortDescription")); // NOI18N
        viewZoom200MenuItem.setName("viewZoom200MenuItem"); // NOI18N
        viewZoom200MenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewZoom200MenuItemActionPerformed(evt);
            }
        });
        viewZoomMenu.add(viewZoom200MenuItem);

        zoomButtonGroup.add(viewZoom400MenuItem);
        viewZoom400MenuItem.setText(bundle.getString("viewZoom400.Action.text")); // NOI18N
        viewZoom400MenuItem.setToolTipText(bundle.getString("viewZoom400.Action.shortDescription")); // NOI18N
        viewZoom400MenuItem.setName("viewZoom400MenuItem"); // NOI18N
        viewZoom400MenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewZoom400MenuItemActionPerformed(evt);
            }
        });
        viewZoomMenu.add(viewZoom400MenuItem);

        viewMenu.add(viewZoomMenu);

        menuBar.add(viewMenu);

        audioMenu.setText("Audio");
        audioMenu.setName("audioMenu"); // NOI18N

        audioPlayMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE, 0));
        audioPlayMenuItem.setText(bundle.getString("audioPlayMenuItem.text")); // NOI18N
        audioPlayMenuItem.setToolTipText(bundle.getString("audioPlayMenuItem.toolTipText")); // NOI18N
        audioPlayMenuItem.setName("audioPlayMenuItem"); // NOI18N
        audioPlayMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                audioPlayMenuItemActionPerformed(evt);
            }
        });
        audioMenu.add(audioPlayMenuItem);

        audioStopMenuItem.setText(bundle.getString("audioStopMenuItem.text")); // NOI18N
        audioStopMenuItem.setToolTipText(bundle.getString("audioStopMenuItem.toolTipText")); // NOI18N
        audioStopMenuItem.setName("audioStopMenuItem"); // NOI18N
        audioStopMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                audioStopMenuItemActionPerformed(evt);
            }
        });
        audioMenu.add(audioStopMenuItem);

        jSeparator6.setName("jSeparator6"); // NOI18N
        audioMenu.add(jSeparator6);

        audioResizeMenuItem.setText(bundle.getString("audioResizeMenuItem.text")); // NOI18N
        audioResizeMenuItem.setName("audioResizeMenuItem"); // NOI18N
        audioMenu.add(audioResizeMenuItem);

        audioTransformMenu.setText(bundle.getString("audioTransformMenu.text")); // NOI18N
        audioTransformMenu.setEnabled(false);
        audioTransformMenu.setName("audioTransformMenu"); // NOI18N

        audioTransformMirrorMenuItem.setText(bundle.getString("audioTransformMirrorMenuItem.text")); // NOI18N
        audioTransformMirrorMenuItem.setName("audioTransformMirrorMenuItem"); // NOI18N
        audioTransformMenu.add(audioTransformMirrorMenuItem);

        audioMenu.add(audioTransformMenu);

        menuBar.add(audioMenu);

        toolsMenu.setText(bundle.getString("toolsMenu.text")); // NOI18N
        toolsMenu.setName("toolsMenu"); // NOI18N

        toolsModeButtonGroup.add(toolSelectionMenuItem);
        toolSelectionMenuItem.setSelected(true);
        toolSelectionMenuItem.setText(bundle.getString("toolSelectionMenuItem.text")); // NOI18N
        toolSelectionMenuItem.setName("toolSelectionMenuItem"); // NOI18N
        toolSelectionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolSelectionMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(toolSelectionMenuItem);

        toolsModeButtonGroup.add(toolPencilMenuItem);
        toolPencilMenuItem.setText(bundle.getString("toolPencilMenuItem.text")); // NOI18N
        toolPencilMenuItem.setName("toolPencilMenuItem"); // NOI18N
        toolPencilMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolPencilMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(toolPencilMenuItem);

        menuBar.add(toolsMenu);

        optionsMenu.setText(bundle.getString("optionsMenu.text")); // NOI18N
        optionsMenu.setName("optionsMenu"); // NOI18N

        optionsColorsMenuItem.setText(bundle.getString("optionsColorsMenuItem.text")); // NOI18N
        optionsColorsMenuItem.setToolTipText(bundle.getString("optionsColorsMenuItem.toolTipText")); // NOI18N
        optionsColorsMenuItem.setName("optionsColorsMenuItem"); // NOI18N
        optionsColorsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsColorsMenuItemActionPerformed(evt);
            }
        });
        optionsMenu.add(optionsColorsMenuItem);

        menuBar.add(optionsMenu);

        for (JMenuItem item : Arrays.asList(filePrintMenuItem, audioResizeMenuItem, optionsColorsMenuItem)) {
            item.setText(item.getText()+DIALOG_MENU_SUFIX);
        }

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 469, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 306, Short.MAX_VALUE)
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void optionsColorsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsColorsMenuItemActionPerformed
        WaveColorDialog dlg = new WaveColorDialog(getFrame(), true, this);
        dlg.setLocationRelativeTo(dlg.getParent());
        dlg.showDialog();
        activePanel.repaint();
        //        activePanel.setToolColor(dlg.getColorPanel().getTextColor());
    }//GEN-LAST:event_optionsColorsMenuItemActionPerformed

    private void filePrintMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filePrintMenuItemActionPerformed
        activePanel.printFile();
    }//GEN-LAST:event_filePrintMenuItemActionPerformed

    private void viewZoom25MenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewZoom25MenuItemActionPerformed
        activePanel.scale(0.25);
    }//GEN-LAST:event_viewZoom25MenuItemActionPerformed

    private void viewZoom50MenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewZoom50MenuItemActionPerformed
        activePanel.scale(0.5);
    }//GEN-LAST:event_viewZoom50MenuItemActionPerformed

    private void viewZoom100MenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewZoom100MenuItemActionPerformed
        activePanel.scale(1);
    }//GEN-LAST:event_viewZoom100MenuItemActionPerformed

    private void viewZoom200MenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewZoom200MenuItemActionPerformed
        activePanel.scale(2);
    }//GEN-LAST:event_viewZoom200MenuItemActionPerformed

    private void viewZoom400MenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewZoom400MenuItemActionPerformed
        activePanel.scale(4);
    }//GEN-LAST:event_viewZoom400MenuItemActionPerformed

    private void audioPlayMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_audioPlayMenuItemActionPerformed
        activePanel.performPlay();
    }//GEN-LAST:event_audioPlayMenuItemActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        activePanel.performPlay();
    }//GEN-LAST:event_playButtonActionPerformed

    private void audioStopMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_audioStopMenuItemActionPerformed
        activePanel.performStop();
    }//GEN-LAST:event_audioStopMenuItemActionPerformed

    private void toolSelectionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolSelectionMenuItemActionPerformed
        activePanel.setToolMode(XBWavePanel.ToolMode.SELECTION);
    }//GEN-LAST:event_toolSelectionMenuItemActionPerformed

    private void toolPencilMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolPencilMenuItemActionPerformed
        activePanel.setToolMode(XBWavePanel.ToolMode.PENCIL);
    }//GEN-LAST:event_toolPencilMenuItemActionPerformed

    private void viewDrawModeDotsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewDrawModeDotsMenuItemActionPerformed
        activePanel.setDrawMode(XBWavePanel.DrawMode.DOTS_MODE);
    }//GEN-LAST:event_viewDrawModeDotsMenuItemActionPerformed

    private void viewDrawModeLinesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewDrawModeLinesMenuItemActionPerformed
        activePanel.setDrawMode(XBWavePanel.DrawMode.LINE_MODE);
    }//GEN-LAST:event_viewDrawModeLinesMenuItemActionPerformed

    private void viewDrawModeIntegralMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewDrawModeIntegralMenuItemActionPerformed
        activePanel.setDrawMode(XBWavePanel.DrawMode.INTEGRAL_MODE);
    }//GEN-LAST:event_viewDrawModeIntegralMenuItemActionPerformed

    private void volumeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_volumeSliderStateChanged
        activePanel.setVolume(volumeSlider.getValue());
    }//GEN-LAST:event_volumeSliderStateChanged

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        activePanel.performStop();
    }//GEN-LAST:event_stopButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        WindowUtils.invokeWindow(new XBWaveEditorFrame());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel activityStatusPanel;
    public javax.swing.JMenu audioMenu;
    private javax.swing.JMenuItem audioPlayMenuItem;
    private javax.swing.JMenuItem audioResizeMenuItem;
    private javax.swing.JMenuItem audioStopMenuItem;
    private javax.swing.JMenu audioTransformMenu;
    private javax.swing.JMenuItem audioTransformMirrorMenuItem;
    private javax.swing.JTextField currentTimeTextField;
    private javax.swing.ButtonGroup drawModeButtonGroup;
    public javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem filePrintMenuItem;
    private javax.swing.JButton jButton4;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPopupMenu mainPopupMenu;
    private javax.swing.JPanel mainStatusPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel operationStatusPanel;
    private javax.swing.JMenuItem optionsColorsMenuItem;
    public javax.swing.JMenu optionsMenu;
    private javax.swing.JButton playButton;
    private javax.swing.JPanel statusBar;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JButton stopButton;
    private javax.swing.JPanel toolBarPanel;
    private javax.swing.JRadioButtonMenuItem toolPencilMenuItem;
    private javax.swing.JRadioButtonMenuItem toolSelectionMenuItem;
    public javax.swing.JMenu toolsMenu;
    private javax.swing.ButtonGroup toolsModeButtonGroup;
    private javax.swing.JRadioButtonMenuItem viewDrawModeDotsMenuItem;
    private javax.swing.JRadioButtonMenuItem viewDrawModeDotsPopupMenuItem;
    private javax.swing.JRadioButtonMenuItem viewDrawModeIntegralMenuItem;
    private javax.swing.JRadioButtonMenuItem viewDrawModeIntegralPopupMenuItem;
    private javax.swing.JRadioButtonMenuItem viewDrawModeLinesMenuItem;
    private javax.swing.JRadioButtonMenuItem viewDrawModeLinesPopupMenuItem;
    private javax.swing.JMenu viewDrawModeMenu;
    private javax.swing.JMenu viewDrawModePopupMenu;
    public javax.swing.JMenu viewMenu;
    private javax.swing.JRadioButtonMenuItem viewZoom100MenuItem;
    private javax.swing.JRadioButtonMenuItem viewZoom200MenuItem;
    private javax.swing.JRadioButtonMenuItem viewZoom25MenuItem;
    private javax.swing.JRadioButtonMenuItem viewZoom400MenuItem;
    private javax.swing.JRadioButtonMenuItem viewZoom50MenuItem;
    private javax.swing.JMenu viewZoomMenu;
    private javax.swing.JSlider volumeSlider;
    private javax.swing.JPanel waveStatusPanel;
    private javax.swing.ButtonGroup zoomButtonGroup;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
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
            return resourceBundle.getString("filter_file_xbs");
        }

        @Override
        public String getFileTypeId() {
            return XBSFILETYPE;
        }
    }

    public FileType newXBSFileType() {
        return new XBWaveEditorFrame.XBSFileType();
    }

    protected javax.swing.JFrame getFrame() {
        return null;
    }

    public JPanel getStatusPanel() {
        return statusPanel;
    }
}
