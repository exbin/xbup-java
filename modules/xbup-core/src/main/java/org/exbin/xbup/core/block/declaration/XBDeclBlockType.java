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
package org.exbin.xbup.core.block.declaration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBDBlockType;
import org.exbin.xbup.core.block.declaration.local.XBLBlockDecl;

/**
 * Block type defined using block declaration.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBDeclBlockType implements XBDBlockType {

    @Nonnull
    private XBBlockDecl blockDecl;

    public XBDeclBlockType(XBBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }

    public XBDeclBlockType(long[] blockRevisionCatalogPath) {
        this.blockDecl = new XBLBlockDecl(blockRevisionCatalogPath);
    }

    public XBDeclBlockType(Long[] blockRevisionCatalogPath) {
        this.blockDecl = new XBLBlockDecl(blockRevisionCatalogPath);
    }

    @Nullable
    @Override
    public XBBasicBlockType getAsBasicType() {
        return null;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof XBDeclBlockType) {
            return ((XBDeclBlockType) obj).blockDecl.equals(blockDecl);
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return blockDecl.hashCode();
    }

    @Nonnull
    @Override
    public XBBlockDecl getBlockDecl() {
        return blockDecl;
    }

    public void setBlockDecl(XBBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }
}
