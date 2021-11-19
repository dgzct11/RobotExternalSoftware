package app.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import javax.swing.event.MouseInputListener;
import java.awt.geom.Path2D;
import java.awt.geom.AffineTransform;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import app.gui.control_panel.SCSetPoint;
import app.gui.trajectory.Circle;
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
    JMenuItem saveSubsytem = new JMenuItem("Save Subsystem Plan");
    JMenuItem editPath = new JMenuItem("Edit Path");
    JMenuItem editVelocity = new JMenuItem("Edit Velocity");
    JMenuItem displayTimeRuler = new JMenuItem("Display Time Ruler");
    JMenuItem simulateRobotPath = new JMenuItem("Simulate Path");

    Velocity velocity;
    SubsystemControlPanel controlPanel;
    public static String currentPath = Paths.get("").toAbsolutePath().toString();
    public static String fs = File.separator;

    public void setVelocity(Velocity velocityPlanning){
        velocity = velocityPlanning;
    }
    public void setControlPanel(SubsystemControlPanel control){
        controlPanel = control;
    }
    public void display(){
        frame = new JFrame();
        frame.setTitle("Trajectory Planning");
       
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new Panel("./RobotController" + fs + "resources" + fs + "Commons_drawing.png", velocity, controlPanel);
        frame.add(panel);
        panel.setFocusable(true);
        frame.setFocusable(true);
     
        frame.requestFocusInWindow();
        panel.requestFocusInWindow();
        System.out.printf("position: %d %d", panel.getWidth(), panel.getHeight());
        System.out.println(panel.getPreferredSize());
       

       
        frame.setSize(panel.getPreferredSize());
       
        JMenuBar menuBar = new JMenuBar();
        JMenu menuActions = new JMenu("Actions");
        menuActions.add(savePathToFile);
        menuActions.add(saveVelocity);
        menuActions.add(saveSubsytem);
        menuActions.add(editPath);
        menuActions.add(editVelocity);
        menuActions.add(displayTimeRuler);
        menuActions.add(simulateRobotPath);
        simulateRobotPath.addActionListener(this);
        displayTimeRuler.addActionListener(this);
        editVelocity.addActionListener(this);
        editPath.addActionListener(this);
        savePathToFile.addActionListener(this);
        saveSubsytem.addActionListener(this);
        saveVelocity.addActionListener(this);
        menuBar.add(menuActions);
        menuActions.setVisible(true);
        frame.setJMenuBar(menuBar);
        menuBar.setVisible(true);
       
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
        else if(e.getSource().equals(saveVelocity)){
            saveV();
        }
        else if(e.getSource().equals(editPath)){
           
            panel.mode = "edit";
            
        }
        else if(e.getSource().equals(editVelocity)){
            panel.velocity.panel.mode = "edit";
        }
        else if(e.getSource().equals(saveSubsytem)){
            saveSubsystems();
        }
        else if(e.getSource().equals(displayTimeRuler)){
            panel.displayTimeRuler = !panel.displayTimeRuler;
        }
        else if(e.getSource().equals(simulateRobotPath)){
            panel.shouldDrawRobot = true;
            panel.initialTime = System.currentTimeMillis()/1000.;
        }
        System.out.println("menu clicked");
    }
    public void saveSubsystems(){
        try{
            FileWriter subsystemWriter = new FileWriter("./RobotController" + fs + "memory" + fs + "subsystem.txt");
            String text = "";
           for(SCSetPoint point:controlPanel.panel.setPoints )
                text += point.toString();
            subsystemWriter.write(text);
            subsystemWriter.close();
        }
        catch(Exception e){
            System.out.println("Couldn't open memory files. Check memory folder. " );
            e.printStackTrace();
        }
    }
    public void saveV(){
        try{
            FileWriter velocityWriter = new FileWriter("./RobotController" + fs + "memory" + fs + "velocity.txt");
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
            FileWriter pointsWriter = new FileWriter("./RobotController" + fs + "memory" + fs + "points.txt");
            String pointsText = "";
           for(double[] point: panel.path.points)
               pointsText += String.format("%f,%f\n", point[1], point[0]);
            pointsWriter.write(pointsText);
            pointsWriter.close();

            FileWriter distancesWriter = new FileWriter("./RobotController" + fs + "memory" + fs + "distances.txt");
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
    public SubsystemControlPanel controlPanel;
    public ArrayList<int[]> dots = new ArrayList<int[]>();
   
    public ArrayList<int[][]>  lines = new ArrayList<int[][]>();
    public ArrayList<double[]> arcs = new ArrayList<double[]>();
    public ArrayList<Double> distances = new ArrayList<Double>();
    public ArrayList<double[]> points = new ArrayList<double[]>();
    public Path path;


    public double initialTime;
    public boolean displayTimeRuler = false;
    public boolean addedDotAfterSecondEndpoint = false;
    public boolean shouldDrawRobot = false;
    public double[] robotPos = new double[2];
    public Panel(String path, Velocity velocityPlanning, SubsystemControlPanel control) {
        velocity = velocityPlanning;
        fieldImage = Toolkit.getDefaultToolkit().getImage(path);
        controlPanel = control;
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.addMouseMotionListener(this);
        setFocusable(true);
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
        if(path != null){
            displayVelocityPoints(g);
            displayControlPanelPoints(g);
            
            drawTimeRuler(g);
        }
        
       
        if(shouldDrawRobot)
            drawRobot(g);
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
     
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        updateShape(e);
        repaint();
        velocity.panel.updateFinalDistance();
        requestFocusInWindow();
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
        requestFocusInWindow();
       repaint();
    }

    public void updateRobotPosition(){
        if(velocity.panel.kinematics != null){
            double time = 0;
            time = System.currentTimeMillis()/1000. - initialTime;
            if(time < velocity.panel.kinematics.totalTime){
                shouldDrawRobot = true;
                
                robotPos = path.getPosition(velocity.panel.kinematics.getDistance(time)).point;
                
            }
            else {
                shouldDrawRobot = false;
            }
        }
        
    }
    public void drawRobot(Graphics g){
        updateRobotPosition();
        int[] pos = M.metersToPixelsInt(robotPos);
        g.setColor(GUIConstants.robotColor);
        
        g.fillRect(pos[0] - (int)(GUIConstants.robotWidth*GUIConstants.pixels_per_meter/2), pos[1] - (int)(GUIConstants.robotWidth*GUIConstants.pixels_per_meter/2), (int)(GUIConstants.robotWidth*GUIConstants.pixels_per_meter), (int)(GUIConstants.robotWidth*GUIConstants.pixels_per_meter));
     
        repaint(pos[0] - (int)(GUIConstants.robotWidth*GUIConstants.pixels_per_meter/2), pos[1] - (int)(GUIConstants.robotWidth*GUIConstants.pixels_per_meter/2), (int)(GUIConstants.robotWidth*GUIConstants.pixels_per_meter), (int)(GUIConstants.robotWidth*GUIConstants.pixels_per_meter));
    }
    public void updateHover(Graphics g){
        if(mode == "stop"){

        }
        else if(mode.equals("edit distance")){
            int index = 0;
            double minDistance = M.distance(path.points[0], mousePos);
            for(int i = 0; i<path.points.length; i++){
                if(M.distance(M.metersToPixels(path.points[i]), mousePos) < minDistance){
                    index = i;
                    minDistance =  M.distance(M.metersToPixels(path.points[i]), mousePos);
                }
            }
            double[] point = path.points[index];
            
          
            g.setColor(GUIConstants.highlightColor);
            g.fillOval((int)(point[0]  *GUIConstants.pixels_per_meter)-GUIConstants.highlightRadius/2, (int)(point[1]  *GUIConstants.pixels_per_meter) - GUIConstants.highlightRadius/2, GUIConstants.highlightRadius, GUIConstants.highlightRadius);
         
            g.setColor(GUIConstants.dotColor);
            g.fillOval((int)(point[0]  *GUIConstants.pixels_per_meter)-GUIConstants.dotRadius/2, (int)(point[1]  *GUIConstants.pixels_per_meter) - GUIConstants.dotRadius/2, GUIConstants.dotRadius, GUIConstants.dotRadius);
     
        }
        else if(mode.equals("edit distance drag")){

            int index = 1;
            double minDistance = M.distance(M.metersToPixels(path.points[1]), mousePos);
            for(int i = 1; i<path.points.length-1; i++){
                if(M.distance(M.metersToPixels(path.points[i]), mousePos) < minDistance){
                    index = i;
                    minDistance =  M.distance(M.metersToPixels(path.points[i]), mousePos);
                }
            }
            int[][] l = lines.get(index-1);
            Line line = new Line(l[0],l[1]); 
            Line mouseLine = new Line(mousePos, -1/((double)((line.endPoint[1]-line.startPoint[1])/(line.endPoint[0] - line.startPoint[0]))));
            double[] intersection = line.getIntersection(mouseLine);
            System.out.println(intersection[0] + " " + intersection[1]);
            System.out.println(l[1][0] + " " + l[1][1]);
            System.out.println("index: " + index );
            distances.set(index-1, M.distance(intersection, points.get(index)));
            System.out.println(distances.get(index-1));
            updatePath();
        }
        else if(mode.equals("edit drag")){
            int index = 0;
            double minDistance = M.distance(M.metersToPixels(path.points[0]), mousePos);
            for(int i = 0; i<path.points.length; i++){
                if(M.distance(M.metersToPixels(path.points[i]), mousePos) < minDistance){
                    index = i;
                    minDistance =  M.distance(M.metersToPixels(path.points[i]), mousePos);
                }
            }
            double[] point = new double[2];
            point[0] = mousePos[0];
            point[1] = mousePos[1];
            points.set(index, point);
           
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

        
            lines.clear();
            arcs.clear();
            dots.clear();

           
            for(Segment segment: path.segments){
              
                if(segment instanceof Line){
                    int[][] line = {M.doubleArrToInt(M.metersToPixels( segment.startPoint)),M.doubleArrToInt(M.metersToPixels(segment.endPoint))};
                    lines.add(line);
                }
                else if(segment instanceof Circle){
                    arcs.add(((Circle)segment).toGUI());
                }
                dots.add(M.metersToPixelsInt(segment.startPoint));
              dots.add(M.metersToPixelsInt(segment.endPoint));
            }
            for(double[] pForLoop: points){
                dots.add(M.doubleArrToInt(pForLoop));
            }
            
           
         
           velocity.panel.updateFinalDistance();
           
        }
        
     
        else if(mode.equals("edit")){
            int index = 0;
            double minDistance = M.distance(path.points[0], mousePos);
            for(int i = 0; i<path.points.length; i++){
                if(M.distance(M.metersToPixels(path.points[i]), mousePos) < minDistance){
                    index = i;
                    minDistance =  M.distance(M.metersToPixels(path.points[i]), mousePos);
                }
            }
            double[] point = path.points[index];
            
          
            g.setColor(GUIConstants.highlightColor);
            g.fillOval((int)(point[0]  *GUIConstants.pixels_per_meter)-GUIConstants.highlightRadius/2, (int)(point[1]  *GUIConstants.pixels_per_meter) - GUIConstants.highlightRadius/2, GUIConstants.highlightRadius, GUIConstants.highlightRadius);
         
            g.setColor(GUIConstants.dotColor);
            g.fillOval((int)(point[0]  *GUIConstants.pixels_per_meter)-GUIConstants.dotRadius/2, (int)(point[1]  *GUIConstants.pixels_per_meter) - GUIConstants.dotRadius/2, GUIConstants.dotRadius, GUIConstants.dotRadius);
     
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
            if(addedDotAfterSecondEndpoint)
                dots.set(dots.size()-1, dot);
            else{
                dots.add(dot);
                addedDotAfterSecondEndpoint = true;
            }
            distances.set(distances.size()-1, M.distance(dot, l[1]));
       }
    }
    public void displayVelocityPoints(Graphics g){
        for(double[] point: velocity.panel.points){
           double[] dot = path.getPosition(point[0]).point;
           dot[0] *= GUIConstants.pixels_per_meter;
           dot[1] *= GUIConstants.pixels_per_meter;
           g.setColor(GUIConstants.velocityDotsColor);
           g.fillRect((int)dot[0] - GUIConstants.velocityDotRadius/2, (int)dot[1] - GUIConstants.velocityDotRadius/2, GUIConstants.velocityDotRadius, GUIConstants.velocityDotRadius);
           
        }
    }
    public void displayControlPanelPoints(Graphics2D g){
        
        for(SCSetPoint point:  controlPanel.panel.setPoints){
            double[] dot = path.getPosition(point.startDistance).point;
            dot[0] *= GUIConstants.pixels_per_meter;
            dot[1] *= GUIConstants.pixels_per_meter;
            g.setColor(GUIConstants.controlPanelDotColor);
            
            g.fillOval((int)dot[0] - GUIConstants.controlPanelDotRadius/2, (int)dot[1] - GUIConstants.controlPanelDotRadius/2, GUIConstants.controlPanelDotRadius, GUIConstants.controlPanelDotRadius);

            dot = path.getPosition(point.endDistance).point;
            dot[0] *= GUIConstants.pixels_per_meter;
            dot[1] *= GUIConstants.pixels_per_meter;
            g.setColor(GUIConstants.controlPanelDotColor);
            
            g.fillOval((int)dot[0] - GUIConstants.controlPanelDotRadius/2, (int)dot[1] - GUIConstants.controlPanelDotRadius/2, GUIConstants.controlPanelDotRadius, GUIConstants.controlPanelDotRadius);
            
            drawPathPortion(point.startDistance, point.endDistance, point.color, g);

            if(point.subsystemIdentifier.equals("navx")){
                double[] pos = path.getPosition((point.startDistance + point.endDistance)/2).point;
                pos[0] *= GUIConstants.pixels_per_meter;
                pos[1] *= GUIConstants.pixels_per_meter;
                drawArrow(g, pos[0], pos[1], point.inputs.get(0));
            }
        }
    }
    public void drawArrow(Graphics2D g, double x, double y, double angle){
        Arrow arrow = new Arrow();
        
    AffineTransform oldXForm = g.getTransform();    
        angle = -angle;
        AffineTransform at = new AffineTransform();
        at.translate(2*(x), 2*(y- arrow.getBounds().getHeight()/4));
        at.rotate(Math.toRadians(angle), 0, arrow.getBounds().getHeight()/2);
        g.setTransform(at);
        
        g.setStroke(GUIConstants.arrowStroke);
        g.draw(arrow);
        g.setTransform(oldXForm);
       
    }
    public void updatePath(){
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

        
            lines.clear();
            arcs.clear();
            dots.clear();

           
            for(Segment segment: path.segments){
              
                if(segment instanceof Line){
                    int[][] line = {M.doubleArrToInt(M.metersToPixels( segment.startPoint)),M.doubleArrToInt(M.metersToPixels(segment.endPoint))};
                    lines.add(line);
                }
                else if(segment instanceof Circle){
                    arcs.add(((Circle)segment).toGUI());
                }
                dots.add(M.metersToPixelsInt(segment.startPoint));
              dots.add(M.metersToPixelsInt(segment.endPoint));
            }
            for(double[] pForLoop: points){
                dots.add(M.doubleArrToInt(pForLoop));
            }
            
    }
    public void updateShape(MouseEvent e){
        mousePos[0] = e.getX();
        mousePos[1] = e.getY();
        if(e.getButton() == MouseEvent.BUTTON3){
            if(mode.equals("second endpoint"))
            lines.remove(lines.size()-1);
            mode = "stop";
            
        }
        else if(e.getButton() == MouseEvent.BUTTON2){
            mode = "edit";
        }
        
       else  if(mode.equals("start")){
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
            addedDotAfterSecondEndpoint = false;
        }
        else if(mode.equals("distance")){
            dots.remove(dots.size()-1);
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
            System.out.printf("\nPoints: TrjPlann: %f %f\n", path.getEndPoint().x, path.getEndPoint().y);
            double[] arc = path.segments.get(path.segments.size()-2).toGUI();
            arcs.add(arc);
            mode = "second endpoint";
            
          for(int i = 0; i<path.segments.size(); i+=2){
              Segment segment = path.segments.get(i);
              int[][] line = {{(int)(segment.startPoint[0]*GUIConstants.pixels_per_meter), (int)(segment.startPoint[1]*GUIConstants.pixels_per_meter)},
              {(int)(segment.endPoint[0]*GUIConstants.pixels_per_meter), (int)(segment.endPoint[1]*GUIConstants.pixels_per_meter)}};
              lines.set(i/2, line);
              dots.add(M.metersToPixelsInt(segment.startPoint));
              dots.add(M.metersToPixelsInt(segment.endPoint));
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
        else if(mode.equals("edit")){
            mode = "edit drag";
        }
        else if(mode.equals("edit drag")){
            mode = "edit";
        }
        else if(mode.equals("edit distance")){
            mode = "edit distance drag";
        }
        

    }

    public void drawTimeRuler(Graphics g){
        if(displayTimeRuler && velocity.panel.kinematics != null){
            g.setColor(GUIConstants.timeRulerColor);
            for(int i = 0; i<velocity.panel.kinematics.totalTime ; i++){
                double[] p1 = path.getPosition(velocity.panel.kinematics.getDistance(i)).point;
                int[] point = {(int)(p1[0]*GUIConstants.pixels_per_meter), (int)(p1[1] * GUIConstants.pixels_per_meter)};
                if(i % 5 == 0){
                    g.fillOval((int)point[0] - GUIConstants.FiveSecondDotRadius/2, (int)point[1] - GUIConstants.FiveSecondDotRadius/2, GUIConstants.FiveSecondDotRadius, GUIConstants.FiveSecondDotRadius);
 
                }
                else{
                    g.fillOval((int)point[0] - GUIConstants.OneSecondDotRadius/2, (int)point[1] - GUIConstants.OneSecondDotRadius/2, GUIConstants.OneSecondDotRadius, GUIConstants.OneSecondDotRadius);
 
                }
            }
           
        }
    }

    public void drawPathPortion(double startDistance, double endDistance, Color color, Graphics2D g){
        double[] startPoint = path.getPosition(startDistance).point;
        double[] endPoint = path.getPosition(endDistance).point;
        int startIndex = path.getIndex(startDistance);
        int endIndex = path.getIndex(endDistance);
        g.setColor(color);
        g.setStroke(GUIConstants.controlPanelSegmentStroke);
        if(startIndex == endIndex){
            if(path.segments.get(startIndex) instanceof Line){
                g.drawLine((int)(startPoint[0] * GUIConstants.pixels_per_meter), (int)(startPoint[1] * GUIConstants.pixels_per_meter), (int)(endPoint[0]*GUIConstants.pixels_per_meter), (int)(endPoint[1]*GUIConstants.pixels_per_meter));
       
            }
            else{
                Circle circle = (Circle)path.segments.get(startIndex);

                Circle newCircle = new Circle(circle.center, circle.radius, startPoint, endPoint);
                double[] arc = newCircle.toGUI();
                g.drawArc((int)arc[0], (int)arc[1], (int)arc[2], (int)arc[3], (int)arc[4], (int)arc[5]);
        
            }
            return;
            
        }
        for(int i = startIndex; i<=endIndex; i++){
            double[] start = path.segments.get(i).startPoint;
            double[] end = path.segments.get(i).endPoint;
            if(i == startIndex){
                start = startPoint;
            }
            else if(i == endIndex){
                end = endPoint;
            }
            if(path.segments.get(i) instanceof Circle){
                Circle circle = (Circle)path.segments.get(i);

                Circle newCircle = new Circle(circle.center, circle.radius, start, end);
                double[] arc = newCircle.toGUI();
                g.drawArc((int)arc[0], (int)arc[1], (int)arc[2], (int)arc[3], (int)arc[4], (int)arc[5]);
            }
            else{
               

                g.drawLine((int)(start[0] * GUIConstants.pixels_per_meter), (int)(start[1] * GUIConstants.pixels_per_meter), (int)(end[0]*GUIConstants.pixels_per_meter), (int)(end[1]*GUIConstants.pixels_per_meter));
       
        
            }
        }

    }
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        if(mode.equals("edit") && e.getKeyCode() == KeyEvent.VK_D){
            mode = "edit distance";
        }
        System.out.println("key typed");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        if(e.getKeyCode() == KeyEvent.VK_D){
            mode = "edit distance";
        }
        System.out.println("key typed");
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
}



class Arrow extends Path2D.Double {

    public Arrow() {
        double arrow_breadth = 36;
        double length = 100;
        double arrow_length = 40;
        moveTo(0, arrow_breadth/2);
        lineTo(length, arrow_breadth/2);
        
        moveTo(length - arrow_length, 0);
        lineTo(length, arrow_breadth/2);
        moveTo(length - arrow_length, arrow_breadth);
        lineTo(length, arrow_breadth/2);
        
    }

}