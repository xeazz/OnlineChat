package Client;

import Settings.LoadSettings;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final Socket clientSocket;
    private final Scanner scanner;
    private final BufferedReader in;
    private final PrintWriter out;
    private Thread thread1;
    private Thread thread2;

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
        thread1 = new Thread(() -> {
            try {
                while (!thread1.isInterrupted()) {
                    if (in.ready()) {
                        String inMessage = in.readLine();
                        System.out.println(inMessage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Сеанс завершен!");
            }
        });
        thread1.start();

        // поток отправляющий сообщения приходящие с консоли на сервер
        thread2 = new Thread(() -> {
            while (!thread2.isInterrupted()) {
                if (scanner.hasNext()) {
                    String outMessage = scanner.nextLine();
                    out.println(outMessage);
                    if (outMessage.equals("/exit")) {
                        disconnect();
                        break;
                    }
                }
            }
        });
        thread2.start();
    }

    public synchronized void disconnect() {
        thread1.interrupt();
        thread2.interrupt();
        try {
            in.close();
            scanner.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Произошла ошибка: " + e);
        }
    }
}
