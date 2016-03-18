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
package org.xbup.lib.audio.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.xbup.lib.audio.swing.renderer.DotsRenderer;
import org.xbup.lib.audio.swing.renderer.IntegralRenderer;
import org.xbup.lib.audio.swing.renderer.LineRenderer;
import org.xbup.lib.audio.swing.renderer.XBWavePanelRenderer;
import org.xbup.lib.audio.wave.XBWave;
import org.xbup.lib.core.block.XBBlockData;
import org.xbup.lib.core.type.XBData;

/**
 * Simple panel audio wave.
 *
 * @version 0.2.0 2016/02/06
 * @author ExBin Project (http://exbin.org)
 */
public class XBWavePanel extends JPanel {

    private XBWave wave;
    private List<XBWave> zoomCache = new ArrayList<>();

    private int windowPosition;
    private int cursorPosition;
    private SelectionRange selection;
    private DrawMode drawMode;
    private ToolMode toolMode;
    private double scaleRatio = 1;
    private SelectionChangedListener selectionChangedListener = null;
    private ZoomChangedListener zoomChangedListener = null;
    private XBWavePanelRenderer renderer = new DotsRenderer();

    private Color waveColor;
    private Color waveFillColor;
    private Color selectionColor;
    private Color cursorWaveColor;
    private Color cursorColor;

