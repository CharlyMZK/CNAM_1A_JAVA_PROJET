import models.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;
import java.util.List;


public class Board extends JPanel implements Runnable, Commons {

    // -- Dimensions
    private Dimension d;
    private int alienX = 150;
    private int alienY = 5;
    private int direction = -5;
    private int directionBoss = -5;
    // -- End dimensions


    // -- Sprites
    private ArrayList aliens;
    private Joueur joueur = SpaceInvaders.getInstance().getJoueurs().get(0);
    private Boss boss;
    private Bonus bonus;
    private Shot shot;
    private List<Shot> machineGun = new ArrayList<Shot>();
    private List<Bomb> machineBomb = new ArrayList<Bomb>();
    // -- End sprites

    // -- Variables
    private boolean machineGunUp = false;
    private String message = "Game Over";
    private int alienSpeed = 5;
    private int shootSpeed = 10;
    private Thread animator;
    private int deaths = 0;
    private boolean ingame = true;
    private boolean isBaptiste = false;
    private boolean bannerVisible = true;
    private boolean hautFaitVisible = true;
    private int niveauActuel = 1;
    private Calendar hautFaitCalendar = Calendar.getInstance();
    private Calendar niveauCalendar = Calendar.getInstance();
    // -- End variables

    // -- Images
    private final String expl = "assets/explosion.png";
    private final String playerShooting = ".assets/playerShooting.png";
    private final String alienImage = "assets/alien.png";
    private final String vaisseauBaptiste = "assets/vaisseauBaptiste.png";
    private final String vaisseauJordan = "assets/alien_fun.png";
    private final String fond = "assets/fond.png";
    private String hautfait = "assets/premierePartieAchievement.png";
    private ImageIcon imageFond = null;
    private ImageIcon imageHautfait = null;
    // -- End images

    // -- Sons
    private final String sonTire = "src/assets/shot.wav";
    private final String sonFond = "src/assets/fond.wav";
    private Clip clipFond = null;
    // -- End sons

