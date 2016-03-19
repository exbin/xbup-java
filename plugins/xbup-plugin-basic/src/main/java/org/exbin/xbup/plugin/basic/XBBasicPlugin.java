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
package org.exbin.xbup.plugin.basic;

import javax.swing.JPanel;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.exbin.xbup.plugin.XBLineEditor;
import org.exbin.xbup.plugin.XBPanelEditor;
import org.exbin.xbup.plugin.XBPlugin;
import org.exbin.xbup.plugin.XBTransformation;
import org.exbin.xbup.plugin.basic.line.BooleanLineEditor;
import org.exbin.xbup.plugin.basic.line.IntegerLineEditor;
import org.exbin.xbup.plugin.basic.line.NaturalLineEditor;
import org.exbin.xbup.plugin.basic.line.StringLineEditor;

/**
 * XBUP Editor plugin - provides panels for basic XBUP data types.
 *
 * @version 0.1.24 2014/12/15
 * @author ExBin Project (http://exbin.org)
 */
@PluginImplementation
public class XBBasicPlugin implements XBPlugin {

    @Override
    public String getPluginPath() {
        return "basic/xbup-plugin-basic-0.1.24-SNAPSHOT.jar";
    }

    @Override
    public long getLineEditorsCount() {
        return 4;
    }

    @Override
    public XBLineEditor getLineEditor(long index) {
        switch ((int) index) {
            case 0: {
                return new NaturalLineEditor();
            }
            case 1: {
                return new IntegerLineEditor();
            }
            case 2: {
                return new StringLineEditor();
            }
            case 3: {
                return new BooleanLineEditor();
            }
        }
        return null;
    }

    @Override
    public long getPanelEditorsCount() {
        return 1;
    }

    @Override
    public XBPanelEditor getPanelEditor(long index) {
        if (index == 0) {
            return new XBPanelEditor() {

                @Override
                public JPanel getPanel() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void attachChangeListener(XBPanelEditor.ChangeListener listener) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            };
        }
        return null;
    }

    @Override
    public long getTransformationCount() {
        return 0;
    }

    @Override
    public XBTransformation getTransformation(long index) {
        return null;
    }
}
