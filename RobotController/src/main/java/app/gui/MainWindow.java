package app.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainWindow {
    public static String currentPath = Paths.get("").toAbsolutePath().toString();
    public static String fs = File.separator;

    public void display() throws IOException {
        BufferedImage img  = ImageIO.read(new File(currentPath + fs + "resources" + fs + "deepSpace.jpeg"));
        JLabel label = new JLabel(new ImageIcon(img));
        JPanel panel = new JPanel();
        panel.add(label);

        JFrame frame = new JFrame();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}