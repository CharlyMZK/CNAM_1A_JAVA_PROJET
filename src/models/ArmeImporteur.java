package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MZK on 20/01/2017.
 */
public class ArmeImporteur {
    public int sizeMinimum;
    public ArrayList<String> listeNoire;
    private Map<String,Integer> frequences;
    private String link;

    /**
     * Constructeur
     * @param sizeMinimum
     * @param listeNoire
     * @param link
     */
    public ArmeImporteur(int sizeMinimum, ArrayList<String> listeNoire, String link) {
        this.frequences = new HashMap<>();
        this.sizeMinimum = sizeMinimum;
        this.listeNoire = listeNoire;
        this.link = link;
    }

    /**
     * Lecture du fichier
     */
    public void lireFichier(){
        try(BufferedReader br = new BufferedReader(new FileReader(this.link))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                String[] lineChars = line.split(" ");
                String word;
                for(int j = 0; j < lineChars.length ; j++){
                    word = normaliser(lineChars[j]);
                    putInFrequences(word);
                }
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insertion dans frequences
     * @param word
     * @return resultat
     */
    public int putInFrequences(String word){
        if(word.length() > this.sizeMinimum){
            System.out.println("Le mot n'a pas pu être entré car il est trop long");
            return 0;
        }
        for(String motListeNoire : listeNoire){
            if(listeNoire.contains(word)){
                System.out.println("Le mot n'a pas pu être entré car il est dans la liste noire");
                return 0;
            }
        }
        if(frequences.containsKey(word) && frequences.get(word) > 0 ){
            frequences.put(word,frequences.get(word)+1);
        }else{
            frequences.putIfAbsent(word,1);
        }
        return 1;
    }

    /**
     * Genère les armes
     */
    public void generateArmes(){
        lireFichier();
        int rand = 1+(int)(Math.random() * Type.values().length);
        Type typeChoisi = null;
        switch (rand){
            case 1 :
                typeChoisi = Type.Direct;
                break;
            case 2 :
                typeChoisi = Type.Explosif;
                break;
            case 3 :
                typeChoisi = Type.Guide;
        }

        for (Map.Entry<String,Integer> e : frequences.entrySet()){
            Armurerie.getInstance().addUneArme(new Arme(e.getKey(),e.getValue(),e.getKey().length(),1, typeChoisi));
        }
    }

    /**
     * Normalise le string
     * @param word
     * @return string
     */
    public String normaliser(String word){
        return  word.replaceAll("\\W", "").toLowerCase();
    }

    /**
     * Get les frequences
     * @return map
     */
    public Map<String, Integer> getFrequences() {
        return frequences;
    }

    /**
     * Set les frequences
     * @param frequences
     */
    public void setFrequences(Map<String, Integer> frequences) {
        this.frequences = frequences;
    }

    /**
     * Get la size minimum d'un mot
     * @return size
     */
    public int getSizeMinimum() {
        return sizeMinimum;
    }

    /**
     * Set la size minimum d'un mot
     * @param sizeMinimum du mot
     */
    public void setSizeMinimum(int sizeMinimum) {
        this.sizeMinimum = sizeMinimum;
    }

    /**
     * Get la liste noire
     * @return liste noire
     */
    public ArrayList<String> getListeNoire() {
        return listeNoire;
    }

    /**
     * Set la liste norie
     * @param listeNoire de mots
     */
    public void setListeNoire(ArrayList<String> listeNoire) {
        this.listeNoire = listeNoire;
    }
}
