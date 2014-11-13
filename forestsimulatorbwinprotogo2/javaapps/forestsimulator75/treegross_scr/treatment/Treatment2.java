/*
 * Treatment2.java
 *
 *  (c) 2002-2006 Juergen Nagel, Northwest German Forest Research Station, 
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

package treegross.treatment;
import treegross.base.*;


/**
 * @author	Henriette Duda 
 * for more information see:
 * Duda, H. (2006): Vergleich forstlicher Managementstrategien. 
 * Dissertation Universität Göttingen, S. 180 
 * http://webdoc.sub.gwdg.de/diss/2006/duda/ 
 */
public class Treatment2 {
    
    TreatmentElements2 te =new TreatmentElements2();
    Layer layer=new Layer();
    
    
    
    /** performs a stand treatment according to user defined preferences
     * this is  a regesigned rotutine from Henriette Duda's treatment
     * @param st stand object
     */ 
    public void executeManager2(Stand st){

        st.sortbyd();
        st.descspecies();
        if (st.status==0) st.status=1;
        //set min outVolume (lowest)
        if (st.trule.minHarvestVolume<=st.trule.minThinningVolume){
            st.trule.minOutVolume=st.trule.minHarvestVolume;
        }
        else{
            st.trule.minOutVolume=st.trule.minThinningVolume;
        }
        //set max outVolume (highest)
        if (st.trule.maxHarvestVolume>=st.trule.maxThinningVolume){
            st.trule.maxOutVolume=st.trule.maxHarvestVolume;
        }
        else{
            st.trule.maxOutVolume=st.trule.maxThinningVolume;
        }

        // reset amount of volume taken out 
        // only, if this treatment takes place at least one year after last treatment
        if (st.year>st.trule.lastTreatment){
            //vout, thinned, harvested is set =0
            te.resetOutTake(st);
        }
//
// create skidtrails, this is only done once        
//        
        if (st.trule.skidtrails) te.createSkidtrails(st);
 
        // do treatment, 
        // if actual year > year of last treatment + treatment step
        // or if actual year is the same as year of last treatment
        if (st.year>=st.trule.lastTreatment+st.trule.treatmentStep || st.year==st.trule.lastTreatment){
            
 // Nature protection block
 // Protect by diameter
            te.markTreesAsHabitatTreesByDiameter(st);
 // 1. crop tree per species to protect minorities
 // 2. habitat tree           
 //           
            // Start minority selection, by choosing one crop tree per species 
            if (st.trule.protectMinorities==true){
                //protect minoritys (select one tree per species)                
                te.SelectOneCropTreePerSpecies(st, true);
            }
            
            if (st.trule.nHabitat>0){
                //habitat trees are selected, habitat trees can not be harvested or chosen as
                // crop trees                
                te.selectHabitatTrees(st);
            }
// Harvesting block
            if (st.degreeOfDensity < st.trule.degreeOfStockingToClearOverStoryStand && 
                    st.h100 > 15.0) te.harvestRemainingTrees(st,true);
            if (st.trule.typeOfHarvest==0){
                // target diameter trees are harvested
                te.harvestTargetDiameter(st);        
            }
// Perform a Schirmschlag, if 0.3% of trees reach target diameter
            if (st.trule.typeOfHarvest==8 && (te.percentOfBasalAreaAboveTargetDiameter(st)>0.3 || st.status > 1)){
                te.harvestSchirmschlag(st); 
            }
// Perform a clearcut, if 0.3% of trees reach target diameter
            if ((st.trule.typeOfHarvest==9 && te.percentOfBasalAreaAboveTargetDiameter(st)>0.3)||st.status > 98){
                te.harvestClearCut(st); 
            }
//if harvest amount was not high enough: set harvested trees alive
// this is when harvest amount is less than the minimum harvest volume
// Idea: You do not bring in a machine for one tree
            te.checkMinHarvestVolume(st);

// Crop tree selection
// 
//   if the number of wanted crop trees is changed and lower than the selected one
//   than reset crop trees
//
            double sumcroptrees=0;
            for (int i=0;i<st.nspecies;i++) sumcroptrees=sumcroptrees+st.sp[i].trule.numberCropTreesWanted;
            if (te.getNCropTrees(st) > sumcroptrees*st.size)
                te.resetCropTrees(st);
//
            // Selection and reselection of Crop Trees
            if (te.getNCropTrees(st)<=0 || st.trule.reselectCropTrees==true)
                {
                    // select defined number of crop trees
                    //number is reduced, depending on dg of leading layer
                    if (st.trule.selectCropTrees) te.selectNCropTrees(st);

                }
              
// Thinning by releasing the crop trees              
            if (st.trule.releaseCropTrees==true && st.trule.typeOfThinning==0){
                te.thinCropTreeCompetition(st);               
                if (st.trule.thinArea==true) {
                    te.thinCompetitionFromAbove(st);
//                    System.out.println("Zwischenfelder durchforsten");
                }
            }

// thin area between crop trees
// selectCropTreesOfAllSpecies auch hier einbeziehen?
            //public double degreeOfThinningArea;
            if (st.trule.typeOfThinning == 1){
                System.out.println("temporäre Zwischenfelder durchforsten");
            //all temp crop tree are deselected
                te.resetTempCropTrees(st);

                //select temp crop trees (wet species)
                te.selectTempCropTreesTargetPercentage(st);
                // Start thinning for all species
                te.thinTempCropTreeCompetition(st);
                if (st.trule.thinArea==true) te.thinCompetitionFromAbove(st);
            }


// Thinning from below              
            if (st.trule.typeOfThinning==2){
                te.thinFromBelow(st);               
            }


            //if thinning amount was not high enough: set thinned trees alive
            te.checkMinThinningVolume(st);

            //if treatment amount was not high enough: set outtaken trees alive
            te.checkMinTreatmentOutVolume(st);
        }

            //if trees has been taken out this year
            if (te.getHarvestedOutVolume(st)>0 || te.getThinnedOutVolume(st)>0){
                st.trule.lastTreatment=st.year;
            }
      if (st.trule.autoPlanting && te.getDegreeOfCover(0, st, false) < st.trule.degreeOfStockingToStartPlanting ){
          if (st.trule.onPlantingRemoveAllTrees) te.harvestRemainingTrees(st,false);
          te.startPlanting(st);
      }

    }      

    
        
