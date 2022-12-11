/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.serial.sequence;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.serial.XBSerializable;

/**
 * XBUP level 1 serialization sequence.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBSerialSequence {

    @Nullable
    private XBBlockType type;
    @Nonnull
    private XBBlockTerminationMode terminationMode = XBBlockTerminationMode.SIZE_SPECIFIED;
    private List<XBSerialSequenceItem> items = new ArrayList<>();

    public XBSerialSequence() {
    }

    public XBSerialSequence(XBBlockTerminationMode terminationMode) {
        this();
        this.terminationMode = terminationMode;
    }

    public XBSerialSequence(@Nullable XBBlockType type, XBBlockTerminationMode terminationMode) {
        this(terminationMode);
        this.type = type;
    }

    public XBSerialSequence(@Nullable XBBlockType type) {
        this(type, XBBlockTerminationMode.SIZE_SPECIFIED);
    }

    public void add(XBSerialSequenceOp op, XBSerializable item) {
        items.add(new XBSerialSequenceItem(op, item));
    }

    private void privateJoin(XBSerializable item) {
        items.add(new XBSerialSequenceItem(XBSerialSequenceOp.JOIN, item));
    }

    public void join(XBSerializable item) {
        privateJoin(item);
    }

    public void consist(XBSerializable item) {
        items.add(new XBSerialSequenceItem(XBSerialSequenceOp.CONSIST, item));
    }

    public void listJoin(XBListJoinSerializable item) {
        items.add(new XBSerialSequenceItem(XBSerialSequenceOp.LIST_JOIN, item));
    }

    public void listConsist(XBListConsistSerializable item) {
        items.add(new XBSerialSequenceItem(XBSerialSequenceOp.LIST_CONSIST, item));
    }

    @Nullable
    public XBBlockType getBlockType() {
        return type;
    }

    public void setBlockType(@Nullable XBBlockType type) {
        this.type = type;
    }

    @Nonnull
    public XBBlockTerminationMode getTerminationMode() {
        return terminationMode;
    }

    public void setTerminationMode(XBBlockTerminationMode terminationMode) {
        this.terminationMode = terminationMode;
    }

    @Nonnull
    public List<XBSerialSequenceItem> getItems() {
        return items;
    }

    public void setItems(List<XBSerialSequenceItem> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public void append(XBSerialSequence seq) {
        items.addAll(seq.items);
    }
}
