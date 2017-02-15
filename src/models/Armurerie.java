package models;

import utilitaire.DegatMoyComparateur;

import java.lang.reflect.Array;
import java.util.ArrayList;

public final class Armurerie {
	private ArrayList<Arme> lesArmes;
	
	/** Instance unique pré-initialisée */
	private static Armurerie instance = null;
 
	/**
	 * Constructeur de classe
	 */
	private Armurerie() {
		lesArmes = new ArrayList<>();
		init();
	} 
	 
	/** Point d'acces pour l'instance unique du singleton */
	public static Armurerie getInstance()
	{	
		if(Armurerie.instance == null){
			Armurerie.instance = new Armurerie();
		}
		return instance;
	}
	
	/**
	 * Retourne les armes
	 * @return lesArmes
	 */
	public ArrayList<Arme> getLesArmes() {
		return lesArmes;
	}
	
	/**
	 * Modifie les armes
	 * @param lesArmes a ajouter
	 */
	public void setLesArmes(ArrayList<Arme> lesArmes) {
		this.lesArmes = lesArmes;
	} 
	
	/**
	 * Retourne une arme en fonction d'un indice dans le tableau 
	 * @param id de l'arme
	 * @return arme
	 */
	public Arme getUneArme(int id){
		return lesArmes.get(id);
	}
	
	/**
	 * Affiche les armes
	 */
	public void afficherLesArmes(){
		System.out.println("[Armurerie] Dans l'armurerie il y a :");
		for(Arme arme : lesArmes){
            System.out.println(arme);
        }
	}
	
	/**
	 * Verifie si l'arme est possedée par l'armurerie
	 * @param arme a verifier
	 * @return check
	 */
	public boolean armePossedee(Arme arme){
		return lesArmes.contains(arme);
	}

	/**
	 * Ajoute une arme a la liste
	 * @param arme
	 */
	public void addUneArme(Arme arme){
		this.lesArmes.add(arme);
	}

    /**
     * Supprime une arme de la liste
     * @param id
     */
	public void supprimerArme(int id ){
        lesArmes.remove(id);
    }

    public void statistiques(){
        System.out.println("STATISTIQUES DES ARMES  : ");

        trierListeArmesParDegatsMoyen();

        System.out.println("Les moins fortes : ");
        for(int i = 0 ; i <= 5 ; i ++ ){
            System.out.println(this.lesArmes.get(i));
        }

        System.out.println("Les plus fortes : ");
        for(int i = lesArmes.size()-5 ; i < lesArmes.size() ; i ++ ){
            System.out.println(this.lesArmes.get(i));
        }
    }
    /**
     * Tri les armes par degat moyen
     */
	public void trierListeArmesParDegatsMoyen(){
	    this.lesArmes.sort(new DegatMoyComparateur());
        this.afficherLesArmes();
    }
	/**
	 * Initialise l'armurerie 
	 */
	private void init(){
		System.out.println("\n[Armurerie] init lancé");
		Arme laser = new Arme("Laser",2,3,1,Type.Direct);
		Arme lucile = new Arme("Hammer",1,8,1.5,Type.Explosif);
		Arme missile = new Arme("Torpille",3,3,2,Type.Guide);
		Arme mitrailleuse  = new Arme("mitrailleuse",2,3,1,Type.Direct);
		Arme emg = new Arme("EMG",1,7,1.5,Type.Explosif);
		Arme missileViper = new Arme("MissileViper",4,100,4,Type.Guide);
		lesArmes.add(laser);
		lesArmes.add(lucile);
		lesArmes.add(missile);
		lesArmes.add(mitrailleuse);
		lesArmes.add(emg);
		lesArmes.add(missileViper);
	}
} 
