package app.networking;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NetClient {
  public double x = 0; 
  public double y = 0;
  NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("SmartDashboard");
        NetworkTableEntry xEntry = table.getEntry("Od x");
        NetworkTableEntry yEntry = table.getEntry("Od y");
    public void run(){
        
        inst.startClientTeam(5057);  // where TEAM=190, 294, etc, or use inst.startClient("hostname") or similar
        inst.startDSClient();
       
    }
    public double[] getPose(){
      x = xEntry.getDouble(0.0);
      y = yEntry.getDouble(0.0);
     double[] result = {x,y};
     return result;
    }
}
