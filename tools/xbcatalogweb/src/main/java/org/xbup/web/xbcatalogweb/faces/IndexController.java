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
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.xbup.lib.catalog.entity.service.XBEXUserService;
import org.xbup.web.xbcatalogweb.service.XBEUserRecordService;

/**
 * Main Website Index controller.
 *
 * @version 0.1.24 2014/11/19
 * @author XBUP Project (http://xbup.org)
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

    public String getRedirectTarget(String spec) {
        if (spec == null || spec.isEmpty()) {
            return "views/about.xhtml";
        }

        return "views/browse.xhtml";
    }

}
