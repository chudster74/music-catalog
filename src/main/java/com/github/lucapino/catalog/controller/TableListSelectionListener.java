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

import ca.odell.glazedlists.swing.DefaultEventTableModel;
import com.github.lucapino.catalog.model.Song;
import com.github.lucapino.catalog.model.Utils;
import com.github.lucapino.catalog.view.MainJFrame;
import com.github.lucapino.catalog.view.PropertiesJPanel;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;

/**
 *
 * @author tagliani
 */
public class TableListSelectionListener implements ListSelectionListener {

    private JTable table;
    private MainJFrame frame;

    public TableListSelectionListener(JTable table, MainJFrame frame) {
        this.table = table;
        this.frame = frame;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        // If cell selection is enabled, both row and column change events are fired
        if (e.getSource() == table.getSelectionModel()
                && table.getRowSelectionAllowed()) {
            PropertiesJPanel propertiesPanel = frame.getPropertiesPanel();
            propertiesPanel.authorTextField.setEnabled(true);
            propertiesPanel.titleTextField.setEnabled(true);
            propertiesPanel.albumTextField.setEnabled(true);
            if (table.getSelectedRowCount() == 1) {
                DefaultEventTableModel<Song> model = (DefaultEventTableModel) table.getModel();
                int row = table.convertRowIndexToModel(table.getSelectedRow());
                // set values
                propertiesPanel.titleTextField.setText(model.getValueAt(row, 0).toString());
                propertiesPanel.authorTextField.setText(model.getValueAt(row, 1).toString());
                propertiesPanel.albumTextField.setText(model.getValueAt(row, 2).toString());
                propertiesPanel.durationTextField.setText(Utils.formatDuration((int) model.getValueAt(row, 3)));
                propertiesPanel.filesizeTextField.setText(model.getValueAt(row, 5).toString());
                propertiesPanel.bitrateTextField.setText(model.getValueAt(row, 6).toString());
                Integer value = (Integer) model.getValueAt(row, 7);
                propertiesPanel.songNumberTextField.setText(value == null ? "" : value.toString());
                propertiesPanel.filenameTextField.setText(model.getValueAt(row, 4).toString());
                // set the current song to edit
                propertiesPanel.setEditedSong(model.getElementAt(row));
                // update image
                File realFile = new File(propertiesPanel.getEditedSong().getFileName());
                if (realFile.exists()) {
                    // update only if the panels are visible
                    for (Component child : frame.getMainPanel().rowPopupMenu.getComponents()) {
                        if (child instanceof JMenuItem) {
                            // conditionally enable menu item
                            if (((JMenuItem) child).getName().equalsIgnoreCase("play")) {
                                child.setEnabled(true);
                            }
                        }
                    }
                    boolean coverVisible = frame.getMultiSplitLayout().getNodeForName("cover").isVisible();
                    boolean lyricsVisible = frame.getMultiSplitLayout().getNodeForName("lyrics").isVisible();
                    if (coverVisible) {
                        try {
                            AudioFile audioFile = AudioFileIO.read(realFile);
                            Tag tag = audioFile.getTag();
                            if (tag.getFirstArtwork() != null) {
                                InputStream in = new ByteArrayInputStream(tag.getFirstArtwork().getBinaryData());
                                BufferedImage bImageFromConvert = ImageIO.read(in);
                                frame.getCoverPanel().setImage(bImageFromConvert);
                                in.close();
                            } else {
                                frame.getCoverPanel().setImage(new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB));
                            }
                        } catch (Exception ex) {
                        }
                    }
                    if (lyricsVisible) {
                        frame.getLyricsPanel().getLyricsTextArea().setText(propertiesPanel.getEditedSong().getLyrics());
                        frame.getLyricsPanel().getLyricsTextArea().setCaretPosition(0);
                    }
                } else {
                    // update only if the panels are visible
                    for (Component child : frame.getMainPanel().rowPopupMenu.getComponents()) {
                        if (child instanceof JMenuItem) {
                            // conditionally enable menu item
                            if (((JMenuItem) child).getName().equalsIgnoreCase("play")) {
                                child.setEnabled(false);
                            }
                        }
                    }
                }
                // enable reset button
                propertiesPanel.resetButton.setEnabled(true);
                // enable save button
                propertiesPanel.saveButton.setEnabled(true);
            } else {

                for (Component child : frame.getMainPanel().rowPopupMenu.getComponents()) {
                    if (child instanceof JMenuItem) {
                        // conditionally enable menu item
                        if (((JMenuItem) child).getName().equalsIgnoreCase("play")) {
                            child.setEnabled(false);
                        }
                    }
                }

                // remove text
                propertiesPanel.titleTextField.setText("");
                propertiesPanel.albumTextField.setText("");
                propertiesPanel.authorTextField.setText("");
                propertiesPanel.durationTextField.setText("");
                propertiesPanel.bitrateTextField.setText("");
                propertiesPanel.filesizeTextField.setText("");
                propertiesPanel.filenameTextField.setText("");
                propertiesPanel.songNumberTextField.setText("");
                // disable text fields
                propertiesPanel.authorTextField.setEnabled(false);
                propertiesPanel.titleTextField.setEnabled(false);
                propertiesPanel.albumTextField.setEnabled(false);
                // disable reset button
                propertiesPanel.resetButton.setEnabled(false);
                // disable save button
                propertiesPanel.saveButton.setEnabled(false);
                // unset the current song to edit
                propertiesPanel.setEditedSong(null);
                // reset cover
                frame.getCoverPanel().setImage(new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB));
                // reset lyrics
                frame.getLyricsPanel().getLyricsTextArea().setText("");
            }
        }
    }
}
