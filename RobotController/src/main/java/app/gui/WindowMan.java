package app.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class WindowMan {
    public static String currentPath = Paths.get("").toAbsolutePath().toString();
    public static String fs = File.separator;

    public static JTextArea addTA(String text, Container container) {
        JTextArea textarea = new JTextArea(text) {
            private static final long serialVersionUID = 1L;

            public Dimension getMaximumSize()
            {
                Dimension d = super.getMaximumSize();
                d.height = getPreferredSize().height;
                d.width = getPreferredSize().width;
                return d;
            }
        };
        textarea.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(textarea);
        return textarea;
    }

    public static JButton addButton(String text, Container container) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(button);
        return button;
    }

    public static JPanel addPanel(float alingment, int order) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, order));
        panel.setAlignmentX(alingment);
        return panel;
    }

    public static JFrame addFrame(String text, JPanel page) {
        JFrame frame = new JFrame(text);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(page);
        frame.setResizable(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }

    //___________________________________________________________________________________________________


    public static class Page extends JPanel {
        private static final long serialVersionUID = 1L;
    
        BufferedImage img;
    
        public Page(int layout) throws IOException {
            img = ImageIO.read(new File("./RobotController"+ fs + "resources" + fs + "deepSpace.jpeg"));
            this.setLayout(new BoxLayout(this, layout));
        }
    
        @Override
        public Dimension getPreferredSize() {
           return new Dimension(img.getWidth(), img.getHeight());
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, this);
        }
    }

    //_______________________________________________________________________________________________

    public static class IntPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private MouseHandler mouseHandler = new MouseHandler();
        private ArrayList<Point[]> points = new ArrayList<Point[]>();
        private Point p1 = new Point(0, 0);
        private Point p2 = new Point(0, 0);
        private boolean drawing;

        public IntPanel() {
            this.setBackground(Color.BLACK);
            this.addMouseListener(mouseHandler);
            this.addMouseMotionListener(mouseHandler);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.GREEN);
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(5,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
            for (int i = 0; i < points.size(); i++) {
                Line2D line = new Line2D.Float(points.get(i)[0], points.get(i)[1]);
                g2.draw(line);
            }

            Line2D linew = new Line2D.Float(p1, p2);
            g2.draw(linew);
        }

        // _______________________________________________________________________________
        
        private class MouseHandler extends MouseAdapter {

            @Override
            public void mousePressed(MouseEvent e) {
                drawing = true;
                p1 = e.getPoint();
                p2 = p1;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                drawing = false;
                p2 = e.getPoint();
                points.add(new Point[] {p1,p2});
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (drawing) {
                    p2 = e.getPoint();
                    repaint();
                }
            }
        }

        @Override
        public Dimension getMaximumSize()
        {
            Dimension d = super.getMaximumSize();
            d.height = 410;
            d.width = 410;
            return d;
        }
    }
}
