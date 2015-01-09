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
package org.xbup.lib.catalog.entity.manager;

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
import org.springframework.stereotype.Repository;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.catalog.base.manager.XBCXFileManager;
import org.xbup.lib.catalog.XBECatalog;
import org.xbup.lib.catalog.entity.XBEXFile;

/**
 * XBUP catalog accessory files manager.
 *
 * @version 0.1.22 2013/07/28
 * @author XBUP Project (http://xbup.org)
 */
@Repository
public class XBEXFileManager extends XBEDefaultManager<XBEXFile> implements XBCXFileManager<XBEXFile>, Serializable {

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
            Logger.getLogger(XBEXFileManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXFileManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public XBEXFile findById(long id) {
        try {
            return (XBEXFile) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXFile as o WHERE o.id = "+ id).getSingleResult();
        } catch (NoResultException ex) {
//            Logger.getLogger(XBEXFileManager.class.getExtensionName()).log(Level.SEVERE, null, ex);
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
            if (parent.getParent() != null) {
                if (parent.getXBIndex() == null) {
                    return null;
                }
                list.add(0, parent.getXBIndex());
            }
            parent = (XBCNode) parent.getParent();
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
//            Logger.getLogger(XBEXFileManager.class.getExtensionName()).log(Level.SEVERE, null, ex);
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
//            Logger.getLogger(XBEXFileManager.class.getExtensionName()).log(Level.SEVERE, null, ex);
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