    /** stand is sorted by diameter and species are describes
     * @param st stand object
     */
    public void updateStandAfterThinning(Stand st){

        st.sortbyd();
        st.descspecies();        
    }
    
    /** unselct all (temp)croptrees
     * @param st stand object
     */
    public void resetAllCropTrees(Stand st){
        te.resetTempCropTrees(st);
        te.resetCropTrees(st);
        
    }

/**
 *
 * @param Stand to be treated
 * @param active true = yes skid trails, false = no skid trails
 * @param trailDistance Distance from the center to the next center of the skid trail [m] Default 20m
 * @param trailWidth  Width of the skid trail [m]. Default 4m
 * All trees on the skid trail will be removed. The method is excuted only once
 *
 */
    public void setSkidTrails(Stand st, boolean active, double trailDistance, double trailWidth){
        st.trule.skidtrails = active;
        st.trule.skidtrailDistance = trailDistance;
        st.trule.skidtrailWidth = trailWidth;
    }


/**
 *
 * @param st Stand object to be treated
 * @param type 0= single tree selection, 1=thinning from above, 2= thinning from below,
 * @param intensity Factor to normal stand density 1.0= normal stand density, no thinning
 *        set intensity = 0.0
 * @param minVolume Minimum Volume to perform thinning [m³]
 * @param maxVolume Maximum Volume of thinning [m³]
 * @param croptreesOnly Release only crop trees
 *
 */
    public void setThinningRegime(Stand st, int type, double intensity, double minVolume, double maxVolume, boolean croptreesOnly){
       st.trule.thinAreaSpeciesDependent =true;
       st.trule.thinArea = true;
       st.trule.selectCropTrees=false;
       st.trule.reselectCropTrees=false;
       st.trule.releaseCropTrees=false;
       st.trule.cutCompetingCropTrees=false;
       st.trule.releaseCropTreesSpeciesDependent =false;
       st.trule.minThinningVolume= minVolume;
       st.trule.maxThinningVolume= maxVolume;
       st.trule.thinningIntensity= intensity;
       if (type == 0){
            st.trule.typeOfThinning=0;
            st.trule.thinArea=true;
            if (croptreesOnly) st.trule.thinArea=false;
            else st.trule.thinArea=true;
            st.trule.selectCropTrees=true;
            st.trule.reselectCropTrees=true;
            st.trule.selectCropTreesOfAllSpecies=false;
            st.trule.releaseCropTrees=true;
            st.trule.cutCompetingCropTrees=true;
            st.trule.releaseCropTreesSpeciesDependent =true;
        }
// Set thinning from above here by temporay crop trees
        if (type == 1){
            st.trule.typeOfThinning=1;
            st.trule.selectCropTreesOfAllSpecies=false;
        }
// Set thinning from below
        if (type == 2){
            st.trule.typeOfThinning=2;
        }
    }


