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
package org.xbup.lib.audio.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.xbup.lib.audio.wave.XBWave;

/**
 * Simple panel audio wave.
 *
 * @version 0.1 wr23.0 2013/12/05
 * @author XBUP Project (http://xbup.org)
 */
public class XBWavePanel extends JPanel implements MouseListener, MouseMotionListener {

    private XBWave wave;
    private int windowPosition;
    private int cursorPosition;
    private SelectionRange selection;
    private DrawMode drawMode;
    private ToolMode toolMode;
    private double scaleRatio;

    private Color dataColor;
    private Color selectionColor;
    private Color cursorColor;

    public XBWavePanel() {
        super();
        scaleRatio = 1;
        setBackground(Color.WHITE);
        repaint();
        setOpaque(true);
        drawMode = DrawMode.LINE_MODE;
        toolMode = ToolMode.SELECTION;
        cursorPosition = 0;
        selectionColor = UIManager.getColor("TextArea.selectionBackground");
        cursorColor = UIManager.getColor("TextArea.caretForeground");
        dataColor = UIManager.getColor("TextArea.foreground");
        if (selectionColor == null) {
            selectionColor = Color.LIGHT_GRAY;
        }
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
//        super.paintComponent(g);

        Rectangle clipBounds = g.getClipBounds();
        int rangeStart = 0;
        int rangeEnd;
        int[] zones = {
            ((int) ((cursorPosition - windowPosition) * scaleRatio))-1,
            ((int) ((cursorPosition - windowPosition) * scaleRatio))+2,
            -1,
            -1};
        if (selection != null) {
            zones[2] = ((int) ((selection.begin - windowPosition) * scaleRatio));
            zones[3] = ((int) ((selection.end - windowPosition) * scaleRatio))+1;
        }

        while (rangeStart < clipBounds.getWidth()) {
            int rangePosition = (clipBounds.x + rangeStart);
            rangeEnd = clipBounds.width;
            for (int i = 0; i < zones.length; i++) {
                int zonePosition = zones[i];
                if ((zonePosition>rangePosition) && (rangeEnd > zonePosition-clipBounds.x)) {
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
            g.setColor(dataColor);
            int channelsCount = wave.getAudioFormat().getChannels();
            int[] prev = { -1, -1 };
            if (stopPos >= (getWaveLength() - windowPosition) * scaleRatio) {
                stopPos = (int) ((getWaveLength() - windowPosition) * scaleRatio)-1;
            }

            for (int pos = clipBounds.x-1; pos < stopPos; pos++) {
                for (int channel = 0; channel < channelsCount; channel++) {
                    int pomPos = pos;
                    if (pomPos < 0) {
                        pomPos = 0;
                    }
                    int value = wave.getRatioValue(windowPosition + (int) (pomPos / scaleRatio), channel, getHeight()/channelsCount) + (channel*getHeight())/channelsCount;
                    int middle = (getHeight() + (2*channel*getHeight())) / (2*channelsCount) ;
                    switch (drawMode) {
                        case DOTS_MODE: {
                            g.drawLine(pos, value, pos, value);
                            break;
                        }
                        case LINE_MODE: {
                            if (prev[channel]>=0) {
                                g.drawLine(pos-1, prev[channel], pos, value);
                            }
                            prev[channel] = value;
                            break;
                        }
                        case INTEGRAL_MODE: {
                            g.drawLine(pos, value, pos, middle);
                            break;
                        }
                    }
                }
            }
        }
//        g.drawRect(g.getClipBounds().x, g.getClipBounds().y, g.getClipBounds().width, g.getClipBounds().height);
//        g.drawString(getBackground().toString() , 20, 20);
    }

    /**
     * @return the wave
     */
    public XBWave getWave() {
        return wave;
    }

    /**
     * @param wave the wave to set
     */
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

    /**
     * @return the position
     */
    public int getCursorPosition() {
        return cursorPosition;
    }

    /**
     * @param position the position to set
     */
    public void setCursorPosition(int position) {
        this.cursorPosition = position;
    }

    public int getWaveWidth() {
        return wave == null ? 0 : (int) (wave.getLengthInTicks() * scaleRatio);
    }

    public int getWaveLength() {
        return wave == null ? 0 : wave.getLengthInTicks();
    }

    public void copy() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void cut() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void paste() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * @return the toolMode
     */
    public ToolMode getToolMode() {
        return toolMode;
    }

    /**
     * @param toolMode the toolMode to set
     */
    public void setToolMode(ToolMode toolMode) {
        this.toolMode = toolMode;
    }

    /**
     * @return the selectionColor
     */
    public Color getSelectionColor() {
        return selectionColor;
    }

    /**
     * @param selectionColor the selectionColor to set
     */
    public void setSelectionColor(Color selectionColor) {
        this.selectionColor = selectionColor;
    }

    /**
     * @return the cursorColor
     */
    public Color getCursorColor() {
        return cursorColor;
    }

    /**
     * @param cursorColor the cursorColor to set
     */
    public void setCursorColor(Color cursorColor) {
        this.cursorColor = cursorColor;
    }

    /**
     * @return the dataColor
     */
    public Color getWaveColor() {
        return dataColor;
    }

    /**
     * @param dataColor the dataColor to set
     */
    public void setWaveColor(Color dataColor) {
        this.dataColor = dataColor;
    }

    /**
     * @return the scaleRatio
     */
    public double getScaleRatio() {
        return scaleRatio;
    }

    /**
     * @param scaleRatio the scaleRatio to set
     */
    public void setScaleRatio(double scaleRatio) {
        this.scaleRatio = scaleRatio;
        repaint();
    }

    /**
     * @return the windowPosition
     */
    public int getWindowPosition() {
        return windowPosition;
    }

    /**
     * @param windowPosition the windowPosition to set
     */
    public void setWindowPosition(int windowPosition) {
        this.windowPosition = windowPosition;
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
            repaint();
        }
    }

    public void selectNone() {
        selection = null;
        repaint();
    }

    boolean mouseDown = false;
    int mousePressPosition;
    int wavePosition;

    @Override
    public void mousePressed(MouseEvent me)
    {
        if (me.getButton() == MouseEvent.BUTTON1) {
            if (toolMode == ToolMode.SELECTION) {
                selectNone();
                int oldPosition = getCursorPosition();
                setCursorPosition((int) (me.getX() / scaleRatio)  + windowPosition);

                repaint();
            } else {
                wavePosition = (int) (me.getX() / scaleRatio);
                int channel = 0;
                if (me.getY()> (getHeight()/2)) {
                    channel = 1;
                }

                wave.setRatioValue(wavePosition + windowPosition, me.getY() - ((getHeight()/2)*channel), channel, getHeight()/2);
                repaint();
            }

            mousePressPosition = getCursorPosition();
        }
        mouseDown = true;
    }

    @Override
    public void mouseReleased(MouseEvent me)
    {
/*        mouseClickEnd = (int) (me.getX() / scaleRatio);
        if (mouseClickEnd < 0) mouseClickEnd = 0;
        if (mouseClickEnd > getWidth()) mouseClickEnd = getWidth();
        repaint(); */
        mouseDown = false;
    }

    @Override
    public void mouseDragged(MouseEvent me)
    {
        if (mouseDown) {
            if (toolMode == ToolMode.SELECTION) {
                setCursorPosition((int) (me.getX() / scaleRatio) + windowPosition);
                if ((selection != null)||(getCursorPosition() > mousePressPosition + 3)||(getCursorPosition() < mousePressPosition - 3)) {
                    if (getCursorPosition() > mousePressPosition) {
                        selection = new SelectionRange(mousePressPosition, getCursorPosition());
                    } else {
                        selection = new SelectionRange(getCursorPosition(),mousePressPosition);
                    }

                    repaint();
                }
            } else {
                int startPosition = me.getX();
                int endPosition;
                if (wavePosition < startPosition) {
                    endPosition = startPosition;
                    startPosition = wavePosition;
                } else {
                    endPosition = wavePosition;
                }
                int channel = 0;
                if (me.getY()>= (getHeight()/2)) {
                    channel = 1;
                }
                for(int drawPosition = startPosition;drawPosition <= endPosition;drawPosition++) {
                    wave.setRatioValue(drawPosition, me.getY() - ((getHeight()/2)*channel), channel, getHeight()/2);
                }
                
                repaint();
                wavePosition = me.getX();
            }
        }
    }

    public class SelectionRange {
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

        /**
         * @return the begin
         */
        public int getBegin() {
            return begin;
        }

        /**
         * @param begin the begin to set
         */
        public void setBegin(int begin) {
            this.begin = begin;
        }

        /**
         * @return the end
         */
        public int getEnd() {
            return end;
        }

        /**
         * @param end the end to set
         */
        public void setEnd(int end) {
            this.end = end;
        }
    }
}
