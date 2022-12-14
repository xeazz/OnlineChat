package Client;

import Settings.LoadSettings;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private Thread treed1;
    private Thread treed2;
    private final Scanner scanner;
    private final BufferedReader in;
    private final PrintWriter out;

    public Client() {
        LoadSettings loadSettings = new LoadSettings();
        try {
            clientSocket = new Socket(loadSettings.getAddress(), loadSettings.getPort());
            scanner = new Scanner(System.in);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startClient() {
        // поток чтения сообщений с сервера
        treed1 = new Thread(() -> {
            try {
                while (true) {
                    if (in.ready()) {
                        String inMessage = in.readLine();
                        if (inMessage == null) {
                            break;
                        } else {
                            System.out.println(inMessage);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Сеанс завершен!");
            }
        });
        treed1.start();
        treed1.interrupt();

        // поток отправляющий сообщения приходящие с консоли на сервер
        treed2 = new Thread(() -> {
            while (true) {
                if (scanner.hasNext()) {
                    String outMessage = scanner.nextLine();
                    out.println(outMessage);
                    if (outMessage.equals("/exit")) {
                        try {
                            in.close();
                            out.close();
                            clientSocket.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                }
            }
        });
        treed2.start();
        treed2.interrupt();
    }

}
