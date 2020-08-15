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
package org.exbin.xbup.catalog.entity.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.swing.ImageIcon;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXFile;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.manager.XBCXFileManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog accessory files manager.
 *
 * @version 0.1.25 2015/09/06
 * @author ExBin Project (http://exbin.org)
 */
@Repository
public class XBEXFileManager extends XBEDefaultCatalogManager<XBEXFile> implements XBCXFileManager<XBEXFile>, Serializable {

    public XBEXFileManager() {
        super();
    }

    public XBEXFileManager(XBECatalog catalog) {
        super(catalog);
    }

    public Long getAllFilesCount() {
        try {
            return (Long) catalog.getEntityManager().createQuery("SELECT count(o) FROM XBXFile as o").getSingleResult();
        } catch (NoResultException ex) {
            return 0l;
        } catch (Exception ex) {
            Logger.getLogger(XBEXFileManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public XBEXFile findById(long id) {
        try {
            return (XBEXFile) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXFile as o WHERE o.id = "+ id).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXFileManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Long[] getFileXBPath(XBCXFile file) {
        ArrayList<Long> list = new ArrayList<>();
        XBCNode parent = file.getNode();
        while (parent != null) {
            if (parent.getParent().isPresent()) {
                list.add(0, parent.getXBIndex());
            }
            parent = (XBCNode) parent.getParent().orElse(null);
        }
        list.add(file.getId());
        return (Long[]) list.toArray(new Long[list.size()]);
    }

    @Override
    public void initializeExtension() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getExtensionName() {
        return "File Repository Extension";
    }

    @Override
    public XBEXFile findFile(XBCNode node, String fileName) {
        try {
            return (XBEXFile) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXFile as o WHERE o.node.id = "+ node.getId() + " AND o.filename = '"+fileName+"'").getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXFileManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public ImageIcon getFileAsImageIcon(XBCXFile iconFile) {
        if (iconFile.getContent() == null) {
            return new ImageIcon();
        }
        return new ImageIcon(iconFile.getContent());
        // XBCXStriManager striManager = (XBCXStriManager) catalog.getExtensionManager(XBCXStriManager.class);
        // XBCXStri itemString = striManager.getItemStringId(iconFile.getNode());
        // return new ImageIcon(catalog.getFileRepositoryPath()+striManager.getFullPath(itemString)+"/"+iconFile.getFilename());
    }

    @Override
    public InputStream getFile(XBCXFile file) {
        if (file.getContent() == null) {
            return new ByteArrayInputStream(new byte[0]);
        }
        return new ByteArrayInputStream(file.getContent());
        /*XBCXStriManager striManager = (XBCXStriManager) catalog.getExtensionManager(XBCXStriManager.class);
         XBCXStri itemString = striManager.getItemStringId(file.getNode());
         try {
         return new FileInputStream(new File(catalog.getFileRepositoryPath() + striManager.getFullPath(itemString) + "/" + file.getFilename()));
         } catch (FileNotFoundException ex) {
         Logger.getLogger(XBEXFileManager.class.getName()).log(Level.SEVERE, null, ex);
         return null;
         } */
    }

    public OutputStream setFile(XBCXFile file) {
        return new OutputStreamImpl(file);
        /*XBCXStriManager striManager = (XBCXStriManager) catalog.getExtensionManager(XBCXStriManager.class);
         XBCXStri itemString = striManager.getItemStringId(file.getNode());
         try {
         return new FileOutputStream(new File(catalog.getFileRepositoryPath() + striManager.getFullPath(itemString) + "/" + file.getFilename()));
         } catch (FileNotFoundException ex) {
         Logger.getLogger(XBEXFileManager.class.getName()).log(Level.SEVERE, null, ex);
         return null;
         } */
    }

    @Override
    public List<XBCXFile> findFilesForNode(XBCNode node) {
        try {
            return (List<XBCXFile>) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXFile as o WHERE o.node.id = "+ node.getId()).getResultList();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXFileManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static class OutputStreamImpl extends OutputStream {

        private final XBCXFile file;
        private final ByteArrayOutputStream outputStream;

        public OutputStreamImpl(XBCXFile file) {
            this.file = file;
            outputStream = new ByteArrayOutputStream();
        }

        @Override
        public void write(int b) throws IOException {
            outputStream.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            outputStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            outputStream.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            outputStream.flush();
        }

        @Override
        public void close() throws IOException {
            ((XBEXFile) file).setContent(outputStream.toByteArray());
        }
    }
}
