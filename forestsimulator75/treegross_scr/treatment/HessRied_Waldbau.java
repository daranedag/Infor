/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package treegross.treatment;

/**
 *
 * @author nagel
 */
public class HessRied_Waldbau {


    boolean rueckegasse = false;
    double minThinningV = 30.0;
    double maxThinningV = 60.0;
    int zBaeumeHa = 0;
    double kritKronenschluss = 0.0;
    String thinning="";
    public void setHauptart(int art1, double h100){
        rueckegasse = false;
        if (art1 > 100 && art1 < 200){
             zBaeumeHa = 120;
             kritKronenschluss = 0.4;
             if (h100 > 12 && h100 <= 14) {
                 rueckegasse = true;
             }
        }
        if (art1 >= 200 && art1 < 300){
             zBaeumeHa = 80;
             kritKronenschluss = 0.3;
             if (h100 > 14 && h100 <= 16) {
                 rueckegasse = true;
             }
        }
        if (art1 >= 300 && art1 < 400){
             zBaeumeHa = 80;
             kritKronenschluss = 0.3;
             if (h100 > 10 && h100 <= 12) {
                 rueckegasse = true;
             }
        }
        if (art1 >= 400 && art1 < 500){
             zBaeumeHa = 80;
             kritKronenschluss = 0.4;
             if (h100 > 8 && h100 <= 10) {
                 rueckegasse = true;
             }
        }
        if (art1 >= 500 && art1 < 600){
             zBaeumeHa = 0;
             kritKronenschluss = 0.5;
             if (h100 > 10 && h100 <= 12) {
                 rueckegasse = true;
             }
        }
        if (art1 >= 600 && art1 < 700){
             zBaeumeHa = 120;
             kritKronenschluss = 0.4;
             if (h100 > 10 && h100 <= 12) {
                 rueckegasse = true;
             }
        }
        if (art1 >= 700 && art1 < 800){
             zBaeumeHa = 150;
             kritKronenschluss = 0.3;
             if (h100 > 10 && h100 <= 12) {
                 rueckegasse = true;
             }
        }
        if (art1 >= 800 && art1 < 900){
             zBaeumeHa = 100;
             kritKronenschluss = 0.4;
             if (h100 > 8 && h100 <= 10) {
                 rueckegasse = true;
             }
        }

    }

    public boolean getRueckegasse(){
        return rueckegasse;
    }
    public double getMinThinningV(){
        return minThinningV;
    }
    public double getMaxThinningV(){
        return maxThinningV;
    }
    public double getZBaeumeHa(){
        return zBaeumeHa;
    }
    public double getKritKronenschluss(){
        return kritKronenschluss;
    }
    public String getThinning(int art){
        if (art >= 100 && art < 200) thinning="14;0.7;20;20;0.8;28;28;0.91;100";
        if (art >= 200 && art < 300) thinning="16;0.49;24;24;0.68;30;30;0.8;100";
        if (art >= 300 && art < 400) thinning="12;0.55;19;19;0.6;28;28;0.65;100";
        if (art >= 400 && art < 500) thinning="10;0.55;18;18;0.61;26;26;0.64;100";
        if (art >= 500 && art < 600) thinning="12;0.62;22;22;0.75;28;28;0.82;100";
        if (art >= 600 && art < 700) thinning="12;0.54;26;26;0.6;32;32;0.67;100";
        if (art >= 700 && art < 800) thinning="12;0.55;22;22;0.71;26;26;0.86;100";
        if (art >= 800 && art < 900) thinning="10;0.7;24;24;0.8;28;28;0.85;100";
        if (art == 311) thinning="12;0.85;19;19;0.9;28;28;0.95;100";
        return thinning;
    }

}
