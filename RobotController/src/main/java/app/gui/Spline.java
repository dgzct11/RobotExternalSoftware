package app.gui;

import java.util.ArrayList;
import java.awt.Point;

public class Spline {
    ArrayList<Point> points = new ArrayList<>();

    Point getPoint(float t) {
        int p0,p1,p2,p3;

        p1 = (int)t + 1;
        p2 = p1 + 1;
        p3 = p2 + 1;
        p0 = p1 - 1;

        t = t - (int) t;

        float tt = t * t;
        float ttt = tt * t;
        
        float q1 = -ttt + 2.0f * tt - t;
        float q2 = 3.0f * ttt - 5.0f * tt + 2.0f;
        float q3 = -3.0f * ttt + 4.0f * tt + t;
        float q4 = ttt - tt;

        int tx = (int) (points.get(p0).x * q1 + points.get(p1).x * q2 + points.get(p2).x * q3
                + points.get(p3).x * q4);
        int ty = (int) (points.get(p0).y * q1 + points.get(p1).y * q2 + points.get(p2).y * q3
                + points.get(p3).y * q4);

        return new Point(tx,ty);
    }
}
