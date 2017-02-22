import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import exceptions.ArmurerieException;
import ihm.SpaceInvadersFrame;
import interfaces.IAptitude;
import models.*;

public class SpaceInvaders {
	private ArrayList<Joueur> joueurs;
	private ArrayList<Vaisseau> ennemis;

	/**
	 * Class constructor
	 */
	private SpaceInvaders() throws ArmurerieException {
		init();
	}
	/**
	 * Main function
	 * @param args string array
	 */
	public static void main(String[] args) throws ArmurerieException {
		new SpaceInvadersFrame();

		// -- Initialisation basique
		System.out.println("\n-------- INITIALISATION DU JEU ------------");

		SpaceInvaders jeu = new SpaceInvaders();
		//demoTP1(jeu);

		// -- Partie 2
		System.out.println("\n================================== [ RECAP AVANT LE DEBUT DU JEU ] =====================================\n");
		// -- Affichage des ennemis
		jeu.afficherEnnemis();
		// -- Affichage du joueur
		jeu.afficherLesJoueurs();
		// -- Que le jeu commence !
		int tour = 0;
		// -- Tant qu'on est pas mort ou qu'il reste des ennemis on joue
		/*while(jeu.getEnnemis().size() > 0 && !jeu.getJoueurs().get(0).getMonVaisseau().isDetruit()){
			tour ++;
			System.out.println("\n================================== [ DEBUT DU TOUR "+tour+" ] ===================================");
			jouerTour(jeu);
			System.out.println("===================================================================================================");
		}*/
		// -- Resultat du jeu
		/*if(jeu.getEnnemis().size() == 0 && !jeu.getJoueurs().get(0).getMonVaisseau().isDetruit() ){
			System.out.println("\n ============================== ### [ WOW YOU WIN SUCH POWER MUCH SKILLS SO NICE ] ### ========================================");
		}else{
			System.out.println("\n ============================== ### [ WOW YOU LOOSE SO BAD TRY AGAIN NOOB  ] ### ========================================");
		}*/
		Scanner sc = new Scanner(System.in);
		String fichierEntree;

		// -- Si il y a des args on prend les args sinon on demande a l'utilisateur
		if(args.length > 0){
			fichierEntree = args[0];
			System.out.println("args : "+args[0]);
		}else{
			System.out.println("Bonjour, veuillez entrer le fichier de config des armes merci");
			fichierEntree = sc.nextLine();
		}

		// -- Initialisation de la lsite noire
		ArrayList<String> listeNoire = new ArrayList<String>();
		listeNoire.add("bonjour");

		ArmeImporteur ai = new ArmeImporteur(8,listeNoire,fichierEntree);
		ai.generateArmes();
		System.out.println("======================== AFFICHER LES ARMES ===================================");
		Armurerie.getInstance().afficherLesArmes();
		System.out.println("======================== TRI DES ARMES ===================================");
		Armurerie.getInstance().trierListeArmesParDegatsMoyen();

		/*IhmSpaceInvaders ihm = new IhmSpaceInvaders();
		ihm.createAndShowGUI(jeu);

		Armurerie.getInstance().statistiques();

		JMenuFrame gameFrame = new JMenuFrame(jeu);*/

	}
	/**
	 * Initialise le jeu en créeant des joueurs
	 */
	private void init() throws ArmurerieException {
		System.out.println("[SpaceInvaders] init lancé ");
		// -- Initialisation des joueurs
		joueurs = new ArrayList<>();
		Joueur luke = new Joueur("Skywalker","luke","bgdelespace", new ViperMKII());
		joueurs.add(luke);
		// -- Initialisation des ennemis
		ennemis = new ArrayList<>();
		Assault assault = new Assault();
		Dart dart = new Dart();
		Alkesh alk = new Alkesh();
		Slavel slavel = new Slavel();
		Tardis tardis = new Tardis();
		ennemis.add(assault);
		ennemis.add(dart);
		ennemis.add(alk);
		//ennemis.add(slavel);
		ennemis.add(tardis);

	}

