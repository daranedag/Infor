/* http://www.nw-fva.de
   Version 30 Juli 2010

   (c) 2010 Juergen Nagel, Northwest German Forest Research Station,
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
public class NutrientBalanceLine {
    
    int year=0;
    double cutVolume = 0.0;
    boolean sortiments = true;
    boolean firewood = true;
    boolean restwood = true;
    double sortimentsBM = 0.0;
    double firewoodBM =0.0;
    double restwoodBM =0.0;
    
    
    /** Creates a new instance of LoggingSortiment */
    public NutrientBalanceLine() {
    }

    
}
