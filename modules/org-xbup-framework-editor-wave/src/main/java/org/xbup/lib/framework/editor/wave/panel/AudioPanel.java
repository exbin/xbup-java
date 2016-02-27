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
package org.xbup.lib.framework.editor.wave.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.InputMethodListener;
import java.awt.event.MouseMotionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import org.xbup.lib.audio.swing.XBWavePanel;
import org.xbup.lib.audio.wave.XBWave;
import org.xbup.lib.core.block.declaration.XBDeclaration;
import org.xbup.lib.core.block.declaration.local.XBLFormatDecl;
import org.xbup.lib.core.catalog.XBPCatalog;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.convert.XBTTypeUndeclaringFilter;
import org.xbup.lib.core.parser.token.event.XBEventWriter;
import org.xbup.lib.core.parser.token.event.convert.XBTEventListenerToListener;
import org.xbup.lib.core.parser.token.event.convert.XBTListenerToEventListener;
import org.xbup.lib.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.xbup.lib.core.parser.token.pull.XBPullReader;
import org.xbup.lib.core.parser.token.pull.convert.XBTPullTypeDeclaringFilter;
import org.xbup.lib.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.xbup.lib.core.serial.XBPSerialReader;
import org.xbup.lib.core.serial.XBPSerialWriter;
import org.xbup.lib.framework.editor.wave.EditorWaveModule;
import org.xbup.lib.framework.editor.wave.dialog.WaveResizeDialog;
import org.xbup.lib.framework.editor.wave.panel.command.WaveClipboardData;
import org.xbup.lib.framework.editor.wave.panel.command.WaveCopyCommand;
import org.xbup.lib.framework.editor.wave.panel.command.WaveCutCommand;
import org.xbup.lib.framework.editor.wave.panel.command.WaveDeleteCommand;
import org.xbup.lib.framework.editor.wave.panel.command.WavePasteCommand;
import org.xbup.lib.framework.editor.wave.panel.command.WaveReverseCommand;
import org.xbup.lib.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.file.api.FileType;
import org.xbup.lib.framework.gui.menu.api.ClipboardActionsUpdateListener;
import org.xbup.lib.framework.gui.menu.api.ComponentClipboardHandler;
import org.xbup.lib.operation.undo.XBUndoHandler;

/**
 * Audio panel for XBSEditor.
 *
 * @version 0.2.0 2016/01/24
 * @author XBUP Project (http://xbup.org)
 */
public class AudioPanel extends javax.swing.JPanel implements XBEditorProvider, ComponentClipboardHandler {

    private XBUndoHandler undoHandler;
    private String fileName;
    private String ext;
    private javax.sound.sampled.AudioFileFormat.Type audioFormatType;
    private FileType fileType;
    private boolean wavePlayed = false;
    private int drawPosition = -1;
    private int wavePosition = -1;

    private final PlayThread playThread = new PlayThread();
    private final WavePaintThread wavePaintThread = new WavePaintThread();

    private Color[] defaultColors;
    private XBWavePanel wavePanel;
    private SourceDataLine sourceDataLine;
    private AudioInputStream audioInputStream;
    private AudioFormat targetFormat;
    private DataLine.Info targetDataLineInfo;
    private int dataLinePosition;
    private InputMethodListener caretListener;
    private final List<StatusChangeListener> statusChangeListeners = new ArrayList<>();
    private final List<WaveRepaintListener> waveRepaintListeners = new ArrayList<>();
    private ClipboardActionsUpdateListener clipboardActionsUpdateListener;

    public AudioPanel() {
        initComponents();
        init();
    }

