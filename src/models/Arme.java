package models;


import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

public class Arme {
	private String nom;
	private int degatMinimum;
	private int degatMaximum;
	private double tempsRechargement;
	private double compteurTour;
	private Type type;

	/**
	 * Constructeur de classe
	 * @param nom de l'arme
	 * @param degatMinimum de l'arme
	 * @param degatMaximum de l'arme
	 * @param tempsRechargement de l'arme
	 * @param type de l'arme
	 */
	public Arme(String nom, int degatMinimum, int degatMaximum, double tempsRechargement, Type type) {
		this.nom = nom;
		this.degatMinimum = degatMinimum;
		this.degatMaximum = degatMaximum;
		this.tempsRechargement = tempsRechargement;
		this.compteurTour = tempsRechargement;
		this.type = type;
	}

	/**
	 * Tire sur un vaisseau
	 * @return int degats
	 */
	public int tirer(){
		// -- Initialisation
		System.out.println("[ Tirer ] Je tire avec "+nom);
		int degats;
		int rand;
		// -- Un tour passz
		this.compteurTour -- ;
		System.out.println("[ Tirer ] Temps rechargement : "+this.tempsRechargement);
		System.out.println("[ Tirer ] Compteur tour : "+this.compteurTour);
		// -- Si le tour est a , on est pret a tirer
		if(this.compteurTour <= 0){
			// -- En fonction du type de l'arme, les degats seront modifiés
			degats =  this.degatsInfliges();
			System.out.println("[ Tirer ] PIEW PIEW PIEW");
			switch (this.type){
				case Direct:
					rand = (int)(Math.random() * 9);
					if(rand == 5){
						degats = 0;
					}
					break;
				case Explosif:
					rand = (int)(Math.random() * 3);
					tempsRechargement = tempsRechargement * 2;
					if(rand == 2){
						degats = 0;
					}else{
						degats = degats * 2;
					}
					break;
				case Guide:
					degats = this.getDegatMinimum();
					break;
			}
			// -- Fin du tir, on remet le compteur au temps de rechargement pusiqu'on viens de tirer
			System.out.println("[ Tirer ] Mon tir inflige "+degats);
			this.compteurTour = this.getTempsRechargement();
			return degats;
		}else{
			// -- L'arme est en chargement, on ne tire pas
			System.out.println("[ Tirer ] Pas de tir en vue");
			return 0;
		}
	}

	/**
	 * Retourne le temps de rechargement
	 * @return tempsRechargement
	 */
	public double getTempsRechargement() {
		return tempsRechargement;
	}

	/**
	 * Modifie le temps de rechargement de l'arme
	 * @param tempsRechargement de l'arme
	 */
	public void setTempsRechargement(double tempsRechargement) {
		this.tempsRechargement = tempsRechargement;
	}

	/**
	 * Retourne le compteur de tours
	 * @return compteurTour
	 */
	public double getCompteurTour() {
		return compteurTour;
	}

	/**
	 * Modifie le compteur de tours
	 * @param compteurTour de l'arme
	 */
	public void setCompteurTour(double compteurTour) {
		this.compteurTour = compteurTour;
	}

	/**
	 * Retourne le nom de l'arme
	 * @return nom
	 */
	public String getNom() {
		return nom;
	}
	
	/**
	 * Modifie le nom de l'arme
	 * @param nom de l'arme
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	/**
	 * Retourne les degats minimum infligés
	 * @return degatMinimum de l'arme
	 */
	public int getDegatMinimum() {
		return degatMinimum;
	}
	
	/**
	 *  Modifie les degats minimum infligés
	 * @param degatMinimum de l'arme
	 */
	public void setDegatMinimum(int degatMinimum) {
		this.degatMinimum = degatMinimum;
	}
	
	/**
	 * Retourne les degats maximum infligés
	 * @return degatMaximum de l'arme
	 */
	public int getDegatMaximum() {
		return degatMaximum;
	}
	
	/**
	 * Modifie les degats maximum infligés
	 * @param degatMaximum de l'arme
	 */
	public void setDegatMaximum(int degatMaximum) {
		this.degatMaximum = degatMaximum;
	}
	
	/**
	 * Retourne le type de l'arme
	 * @return type
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * Modifie le type de l'arme
	 * @param type de l'arme
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	/**
	 * Calcule et retourne les degats moyen de l'arme
	 * @return degats moyen
	 */
	public int degatMoyen(){
		int degat;
		// -- Si le type des guidé, les dégats sont constants sur le minimum
		if (this.getType() == Type.Guide)
			degat = degatMinimum;
		else
			degat = (degatMinimum + degatMaximum) / 2;
		return degat;
	}

	/**
	 * Degats inflgiés
	 * @return int degats
	 */
	public int degatsInfliges(){
		return this.getDegatMaximum()+(int)(Math.random() * this.getDegatMaximum());
	}

	/**
	 * On affiche toute les informations possible pour une arme
	 */
	@Override
	public String toString(){
		return "- "+nom+" degats : ["+degatMinimum+"~"+degatMaximum+" = "+degatMoyen()+" ] temps de rechargement : "+tempsRechargement+" compteur tour : "+compteurTour+"\n";
	}
}   

