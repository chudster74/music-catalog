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

import com.github.lucapino.mp3catalog.controller.TableListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.MultiSplitLayout.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 *
 * @author luca
 */
public class MainJFrame extends JFrame {

    private Logger logger = LoggerFactory.getLogger("it.mp3catalog");
    private static String LAYOUT_CONFIG_FILE = "layoutConfig.xml";
    JMenuBar menuBar;
    // last folder selected by JFileChooser
    private String lastFolder;
    private MainJPanel mainPanel;
    private CoverJPanel coverPanel;
    private NavigatorJPanel navigatorPanel;
    private LyricsJPanel lyricsPanel;
    private PlayerJPanel playerPanel;
    private PropertiesJPanel propertiesPanel;
    private StatusBarJPanel statusBarPanel;
    private MultiSplitLayout layout;
    private JXMultiSplitPane msp;

    public MainJPanel getMainPanel() {
        return mainPanel;
    }

    public CoverJPanel getCoverPanel() {
        return coverPanel;
    }

    public NavigatorJPanel getNavigatorPanel() {
        return navigatorPanel;
    }

    public LyricsJPanel getLyricsPanel() {
        return lyricsPanel;
    }

    public PlayerJPanel getPlayerPanel() {
        return playerPanel;
    }

    public PropertiesJPanel getPropertiesPanel() {
        return propertiesPanel;
    }

    public MainJFrame() throws HeadlessException {
        // initializing last foder with usere home
        lastFolder = System.getProperty("user.home");

        UIManager.put("Table.alternateRowColor", new Color(242, 245, 249));

        MultiSplitLayout.Split leftColumn = new MultiSplitLayout.Split();
        leftColumn.setRowLayout(false);
        MultiSplitLayout.Leaf playlist = new MultiSplitLayout.Leaf("playlist");
        playlist.setWeight(0.9);
        MultiSplitLayout.Leaf cover = new MultiSplitLayout.Leaf("cover");
        cover.setWeight(0.1);
        leftColumn.setChildren(playlist, new MultiSplitLayout.Divider(), cover);
        // sets the column weight to 20 percent
        leftColumn.setWeight(0.05);

        // create the splitpane model
        layout = new MultiSplitLayout();
        try {
            logger.info("Trying to load previous saved layout...");
            try (XMLDecoder d = new XMLDecoder(new BufferedInputStream(
                    new FileInputStream(System.getProperty("user.home") + "/" + LAYOUT_CONFIG_FILE)))) {
                Node model = (Node) (d.readObject());
                layout.setModel(model);
                layout.setFloatingDividers(false);
            }
            logger.info("Previous layout found.");
        } catch (Exception exc) {
            logger.info("No layout found. Creating default layout...");

            MultiSplitLayout.Split rightColumn = new MultiSplitLayout.Split();
            rightColumn.setRowLayout(false);

            MultiSplitLayout.Split editorLyrics = new MultiSplitLayout.Split();
            editorLyrics.setRowLayout(true);
            editorLyrics.setWeight(0.95);

            MultiSplitLayout.Leaf editor = new MultiSplitLayout.Leaf("editor");
            editor.setWeight(0.9);
            MultiSplitLayout.Leaf lyrics = new MultiSplitLayout.Leaf("lyrics");

            editorLyrics.setChildren(editor, new MultiSplitLayout.Divider(), lyrics);

            MultiSplitLayout.Leaf properties = new MultiSplitLayout.Leaf("properties");
            properties.setWeight(0.05);

            rightColumn.setChildren(editorLyrics, new MultiSplitLayout.Divider(), properties);
            rightColumn.setWeight(0.95);

            MultiSplitLayout.Split row = new MultiSplitLayout.Split();
            row.setRowLayout(true);

            row.setChildren(leftColumn, new MultiSplitLayout.Divider(), rightColumn);

            layout.setModel(row);
            logger.info("Default layout created.");
        }
        msp = new JXMultiSplitPane();
        msp.setContinuousLayout(false);
        layout.setDividerSize(3);
        msp.setLayout(layout);
        lyricsPanel = new LyricsJPanel();
        navigatorPanel = new NavigatorJPanel(this);
        coverPanel = new CoverJPanel();
        mainPanel = new MainJPanel(this);
        propertiesPanel = new PropertiesJPanel(mainPanel.getjXTable2());

        // add components
        msp.add(mainPanel, "editor");
        msp.add(propertiesPanel, "properties");
        msp.add(lyricsPanel, "lyrics");
        msp.add(coverPanel, "cover");
        msp.add(navigatorPanel, "playlist");

        Container cp = this.getContentPane();
        
        // create player panel
        playerPanel = new PlayerJPanel(this);

        // create statusbar
        statusBarPanel = new StatusBarJPanel(this);
        statusBarPanel.syncButtonState();

        cp.add(playerPanel, BorderLayout.NORTH);
        cp.add(msp, BorderLayout.CENTER);
        cp.add(statusBarPanel, BorderLayout.SOUTH);
        
        // adding the menu
        createMenu();
        pack();
        TableListSelectionListener tlsl = new TableListSelectionListener(mainPanel.getjXTable2(), this);
        mainPanel.getjXTable2().getSelectionModel().addListSelectionListener(tlsl);
    }

