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
package org.exbin.xbup.core.remote;

import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;

/**
 * XBUP level 1 RPC procedure definition interface.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface XBProcedureDef {

    /**
     * Returns type of this procedure.
     *
     * @return type of this procedure
     */
    XBBlockType getType();

    /**
     * Returns type of input data parameter.
     *
     * @return declaration of input type
     */
    XBBlockDecl getParameterType();

    /**
     * Returns type of output data returned as result.
     *
     * @return declaration of output type
     */
    XBBlockDecl getReturnType();

    /**
     * Returns type of status data for reporting proper execution or exception.
     *
     * @return declaration of execution status type
     */
    XBBlockDecl getStatusType();
}
