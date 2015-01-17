/*
*  (c) 2010 Juergen Nagel, Northwest German Research Station,
*      GrÃ¤tzelstr.2, 37079 GÃ¶ttingen, Germany
*      E-Mail: Juergen.Nagel@nw-fva.de
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU General Public License
*  as published by the Free Software Foundation.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
 */

/**
 * EklToAbsHAlter100 converts the relative yield class to the absolute Height at Age 100
 * The linear equations are fitted by the yield table data
 *
 * @author nagel
 */
package vis2006;

import treegross.base.Species;

public class EklToAbsHAlter100 {

/**  Regression Parameter a (slope) */
double a =-9;
/**  Regression Parameter b (constant) */
double b =0.0;

private void setParameter(Species species){
    a=0.0;b=-90;
    int code = species.code;
    for(int i = 0; i < 2; i++){
        if (a != 0.0 && b != -90) break;
        if (code<113) { a=-3.27;b=30.4;}
        if (code==113) { a=-3.3;b=35.6;}
        if (code==211) { a=-3.9;b=36.35;}
        if (code==511) { a=-3.85;b=38.89;}
        if (code==611) { a=-5.0;b=50;}
        if (code==711) { a=-3.9;b=32.96;}
        if (code==811) { a=-4.4;b=39.2;}
        if (code==321) { a=-5.05;b=39.13;}
        if (code==342) { a=-4;b=38.0;}
        if (a == 0.0 && b == -90) code = species.spDef.handledLikeCode;
    }
    
}
/** Returns the height at age 100 for a given relative yield class and species code
 *
 * @param code : Species code of yield table (Lower Saxony)
 * @param ekl : relative yield class
 * @return height of yield table at age 100 [m]; if no information for species
 * is available then the return is -9
 */
public double getAbsHAge100 (Species species, double ekl) {
    setParameter(species);
    double h = a*ekl + b;
    return h;
}
/** Returns the relative yield class for a given height at age 100 and species code
 *
 * @param code : Species code of yield table (Lower Saxony)
 * @param height : height of yield table at age 100 [m]
 * @return relative yield class; if no information for species
 * is available then the return is -9
 */
public double getEkl (Species species, double height) {
    setParameter(species);
    double ekl = -9;
    if (b != 0.0) ekl = (height-b)/a;
    return ekl;
}


}
