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
package org.xbup.lib.audio.swing.renderer;

import java.awt.Graphics;
import java.awt.Rectangle;
import org.xbup.lib.audio.swing.XBWavePanel;

/**
 * Default wave renderer.
 *
 * @version 0.2.0 2016/02/05
 * @author XBUP Project (http://xbup.org)
 */
public class DefaultRenderer implements XBWavePanelRenderer {

    public DefaultRenderer() {
    }

    @Override
    public void paint(Graphics g, XBWavePanel panel, int begin, int end, RenderType renderType) {
        Rectangle clipBounds = g.getClipBounds();

        switch (renderType) {
            case NORMAL: {
                g.setColor(panel.getBackground());
                break;
            }
            case CURSOR: {
                g.setColor(panel.getCursorColor());
                break;
            }
            case SELECTION: {
                g.setColor(panel.getSelectionColor());
                break;
            }
            default:
                throw new IllegalStateException();
        }

        g.fillRect(begin, clipBounds.y, end, clipBounds.height);
    }
}
