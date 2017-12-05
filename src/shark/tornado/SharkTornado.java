/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shark.tornado;

import javax.swing.JOptionPane;

/**
 *
 * @author Basil
 */
public class SharkTornado {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    /*   String a = "C:\\Documents and Settings\\Basil\\Desktop\\pain\\Planet Of Zeus - Macho Libre\\01 - Doteru";
       
     String b = "C:\\Documents and Settings\\Basil\\Desktop\\aFile";
        
     String c = "C:\\WINDOWS\\Media\\chimes.wav";
        
     String d = "C:\\Documents and Settings\\Basil\\Desktop\\chimes";
        
     String f = "C:\\Documents and Settings\\Basil\\My Documents\\Dropbox\\netbeans projects\\shark tornado\\Shark Tornado\\chimes";
        
     String g = "C:\\Documents and Settings\\Basil\\Desktop\\pain\\Planet Of Zeus - Macho Libre\\04 - Vanity Suit.mp3";
        
     String h = "C:\\Documents and Settings\\Basil\\Desktop\\pain\\Metallica - 1986 - Master Of Puppets\\08 - Damage, Inc.";
        
     String j = "C:\\Documents and Settings\\Basil\\My Documents\\Dropbox\\accounting\\audio\\929.mp3";
        
     */
        //soundRetriever sR = new soundRetriever(g+".mp3");

    //System.out.println(image);
 
    if (args.length != 0) {
      Control c = new Control();
      c.createNewPanel(args);
    } else {
      Control c = new Control();
      c.createNewPanel();
    }

    //DemoImage di = new DemoImage();
    //di.showImage(image);
  }

}
