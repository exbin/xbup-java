/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exbin.framework.api;

import org.exbin.xbup.plugin.XBModuleRecord;

/**
 * Interface for record about single application module.
 *
 * @version 0.2.0 2016/03/27
 * @author ExBin Project (http://exbin.org)
 */
public interface XBApplicationModuleInfo extends XBModuleRecord {

    XBApplicationModule getPlugin();
}
