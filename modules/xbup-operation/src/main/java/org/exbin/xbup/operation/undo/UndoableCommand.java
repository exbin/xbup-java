/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.operation.undo;

import org.exbin.xbup.operation.command.Command;

/**
 * Undoable command.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface UndoableCommand extends Command {

    /**
     * Performs redo on given document.
     */
    void redo();

    /**
     * Performs undo operation on given document.
     */
    void undo();
}
