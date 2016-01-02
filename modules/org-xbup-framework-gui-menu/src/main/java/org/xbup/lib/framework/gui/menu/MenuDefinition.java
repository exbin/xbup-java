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
package org.xbup.lib.framework.gui.menu;

import org.xbup.lib.framework.gui.menu.api.MenuContribution;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of XBUP framework menu module.
 *
 * @version 0.2.0 2015/12/16
 * @author XBUP Project (http://xbup.org)
 */
public class MenuDefinition {

    private List<MenuContribution> contributions = new ArrayList<>();

    /**
     * @return the contributions
     */
    public List<MenuContribution> getContributions() {
        return contributions;
    }

    /**
     * @param contributions the contributions to set
     */
    public void setContributions(List<MenuContribution> contributions) {
        this.contributions = contributions;
    }
}
