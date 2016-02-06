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
import org.xbup.lib.audio.swing.XBWavePanel;
import org.xbup.lib.audio.wave.XBWave;

/**
 * Dots wave renderer.
 *
 * @version 0.2.0 2016/02/05
 * @author XBUP Project (http://xbup.org)
 */
public class IntegralRenderer extends DefaultRenderer {

    public IntegralRenderer() {
    }

    @Override
    public void paint(Graphics g, XBWavePanel panel, int begin, int end, RenderType renderType) {
        super.paint(g, panel, begin, end, renderType);

        LineRecord lineRecord = new LineRecord();
        XBWave wave = panel.getWave();
        int stopPos = end;
        if (wave != null) {
            g.setColor(panel.getWaveColor());
            int channelsCount = wave.getAudioFormat().getChannels();
            int[] prevMin = {-1, -1};
            int[] prevMax = {-1, -1};
            if (stopPos >= (panel.getWaveLength() - panel.getWindowPosition()) * panel.getScaleRatio()) {
                stopPos = (int) ((panel.getWaveLength() - panel.getWindowPosition()) * panel.getScaleRatio()) - 1;
            }

            for (int pos = begin; pos < stopPos; pos++) {
                for (int channel = 0; channel < channelsCount; channel++) {
                    int pomPos = pos;
                    if (pomPos < 0) {
                        pomPos = 0;
                    }

                    int linePosition = panel.getWindowPosition() + (int) (pomPos / panel.getScaleRatio());
                    int value = wave.getRatioValue(linePosition, channel, panel.getHeight() / channelsCount) + (channel * panel.getHeight()) / channelsCount;
                    lineRecord.min = value;
                    lineRecord.max = value;
                    if (panel.getScaleRatio() < 1) {
                        int lineEndPosition = panel.getWindowPosition() + (int) ((pomPos + 1) / panel.getScaleRatio());
                        for (int inLinePosition = linePosition + 1; inLinePosition < lineEndPosition; inLinePosition++) {
                            int inValue = wave.getRatioValue(inLinePosition, channel, panel.getHeight() / channelsCount) + (channel * panel.getHeight()) / channelsCount;
                            if (inValue < lineRecord.min) {
                                lineRecord.min = inValue;
                            }
                            if (inValue > lineRecord.max) {
                                lineRecord.max = inValue;
                            }
                        }
                    }

                    if (renderType == RenderType.CURSOR) {
                        g.setColor(panel.getCursorWaveColor());
                    } else {
                        g.setColor(panel.getWaveColor());
                    }
                    int middle = (panel.getHeight() + (2 * channel * panel.getHeight())) / (2 * channelsCount);
                    if (panel.getScaleRatio() < 1) {
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
                }
            }
        }
    }

    private class LineRecord {

        int min;
        int max;
    }
}
