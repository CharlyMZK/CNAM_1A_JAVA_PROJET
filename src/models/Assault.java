package models;

import exceptions.ArmurerieException;
import interfaces.IAptitude;
import java.util.ArrayList;

public class Assault extends Vaisseau implements IAptitude {

    /**
     * Constructeur de classe
     */
    public Assault() throws ArmurerieException {
        super(15, 0 );
    }

    /**
     * Attaque un vaisseau
     * @param vaisseau attaqué
     */
    @Override
    public void attaque(Vaisseau vaisseau) {
        // -- Je rajoute le code d'attaque au cas ou si il peut attaquer
        int degats = 0;
        // -- Pour chaque arme, on essaie de tirer avec, si elle a son compteur a 0 elle tirera, l'assault peut tirer avec plusieurs armes a la fois
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
     * Utilise le pouvoir spécial du vaisseau
     * @param list d'ennemis
     * @return des dégats
     */
    public int utilise(ArrayList<Vaisseau> list){
        System.out.println("[ Aptitude ] Pfiou mon pouvoir magique est activé !");
        // -- Si le vaisseau est le premier de la liste ( On vérifie pas si il est vivant car les morts sont enlevé a chaque tour )
        if(list.get(0) == this){
            System.out.println("[ Aptitude ] BOOM !");
            this.setDetruit(true);
            return 10;
        }else{ // -- Sinon il fait rien
            System.out.println("[ Aptitude ] Rien a proximité, je n'explose pas");
            return 0;
        }
    }

    /**
     * Override toString
     * @return Strign
     */
    public String toString(){
        return "Vaisseau Assault : \nStructure max : "+pointsStructureMax+"\nBouclier max : "+pointsBouclierMax+"\nStructure actuel : "+pointsStructureActuel+"\nBouclier actuel : "+pointsBouclierActuel+" Le vaisseau est est détruit : "+detruit+"\nArmes : \n"+getInformationArmes()+"\n";
    }

}
