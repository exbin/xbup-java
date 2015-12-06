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
package org.xbup.lib.framework.gui;

import org.xbup.lib.framework.gui.api.XBApplicationModule;
import org.xbup.lib.framework.gui.api.ApplicationModulePlugin;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;
import org.xbup.lib.core.serial.sequence.XBStringListConsistSerializable;
import org.xbup.lib.core.type.XBString;

/**
 * Record about single module.
 *
 * @version 0.2.0 2015/02/03
 * @author XBUP Project (http://xbup.org)
 */
public class XBBasicApplicationModule implements XBPSequenceSerializable, XBApplicationModule {

    static long[] XBUP_BLOCKREV_CATALOGPATH = {1, 3, 1, 2, 0, 0};

    private final String moduleId;
    private final ApplicationModulePlugin plugin;
    private String name;
    private String description;
    private final List<String> optionalModuleIds = new ArrayList<>();
    private final List<String> dependencyModuleIds = new ArrayList<>();

    public XBBasicApplicationModule(String moduleId, ApplicationModulePlugin plugin) {
        this.moduleId = moduleId;
        this.plugin = plugin;
    }

    @Override
    public String getModuleId() {
        return moduleId;
    }

    @Override
    public ApplicationModulePlugin getPlugin() {
        return plugin;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<String> getOptionalModuleIds() {
        return optionalModuleIds;
    }

    public void addOptionalModuleIds(List<String> optionalModuleIds) {
        optionalModuleIds.addAll(this.optionalModuleIds);
    }

    @Override
    public List<String> getDependencyModuleIds() {
        return dependencyModuleIds;
    }

    public void addDependencyModuleIds(List<String> dependencyModuleIds) {
        dependencyModuleIds.addAll(this.dependencyModuleIds);
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        XBStringListConsistSerializable dependencies = new XBStringListConsistSerializable(dependencyModuleIds);
        XBStringListConsistSerializable optionals = new XBStringListConsistSerializable(optionalModuleIds);
        if (serial.getSerializationMode() == XBSerializationMode.PULL) {
            XBString nameString = new XBString();
            serial.consist(nameString);
            name = nameString.getValue();
            XBString descriptionString = new XBString();
            serial.consist(descriptionString);
            description = descriptionString.getValue();
        } else {
            serial.consist(new XBString(name));
            serial.consist(new XBString(description));
        }

        serial.listConsist(dependencies);
        serial.listConsist(optionals);
        serial.end();
    }
}
