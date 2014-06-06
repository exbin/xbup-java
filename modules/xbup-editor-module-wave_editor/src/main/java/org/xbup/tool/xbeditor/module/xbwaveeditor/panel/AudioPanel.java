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
package org.xbup.tool.xbeditor.module.xbwaveeditor.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.undo.UndoManager;
import org.xbup.lib.xb.block.XBBasicBlockType;
import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.block.XBFixedBlockType;
import org.xbup.lib.xb.block.declaration.XBBlockDecl;
import org.xbup.lib.xb.block.declaration.XBContext;
import org.xbup.lib.xb.block.declaration.XBDeclaration;
import org.xbup.lib.xb.block.declaration.XBFormatDecl;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.basic.convert.XBTEncapsulator;
import org.xbup.lib.xb.parser.token.event.convert.XBTEventListenerToListener;
import org.xbup.lib.xb.parser.token.event.convert.XBTListenerToEventListener;
import org.xbup.lib.xb.parser.token.event.convert.XBTToXBEventConvertor;
import org.xbup.lib.xb.parser.token.pull.XBPullReader;
import org.xbup.lib.xb.parser.token.pull.convert.XBToXBTPullConvertor;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationFromXB;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.child.XBTChildListener;
import org.xbup.lib.xb.serial.child.XBTChildListenerSerialHandler;
import org.xbup.lib.xb.serial.child.XBTChildListenerSerialMethod;
import org.xbup.lib.xb.serial.child.XBTChildProvider;
import org.xbup.lib.xb.serial.sequence.XBSerialSequence;
import org.xbup.lib.xb.serial.child.XBTChildProviderSerialHandler;
import org.xbup.lib.xb.serial.child.XBTChildProviderSerialMethod;
import org.xbup.lib.xb.serial.sequence.XBTSerialSequence;
import org.xbup.lib.xb.serial.sequence.XBTSerialSequenceListenerMethod;
import org.xbup.lib.xb.serial.sequence.XBTSerialSequenceProviderMethod;
import org.xbup.lib.xb.stream.file.XBFileOutputStream;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.type.UBNat32;
import org.xbup.lib.xbaudio.swing.XBWavePanel;
import org.xbup.lib.xbaudio.wave.XBWave;
import org.xbup.tool.xbeditor.module.xbwaveeditor.XBWaveEditorFrame;
import org.xbup.tool.xbeditor.module.xbwaveeditor.dialog.ImageResizeDialog;
import org.xbup.tool.xbeditorbase.base.api.ApplicationFilePanel;
import org.xbup.tool.xbeditorbase.base.api.FileType;

/**
 * XBSEditor audio panel.
 *
 * @version 0.1 wr23.0 2014/03/04
 * @author XBUP Project (http://xbup.org)
 */
public class AudioPanel extends javax.swing.JPanel implements ApplicationFilePanel {

    final UndoManager undo;
    private String fileName;
    private String ext;
    private javax.sound.sampled.AudioFileFormat.Type audioFormatType;
    private FileType fileType;
    private boolean modified = false;
    private boolean wavePlayed = false;
    private int drawPosition = -1;
    private int wavePosition = -1;

    private final PlayThread playThread;
    private final WavePaintThread wavePaintThread;

    private double scaleRatio;
    private Color[] defaultColors;
    private int toolMode;
    private XBWavePanel wavePanel;
    private SourceDataLine sourceDataLine;
    private AudioInputStream audioInputStream;
    private AudioFormat targetFormat;
    private DataLine.Info targetDataLineInfo;
    private int dataLinePosition;
    private InputMethodListener caretListener;
    private List<StatusChangeListener> statusChangeListeners;
    private List<WaveRepaintListener> waveRepaintListeners;

