/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exbin.xbup.plugin;

import java.util.List;

/**
 * Interface for record about single module.
 *
 * @version 0.2.0 2015/12/06
 * @author ExBin Project (http://exbin.org)
 */
public interface XBModuleRecord {

    /**
     * Returns module ID.
     *
     * @return module ID
     */
    String getModuleId();

    /**
     * Returns module name.
     *
     * @return module name
     */
    String getName();

    /**
     * Returns module description.
     *
     * @return description text
     */
    String getDescription();

    /**
     * Returns list of required dependency modules.
     *
     * @return list of dependecy module identifiers
     */
    List<String> getDependencyModuleIds();

    /**
     * Returns list of optional modules.
     *
     * @return list of module identifiers
     */
    List<String> getOptionalModuleIds();

    /**
     * Returns instance of the module.
     *
     * @return module instance
     */
    XBModule getModule();
}
