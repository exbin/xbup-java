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
package org.xbup.lib.xbprog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import org.xbup.lib.catalog.XBAECatalog;
import org.xbup.lib.catalog.entity.manager.XBEXDescManager;
import org.xbup.lib.catalog.entity.manager.XBEXLangManager;
import org.xbup.lib.catalog.entity.manager.XBEXNameManager;
import org.xbup.lib.catalog.update.XBCUpdatePHPHandler;
import org.xbup.lib.core.catalog.base.manager.XBCXDescManager;
import org.xbup.lib.core.catalog.base.manager.XBCXLangManager;
import org.xbup.lib.core.catalog.base.manager.XBCXNameManager;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.token.XBPullProviderSerialHandler;
import org.xbup.lib.core.stream.file.XBFileInputStream;
import org.xbup.lib.core.stream.file.XBFileOutputStream;
import org.xbup.lib.xbprog.grammar.XBBNFGrammar;
import org.xbup.lib.xbprog.grammar.XBBNFGrammarRule;
import org.xbup.lib.xbprog.grammar.XBRegularGrammar;
import org.xbup.lib.xbprog.grammar.XBRegularGrammarRule;

/**
 * Testing application for Grammars.
 *
 * @version 0.1.19 2010/02/12
 * @author ExBin Project (http://exbin.org)
 */
public class GrammarTest {

    public static void main(String[] args) {
        test2();
    }

    public static void test1() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("XBMathLibPU");
        EntityManager em = emf.createEntityManager();
        em.setFlushMode(FlushModeType.AUTO);
        XBAECatalog catalog = new XBAECatalog(em);
        catalog.initCatalog();
        catalog.addCatalogManager(XBCXLangManager.class, new XBEXLangManager(catalog));
        catalog.addCatalogManager(XBCXNameManager.class, new XBEXNameManager(catalog));
        catalog.addCatalogManager(XBCXDescManager.class, new XBEXDescManager(catalog));
        XBCUpdatePHPHandler wsHandler = new XBCUpdatePHPHandler(catalog);
        wsHandler.init();
        wsHandler.getPort().getLanguageId("en");
        catalog.setUpdateHandler(wsHandler);

        //
        XBRegularGrammar regularGrammar = new XBRegularGrammar();
        XBRegularGrammarRule rule = new XBRegularGrammarRule();
        regularGrammar.getRules().add(rule);
        rule = new XBRegularGrammarRule();
        rule.setLeftNonterminal(1);
        rule.setRightNonterminal(2);
        rule.setRightTerminal(1);
        regularGrammar.getRules().add(rule);
        XBFileOutputStream fileOutputStream;
//        XBOMOutputStream buffer;
        //XBTreeNode
        try {
//            buffer = new XBOMOutputStream(node);
//            System.out.println("Writing output.xb using level 0");
//            fileOutputStream = new XBFileOutputStream("output.xb");
//            XBEventListenerSerialHandler handler = new XBEventListenerSerialHandler();
//            handler.attachXBEventListener(fileOutputStream);
//            regularGrammar.serializeXB(XBSerializationType.TO_XB, 0, handler);
//            fileOutputStream.close();
//            System.out.println("Writing output_cat.xb using level 1");
//            fileOutputStream = new XBFileOutputStream("output_cat.xb");
//            XBCDeclaration declaration = (XBCDeclaration) regularGrammar.getXBDeclaration();
////            ((XBAECatalog) catalog).
//            Long[] path = declaration.getContextFormat().getCatalogPath().getLongPath();
//            Long[] myPath = new Long[path.length-1];
//            System.arraycopy(path, 0, myPath, 0, path.length-1);
//            catalog.getUpdateHandler().updateFormatSpec(myPath, path[path.length-1]);
//            declaration.setCatalog(catalog);
//            XBTEncapsulator encapsulator = new XBTEncapsulator(new XBContext(catalog, declaration));
//            encapsulator.attachXBTListener(new XBTEventListenerToListener(new XBTToXBEventConvertor(fileOutputStream)));
//
//            XBTEventListenerSerialHandler handler2 = new XBTEventListenerSerialHandler();
//            handler2.attachXBTEventListener(new XBTEventOutputStream(new XBTListenerToEventListener(encapsulator)));
//            regularGrammar.serializeXB(XBSerializationType.TO_XB, 1, handler);
//            fileOutputStream.close();
        } catch (XBProcessingException ex) {
            Logger.getLogger(GrammarTest.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(GrammarTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        regularGrammar = new XBRegularGrammar();
        XBFileInputStream fileInputStream;
//        XBOMOutputStream buffer;
        //XBTreeNode
        try {
//            buffer = new XBOMOutputStream(node);
            fileInputStream = new XBFileInputStream("output.xb");
            XBPullProviderSerialHandler handler = new XBPullProviderSerialHandler();
            handler.attachXBPullProvider(fileInputStream);
//            regularGrammar.serializeXB(XBSerializationType.FROM_XB, 0, handler);
            // new XBTEncapsulator(regularGrammar.getXBDeclaration(), XBTDefaultEventListener.toXBListener(stream))
            fileInputStream.close();
        } catch (XBProcessingException ex) {
            Logger.getLogger(GrammarTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GrammarTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void test2() {

        XBBNFGrammar grammar = new XBBNFGrammar();
        XBBNFGrammarRule startRule = new XBBNFGrammarRule();
        startRule.setRuleName("start");

        XBBNFGrammarRule nSampleRule = new XBBNFGrammarRule();
        nSampleRule.setRuleName("nSample");
        List<String> subRules = new ArrayList<String>();
        subRules.add(nSampleRule.getRuleName());
        nSampleRule.setRules(subRules);

        XBBNFGrammarRule terminalSampleRule = new XBBNFGrammarRule();
        terminalSampleRule.setRuleName("terminalSample");
        terminalSampleRule.setTerminal("terminal");

        subRules = new ArrayList<String>();
        subRules.add(nSampleRule.getRuleName());
        subRules.add(terminalSampleRule.getRuleName());
        startRule.setRules(subRules);
        grammar.addRule(startRule);
        grammar.addRule(nSampleRule);
        grammar.addRule(terminalSampleRule);

        System.out.print(grammar.generateGrammarDefinition());
    }
}
