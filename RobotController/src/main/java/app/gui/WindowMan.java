package app.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Stroke;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;

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
        public static Point initial = new Point(0, 0);
        public static Point recent = new Point(0, 0);
        public static boolean drawing = false;
        private JPanel fpan;
        private JPanel vpan;
        boolean first = true;

        public Handler(JPanel fpan, JPanel vpan) {
            this.fpan = fpan;
            this.vpan = vpan;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            drawing = true;
            recent = e.getPoint();
            fpan.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            drawing = false;
            fpan.repaint();
            spline.points.add(recent);
            if (first) {initial = recent; first = false;}
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (drawing) {
                recent = e.getPoint();
                fpan.repaint();
            }
        }
    }

    @SuppressWarnings("serial")
    public static class FieldPanel extends JPanel {
        BufferedImage img;
        //boolean witch;

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
            Path2D.Double path = new Path2D.Double();
            g2.fillOval(Handler.initial.x-4, Handler.initial.y-4, 10,10);
            for (int i = 0; i < Handler.spline.points.size(); i++) {
                for (float t = 0; t < (float) i-2; t += 0.05f) {
                    Point pos = Handler.spline.getPoint(t);
                    path.moveTo(pos.x/2, pos.y/2);
                    path.lineTo(pos.x/2, pos.y/2);
                }
                g2.fillOval(Handler.spline.points.get(i).x-4, (int)Handler.spline.points.get(i).y-4, 10, 10);
            }
            g2.fillOval(Handler.recent.x-4, Handler.recent.y-4, 10, 10);
            g2.draw(path);
        }

        @Override
        public Dimension getPreferredSize() {
           return new Dimension(img.getWidth(), img.getHeight());
        }
    }

    @SuppressWarnings("serial")
    public static class Graph extends JPanel {

        private int width = 800;
        private int heigth = 420;
        private int padding = 10;
        private int labelPadding = 32;
        private Color lineColor = new Color(44, 102, 230, 180);
        private Color pointColor = new Color(100, 100, 100, 180);
        private Color gridColor = new Color(200, 200, 200, 200);
        private final Stroke GRAPH_STROKE = new BasicStroke(2f);
        private int pointWidth = 4;
        private int numberYDivisions = 10;
        private List<Double> scores;
    
        public Graph(List<Double> scores) {
            this.scores = scores;
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
            double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (scores.size() - 1);
            double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());
    
            List<Point> graphPoints = new ArrayList<>();
            for (int i = 0; i < scores.size(); i++) {
                int x1 = (int) ((getMaxScore() - scores.get(i)) * xScale + padding + labelPadding);
                int y1 = (int) (i * yScale + padding);
                graphPoints.add(new Point(x1, y1));
            }
    
            // draw white background
            g2.setColor(Color.WHITE);
            g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
            g2.setColor(Color.BLACK);
    
            // create hatch marks and grid lines for y axis.
            for (int i = 0; i < numberYDivisions + 1; i++) {
                int x0 = padding + labelPadding;
                int x1 = pointWidth + padding + labelPadding;
                int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
                int y1 = y0;
                if (scores.size() > 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                    g2.setColor(Color.BLACK);
                    String yLabel = ((int) ((getMinScore() + (getMaxScore() - getMinScore()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(yLabel);
                    g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }
    
            // and for x axis
            for (int i = 0; i < scores.size(); i++) {
                if (scores.size() > 1) {
                    int x0 = i * (getWidth() - padding * 2 - labelPadding) / (scores.size() - 1) + padding + labelPadding;
                    int x1 = x0;
                    int y0 = getHeight() - padding - labelPadding;
                    int y1 = y0 - pointWidth;
                    if ((i % ((int) ((scores.size() / 20.0)) + 1)) == 0) {
                        g2.setColor(gridColor);
                        g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                        g2.setColor(Color.BLACK);
                        String xLabel = i + "";
                        FontMetrics metrics = g2.getFontMetrics();
                        int labelWidth = metrics.stringWidth(xLabel);
                        g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                    }
                    g2.drawLine(x0, y0, x1, y1);
                }
            }
    
            // create x and y axes 
            g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
            g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);
    
            Stroke oldStroke = g2.getStroke();
            g2.setColor(lineColor);
            g2.setStroke(GRAPH_STROKE);
            for (int i = 0; i < graphPoints.size() - 1; i++) {
                int x1 = graphPoints.get(i).x;
                int y1 = graphPoints.get(i).y;
                int x2 = graphPoints.get(i + 1).x;
                int y2 = graphPoints.get(i + 1).y;
                g2.drawLine(x1, y1, x2, y2);
            }
    
            g2.setStroke(oldStroke);
            g2.setColor(pointColor);
            for (int i = 0; i < graphPoints.size(); i++) {
                int x = graphPoints.get(i).x - pointWidth / 2;
                int y = graphPoints.get(i).y - pointWidth / 2;
                int ovalW = pointWidth;
                int ovalH = pointWidth;
                g2.fillOval(x, y, ovalW, ovalH);
            }
        }
    
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(width, heigth);
        }
        private double getMinScore() {
            double minScore = Double.MAX_VALUE;
            for (Double score : scores) {
                minScore = Math.min(minScore, score);
            }
            return minScore;
        }
    
        private double getMaxScore() {
            double maxScore = Double.MIN_VALUE;
            for (Double score : scores) {
                maxScore = Math.max(maxScore, score);
            }
            return maxScore;
        }
    
        public void setScores(List<Double> scores) {
            this.scores = scores;
            invalidate();
            this.repaint();
        }
    
        public List<Double> getScores() {
            return scores;
        }
    }
}
