import models.Commons;

import javax.swing.*;

/**
 * Created by MZK on 21/02/2017.
 */
public class ArcadeFrame extends JFrame implements Commons {

        public ArcadeFrame()
        {
            add(new Board());
            setTitle("Space Invaders");
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setSize(BOARD_WIDTH, BOARD_HEIGTH);
            setLocationRelativeTo(null);
            setVisible(true);
            setResizable(false);
        }

}
