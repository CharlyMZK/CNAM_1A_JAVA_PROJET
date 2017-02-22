package ihm;

import javax.swing.*;

/**
 * Created by MZK on 21/02/2017.
 */
public class SpaceInvadersFrame extends JFrame implements Commons{

        public SpaceInvadersFrame()
        {
            add(new Board());
            setTitle("Space Invaders");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(BOARD_WIDTH, BOARD_HEIGTH);
            setLocationRelativeTo(null);
            setVisible(true);
            setResizable(false);
        }

}
