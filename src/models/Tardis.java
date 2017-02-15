package models;

import exceptions.ArmurerieException;
import interfaces.IAptitude;
import java.util.ArrayList;

public class Tardis extends Vaisseau implements IAptitude {

    /**
     * Constructeur de classe
     */
    public Tardis() throws ArmurerieException {
        super(1, 0 );
    }

    /**
     * Attaque un vaisseau
     * @param vaisseau attaqué
     */
    @Override
    public void attaque(Vaisseau vaisseau) {
        // -- Je rajoute le code d'attaque au cas ou si il peut attaquer
        int degats = 0;
        // -- Pour chaque arme, on essaie de tirer avec, si elle a son compteur a 0 elle tirera, le dart peut tirer avec plusieurs armes a la fois
        for (Arme arme : this.getLesArmes()){
            degats += arme.tirer();
        }
        vaisseau.recevoirDegats(degats);
    }

    /**
     * Ajouter une arme
     * @param arme ajoutée
     * @throws ArmurerieException si l'arme n'existe pas
     */
    public void ajouterArme(Arme arme,Armurerie armurerie) throws ArmurerieException{
        // -- Je rajoute le code d'ajout d'une arme au cas ou si il est possiible d'en avoir
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
        return "Vaisseau tardis : \nStructure max : "+pointsStructureMax+"\nBouclier max : "+pointsBouclierMax+"\nStructure actuel : "+pointsStructureActuel+"\nBouclier actuel : "+pointsBouclierActuel+"\nLe vaisseau est est détruit : "+detruit+"\nArmes : \n"+getInformationArmes()+"\n";
    }

    /**
     * Utilise le pouvoir spécial du vaisseau
     * @param list d'ennemis
     * @return des dégats
     */
    public int utilise(ArrayList<Vaisseau> list){
        System.out.println("Pfiou mon pouvoir magique est activé !");
        // -- Le pouvoir de tardis inverse deux vaisseaux
        int pos1 = (int)(Math.random() * (list.size()-1));
        int pos2 = (int)(Math.random() * (list.size()-1));
        Vaisseau vaisseau1 = list.get(pos1);
        Vaisseau vaisseau2 = list.get(pos2);
        System.out.println("je swap la position "+pos1+" et "+pos2+" et donc les vaisseaux "+vaisseau1.getClass().getName()+" et "+vaisseau2.getClass().getName());
        list.set(pos1,vaisseau2);
        list.set(pos2,vaisseau1);
        // -- Fin inversion
        return 0;
    }
}
