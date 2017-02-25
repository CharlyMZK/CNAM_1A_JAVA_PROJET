package models;

import javax.swing.*;

/**
 * Created by MZK on 25/02/2017.
 */
public class Bomb extends Sprite {

    private final String bomb = "../assets/bomb.png";
    private boolean destroyed;

    public Bomb(int x, int y) {
        setDestroyed(true);
        this.x = x;
        this.y = y;
        ImageIcon ii = new ImageIcon(this.getClass().getResource(bomb));
        setImage(ii.getImage());
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}