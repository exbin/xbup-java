/*
 * SimplyHTML, a word processor based on Java, HTML and CSS
 * Copyright (C) 2002 Ulrich Hilger
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.lightdev.app.shtm;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.MissingResourceException;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * Provides methods to dynamically combine components and resource bundles.
 *
 * <p>The actions, menus and menuitems created by this object are
 * stored privately inside this object. This allows for later
 * access to one of the stored items through the action command name.</p>
 *
 * <p><b>IMPORTANT:</b> Action command names must be unique, if actions
 * or menus are added to an instance of this class and if the actions or
 * menus are defined in different TextResourcess, the action names
 * must be unique over all TextResourcess involved, because the action
 * names are used as identifiers for connection of actions to compnents
 * such as menus and menu items.</p>
 *
 * <p>Component creation methods such as createMenu or createMenuItem
 * expect definitions coming from a TextResources, typically a
 * text file ending with '.properties'.</p>
 *
 * <p>
 * Inside the .properties file, a menu definition is looking similar to
 * <pre>
 *   # plugin menu definition
 *   plugin=test1 test2 test3
 *   pluginLabel=Test Plug-In
 *
 *   # plugin menu items
 *   test1Label=Test 1
 *   test1Image=images/test1.gif
 *   test1Tip=test menu item 1
 *   test2Label=Test 2
 *   test3Label=Test 3
 * </pre>
 * </p>
 *
 * <p>
 * The calling class has to define actions named accordingly, e.g.
 * <pre>
 *    DynamicResource dynRes = new DynamicResource("com.foo.bar.myPlugin");
 *    dynRes.addAction("test1", new MyAction("test1");
 *    dynRes.addAction("test2", new MyAction("test2");
 *    dynRes.addAction("test3", new MyAction("test3");
 * </pre>
 * </p>
 *
 * @author Ulrich Hilger
 * @author Light Development
 * @author <a href="http://www.lightdev.com">http://www.lightdev.com</a>
 * @author <a href="mailto:info@lightdev.com">info@lightdev.com</a>
 * @author published under the terms and conditions of the
 *      GNU General Public License,
 *      for details see file gpl.txt in the distribution
 *      package of this software
 *
 * 
 */
class DynamicResource {
    /** name constant for labels in the resource file */
    public static final String labelSuffix = "Label";
    /** name constant for action commands in the resource file */
    private static final String actionSuffix = "Action";
    /** name constant for indicating image resources in the resource file */
    public static final String imageSuffix = "Image";
    /** name constant for tool tip strings in the resource file */
    public static final String toolTipSuffix = "Tip";
    /** name constant for selected icon names in the resource file */
    public static final String selectedIconSuffix = "SelectedIcon";
    /** indicator for menu separators */
    public static final String menuSeparatorKey = "-";
    /** dynamic storage for menu items */
    private final Hashtable menuItems = new Hashtable();
    /** dynamic storage for actions */
    private final Hashtable commands = new Hashtable();
    /** dynamic storage for menus */
    private final Hashtable menus = new Hashtable();
    public static final String IMAGE_EMPTY = "empty.gif";

    /**
     * construct a new DynamicResource.
     */
    public DynamicResource() {
    }

    /**
     * add an action to this <code>DynamicResource</code>
     *
     * @param cmd  the internal identifier for the action
     * @param action  the action to associate with actionCommand
     */
    public void addAction(final String cmd, final Action action) {
        commands.put(cmd, action);
    }

    /**
     * Create a menu bar.  This reads the
     * definition of the menu from the associated resource file.
     *
     * @param resources  the TextResources to get the menu definition from
     * @param name  name of the menu bar definition
     *
     * @return the created menu bar
     */
    public SHTMLMenuBar createMenubar(final TextResources resources, final String name) {
        final SHTMLMenuBar mb = new SHTMLMenuBar();
        final String[] menuKeys = Util.tokenize(Util.getResourceString(resources, name), " ");
        for (int i = 0; i < menuKeys.length; i++) {
            final JMenu m = createMenu(resources, menuKeys[i]);
            if (m != null) {
                mb.add(m);
            }
        }
        return mb;
    }

    /**
     * Create a menu for the app.  This reads the
     * definition of the menu from the associated resource file.
     *
     * @param resources  the TextResources to get the menu definition from
     * @param key  the key of the menu definition in the resource file
     * @return the created menu
     */
    public JMenu createMenu(final TextResources resources, final String key) {
        JMenu menu = null;
        String def = Util.getResourceString(resources, key);
        if (def == null) {
            def = "";
        }
        final String[] itemKeys = Util.tokenize(def, " ");
        menu = new JMenu(Util.getResourceString(resources, key + labelSuffix));
        for (int i = 0; i < itemKeys.length; i++) {
            if (itemKeys[i].equals(menuSeparatorKey)) {
                menu.addSeparator();
            }
            else {
                final JMenuItem mi = createMenuItem(resources, itemKeys[i]);
                menu.add(mi);
            }
        }
        menu.addMenuListener(new DynamicMenuListener());
        /**
         * store the menu in the menus hashtable for possible later use
         */
        menus.put(key, menu);
        return menu;
    }

