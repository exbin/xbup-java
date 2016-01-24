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
import org.xbup.lib.audio.wave.XBWave;
import org.xbup.lib.core.type.XBData;
import org.xbup.lib.operation.XBTDocCommand;
import org.xbup.lib.operation.basic.XBBasicCommandType;

/**
 * Wave reverse command.
 *
 * @version 0.2.0 2016/01/24
 * @author XBUP Project (http://xbup.org)
 */
public class WaveReverseCommand extends XBTDocCommand {

    private XBWave wave;
    private int startPosition;
    private int endPosition;

    XBData deletedData;

    public WaveReverseCommand(XBWave wave) {
        this(wave, -1, -1);
    }

    public WaveReverseCommand(XBWave wave, int startPosition, int endPosition) {
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
            wave.performTransformReverse(startPosition, endPosition);
        } else {
            wave.performTransformReverse();
        }
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

    @Override
    public XBBasicCommandType getBasicType() {
        return XBBasicCommandType.NODE_SWAPED;
    }
}
