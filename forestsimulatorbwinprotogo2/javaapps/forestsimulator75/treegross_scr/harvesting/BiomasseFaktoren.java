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

/**
 *
 * @author nagel
 */
public class BiomasseFaktoren {
    
    /** Creates a new instance of BiomasseFaktoren */
    public BiomasseFaktoren() {
    }

    public double getFactor(int code){
 // Nach Knigge et al.     
        double fac = 0.64 ; // Eiche
        if (code > 200 && code < 500) fac = 0.66;
        if (code > 300 && code < 400) fac = 0.64;
        if (code > 400 && code < 500) fac = 0.37;
        if (code > 500 && code < 1000) fac = 0.43;
        if (code > 600 && code < 700) fac = 0.47;
        if (code > 711 && code < 800) fac = 0.49;
        if (code > 811 && code < 900) fac = 0.55;
        
        return fac;
    }

    public double getFactorCa(int code){
 // Jacobsen S.11    
        double fac = 2.47 ; // Eiche
        if (code > 200 && code < 500) fac = 1.8;
        if (code > 400 && code < 500) fac = 1.2;
        if (code > 500 && code < 1000) fac = 1.41;
        if (code > 611 && code < 700) fac = 0.75;
        if (code > 711 && code < 800) fac = 1.08;
        if (code > 800 && code < 900) fac = 0.74;
        return fac;
    }
    public double getFactorK(int code){
 // Jacobsen S.11    
        double fac = 1.05 ; // Eiche
        if (code > 200 && code < 500) fac = 1.04;
        if (code > 400 && code < 500) fac = 0.75;
        if (code > 500 && code < 1000) fac = 0.77;
        if (code > 611 && code < 700) fac = 0.87;
        if (code > 711 && code < 800) fac = 0.65;
        if (code > 800 && code < 900) fac = 0.73;
        return fac;
    }
    public double getFactorMg(int code){
 // Jacobsen S.11    
        double fac = 0.18 ; // Eiche
        if (code > 200 && code < 500) fac = 0.26;
        if (code > 400 && code < 500) fac = 0.20;
        if (code > 500 && code < 1000) fac = 0.18;
        if (code > 611 && code < 700) fac = 0.12;
        if (code > 711 && code < 800) fac = 0.24;
        if (code > 800 && code < 900) fac = 0.26;
        return fac;
    }
        public double getFactorCaAst(int code){
 // Jacobsen S.12    
        double fac = 4.41 ; // Eiche
        if (code > 200 && code < 500) fac = 4.02;
        if (code > 400 && code < 500) fac = 4.6;
        if (code > 500 && code < 1000) fac = 3.33;
        if (code > 611 && code < 700) fac = 4.22;
        if (code > 711 && code < 800) fac = 2.7;
        if (code > 800 && code < 900) fac = 2.49;
        return fac;
    }
    public double getFactorKAst(int code){
 // Jacobsen S.12    
        double fac = 2.00 ; // Eiche
        if (code > 200 && code < 500) fac = 1.5;
        if (code > 400 && code < 500) fac = 2.0;
        if (code > 500 && code < 1000) fac = 2.39;
        if (code > 611 && code < 700) fac = 1.65;
        if (code > 711 && code < 800) fac = 1.67;
        if (code > 800 && code < 900) fac = 2.71;
        return fac;
    }
    public double getFactorMgAst(int code){
 // Jacobsen S.12    
        double fac = 0.44 ; // Eiche
        if (code > 200 && code < 500) fac = 0.36;
        if (code > 400 && code < 500) fac = 0.50;
        if (code > 500 && code < 1000) fac = 0.53;
        if (code > 611 && code < 700) fac = 0.41;
        if (code > 711 && code < 800) fac = 0.43;
        if (code > 800 && code < 900) fac = 0.66;
        return fac;
    }
        public double getFactorCaBlatt(int code){
 // Jacobsen S.13    
        double fac = 11.43 ; // Eiche
        if (code > 200 && code < 500) fac = 8.88;
        if (code > 400 && code < 500) fac = 9.6;
        if (code > 500 && code < 1000) fac = 6.03;
        if (code > 611 && code < 700) fac = 7.41;
        if (code > 711 && code < 800) fac = 4.08;
        if (code > 800 && code < 900) fac = 3.8;
        return fac;
    }
    public double getFactorKBlatt(int code){
 // Jacobsen S.13    
        double fac = 7.38 ; // Eiche
        if (code > 200 && code < 500) fac = 8.66;
        if (code > 400 && code < 500) fac = 16.00;
        if (code > 500 && code < 1000) fac = 5.70;
        if (code > 611 && code < 700) fac = 5.98;
        if (code > 711 && code < 800) fac = 5.03;
        if (code > 800 && code < 900) fac = 11.0;
        return fac;
    }
    public double getFactorMgBlatt(int code){
 // Jacobsen S.13    
        double fac = 2.27 ; // Eiche
        if (code > 200 && code < 500) fac = 1.25;
        if (code > 400 && code < 500) fac = 2.7;
        if (code > 500 && code < 1000) fac = 0.79;
        if (code > 611 && code < 700) fac = 1.28;
        if (code > 711 && code < 800) fac = 0.87;
        if (code > 800 && code < 900) fac = 1.6;
        return fac;
    }

    
    
}
