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
package org.xbup.lib.xbvisual;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import org.xbup.lib.xb.block.XBBasicBlockType;
import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.block.XBFixedBlockType;
import org.xbup.lib.xb.block.declaration.XBContext;
import org.xbup.lib.xb.block.declaration.XBDeclaration;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.basic.convert.XBTEncapsulator;
import org.xbup.lib.xb.parser.token.event.convert.XBTEventListenerToListener;
import org.xbup.lib.xb.parser.token.event.convert.XBTListenerToEventListener;
import org.xbup.lib.xb.parser.token.event.convert.XBTToXBEventConvertor;
import org.xbup.lib.xb.parser.token.pull.convert.XBToXBTPullConvertor;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.child.XBTChildListenerSerialHandler;
import org.xbup.lib.xb.serial.child.XBTChildProviderSerialHandler;
import org.xbup.lib.xb.stream.file.XBFileInputStream;
import org.xbup.lib.xb.stream.file.XBFileOutputStream;
import org.xbup.lib.xbvisual.picture.XBBufferedImage;
import org.xbup.lib.xbvisual.xbplugins.XBPicturePanel;

/**
 * Testing Visual
 *
 * @version 0.1 wr23.0 2013/11/21
 * @author XBUP Project (http://xbup.org)
 */
public class Test1 {

    public static void main(String args[]) {
        test1();
    }

    private static void test1() {
        try {
            File f = new File("samples/standby.png");
            XBDeclaration declaration = new XBDeclaration() {

                @Override
                public XBFixedBlockType toStaticType(XBBlockType type) {
                    return new XBFixedBlockType(XBBasicBlockType.BLOCK_DECLARATION);
                }
            };
            XBBufferedImage image = new XBBufferedImage(ImageIO.read(f));
            XBFileOutputStream output = new XBFileOutputStream("picture.dat");
            XBTChildListenerSerialHandler handler = new XBTChildListenerSerialHandler();
            XBTEncapsulator encapsulator = new XBTEncapsulator(new XBContext(null, image.getXBDeclaration()));
            encapsulator.attachXBTListener(new XBTEventListenerToListener(new XBTToXBEventConvertor(output)));
            handler.attachXBTEventListener(new XBTListenerToEventListener(encapsulator));
            image.serializeXB(XBSerializationType.TO_XB, 0, handler);
//            new XBL1ToL0StreamConvertor.XBCL1ToL0DefaultStreamConvertor((XBCL1Streamable) new XBL2ToL1StreamConvertor.XBCL2ToL1DefaultStreamConvertor(text)).writeXBStream(output);
            output.close();
            image.setImage(null);
            XBFileInputStream input = new XBFileInputStream("picture.dat");
            XBTChildProviderSerialHandler providerHandler = new XBTChildProviderSerialHandler();
            providerHandler.attachXBTPullProvider(new XBToXBTPullConvertor(input));
            image.serializeXB(XBSerializationType.FROM_XB, 0, providerHandler);

            output = new XBFileOutputStream("picture2.dat");
            XBTChildListenerSerialHandler listenerHandler = new XBTChildListenerSerialHandler();
            XBTEncapsulator encapsulator1 = new XBTEncapsulator(new XBContext(null, image.getXBDeclaration()));
            encapsulator1.attachXBTListener(new XBTEventListenerToListener(new XBTToXBEventConvertor(output)));
            listenerHandler.attachXBTEventListener(new XBTListenerToEventListener(encapsulator1));
            image.serializeXB(XBSerializationType.TO_XB, 0, listenerHandler);
//            new XBL1ToL0StreamConvertor.XBCL1ToL0DefaultStreamConvertor((XBCL1Streamable) new XBL2ToL1StreamConvertor.XBCL2ToL1DefaultStreamConvertor(text)).writeXBStream(output);
            output.close();
        } catch (XBProcessingException ex) {
            Logger.getLogger(Test1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Test1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Test1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void test2() {
        JFrame panel = new JFrame();
        panel.setSize(600,400);
        panel.add(new XBPicturePanel());
        panel.setVisible(true);
        panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
