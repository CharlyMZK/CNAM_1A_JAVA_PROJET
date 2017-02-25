package models;

import javax.swing.*;

/**
 * Created by MZK on 25/02/2017.
 */


public class Alien extends Sprite {

    private Bomb bomb;
    private final String shot = "../assets/alien.png";

    public Alien(int x, int y) {
        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);
        ImageIcon ii = new ImageIcon(this.getClass().getResource(shot));
        setImage(ii.getImage());

    }

    public void act(int direction) {
        this.x += direction;
    }

    public Bomb getBomb() {
        return bomb;
    }
}