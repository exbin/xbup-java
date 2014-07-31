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
package org.xbup.lib.audio.wave;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.xbup.lib.core.block.declaration.XBDBlockType;
import org.xbup.lib.core.block.declaration.XBDeclaration;
import org.xbup.lib.core.catalog.declaration.XBCDeclaration;
import org.xbup.lib.core.catalog.declaration.XBCPBlockDecl;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerialHandler;
import org.xbup.lib.core.serial.XBSerialMethod;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.XBSerializationType;
import org.xbup.lib.core.serial.child.XBTChildListener;
import org.xbup.lib.core.serial.child.XBTChildListenerSerialMethod;
import org.xbup.lib.core.serial.child.XBTChildProvider;
import org.xbup.lib.core.serial.child.XBTChildProviderSerialMethod;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Simple panel audio wave.
 *
 * @version 0.1 wr23.0 2014/03/04
 * @author XBUP Project (http://xbup.org)
 */
public class XBWave implements XBSerializable {

    public static long[] xbBlockPath = {0, 1000, 0, 0}; // Testing only
    private AudioFormat audioFormat;
    private List<byte[]> data;
    public int chunkSize;

    public XBWave() {
        audioFormat = null;
        chunkSize = 65520;
        data = new ArrayList<byte[]>();
    }

    public void loadFromFile(File soundFile) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            audioFormat = audioInputStream.getFormat();
            System.out.println(getAudioFormat());
            if ((audioFormat.getChannels() > 2)
                    || (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED && audioFormat.getSampleSizeInBits() != 8)
                    || (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED && audioFormat.getEncoding() != AudioFormat.Encoding.PCM_UNSIGNED && audioFormat.getSampleSizeInBits() == 8)
                    || (!(audioFormat.getSampleSizeInBits() == 8 || audioFormat.getSampleSizeInBits() == 16 || audioFormat.getSampleSizeInBits() == 24 || audioFormat.getSampleSizeInBits() == 32))) {
                System.out.println("Unable to load! Currently only 44kHz SIGNED 16bit Mono/Stereo is supported.");
                return;
            }
//            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, getAudioFormat());
//            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);

//            sourceDataLine.open(getAudioFormat());
//            sourceDataLine.start();
            loadRawFromStream(audioInputStream);
//        } catch (LineUnavailableException ex) {
//            Logger.getLogger(XBWave.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(XBWave.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBWave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadRawFromStream(InputStream inputStream) {
        try {
            data = new ArrayList<byte[]>();
            byte[] buffer = new byte[chunkSize];
            int cnt;
            int offset = 0;
            while ((cnt = inputStream.read(buffer, offset, buffer.length - offset)) != -1) {
                if (cnt + offset < chunkSize) {
                    offset = offset + cnt;
                } else {
                    data.add(buffer);
                    buffer = new byte[chunkSize];
                    offset = 0;
                }
            }

            if (offset > 0) {
                byte[] tail = new byte[offset];
                System.arraycopy(buffer, 0, tail, 0, offset - 1);
                data.add(tail);
            }
        } catch (IOException ex) {
            Logger.getLogger(XBWave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveToFile(File soundFile) {
        saveToFile(soundFile, Type.WAVE);
    }

    public void saveToFile(File soundFile, Type fileType) {
        try {
            AudioSystem.write(new AudioInputStream(new WaveInputStream(), audioFormat, getLengthInTicks()), fileType, soundFile);
        } catch (IOException ex) {
            Logger.getLogger(XBWave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public XBDeclaration getXBDeclaration() {
        return new XBCDeclaration(new XBCPBlockDecl(xbBlockPath));
    }

    @Override
    public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
        return serialType == XBSerializationType.FROM_XB
                ? Arrays.asList(new XBSerialMethod[]{new XBTChildProviderSerialMethod()})
                : Arrays.asList(new XBSerialMethod[]{new XBTChildListenerSerialMethod()});
    }

    @Override
    public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
        if (serialType == XBSerializationType.FROM_XB) {
            XBTChildProvider serial = (XBTChildProvider) serializationHandler;
            serial.getType(); //setType(new XBCBlockDecl(xbBlockPath));
            UBNatural sampleRate = serial.nextAttribute();
            UBNatural sampleSizeInBits = serial.nextAttribute();
            UBNatural channels = serial.nextAttribute();
            UBNatural signed = serial.nextAttribute();
            UBNatural bigEndian = serial.nextAttribute();
            audioFormat = new AudioFormat(sampleRate.getInt(), sampleSizeInBits.getInt(), channels.getInt(), signed.getInt() == 1, bigEndian.getInt() == 1);
            serial.nextChild(new XBSerializable() {
                @Override
                public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
                    return Arrays.asList(new XBSerialMethod[]{new XBTChildProviderSerialMethod()});
                }

                @Override
                public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
                    XBTChildProvider serial = (XBTChildProvider) serializationHandler;
                    loadRawFromStream(serial.nextData());
                    serial.end();
                }
            }, 0);
            serial.end();
        } else {
            XBTChildListener serial = (XBTChildListener) serializationHandler;
            serial.setType(new XBDBlockType(new XBCPBlockDecl(xbBlockPath)));
            serial.addAttribute(new UBNat32((long) audioFormat.getSampleRate()));
            serial.addAttribute(new UBNat32(audioFormat.getSampleSizeInBits()));
            serial.addAttribute(new UBNat32(audioFormat.getChannels()));
            serial.addAttribute(new UBNat32(audioFormat.getEncoding() == AudioFormat.Encoding.PCM_SIGNED ? 1 : 0));
            serial.addAttribute(new UBNat32(audioFormat.isBigEndian() ? 1 : 0));
            serial.addChild(new XBSerializable() {
                @Override
                public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
                    return Arrays.asList(new XBSerialMethod[]{new XBTChildListenerSerialMethod()});
                }

                @Override
                public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
                    XBTChildListener serial = (XBTChildListener) serializationHandler;
                    serial.addData(new WaveInputStream());
                    serial.end();
                }
            }, 0);
            serial.end();
        }
    }

    private class WaveInputStream extends InputStream {

        private int blockPosition;
        private int offsetPosition;

        public WaveInputStream() {
            blockPosition = 0;
            offsetPosition = 0;
        }

        @Override
        public int read() throws IOException {
            if (blockPosition >= data.size()) {
                return -1;
            }

            byte[] block = data.get(blockPosition);
            int result = (int) block[offsetPosition] & 0xFF;
            offsetPosition++;
            if (offsetPosition >= block.length) {
                blockPosition++;
                offsetPosition = 0;
            }

            return result;
        }

        @Override
        public int read(byte[] output, int off, int len) throws IOException {
            if (blockPosition >= data.size()) {
                return -1;
            }

            if (output.length == 0) {
                return 0;
            }

            int length = len;
            if (length > output.length - off) {
                length = output.length - off;
            }

            byte[] block = data.get(blockPosition);
            if (length + offsetPosition >= block.length) {
                length = block.length - offsetPosition;
            }

            System.arraycopy(block, offsetPosition, output, off, length);
            offsetPosition += length;
            if (offsetPosition >= block.length) {
                blockPosition++;
                offsetPosition = 0;
            }

            return length;
        }

        @Override
        public int available() throws IOException {
            if (blockPosition >= data.size()) {
                return 0;
            }

            return 1;
        }
    }

    public InputStream getInputStream() {
        return new WaveInputStream();
    }

    public AudioInputStream getAudioInputStream() {
        return new AudioInputStream(new WaveInputStream(), audioFormat, getLengthInTicks());
    }

    public List<byte[]> getData() {
        return data;
    }

    public void setData(List<byte[]> data) {
        this.data = data;
    }

    public int getRatioValue(int pos, int channel, int height) {
        int bytesPerSample = audioFormat.getSampleSizeInBits() >> 3;

        int chunk = ((pos * audioFormat.getChannels() + channel) * bytesPerSample) / chunkSize;
        int offset = ((pos * audioFormat.getChannels() + channel) * bytesPerSample) % chunkSize;

        long value;
        if (audioFormat.getEncoding() == AudioFormat.Encoding.PCM_UNSIGNED) {
            value = getBlock(chunk)[offset] & 0xFF;
        } else {
            value = 127 + getBlock(chunk)[offset];
        }
        if (bytesPerSample > 1) {
            value += ((long) ((getBlock(chunk)[offset + 1] + 127)) << 8);
            if (bytesPerSample > 2) {
                value += ((long) ((getBlock(chunk)[offset + 2] + 127)) << 16);
                if (bytesPerSample > 3) {
                    value += ((long) ((getBlock(chunk)[offset + 3] + 127)) << 24);
                }
            }
        }

        return (int) (((long) value * height) >> audioFormat.getSampleSizeInBits());
    }

    public void setRatioValue(int pos, int value, int channel, int height) {
        // TODO: support for different bitsize
        int chunk = ((pos * audioFormat.getChannels() + channel) * 2) / chunkSize;
        int offset = ((pos * audioFormat.getChannels() + channel) * 2) % chunkSize;
        byte[] block = getBlock(chunk);

        int pomValue = ((value - (height / 2)) << 16) / height;
        block[offset] = (byte) (pomValue & 255);
        block[offset + 1] = (byte) ((pomValue >> 8) & 255);
        /*        int value = 127 + getBlock(chunk)[offset] + (getBlock(chunk)[offset+1] + 127)*256;
         return (int) ((long) value * height) / 65536; */
    }

    public byte[] getBlock(int pos) {
        if (pos >= data.size()) {
            return null;
        }
        return data.get(pos);
    }

    /**
     * @return the audioFormat
     */
    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

    public int getLengthInTicks() {
        return ((data.size() - 1) * chunkSize + data.get(data.size() - 1).length) / (audioFormat.getChannels() * 2);
    }

    public void append(byte[] data) {

    }

    public void apendTo(XBWave wave) {
        apendTo(wave, 0, getLengthInTicks());
    }

    public void apendTo(XBWave wave, int start, int length) {

    }

    public byte[] readChunk(int start, int length) {
        int pos = 0;
        return null;
    }

    public XBWave cut(int start, int length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
