/*
 * Layer.java
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
 * sort trees into layers by given definitions
 */
public class Layer
{
    

    /* count number of layer
     * first layer have to have at least 1000m²/ha crownsurfacearea
     * second layer has to hava at least ?m²/ha crownsurfacearea
     * third layer has to hava at least ?m²/ha crownsurfacearea
     */
    public int getNLayer(Stand st){
        int n_layer=0;         
        double sumcsa1=0;
        double sumcsa2=0;
        double sumcsa3=0;        
        
        int flag1=0;
        int flag2=0;
        int flag3=0;
        
        for (int i=0;i<st.ntrees;i++){
            if (st.tr[i].layer==1) {
                sumcsa1=sumcsa1+getFromTree(st.tr[i]);
            }
            if (st.tr[i].layer==2) {
                sumcsa2=sumcsa2+getFromTree(st.tr[i]);                 
            }
            if (st.tr[i].layer==3) {
                sumcsa3=sumcsa3+getFromTree(st.tr[i]);            
            }
        }
        
        if (sumcsa1/st.size>1000) flag1=1;
        if (sumcsa3/st.size>1000) flag3=1;  
        if (sumcsa2/st.size>1000) flag2=1;

        n_layer=flag1+flag2+flag3;
        
        return n_layer;

    }
    
    /* find leading layer by crown surface area
     */
    public int getLeadingLayer(Stand st){
        int l_layer=0;         
        double sumcsa1=0;
        double sumcsa2=0;
        double sumcsa3=0;        
        

        
        for (int i=0;i<st.ntrees;i++){
            if (st.tr[i].layer==1) {
                sumcsa1=sumcsa1+getFromTree(st.tr[i]);
            }
            if (st.tr[i].layer==2) {
                sumcsa2=sumcsa2+getFromTree(st.tr[i]);                 
            }
            if (st.tr[i].layer==3) {
                sumcsa3=sumcsa3+getFromTree(st.tr[i]);            
            }
        }
        
        if (sumcsa1>=sumcsa2 && sumcsa1>=sumcsa3 && sumcsa1!=0) l_layer=1;
        if (sumcsa2>sumcsa1 && sumcsa2>=sumcsa3 && sumcsa2!=0) l_layer=2;
        if (sumcsa3>sumcsa1 && sumcsa3>sumcsa2) l_layer=3;
      
        

        return l_layer;
    }   
    
    /* find leading layer by crown surface area for one species
     */
    public int getLeadingLayerSpecies(Stand st, int species){
        int l_layer=0;         
        double sumcsa1=0;
        double sumcsa2=0;
        double sumcsa3=0;        
        

        
        for (int i=0;i<st.ntrees;i++){
            if (st.tr[i].layer==1 && st.tr[i].code==species) {
                sumcsa1=sumcsa1+getFromTree(st.tr[i]);
            }
            if (st.tr[i].layer==2 && st.tr[i].code==species) {
                sumcsa2=sumcsa2+getFromTree(st.tr[i]);                 
            }
            if (st.tr[i].layer==3 && st.tr[i].code==species) {
                sumcsa3=sumcsa3+getFromTree(st.tr[i]);            
            }
        }
        
        if (sumcsa1>=sumcsa2 && sumcsa1>=sumcsa3 && sumcsa1!=0) l_layer=1;
        if (sumcsa2>sumcsa1 && sumcsa2>=sumcsa3 && sumcsa2!=0) l_layer=2;
        if (sumcsa3>sumcsa1 && sumcsa3>sumcsa2) l_layer=3;
      
        

        return l_layer;
    }    
    
     /* Number of trees in layer per ha
     */
    public int getNTreesLayerHa(Stand st, int layer){
        int ntreeslayer=0;
        

        
        for (int i=0;i<st.ntrees;i++){
            if (st.tr[i].layer==layer && st.tr[i].out <0) {
                ntreeslayer=ntreeslayer+1;
            }
        } 
        
        if (st.size>0){
            ntreeslayer=(int) (ntreeslayer/st.size);
        }
        else ntreeslayer=0;

        return ntreeslayer;
    }     
    
     /* Basal area (m2) of trees in layer per ha
     */
    public double getBALayerHa(Stand st, int layer){
        double  balayer=0;
        double batree=0;
        

        
        for (int i=0;i<st.ntrees;i++){
            if (st.tr[i].layer==layer && st.tr[i].out <0 && st.tr[i].d>0) {
                batree=Math.PI/4*Math.pow(st.tr[i].d,2);
                balayer=balayer+batree;
            }
        } 

        if (st.size>0){
            balayer=(int) (balayer/st.size)/10000;
        }
        else balayer=0;
        
        return balayer;
    }         
    
