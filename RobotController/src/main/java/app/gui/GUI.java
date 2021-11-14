package app.gui;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import app.gui.WindowMan.FieldPanel;
import app.gui.WindowMan.Handler;

public class GUI {
    @SuppressWarnings("unused")
    public void display() throws IOException {

        FieldPanel fpan = new FieldPanel();
        JFrame field = WindowMan.addFrame("Field", fpan);

        ArrayList<Double> scores = new ArrayList<>();
        Random random = new Random();
        int maxDataPoints = 100;
        scores.add(0.0);
        for (int i = 0; i < maxDataPoints ; i++) {
            scores.add(random.nextDouble()*100);
        }

        Graph vpan = new Graph(scores);
        JFrame velocity = WindowMan.addFrame("Velocity", vpan);


        Handler mouseHandler = new Handler(fpan, vpan);
        fpan.addMouseListener(mouseHandler);
        fpan.addMouseMotionListener(mouseHandler);

        JPanel cpan = WindowMan.addPanel(Component.CENTER_ALIGNMENT, BoxLayout.Y_AXIS);
        JButton align = WindowMan.addButton("Align", cpan);
        JButton distance = WindowMan.addButton("Distance", cpan);

        JPanel elePan = WindowMan.addPanel(Component.LEFT_ALIGNMENT, BoxLayout.X_AXIS);
        JButton setHeight = WindowMan.addButton("Set Height", elePan);
        JTextArea shtext = WindowMan.addTA("text", elePan);
        cpan.add(elePan);

        JButton intake = WindowMan.addButton("Start Motors", cpan);

        JFrame controls = WindowMan.addFrame("Controls", cpan);
    }
}