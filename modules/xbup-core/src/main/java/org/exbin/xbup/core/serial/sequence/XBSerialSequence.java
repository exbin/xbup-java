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
 * @version 0.1.24 2015/01/22
 * @author ExBin Project (http://exbin.org)
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
