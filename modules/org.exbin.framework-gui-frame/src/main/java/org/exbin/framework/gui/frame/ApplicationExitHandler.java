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
package org.exbin.framework.gui.frame;

import java.util.ArrayList;
import java.util.List;
import org.exbin.framework.gui.frame.api.ApplicationExitListener;
import org.exbin.framework.gui.frame.api.ApplicationFrameHandler;

/**
 * Application exit handler.
 *
 * @version 0.2.0 2016/01/10
 * @author ExBin Project (http://exbin.org)
 */
public class ApplicationExitHandler {

    private final List<ApplicationExitListener> listeners = new ArrayList<>();

    public ApplicationExitHandler() {
    }

    public void addListener(ApplicationExitListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ApplicationExitListener listener) {
        listeners.remove(listener);
    }

    public void executeExit(ApplicationFrameHandler frameHandler) {
        for (ApplicationExitListener listener : listeners) {
            boolean canContinue = listener.processExit(frameHandler);
            if (!canContinue) {
                return;
            }
        }

        System.exit(0);
    }
}
