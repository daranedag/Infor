/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vis2006;

/**
 *
 * @author sprauer
 */
public class StammvStandpunkt {
    String name;
    double x = 0.0;
    double y = 0.0;
    double z = 0.0;
    double hLinse;


    /** Creates a new instance of Standpunkt */
    public StammvStandpunkt(String Name, double X, double Y, double Z, double HLinse) {
        name = Name;
        x = X;
        y = Y;
        z = Z;
        hLinse = HLinse;
    }

    public void koordinatenSetzen(double X, double Y, double Z){
        x = X;
        y = Y;
        z = Z;
    }

}
