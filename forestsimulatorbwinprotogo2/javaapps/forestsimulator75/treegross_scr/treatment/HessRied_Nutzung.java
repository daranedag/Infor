/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package treegross.treatment;

import treegross.base.Stand;

/**
 *
 * @author nagel
 */
public class HessRied_Nutzung {

    int nutzungstyp = 0;
    String vgz ="";
    double minHarvest = 40.0;
    double maxHarvest = 80.0;

    public int getNutzungstyp(Stand st, int zieltyp){
        // Hauptbaumart festlegen
        nutzungstyp =1; //Schirmschlag
        int art1 = 0;  
        double gmax =0.0;
        for (int i=0;i<st.nspecies;i++ )
            if (gmax < st.sp[i].gha) {
                art1 = st.sp[i].code;
                gmax = st.sp[i].gha;
        }
        int l1 = getLichtTypbyArt(art1);
        HessRied_BZT_Regel BZT = new HessRied_BZT_Regel();
        int art2 = BZT.getArt1byWET(zieltyp);
        int l2 = getLichtTypbyArt(art2);
        if (l1 == 0 && l2 == 2) nutzungstyp =0; //Zielstärke
        if (l1 == 2 && l2 == 2) nutzungstyp =0; //Zielstärke
// Verjüngungsgang festlegen
        if (l1 == 0 && l2 == 0) vgz = "0.6;0.2;0.0;";
        if (l1 == 0 && l2 == 2) vgz = "1.0;0.6;0.4;0.4;0.2;0.0;";
        if (l1 == 0 && l2 == 3) vgz = "";
        if (l1 == 1 && l2 == 0) vgz = "1.0;0.0;";
        if (l1 == 1 && l2 == 1) vgz = "1.0;0.6;0.6;0.0;";
        if (l1 == 1 && l2 == 2) vgz = "1.0;0.6;0.2;0.0;";
        if (l1 == 2 && l2 == 0) vgz = "0.4;0.0;";
        if (l1 == 2 && l2 == 1) vgz = "0.4;0.4;0.0;";
        if (l1 == 2 && l2 == 2) vgz = "";
        if (art1 == 711 && art2 == 711) vgz = "0.4;0.2;0.0;";
//
        return nutzungstyp;
    }

   public String getVerjuengungsgang(){
       return vgz;
   }
   public double getMinHarvest(){
       return minHarvest;
   }

   public double getMaxHarvest(){
       return maxHarvest;
   }


   public int getLichtTypbyArt(int art){
         int ltyp = 0;  // Lichtbaumart
// Halbschatten
         if (art == 321 || art == 331 || art == 352 || art == 353) ltyp = 1;
         if (art == 357 || art == 511 || art == 512 || art == 611) ltyp = 1;
// Schatten
         if (art == 211 || art == 221 || art == 341 || art == 521) ltyp = 2;
         if (art == 541 || art == 542 || art == 523 || art == 543) ltyp = 2;
         return ltyp;
   }
}
