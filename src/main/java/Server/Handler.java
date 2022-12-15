package Server;

import java.io.*;
import java.net.Socket;

public class Handler {
    private final Socket socket;
    private Thread rxThread;
    private final BufferedReader in;
    private final BufferedWriter out;
    public String userName;
    private static int CLIENT_COUNT = 0;

    //        public Handler (Connections connect, String ipAddr, int port) throws IOException {
//        this(connect, new Socket(ipAddr, port));
//    }
    public Handler(Server server, Socket socket) throws IOException {
        CLIENT_COUNT++;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        //Поток, который обрабатывает каждое новое подключение
        rxThread = new Thread(() -> {
            try {
                while (true) {
                    out.write("Введите Ваше имя" + "\r\n");
                    out.flush();
                    userName = in.readLine();
                    if (server.checkName(userName)) {
                        break;
                    } else {
                        out.write("Пользователь с таким именем уже существует" + "\r\n");
                        out.flush();
                    }
                }
                out.write("Добро пожаловать в чат! Если захотите выйти наберите /exit" + "\r\n");
                out.flush();
                if (userName != null) {
                    server.onConnection(Handler.this);
                    server.onlineClients(CLIENT_COUNT);
                } else {
                    disconnect();
                }
                while (!rxThread.isInterrupted()) {
                    String input = in.readLine();
                    if (input == null || input.equals("/exit")) {
                        disconnect();
                        break;
                    } else {
                        server.onReceiveMsg(Handler.this, input);
                    }
                }
            } catch (IOException e) {
                System.out.println("Произошла ошибка: " + e);
            } finally {
                server.onDisconnect(Handler.this);
                CLIENT_COUNT--;
                server.onlineClients(CLIENT_COUNT);
            }
        });
        rxThread.start();
    }

    public String getUserName() {
        return userName;
    }

    public synchronized void sendMsg(String value) {
        try {
            out.write(value + "\r\n");
            out.flush();
        } catch (IOException e) {
            System.out.println("Клиент нагло покинул чат " + e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Произошла ошибка: " + e);
        }
    }

}
