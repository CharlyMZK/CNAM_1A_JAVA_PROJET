package models;

import java.util.ArrayList;

import exceptions.ArmurerieException;

public abstract class Vaisseau {
	protected int pointsStructureMax;
	protected int pointsBouclierMax;
	protected int pointsStructureActuel;
	protected int pointsBouclierActuel;
	protected boolean detruit;
	protected ArrayList<Arme> lesArmes;
	protected String image;

	/**
	 * Constructeur de classe
	 * @param pointsStructureMax integer
	 * @param pointsBouclierMax integer
	 */
	public Vaisseau(int pointsStructureMax, int pointsBouclierMax) {
		this.lesArmes = new ArrayList<>();
		this.pointsStructureMax = pointsStructureMax;
		this.pointsBouclierMax = pointsBouclierMax;
		this.pointsStructureActuel = pointsStructureMax;
		this.pointsBouclierActuel = pointsBouclierMax;
		this.detruit = false;
	}

	/**
	 * Attaque un autre vaisseau
	 * @param vaisseau attaqué
	 */
	public abstract void attaque(Vaisseau vaisseau);

	/**
	 * Recevoir des degats
	 * @param degats reçus
	 */
	public void recevoirDegats(int degats){
		if(degats > 0){System.out.println("[ Reçu } Ouch ! Mon vaisseau a pris "+degats+" degats");}
		// -- Si on a encore du bouclier, on prends dans le bouclier
		if(this.getPointsBouclierActuel() > 0){
			// -- Mais si on peut prendre dans le bouclier, mais que ça deviens négatif, on partage entre bouclier et structure
			if( ( this.pointsBouclierActuel - degats) < 0 ) {
				this.pointsStructureActuel = this.pointsStructureActuel + (this.pointsBouclierActuel - degats);
				this.pointsBouclierActuel = 0;
			}else{ // -- Sinon on prends tout dans bouclier
			 	pointsBouclierActuel = this.pointsBouclierActuel - degats;
			}
		}else{ // -- Sinon on prends dans structure
			this.pointsStructureActuel = this.pointsStructureActuel - degats;
		}
		// -- Si on a plus rien, on meurs
		if(this.pointsStructureActuel <= 0 && this.pointsBouclierActuel <= 0 ){
			System.out.println("[ Reçu ] "+this.getClass().getName()+" est détruit !!");
			this.detruit = true;
		}
	}

	/**
	 * Retourne les points de structure actuel
	 * @return points de structure
	 */
	public int getPointsStructureActuel() {
		return pointsStructureActuel;
	}

	/**
	 * Modifie les points de structure actuel
	 * @param pointsStructureActuel du vaisseau
	 */
	public void setPointsStructureActuel(int pointsStructureActuel) {
		this.pointsStructureActuel = pointsStructureActuel;
	}

	/**
	 * Retourne les points de bouclier actuels
	 * @return points de bouclier actuel
	 */
	public int getPointsBouclierActuel() {
		return pointsBouclierActuel;
	}

	/**
	 * Modifie les points de bouclier actuel
	 * @param pointsBouclierActuel du vaisseau
	 */
	public void setPointsBouclierActuel(int pointsBouclierActuel) {
		this.pointsBouclierActuel = pointsBouclierActuel;
	}

	/**
	 * Retourne le nombre de points de structure maximum
	 * @return pointsStructureMax
	 */
	public int getPointsStructureMax() {
		return pointsStructureMax;
	}
	
	/**
	 * Modifie le nombre de points de structure maximum
	 * @param pointsStructureMax integer
	 */
	public void setPointsStructureMax(int pointsStructureMax) {
		this.pointsStructureMax = pointsStructureMax;
	}
	
	/**
	 * Retourne le nombre de points de bouclier maximum
	 * @return pointsBouclierMax
	 */
	public int getPointsBouclierMax() {
		return pointsBouclierMax;
	}
	
	/**
	 * Modifie le nombre de points de bouclier maximum
	 * @param pointsBouclierMax integer
	 */
	public void setPointsBouclierMax(int pointsBouclierMax) {
		this.pointsBouclierMax = pointsBouclierMax;
	}
	
	/**
	 * Retourne si le vaisseau est détruit
	 * @return detruit boolean
	 */
	public boolean isDetruit() {
		return detruit;
	}
	
	/**
	 * Modifie la propriété detruit
	 * @param detruit boolean
	 */
	public void setDetruit(boolean detruit) {
		this.detruit = detruit;
	}
	
	/**
	 * Retourne la totalité des armes
	 * @return lesArmes
	 */
	public ArrayList<Arme> getLesArmes() {
		return lesArmes;
	}
	
	/**
	 * Modifie les armes avec un tableau d'armes
	 * @param lesArmes arraylist d'armes
	 */
	public void setLesArmes(ArrayList<Arme> lesArmes) {
		// -- On verifie si le tableau donné ne comporte pas plus de 3 armes
		if(lesArmes.size() <= 3 ){
			this.lesArmes = lesArmes;
		}else{
			System.out.println("Ce tableau contient plus que 3 armes ! ");
		}
	}
	
	/**
	 * Ajouter une arme
	 * @param arme ajoutée
	 * @throws ArmurerieException si l'arme n'existe pas
	 */
	public abstract void ajouterArme(Arme arme,Armurerie armurerie) throws ArmurerieException;
	
	/**
	 * Supprimer une arme
	 * @param arme retirée
	 */
	public void removeArme(Arme arme){
		// -- Si cette arme est effectivement dans la liste on l'enleve, sinon on affiche un message 
		if(lesArmes.contains(arme)){
			lesArmes.remove(arme);
		}else{
			System.out.println("Cette arme n'existe pas dans la liste");
		} 
	}
	
	/**
	 * Calcul des degats du vaisseau
	 * @return degats
	 */
	public int calculDegats(){
		int degats = 0;
		if(lesArmes.size() > 0){
			for(Arme arme : lesArmes){
				degats += arme.degatMoyen();
			}
			degats =  degats / lesArmes.size();
		}
		return degats;
	}
	
	/**
	 * Affiche toute les armes  
	 */
	public void afficherArmes(){
		System.out.println("Je possède : ");
		for(Arme arme : lesArmes){
			System.out.println(" - "+arme.getNom());
		}	
		System.out.println("Et j'inflige "+calculDegats()+" degats");
	} 
	
	/** 
	 * Retourne les informations des armes sous forme de chaine de caractères
	 */
	public String getInformationArmes(){
		String informationArmes; //-- Chaine d'information

		// -- Debut construction de la chaine
		informationArmes =  "le vaisseau possède : \n";
		// -- Si on a des armes
		if(lesArmes.size() > 0){
			
			for(Arme arme : lesArmes){ 
				informationArmes += arme;
				// -- Si c'est pas la dernière arme, on met une ,
			}   
			 
		}else{ // -- Sinon, on a aucuhe arme 
			informationArmes += "aucune arme\n";
		}
		informationArmes += "Au total j'inflige "+calculDegats()+" degats";
		// -- Fin construction 
		 
		return informationArmes;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * On affiche toute les informations possible pour un vaisseau
	 */
	@Override
	public String toString(){ 
		return "Vaisseau : \nStructure max : "+pointsStructureMax+"\nBouclier max : "+pointsBouclierMax+"\nStructure actuel : "+pointsStructureActuel+"\nBouclier actuel : "+pointsBouclierActuel+" Le vaisseau est est détruit : "+detruit+"\nArmes : \n"+getInformationArmes()+"\n";
	}

}