	/**
	 * Joue un tour du jeu
	 * @param jeu en cours
	 */
	private static void  jouerTour(SpaceInvaders jeu){
		// -- Début du jeu

		// -- Initialisation
		ArrayList<Vaisseau> ennemis = jeu.getEnnemis();
		ArrayList<Vaisseau> vaisseauxJoueurs = jeu.getVaisseauxJoueurs(jeu.getJoueurs());
		int cibleEnnemie = 0 + (int)(Math.random() * ennemis.size());
		int iteration = 0;
		double rand = Math.random();
		boolean aAttaque = false;

		// -- Regeneration
		jeu.regenererBouclier();


		// -- Utilisation des pouvoirs
		System.out.println("\n ============================== [ UTILISATION DES POUVOIRS ] ================================= \n");

		// -- Pouvoirs des ennemis
		for(Vaisseau v : ennemis){
			if(v instanceof IAptitude){
				System.out.println("\n =========== [ POUVOIR DE "+v.getClass().getName()+" ] =====");
				for(Vaisseau vaisseauJoueur : vaisseauxJoueurs){
					vaisseauJoueur.recevoirDegats(  ((IAptitude) v).utilise(ennemis)  );
				}

			}
		}

		// -- Pouvoirs des joueurs
		for(Vaisseau v : vaisseauxJoueurs){
			if(v instanceof IAptitude){
				System.out.println("\n =========== [ POUVOIR DE "+v.getClass().getName()+" ] =====");
				for(Vaisseau ennemi : ennemis){
					ennemi.recevoirDegats(  ((IAptitude) v).utilise(ennemis)  );
				}

			}
		}

		// -- Phase d'attaque
		for(Vaisseau v : ennemis){
			System.out.println("\n================================== [ PHASE D'ATTAQUE ENNEMIE, TOUR DE "+v.getClass().getName()+" ] =====================================");
			iteration++;
			rand = Math.random() * ennemis.size();
			// -- Si on a de la chance, le joueur tire
			if(!aAttaque && iteration > rand){
				System.out.println("\n================================== [ PHASE D'ATTAQUE DU JOUEUR ] =====================================");
				System.out.println("[ Tour ] Le vaisseau du joueur attaque la cible "+cibleEnnemie);
				for(Vaisseau vaisseauJoueur : vaisseauxJoueurs) {
					// -- Le joueur attaque si il n'est pas détruit
					if(!vaisseauJoueur.isDetruit()){ vaisseauJoueur.attaque(ennemis.get(cibleEnnemie)); }
				}
				aAttaque = true;
				System.out.println("========================================================================================================");
			}
			// -- L'enemi attaque
			for(Vaisseau vaisseauJoueur : vaisseauxJoueurs){
				// -- Si il n'est pas détruit
				if(!v.isDetruit()){ v.attaque(vaisseauJoueur); }
			}
			// -- Etat de la fin du tour
			System.out.println("\n ---------------- [ RAPPORT DE COMBAT, VAISSEAU DU JOUEUR : ] ----------------- ");
			for(Vaisseau vaisseauJoueur : vaisseauxJoueurs){
				System.out.println("\nVaisseau du joueur : "+vaisseauJoueur.getPointsBouclierActuel()+" de boucliers et "+vaisseauJoueur.getPointsStructureActuel()+" de points de structure\n");
			}
			System.out.println("========================================================================================================================================");
		}

		System.out.println("\n================================================================ [ FIN DU TOUR ] ========================================================================================\n");

		System.out.println("\n================================================================ [ AFFICHAGE RESUME ] ========================================================================================\n");

		// -- Puis on sort tout ce qui a été tué
		jeu.collecterDebris();

		// -- Affichage de l'etat des vaisseaux
		for(Vaisseau v : ennemis){
			System.out.println("\n ================================ [ ENNEMI ] =============================================");
			System.out.println(v);
			System.out.println("=========================================================================================");
		}

	}

	/**
	 * Regenère le bouclier de 2 chaque tour
	 */
	public void regenererBouclier(){
		System.out.println("\n================================== [ REGENERATION DES BOUCLIERS ] =====================================");
		// -- Regeneration des boucliers ennemis
		for(Vaisseau v : ennemis){
			if(v.getPointsBouclierMax() > v.getPointsBouclierActuel()){
				if( ( v.getPointsBouclierActuel() + 2 ) <= v.getPointsBouclierMax()){
					System.out.println("\n"+v.getClass().getName()+" a "+v.getPointsBouclierActuel()+" / "+v.getPointsBouclierMax()+" , il gagne donc 2 pdb");
					v.setPointsBouclierActuel(v.getPointsBouclierActuel() + 2);
				}
			}
		}
		// -- Regeneration des boucliers alliés
		for(Vaisseau v : this.getVaisseauxJoueurs(joueurs)){
			if(v.getPointsBouclierMax() > v.getPointsBouclierActuel()){
				if( ( v.getPointsBouclierActuel() + 2 ) <= v.getPointsBouclierMax()){
					System.out.println("\n"+v.getClass().getName()+" a "+v.getPointsBouclierActuel()+" / "+v.getPointsBouclierMax()+" , il gagne donc 2 pdb");
					v.setPointsBouclierActuel(v.getPointsBouclierActuel() + 2);
				}
			}
		}

	}

