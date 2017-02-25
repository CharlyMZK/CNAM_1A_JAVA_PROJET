import exceptions.ArmurerieException;
import models.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by MZK on 03/02/2017.
 */
public class JMenuFrame extends JFrame{
    private JMenuBar menuBar;
    private JMenu menuJoueurs;
    private JMenu menuVaisseaux;
    private JMenu menuArmurerie;


    private JMenuItem itemCreationJoueur;
    private JMenuItem itemSupressionJoueur;
    private JMenuItem itemChoixJoueur;
    private JMenuItem itemImportArmes;

    private JMenuItem itemCreationVaisseau;
    private JMenuItem itemSupressionVaisseau;

    private JMenuItem itemCreationArmurerie;
    private JMenuItem itemSupressionArmurerie;


    private JPanel rootPanel;
    private JButton shoppingButton;
    private JButton apparenceButton;
    private JList playerList;

    private JButton deletePlayerButton;
    private JButton createPlayerButton;
    private JPanel vaisseauPanel;

    public JMenuFrame(SpaceInvaders jeu) {

        // -- Menu bar
        menuBar = new JMenuBar();

        // -- JMenus
        menuJoueurs = new JMenu();
        menuVaisseaux = new JMenu();
        menuArmurerie = new JMenu();

        // -- Menu items
        itemImportArmes = new JMenuItem();
        itemCreationJoueur = new JMenuItem();
        itemSupressionJoueur = new JMenuItem();
        itemChoixJoueur = new JMenuItem();
        itemCreationVaisseau = new JMenuItem();
        itemSupressionVaisseau = new JMenuItem();
        itemCreationArmurerie = new JMenuItem();
        itemSupressionArmurerie = new JMenuItem();

        // -- Menu joueurs
        menuJoueurs = new JMenu("Menu joueurs");
        menuJoueurs.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menuJoueurs);

