/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vis2006;

/**
 *
 * @author sprauer
 */
public class DauerBaum {
    String nr;
    int artnr;
    double vegJahrEKAuf1;
    double vegJahrEKAuf2;
    int auf1;
    int auf2;
    double d1;
    double d2;
    double vegJahrDauerAufs[] = new double[50];
    double ddauer[] = new double[50];
    int dauerAuf[] = new int[50];
    int nDauerAufs;
    //int extraAufs[] = new int[50];  // Dauerumfangmessband-Aufnahmen (Nummer der Aufnahme), die keine, unplausible
                                    //     oder zu verwerfende (Herbstaufnahmen) Durchmesserwerte haben
    //int nExtraAufs;
    //double vegJahrExtraAufs[] = new double[50];
    

    // Constructor
    public DauerBaum(String nrx, int artx){
        
        nr = nrx;
        artnr = artx;
        nDauerAufs = 0;
        //nExtraAufs = 0;
    }

    public void setEKAufnahme1(int jahrx, int monatx, double dx, int auf){

        AltersDezimale aDez = new AltersDezimale();
        vegJahrEKAuf1 =  jahrx - 1 + aDez.getAltersdezimale(monatx);
        d1 = dx;
        auf1 = auf;
    }

    public void setEKAufnahme2(int jahrx, int monatx, double dx, int auf){

        AltersDezimale aDez = new AltersDezimale();
        vegJahrEKAuf2 =  jahrx - 1 + aDez.getAltersdezimale(monatx);
        d2 = dx;
        auf2 = auf;

    }

    public void setDauerAuf(int jahr, int monat, int auf, double d){ 

        AltersDezimale aDez = new AltersDezimale();
        vegJahrDauerAufs[nDauerAufs] = jahr - 1 + aDez.getAltersdezimale(monat);
        ddauer[nDauerAufs] = d;
        dauerAuf[nDauerAufs] = auf;
        nDauerAufs ++;
    }

 //   public void setExtraAufnahme(int auf, int jahr, int monat){

        
 //       AltersDezimale aDez = new AltersDezimale();
 //      extraAufs[nExtraAufs] = auf;
 //       vegJahrExtraAufs[nExtraAufs] = jahr - 1 + aDez.getAltersdezimale(monat);
 //       nExtraAufs ++;
 //   }


}
