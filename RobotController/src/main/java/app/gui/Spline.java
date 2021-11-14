package app.gui;

import java.util.ArrayList;
import java.awt.Point;

public class Spline {
    public ArrayList<Point> points = new ArrayList<>();
    public ArrayList<Double> distances = new ArrayList<>();

    private double distance = 0;

    public Point getPoint(float t) {
        Point p0,p1,p2,p3;

        p0 = points.get((int)t);
        p1 = points.get((int)t + 1);
        p2 = points.get((int)t + 2);
        p3 = points.get((int)t + 3);

        t = t - (int) t;
        float tt = t * t;
        float ttt = tt * t;

        int tx = (int) ((2 * p1.x) + (-p0.x + p2.x)*t + (2 * p0.x - 5 * p1.x + 4 * p2.x - p3.x)*tt + (-p0.x + 3 * p1.x - 3 * p2.x + p3.x)*ttt);
        int ty = (int) ((2 * p1.y) + (-p0.y + p2.y)*t + (2 * p0.y - 5 * p1.y + 4 * p2.y - p3.y)*tt + (-p0.y + 3 * p1.y - 3 * p2.y + p3.y)*ttt);

        return new Point(tx/2,ty/2);
    }

    public void setZero() {
        distances.add(distance);
        distance = distance - distance;
    }

    public void addDistance(Point p1, Point p2) {
        distance += Math.sqrt(Math.pow(p2.x-p1.x,2) + Math.pow(p2.y - p1.y, 2));
    }
}
