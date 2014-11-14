/* 
 * @(#) mortality.java  
 *  (c) 2002 -2007 Juergen Nagel, Northwest German Forest Research Station , 
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
 */
package treegross.base;

// import treegross.auxiliaries.*;
/** Mortality.java calculates the mortaliy for a given influence zone.  It
    expects a class stand and processes all living trees in that stand. 
    The specific values for the critical C66 index were derived by the
    growth and yield data of the NW-FVA for Northwest Germany. It is supposed
    that a tree can not live anymore if the calculated c66 value is greater
    than the critical value. The critical values are set in this method directly.
 *  The mortality class can be exchanged for different regions via the PlugInMortality class.
*/
class Mortality implements PlugInMortality{
 
	public void mortalityByInfluenceZone(Stand st)
	{	
/* Order all internal tree numbers of st.tr[] to an array treeNo[] by c66xy, where the
 * c66xy is higher than the critical value
 */
      int treeNo[] = new int[st.maxStandTrees];
      int numberOfCandidates = 0;
// Set critical crownclosure for XML from maxDensity for dg,hg
      for (int i=0; i< st.nspecies;i++){
                Tree atree = new Tree();
                atree.sp = st.sp[i];
                atree.st = st;
                atree.code = st.sp[i].code;
                atree.d = st.sp[i].dg;
                if (atree.d < 7.0) atree.d=7.0;
                atree.h = st.sp[i].hg;
                if (atree.h < 7.0) atree.h=7.0;
                atree.cw = atree.calculateCw();
                atree.cb = atree.calculateCb();
                double maxBa=atree.calculateMaxBasalArea();
                double maxNha = maxBa/(Math.PI*Math.pow(atree.d/200.0,2.0));
                st.sp[i].spDef.criticalCrownClosure=(maxNha * Math.PI*Math.pow((atree.calculateCw()/2.0),2.0))/10000.0;
       } 
          
      
// eliminate iteratively all trees with critical crown closure
       do {
          for (int i=1; i<numberOfCandidates; i++) st.tr[treeNo[i]].updateCompetition();
          numberOfCandidates = 0;
          for (int i=0;i<st.ntrees;i++){
              if (st.tr[i].out < 0 && st.tr[i].c66xy > st.tr[i].sp.spDef.criticalCrownClosure){
                  treeNo[numberOfCandidates]=i;
                  numberOfCandidates=numberOfCandidates+1;
              }
          }
// sort by c66xy
          for (int i=0;i<numberOfCandidates-1;i++){
              for (int j=i+1;j<numberOfCandidates;j++){
                  if ((st.tr[treeNo[i]].c66xy-st.tr[treeNo[i]].sp.spDef.criticalCrownClosure) <
                      (st.tr[treeNo[j]].c66xy-st.tr[treeNo[j]].sp.spDef.criticalCrownClosure) ){
                          int merk = i;
                          treeNo[i]=treeNo[j];
                          treeNo[j]= merk;
                  }
              }
          }
// take first tree out
          if (numberOfCandidates > 0) {
               st.tr[treeNo[0]].out=st.year; 
               st.tr[treeNo[0]].outtype=1; 
          }
         
//
       } while (numberOfCandidates > 0); 
}
          

}
