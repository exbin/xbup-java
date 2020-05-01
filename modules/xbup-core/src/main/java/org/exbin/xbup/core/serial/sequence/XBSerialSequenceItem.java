/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.serial.sequence;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.serial.XBSerializable;

/**
 * XBUP level 1 serialization sequence.
 *
 * @version 0.1.24 2015/01/22
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBSerialSequenceItem {

    private XBSerialSequenceOp sequenceOp;
    private XBSerializable item;

    public XBSerialSequenceItem(XBSerialSequenceOp sequenceOp, XBSerializable item) {
        this.sequenceOp = sequenceOp;
        this.item = item;
    }

    @Nonnull
    public XBSerialSequenceOp getSequenceOp() {
        return sequenceOp;
    }

    public void setSequenceOp(XBSerialSequenceOp sequenceOp) {
        this.sequenceOp = sequenceOp;
    }

    @Nonnull
    public XBSerializable getItem() {
        return item;
    }

    public void setItem(XBSerializable item) {
        this.item = item;
    }
}
