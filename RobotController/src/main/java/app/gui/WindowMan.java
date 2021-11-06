package app.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

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
        JFrame frame = new JFrame();
        frame.setTitle(text);
        frame.setContentPane(page);
        frame.setSize(page.getPreferredSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);  
        frame.pack();
        frame.setVisible(true);
        return frame;
    }
    public static class Page extends JPanel {
        private static final long serialVersionUID = 1L;
    
        BufferedImage img;
    
        public Page(int layout) throws IOException {
            img = ImageIO.read(new File(currentPath + fs + "resources" + fs + "deepSpace.jpeg"));
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
    public static class IntLabel extends JLabel {
        private static final long serialVersionUID = 1L;

        public static ArrayList<ArrayList<Integer>> a = new ArrayList<ArrayList<Integer>>();
        public static ArrayList<Integer> b = new ArrayList<Integer>();

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(5f));
            g2.setColor(Color.GREEN);
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
