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
 * You should have received a performCopy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xbup.lib.audio.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.xbup.lib.audio.wave.XBWave;

/**
 * Simple panel audio wave.
 *
 * @version 0.2.0 2016/01/29
 * @author XBUP Project (http://xbup.org)
 */
public class XBWavePanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

    private XBWave wave;
    private int windowPosition;
    private int cursorPosition;
    private SelectionRange selection;
    private DrawMode drawMode;
    private ToolMode toolMode;
    private double scaleRatio;
    private SelectionChangedListener selectionChangedListener = null;

    private Color waveColor;
    private Color waveFillColor;
    private Color selectionColor;
    private Color cursorWaveColor;
    private Color cursorColor;

    public XBWavePanel() {
        super();
        scaleRatio = 1;
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
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        LineRecord lineRecord = new LineRecord();
//        super.paintComponent(g);

        Rectangle clipBounds = g.getClipBounds();
        int rangeStart = 0;
        int rangeEnd;
        int[] zones = {
            ((int) ((cursorPosition - windowPosition) * scaleRatio)) - 1,
            ((int) ((cursorPosition - windowPosition) * scaleRatio)) + 2,
            -1,
            -1};
        if (selection != null) {
            zones[2] = ((int) ((selection.begin - windowPosition) * scaleRatio));
            zones[3] = ((int) ((selection.end - windowPosition) * scaleRatio)) + 1;
        }

        while (rangeStart < clipBounds.getWidth()) {
            int rangePosition = (clipBounds.x + rangeStart);
            rangeEnd = clipBounds.width;
            for (int i = 0; i < zones.length; i++) {
                int zonePosition = zones[i];
                if ((zonePosition > rangePosition) && (rangeEnd > zonePosition - clipBounds.x)) {
                    rangeEnd = zonePosition - clipBounds.x;
                }
            }
            if ((rangePosition >= zones[0]) && (rangePosition < zones[1])) {
                g.setColor(cursorColor);
            } else if ((rangePosition >= zones[2]) && (rangePosition < zones[3])) {
                g.setColor(selectionColor);
            } else {
                g.setColor(getBackground());
            }
            g.fillRect(rangePosition, clipBounds.y, rangeEnd + clipBounds.x, clipBounds.height);
            rangeStart = rangeEnd;
        }

        // g.drawString( String.valueOf(g.getClipBounds().width) + "," + String.valueOf(g.getClipBounds().height) , 20, 10);
        // Random random = new Random();
        // Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        // g.setColor(color);
        int stopPos = clipBounds.width + clipBounds.x;
        if (wave != null) {
            g.setColor(waveColor);
            int channelsCount = wave.getAudioFormat().getChannels();
            int[] prevMin = {-1, -1};
            int[] prevMax = {-1, -1};
            if (stopPos >= (getWaveLength() - windowPosition) * scaleRatio) {
                stopPos = (int) ((getWaveLength() - windowPosition) * scaleRatio) - 1;
            }

            for (int pos = clipBounds.x - 1; pos < stopPos; pos++) {
                for (int channel = 0; channel < channelsCount; channel++) {
                    int pomPos = pos;
                    if (pomPos < 0) {
                        pomPos = 0;
                    }

                    int linePosition = windowPosition + (int) (pomPos / scaleRatio);
                    boolean cursorMode = linePosition >= getCursorPosition() - scaleRatio && linePosition <= getCursorPosition() + scaleRatio;
                    int value = wave.getRatioValue(linePosition, channel, getHeight() / channelsCount) + (channel * getHeight()) / channelsCount;
                    lineRecord.min = value;
                    lineRecord.max = value;
                    if (scaleRatio < 1) {
                        int lineEndPosition = windowPosition + (int) ((pomPos + 1) / scaleRatio);
                        for(int inLinePosition = linePosition + 1; inLinePosition < lineEndPosition; inLinePosition++) {
                            int inValue = wave.getRatioValue(inLinePosition, channel, getHeight() / channelsCount) + (channel * getHeight()) / channelsCount;
                            if (inValue < lineRecord.min) {
                                lineRecord.min = inValue;
                            }
                            if (inValue > lineRecord.max) {
                                lineRecord.max = inValue;
                            }
                        }
                    }
                    switch (drawMode) {
                        case DOTS_MODE: {
                            if (cursorMode) {
                                g.setColor(cursorWaveColor);
                                g.drawLine(pos, lineRecord.min, pos, lineRecord.max);
                            } else {
                                g.setColor(waveFillColor);
                                g.drawLine(pos, lineRecord.min, pos, lineRecord.max);
                                g.setColor(waveColor);
                                g.drawLine(pos, lineRecord.min, pos, lineRecord.min);
                                g.drawLine(pos, lineRecord.max, pos, lineRecord.max);
                            }
                            break;
                        }
                        case LINE_MODE: {
                            if (scaleRatio < 1) {
                                if (prevMax[channel] >= 0) {
                                    g.drawLine(pos - 1, prevMax[channel], pos, lineRecord.max);
                                    g.drawLine(pos - 1, prevMin[channel], pos, lineRecord.min);
                                }

                                prevMax[channel] = lineRecord.max;
                                prevMin[channel] = lineRecord.min;
                            } else {
                                if (prevMax[channel] >= 0) {
                                    g.drawLine(pos - 1, prevMax[channel], pos, value);
                                }
                                prevMax[channel] = value;
                            }
                            break;
                        }
                        case INTEGRAL_MODE: {
                            if (cursorMode) {
                                g.setColor(cursorWaveColor);
                            } else {
                                g.setColor(waveColor);
                            }
                            int middle = (getHeight() + (2 * channel * getHeight())) / (2 * channelsCount);
                            if (scaleRatio < 1) {
                                if (lineRecord.min > middle) {
                                    lineRecord.min = middle;
                                }
                                if (lineRecord.max < middle) {
                                    lineRecord.max = middle;
                                }
                                g.drawLine(pos, lineRecord.min, pos, lineRecord.max);
                            } else {
                                g.drawLine(pos, value, pos, middle);
                            }
                            break;
                        }
                    }
                }
            }
        }
//        g.drawRect(g.getClipBounds().x, g.getClipBounds().y, g.getClipBounds().width, g.getClipBounds().height);
//        g.drawString(getBackground().toString() , 20, 20);
    }

    public XBWave getWave() {
        return wave;
    }

    public SelectionRange getSelection() {
        return selection;
    }

    public void setWave(XBWave wave) {
        this.wave = wave;
        repaint();
    }

    /**
     * @return the drawMode
     */
    public DrawMode getDrawMode() {
        return drawMode;
    }

    /**
     * @param drawMode the drawMode to set
     */
    public void setDrawMode(DrawMode drawMode) {
        this.drawMode = drawMode;
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
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
        repaint();
    }

    public int getWindowPosition() {
        return windowPosition;
    }

    public void setWindowPosition(int windowPosition) {
        this.windowPosition = windowPosition;
    }

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
        setWindowPosition(position);
        repaint();
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

    boolean mouseDown = false;
    int mousePressPosition;
    int wavePosition;

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
        /*        mouseClickEnd = (int) (me.getX() / scaleRatio);
         if (mouseClickEnd < 0) mouseClickEnd = 0;
         if (mouseClickEnd > getWidth()) mouseClickEnd = getWidth();
         repaint(); */
        mouseDown = false;
    }

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

    private class LineRecord {
        int min;
        int max;
    }
}
