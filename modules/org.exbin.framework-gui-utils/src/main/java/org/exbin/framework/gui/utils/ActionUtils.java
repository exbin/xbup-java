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
package org.exbin.framework.gui.utils;

import java.util.ResourceBundle;
import javax.swing.Action;

/**
 * Some simple static methods usable for actions, menus and toolbars.
 *
 * @version 0.2.0 2016/01/10
 * @author ExBin Project (http://exbin.org)
 */
public class ActionUtils {

    /**
     * Action type like or check, radio.
     *
     * Value is ActionType.
     */
    public static final String ACTION_TYPE = "type";
    /**
     * Radio group name value.
     *
     * Value is String.
     */
    public static final String ACTION_RADIO_GROUP = "radioGroup";
    /**
     * Action mode for actions opening dialogs.
     *
     * Value is Boolean.
     */
    public static final String ACTION_DIALOG_MODE = "dialogMode";

    /**
     * Sets action values according to values specified by resource bundle.
     *
     * @param action modified action
     * @param bundle source bundle
     * @param actionName bundle key prefix
     */
    public static void setupAction(Action action, ResourceBundle bundle, String actionName) {
        action.putValue(Action.NAME, bundle.getString(actionName + ".text"));

        // TODO keystroke from string with meta mask translation
        if (bundle.containsKey(actionName + ".shortDescription")) {
            action.putValue(Action.SHORT_DESCRIPTION, bundle.getString(actionName + ".shortDescription"));
        }
        if (bundle.containsKey(actionName + ".smallIcon")) {
            action.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(action.getClass().getResource(bundle.getString(actionName + ".smallIcon"))));
        }
        if (bundle.containsKey(actionName + ".largeIcon")) {
            action.putValue(Action.LARGE_ICON_KEY, new javax.swing.ImageIcon(action.getClass().getResource(bundle.getString(actionName + ".largeIcon"))));
        }
    }

    /**
     * Returns resource bundle for properties file with path derived from class
     * name.
     *
     * @param targetClass target class
     * @return resource bundle
     */
    public static ResourceBundle getResourceBundleByClass(Class targetClass) {
        return ResourceBundle.getBundle(getResourceBaseNameBundleByClass(targetClass));
    }

    /**
     * Returns resource bundle base name for properties file with path derived
     * from class name.
     *
     * @param targetClass target class
     * @return base name string
     */
    public static String getResourceBaseNameBundleByClass(Class targetClass) {
        String classNamePath = getClassNamePath(targetClass);
        int classNamePos = classNamePath.lastIndexOf("/");
        return classNamePath.substring(0, classNamePos + 1) + "resources" + classNamePath.substring(classNamePos);
    }

    /**
     * Returns class name path.
     *
     * Result is canonical name with dots replaced with slashes.
     *
     * @param targetClass target class
     * @return name path
     */
    public static String getClassNamePath(Class targetClass) {
        return targetClass.getCanonicalName().replace(".", "/");

    }

    public enum ActionType {
        PUSH,
        CHECK,
        RADIO
    }
}
