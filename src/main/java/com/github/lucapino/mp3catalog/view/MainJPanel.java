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
package com.github.lucapino.mp3catalog.view;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.EventJXTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import com.github.lucapino.mp3catalog.controller.PlayListItem;
import com.github.lucapino.mp3catalog.controller.SongTableFormat;
import com.github.lucapino.mp3catalog.model.HibernateUtil;
import com.github.lucapino.mp3catalog.model.Song;
import com.github.lucapino.mp3catalog.model.Utils;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.ToolTipHighlighter;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.table.TableColumnExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main panel for displaying the list of files
 *
 * @author luca
 */
public class MainJPanel extends javax.swing.JPanel {

    List<Song> songs = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger("it.mp3catalog");
    public JPopupMenu rowPopupMenu;
    public JPopupMenu headerPopupMenu;
    private MainJFrame frame;
    private EventList<Song> songsEventList = new BasicEventList<>();

    public EventList<Song> getSongsEventList() {
        return songsEventList;
    }

    /**
     * Creates new form MainJpanel
     */
    public MainJPanel(MainJFrame frame) {


        initComponents();
        this.frame = frame;
        // populate the list with the song present in the DB
        fillTableModel();

        jXTable2.setDefaultRenderer(String.class, new StringTableCellRendered());

//        jXTable2.addMouseMotionListener(new MouseMotionAdapter() {
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                Point p = e.getPoint();
//                if (jXTable2.getRowCount() > 0) {
//                    int row = jXTable2.rowAtPoint(p);
//                    int column = jXTable2.columnAtPoint(p);
//                    if (row >= 0) {
//                        jXTable2.setToolTipText(String.valueOf(jXTable2.getValueAt(row, column)));
//                    }
//                }
//            }//end MouseMoved
//        }); // end MouseMotionAdapter
    }

    public JTable getjXTable2() {
        return jXTable2;
    }

    private void syncTree() {
        JTree tree = frame.getNavigatorPanel().getCategoriesTree();
        tree.setModel(null);
    }

    final void fillTableModel() {
        if (songs.isEmpty()) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            songs = session.createQuery("from Song").list();
            logger.info("Trovate " + songs.size() + " canzoni");
            session.close();
        }

        songsEventList.addAll(songs);
        SortedList<Song> sortedSongs = new SortedList<>(songsEventList);
        MatcherEditor<Song> textMatcherEditor = new TextComponentMatcherEditor<>(searchTextField, new SongTextFilterator());
        FilterList<Song> textFilteredIssues = new FilterList<>(sortedSongs, textMatcherEditor);
        EventJXTableModel<Song> songsTableModel = new EventJXTableModel(textFilteredIssues, new SongTableFormat());
        jXTable2.setModel(songsTableModel);
        TableComparatorChooser tableSorter = TableComparatorChooser.install(
                (JTable) jXTable2, sortedSongs, TableComparatorChooser.MULTIPLE_COLUMN_KEYBOARD);



        // reload the tableModel
//        SongTableModel newModel = new SongTableModel(songs);
//        newModel.setColumnIdentifiers(new Object[]{"Titolo", "Artist", "Album", "Durata", "Nome file", "Dimensione", "Bit Rate", "Numero traccia", "Tipo", "Classifica"});
//        newModel.addTableModelListener(new TableModelListener() {
//            @Override
//            public void tableChanged(TableModelEvent e) {
//                // if deleting
//                if (e.getType() == TableModelEvent.DELETE) {
//                    Song deletedSong = songs.remove(e.getFirstRow());
//                    Session session = HibernateUtil.getSessionFactory().openSession();
//                    Transaction tx = session.beginTransaction();
//                    session.delete(deletedSong);
//                    tx.commit();
//                }
//            }
//        });
//        jXTable2.setModel(newModel);


        // set a custom cellrenderer for Duration column
        StringValue sv = new StringValue() {
            @Override
            public String getString(Object value) {
                String res = Utils.formatDuration((int) value);
                return res;
            }
        };
        jXTable2.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableRenderer(sv, SwingConstants.RIGHT));

        jXTable2.addHighlighter(new ToolTipHighlighter(HighlightPredicate.IS_TEXT_TRUNCATED));

        // add a popup menu on the table for deleting selected rows
        rowPopupMenu = new JPopupMenu();
        // play song (default disabled)
        JMenuItem playSongMenuItem = new JMenuItem("Riproduci");
        playSongMenuItem.setEnabled(false);
        playSongMenuItem.addActionListener(new ActionAdapter(this));
        rowPopupMenu.add(playSongMenuItem);
        // add to playlist (always enabled)
        JMenuItem addSongToPlaylistMenuItem = new JMenuItem("Aggiungi a playlist");
        addSongToPlaylistMenuItem.addActionListener(new ActionAdapter(this));
        rowPopupMenu.add(addSongToPlaylistMenuItem);
        // separator
        rowPopupMenu.addSeparator();
        // delete from collection (always enabled)
        JMenuItem removeSongsMenuItem = new JMenuItem("Rimuovi canzoni");
        removeSongsMenuItem.addActionListener(new ActionAdapter(this));
        rowPopupMenu.add(removeSongsMenuItem);

        MouseListener popupListener = new PopupListener();
        jXTable2.addMouseListener(popupListener);