    /**
     * Creates new form AudioPanel
     */
    public AudioPanel() {
        initComponents();
        scaleRatio = 1;
        toolMode = 0;
        undo = new UndoManager();
        fileName = "";
        audioFormatType = null;

        wavePanel = new XBWavePanel();
        sourceDataLine = null;
        defaultColors = getAudioPanelColors();

        add(wavePanel);
        /*scrollPane.setViewportView(wavePanel); */

        scrollBar.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent evt) {
                int valuePosition = evt.getValue();
                if (wavePlayed) {
                    if ((int) (wavePosition * scaleRatio) != valuePosition) {
                        seekPlaying((int) (valuePosition / scaleRatio));
                    }
                } else {
                    if (wavePosition != valuePosition) {
                        wavePanel.setWindowPosition(valuePosition < 0 ? 0 : (int) (valuePosition / scaleRatio));
                        repaint();
                    }
                }
            }
        });

        // if the document is ever edited, assume that it needs to be saved
/*        imageArea.getDocument().addDocumentListener(new DocumentListener() {
         public void changedUpdate(DocumentEvent e) { setModified(true); }
         public void insertUpdate(DocumentEvent e) { setModified(true); }
         public void removeUpdate(DocumentEvent e) { setModified(true); }
         }); */
        // Listen for undo and redo events
/*        imageArea.getDocument().addUndoableEditListener(new UndoableEditListener() {

         public void undoableEditHappened(UndoableEditEvent evt) {
         undo.addEdit(evt.getEdit());
         firePropertyChange("undoAvailable", false, true);
         firePropertyChange("redoAvailable", false, true);
         }
         }); */
        // Create an undo action and add it to the text component
/*        imageArea.getActionMap().put("Undo", new AbstractAction("Undo") {

         public void actionPerformed(ActionEvent evt) {
         try {
         if (undo.canUndo()) {
         undo.undo();
         }
         } catch (CannotUndoException ex) {
         Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
         });

         // Bind the undo action to ctl-Z
         imageArea.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");

         // Create a redo action and add it to the text component
         imageArea.getActionMap().put("Redo", new AbstractAction("Redo") {

         public void actionPerformed(ActionEvent evt) {
         try {
         if (undo.canRedo()) {
         undo.redo();
         }
         } catch (CannotRedoException ex) {
         Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
         });
         // Bind the redo action to ctl-Y
         imageArea.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
         */
        playThread = new PlayThread();
        playThread.start();
        wavePaintThread = new WavePaintThread();
        wavePaintThread.start();
        targetDataLineInfo = null;
        audioInputStream = null;

        statusChangeListeners = new ArrayList<StatusChangeListener>();
        waveRepaintListeners = new ArrayList<WaveRepaintListener>();
