package models;

import exceptions.ArmurerieException;

public class Alkesh extends Vaisseau{

    /**
     * Constructeur de classe
     */
    public Alkesh() throws ArmurerieException {
        super(3, 5 );
        this.ajouterArme(Armurerie.getInstance().getUneArme(2), Armurerie.getInstance());
    }

    /**
     * Attaque un vaisseau
     * @param vaisseau attaqué
     */
    @Override
    public void attaque(Vaisseau vaisseau) {
        int degats = 0;
        // -- Pour chaque arme, on essaie de tirer avec, si elle a son compteur a 0 elle tirera, l'alkesh peut tirer avec plusieurs armes a la fois
        for (Arme arme : this.getLesArmes()){
            degats += arme.tirer();
        }
        System.out.println("[ Attaque ] J'inflige "+degats+" degats");
        vaisseau.recevoirDegats(degats);
    }

    /**
     * Ajouter une arme
     * @param arme ajoutée
     * @throws ArmurerieException si l'arme n'existe pas
     */
    public void ajouterArme(Arme arme,Armurerie armurerie) throws ArmurerieException{
        // -- Ici pour éviter de le lancer a chaque attaque, pas besoin de redondance, si on mets l'arme dans un alkesh
        // -- Elle reçois une upgrade, trop cool !
        if(arme.getType() == Type.Guide){
            arme.setTempsRechargement(1);
        }

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
        return "Vaisseau Alkesh : \nStructure max : "+pointsStructureMax+"\nBouclier max : "+pointsBouclierMax+"\nStructure actuel : "+pointsStructureActuel+"\nBouclier actuel : "+pointsBouclierActuel+"\nLe vaisseau est est détruit : "+detruit+"\nArmes : \n"+getInformationArmes()+"\n";
    }
}
