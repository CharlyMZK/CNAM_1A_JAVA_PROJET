package utilitaire;

import models.Arme;

import java.util.Comparator;

/**
 * Created by MZK on 20/01/2017.
 */
public class DegatMinComparateur implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        if(o1 instanceof Arme && o2 instanceof Arme){
            if(((Arme) o1).getDegatMinimum() > ((Arme) o2).getDegatMinimum()){
                return 1;
            }else if(((Arme) o1).getDegatMinimum() == ((Arme) o2).getDegatMinimum()){
                return 0;
            }else{
                return -1;
            }
        }
        return 0;
    }
}