    private void init() {
        fileName = "";
        audioFormatType = null;

        wavePanel = new XBWavePanel();
        wavePanel.setSelectionChangedListener(new XBWavePanel.SelectionChangedListener() {
            @Override
            public void selectionChanged() {
                if (clipboardActionsUpdateListener != null) {
                    clipboardActionsUpdateListener.stateChanged();
                }
            }
        });
        wavePanel.setZoomChangedListener(new XBWavePanel.ZoomChangedListener() {
            @Override
            public void zoomChanged() {
                scrollBar.setMaximum(wavePanel.getWaveWidth());
            }
        });
        sourceDataLine = null;
        defaultColors = getAudioPanelColors();

        add(wavePanel);
        /*scrollPane.setViewportView(wavePanel); */

        scrollBar.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent evt) {
                int valuePosition = evt.getValue();
                if (wavePlayed) {
                    if ((int) (wavePosition * wavePanel.getScaleRatio()) != valuePosition) {
                        seekPlaying((int) (valuePosition / wavePanel.getScaleRatio()));
                    }
                } else if (wavePosition != valuePosition) {
                    wavePanel.setWindowPosition(valuePosition < 0 ? 0 : (int) (valuePosition / wavePanel.getScaleRatio()));
                    repaint();
                }
            }
        });

        playThread.start();
        wavePaintThread.start();
        targetDataLineInfo = null;
        audioInputStream = null;
    }

    @Override
    public void performCopy() {
        XBWavePanel.SelectionRange selectionRange = wavePanel.getSelection();
        WaveCopyCommand copyCommand = new WaveCopyCommand(wavePanel, selectionRange.getBegin(), selectionRange.getEnd());
        copyCommand.execute();
    }

    @Override
    public void performCut() {
        XBWavePanel.SelectionRange selectionRange = wavePanel.getSelection();
        WaveCutCommand cutCommand = new WaveCutCommand(wavePanel, selectionRange.getBegin(), selectionRange.getEnd());
        try {
            undoHandler.execute(cutCommand);
        } catch (Exception ex) {
            Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        wavePanel.clearSelection();
    }

    @Override
    public void performDelete() {
        XBWavePanel.SelectionRange selectionRange = wavePanel.getSelection();
        WaveDeleteCommand deleteCommand = new WaveDeleteCommand(wavePanel, selectionRange.getBegin(), selectionRange.getEnd());
        try {
            undoHandler.execute(deleteCommand);
        } catch (Exception ex) {
            Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        wavePanel.clearSelection();
    }

    @Override
    public void performPaste() {
        WavePasteCommand pasteCommand = new WavePasteCommand(wavePanel, wavePanel.getCursorPosition());
        try {
            undoHandler.execute(pasteCommand);
        } catch (Exception ex) {
            Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        wavePanel.clearSelection();
    }

    @Override
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
        int windowPosition;
        int screenWidth = (int) (getWidth() / wavePanel.getScaleRatio());
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
        Color[] colors = new Color[6];
        colors[0] = wavePanel.getWaveColor();
        colors[1] = wavePanel.getWaveFillColor();
        colors[2] = wavePanel.getCursorColor();
        colors[3] = wavePanel.getCursorWaveColor();
        colors[4] = wavePanel.getBackground();
        colors[5] = wavePanel.getSelectionColor();
        return colors;
    }

    public void setAudioPanelColors(Color[] colors) {
        wavePanel.setWaveColor(colors[0]);
        wavePanel.setWaveFillColor(colors[1]);
        wavePanel.setCursorColor(colors[2]);
        wavePanel.setCursorWaveColor(colors[3]);
        wavePanel.setBackground(colors[4]);
        wavePanel.setSelectionColor(colors[5]);
        wavePanel.repaint();
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

    @Override
    public boolean isModified() {
        return undoHandler.getCommandPosition() != undoHandler.getSyncPoint();
    }

    @Override
    public void loadFromFile() {
        if (EditorWaveModule.XBS_FILE_TYPE.equals(fileType.getFileTypeId())) {
            try {
                XBPCatalog catalog = new XBPCatalog();
                catalog.addFormatDecl(getContextFormatDecl());
                XBLFormatDecl formatDecl = new XBLFormatDecl(XBWave.XBUP_FORMATREV_CATALOGPATH);
                XBWave wave = new XBWave();
                XBDeclaration declaration = new XBDeclaration(formatDecl, wave);
                XBTPullTypeDeclaringFilter typeProcessing = new XBTPullTypeDeclaringFilter(catalog);
                typeProcessing.attachXBTPullProvider(new XBToXBTPullConvertor(new XBPullReader(new FileInputStream(getFileName()))));
                XBPSerialReader reader = new XBPSerialReader(typeProcessing);
                reader.read(declaration);
                wavePanel.setWave(wave);
                scrollBar.setMaximum(wavePanel.getWaveWidth());
            } catch (XBProcessingException | IOException ex) {
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
        if (EditorWaveModule.XBS_FILE_TYPE.equals(fileType.getFileTypeId())) {
            try {
                FileOutputStream output = new FileOutputStream(file);

                XBPCatalog catalog = new XBPCatalog();
                catalog.addFormatDecl(getContextFormatDecl());
                XBLFormatDecl formatDecl = new XBLFormatDecl(XBWave.XBUP_FORMATREV_CATALOGPATH);
                XBDeclaration declaration = new XBDeclaration(formatDecl, wavePanel.getWave());
                declaration.realignReservation(catalog);
                XBTTypeUndeclaringFilter typeProcessing = new XBTTypeUndeclaringFilter(catalog);
                typeProcessing.attachXBTListener(new XBTEventListenerToListener(new XBTToXBEventConvertor(new XBEventWriter(output))));
                XBPSerialWriter writer = new XBPSerialWriter(new XBTListenerToEventListener(typeProcessing));
                writer.write(declaration);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (getFileType() == null) {
            wavePanel.getWave().saveToFile(file);
        } else {
            wavePanel.getWave().saveToFile(file, getFileType());
        }
        undoHandler.setSyncPoint();
    }

    /**
     * Returns local format declaration when catalog or service is not
     * available.
     *
     * @return local format declaration
     */
    public XBLFormatDecl getContextFormatDecl() {
        /*XBLFormatDef formatDef = new XBLFormatDef();
         List<XBFormatParam> groups = formatDef.getFormatParams();
         XBLGroupDecl waveGroup = new XBLGroupDecl(new XBLGroupDef());
         List<XBGroupParam> waveBlocks = waveGroup.getGroupDef().getGroupParams();
         waveBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 5, 0, 0})));
         ((XBLGroupDef) waveGroup.getGroupDef()).provideRevision();
         groups.add(new XBFormatParamConsist(waveGroup));
         formatDef.realignRevision();

         XBLFormatDecl formatDecl = new XBLFormatDecl(formatDef);
         formatDecl.setCatalogPath(XBWave.XBUP_FORMATREV_CATALOGPATH);
         return formatDecl;*/

        XBPSerialReader reader = new XBPSerialReader(ClassLoader.class.getResourceAsStream("/org/xbup/tool/editor/module/wave_editor/resources/xbs_format_decl.xb"));
        XBLFormatDecl formatDecl = new XBLFormatDecl();
        try {
            reader.read(formatDecl);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return formatDecl;
    }

    @Override
    public void newFile() {
        wavePanel.setWave(null);
        scrollBar.setMaximum(wavePanel.getWaveWidth());
        undoHandler.clear();
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setUndoHandler(XBUndoHandler undoHandler) {
        this.undoHandler = undoHandler;
    }

    public void setPopupMenu(JPopupMenu menu) {
        wavePanel.setComponentPopupMenu(menu);
    }

    public void setFileMode(FileType fileMode) {
        this.fileType = fileMode;
    }

    public void setScale(double ratio) {
        wavePanel.setScaleRatio(ratio);
        scrollBar.setMaximum(wavePanel.getWaveWidth());
        wavePanel.repaint();
    }

    public void scaleAndSeek(double ratio) {
        int windowPosition = wavePanel.getWindowPosition();
        windowPosition = (int) (windowPosition / (wavePanel.getScaleRatio() / ratio)) - wavePanel.getWidth() / 2;
        if (windowPosition < 0) {
            windowPosition = 0;
        }
        setScale(ratio);
        wavePanel.setWindowPosition(windowPosition);
        wavePanel.repaint();
    }

    public double getScale() {
        return wavePanel.getScaleRatio();
    }

    public String getExt() {
        return ext;
    }

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

    public void showResizeDialog(WaveResizeDialog dlg) {
        if (dlg.getDialogOption() == JOptionPane.OK_OPTION) {
        }
    }

    public String getPositionTime() {
        int position;
        Point point = getMousePosition();
        if (point == null) {
            position = wavePanel.getCursorPosition();
        } else {
            position = (point.x + wavePanel.getWindowPosition());
        }

        return getTimeForTicks(position, wavePanel.getWave());
    }

    public javax.sound.sampled.AudioFileFormat.Type getFileType() {
        return audioFormatType;
    }

    public void setFileType(javax.sound.sampled.AudioFileFormat.Type fileType) {
        this.audioFormatType = fileType;
    }

    @Override
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

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

    public void setDrawMode(XBWavePanel.DrawMode drawMode) {
        wavePanel.setDrawMode(drawMode);
    }

    public void setToolMode(XBWavePanel.ToolMode toolMode) {
        wavePanel.setToolMode(toolMode);
        wavePanel.repaint();
    }

    public void performTransformReverse() {
        WaveReverseCommand waveReverseCommand;
        if (isSelection()) {
            XBWavePanel.SelectionRange selectionRange = wavePanel.getSelection();
            waveReverseCommand = new WaveReverseCommand(wavePanel, selectionRange.getBegin(), selectionRange.getEnd());
        } else {
            waveReverseCommand = new WaveReverseCommand(wavePanel);
        }
        try {
            undoHandler.execute(waveReverseCommand);
        } catch (Exception ex) {
            Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public boolean isSelection() {
        return wavePanel.hasSelection() && wavePanel.getWave() != null;
    }

    @Override
    public boolean isEditable() {
        return wavePanel.getWave() != null;
    }

    @Override
    public boolean canSelectAll() {
        return true;
    }

    @Override
    public void setUpdateListener(ClipboardActionsUpdateListener updateListener) {
        this.clipboardActionsUpdateListener = updateListener;
    }

    @Override
    public boolean canPaste() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        return clipboard.isDataFlavorAvailable(WaveClipboardData.WAVE_FLAVOR);
    }

    public boolean isEmpty() {
        return wavePanel.getWave() == null;
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
                    int bufferLength = wavePanel.getWave().getPageSize() / 6; // Workaround for getFramePosition issue in Java 1.6
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
                    } catch (IOException | LineUnavailableException ex) {
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
                                scrollBar.setValue((int) (wavePosition * wavePanel.getScaleRatio()));
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
     * Prints supported formats to system console.
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

    public AudioFormat getWaveFormat() {
        return wavePanel.getWave().getAudioFormat();
    }

    public String getWaveLength() {
        return getTimeForTicks(wavePanel.getWave().getLengthInTicks(), wavePanel.getWave());
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

    public static String getTimeForTicks(int position, XBWave wave) {
        if (wave == null) {
            return "0:00.00";
        }

        float sampleRate = wave.getAudioFormat().getSampleRate();
        float adjustedPosition = position / sampleRate;

        String sub = String.valueOf((long) ((adjustedPosition - Math.floor(adjustedPosition)) * 100));
        if (sub.length() < 2) {
            sub = "0" + sub;
        }

        String sec = String.valueOf(((long) adjustedPosition) % 60);
        if (sec.length() < 2) {
            sec = "0" + sec;
        }

        return String.valueOf((long) adjustedPosition / 60) + ":" + sec + "." + sub;
    }
}
