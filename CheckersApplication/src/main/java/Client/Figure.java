/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

import Client.Area;
import javax.swing.ImageIcon;

/**
 *
 * @author Vasya
 */
public class Figure {
    private Integer id;
    private String form;
    private Integer[] position;
    private boolean target = false;
    
    Figure(Area area, Integer x, Integer y, String color) {
        //this.id = id;
        position = new Integer[2];
        position[0] = x;
        position[1] = y;
        form = "C:\\Users\\Vasya\\Documents\\NetBeansProjects\\"
            + "CheckersApplication\\src\\main\\java\\Assets\\" + color +"Fig";
        //area.getGraphics().drawImage(new ImageIcon(form).getImage(), 5, 30, null);
    }
    
    public Integer GetId() { return id; }
    public Integer GetX() { return position[0]; }
    public Integer GetY() { return position[1]; }
    public void setX(Integer x) { position[0] = x; }
    public void setY(Integer y) { position[1] = y; }
    public void setTarget(boolean t) { target = t; }
    public boolean getTarget() { return target; }
    
    public void Moving() {
        
    }
    
}
