package app;

import app.gui.MainWindow;
import app.networking.NetClient;


public class Application {
    public void start(){
        MainWindow window = new MainWindow();
        window.display();
        /*NetClient netClient = new NetClient();
        netClient.run();*/
    }   
}
