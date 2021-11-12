package app.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.util.ArrayList;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import app.gui.trajectory.Line;
import app.gui.trajectory.M;
import app.gui.trajectory.Path;
import app.gui.trajectory.Segment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics2D;
public class TrajectoryPlanning extends JFrame implements ActionListener{
    JFrame frame;
    Panel panel;
    JMenuItem savePathToFile = new JMenuItem("Save Path");
    JMenuItem saveVelocity = new JMenuItem("Save Velocity");
    Velocity velocity;
    public void setVelocity(Velocity velocityPlanning){
        velocity = velocityPlanning;
    }
    public void display(){
        frame = new JFrame();
        frame.setTitle("Trajectory Planning");
       
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new Panel("./RobotController\\resources\\deepSpace.jpeg", velocity);
        frame.add(panel);

        
        System.out.printf("position: %d %d", panel.getWidth(), panel.getHeight());
        System.out.println(panel.getPreferredSize());
       

       
        frame.setSize(panel.getPreferredSize());
       
        JMenuBar menuBar = new JMenuBar();
        JMenu menuActions = new JMenu("Actions");
        menuActions.add(savePathToFile);
        menuActions.add(saveVelocity);
        savePathToFile.addActionListener(this);
        
        saveVelocity.addActionListener(this);
        menuBar.add(menuActions);

        frame.setJMenuBar(menuBar);
       
        frame.pack();
        frame.setLocation(GUIConstants.trajectoryPlanningX, GUIConstants.trajectoryPlanningY);
        frame.setVisible(true);
        GUIConstants.trajectoryPlanningWidth = frame.getWidth();
        
        GUIConstants.trajectoryPlanningHeight = frame.getHeight();
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource().equals(savePathToFile)){
            savePath();
        }
        if(e.getSource().equals(saveVelocity)){
            saveV();
        }
        System.out.println("menu clicked");
    }
    public void saveV(){
        try{
            FileWriter velocityWriter = new FileWriter("./RobotController\\memory\\velocity.txt");
            String text = "";
           for(double[] point: panel.velocity.panel.kinematics.velocities)
               text += String.format("%f,%f\n", point[0], point[1]);
            velocityWriter.write(text);
            velocityWriter.close();
        }
        catch(Exception e){
            System.out.println("Couldn't open memory files. Check memory folder. " );
            e.printStackTrace();
        }
    }
    public void savePath(){
        
       
           
        try{
            FileWriter pointsWriter = new FileWriter("./RobotController\\memory\\points.txt");
            String pointsText = "";
           for(double[] point: panel.path.points)
               pointsText += String.format("%f,%f\n", point[0]/GUIConstants.pixels_per_meter, point[1]/GUIConstants.pixels_per_meter);
            pointsWriter.write(pointsText);
            pointsWriter.close();

            FileWriter distancesWriter = new FileWriter("./RobotController\\memory\\distances.txt");
            String distanceText = "";
            for(double distance: panel.path.distances)
               distanceText += String.format("%f\n", distance);
            distancesWriter.write(distanceText);
            distancesWriter.close();
            System.out.println("Path Saved");
        }
        catch(Exception e){
            System.out.println("Couldn't open memory files. Check memory folder. " );
            e.printStackTrace();
        }
    }
    
}


//__________________________________________________________________


class Panel extends JPanel implements MouseInputListener, KeyListener{
    public Image fieldImage;
    public String mode;
    public int[] mousePos = new int[2];

    public Velocity velocity;
    public ArrayList<int[]> dots = new ArrayList<int[]>();
   
    public ArrayList<int[][]>  lines = new ArrayList<int[][]>();
    public ArrayList<double[]> arcs = new ArrayList<double[]>();
    public ArrayList<Double> distances = new ArrayList<Double>();
    public ArrayList<double[]> points = new ArrayList<double[]>();
    public Path path;


    public Panel(String path, Velocity velocityPlanning) {
        velocity = velocityPlanning;
        fieldImage = Toolkit.getDefaultToolkit().getImage(path);
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        mode = "start";
    }

    @Override
    public void paint(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;
        g.drawImage(fieldImage, 0, 0, null);

        g.setColor(GUIConstants.dotColor);
        for(int[] dot: dots){
            g.fillOval(dot[0]-GUIConstants.dotRadius/2, dot[1] - GUIConstants.dotRadius/2, GUIConstants.dotRadius, GUIConstants.dotRadius);
        }
        updateHover(g1);
        g.setColor(GUIConstants.lineColor);
        g.setStroke(GUIConstants.lineStroke);
        for(int[][] line: lines){
            g.drawLine((int)line[0][0], (int)line[0][1], (int)line[1][0], (int)line[1][1]);
        }
        
        for(double[] arc: arcs){
            //x, y, 
            g.drawArc((int)arc[0], (int)arc[1], (int)arc[2], (int)arc[3], (int)arc[4], (int)arc[5]);
        }
      
        velocity.panel.repaint();
        if(path != null)
            displayVelocityPoints(g);
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
      repaint();
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        repaint();
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
      
       repaint();
    }

