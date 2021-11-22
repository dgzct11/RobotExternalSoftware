package app.gui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;

import javax.swing.plaf.ColorUIResource;

import java.awt.BasicStroke;
public class GUIConstants {

    //General Constants
    public static final double pixels_per_meter = 986/13.5;
    public static Color highlightColor = new Color(0,0,0);
    public static int highlightRadius = 80;

    public static Color timeRulerColor = new Color(0,0,0);
    public static int FiveSecondDotRadius = 20;
    public static int OneSecondDotRadius = 15;
    public static int halfSecondDotRadius = 10;

    public static Color robotColor = new Color(200,0,0);
    public static double robotWidth = 0.8763;

    public static Stroke controlPanelSegmentStroke = new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
    
    public static Color navXColor = new Color(255,0,0);

    public static Stroke arrowStroke = new BasicStroke(8);
    //trajectory Panel
    public static int trajectoryPlanningWidth = 500;
    public static int trajectoryPlanningHeight = 500;

     
    public static int trajectoryPlanningX = 0;
    public static int trajectoryPlanningY = 0;

    public static int dotRadius = 15;
    public static Color dotColor = new Color(255,127,80);
    public static Color lineColor = new Color(48, 213, 200);
    public static BasicStroke lineStroke = new BasicStroke(5);

   

    //velocity Panel
    public static final Color velocityDotsColor = new Color(255,0,255);

    public static Color velocityTextColor = new Color(255,255,255);

    public static int velocityPlanningWidth = 500;
    public static int velocityPlanningHeight = 250;

    public static int velocityPlanningX = 0;
    public static int velocityPlanningY = 500;

    public static Color velocityPlanningColor = new Color(49,49,49);
    public static int velocityAxisOffset = 50;
    public static Color velocityAxisColor = new Color(255,255,255);

    public static int distanceX = 800;
    public static int distanceY = 20;
    public static int timeX = distanceX;
    public static int timeY = distanceY + 15;

    public static int velocityDotRadius = 15;
    public static Color velocityLineColor = new Color(204,204,255);
    public static Stroke velocityLineStroke = new BasicStroke(3);
    public static int velocityMaxX = 800;
    public static double maxVelocity = 5;
    public static Color velocityHoverPoint = new Color(0,0,255);


    public static Stroke velocityAxisStroke = new BasicStroke(3);
     //Control Panel
     public static int controlPanelWidth = 400;
     public static int controlPanelHeight = 800;
 
     public static int controlPanelX = 500;
     public static int controlPanelY = 500;

    public static Color controlPanelColor = new Color(255,255,255);

    public static Color controlPanelAxisColor = new Color(16, 52, 166);
    public static Stroke controlPanelAxisStroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);

    public static Color controlPanelTextColor = new Color(0,0,0);

    public static int controlPanelLeftTextX = 100;

    public static Font controlPanelFont = new Font("Verdana", Font.PLAIN, 18);

    public static int controlPanelButtonsLeftX = 130;

    public static int controlPanelPadding = 20;

    public static int controlPanelDotRadius = 20;

    public static Color controlPanelBackground = new Color(200,200,200);

    public static Color controlPanelDotColor = new Color(137, 207, 240);
    public static Color velocityHighlightColor = new Color(255, 255, 255);








   

    public static final int controlPanelAxisOffset = 50;
}
