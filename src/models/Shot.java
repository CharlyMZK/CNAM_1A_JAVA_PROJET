package models;

import models.Sprite;

import javax.swing.ImageIcon;


public class Shot extends Sprite {

    private String shot = "../assets/shot.png";
    private final int H_SPACE = 6;
    private final int V_SPACE = 1;

    public Shot() {
    }

    public Shot(int x, int y) {

        ImageIcon ii = new ImageIcon(this.getClass().getResource(shot));
        setImage(ii.getImage());
        setX(x + H_SPACE);
        setY(y - V_SPACE);
    }

    public void move(int distance){
        int y = this.getY();
        y -= distance;

        if (y < 0){
            this.die();
        }
        else this.setY(y);
    }
}