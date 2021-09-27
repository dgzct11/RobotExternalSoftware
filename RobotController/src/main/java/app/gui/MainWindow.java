package app.gui;

import javax.swing.JFrame;

public class MainWindow {
    int windowWidth = 100;
    int windowHeight = 200;
    public void display(){
        JFrame frame = new JFrame();
        frame.setTitle("Robot Controller");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(windowWidth, windowHeight);
    }
}
