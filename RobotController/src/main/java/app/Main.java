package app;

import java.io.IOException;
import app.gui.GUI;
import app.networking.NetClient;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");
        GUI window = new GUI();
        window.display();
        /*NetClient netClient = new NetClient();
        netClient.run();*/
    }
}
