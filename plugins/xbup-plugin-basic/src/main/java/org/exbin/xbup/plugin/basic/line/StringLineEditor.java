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
package org.exbin.xbup.plugin.basic.line;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JTextField;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.type.XBString;
import org.exbin.xbup.plugin.XBAbstractLineEditor;
import org.exbin.xbup.plugin.XBLineEditor;

/**
 * XBUP Editor plugin - provides panels for basic XBUP data types.
 *
 * @version 0.1.24 2015/01/09
 * @author ExBin Project (http://exbin.org)
 */
public class StringLineEditor extends XBAbstractLineEditor implements XBLineEditor, XBPSequenceSerializable {

    private XBString value = new XBString();

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.append(value);
    }

    @Override
    public JComponent getComponent() {
        JTextField component = new JTextField(value.getValue());
        component.setEditable(false);
        component.setOpaque(false);
        return component;
    }

    @Override
    public JComponent getEditor() {
        JTextField component = new JTextField(value.getValue());
        component.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(final FocusEvent fe) {
            }

            @Override
            public void focusGained(final FocusEvent fe) {
                ((JTextField) fe.getComponent()).selectAll();
            }
        });
        return component;
    }

    @Override
    public boolean finishEditor(JComponent editor) {
        JTextField component = (JTextField) editor;
        component.setCaretPosition(0);
        value.setValue(component.getText());
        fireValueChange();
        return true;
    }

    public XBString getValue() {
        return value;
    }

    public void setValue(XBString value) {
        this.value = value;
    }
}
