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
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

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
        @SuppressWarnings("serial")
        JTextArea textarea = new JTextArea(text) {
            public Dimension getMaximumSize()
            {
                Dimension d = super.getMaximumSize();
                d.height = getPreferredSize().height;
                d.width = getPreferredSize().width;
                return d;
            }
        };
        textarea.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(textarea);
        return textarea;
    }

    public static JButton addButton(String text, Container container) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
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
        frame.getContentPane().add(page);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        return frame;
    }

    public static class Handler extends MouseAdapter {
        public static Spline spline = new Spline();
        public static Point recent = new Point(0, 0);
        public static boolean drawing = false;
        private FieldPanel fpan;
        private Graph vpan;
        boolean first = true;

        public Handler(FieldPanel fpan, Graph vpan) {
            this.fpan = fpan;
            this.vpan = vpan;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            drawing = true;
            recent = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            drawing = false;
            spline.points.add(recent);
            fpan.repaint();
            vpan.setScores(spline.distances);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (drawing) {
                recent = e.getPoint();
            }
        }
    }

    @SuppressWarnings("serial")
    public static class FieldPanel extends JPanel {
        BufferedImage img;

        public FieldPanel() throws IOException {
            img = ImageIO.read(new File(currentPath + fs + "resources" + fs + "deepSpace.jpeg"));
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setBackground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, this);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.ORANGE);
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(5,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
            int size = Handler.spline.points.size();
            for (float t = 0.05f; t <= (float) size-3; t += 0.01f) {
                Point ppos = Handler.spline.getPoint(t-0.05f);
                Point pos = Handler.spline.getPoint(t);
                g2.drawLine(ppos.x, ppos.y, pos.x, pos.y);
                if (t >= size-4) {Handler.spline.addDistance(ppos, pos);}
            }

            for (int i = 0; i < size; i++) {
                g2.fillOval(Handler.spline.points.get(i).x-4, Handler.spline.points.get(i).y-4, 10, 10);
            }

            if(size-4 >= 1){ Handler.spline.setZero();}
        }

        @Override
        public Dimension getPreferredSize() {
           return new Dimension(img.getWidth(), img.getHeight());
        }
    }
}
