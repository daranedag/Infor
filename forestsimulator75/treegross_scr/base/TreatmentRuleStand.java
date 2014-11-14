/*
 * TreatmentRuleStand.java
 *
 *  (c) 2002-2008 Juergen Nagel, Northwest German Forest Research Station 
 *      Grätzelstr.2, 37079 Göttingen, Germany
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
 *
 *  http://treegross.sourceforge.net
 */
package treegross.base;
/** 
 * Created on 15. Dezember 2004, 13:48
 *  @author	Henriette Duda
 *
 *
 * treatment rules regulating treatments
 * treatment rules that refer to the stand 
 * can be defined
 */
public class TreatmentRuleStand implements Cloneable{
    /** treatmentype code
     * 0=LÖWE-Concept of Lower Saxony
     * 1=PNV
     * 2=Net Yield
     * 3=Process
     */
    public int treatmentType;
    /** Max. Harvestvolume (fm per ha), taken out during one harvesting step (sum over all species)*/
    public double maxHarvestVolume;
    /** min. volume per ha taken out during harvesting  (fm per ha)*/    
    public double minHarvestVolume;
    /** max. harvest volume  (fm per ha) taken out during one treatment (sum over all species)
     * manThinningVolume - already harvested volume= actual thinning volume
     * thus -> condition for thinning is: the full ammount has not been tapped during harvesting
     */    
    public double maxThinningVolume;
    /** min. volume  (fm per ha) taken out during thinning in volume per ha*/      
    public double minThinningVolume;    
    /** min. volume  (fm per ha) taken out during one treatment
     * (sum over all species) sum of harvested and thinned volume per ha*/
    public double minOutVolume;
    /** max. volume  (fm per ha) taken out during one treatment
     * (sum over all species) sum of harvested and thinned volume per ha*/
    public double maxOutVolume;    
    /** actual stand type code*/
    public int standType;
    /** target stand type code*/
    public int targetType;
    /** number of wanted habitat trees per ha*/
    public double nHabitat;
    /** one tree of each species will be maintained (selected as crop tree)*/
    public boolean protectMinorities;
    /** defines periode between treatments in stand (years)*/
    public int treatmentStep;
    /** harvesting started so many years ago*/ 
    public int harvestingYears;
    /** defines in which periode harvesting must be complete*/ 
    public int maxHarvestingPeriode;  
    /** true-> harvest layer from below
     * false-> harvest layer from above
     * (relevant if type of harvest=1)  */
    public boolean harvestLayerFromBelow;
    /** select crop trees if not done */
    public boolean selectCropTrees;
    /** reselect crop trees automatically */
    public boolean reselectCropTrees;    
    /** select crop trees with no preference to certain species*/
    public boolean selectCropTreesOfAllSpecies;
    /** release crop trees */
    public boolean releaseCropTrees;
    /** release crop trees species dependent*/
    public boolean releaseCropTreesSpeciesDependent;           
    /** cut competing crop trees */
    public boolean cutCompetingCropTrees;
    /** thin none crop trees (by using temporary crop trees)*/
    public boolean thinArea;
    /** release temp crop trees species dependent*/
    public boolean thinAreaSpeciesDependent;     
    /** degree of thinning for thinArea */
    public double thinningIntensityArea;
    /** degree of thinning intensity */
    public double thinningIntensity = 1.0;
    /** type of thinning: 0= single tree selection, 1= from above, 2= from below, 9 = clear cut */
    public int typeOfThinning;
    /** year harvesting was started important for Schirmschlag */
    public int typeOfHarvest;
    /** year harvesting was started important for Schirmschlag */
    public int startOfHarvest;
    /** harvest or thinning was performed that year*/
    public int lastTreatment;
    /** decission if a habitat tree <0 is supposed to be selected, considering likelyhood by stand.size*/
    public boolean selectHabiatPart;
    // added by jhansen
    /** wanted volume of death wood remaining in stand*/
    public double wantedDeathVol=20.0;
    /** minimum diameter of death log*/
    public double minDeathDiameter=20.0;
    /** skidtrails*/
    public boolean skidtrails=false;
    /** skidtrails distance m*/
    public double skidtrailDistance=20.0;
    /** skidtrails*/
    public double skidtrailWidth=20.0;
    /** degree of stocking to clear overstory stand */
    public double degreeOfStockingToClearOverStoryStand=0.3;
        /** habitat tree type 0=hardwoods except Aln, 1= all hardwoods, 2= all   */
    public int habitatTreeType=0;
        /** trees protected from harvest and thinning, if DBH > value
            the protected trees will be marked and treated as additional habitat trees*/
    public int treeProtectedfromBHD=150;
        /** automatic Planting active   */
    public boolean autoPlanting=false;
        /** Planting: clear all trees first  */
    public boolean onPlantingRemoveAllTrees=false;
        /** Restricts all harvesting to a minimum coverage of the overstory*/
    public double minimumCoverage=0.0;
        /** Planting degree of stocking when Planting starts */
    public double degreeOfStockingToStartPlanting=0.3;
        /** Planting: String with species and area per ha [ha/ha]
         *  Example plant "211 [0.6];511 [0.3]" = 0.6ha beech and 0.3ha spruce */
    public String plantingString="";
        /** Regeneration Process: String with degree of stocking levels
         *  Example "0.6;0.4;0.2;0.0" means first harvest cut stand to 0.6 degree stocking,
            2nd Period cut Stand to 0.4, then 0.2 and then remove the rest*/
    public String regenerationProcess="";

    
    
