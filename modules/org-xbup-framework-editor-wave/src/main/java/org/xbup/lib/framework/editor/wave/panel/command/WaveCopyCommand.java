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
package org.xbup.lib.framework.editor.wave.panel.command;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import org.xbup.lib.audio.wave.XBWave;

/**
 * Wave copy command.
 *
 * @version 0.2.0 2016/01/24
 * @author XBUP Project (http://xbup.org)
 */
public class WaveCopyCommand {

    private Clipboard clipboard;
    private XBWave wave;
    private final int startPosition;
    private final int endPosition;

    public WaveCopyCommand(XBWave wave, int startPosition, int endPosition) {
        this.wave = wave;
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public void execute() {
        XBWave copiedWave = wave.copy(startPosition, endPosition - startPosition);
        WaveClipboardData clipboardData = new WaveClipboardData(copiedWave);
        clipboard.setContents(clipboardData, clipboardData);
    }
}
