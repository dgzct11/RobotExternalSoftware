package app.gui.control_panel;
import java.awt.Color;
import java.lang.reflect.Array;
import java.util.ArrayList;

import app.gui.GUIConstants;

public class SCSetPoint {
    public double startDistance;
    public double endDistance;
    public String subsystemIdentifier;
    public ArrayList<Double> inputs = new ArrayList<Double>();

    public Color color;
    public SCSetPoint(double sd, double ed, String id){
        startDistance = sd;
        endDistance = ed;
        subsystemIdentifier = id;

        if(id.equals("navx")){
            color = GUIConstants.navXColor;
        }
    }

    public String toString(){
        String inputString = "";
        for(Double input: inputs)
            inputString += input + ",";
        return String.format("%s:%f,%f:%s\n",subsystemIdentifier, startDistance, endDistance, inputString);
    }
}
