/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vis2006;

/**
 * Speichert einen Baum oder sonstigen Messpunkt einer Topcon-Datei.
 *
 * @param typ           Art des Elementes (Baum oder Standpunkt)
 * @param name          Bezeichnung des Elements (Baum- oder Standpunktnummer)
 * @param art           Baumart nach erweiterertem nieders�chsischem Schl�ssel
 * @param xwert         x-Koordinate des Elements [m]
 * @param ywert         y-Koordinate des Elements [m]
 * @param zwert         z-Koordinate des Elements (relative Gel�ndeh�he) [m]
 * @param spiegelHoehe  H�he des Messspiegels beim Einmessen des Baumes
 * @param winkel        Streichwinkel zu Nord [gon] (aus Sicht des Messger�ts)
 * @param entfernung    vertikale Entfernung des Messspiegels vom Messger�t [m]
 * @param zDifferenz    H�hendifferenz zwischen Messger�t und Messspiegel [m]
 *
 * @author sprauer
 */
public class StammvBaum {
    public String typ;
    public String name;
    public int art;
    public double xwert;
    public double ywert;
    public double zwert;
    public double spiegelHoehe;
    public double winkel;
    public double entfernung;
    public double zDifferenz;
    public String standpunkt;

    /**
     * Erstellt eine neue Instanz von StammvElement
     */
    public StammvBaum(){
        typ = "";
        name = "";
        art = 0;
        xwert = 0.0;
        ywert = 0.0;
        zwert = 0.0;
        spiegelHoehe =0.0;
        winkel = 0;
        entfernung = 0.0;
        zDifferenz = 0.0;
        standpunkt = "";
    }


    public void calculateXY(double xStandpunkt, double yStandpunkt){
       xwert = xStandpunkt + entfernung*Math.sin(winkel/400*2*Math.PI);
       ywert = yStandpunkt + entfernung*Math.cos(winkel/400*2*Math.PI);
    }

    public void calculateZ(double zStandpunkt, double linsenHoehe){
        zwert = zStandpunkt + linsenHoehe - spiegelHoehe + zDifferenz;
    }



}
