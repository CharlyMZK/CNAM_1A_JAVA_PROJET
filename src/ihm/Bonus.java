package ihm;

import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.ImageIcon;


public class Bonus extends Sprite implements Commons{

    private final int START_Y = 280;
    private int start_x = 270;

    private final String boost = "../spacepix/boost.png";
    private int width;

    public Bonus() {

        ImageIcon ii = new ImageIcon(this.getClass().getResource(boost));

        width = ii.getImage().getWidth(null);

        setImage(ii.getImage());

        this.setVisible(false);

        start_x = (int)(Math.random() * (300+1-1)) + 1;
        setX(start_x);
        setY(START_Y);
    }

}