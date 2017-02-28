package models;

import javax.swing.*;

/**
 * Created by MZK on 25/02/2017.
 */


public class Alien extends Sprite {

    private Bomb bomb;
    private final String alien = "../assets/alien.png";
    private Vaisseau vaisseau;

    public Alien(int x, int y) {
        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);
        try{
            this.vaisseau = new Alkesh();
        }catch(Exception e){
        }

        ImageIcon ii = new ImageIcon(this.getClass().getResource(alien));
        setImage(ii.getImage());

    }

    public void act(int direction) {
        this.x += direction;
    }

    public Bomb getBomb() {
        return bomb;
    }

    public Vaisseau getVaisseau() {
        return vaisseau;
    }

    public void setVaisseau(Vaisseau vaisseau) {
        this.vaisseau = vaisseau;
    }
}