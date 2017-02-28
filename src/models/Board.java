package models;

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
    private Joueur joueur;
    private Boss boss;
    private Bonus bonus;
    private Shot shot;
    private List<Shot> machineGun = new ArrayList<Shot> ();
    private List<Bomb> machineBomb = new ArrayList<Bomb> ();
    private boolean machineGunUp = false;
    private int alienX = 150;
    private int alienY = 5;
    private int direction = -5;
    private int directionBoss = -5;
    private int deaths = 0;
    private boolean ingame = true;
    private final String expl = "../assets/explosion.png";
    private final String playerShip = "../assets/player.png";
    private final String playerShooting = "../assets/playerShooting.png";
    private final String alienImage = "../assets/alien.png";
    private final String vaisseauBaptiste = "../assets/vaisseauBaptiste.png";
    private final String vaisseauJordan = "../assets/alien_fun.png";
    private String message = "Game Over";
    private int alienSpeed = 5;
    private int shootSpeed = 10;
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



        ImageIcon ii = new ImageIcon(this.getClass().getResource(alienImage));

        for (int i=0; i < 4; i++) {
            for (int j=0; j < 6; j++) {
                Alien alien = new Alien(alienX + 55*j, alienY + 55*i);
                alien.setImage(ii.getImage());
                aliens.add(alien);
            }
        }
        try {
            joueur = new Joueur("Skywalker", "luke", "bgdelespace", new ViperMKII());
        }catch(Exception e){

        }
        shot = new Shot();
        bonus = new Bonus();
        boss = new Boss(200,20);
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

    public void drawBoss(Graphics g)
    {
        if (boss.isVisible()) {
            g.drawImage(boss.getImage(), boss.getX(), boss.getY(), this);
        }

        if (boss.isDying()) {
            boss.die();
        }
    }

    public void drawPlayer(Graphics g) {

        if (joueur.isVisible()) {
            g.drawImage(joueur.getImage(), joueur.getX(), joueur.getY(), this);
        }

        if (joueur.isDying()) {
            joueur.die();
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

            Bomb b = a.getBomb();

            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }

        // -- Boss bomb
        Bomb bossBomb = boss.getBomb();
        if (!bossBomb.isDestroyed()) {
            g.drawImage(bossBomb.getImage(), bossBomb.getX(), bossBomb.getY(), this);
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
            drawBoss(g);
            drawPlayer(g);
            drawShot(g);
            drawShots(g);
            drawBombing(g);
            drawBonus(g);


             if( (actualCalendar.get(Calendar.SECOND)%4)  == 0){
                bonus.setVisible(true);
             }
            if((actualCalendar.get(Calendar.SECOND)%8)  == 0){
                bonus.die();
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

    public void animationCycle() {


        Iterator alienDeadIterator = aliens.iterator();
        int deadCounter=0;
        while (alienDeadIterator.hasNext()) {
            Alien alien = (Alien) alienDeadIterator.next();
            if(alien.isDying()){
                deadCounter++;
            }
        }

        if (deadCounter == NUMBER_OF_ALIENS_TO_DESTROY){
            System.out.println("BOSS APPEARS");
            boss.setVisible(true);
        }

        if (deadCounter == NUMBER_OF_ALIENS_TO_DESTROY && boss.isDying()) {
            System.out.println("WIN");
            ingame = false;
            message = "Game won!";
        }

        // player

        joueur.act();

        // bonus
        if (bonus.isVisible()) {
            int bonusX = bonus.getX();
            int bonuxY = bonus.getY();
            int playerX = joueur.getX();
            int playerY = joueur.getY();

            if (bonus.isVisible() && joueur.isVisible()) {
                if (bonusX >= (playerX) &&
                        bonusX <= (playerX + PLAYER_WIDTH) &&
                        bonuxY >= (playerY) &&
                        bonuxY <= (playerY+PLAYER_HEIGHT) ) {


                    if(bonus.getMode() == 1){
                        ImageIcon ii = new ImageIcon(getClass().getResource(expl));
                        joueur.setImage(ii.getImage());

                        playerX = playerX - 30;
                        for(int i = 0; i <= 50 ; i++){
                            machineGun.add(new Shot(playerX+(i*5),playerY));
                        }
                    }
                    if(bonus.getMode() == 2){
                        Iterator it = aliens.iterator();
                        while (it.hasNext()) {
                            Alien alien = (Alien) it.next();
                            ImageIcon ii =
                                    new ImageIcon(getClass().getResource(vaisseauJordan));
                            alien.setImage(ii.getImage());
                            alienSpeed = 5;
                        }
                    }
                    if(bonus.getMode() == 3){
                        ImageIcon ii = new ImageIcon(getClass().getResource(vaisseauBaptiste));
                        joueur.setImage(ii.getImage());
                        shootSpeed = shootSpeed * 2;
                        joueur.setMoveSpeed(joueur.getMoveSpeed()+3);
                    }
                    bonus.die();
                    bonus = new Bonus();


                }
            }
        }

        if(machineGun.size() > 0){
            for(Shot s : machineGun){


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

                    int y = s.getY();
                    y -=     10;

                    if(y < 650){
                        ImageIcon ii = new ImageIcon(this.getClass().getResource(playerShip));
                        joueur.setImage(ii.getImage());

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


                        // -- Comptage des points de vie
                        System.out.println("// ------------------------------");
                        System.out.println("// -- Vie de l'alien avant : ");
                        System.out.println(alien.getVaisseau().getPointsBouclierActuel());
                        System.out.println(alien.getVaisseau().getPointsStructureActuel());
                        joueur.getMonVaisseau().attaque(alien.getVaisseau());
                        System.out.println("// -- Vie de l'alien apres : ");
                        System.out.println(alien.getVaisseau().getPointsBouclierActuel());
                        System.out.println(alien.getVaisseau().getPointsStructureActuel());
                        System.out.println("Il est mort : "+alien.getVaisseau().isDetruit());
                        System.out.println("// ------------------------------");
                        // --
                        if(alien.getVaisseau().isDetruit()){

                            ImageIcon ii =
                                    new ImageIcon(getClass().getResource(expl));
                            alien.setImage(ii.getImage());

                            alien.setDying(true);
                            deaths++;

                        }
                        shot.die();
                    }
                }
            }

            if(boss.isVisible() && shot.isVisible()){
                int bossX = boss.getX();
                int bossY = boss.getY();
                    if (shotX >= (bossX) &&
                            shotX <= (bossX + BOSS_WIDTH) &&
                            shotY >= (bossY) &&
                            shotY <= (bossY+BOSS_HEIGHT) ) {


                        // -- Comptage des points de vie
                        System.out.println("// ------------------------------");
                        System.out.println("// -- Vie du boss avant : ");
                        System.out.println(boss.getVaisseau().getPointsBouclierActuel());
                        System.out.println(boss.getVaisseau().getPointsStructureActuel());
                        joueur.getMonVaisseau().attaque(boss.getVaisseau());
                        System.out.println("// -- Vie de l'alien apres : ");
                        System.out.println(boss.getVaisseau().getPointsBouclierActuel());
                        System.out.println(boss.getVaisseau().getPointsStructureActuel());
                        System.out.println("Il est mort : "+boss.getVaisseau().isDetruit());
                        System.out.println("// ------------------------------");
                        // --
                        if(boss.getVaisseau().isDetruit()){

                            ImageIcon ii =
                                    new ImageIcon(getClass().getResource(expl));
                            boss.setImage(ii.getImage());

                            boss.setDying(true);
                            deaths++;

                        }
                        shot.die();
                    }

            }


            int y = shot.getY();
            y -= shootSpeed;

            /*if(y < 650){
                ImageIcon ii = new ImageIcon(this.getClass().getResource(playerShip));
                joueur.setImage(ii.getImage());

            }*/

            if (y < 0){
                shot.die();
            }
            else shot.setY(y);
        }

        // -- DEPLACEMENT ALIENS

        Iterator it1 = aliens.iterator();

        while (it1.hasNext()) {
           Alien a1 = (Alien) it1.next();
            int x = a1.getX();

            if (x  >= BOARD_WIDTH - BORDER_RIGHT && direction != -alienSpeed) {
                direction = -alienSpeed;
                Iterator i1 = aliens.iterator();
                while (i1.hasNext()) {
                    Alien a2 = (Alien) i1.next();
                    a2.setY(a2.getY() + GO_DOWN);
                }
            }

            if (x <= BORDER_LEFT && direction != alienSpeed) {
                direction = alienSpeed;

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

        // --

        // -- DEPLACEMENT BOSS
        if(boss.isVisible()){
            int x = boss.getX();

            if (x  >= BOARD_WIDTH - BORDER_RIGHT && directionBoss != -5) {
                directionBoss = -5;
                boss.setY(boss.getY() + GO_DOWN);

            }

            if (x <= BORDER_LEFT && directionBoss != 5) {
                directionBoss = 5;
                boss.setY(boss.getY() + GO_DOWN);
            }

            boss.act(directionBoss);


            Bomb b = boss.getBomb();
            System.out.println("BOMB NORMALE");
            if (boss.isVisible() && b.isDestroyed()) {
                System.out.println(boss.getX()+" - "+boss.getY());
                b.setDestroyed(false);
                b.setX(boss.getX());
                b.setY(boss.getY()+200);
            }
            int bombX = b.getX();
            int bombY = b.getY();
            int playerX = joueur.getX();
            int playerY = joueur.getY();

            if (joueur.isVisible() && !b.isDestroyed()) {
                System.out.println(bombX+" - "+bombY);
                if ( bombX >= (playerX) &&
                        bombX <= (playerX+PLAYER_WIDTH) &&
                        bombY >= (playerY) &&
                        bombY <= (playerY+PLAYER_HEIGHT) ) {

                    // -- Comptage des points de vie
                    System.out.println("// ------------------------------");
                    System.out.println("// -- Vie de l'alien avant : ");
                    System.out.println(joueur.getMonVaisseau().getPointsBouclierActuel());
                    System.out.println(joueur.getMonVaisseau().getPointsStructureActuel());
                    boss.getVaisseau().attaque(joueur.getMonVaisseau());
                    System.out.println("// -- Vie de l'alien apres : ");
                    System.out.println(joueur.getMonVaisseau().getPointsBouclierActuel());
                    System.out.println(joueur.getMonVaisseau().getPointsStructureActuel());
                    System.out.println("Il est mort : "+joueur.getMonVaisseau().isDetruit());
                    System.out.println("// ------------------------------");
                    // --
                    if(joueur.getMonVaisseau().isDetruit()){


                        ImageIcon ii =
                                new ImageIcon(this.getClass().getResource(expl));
                        joueur.setImage(ii.getImage());
                        joueur.setDying(true);


                    }



                    b.setDestroyed(true);
                }
            }

            if (!b.isDestroyed()) {
                b.setY(b.getY() + 15);
                if (b.getY() >= GROUND - BOMB_HEIGHT) {
                    b.setDestroyed(true);
                }
            }

        }


        // --

        // bombs

        Iterator i3 = aliens.iterator();
        Random generator = new Random();

        while (i3.hasNext()) {
            int shot = generator.nextInt(15);
            Alien a = (Alien) i3.next();
            Bomb b = a.getBomb();
            if (shot == CHANCE && a.isVisible() && b.isDestroyed()) {

                b.setDestroyed(false);
                b.setX(a.getX());
                b.setY(a.getY());
            }

            int bombX = b.getX();
            int bombY = b.getY();
            int playerX = joueur.getX();
            int playerY = joueur.getY();

            if (joueur.isVisible() && !b.isDestroyed()) {
                if ( bombX >= (playerX) &&
                        bombX <= (playerX+PLAYER_WIDTH) &&
                        bombY >= (playerY) &&
                        bombY <= (playerY+PLAYER_HEIGHT) ) {





                    // -- Comptage des points de vie
                    System.out.println("// ------------------------------");
                    System.out.println("// -- Vie de l'alien avant : ");
                    System.out.println(joueur.getMonVaisseau().getPointsBouclierActuel());
                    System.out.println(joueur.getMonVaisseau().getPointsStructureActuel());
                    a.getVaisseau().attaque(joueur.getMonVaisseau());
                    System.out.println("// -- Vie de l'alien apres : ");
                    System.out.println(joueur.getMonVaisseau().getPointsBouclierActuel());
                    System.out.println(joueur.getMonVaisseau().getPointsStructureActuel());
                    System.out.println("Il est mort : "+joueur.getMonVaisseau().isDetruit());
                    System.out.println("// ------------------------------");
                    // --
                    if(joueur.getMonVaisseau().isDetruit()){


                        ImageIcon ii =
                                new ImageIcon(this.getClass().getResource(expl));
                        joueur.setImage(ii.getImage());
                        joueur.setDying(true);


                    }



                    b.setDestroyed(true);
                }
            }

            if (!b.isDestroyed()) {
                b.setY(b.getY() + 6);
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
            joueur.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {

            joueur.keyPressed(e);

            int x = joueur.getX();
            int y = joueur.getY();

            if (ingame)
            {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    /*ImageIcon ii = new ImageIcon(this.getClass().getResource(playerShooting));
                    joueur.setImage(ii.getImage());*/

                    if (!shot.isVisible())
                        shot = new Shot(x, y);

                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    Iterator it = aliens.iterator();

                    while (it.hasNext()) {
                        Alien alien = (Alien) it.next();
                        alien.die();
                        deaths++;
                        boss.setVisible(true);
                    }

                }
            }
        }
    }
}