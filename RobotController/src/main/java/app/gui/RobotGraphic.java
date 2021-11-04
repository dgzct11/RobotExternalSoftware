package app.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class RobotGraphic extends JPanel { 
    Rectangle rect;
    public RobotGraphic(int x, int y){
        rect = new Rectangle(x, y, 50, 50);
        setLayout(null);
    }

    @Override
    public Dimension getPreferredSize() {
       return new Dimension(50, 50);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.draw(rect); 
    }
}
