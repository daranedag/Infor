/*
 * GenerateMissingHeights.java
 *
 * Created on 17. August 2005, 07:48
 */

package vis2006;
import treegross.base.*;

/**
 *
 * @author  J. Nagel
 */
public class GenerateMissingHeights {
    
    /** Creates a new instance of GenerateMissingHeights */
    public GenerateMissingHeights() {
    }
    public void replaceMissingHeights(Stand st,boolean alleWerte) {
                // calculate diameter-height curve
// alle Baumarten abarbeiten  
// Bäumes des ausscheidenden Bestands werden berücksichtigt        
        for (int i=0;i<st.nspecies;i++){
            int ndh = 0; // number of diameter and height values
// set heightcurve parameter =-99.9
            st.sp[i].heightcurveUsedP0=-99.9;
            st.sp[i].heightcurveUsedP1=-99.9;

            for (int j=0;j<st.ntrees;j++) 
                if ( st.tr[j].code==st.sp[i].code && st.tr[j].hMeasuredValue >1.3) ndh=ndh+1;
//
            if (alleWerte != true){
                  for (int j=0;j<st.ntrees;j++) 
                   if ( st.tr[j].code==st.sp[i].code && st.tr[j].hMeasuredValue >1.3) 
                         st.tr[j].h=st.tr[j].hMeasuredValue;
            }
            // int missingheights=st.getMissingHeight(st.sp[i].code);

//Number of diameter-height values > 5 then height curve
            if (ndh>5) {
                int k=ndh/1000; //if there are more than 1000 ndh then use only every k th tree
                k=k+1; 
                ndh=0;
                for (int j=0;j<st.ntrees;j=j+k)
                     if (st.tr[j].code==st.sp[i].code && st.tr[j].hMeasuredValue >1.3)ndh=ndh+1; 
                HeightCurve m =new HeightCurve(); 
                m.heightcurve();
                for (int j=0;j<st.ntrees;j=j+k)
                    if (st.tr[j].code==st.sp[i].code && st.tr[j].hMeasuredValue >1.3 ){
                      m.adddh(st.tr[j].sp.spDef.heightCurve,ndh,st.tr[j].d,st.tr[j].hMeasuredValue);
                    }
                m.start();
            
                st.sp[i].heightcurveUsed=m.getHeightCurveName(st.sp[i].spDef.heightCurve);
                st.sp[i].heightcurveUsedP0=m.getb0();
                st.sp[i].heightcurveUsedP1=m.getb1();
                st.sp[i].heightcurveUsedP2=m.getb2();
                if (st.sp[i].spDef.heightCurve==2) st.sp[i].heightcurveUsedP2=3.0;

                for (int j=0;j<st.ntrees;j++)
                    if (st.tr[j].code==st.sp[i].code && st.tr[j].h <1.3) {
                          st.tr[j].h=m.hwert(st.sp[i].spDef.heightCurve,st.tr[j].d);
                    }
            }

// Alternative if there are only 1 to 4 heights
// Use uniform height curve 
            double dk=0.0; double hk=0.0;
            if (ndh >0 && ndh <=5 ) { 
               for (int j=0;j<st.ntrees;j++)
                   if (st.tr[j].code==st.sp[i].code && st.tr[j].hMeasuredValue >1.3 ){
                       if (st.tr[j].d > dk){  //nur der dickste Baum als Eingangsgröße für die Einheitshöhenkurve
                          dk=st.tr[j].d;
                          hk=st.tr[j].hMeasuredValue;
                      }
                  }
               FunctionInterpreter fi = new FunctionInterpreter(); 
               st.sp[i].heightcurveUsed="Einheitshöhenkurve: ";
               st.sp[i].heightcurveUsedP0=dk;
               st.sp[i].heightcurveUsedP1=hk;
               double dgmerk = st.sp[i].dg; 
               double hgmerk = st.sp[i].hg;
               for (int j=0;j<st.ntrees;j++){
                    if (st.tr[j].code==st.sp[i].code && st.tr[j].h <1.3) {
                        //st.tr[j].h=ufh.height(st.tr[j].sp,st.tr[j].d,dk,hk,st);
                        Tree tree = new Tree();
                        tree.d = dk;
                        tree.sp = st.sp[i];
                        tree.sp.dg=dk;
                        tree.sp.hg=hk;
                        st.tr[j].h=fi.getValueForTree(st.tr[j],st.tr[j].sp.spDef.uniformHeightCurveXML);
                    }
               }
               st.sp[i].dg = dgmerk;
               st.sp[i].hg = hgmerk;
            }
        } // Ende der Schleife über alle Baumarten
       
// If one species has got no height curve, choose the one of the closed other species with heigth curve
        boolean HKurveVorhanden=false;
        boolean HKfehlt=false;
        for (int i=0; i< st.nspecies;i++){
            if (st.sp[i].heightcurveUsedP0 != -99.9) HKurveVorhanden = true;
            else HKfehlt = true;
        }
        if (HKurveVorhanden == true && HKfehlt == true){
            for (int i=0; i< st.nspecies;i++)
                  if (st.sp[i].heightcurveUsedP0 == -99.9) {
                      int speciesDistance=99999;
                      int spi = st.sp[i].code;
                      if (spi > 500) spi=spi+1000;
                      int merkart=0;
                      for (int j=0; j< st.nspecies;j++){
                          int spj = st.sp[j].code;
                          if (spj > 500) spj=spj+1000;
                          if (spj != spi && speciesDistance > Math.abs(spi-spj)){
                              speciesDistance=Math.abs(spi-spj);
                              //st.sp[i].heightcurveUsedP0=st.sp[j].heightcurveUsedP0;                               
                              //st.sp[i].heightcurveUsedP0=st.sp[j].heightcurveUsedP0;                               
                              //st.sp[i].heightcurveUsedP1=st.sp[j].heightcurveUsedP1;                               
                              //st.sp[i].heightcurveUsed=st.sp[j].heightcurveUsed; 
                              //st.sp[i].spDef.heightCurve=st.sp[j].spDef.heightCurve;
                              merkart = j;
                          }
                      }
                      double merkdg = st.sp[merkart].dg;
                      double merkhg = st.sp[merkart].hg;
                      double dk = 0.0;
                      double hk = 0.0;
                      for(int j = 0; j < st.ntrees; j++){
                          if (st.tr[j].d >= 7 && st.tr[j].h > 1.3 && st.tr[j].out < 1) {
                              if (st.tr[j].d > dk) {
                                  dk = st.tr[j].d;
                                  hk = st.tr[j].h;
                              }
                          }
                      } 
                      for (int j = 0; j < st.ntrees; j++) {
                          Tree tree = new Tree();
                          tree.sp = st.sp[merkart];
                          tree.sp.dg=dk;
                          tree.sp.hg=hk;
                          if (st.tr[j].code == st.sp[i].code && st.tr[j].h < 1.3) {
                              
                              tree.d = st.tr[j].d;
                              
                              FunctionInterpreter fi = new FunctionInterpreter();
                              st.tr[j].h = fi.getValueForTree(tree, tree.sp.spDef.uniformHeightCurveXML);
                              //st.tr[j].h = 10.0;
                          }

                      }
                      st.sp[merkart].dg = merkdg;
                      st.sp[merkart].hg = merkhg;
 
                  }
                
            
        } // Ende: Höhenwerte wenn eine Art keine Höhe hat
    
    }
}
