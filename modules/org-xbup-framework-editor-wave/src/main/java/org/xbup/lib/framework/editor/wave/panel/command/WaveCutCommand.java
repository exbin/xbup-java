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

import java.util.Date;
import org.xbup.lib.audio.swing.XBWavePanel;
import org.xbup.lib.audio.wave.XBWave;
import org.xbup.lib.operation.XBTDocCommand;
import org.xbup.lib.operation.basic.XBBasicCommandType;

/**
 * Wave delete command.
 *
 * @version 0.2.0 2016/01/24
 * @author XBUP Project (http://xbup.org)
 */
public class WaveCutCommand extends XBTDocCommand {

    WaveCopyCommand copyCommand;
    WaveDeleteCommand deleteCommand;

    public WaveCutCommand(XBWavePanel wave, int startPosition, int endPosition) {
        copyCommand = new WaveCopyCommand(wave, startPosition, endPosition);
        deleteCommand = new WaveDeleteCommand(wave, startPosition, endPosition);
    }

    @Override
    public String getCaption() {
        return "Wave section cut out";
    }

    @Override
    public void execute() throws Exception {
        copyCommand.execute();
        deleteCommand.execute();
    }

    @Override
    public void redo() throws Exception {
        deleteCommand.redo();
    }

    @Override
    public void undo() throws Exception {
        deleteCommand.undo();
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

    @Override
    public XBBasicCommandType getBasicType() {
        return XBBasicCommandType.NODE_DELETED;
    }
}
