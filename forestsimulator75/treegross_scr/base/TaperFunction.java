/** TreeGrOSS : class taper function by Brink calculates volume and diameter
   Version 11-NOV-2004  */
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
import java.awt.*;
import java. io. Serializable;

 public class TaperFunction
{  double a[];                                    // array for parameter value 
   double b[];
   String modelRegion = "default";
   String info="";
   
public TaperFunction(String region){
      modelRegion=region;
  }    
   
// load Parameter values, spcode=species code according to Lower Saxony
public void loadParameter(int funNo)
     {  //System.out.println(" Laden der Parameter "+spcode+"  ");
        try {
            String modelPlugIn="treegross.base."+modelRegion;  
            PlugInTaperFunction tap = (PlugInTaperFunction)Class.forName(modelPlugIn).newInstance();  
            tap.loadParameter(funNo);
        }
        catch (Exception e){System.out.println(e);System.out.println("ERROR TaperFunction"); }

     }    

public double barkreduce(int funNo,double d)
{
  double bark=0.0;
        try {
            String modelPlugIn="treegross.base."+modelRegion;  
            PlugInTaperFunction tap = (PlugInTaperFunction)Class.forName(modelPlugIn).newInstance();  
            bark=tap.barkreduce(funNo, d);
        }
        catch (Exception e){System.out.println(e);System.out.println("ERROR TaperFunction"); }
  return bark;
} // end barkreduce

/** finds the diameter at a given height */
public double getDiameterEst(int funNo,double dbh,double height,double h,int barkindex,int sortindex)      //Berechnet Schaftradius bei gegebener stemheight h
                                                                                                  //bei Rindabindex=1 wird die doppelte Rindenstärke vom Durchmesser abgezogen, als Eingangsvariable für die Berechnung der doppelten Rindenstärke wird der abgerundete Schaftdurchmesser benötigt                                                                                                 
                                                                                                  //bei Forstindex=1 wird der Schaftdurchmesser mit Rinde auf ganze cm abgerundet
{ 
  double diameter=0.0;
        try {
            String modelPlugIn="treegross.base."+modelRegion;  
            PlugInTaperFunction tap = (PlugInTaperFunction)Class.forName(modelPlugIn).newInstance();  
            diameter = tap.getDiameterEst(funNo, dbh, height, h, barkindex, sortindex);
        }
        catch (Exception e){System.out.println(e);System.out.println("ERROR TaperFunction"); }
  return diameter;
}              


/** finds the height for a given diameter */

public double getLengthEst(int funNo, double dbh,double height,double stemd)         //sucht Höhe zu einem vorgegebenen Durchmesser: stemd, 
{
  double length=0.0;
        try {
            String modelPlugIn="treegross.base."+modelRegion;  
            PlugInTaperFunction tap = (PlugInTaperFunction)Class.forName(modelPlugIn).newInstance();  
            length=tap.getLengthEst(funNo, dbh, height, stemd);
        }
        catch (Exception e){System.out.println(e);System.out.println("ERROR TaperFunction"); }
  return length;
}


   /** calculates the taper functionvolume according to the modell of Pain and Boyer 1996 / modell of Riemer et al. 
    species code according to the code of Lower Saxony */

public double getCumVolume(int funNo, double dbh,double height,double h,int barkindex,int sortindex)                   //bei sortindex=1 wird der Schaftdurchmesser mit Rinde auf ganze cm abgerundet,bei barkindex=1 wird die doppelte Rindenstärke vom Durchmesser abgezogen, als Eingangsvariable für die Berechnung der doppelten Rindenstärke wird der abgerundete Schaftdurchmesser benötigt.
{
  double cumVolume=0.0;
        try {
            String modelPlugIn="treegross.base."+modelRegion;  
            PlugInTaperFunction tap = (PlugInTaperFunction)Class.forName(modelPlugIn).newInstance();  
            cumVolume=tap.getCumVolume(funNo, dbh, height, h, barkindex, sortindex);
        }
        catch (Exception e){System.out.println(e);System.out.println("ERROR TaperFunction"); }
  return cumVolume;
}

public int getNumberOfFunctions(){
     int nFunNr=0;
        try {
            String modelPlugIn="treegross.base."+modelRegion;  
            PlugInTaperFunction tap = (PlugInTaperFunction)Class.forName(modelPlugIn).newInstance();  
            nFunNr=tap.getNumberOfFunctions();
        }
        catch (Exception e){System.out.println(e);System.out.println("ERROR TaperFunction"); }
        return nFunNr;
   }

   public String getFunctionName(int funNo){
     String funName="not found";
        try {
            String modelPlugIn="treegross.base."+modelRegion;  
            PlugInTaperFunction tap = (PlugInTaperFunction)Class.forName(modelPlugIn).newInstance();  
            funName=tap.getFunctionName(funNo);
        }
        catch (Exception e){System.out.println(e);System.out.println("ERROR TaperFunction"); }
        return funName;
   }    
   public int getFunctionNumber(int speciesCode){
     int funNo=0;
        try {
            String modelPlugIn="treegross.base."+modelRegion;
            PlugInTaperFunction tap = (PlugInTaperFunction)Class.forName(modelPlugIn).newInstance();
            funNo=tap.getFunctionNumber(speciesCode);
        }
        catch (Exception e){System.out.println(e);System.out.println("ERROR TaperNumber"); }
        return funNo;
   }
  
//
//                         
}

