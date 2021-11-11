package app.gui;
import java.awt.Color;
import java.awt.Stroke;

import javax.swing.plaf.ColorUIResource;

import java.awt.BasicStroke;
public class GUIConstants {
    public static final double pixels_per_meter = 616/8.23;
    public static final Color velocityDotsColor = new Color(255,0,0);
    public static int controlPanelWidth = 400;
    public static int controlPanelHeight = 800;

    public static int velocityPlanningWidth = 500;
    public static int velocityPlanningHeight = 250;

    public static int trajectoryPlanningWidth = 500;
    public static int trajectoryPlanningHeight = 500;
    
    public static int controlPanelX = 500;
    public static int controlPanelY = 500;

    public static int velocityPlanningX = 0;
    public static int velocityPlanningY = 500;
    
    public static int trajectoryPlanningX = 0;
    public static int trajectoryPlanningY = 0;
    
    
    public static int dotRadius = 15;
    public static Color dotColor = new Color(25,25,100);
    public static Color lineColor = new Color(25,100,25);
    public static BasicStroke lineStroke = new BasicStroke(5);
    public static Color velocityPlanningColor = new Color(255,255,255);
    public static int velocityAxisOffset = 50;
    public static Color velocityAxisColor = new Color(0,0,0);



    public static int distanceX = 800;
    public static int distanceY = 20;
    public static int timeX = distanceX;
    public static int timeY = distanceY + 15;
    public static int velocityDotRadius = 15;
    public static Color velocityLineColor = new Color(255,0,0);
    public static Stroke velocityLineStroke = new BasicStroke(3);
    public static int velocityMaxX = 800;
    public static double maxVelocity = 5;
    public static Color velocityHoverPoint = new Color(0,0,255);
}
