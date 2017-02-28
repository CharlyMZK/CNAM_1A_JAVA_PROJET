package models;

import exceptions.ArmurerieException;

public class ViperMKII extends Vaisseau{

    /**
     * Constructeur de classe
     */
    public ViperMKII() throws ArmurerieException {
        super(3, 0 );
        this.ajouterArme(Armurerie.getInstance().getUneArme(3), Armurerie.getInstance());
    }

    /**
     * Attaque un vaisseau
     * @param vaisseau attaqué
     */
    @Override
    public void attaque(Vaisseau vaisseau) {
        int degats;
        // -- L'arme utilisée sera la meilleure arme
        Arme armeUtilisée = null;
        // -- On réduit le tour de 1 de chaque arme car on attaque
        // -- Parcours des armes
        for (Arme arme : this.getLesArmes()){
            // -- On descends le compteur de chaque arme car on tire
            if(arme.getCompteurTour() > 1 ){arme.setCompteurTour(arme.getCompteurTour()-1);}
            // -- Recuperation de l'arme utilisée
            if(armeUtilisée == null && arme.getCompteurTour() <= 1){
                armeUtilisée = arme;
            }else{
                if(armeUtilisée != null && arme.degatMoyen() > armeUtilisée.degatMoyen()){
                    armeUtilisée = arme;
                }
            }

        }
        // -- Après avoir récupéré l'arme on tire
        degats = armeUtilisée.tirer();
        System.out.println("[ Attaque ] J'inflige "+degats+" degats");
        vaisseau.recevoirDegats(degats);
    }

    /**
     * Ajouter une arme
     * @param arme ajoutée
     * @throws ArmurerieException si l'arme n'existe pas
     */
    public void ajouterArme(Arme arme,Armurerie armurerie) throws ArmurerieException{
        // -- Si l'arme est dans l'armurerie on l'ajoute, sinon on throw une exception
        if(armurerie.armePossedee(arme)){
            // -- Si il y a encore de la place on l'ajoute, sinon on affiche un message
            if(this.getLesArmes().size() > 2){
                System.out.println("Vous en pouvez pas ajouter plus de trois armes");
            }else{
                this.getLesArmes().add(arme);
            }
        }else{
            throw new ArmurerieException("Je n'ai pas cette arme");
        }
    }

    /**
     * Override toString
     * @return Strign
     */
    public String toString(){
        return "Vaisseau viper : \nStructure max : "+pointsStructureMax+"\nBouclier max : "+pointsBouclierMax+"\nStructure actuel : "+pointsStructureActuel+"\nBouclier actuel : "+pointsBouclierActuel+"\nLe vaisseau est est détruit : "+detruit+"\nArmes : \n"+getInformationArmes()+"\n";
    }
}
