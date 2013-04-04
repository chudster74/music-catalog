/*
 *    Copyright 2012 Luca Tagliani
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.lucapino.catalog.controller;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventJXTableModel;
import com.github.lucapino.catalog.model.HibernateUtil;
import com.github.lucapino.catalog.model.Song;
import com.github.lucapino.catalog.view.MainJPanel;
import com.github.lucapino.catalog.view.RemoveSongJDialog;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import org.hibernate.Session;

/**
 *
 * @author luca
 */
public class RemoveSongTask extends SwingWorker<Void, Void> {

    private JTable jTable;
    private MainJPanel mainPanel;
    private RemoveSongJDialog parent;

    public RemoveSongTask(MainJPanel mainPanel, RemoveSongJDialog parent) {
        this.mainPanel = mainPanel;
        this.jTable = mainPanel.getjXTable2();
        this.parent = parent;
    }

    @Override
    public Void doInBackground() {
        EventJXTableModel<Song> model = (EventJXTableModel<Song>) jTable.getModel();
        EventList<Song> songEventList = mainPanel.getSongsEventList();
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        songEventList.getReadWriteLock().writeLock().tryLock();
        try {
            int[] selectedRowIds = jTable.getSelectedRows();
            for (int i = selectedRowIds.length - 1; i >= 0; i--) {
                int j = selectedRowIds[i];
                parent.jLabel1.setText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("bundle").getString("REMOVING {0}"), new Object[]{model.getValueAt(jTable.convertRowIndexToModel(j), 4)}));
                try {
                    songEventList.remove(model.getElementAt(jTable.convertRowIndexToModel(j)));
                    session.delete(model.getElementAt(jTable.convertRowIndexToModel(j)));
                } catch (Exception e) {
                    // do nothing
                }
            }
            session.flush();
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.flush();
            session.getTransaction().rollback();
        } finally {
            songEventList.getReadWriteLock().writeLock().unlock();
        }
        return null;
    }

    @Override
    public void done() {
        parent.setVisible(false);
    }
}