    public void updateHover(Graphics g){
        if(mode == "stop"){

        }
       else if(!mode.equals("distance")){
            g.setColor(GUIConstants.dotColor);
            g.fillOval(mousePos[0]-GUIConstants.dotRadius/2, mousePos[1] - GUIConstants.dotRadius/2, GUIConstants.dotRadius, GUIConstants.dotRadius);
       }
       else{
            
            int[][] l = lines.get(lines.size()-2);
            Line line = new Line(l[0],l[1]); 
            Line mouseLine = new Line(mousePos, -1/((double)((line.endPoint[1]-line.startPoint[1])/(line.endPoint[0] - line.startPoint[0]))));
            double[] intersection = line.getIntersection(mouseLine);
            int[] dot = {(int)intersection[0], (int)intersection[1]};
            dots.set(dots.size()-1, dot);
            distances.set(distances.size()-1, M.distance(dot, l[1]));
       }
    }
    public void displayVelocityPoints(Graphics g){
        for(double[] point: velocity.panel.points){
           double[] dot = path.getPosition(point[0]).point;
           dot[0] *= GUIConstants.pixels_per_meter;
           dot[1] *= GUIConstants.pixels_per_meter;
           g.setColor(GUIConstants.velocityDotsColor);
           g.fillOval((int)dot[0] - GUIConstants.velocityDotRadius/2, (int)dot[1] - GUIConstants.velocityDotRadius/2, GUIConstants.velocityDotRadius, GUIConstants.velocityDotRadius);

        }
    }
    public void updateShape(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON3){
            mode = "stop";
            lines.remove(lines.size()-1);
        }
        mousePos[0] = e.getX();
        mousePos[1] = e.getY();
        if(mode.equals("start")){
            dots.add(mousePos.clone());
            mode = "first endpoint";
            int[][] line = {mousePos.clone(), mousePos};
            points.add(M.intArrToDouble(line[0]));
            lines.add(line);
        }
        else if(mode.equals("first endpoint")){
            dots.add(mousePos.clone());
            int[][] line = {lines.get(lines.size()-1)[0], mousePos.clone()};
            points.add(M.intArrToDouble(line[1]));
            lines.set(lines.size()-1, line);
            
            int[][] line2 = {mousePos.clone(), mousePos};
           
            lines.add(line2);
            mode = "second endpoint";   
            System.out.println("first endpoint");
        }
        else if(mode.equals("second endpoint")){
            dots.add(mousePos.clone());
            int[][] line = {lines.get(lines.size()-1)[0], mousePos.clone()};
            lines.set(lines.size()-1, line);
            points.add(M.intArrToDouble(line[1]));
            mode = "distance";
            distances.add(Double.valueOf(0));
            System.out.println("second endpoint");
        }
        else if(mode.equals("distance")){
            double[][] p = new double[points.size()][2];
            
            double[] distanceArr = new double[distances.size()];
            double[] angles = new double[distances.size()+1];
            for(int i = 0; i<distanceArr.length; i++){
                distanceArr[i] = distances.get(i)/GUIConstants.pixels_per_meter; 
            }
            for(int i = 0; i<points.size(); i++){
                p[i][0] = points.get(i)[0]/GUIConstants.pixels_per_meter;
                p[i][1] = points.get(i)[1]/GUIConstants.pixels_per_meter;
            }

            path = new Path(p, distanceArr,angles );
            double[] arc = path.segments.get(path.segments.size()-2).toGUI();
            arcs.add(arc);
            mode = "second endpoint";
           
          for(int i = 0; i<path.segments.size(); i+=2){
              Segment segment = path.segments.get(i);
              int[][] line = {{(int)(segment.startPoint[0]*GUIConstants.pixels_per_meter), (int)(segment.startPoint[1]*GUIConstants.pixels_per_meter)},
              {(int)(segment.endPoint[0]*GUIConstants.pixels_per_meter), (int)(segment.endPoint[1]*GUIConstants.pixels_per_meter)}};
              lines.set(i/2, line);
          }
        
          int[][] line2 = {lines.get(lines.size()-1)[1], mousePos};
          lines.add(line2);
           velocity.panel.updateFinalDistance();
        }
        else if(mode.equals("stop") && e.getButton() == MouseEvent.BUTTON1){
            int[][] line2 = {lines.get(lines.size()-1)[1], mousePos};
           
            lines.add(line2);
            mode = "second endpoint";
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
}
