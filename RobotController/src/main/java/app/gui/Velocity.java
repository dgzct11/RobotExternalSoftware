package app.gui;

import javax.swing.JFrame;

public class Velocity extends JFrame{
    JFrame frame;
    public void display(){
        frame = new JFrame();
        frame.setTitle("Velocity Planning");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
}