	/**
	 * On enlève tout les vaisseaux morts
	 */
	public void collecterDebris(){
		// -- On utilise l'iterator pour éviter de throw une exception
		// -- Nettoyage des ennemis
		for (Iterator<Vaisseau> iterateur = ennemis.iterator(); iterateur.hasNext(); ) {
			Vaisseau vaisseau = iterateur.next();
			if (vaisseau.isDetruit()) {
				iterateur.remove();
			}
		}
		// -- Nettoyage des joueurs
		for (Iterator<Vaisseau> iterateur = this.getVaisseauxJoueurs(joueurs).iterator(); iterateur.hasNext(); ) {
			Vaisseau vaisseau = iterateur.next();
			if (vaisseau.isDetruit()) {
				iterateur.remove();
			}
		}
	}
	/**
	 * Get le vaisseau de chaque joueur
	 * @param lesJoueurs du jeu
	 * @return liste des vaisseaux
	 */
	public ArrayList<Vaisseau> getVaisseauxJoueurs(ArrayList<Joueur> lesJoueurs){
		ArrayList<Vaisseau> lesVaisseaux = new ArrayList<>();
		for(Joueur j : lesJoueurs){
			lesVaisseaux.add(j.getMonVaisseau());
		}
		return lesVaisseaux;
	}

	/**
	 * Lance la démo de la première partie du tp
	 * @param jeu en cours
	 * @throws ArmurerieException si l'arme n'est pas dans l'armurerie
	 */
	private static void demoTP1(SpaceInvaders jeu) throws ArmurerieException{
		jeu.afficherLesJoueurs();
		Armurerie.getInstance().afficherLesArmes();
		int choixArme;
		int size =  Armurerie.getInstance().getLesArmes().size() - 1;
		System.out.println("\n--------------------- AJOUTS D'ARMES AUX VAISSEAUX ---------------------- \n");

		for(Joueur joueur : jeu.joueurs){
			System.out.println("["+joueur.getPseudo()+"] : ");
			choixArme =  0 + (int)(Math.random() * size);
			System.out.println("Je choisis l'arme : "+choixArme);
			try {
				joueur.getMonVaisseau().ajouterArme(Armurerie.getInstance().getUneArme(choixArme),  Armurerie.getInstance());
			} catch (ArmurerieException e) {
				e.printStackTrace();
			}
			System.out.println("["+joueur.getPseudo()+"] : ");
			choixArme =  0 + (int)(Math.random() * size);
			System.out.println("Et l'arme : "+choixArme);
			try {
				joueur.getMonVaisseau().ajouterArme(Armurerie.getInstance().getUneArme(choixArme),  Armurerie.getInstance());
			} catch (ArmurerieException e) {
				e.printStackTrace();
			}
			System.out.println("\n");
		}
		System.out.println("\n Affichage des joueurs \n");
		jeu.afficherLesJoueurs();

		Arme uneArme = new Arme("Bombe",1000,1000,0,Type.Direct);
		Joueur unJoueur = new Joueur("wesh","alors","tkt", new ViperMKII());
		Vaisseau unVaisseau = new ViperMKII();
		unJoueur.setMonVaisseau(unVaisseau);

		try {
			unVaisseau.ajouterArme(Armurerie.getInstance().getUneArme(0), Armurerie.getInstance());
		} catch (ArmurerieException e) {
			e.printStackTrace();
		}

		try {
			unVaisseau.ajouterArme(uneArme, Armurerie.getInstance());
		} catch (ArmurerieException e) {
			e.printStackTrace();
		}

		System.out.println(uneArme);
		System.out.println(unJoueur);
		System.out.println(unVaisseau);

	}

	/**
	 * Affiche les ennemis
	 */
	private void afficherEnnemis(){
		for(Vaisseau v : ennemis){
			System.out.println("================================ [ ENNEMI ] =============================================");
			System.out.println(v);
			System.out.println("=========================================================================================");
		}
	}

