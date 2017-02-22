package ihm;

        import java.awt.Color;
        import java.awt.Dimension;
        import java.awt.Font;
        import java.awt.FontMetrics;
        import java.awt.Graphics;
        import java.awt.Toolkit;
        import java.awt.event.KeyAdapter;
        import java.awt.event.KeyEvent;

        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.Iterator;
        import java.util.List;
        import java.util.Random;

        import javax.swing.ImageIcon;
        import javax.swing.JPanel;


public class Board extends JPanel implements Runnable, Commons {

    private Dimension d;
    private ArrayList aliens;
    private Player player;
    private Bonus bonus;
    private Shot shot;
    private List<Shot> machineGun = new ArrayList<Shot> ();
    private int alienX = 150;
    private int alienY = 5;
    private int direction = -1;
    private int deaths = 0;

    private boolean ingame = true;
    private final String expl = "../spacepix/explosion.png";
    private final String playerShip = "../spacepix/player.png";
    private final String playerShooting = "../spacepix/playerShooting.png";
    private final String alienpix = "../spacepix/alien.png";
    private String message = "Game Over";

    private Thread animator;

    public Board()
    {

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGTH);
        setBackground(Color.black);

        gameInit();
        setDoubleBuffered(true);
    }

    public void addNotify() {
        super.addNotify();
        gameInit();
    }

    public void gameInit() {

        aliens = new ArrayList();

        ImageIcon ii = new ImageIcon(this.getClass().getResource(alienpix));

        for (int i=0; i < 4; i++) {
            for (int j=0; j < 6; j++) {
                Alien alien = new Alien(alienX + 18*j, alienY + 18*i);
                alien.setImage(ii.getImage());
                aliens.add(alien);
            }
        }

        player = new Player();
        shot = new Shot();
        bonus = new Bonus();

        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
        }
    }

    public void drawAliens(Graphics g)
    {
        Iterator it = aliens.iterator();

        while (it.hasNext()) {
            Alien alien = (Alien) it.next();

            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {
                alien.die();
            }
        }
    }

    public void drawPlayer(Graphics g) {

        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {
            player.die();
            ingame = false;
        }
    }

    public void drawShot(Graphics g) {
        if (shot.isVisible())
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
    }
    public void drawShots(Graphics g) {
        for(Shot shot : machineGun){
            if (shot.isVisible())
                g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }
    public void drawBombing(Graphics g) {

        Iterator i3 = aliens.iterator();

        while (i3.hasNext()) {
            Alien a = (Alien) i3.next();

            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }


    public void drawBonus(Graphics g) {
        if (bonus.isVisible()) {
            g.drawImage(bonus.getImage(), bonus.getX(), bonus.getY(), this);
        }
    }

    public void paint(Graphics g)
    {
        super.paint(g);

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        Date actualDate = new Date();
        Calendar actualCalendar =  Calendar.getInstance();

        if (ingame) {

            g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawShots(g);
            drawBombing(g);
            drawBonus(g);
            System.out.println("Date : "+actualCalendar.get(Calendar.SECOND));

            if(actualCalendar.get(Calendar.SECOND) == 15 || actualCalendar.get(Calendar.SECOND) == 45 ){
                bonus.setVisible(true);
            }

        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void gameOver()
    {

        Graphics g = this.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGTH);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH/2 - 30, BOARD_WIDTH-100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH/2 - 30, BOARD_WIDTH-100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message))/2,
                BOARD_WIDTH/2);
    }

    public void animationCycle()  {

        if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
            ingame = false;
            message = "Game won!";
        }

        // player

        player.act();

        // bonus
        if (bonus.isVisible()) {
            int bonusX = bonus.getX();
            int bonuxY = bonus.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (bonus.isVisible() && player.isVisible()) {
                if (bonusX >= (playerX) &&
                        bonusX <= (playerX + PLAYER_WIDTH) &&
                        bonuxY >= (playerY) &&
                        bonuxY <= (playerY+PLAYER_HEIGHT) ) {

                    ImageIcon ii =
                            new ImageIcon(getClass().getResource(expl));
                    player.setImage(ii.getImage());

                    playerX = playerX - 30;
                    for(int i = 0; i <= 50 ; i++){
                        machineGun.add(new Shot(playerX+(i*2),playerY));
                    }

                    bonus.die();



                }
            }
        }

        if(machineGun.size() > 0){
            for(Shot s : machineGun){
                System.out.println("Machine gun - "+s.isVisible());

                if (s.isVisible()) {
                    Iterator it = aliens.iterator();
                    int shotX = s.getX();
                    int shotY = s.getY();

                    while (it.hasNext()) {
                        Alien alien = (Alien) it.next();
                        int alienX = alien.getX();
                        int alienY = alien.getY();

                        if (alien.isVisible() && s.isVisible()) {
                            if (shotX >= (alienX) &&
                                    shotX <= (alienX + ALIEN_WIDTH) &&
                                    shotY >= (alienY) &&
                                    shotY <= (alienY+ALIEN_HEIGHT) ) {
                                ImageIcon ii =
                                        new ImageIcon(getClass().getResource(expl));
                                alien.setImage(ii.getImage());
                                alien.setDying(true);
                                deaths++;
                                shot.die();
                            }
                        }
                    }
                    System.out.println("bouge - "+s.getY());
                    int y = s.getY();
                    y -= 4;

                    if(y < 200){
                        ImageIcon ii = new ImageIcon(this.getClass().getResource(playerShip));
                        player.setImage(ii.getImage());

                    }

                    if (y < 0){
                        s.die();

                    }
                    else s.setY(y);
                }
            }

        }

        // shot
        if (shot.isVisible()) {
            System.out.println("shot");
            Iterator it = aliens.iterator();
            int shotX = shot.getX();
            int shotY = shot.getY();

            while (it.hasNext()) {
                Alien alien = (Alien) it.next();
                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX) &&
                            shotX <= (alienX + ALIEN_WIDTH) &&
                            shotY >= (alienY) &&
                            shotY <= (alienY+ALIEN_HEIGHT) ) {
                        ImageIcon ii =
                                new ImageIcon(getClass().getResource(expl));
                        alien.setImage(ii.getImage());
                        alien.setDying(true);
                        deaths++;
                        shot.die();
                    }
                }
            }

            int y = shot.getY();
            y -= 4;

            if(y < 200){
                ImageIcon ii = new ImageIcon(this.getClass().getResource(playerShip));
                player.setImage(ii.getImage());

            }

            if (y < 0){
                shot.die();
            }
            else shot.setY(y);
        }

        // aliens

        Iterator it1 = aliens.iterator();

        while (it1.hasNext()) {
            Alien a1 = (Alien) it1.next();
            int x = a1.getX();

            if (x  >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
                direction = -1;
                Iterator i1 = aliens.iterator();
                while (i1.hasNext()) {
                    Alien a2 = (Alien) i1.next();
                    a2.setY(a2.getY() + GO_DOWN);
                }
            }

            if (x <= BORDER_LEFT && direction != 1) {
                direction = 1;

                Iterator i2 = aliens.iterator();
                while (i2.hasNext()) {
                    Alien a = (Alien)i2.next();
                    a.setY(a.getY() + GO_DOWN);
                }
            }
        }


        Iterator it = aliens.iterator();

        while (it.hasNext()) {
            Alien alien = (Alien) it.next();
            if (alien.isVisible()) {

                int y = alien.getY();

                if (y > GROUND - ALIEN_HEIGHT) {
                    ingame = false;
                    message = "Invasion!";
                }

                alien.act(direction);
            }
        }

        // bombs

        Iterator i3 = aliens.iterator();
        Random generator = new Random();

        while (i3.hasNext()) {
            int shot = generator.nextInt(15);
            Alien a = (Alien) i3.next();
            Alien.Bomb b = a.getBomb();
            if (shot == CHANCE && a.isVisible() && b.isDestroyed()) {

                b.setDestroyed(false);
                b.setX(a.getX());
                b.setY(a.getY());
            }

            int bombX = b.getX();
            int bombY = b.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !b.isDestroyed()) {
                if ( bombX >= (playerX) &&
                        bombX <= (playerX+PLAYER_WIDTH) &&
                        bombY >= (playerY) &&
                        bombY <= (playerY+PLAYER_HEIGHT) ) {
                    ImageIcon ii =
                            new ImageIcon(this.getClass().getResource(expl));
                    player.setImage(ii.getImage());
                    player.setDying(true);
                    b.setDestroyed(true);;
                }
            }

            if (!b.isDestroyed()) {
                b.setY(b.getY() + 1);
                if (b.getY() >= GROUND - BOMB_HEIGHT) {
                    b.setDestroyed(true);
                }
            }
        }
    }

    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (ingame) {
            repaint();
            animationCycle();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0)
                sleep = 2;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
            beforeTime = System.currentTimeMillis();
        }
        gameOver();
    }

    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            if (ingame)
            {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    ImageIcon ii = new ImageIcon(this.getClass().getResource(playerShooting));
                    player.setImage(ii.getImage());

                    if (!shot.isVisible())
                        shot = new Shot(x, y);
                }
            }
        }
    }
}