    /**
     * Constructor
     */
    public Board() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGTH);
        setBackground(Color.black);
        gameInit();
        setDoubleBuffered(true);
    }

    /**
     * Add a notification
     */
    public void addNotify() {
        super.addNotify();
        gameInit();
    }

    /**
     * Initalise le jeu
     */
    public void gameInit() {
        musicDeFond(1);
        aliens = new ArrayList(); // -- Initialisation des aliens
        ImageIcon ii = new ImageIcon(this.getClass().getResource(alienImage)); // -- Image des aliens
        imageFond = new ImageIcon(this.getClass().getResource(fond)); // -- Image du fond
        imageHautfait = new ImageIcon(this.getClass().getResource(hautfait)); // -- Image du haut fait
        // -- Creation des aliens
        for (int i = 0; i < (niveauActuel * 2); i++) {
            for (int j = 0; j < 6; j++) {
                Alien alien = new Alien(alienX + 55 * j, alienY + 55 * i);
                alien.setImage(ii.getImage());
                aliens.add(alien);
            }
        }

        // -- Creation du boss
        boss = new Boss(200, -500);
        for (int i = 0; i <= 70; i++) {
            machineBomb.add(new Bomb(boss.getX() + (i * 5), boss.getY()));
        }
        // -- Creation des autre sprites
        shot = new Shot();
        bonus = new Bonus();

        // -- Animation
        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
        }
    }

    /**
     * Dessine une banniere
     *
     * @param g graphics
     */
    public void drawBanner(Graphics g) {
        g.setColor(new Color(0, 32, 48));
        g.fillRect(400, BOARD_WIDTH / 2 - 30, 140, 50);
        g.setColor(Color.white);
        g.drawRect(400, BOARD_WIDTH / 2 - 30, 140, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString("Niveau " + niveauActuel, (BOARD_WIDTH - metr.stringWidth(message)) / 2, BOARD_WIDTH / 2);

    }

    /**
     * Dessine les aliens
     *
     * @param g graphics
     */
    public void drawAliens(Graphics g) {
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

    /**
     * Dessine le boss
     *
     * @param g graphics
     */
    public void drawBoss(Graphics g) {
        if (boss.isVisible()) {
            g.drawImage(boss.getImage(), boss.getX(), boss.getY(), this);
        }

        if (boss.isDying()) {
            boss.die();
        }
    }

    /**
     * Dessine le joueur
     *
     * @param g graphics
     */
    public void drawPlayer(Graphics g) {

        if (joueur.isVisible()) {
            g.drawImage(joueur.getImage(), joueur.getX(), joueur.getY(), this);
        }

        if (joueur.isDying()) {
            joueur.die();
            ingame = false;
        }
    }

    /**
     * Dessine le laser tiré par le joueur
     *
     * @param g graphics
     */
    public void drawShot(Graphics g) {
        if (shot.isVisible())
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
    }

    /**
     * Dessine les tirs créés par le bonus
     *
     * @param g graphics
     */
    public void drawShots(Graphics g) {
        for (Shot shot : machineGun) {
            if (shot.isVisible()) {
                try {
                    g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
                } catch (Exception e) {
                    System.out.println("Exception");
                }

            }
        }
    }

    /**
     * Dessine les bombes des aliens
     *
     * @param g graphics
     */
    public void drawBombing(Graphics g) {
        Iterator alienIterator = aliens.iterator(); // -- Recupération de l'itérateur

        // -- Pour chaque alien, on dessine sa bombe
        while (alienIterator.hasNext()) {
            Alien a = (Alien) alienIterator.next(); // -- Get l'alien
            Bomb b = a.getBomb();                   // -- Get la bombe

            // -- Si la bombe est toujours sur le terrain
            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }

        // -- Get la bombe du boss
        Bomb bossBomb = boss.getBomb();
        if (!bossBomb.isDestroyed()) {
            g.drawImage(bossBomb.getImage(), bossBomb.getX(), bossBomb.getY(), this);
        }
    }

    /**
     * Dessine les bombes des aliens
     *
     * @param g graphics
     */
    public void drawMachineBomb(Graphics g) {
        // -- Get la bombe du boss
        if (boss.isVisible()) {
            for (Bomb b : machineBomb) {
                if (!b.isDestroyed()) {
                    g.drawImage(b.getImage(), b.getX(), b.getY(), this);
                }
            }
        }
    }

    /**
     * Dessine un bonus
     *
     * @param g graphics
     */
    public void drawBonus(Graphics g) {
        if (bonus.isVisible()) {
            g.drawImage(bonus.getImage(), bonus.getX(), bonus.getY(), this);
        }
    }

    /**
     * Dessine un hautfait
     *
     * @param g graphics
     */
    public void drawHautFait(Graphics g) {
        if (hautFaitVisible) {
            g.drawImage(imageHautfait.getImage(), BOARD_WIDTH - 200, 20, this);
        }
    }

    /**
     * Dessine le board
     *
     * @param g graphics
     */
    public void paint(Graphics g) {
        super.paint(g);
        // -- Couleur du fond
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.drawImage(imageFond.getImage(), 0, 0, this);
        g.setColor(Color.green);
        Calendar actualCalendar = Calendar.getInstance();

        if (ingame) {

            // -- Dessin des sprites
            g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
            drawAliens(g);
            Date actualDate = new Date();
            if (bannerVisible) {
                drawBanner(g);
                if (((actualDate.getTime() - hautFaitCalendar.getTime().getTime()) / 1000) == 2) {
                    bannerVisible = false;
                }
            }
            if (hautFaitVisible) {
                drawHautFait(g);
                if (((actualDate.getTime() - niveauCalendar.getTime().getTime()) / 1000) == 3) {
                    hautFaitVisible = false;
                }
            }

            drawBoss(g);
            drawPlayer(g);
            drawShot(g);
            drawShots(g);
            drawBombing(g);
            drawMachineBomb(g);
            drawBonus(g);

            // -- Affichage periodique des bonus
            if ((actualCalendar.get(Calendar.SECOND) % 4) == 0) {
                bonus.setVisible(true);
            }
            if ((actualCalendar.get(Calendar.SECOND) % 8) == 0) {
                bonus.die();
            }
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    /**
     * Affichage du Game over
     */
    public void gameOver() {
        musicDeFond(0);
        Graphics g = this.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGTH);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message)) / 2,
                BOARD_WIDTH / 2);
    }

    /**
     * Lance l'effet du bonus recupéré
     *
     * @param playerX cooroonnées
     * @param playerY cooroonnées
     */
    public void lancerEffetBonus(int playerX, int playerY) {
        if (bonus.getMode() == 1) {
            ImageIcon ii = new ImageIcon(getClass().getResource(expl));
            joueur.setImage(ii.getImage());
            playerX = playerX - 30;
            for (int i = 0; i <= 50; i++) {
                machineGun.add(new Shot(playerX + (i * 5), playerY));
            }
        }
        if (bonus.getMode() == 2) {
            Iterator it = aliens.iterator();
            while (it.hasNext()) {
                Alien alien = (Alien) it.next();
                ImageIcon ii =
                        new ImageIcon(getClass().getResource(vaisseauJordan));
                alien.setImage(ii.getImage());
                alienSpeed = 10;
            }

        }
        if (bonus.getMode() == 3) {
            ImageIcon ii = new ImageIcon(getClass().getResource(vaisseauBaptiste));
            joueur.setImage(ii.getImage());
            shootSpeed = shootSpeed * 2;
            joueur.setMoveSpeed(joueur.getMoveSpeed() + 3);
            isBaptiste = true;
        }
        bonus.die();
        bonus = new Bonus();
    }

    /**
     * On verifie si le jeu est fini
     */
    public void verificationFinJeu() {
        Iterator alienDeadIterator = aliens.iterator();
        int deadCounter = 0;
        while (alienDeadIterator.hasNext()) {
            Alien alien = (Alien) alienDeadIterator.next();
            if (alien.isDying()) {
                deadCounter++;
            }
        }

        if (deadCounter == (NUMBER_OF_ALIENS_TO_DESTROY * niveauActuel)) {
            System.out.println("BOSS APPEARS");
            boss.setVisible(true);
        }

        if (deadCounter == (NUMBER_OF_ALIENS_TO_DESTROY * niveauActuel) && boss.isDying()) {
            System.out.println("WIN");

            if (niveauActuel == 7) {
                ingame = false;
                message = "Game won!";
            } else {
                niveauActuel++;
                hautfait = "assets/niveau" + niveauActuel + "Achievement.png";
                hautFaitVisible = true;
                Date dateActuelle = new Date();
                hautFaitCalendar.setTime(dateActuelle);
                bannerVisible = true;
                niveauCalendar.setTime(dateActuelle);
                gameInit();
            }
        }
    }

    /**
     * On verifie si l'alien est touché par un laser
     *
     * @param s le laser
     */
    public void verificationAlienTouche(Shot s) {
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
                        shotY <= (alienY + ALIEN_HEIGHT)) {
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
        y -= 10;


        if (y < 650) {
            if (isBaptiste) {
                ImageIcon ii = new ImageIcon(this.getClass().getResource(vaisseauBaptiste));
                joueur.setImage(ii.getImage());
            } else {
                ImageIcon ii = new ImageIcon(SpaceInvaders.getInstance().getJoueurs().get(0).getImage());
                joueur.setImage(ii.getImage());
            }


        }


        if (y < 0) {
            s.die();

        } else s.setY(y);
    }

    /**
     * On verifie si le boss est touché par un laser
     *
     * @param s le laser
     */
    public void verificationBossTouche(Shot s) {
        int shotX = s.getX();
        int shotY = s.getY();
        int bossX = boss.getX();
        int bossY = boss.getY();
        if (shotX >= (bossX) &&
                shotX <= (bossX + BOSS_WIDTH) &&
                shotY >= (bossY) &&
                shotY <= (bossY + BOSS_HEIGHT)) {


            // -- Comptage des points de vie
            System.out.println("// ------------------------------");
            System.out.println("// -- Vie du boss avant : ");
            System.out.println(boss.getVaisseau().getPointsBouclierActuel());
            System.out.println(boss.getVaisseau().getPointsStructureActuel());
            joueur.getMonVaisseau().attaque(boss.getVaisseau());
            System.out.println("// -- Vie de l'alien apres : ");
            System.out.println(boss.getVaisseau().getPointsBouclierActuel());
            System.out.println(boss.getVaisseau().getPointsStructureActuel());
            System.out.println("Il est mort : " + boss.getVaisseau().isDetruit());
            System.out.println("// ------------------------------");
            // --
            if (boss.getVaisseau().isDetruit()) {
                ImageIcon ii = new ImageIcon(getClass().getResource(expl));
                boss.setImage(ii.getImage());
                boss.setDying(true);
                deaths++;
                for (Bomb b : machineBomb) {
                    b.setDying(true);
                }
                machineBomb.clear();
            }
            shot.die();
        }

    }

    /**
     * On verifie si l'alien touche le bord
     */
    public void verificationAlienCollision() {
        Iterator aliensMouvementIterator = aliens.iterator();

        while (aliensMouvementIterator.hasNext()) {
            Alien alien = (Alien) aliensMouvementIterator.next();
            int x = alien.getX();
            // -- Si l'alien touche le bord il descend
            if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -alienSpeed) {
                direction = -alienSpeed;
                Iterator i1 = aliens.iterator();
                while (i1.hasNext()) {
                    Alien a2 = (Alien) i1.next();
                    a2.setY(a2.getY() + GO_DOWN);
                }
            }
            // -- Si l'alien touche le bord il descend
            if (x <= BORDER_LEFT && direction != alienSpeed) {
                direction = alienSpeed;

                Iterator i2 = aliens.iterator();
                while (i2.hasNext()) {
                    Alien a = (Alien) i2.next();
                    a.setY(a.getY() + GO_DOWN);
                }
            }


        }
    }

    /**
     * On verifie si le boss touche le bord
     */
    public void verificationBossCollision() {
        int x = boss.getX();

        if (x >= BOARD_WIDTH - BORDER_RIGHT && directionBoss != -5) {
            directionBoss = -5;
            boss.setY(boss.getY() + GO_DOWN);

        }

        if (x <= BORDER_LEFT && directionBoss != 5) {
            directionBoss = 5;
            boss.setY(boss.getY() + GO_DOWN);
        }
    }

    /**
     * Les aliens avancent
     */
    public void lancerInvasion() {

        Iterator alienInvasionIterator = aliens.iterator();
        // -- Pour chaque alien
        while (alienInvasionIterator.hasNext()) {
            Alien alien = (Alien) alienInvasionIterator.next();
            if (alien.isVisible()) {

                // -- On verifie si il a envahi
                int y = alien.getY();
                if (y > GROUND - ALIEN_HEIGHT) {
                    ingame = false;
                    message = "Invasion!";
                }

                // -- Sinon il bouge
                alien.act(direction);
            }
        }
    }

    /**
     * Le boss avance
     */
    public void lancerBossInvasion() {
        // -- On verifie si il a envahi
        int y = boss.getY();
        if (y < 10) {
            boss.setY(boss.getY() + 3);
        }
        if (y > GROUND - BOSS_HEIGHT) {
            ingame = false;
            message = "Invasion!";
        }
        boss.act(directionBoss);
    }

    /**
     * Le boss tire une bombe
     */
    public void bossTirerBombe() {
        Boolean bombesDetruites = true;
        for (Bomb b : machineBomb) {
            if (!b.isDestroyed()) {
                bombesDetruites = false;
            }
        }
        if (bombesDetruites) {
            machineBomb.clear();
            for (int i = 0; i <= 70; i++) {
                machineBomb.add(new Bomb(boss.getX() + (i * 5), boss.getY()));
            }
        }


        for (Bomb b : machineBomb) {
            if (boss.isVisible() && b.isDestroyed()) {
                b.setDestroyed(false);
            }
            int bombX = b.getX();
            int bombY = b.getY();
            int playerX = joueur.getX();
            int playerY = joueur.getY();

            if (joueur.isVisible() && !b.isDestroyed()) {

                if (bombX >= (playerX) && bombX <= (playerX + PLAYER_WIDTH) && bombY >= (playerY) && bombY <= (playerY + PLAYER_HEIGHT)) {

                    // -- Comptage des points de vie
                    boss.getVaisseau().attaque(joueur.getMonVaisseau());

                    if (joueur.getMonVaisseau().isDetruit()) {
                        ImageIcon ii = new ImageIcon(this.getClass().getResource(expl));
                        joueur.setImage(ii.getImage());
                        joueur.setDying(true);
                    }
                    b.setDestroyed(true);
                }
            }

            // -- La bombe bouge
            if (!b.isDestroyed()) {
                b.setY(b.getY() + 15);
                if (b.getY() >= GROUND - BOMB_HEIGHT) {
                    b.setDestroyed(true);
                }
            }
        }
    }

    /**
     * Les aliens tire leurs bombes
     */
    public void alienTirerBombe() {
        Iterator alienIterator = aliens.iterator();
        Random generator = new Random();

        while (alienIterator.hasNext()) {
            int shot = generator.nextInt(15);
            Alien a = (Alien) alienIterator.next();
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
                if (bombX >= (playerX) &&
                        bombX <= (playerX + PLAYER_WIDTH) &&
                        bombY >= (playerY) &&
                        bombY <= (playerY + PLAYER_HEIGHT)) {

                    // -- Comptage des points de vie
                    System.out.println("// ------------------------------");
                    System.out.println("// -- Vie de l'alien avant : ");
                    System.out.println(joueur.getMonVaisseau().getPointsBouclierActuel());
                    System.out.println(joueur.getMonVaisseau().getPointsStructureActuel());
                    a.getVaisseau().attaque(joueur.getMonVaisseau());
                    System.out.println("// -- Vie de l'alien apres : ");
                    System.out.println(joueur.getMonVaisseau().getPointsBouclierActuel());
                    System.out.println(joueur.getMonVaisseau().getPointsStructureActuel());
                    System.out.println("Il est mort : " + joueur.getMonVaisseau().isDetruit());
                    System.out.println("// ------------------------------");
                    // --
                    if (joueur.getMonVaisseau().isDetruit()) {
                        ImageIcon ii = new ImageIcon(this.getClass().getResource(expl));
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

    /**
     * Le joueur tire un laser
     */
    public void joueurTirerLaser() {
        // -- Si on a hit le bonus machineGun, on verifie si les aliens sont touchés par nos lasers
        if (machineGun.size() > 0) {
            for (Shot s : machineGun) {
                if (s.isVisible()) {
                    verificationAlienTouche(s);
                }
            }

        }

        // Traitement du tir du joueur
        if (shot.isVisible()) {
            verificationAlienTouche(shot);
            if (boss.isVisible()) {
                verificationBossTouche(shot);
            }
            shot.move(shootSpeed);
        }
    }

    /**
     * Cycle periodique de l'animation en jeu
     */
    public void animationCycle() {

        // -- On verifie si le jeu est fini
        verificationFinJeu();
        // -- Sinon

        // -- Le joueur joue
        joueur.act();

        // -- Si le bonus est visible : traitement du bonus
        if (bonus.isVisible()) {
            int bonusX = bonus.getX();
            int bonuxY = bonus.getY();
            int playerX = joueur.getX();
            int playerY = joueur.getY();

            if (bonus.isVisible() && joueur.isVisible()) {
                if (bonusX >= (playerX) && bonusX <= (playerX + PLAYER_WIDTH) && bonuxY >= (playerY) && bonuxY <= (playerY + PLAYER_HEIGHT)) {
                    lancerEffetBonus(playerX, playerY);
                }
            }
        }

        // -- Le joueur tire
        joueurTirerLaser();

        // -- DEPLACEMENT ALIENS

        // -- Si l'alien touche le bord on le descend
        verificationAlienCollision();

        // -- Deplace tout les aliens s'ils n'ont pas envahi le joueur
        lancerInvasion();

        // -- Chaque alien tire une bombe
        alienTirerBombe();

        // -- FIN DEPLACEMENT ALIENS

        // -- DEPLACEMENT BOSS
        if (boss.isVisible()) {
            verificationBossCollision(); // -- On verifie si il touche le bord
            lancerBossInvasion();        // -- On déplace le boss
            bossTirerBombe();            // -- Il tire une bombe
        }
        // -- FIN DEPLACEMENT BOSS
    }

    /**
     * Run du thread
     */
    public void run() {
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();

        // -- Pendant le jeu
        while (ingame) {
            // -- On redessine le board
            repaint();
            // -- Relance l'animation
            animationCycle();

            // -- Sleep
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

    /**
     * Key pressed par le joueur
     */
    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            joueur.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {

            joueur.keyPressed(e);

            int x = joueur.getX();
            int y = joueur.getY();

            if (ingame) {
                // -- Si on press la fleche du haut : tir
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    jouerSon(sonTire);
                    if (!shot.isVisible()) {
                        shot = new Shot(x, y);
                    }

                }
                // -- Fleche du bas : tue tout les aliens
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    Iterator it = aliens.iterator();

                    while (it.hasNext()) {
                        Alien alien = (Alien) it.next();
                        alien.setDying(true);
                        alien.die();
                        deaths++;
                        boss.setVisible(true);
                        boss.setDying(true);
                        boss.die();
                    }

                }

                if (e.getKeyCode() == KeyEvent.VK_ALT) {
                    System.out.println("reset");
                    gameInit();
                }
            }
        }
    }

    // Permet de jouer un son
    private void jouerSon(String chemin) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(chemin));
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Permet de jouer la musique en background
    private void musicDeFond(int mode) {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(sonFond));
            if (clipFond == null) {
                clipFond = AudioSystem.getClip();
            }

            if (mode == 1) {
                try {
                    clipFond.open(inputStream);
                    clipFond.loop(Clip.LOOP_CONTINUOUSLY);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                clipFond.stop();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void setIngame(boolean ingame) {
        this.ingame = ingame;
    }
}