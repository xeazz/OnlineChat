package Server;

import Logger.Logger;
import Settings.LoadSettings;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final List<Handler> list = new ArrayList<>();
    private final LoadSettings loadSettings;
    private static final Logger logger = Logger.getInstance();

    public Server() {
        this.loadSettings = new LoadSettings();
    }

    public void start() {
        System.out.println("Server running...");
        logger.writeLog("Server running...");
        try (ServerSocket serverSocket = new ServerSocket(loadSettings.getPort())) {
            while (true) {
                try {
                    new Handler(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("Ошибка подключения: " + e);
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка подключения сервера" + e);
        }
    }

    public synchronized void onConnection(Handler handler) {
        list.add(handler);
        sendToAllConnections("Новый участник " + handler.getUserName() + " подключился к нашему чату!");
        logger.writeLog("Новый участник " + handler.getUserName() + " подключился к нашему чату!");
    }

    public synchronized void onReceiveMsg(Handler handler, String inMessage) {
        sendToAllConnections(handler.getUserName() + " отправил: " + inMessage);
        logger.writeLog(handler.getUserName() + " отправил: " + inMessage);
    }

    public synchronized void onDisconnect(Handler handler) {
        list.remove(handler);
        sendToAllConnections(handler.getUserName() + " вышел из чата.");
        logger.writeLog(handler.getUserName() + " вышел из чата.");

    }

    public synchronized void onlineClients(int onlineClients) {
        sendToAllConnections("Клиентов сейчас в сети: " + onlineClients);
//        logger.writeLog("Клиентов сейчас в сети: " + onlineClients);
    }

    public void sendToAllConnections(String inMessage) {
        System.out.println(inMessage);
        for (Handler handler : list) {
            handler.sendMsg(inMessage);
        }
    }

    public synchronized boolean checkName(String userName) {
        for (Handler name : list) {
            if (name.getUserName().equals(userName)) return false;
        }
        return true;
    }
}
