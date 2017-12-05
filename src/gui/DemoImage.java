package gui;

import javax.swing.*;

public class DemoImage extends JFrame {

    public void showImage(String images) {

        // creates the actual frame with title 'My GUI' and dimensions
        JFrame frame = new JFrame("Shark Tornado");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(768, 300);
        frame.setResizable(true);

        frame.setLocationRelativeTo(null);

		// Inserts the image icon
        ImageIcon image = new ImageIcon(images);
        JLabel label1 = new JLabel(" ", image, JLabel.CENTER);
        javax.swing.JScrollPane myScroll = new javax.swing.JScrollPane();
        myScroll.setName("waveForm");
        myScroll.setViewportView(label1);
        myScroll.setSize(frame.getWidth(), 200);
        myScroll.validate();
        
        frame.getContentPane().add(myScroll);
        System.out.println(frame.getContentPane().getComponent(frame.getComponentCount()-1).getName());
        frame.getContentPane().getComponent(frame.getComponentCount()-1).setSize(frame.getWidth(), 200);
        
        

        frame.validate();
        frame.setVisible(true);

    }

}
