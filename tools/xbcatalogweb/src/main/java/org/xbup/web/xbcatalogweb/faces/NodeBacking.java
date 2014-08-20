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
package org.xbup.web.xbcatalogweb.faces;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.catalog.entity.XBENode;

/**
 * Node controller.
 *
 * @version 0.1 wr23.0 2014/03/18
 * @author XBUP Project (http://xbup.org)
 */
@Controller
@Scope("view")
@Qualifier("nodeBacking")
public final class NodeBacking implements Serializable {

    @Autowired
    private XBCNodeService nodeService;

    private List<XBENode> nodes;

    public NodeBacking() {
        /* nodes = new ArrayList<XBENode>();
         XBENode node = new XBENode();
         node.setId(new Long(1));
         nodes.add(node); */
    }

    @PostConstruct
    public void init() {
        nodes = nodeService.getAllItems();
    }

    public List<XBENode> getNodes() {
        return nodes;
    }

    public void setNodes(List<XBENode> nodes) {
        this.nodes = nodes;
    }
}