    public XBWavePanel() {
        super();
        setBackground(Color.WHITE);
        repaint();
        setOpaque(true);
        drawMode = DrawMode.DOTS_MODE;
        toolMode = ToolMode.SELECTION;
        cursorPosition = 0;
        selectionColor = UIManager.getColor("TextArea.selectionBackground");
        waveColor = UIManager.getColor("TextArea.foreground");
        waveFillColor = UIManager.getColor("TextArea.foreground").brighter().brighter();
        cursorColor = UIManager.getColor("TextArea.caretForeground");
        cursorWaveColor = new Color(255 - waveColor.getRed(), 255 - waveColor.getGreen(), 255 - waveColor.getBlue());
        if (selectionColor == null) {
            selectionColor = Color.LIGHT_GRAY;
        }
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON1) {
                    if (toolMode == ToolMode.SELECTION) {
                        if ((me.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) > 0) {
                            setCursorPosition((int) (me.getX() / scaleRatio) + windowPosition);
                            if (getCursorPosition() > mousePressPosition) {
                                selection = new SelectionRange(mousePressPosition, getCursorPosition());
                            } else {
                                selection = new SelectionRange(getCursorPosition(), mousePressPosition);
                            }
                        } else {
                            clearSelection();
                            int oldPosition = getCursorPosition();
                            setCursorPosition((int) (me.getX() / scaleRatio) + windowPosition);
                            mousePressPosition = getCursorPosition();
                        }

                        repaint();
                    } else if (toolMode == ToolMode.PENCIL) {
                        wavePosition = (int) (me.getX() / scaleRatio);
                        int channel = 0;
                        if (me.getY() > (getHeight() / 2)) {
                            channel = 1;
                        }

                        wave.setRatioValue(wavePosition + windowPosition, me.getY() - ((getHeight() / 2) * channel), channel, getHeight() / 2);
                        repaint();
                        mousePressPosition = getCursorPosition();
                    }
                }
                mouseDown = true;
            }

            @Override
            public void mouseReleased(MouseEvent me) {
//                mouseClickEnd = (int) (me.getX() / scaleRatio);
//                if (mouseClickEnd < 0) mouseClickEnd = 0;
//                if (mouseClickEnd > getWidth()) mouseClickEnd = getWidth();
//                repaint();
                mouseDown = false;
            }

        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                if (mouseDown) {
                    if (toolMode == ToolMode.SELECTION) {
                        setCursorPosition((int) (me.getX() / scaleRatio) + windowPosition);
                        if ((selection != null) || (getCursorPosition() > mousePressPosition + 3) || (getCursorPosition() < mousePressPosition - 3)) {
                            if (getCursorPosition() > mousePressPosition) {
                                selection = new SelectionRange(mousePressPosition, getCursorPosition());
                            } else {
                                selection = new SelectionRange(getCursorPosition(), mousePressPosition);
                            }
                            if (selectionChangedListener != null) {
                                selectionChangedListener.selectionChanged();
                            }

                            repaint();
                        }
                    } else if (toolMode == ToolMode.PENCIL) {
                        int startPosition = (int) (me.getX() / scaleRatio) + windowPosition;
                        int endPosition;
                        if (wavePosition < startPosition) {
                            endPosition = startPosition;
                            startPosition = wavePosition;
                        } else {
                            endPosition = wavePosition;
                        }

                        int channel = 0;
                        if (me.getY() >= (getHeight() / 2)) {
                            channel = 1;
                        }
                        for (int drawPosition = startPosition; drawPosition <= endPosition; drawPosition++) {
                            wave.setRatioValue(drawPosition, me.getY() - ((getHeight() / 2) * channel), channel, getHeight() / 2);
                        }

                        repaint();
                        wavePosition = (int) (me.getX() / scaleRatio) + windowPosition;
                    }
                }
            }

        });
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int position = windowPosition + (int) (e.getPoint().x / scaleRatio);
                if (e.getWheelRotation() == 1) {
                    setScaleRatio(scaleRatio * 2);
                    position -= (int) (e.getPoint().x / scaleRatio);

                } else if (e.getWheelRotation() == -1) {
                    setScaleRatio(scaleRatio / 2);
                    position -= (int) (e.getPoint().x / scaleRatio);
                }
                if (position < 0) {
                    position = 0;
                }
                if (zoomChangedListener != null) {
                    zoomChangedListener.zoomChanged();
                }
                setWindowPosition(position);
                repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        Rectangle clipBounds = g.getClipBounds();
        int begin = clipBounds.x;
        int end = clipBounds.x + clipBounds.width;

        int selectionBegin = selection == null ? -1 : (int) ((selection.begin - windowPosition) * scaleRatio);
        int selectionEnd = selection == null ? -1 : (int) Math.ceil((selection.end - windowPosition) * scaleRatio);
        int cursorAdjusted = (int) ((cursorPosition - windowPosition) * scaleRatio);
        int cursorBegin = cursorAdjusted - 1;
        int cursorEnd = cursorAdjusted + 1;
        while (begin <= end) {
            int section = end;
            if (selectionBegin > begin && selectionBegin <= section) {
                section = selectionBegin - 1;
            }
            if (selectionEnd >= begin && selectionEnd < section) {
                section = selectionEnd;
            }
            if (cursorBegin > begin && cursorBegin <= section) {
                section = cursorBegin - 1;
            }
            if (cursorEnd >= begin && cursorEnd < section) {
                section = cursorEnd;
            }

            XBWavePanelRenderer.RenderType rendererType
                    = begin >= cursorBegin && begin <= cursorEnd ? XBWavePanelRenderer.RenderType.CURSOR
                            : begin >= selectionBegin && begin <= selectionEnd ? XBWavePanelRenderer.RenderType.SELECTION
                                    : XBWavePanelRenderer.RenderType.NORMAL;
            renderer.paint(g, this, begin, section + 1, rendererType);
            begin = section + 1;
        }
    }

    public XBWave getWave() {
        return wave;
    }

    public List<XBWave> getZoomCache() {
        return zoomCache;
    }

    public SelectionRange getSelection() {
        return selection;
    }

    public void setWave(XBWave wave) {
        this.wave = wave;
        rebuildZoomCache();
        repaint();
    }

    public DrawMode getDrawMode() {
        return drawMode;
    }

    public void setDrawMode(DrawMode drawMode) {
        this.drawMode = drawMode;
        switch (drawMode) {
            case DOTS_MODE: {
                renderer = new DotsRenderer();
                break;
            }
            case LINE_MODE: {
                renderer = new LineRenderer();
                break;
            }
            case INTEGRAL_MODE: {
                renderer = new IntegralRenderer();
                break;
            }
            default:
                throw new IllegalStateException();
        }
        repaint();
    }

    public int getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition(int position) {
        this.cursorPosition = position;
    }

    public int getWaveWidth() {
        return wave == null ? 0 : (int) (wave.getLengthInTicks() * scaleRatio);
    }

    public int getWaveLength() {
        return wave == null ? 0 : wave.getLengthInTicks();
    }

    public ToolMode getToolMode() {
        return toolMode;
    }

    public void setToolMode(ToolMode toolMode) {
        this.toolMode = toolMode;
    }

    public Color getWaveColor() {
        return waveColor;
    }

    public void setWaveColor(Color waveColor) {
        this.waveColor = waveColor;
    }

    public Color getWaveFillColor() {
        return waveFillColor;
    }

    public void setWaveFillColor(Color waveFillColor) {
        this.waveFillColor = waveFillColor;
    }

    public Color getCursorColor() {
        return cursorColor;
    }

    public void setCursorColor(Color cursorColor) {
        this.cursorColor = cursorColor;
    }

    public Color getCursorWaveColor() {
        return cursorWaveColor;
    }

    public void setCursorWaveColor(Color cursorWaveColor) {
        this.cursorWaveColor = cursorWaveColor;
    }

    public Color getSelectionColor() {
        return selectionColor;
    }

    public void setSelectionColor(Color selectionColor) {
        this.selectionColor = selectionColor;
    }

    public double getScaleRatio() {
        return scaleRatio;
    }

    public void setScaleRatio(double scaleRatio) {
        this.scaleRatio = scaleRatio;
    }

    public int getWindowPosition() {
        return windowPosition;
    }

    public void setWindowPosition(int windowPosition) {
        this.windowPosition = windowPosition;
    }

    public void rebuildZoomCache() {
        // Rebuild cache
        zoomCache.clear();
        int numberOflevels = 2;

        if (wave == null) {
            return;
        }

        int lengthInTicks = wave.getLengthInTicks();
        ZoomLevelRecord[] levelRecords = new ZoomLevelRecord[numberOflevels];
        for (int level = 0; level < numberOflevels; level++) {
            levelRecords[level] = new ZoomLevelRecord(wave, lengthInTicks, level + 1);
        }

        for (int channel = 0; channel < wave.getAudioFormat().getChannels(); channel++) {
            int position = 0;
            while (position < lengthInTicks) {
                for (int level = 0; level < numberOflevels; level++) {
                    ZoomLevelRecord levelRecord = levelRecords[level];
                    if (position % levelRecord.levelRatio == 0) {
                        if (position > 0) {
                            int zoomWavePosition = (position >> (4 * level + 4)) * 2 - 1;
                            levelRecord.zoomWave.setValue(levelRecord.minValue, zoomWavePosition, channel);
                            levelRecord.zoomWave.setValue(levelRecord.maxValue, zoomWavePosition + 1, channel);
                        }

                        wave.getValue(levelRecord.maxValue, position, channel);
                        wave.getValue(levelRecord.minValue, position, channel);
                    } else {
                        wave.getValue(levelRecord.value, position, channel);
                        if (!dataIsGreaterOrEqual(levelRecord.minValue, levelRecord.value, levelRecord.bytesPerSample)) {
                            levelRecord.value.copyTo(levelRecord.minValue, 0, levelRecord.bytesPerSample, 0);
                        }
                        if (dataIsGreater(levelRecord.value, levelRecord.maxValue, levelRecord.bytesPerSample)) {
                            levelRecord.value.copyTo(levelRecord.maxValue, 0, levelRecord.bytesPerSample, 0);
                        }
                    }
                }
                position++;
            }

            if (position > 0) {
                for (int level = 0; level < numberOflevels; level++) {
                    ZoomLevelRecord levelRecord = levelRecords[level];
                    int zoomWavePosition = (position >> (4 * level + 4)) * 2 - 1;
                    levelRecord.zoomWave.setValue(levelRecord.minValue, zoomWavePosition, channel);
                    levelRecord.zoomWave.setValue(levelRecord.maxValue, zoomWavePosition + 1, channel);
                }
            }
        }

        for (int level = 0; level < numberOflevels; level++) {
            zoomCache.add(levelRecords[level].zoomWave);
        }
    }

    private class ZoomLevelRecord {

        final XBWave zoomWave;
        final int bytesPerSample;
        final int levelRatio;
        final XBData value;
        final XBData maxValue;
        final XBData minValue;

        public ZoomLevelRecord(XBWave wave, int lengthInTicks, int level) {
            levelRatio = (1 << (level * 4));
            zoomWave = new XBWave(wave.getAudioFormat());
            zoomWave.setLengthInTicks((int) Math.ceil((lengthInTicks * 2) / (16 * level)) + 1);

            bytesPerSample = wave.getAudioFormat().getSampleSizeInBits() >> 3;
            value = new XBData();
            value.setDataSize(bytesPerSample);
            maxValue = new XBData();
            maxValue.setDataSize(bytesPerSample);
            minValue = new XBData();
            minValue.setDataSize(bytesPerSample);
        }

    }

    public enum DrawMode {

        DOTS_MODE,
        LINE_MODE,
        INTEGRAL_MODE
    }

    public enum ToolMode {

        SELECTION,
        PENCIL
    }

    public boolean inSelection(int pos) {
        if (selection == null) {
            return false;
        }
        return (pos >= selection.getBegin()) && (pos <= selection.getEnd());
    }

    public void selectAll() {
        if (wave != null) {
            selection = new SelectionRange(0, wave.getLengthInTicks());
            if (selectionChangedListener != null) {
                selectionChangedListener.selectionChanged();
            }
            repaint();
        }
    }

    public void clearSelection() {
        selection = null;
        if (selectionChangedListener != null) {
            selectionChangedListener.selectionChanged();
        }

        repaint();
    }

    public boolean hasSelection() {
        return selection != null;
    }

    public void setSelectionChangedListener(SelectionChangedListener selectionChangedListener) {
        this.selectionChangedListener = selectionChangedListener;
    }

    public void setZoomChangedListener(ZoomChangedListener zoomChangedListener) {
        this.zoomChangedListener = zoomChangedListener;
    }

    private static boolean dataIsGreater(XBBlockData data, XBBlockData comparedData, int bytesCount) {
        for (int i = 0; i < bytesCount; i++) {
            if (data.getByte(i) > comparedData.getByte(i)) {
                return true;
            } else if (data.getByte(i) < comparedData.getByte(i)) {
                return false;
            }
        }
        return false;
    }

    private static boolean dataIsGreaterOrEqual(XBBlockData data, XBBlockData comparedData, int bytesCount) {
        for (int i = 0; i < bytesCount; i++) {
            if (data.getByte(i) > comparedData.getByte(i)) {
                return true;
            } else if (data.getByte(i) < comparedData.getByte(i)) {
                return false;
            }
        }
        return true;
    }

    boolean mouseDown = false;
    int mousePressPosition;
    int wavePosition;

    public static class SelectionRange {

        private int begin;
        private int end;

        public SelectionRange() {
            begin = 0;
            end = 0;
        }

        public SelectionRange(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        public int getBegin() {
            return begin;
        }

        public void setBegin(int begin) {
            this.begin = begin;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }
    }

    public interface SelectionChangedListener {

        void selectionChanged();
    }

    public interface ZoomChangedListener {

        void zoomChanged();
    }
}
