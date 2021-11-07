package app.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import app.gui.WindowMan.IntPanel;
import app.gui.WindowMan.Page;

public class MainWindow {
    
    public void display() throws IOException {

        //Create the page
        Page page = new Page(BoxLayout.Y_AXIS);

        //Panel 1
        JPanel pan1 = WindowMan.addPanel(Component.CENTER_ALIGNMENT, BoxLayout.X_AXIS);
        JButton b1 = WindowMan.addButton("Button 1", pan1);
        JTextArea jta1 = WindowMan.addTA("help", pan1);
        JButton b6 = WindowMan.addButton("Button 1", pan1);
        JTextArea jta6 = WindowMan.addTA("help", pan1);
        JButton b7 = WindowMan.addButton("Button 1", pan1);
        JTextArea jta7 = WindowMan.addTA("help", pan1);
        JButton b8 = WindowMan.addButton("Button 1", pan1);
        JTextArea jta8 = WindowMan.addTA("help", pan1);
        JButton b9 = WindowMan.addButton("Button 1", pan1);
        JTextArea jta9 = WindowMan.addTA("help", pan1);

        //Panel 2
        JPanel pan2 = WindowMan.addPanel(Component.LEFT_ALIGNMENT, BoxLayout.Y_AXIS);
        JButton b2 = WindowMan.addButton("Button 1", pan2);
        JTextArea jta2 = WindowMan.addTA("help", pan2);
        JButton b3 = WindowMan.addButton("Button 1", pan2);
        JTextArea jta3 = WindowMan.addTA("help", pan2);

        //Panel 3
        IntPanel pan3 = new IntPanel();

        //Panel 4
        JPanel pan4 = WindowMan.addPanel(Component.RIGHT_ALIGNMENT, BoxLayout.Y_AXIS);
        JButton b4 = WindowMan.addButton("Button 1", pan4);
        JTextArea jta4 = WindowMan.addTA("help", pan4);
        JButton b5 = WindowMan.addButton("Button 1", pan4);
        JTextArea jta5 = WindowMan.addTA("help", pan4);
        
        //Panel 5, the holding panel
        JPanel pan5 = WindowMan.addPanel(Component.CENTER_ALIGNMENT, BoxLayout.X_AXIS);

        //Add panels to page
        page.add(pan1);
        pan5.add(pan2);
        pan5.add(pan3);
        pan5.add(pan4);
        page.add(pan5);

        //Button functions
        b2.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {jta2.setText("tu mama");}});
        b3.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {jta3.setText("tu mama");}});
        b4.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {jta4.setText("tu mama");}});
        b5.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {jta5.setText("tu mama");}});

        //Make the frame to display everything
        JFrame frame = WindowMan.addFrame("Robot Control", page);
    }
}