package models;

import javax.swing.ImageIcon;
import java.util.Random;


public class Bonus extends Sprite implements Commons {

    private final int START_Y = 850;
    private int start_x = 870;
    private String boost = null;
    private int mode = 0;
    private int width;

    public Bonus() {

        Random generator = new Random();
        int rand = generator.nextInt(3);

        if(rand == 0){
            boost = "../assets/boost.png";
            mode = 1;
        }
        if(rand == 1){
            boost = "../assets/alien_fun.png";
            mode = 2;
        }
        if(rand == 2){
            boost = "../assets/vaisseauBaptisteShooting.png";
            mode = 3;
        }
        if(boost == null){
            boost = "../assets/boost.png";
        }

        ImageIcon ii = new ImageIcon(this.getClass().getResource(boost));
        width = ii.getImage().getWidth(null);

        setImage(ii.getImage());

        this.setVisible(false);

        start_x = (int)(Math.random() * (300+1-1)) + 1;
        setX(start_x);
        setY(START_Y);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}