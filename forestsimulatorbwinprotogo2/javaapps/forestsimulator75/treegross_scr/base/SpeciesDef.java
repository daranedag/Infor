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

public class SpeciesDef implements Comparable, Cloneable{
    public int code;
    public int internalCode;
    public String shortName;
    public String longName;
    public String latinName;
    public int codeGroup;
    public int handledLikeCode;
    public int heightCurve=-1;
    public int taperFunction;
    public int crownType;
    public double criticalCrownClosure;
    public int maximumAge;
    public double targetDiameter;
    public double heightOfThinningStart;
    public int colorGreen, colorRed, colorBlue;
    public boolean defined=false;
    public String modelRegion="default";
    public String volumeFunctionXML="";
    public String stemVolumeFunctionXML="";
    public String uniformHeightCurveXML="";
    public String diameterDistributionXML="";    
    public String crownwidthXML="";    
    public String crownbaseXML="";    
    public String siteindexXML="";    
    public String diameterIncrementXML=""; 
    public double diameterIncrementError =0.0;
    public String heightIncrementXML="";    
    public double heightIncrementError =0.0;
    public String heightVariationXML="";
    public String siteindexHeightXML="";
    public String potentialHeightIncrementXML="";
    public String maximumDensityXML="";
    public String moderateThinning="";
    public String colorXML="100,200,300";
    public String decayXML="";
    public String competitionXML="";
    public String ingrowthXML="";
    public String taperFunctionXML="";
    public String coarseRootBiomass="";
    public String smallRootBiomass="";
    public String fineRootBiomass="";
    public String totalRootBiomass="";


    /** Creates a new instance of Class */
    public SpeciesDef(){}
    
    public SpeciesDef clone(){
        SpeciesDef clone = new SpeciesDef();
        clone.code= new Integer(this.code);
        clone.internalCode= new Integer(this.internalCode);
        clone.shortName= new String(this.shortName);
        clone.longName= new String(this.longName);
        clone.latinName= new String(this.latinName);
        clone.codeGroup= new Integer(this.codeGroup);
        clone.handledLikeCode= new Integer(this.handledLikeCode);
        clone.heightCurve= new Integer(this.heightCurve);
        clone.taperFunction= new Integer(this.taperFunction);
        clone.crownType= new Integer(this.crownType);
        clone.criticalCrownClosure= new Double(this.criticalCrownClosure);
        clone.maximumAge= new Integer(this.maximumAge);
        clone.targetDiameter= new Double(this.targetDiameter);
        clone.heightOfThinningStart= new Double(this.heightOfThinningStart);
        clone.colorGreen= new Integer(this.colorGreen);
        clone.colorRed= new Integer(this.colorRed);
        clone.colorBlue= new Integer(this.colorBlue);
        clone.defined= new Boolean(this.defined);
        clone.modelRegion= new String(this.modelRegion);
        clone.volumeFunctionXML= new String(this.volumeFunctionXML);
        clone.stemVolumeFunctionXML= new String(this.stemVolumeFunctionXML);
        clone.uniformHeightCurveXML= new String(this.uniformHeightCurveXML);
        clone.diameterDistributionXML= new String(this.diameterDistributionXML);
        clone.crownwidthXML= new String(this.crownwidthXML);
        clone.crownbaseXML= new String(this.crownbaseXML);
        clone.siteindexXML= new String(this.siteindexXML);
        clone.diameterIncrementXML= new String(this.diameterIncrementXML);
        clone.diameterIncrementError= new Double(this.diameterIncrementError);
        clone.heightIncrementXML= new String(this.heightIncrementXML);
        clone.heightIncrementError= new Double(this.heightIncrementError);
        clone.heightVariationXML= new String(this.heightVariationXML);
        clone.siteindexHeightXML= new String(this.siteindexHeightXML);
        clone.potentialHeightIncrementXML= new String(this.potentialHeightIncrementXML);
        clone.maximumDensityXML= new String(this.maximumDensityXML);
        clone.moderateThinning= new String(this.moderateThinning);
        clone.colorXML= new String(this.colorXML);
        clone.decayXML= new String(this.decayXML);
        clone.competitionXML= new String(this.competitionXML);
        clone.ingrowthXML= new String(this.ingrowthXML);
        clone.taperFunctionXML= new String(this.taperFunctionXML);
        
        clone.coarseRootBiomass= new String(this.coarseRootBiomass);
        clone.smallRootBiomass= new String(this.smallRootBiomass);
        clone.fineRootBiomass= new String(this.fineRootBiomass);
        clone.totalRootBiomass= new String(this.totalRootBiomass);
        return clone;
    }    

    public int compareTo(Object o){
        if(code ==((SpeciesDef)o).code)
            return 1;
        else
            return 0;
    }   
    
    void setDefined(boolean defined){
        this.defined=defined;
    } 
}
