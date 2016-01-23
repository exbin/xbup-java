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
package org.xbup.lib.framework.editor.text.panel;

import java.util.ArrayList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;

/**
 * Undo Manager with compound operations.
 *
 * @version 0.2.0 2016/01/23
 * @author XBUP Project (http://xbup.org)
 */
public class TextPanelCompoundUndoManager extends AbstractUndoableEdit implements UndoableEditListener {

    private DocumentEvent.EventType lastEditType = null;
    private final ArrayList<MyCompoundEdit> edits = new ArrayList<>();
    private MyCompoundEdit current;
    private int pointer = -1;
    private int lastOffset = -1;

    TextPanelCompoundUndoManager() {
    }

    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        UndoableEdit edit = e.getEdit();
        if (edit instanceof AbstractDocument.DefaultDocumentEvent) {
            DocumentEvent.EventType editType = ((AbstractDocument.DefaultDocumentEvent) edit).getType();
            AbstractDocument.DefaultDocumentEvent event = (AbstractDocument.DefaultDocumentEvent) edit;
            int offset = event.getOffset();
            boolean isNeedStart = false;
            if (current == null) {
                isNeedStart = true;
            } else if (lastEditType == null || !lastEditType.equals(editType)) {
                isNeedStart = true;
            } else if (lastEditType == DocumentEvent.EventType.INSERT) {
                if (offset != lastOffset + 1) {
                    isNeedStart = true;
                }
            } else if (lastEditType == DocumentEvent.EventType.REMOVE) {
                if (offset != lastOffset - 1) {
                    isNeedStart = true;
                }
            }

            while (pointer < edits.size() - 1) {
                edits.remove(edits.size() - 1);
                isNeedStart = true;
            }
            if (isNeedStart) {
                createCompoundEdit();
            }
            current.addEdit(edit);
            lastEditType = editType;
            lastOffset = offset;
        }
    }

    public void createCompoundEdit() {
        if (current == null) {
            current = new MyCompoundEdit();
        } else if (current.getLength() > 0) {
            current = new MyCompoundEdit();
        }
        edits.add(current);
        pointer++;
    }

    @Override
    public void undo() throws CannotUndoException {
        if (!canUndo()) {
            throw new CannotUndoException();
        }
        MyCompoundEdit u = edits.get(pointer);
        u.undo();
        pointer--;

        lastOffset = -1;
        lastEditType = null;
    }

    @Override
    public void redo() throws CannotUndoException {
        if (!canRedo()) {
            throw new CannotUndoException();
        }
        pointer++;
        MyCompoundEdit u = edits.get(pointer);
        u.redo();

        lastOffset = -1;
        lastEditType = null;
    }

    @Override
    public boolean canUndo() {
        return pointer >= 0;
    }

    @Override
    public boolean canRedo() {
        return edits.size() > 0 && pointer < edits.size() - 1;
    }

    class MyCompoundEdit extends CompoundEdit {
        boolean isUnDone=false;
        public int getLength() {
            return edits.size();
        }

        @Override
        public void undo() throws CannotUndoException {
            super.undo();
            isUnDone=true;
        }
        @Override
        public void redo() throws CannotUndoException {
            super.redo();
            isUnDone=false;
        }
        @Override
        public boolean canUndo() {
            return edits.size()>0 && !isUnDone;
        }

        @Override
        public boolean canRedo() {
            return edits.size()>0 && isUnDone;
        }

    }
}