    public double getAverageAgeLayer(Stand st, int layer){
        double ageLayer=0;
        double sumAge=0;
        int n=0;

        
        for (int j=0; j<st.ntrees; j++){
            if (st.tr[j].out <0){
                if(st.tr[j].layer==layer){
                    sumAge=sumAge+st.tr[j].age;
                    n=n+1;                    
                }         
            }
        }
        
        if (n!=0){ageLayer=sumAge/n;} else ageLayer=0;
        return ageLayer;
    }
    
    public double getAverageHeightLayer(Stand st, int layer){
        double heightLayer=0;
        double sumHeight=0;
        int n=0;

        
        for (int j=0; j<st.ntrees; j++){
            if (st.tr[j].out <0){
                if(st.tr[j].layer==layer){
                    sumHeight=sumHeight+st.tr[j].h;
                    n=n+1;                    
                }         
            }
        }
        
        if (n!=0){heightLayer=sumHeight/n;} else heightLayer=0;
        return heightLayer;
    }    
    
    public double getAverageDBHLayer(Stand st, int layer){
        double dbhLayer=0;
        double sumDBH=0;
        int n=0;

        
        for (int j=0; j<st.ntrees; j++){
            if (st.tr[j].out <0){
                if(st.tr[j].layer==layer){
                    sumDBH=sumDBH+st.tr[j].d;
                    n=n+1;                    
                }         
            }
        }
        
        if (n!=0){dbhLayer=sumDBH/n;} else dbhLayer=0;
        return dbhLayer;
    }    
    
    public double getAverageDBHLayer(Stand st, int layer, int species){
        double dbhLayer=0;
        double sumDBH=0;
        int n=0;

        
        for (int j=0; j<st.ntrees; j++){
            if (st.tr[j].out <0){
                if(st.tr[j].layer==layer && st.tr[j].sp.code==species){
                    sumDBH=sumDBH+st.tr[j].d;
                    n=n+1;                    
                }         
            }
        }
        
        if (n!=0){dbhLayer=sumDBH/n;} else dbhLayer=0;
        return dbhLayer;
    }    
    /* crown surface area per ha in layer x
     @param st: stand object
     @param layer: return crown surface area of this layer*/
    public double getCSLayerHa(Stand st, int layer){
        double csLayer=0;
        int n=0;

        
        for (int j=0; j<st.ntrees; j++){
            if (st.tr[j].out <0){
                if(st.tr[j].layer==layer){
                    csLayer=csLayer+getFromTree(st.tr[j]);                    
                }         
            }
        }
        csLayer=csLayer/st.size;
        return csLayer;
    }        
    public double getAverageDBHLayerSpecies(Stand st, int layer, int code){
        double dbhLayer=0;
        double sumDBH=0;
        int n=0;

        
        for (int j=0; j<st.ntrees; j++){
            if (st.tr[j].out <0){
                if(st.tr[j].layer==layer && st.tr[j].code==code){
                    sumDBH=sumDBH+st.tr[j].d;
                    n=n+1;                    
                }         
            }
        }
        
        if (n!=0){dbhLayer=sumDBH/n;} else dbhLayer=0;
        return dbhLayer;
    }       
    
    /** returns species of layer messued by percent of basal area of stand
     * returns sp.code=0 if there is no species found in leading layer
     */
    public Species getLeadingSpeciesBALayer(Stand st, int layer){
        Species leadingSp= new Species();
        leadingSp.code=0;
        double sumBA=0;
        double remBA=0;
        //if there are trees in stand
        if (st.ntrees>0){
            for (int i=0; i<st.nspecies;i++){
                //reset basal area to 0
                sumBA=0;
                for (int j=0; j<st.ntrees; j++){
                    if (st.tr[j].out <0){
                        //sum of basal area of trees of that species in that layer
                        if(st.tr[j].code==st.sp[i].code && st.tr[j].layer==layer){
                            sumBA=sumBA+Math.PI * Math.pow((st.tr[j].d/200.0),2.0);                
                        }         
                    }
                } 
                //resmeber species with heighest basal area
                if (sumBA>remBA){
                    leadingSp=st.sp[i];
                    remBA=sumBA;
                }
            }
        }


        return leadingSp;
    }     
    public double getFromStand(Stand st){
        double sumCSA=0.0;
        //sum crown percentage over all trees
        for (int i=0;i<st.ntrees;i++){
            if (st.tr[i].out<0){
                sumCSA=sumCSA+st.tr[i].fac*Math.PI*Math.pow(st.tr[i].cw/2.0,2.0);
            }
        }
        
        sumCSA=sumCSA;
        
        return sumCSA;
    }
    
    /** calculates crown surface area
     * @param st treegross.base.stand object
     */
    public double getFromTree(Tree tr){
        double sumCSA=0.0;
        //sum crown percentage over all trees
        sumCSA=sumCSA+tr.fac*Math.PI*Math.pow(tr.cw/2.0,2.0);
        
        return sumCSA;
    }    
      
}