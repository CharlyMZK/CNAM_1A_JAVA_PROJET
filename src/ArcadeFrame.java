import models.Commons;

import javax.swing.*;

/**
 * Created by MZK on 21/02/2017.
 */
public class ArcadeFrame extends JFrame implements Commons {

    public ArcadeFrame() {
        Board board = new Board();
        add(board);
        setTitle("Mraled Space Project");
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                board.setIngame(false);
            }
        });
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGTH);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

}
