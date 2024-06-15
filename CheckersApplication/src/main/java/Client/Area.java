/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Vasya
 */
public class Area extends JFrame implements MouseListener {
    private Integer countFigure = 0;
    private ArrayList<Figure> figures;
    private ArrayList<Figure> contrFigures;
    private Integer[][] positions;
    private boolean target;
    private String color;
    private String contrColor;
    private boolean event = false;
    private Integer numColor;
    private Integer numContrColor;
    
    public Area(String color) {
        super();
        // Определение положение окна на экране
        setLocation(300, 300);
        // Определение размера окна
        setSize (410, 435);
        // Открываем окно
        setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.color = color;
        if (color.equals("red")) {
            numColor = 1;
            numContrColor = 2;
            contrColor = "blue";
        }
        else {
            numColor = 2;
            numContrColor = 1;
            contrColor = "red";
        }
        figures = new ArrayList<Figure>();
        contrFigures = new ArrayList<Figure>();
        
        // Инициализация игрового поля
        positions = new Integer[][]{
                    {0, numContrColor, 0, numContrColor, 0, numContrColor, 0, numContrColor},
                    {numContrColor, 0, numContrColor, 0, numContrColor, 0, numContrColor, 0},
                    {0, numContrColor, 0, numContrColor, 0, numContrColor, 0, numContrColor},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {numColor, 0, numColor, 0, numColor, 0, numColor, 0},
                    {0, numColor, 0, numColor, 0, numColor, 0, numColor},
                    {numColor, 0, numColor, 0, numColor, 0, numColor, 0}};
        
        // Инициализация фигур
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (positions[i][j] == numColor) 
                    figures.add(new Figure(this, j*50, i*50, color));
                else if (positions[i][j] == numContrColor)
                    contrFigures.add(new Figure(this, j*50, i*50, contrColor));
            }
        }
        
        this.addMouseListener((MouseListener) this);
        //new MouseThread().start();
    }
    
    public String getColor() { return color; }
    public boolean getEvent() { return event; }
    public void setEvent(boolean e) { event = e; }
    public Integer[][] getPositions() { return positions; }
    public void setPositions(Integer y, Integer x, Integer res) { positions[y][x] = res; }
    public ArrayList<Figure> getFigures() { return figures; }
    public ArrayList<Figure> getContrFigures() { return contrFigures; }
    public void setFigures(ArrayList<Figure> f) { figures = f; }
    public void setContrFigures(ArrayList<Figure> f) { contrFigures = f; }
    public Integer getNumColor() { return numColor; }
    public Integer getNumContrColor() { return numContrColor; }
    
    public void DrawImage(Graphics g, String name, Integer x, Integer y) {
        String src = "C:\\Users\\Vasya\\Documents\\NetBeansProjects\\"
                + "CheckersApplication\\src\\main\\java\\Assets\\" + name;
        g.drawImage(new ImageIcon(src).getImage(), x, y, null);
    }
    
    
    @Override
    public void paint(Graphics g) {      
        DrawImage(g, "chekersBoard400.png", 5, 30);
      
        for (var figure : figures)
            DrawImage(g, color + "Fig.png", figure.GetX() + 5, figure.GetY() + 30);

        for (var figure : contrFigures)
            DrawImage(g, contrColor + "Fig.png", figure.GetX() + 5, figure.GetY() + 30);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        int numX = ((e.getPoint().x - 5) - (e.getPoint().x - 5)%50)/50;
        int numY = ((e.getPoint().y - 30) - (e.getPoint().y - 30)%50)/50;

        // Проверка на правильность хода
        if (target) {
            for (var fig : figures)  {
                if (fig.getTarget()) {
                    if (positions[numY][numX] != 0) {
                        target = false;
                        fig.setTarget(false);
                        break;
                    }
                    
                    // Перемещение фигуры (2 варианта)
                    if (fig.GetX()/50 == numX + 1 &&
                            fig.GetY()/50 == numY + 1) 
                    {
                        positions[numY][numX] = numColor;
                        positions[numY + 1][numX + 1] = 0;
                        fig.setX(numX * 50);
                        fig.setY(numY * 50);
                        repaint();
                    }
                    else if (fig.GetX()/50 == numX - 1 &&
                            fig.GetY()/50 == numY + 1)
                    {
                        positions[numY][numX] = numColor;
                        positions[numY + 1][numX - 1] = 0;
                        fig.setX(numX * 50);
                        fig.setY(numY * 50);
                        repaint();
                    }
                    
                    // Нанесение удара (2 варианта)
                    if (positions[numY+1][numX+1] == numContrColor &&
                            fig.GetX()/50 == numX + 2 &&
                            fig.GetY()/50 == numY + 2)
                    {
                        positions[numY][numX] = numColor;
                        positions[numY + 1][numX + 1] = 0;
                        positions[numY + 2][numX + 2] = 0;
                        fig.setX(numX * 50);
                        fig.setY(numY * 50);
                        contrFigures.remove(0);
                        int countNumColor = 0;
                        for (int i = 0; i < 8; i++) {
                            for (int j = 0; j < 8; j++) {
                                if (positions[i][j] == numContrColor) {
                                    contrFigures.get(countNumColor).setX(j * 50);
                                    contrFigures.get(countNumColor).setY(i * 50);
                                    countNumColor++;
                                }
                            }
                        }
                        
                        if (countNumColor == 0)
                            JOptionPane.showMessageDialog(this, "Вы выйграли");
                        
                        repaint();
                    }
                    else if (positions[numY+1][numX-1] == numContrColor &&
                            fig.GetX()/50 == numX - 2 &&
                            fig.GetY()/50 == numY + 2)
                    {
                        positions[numY][numX] = numColor;
                        positions[numY + 1][numX - 1] = 0;
                        positions[numY + 2][numX - 2] = 0;
                        fig.setX(numX * 50);
                        fig.setY(numY * 50);
                        contrFigures.remove(0);
                        int countNumColor = 0;
                        for (int i = 0; i < 8; i++) {
                            for (int j = 0; j < 8; j++) {
                                if (positions[i][j] == numContrColor) {
                                    contrFigures.get(countNumColor).setX(j * 50);
                                    contrFigures.get(countNumColor).setY(i * 50);
                                    countNumColor++;
                                }
                            }
                        }
                        
                        if (countNumColor == 0)
                            JOptionPane.showMessageDialog(this, "Вы выйграли");
                        
                        repaint();
                    }
                    
                    fig.setTarget(false);
                    target = false;
                    event = true;
                    return;
                }
            }
            target = false;
            return;
        }

        // Проверка на правильность выбранной фигуры
        if (positions[numY][numX] == numColor) {
            for (var fig : figures) {
                if (fig.GetX()/50 == numX && fig.GetY()/50 == numY) {
                    // Проверка на свободное пространство вокруг фигуры
//                    if (numX == 7 || numX == 0 || numY == 7 || numY == 0) {
//                        if (numX == 7)
//                            if (positions[numY + 1][numX - 1] == numColor &&
//                            positions[numY - 1][numX - 1] == numColor)
//                                return;
//                        else if (numY == 7) {
//                            if (positions[numY - 1][numX + 1] == numColor &&
//                            positions[numY - 1][numX - 1] == numColor)
//                                return;
//                        }
//                        else if (numX == 0) {
//                            if (positions[numY - 1][numX + 1] == numColor &&
//                            positions[numY + 1][numX + 1] == numColor)
//                                return;
//                        }
//                        else if (numY == 0) {
//                            if (positions[numY + 1][numX + 1] == numColor &&
//                            positions[numY + 1][numX - 1] == numColor)
//                                return;
//                        }
//                        
//                        fig.setTarget(true);
//                        target = true;
//                        return;
//                    }
//                    
//                    if (positions[numY + 1][numX + 1] == numColor &&
//                        positions[numY + 1][numX - 1] == numColor &&
//                        positions[numY - 1][numX + 1] == numColor &&
//                        positions[numY - 1][numX - 1] == numColor)
//                        return;

                    fig.setTarget(true);
                    target = true;
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    
//    private class MouseThread extends Thread {
//        MouseThread() {
//            super("MouseThread");
//        }
//        
//        
//    }

}
