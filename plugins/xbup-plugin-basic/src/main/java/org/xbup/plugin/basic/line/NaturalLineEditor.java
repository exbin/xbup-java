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
package org.xbup.plugin.basic.line;

import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JTextField;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerialHandler;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerializable;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.plugin.XBLineEditor;

/**
 * XBUP Editor plugin - provides panels for basic XBUP data types.
 *
 * @version 0.1.24 2014/12/20
 * @author XBUP Project (http://xbup.org)
 */
public class NaturalLineEditor implements XBLineEditor, XBTSequenceSerializable {

    private UBNat32 value = new UBNat32();

    @Override
    public void serializeXB(XBTSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.append(value);
    }

    @Override
    public JComponent getComponent() {
        JTextField component = new JTextField(String.valueOf(value.getLong()));
        component.setEditable(false);
        return component;
    }

    @Override
    public JComponent getEditor() {
        JTextField component = new JTextField(String.valueOf(value.getLong()));
        return component;
    }

    @Override
    public boolean finishEditor(JComponent editor) {
        JTextField component = (JTextField) editor;
        value.setValue(Long.valueOf(component.getText()));
        return true;
    }

    public UBNat32 getValue() {
        return value;
    }

    public void setValue(UBNat32 value) {
        this.value = value;
    }
}
