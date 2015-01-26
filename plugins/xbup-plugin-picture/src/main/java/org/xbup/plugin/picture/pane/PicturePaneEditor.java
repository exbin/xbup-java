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
package org.xbup.plugin.picture.pane;

import java.io.IOException;
import javax.swing.JPanel;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.plugin.XBAbstractPaneEditor;
import org.xbup.lib.plugin.XBPanelEditor;
import org.xbup.lib.visual.xbplugins.XBPicturePanel;

/**
 * XBUP Editor plugin - provides panels for basic XBUP data types.
 *
 * @version 0.1.24 2015/01/14
 * @author XBUP Project (http://xbup.org)
 */
public class PicturePaneEditor extends XBAbstractPaneEditor implements XBPanelEditor, XBPSequenceSerializable {

    private XBPicturePanel value = new XBPicturePanel();

    public PicturePaneEditor() {
        value.attachChangeListener(new XBPicturePanel.ChangeListener() {

            @Override
            public void valueChanged() {
                fireValueChange();
            }
        });
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.append(value);
    }

    @Override
    public JPanel getPanel() {
        return value;
    }

    public XBPicturePanel getValue() {
        return value;
    }

    public void setValue(XBPicturePanel value) {
        this.value = value;
    }

}
