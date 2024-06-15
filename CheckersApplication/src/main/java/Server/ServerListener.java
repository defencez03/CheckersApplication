package Server;

import Client.Figure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class ServerListener extends Thread {
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    Integer[][] positions;
    Integer[][] positionsReverse;
    ArrayList<Socket> sockets;

    public ServerListener(Socket socket, ArrayList<Socket> sockets) {
        super("ServerListener");
        this.socket = socket;
        this.positions = new Integer[8][8];
        this.positionsReverse = new Integer[8][8];
        this.sockets = sockets;
    }

    public void run() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // Проверяем живой ли канал и работаем если живой
            while (!socket.isOutputShutdown()) {
                // Проверка на приход от клиента данных
                while (in.available() == 0)
                    Thread.sleep(1);

                String data = in.readUTF();
                String[] arrStr = data.split(" ");

                // Изменение данных
                int countArr = 0;
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        positions[i][j] = Integer.parseInt(arrStr[countArr]);
                        positionsReverse[i][j] = positions[i][7-j];
                        countArr++;
                    }
                }

                // Отправка измененных данных другому клиенту
                for (var item : sockets) {
                    if (item != socket) {
                        out = new DataOutputStream(item.getOutputStream());
                        out.writeUTF(data);
                        out.flush();
                        Thread.sleep(10);
                    }
                }
            }
        }
        catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
