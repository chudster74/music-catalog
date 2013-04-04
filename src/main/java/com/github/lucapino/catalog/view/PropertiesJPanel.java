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
package com.github.lucapino.catalog.view;

import ca.odell.glazedlists.swing.EventJXTableModel;
import com.github.lucapino.catalog.model.HibernateUtil;
import com.github.lucapino.catalog.model.Song;
import java.io.File;
import javax.swing.JTable;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/**
 *
 * @author luca
 */
public class PropertiesJPanel extends javax.swing.JPanel {

    private static int TITLE = 0;
    private static int AUTHOR = 1;
    private static int ALBUM = 2;
    private String[] oldValues = new String[7];
    private Song editedSong = null;
    private JTable mainTable;

    /**
     * Creates new form PropertiesJPanel
     */
    public PropertiesJPanel(JTable mainTable) {
        initComponents();
        this.mainTable = mainTable;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        durationLabel = new javax.swing.JLabel();
        authorLabel = new javax.swing.JLabel();
        albumLabel = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        titleTextField = new javax.swing.JTextField();
        albumTextField = new javax.swing.JTextField();
        authorTextField = new javax.swing.JTextField();
        durationTextField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        trackNumberLabel = new javax.swing.JLabel();
        filenameLabel = new javax.swing.JLabel();
        filesizeLabel = new javax.swing.JLabel();
        bitrateLabel = new javax.swing.JLabel();
        bitrateTextField = new javax.swing.JTextField();
        filesizeTextField = new javax.swing.JTextField();
        filenameTextField = new javax.swing.JTextField();
        songNumberTextField = new javax.swing.JTextField();
        resetButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("bundle"); // NOI18N
        setToolTipText(bundle.getString("PROPERTIES")); // NOI18N

        durationLabel.setText(bundle.getString("DURATION")); // NOI18N

        authorLabel.setText(bundle.getString("ARTIST")); // NOI18N

        albumLabel.setText(bundle.getString("ALBUM")); // NOI18N

        titleLabel.setText(bundle.getString("TITLE")); // NOI18N

        durationTextField.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel)
                    .addComponent(albumLabel)
                    .addComponent(authorLabel)
                    .addComponent(durationLabel))
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(durationTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .addComponent(authorTextField)
                    .addComponent(albumTextField)
                    .addComponent(titleTextField))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleLabel)
                    .addComponent(titleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(albumLabel)
                    .addComponent(albumTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(authorLabel)
                    .addComponent(authorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(durationLabel)
                    .addComponent(durationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        trackNumberLabel.setText(bundle.getString("TRACK NUMBER")); // NOI18N

        filenameLabel.setText(bundle.getString("FILENAME")); // NOI18N

        filesizeLabel.setText(bundle.getString("FILESIZE")); // NOI18N

        bitrateLabel.setText(bundle.getString("BITRATE")); // NOI18N

        bitrateTextField.setEnabled(false);

        filesizeTextField.setEnabled(false);

        filenameTextField.setEnabled(false);

        songNumberTextField.setEnabled(false);

        resetButton.setText(bundle.getString("RESET")); // NOI18N
        resetButton.setEnabled(false);
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        saveButton.setText(bundle.getString("SAVE")); // NOI18N
        saveButton.setEnabled(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(filesizeLabel)
                            .addComponent(bitrateLabel)
                            .addComponent(filenameLabel))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(filenameTextField)
                            .addComponent(bitrateTextField)
                            .addComponent(filesizeTextField)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(trackNumberLabel)
                        .addGap(23, 23, 23)
                        .addComponent(songNumberTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveButton)))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {resetButton, saveButton});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {bitrateLabel, filenameLabel, filesizeLabel, trackNumberLabel});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bitrateLabel)
                    .addComponent(bitrateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filesizeLabel)
                    .addComponent(filesizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filenameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filenameLabel))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resetButton)
                    .addComponent(saveButton)
                    .addComponent(songNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(trackNumberLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {bitrateLabel, filenameLabel, filesizeLabel, trackNumberLabel});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        // reset old values
        titleTextField.setText(oldValues[TITLE]);
        authorTextField.setText(oldValues[AUTHOR]);
        albumTextField.setText(oldValues[ALBUM]);
    }//GEN-LAST:event_resetButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // synch textfield with song
        editedSong.setTitle(titleTextField.getText());
        editedSong.setArtist(authorTextField.getText());
        editedSong.setAlbum(albumTextField.getText());
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.update(editedSong);
        tx.commit();
        // update the field in file
        try {
            AudioFile audioFile = AudioFileIO.read(new File(filesizeTextField.getText()));
            Tag tag = audioFile.getTag();
            tag.setField((FieldKey.TITLE), editedSong.getTitle());
            tag.setField((FieldKey.ARTIST), editedSong.getTitle());
            tag.setField((FieldKey.ALBUM), editedSong.getTitle());
            audioFile.commit();
        } catch (Exception ex) {
        }
        // update model
        EventJXTableModel<Song> model = (EventJXTableModel) mainTable.getModel();
        int row = mainTable.getSelectedRow();
        model.fireTableRowsUpdated(row, row);
    }//GEN-LAST:event_saveButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel albumLabel;
    public javax.swing.JTextField albumTextField;
    public javax.swing.JLabel authorLabel;
    public javax.swing.JTextField authorTextField;
    public javax.swing.JLabel bitrateLabel;
    public javax.swing.JTextField bitrateTextField;
    public javax.swing.JLabel durationLabel;
    public javax.swing.JTextField durationTextField;
    public javax.swing.JLabel filenameLabel;
    public javax.swing.JTextField filenameTextField;
    public javax.swing.JLabel filesizeLabel;
    public javax.swing.JTextField filesizeTextField;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel2;
    public javax.swing.JButton resetButton;
    public javax.swing.JButton saveButton;
    public javax.swing.JTextField songNumberTextField;
    public javax.swing.JLabel titleLabel;
    public javax.swing.JTextField titleTextField;
    public javax.swing.JLabel trackNumberLabel;
    // End of variables declaration//GEN-END:variables

    public Song getEditedSong() {
        return editedSong;
    }

    public void setEditedSong(Song editedSong) {
        this.editedSong = editedSong;
    }
}
