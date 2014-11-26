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
package org.xbup.lib.client.catalog.remote.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.catalog.base.manager.XBCXLangManager;
import org.xbup.lib.client.XBCatalogServiceMessage;
import org.xbup.lib.client.catalog.remote.XBRXLanguage;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.stream.XBStreamChecker;

/**
 * Manager class for XBRXLanguage catalog items.
 *
 * @version 0.1.21 2011/12/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXLangManager extends XBRDefaultManager<XBRXLanguage> implements XBCXLangManager<XBRXLanguage> {

    public XBRXLangManager(XBRCatalog catalog) {
        super(catalog);
    }

    @Override
    public XBRXLanguage getDefaultLang() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.DEFAULT_LANG_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long langId = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRXLanguage(client, langId);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXLangManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXLangManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<XBCXLanguage> getLangs() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.LANGS_LANG_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            List<XBCXLanguage> result = new ArrayList<XBCXLanguage>();
            long count = checker.attribXB().getLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRXLanguage(client,checker.attribXB().getLong()));
            }
            checker.endXB();
            message.close();
            return result;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXLangManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXLangManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getAllLangsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.LANGSCOUNT_LANG_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long count = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return count;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXLangManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXLangManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
         return "Language Extension";
    }
}
