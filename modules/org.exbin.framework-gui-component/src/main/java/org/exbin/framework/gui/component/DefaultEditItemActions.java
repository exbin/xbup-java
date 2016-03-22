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
package org.exbin.framework.gui.component;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.exbin.framework.gui.component.api.EditItemActions;
import org.exbin.framework.gui.component.api.EditItemActionsHandler;
import org.exbin.framework.gui.utils.ActionUtils;

/**
 * Item editation default action set.
 *
 * @version 0.2.0 2016/03/22
 * @author ExBin Project (http://exbin.org)
 */
public class DefaultEditItemActions implements EditItemActions {

    private final ResourceBundle resourceBundle = ActionUtils.getResourceBundleByClass(GuiComponentModule.class);
    private final int metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    private EditItemActionsHandler actionsHandler = null;
    private Action addItemAction = null;
    private Action editItemAction = null;
    private Action deleteItemAction = null;

    public DefaultEditItemActions() {
    }

    @Override
    public void setEditItemActionsHandler(EditItemActionsHandler actionsHandler) {
        this.actionsHandler = actionsHandler;
    }

    @Override
    public Action getAddItemAction() {
        if (addItemAction == null) {
            addItemAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    actionsHandler.performAddItem();
                }
            };
            ActionUtils.setupAction(addItemAction, resourceBundle, "addItemAction");
            addItemAction.setEnabled(false);
        }
        return addItemAction;
    }

    @Override
    public Action getEditItemAction() {
        if (editItemAction == null) {
            editItemAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    actionsHandler.performEditItem();
                }
            };
            ActionUtils.setupAction(editItemAction, resourceBundle, "editItemAction");
            editItemAction.setEnabled(false);
        }
        return editItemAction;
    }

    @Override
    public Action getDeleteItemAction() {
        if (deleteItemAction == null) {
            deleteItemAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    actionsHandler.performDeleteItem();
                }
            };
            ActionUtils.setupAction(deleteItemAction, resourceBundle, "deleteItemAction");
            deleteItemAction.setEnabled(false);
        }
        return deleteItemAction;
    }

    @Override
    public void updateEditItemActions() {
        if (addItemAction != null) {
            addItemAction.setEnabled(actionsHandler.isEditable());
        }
        if (editItemAction != null) {
            editItemAction.setEnabled(actionsHandler.isSelection() && actionsHandler.isEditable());
        }
        if (deleteItemAction != null) {
            deleteItemAction.setEnabled(actionsHandler.isSelection() && actionsHandler.isEditable());
        }
    }
}
