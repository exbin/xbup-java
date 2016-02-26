/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xbup.lib.framework.api;

/**
 * XBUP framework modules repository utilities class.
 *
 * @version 0.2.0 2015/12/25
 * @author XBUP Project (http://xbup.org)
 */
public class XBModuleRepositoryUtils {

    private static final String API_CLASS_EXT = "Api";
    private static final String API_PACKAGE_EXT = ".api";

    /**
     * Returns module identifier for class representation.
     *
     * @param moduleClass module class
     * @return string ID
     */
    public static String getModuleIdByApi(Class moduleClass) {
        String packageName = moduleClass.getPackage().getName();
        String className = moduleClass.getSimpleName();

        return (packageName.endsWith(API_PACKAGE_EXT) ? packageName.substring(0, packageName.length() - 3) : packageName + ".")
                + (className.endsWith(API_CLASS_EXT) ? className.substring(0, className.length() - 3) : className);
    }
}