    public void setHarvestRegime(Stand st,int type,  double minVolume, double maxVolume, double degreeToClear, String regenerationProc){
       st.trule.minHarvestVolume=minVolume;
       st.trule.maxHarvestVolume=maxVolume;
       st.trule.harvestLayerFromBelow =false;
       if (type == 0){
            st.trule.typeOfHarvest=0;
            st.trule.maxHarvestingPeriode=6;
            st.trule.lastTreatment =0;
            st.trule.degreeOfStockingToClearOverStoryStand=degreeToClear;
        }
// set Harvest type Schirmschlag
        if (type == 1){
            st.trule.minThinningVolume=0;
            st.trule.maxThinningVolume=900;
            st.trule.thinningIntensityArea=0.0;
            st.trule.minHarvestVolume=0.0;
            st.trule.maxHarvestVolume=500.0;
            st.trule.typeOfHarvest=8;
            st.trule.cutCompetingCropTrees=true;
            st.trule.startOfHarvest=0;
            st.trule.regenerationProcess=regenerationProc;
        }
// set Harvest type Clear Cut
        if (type == 2){
            st.trule.thinningIntensityArea=0.0;
            st.trule.minHarvestVolume=0.0;
            st.trule.maxHarvestVolume=99999.0;
            st.trule.typeOfHarvest=9;
            st.trule.maxHarvestingPeriode=0;
            st.trule.lastTreatment =0;
        }


    }
/**
 *
 * @param st  Stand to be treated
 * @param habitatTrees number of trees /ha
 * @param typeOfHabitat 0=ei,bu; 1= hardwoods; 2= all trees
 * @param minority protect rare species
 * @param minimum stocking restricts further harvesting
 * @param diameterProtection protects trees form thinning and harvest if dbh > value
 */
    public void setNatureProtection(Stand st,int habitatTrees,  int typeOfHabitat, boolean minority, double minStocking, int diameterProtection){
        st.trule.protectMinorities=minority;
        st.trule.nHabitat=habitatTrees;
        st.trule.habitatTreeType=typeOfHabitat;
        st.trule.minimumCoverage=minStocking;
        st.trule.treeProtectedfromBHD=diameterProtection;
      }

    public void setAutoPlanting(Stand st,boolean active,  boolean clearArea, double critCoverage, String plantingStr){
        st.trule.autoPlanting=active;
        st.trule.onPlantingRemoveAllTrees=clearArea;
        st.trule.degreeOfStockingToStartPlanting=critCoverage;
        st.trule.plantingString=plantingStr;
      }
    

    

    
}



