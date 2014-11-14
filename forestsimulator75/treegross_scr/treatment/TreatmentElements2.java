/*
 * TreatmentElements2.java
 *
 *
 *  (c) 2002-2008 Juergen Nagel, Northwest Forest Research Station, 
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
 *  http://www.nw-fva.de
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
public class TreatmentElements2 {
    
    /** maximal harvest volume*/
    double vmaxharvest;   
    
    /** maximal thinningvolume*/
    double vmaxthinning;    
    
     /**The actual harvested volume*/
    double vout;      
    
    /** volume harvested */
    double harvested;

    /** volume thinned */
    double thinned;

    
    Layer layer=new Layer();    
    //Choose trees for stand target mixture dependant on scenario
    CropTreeSelection ctselect=new CropTreeSelection();
    HabitatTreeSelection htselect= new HabitatTreeSelection();    
    
    /** set volume of trees, which are taken out to 0. This sets the variables
     *  vout, harvested, thinned, vmaxharvest, vmaxthinning = 0.0
     * @param st stand object
     */
    public void resetOutTake(Stand st){      
        // reset actual harvest volume
        vout=0;
        //reset counter for harvested and thinned ammount
        harvested=0;
        thinned=0;
        //reset max volumes
        vmaxharvest=0;
        vmaxthinning=0;         
    }
    
    /** unselect all crop trees
     * @param st stand object
     */
    public void resetCropTrees(Stand st){
        for (int i=0;i<st.ntrees;i++) {st.tr[i].crop=false;} 
    }
    
    /** unselect all temp crop trees*/
    public void resetTempCropTrees(Stand st){
        for (int i=0;i<st.ntrees;i++) {st.tr[i].tempcrop=false;} 
    }

    /** unselect all habitat trees
     * @param st stand object
     */
    public void resetHabitatTrees(Stand st){
        for (int i=0;i<st.ntrees;i++) {st.tr[i].habitat=false;} 
    }
    
    
    /** to obtain at least one tree of each species helps to
     * secure rare species and
     * to create a high bioderversity
     * @param st stand object
     */
     public void SelectOneCropTreePerSpecies(Stand st, boolean forceSelection){
       double rn;
       
       if (forceSelection==true){
           rn=0;
       }
       //random number between 0 and 1
       else rn=Math.random();
       // if standsize < 1ha select trees with likelihood of standsize
       if (rn<= st.size){
            CropTreeSpecies ctspecies[]= new CropTreeSpecies[30];
            //Distance CropTrees
            double dist_ct;

            // Sort species that the species with the least trees will be first
            // sort ascening
            Species sptemp = new Species();
            for (int i=0;i<st.nspecies-1;i++){
                for (int j=i+1;j<st.nspecies;j++){
                    if (st.sp[i].nha > st.sp[j].nha){
                        sptemp=st.sp[i];
                        st.sp[i]=st.sp[j];
                        st.sp[j]=sptemp; 
                    }
                }
            }           

            //Define CTSpecies
            //Initialize Croptreespecies dependent on target mixture percentage and height of first thinning
            for (int i=0;i<st.nspecies; i++){
                //calculate distance dependent on crownwidth (dependent on species and TargetDBH)
                Tree atree = new Tree();
                atree.st = st;
                atree.code=st.sp[i].code;
                atree.sp=st.sp[i];
                //distance is depending on crownwidth of tree of that species, that has reached target diameter
                atree.d=st.sp[i].trule.targetDiameter;
                dist_ct=atree.calculateCw();

                //Initialize CropTreeSpecies
                ctspecies[i]= new CropTreeSpecies();
                ctspecies[i].addCtsp(st.sp[i].code, 1.0/st.size , dist_ct, st.sp[i].trule.minCropTreeHeight);
            }             

            //Select Croptrees dependent on CTSpecies
            ctselect.selectCropTrees(st, ctspecies);        
       }
    }
    
    
    /** select croptrees dependent on target percentages of species
     * @param st stand object
     */
    public void selectCropTreeTargetPercentage(Stand st){
        
        CropTreeSpecies ctspecies[]=new CropTreeSpecies[30];
       
        //Distance CropTrees
        double dist_ct;
        //Number of Crop Trees
        int n_ct_ha;
        
        //Define CTSpecies
        //Initialize Croptreespecies dependent on target mixture percentage and height of first thinning
        for (int i=0;i<st.nspecies; i++){
            //if (st.sp[i].nha>0){
                //calculate distance dependent on crownwidth (dependent on species and TargetDBH)
                Tree atree = new Tree();
                atree.st = st;
                atree.code=st.sp[i].code;
                atree.sp=st.sp[i];
                atree.d=st.sp[i].trule.targetDiameter;
                dist_ct=atree.calculateCw();

                //Number of crop trees dependent on calcualted distance and target mixture percent
                n_ct_ha=(int)((10000.0/((Math.PI*Math.pow(dist_ct,2.0))/4))*st.sp[i].trule.targetCrownPercent/100.0);

                //Initialize CropTreeSpecies
                ctspecies[i]= new CropTreeSpecies();
                ctspecies[i].addCtsp(st.sp[i].code, n_ct_ha, dist_ct, st.sp[i].trule.minCropTreeHeight);
            //}
        }                      
        //Select Croptrees dependent on CTSpecies
        ctselect.selectCropTrees(st, ctspecies);
    }
    
    /** select croptrees dependent on number wanted
     * number is reduced to a number that fit actual dg of species in its leading layer
     * number is reduced by targetCrownPercent of species
     * @param st stand object
     */
    public void selectNCropTrees(Stand st){
        
        CropTreeSpecies ctspecies[]=new CropTreeSpecies[30];
       
        /**Number of crop trees to select*/
        int n_ct_ha;
        
        /**distance of crop trees to select*/
        double dist_ct;        
        
        
        //Initialize Croptreespecies dependent on target mixture percentage and height of first thinning
        for (int i=0;i<st.nspecies; i++){
            if (st.sp[i].trule.numberCropTreesWanted==0) dist_ct=0.01;
//            else dist_ct=0.90* Math.sqrt((st.size*100.0*st.sp[i].trule.targetCrownPercent/(st.sp[i].trule.numberCropTreesWanted*st.size)));
            else dist_ct=0.80* Math.sqrt((100.0*st.sp[i].trule.targetCrownPercent/(st.sp[i].trule.numberCropTreesWanted)));
              //Initialize CropTreeSpecies
            ctspecies[i]= new CropTreeSpecies();
            ctspecies[i].addCtsp(st.sp[i].code, st.sp[i].trule.numberCropTreesWanted, dist_ct, st.sp[i].trule.minCropTreeHeight);
        }                      
        //Select Croptrees dependent on CTSpecies
        ctselect.selectCropTrees(st, ctspecies);
    }    
      
    
 
    
    /** select croptrees dependent on target percentages of species
     * @param st stand object
     */
    public void selectTempCropTreesTargetPercentage (Stand st){
        
        CropTreeSpecies ctspecies[]=new CropTreeSpecies[30];
       
        //Distance CropTrees
        double dist_ct;
        //Number of Crop Trees
        int n_ct_ha;
        //Find Crownwidth
        //Define CTSpecies
        //Initialize Temp Croptrees dependent on target mixture percentage and height of first thinning
        for (int i=0;i<st.nspecies; i++){   
            //if (st.sp[i].nha>0){
                Tree atree = new Tree();
                atree.st = st;
                atree.code=st.sp[i].code;
                atree.d=st.sp[i].d100;
                atree.sp=st.sp[i];
                dist_ct=atree.calculateCw();

                //Number of temp crop trees dependent on calcualted distance and target mixture percent
                // number: 1/2 of matching tree number
                n_ct_ha=(int)((10000.0/((Math.PI*Math.pow(dist_ct,2.0))/4))*st.sp[i].trule.targetCrownPercent/100.0);


                //Initialize Temp CropTreeSpecies
                ctspecies[i]= new CropTreeSpecies();
                ctspecies[i].addCtsp(st.sp[i].code, n_ct_ha, dist_ct, st.sp[i].trule.minCropTreeHeight);
            //}
         
        }    
        
        //Select Temp Croptrees dependent on target mixture percentage
        ctselect.selectTempCropTrees(st, ctspecies);     
    }       
    
    
     public void selectHabitatTrees (Stand st){
        htselect.selectHabitatTrees(st);
    }
    

    /** harvest trees with dbh> targetdiameter, start with trees 
     * no habitat tree will be taken out
     * trees are taken out until max harvest volume is reached*/
    public void harvestTargetDiameter(Stand st){
        
        //set max harvest volume (vmaxharvest) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxHarvestVolume)
        vmaxharvest=st.size*st.trule.maxHarvestVolume-harvested;

        //reduce max harvest if max allowed amount for stand (st.size*st.trule.maxHarvestVolume)
        // minus outaken amount (vout) is less than set max harvest volume (vmaxharvest)
        if ((st.size*st.trule.maxOutVolume-vout)<vmaxharvest){
            vmaxharvest=st.size*st.trule.maxOutVolume-vout;
        }
        
        // if there is a amount to be harvested
        if (vmaxharvest>0){
            //Sort st.tr by difference targetdiameter -diameter ascending        
            Tree trtemp = new Tree();
            for (int i=0;i<st.ntrees-1;i++){
                for (int j=i+1;j<st.ntrees;j++){
                    if ((st.tr[i].sp.trule.targetDiameter-st.tr[i].d) > (st.tr[j].sp.trule.targetDiameter-st.tr[j].d)){
                        trtemp=st.tr[i];
                        st.tr[i]=st.tr[j];
                        st.tr[j]=trtemp;
                    }
                } 
            }              
            //see if there are target diameter trees , then harvest those trees
            // conditions: no habitat tree, diameter > target diameter, standing, max harvest volume has not been reached
            for (int i=0;i<st.ntrees;i++) {
                if (st.tr[i].habitat==false && st.tr[i].d > st.tr[i].sp.trule.targetDiameter
                        && st.tr[i].out < 0 && harvested < vmaxharvest) {
                    if (this.getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) break;
                    vout=vout+st.tr[i].fac*st.tr[i].v;
                    harvested=harvested+st.tr[i].fac*st.tr[i].v;
                    st.tr[i].out=st.year;
                    st.tr[i].outtype=3;
                }      
            }
        }
    }     
    
    
    
    
    /* added by jhansen
     */
       public void harvestTargetDiameterHighPrecision(Stand st){     
       vmaxharvest=st.size*st.trule.maxHarvestVolume; 
       if (vmaxharvest>0){
            //Sort st.tr by diameter        
            Tree tempTree = new Tree();
            for (int i=0; i<st.ntrees-1; i++){
                for (int j=i+1; j<st.ntrees; j++){
                    if (st.tr[i].d<st.tr[j].d){
                        tempTree=st.tr[i];
                        st.tr[i]=st.tr[j]; 
                        st.tr[j]=tempTree;
                    } 
                }
            }
            for (int i=0;i<st.ntrees;i++) {
                if(st.tr[i].out < 0 && harvested < vmaxharvest){
                    if((harvested+(st.tr[i].fac*st.tr[i].v))<=vmaxharvest){
                        vout=vout+(st.tr[i].fac*st.tr[i].v);
                        harvested=harvested+(st.tr[i].fac*st.tr[i].v);
                        st.tr[i].out=st.year;
                        if(st.tr[i].d > st.tr[i].sp.trule.targetDiameter) st.tr[i].outtype=3;
                        else st.tr[i].outtype=2;
                    }
                }      
            }
        }
    }   
    
    
    /**Check all trees if they are a crop tree, if they are competing with another crop tree
     * Mesurement: overlapping of their crowns
     * crop tree that diamteter exceeds target diameter most will be taken out first
     * This void needs CropTreeSelection 
     *
     * @param st stand object
     */
    public void harvestCompetingCropTrees(Stand st){
        //set max harvest volume (vmaxharvest) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxHarvestVolume)
        vmaxharvest=st.size*st.trule.maxHarvestVolume-harvested;

        //reduce max harvest if max allowed amount for stand (st.size*st.trule.maxHarvestVolume)
        // minus outaken amount (vout) is less than set max harvest volume (vmaxharvest)
        if ((st.size*st.trule.maxOutVolume-vout)<vmaxharvest){
            vmaxharvest=st.size*st.trule.maxOutVolume-vout;
        }


        if (vmaxharvest>0){
            
            //Sort st.tr by difference targetdiameter -diameter ascending        
            Tree trtemp = new Tree();
            for (int i=0;i<st.ntrees-1;i++){
                for (int j=i+1;j<st.ntrees;j++){
                    if ((st.tr[i].sp.trule.targetDiameter-st.tr[i].d) > (st.tr[j].sp.trule.targetDiameter-st.tr[j].d)){
                        trtemp=st.tr[i];
                        st.tr[i]=st.tr[j];
                        st.tr[j]=trtemp;
                    }
                } 
            }               
       
            /**distance of two trees*/
            double dist_trees=0.0;
            double dist_min=0.0;

            /**height for the cross section of crop tree
            * cb(i)+ (h(i)+cb(i)/3)*/
            double h66_i;
            /** cb(j)+ (h(j)+cb(j)/3)*/

            for (int i=0;i<st.ntrees;i++) {
                //check only crop trees for competing crop trees
                if (st.tr[i].out<1 && st.tr[i].crop==true){
                    for (int j=0;j<st.ntrees;j++){
                        //competitor has to be a crop tree                            
                        if (st.tr[j].out < 0 && st.tr[j].crop==true && st.tr[i].no != st.tr[j].no && st.tr[j].habitat==false){
                            //caluclate distance between crop trees                                
                            dist_trees=Math.sqrt(Math.pow(st.tr[i].x-st.tr[j].x,2.0)+Math.pow(st.tr[i].y-st.tr[j].y,2.0));

                            //calculate height of considered trees in 1/3 of the height their crown
                            //geändert index auf i von j
                            h66_i=st.tr[i].cb+(st.tr[i].h-st.tr[i].cb)/3;

                            //tree (j) can only be a competitor 
                            // if it is higher than potentially pressed tree (i) in its h66
                            if (h66_i<st.tr[j].h){
                                //potentially pressed tree (i) is higher than tree (j)
                                    dist_min= (st.tr[j].calculateCwAtHeight(h66_i)+ st.tr[i].calculateCwAtHeight(h66_i))/2;
                                    dist_min= (st.tr[i].calculateCwAtHeight(h66_i)+ st.tr[j].calculateCwAtHeight(h66_i))/2;   
                            }
                            // tree is no competitor
                            else dist_min=0.0;  

                            if (harvested < vmaxharvest 
                                    && dist_trees<dist_min
                                    && (st.tr[j].sp.trule.targetDiameter-st.tr[j].d)<=(st.tr[i].sp.trule.targetDiameter-st.tr[i].d)) {
                                st.tr[j].out=st.year;
                                st.tr[j].outtype=3;
                                vout=vout+st.tr[j].fac*st.tr[j].v;
                                harvested=harvested+st.tr[j].fac*st.tr[j].v;  
                            }                          
                        }
                    }
                }
            }   
        }
    }
        
