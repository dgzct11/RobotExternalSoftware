package app.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainWindow {

    public static String currentPath = Paths.get("").toAbsolutePath().toString();
    public static String fs = File.separator;

    public void display() throws IOException {

        Page page = new Page();
        page.setLayout( new BoxLayout(page, BoxLayout.Y_AXIS));

        JTextArea jta = new JTextArea("help") {
            public Dimension getMaximumSize()
            {
                Dimension d = super.getMaximumSize();
                d.height = getPreferredSize().height;
                d.width = getPreferredSize().width;
                return d;
            }
        };
        jta.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton b1 = addButton("Button 1", page);
        addButton("Button 2", page);
        addButton("Button 3", page);
        addButton("Long-Named Button 4", page);
        addButton("5", page);
        page.add(jta);

        b1.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {jta.setText("tu mama");}});
        JFrame frame = new JFrame();

        JScrollPane sPane = new JScrollPane();
        sPane.setViewportView(page);

        frame.setTitle("Robot Controller");
        frame.add(sPane);
        frame.setSize(page.getPreferredSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    private static JButton addButton(String text, Container container) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(button);
        return button;
    }
}

class Page extends JPanel {
    private static final long serialVersionUID = 1L;

    BufferedImage img;

    public Page() throws IOException {
        img = ImageIO.read(new File(MainWindow.currentPath + MainWindow.fs + "resources" + MainWindow.fs + "deepSpace.jpeg"));
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