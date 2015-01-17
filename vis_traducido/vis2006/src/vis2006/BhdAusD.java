/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vis2006;

/**
 *
 * @author sprauer
 */
public class BhdAusD {




    public BhdAusD() {
    }

    public double getBhd(double d, int mh, int art){
        double bhd = 0.0;
        if(mh == 13) bhd = d;
        else if(d < 0.0001) bhd = d;
        else if(art < 500) bhd = 10.0*( Math.pow((d/10.0), 0.99098) * Math.pow((mh/10.0), 0.096449));
        else if(art == 611) bhd = 10.0*( Math.pow((d/10.0), 0.98841)* Math.pow((mh/10.), 0.12338));
        else if (art > 499) bhd =  10.0*( Math.pow((d/10.0), 0.98511)* Math.pow((mh/10.0), 0.14025));

        return bhd;
    }



}