/** clearcut of all overstory trees h >= st.h100*0.4
 * all trees marked outtype = 99
 * Stand status is set to 99
 * @param st Stand
 */
    public void harvestClearCut(Stand st){
       for (int i=0; i < st.ntrees; i++)
           if (st.tr[i].out < 1 && st.tr[i].h >= st.h100*0.4 && st.tr[i].habitat != true ){
              if (this.getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) break;
               st.tr[i].out = st.year;
               st.tr[i].outtype = 3;
           }
    //clear all remaining trees above 7 cm if wanted
       if (st.trule.onPlantingRemoveAllTrees ){
           for (int i=0;i<st.ntrees;i++)
               if (st.tr[i].out < 0 ){
                   if (this.getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) break;
                   st.tr[i].out=st.year;
                   st.tr[i].outtype=3;
                }
       }
       st.status=1;
    }         

     public double percentOfBasalAreaAboveTargetDiameter(Stand st){
        // Check basal area of target trees
         double perc=0.0;
         double sum =0.0;
         double sumTarget = 0.0;
         for (int i=0; i < st.ntrees; i++)
              if (st.tr[i].out < 0 && st.tr[i].d >=7.0 && st.tr[i].habitat==false){
                 sum = sum + Math.pow(st.tr[i].d,2.0);
                 if (st.tr[i].d > st.tr[i].sp.trule.targetDiameter ) sumTarget = sumTarget + Math.pow(st.tr[i].d,2.0);
              }
         perc = sumTarget/sum;
         return perc;
     }



     /**
      * shelter wood harvest
      * Harvest target diameter trees according to st.regenerationProcess
      * the desired degree of stocking is determined by st.status
      * @param st stand object
      */
    public void harvestSchirmschlag(Stand st){

        double degree = 0.0;
        boolean finito = false;
        String rp = st.trule.regenerationProcess;
        for (int i=0;i<st.status;i++){
            int m = rp.indexOf(";");
            if (m < 1){ //last Element reached, final clear cut
                if (degree == 0.0) st.status=98; else st.status=1;
                finito = true;
                break;
            }
            else {
                degree = Double.parseDouble(rp.substring(0, m).trim());
                rp = rp.substring(m+1, rp.length());
            }
        }
        st.status=st.status+1;
        double baHarv =0.0;
        double baOut =0.0;
        if (degree > 0.0){
            baOut =st.bha - getMaxStandBasalArea(st)*degree;
            if (baOut < 0.0) baOut=0.0;
            while (baHarv < baOut){
// Harvest crop trees            
                double max = -9999999.0;
                int merk = -9;
                for (int i=0;i<st.ntrees;i++)
                    if (st.tr[i].out < 0 && st.tr[i].d >= 7.0 && st.tr[i].habitat == false){
                        double diff = st.tr[i].d - st.tr[i].sp.trule.targetDiameter;
                        if (max < diff){
                            merk = i;
                            max = diff;
                        }
                    }
                if (this.getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) break;
                st.tr[merk].out = st.year;
                st.tr[merk].outtype = 3;
                baHarv = baHarv + Math.PI*Math.pow(st.tr[merk].d/200.0,2.0)*(st.tr[merk].fac/st.size);
            }
        }
     }
 
                          
    
     /**
     * harvest all remaining trees or only overstory trees
     * this is used for final removal
     *
     *
     * @param st stand object
     */
    public void harvestRemainingTrees(Stand st,boolean overstoryOnly){
       double hx = 1.3;
       if (overstoryOnly ) hx=st.sp[0].hbon*0.4;
       for (int i=0; i < st.ntrees; i++)
            if (st.tr[i].out < 1 && st.tr[i].h >=hx && st.tr[i].habitat==false){
                   if (this.getDegreeOfCover(0, st, true) < st.trule.minimumCoverage) break;
                   st.tr[i].out = st.year;
                   st.tr[i].outtype = 3;
            }
          st.status=1;
          }
   
    public double getMaxStandBasalArea(Stand st){
        double maxBA =0.0;
        for ( int i=0; i < st.nspecies; i++){
            Tree atree = new Tree();
            atree.sp=st.sp[i];
            atree.st=st;
            atree.d =st.sp[i].d100;
            atree.h =st.sp[i].h100;
            atree.age =(int) Math.round(st.sp[i].h100age);
            atree.cw=atree.calculateCw();
            atree.code=st.sp[i].code;
            maxBA = maxBA + atree.calculateMaxBasalArea()*atree.getModerateThinningFactor()*
                      (st.sp[i].percCSA/100.0);
           }
        return maxBA;
    }
    
    
    /**Check all trees if they are no crop tree, if they are competing with a crop tree
     * Mesurement: is the overlap of the crown
     * This void needs CropTreeSelection 
     *
     * @param st stand object
     */

        public void thinCropTreeCompetition(Stand st){
        
        //set max thinning volume (vmaxthinning) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        vmaxthinning=st.size*st.trule.maxThinningVolume-thinned;
        
        
        //reduce max thinning if max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        // minus outaken amount (vout) is less than set max thinning volume (vmaxthinning)
        if ((st.size*st.trule.maxOutVolume-vout)<vmaxthinning){
            vmaxthinning=st.size*st.trule.maxOutVolume-vout;
        }

                
        if (vmaxthinning>0){
        // Thinning is done iteratively tree by tree
        // 1. Calculate the overlap of all crop trees
        // 2. Calculate tolerable overlap of crop tree according to Spellmann et al,
        //    Heidi Doebbeler and crown width functions
        // 3. Find tree with the highest differenz in overlap - tolerable overlap
        // 4. Remove for the crop tree of 3.) the tree with the greates overlap area
        // 5. Start with 1. again 
            
//
           double intensity = 2.0-st.trule.thinningIntensity;
           if (intensity == 0.0) intensity = 1.0; 
//      Festlegen der Grundflächenansenkung   
           double maxStandBasalArea =getMaxStandBasalArea(st); 
           if (st.trule.thinningIntensity == 0.0 ) maxStandBasalArea = maxStandBasalArea* 100.0;
              else  maxStandBasalArea = maxStandBasalArea*(2.0-st.trule.thinningIntensity);
           double maxBasalAreaOut = st.bha - maxStandBasalArea;
//           
            boolean doNotEndThinning = true;   
            if (maxBasalAreaOut <= 0.0) doNotEndThinning = false; 
            else{
            do {
// update competition overlap for crop trees 
                for ( int i=0; i < st.ntrees; i++)
                    if (st.tr[i].out < 0 && st.tr[i].crop)
                        st.tr[i].updateCompetition();
// find crop with most competition, defined as that tree with greates ratio of
// actual c66xy devided by maximum c66
                int indexOfCroptree =-9;
                double maxCompetition = -99999.9;
                for ( int i=0; i < st.ntrees; i++)
                    if (st.tr[i].out < 0 && st.tr[i].crop){
// calculate maxc66
                       double maxBasalArea = st.tr[i].calculateMaxBasalArea()*st.tr[i].getModerateThinningFactor();
                       if (st.trule.thinningIntensity == 0.0 ) maxBasalArea = maxBasalArea* 100.0;
                            else  maxBasalArea = maxBasalArea*(2.0-st.trule.thinningIntensity);
                       double maxN = maxBasalArea/(Math.PI*Math.pow((st.tr[i].d/200.0),2.0));
                       double maxC66 = maxN * Math.PI* Math.pow((st.tr[i].cw/2.0),2.0)/10000.0;
                       double c66Ratio = st.tr[i].c66xy/maxC66;
// remember tree if c66Ratio is greater than maxCompetition
                       if (c66Ratio > maxCompetition) {
                           indexOfCroptree =i;
                           maxCompetition=c66Ratio;
                       }
                    }
// release the crop tree with indexOfCropTree and take out neighbor, which comes closest with the
// crown to the crop tree's crown at height crown base. Neighbors are taken out only if they come
// into the limit of twice the crown radius of the crop tree size 
//                 
// Find neighbor who comes closest 
                if (indexOfCroptree < 0)doNotEndThinning = false;
                else {
                    double dist = 9999.0;
                    int merk =-9;
                    double h66 = st.tr[indexOfCroptree].cb; 
                    for ( int i=0; i < st.tr[indexOfCroptree].nNeighbor; i++)
                            if ( st.tr[st.tr[indexOfCroptree].neighbor[i]].d > 7 && st.tr[st.tr[indexOfCroptree].neighbor[i]].out < 0 && (st.trule.cutCompetingCropTrees ||
                                     st.tr[st.tr[indexOfCroptree].neighbor[i]].crop == false) && st.tr[i].habitat==false){
                                double radius = st.tr[st.tr[indexOfCroptree].neighbor[i]].calculateCwAtHeight(h66)/2.0;
                                double ent = Math.sqrt(Math.pow(st.tr[indexOfCroptree].x-st.tr[st.tr[indexOfCroptree].neighbor[i]].x,2.0)+
                                    Math.pow(st.tr[indexOfCroptree].y-st.tr[st.tr[indexOfCroptree].neighbor[i]].y,2.0));
                                if ((ent- radius < st.tr[indexOfCroptree].cw*(0.75/intensity)) && dist>(ent-radius) ) {
                                    merk=st.tr[indexOfCroptree].neighbor[i];
                                    dist=ent-radius;
                                }
                            }
// if merk > 9 then cut tree else stop crop tree release                    
                     if (merk == -9) {doNotEndThinning = false;}
                     else {
                          st.tr[merk].out = st.year; 
                          st.tr[merk].outtype = 2; 
                          thinned = thinned + (st.tr[merk].fac*st.tr[merk].v);
                          maxBasalAreaOut = maxBasalAreaOut - (st.tr[merk].fac*Math.PI*Math.pow(st.tr[merk].d/200.0,2.0))/st.size;
                          if (maxBasalAreaOut <= 0.0) doNotEndThinning = false; 
                     }
                }

            }
            //stop if max thinning amount is reached or all competitors are taken out
            while (thinned < vmaxthinning && doNotEndThinning);
            }
        }
       // System.out.println("");
    } 

    
    /**Check all trees if they are no crop tree, if they are competing with a crop tree
     * Mesurement: a-value
     * This void needs CropTreeSelection
     * Thinning degree for each species is taken from st.tr[j].sp.trule.thinningIntensity
     *
     * @param st stand object
     */
        public void thinTempCropTreeCompetition(Stand st){
        
        //set max thinning volume (vmaxthinning) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        vmaxthinning=st.size*st.trule.maxThinningVolume-thinned;
        
        
        //reduce max thinning if max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        // minus outaken amount (vout) is less than set max thinning volume (vmaxthinning)
        if ((st.size*st.trule.maxOutVolume-vout)<vmaxthinning){
            vmaxthinning=st.size*st.trule.maxOutVolume-vout;
        }

                
        if (vmaxthinning>0){
        // Thinning is done iteratively tree by tree
        // 1. Calculate the overlap of all crop trees
        // 2. Calculate tolerable overlap of crop tree according to Spellmann et al,
        //    Heidi Doebbeler and crown width functions
        // 3. Find tree with the highest differenz in overlap - tolerable overlap
        // 4. Remove for the crop tree of 3.) the tree with the greates overlap area
        // 5. Start with 1. again 
            
//
//      Festlegen der Grundflächenansenkung   
           double maxStandBasalArea =0.0; 
           for ( int i=0; i < st.nspecies; i++){
              Tree atree = new Tree();
              atree.st=st;
              atree.sp=st.sp[i];
              atree.d =st.sp[i].d100;
              atree.h =st.sp[i].h100;
              atree.age =(int) Math.round(st.sp[i].h100age);
              atree.code=st.sp[i].code;
              atree.cw=atree.calculateCw();
              maxStandBasalArea = maxStandBasalArea+ atree.calculateMaxBasalArea()*atree.getModerateThinningFactor()*
                      (st.sp[i].percCSA/100.0);
           }
           if (st.trule.thinningIntensity == 0.0 ) maxStandBasalArea = maxStandBasalArea* 100.0;
              else  maxStandBasalArea = maxStandBasalArea*(2.0-st.trule.thinningIntensity);
           double maxBasalAreaOut = st.bha - maxStandBasalArea;
           double intensity = st.trule.thinningIntensity; 
           if (intensity == 0.0) intensity = 1.0; 
           
            
            
            
            boolean doNotEndThinning = true;   
            if (maxBasalAreaOut <= 0.0) doNotEndThinning = false;
            else{
            do {
// update competition overlap for crop trees 
                for ( int i=0; i < st.ntrees; i++)
                    if (st.tr[i].out < 0 && st.tr[i].tempcrop)
                        st.tr[i].updateCompetition();
// find crop with most competition, defined as that tree with greates ratio of
// actual c66xy devided by maximum c66
                int indexOfCroptree =-9;
                double maxCompetition = -99999.9;
                for ( int i=0; i < st.ntrees; i++)
                    if (st.tr[i].out < 0 && st.tr[i].tempcrop){
// calculate maxc66
                       double maxBasalArea = st.tr[i].calculateMaxBasalArea()*st.tr[i].getModerateThinningFactor();
                       if (st.trule.thinningIntensity == 0.0 ) maxBasalArea = maxBasalArea* 100.0;
                            else  maxBasalArea = maxBasalArea*(2.0-st.trule.thinningIntensity);
                       double maxN = maxBasalArea/(Math.PI*Math.pow((st.tr[i].d/200.0),2.0));
                       double maxC66 = maxN * Math.PI* Math.pow((st.tr[i].cw/2.0),2.0)/10000.0;
                       double c66Ratio = st.tr[i].c66xy/maxC66;
// remember tree if c66Ratio is greater than maxCompetition
                       if (c66Ratio > maxCompetition) {
                           indexOfCroptree =i;
                           maxCompetition=c66Ratio;
                       }
                    }
// release the crop tree with indexOfCropTree and take out neighbor, which comes closest with the
// crown to the crop tree's crown at height crown base. Neighbors are taken out only if they come
// into the limit of twice the crown radius of the crop tree size 
//                 
// Find neighbor who comes closest 
                if (indexOfCroptree < 0)doNotEndThinning = false;
                else {
                    double dist = 9999.0;
                    int merk =-9;
                    double h66 = st.tr[indexOfCroptree].cb; 
                    for ( int i=0; i < st.tr[indexOfCroptree].nNeighbor; i++)
                            if ( st.tr[st.tr[indexOfCroptree].neighbor[i]].d < 7 && st.tr[st.tr[indexOfCroptree].neighbor[i]].out < 0 && (st.trule.cutCompetingCropTrees ||
                                     st.tr[st.tr[indexOfCroptree].neighbor[i]].tempcrop == false)){
                                double radius = st.tr[st.tr[indexOfCroptree].neighbor[i]].calculateCwAtHeight(h66)/2.0;
                                double ent = Math.sqrt(Math.pow(st.tr[indexOfCroptree].x-st.tr[st.tr[indexOfCroptree].neighbor[i]].x,2.0)+
                                    Math.pow(st.tr[indexOfCroptree].y-st.tr[st.tr[indexOfCroptree].neighbor[i]].y,2.0));
                                if ((ent- radius < st.tr[indexOfCroptree].cw*(0.75/intensity)) && dist>(ent-radius) ) {
                                    merk=st.tr[indexOfCroptree].neighbor[i];
                                    dist=ent-radius;
                                }
                            }
// if merk > 9 then cut tree else stop crop tree release                    
                     if (merk == -9) {doNotEndThinning = false;}
                     else {
                          st.tr[merk].out = st.year; 
                          st.tr[merk].outtype = 2; 
                          thinned = thinned + (st.tr[merk].fac*st.tr[merk].v);
                          maxBasalAreaOut = maxBasalAreaOut - (st.tr[merk].fac*Math.PI*Math.pow(st.tr[merk].d/200.0,2.0))/st.size;
                          if (maxBasalAreaOut <= 0.0) doNotEndThinning = false; 
                     }
                }

            }
            //stop if max thinning amount is reached or all competitors are taken out
            while (thinned < vmaxthinning && doNotEndThinning); 
            }
        }
       // System.out.println("");
    } 

        public void thinCompetitionFromAbove(Stand st){
        
        //set max thinning volume (vmaxthinning) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        vmaxthinning=st.size*st.trule.maxThinningVolume-thinned;
        
        
        //reduce max thinning if max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        // minus outaken amount (vout) is less than set max thinning volume (vmaxthinning)
        if ((st.size*st.trule.maxOutVolume-vout)<vmaxthinning){
            vmaxthinning=st.size*st.trule.maxOutVolume-vout;
        }

                
        if (vmaxthinning>0){
        // Thinning is done iteratively tree by tree
        // 1. Calculate the overlap of all crop trees
        // 2. Calculate tolerable overlap of crop tree according to Spellmann et al,
        //    Heidi Doebbeler and crown width functions
        // 3. Find tree with the highest differenz in overlap - tolerable overlap
        // 4. Remove for the crop tree of 3.) the tree with the greates overlap area
        // 5. Start with 1. again 
            
//
//      Festlegen der Grundflächenansenkung   
           double maxStandBasalArea =0.0; 
           for ( int i=0; i < st.nspecies; i++){
              Tree atree = new Tree();
              atree.sp=st.sp[i];
              atree.st=st;
              atree.d =st.sp[i].d100;
              atree.h =st.sp[i].h100;
              atree.age =(int) Math.round(st.sp[i].h100age);
              atree.code=st.sp[i].code;
              atree.cw=atree.calculateCw();
              maxStandBasalArea = maxStandBasalArea+ atree.calculateMaxBasalArea()*atree.getModerateThinningFactor()*
                      (st.sp[i].percCSA/100.0);
           }
           if (st.trule.thinningIntensity == 0.0 ) maxStandBasalArea = maxStandBasalArea* 100.0;
              else  maxStandBasalArea = maxStandBasalArea*(2.0-st.trule.thinningIntensity);
           double maxBasalAreaOut = st.bha - maxStandBasalArea;
           
            
            
            
            boolean doNotEndThinning = true;   
            if (maxBasalAreaOut <= 0.0) doNotEndThinning = false; 
            else{
            do {
// find non crop tree with most competition to other trees, defined as that the maximum overlap area for neighbor trees
                int indextree =-9;
                double maxOverlap = -99999.9;
                for ( int i=0; i < st.ntrees; i++)
                    if (st.tr[i].d > 7 && st.tr[i].out < 0 && st.tr[i].crop==false && st.tr[i].tempcrop==false && st.tr[i].habitat==false){
                        double ovlp = 0.0;
                        for ( int j=0; j < st.tr[i].nNeighbor; j++){
                            double distance = Math.sqrt(Math.pow(st.tr[i].x-st.tr[st.tr[i].neighbor[j]].x,2.0)+
                                    Math.pow(st.tr[i].y-st.tr[st.tr[i].neighbor[j]].y,2.0));
                            double ri = st.tr[i].cw/2.0;
                            double rj = st.tr[st.tr[i].neighbor[j]].cw/2.0;
// only if there is an overlap and ri > rj                           
                            if (ri+rj > distance && ri > rj){
                                ovlp = ovlp+ overlap(rj,ri,distance);
                            } 
                                
                        }
                        if (ovlp > maxOverlap){
                            maxOverlap=ovlp;
                            indextree = i;
                        }
                       }
                    
// release the crop tree with indexOfCropTree and take out neighbor, which comes closest with the
// crown to the crop tree's crown at height crown base. Neighbors are taken out only if they come
// into the limit of twice the crown radius of the crop tree size 
//                 
// if merk > 9 then cut tree else stop crop tree release                    
                     if (indextree == -9) {doNotEndThinning = false;}
                     else {
                          st.tr[indextree].out = st.year; 
                          st.tr[indextree].outtype = 2; 
                          thinned = thinned + (st.tr[indextree].fac*st.tr[indextree].v);
                          maxBasalAreaOut = maxBasalAreaOut - (st.tr[indextree].fac*Math.PI*Math.pow(st.tr[indextree].d/200.0,2.0))/st.size;
                          if (maxBasalAreaOut <= 0.0) doNotEndThinning = false; 
                     }

            }
            //stop if max thinning amount is reached or all competitors are taken out
            while (thinned < vmaxthinning && doNotEndThinning);
            }
        }
       // System.out.println("");
    } 
    
    public void thinFromBelow(Stand st){
        
        //Max. Harvestvolume is defined
        //set max thinning volume (vmaxthinning) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        vmaxthinning=st.size*st.trule.maxThinningVolume-thinned;
        
        
        //reduce max thinning if max allowed amount for stand (st.size*st.trule.maxThinningVolume)
        // minus outaken amount (vout) is less than set max thinning volume (vmaxthinning)
        if ((st.size*st.trule.maxOutVolume-vout)<vmaxthinning){
            vmaxthinning=st.size*st.trule.maxOutVolume-vout;
        }        

        
        if (vmaxthinning>0){
        // calculate max. density
           double baToTakeOut =0.0; 
             double maxG = 0.0;
             for ( int i=0; i < st.nspecies; i++){
                 Tree tree = new Tree();
                 tree.sp=st.sp[i];
                 tree.code = st.sp[i].code;
                 tree.h = st.sp[i].hg;
                 tree.d = st.sp[i].dg;
                 tree.cw=tree.calculateCw();
                 double maxBasalArea = tree.calculateMaxBasalArea()*tree.getModerateThinningFactor();
                 if (st.trule.thinningIntensity == 0.0 ) maxBasalArea = maxBasalArea* 100.0;
                   else  maxBasalArea = maxBasalArea*(2.0-st.trule.thinningIntensity);
                 maxG = maxG + maxBasalArea*(st.sp[i].percCSA/100.0);
             }
             baToTakeOut = st.bha - maxG;
            
            
            double baout = 0.0;
            boolean doNotEndThinning = true;   
            do {
// update competition overlap for crop trees 
                double dmin = 99999.9;
                int merk = -9;
                for ( int i=0; i < st.ntrees; i++)
                    if (st.tr[i].out < 0 && dmin > st.tr[i].d && st.tr[i].habitat==false){
                        dmin = st.tr[i].d;
                        merk = i;
                    }
 // Baum entfernen, sofern eine Überlappung besteht
                    if (merk > -1 && baout < baToTakeOut ){
                           st.tr[merk].out = st.year; 
                           st.tr[merk].outtype = 2; 
                           thinned = thinned + st.tr[merk].fac*st.tr[merk].v;
                           baout = baout + (st.tr[merk].fac*Math.PI*Math.pow((st.tr[merk].d/200.0),2.0))/st.size;
                        }
                        else doNotEndThinning = false;
                
                    if  (baout >= baToTakeOut ) doNotEndThinning= false; 
                 

            }
            //stop if max thinning amount is reached or all competitors are taken out
            while (thinned < vmaxthinning && doNotEndThinning);   
        }
       // System.out.println("");
    } 
    
    /** return total volume per ha taken put during year (harvested, thinned and fallen)
     * @param st stand object
     */
    public double getTotalOutVolume(Stand st){
        double outvolume=0.0;
        for (int i=0;i<st.ntrees;i++)
            //volume sum of outaken (thinned or harvested) and died trees in the current year
           if (st.tr[i].out==st.year){
               outvolume=outvolume+st.tr[i].fac*st.tr[i].v;
           }
       return outvolume/st.size ;
    }
    
    /** return total volume per ha taken put during year (thinned, harvested)
     * @param st stand object
     */
    public double getTreatmentOutVolume(Stand st){
        double volume=0.0;
        for (int i=0;i<st.ntrees;i++)
            //volume sum of outaken (thinned or harvested) trees in the current year            
           if (st.tr[i].out==st.year && st.tr[i].outtype >1){
               volume=volume+st.tr[i].fac*st.tr[i].v;
           }
       return volume/st.size ;
    }       
           
    /** return total volume per ha taken put during year(harvested)
     * @param st stand object
     */
    public double getHarvestedOutVolume(Stand st){
        double volume=0.0;
        for (int i=0;i<st.ntrees;i++)
            //volume sum of harvested trees in the current year               
           if (st.tr[i].out==st.year && st.tr[i].outtype ==3){
               volume=volume+st.tr[i].fac*st.tr[i].v;
           }
       return volume/st.size ;
    }    
    
    /** return total volume per ha taken put during year (thinned)
     * @param st stand object
     */
    public double getThinnedOutVolume(Stand st){
        double volume=0.0;
        for (int i=0;i<st.ntrees;i++)
            //volume sum of thinned trees in the current year               
           if (st.tr[i].out==st.year && st.tr[i].outtype ==2){
               volume=volume+st.tr[i].fac*st.tr[i].v;
           }
       return volume/st.size ;
    }

    /** returns number of crop trees per ha selected
     * @param st treegross.base.stand object
     */
    public double getNCropTrees (Stand st){
        double nCT=0;
        for (int i=0;i<st.ntrees;i++) {            
            if (st.tr[i].crop==true && st.tr[i].out==-1){
                nCT=nCT+1;
            }
        }
        return nCT;
    }   
    
   
    /** unselect all trees taken out during harvesting that year 
     * if min ammount for treatment per ha has not been exceeded
     * @param st treegross.base.stand object
     */
    public void checkMinHarvestVolume (Stand st){
        
        if (this.getHarvestedOutVolume(st)<st.trule.minHarvestVolume){
            for (int i=0;i<st.ntrees;i++) {
                //reset harvested trees in the current year               
               if (st.tr[i].out==st.year && st.tr[i].outtype ==3){
                        st.tr[i].out=-1;
                        st.tr[i].outtype=0;
                }
            }        
        }
    }  
    
    /** unselect all trees taken out during treatment that year 
     * if min ammount for treatment per ha has not been exceeded
     * @param st treegross.base.stand object
     */
    public void checkMinThinningVolume (Stand st){

        if (this.getThinnedOutVolume(st)<st.trule.minThinningVolume){
            for (int i=0;i<st.ntrees;i++) {
                //reset thinned trees in the current year               
               if (st.tr[i].out==st.year && st.tr[i].outtype ==2){                
                        st.tr[i].out=-1;
                        st.tr[i].outtype=0;
                }
            }        
        }
    }     

    /** unselect all trees taken out that year 
     * if min ammount for treatment per ha has not been exceeded
     * @param st stand object
     */
    public void checkMinTreatmentOutVolume (Stand st){
        
        if (this.getTreatmentOutVolume(st)<st.trule.minOutVolume*st.size){
            for (int i=0;i<st.ntrees;i++) {
                //reset (thinned or harvested) trees in the current year            
               if (st.tr[i].out==st.year && st.tr[i].outtype >1){
                        st.tr[i].out=-1;
                        st.tr[i].outtype=0;
                }
            }        
        }
    }    
  
    
    /** sets harvsetigPeriode=0 to start time mesurement for harvesting cycle*/
    public void setStartHarvestingYears(Stand st){
        st.trule.harvestingYears=0;
    }
    
    /** returns year, in which a harvesting action was performed to stand*/
    public void setHarvestingYearsAddStep(Stand st){
        st.trule.harvestingYears=st.trule.harvestingYears+st.trule.treatmentStep;
    }
            
    /** calculate overlap area of two circle only if they overlap */
    private double overlap(double r1, double r2, double e){
        double x,y,f;
        f=0.0; 
        if (r1>r2) {	x=r1; r1=r2; r2=x; }  //r1 should always be the smaller radius

        if (e-(r1+r2)>=0) {	f=0.0; }  //no overlap area =0
        
        // partly or full overlap
        if (e-(r1+r2)<0) {
            if (e+r1<=r2) {
                f=Math.PI*r1*r1; 
            }  
            //full overlap
            else {	
                x=(Math.pow(e,2.0)+Math.pow(r1,2.0)-Math.pow(r2,2.0))/(2.0*e); 
                y=Math.sqrt(Math.pow(r1,2.0)-Math.pow(x,2.0)); 
                f=r1*r1*Math.acos((e*e+r1*r1-r2*r2)/(2*e*r1))+r2*r2*Math.acos((e*e+r2*r2-r1*r1)/(2*e*r2))-e*y; 
            }
        }
        return f; 
    }
    /** create skidtrails */
    public void createSkidtrails(Stand st){
        double xmin = 999.9;
        double xmax = -9990.0;
        for (int i=0;i < st.ncpnt; i++){
            if ( st.cpnt[i].x < xmin ) xmin = st.cpnt[i].x;
            if ( st.cpnt[i].x > xmax ) xmax = st.cpnt[i].x;
        }
        xmin = xmin - st.trule.skidtrailDistance/2.0;
        do {
           xmin = xmin + st.trule.skidtrailDistance;
           double x2 = xmin + st.trule.skidtrailWidth;
           for (int i=0; i < st.ntrees; i++)
               if (st.tr[i].out < 0 && st.tr[i].x > xmin && st.tr[i].x < x2){
                   st.tr[i].out=st.year;
                   if (st.tr[i].d < st.tr[i].sp.spDef.targetDiameter) st.tr[i].outtype=2;
                   else st.tr[i].outtype=3;
               }
 
        } while (xmin < xmax);
       
        
    }

    /** mark trees as habitat trees by diameter */
    public void markTreesAsHabitatTreesByDiameter(Stand st){
        for (int i=0; i < st.ntrees; i++)
               if (st.tr[i].out < 0 && st.tr[i].d >= st.trule.treeProtectedfromBHD){
                   st.tr[i].habitat=true;
               }
    }

    public int numberOfCropTrees(Species sp, double diameter, double percentage){
        Tree atree = new Tree();
        atree.code=sp.code;
        atree.sp=sp;
        atree.d=diameter;
        atree.h=sp.hg;
        double dist_ct=0.0;
        dist_ct=atree.calculateCw();
//Number of crop trees dependent on calcualted distance and actual mixture percent
        return  (int)((10000.0/((Math.PI*Math.pow(dist_ct,2.0))/4))*percentage/100.0);            
    } 
    
    public void removeTargetTreesBySpecies(Stand st, int species, double volume){
        //set max harvest volume (vmaxharvest) if outaken amount (vout) 
        //has not reached max allowed amount for stand (st.size*st.trule.maxHarvestVolume)
        vmaxharvest=st.size*volume;
        // reset crop tress
        resetCropTrees(st);
        // Select all trees of species as crop trees
        for (int i=0;i<st.nspecies;i++){
            if (species==st.sp[i].code){
               st.sp[i].trule.numberCropTreesWanted=1000;
               st.sp[i].trule.targetCrownPercent=99.9;
               st.sp[i].trule.targetDiameter=7.0;
               st.sp[i].trule.minCropTreeHeight=8.0;
            }
            else{
               st.sp[i].trule.numberCropTreesWanted=0;
               st.sp[i].trule.targetCrownPercent=0.1;
               st.sp[i].trule.targetDiameter=200.0;
               st.sp[i].trule.minCropTreeHeight=12.0;
            }
             
        }
        selectNCropTrees(st);
        if (vmaxharvest>0){
            //Sort st.tr by difference targetdiameter -diameter ascending        
            Tree trtemp = new Tree();
            for (int i=0;i<st.ntrees-1;i++){
                for (int j=i+1;j<st.ntrees;j++){
                    if ((st.tr[i].sp.trule.targetDiameter-st.tr[i].d) > (st.tr[j].sp.trule.targetDiameter-st.tr[j].d)){
                        trtemp=st.tr[i];
                        st.tr[i]=st.tr[j];
                        st.tr[j]=trtemp;
                    }
                } 
            }               
       
            /**distance of two trees*/
            double dist_trees=0.0;
            double dist_min=0.0;

            /**height for the cross section of crop tree
            * cb(i)+ (h(i)+cb(i)/3)*/
            double h66_i;
            /** cb(j)+ (h(j)+cb(j)/3)*/

            for (int i=0;i<st.ntrees;i++) {
                //check only crop trees for competing crop trees
                if (st.tr[i].out<1 && st.tr[i].crop==true){
                    for (int j=0;j<st.ntrees;j++){
                        //competitor has to be a crop tree                            
                        if (st.tr[j].out < 0 && st.tr[j].crop==true && st.tr[i].no.equals(st.tr[j].no) == false){
                            //caluclate distance between crop trees                                
                            dist_trees=Math.sqrt(Math.pow(st.tr[i].x-st.tr[j].x,2.0)+Math.pow(st.tr[i].y-st.tr[j].y,2.0));

                            //calculate height of considered trees in 1/3 of the height their crown
                            h66_i=st.tr[i].cb+(st.tr[i].h-st.tr[i].cb)/3;

                            //tree (j) can only be a competitor 
                            // if it is higher than potentially pressed tree (i) in its h66
                            if (h66_i<st.tr[j].h){
                                //potentially pressed tree (i) is higher than tree (j)
                                    //calcualte minimal distance, trees has to have to be competitive
                                    // from crown of higher competitor in its h66(j)
                                    dist_min= (st.tr[i].calculateCwAtHeight(h66_i)+ st.tr[j].calculateCwAtHeight(h66_i))/2;   
                            }
                            // tree is no competitor
                            else dist_min=0.0;  

                            if (harvested < vmaxharvest 
                                    && dist_trees<dist_min
                                    && (st.tr[j].sp.trule.targetDiameter-st.tr[j].d)<=(st.tr[i].sp.trule.targetDiameter-st.tr[i].d)) {
                                st.tr[j].out=st.year;
                                st.tr[j].outtype=3;
                                vout=vout+st.tr[j].fac*st.tr[j].v;
                                harvested=harvested+st.tr[j].fac*st.tr[j].v;  
                            }                          
                        }
                    }
                }
            }   
        }
    }

    public double getDegreeOfCover(int code, Stand st, boolean overstoryOnly){
        double degree = 0.0;
        double ks =0.0;
        double hx =0.0;
        if (overstoryOnly) hx=st.h100*0.4;
        for (int j=0;j < st.ntrees;j++)
            if ((code==st.tr[j].code || code ==0)&& st.tr[j].out < 0 && st.tr[j].h > hx)
                ks = ks + st.tr[j].fac* Math.PI * Math.pow(st.tr[j].cw/2.0,2.0);
        degree = ks /(10000.0*st.size);
        return degree;
    }

    public void startPlanting(Stand st){
     // generate placeholder trees
        String ps = st.trule.plantingString;
        int art = 211;
        while (ps.indexOf(";") > 0){
            int m = ps.indexOf("[");
            art = Integer.parseInt(ps.substring(0, m).trim());
            ps = ps.substring(m+1, ps.length());
            m = ps.indexOf("]");
            double ha = Double.parseDouble(ps.substring(0, m).trim());
            ps = ps.substring(m+1, ps.length());
            m = ps.indexOf(";");
            ps = ps.substring(m+1, ps.length());
//  site index aus genutzten Bäumen und auch hbon ermitteln
            double site = 25.0;
            if (art > 200 && art < 400) site=28.0;
            if (art > 200 && art < 300) site=28.0;
            if (art > 500 && art < 600) site=31.0;
            if (art > 600 && art < 700) site=40.0;
            if (art > 700 && art < 800) site=25.0;
            if (art > 800 && art < 900) site=31.0;
            for (int j=0;j < st.ntrees;j++)
                if (art==st.tr[j].code && site < st.tr[j].si) site=st.tr[j].si;
// degree of coverage of that species
            double spcov = getDegreeOfCover( art, st, false);
// number missing of placeholders
            int npl=0;
            if (spcov < ha) npl = (int) Math.round(10000.0*st.size*(ha-spcov)/5.0);  //5 Square Meter paceholders
//        System.out.println("auto Pflanzen: art anzahl "+art+npl);
// create trees
            for (int j=0;j < npl;j++){
                String no = "p"+st.ntrees;
                try {
                  st.addTreeFromNaturalIngrowth(art,no,5,-1,0.25,0.5,0.1,2.52,site,-9.0,-9.0,0,0,0,0);  //first is site index
                } catch(Exception e){ System.out.println(e);}
            }
            for (int i=0;i<st.ntrees;i++) st.tr[i].setMissingData();
            GenerateXY xy = new GenerateXY();
            xy.zufall(st);
      
          }
    }

     
}