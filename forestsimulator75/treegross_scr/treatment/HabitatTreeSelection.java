/* 
 *  HabitatTreeSelection.java
 *
 *
 *  (c) 2002-2008 Juergen Nagel, Northwest Forest Research Station, 
 *      Gr�tzelstr.2, 37079 G�ttingen, Germany
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
import java.util.*;
/**
 * @author	Henriette Duda 
 * for more information see:
 * Duda, H. (2006): Vergleich forstlicher Managementstrategien. 
 * Dissertation Universit�t G�ttingen, S. 180 
 * http://webdoc.sub.gwdg.de/diss/2006/duda/ 
 * 
 * method select defined number habitat trees per ha
 * select randomly from 100 heighest trees per ha if height>=height of croptrees or age>=max age
 * it expects a stand and number of habitattrees per ha
 * @param st stand object
 */

public class HabitatTreeSelection {
               

    public void selectHabitatTrees(Stand st){
        
  
        
        /** remember number of tree that meet the criteria of a habitat tree*/
        int criteriatreenumber[]= new int[(int)(100*st.size)]; 
        /** number of trees that meet the criteria of a habitat tree*/
        int ntcriteria=0;


        /** number of selected habitat trees*/
        int nselectedhabitattrees;
        /** flag to sgnalize the end of selection.
         * Reason for end of selection:
         * 1. enough trees are selected
         * 2. no further tree can be found*/
        boolean endselection;
        /** minimum distance from habitat tree to a crop tree
         * dependent on expected crownwidth of this species when reaching species TargetDBH*/
        double mindistanceallowed=0;
        /** distance between considerd habitat tree to considered crop tree*/
        double distance=0;
        /** minimal distance found from habitat to a crop tree*/
        double mindistance=99999999;
        /** store random number*/
        double z;
        
        // random number 
        Random r = new Random();
        //remember marked trees
        BitSet b = new BitSet();
        
        if (this.getNHabitatTrees(st)<this.getTargetNHabitat(st)){
        
        // 1. Sort st.tr by height descending        
        Tree trtemp = new Tree();
        
        for (int n=0;n<st.ntrees-1;n++){
            for (int m=n+1;m<st.ntrees;m++){
                if (st.tr[n].h < st.tr[m].h){
                    trtemp=st.tr[n];
                    st.tr[n]=st.tr[m];
                    st.tr[m]=trtemp;
                }
            }  
        }
        
        
        // save treenumber of trees that meet criteria of a habitat tree
        // starting with the highest trees (see sorting above)
        int habspecies = 1000;
        if (st.trule.habitatTreeType==0)habspecies=500;
        if (st.trule.habitatTreeType==1)habspecies=300;
        for (int n=0;n<st.ntrees;n++) {
            // criteria:
            // tree height > minCropTreeHeight
            // max 100 trees per ha        
            // standing (outtype==0)
            // no habitat tree yet (habitat==false)
            // no crop tree yet (crop==false)
            // distance to crop trees in in range (see "for"-loop)
            if (st.tr[n].h>st.tr[n].sp.trule.minCropTreeHeight && ntcriteria<(int)(100*st.size) && st.tr[n].outtype==0 && st.tr[n].habitat==false && 
                    st.tr[n].crop==false && st.tr[n].code < habspecies ){

                //reset mindistance
                mindistance=99999999;
                // check if distance is in range of allowed distance or no tree selected so far
                // minimal allowed distance from habitat tree to crop tree= crownwidth of crop tree with targetdiameter                
                for (int ii=0;ii<st.ntrees;ii++) {                                  
                    if (st.tr[ii].crop==true && st.tr[ii].out<0){
                                        
                        //caluculate actual distance from considered tree to crop trees
                        distance=Math.sqrt(Math.pow(st.tr[n].x-st.tr[ii].x,2.0)+ Math.pow(st.tr[n].y-st.tr[ii].y,2.0));
                        
                        // find closesed crop tree
                        // remember minmal distance this crop tree needs
                        if (distance<mindistance){
                            //calculate mindistanceallowed dependent on crownwidth (dependent on species and TargetDBH)
                            Tree atree = new Tree();
                            atree.code=st.tr[ii].code;
                            atree.sp=st.tr[ii].sp;
                            atree.d=st.tr[ii].sp.trule.targetDiameter;
                            mindistanceallowed=atree.calculateCw();   
                            
                            mindistance=distance;                     
                        }
                    }
                }
                
                // remember number of tree taht meet criteria
                // count number of possible trees  
                if (mindistance>mindistanceallowed){
                    criteriatreenumber[ntcriteria]=n;
                    ntcriteria=ntcriteria+1;
                }
            }
     
        }

        endselection=false;
        
                 
      
        // if there are trees that meet criteria
        if (ntcriteria>0){       
            //this loop runs until endselection=true; 
            //enough trees are selected or no further tree can be found               
            do {
                // number of already selected crop trees of species
                nselectedhabitattrees=getNHabitatTrees(st);

                    if (nselectedhabitattrees>=this.getTargetNHabitat(st) || nselectedhabitattrees==ntcriteria) endselection=true;
                else endselection=false;

                // select another tree: random of 100 heighest trees per ha
                if (endselection==false){
                    int i=Math.abs(r.nextInt()) % ntcriteria;
                    
                    //so be sure, that a number is chosen only once
                    if (!b.get(i)){
                        st.tr[criteriatreenumber[i]].habitat=true;
                        b.set(i);                        
                    }
                }
            }
            while (endselection==false);
        }
        }


    }
    
    
    /** get number of marked habitat trees in stand
     * @param st stand object
     */                        
   public int getNHabitatTrees(Stand st) {
        
       int n=0;
       for (int i=0;i<st.ntrees;i++) {
            if (st.tr[i].outtype==0 && st.tr[i].habitat==true)
                n=n+1;
        } 
        return n;
    }                     
    
   public int getTargetNHabitat(Stand st){
       int targetNHabitatStand=0;
       targetNHabitatStand=(int)(Math.floor(st.trule.nHabitat*st.size));   
        
       if (st.trule.selectHabiatPart==true) targetNHabitatStand=targetNHabitatStand+1;       
       else {
           double hab = st.trule.nHabitat*st.size;
           if (hab > 0.0 && hab < 1.0 && Math.random() < hab ) targetNHabitatStand=1;  
           if (hab >= 1.0) targetNHabitatStand=(int)Math.round(hab);
       }
       return targetNHabitatStand;
   }
    
   
    /** get volume of habitat trees in stand
     * @param st stand object
     */                        
   public double getVolHabitatTrees(Stand st) {
        
       double fm=0;
       for (int i=0;i<st.ntrees;i++) {
            if (st.tr[i].outtype==0 && st.tr[i].habitat==true)
                fm=fm+st.tr[i].v;
        } 
        return fm;
    }  
   
          
   
    public boolean selectHabitatPartByStandSize(Stand st){
        boolean selHabitatPart=false;
        
        int floorStandSize=0;
        double part;           
        double randomNumber=Math.random();
        
        floorStandSize=(int)(Math.floor(st.size));
        
        /** remember decimal places*/
        part=(st.size-floorStandSize);        

        if (randomNumber<=part){
            selHabitatPart=true;
        }
                    
        return selHabitatPart;
    }   
}
