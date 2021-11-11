// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package app.gui.trajectory;

import java.util.ArrayList;



/** Add your docs here. */
public abstract class Trajectory {
    
    ArrayList<Segment> segments;
    public double maxVelocity;
    public double acceleration;
    public double totalDistance = 0;
    public double timeToMax = 0;
    public double timeToBreak = 0;
    public double totalTime = 0;
    public double distanceToAccelerate;
    public double distanceToBreak;

    public int currentIndex = 0;
    public double[] angles;
    public Trajectory(double a, double mv, double[] ang){
        acceleration = a;
        maxVelocity = mv;
        timeToMax = maxVelocity/acceleration;
        timeToBreak = (maxVelocity/acceleration);
        distanceToAccelerate = (maxVelocity*maxVelocity/(2*acceleration));
        distanceToBreak = (maxVelocity*maxVelocity/(2*acceleration));
        angles = ang;
    }
    public double getCurrentAngle(){
        return segments.get(currentIndex).angle;
    }
    public void setMaxAV(double a, double v){
        acceleration = a;
        maxVelocity = v;
    }
    public Position getEndPoint(){
        Segment seg = segments.get(segments.size()-1);
            return new Position(seg.endPoint, M.angleFromSlope(seg.startPoint, seg.endPoint));
    }
    public Position getPosition(double time){
        /*
       if time<acceltime:
        use x = x + vt + a/2t^2
        if time>breakingTime:

        else:
        */
        double distance;
        if(time>totalTime){
            Segment seg = segments.get(segments.size()-1);
            currentIndex = segments.size()-1;
            return new Position(seg.endPoint, M.angleFromSlope(seg.startPoint, seg.endPoint));
        }
        if(time <= timeToMax){
             distance = acceleration/2*time*time;
        }
        else if(time >= totalTime-timeToBreak){
            distance = totalDistance-distanceToBreak + maxVelocity*(time-(totalTime-timeToBreak)) -acceleration/2*Math.pow((time-(totalTime-timeToBreak)),2);
        }
        else{
            distance = distanceToAccelerate + maxVelocity*(time-timeToMax);
        }
        if(distance>totalDistance){
            Segment seg = segments.get(segments.size()-1);
            currentIndex = segments.size()-1;
            return new Position(seg.endPoint, M.angleFromSlope(seg.startPoint, seg.endPoint));
        }
        double currentDistance = 0;
        int index = 0;
        while(index<segments.size()-1 && currentDistance +segments.get(index).length <= distance){
            currentDistance += segments.get(index).length;   
            index ++;
        }
        currentIndex = index;
        return segments.get(index).getPosition(distance - currentDistance );
        //turn currentDistance into position
    }
    public String toString(){
        String result = "";
        for(Segment segment: segments)
            result += segment.toString();
        return result;
    }
    public double getTotalDistance(){
        for(Segment seg: segments){
           totalDistance += seg.length;
        }
        totalTime = (totalDistance - distanceToAccelerate - distanceToBreak )/maxVelocity + timeToMax + timeToBreak;

        
      return totalDistance;
    }
    public abstract void initializeSegments();
}
