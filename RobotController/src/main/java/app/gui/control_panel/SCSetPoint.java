package app.gui.control_panel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SCSetPoint {
    public double startDistance;
    public double endDistance;
    public String subsystemIdentifier;
    public ArrayList<Double> inputs = new ArrayList<Double>();
    public SCSetPoint(double sd, double ed, String id){
        startDistance = sd;
        endDistance = ed;
        subsystemIdentifier = id;
    }
}
