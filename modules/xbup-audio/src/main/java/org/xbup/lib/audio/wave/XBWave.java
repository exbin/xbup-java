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
package org.xbup.lib.audio.wave;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.xbup.lib.core.block.XBBlockData;
import org.xbup.lib.core.block.XBEditableBlockData;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;
import org.xbup.lib.core.type.XBData;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Simple panel audio wave.
 *
 * @version 0.2.0 2016/01/24
 * @author ExBin Project (http://exbin.org)
 */
public class XBWave implements XBPSequenceSerializable {

    public static final long[] XBUP_BLOCKREV_CATALOGPATH = {1, 5, 0, 0};
    public static final long[] XBUP_FORMATREV_CATALOGPATH = {1, 5, 0, 0};
    private AudioFormat audioFormat;
    private final XBData data = new XBData(65520);

    public XBWave() {
        audioFormat = null;
    }

    public XBWave(AudioFormat audioFormat) {
        this.audioFormat = audioFormat;
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
            data.loadFromStream(audioInputStream);
//        } catch (LineUnavailableException ex) {
//            Logger.getLogger(XBWave.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(XBWave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveToFile(File soundFile) {
        saveToFile(soundFile, Type.WAVE);
    }

    public void saveToFile(File soundFile, Type fileType) {
        try {
            AudioSystem.write(new AudioInputStream(data.getDataInputStream(), audioFormat, getLengthInTicks()), fileType, soundFile);
        } catch (IOException ex) {
            Logger.getLogger(XBWave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBDeclBlockType(XBUP_BLOCKREV_CATALOGPATH));
        if (serial.getSerializationMode() == XBSerializationMode.PULL) {
            UBNatural sampleRate = serial.pullAttribute().convertToNatural();
            UBNatural sampleSizeInBits = serial.pullAttribute().convertToNatural();
            UBNatural channels = serial.pullAttribute().convertToNatural();
            UBNatural signed = serial.pullAttribute().convertToNatural();
            UBNatural bigEndian = serial.pullAttribute().convertToNatural();
            audioFormat = new AudioFormat(sampleRate.getInt(), sampleSizeInBits.getInt(), channels.getInt(), signed.getInt() == 1, bigEndian.getInt() == 1);
        } else {
            serial.putAttribute(new UBNat32((long) audioFormat.getSampleRate()));
            serial.putAttribute(new UBNat32(audioFormat.getSampleSizeInBits()));
            serial.putAttribute(new UBNat32(audioFormat.getChannels()));
            serial.putAttribute(new UBNat32(audioFormat.getEncoding() == AudioFormat.Encoding.PCM_SIGNED ? 1 : 0));
            serial.putAttribute(new UBNat32(audioFormat.isBigEndian() ? 1 : 0));
        }
        serial.consist(new XBPSequenceSerializable() {
            @Override
            public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
                serial.begin();
                if (serial.getSerializationMode() == XBSerializationMode.PULL) {
                    data.loadFromStream(serial.pullData());
                } else {
                    serial.putData(data.getDataInputStream());
                }
                serial.end();
            }
        });
        serial.end();
    }

    public void performTransformReverse() {
        performTransformReverse(0, getLengthInTicks() - 1);
    }

    public void performTransformReverse(long startPosition, long endPosition) {
        if (!data.isEmpty()) {
            long startDataPos = startPosition * audioFormat.getChannels() * 2;
            long endDataPos = endPosition * audioFormat.getChannels() * 2;

            // TODO support for non-whole-byte alignment later
            int sampleSize = audioFormat.getSampleSizeInBits() / 8;
            byte[] buffer = new byte[sampleSize];
            long remaining = endPosition - startPosition;
            int headBlockIndex = (int) (startDataPos / data.getPageSize());
            int headPosition = (int) (startDataPos % data.getPageSize());
            byte[] headBlock = data.getPage(headBlockIndex);
            int tailBlockIndex = (int) (endDataPos / data.getPageSize());
            int tailPosition = (int) (endDataPos % data.getPageSize());
            byte[] tailBlock = data.getPage(tailBlockIndex);

            while (remaining > 0) {
                if (headPosition + sampleSize <= data.getPageSize()) {
                    System.arraycopy(headBlock, headPosition, buffer, 0, sampleSize);
                    headPosition += sampleSize;
                } else {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                if (tailPosition >= sampleSize) {
                    System.arraycopy(tailBlock, tailPosition - sampleSize, headBlock, headPosition - sampleSize, sampleSize);
                    System.arraycopy(buffer, 0, tailBlock, tailPosition - sampleSize, sampleSize);
                    tailPosition -= sampleSize;
                } else {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                if (headPosition == data.getPageSize()) {
                    headPosition = 0;
                    headBlockIndex++;
                    headBlock = data.getPage(headBlockIndex);
                }

                if (tailPosition == 0) {
                    tailPosition = data.getPageSize();
                    tailBlockIndex--;
                    tailBlock = data.getPage(tailBlockIndex);
                }

                remaining--;
            }
        }
    }

    public int getPageSize() {
        return data.getPageSize();
    }

    public InputStream getInputStream() {
        return data.getDataInputStream();
    }

    public AudioInputStream getAudioInputStream() {
        return new AudioInputStream(data.getDataInputStream(), audioFormat, getLengthInTicks());
    }

    /**
     * Returns data size in bytes.
     *
     * @return size in bytes
     */
    public long getDataSize() {
        return data.getDataSize();
    }

    /**
     * Returns data for single tick.
     * 
     * @param targetData target data
     * @param position tick position
     * @param channel channgel number
     */
    public void getValue(XBEditableBlockData targetData, int position, int channel) {
        int bytesPerSample = audioFormat.getSampleSizeInBits() >> 3;
        int dataPosition = (position * audioFormat.getChannels() + channel) * bytesPerSample;
        data.copyTo(targetData, dataPosition, bytesPerSample, 0);
    }

    public int getRatioValue(int position, int channel, int height) {
        int bytesPerSample = audioFormat.getSampleSizeInBits() >> 3;

        int chunk = ((position * audioFormat.getChannels() + channel) * bytesPerSample) / data.getPageSize();
        int offset = ((position * audioFormat.getChannels() + channel) * bytesPerSample) % data.getPageSize();

        long value;
        if (audioFormat.getEncoding() == AudioFormat.Encoding.PCM_UNSIGNED) {
            value = data.getPage(chunk)[offset] & 0xFF;
        } else {
            value = data.getPage(chunk)[offset];
        }
        if (bytesPerSample > 1) {
            value += ((long) ((data.getPage(chunk)[offset + 1] + 127)) << 8);
            if (bytesPerSample > 2) {
                value += ((long) ((data.getPage(chunk)[offset + 2] + 127)) << 16);
                if (bytesPerSample > 3) {
                    value += ((long) ((data.getPage(chunk)[offset + 3] + 127)) << 24);
                }
            }
        }

        return (int) (((long) value * height) >> audioFormat.getSampleSizeInBits());
    }

    public void setValue(XBBlockData sourceData, int position, int channel) {
        int bytesPerSample = audioFormat.getSampleSizeInBits() >> 3;
        int dataPosition = (position * audioFormat.getChannels() + channel) * bytesPerSample;
        sourceData.copyTo(data, 0, bytesPerSample, dataPosition);
    }

    public void setRatioValue(int pos, int value, int channel, int height) {
        // TODO: support for different bitsize
        int chunk = ((pos * audioFormat.getChannels() + channel) * 2) / data.getPageSize();
        int offset = ((pos * audioFormat.getChannels() + channel) * 2) % data.getPageSize();
        byte[] block = data.getPage(chunk);

        int pomValue = ((value - (height / 2)) << 16) / height;
        block[offset] = (byte) (pomValue & 255);
        block[offset + 1] = (byte) ((pomValue >> 8) & 255);
        data.setPage(chunk, block);
        /*        int value = 127 + getBlock(chunk)[offset] + (getBlock(chunk)[offset+1] + 127)*256;
         return (int) ((long) value * height) / 65536; */
    }

    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

    public int getLengthInTicks() {
        int bytesPerSample = audioFormat.getSampleSizeInBits() >> 3;
        return (int) data.getDataSize() / (audioFormat.getChannels() * bytesPerSample);
    }

    public void setLengthInTicks(int ticks) {
        int bytesPerSample = audioFormat.getSampleSizeInBits() >> 3;
        data.setDataSize(ticks * audioFormat.getChannels() * bytesPerSample);
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

    /**
     * Cuts section of wave as plain data.
     *
     * @param startPosition position in ticks
     * @param length length in ticks
     * @return data blob
     */
    public XBData cutData(int startPosition, int length) {
        long startDataPos = startPosition * audioFormat.getChannels() * 2;
        long dataLength = length * audioFormat.getChannels() * 2;
        XBData cutData = data.copy(startDataPos, dataLength);
        data.remove(startDataPos, dataLength);
        return cutData;
    }

    public void insertData(XBData deletedData, int startPosition) {
        long startDataPos = startPosition * audioFormat.getChannels() * 2;
        data.insert(startDataPos, deletedData);
    }

    public XBWave copy(int startPosition, int length) {
        long startDataPos = startPosition * audioFormat.getChannels() * 2;
        long dataLength = length * audioFormat.getChannels() * 2;
        XBWave waveCopy = new XBWave();
        waveCopy.audioFormat = audioFormat;
        data.copyTo(waveCopy.data, startDataPos, dataLength, 0);
        return waveCopy;
    }

    public void insertWave(XBWave pastedWave, int startPosition) {
        // TODO match audio format
        insertData(pastedWave.data, startPosition);
    }
}
