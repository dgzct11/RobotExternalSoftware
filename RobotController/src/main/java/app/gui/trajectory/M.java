package app.gui.trajectory;

import app.gui.GUIConstants;

public class M {
  
  //subsystems

 

  
  public static double navxTo360(double angle){
        
    if (angle<=0) angle += 360;

    return 360-angle;
  }
  public static double to360(double angle) {
    if (angle <= 0) angle += 360;

    return Math.abs(angle%360);
  }
  public static double stickTo360(double x, double y){
   return (Math.toDegrees(Math.atan2(-x, y)) +360 )%360;
  }
  public static boolean shouldTurnLeftNavx(double currentNavxAngle, double targetAngle){
    double angle = navxTo360(currentNavxAngle);
    boolean value = false;

    if(targetAngle < 180) value = angle<targetAngle || angle> 180+targetAngle;
    else value = angle<targetAngle && angle> targetAngle-180;
    return value;
  }
  public static boolean shouldTurnLeft(double currentNavxAngle, double targetAngle){
    double angle = currentNavxAngle;
    boolean value = false;

    if(targetAngle <= 180) value = angle<targetAngle || angle> 180+targetAngle;
    else value = angle<targetAngle && angle> targetAngle-180;
    return value;
  }
 
  public static double[][] intArrToDouble(int[][] arr){
    double[][] result = new double[arr.length][arr[0].length];
    for(int i = 0; i<result.length; i++){
      for(int x = 0; x<result[0].length; x++){
        result[i][x] = arr[i][x];
      }
    }
    return result;
  }
 
  public static double[] metersToPixels(double[] arr){
    double[] result = new double[arr.length];
    for(int i = 0; i<arr.length; i++)
      result[i] = arr[i] * GUIConstants.pixels_per_meter;
    return result;
  }
  public static int[] metersToPixelsInt(double[] arr){
    int[] result = new int[arr.length];
    for(int i = 0; i<arr.length; i++)
      result[i] = (int)(arr[i] * GUIConstants.pixels_per_meter);
    return result;
  }
  public static double[] intArrToDouble(int[] arr){
    double[] result = new double[arr.length];
    for(int i = 0; i<arr.length; i++){
      result[i] = arr[i];
    }
    return result;
  }

  public static int[] doubleArrToInt(double[] arr){
    int[] result = new int[arr.length];
    for(int i = 0; i<arr.length; i++){
      result[i] = (int)arr[i];
    }
    return result;
  }

  public static double distance(double[] p1, double[] p2){
    return Math.sqrt( Math.pow(p1[1] - p2[1], 2) + Math.pow(p1[0] - p2[0], 2));
  }
  public static double distance(double[] p1, int[] p2){
    return Math.sqrt( Math.pow(p1[1] - p2[1], 2) + Math.pow(p1[0] - p2[0], 2));
  }
  public static double distance(int[] p1, int[] p2){
    return Math.sqrt( Math.pow(p1[1] - p2[1], 2) + Math.pow(p1[0] - p2[0], 2));
  }


  public static double angleFromSlope(double[] start, double[] end){
    return Math.toDegrees(Math.atan2((end[1] - start[1]), end[0] - start[0]));
  }
  public static double magnitutde(double[] vector){
    return Math.sqrt((vector[0]*vector[0]) + (vector[1]*vector[1]));
  }

  public static double angleDistance2(double targetAngle, double angle){
    
    double distance = Math.abs(targetAngle - angle)%360;
    if (distance > 180) distance = 360 - distance;
    return distance;
  }


  public static double floorMod(double x, double y){
    if(x<0)
        return y - Math.abs(x)%y;
    return x%y;
}

public static double angleToPoint(double[] start, double[] end){
  double dx = end[0]-start[0];
  double dy = end[1]-start[1];
  return to360(Math.toDegrees(Math.atan2(-dx, dy)));

}
public static double getArcLength(Circle circle){
  Line base = new Line(circle.startPoint, circle.endPoint);
  double[] midPoint = base.getMidPoint();
  double halfAngle = Math.atan(distance(midPoint, base.startPoint)/distance(midPoint, circle.center));
  return halfAngle*2*circle.radius;
}

}
