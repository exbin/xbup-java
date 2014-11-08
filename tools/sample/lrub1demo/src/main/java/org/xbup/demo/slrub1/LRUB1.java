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
package org.xbup.demo.slrub1;

import java.util.ArrayList;
import java.util.List;

/**
 * Library for simple conversion of UBNumber encoding schemes.
 *
 * @version 0.1.20 2011/01/16
 * @author XBUP Project (http://xbup.org)
 */
public class LRUB1 {

    public String codeFromNumber(long value) {
        StringBuilder result = new StringBuilder();
        codeFromNumber(result, value);
        return result.toString();
    }

    public void codeFromNumber(StringBuilder result, long value) {

        LengthCode lengthCode = new LengthCode();
        long decrementor = 2;
        long length = 1;

        while (value >= decrementor) {
            long nextLength = lengthCode.nextLength();

            value -= decrementor;

            if (nextLength > length) {
                length = nextLength;
                decrementor *= 4;
            }
        }
        lengthCode.getCode(result);
        //result.append(" ");
        result.append(numberToBinary(value, (int) length));
    }

    public long numberFromCode(String source) {
        LengthCode lengthCode = new LengthCode();

        long incrementor = 2;
        long length = 1;
        int sourcePos = 0;
        int depth = 0;
        long value = 0;
        while ("1".equals(String.valueOf(source.charAt(sourcePos)))) {
            sourcePos++;
            depth++;
            while (lengthCode.getDepth() < depth) {
                long nextLength = lengthCode.nextLength();

                value += incrementor;
                if (nextLength > length) {
                    length = nextLength;
                    incrementor *= 4;
                }
            }
        }
        sourcePos++;
        while (depth > 0) {
            depth--;
            DepthValue status = lengthCode.getDepthValue(depth);
            long subValue = binaryToNumber(source.substring(sourcePos, sourcePos + status.getLength()));
            sourcePos += status.getLength();
            while (lengthCode.getDepthValue(depth).getValue() < subValue) {
                long nextLength = lengthCode.nextLength();

                value += incrementor;
                if (nextLength > length) {
                    length = nextLength;
                    incrementor *= 4;
                }
            }
        }
        value += binaryToNumber(source.substring(sourcePos, sourcePos + (int) length));
        return value;
    }

    /**
     * Convert number to binary sequence of given length.
     *
     * @param value value
     * @param length target length
     * @return
     */
    public String numberToBinary(long value, int length) {
        String result = Long.toBinaryString(value);
        if (result.length() < length) {
            result = repeat("0", length - result.length()) + result;
        }
        return result;
    }

    public long binaryToNumber(String value) {
        return Long.parseLong(value, 2);
    }

    /**
     * Return n-times repeate contatenation of given string.
     */
    private String repeat(String string, int repetitions) {
        StringBuilder stringBuilder = new StringBuilder(string.length() * repetitions);
        for (int i = 0; i < repetitions; i++) {
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    public class CodeResult {

        private String prefix;
        private long length;

        /**
         * @return the value
         */
        public String getValue() {
            return prefix;
        }

        /**
         * @param value the value to set
         */
        public void setValue(String value) {
            this.prefix = value;
        }

        /**
         * @return the length
         */
        public long getLength() {
            return length;
        }

        /**
         * @param length the length to set
         */
        public void setLength(long length) {
            this.length = length;
        }
    }

    public class DepthValue {

        private long value;
        private long limit;
        private int length;

        public DepthValue(long value, int length, long limit) {
            this.value = value;
            this.length = length;
            this.limit = limit;
        }

        /**
         * @return the value
         */
        public long getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(long value) {
            this.value = value;
        }

        /**
         * @return the limit
         */
        public long getLimit() {
            return limit;
        }

        /**
         * @param limit the limit to set
         */
        public void setLimit(long limit) {
            this.limit = limit;
        }

        /**
         * @return the length
         */
        public int getLength() {
            return length;
        }

        /**
         * @param length the length to set
         */
        public void setLength(int length) {
            this.length = length;
        }
    }

    public class LengthCode {

        private final List<DepthValue> statuses;
        private int length;

        public LengthCode() {
            statuses = new ArrayList<>();
            length = 0;
        }

        public long nextLength() {
            if (statuses.isEmpty()) {
                statuses.add(new DepthValue(0, 1, 1));
                length = 1;
                return length;
            }
            int depth = 0;
            DepthValue value = statuses.get(depth);
            if (value.getValue() < value.getLimit()) {
                value.setValue(value.getValue() + 1);
                length += 2;
            } else {
                while (depth < statuses.size()) {
                    value = statuses.get(depth);
                    if (value.getValue() < value.getLimit()) {
                        value.setValue(value.getValue() + 1);
                        break;
                    } else {
                        value.setValue(0);
                        if (depth == statuses.size() - 1) {
                            statuses.add(new DepthValue(0, 1, 1));
                            break;
                        } else {
                            depth++;
                            DepthValue next = statuses.get(depth);
                            if (next.getValue() < next.getLimit()) {
                                value.setLimit(value.getLimit() * 2 + 1);
                                value.setLength(value.getLength() + 2);
                            }
                        }
                    }
                }
            }
            return length;
        }

        private void getCode(StringBuilder result) {
            result.append(repeat("1", statuses.size()));
            result.append("0");
            for (int i = statuses.size() - 1; i >= 0; i--) {
                DepthValue depthValue = statuses.get(i);
                //result.append("|");
                result.append(numberToBinary(depthValue.getValue(), depthValue.getLength()));
            }
        }

        private int getDepth() {
            return statuses.size();
        }

        private DepthValue getDepthValue(int index) {
            return statuses.get(index);
        }
    }
}