        // -- Menu vaisseaux
        menuVaisseaux = new JMenu("Menu vaisseaux");
        menuVaisseaux.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menuVaisseaux);

        // -- Menu armurerie
        menuArmurerie = new JMenu("Menu armurerie");
        menuArmurerie.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menuArmurerie);

        // -- Add et actions joueurs

        // -- Creation du joueur
        itemCreationJoueur = new JMenuItem("Créer un joueur",
                KeyEvent.VK_T);

        // -- Supression du joueur
        itemSupressionJoueur = new JMenuItem("Supprimer un joueur ");
        itemSupressionJoueur.setMnemonic(KeyEvent.VK_B);

        // -- Choix du joueur
        itemChoixJoueur = new JMenuItem("Choix un joueur ");
        itemChoixJoueur.setMnemonic(KeyEvent.VK_B);

        // -- Add et actions vaisseaux
        itemCreationVaisseau = new JMenuItem("Créer un vaisseau ennemi",
                KeyEvent.VK_T);

        itemSupressionVaisseau = new JMenuItem("Supprimer un vaisseau ennemi",
                KeyEvent.VK_T);

        menuVaisseaux.add(itemSupressionVaisseau);
        menuVaisseaux.add(itemCreationVaisseau);

        // -- Add et actions armure
        itemCreationArmurerie = new JMenuItem("Créer une arme",
                KeyEvent.VK_T);

        itemSupressionArmurerie = new JMenuItem("Supprimer une arme de l'armurerie",
                KeyEvent.VK_T);


        // -- creation arme
        itemImportArmes = new JMenuItem("Shopping",
                KeyEvent.VK_T);


        menuArmurerie.add(itemCreationArmurerie);
        menuArmurerie.add(itemSupressionArmurerie);
        menuJoueurs.add(itemCreationJoueur);
        menuJoueurs.add(itemSupressionJoueur);
        menuJoueurs.add(itemChoixJoueur);
        menuJoueurs.add(itemCreationJoueur);


        JImagePanel imagePanel = new JImagePanel();
        JDescriptionPanel descriptionPanel = new JDescriptionPanel() ;
        imagePanel.setImage(null);

        imagePanel.setPreferredSize (new Dimension(50,50));
        vaisseauPanel.add(BorderLayout.NORTH,descriptionPanel);
        vaisseauPanel.add(BorderLayout.CENTER,imagePanel);

        // -- liste des joueurs
        updateListPlayers(jeu);



        // -- LISTENERS DU TP3
        itemCreationJoueur.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom;
                String prenom;
                String pseudo;

                // -- Fields
                JTextField fieldNom = new JTextField();
                JTextField fieldPrenom = new JTextField();
                JTextField fieldPseudo = new JTextField();

                JPanel panel = new JPanel(new GridLayout(0, 1));

                panel.add(new JLabel("Veuillez indiquer un nom :"));
                panel.add(fieldNom);

                panel.add(new JLabel("Veuillez indiquer un prenom :"));
                panel.add(fieldPrenom);

                panel.add(new JLabel("Veuillez indiquer un pseudo :"));
                panel.add(fieldPseudo);


                // -- Display dialog
                int result = JOptionPane.showConfirmDialog(null, panel, "Test",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    nom = fieldNom.getText();
                    prenom = fieldPrenom.getText();
                    pseudo = fieldPseudo.getText();

                    try {
                        jeu.ajouterJoueur(new Joueur(nom,prenom,pseudo));
                    } catch (ArmurerieException e1) {
                        e1.printStackTrace();
                    }


                } else {
                    System.out.println("Cancelled");
                }

                // -- Create player
                System.out.println("Un joueur a été crée ! Liste des joueurs : ");
                System.out.println(jeu.getJoueurs());
                updateListPlayers(jeu);
            }
        });
        itemSupressionJoueur.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // -- Pseudo du joueur
                JTextField fieldJoueurPseudo = new JTextField();
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Veuillez indiquer le pseudo du joueur à supprimer :"));
                panel.add(fieldJoueurPseudo);

                // -- Display dialog
                int result = JOptionPane.showConfirmDialog(null, panel, "Test",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    System.out.println(fieldJoueurPseudo.getText());
                } else {
                    System.out.println("Cancelled");
                }

                // -- Delete player
                String pseudo;
                pseudo = fieldJoueurPseudo.getText();
                jeu.supprimerJoueurAvecPseudo(pseudo);
                updateListPlayers(jeu);
            }
        });
        itemChoixJoueur.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // -- Pseudo du joueur
                String pseudo;
                JTextField fieldJoueurPseudo = new JTextField();
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Veuillez indiquer le pseudo du joueur à choisir :"));
                panel.add(fieldJoueurPseudo);

                // -- Display dialog
                int result = JOptionPane.showConfirmDialog(null, panel, "Test",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    System.out.println(fieldJoueurPseudo.getText());
                } else {
                    System.out.println("Cancelled");
                }

                // -- Choix du joueur
                pseudo = fieldJoueurPseudo.getText();
                System.out.println("Veuillez entrer le pseudo du joueur choisi: ");
                jeu.choisirJoueurAvecPseudo(pseudo);
                updateListPlayers(jeu);
            }

        });
        itemCreationVaisseau.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String type = "";
                int choix;
                Vaisseau vaisseaucree = null;

                System.out.println("Veuillez entrer le type de vaisseau : ");

                // -- Type de vaisseau
                JTextField fieldVaisseauType = new JTextField();
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Veuillez indiquer le type du vaisseau choisi :"));
                panel.add(fieldVaisseauType);

                // -- Display dialog
                int result = JOptionPane.showConfirmDialog(null, panel, "Test",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // -- Create vaisseau
                if (result == JOptionPane.OK_OPTION) {
                    type = fieldVaisseauType.getText();

                    switch(type){
                        case "Slavel" :
                            try {
                                vaisseaucree = new Slavel();
                            } catch (ArmurerieException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case "Tardis" :
                            try {
                                vaisseaucree = new Tardis();
                            } catch (ArmurerieException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case "Alkesh" :
                            try {
                                vaisseaucree = new Alkesh();
                            } catch (ArmurerieException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case "Assault" :
                            try {
                                vaisseaucree = new Assault();
                            } catch (ArmurerieException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case "Dart" :
                            try {
                                vaisseaucree = new Dart();
                            } catch (ArmurerieException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        default :
                            try {
                                vaisseaucree = new ViperMKII();
                            } catch (ArmurerieException e1) {
                                e1.printStackTrace();
                            }
                            break;

                    }
                    if(vaisseaucree != null){
                        jeu.ajouterEnnemi(vaisseaucree);
                    }
                } else {
                    System.out.println("Cancelled");
                }

                System.out.println(jeu.getEnnemis());
            }
        });
        itemSupressionVaisseau.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int vaisseauId = 0;
                Vaisseau vaisseaucree = null;

                // -- Id du vaisseau a supprimer
                JTextField fieldVaisseauType = new JTextField();
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Veuillez indiquer l'id du vaisseau a supprimer :"));
                panel.add(fieldVaisseauType);

                // -- Display dialog
                int result = JOptionPane.showConfirmDialog(null, panel, "Test",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    vaisseauId = Integer.parseInt(fieldVaisseauType.getText());
                }

                // -- Supression vaisseau
                System.out.println("Veuillez entrer l'id du vaisseau a supprimer : ");
                System.out.println("Id entré : "+vaisseauId);
                jeu.supprimerVaisseauEnnemi(vaisseauId-1);
                System.out.println(jeu.getEnnemis());

            }
        });
        itemCreationArmurerie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom;
                int degatmin;
                int degatmax;
                double tempsRechargement;
                String type;
                models.Type typechoisi = models.Type.Direct;
                JPanel panel = new JPanel(new GridLayout(0, 1));

                // -- Fields
                JTextField fieldArmeNom = new JTextField();
                JTextField fieldDegatsMin = new JTextField();
                JTextField fieldDegatsMax = new JTextField();
                JTextField fieldTempsRechargement = new JTextField();
                JTextField fieldType = new JTextField();

                panel.add(new JLabel("Veuillez saisir un nom :"));
                panel.add(fieldArmeNom);

                panel.add(new JLabel("Veuillez saisir les dégats min :"));
                panel.add(fieldDegatsMin);

                panel.add(new JLabel("Veuillez saisir les dégats max :"));
                panel.add(fieldDegatsMax);

                panel.add(new JLabel("Veuillez saisir un temps de rechargement :"));
                panel.add(fieldTempsRechargement);

                panel.add(new JLabel("Veuillez saisir un type :"));
                panel.add(fieldType);


                // -- Display dialog
                int result = JOptionPane.showConfirmDialog(null, panel, "Test",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    nom = fieldArmeNom.getText();
                    degatmin = Integer.parseInt(fieldDegatsMin.getText());
                    degatmax = Integer.parseInt(fieldDegatsMax.getText());
                    tempsRechargement = Double.parseDouble(fieldTempsRechargement.getText());
                    type = fieldType.getText();
                    switch (type){
                        case "Direct" :
                            typechoisi = models.Type.Direct;
                            break;
                        case "Explosif" :
                            typechoisi = models.Type.Explosif;
                            break;
                        case "Guide" :
                            typechoisi = models.Type.Guide;
                    }
                    Armurerie.getInstance().addUneArme(new Arme(nom,degatmin,degatmax,tempsRechargement,typechoisi));
                }

                // -- Create arme

                System.out.println("Une arme ! Liste des arme : ");
                System.out.println(Armurerie.getInstance().getLesArmes());
            }
        });
        itemSupressionArmurerie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int armeId = 0;

                // -- Id de l'arme
                JTextField fieldArmeId = new JTextField();
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Veuillez indiquer l'id de l'arme a supprimer"));
                panel.add(fieldArmeId);
                int result = JOptionPane.showConfirmDialog(null, panel, "Test",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    armeId = Integer.parseInt(fieldArmeId.getText());
                }

                // -- Supression d'une arme
                System.out.println("Veuillez entrer l'id de l'arme a supprimer : ");
                System.out.println("Id entré : "+armeId);

                Armurerie.getInstance().supprimerArme(armeId-1);
                System.out.println(Armurerie.getInstance().getLesArmes());

            }
        });

        // -- LISTENERS DU TP4
        itemImportArmes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JPanel panel = new JPanel(new GridLayout(0, 1));

                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "fichiers txt", "txt");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(panel);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    System.out.println("You chose to open this file: " +
                            chooser.getSelectedFile().getName());
                }

                // -- Initialisation de la lsite noire
                ArrayList<String> listeNoire = new ArrayList<String>();
                listeNoire.add("bonjour");
                ArmeImporteur ai = new ArmeImporteur(8,listeNoire, chooser.getSelectedFile().getPath());
                ai.generateArmes();

            }
        });


        shoppingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JPanel panel = new JPanel(new GridLayout(0, 1));

                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "fichiers txt", "txt");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(panel);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    System.out.println("You chose to open this file: " +
                            chooser.getSelectedFile().getName());
                }

                // -- Initialisation de la lsite noire
                ArrayList<String> listeNoire = new ArrayList<String>();
                listeNoire.add("bonjour");
                ArmeImporteur ai = new ArmeImporteur(8,listeNoire, chooser.getSelectedFile().getPath());
                ai.generateArmes();

            }
        });


        apparenceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // -- Pseudo du joueur
                String pseudo = "";
                Joueur joueurChoisi;
                JTextField fieldJoueurPseudo = new JTextField();
                JPanel panel = new JPanel(new GridLayout(0, 1));

                // -- Choix du joueur
                if( playerList.getSelectedValue() != null){
                    pseudo = playerList.getSelectedValue().toString();

                    joueurChoisi = jeu.getJoueurAvecPseudo(pseudo);


                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "fichiers jpg", "jpg");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(panel);
                    if(returnVal == JFileChooser.APPROVE_OPTION) {
                        System.out.println("You chose to open this file: " +
                                chooser.getSelectedFile().getName());
                    }
                    joueurChoisi.getMonVaisseau().setImage(chooser.getSelectedFile().getPath());
                    Vaisseau vaisseauJoueur = joueurChoisi.getMonVaisseau();
                    String image = vaisseauJoueur.getImage();

                    System.out.println("la val selectionnée es t: "+playerList.getSelectedValue());

                    imagePanel.setImage(image);
                    imagePanel.updateUI();
                }else{
                    JPanel panelDialog = new JPanel();
                    JLabel label = new JLabel("Veuillez selectionner un joueur");
                    panelDialog.add(label);
                    int result = JOptionPane.showConfirmDialog(null, panelDialog, "Test",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                }





            }
        });


        // -- creation de joueur
        createPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nom;
                String prenom;
                String pseudo;

                // -- Fields
                JTextField fieldNom = new JTextField();
                JTextField fieldPrenom = new JTextField();
                JTextField fieldPseudo = new JTextField();

                JPanel panel = new JPanel(new GridLayout(0, 1));

                panel.add(new JLabel("Veuillez indiquer un nom :"));
                panel.add(fieldNom);

                panel.add(new JLabel("Veuillez indiquer un prenom :"));
                panel.add(fieldPrenom);

                panel.add(new JLabel("Veuillez indiquer un pseudo :"));
                panel.add(fieldPseudo);


                // -- Display dialog
                int result = JOptionPane.showConfirmDialog(null, panel, "Test",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    nom = fieldNom.getText();
                    prenom = fieldPrenom.getText();
                    pseudo = fieldPseudo.getText();

                    try {
                        jeu.ajouterJoueur(new Joueur(nom,prenom,pseudo));

                    } catch (ArmurerieException e1) {
                        e1.printStackTrace();
                    }


                } else {
                    System.out.println("Cancelled");
                }

                // -- Create player
                System.out.println("Un joueur a été crée ! Liste des joueurs : ");
                System.out.println(jeu.getJoueurs());

                // -- Update de la liste
                updateListPlayers(jeu);

            }
        });

        // -- delete player
        deletePlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // -- Pseudo du joueur
                JTextField fieldJoueurPseudo = new JTextField();
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Veuillez indiquer le pseudo du joueur à supprimer :"));
                panel.add(fieldJoueurPseudo);

                // -- Display dialog
                int result = JOptionPane.showConfirmDialog(null, panel, "Test",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    System.out.println(fieldJoueurPseudo.getText());
                } else {
                    System.out.println("Cancelled");
                }

                // -- Delete player
                String pseudo;
                pseudo = fieldJoueurPseudo.getText();
                jeu.supprimerJoueurAvecPseudo(pseudo);
                // -- Update de la liste
                updateListPlayers(jeu);
            }
        });

        playerList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if( playerList.getSelectedValue() != null){
                    Joueur joueurSelectionne = null;
                    joueurSelectionne = jeu.getJoueurAvecPseudo(playerList.getSelectedValue().toString());
                    Vaisseau vaisseauJoueur = joueurSelectionne.getMonVaisseau();
                    String image = vaisseauJoueur.getImage();

                    System.out.println("la val selectionnée es t: "+playerList.getSelectedValue());

                    imagePanel.setImage(image);
                    imagePanel.updateUI();

                    descriptionPanel.updateText(joueurSelectionne);
                    descriptionPanel.updateUI();

                }
            }
        });

        playerList.updateUI();
        playerList.updateUI();



        this.setJMenuBar(menuBar);
        this.setContentPane(this.rootPanel);
        this.setSize(800,800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }



    public void updateListPlayers(SpaceInvaders jeu){
        // -- Update de la liste
        DefaultListModel model = new DefaultListModel();
        playerList.setModel(model);
        for(Joueur j : jeu.getJoueurs()){
            model.addElement(j.getPseudo());
        }
        playerList.updateUI();
    }
}