//        if ( !AudioSystem.isLineSupported( info ) )
    }

    public void performCopy() {
        wavePanel.copy();
    }

    public void performCut() {
        wavePanel.cut();
    }

    public void performDelete() {
        wavePanel.getInputContext().dispatchEvent(new KeyEvent(this, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED));
    }

    public void performPaste() {
        wavePanel.paste();
    }

    public void performSelectAll() {
        wavePanel.selectAll();
    }

    public void performPlay() {
        wavePlay();
        notifyStatusChangeListeners();
    }

    public void performStop() {
        waveStop();
        notifyStatusChangeListeners();
    }

    private void wavePlay() {
        if (isPlaying()) {
            stopPlaying();
        } else {
            stopPlaying();
            startPlaying();
        }
    }

    private void waveStop() {
        if (isPlaying()) {
            stopPlaying();
        }

        wavePanel.setCursorPosition(0);
        wavePanel.setWindowPosition(0);
        wavePanel.repaint();
    }

    public int repaintWave() {
        int position = (int) playThread.getFramePosition();
        if (position > 0) {
            if (position != drawPosition) {
                return repaintOnPosition(position);
            }
        }

        return -1;
    }

    private int repaintOnPosition(int position) {
        drawPosition = position;
        int waveWidth = wavePanel.getWaveLength();
        int windowPosition = 0;
        int screenWidth = (int) (getWidth() / scaleRatio);
        if (position > waveWidth - screenWidth) {
            windowPosition = waveWidth - screenWidth;
        } else {
            windowPosition = (int) position - (screenWidth / 2);
        }

        if (windowPosition < 0) {
            windowPosition = 0;
        }

        wavePanel.setWindowPosition(windowPosition);
        wavePanel.setCursorPosition(position);
        wavePanel.repaint();
        notifyWaveRepaintListeners();
        return windowPosition;
    }

    private boolean isPlaying() {
        return playThread.playing;
    }

    private synchronized void playingChange(boolean play, boolean stop, int position) {
        if (wavePanel.getWave() != null) {
            wavePaintThread.terminated = true;
            wavePaintThread.terminate();
        }

        if (stop) {
            if (wavePanel.getWave() != null) {
                wavePaintThread.terminate();

                playThread.terminated = true;
                sourceDataLine.stop();
                playThread.terminate();
            }
        }

        if (stop && play) {
            repaintOnPosition(position);
        }

        if (play) {
            if (wavePanel.getWave() != null) {
                dataLinePosition = wavePanel.getCursorPosition();
                if (dataLinePosition >= wavePanel.getWave().getLengthInTicks()) {
                    dataLinePosition = 0;
                }

                synchronized (playThread) {
                    if (dataLinePosition < 0) {
                        playThread.bufferPosition = 0;
                    } else {
                        playThread.bufferPosition = dataLinePosition * targetFormat.getFrameSize();
                    }

                    playThread.playing = true;
                    playThread.notify();
                }

                drawPosition = dataLinePosition;
                synchronized (wavePaintThread) {
                    wavePaintThread.drawing = true;
                    wavePaintThread.notify();
                }
            }
        }
    }

    private void startPlaying() {
        playingChange(true, false, 0);
    }

    private void stopPlaying() {
        playingChange(false, true, 0);
    }

    private void seekPlaying(int position) {
        playingChange(true, true, position);
    }

    public void printFile() {
        PrinterJob job = PrinterJob.getPrinterJob();
        if (job.printDialog()) {
            try {
//                PrintJob myJob = imageArea.getToolkit().getPrintJob(null, fileName, null);
//                if (myJob != null) {
                job.setPrintable(new Printable() {

                    @Override
                    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
//                        imageArea.print(graphics); // TODO: Rescale on page
                        if (pageIndex == 0) {
                            return Printable.PAGE_EXISTS;
                        }
                        return Printable.NO_SUCH_PAGE;
                    }
                });
                job.print();
//                }
            } catch (PrinterException ex) {
                Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Color[] getAudioPanelColors() {
        Color[] colors = new Color[4];
        colors[0] = wavePanel.getWaveColor();
        colors[1] = wavePanel.getCursorColor();
        colors[2] = wavePanel.getBackground();
        colors[3] = wavePanel.getSelectionColor();
        return colors;
    }

    public void setAudioPanelColors(Color[] colors) {
        wavePanel.setWaveColor(colors[0]);
        wavePanel.setCursorColor(colors[1]);
        wavePanel.setBackground(colors[2]);
        wavePanel.setSelectionColor(colors[3]);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollBar = new javax.swing.JScrollBar();

        setAutoscrolls(true);
        setInheritsPopupMenu(true);
        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        scrollBar.setBlockIncrement(40);
        scrollBar.setMaximum(0);
        scrollBar.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scrollBar.setName("scrollBar"); // NOI18N
        add(scrollBar, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollBar scrollBar;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the modified
     */
    @Override
    public boolean isModified() {
        return modified;
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(boolean modified) {
        /*        if (highlight != null) {
         imageArea.getHighlighter().removeHighlight(highlight);
         highlight = null;
         }
         boolean oldValue = this.modified;
         this.modified = modified;
         firePropertyChange("modified", oldValue, this.modified); */
    }

    public boolean isEditEnabled() {
        return false;
//        return imageArea.isEditable();
    }

    public boolean isPasteEnabled() {
        return false;
//        return imageArea.isEditable();
    }

    @Override
    public void loadFromFile() {
        if (XBWaveEditorFrame.XBSFILETYPE.equals(fileType.getFileTypeId())) {
            XBTChildProviderSerialHandler handler = new XBTChildProviderSerialHandler();
            try {
                handler.attachXBTPullProvider(new XBToXBTPullConvertor(new XBPullReader(new FileInputStream(getFileName()))));
                getStubXBTDataSerializator().serializeXB(XBSerializationType.FROM_XB, 0, handler);
            } catch (XBProcessingException ex) {
                Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            XBWave wave = new XBWave();
            wave.loadFromFile(new File(getFileName()));
            wavePanel.setWave(wave);
            scrollBar.setMaximum(wavePanel.getWaveWidth());
        }

        targetFormat = wavePanel.getWave().getAudioFormat();
        targetDataLineInfo = new DataLine.Info(SourceDataLine.class, wavePanel.getWave().getAudioFormat());
        audioInputStream = wavePanel.getWave().getAudioInputStream();

        // If the format is not supported directly (i.e. if it is not PCM
        // encoded, then try to transcode it to PCM.
        if (!AudioSystem.isLineSupported(targetDataLineInfo)) {
            // This is the PCM format we want to transcode to.
            // The parameters here are audio format details that you
            // shouldn't need to understand for casual use.
            AudioFormat pcm = new AudioFormat(targetFormat.getSampleRate(), 16, targetFormat.getChannels(), true, false);

            // Get a wrapper stream around the input stream that does the
            // transcoding for us.
            audioInputStream = AudioSystem.getAudioInputStream(pcm, audioInputStream);

            // Update the format and info variables for the transcoded data
            targetFormat = audioInputStream.getFormat();
            targetDataLineInfo = new DataLine.Info(SourceDataLine.class, targetFormat);
        }
        try {
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(targetDataLineInfo); //(SourceDataLine) AudioSystem.getSourceDataLine(targetFormat, AudioSystem.getMixerInfo()[0]); //Line(targetDataLineInfo);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        wavePanel.setCursorPosition(0);
        wavePanel.setWindowPosition(0);
        wavePanel.repaint();
    }

    @Override
    public void saveToFile() {
        File file = new File(getFileName());
        if (XBWaveEditorFrame.XBSFILETYPE.equals(fileType.getFileTypeId())) {
            try {
                XBFileOutputStream output = new XBFileOutputStream(file);
                XBTChildListenerSerialHandler handler = new XBTChildListenerSerialHandler();
                
                XBTEncapsulator encapsulator = new XBTEncapsulator(new StubContext(getStubXBTDataSerializator()));
                encapsulator.attachXBTListener(new XBTEventListenerToListener(new XBTToXBEventConvertor(output)));
                handler.attachXBTEventListener(new XBTListenerToEventListener(encapsulator));
                getStubXBTDataSerializator().serializeXB(XBSerializationType.TO_XB, 0, handler);
                output.close();
            } catch (XBProcessingException ex) {
                Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (getFileType() == null) {
                wavePanel.getWave().saveToFile(file);
            } else {
                wavePanel.getWave().saveToFile(file, getFileType());
            }
        }
    }

    @Override
    public void newFile() {
        wavePanel.setWave(null);
        scrollBar.setMaximum(wavePanel.getWaveWidth());
        setModified(false);
    }

    /**
     * @return the fileName
     */
    @Override
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public UndoManager getUndo() {
        return undo;
    }

    public void setPopupMenu(JPopupMenu menu) {
        wavePanel.setComponentPopupMenu(menu);
    }

    /**
     * @param fileMode the fileMode to set
     */
    public void setFileMode(FileType fileMode) {
        this.fileType = fileMode;
    }

    public void scale(double ratio) {
        scaleRatio = ratio;
        wavePanel.setScaleRatio(scaleRatio);
        scrollBar.setMaximum(wavePanel.getWaveWidth());
        wavePanel.repaint();
    }

    /**
     * @return the ext
     */
    public String getExt() {
        return ext;
    }

    /**
     * @param ext the ext to set
     */
    public void setExt(String ext) {
        this.ext = ext;
    }

    @Override
    public Point getMousePosition() {
        return wavePanel.getMousePosition();
    }

    public void attachCaretListener(MouseMotionListener listener) {
        wavePanel.addMouseMotionListener(listener);
    }

    public void showResizeDialog(ImageResizeDialog dlg) {
        if (dlg.getOption() == JOptionPane.OK_OPTION) {
        }
    }

    public XBSerializable getXBTDataSerializator() {
        return new XBSerializable() {
            @Override
            public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
                return serialType == XBSerializationType.FROM_XB
                        ? Arrays.asList(new XBSerialMethod[]{new XBTChildProviderSerialMethod()})
                        : Arrays.asList(new XBSerialMethod[]{new XBTChildListenerSerialMethod()});
            }

            @Override
            public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
                if (serialType == XBSerializationType.FROM_XB) {
                    XBTChildProvider serial = (XBTChildProvider) serializationHandler;
                    /* TODO                XBBufferedImage srcImage = new XBBufferedImage();
                     srcImage.serializeFromXBT(serial);
                     image = toBufferedImage(srcImage.getImage());
                     scaledImage = image;
                     grph = image.getGraphics();
                     grph.setColor(toolColor);
                     imageArea.setIcon(new ImageIcon(image));
                     scale(scaleRatio); */
                    throw new UnsupportedOperationException("Not supported yet.");
                } else {
                    XBTChildListener serial = (XBTChildListener) serializationHandler;
                    // TODO                new XBBufferedImage(toBufferedImage(image)).serializeToXBT(serial);
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            }
        };
    }

    // TODO: This is ugly stub for loading files skipping definition
    public XBSerializable getStubXBTDataSerializator() {
        return new XBSerializable() {
            @Override
            public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
                return serialType == XBSerializationType.FROM_XB
                        ? Arrays.asList(new XBSerialMethod[]{new XBTChildProviderSerialMethod()})
                        : Arrays.asList(new XBSerialMethod[]{new XBTChildListenerSerialMethod()});
            }

            @Override
            public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
                if (serialType == XBSerializationType.FROM_XB) {
                    XBTChildProvider serial = (XBTChildProvider) serializationHandler;
                    serial.getType();
                    serial.nextAttribute();
                    serial.nextAttribute();
                    serial.nextChild(new XBSerializable() {
                        @Override
                        public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
                            return Arrays.asList(new XBSerialMethod[]{new XBTChildProviderSerialMethod()});
                        }

                        @Override
                        public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
                            XBTChildProvider serial = (XBTChildProvider) serializationHandler;
                            serial.getType();
                            serial.nextAttribute();
                            serial.nextAttribute();
                            serial.nextAttribute();
                            serial.nextAttribute();
                            serial.nextAttribute();
                            serial.nextAttribute();
                            serial.end();
                        }
                    }, 0);
                    serial.nextChild(new XBSerializable() {
                        @Override
                        public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
                            return Arrays.asList(new XBSerialMethod[]{new XBTChildProviderSerialMethod()});
                        }

                        @Override
                        public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
                            XBTChildProviderSerialHandler childSerial = (XBTChildProviderSerialHandler) serializationHandler;
                            XBWave srcWave = new XBWave();
                            srcWave.serializeXB(XBSerializationType.FROM_XB, 0, childSerial);
                            wavePanel.setWave(srcWave);
                            scrollBar.setMaximum(wavePanel.getWaveWidth());
                        }
                    }, 0);
                    serial.end();
                } else {
                    XBTChildListener serial = (XBTChildListener) serializationHandler;
                    wavePanel.getWave().serializeXB(XBSerializationType.TO_XB, 0, serializationHandler);
                }
            }
        };
    }

    public String getPositionTime() {
        XBWave wave = wavePanel.getWave();
        if (wave == null) {
            return "0:00.00";
        }

        float sampleRate = wave.getAudioFormat().getSampleRate();

        float position;
        Point point = getMousePosition();
        if (point == null) {
            position = wavePanel.getCursorPosition() / sampleRate;
        } else {
            position = (point.x + wavePanel.getWindowPosition()) / sampleRate;
        }

        String sub = String.valueOf((long) ((position - Math.floor(position)) * 100));
        if (sub.length() < 2) {
            sub = "0" + sub;
        }

        String sec = String.valueOf(((long) position) % 60);
        if (sec.length() < 2) {
            sec = "0" + sec;
        }

        return String.valueOf((long) position / 60) + ":" + sec + "." + sub;
    }

    /**
     * @return the fileType
     */
    public javax.sound.sampled.AudioFileFormat.Type getFileType() {
        return audioFormatType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(javax.sound.sampled.AudioFileFormat.Type fileType) {
        this.audioFormatType = fileType;
    }

    @Override
    public String getPanelName() {
        return "AudioPanel";
    }

    @Override
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the defaultColors
     */
    public Color[] getDefaultColors() {
        return defaultColors;
    }

    @Override
    public void setPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
    }

    @Override
    public String getWindowTitle(String frameTitle) {
        if (!"".equals(fileName)) {
            int pos;
            int newpos = 0;
            do {
                pos = newpos;
                newpos = fileName.indexOf(File.separatorChar, pos) + 1;
            } while (newpos > 0);
            return fileName.substring(pos) + " - " + frameTitle;
        }

        return frameTitle;
    }

    public void setVolume(int value) {
        if (sourceDataLine != null && sourceDataLine.isOpen()) {
            FloatControl control = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
            control.setValue(control.getMinimum() * (1 - (float) Math.sqrt((float) value / 100)));
        }
    }

    public boolean getIsPlaying() {
        return isPlaying();
    }

    private static class StubContext extends XBContext {

        private final XBSerializable source;

        public StubContext(XBSerializable source) {
            this.source = source;
        }

        @Override
        public XBDeclaration getDeclaration() {
            XBDeclaration decl = new StubDeclaration();
            decl.setGroupsReserved(3);
            decl.setRootNode(new XBSerializationFromXB(source));

            return decl;
        }

        @Override
        public XBBlockType getBlockType(XBBlockDecl type) {
            return new XBBlockType() {

                @Override
                public UBNatural getGroupID() {
                    return new UBNat32(1);
                }

                @Override
                public UBNatural getBlockID() {
                    return new UBNat32(0);
                }
            };
        }
    }

    private static class StubDeclaration extends XBDeclaration {

        public StubDeclaration() {
        }

        @Override
        public XBFormatDecl getFormat() {
            long[] path = {1, 4, 0, 1};
            return new StubXBFormatDecl(path);
        }
    }

    private static class StubXBFormatDecl extends XBFormatDecl {

        public StubXBFormatDecl(long[] path) {
            super(path);
        }

        @Override
        public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
            return serialType == XBSerializationType.FROM_XB
                    ? Arrays.asList(new XBSerialMethod[]{new XBTSerialSequenceProviderMethod()})
                    : Arrays.asList(new XBSerialMethod[]{new XBTSerialSequenceListenerMethod()});
        }

        @Override
        public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
            XBSerialSequence seq = new XBSerialSequence(new XBFixedBlockType(XBBasicBlockType.FORMAT_DECLARATION));
            seq.join(new UBNat32(2));
            seq.join(getCatalogPath());
            XBTSerialSequence serial = (XBTSerialSequence) serializationHandler;
            serial.sequenceXB(seq);
        }
    }

    public void setDrawMode(XBWavePanel.DrawMode drawMode) {
        wavePanel.setDrawMode(drawMode);
    }

    public void setToolMode(XBWavePanel.ToolMode toolMode) {
        wavePanel.setToolMode(toolMode);
        wavePanel.repaint();
    }

    class PlayThread extends Thread {

        boolean playing;
        boolean terminated;
        int bufferPosition;

        @Override
        public void run() {
            playing = false;
            terminated = false;
            mainLoop();
        }

        private void mainLoop() {
            while (true) {
                try {
                    synchronized (this) {
                        wait();
                    }

                    terminated = false;
                    int bufferLength = wavePanel.getWave().chunkSize / 6; // Workaround for getFramePosition issue in Java 1.6
                    try {
                        sourceDataLine.open(targetFormat, bufferLength); // wavePanel.getWave().getAudioFormat()
                        sourceDataLine.start();

                        //buffer = wavePanel.getWave().getBlock(block);
                        byte[] buffer = new byte[bufferLength];

                        DataLine.Info info = new DataLine.Info(SourceDataLine.class, wavePanel.getWave().getAudioFormat(), bufferLength);
                        boolean bIsSupportedDirectly = AudioSystem.isLineSupported(info);
                        audioInputStream = wavePanel.getWave().getAudioInputStream();
                        audioInputStream.skip(bufferPosition);
                        // TODO Detection doesn't work for > 16 bit lines for some reason 
                        if (!bIsSupportedDirectly || wavePanel.getWave().getAudioFormat().getSampleSizeInBits() != 16) {
                            audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
                        }

                        bufferLength = audioInputStream.read(buffer, 0, bufferLength);
                        //wavePanel.getWave().getBlock(block);
                        int offset = 0; //bufferPosition % bufferLength;
                        while ((buffer != null) && (!terminated) && (bufferLength > offset)) {
                            bufferPosition += bufferLength - offset;
                            sourceDataLine.write(buffer, offset, bufferLength - offset);
                            //block++;
                            //buffer = wavePanel.getWave().getBlock(block);
                            bufferLength = audioInputStream.read(buffer, 0, bufferLength);
                            offset = 0;
                        }

                        sourceDataLine.drain();
                        sourceDataLine.flush();
                        sourceDataLine.close();
                        playing = false;

                        if (!terminated) {
                            wavePanel.setCursorPosition(wavePanel.getWaveLength());
                            notifyStatusChangeListeners();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (LineUnavailableException ex) {
                        Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private long getFramePosition() {
            return (bufferPosition / targetFormat.getFrameSize());
        }

        private void terminate() {
            while (playing) {
                try {
                    terminated = true;
                    sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    class WavePaintThread extends Thread {

        boolean terminated;
        boolean drawing;

        @Override
        public void run() {
            terminated = false;
            drawing = false;
            mainLoop();
        }

        private void mainLoop() {
            while (true) {
                try {
                    synchronized (this) {
                        wait();
                    }

                    terminated = false;
                    while (!terminated) {
                        sleep(50);

                        if (!terminated) {
                            wavePosition = repaintWave();
                            if (wavePosition >= 0) {
                                scrollBar.setValue((int) (wavePosition * scaleRatio));
                            }
                        }
                    }

                    drawing = false;
                } catch (InterruptedException ex) {
                    Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void terminate() {
            while (drawing) {
                try {
                    terminated = true;
                    sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * display formats supported
     *
     * @param li the line information.
     */
    private static void showFormats(Line.Info li) {
        if (li instanceof DataLine.Info) {
            AudioFormat[] afs = ((DataLine.Info) li).getFormats();
            for (AudioFormat af : afs) {
                System.out.println("        " + af.toString());
            }
        }
    }

    public static void main(String[] args) {
        // loop through all mixers, and all source and target lines within each mixer.
        Mixer.Info[] mis = AudioSystem.getMixerInfo();
        for (Mixer.Info mi : mis) {
            Mixer mixer = AudioSystem.getMixer(mi);
            // e.g. com.sun.media.sound.DirectAudioDevice
            System.out.println("mixer: " + mixer.getClass().getName());
            Line.Info[] lis = mixer.getSourceLineInfo();
            for (Line.Info li : lis) {
                System.out.println("    source line: " + li.toString());

                showFormats(li);
            }
            lis = mixer.getTargetLineInfo();
            for (Line.Info li : lis) {
                System.out.println("    target line: " + li.toString());
                showFormats(li);
            }
            Control[] cs = mixer.getControls();
            for (Control c : cs) {
                System.out.println("    control: " + c.toString());
            }
        }
    }

    public interface StatusChangeListener extends EventListener {

        public void statusChanged();
    }

    public void addStatusChangeListener(StatusChangeListener listener) {
        statusChangeListeners.add(listener);
    }

    public void removeStatusChangeListener(StatusChangeListener listener) {
        statusChangeListeners.remove(listener);
    }

    private void notifyStatusChangeListeners() {
        for (StatusChangeListener listener : statusChangeListeners) {
            if (wavePlayed != isPlaying()) {
                wavePlayed = !wavePlayed;
            }

            listener.statusChanged();
        }
    }

    public interface WaveRepaintListener extends EventListener {

        public void waveRepaint();
    }

    public void addWaveRepaintListener(WaveRepaintListener listener) {
        waveRepaintListeners.add(listener);
    }

    public void removeWaveRepaintListener(WaveRepaintListener listener) {
        waveRepaintListeners.remove(listener);
    }

    private void notifyWaveRepaintListeners() {
        for (WaveRepaintListener listener : waveRepaintListeners) {
            listener.waveRepaint();
        }
    }
}
