package models;

import javax.swing.*;

/**
 * Created by MZK on 28/02/2017.
 */

public class Boss extends Sprite {

    private Bomb bomb;
    private final String boss = "../assets/boss.png";
    private Vaisseau vaisseau;

    public Boss(int x, int y) {
        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);
        try{
            this.vaisseau = new Dart();
        }catch(Exception e){
        }

        ImageIcon ii = new ImageIcon(this.getClass().getResource(boss));
        setImage(ii.getImage());
        this.setVisible(false);
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