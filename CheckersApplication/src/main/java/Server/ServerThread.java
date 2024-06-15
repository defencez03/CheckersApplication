package Server;

import Client.Figure;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;


public class ServerThread extends Thread {
    private Server srv;

    public ServerThread(Server srv)
    {
        super("ServerThread");
        this.srv = srv;
    }

    public void run()
    {
        // Запуск серверного сокета
        try (ServerSocket serverSocket = new ServerSocket(80))
        {
            System.out.println("Server to start...");

            // Проверка канала
            while (!serverSocket.isClosed() && srv.getClientNum() < 2)
            {
                // Ожидание подключения клиента
                srv.getListSocket().add(serverSocket.accept());
                srv.getListOut().add(new DataOutputStream(srv.getListSocket().getLast().getOutputStream()));
                srv.getListIn().add(new DataInputStream(srv.getListSocket().getLast().getInputStream()));

                if (srv.getClientNum() == 0) {
                    srv.getListOut().getLast().writeUTF("red");
                    Thread.sleep(10);
//                    synchronized (srv.getClientInfo()) {
//                        srv.getClientInfo().put(srv.getClientNum(), new Figure(0, 200, "src/Assets/tank100RedRight.png", "right"));
//                    }
                } else {
                    srv.getListOut().getLast().writeUTF("blue");
                    srv.getListOut().getLast().flush();
                    Thread.sleep(10);
//                    synchronized (srv.getClientInfo()) {
//                        srv.getClientInfo().put(srv.getClientNum(), new Figure(500, 200, "src/Assets/tank100BlueLeft.png", "left"));
//                    }
                }

                srv.setClientNum(srv.getClientNum() + 1);

                new ServerListener(srv.getListSocket().getLast(), srv.getListSocket()).start();
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
