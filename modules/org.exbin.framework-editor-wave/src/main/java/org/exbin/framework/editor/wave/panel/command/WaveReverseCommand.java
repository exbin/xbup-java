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
package org.exbin.framework.editor.wave.panel.command;

import java.util.Date;
import org.exbin.xbup.audio.swing.XBWavePanel;
import org.exbin.xbup.core.type.XBData;
import org.exbin.xbup.operation.AbstractCommand;

/**
 * Wave reverse command.
 *
 * @version 0.2.0 2016/01/24
 * @author ExBin Project (http://exbin.org)
 */
public class WaveReverseCommand extends AbstractCommand {

    private XBWavePanel wave;
    private int startPosition;
    private int endPosition;

    XBData deletedData;

    public WaveReverseCommand(XBWavePanel wave) {
        this(wave, -1, -1);
    }

    public WaveReverseCommand(XBWavePanel wave, int startPosition, int endPosition) {
        this.wave = wave;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    @Override
    public String getCaption() {
        return "Wave section reversed";
    }

    @Override
    public void execute() throws Exception {
        if (startPosition >= 0) {
            wave.getWave().performTransformReverse(startPosition, endPosition);
        } else {
            wave.getWave().performTransformReverse();
        }
        wave.rebuildZoomCache();
    }

    @Override
    public void redo() throws Exception {
        execute();
    }

    @Override
    public void undo() throws Exception {
        execute();
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void dispose() throws Exception {
    }

    @Override
    public Date getExecutionTime() {
        return null;
    }
}
