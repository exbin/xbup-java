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
package org.exbin.framework.api;

/**
 * Framework application event class.
 *
 * @version 0.2.0 2015/11/16
 * @author ExBin Project (http://exbin.org)
 */
public class XBApplicationEvent {

    private XBApplication application;
    private XBApplicationEventType eventType;
    private Object parameter;

    public XBApplication getApplication() {
        return application;
    }

    public void setApplication(XBApplication application) {
        this.application = application;
    }

    public XBApplicationEventType getEventType() {
        return eventType;
    }

    public void setEventType(XBApplicationEventType eventType) {
        this.eventType = eventType;
    }

    public Object getParameter() {
        return parameter;
    }

    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }

    /**
     * Enumeration of event types.
     */
    public enum XBApplicationEventType {
        INIT,
        STARTUP
    }
}