    // max percentage of harvested/thinned wood must be added
    
    // added by jhansen
    public TreatmentRuleStand clone(){
        TreatmentRuleStand clone= new TreatmentRuleStand();
        clone.cutCompetingCropTrees= new Boolean(this.cutCompetingCropTrees);
        clone.harvestLayerFromBelow= new Boolean(this.harvestLayerFromBelow);
        clone.harvestingYears= new Integer(this.harvestingYears);
        clone.lastTreatment= new Integer(this.lastTreatment);
        clone.maxHarvestVolume= new Double(this.maxHarvestVolume);
        clone.maxHarvestingPeriode= new Integer(this.maxHarvestingPeriode);
        clone.maxOutVolume= new Double(this.maxOutVolume);
        clone.maxThinningVolume= new Double(this.maxThinningVolume);
        clone.minHarvestVolume= new Double(this.minHarvestVolume);
        clone.minOutVolume= new Double(this.minOutVolume);
        clone.minThinningVolume= new Double(this.minThinningVolume);
        clone.nHabitat= new Double(this.nHabitat);
        clone.protectMinorities= new Boolean(this.protectMinorities);
        clone.releaseCropTrees= new Boolean(this.releaseCropTrees);
        clone.releaseCropTreesSpeciesDependent= new Boolean(this.releaseCropTreesSpeciesDependent);
        clone.reselectCropTrees= new Boolean(this.reselectCropTrees);
        clone.selectCropTrees= new Boolean(this.selectCropTrees);
        clone.selectCropTreesOfAllSpecies= new Boolean(this.selectCropTreesOfAllSpecies);
        clone.selectHabiatPart= new Boolean(this.selectHabiatPart);
        clone.standType= new Integer(this.standType);
        clone.targetType= new Integer(this.targetType);
        clone.thinArea= new Boolean(this.thinArea);
        clone.thinAreaSpeciesDependent= new Boolean(this.thinAreaSpeciesDependent);
        clone.thinningIntensityArea= new Double(this.thinningIntensityArea);
        clone.treatmentStep= new Integer(this.treatmentStep);
        clone.treatmentType= new Integer(this.treatmentType);
        clone.typeOfHarvest= new Integer(this.typeOfHarvest);    
        clone.wantedDeathVol= new Double(this.wantedDeathVol);
        clone.minDeathDiameter= new Double(this.minDeathDiameter);
        clone.degreeOfStockingToClearOverStoryStand= new Double(this.degreeOfStockingToClearOverStoryStand);
        clone.habitatTreeType= new Integer(this.habitatTreeType);
        clone.plantingString= new String(this.plantingString);
        clone.regenerationProcess= new String(this.regenerationProcess);
        return clone;
    }
}


