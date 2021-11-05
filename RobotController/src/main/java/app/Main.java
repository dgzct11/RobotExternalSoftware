package app;

import java.io.IOException;

import app.gui.MainWindow;
import app.networking.NetClient;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");
        MainWindow window = new MainWindow();
        window.display();
        /*NetClient netClient = new NetClient();
        netClient.run();*/
    }
}
