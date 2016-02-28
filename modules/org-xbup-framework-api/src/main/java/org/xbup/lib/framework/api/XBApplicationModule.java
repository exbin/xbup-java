/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xbup.lib.framework.api;

import java.util.List;

/**
 * Interface for record about single module.
 *
 * @version 0.2.0 2015/12/06
 * @author ExBin Project (http://exbin.org)
 */
public interface XBApplicationModule {

    /**
     * Returns list of required dependency modules.
     *
     * @return list of dependecy module identifiers
     */
    List<String> getDependencyModuleIds();

    /**
     * Returns module description.
     *
     * @return description text
     */
    String getDescription();

    String getModuleId();

    String getName();

    /**
     * Returns list of optional modules.
     *
     * @return list of module identifiers
     */
    List<String> getOptionalModuleIds();

    XBApplicationModulePlugin getPlugin();
}