//        jXTable2.getSelectionModel().addListSelectionListener(new SelectionListener());
        // resync the tree
//        syncTree();
        MouseListener headerPopupListener = new HeaderPopoupListener();
        jXTable2.getTableHeader().addMouseListener(headerPopupListener);

        jXTable2.packAll();
        // set preferredSize of columns
        // Title
        jXTable2.getColumnModel().getColumn(0).setPreferredWidth(200);
        // Artist
        jXTable2.getColumnModel().getColumn(1).setPreferredWidth(200);
        // Album
        jXTable2.getColumnModel().getColumn(2).setPreferredWidth(200);
        // default sort on Artist column
        jXTable2.getRowSorter().toggleSortOrder(1);
    }

    void removeRowsFromTable() {
        int[] selectedRowIds = jXTable2.getSelectedRows();
        // ask for confirmation
        int res = JOptionPane.showConfirmDialog(frame, "Conferma la rimozione di " + selectedRowIds.length + " canzoni dal catalogo", "Conferma", JOptionPane.OK_CANCEL_OPTION);
        // remove selected rows
        if (res == JOptionPane.OK_OPTION) {
            // show progress dialog
            RemoveSongJDialog dialog = new RemoveSongJDialog(frame, true);
            dialog.setLocationRelativeTo(null);
            dialog.removeFiles(this);
            dialog.setVisible(true);
        }

        // resync the tree
        syncTree();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jXTable2 = new org.jdesktop.swingx.JXTable();
        jLabel1 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jXTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jXTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jXTable2.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        jXTable2.setDoubleBuffered(true);
        jXTable2.setEditable(false);
        jScrollPane1.setViewportView(jXTable2);

        jLabel1.setText("Search:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXTable jXTable2;
    private javax.swing.JTextField searchTextField;
    // End of variables declaration//GEN-END:variables

    private class StringTableCellRendered extends DefaultTableCellRenderer {

        int __toolTipRow = -1;
        int __toolTipColumn = -1;
        int __toolTipX = 0;
        int __toolTipY = 0;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setToolTipText(jXTable2.getStringAt(row, column));
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

        /**
         * Returns the tooltip location in this component's coordinate system.
         * Without this, the if the mouse moves from between cells that have the
         * same tooltip, the tooltip won't follow the mouse. The __toolTipRow,
         * etc., logic is to prevent the tooltip from following the mouse too
         * closely (i.e., every pixel), as that can be distracting this way, it
         * only follows the mouse when the mouse moves to a new cell
         *
         * @param event the MouseEvent that caused the ToolTipManager to
         *
         * show the tooltip
         */
        @Override
        public Point getToolTipLocation(MouseEvent event) {
            Point point = event.getPoint();
            int row = jXTable2.rowAtPoint(point);
            int column = jXTable2.columnAtPoint(point);
            if (row != __toolTipRow || column != __toolTipColumn) {
                __toolTipRow = row;
                __toolTipColumn = column;
                __toolTipX = (int) point.getX();
                __toolTipY = (int) point.getY();
            }
            return new Point(__toolTipX + 10, __toolTipY + 20);
        }
    }

    class HeaderActionAdapter implements ActionListener {

        private JXTable table;

        public HeaderActionAdapter(JXTable table) {
            this.table = table;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean active = ((JCheckBoxMenuItem) e.getSource()).getState();
            String identifier = ((JCheckBoxMenuItem) e.getSource()).getText();
            table.getColumnExt(identifier).setVisible(active);
        }
    }

    class HeaderPopoupListener implements MouseListener {

        @Override
        public void mousePressed(MouseEvent e) {
            showPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            showPopup(e);
        }

        private void showPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                // add a popup menu on the header for selecting column visibility
                headerPopupMenu = new JPopupMenu();
                // get all column (also the invisible one)
                // int columnCount = jXTable2.getColumnCount(true);
                List<TableColumn> columns = jXTable2.getColumns(true);
                JMenuItem item;
                for (TableColumn tableColumn : columns) {
                    TableColumnExt column = jXTable2.getColumnExt(tableColumn.getIdentifier());
                    item = new JCheckBoxMenuItem(column.getTitle(), column.isVisible());
                    item.addActionListener(new HeaderActionAdapter(jXTable2));
                    headerPopupMenu.add(item);
                }
                headerPopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    class PopupListener implements MouseListener {

        @Override
        public void mousePressed(MouseEvent e) {
            showPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (jXTable2.getSelectedRowCount() == 1) {
                int r = jXTable2.rowAtPoint(e.getPoint());
                if (r >= 0 && r < jXTable2.getRowCount()) {
                    jXTable2.setRowSelectionInterval(r, r);
                } else {
                    jXTable2.clearSelection();
                }
            }

            showPopup(e);
        }

        private void showPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                rowPopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                JTable table = frame.getMainPanel().getjXTable2();
                EventJXTableModel<Song> model = (EventJXTableModel<Song>) table.getModel();
                int row = table.convertRowIndexToModel(table.getSelectedRow());
                Song currentSong = model.getElementAt(row);
                JOptionPane.showMessageDialog(frame, "Riproduci\n" + currentSong.getTitle(), "Riproduci con doppio click", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

//    class SelectionListener implements ListSelectionListener {
//
//        @Override
//        public void valueChanged(ListSelectionEvent e) {
//            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
//            // Find out which indexes are selected.
//            int minIndex = lsm.getMinSelectionIndex();
//            int maxIndex = lsm.getMaxSelectionIndex();
//            for (int i = minIndex; i <= maxIndex; i++) {
//                if (lsm.isSelectedIndex(i)) {
//                    Song currentSong = ((SongTableModel) jXTable2.getModel()).getSong(i);
//                    // update image
//                    if (currentSong.getFileImage() != null) {
//                        InputStream in = new ByteArrayInputStream(currentSong.getFileImage());
//                        try {
//                            BufferedImage bImageFromConvert = ImageIO.read(in);
//                            frame.getCoverPanel().setImage(bImageFromConvert);
//                        } catch (Exception ex) {
//                        }
//                    }
//                    // update lyrics
//                    if (!currentSong.getLyrics().isEmpty()) {
//                        frame.getLyricsPanel().getLyricsTextArea().setText(currentSong.getLyrics());
//                        frame.getLyricsPanel().getLyricsTextArea().setCaretPosition(0);
//                    }
//                    break;
//                }
//            }
//
//
//        }
//    }
    class ActionAdapter implements ActionListener {

        MainJPanel adapter;

        ActionAdapter(MainJPanel adapter) {
            this.adapter = adapter;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JXTable table = (JXTable) frame.getMainPanel().getjXTable2();
            EventJXTableModel<Song> model = (EventJXTableModel) table.getModel();
            int row = table.convertRowIndexToModel(table.getSelectedRow());
            Song currentSong = model.getElementAt(row);
            JMenuItem item = (JMenuItem) e.getSource();
            if (item.getText().equals("Rimuovi canzoni")) {
                adapter.removeRowsFromTable();
            }
            if (item.getText().equals("Riproduci")) {
                // TEMP
                JOptionPane.showMessageDialog(frame, "Riproduci " + currentSong.getTitle(), "Riproduci canzone", JOptionPane.INFORMATION_MESSAGE);
                // adapter.removeRowsFromTable();
            }
            if (item.getText().equals("Aggiungi a playlist")) {
                // TEMP
                if (table.getSelectedRowCount() == 1) {
                    // JOptionPane.showMessageDialog(frame, "Aggiungi a playlist la canzone " + currentSong.getTitle(), "Aggiungi canzone a playlist", JOptionPane.INFORMATION_MESSAGE);
                    frame.getNavigatorPanel().getList().getModel().addItem(new PlayListItem(currentSong.getTitle(), currentSong.getFileName()));
                } else {
                    for (int selectedRow : table.getSelectedRows()) {
                        currentSong = model.getElementAt(table.convertRowIndexToModel(selectedRow));
                        frame.getNavigatorPanel().getList().getModel().addItem(new PlayListItem(currentSong.getTitle(), currentSong.getFileName()));
                    }
                    // JOptionPane.showMessageDialog(frame, "Aggiungi a playlist " + table.getSelectedRowCount() + " canzoni", "Aggiungi canzone a playlist", JOptionPane.INFORMATION_MESSAGE);
                }
                // adapter.removeRowsFromTable();
            }
        }
    }
}
