import models.Joueur;

import javax.swing.*;

/**
 * Created by MZK on 03/02/2017.
 */
public class JDescriptionPanel extends JPanel{
    private JLabel labelNom;
    private JLabel labelPrenom;
    private JLabel labelPseudo;
    private JLabel labelVaisseau;
    private JLabel nomField;
    private JLabel prenomField;
    private JLabel pseudoField;
    private JLabel vaisseauField;
    private JTable table1;


    public void updateText(Joueur joueur){
        labelNom.setText(joueur.getNom());
        labelPrenom.setText(joueur.getNom());
        labelPseudo.setText(joueur.getNom());
        labelVaisseau.setText(joueur.getNom());

        Object[][] data = {
                {"Cysboy", "28 ans", "1.80 m"},
                {"BZHHydde", "28 ans", "1.80 m"},
                {"IamBow", "24 ans", "1.90 m"},
                {"FunMan", "32 ans", "1.85 m"}
        };

        //Les titres des colonnes
        String  title[] = {"Pseudo", "Age", "Taille"};
        table1 = new JTable(data, title);

        table1.updateUI();
    }
}
