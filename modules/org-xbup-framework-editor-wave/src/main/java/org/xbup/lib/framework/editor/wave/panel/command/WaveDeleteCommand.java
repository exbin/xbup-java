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
package org.xbup.lib.framework.editor.wave.panel.command;

import java.util.Date;
import org.xbup.lib.audio.swing.XBWavePanel;
import org.xbup.lib.core.type.XBData;
import org.xbup.lib.operation.AbstractCommand;

/**
 * Wave delete command.
 *
 * @version 0.2.0 2016/01/24
 * @author ExBin Project (http://exbin.org)
 */
public class WaveDeleteCommand extends AbstractCommand {

    private final XBWavePanel wave;
    private final int startPosition;
    private final int endPosition;

    XBData deletedData;

    public WaveDeleteCommand(XBWavePanel wave, int startPosition, int endPosition) {
        this.wave = wave;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    @Override
    public String getCaption() {
        return "Wave section deleted";
    }

    @Override
    public void execute() throws Exception {
        deletedData = wave.getWave().cutData(startPosition, endPosition - startPosition);
        wave.rebuildZoomCache();
    }

    @Override
    public void redo() throws Exception {
        execute();
    }

    @Override
    public void undo() throws Exception {
        wave.getWave().insertData(deletedData, startPosition);
        wave.rebuildZoomCache();
        deletedData.clear();
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
