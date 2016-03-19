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
package org.exbin.xbup.web.xbcatalogweb.faces;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.exbin.xbup.catalog.entity.service.XBEXUserService;
import org.exbin.xbup.web.xbcatalogweb.service.XBEUserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Main Website Index controller.
 *
 * @version 0.1.24 2015/01/16
 * @author ExBin Project (http://exbin.org)
 */
@Controller
@Qualifier("indexController")
public final class IndexController implements Serializable {

    @Autowired
    private XBEXUserService userService;
    @Autowired
    private XBEUserRecordService userRecordService;

    public IndexController() {
    }

    @PostConstruct
    public void init() {
    }

    public String getRedirectTarget(HttpServletRequest request) {
        String node = request.getParameter("node");
        String format = request.getParameter("format");
        String group = request.getParameter("group");
        String block = request.getParameter("block");
        if (node != null) {
            return "views/item.xhtml?node=" + node;
        }

        if (format != null && !format.isEmpty()) {
            return "views/item.xhtml?format=" + format;
        }

        if (group != null && !group.isEmpty()) {
            return "views/item.xhtml?group=" + group;
        }

        if (block != null && !block.isEmpty()) {
            return "views/item.xhtml?block=" + block;
        }

        return "views/about.xhtml";
    }

}
