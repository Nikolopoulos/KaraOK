/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import static java.lang.System.exit;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import shark.tornado.Control;
import shark.tornado.Mp3Filter;
import shark.tornado.TxtFilter;
import shark.tornado.XmlFilter;
import shark.tornado.lyric;
import shark.tornado.xmlandtextfilter;
import soundAnalyzer.AudioWaveformCreator;

/**
 *
 * @author Basil
 */
public class mainPanel extends javax.swing.JFrame {

    /**
     * Creates new form mainPanel
     */
    //global scope variables decleration
    ArrayList<lyric> ra;
    boolean memoryFlag = false;
    String tempText = "";
    boolean stopCopy = false;
    boolean frommouse = false;
    boolean wordscroll = false;
    String buffer = "";
    String version = "version Super Turbo HD World Edition Alpha 1.6 Black Edition Remix";
    String title = "Hyper Kara-OK! II - " + version;
    boolean trueClose = true;
    boolean carryOn = false;
    int activeLyric = -1;
    boolean fireDocument = true;
    AudioInputStream sound;
    float pixelperms = 0;
    long laste = 0;
    String image;
    JFrame self = this;
    AudioWaveformCreator awc;
    BufferedImage bfimg;
    Boolean yes = true;
    Boolean play = false;
    Thread imageThread;
    Thread soundThread;
    File toBeStreamed;
    int markerPosition = 0;
    int tempMarker = 0;
    SourceDataLine line;
    Thread move;
    int tokenizeFrom = -1;
    int tokenizeUntil = 0;
    Boolean hasChanges = false;
    Control contr;
    String audiopath;
    String textpath;
    String module = "";
    KeyListener kl;
    int globalReturn = 0;
    boolean debug = false;
    logger logger;
    int cancel = 0;
    KeyListener tkl;
    long freememory = 0;
    Runtime runtime;

    public mainPanel(Control control) {
        splashScreen s = new gui.splashScreen();

        if (!debug) {
            logger = new logger();

        }
        else {
            logger = new logger();
            logger.setVisible(true);
        }
        logger.log("Ο ορισμος του καραόκε κυρία μου!");

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex) {
            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex) {
            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        File file = null;
        this.contr = control;

        Dimension screenSize2 = Toolkit.getDefaultToolkit().getScreenSize();
        double width2 = screenSize2.getWidth();
        double height2 = screenSize2.getHeight();

        final JFileChooser fcm = new JFileChooser();
        s.setFlavourText("Select your audio");
        logger.log("Select your audio");

        fcm.setDialogTitle("Select your audio");
        fcm.setFileFilter(new Mp3Filter());
        fcm.setAcceptAllFileFilterUsed(false);

        fcm.requestFocus();

        int returnVal = fcm.showOpenDialog(s);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fcm.getSelectedFile();
            module = file.getName();
            audiopath = file.getParent();
        }

        if (!(returnVal == JFileChooser.APPROVE_OPTION)) {
            exit(-1);
        }

        try {

            // JFrame wait = new JFrame();
            s.setFlavourText("Analysing " + module + " now!");
            logger.log("Analysing " + module + " now!");
            //JLabel pw = new JLabel(icon);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            double width = screenSize.getWidth();
            double height = screenSize.getHeight();
//      wait.setLocation(new Point((int) Math.round((width - 200) / 2), (int) Math.round((height - 100) / 2)));
//      wait.setSize(200, 100);
//
//
//      //wait.getContentPane().getComponent(wait.getContentPane().getComponentCount() - 1).setLocation(new Point(23541325, 50));
//      //wait.setUndecorated(true);
//      wait.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//      wait.setTitle("Analyzing sound - Please wait");
//      wait.setBackground(Color.gray);
//      wait.validate();
//      wait.repaint();
//      wait.setVisible(true);
//      wait.repaint();
//      wait.revalidate();
            awc = new AudioWaveformCreator(fcm.getSelectedFile().getAbsolutePath());

            awc.createAudioInputStream();

            image = awc.getImage();
            bfimg = ImageIO.read(new File(image));
            String soundPath = awc.getDecodedPath();
            toBeStreamed = new File(soundPath);
            sound = AudioSystem.getAudioInputStream(toBeStreamed);
            //wait.setVisible(false);
        }
        catch (Exception e) {
            
            if (debug) {
                Writer writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                e.printStackTrace(printWriter);
                String output = writer.toString();
                logger.log(output);
            }
            
            JOptionPane.showMessageDialog(this, "Something went Terribly wrong! Please contact and administrator if this error persists! " + e, "Error",
                    JOptionPane.ERROR_MESSAGE);
            if (debug) {
                Writer writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                e.printStackTrace(printWriter);
                String output = writer.toString();
                logger.log(output);
            }
            System.exit(-1);
        }

        ra = new ArrayList<lyric>();

        File text = null;
        String everything = "";
        initComponents();

        final JFileChooser fc = new JFileChooser();

        fc.setDialogTitle("Select your text");
        s.setFlavourText("Select your text");
        logger.log("Select your text");
        fc.setCurrentDirectory(new File(audiopath + "\\.."));
        fc.setFileFilter(new XmlFilter());
        fc.setFileFilter(new TxtFilter());
        fc.setFileFilter(new xmlandtextfilter());

