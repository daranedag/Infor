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
package treegross.base;

public interface PlugInTaperFunction
{ 

/** Loads the function coefficients for right species */
    public void loadParameter(int funNo);
/** reduces the bark */
    public double barkreduce(int funNo,double D);
/** finds the diameter at a given height 
 *  Berechnet Schaftradius bei gegebener stemheight h
 *  0 bzw. bei Rindabindex=1 wird die doppelte Rindenstärke vom Durchmesser abgezogen, als Eingangsvariable für die Berechnung der doppelten Rindenstärke wird der abgerundete Schaftdurchmesser benötigt                                                                                                 
 *  0 bzw. bei Forstindex=1 wird der Schaftdurchmesser mit Rinde auf ganze cm abgerundet
 */
    public double getDiameterEst(int funNo,double dbh,double height,double h,int barkindex,int sortindex);
/** finds the height for a given diameter */
    public double getLengthEst(int funNo, double dbh,double height,double stemd);         
    //sucht Höhe zu einem vorgegebenen Durchmesser: stemd,
/** bei sortindex=1 wird der Schaftdurchmesser mit Rinde auf ganze cm abgerundet,bei barkindex=1 wird die doppelte Rindenstärke vom Durchmesser abgezogen, als Eingangsvariable für die Berechnung der doppelten Rindenstärke wird der abgerundete Schaftdurchmesser benötigt.*/    
    public double getCumVolume(int funNo, double dbh,double height,double h,int barkindex,int sortindex);                   
/** number of tree individual functions */
    public int getNumberOfFunctions();
/** returns the function name for number */
    public String getFunctionName(int funNo);
/** returns the function number for a given species Nds */
    public int getFunctionNumber(int speciesCode);
}
