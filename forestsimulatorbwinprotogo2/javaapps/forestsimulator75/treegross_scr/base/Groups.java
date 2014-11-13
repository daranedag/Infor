/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package treegross.base;

/**
 * This class allows to analyse a stand by groups
 * Each tree is assigned to a group value in tr.group
 *
 * @author nagel
 */
public class Groups {
     Stand st = null;
     public Groups(Stand stx){
         st=stx;
     }
     public int getNumberOfGroups(){
         int ngr =-9;
         for (int i=0; i < st.ntrees; i++)
             if (ngr < st.tr[i].group) ngr=st.tr[i].group;
         return ngr;
     }
     public double getGha(int gr){
        double bha=0.0;
        for (int i=0;i<st.ntrees;i++)
            if (st.tr[i].group == gr && st.tr[i].d >=7.0 && st.tr[i].out<1 ){
                bha = bha+st.tr[i].fac*Math.PI*(st.tr[i].d/200.0)*(st.tr[i].d/200.0);
            }
        bha=bha/st.size;
        return bha;
     }
     public double getNha(int gr){
        double nha=0.0;
        for (int i=0;i<st.ntrees;i++)
            if (st.tr[i].group == gr && st.tr[i].d >=7.0 && st.tr[i].out<1 ){
                nha = nha+st.tr[i].fac;
            }
        nha=nha/st.size;
        return nha;
     }
     public double getVha(int gr){
        double vha=0.0;
        for (int i=0;i<st.ntrees;i++)
            if (st.tr[i].group == gr && st.tr[i].d >=7.0 && st.tr[i].out<1 ){
                vha = vha+st.tr[i].fac*st.tr[i].v;
            }
        vha=vha/st.size;
        return vha;
     }
     public double getDg(int gr){
        double dg=0.0;
        double nha = getNha(gr);
        if (nha > 0.0) dg = 200.0*Math.sqrt((getGha(gr)/getNha(gr))/Math.PI);
        return dg;
     }
     public int getSpeciesCode(int gr){
        int code=0;
        for (int i=0;i<st.ntrees;i++)
            if (st.tr[i].group == gr  ){
                code = st.tr[i].code;
            }
         return code;
     }
     public double getHg(int gr){
        double hg=0.0;
        double dg=getDg(gr);
        int code = getSpeciesCode(gr);
        int merk = 0;
        for (int i=0;i < st.nspecies;i++)
            if (st.sp[i].code==code) merk=i;
        HeightCurve hc =new HeightCurve();
        if (dg > 0) {
            int ncurve = Integer.parseInt(st.sp[merk].heightcurveUsed.substring(0, 1));
            hg= hc.getHeight(ncurve, dg, st.sp[merk].heightcurveUsedP0,st.sp[merk].heightcurveUsedP1,st.sp[merk].heightcurveUsedP2);
        }
        return hg;
     }
     public double getAge(int gr){
        double age=0.0;
        double n=0;
        for (int i=0;i<st.ntrees;i++)
            if (st.tr[i].group == gr && st.tr[i].d >=7.0 && st.tr[i].out<1 ){
                age = age+st.tr[i].age;
                n = n+1;
            }
        age=age/n;
        return age;
     }



}
