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
import org.exbin.framework.gui.component.api.MoveItemActions;
import org.exbin.framework.gui.component.api.MoveItemActionsHandler;
import org.exbin.framework.gui.utils.ActionUtils;

/**
 * Item movement default action set.
 *
 * @version 0.2.0 2016/03/21
 * @author ExBin Project (http://exbin.org)
 */
public class DefaultMoveItemActions implements MoveItemActions {

    private final ResourceBundle resourceBundle = ActionUtils.getResourceBundleByClass(GuiComponentModule.class);
    private final int metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    private MoveItemActionsHandler actionsHandler = null;
    private Action moveUpAction = null;
    private Action moveDownAction = null;
    private Action moveTopAction = null;
    private Action moveBottomAction = null;

    public DefaultMoveItemActions() {
    }

    @Override
    public void setMoveActionsHandler(MoveItemActionsHandler actionsHandler) {
        this.actionsHandler = actionsHandler;
    }

    @Override
    public Action getMoveUpAction() {
        if (moveUpAction == null) {
            moveUpAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    actionsHandler.performMoveUp();
                }
            };
            ActionUtils.setupAction(moveUpAction, resourceBundle, "moveItemUpAction");
            moveUpAction.setEnabled(false);
        }
        return moveUpAction;
    }

    @Override
    public Action getMoveDownAction() {
        if (moveDownAction == null) {
            moveDownAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    actionsHandler.performMoveDown();
                }
            };
            ActionUtils.setupAction(moveDownAction, resourceBundle, "moveItemDownAction");
            moveDownAction.setEnabled(false);
        }
        return moveDownAction;
    }

    @Override
    public Action getMoveTopAction() {
        if (moveTopAction == null) {
            moveTopAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    actionsHandler.performMoveTop();
                }
            };
            ActionUtils.setupAction(moveTopAction, resourceBundle, "moveItemTopAction");
            moveTopAction.setEnabled(false);
        }
        return moveTopAction;
    }

    @Override
    public Action getMoveBottomAction() {
        if (moveBottomAction == null) {
            moveBottomAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    actionsHandler.performMoveBottom();
                }
            };
            ActionUtils.setupAction(moveBottomAction, resourceBundle, "moveItemBottomAction");
            moveBottomAction.setEnabled(false);
        }
        return moveBottomAction;
    }

    @Override
    public void updateMoveItemActions() {
        boolean enabled = actionsHandler.isEditable() && actionsHandler.isSelection();
        if (moveUpAction != null) {
            moveUpAction.setEnabled(enabled);
        }
        if (moveDownAction != null) {
            moveDownAction.setEnabled(enabled);
        }
        if (moveTopAction != null) {
            moveTopAction.setEnabled(enabled);
        }
        if (moveBottomAction != null) {
            moveBottomAction.setEnabled(enabled);
        }
    }
}
