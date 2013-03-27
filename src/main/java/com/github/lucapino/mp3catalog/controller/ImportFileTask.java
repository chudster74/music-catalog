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
package com.github.lucapino.mp3catalog.controller;

import com.github.lucapino.mp3catalog.model.HibernateUtil;
import com.github.lucapino.mp3catalog.model.Song;
import com.github.lucapino.mp3catalog.view.ImportFileJDialog;
import java.io.File;
import javax.swing.SwingWorker;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author luca
 */
public class ImportFileTask extends SwingWorker<Void, Void> {

    private File[] selectedFolders;
    private ImportFileJDialog parent;
    private Session session;
    private int index;
    private Query query;
    private Logger logger = LoggerFactory.getLogger(ImportFileTask.class);

    public ImportFileTask(File[] selectedFolders, ImportFileJDialog parent) {
        this.selectedFolders = selectedFolders;
        this.parent = parent;
    }

    void recurse(File currentFile) throws Exception {
        if (isCancelled()) {
            throw new Exception();
        }
        if (currentFile.isDirectory()) {
            File[] files = currentFile.listFiles();
            for (File file : files) {
                recurse(file);
            }
        } else {
            if (isCancelled()) {
                throw new Exception();
            }
            try {
                AudioFile audioFile = AudioFileIO.read(currentFile);
                parent.jLabel1.setText("Importing " + currentFile.getAbsolutePath());
                Tag tag = audioFile.getTag();
                // create a new song to persist
                query.setParameter("fn", currentFile.getAbsolutePath());
                // logger.info("Executig query " + query.getQueryString());
                if (query.uniqueResult() == null) {
                    // logger.info("Adding file " + currentFile.getName());
                    Song song = new Song();
                    // "Title", "Author", "Album", "Duration", "Filename", "FileSize", "Bitrate", "Song number"
                    song.setTitle(tag.getFirst(FieldKey.TITLE));
                    song.setArtist(tag.getFirst(FieldKey.ARTIST));
                    song.setAlbum(tag.getFirst(FieldKey.ALBUM));
                    song.setDuration((int) audioFile.getAudioHeader().getTrackLength());
                    song.setFileName(currentFile.getPath());
                    song.setFileSize(Long.valueOf(currentFile.length()).intValue());
                    song.setBitrate(audioFile.getAudioHeader().getBitRate());
                    String trackNo = tag.getFirst(FieldKey.TRACK);
                    if (trackNo.isEmpty()) {
                    song.setSongNumber(null);
                    } else {
                        song.setSongNumber(Long.valueOf(trackNo).intValue());
                    }
                    // song.setComment(tag.getFirst(FieldKey.COMMENT));
                    //if (tag.getFirstArtwork() != null) {
                    //    song.setFileImage(tag.getFirstArtwork().getBinaryData());
                    //}
                    song.setRating(tag.getFirst(FieldKey.RATING));
                    song.setLyrics(tag.getFirst(FieldKey.LYRICS));
                    session.save(song);
                    if (index % 50 == 0) {
                        session.flush();
                        session.clear();
                    }
                } else {
                    logger.info("Skipping existing file " + currentFile.getPath());
                }
            } catch (Exception ex) {
                logger.error("Error verifying file " + currentFile.getName() + ": " + ex.getMessage());
                session.flush();
                session.clear();
            }
        }

    }

    @Override
    public Void doInBackground() {
        // import files in the db
        index = 0;
        for (File selectedFolder : selectedFolders) {
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                query = session.createQuery("from Song where fileName = :fn");
                session.beginTransaction();
                recurse(selectedFolder);
                session.getTransaction().commit();
                session.close();
            } catch (Exception ex) {
                // break the execution
                break;
            }
        }
        session.close();
        return null;
    }

    @Override
    public void done() {
        parent.jProgressBar1.setValue(100);
        parent.jProgressBar1.setIndeterminate(false);
        parent.jLabel1.setText("Import done");
        parent.setVisible(false);
    }
}
