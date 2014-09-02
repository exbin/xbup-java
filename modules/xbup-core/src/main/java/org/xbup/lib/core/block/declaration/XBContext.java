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
package org.xbup.lib.core.block.declaration;

import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.declaration.local.XBDGroupDecl;

/**
 * Representation of current declaration context for block types.
 *
 * @version 0.1.24 2014/09/01
 * @author XBUP Project (http://xbup.org)
 */
public class XBContext {

    private XBContext parent;
    private int startFrom = 1;
    private List<XBDGroupDecl> groups;

    public XBContext() {
        parent = null;
        groups = new ArrayList<>();
    }

    public XBGroupDecl getGroupForId(int groupId) {
        if (groupId < startFrom) {
            return parent.getGroupForId(groupId);
        }

        if (groupId - startFrom < groups.size()) {
            return groups.get(groupId - startFrom);
        }

        return null;
    }

    public XBContext getParent() {
        return parent;
    }

    public void setParent(XBContext parent) {
        this.parent = parent;
    }

    public int getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(int startFrom) {
        this.startFrom = startFrom;
    }

    public List<XBDGroupDecl> getGroups() {
        return groups;
    }

    public void setGroups(List<XBDGroupDecl> groups) {
        this.groups = groups;
    }
}
