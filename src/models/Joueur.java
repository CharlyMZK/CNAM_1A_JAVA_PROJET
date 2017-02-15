package models;


import exceptions.ArmurerieException;

public class Joueur {
	private final String nom; 
	private final String prenom;
	private String pseudo;
	private Vaisseau monVaisseau;
	
	/**
	 * Constructeur de classe
	 * @param nom du joueur
	 * @param prenom du joueur
	 * @param pseudo du joueur
	 * @param vaisseau du joueur
	 */
	public Joueur(String nom, String prenom, String pseudo, Vaisseau vaisseau) throws ArmurerieException {
		this.nom = capitalizeName(nom);
		this.prenom = capitalizeName(prenom);
		this.pseudo = pseudo;
		this.monVaisseau = vaisseau;
	}

	/**
	 * Constructeur de classe
	 * @param nom du joueur
	 * @param prenom du joueur
	 * @param pseudo du joueur
	 */
	public Joueur(String nom, String prenom, String pseudo) throws ArmurerieException {
		this.nom = capitalizeName(nom);
		this.prenom = capitalizeName(prenom);
		this.pseudo = pseudo;
		this.monVaisseau = new ViperMKII();
	}

	/**
	 * Retourne le nom et le prénom du joueur
	 * @return String nom et prenom
	 */
	public String getInformations(){
		return "Nom : "+nom+"\nPrenom : "+prenom;
	}
	
	/**
	 * Retourne le pseudo du joueur
	 * @return pseudo
	 */
	public String getPseudo() {
		return pseudo; 
	}
	
	/**
	 * Modifie le pseudo d'un joueur
	 * @param pseudo a modifier
	 */
	public void setPseudo(String pseudo){
		this.pseudo = pseudo;
	} 
	
	/**
	 * Retourne le vaisseau du joueur
	 * @return un vaisseau
	 */
	public Vaisseau getMonVaisseau() {
		return monVaisseau;
	}
	
	/**
	 * Modifie le vaisseau du joueur
	 * @param monVaisseau Vaisseau a set
	 */
	public void setMonVaisseau(Vaisseau monVaisseau) {
		this.monVaisseau = monVaisseau;
	}
		
	/**
	 * Permet de mettre la premiere lettre d'un String en majuscule, puis les autres lettres en minuscule.
	 * Prends en compte les noms composé d'un -
	 * @param name string a mettre en majuscule
	 * @return String fullName
	 */
	public static String capitalizeName(String name) {
		String fullName = ""; // -- Chaine qui comporte le nom en entier
		int indice = 0; // -- Compteur qui permet de savoir si on ajoute un - dans les nom composés
		String names[] = name.split("-"); // -- On split par rapport aux - pour les noms composés
		
		// -- Pour chaque partie on met en majuscule la première lettre
		for (String n: names) {
			indice ++;
	    	fullName = fullName + n.substring(0, 1).toUpperCase() + n.toLowerCase().substring(1, n.length());
	    	if(names.length > 1 && indice == 1){
	    		fullName += "-";
	    	}
		}
		// -- Fin traitement

		
	    return fullName;
	}
	
	/**
	 * On affiche le pseudo, le nom ainsi que le prénom
	 */
	@Override
	public String toString(){
		return "Joueur : "+pseudo+""+"("+nom+" "+prenom+")";
	}
	
	/**
	 * Override d'equals : On veut comparer les pseudo des joueurs
	 */
	@Override
	public boolean equals(Object o) {
		boolean check = false;
		Joueur j = null;
		try{
			j = (Joueur) o; 
		}catch(Exception e){
			System.out.println("Cet objet n'est pas un joueur : "+e.getMessage());
		}
		if(this.pseudo == j.getPseudo()){
			check = true;
		}
		return check;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}
}