package app.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class TrajectoryPlanning extends JFrame{
    JFrame frame;
    public void display(){
        frame = new JFrame();
        frame.setTitle("Trajectory Planning");
       
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Panel panel = new Panel("./RobotController\\resources\\deepSpace.jpeg");
        frame.add(panel);

        
        System.out.printf("position: %d %d", panel.getWidth(), panel.getHeight());
        System.out.println(panel.getPreferredSize());
        GUIConstants.trajectoryPlanningWidth = panel.getPreferredSize().width;
        GUIConstants.trajectoryPlanningHeight = panel.getPreferredSize().height;

       
        frame.setSize(panel.getPreferredSize());
        
       
        frame.pack();
        frame.setLocation(GUIConstants.trajectoryPlanningX, GUIConstants.trajectoryPlanningY);
        frame.setVisible(true);
    }
    
}


//__________________________________________________________________


class Panel extends JPanel implements MouseInputListener{
    public Image fieldImage;
    public String mode;
    public int[] mousePos = new int[2];

    public ArrayList<int[]> dots = new ArrayList<int[]>();

    public Panel(String path) {
        fieldImage = Toolkit.getDefaultToolkit().getImage(path);
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        mode = "start";
    }

    @Override
    public void paint(Graphics g) {
        
        g.setColor(new Color(25,93,10));
        g.drawImage(fieldImage, 0, 0, null);
        for(int[] dot: dots){
            g.fillOval(dot[0]-GUIConstants.dotRadius/2, dot[1] - GUIConstants.dotRadius/2, GUIConstants.dotRadius, GUIConstants.dotRadius);
        
        }
        g.fillOval(mousePos[0], mousePos[1], GUIConstants.dotRadius, GUIConstants.dotRadius);
        
    }
    public int getHeight(){
        return fieldImage.getHeight(null);
    }
    public int getWidth(){
        return fieldImage.getWidth(null);
    }
    @Override
    public Dimension getPreferredSize() {
        
        return new Dimension(fieldImage.getWidth(null), fieldImage.getHeight(null));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
      updateShape(e);
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
       
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        mousePos[0] = e.getX();
       
        mousePos[1] = e.getY();
        System.out.printf("\nmousePos: %d %d", mousePos[0], mousePos[1]);
        repaint();
        
    }

    public void updateShape(MouseEvent e){
        mousePos[0] = e.getX();
        mousePos[1] = e.getY();
        if(mode == "start"){
            dots.add(mousePos.clone());
        }
    }
}
