/**
 * Created by MZK on 19/01/2017.
 */

import exceptions.ArmurerieException;
import models.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Scanner;

import javax.swing.*;

public class IhmSpaceInvaders {



    public static void createAndShowGUI(SpaceInvaders jeu){
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("SPACE INVADER");
        JTextArea infoTextArea = new JTextArea();
        infoTextArea.setEditable(false);
        // -- Menu declaration
        JMenuBar menuBar;
        JMenu menuJoueurs;
        JMenu menuVaisseaux;
        JMenu menuArmurerie;

        JMenuItem itemCreationJoueur;
        JMenuItem itemSupressionJoueur;
        JMenuItem itemChoixJoueur;

        JMenuItem itemCreationVaisseau;
        JMenuItem itemSupressionVaisseau;

        JMenuItem itemCreationArmurerie;
        JMenuItem itemSupressionArmurerie;

        // -- Pannel
        JPanel panelInfo = new JPanel();
        JScrollPane scroll = new JScrollPane(infoTextArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        scroll.setPreferredSize (new Dimension(400,400));
        panelInfo.add(scroll);
        infoTextArea.setText( jeu.getInformations() );


        //Create the menu bar.
        menuBar = new JMenuBar();


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
                        infoTextArea.append( "\nLe joueur a bien été crée ! " );
                    } catch (ArmurerieException e1) {
                        e1.printStackTrace();
                    }


                } else {
                    System.out.println("Cancelled");
                }

                // -- Create player
                System.out.println("Un joueur a été crée ! Liste des joueurs : ");
                System.out.println(jeu.getJoueurs());
                infoTextArea.setText( jeu.getInformations() );
            }
        });
        menuJoueurs.add(itemCreationJoueur);

        // -- Supression du joueur
        itemSupressionJoueur = new JMenuItem("Supprimer un joueur ");
        itemSupressionJoueur.setMnemonic(KeyEvent.VK_B);
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
                infoTextArea.setText( jeu.supprimerJoueurAvecPseudo(pseudo) );
                System.out.println(jeu.getJoueurs());
                infoTextArea.setText( jeu.getInformations() );
            }
        });
        menuJoueurs.add(itemSupressionJoueur);

        // -- Choix du joueur
        itemChoixJoueur = new JMenuItem("Choix un joueur ");
        itemChoixJoueur.setMnemonic(KeyEvent.VK_B);
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
                infoTextArea.setText( "\nLe joueur "+pseudo+" a été choisi" );
                System.out.println(jeu.getJoueurs());
                infoTextArea.setText( jeu.getInformations() );
            }
        });
        menuJoueurs.add(itemChoixJoueur);
        menuJoueurs.add(itemCreationJoueur);

        // -- Add et actions vaisseaux
        itemCreationVaisseau = new JMenuItem("Créer un vaisseau ennemi",
                KeyEvent.VK_T);
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

                    infoTextArea.setText( "\nLe vaisseau a bien été crée" );
                } else {
                    System.out.println("Cancelled");
                }

                System.out.println(jeu.getEnnemis());
                infoTextArea.setText( jeu.getInformations() );
            }
        });

        itemSupressionVaisseau = new JMenuItem("Supprimer un vaisseau ennemi",
                KeyEvent.VK_T);
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
                infoTextArea.setText( "\nLe vaisseau a bien été supprimé" );
                infoTextArea.setText( jeu.getInformations() );
            }
        });

        menuVaisseaux.add(itemSupressionVaisseau);
        menuVaisseaux.add(itemCreationVaisseau);

        // -- add et actions armurerie

        itemCreationArmurerie = new JMenuItem("Créer une arme",
                KeyEvent.VK_T);
        itemCreationArmurerie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom;
                int degatmin;
                int degatmax;
                double tempsRechargement;
                String type;
                Type typechoisi = Type.Direct;
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
                            typechoisi = Type.Direct;
                            break;
                        case "Explosif" :
                            typechoisi = Type.Explosif;
                            break;
                        case "Guide" :
                            typechoisi = Type.Guide;
                    }
                    Armurerie.getInstance().addUneArme(new Arme(nom,degatmin,degatmax,tempsRechargement,typechoisi));
                }

                // -- Create arme
                infoTextArea.setText( "\nL'arme a bien été créee" );
                infoTextArea.setText( jeu.getInformations() );
                System.out.println("Une arme ! Liste des arme : ");
                System.out.println(Armurerie.getInstance().getLesArmes());
            }
        });

        itemSupressionArmurerie = new JMenuItem("Supprimer une arme de l'armurerie",
                KeyEvent.VK_T);
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
                infoTextArea.setText( "\nL'arme a bien été supprimée" );
                Armurerie.getInstance().supprimerArme(armeId-1);
                System.out.println(Armurerie.getInstance().getLesArmes());
                infoTextArea.setText( jeu.getInformations() );
            }
        });

        menuArmurerie.add(itemCreationArmurerie);
        menuArmurerie.add(itemSupressionArmurerie);

        frame.setJMenuBar(menuBar);


        frame.getContentPane().setLayout(new BorderLayout());
        label.setSize(20, 20);
        frame.getContentPane().add(BorderLayout.NORTH,panelInfo);

       // frame.setVisible(true);
    }
}
