/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.plugin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBEndToken;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.event.XBEventListener;
import org.exbin.xbup.core.parser.token.event.XBEventProducer;
import org.exbin.xbup.core.parser.token.event.XBEventWriter;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.param.XBSerializationMode;
import org.exbin.xbup.core.serial.sequence.XBStringListConsistSerializable;
import org.exbin.xbup.core.type.XBString;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * Record about single module.
 *
 * @version 0.2.1 2020/09/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBModuleInfo implements XBPSequenceSerializable, XBModuleRecord {

    static long[] XBUP_BLOCKREV_CATALOGPATH = {1, 3, 1, 2, 0, 0};

    private String moduleId;
    private XBModule module;
    private ClassLoader classLoader;
    private String name;
    private String description;
    private final List<String> optionalModuleIds = new ArrayList<>();
    private final List<String> dependencyModuleIds = new ArrayList<>();

    public XBModuleInfo() {
    }

    private XBModuleInfo(String moduleId) {
        this.moduleId = moduleId;
    }

    public XBModuleInfo(String moduleId, XBModule module, ClassLoader classLoader) {
        this.moduleId = moduleId;
        this.module = module;
        this.classLoader = classLoader;
    }

    // Temporary method to create module file.
    public static void main(String[] params) {
        XBModuleInfo module = new XBModuleInfo("org.exbin.framework.gui.help.GuiHelpModule");
        module.setName("XBUP Help Support Module");
        module.setDescription("Module supporting basic help support");
        String[] depedency = new String[]{}; // "org.exbin.framework.gui.file.GuiFileModule", "org.exbin.framework.gui.menu.GuiMenuModule" };

        module.addDependencyModuleIds(Arrays.asList(depedency));
        try {
            XBEventWriter eventWriter = new XBEventWriter(new FileOutputStream("module.xb"));
            XBPListenerSerialHandler serial = new XBPListenerSerialHandler(new XBTToXBEventTypeRemover(eventWriter));
            serial.process(module);
        } catch (IOException ex) {
            Logger.getLogger(XBModuleInfo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Nullable
    @Override
    public String getModuleId() {
        return moduleId;
    }

    @Nonnull
    @Override
    public XBModule getModule() {
        if (module == null) {
            throw new IllegalStateException("Attempt to use uninitialized module");
        }
        return module;
    }

    public void setModule(XBModule module) {
        this.module = module;
    }

    @Nonnull
    public ClassLoader getClassLoader() {
        if (classLoader == null) {
            throw new IllegalStateException("Attempt to use uninitialized module");
        }
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Nonnull
    @Override
    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nonnull
    @Override
    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Nonnull
    @Override
    public List<String> getOptionalModuleIds() {
        return optionalModuleIds;
    }

    public void addOptionalModuleIds(List<String> optionalModuleIds) {
        this.optionalModuleIds.addAll(optionalModuleIds);
    }

    @Nonnull
    @Override
    public List<String> getDependencyModuleIds() {
        return dependencyModuleIds;
    }

    public void addDependencyModuleIds(List<String> dependencyModuleIds) {
        this.dependencyModuleIds.addAll(dependencyModuleIds);
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType();
        XBStringListConsistSerializable dependencies = new XBStringListConsistSerializable(dependencyModuleIds);
        XBStringListConsistSerializable optionals = new XBStringListConsistSerializable(optionalModuleIds);
        if (serial.getSerializationMode() == XBSerializationMode.PULL) {
            if (serial.pullIfEmptyBlock()) {
                moduleId = null;
            } else {
                XBString moduleIdString = new XBString();
                serial.consist(moduleIdString);
                moduleId = moduleIdString.getValue();
            }
            if (serial.pullIfEmptyBlock()) {
                name = null;
            } else {
                XBString nameString = new XBString();
                serial.consist(nameString);
                name = nameString.getValue();
            }

            if (serial.pullIfEmptyBlock()) {
                description = null;
            } else {
                XBString descriptionString = new XBString();
                serial.consist(descriptionString);
                description = descriptionString.getValue();
            }
        } else {
            serial.consist(new XBString(moduleId));
            serial.consist(new XBString(name));
            serial.consist(new XBString(description));
        }

        serial.listConsist(dependencies);
        serial.listConsist(optionals);
        serial.end();
    }

    @ParametersAreNonnullByDefault
    private static class XBTToXBEventTypeRemover implements XBTEventListener, XBEventProducer {

        private XBEventListener eventListener;
        private boolean blockIdSent = false;

        public XBTToXBEventTypeRemover(XBEventListener eventListener) {
            this.eventListener = eventListener;
        }

        @Override
        public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
            switch (token.getTokenType()) {
                case BEGIN: {
                    eventListener.putXBToken(XBBeginToken.create(((XBTBeginToken) token).getTerminationMode()));
                    blockIdSent = false;
                    break;
                }
                case TYPE: {
                    eventListener.putXBToken(XBAttributeToken.create(new UBNat32()));
                    break;
                }
                case ATTRIBUTE: {
                    if (!blockIdSent) {
                        eventListener.putXBToken(XBAttributeToken.create(new UBNat32()));
                        blockIdSent = true;
                    }
                    eventListener.putXBToken(XBAttributeToken.create(((XBTAttributeToken) token).getAttribute()));
                    break;
                }
                case DATA: {
                    eventListener.putXBToken(XBDataToken.create(((XBTDataToken) token).getData()));
                    break;
                }
                case END: {
                    eventListener.putXBToken(XBEndToken.create());
                    break;
                }
            }
        }

        @Override
        public void attachXBEventListener(XBEventListener eventListener) {
            this.eventListener = eventListener;
        }
    }
}
