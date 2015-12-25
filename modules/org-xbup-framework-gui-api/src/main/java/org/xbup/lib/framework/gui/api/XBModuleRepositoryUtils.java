/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xbup.lib.framework.gui.api;

/**
 * XBUP framework modules repository utilities class.
 *
 * @version 0.2.0 2015/12/25
 * @author XBUP Project (http://xbup.org)
 */
public class XBModuleRepositoryUtils {

    /**
     * Returns module identifier for class representation.
     *
     * @param moduleClass module class
     * @return string ID
     */
    public static String getModuleIdByApi(Class moduleClass) {
        String packageName = moduleClass.getPackage().getName();
        String className = moduleClass.getSimpleName();

        return (packageName.endsWith(".api") ? packageName.substring(0, packageName.length() - 3) : packageName)
                + (className.endsWith("Api") ? className.substring(0, className.length() - 3) : className);
    }
}
