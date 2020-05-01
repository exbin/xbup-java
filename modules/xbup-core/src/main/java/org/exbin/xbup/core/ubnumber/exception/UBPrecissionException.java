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
package org.exbin.xbup.core.ubnumber.exception;

import javax.annotation.Nullable;

/**
 * Runtime exception related to LRUB-encoded values processing precision issues.
 *
 * @version 0.2.1 2017/05/18
 * @author ExBin Project (http://exbin.org)
 */
public class UBPrecissionException extends UBException {

    /**
     * Creates a new instance of UBPrecissionException.
     */
    public UBPrecissionException() {
        super();
    }

    /**
     * Creates a new instance of UBPrecissionException.
     *
     * @param comment exception comment
     */
    public UBPrecissionException(@Nullable String comment) {
        super(comment);
    }
}