    /**
     * Create a menu for the app.  This reads the
     * definition of the menu from the associated resource file.
     *
     * @param resources  the TextResources to get the menu definition from
     * @param key  the key of the menu definition in the resource file
     * @return the created menu
     */
    public JPopupMenu createPopupMenu(final TextResources resources, final String key) {
        JPopupMenu menu = null;
        String def = Util.getResourceString(resources, key);
        if (def == null) {
            def = "";
        }
        final String[] itemKeys = Util.tokenize(def, " ");
        menu = new JPopupMenu();
        for (int i = 0; i < itemKeys.length; i++) {
            if (itemKeys[i].equals(menuSeparatorKey)) {
                menu.addSeparator();
            }
            else {
                final JMenuItem mi = createMenuItem(resources, itemKeys[i]);
                menu.add(mi);
            }
        }
        return menu;
    }

    public JMenu getMenu(final String cmd) {
        return (JMenu) menus.get(cmd);
    }

    /**
     * create a menu item
     *
     * @param resources  the TextResources to get the item definition from
     * @param cmd the action command to be associated
     *      with the new menu item
     * @return the created menu item
     */
    public JMenuItem createMenuItem(final TextResources resources, final String cmd) {
        /**
         * create a new menu item with the appropriate label from the
         * resource file. This label later is set from the action this
         * menu item is associated to (see below).
         */
        JMenuItem mi;
        mi = new JMenuItem();
        String astr = Util.getResourceString(resources, cmd + actionSuffix);
        if (astr == null) {
            astr = cmd;
        }
        mi.setActionCommand(astr);
        /**
         * connect action and menu item with appropriate listeners
         */
        final Action a = getAction(astr);
        if (a != null) {
            final Object aKey = a.getValue(AbstractAction.ACCELERATOR_KEY);
            if (aKey != null) {
                mi.setAccelerator((KeyStroke) aKey);
            }
            /**
             * add Action 'a' as the listener to action events
             * fired from this menu, i.e. execute action 'a' with
             * menu item 'mi'
             */
            mi.addActionListener(a);
            /**
             * cause an instance of inner class ActionChangeListener
             * to listen to property changes of Action 'a' and to apply
             * changed properties of Action 'a' to menu item 'mi'
             */
            a.addPropertyChangeListener(createActionChangeListener(mi));
            /**
             * if the action has an image,
             * associate it with the menu item
             */
            final Icon icon = (Icon) a.getValue(Action.SMALL_ICON);
            if (icon != null) {
                mi.setHorizontalTextPosition(JButton.RIGHT);
                mi.setIcon(icon);
            }
            String name = (String) a.getValue(Action.NAME);
            if(name == null)
            	name = Util.getResourceString(resources, cmd + labelSuffix);
			mi.setText(name);
            /**
             * initially set the enabled state of the menu item
             * according to its action's enabled state
             */
            mi.setEnabled(a.isEnabled());
        }
        else {
        	mi.setText(Util.getResourceString(resources, cmd + labelSuffix));
            mi.setEnabled(false);
        }
        /**
         * store the menu item in the menuItems hashtable for possible later use
         */
        menuItems.put(cmd, mi);
        return mi;
    }

    /**
     * get a string from the resources file
     *
     * @param resources  the TextResources to get the string from
     * @param nm  the key of the string
     * @return the string for the given key or null if not found
     */
    static public String getResourceString(final TextResources resources, final String key) {
        try {
            //System.out.println("getResourceString nm=" + nm);
            if (resources != null) {
                return resources.getString(key);
            }
            System.err.println("SimplyHTML : Warning : resources are null.");
            new Throwable("Dummy").printStackTrace();
            return key;
        }
        catch (final MissingResourceException mre) {
            System.err.println("SimplyHTML : Warning : resource is missing: " + key);
            return key;
        }
    }

    /**
     * listen to menu select events for proper updating of menu items
     *
     * whenever a menu is selected, its menu items are iterated and the
     * update method of the item's action is called causing
     * the menu item to reflect the correct enabled state.
     *
     * As each menu item is connected with a PropertyChangeListener
     * listening to property changes on it'Saction, the menu item is
     * updated by the PropertyChangeListener whenever the enabledState
     * of the action changes.
     */
    private class DynamicMenuListener implements MenuListener {
        public DynamicMenuListener() {
        }

        public void menuSelected(final MenuEvent e) {
            final Component[] items = ((JMenu) e.getSource()).getMenuComponents();
            Action action;
            for (int i = 0; i < items.length; i++) {
                if (items[i] instanceof JPopupMenu.Separator) {
                }
                else if (items[i] instanceof JMenuItem) {
                    action = getAction(((JMenuItem) items[i]).getActionCommand());
                    if (action instanceof SHTMLAction) {
                        ((SHTMLAction) action).update();
                    }
                }
            }
        }