        fc.setAcceptAllFileFilterUsed(false);
        fc.requestFocus();
        returnVal = fc.showOpenDialog(s);
        String ext = "";
        boolean show = false;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            text = fc.getSelectedFile();
            textpath = text.getParent();
            StringTokenizer st = new StringTokenizer(text.getName(), ".");
            ext = "";
            while (st.hasMoreElements()) {
                ext = st.nextToken();

            }

        }
        if (!(returnVal == JFileChooser.APPROVE_OPTION)) {
            JOptionPane.showMessageDialog(this, "Since you didn't open a text file, you'll have to copy paste the text", "Warning",
                    JOptionPane.WARNING_MESSAGE);

            textpath = "notset";
        }

        if (ext.equals("txt")) {
            s.setFlavourText("Reading " + text.getName());
            logger.log("Reading " + text.getName());
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(text), "ISO-8859-1"));

                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append('\n');
                    line = br.readLine();
                }
                everything = sb.toString();
                this.jTextPane1.setText(everything);
                updateLyrics();
            }
            catch (Exception e) {
                textpath = "notset";
                JOptionPane.showMessageDialog(this, "Could not read text file, please copy paste it.", "Warning",
                        JOptionPane.WARNING_MESSAGE);

            }
        }
        else if (ext.equals("xml")) {
            s.setFlavourText("Reading " + text.getName());
            logger.log("Reading " + text.getName());
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(text), "ISO-8859-1"));
                String buffer = "";
                ra.clear();
                StringTokenizer sa = new StringTokenizer(buffer);
                while ((buffer = br.readLine()) != null) {

                    if (buffer.contains("<lyric text=\"")) {
                        while (Character.isWhitespace(buffer.charAt(0))) {
                            buffer = buffer.substring(1);
                        }
                        if (debug) {
                            logger.log(buffer);
                        }
                        buffer = buffer.substring(13);
                        sa = new StringTokenizer(buffer, "\"");
                        String lyric = sa.nextToken();
                        String skip = sa.nextToken();
                        while (!skip.contains("time=")) {
                            if (debug) {
                                logger.log(skip);
                            }
                            skip = sa.nextToken();
                        }

                        String time = sa.nextToken();

                        String toTest = lyric;
                        String toWrite = "";
                        for (int j = 0; j < toTest.length(); j++) {
                            if (j < toTest.length() - 2 && toTest.charAt(j) == '\'' && toTest.charAt(j + 1) == '\'') {
                                toWrite += "\"";
                                j = j + 1;
                            }
                            else if (j < toTest.length() - 5 && toTest.charAt(j) == '&' && toTest.charAt(j + 1) == 'a' && toTest.charAt(j + 2) == 'm' && toTest.charAt(j + 3) == 'p' && toTest.charAt(j + 4) == ';') {
                                toWrite += "&";
                                j = j + 4;
                            }
                            else if (j < toTest.length() - 4 && toTest.charAt(j) == '&' && toTest.charAt(j + 1) == 'l' && toTest.charAt(j + 2) == 't' && toTest.charAt(j + 3) == ';') {
                                toWrite += "<";
                                j = j + 3;
                            }
                            else if (j < toTest.length() - 4 && toTest.charAt(j) == '&' && toTest.charAt(j + 1) == 'g' && toTest.charAt(j + 2) == 't' && toTest.charAt(j + 3) == ';') {
                                toWrite += ">";
                                j = j + 3;
                            }
                            else {
                                toWrite += toTest.charAt(j);
                            }
                        }
                        if (ra.size() == 0) {
                            ra.add(new lyric(Long.parseLong(time) + 200, toWrite));
                        }
                        else {
                            ra.add(new lyric(Long.parseLong(time), toWrite));
                        }
                        Long test = Long.parseLong(time);
                        if (debug) {
                            logger.log(ra.get(ra.size() - 1) + "    my ulong is: " + test);
                        }
                    }

                }
                br.close();
                DefaultListModel ls = new DefaultListModel();

                String newText = "";
                for (int i = 0; i < ra.size(); i++) {

                    ls.addElement(ra.get(i));
                    newText += " " + ra.get(i).getS();
                }
                this.jTextPane1.setText(newText);
                this.jList2.setModel(ls);
                if (ra.size() > 0) {
                    jList2.setSelectedIndex(0);
                }
                jList2.removeKeyListener(jList2.getKeyListeners()[0]);
                //jList2.addKeyListener(kl);
                jList2.repaint();
            }
            catch (Exception e) {
                textpath = "notset";
                JOptionPane.showMessageDialog(this, "Could not read text file, please copy paste it.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                if (debug) {
                    Writer writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(writer);
                    e.printStackTrace(printWriter);
                    String output = writer.toString();
                    logger.log(output);
                }

            }

        }

        this.setTitle(title + "- Working on " + module);
        this.setLocation((int) Math.round((width2 / 2) - (this.getWidth() / 2)), (int) Math.round((height2 / 2) - (this.getHeight() / 2)));
        ImageIcon imageIcon = new ImageIcon(bfimg);

        JLabel label1 = new JLabel(imageIcon);
        this.getContentPane().add(label1);
        label1.setName(image);
        label1.validate();

        this.jScrollPane1.setName("waveForm " + image);
        this.jScrollPane1.add(label1);
        this.jScrollPane1.validate();

        this.jScrollPane1.validate();

        this.jScrollPane1.setViewportView(this.jScrollPane1.getComponent(this.jScrollPane1.getComponentCount() - 1));
        this.jScrollPane1.getComponent(this.jScrollPane1.getComponentCount() - 1).setSize(1024, 768);
        this.jScrollPane1.validate();
        this.jScrollPane1.repaint();

        this.validate();

        this.validate();
        this.repaint();

        this.jTextPane1.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                if (debug) {
                    logger.log("Insert!!!");
                }
                hasChanges = true;

                if (!ra.isEmpty()) {
                    updateLyricsAdd(e);
                }
                else {
                    updateLyrics();
                }

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (debug) {
                    logger.log("Remove!!!");
                }
                hasChanges = true;
                updateLyricsRemove2(e);

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

                try {
                    if (debug) {
                        logger.log("Change update! " + e.getOffset() + " Length of it is " + e.getLength() + " kai mallon prose8esa to " + jTextPane1.getText(e.getOffset(), e.getLength()));
                    }
                }
                catch (BadLocationException ex) {
                    Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
        );
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {

                if (hasChanges) {
                    JOptionPane warning = new JOptionPane();
                    int n = warning.showConfirmDialog(self, "There are unsaved changes! do you want to save?", "Warning",
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (n == JOptionPane.NO_OPTION) {
                        awc.closeStreams();

                        if (trueClose) {

                            System.exit(0);
                            self.dispose();
                        }

                    }
                    if (n == JOptionPane.YES_OPTION) {
                        String soundName = awc.getFileName();
                        if (debug) {
                            logger.log(soundName);
                        }
                        String Path = "";
                        String tempHolder = "";
                        String fileName = "";
                        StringTokenizer st = new StringTokenizer(soundName, "\\");
                        while (st.hasMoreElements()) {
                            tempHolder = st.nextToken();
                            if (st.hasMoreTokens()) {
                                Path += tempHolder + "\\";
                            }
                            else {
                                fileName = tempHolder;
                            }
                        }

                        String fileName2 = "";
                        st = new StringTokenizer(fileName, ".");
                        while (st.hasMoreElements()) {
                            tempHolder = st.nextToken();
                            if (st.hasMoreTokens()) {
                                fileName2 += tempHolder;
                            }
                        }
                        File file = new File(fileName2 + ".xml");

                        int returnVal = 0;

                        final JFileChooser fc = new JFileChooser();

                        fc.setDialogTitle("Save as XML");
                        fc.setCurrentDirectory(new File("C:\\Users\\Basil\\Desktop\\sports\\audio\\" + fileName2 + ".xml"));
                        fc.setSelectedFile(file);
                        fc.setFileFilter(new XmlFilter());
                        fc.setAcceptAllFileFilterUsed(false);
                        fc.requestFocus();
                        returnVal = fc.showSaveDialog(self);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            file = new File(fc.getSelectedFile().getAbsolutePath());

                        }

                        if (!(returnVal == JFileChooser.APPROVE_OPTION)) {
                            return;
                        }

                        try {
                            PrintWriter writer = new PrintWriter(file, "ISO-8859-1");
                            writer.print("<lyrics>\n");
                            for (int i = 0; i < ra.size(); i++) {
                                writer.print("<lyric text=\"" + ra.get(i).getS() + "\" time=\"" + ra.get(i).getTime() + "\"/>\n");
                            }
                            writer.print("</lyrics>");
                            writer.close();

                        }
                        catch (FileNotFoundException ex) {
                            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        catch (UnsupportedEncodingException ex) {
                            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    if (n == JOptionPane.CANCEL_OPTION) {

                        return;
                    }
                }
                if ((!hasChanges && (!carryOn || trueClose))) {
                    awc.closeStreams();
                    self.dispose();
                    System.exit(0);
                }

                if (carryOn) {
                    // contr.createNewPanel(self);
                    contr.createNewPanel((mainPanel) self);

                }
                else {
                    trueClose = true;
                }

            }

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
        this.jScrollPane1.getViewport().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                logger.log("The view port has changed to " + jScrollPane1.getViewport().getViewPosition() + " while markerPosition is " + markerPosition);
            }
        });
        this.setVisible(true);
        s.setVisible(false);
        s.dispose();
        this.jScrollPane1.setFocusable(true);

        this.jScrollPane1.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!play && e.getClickCount() > 1) {
                    tempMarker = markerPosition;
                    playSound();
                    play = true;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                jScrollPane1.requestFocus();
                int x = e.getXOnScreen() - getLocationOnScreen().x - 21 + jScrollPane1.getHorizontalScrollBar().getValue();
                int y = e.getYOnScreen();
                markerPosition = x;
                if (debug) {
                    logger.log("Scroll bar: " + jScrollPane1.getHorizontalScrollBar().getValue() + " Click: " + e.getXOnScreen() + " Window Location: " + getLocationOnScreen().x + " Final X (-4): " + x);
                    logger.log(e.getClickCount() + " clicks on this event");
                }
                if (play) {
                    stopSound();
                    play = false;

                    playSound();
                    play = true;
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                jScrollPane1.requestFocus();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        jList2.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

                JList list = (JList) e.getSource();
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    int index = list.getSelectedIndex();
                    long ms = ra.get(index).getTime();
                    if (ms != -1) {
                        markerPosition = (int) (pixelperms * ms);
                        tempMarker = markerPosition;
                        jLabel3.setText("Word:" + (jList2.getSelectedIndex() + 1) + "/" + jList2.getModel().getSize());
                        jLabel3.repaint();
                        if (play) {
                            stopSound();
                            playSound();
                        }
                    }
                }

                if (e.getButton() == MouseEvent.BUTTON3) {
                    frommouse = true;
                    int i = list.locationToIndex(e.getPoint());

                    list.setSelectedIndex(i);
                    int index = list.getSelectedIndex();
                    JPopupMenu popup = new JPopupMenu();
                    JMenuItem item1 = new JMenuItem("Edit Text");
                    popup.add(item1);
                    JMenuItem item2 = new JMenuItem("Remove Word");
                    popup.add(item2);

                    item1.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int index = jList2.getSelectedIndex();
                            String change = (String) JOptionPane.showInputDialog(self, "Editing text for " + ra.get(jList2.getSelectedIndex()).getS(), ra.get(jList2.getSelectedIndex()).getS());
                            if (!(change == null) && !change.isEmpty()) {

                                ArrayList<String> changes = new <String>ArrayList();
                                ArrayList<lyric> newlist = new <lyric>ArrayList();
                                StringTokenizer st = new StringTokenizer(change, " ");
                                while (st.hasMoreTokens()) {
                                    changes.add(st.nextToken());
                                }
                                DefaultListModel ls = new DefaultListModel();
                                for (int i = 0; i < jList2.getSelectedIndex(); i++) {
                                    ls.addElement(ra.get(i));
                                    newlist.add(ra.get(i));
                                }
                                ls.addElement(new lyric(ra.get(jList2.getSelectedIndex()).getTime(), changes.get(0)));
                                newlist.add(new lyric(ra.get(jList2.getSelectedIndex()).getTime(), changes.get(0)));
                                for (int i = 1; i < changes.size(); i++) {
                                    ls.addElement(new lyric(-1, changes.get(i)));
                                    newlist.add(new lyric(-1, changes.get(i)));
                                }
                                for (int i = ls.getSize() - changes.size() + 1; i < ra.size(); i++) {
                                    ls.addElement(ra.get(i));
                                    newlist.add(ra.get(i));
                                }
                                ra = newlist;
                                jList2.setModel(ls);
                                jList2.repaint();
                                String text = "";
                                for (int i = 0; i < ra.size(); i++) {
                                    text += " " + ra.get(i).getS();
                                }
                                jTextPane1.setText(text);
                            }

                        }
                    });
                    item2.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int index = jList2.getSelectedIndex();
                            int returnval = JOptionPane.showConfirmDialog(self, "Are you sure you want to remove " + ra.get(jList2.getSelectedIndex()).getS());

                            if (returnval == JOptionPane.OK_OPTION) {
                                ra.remove(jList2.getSelectedIndex());

                                DefaultListModel ls = new DefaultListModel();
                                for (int i = 0; i < ra.size(); i++) {
                                    ls.addElement(ra.get(i));

                                }

                                jList2.setModel(ls);
                                jList2.setSelectedIndex(index);
                                jList2.repaint();
                                String text = "";
                                for (int i = 0; i < ra.size(); i++) {
                                    text += " " + ra.get(i).getS();
                                }
                                jTextPane1.setText(text);
                            }

                        }
                    });
                    //popup.setLocation(e.getPoint());
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        jCheckBox1.addKeyListener(kl);
        this.initiateMemoryLog();
        markImage();
        initiateLyricDaemon();
        playSound();
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while (line == null) {
                        Thread.sleep(10);
                    }
                }
                catch (InterruptedException ex) {
                    Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                stopSound();
                markerPosition = 0;
            }
        });
        t.run();

        tempText = this.jTextPane1.getText();
        kl = initializeKL();
        tkl = new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                    jScrollPane1.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        this.jSlider1.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                jLabel5.setText(jSlider1.getValue() + "ms");
            }
        });
        jLabel5.setText("0 ms");
        this.jSlider1.addKeyListener(kl);
        this.jList2.addKeyListener(kl);
        this.jTextPane1.addKeyListener(tkl);

        this.jScrollPane1.addKeyListener(kl);
        for (int i = 0; i < this.jList2.getKeyListeners().length; i++) {
            this.jList2.removeKeyListener(this.jList2.getKeyListeners()[i]);
        }
        move.setPriority(Thread.MAX_PRIORITY);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.jScrollPane1.requestFocus();
    }

    public mainPanel(Control control, String audio, String textarg, boolean debuga, boolean memoryFlaga) {
        memoryFlag = memoryFlaga;
        splashScreen s = new gui.splashScreen();

        this.debug = debuga;
        logger = new logger();
        if (!debug) {

            logger.setVisible(false);

        }
        else {
            logger.setVisible(true);
        }
        logger.log("Ο ορισμος του καραόκε κυρία μου!");
        if (memoryFlaga) {
            logger.log("Memory awareness edition");
        }
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());

        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex) {
            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex) {
            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        File file = null;

        this.contr = control;
        Dimension screenSize2 = Toolkit.getDefaultToolkit().getScreenSize();
        double width2 = screenSize2.getWidth();
        double height2 = screenSize2.getHeight();
        if (audio.equals("none") || !(new File(audio).exists())) {
            final JFileChooser fcm = new JFileChooser();

            fcm.setDialogTitle("Select your audio");
            s.setFlavourText("Select your audio");
            logger.log("Select your audio");
            fcm.setFileFilter(new Mp3Filter());
            fcm.setAcceptAllFileFilterUsed(false);
            fcm.requestFocus();
            int returnVal = fcm.showOpenDialog(s);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fcm.getSelectedFile();
                module = file.getName();
                audiopath = file.getParent();
            }

            if (!(returnVal == JFileChooser.APPROVE_OPTION)) {
                exit(-1);
            }
        }
        else {
            file = new File(audio);
            module = file.getName();
            audiopath = file.getParent();
        }
        try {
            JFrame wait = new JFrame();
            s.setFlavourText("Analysing " + file.getName() + " now");
            logger.log("Analysing " + file.getName() + " now");
            JLabel pw = new JLabel("Analysing the mp3 file now");

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            double width = screenSize.getWidth();
            double height = screenSize.getHeight();
            wait.setLocation(new Point((int) Math.round((width - 200) / 2), (int) Math.round((height - 100) / 2)));
            wait.setSize(200, 100);
            wait.getContentPane().add(pw);
            pw.setName("wait");

            /* wait.getContentPane().getComponent(wait.getContentPane().getComponentCount() - 1).setLocation(new Point(23541325, 50));
             //wait.setUndecorated(true);
             wait.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
             wait.setTitle("Please wait");
             wait.setBackground(Color.gray);
             wait.validate();
             wait.repaint();
             wait.setVisible(true);
             wait.repaint();
             wait.revalidate();*/
            awc = new AudioWaveformCreator(file.getAbsolutePath());

            awc.createAudioInputStream();

            image = awc.getImage();
            bfimg = ImageIO.read(new File(image));
            String soundPath = awc.getDecodedPath();
            toBeStreamed = new File(soundPath);
            sound = AudioSystem.getAudioInputStream(toBeStreamed);
            wait.setVisible(false);
        }
        catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Something went Terribly wrong! Please contact and administrator if this error persists! " + e.getStackTrace(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            if (debug) {
                Writer writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                e.printStackTrace(printWriter);
                String output = writer.toString();
                logger.log(output);
            }
            System.exit(-1);
        }

        ra = new ArrayList<lyric>();

        File text = null;
        String everything = "";
        initComponents();
        String ext = "";
        if (textarg.equalsIgnoreCase("none") || !(new File(textarg).exists())) {
            final JFileChooser fc = new JFileChooser();

            fc.setDialogTitle("Select your text");
            s.setFlavourText("Select your text");
            logger.log("Select your text");
            fc.setCurrentDirectory(new File(audiopath + "\\.."));
            fc.setFileFilter(new XmlFilter());
            fc.setFileFilter(new TxtFilter());
            fc.setFileFilter(new xmlandtextfilter());

            fc.setAcceptAllFileFilterUsed(false);
            fc.requestFocus();
            int returnVal2 = fc.showOpenDialog(s);

            boolean show = false;
            if (returnVal2 == JFileChooser.APPROVE_OPTION) {
                text = fc.getSelectedFile();
                textpath = text.getParent();
                StringTokenizer st = new StringTokenizer(text.getName(), ".");
                ext = "";
                while (st.hasMoreElements()) {
                    ext = st.nextToken();

                }

            }
            if (!(returnVal2 == JFileChooser.APPROVE_OPTION)) {
                JOptionPane.showMessageDialog(this, "Since you didn't open a text file, you'll have to copy paste the text", "Warning",
                        JOptionPane.ERROR_MESSAGE);

                textpath = "notset";
            }
        }
        else {
            text = new File(textarg);
            textpath = text.getParent();
            StringTokenizer st = new StringTokenizer(text.getName(), ".");
            ext = "";
            while (st.hasMoreElements()) {
                ext = st.nextToken();

            }

        }

        if (ext.equals("txt")) {
            s.setFlavourText("Reading " + text.getName());
            logger.log("Reading " + text.getName());
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(text), "ISO-8859-1"));

                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append('\n');
                    line = br.readLine();
                }
                everything = sb.toString();
                this.jTextPane1.setText(everything);
                updateLyrics();
            }
            catch (Exception e) {
                textpath = "notset";
                JOptionPane.showMessageDialog(this, "Could not read text file, please copy paste it.", "Warning",
                        JOptionPane.WARNING_MESSAGE);

            }
        }
        else if (ext.equals("xml")) {
            s.setFlavourText("Reading " + text.getName());
            logger.log("Reading " + text.getName());
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(text), "ISO-8859-1"));
                String buffer = "";
                ra.clear();
                StringTokenizer sa = new StringTokenizer(buffer);
                while ((buffer = br.readLine()) != null) {

                    if (buffer.contains("<lyric text=\"")) {
                        while (Character.isWhitespace(buffer.charAt(0))) {
                            buffer = buffer.substring(1);
                        }
                        if (debug) {
                            logger.log(buffer);
                        }
                        buffer = buffer.substring(13);
                        sa = new StringTokenizer(buffer, "\"");
                        String lyric = sa.nextToken();
                        String skip = sa.nextToken();
                        while (!skip.contains("time=")) {
                            if (debug) {
                                logger.log(skip);
                            }
                            skip = sa.nextToken();
                        }

                        String time = sa.nextToken();

                        String toTest = lyric;
                        String toWrite = "";
                        try {

                            Pattern p = Pattern.compile(".*&#([0-9]+);.*");
                            Matcher m = p.matcher(toTest);
                            while (m.find()) {
                                String tempLyr = toTest.substring(0, toTest.indexOf("&#"));
                                String number = m.group(1);
//              System.out.println("The first substring is  "+tempLyr);
                                Integer numberint = Integer.parseInt(number);
                                int pureint = numberint.intValue();
                                char[] resolve = Character.toChars(pureint);
                                tempLyr += resolve[0];
                                tempLyr += toTest.substring(toTest.indexOf("&#") + m.group(1).length() + 3);
                                toTest = tempLyr;
                                System.out.println(tempLyr);
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        for (int j = 0; j < toTest.length(); j++) {
                            if (j < toTest.length() - 2 && toTest.charAt(j) == '\'' && toTest.charAt(j + 1) == '\'') {
                                toWrite += "\"";
                                j = j + 1;
                            }
                            else if (j < toTest.length() - 5 && toTest.charAt(j) == '&' && toTest.charAt(j + 1) == 'a' && toTest.charAt(j + 2) == 'm' && toTest.charAt(j + 3) == 'p' && toTest.charAt(j + 4) == ';') {
                                toWrite += "&";
                                j = j + 4;
                            }
                            else if (j < toTest.length() - 4 && toTest.charAt(j) == '&' && toTest.charAt(j + 1) == 'l' && toTest.charAt(j + 2) == 't' && toTest.charAt(j + 3) == ';') {
                                toWrite += "<";
                                j = j + 3;
                            }
                            else if (j < toTest.length() - 4 && toTest.charAt(j) == '&' && toTest.charAt(j + 1) == 'g' && toTest.charAt(j + 2) == 't' && toTest.charAt(j + 3) == ';') {
                                toWrite += ">";
                                j = j + 3;
                            }
                            else {
                                toWrite += toTest.charAt(j);
                            }
                        }
                        if (ra.size() == 0) {
                            ra.add(new lyric(Long.parseLong(time) + 200, toWrite));
                        }
                        else {
                            ra.add(new lyric(Long.parseLong(time), toWrite));
                        }
                        Long test = Long.parseLong(time);

                        if (debug) {
                            logger.log(ra.get(ra.size() - 1) + "    my ulong is: " + test);
                        }
                    }

                }
                br.close();
                DefaultListModel ls = new DefaultListModel();

                String newText = "";
                for (int i = 0; i < ra.size(); i++) {

                    ls.addElement(ra.get(i));
                    newText += " " + ra.get(i).getS();
                }
                this.jTextPane1.setText(newText);
                this.jList2.setModel(ls);
                if (ra.size() > 0) {
                    jList2.setSelectedIndex(0);
                }
                jList2.removeKeyListener(jList2.getKeyListeners()[0]);
                //jList2.addKeyListener(kl);
                jList2.repaint();
            }
            catch (Exception e) {
                textpath = "notset";
                JOptionPane.showMessageDialog(this, "Could not read text file, please copy paste it.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                if (debug) {
                    Writer writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(writer);
                    e.printStackTrace(printWriter);
                    String output = writer.toString();
                    logger.log(output);
                }

            }

        }

        this.setTitle(title + "- Working on " + module);
        this.setLocation((int) Math.round((width2 / 2) - (this.getWidth() / 2)), (int) Math.round((height2 / 2) - (this.getHeight() / 2)));
        ImageIcon imageIcon = new ImageIcon(bfimg);

        JLabel label1 = new JLabel(imageIcon);
        this.getContentPane().add(label1);
        label1.setName(image);
        label1.validate();

        this.jScrollPane1.setName("waveForm " + image);
        this.jScrollPane1.add(label1);
        this.jScrollPane1.validate();

        this.jScrollPane1.validate();

        this.jScrollPane1.setViewportView(this.jScrollPane1.getComponent(this.jScrollPane1.getComponentCount() - 1));
        this.jScrollPane1.getComponent(this.jScrollPane1.getComponentCount() - 1).setSize(1024, 768);
        this.jScrollPane1.validate();
        this.jScrollPane1.repaint();

        this.validate();

        this.validate();
        this.repaint();

        this.jTextPane1.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                if (debug) {
                    logger.log("Insert!!!");
                }
                hasChanges = true;

                if (!ra.isEmpty()) {
                    updateLyricsAdd(e);
                }
                else {
                    updateLyrics();
                }

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (debug) {
                    logger.log("Remove!!!");
                }
                hasChanges = true;
                updateLyricsRemove2(e);

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

                try {
                    if (debug) {
                        logger.log("Change update! " + e.getOffset() + " Length of it is " + e.getLength() + " kai mallon prose8esa to " + jTextPane1.getText(e.getOffset(), e.getLength()));
                    }
                }
                catch (BadLocationException ex) {
                    Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
        );
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {

                if (hasChanges) {
                    JOptionPane warning = new JOptionPane();
                    int n = warning.showConfirmDialog(self, "There are unsaved changes! do you want to save?", "Warning",
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (n == JOptionPane.NO_OPTION) {
                        awc.closeStreams();

                        if (trueClose) {

                            System.exit(0);
                            self.dispose();
                        }

                    }
                    if (n == JOptionPane.YES_OPTION) {
                        String soundName = awc.getFileName();
                        if (debug) {
                            logger.log(soundName);
                        }
                        String Path = "";
                        String tempHolder = "";
                        String fileName = "";
                        StringTokenizer st = new StringTokenizer(soundName, "\\");
                        while (st.hasMoreElements()) {
                            tempHolder = st.nextToken();
                            if (st.hasMoreTokens()) {
                                Path += tempHolder + "\\";
                            }
                            else {
                                fileName = tempHolder;
                            }
                        }

                        String fileName2 = "";
                        st = new StringTokenizer(fileName, ".");
                        while (st.hasMoreElements()) {
                            tempHolder = st.nextToken();
                            if (st.hasMoreTokens()) {
                                fileName2 += tempHolder;
                            }
                        }
                        File file = new File(fileName2 + ".xml");

                        int returnVal = 0;

                        final JFileChooser fc = new JFileChooser();

                        fc.setDialogTitle("Save as XML");
                        fc.setCurrentDirectory(new File("C:\\Users\\Basil\\Desktop\\sports\\audio\\" + fileName2 + ".xml"));
                        fc.setSelectedFile(file);
                        fc.setFileFilter(new XmlFilter());
                        fc.setAcceptAllFileFilterUsed(false);
                        fc.requestFocus();
                        returnVal = fc.showSaveDialog(self);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            file = new File(fc.getSelectedFile().getAbsolutePath());

                        }

                        if (!(returnVal == JFileChooser.APPROVE_OPTION)) {
                            return;
                        }

                        try {
                            PrintWriter writer = new PrintWriter(file, "ISO-8859-1");
                            writer.print("<lyrics>\n");
                            for (int i = 0; i < ra.size(); i++) {
                                writer.print("<lyric text=\"" + ra.get(i).getS() + "\" time=\"" + ra.get(i).getTime() + "\"/>\n");
                            }
                            writer.print("</lyrics>");
                            writer.close();

                        }
                        catch (FileNotFoundException ex) {
                            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        catch (UnsupportedEncodingException ex) {
                            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    if (n == JOptionPane.CANCEL_OPTION) {

                        return;
                    }
                }
                if ((!hasChanges && (!carryOn || trueClose))) {
                    awc.closeStreams();
                    self.dispose();
                    File tempdir = new File(System.getProperty("java.io.tmpdir") + "/karaok");
                    File temps[] = tempdir.listFiles();
                    for (File tempfile : temps) {
                        System.out.print(tempfile.getAbsolutePath());
                        Boolean did = tempfile.delete();
                        System.out.println(" deleted: " + did);
                    }
                    System.exit(0);
                }

                if (carryOn) {
                    // contr.createNewPanel(self);
                    contr.createNewPanel((mainPanel) self);

                }
                else {
                    trueClose = true;
                }

            }

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        s.setVisible(false);
        this.setVisible(true);
        s.dispose();
        this.jScrollPane1.setFocusable(true);

        this.jScrollPane1.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!play && e.getClickCount() > 1) {
                    tempMarker = markerPosition;
                    playSound();
                    play = true;
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
                jScrollPane1.requestFocus();
                int x = e.getXOnScreen() - getLocationOnScreen().x - 21 + jScrollPane1.getHorizontalScrollBar().getValue();
                int y = e.getYOnScreen();
                markerPosition = x;
                if (debug) {
                    logger.log("Scroll bar: " + jScrollPane1.getHorizontalScrollBar().getValue() + " Click: " + e.getXOnScreen() + " Window Location: " + getLocationOnScreen().x + " Final X (-4): " + x);
                }

                if (play) {
                    stopSound();
                    play = false;

                    playSound();
                    play = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                jScrollPane1.requestFocus();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        jList2.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

                JList list = (JList) e.getSource();
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    int index = list.getSelectedIndex();
                    long ms = ra.get(index).getTime();
                    if (ms != -1) {
                        markerPosition = (int) (pixelperms * ms);
                        tempMarker = markerPosition;
                        jLabel3.setText("Word:" + (jList2.getSelectedIndex() + 1) + "/" + jList2.getModel().getSize());
                        jLabel3.repaint();
                        if (play) {
                            stopSound();
                            playSound();
                        }
                    }
                }

                if (e.getButton() == MouseEvent.BUTTON3) {
                    frommouse = true;
                    int i = list.locationToIndex(e.getPoint());

                    list.setSelectedIndex(i);
                    int index = list.getSelectedIndex();
                    JPopupMenu popup = new JPopupMenu();
                    JMenuItem item1 = new JMenuItem("Edit Text");
                    popup.add(item1);
                    JMenuItem item2 = new JMenuItem("Remove Word");
                    popup.add(item2);

                    item1.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int index = jList2.getSelectedIndex();
                            String change = (String) JOptionPane.showInputDialog(self, "Editing text for " + ra.get(jList2.getSelectedIndex()).getS(), ra.get(jList2.getSelectedIndex()).getS());
                            if (!(change == null) && !change.isEmpty()) {

                                ArrayList<String> changes = new <String>ArrayList();
                                ArrayList<lyric> newlist = new <lyric>ArrayList();
                                StringTokenizer st = new StringTokenizer(change, " ");
                                while (st.hasMoreTokens()) {
                                    changes.add(st.nextToken());
                                }
                                DefaultListModel ls = new DefaultListModel();
                                for (int i = 0; i < jList2.getSelectedIndex(); i++) {
                                    ls.addElement(ra.get(i));
                                    newlist.add(ra.get(i));
                                }
                                ls.addElement(new lyric(ra.get(jList2.getSelectedIndex()).getTime(), changes.get(0)));
                                newlist.add(new lyric(ra.get(jList2.getSelectedIndex()).getTime(), changes.get(0)));
                                for (int i = 1; i < changes.size(); i++) {
                                    ls.addElement(new lyric(-1, changes.get(i)));
                                    newlist.add(new lyric(-1, changes.get(i)));
                                }
                                for (int i = ls.getSize() - changes.size() + 1; i < ra.size(); i++) {
                                    ls.addElement(ra.get(i));
                                    newlist.add(ra.get(i));
                                }
                                ra = newlist;
                                jList2.setModel(ls);
                                jList2.repaint();
                                String text = "";
                                for (int i = 0; i < ra.size(); i++) {
                                    text += " " + ra.get(i).getS();
                                }
                                jTextPane1.setText(text);
                            }
                            jList2.setSelectedIndex(index);
                        }
                    });
                    item2.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int returnval = JOptionPane.showConfirmDialog(self, "Are you sure you want to remove " + ra.get(jList2.getSelectedIndex()).getS());

                            if (returnval == JOptionPane.OK_OPTION) {
                                ra.remove(jList2.getSelectedIndex());

                                DefaultListModel ls = new DefaultListModel();
                                for (int i = 0; i < ra.size(); i++) {
                                    ls.addElement(ra.get(i));

                                }

                                jList2.setModel(ls);
                                jList2.repaint();
                                String text = "";
                                for (int i = 0; i < ra.size(); i++) {
                                    text += " " + ra.get(i).getS();
                                }
                                jTextPane1.setText(text);
                            }

                        }
                    });
                    //popup.setLocation(e.getPoint());
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        jList2.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

                JList list = (JList) e.getSource();
                if (e.getClickCount() == 2) {
                    int index = list.getSelectedIndex();
                    long ms = ra.get(index).getTime();
                    if (ms != -1) {
                        markerPosition = (int) (pixelperms * ms);
                        tempMarker = markerPosition;
                        jLabel3.setText("Word:" + (jList2.getSelectedIndex() + 1) + "/" + jList2.getModel().getSize());
                        jLabel3.repaint();
                        if (play) {
                            stopSound();
                            playSound();
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        jCheckBox1.addKeyListener(kl);
        this.initiateMemoryLog();
        markImage();
        initiateLyricDaemon();
        playSound();
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while (line == null) {
                        Thread.sleep(10);
                    }
                }
                catch (InterruptedException ex) {
                    Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                stopSound();
                markerPosition = 0;
            }
        });
        t.run();
        tempText = this.jTextPane1.getText();
        kl = initializeKL();
        tkl = new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                    jScrollPane1.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        this.jSlider1.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                jLabel5.setText(jSlider1.getValue() + " ms");
            }
        });
        jLabel5.setText("0 ms");
        this.jSlider1.addKeyListener(kl);
        this.jList2.addKeyListener(kl);
        this.jTextPane1.addKeyListener(tkl);

        this.jScrollPane1.addKeyListener(kl);
        for (int i = 0; i < this.jList2.getKeyListeners().length; i++) {
            this.jList2.removeKeyListener(this.jList2.getKeyListeners()[i]);
        }
        move.setPriority(Thread.MAX_PRIORITY);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.jScrollPane1.requestFocus();
    }

    public KeyListener initializeKL() {
        kl = new KeyListener() {
            int last = -1;

            @Override
            public void keyTyped(KeyEvent e) {
                if (last == KeyEvent.VK_A && play == false) {
                    //logger.log("DOWN fired");
                    last = e.getKeyCode();
                    markerPosition--;
                    if (markerPosition < 0) {
                        markerPosition = 0;
                    }
                    //move.interrupt();
                    jScrollPane1.requestFocus();
                    return;

                }

//        if (last == KeyEvent.VK_S) {
//          logger.log("FIRE SCROLL from the last!");
//          last = e.getKeyCode();
//          boolean flag = true;
//
//          if (jCheckBox1.isSelected() && flag) {
//
//            jCheckBox1.setSelected(false);
//            flag = false;
//            return;
//          }
//          if (!jCheckBox1.isSelected() && flag) {
//            flag = false;
//            jCheckBox1.setSelected(true);
//          }
//
//          if (jCheckBox1.isSelected()) {
//            wordscroll = true;
//            jScrollPane1.requestFocus();
//          } else {
//            wordscroll = false;
//            jScrollPane1.requestFocus();
//          }
//          jCheckBox1.repaint();
//        }
                if (last == KeyEvent.VK_D && play == false) {
                    last = e.getKeyCode();
                    //markerPosition++;
                    if (markerPosition < bfimg.getWidth()) {
                        markerPosition++;

                    }

                    jScrollPane1.requestFocus();
                }

                if (last == KeyEvent.VK_A && play == true) {
                    last = e.getKeyCode();
                    markerPosition -= 50;
                    if (markerPosition < 0) {
                        markerPosition = 0;
                    }

                    stopSound();
                    playSound();
                    jScrollPane1.requestFocus();

                }

                if (last == KeyEvent.VK_D && play == true) {
                    last = e.getKeyCode();

                    if (markerPosition + 50 > bfimg.getWidth()) {
                        markerPosition = bfimg.getWidth();

                    }
                    else {
                        markerPosition += 50;
                    }

                    stopSound();
                    playSound();
                    jScrollPane1.requestFocus();
                }

                if (last == KeyEvent.VK_NUMPAD4 && play == false) {
                    //logger.log("DOWN fired");
                    last = e.getKeyCode();
                    markerPosition--;
                    if (markerPosition < 0) {
                        markerPosition = 0;
                    }

                    jScrollPane1.requestFocus();

                }

                if (last == KeyEvent.VK_NUMPAD6 && play == false) {
                    last = e.getKeyCode();

                    if (markerPosition < bfimg.getWidth()) {
                        markerPosition++;

                    }
                    jScrollPane1.requestFocus();

                }

                if (last == KeyEvent.VK_NUMPAD4 && play == true) {
                    last = e.getKeyCode();
                    markerPosition -= 50;
                    if (markerPosition < 0) {
                        markerPosition = 0;
                    }

                    jScrollPane1.requestFocus();
                    stopSound();
                    playSound();

                }

                if (last == KeyEvent.VK_NUMPAD6 && play == true) {
                    last = e.getKeyCode();

                    if (markerPosition + 50 > bfimg.getWidth()) {
                        markerPosition = bfimg.getWidth();

                    }
                    else {
                        markerPosition += 50;
                    }

                    jScrollPane1.requestFocus();
                    stopSound();
                    playSound();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                float quantum = awc.getDurationInSeconds() * 1000 / bfimg.getWidth();

                if (e.getKeyChar() == ' ' && play == false) {
                    last = e.getKeyCode();
                    play = true;
                    tempMarker = markerPosition;
                    activeLyric = jList2.getSelectedIndex();
                    logger.log("view port is at " + jScrollPane1.getViewport().getViewPosition() + " while markerPosition is " + markerPosition + " and width is " + jScrollPane1.getWidth());

                    if (jScrollPane1.getViewport().getViewPosition().x > markerPosition || jScrollPane1.getViewport().getViewPosition().x < (markerPosition - jScrollPane1.getWidth() - 10)) {
                        jScrollPane1.getViewport().setViewPosition(new Point(markerPosition, 0));
                        logger.log("position was not visible, moved to " + jScrollPane1.getViewport().getViewPosition() + "marker point was " + markerPosition);
                    }
                    //move.interrupt();
                    jScrollPane1.requestFocus();
                    playSound();
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_S && !(e.getModifiers() == KeyEvent.CTRL_MASK)) {
                    if (debug) {
                        logger.log("FIRE SCROLL from the e.keycode!");
                    }
                    last = e.getKeyCode();
                    boolean flag = true;

                    jCheckBox1.setSelected(!jCheckBox1.isSelected());

                    if (jCheckBox1.isSelected()) {
                        wordscroll = true;
                        jScrollPane1.requestFocus();
                    }
                    else {
                        wordscroll = false;
                        jScrollPane1.requestFocus();
                    }
                    jCheckBox1.repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_E && play == false) {

                    if (System.currentTimeMillis() - laste > 600) {
                        laste = System.currentTimeMillis();
                        hasChanges = true;
                        last = e.getKeyCode();
                        int tempindex = jList2.getSelectedIndex() + 1;

                        ra.get(jList2.getSelectedIndex()).setTime(Math.round(quantum) * markerPosition);

                        DefaultListModel ls = new DefaultListModel();

                        for (int i = 0; i < ra.size(); i++) {

                            ls.addElement(ra.get(i));

                        }

                        jList2.setModel(ls);
                        if (ra.size() > 0) {
                            jList2.setSelectedIndex(0);
                        }
                        jList2.repaint();
                        jList2.setSelectedIndex(tempindex);
                        int lastVisible = jList2.getLastVisibleIndex();
                        int firstVisible = jList2.getFirstVisibleIndex();
                        int lastSelected = tempindex;

                        //if(debug){logger.log("last Selected - First Visible = "+lastSelected+"-"+firstVisible+"="+(lastSelected-firstVisible)+" and totalVisible is "+lastVisible+" and the quarter rounded is "+((int)(totalVisible/4))+"and their subtraction is "+(totalVisible-(int)(totalVisible/4)));}
                        if (lastSelected - lastVisible < 5) {
                            jList2.ensureIndexIsVisible(lastSelected + 10);
                            //if(debug){logger.log("i should have moved now to "+(lastSelected+(int)(totalVisible/4)));}
                            jList2.setSelectedIndex(tempindex);

                            jLabel3.setText("Word:" + (tempindex + 1) + "/" + jList2.getModel().getSize());
                            jLabel3.repaint();
                        }
                        else {
                            jList2.ensureIndexIsVisible(tempindex);
                            jList2.setSelectedIndex(tempindex);

                            jLabel3.setText("Word:" + (tempindex) + "/" + jList2.getModel().getSize());
                            jLabel3.repaint();
                        }
                        jList2.ensureIndexIsVisible(jList2.getSelectedIndex());
                        jList2.removeKeyListener(jList2.getKeyListeners()[0]);
                        jList2.addKeyListener(kl);

                        jScrollPane1.requestFocus();
                        jLabel3.setText("Word:" + (jList2.getSelectedIndex() + 1) + "/" + jList2.getModel().getSize());
                        jLabel3.repaint();
                        return;
                    }
                    else {
                        Toolkit.getDefaultToolkit().beep();
                    }
                }

                if (e.getKeyChar() == ' ' && play == true) {
                    last = e.getKeyCode();

                    stopSound();

                    //imageThread.stop();
                    play = false;
                    markerPosition = tempMarker;
                    if (jScrollPane1.getViewport().getViewPosition().x > markerPosition || jScrollPane1.getViewport().getViewPosition().x < (markerPosition - jScrollPane1.getWidth() - 10)) {
                        jScrollPane1.getViewport().setViewPosition(new Point(markerPosition, 0));
                    }
                    jScrollPane1.requestFocus();

                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_A && play == false) {
                    last = e.getKeyCode();
                    markerPosition--;
                    if (markerPosition < 0) {
                        markerPosition = 0;
                    }

                    jScrollPane1.requestFocus();
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_D && play == false) {
                    last = e.getKeyCode();
                    //markerPosition++;
                    if (markerPosition < bfimg.getWidth()) {
                        markerPosition++;

                    }

                    jScrollPane1.requestFocus();
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_A && play == true) {
                    last = e.getKeyCode();
                    markerPosition -= 50;

                    if (markerPosition < 0) {
                        markerPosition = 0;
                    }

                    stopSound();
                    playSound();
                    jScrollPane1.requestFocus();
                    return;

                }

                if (e.getKeyCode() == KeyEvent.VK_D && play == true) {
                    last = e.getKeyCode();
                    markerPosition += 50;
                    if (markerPosition < bfimg.getWidth()) {
                        markerPosition++;

                    }
                    // move.interrupt();
                    stopSound();
                    playSound();
                    jScrollPane1.requestFocus();
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_NUMPAD4 && play == false) {
                    last = e.getKeyCode();
                    markerPosition--;
                    if (markerPosition < 0) {
                        markerPosition = 0;
                    }

                    jScrollPane1.requestFocus();
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_NUMPAD6 && play == false) {
                    last = e.getKeyCode();

                    if (markerPosition < bfimg.getWidth()) {
                        markerPosition++;

                    }

                    jScrollPane1.requestFocus();
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_NUMPAD4 && play == true) {
                    last = e.getKeyCode();
                    markerPosition -= 50;

                    if (markerPosition < 0) {
                        markerPosition = 0;
                    }

                    jScrollPane1.requestFocus();
                    stopSound();
                    playSound();
                    return;

                }

                if (e.getKeyCode() == KeyEvent.VK_NUMPAD6 && play == true) {
                    last = e.getKeyCode();
                    markerPosition += 50;
                    if (markerPosition < bfimg.getWidth()) {
                        markerPosition++;

                    }

                    jScrollPane1.requestFocus();
                    stopSound();
                    playSound();
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_Q && play == true) {
                    last = e.getKeyCode();
                    int temp = markerPosition;
                    stopSound();
                    markerPosition = temp;
                    play = false;
                    jScrollPane1.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
                    if (jList2.getSelectedIndex() > 0) {
                        jList2.setSelectedIndex(jList2.getSelectedIndex() - 1);
                        jList2.ensureIndexIsVisible(jList2.getSelectedIndex());
                    }
                    jLabel3.setText("Word:" + (jList2.getSelectedIndex() + 1) + "/" + jList2.getModel().getSize());
                    jLabel3.repaint();
                    jScrollPane1.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
                    if (jList2.getSelectedIndex() < jList2.getModel().getSize() - 1) {
                        jList2.setSelectedIndex(jList2.getSelectedIndex() + 1);
                        jList2.ensureIndexIsVisible(jList2.getSelectedIndex());
                    }
                    jLabel3.setText("Word:" + (jList2.getSelectedIndex() + 1) + "/" + jList2.getModel().getSize());
                    jLabel3.repaint();
                    jScrollPane1.requestFocus();
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
        return kl;
    }

    public void initiateLyricDaemon() {

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                float quantum = awc.getDurationInSeconds() * 1000 / bfimg.getWidth();
                while (true) {
                    if (play && wordscroll) {
                        long tempTime = Math.round(quantum) * markerPosition;
                        //logger.log("Worken ra size is " + ra.size());
                        ArrayList<Integer> temporList = new ArrayList<Integer>();

                        for (int i = 0; i < ra.size(); i++) {

                            if (ra.get(i).getTime() - tempTime <= 150 && ra.get(i).getTime() - tempTime > 100) {
                                temporList.add(new Integer(i));
                            }

                        }

                        if (temporList.size() > 0) {
                            int indices[] = new int[temporList.size()];
                            for (int i = 0; i < temporList.size(); i++) {
                                indices[i] = temporList.get(i).intValue();
                            }
                            int lastVisible = jList2.getLastVisibleIndex();
                            int firstVisible = jList2.getFirstVisibleIndex();
                            int lastSelected = indices[indices.length - 1];

                            //if(debug){logger.log("last Selected - First Visible = "+lastSelected+"-"+firstVisible+"="+(lastSelected-firstVisible)+" and totalVisible is "+lastVisible+" and the quarter rounded is "+((int)(totalVisible/4))+"and their subtraction is "+(totalVisible-(int)(totalVisible/4)));}
                            if (lastSelected - lastVisible < 5) {
                                jList2.ensureIndexIsVisible(lastSelected + 10);
                                //if(debug){logger.log("i should have moved now to "+(lastSelected+(int)(totalVisible/4)));}
                                jList2.setSelectedIndices(indices);
                                jList2.repaint();
                                jLabel3.setText("Word:" + (indices[0] + 1) + "/" + jList2.getModel().getSize());
                                jLabel3.repaint();
                            }
                            else {
                                jList2.ensureIndexIsVisible(indices[indices.length - 1]);
                                jList2.setSelectedIndices(indices);
                                jList2.repaint();
                                jLabel3.setText("Word:" + (indices[0] + 1) + "/" + jList2.getModel().getSize());
                                jLabel3.repaint();
                            }

                            try {
                                sleep(80);
                            }
                            catch (InterruptedException ex) {
                                Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    }
                    else {
                        try {
                            sleep(3);
                        }
                        catch (InterruptedException ex) {
                            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
        t.start();
    }

    public void initiateMemoryLog() {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                runtime = Runtime.getRuntime();
                while (true) {
                    try {
                        freememory = runtime.freeMemory() / (1024 * 1024);
                        sleep(10);
                    }
                    catch (InterruptedException ex) {
                        Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        t.start();
    }

    public void updateLyrics() {
        String s = jTextPane1.getText();

        StringTokenizer st = new StringTokenizer(s);

        DefaultListModel ls = new DefaultListModel();
        ra = new ArrayList<lyric>();
        while (st.hasMoreTokens()) {
            ra.add(new lyric(-1, st.nextToken()));
            ls.addElement(ra.get(ra.size() - 1));
        }
        jList2.setModel(ls);
        if (ra.size() > 0) {
            jList2.setSelectedIndex(0);
            jList2.ensureIndexIsVisible(0);
        }
        jList2.removeKeyListener(jList2.getKeyListeners()[0]);
        jList2.addKeyListener(kl);
        tempText = jTextPane1.getText();
        jLabel3.setText("Word:" + (jList2.getSelectedIndex() + 1) + "/" + jList2.getModel().getSize());
        jLabel3.repaint();
    }

    private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
        SourceDataLine res = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        res = (SourceDataLine) AudioSystem.getLine(info);
        res.open(audioFormat);
        return res;
    }

    private void playSound() {

        if (soundThread != null) {
            soundThread.stop();
            System.gc();
        }
        soundThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    int tempPosition = markerPosition;
                    sound = AudioSystem.getAudioInputStream(toBeStreamed);
                    float quantum = awc.getDurationInSeconds() * 1000 / bfimg.getWidth();
                    pixelperms = bfimg.getWidth() / ((sound.getFrameLength() / sound.getFormat().getFrameRate()) * 1000);

                    float pixelSkip = sound.getFormat().getFrameSize() * (sound.getFormat().getFrameRate() / 1000);
                    pixelSkip *= 2;

                    sound.skip(markerPosition * Math.round(quantum) * sound.getFormat().getFrameSize() * Math.round(sound.getFormat().getFrameRate() / 1000));

                    byte[] data = new byte[Math.round(pixelSkip / pixelperms)];
                    line = getLine(sound.getFormat());
                    if (line != null) {

                        line.start();

                        int nBytesRead = 0, nBytesWritten = 0;
                        while (nBytesRead != -1) {
                            nBytesRead = sound.read(data, 0, data.length);

                            if (nBytesRead != -1) {
                                int bytesPlayed = 0;
                                nBytesWritten = line.write(data, 0, nBytesRead);
                                bytesPlayed += line.getFramePosition();
                                if (bytesPlayed > Math.round(pixelSkip / pixelperms)) {
                                    bytesPlayed -= Math.round(pixelSkip / pixelperms);
                                    markerPosition += 2;
                                }

                                /*float seconds = ((sound.getFrameLength() / sound.getFormat().getFrameRate()) * ((float) markerPosition / bfimg.getWidth()));
                                 float millis = seconds - (int) seconds;
                                 String millisprint = "";
                                 String secondsprint = "";
                                 if (seconds % 60 < 10) {
                                 secondsprint = "0" + (int) seconds % 60;
                                 } else {
                                 secondsprint = "" + (int) seconds % 60;
                                 }
                                 int millistp = (int) (millis * 1000);
                                 if (millistp < 10) {
                                 millisprint = "00" + millistp;

                                 }
                                 if (millistp < 100 && millistp > 9) {
                                 millisprint = "0" + millistp;

                                 }
                                 if (millistp < 1000 && millistp > 99) {
                                 millisprint = millistp + "";

                                 }

                                 jLabel2.setText((int) (seconds / 60) + ":" + secondsprint + ":" + millisprint);

                                 jLabel2.repaint();*/
                            }
                        }
                        // Stop
                        play = false;
                        markerPosition = tempPosition;
                        //markImage();
                    }
                }
                catch (Exception e) {
                }
            }
        });
        soundThread.setName("sound thread");
        soundThread.start();
        //this.jButton1.removeKeyListener(this.jButton1.getKeyListeners()[0]);
        this.jButton1.addKeyListener(kl);
        //this.jButton2.removeKeyListener(this.jButton1.getKeyListeners()[0]);
        this.jButton2.addKeyListener(kl);
    }

    public void stopSound() {
        if (soundThread != null) {
            soundThread.stop();
            //line.drain();
            line.stop();
            line.close();
        }
    }

    public BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = null;
        boolean isAlphaPremultiplied = false;
        WritableRaster raster = null;

        while (freememory < 20 && play && memoryFlag) {

            try {
                logger.log(new Date().toGMTString() + "\nfree memory: " + freememory + "\ntotal memory: " + (runtime.totalMemory() / (1024 * 1024)));
            }
            catch (Exception e) {
                logger.log(e.getLocalizedMessage());
            }
            try {
                sleep(100);
            }
            catch (InterruptedException ex) {
                Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        {
            try {
                cm = bi.getColorModel();
                isAlphaPremultiplied = cm.isAlphaPremultiplied();

                raster = bi.copyData(null);

            }
            catch (OutOfMemoryError e) {

                Thread t = move;
                raster = null;
                this.setVisible(false);

                System.gc();
                String savepath = save();
                logger.log("Dump of your work is available at " + savepath);
                JOptionPane.showMessageDialog(self, "Java's at it again! The application has crashed because of java's inconsistency. Your work so far has been saved in " + savepath + ", you can reload it from there. " + e.getStackTrace(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                if (debug) {
                    Writer writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(writer);
                    e.printStackTrace(printWriter);
                    String output = writer.toString();
                    logger.log(output);
                }

                t.stop();
                System.exit(-1);
            }
            catch (Exception e) {

                raster = null;
                this.setVisible(false);
                System.gc();
                String savepath = save();
                logger.log("Dump of your work is available at " + savepath);
                JOptionPane.showMessageDialog(self, "Java's at it again! The application has crashed because of java's inconsistency. Your work so far has been saved in " + savepath + ", you can reload it from there. " + e.getStackTrace(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                if (debug) {
                    Writer writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(writer);
                    e.printStackTrace(printWriter);
                    String output = writer.toString();
                    logger.log(output);
                }
                System.exit(-1);

            }
        }
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public void scrollImage() {

        imageThread = new Thread(new Runnable() {

            @Override
            public void run() {
                play = true;
                BufferedImage temp;
                Graphics2D g;
                int counter = 0;
                float tempAdder = 0;
                float quantum = bfimg.getWidth() / awc.getDurationInSeconds();

                quantum = quantum / 1000;

                int tempPos = markerPosition;
                int times = 1;
                int lastposition = 0;
                boolean repaint = false;

                while (tempPos < bfimg.getWidth()) {
                    tempAdder += (quantum * 100);

                    while (tempAdder > 1.0 || tempAdder == 1.0) {
                        tempAdder--;
                        tempPos++;

                        repaint = true;
                    }
                    if (repaint && freememory > 30) {

                        repaint = false;

                        temp = deepCopy(bfimg);

                        g = temp.createGraphics();
                        g.setColor(Color.red);
                        g.drawLine(tempPos, 0, tempPos, jScrollPane1.getHeight());
                        ImageIcon ii = new ImageIcon(temp);
                        JLabel updated = new JLabel(ii);
                        jScrollPane1.add(updated);
                        jScrollPane1.validate();

                        if (tempPos != 0) {
                            times = tempPos / getWidth();
                        }
                        else {
                            times = 0;
                        }
                        if (getWidth() * times - tempPos < 100) {

                            JViewport vp = new JViewport();
                            vp.setView(updated);
                            vp.setViewPosition(new Point((Math.round(getWidth()) * Math.round(times)), 0));

                            jScrollPane1.setViewport(vp);

                            counter++;
                        }
                        else {

                            JViewport vp = new JViewport();
                            vp.setView(updated);
                            vp.setViewPosition(new Point(Math.round(getWidth() * Math.round(times - 1)), 0));

                            jScrollPane1.setViewport(vp);

                        }

                        jScrollPane1.repaint();

                        try {
                            sleep(100);
                        }
                        catch (InterruptedException ex) {
                            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    else {
                        logger.log("****************System is on low Memory!! " + freememory + "MB ARE FREE!!**************");
                    }
                }

                play = false;
                markerPosition = tempMarker;
                //markImage();
            }
        });
        imageThread.start();

    }   //deprecated

    public void markImage() {

        move = new Thread(new Runnable() {

            @Override
            public void run() {
                BufferedImage temp;
                Graphics2D g;
                int counter = 0;
                int times = 1;

                while (true) {
                    float seconds = ((sound.getFrameLength() / sound.getFormat().getFrameRate()) * ((float) markerPosition / bfimg.getWidth()));
                    String secondsprint = "";
                    float millis = seconds - (int) seconds;
                    if (seconds % 60 < 10) {
                        secondsprint = "0" + (int) seconds % 60;
                    }
                    else {
                        secondsprint = "" + (int) seconds % 60;
                    }
                    String millisprint = "";
                    int millistp = (int) (millis * 1000);
                    if (millistp < 10) {
                        millisprint = "00" + millistp;

                    }
                    if (millistp < 100 && millistp > 9) {
                        millisprint = "0" + millistp;

                    }
                    if (millistp < 1000 && millistp > 99) {
                        millisprint = millistp + "";

                    }

                    jLabel2.setText((int) (seconds / 60) + ":" + secondsprint + ":" + millisprint);

                    temp = deepCopy(bfimg);
                    int myMarker = markerPosition;
                    if (!play) {
                        myMarker = jScrollPane1.getHorizontalScrollBar().getValue();
                    }
                    g = temp.createGraphics();
                    g.setColor(Color.red);

                    g.drawLine(markerPosition, 0, markerPosition, temp.getHeight());

                    ImageIcon ii = new ImageIcon(temp);

                    JLabel updated = new JLabel(ii);
                    updated.setName("label new");
                    for (int i = 0; i < jScrollPane1.getComponentCount(); i++) {
                        if (jScrollPane1.getComponent(i) != null) {
                            if (jScrollPane1.getComponent(i).getName() != null) {
                                if (jScrollPane1.getComponent(i).getName().equals("label new")) {
                                    jScrollPane1.getComponent(i).setName("label old");
                                }
                            }
                        }

                    }

                    jScrollPane1.add(updated);
                    for (int i = 0; i < jScrollPane1.getComponentCount(); i++) {
                        if (jScrollPane1.getComponent(i) != null) {
                            if (jScrollPane1.getComponent(i).getName() != null) {
                                if (jScrollPane1.getComponent(i).getName().equals("label old")) {
                                    jScrollPane1.remove(i);
                                }
                            }
                        }

                    }

                    JViewport vp = new JViewport();
                    vp.setView(updated);
                    vp.setViewPosition(jScrollPane1.getViewport().getViewPosition());
                    if (play && jScrollPane1.getViewport().getViewPosition().x + jScrollPane1.getWidth() < markerPosition) {

                        vp.setView(updated);
                        vp.setViewPosition(new Point(myMarker - (int) Math.round(jScrollPane1.getViewport().getWidth() / 4), 0));

                        counter++;

                    }

                    jScrollPane1.validate();
                    jScrollPane1.setViewport(vp);

                    if (soundThread != null) {
                        soundThread.interrupt();
                    }

                    try {

                        sleep(10);

                    }
                    catch (InterruptedException ex) {

                    }

                }

            }
        });
        move.setName("moving image thread");
        move.start();

    }

    public void revertImage() {
        ImageIcon ii = new ImageIcon(bfimg);
        JLabel updated = new JLabel(ii);
        jScrollPane1.add(updated);
        jScrollPane1.validate();

        jScrollPane1.setViewportView(updated);

        jScrollPane1.repaint();

        play = false;

    }

    boolean isAlpha(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < 0x30 || (c >= 0x3a && c <= 0x40) || (c > 0x5a && c <= 0x60) || c > 0x7a) {
                return false;
            }
        }

        return true;
    }

    public void updateLyricsAdd(DocumentEvent e) {
        String errc = "";
        activeLyric = jList2.getSelectedIndex();

        try {

            String curr = e.getDocument().getText((e.getOffset()), 1);

            char c = 'c';
            char p = 'p';

            String next = e.getDocument().getText((e.getOffset() + 1), 1);
            char n = 'n';

            if (e.getOffset() != 0) {
                String prev = e.getDocument().getText((e.getOffset() - 1), 1);
                curr = e.getDocument().getText((e.getOffset()), 1);
                prev = e.getDocument().getText((e.getOffset() - 1), 1);

                for (int i = 0; i < prev.length(); i++) {
                    p = prev.charAt(i);
                }
                for (int i = 0; i < next.length(); i++) {
                    n = next.charAt(i);
                }

            }
            for (int i = 0; i < curr.length(); i++) {
                c = curr.charAt(i);
            }

            if (Character.isWhitespace(c) && e.getLength() == 1) {

                if (e.getOffset() != 0 && (!Character.isWhitespace(p) && !Character.isWhitespace(n))) {
                    StringTokenizer st = new StringTokenizer(e.getDocument().getText(0, e.getOffset()));
                    String last = "";
                    int counter = -1;
                    while (st.hasMoreTokens()) {
                        last = st.nextToken();
                        counter++;
                    }
                    ra.get(counter).setS(last);
                    st = new StringTokenizer(e.getDocument().getText(e.getOffset(), e.getDocument().getLength() - e.getOffset()));
                    last = st.nextToken();
                    ra.add(counter + 1, new lyric(-1, last));

                }

                /*   if ((c == ' ')) {
                 logger.log("I AM INDEED A space");
                 if (e.getOffset() != 0 && (!Character.isWhitespace(p) && !Character.isWhitespace(n))) {
                 StringTokenizer st = new StringTokenizer(e.getDocument().getText(0, e.getOffset()));
                 String last = "";
                 int counter = -1;
                 while (st.hasMoreTokens()) {
                 last = st.nextToken();
                 counter++;
                 }
                 ra.get(counter).setS(last);
                 st = new StringTokenizer(e.getDocument().getText(e.getOffset(), e.getDocument().getLength() - e.getOffset()));
                 last = st.nextToken();
                 ra.add(counter + 1, new lyric(-1, last));
                 }

                 }*/
                throw (new completeException());
            }

            if (e.getOffset() == 0 && e.getLength() == 1) {
                errc = "got at 1 ";
                if (debug) {
                    logger.log("Firing 1");
                }
                String check = e.getDocument().getText(e.getOffset(), 1);
                char test = 'p';
                for (int i = 0; i < check.length(); i++) {
                    test = check.charAt(i);
                }
                if (e.getDocument().getLength() == 1 && !Character.isWhitespace(test)) {
                    ra.add(new lyric(-1, e.getDocument().getText(0, e.getDocument().getLength())));
                }
                else if (!Character.isWhitespace(test)) {
                    ra.get(0).setS(e.getDocument().getText(0, 1) + ra.get(0).getS());
                }

            }

            if (e.getOffset() != 0 && Character.isWhitespace(n) && e.getLength() == 1 && !Character.isWhitespace(p)) {
                errc = "got at 2 ";
                if (debug) {
                    logger.log("Firing 2");
                }
                StringTokenizer st = new StringTokenizer(e.getDocument().getText(0, e.getOffset() + 1));
                String last = "";
                int counter = -1;
                while (st.hasMoreTokens()) {
                    last = st.nextToken();
                    counter++;
                }

                String check = e.getDocument().getText(e.getOffset(), 1);
                char test = 'p';
                for (int i = 0; i < check.length(); i++) {
                    test = check.charAt(i);
                }

                if (!Character.isWhitespace(test) && !(last.length() == 1)) {
                    ra.get(counter).setS(last);
                }

                if (!Character.isWhitespace(test) && last.length() == 1) {
                    ra.add(counter, new lyric(-1, last));
                }
                throw (new completeException());
            }

            if (e.getOffset() != 0 && Character.isWhitespace(p) && !Character.isWhitespace(n) && e.getLength() == 1) {
                errc = "got at 3 ";
                if (debug) {
                    logger.log("Firing 3");
                }
                StringTokenizer st = new StringTokenizer(e.getDocument().getText(0, e.getOffset() - 1));
                String last = "";
                int counter = 0;
                while (st.hasMoreTokens()) {
                    last = st.nextToken();
                    counter++;

                }
                if (debug) {
                    logger.log(last);
                }
                String check = e.getDocument().getText(e.getOffset(), 1);
                char test = 'p';
                for (int i = 0; i < check.length(); i++) {
                    test = check.charAt(i);
                }

                if (last.length() != 1 && !Character.isWhitespace(test)) {
                    ra.get(counter).setS(e.getDocument().getText(e.getOffset(), 1) + ra.get(counter).getS());
                }
                else if (!Character.isWhitespace(test)) {

                    ra.add(counter, new lyric(ra.get(counter).getTime(), last));
                    ra.get(counter - 1).setTime(-1);
                }
                throw (new completeException());
            }

            if (e.getOffset() != 0 && !Character.isWhitespace(n) && !Character.isWhitespace(p) && e.getLength() == 1) {
                errc = "got at 4 ";
                if (debug) {
                    logger.log("Firing 4");
                }
                StringTokenizer st = new StringTokenizer(e.getDocument().getText(0, e.getOffset()));
                String last = "";

                int counter = 0;
                while (st.hasMoreTokens()) {
                    last = st.nextToken();
                    counter++;

                }
                counter--;

                st = new StringTokenizer(e.getDocument().getText(e.getOffset(), e.getDocument().getLength() - e.getOffset()));
                String last2 = st.nextToken();

                if (debug) {
                    logger.log("Buffer is " + buffer + " ra at counter is " + ra.get(counter).getS());
                }
                String check = e.getDocument().getText(e.getOffset(), 1);
                char test = 'p';
                for (int i = 0; i < check.length(); i++) {
                    test = check.charAt(i);
                }

                if (!Character.isWhitespace(test)) {
                    errc += " on the first case scenario ";

                    ra.get(counter).setS(last + last2);
                }
                else {
                    errc += " on the else case scenario ";

                    ra.get(counter).setS(last);
                    ra.add(counter + 1, new lyric(-1, last2));
                    buffer = "";

                }
                throw (new completeException());

            }

            if (e.getOffset() == e.getDocument().getLength() - 1 && e.getDocument().getLength() != 1 && e.getLength() == 1 && !e.getDocument().getText(e.getOffset(), 1).equals(" ")) {
                errc = "got at 5 ";
                if (debug) {
                    logger.log("Firing 5");
                }
                StringTokenizer st = new StringTokenizer(e.getDocument().getText(0, e.getDocument().getLength()));
                String last = "";

                int counter = 0;
                while (st.hasMoreTokens()) {
                    last = st.nextToken();
                    counter++;
                }
                if (counter == ra.size()) {
                    errc = "errc: 1";
                    ra.get(ra.size() - 1).setS(last);

                }
                else {
                    errc = "errc: 2";
                    ra.add(new lyric(-1, last));

                }
                if (debug) {
                    logger.log("Case 5 " + e.getLength());
                }
                throw (new completeException());
            }
            if (e.getOffset() != 0 && Character.isWhitespace(n) && Character.isWhitespace(p) && e.getLength() == 1) {
                StringTokenizer st = new StringTokenizer(e.getDocument().getText(0, e.getOffset()));
                int counter = -1;
                if (!Character.isWhitespace(c)) {
                    while (st.hasMoreElements()) {
                        st.nextElement();
                        counter++;
                    }
                    ra.add(counter + 1, new lyric(-1, e.getDocument().getText(e.getOffset(), 1)));
                    if (debug) {
                        logger.log("add Case new! ");
                    }
                    throw (new completeException());
                }
            }
            //the following code is to determine paste of multiple characters
            if (e.getLength() > 1 && e.getOffset() < tempText.length() - 1) {
                if (jTextPane1.getText().length() > 0) {
                    StringTokenizer st = new StringTokenizer(jTextPane1.getText(0, e.getOffset()));
                    int counter = -1;
                    String last = "";
                    while (st.hasMoreElements()) {
                        last = st.nextToken();
                        counter++;
                    }
                    String pasted = jTextPane1.getText(e.getOffset(), e.getLength());
                    st = new StringTokenizer(pasted);
                    if (debug) {
                        logger.log("char before is \"" + tempText.charAt(e.getOffset() - 1) + " \"char after is \"" + tempText.charAt(e.getOffset()) + "\"");
                    }
                    if (!Character.isWhitespace(tempText.charAt(e.getOffset() - 1)) && Character.isWhitespace(tempText.charAt(e.getOffset()))) {
                        if (debug) {
                            logger.log("Condition 1");
                        }
                        ra.get(counter).setS(ra.get(counter).getS() + st.nextToken());

                        while (st.hasMoreElements()) {
                            ra.add(counter + 1, new lyric(-1, st.nextToken()));
                            counter++;
                        }
                    }

                    if (Character.isWhitespace(tempText.charAt(e.getOffset() - 1)) && !Character.isWhitespace(tempText.charAt(e.getOffset()))) {

                        if (debug) {
                            logger.log("Condition 2");
                        }
                        while (st.hasMoreElements()) {
                            ra.add(counter + 1, new lyric(-1, st.nextToken()));
                            counter++;
                        }
                        ra.get(counter).setS(ra.get(counter).getS() + ra.get(counter + 1).getS());
                        ra.remove(counter + 1);
                    }

                    if (Character.isWhitespace(tempText.charAt(e.getOffset() - 1)) && Character.isWhitespace(tempText.charAt(e.getOffset()))) {

                        if (debug) {
                            logger.log("Condition both white");
                        }
                        while (st.hasMoreElements()) {
                            ra.add(counter + 1, new lyric(-1, st.nextToken()));
                            counter++;
                        }

                    }

                    if (Character.isWhitespace(tempText.charAt(e.getOffset() - 1)) && Character.isWhitespace(tempText.charAt(e.getOffset()))) {

                        if (debug) {
                            logger.log("Condition both white");
                        }
                        while (st.hasMoreElements()) {
                            ra.add(counter + 1, new lyric(-1, st.nextToken()));
                            counter++;
                        }

                    }

                    if (!Character.isWhitespace(tempText.charAt(e.getOffset() - 1)) && !Character.isWhitespace(tempText.charAt(e.getOffset()))) {
                        if (debug) {
                            logger.log("Condition 3");
                        }

                        int lenofpre = last.length();

                        int lenofpost = ra.get(counter).getS().length() - lenofpre;
                        String keepThis = ra.get(counter).getS().substring(lenofpre, lenofpre + lenofpost);
                        if (!Character.isWhitespace(jTextPane1.getText().charAt(e.getOffset()))) {
                            ra.get(counter).setS(last + st.nextToken());
                        }
                        else {
                            if (debug) {
                                logger.log("T'is but a space mylord " + jTextPane1.getText().charAt(e.getOffset()));
                            }
                            ra.get(counter).setS(last);
                            while (Character.isWhitespace(pasted.charAt(0))) {
                                pasted = pasted.substring(1);
                            }

                        }
                        while (st.hasMoreElements()) {
                            ra.add(counter + 1, new lyric(-1, st.nextToken()));
                            counter++;
                        }
                        ra.get(counter).setS(ra.get(counter).getS() + keepThis);

                    }
                }
            }
            else if (e.getLength() > 1 && (e.getOffset() == tempText.length() - 1 || e.getOffset() == tempText.length()) && tempText.length() != 0) {
                if (debug) {
                    logger.log("I'm in!!");
                }
                StringTokenizer st = new StringTokenizer(jTextPane1.getText(e.getOffset(), e.getDocument().getLength() - e.getOffset()));

                if (Character.isWhitespace(tempText.charAt(tempText.length() - 1)) || Character.isWhitespace(jTextPane1.getText().charAt(e.getOffset()))) {
                    if (debug) {
                        logger.log("now @ if");
                    }
                    if (Character.isWhitespace(tempText.charAt(tempText.length() - 1)) && !Character.isWhitespace(jTextPane1.getText().charAt(e.getOffset()))) {
                        if (debug) {
                            logger.log("prev isn't next is");
                        }
                        ra.get(ra.size() - 1).setS(ra.get(ra.size() - 1).getS() + st.nextToken());
                        while (st.hasMoreElements()) {
                            ra.add(new lyric(-1, st.nextToken()));
                        }

                    }
                    if (!Character.isWhitespace(tempText.charAt(tempText.length() - 1)) && Character.isWhitespace(jTextPane1.getText().charAt(e.getOffset()))) {
                        //ra.get(ra.size() - 1).setS(ra.get(ra.size() - 1).getS() + st.nextToken());
                        if (debug) {
                            logger.log("prev is next isn't");
                        }
                        while (st.hasMoreElements()) {
                            ra.add(new lyric(-1, st.nextToken()));
                        }

                    }

                }
                else {
                    if (debug) {
                        logger.log("now @ else");
                    }
                    ra.get(ra.size() - 1).setS(ra.get(ra.size() - 1).getS() + st.nextToken());
                    while (st.hasMoreElements()) {
                        ra.add(new lyric(-1, st.nextToken()));
                    }
                }
            }

        }
        catch (Exception ex) {
            if (debug) {
                logger.log(errc + ex + e.toString());
            }
        }

        DefaultListModel ls = new DefaultListModel();
        for (int i = 0; i < ra.size(); i++) {
            ls.addElement(ra.get(i));
        }
        jList2.setModel(ls);
        if (ra.size() > 0) {
            jList2.setSelectedIndex(0);
        }
        jList2.setSelectedIndex(activeLyric);
        jList2.removeKeyListener(jList2.getKeyListeners()[0]);
        jList2.addKeyListener(kl);
        jList2.repaint();

        tempText = jTextPane1.getText();
    }

    public void seriousRemoval(DocumentEvent e) {

        int until = e.getLength();
        StringTokenizer st = new StringTokenizer(tempText.substring(0, until), " ");
        int counter = 0;
        if (debug) {
            logger.log("I arrived");
        }
        while (st.hasMoreElements()) {
            counter++;
            st.nextToken();
        }
        if (debug) {
            logger.log("and counted " + counter);
        }
        try {
            st = new StringTokenizer(e.getDocument().getText(0, (e.getDocument().getLength() - 1)), " ");
            String newWord = null;
            if (st.hasMoreElements()) {
                newWord = st.nextToken();
            }
            if (newWord != null) {
                ra.get(counter).setS(newWord);
                if (debug) {
                    logger.log("and setted " + newWord);
                }
                for (int i = 0; i < counter; i++) {
                    ra.remove(0);
                }
                DefaultListModel ls = new DefaultListModel();
                for (int i = 0; i < ra.size(); i++) {
                    ls.addElement(ra.get(i));
                }
                jList2.setModel(ls);
                jList2.setSelectedIndex(0);
                if (debug) {
                    logger.log("and removed");
                }
            }
        }
        catch (BadLocationException ex) {
            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (debug) {
                logger.log("and new tempTexted");
            }
            tempText = e.getDocument().getText(0, (e.getDocument().getLength() - 1));
        }
        catch (BadLocationException ex) {
            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        jList2.repaint();
    }

    public void updateLyricsRemove2(DocumentEvent e) {
        if (frommouse) {
            frommouse = false;
            return;
        }
        if (debug) {
            logger.log("When I begin the remove2 procedure tempText is probably " + tempText);
        }
        if (e.getLength() == 1 && !tempText.equals("")) {
            if (!Character.isWhitespace(tempText.charAt(e.getOffset()))) {
                StringTokenizer st = new StringTokenizer(tempText.substring(0, e.getOffset()));
                if (e.getOffset() != 0) {
                    int counter = -1;
                    String last = "";
                    while (st.hasMoreElements()) {
                        last = st.nextToken();
                        counter++;
                    }
                    if (debug) {
                        logger.log(last + " " + ra.get(counter).getS());
                    }
                    if (last.equals(ra.get(counter).getS())) {

                        if (!ra.get(counter + 1).getS().substring(1).equals("")) {
                            ra.get(counter + 1).setS(ra.get(counter + 1).getS().substring(1));
                        }
                        else {
                            ra.remove(counter + 1);
                        }
                    }
                    else if (ra.get(counter).getS().startsWith(last)) {
                        st = new StringTokenizer(tempText.substring(e.getOffset(), tempText.length()));
                        String change = st.nextToken();
                        ra.get(counter).setS(last + change.substring(1));
                    }
                }
                else if (e.getOffset() == 0) {
                    if (!ra.get(0).getS().substring(1).equals("")) {
                        ra.get(0).setS(ra.get(0).getS().substring(1));
                    }
                    else {
                        ra.remove(0);
                    }

                }

            }
            if (Character.isWhitespace(tempText.charAt(e.getOffset()))) {
                //logger.log("Whitespace");
                if (e.getOffset() != 0 && e.getOffset() != tempText.length() - 1) {
                    //logger.log("Not start or end");
                    if (!Character.isWhitespace(tempText.charAt(e.getOffset() + 1)) && !Character.isWhitespace(tempText.charAt(e.getOffset() - 1))) {
                        StringTokenizer st = new StringTokenizer(tempText.substring(0, e.getOffset()));
                        String last = "";
                        int counter = -1;
                        while (st.hasMoreTokens()) {
                            last = st.nextToken();
                            counter++;
                        }
                        ra.get(counter).setS(ra.get(counter).getS() + ra.get(counter + 1).getS());
                        ra.remove(counter + 1);
                    }

                }
            }

        }
        else if (e.getLength() > 1) {
            if (debug) {
                logger.log("When I get to the long change tempText is probably " + tempText);
            }
            String change = tempText.substring(e.getOffset(), e.getOffset() + e.getLength());
            if (debug) {
                logger.log("Although, change string is " + change + "\noffset is " + e.getOffset() + "\nand length is " + e.getLength() + "\ntemptext length is " + tempText.length() + "\nnew text on the pane is " + e.getDocument().getLength());
            }

            boolean hasText = false;
            int firstChar = 0;
            for (int i = 0; i < change.length(); i++) {

                if (!Character.isWhitespace(change.charAt(i))) {
                    hasText = true;
                    firstChar = i;
                    break;

                }

            }
            if (hasText) {
                if (e.getLength() == tempText.length()) {
                    ra.clear();

                }
                else {
                    StringTokenizer st;
                    boolean firstWordDeletedFromFirstLetter = false;
                    int counter = -1;
                    int firstAffected = 0;
                    String first = "";
                    if (e.getOffset() != 0) {
                        st = new StringTokenizer(tempText.substring(0, e.getOffset()));
                        if (debug) {
                            logger.log("Affected text is: " + tempText.substring(0, e.getOffset()));
                        }
                        while (st.hasMoreElements()) {
                            first = st.nextToken();
                            counter++;
                        }
                    }
                    else {
                        seriousRemoval(e);
                        return;
                    }

                    if (first.equals(ra.get(counter).getS())) {
                        firstWordDeletedFromFirstLetter = true;
                        firstAffected = counter + 1;
                    }
                    else {
                        firstAffected = counter;
                        counter--;
                    }

                    st = new StringTokenizer(tempText.substring(e.getOffset(), e.getOffset() + e.getLength()));

                    String last = "";
                    while (st.hasMoreElements()) {
                        last = st.nextToken();
                        counter++;
                    }

                    int lastAffected = counter;
                    int between = lastAffected - firstAffected - 1;
//                    logger.log("First word affected is \"" + ra.get(firstAffeceted).getS() + "\" from  \"" + first + "\" and on while last word affected is \"" + ra.get(lastAffected).getS() + "\" up until\"" + last + "\" in between there are " + (between) + " words");

                    if (between == -1) {
                        boolean removedWord = false;
                        if (ra.get(firstAffected).getS().equals(last)) {
                            ra.remove(firstAffected);
                            removedWord = true;
                        }
                        else if (firstAffected != 0 && first.equals(ra.get(firstAffected - 1).getS())) {
                            int prefix = last.length();
                            String update = "";

                            update += ra.get(firstAffected).getS().substring(prefix);
                            ra.get(firstAffected).setS(update);
                            removedWord = true;
                            if (debug) {
                                logger.log("My new text is " + ra.get(firstAffected).getS());
                            }
                        }
                        else {
                            int prefix = first.length();
                            int remlen = last.length();
                            String update = "";
                            update += first;
                            update += ra.get(firstAffected).getS().substring(prefix + remlen);
                            ra.get(firstAffected).setS(update);

                            if (Character.isWhitespace(tempText.charAt(e.getOffset() + e.getLength() - 1))) {
                                ra.get(firstAffected).setS(ra.get(firstAffected).getS() + ra.get(firstAffected + 1).getS());
                                ra.remove(firstAffected + 1);
                            }
                        }

                        if (removedWord && (Character.isWhitespace(tempText.charAt(e.getOffset() + firstChar)) || Character.isWhitespace(tempText.charAt(e.getOffset() + e.getLength())))) {
                            ra.get(firstAffected - 1).setS(ra.get(firstAffected - 1).getS() + ra.get(firstAffected).getS());
                            ra.remove(firstAffected);
                        }

                    }
                    else {

                        for (int i = 0; i < between; i++) {
//                            logger.log("Gonna remove " + ra.get(firstAffeceted + 1 + i).getS() + " the sum is " + (firstAffeceted + 1 + i));
                            ra.remove(firstAffected + 1);
                        }
                        boolean didNotRemoveFirst = true;
                        if (ra.get(firstAffected - 1).getS().equals(first)) {
                            ra.remove(firstAffected);
                            didNotRemoveFirst = false;
                        }
                        boolean didNotRemoveSecond = true;
                        if (!didNotRemoveFirst && last.equals(ra.get(firstAffected).getS())) {
                            ra.remove(firstAffected);
                            didNotRemoveSecond = false;
                        }
                        else if (didNotRemoveFirst && last.equals(ra.get(firstAffected + 1).getS())) {
                            ra.remove(firstAffected + 1);
                            didNotRemoveSecond = false;
                        }

                        if (didNotRemoveFirst) {
                            int prefix = first.length();
                            ra.get(firstAffected).setS(ra.get(firstAffected).getS().substring(0, prefix));
                        }

                        if (didNotRemoveSecond && didNotRemoveFirst) {
                            int prefix = last.length();
                            ra.get(firstAffected + 1).setS(ra.get(firstAffected + 1).getS().substring(prefix));
                            ra.get(firstAffected).setS(ra.get(firstAffected).getS() + ra.get(firstAffected + 1).getS());
                            ra.remove(firstAffected + 1);
                        }

                        if (didNotRemoveSecond && !didNotRemoveFirst) {
                            int prefix = last.length();
                            ra.get(firstAffected).setS(ra.get(firstAffected).getS().substring(prefix));
                            if (Character.isWhitespace(tempText.charAt(e.getOffset()))) {
                                ra.get(firstAffected - 1).setS(ra.get(firstAffected - 1).getS() + ra.get(firstAffected).getS());
                                ra.remove(firstAffected);
                            }

                        }

                        if (!didNotRemoveSecond && didNotRemoveFirst) {

                            if (!(firstAffected + 1 == ra.size())) {
                                if (Character.isWhitespace(tempText.charAt(e.getOffset() + e.getLength() - 1))) {
                                    ra.get(firstAffected + 1).setS(ra.get(firstAffected).getS() + ra.get(firstAffected + 1).getS());
                                    ra.remove(firstAffected);
                                }
                            }

                        }
                        if (!didNotRemoveSecond && !didNotRemoveFirst) {

                            boolean x = Character.isWhitespace(tempText.charAt(e.getOffset()));
                            boolean y = Character.isWhitespace(tempText.charAt(e.getOffset() + e.getLength() - 1));
                            if (debug) {
                                logger.log("Gonna check spaces x is " + x + " and y is " + y);
                            }
                            if ((x && y) && firstAffected != ra.size()) {

                                ra.get(firstAffected - 1).setS(ra.get(firstAffected - 1).getS() + ra.get(firstAffected).getS());
                                ra.remove(firstAffected);

                            }
                        }

                    }
                }

            }
        }

        DefaultListModel ls = new DefaultListModel();
        for (int i = 0; i < ra.size(); i++) {
            ls.addElement(ra.get(i));
        }
        jList2.setModel(ls);
        if (ra.size() > 0) {
            jList2.setSelectedIndex(0);
        }
        jList2.setSelectedIndex(activeLyric);
        if (!jList2.getKeyListeners()[0].equals(null)) {
            jList2.removeKeyListener(jList2.getKeyListeners()[0]);
        }
        jList2.addKeyListener(kl);
        jList2.repaint();
        tempText = jTextPane1.getText();

    }

    public void newSound() {
        File file = null;
        final JFileChooser fcm = new JFileChooser();

        fcm.setDialogTitle("Select your audio");
        fcm.setFileHidingEnabled(false);
        if (!textpath.equals("notset")) {
            fcm.setCurrentDirectory(new File(textpath + "\\.."));
        }
        fcm.setFileFilter(new Mp3Filter());
        fcm.setAcceptAllFileFilterUsed(false);
        fcm.requestFocus();
        int returnVal = fcm.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fcm.getSelectedFile();
            module = file.getName();
            audiopath = file.getAbsolutePath();
            this.setTitle(title + "- Working on " + module);
        }
        if (!(returnVal == JFileChooser.APPROVE_OPTION)) {
            return;
        }
        try {
            JFrame wait = new JFrame();
            JLabel pw = new JLabel("Analysing the mp3 file now");

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            double width = screenSize.getWidth();
            double height = screenSize.getHeight();
            wait.setLocation(new Point((int) Math.round((width - 200) / 2), (int) Math.round((height - 100) / 2)));
            wait.setSize(200, 100);
            wait.getContentPane().add(pw);
            pw.setName("wait");

            //wait.getContentPane().getComponent(wait.getContentPane().getComponentCount() - 1).setLocation(new Point(23541325, 50));
            //wait.setUndecorated(true);
            wait.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            wait.setTitle("Please wait");
            wait.setBackground(Color.BLACK);
            wait.validate();
            wait.pack();
            wait.setVisible(true);
            move.stop();
            if (play) {
                play = false;
                stopSound();
            }
            markerPosition = 0;
            this.setEnabled(false);
            awc = new AudioWaveformCreator(fcm.getSelectedFile().getAbsolutePath());

            awc.createAudioInputStream();

            image = awc.getImage();
            bfimg = ImageIO.read(new File(image));
            String soundPath = awc.getDecodedPath();
            toBeStreamed = new File(soundPath);
            sound = AudioSystem.getAudioInputStream(toBeStreamed);
            wait.setVisible(false);
            this.setEnabled(true);
            markImage();

            this.jScrollPane1.getViewport().setViewPosition(new Point(0, 0));
            this.jScrollPane1.requestFocus();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Severe error Occured. Please contact Billaros\n" + e.getStackTrace(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            if (debug) {
                Writer writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                e.printStackTrace(printWriter);
                String output = writer.toString();
                logger.log(output);
            }
            System.exit(-1);
        }

    }

    public void newText() {

        ra = new ArrayList<lyric>();

        File text = null;
        String everything = "";
        initComponents();

        final JFileChooser fc = new JFileChooser();

        fc.setDialogTitle("Select your text");
        fc.setCurrentDirectory(new File(audiopath + "\\.."));
        fc.setFileFilter(new XmlFilter());
        fc.setFileFilter(new TxtFilter());
        fc.setFileFilter(new xmlandtextfilter());

        fc.setAcceptAllFileFilterUsed(false);
        fc.requestFocus();
        int returnVal = fc.showOpenDialog(this);
        String ext = "";
        boolean show = false;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            text = fc.getSelectedFile();
            textpath = text.getParent();
            StringTokenizer st = new StringTokenizer(text.getName(), ".");
            ext = "";
            while (st.hasMoreElements()) {
                ext = st.nextToken();

            }

        }
        if (!(returnVal == JFileChooser.APPROVE_OPTION)) {
            JOptionPane.showMessageDialog(this, "Since you didn't open a text file, you'll have to copy paste the text", "Warning",
                    JOptionPane.ERROR_MESSAGE);

            textpath = "notset";
        }
        if (ext.equals("txt")) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(text), "ISO-8859-1"));

                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append('\n');
                    line = br.readLine();
                }
                everything = sb.toString();
                this.jTextPane1.setText(everything);
                updateLyrics();
            }
            catch (Exception e) {
                textpath = "notset";
                JOptionPane.showMessageDialog(this, "Could not read text file, please copy paste it.", "Error",
                        JOptionPane.ERROR_MESSAGE);

            }
        }
        else if (ext.equals("xml")) {

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(text), "ISO-8859-1"));
                String buffer = "";
                ra.clear();
                StringTokenizer sa = new StringTokenizer(buffer);
                while ((buffer = br.readLine()) != null) {

                    if (buffer.contains("<lyric text=\"")) {
                        while (Character.isWhitespace(buffer.charAt(0))) {
                            buffer = buffer.substring(1);
                        }
                        if (debug) {
                            logger.log(buffer);
                        }
                        buffer = buffer.substring(13);
                        sa = new StringTokenizer(buffer, "\"");
                        String lyric = sa.nextToken();
                        String skip = sa.nextToken();
                        while (!skip.contains("time=")) {
                            if (debug) {
                                logger.log(skip);
                            }
                            skip = sa.nextToken();
                        }

                        String time = sa.nextToken();

                        String toTest = lyric;
                        String toWrite = "";
                        for (int j = 0; j < toTest.length(); j++) {
                            if (j < toTest.length() - 2 && toTest.charAt(j) == '\'' && toTest.charAt(j + 1) == '\'') {
                                toWrite += "\"";
                                j = j + 1;
                            }
                            else if (j < toTest.length() - 5 && toTest.charAt(j) == '&' && toTest.charAt(j + 1) == 'a' && toTest.charAt(j + 2) == 'm' && toTest.charAt(j + 3) == 'p' && toTest.charAt(j + 4) == ';') {
                                toWrite += "&";
                                j = j + 4;
                            }
                            else if (j < toTest.length() - 4 && toTest.charAt(j) == '&' && toTest.charAt(j + 1) == 'l' && toTest.charAt(j + 2) == 't' && toTest.charAt(j + 3) == ';') {
                                toWrite += "<";
                                j = j + 3;
                            }
                            else if (j < toTest.length() - 4 && toTest.charAt(j) == '&' && toTest.charAt(j + 1) == 'g' && toTest.charAt(j + 2) == 't' && toTest.charAt(j + 3) == ';') {
                                toWrite += ">";
                                j = j + 3;
                            }
                            else {
                                toWrite += toTest.charAt(j);
                            }
                        }

                        ra.add(new lyric(Long.parseLong(time), toWrite));
                        Long test = Long.parseLong(time);

                        if (debug) {
                            logger.log(ra.get(ra.size() - 1) + "    my ulong is: " + test);
                        }
                    }

                }
                br.close();
                DefaultListModel ls = new DefaultListModel();

                String newText = "";
                for (int i = 0; i < ra.size(); i++) {

                    ls.addElement(ra.get(i));
                    newText += " " + ra.get(i).getS();
                }
                this.jTextPane1.setText(newText);
                this.jList2.setModel(ls);
                if (ra.size() > 0) {
                    jList2.setSelectedIndex(0);
                }
                jList2.removeKeyListener(jList2.getKeyListeners()[0]);
                jList2.addKeyListener(kl);
                jList2.repaint();
            }
            catch (Exception e) {
                textpath = "notset";
                JOptionPane.showMessageDialog(this, "Could not read text file, please copy paste it.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                if (debug) {
                    Writer writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(writer);
                    e.printStackTrace(printWriter);
                    String output = writer.toString();
                    logger.log(output);
                }

            }

        }

    }

    public String save() {
        jMenuItem5ActionPerformed(new java.awt.event.ActionEvent(this, 0, null));
        /* String soundName = awc.getFileName();
         if (debug) {
         logger.log(soundName);
         }
         String Path = "";
         String tempHolder = "";
         String fileName = "";
         StringTokenizer st = new StringTokenizer(soundName, "\\");
         while (st.hasMoreElements()) {
         tempHolder = st.nextToken();
         if (st.hasMoreTokens()) {
         Path += tempHolder + "\\";
         } else {
         fileName = tempHolder;
         }
         }

         String fileName2 = "";
         st = new StringTokenizer(fileName, ".");
         while (st.hasMoreElements()) {
         tempHolder = st.nextToken();
         if (st.hasMoreTokens()) {
         fileName2 += tempHolder;
         }
         }
         File file = new File(fileName2 + ".xml");

         try {
         PrintWriter writer = new PrintWriter(file, "ISO-8859-1");
         writer.print("<lyrics>\n");
         for (int i = 0; i < ra.size(); i++) {
         String toTest = ra.get(i).getS();
         String toWrite = "";
         for (int j = 0; j < toTest.length(); j++) {
         if (toTest.charAt(j) == '"') {
         toWrite += "''";
         } else if (toTest.charAt(j) == '&') {
         toWrite += "&amp;";
         } else if (toTest.charAt(j) == '<') {
         toWrite += "&lt;";
         } else if (toTest.charAt(j) == '>') {
         toWrite += "&gt;";
         } else {
         toWrite += toTest.charAt(j);
         }
         //          if (debug) {
         //            logger.log("Lyric is " + toTest + " length is " + toTest.length() + " charAt(" + j + ") is " + toTest.charAt(j));
         //          }
         }
         if (i == 0) {
         if (ra.get(i).getTime() > 200) {
         writer.print("\t<lyric text=\"" + toWrite + "\" time=\"" + (ra.get(i).getTime() - 200) + "\"/>\n");
         } else {
         writer.print("\t<lyric text=\"" + toWrite + "\" time=\"" + ra.get(i).getTime() + "\"/>\n");
         }
         } else {
         writer.print("\t<lyric text=\"" + toWrite + "\" time=\"" + ra.get(i).getTime() + "\"/>\n");
         }

         }
         writer.print("</lyrics>");
         writer.close();
         hasChanges = false;
         } catch (FileNotFoundException ex) {
         Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
         } catch (UnsupportedEncodingException ex) {
         Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        return "";

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar1 = new java.awt.MenuBar();
        menu1 = new java.awt.Menu();
        menu2 = new java.awt.Menu();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jButton5 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        menu1.setLabel("File");
        menuBar1.add(menu1);

        menu2.setLabel("Edit");
        menuBar1.add(menu2);

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextPane1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jScrollPane4.setViewportView(jTextPane1);

        jList2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jScrollPane5.setViewportView(jList2);

        jButton1.setText("Clear All");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Clear Specific");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Scroll");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton3.setText("Fix Lyrics from List");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Fix Lyrics from Text");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel4.setText("Transpose all lyrics: ");

        jSlider1.setMajorTickSpacing(1);
        jSlider1.setMaximum(5000);
        jSlider1.setMinimum(-5000);
        jSlider1.setMinorTickSpacing(1);
        jSlider1.setSnapToTicks(true);
        jSlider1.setValue(0);

        jButton5.setText("Transpose");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jMenu1.setText("File");

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText("New karaoke");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Export as XML");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem4.setText("Exit");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Open other Mp3");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Open other Text");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Open other XML");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(33, 33, 33)
                                        .addComponent(jButton5)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox1))
                            .addComponent(jScrollPane5))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
                    .addComponent(jScrollPane4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton1)
                                .addComponent(jButton2))
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jCheckBox1))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton3)
                        .addComponent(jButton4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        File file = null;
        globalReturn = 0;
        final JFileChooser fcm = new JFileChooser();

        fcm.setDialogTitle("Select your audio");
        fcm.setFileHidingEnabled(false);
        if (!textpath.equals("notset")) {
            fcm.setCurrentDirectory(new File(textpath + "\\.."));
        }
        fcm.setFileFilter(new Mp3Filter());
        fcm.setAcceptAllFileFilterUsed(false);
        fcm.requestFocus();
        int returnVal = fcm.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (play) {
                stopSound();
            }
            file = fcm.getSelectedFile();

            module = file.getName();
            audiopath = file.getAbsolutePath();
            this.setTitle(title + "- Working on " + module);
            globalReturn = 1;
        }
        if (!(returnVal == JFileChooser.APPROVE_OPTION)) {
            globalReturn = 0;
            return;
        }
        try {
            JFrame wait = new JFrame();
            JLabel pw = new JLabel("Analysing the mp3 file now");

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            double width = screenSize.getWidth();
            double height = screenSize.getHeight();
            wait.setLocation(new Point((int) Math.round((width - 200) / 2), (int) Math.round((height - 100) / 2)));
            wait.setSize(200, 100);
            wait.getContentPane().add(pw);
            pw.setName("wait");

            //wait.getContentPane().getComponent(wait.getContentPane().getComponentCount() - 1).setLocation(new Point(23541325, 50));
            //wait.setUndecorated(true);
            wait.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            wait.setTitle("Please wait");
            wait.setBackground(Color.BLACK);
            wait.validate();
            wait.pack();
            wait.setVisible(true);
            move.stop();
            if (play) {
                play = false;
                stopSound();
            }
            markerPosition = 0;
            this.setEnabled(false);
            awc = new AudioWaveformCreator(fcm.getSelectedFile().getAbsolutePath());

            awc.createAudioInputStream();

            image = awc.getImage();
            bfimg = ImageIO.read(new File(image));
            String soundPath = awc.getDecodedPath();
            toBeStreamed = new File(soundPath);
            sound = AudioSystem.getAudioInputStream(toBeStreamed);
            wait.setVisible(false);
            this.setEnabled(true);
            markImage();

            jScrollPane1.getViewport().setViewPosition(new Point(0, 0));
            jScrollPane1.requestFocus();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Severe error Occured. Please contact Billaros\n" + e.getStackTrace(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            if (debug) {
                Writer writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                e.printStackTrace(printWriter);
                String output = writer.toString();
                logger.log(output);
            }
            System.exit(-1);
        }

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        File text = null;
        String everything = "";

        int returnVal = 0;

        final JFileChooser fc = new JFileChooser();

        fc.setDialogTitle("Select your text");
        fc.setCurrentDirectory(new File(audiopath + "\\.."));
        fc.setFileHidingEnabled(false);
        fc.setFileFilter(new TxtFilter());
        fc.setAcceptAllFileFilterUsed(false);
        fc.requestFocus();
        returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            text = fc.getSelectedFile();

        }
        boolean show = true;
        if (!(returnVal == JFileChooser.APPROVE_OPTION)) {
            return;
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(text), "ISO-8859-1"));

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            everything = sb.toString();
            for (int i = 0; i < everything.length(); i++) {
                if (everything.charAt(i) == '’') {
                    String buffer = everything.substring(0, i - 1);
                    buffer = buffer + "'";
                    buffer = buffer + everything.substring(i + 1, everything.length() - 1);

                }
            }
        }
        catch (Exception e) {
            if (show) {
                JOptionPane.showMessageDialog(this, "Could not read text file, please copy paste it.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                if (debug) {
                    Writer writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(writer);
                    e.printStackTrace(printWriter);
                    String output = writer.toString();
                    logger.log(output);
                }
            }
        }

        this.jTextPane1.setText(everything);
        tempText = this.jTextPane1.getText();
        updateLyrics();

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        File text = null;
        String everything = "";
        fireDocument = false;
        int returnVal = 0;

        final JFileChooser fc = new JFileChooser();

        fc.setDialogTitle("Select your XML");
        fc.setCurrentDirectory(new File(audiopath + "\\.."));
        fc.setFileFilter(new XmlFilter());
        fc.setAcceptAllFileFilterUsed(false);
        fc.requestFocus();
        returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            text = fc.getSelectedFile();

        }
        boolean show = true;
        if (!(returnVal == JFileChooser.APPROVE_OPTION)) {
            return;
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(text), "ISO-8859-1"));
            String buffer = "";
            ra = new <lyric> ArrayList();
            StringTokenizer st = new StringTokenizer(buffer);
            while ((buffer = br.readLine()) != null) {

                if (buffer.contains("<lyric text=\"")) {
                    while (Character.isWhitespace(buffer.charAt(0))) {
                        buffer = buffer.substring(1);
                    }
                    if (debug) {
                        logger.log(buffer);
                    }
                    buffer = buffer.substring(13);
                    st = new StringTokenizer(buffer, "\"");
                    String lyric = st.nextToken();
                    String skip = st.nextToken();
                    while (!skip.contains("time=")) {
                        if (debug) {
                            logger.log(skip);
                        }
                        skip = st.nextToken();
                    }

                    String time = st.nextToken();

                    String toTest = lyric;
                    String toWrite = "";
                    if (toTest.matches("(.*)&#[0-9]+;")) {
                        System.out.println(toTest + " this contains coded html chars");
                    }
                    for (int j = 0; j < toTest.length(); j++) {
                        if (j < toTest.length() - 2 && toTest.charAt(j) == '\'' && toTest.charAt(j + 1) == '\'') {
                            toWrite += "\"";
                            j = j + 1;
                        }
                        else if (j < toTest.length() - 5 && toTest.charAt(j) == '&' && toTest.charAt(j + 1) == 'a' && toTest.charAt(j + 2) == 'm' && toTest.charAt(j + 3) == 'p' && toTest.charAt(j + 4) == ';') {
                            toWrite += "&";
                            j = j + 4;
                        }
                        else if (j < toTest.length() - 4 && toTest.charAt(j) == '&' && toTest.charAt(j + 1) == 'l' && toTest.charAt(j + 2) == 't' && toTest.charAt(j + 3) == ';') {
                            toWrite += "<";
                            j = j + 3;
                        }
                        else if (j < toTest.length() - 4 && toTest.charAt(j) == '&' && toTest.charAt(j + 1) == 'g' && toTest.charAt(j + 2) == 't' && toTest.charAt(j + 3) == ';') {
                            toWrite += ">";
                            j = j + 3;
                        }
                        else {
                            toWrite += toTest.charAt(j);
                        }
                    }
                    if (ra.size() == 0) {
                        ra.add(new lyric(Long.parseLong(time) + 200, toWrite));
                    }
                    else {
                        ra.add(new lyric(Long.parseLong(time), toWrite));
                    }
                    Long test = Long.parseLong(time);

                    if (debug) {
                        logger.log(ra.get(ra.size() - 1) + "    my ulong is: " + test);
                    }
                }

            }
            br.close();
            frommouse = true;
            if (debug) {
                for (int i = 0; i < ra.size(); i++) {
                    logger.log(ra.get(i).getS() + " @ " + ra.get(i).getTime());
                }
            }
            DefaultListModel ls = new DefaultListModel();

            String newText = "";
            for (int i = 0; i < ra.size(); i++) {

                ls.addElement(ra.get(i).toString());
                newText += " " + ra.get(i).getS();
            }
            if (debug) {
                for (int i = 0; i < ra.size(); i++) {
                    logger.log(ra.get(i).getS() + " @ " + ra.get(i).getTime());
                }
            }
            this.jTextPane1.setText(newText);
            //this.jList2.setModel(null);

            this.jList2.setModel(ls);

            // jList2.removeKeyListener(jList2.getKeyListeners()[0]);
            jList2.addKeyListener(kl);
            // jList2.repaint();
            if (ra.size() > 0) {
                jList2.setSelectedIndex(0);
                jList2.ensureIndexIsVisible(0);
            }
            if (debug) {
                logger.log("Index is " + jList2.getSelectedIndex());
            }
            fireDocument = true;
            show = false;
            if (debug) {
                for (int i = 0; i < ra.size(); i++) {
                    logger.log(ra.get(i).getS() + " @ " + ra.get(i).getTime());
                }
            }
        }
        catch (Exception e) {
            if (show) {
                JOptionPane.showMessageDialog(this, "Could not read text file, please copy paste it. ", "Error",
                        JOptionPane.ERROR_MESSAGE);
                if (debug) {
                    Writer writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(writer);
                    e.printStackTrace(printWriter);
                    String output = writer.toString();
                    logger.log(output);
                }
            }
        }

        if (debug) {
            for (int i = 0; i < ra.size(); i++) {
                logger.log(ra.get(i).getS() + " @ " + ra.get(i).getTime());
            }
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        trueClose = true;
        WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);

    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed

        String soundName = awc.getFileName();

        if (debug) {
            logger.log(soundName);
        }
        String Path = "";
        String tempHolder = "";
        String fileName = "";
        StringTokenizer st = new StringTokenizer(soundName, "\\");
        while (st.hasMoreElements()) {
            tempHolder = st.nextToken();
            if (st.hasMoreTokens()) {
                Path += tempHolder + "\\";
            }
            else {
                fileName = tempHolder;
            }
        }

        String fileName2 = "";
        st = new StringTokenizer(fileName, ".");
        while (st.hasMoreElements()) {
            tempHolder = st.nextToken();
            if (st.hasMoreTokens()) {
                fileName2 += tempHolder;
            }
        }
        File file = new File(fileName2 + ".xml");

        int returnVal = 0;

        final JFileChooser fc = new JFileChooser();

        fc.setDialogTitle("Save as XML");
        fc.setCurrentDirectory(new File(audiopath + "\\.."));
        fc.setSelectedFile(file);
        fc.setFileFilter(new XmlFilter());
        fc.setAcceptAllFileFilterUsed(false);
        fc.requestFocus();
        returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = new File(fc.getSelectedFile().getAbsolutePath());
            cancel = 0;

        }

        if (!(returnVal == JFileChooser.APPROVE_OPTION)) {
            cancel = 1;
            return;
        }
        for (int i = 0; i < ra.size(); i++) {
            if (i == 0) {
                ra.get(i).setPositionInText(0);
            }
            else {
                int whitespaces = 0;
                if (debug) {
                    logger.log("" + ra.get(i - 1).getPositionInText() + ra.get(i - 1).getS().length());
                    logger.log(this.jTextPane1.getText().toString().substring(ra.get(i - 1).getPositionInText() + ra.get(i - 1).getS().length()));
                }
                String curr = this.jTextPane1.getText().substring(ra.get(i - 1).getPositionInText() + ra.get(i - 1).getS().length());
                while (Character.isWhitespace(curr.charAt(whitespaces))) {
                    whitespaces++;
                }
                ra.get(i).setPositionInText(ra.get(i - 1).getPositionInText() + ra.get(i - 1).getS().length() + 1 + whitespaces);
                //ra.get(i).setPositionInText(ra.get(-1).getPositionInText()+ra.get(i-1).getS().length() +1); deprecated
            }
        }
        try {
            PrintWriter writer = new PrintWriter(file, "ISO-8859-1");
            writer.print("<lyrics>\n");
            StringTokenizer strtok = new StringTokenizer(this.jTextPane1.getText());
            for (int i = 0; i < ra.size(); i++) {
                String toTest = strtok.nextToken();
                String toWrite = "";
                for (int j = 0; j < toTest.length(); j++) {
                    if (toTest.charAt(j) == '"') {
                        toWrite += "''";
                    }
                    else if (toTest.charAt(j) == '&' && toTest.length() > 1 && toTest.charAt(j + 1) != '#') {
                        toWrite += "&amp;";
                    }
                    else if (toTest.charAt(j) == '<') {
                        toWrite += "&lt;";
                    }
                    else if (toTest.charAt(j) == '•') {
                        toWrite += "&#8226;";
                    }
                    else if (toTest.charAt(j) == '>') {
                        toWrite += "&gt;";
                    }
                    else if (toTest.charAt(j) > 122) {
//              System.out.println("This is hawkward tho");
                        toWrite += "&#" + (int) toTest.charAt(j) + ";";
                    }
                    else if (toTest.charAt(j) == 'Œ') {
                        toWrite += "&#338;";
                    }
                    else if (toTest.charAt(j) == 'ƒ') {
                        toWrite += "&#402;";
                    }
                    else if (toTest.charAt(j) == '—') {
                        toWrite += "&#8212";
                    }
                    else if (toTest.charAt(j) == '–') {
                        toWrite += "&#8211;";
                    }
                    else if (toTest.charAt(j) == '˜') {
                        toWrite += "&#732;";
                    }
                    else if (toTest.charAt(j) == 'ˆ') {
                        toWrite += "&#710;";
                    }
                    else if (toTest.charAt(j) == '‘') {
                        toWrite += "&#8216;";
                    }
                    else if (toTest.charAt(j) == 146) {
                        toWrite += "&#8217;";
                    }
                    else if (toTest.charAt(j) == '‚') {
                        toWrite += "&#8218;";
                    }
                    else if (toTest.charAt(j) == 147) {//opening quotes
                        toWrite += "&#8220;";
                    }
                    else if (toTest.charAt(j) == 148) {//closing quotes
                        toWrite += "&#8221;";
                    }
                    else if (toTest.charAt(j) == '„') {
                        toWrite += "&#8222;";
                    }
                    else if (toTest.charAt(j) == '†') {
                        toWrite += "&#8224;";
                    }
                    else if (toTest.charAt(j) == '‡') {
                        toWrite += "&#8225;";
                    }
                    else if (toTest.charAt(j) == 176) {
                        toWrite += "&#176;";
                    }
                    else if (toTest.charAt(j) == 149) {
                        toWrite += "&#8226;";
                    }
                    else if (toTest.charAt(j) == '…') {
                        toWrite += "&#8230;";
                    }
                    else if (toTest.charAt(j) == '‰') {
                        toWrite += "&#8240;";
                    }
                    else if (toTest.charAt(j) == '′') {
                        toWrite += "&#8242;";
                    }
                    else if (toTest.charAt(j) == '″') {
                        toWrite += "&#8243;";
                    }
                    else if (toTest.charAt(j) == '‹') {
                        toWrite += "&#8249;";
                    }
                    else if (toTest.charAt(j) == '›') {
                        toWrite += "&#8250;";
                    }
                    else if (toTest.charAt(j) == '‾') {
                        toWrite += "&#8254;";
                    }
                    else if (toTest.charAt(j) == '€') {
                        toWrite += "&#8364;";
                    }
                    else if (toTest.charAt(j) == '™') {
                        toWrite += "&#8482;";
                    }
                    else if (toTest.charAt(j) == '←') {
                        toWrite += "&#8592;";
                    }
                    else if (toTest.charAt(j) == '↑') {
                        toWrite += "&#8593;";
                    }
                    else if (toTest.charAt(j) == '→') {
                        toWrite += "&#8594;";
                    }
                    else if (toTest.charAt(j) == '↓') {
                        toWrite += "&#8595;";
                    }
                    else if (toTest.charAt(j) == '↔') {
                        toWrite += "&#8596;";
                    }
                    else if (toTest.charAt(j) == '◊') {
                        toWrite += "&#9674;";
                    }
                    else if (toTest.charAt(j) == '♠') {
                        toWrite += "&#9824;";
                    }
                    else if (toTest.charAt(j) == '♣') {
                        toWrite += "&#9827;";
                    }
                    else if (toTest.charAt(j) == '♥') {
                        toWrite += "&#9829;";
                    }
                    else if (toTest.charAt(j) == '♦') {
                        toWrite += "&#9830;";
                    }
                    else if (toTest.charAt(j) == 9744) {//unchecked boxx
                        toWrite += "&#x2610;";
                    }
                    else if (toTest.charAt(j) == 9745) {//checked box
                        toWrite += "&#x2611;";
                    }
                    else if (toTest.charAt(j) == 150) {//typographic -
                        toWrite += "-";
                    }
                    else {
                        toWrite += toTest.charAt(j);
                    }
                    if (debug) {
                        logger.log("Lyric is " + toTest + " length is " + toTest.length() + " charAt(" + j + ") is " + toTest.charAt(j));
                        System.out.println("Lyric is " + toTest + " length is " + toTest.length() + " charAt(" + j + ") is " + toTest.charAt(j) + "charcode is " + (int) toTest.charAt(j) + " transcoded is " + toWrite);
                    }
                }
                if (i == 0) {
                    if (ra.get(i).getTime() > 200) {
                        writer.print("\t<lyric text=\"" + toWrite + "\"\tstartPosition=\"" + ra.get(i).getPositionInText() + "\"\ttime=\"" + (ra.get(i).getTime() - 200) + "\"/>\n");
                    }
                    else {
                        writer.print("\t<lyric text=\"" + toWrite + "\"\tstartPosition=\"" + ra.get(i).getPositionInText() + "\"\ttime=\"" + ra.get(i).getTime() + "\"/>\n");
                    }
                }
                else {
                    writer.print("\t<lyric text=\"" + toWrite + "\"\tstartPosition=\"" + ra.get(i).getPositionInText() + "\"\ttime=\"" + ra.get(i).getTime() + "\"/>\n");
                }
            }
            writer.print("</lyrics>");
            writer.close();
            hasChanges = false;
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (UnsupportedEncodingException ex) {
            Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        if (hasChanges) {
            JOptionPane warning = new JOptionPane();
            int n = warning.showConfirmDialog(self, "There are unsaved changes! Do you want to save?", "Warning",
                    JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                jMenuItem5ActionPerformed(new java.awt.event.ActionEvent(this, 0, null));
            }
            if (cancel == 1) {
                return;
            }
            cancel = 0;
        }
        String options[] = new String[2];
        options[0] = "Plain text";
        options[1] = "Xml";

        jMenuItem1ActionPerformed(new java.awt.event.ActionEvent(this, 0, null));
        if (globalReturn == 0) {
            return;
        }
        else {
            globalReturn = 0;
        }
        int choice = JOptionPane.showOptionDialog(null, //Component parentComponent
                "Plain text or XML source?", //Object message,
                "Choose an option", //String title
                JOptionPane.YES_NO_OPTION, //int optionType
                JOptionPane.INFORMATION_MESSAGE, //int messageType
                null, //Icon icon,
                options, //Object[] options,
                "Plain text");//Object initialValue 
        if (choice == 0) {
            jMenuItem2ActionPerformed(new java.awt.event.ActionEvent(this, 0, null));
        }
        else {
            jMenuItem3ActionPerformed(new java.awt.event.ActionEvent(this, 0, null));
        }

        this.repaint();
        hasChanges = false;


    }//GEN-LAST:event_jMenuItem6ActionPerformed
    private void repaintList() {
        DefaultListModel ls = new DefaultListModel();

        String newText = "";
        for (int i = 0; i < ra.size(); i++) {

            ls.addElement(ra.get(i));
            newText += " " + ra.get(i).getS();
        }
        
        this.jList2.setModel(ls);
        if (ra.size() > 0) {
            jList2.setSelectedIndex(0);
        }
        
        jList2.repaint();
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        int val = JOptionPane.showConfirmDialog(this, "You are about to erase ALL syncs! Do you really want to do that?", "WARNING!",
                JOptionPane.WARNING_MESSAGE);
        if (val == JOptionPane.YES_OPTION) {
            hasChanges = true;
            for (int i = 0; i < ra.size(); i++) {

                ra.get(i).setTime(-1);
            }
            DefaultListModel ls = new DefaultListModel();

            int old = jList2.getSelectedIndex();
            for (int i = 0; i < ra.size(); i++) {

                ls.addElement(ra.get(i));

            }

            this.jList2.setModel(ls);

            jList2.setSelectedIndex(old);
            jList2.removeKeyListener(jList2.getKeyListeners()[0]);
            jList2.addKeyListener(kl);

            jList2.repaint();
            jScrollPane1.requestFocus();

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        hasChanges = true;
        ra.get(jList2.getSelectedIndex()).setTime(-1);
        DefaultListModel ls = new DefaultListModel();
        int old = jList2.getSelectedIndex();
        for (int i = 0; i < ra.size(); i++) {

            ls.addElement(ra.get(i));

        }

        this.jList2.setModel(ls);

        jList2.setSelectedIndex(old);
        jList2.removeKeyListener(jList2.getKeyListeners()[0]);
        jList2.addKeyListener(kl);

        jList2.repaint();
        jScrollPane1.requestFocus();
    }//GEN-LAST:event_jButton2ActionPerformed

  private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
      if (jCheckBox1.isSelected()) {
          wordscroll = true;
          this.jScrollPane1.requestFocus();
      }
      else {
          wordscroll = false;
          this.jScrollPane1.requestFocus();
      }
  }//GEN-LAST:event_jCheckBox1ActionPerformed

  private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
      if (debug) {
          for (int i = 0; i < ra.size(); i++) {
              logger.log(ra.get(i).getS() + " @ " + ra.get(i).getTime());
          }
      }
      String text = "";
      frommouse = true;
      for (int i = 0; i < ra.size(); i++) {
          text += " " + ra.get(i).getS();
      }
      jTextPane1.setText(text);
      jScrollPane1.requestFocus();
      if (debug) {
          for (int i = 0; i < ra.size(); i++) {
              logger.log(ra.get(i).getS() + " @ " + ra.get(i).getTime());
          }
      }
  }//GEN-LAST:event_jButton3ActionPerformed

  private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
      try {
          String curtext = this.jTextPane1.getDocument().getText(0, this.jTextPane1.getDocument().getLength());
          StringTokenizer strtok = new StringTokenizer(curtext);

          if (strtok.countTokens() >= ra.size()) {
              int counter = 0;
              for (; counter < ra.size(); counter++) {
                  ra.get(counter).setS(strtok.nextToken());
              }
              while (strtok.hasMoreTokens()) {
                  ra.add(new lyric(-1, strtok.nextToken()));
              }
          }
          else {
              int counter = 0;
              while (strtok.hasMoreElements()) {
                  ra.get(counter).setS(strtok.nextToken());
                  counter++;
              }
          }
          DefaultListModel ls = new DefaultListModel();
          for (int i = 0; i < ra.size(); i++) {
              ls.addElement(ra.get(i));
          }
          jList2.setModel(ls);
          jList2.repaint();
//      
//      tempText = curtext;
//      

      }
      catch (BadLocationException ex) {
          Logger.getLogger(mainPanel.class.getName()).log(Level.SEVERE, null, ex);
      }
  }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        int val = JOptionPane.showConfirmDialog(this, "You are about to change the time of all lyrics by " + jSlider1.getValue() + "ms! Do you really want to do that?", "WARNING!",
                JOptionPane.WARNING_MESSAGE);
        if (val == JOptionPane.YES_OPTION) {

            for (int i = 0; i < ra.size(); i++) {
                if (ra.get(i).getTime() + this.jSlider1.getValue() > 0) {
                    ra.get(i).setTime(ra.get(i).getTime() + this.jSlider1.getValue());
                    repaintList();
                }
                else {
                    ra.get(i).setTime(0);
                    repaintList();
                }
            }
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList jList2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTextPane jTextPane1;
    private java.awt.Menu menu1;
    private java.awt.Menu menu2;
    private java.awt.MenuBar menuBar1;
    // End of variables declaration//GEN-END:variables
}
