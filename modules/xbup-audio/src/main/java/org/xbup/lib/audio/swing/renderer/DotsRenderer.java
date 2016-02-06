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
package org.xbup.lib.audio.swing.renderer;

import java.awt.Graphics;
import java.util.List;
import org.xbup.lib.audio.swing.XBWavePanel;
import org.xbup.lib.audio.wave.XBWave;

/**
 * Dots wave renderer.
 *
 * @version 0.2.0 2016/02/06
 * @author XBUP Project (http://xbup.org)
 */
public class DotsRenderer extends DefaultRenderer {

    public DotsRenderer() {
    }

    @Override
    public void paint(Graphics g, XBWavePanel panel, int begin, int end, RenderType renderType) {
        super.paint(g, panel, begin, end, renderType);

        double scaleRatio = panel.getScaleRatio();
        LineRecord lineRecord = new LineRecord();
        XBWave wave = panel.getWave();
        List<XBWave> zoomCache = panel.getZoomCache();
        int stopPos = end;
        if (wave != null) {
            g.setColor(panel.getWaveColor());
            int channelsCount = wave.getAudioFormat().getChannels();
            if (stopPos >= (panel.getWaveLength() - panel.getWindowPosition()) * scaleRatio) {
                stopPos = (int) ((panel.getWaveLength() - panel.getWindowPosition()) * scaleRatio) - 1;
            }

            LineRecord value = new LineRecord();
            for (int pos = begin; pos < stopPos; pos++) {
                for (int channel = 0; channel < channelsCount; channel++) {
                    int pomPos = pos;
                    if (pomPos < 0) {
                        pomPos = 0;
                    }
                    
                    int height = panel.getHeight() / channelsCount;
                    int heightShift = (channel * panel.getHeight()) / channelsCount;

                    int zoomScale = getZoomScale(scaleRatio);
                    int linePosition = panel.getWindowPosition() / zoomScale + (int) (pomPos / (scaleRatio * zoomScale));
                    getRatioValue(value, linePosition, channel, height, heightShift, panel, zoomScale);
                    lineRecord.min = value.min;
                    lineRecord.max = value.max;
                    if (scaleRatio < 1) {
                        int lineEndPosition = panel.getWindowPosition() / zoomScale + (int) ((pomPos + 1) / (scaleRatio * zoomScale));
                        for (int inLinePosition = linePosition + 1; inLinePosition < lineEndPosition; inLinePosition++) {
                            getRatioValue(value, inLinePosition, channel, height, heightShift, panel, zoomScale);
                            if (value.min < lineRecord.min) {
                                lineRecord.min = value.min;
                            }
                            if (value.max > lineRecord.max) {
                                lineRecord.max = value.max;
                            }
                        }
                    }

                    if (renderType == RenderType.CURSOR) {
                        g.setColor(panel.getCursorWaveColor());
                        g.drawLine(pos, lineRecord.min, pos, lineRecord.max);
                    } else {
                        g.setColor(panel.getWaveFillColor());
                        g.drawLine(pos, lineRecord.min, pos, lineRecord.max);
                        g.setColor(panel.getWaveColor());
                        g.drawLine(pos, lineRecord.min, pos, lineRecord.min);
                        g.drawLine(pos, lineRecord.max, pos, lineRecord.max);
                    }
                }
            }
        }
    }

    private void getRatioValue(LineRecord record, int position, int channel, int height, int heightShift, XBWavePanel panel, int zoomScale) {
        if (zoomScale == 1) {
            int value = panel.getWave().getRatioValue(position, channel, height) + heightShift;
            record.min = value;
            record.max = value;
        } else {
            XBWave wave;
            if (zoomScale == 16) {
                wave = panel.getZoomCache().get(0);
            } else {
                wave = panel.getZoomCache().get(1);
            }

            record.min = wave.getRatioValue(position * 2, channel, height) + heightShift;
            record.max = wave.getRatioValue(position * 2 + 1, channel, height) + heightShift;
        }
    }

    private class LineRecord {

        int min;
        int max;
    }
}
