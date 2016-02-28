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
package org.xbup.plugin.basic.line;

import java.io.IOException;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.ubnumber.type.UBBool;
import org.xbup.lib.plugin.XBAbstractLineEditor;
import org.xbup.lib.plugin.XBLineEditor;

/**
 * XBUP Editor plugin - provides panels for basic XBUP data types.
 *
 * @version 0.1.24 2015/01/13
 * @author ExBin Project (http://exbin.org)
 */
public class BooleanLineEditor extends XBAbstractLineEditor implements XBLineEditor, XBPSequenceSerializable {

    private UBBool value = new UBBool();
    private static final String TRUE_TEXT = "true";
    private static final String FALSE_TEXT = "false";

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.append(value);
    }

    @Override
    public JComponent getComponent() {
        JCheckBox component = new JCheckBox(FALSE_TEXT);
        component.addChangeListener(new javax.swing.event.ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JCheckBox source = (JCheckBox) e.getSource();
                source.setText(source.isSelected() ? TRUE_TEXT : FALSE_TEXT);
            }
        });
        component.setSelected(value.getBoolean());
        // component.setEditable(false);
        component.setOpaque(false);
        return component;
    }

    @Override
    public JComponent getEditor() {
        JCheckBox component = new JCheckBox(FALSE_TEXT);
        component.addChangeListener(new javax.swing.event.ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JCheckBox source = (JCheckBox) e.getSource();
                source.setText(source.isSelected() ? TRUE_TEXT : FALSE_TEXT);
            }
        });
        component.setSelected(value.getBoolean());
        component.requestFocusInWindow();
        return component;
    }

    @Override
    public boolean finishEditor(JComponent editor) {
        JCheckBox component = (JCheckBox) editor;
        value.setValue(component.isSelected());
        fireValueChange();
        return true;
    }

    public UBBool getValue() {
        return value;
    }

    public void setValue(UBBool value) {
        this.value = value;
    }
}