    public MultiSplitLayout getMultiSplitLayout() {
        return layout;
    }

    private void createMenu() {
        menuBar = new JMenuBar();
        // fileMenu
        JMenu fileMenu = new JMenu("File");
        JMenuItem importMenuItem = new JMenuItem("Import files");
        importMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                importMenuItemActionPerformed(evt);
            }
        });
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(importMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        // helpMenu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                showAboutDialogActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);

        // setting the JMenuBar
        setJMenuBar(menuBar);
    }

    private void exitMenuItemActionPerformed(ActionEvent evt) {
        closeWindow();
    }

    private void showAboutDialogActionPerformed(ActionEvent evt) {
        AboutJDialog dialog = new AboutJDialog(this, true);
        dialog.setVisible(true);
    }

    private void importMenuItemActionPerformed(ActionEvent evt) {
        // open dialog to select a folder
        JFileChooser chooser = new JFileChooser(lastFolder);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            // retrieve the folder selected
            File[] selectedFolders = chooser.getSelectedFiles();
            if (selectedFolders.length > 0) {
                lastFolder = selectedFolders[0].getParent();
                // show a modal dialog (not closeable) with import statistics
                ImportFileJDialog dialog = new ImportFileJDialog(this, true);
                dialog.setLocationRelativeTo(null);
                dialog.importFiles(selectedFolders);
                dialog.setVisible(true);
                mainPanel.songs.clear();
                mainPanel.fillTableModel();
            }
        }
    }

    public static void main(String[] args) {
        // shut up jul
        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            rootLogger.removeHandler(handlers[i]);
        }
        SLF4JBridgeHandler.install();
        /**
         * this gets rid of exception for not using native acceleration
         */
        System.setProperty("com.sun.media.jai.disableMediaLib", "true");

        try {
            // Set Nimbus L&F
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainJFrame frame = new MainJFrame();
                GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
                frame.setSize(e.getMaximumWindowBounds().width, e.getMaximumWindowBounds().height);
                frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                frame.addWindowListener(new ClosingAdapter(frame));
                frame.setVisible(true);
            }
        });
    }

    private void closeWindow() {
        // save layout
        try {
            logger.info("Saving layout...");
            try (XMLEncoder enc = new XMLEncoder(new BufferedOutputStream(
                                  new FileOutputStream(System.getProperty("user.home") + "/" + LAYOUT_CONFIG_FILE)))) {
                MultiSplitLayout.Node model = msp.getMultiSplitLayout().getModel();
                enc.writeObject(model);
            }
        } catch (Exception ex) {
        }
        logger.info("Layout saved. Quitting application.");
        System.exit(0);
    }

    private static class ClosingAdapter extends WindowAdapter {

        private MainJFrame frame;

        public ClosingAdapter(MainJFrame frame) {
            this.frame = frame;
        }

        @Override
        public void windowClosing(WindowEvent e) {
            frame.closeWindow();
        }
    }
}
