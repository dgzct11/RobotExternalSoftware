package app.gui;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TrajectoryPlanning extends JFrame{
    JFrame frame;
    public void display(){
        frame = new JFrame();
        frame.setTitle("Trajectory Planning");
       
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new Panel("./RobotController\\resources\\deepSpace.jpeg"));
        frame.pack();
        frame.setSize(GUIConstants.trajectoryPlanningWidth, GUIConstants.trajectoryPlanningHeight);
        frame.setLocation(GUIConstants.trajectoryPlanningX, GUIConstants.trajectoryPlanningY);
        frame.setVisible(true);
    }
    
}


//__________________________________________________________________


class Panel extends JPanel {
    Image fieldImage;
    public Panel(String path) {
        fieldImage = Toolkit.getDefaultToolkit().getImage(path);
    }

    @Override
    public void paint(Graphics g) {
        
        
        g.drawImage(fieldImage, 0, 0, null);
    }
}
