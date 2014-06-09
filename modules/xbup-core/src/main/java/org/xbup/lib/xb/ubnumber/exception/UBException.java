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
package org.xbup.lib.xb.ubnumber.exception;

/**
 * Runtime exception related to LRUB-encoded values processing.
 *
 * @version 0.1 wr24.0 2014/06/07
 * @author XBUP Project (http://xbup.org)
 */
public class UBException extends RuntimeException {

    /**
     * Creates a new instance of UBException.
     */
    public UBException() {
    }

    /**
     * Creates a new instance of UBException.
     *
     * @param comment exception comment
     */
    public UBException(String comment) {
    }
}