        public void menuDeselected(final MenuEvent e) {
        }

        public void menuCanceled(final MenuEvent e) {
        }
    }

    /**
     * get an action from the commands table
     *
     * @param cmd  the name of the action the get
     * @return the action found for the given name
     */
    public Action getAction(final String cmd) {
        return (Action) commands.get(cmd);
    }

    /**
     * get all actions registered in this <code>DynamicResource</code>
     *
     * @return all actions
     */
    public Enumeration getActions() {
        return commands.elements();
    }

    /** create our PropertyChangeListener implementation */
    private PropertyChangeListener createActionChangeListener(final AbstractButton b) {
        return new ActionChangedListener(b);
    }

    /**
     * associate a menu item to an action.
     *
     * When registering this
     * action listener with an action, it gets informed by
     * property changes of that particular action.
     *
     * By passing a menu item to the constructor of ActionChangedListener,
     * an instance of ActionChangedListener 'remembers' the menu item
     * its property are associated to.
     */
    private class ActionChangedListener implements PropertyChangeListener {
        AbstractButton menuItem;

        ActionChangedListener(final AbstractButton mi) {
            super();
            menuItem = mi;
        }

        public void propertyChange(final PropertyChangeEvent e) {
            final String propertyName = e.getPropertyName();
            if (e.getPropertyName().equals(Action.NAME)) {
                final String text = (String) e.getNewValue();
                menuItem.setText(text);
            }
            else if (propertyName.equals("enabled")) {
                final Boolean enabledState = (Boolean) e.getNewValue();
                menuItem.setEnabled(enabledState.booleanValue());
            }
        }
    }

    /**
     * get the menu item that was created for the given
     * command.
     *
     * @param cmd  name of the action.
     * @return item created for the given command or null
     *  if one wasn't created.
     */
    public JMenuItem getMenuItem(final String cmd) {
        return (JMenuItem) menuItems.get(cmd);
    }

    /**
     * get the icon for a given command.
     *
     * <p>If the resource bundle has a reference to an icon for the
     * given commamd, an icon is created for the respective image resource.
     * otherwise, null is returned.</p>
     *
     * @param resources  the TextResources to get the icon from
     * @param cmd  the command an icon is requested for
     *
     * @return the icon for that command or null, if none is present
     *        for this command
     */
    static public Icon getIconForCommand(final TextResources resources, final String cmd) {
        return DynamicResource.getIconForName(resources, cmd + imageSuffix);
    }

    static public Icon getIconForName(final TextResources resources, final String name) {
        Icon icon = null;
        final URL url = DynamicResource.getResource(resources, name);
        //System.out.println("getIconForName name=" + name + ", url=" + url);
        if (url != null) {
            icon = new ImageIcon(url);
        }
        return icon;
    }

    /**
     * get the location of a resource.
     *
     * <p>Resources such as images are delivered with the application in
     * the path containing the application's classes. The resources file
     * coming with SimplyHTML has a key for every resource pointing to
     * the subdirectory relative to the class path.</p>
     *
     * @param resources  the TextResources to get the resource from
     * @param key  the key of the resource in the resource file
     * @return the resource location as a URL
     */
    static public URL getResource(final TextResources resources, final String key) {
        final String name = Util.getResourceString(resources, key);
        if (name != null/* && !name.endsWith(IMAGE_EMPTY)*/) {
            final URL url = DynamicResource.class.getResource(name);
            return url;
        }
        return null;
    }

    /**
     * Create a tool bar.  This reads the definition of a tool bar
     * from the associated resource file.
     *
     * @param resources  the TextResources to get the tool bar definition from
     * @param nm  the name of the tool bar definition in the resource file
     *
     * @return the created tool bar
     */
    public JToolBar createToolBar(final TextResources resources, final String nm) {
        Action action;
        AbstractButton newButton;
        final java.awt.Dimension buttonSize = new java.awt.Dimension(24, 24);
        new java.awt.Dimension(3, 20);
        JSeparator separator;
        final String[] itemKeys = Util.tokenize(Util.getResourceString(resources, nm), " ");
        final JToolBar toolBar = new JToolBar();
        toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        for (int i = 0; i < itemKeys.length; i++) {
            /** special handling for separators */
            if (itemKeys[i].equals(menuSeparatorKey)) {
                separator = new JSeparator(JSeparator.VERTICAL);
                toolBar.add(separator);
            }
            else {
                action = getAction(itemKeys[i]);
                newButton = toolBar.add(action);
                newButton.setMinimumSize(buttonSize);
                newButton.setPreferredSize(buttonSize);
                newButton.setMaximumSize(buttonSize);
                newButton.setFocusPainted(false);
            }
        }
        return toolBar;
    }
}
