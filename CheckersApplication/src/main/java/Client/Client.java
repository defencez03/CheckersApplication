package Client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringJoiner;
import javax.swing.JOptionPane;


public class Client {
    private Socket socket;
    private DataOutputStream oos;
    private DataInputStream ois;
    public Area area;
    private String colorFigures;
    private Integer[][] positions;
    private boolean event = false;

    public Client() {
        try {
            socket = new Socket("localhost", 80);
            ois = new DataInputStream(socket.getInputStream());
            oos = new DataOutputStream(socket.getOutputStream());

            if (!socket.isOutputShutdown()) {

                while (ois.available() == 0)
                    Thread.sleep(1);

                colorFigures = ois.readUTF();
            }
            
            positions = new Integer[8][8];           
            area = new Area(colorFigures);
            area.toFront();
            new ClientListener().start();
            new ClientSender().start();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Нить на отправку данных
    private class ClientSender extends Thread {

        public ClientSender() {
           super("ClientSender");
        }

        public void run() {
            Integer numClient;
            // Проверяем живой ли канал и работаем если живой
            while (!socket.isOutputShutdown()) {
                // Проверка на совершение игроком действия
                while(!area.getEvent()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                // Отправка данных на сервер
                try {
//                    if (colorFigures.equals("red"))
//                        numClient = 0;
//                    else
//                        numClient = 1;

                    StringJoiner joiner = new StringJoiner(" ");
                    
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            joiner.add(area.getPositions()[i][j].toString());
                        }
                    }
                    
                    String str = joiner.toString();
                    
                    oos.writeUTF(str);
                    oos.flush();
                    Thread.sleep(10);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

                area.setEvent(false);
            }
        }
    }

    // Нить прослушивания
    private class ClientListener extends Thread {

        public ClientListener() {
            super("ClientListener");
        }

        public void run() {
            try {
                // Проверяем живой ли канал и работаем если живой
                while (!socket.isOutputShutdown()) {
                    // Проверка на приход от сервера данных
                    while (ois.available() == 0)
                        Thread.sleep(1);

                    String data = ois.readUTF();
                    String[] arrStr = data.split(" ");

                    // Изменение данных об объектах
                    int countArr = 0;
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            positions[i][j] = Integer.parseInt(arrStr[countArr]);
                            countArr++;
                        }
                    }
                    
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            area.setPositions(7-i, 7-j, positions[i][j]);
                        }
                    }
                    
                    ArrayList<Figure> fig = area.getFigures();
                    ArrayList<Figure> contrFig = area.getContrFigures();
                    int countNumColor = 0;
                    int countNumContrColor = 0;
                    
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (area.getPositions()[i][j] == area.getNumColor()) {
                                fig.get(countNumColor).setX(j * 50);
                                fig.get(countNumColor).setY(i * 50);
                                countNumColor++;
                            }
                            else if (area.getPositions()[i][j] == area.getNumContrColor()){
                                contrFig.get(countNumContrColor).setX(j * 50);
                                contrFig.get(countNumContrColor).setY(i * 50);
                                countNumContrColor++;
                            }
                        }
                    }
                    
                    if (countNumColor == 0) 
                        JOptionPane.showMessageDialog(area, "Вы проиграли");
                    else if (countNumContrColor == 0)
                        JOptionPane.showMessageDialog(area, "Вы выйграли");
                    
                    if (countNumColor < fig.size())
                        fig.removeLast();
                    
                    area.setFigures(fig);
                    area.setContrFigures(contrFig);
                    
                    area.repaint();
                }

            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
