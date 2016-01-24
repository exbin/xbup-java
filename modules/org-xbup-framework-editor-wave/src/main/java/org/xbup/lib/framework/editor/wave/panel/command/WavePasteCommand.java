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
import java.util.Date;
import org.xbup.lib.audio.wave.XBWave;
import org.xbup.lib.core.type.XBData;
import org.xbup.lib.operation.XBTDocCommand;
import org.xbup.lib.operation.basic.XBBasicCommandType;

/**
 * Wave delete command.
 *
 * @version 0.2.0 2016/01/24
 * @author XBUP Project (http://xbup.org)
 */
public class WavePasteCommand extends XBTDocCommand {

    private XBWave pastedWave;
    private XBWave wave;
    private int startPosition;

    XBData deletedData;

    public WavePasteCommand(XBWave wave, int startPosition) {
        this.wave = wave;
        this.startPosition = startPosition;
    }

    @Override
    public String getCaption() {
        return "Wave section pasted";
    }

    @Override
    public void execute() throws Exception {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        if (clipboard.isDataFlavorAvailable(WaveClipboardData.WAVE_FLAVOR)) {
            pastedWave = (XBWave) clipboard.getData(WaveClipboardData.WAVE_FLAVOR);
        }

        wave.insertWave(pastedWave, startPosition);
    }

    @Override
    public void redo() throws Exception {
        execute();
    }

    @Override
    public void undo() throws Exception {
        wave.insertData(deletedData, startPosition);
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

    @Override
    public XBBasicCommandType getBasicType() {
        return XBBasicCommandType.NODE_DELETED;
    }
}
