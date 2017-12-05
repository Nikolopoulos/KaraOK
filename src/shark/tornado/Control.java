/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shark.tornado;

import gui.mainPanel;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

/**
 *
 * @author Basil
 */
public class Control {

  public void createNewPanel() {
    try {
      new mainPanel(this);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "GOD DAMN IT LEROY!\n" + e.getLocalizedMessage());
    }
  }

  public void createNewPanel(String args[]) {
    String audio = "none";
    String text = "none";
    String suffix = "";
    boolean debug = false;
    boolean memory = false;
    if (args.length > 0 && args.length < 4) {
      for (int i = 0; i < args.length; i++) {
        StringTokenizer st = new StringTokenizer(args[i], ".");
        while (st.hasMoreTokens()) {

          suffix = st.nextToken();

        }

        if (suffix.equalsIgnoreCase("mp3") && audio.equalsIgnoreCase("none")) {
          audio = args[i];
        }
        if (suffix.equalsIgnoreCase("txt") && text.equalsIgnoreCase("none")) {
          text = args[i];
        }
        if (suffix.equalsIgnoreCase("xml") && text.equalsIgnoreCase("none")) {
          text = args[i];
        }
        if (args[i].equals("true")) {
          debug = true;
        }
        if (args[i].equals("testmem")) {
          memory = true;
        }
      }
    }
    if (args.length == 4) {
      for (int i = 0; i < args.length; i++) {
        StringTokenizer st = new StringTokenizer(args[i], ".");
        while (st.hasMoreTokens()) {

          suffix = st.nextToken();

        }

        if (suffix.equalsIgnoreCase("mp3") && audio.equalsIgnoreCase("none")) {
          audio = args[i];
        }
        if (suffix.equalsIgnoreCase("txt") && text.equalsIgnoreCase("none")) {
          text = args[i];
        }
        if (suffix.equalsIgnoreCase("xml") && text.equalsIgnoreCase("none")) {
          text = args[i];
        }
        if (args[i].equals("true")) {
          debug = true;
        }
        if (args[i].equals("testmem")) {
          memory = true;
        }
      }
    }
    try {
      new mainPanel(this, audio, text, debug,memory);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "GOD DAMN IT LEROY!\n" + e.getLocalizedMessage());
    }
  }

  public void createNewPanel(mainPanel s) {

    s.stopSound();
    //s.setVisible(false);
    //s.reInitialize(this);
    System.gc();
    try {
      s = new mainPanel(this);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "GOD DAMN IT LEROY!\n" + e.getLocalizedMessage());
    }
    //this.createNewPanel();
  }
}