	/**
	 * Affiche les joueurs
	 */
	private void afficherLesJoueurs(){
		for(Joueur joueur : joueurs){
			System.out.println("========================== [ JOUEUR "+joueur.getPseudo()+" ] =======================================");
			System.out.println(joueur.getMonVaisseau());
			System.out.println("====================================================================================================");
		}
	}

	/**
	 * Retourne la liste des joueurs
	 * @return joueurs
	 */
	public ArrayList<Joueur> getJoueurs() {
		return joueurs;
	}

	/**
	 * Modifie la liste des joueurs
	 * @param joueurs list
	 */
	public void setJoueurs(ArrayList<Joueur> joueurs) {
		this.joueurs = joueurs;
	}

	/**
	 * Retourne la liste des ennemis
	 * @return ennemis
	 */
	public ArrayList<Vaisseau> getEnnemis() {
		return ennemis;
	}

	/**
	 * Modifie la liste des ennemis
	 * @param ennemis list
	 */
	public void setEnnemis(ArrayList<Vaisseau> ennemis) {
		this.ennemis = ennemis;
	}

	/**
	 * Ajoute un joueur au jeu
	 * @param joueur
	 */
	public void ajouterJoueur(Joueur joueur){
		this.joueurs.add(joueur);
	}

	/**
	 * Ajoute un ennemi au jeu
	 * @param ennemi
	 */
	public void ajouterEnnemi(Vaisseau ennemi){
		this.ennemis.add(ennemi);
	}
	/**
	 * Supprime un joueur avec son pseudo
	 * @param pseudo
	 */
	public String supprimerJoueurAvecPseudo(String pseudo){
		Joueur saveJoueur = null;
		String response;
		for(Joueur joueur : joueurs){
			System.out.println("pseudo : "+joueur.getPseudo());
			if(joueur.getPseudo().equals(pseudo)){
				saveJoueur = joueur;
			}
		}
		if(saveJoueur != null){
			response = "Le joueur "+pseudo+" a été supprimé ! Liste des joueurs : ";
			System.out.println("Le joueur "+pseudo+" a été supprimé ! Liste des joueurs : ");
			joueurs.remove(saveJoueur);
		}else{
			response = "Le joueur "+pseudo+" n'a pas été trouvé malheureusement";
			System.out.println("Le joueur "+pseudo+" n'a pas été trouvé malheureusement");
		}
		return response;
	}

	/**
	 * Supprimer un vaisseau ennemi
	 * @param id
	 */
	public void supprimerVaisseauEnnemi(int id){
		this.ennemis.remove(id);
	}

	/**
	 *
	 * @param pseudo
	 */
	public void choisirJoueurAvecPseudo(String pseudo){
		Joueur saveJoueur = null;
		for(Joueur joueur : joueurs){
			System.out.println("pseudo : "+joueur.getPseudo());
			if(joueur.getPseudo().equals(pseudo)){
				saveJoueur = joueur;
			}
		}
		if(saveJoueur != null){
			joueurs.remove(saveJoueur);
			joueurs.add(0,saveJoueur);
			System.out.println("Le joueur "+pseudo+" a été choisi ! Liste des joueurs : ");
		}else{
			System.out.println("Le joueur "+pseudo+" n'a pas été trouvé malheureusement");
		}
	}

    public String getInformations(){
		String informations = "";
		for(Joueur joueur : joueurs){
			informations += "\n= [ JOUEUR "+joueur.getPseudo()+" ] =\n";
			informations += joueur.getMonVaisseau();
			informations += "\n====================================\n";
		}
		for(Vaisseau v : ennemis){
			informations += "\n= [ ENNEMI ] =\n";
			informations += v;
			informations += "\n=====================================\n";
		}
		for(Arme arme : Armurerie.getInstance().getLesArmes()){
			informations += "\n= [ ARME ] =\n";
			informations += arme;
			informations += "\n=====================================\n";
		}
		return informations;
	}


	public Joueur getJoueurAvecPseudo(String pseudo){
		Joueur saveJoueur = null;
		for(Joueur joueur : joueurs){
			System.out.println("pseudo : "+joueur.getPseudo());
			if(joueur.getPseudo().equals(pseudo)){
				saveJoueur = joueur;
			}
		}
		if(saveJoueur != null){
			return saveJoueur;
		}else{
			System.out.println("Le joueur "+pseudo+" n'a pas été trouvé malheureusement");
		}
		return null;
	}
}
