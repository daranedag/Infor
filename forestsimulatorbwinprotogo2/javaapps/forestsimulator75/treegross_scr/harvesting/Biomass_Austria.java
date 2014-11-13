/* http://www.nw-fva.de
   Version 07-11-2008

   (c) 2002 Juergen Nagel, Northwest German Forest Research Station, 
       Grätzelstr.2, 37079 Göttingen, Germany
       E-Mail: Juergen.Nagel@nw-fva.de
 
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT  WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
 */
package treegross.harvesting;
import treegross.base.*;

/**
 *
 * @author J. Nagel
 *
 * Quelle 1: Eckmüllner, O. (2006) Allometric relations to estimate needle and branch mass
 * of Norway spruce and Scots Pine on Austria. Austrian Journal of Forest Science, 
 * 123. Jahrgang, Heft 1/2, S.7-16
 *
 * Quelle 2: Gschwantner, T.; Schadauer, K. (2006) Biomass branch functions for broadleaved tree
 * species in Austria. Austrian Journal of Forest Science, 123. Jahrgang, Heft 1/2, S.17-34
 */
public class Biomass_Austria {
    
    double p0,p1,p2,p3;
    int type;
    
    /** Creates a new instance of Biomass_Austria */
    public Biomass_Austria() {
    }
// Coefficients for needle biomass ND Table 3
    private void loadCoeffientsND(int code){
        //Oak Quelle 2
        if (code < 200) { p0 = 0.0; p1=0.0; p2=0.0; p3= 0.0; type=1;} 
        // beech Quelle 2
        if (code > 199 && code < 500) { p0 = 0.0; p1=0.0; p2=0.0; p3=0.0; type=1;}
        // spruce Quelle 1
        if (code > 499) { p0 = -2.5487; p1=2.4961; p2=-0.6365; p3=-0.3385; type=2;}
        // pine Quelle 1
        if (code > 699 && code < 800) { p0 = -0.8577; p1=1.9417; p2=-0.6182; p3= 0.0; type=2;}
    }    
// Coefficients for branch biomass BR Table 3
    private void loadCoeffientsBR(int code){
        //Oak Quelle 2
        if (code < 200) { p0 = -7.14617; p1=4.0895; p2=0.28758; p3= -0.98244; type=1; } 
        // beech Quelle 2
        if (code > 199 && code < 500) { p0 = -9.31816; p1=3.7634; p2=0.59888; p3= 0.15215; type=1;}
        // spruce Quelle 1
        if (code > 499) { p0 = -3.627; p1=2.8018; p2=-0.7826; p3= -0.3408; type=2; }
        // pine Quelle 1
        if (code > 699 && code < 800) { p0 =-1.1656; p1=2.1163; p2=-1.0197; p3= -0.8051; type=2;}
    }    
/** Biomass of leaves or needles (kg) */
    public double getLeafBM(Tree t){
        double result =0.0;
        loadCoeffientsND(t.code);
        if (type==1) result=0.0;
        if (type==2) {
            result=Math.exp(p0+p1*Math.log(t.d)+p2*Math.log(t.h)+p3*Math.log(1.0-((t.h-t.cb)/t.h)) );
            result=result - getBranchBM(t);
        }
        return result;
    }
/** Biomass of leaves or needles (kg) */
    public double getBranchBM(Tree t){
        double result =0.0;
        loadCoeffientsBR(t.code);
        if (type==1) result=Math.exp(p0+p1*Math.log(t.d)+p2*Math.log((t.h-t.cb)/t.h)+p3*Math.log(t.h) );
        if (type==2) result=Math.exp(p0+p1*Math.log(t.d)+p2*Math.log(t.h)+p3*Math.log(1.0-((t.h-t.cb)/t.h)) );
        return result;
    }
    
}
