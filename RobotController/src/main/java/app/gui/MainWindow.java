package app.gui;

import java.io.*;
import java.nio.file.Paths;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.Toolkit;


import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class MainWindow{
    int windowWidth = 600;
    int windowHeight = 600;
    public static String currentPath = Paths.get("").toAbsolutePath().getParent().getParent().getParent().getParent().toString();
    public static String fs = File.separator;
    public void display(){
        ImageIcon img = new ImageIcon(currentPath + fs + "resources"+ fs +"deepSpace.jpeg");
        Page page = new Page();

        JFrame frame = new JFrame();
        frame.setTitle("Robot Controller");
        frame.add(page);
        frame.setSize(page.getPreferredSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
class Page extends JPanel{
    BufferedImage img;
    BufferedImage robot;
    int STARTX = 350;
    int STARTY = 100;
    public Page() {
        try{
            img = ImageIO.read(new File(MainWindow.currentPath + MainWindow.fs + "resources" + MainWindow.fs + "deepSpace.jpeg"));
        } catch(IOException e) {
            System.out.print("can't find image");
        }
        robot = new BufferedImage(50,50, BufferedImage.TYPE_3BYTE_BGR);
        }
    @Override
    public Dimension getPreferredSize() {
       return new Dimension(img.getWidth(), img.getHeight());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this);
        g.drawImage(robot,STARTX,STARTY,this);
    }
}
