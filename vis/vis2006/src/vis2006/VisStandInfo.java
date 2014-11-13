/*
 * StandDiameters.java
 *
 * Created on 30. August 2005, 06:13
 */

package vis2006;
import treegross.base.*;

/**
 *
 * @author  admin
 */
public class VisStandInfo {
    Double minAlter = 1000.0;
    Double maxAlter = 0.0;
    
    /** Creates a new instance of StandDiameters */
    public VisStandInfo() {
    }

// for the following get methods "code" means tree code, if code==0 then take whole stand
// "taken" controls, which tree are taken 0=all, -1= all alive, 1990 all removed that year
// "ou" controls, 0 = all , -1 = understory, 1 = overstory
// "zf" controls, 0 = all , -1 = no crop tree, 1= crop tree

    // nur Derbholz > 7 cm
    public double getN(Stand st,int acode, int taken, int ou, int zf){
        double anz=0.0;
        for (int i=0;i<st.ntrees;i++){
            if (st.tr[i].d >= 7.0 && (st.tr[i].code==acode || acode==0) &&
               (taken==0 || (taken > -90 && taken<0 && st.tr[i].out<0) || (taken>0 && taken==st.tr[i].out)
               || ( taken==-99 && st.tr[i].remarks.indexOf("Einw")>-1))&&
               (ou == 0 || (ou < 0 && st.tr[i].ou==2) || (ou > 0 && st.tr[i].ou==1)) &&
               (zf == 0 || (zf < 0 && st.tr[i].crop==false) || (zf > 0 && st.tr[i].crop==true))){
            anz=anz+st.tr[i].fac;
        }}
        return anz;
    }

    // Alle Bäume inkl. < 7 cm
    public double getNAlle(Stand st,int acode, int taken, int ou, int zf){
        double anz=0.0;
        for (int i=0;i<st.ntrees;i++){
            if ((st.tr[i].code==acode || acode==0) &&
               (taken==0 || (taken > -90 && taken<0 && st.tr[i].out<0) || (taken>0 && taken==st.tr[i].out)
               || ( taken==-99 && st.tr[i].remarks.indexOf("Einw")>-1))&&
               (ou == 0 || (ou < 0 && st.tr[i].ou==2) || (ou > 0 && st.tr[i].ou==1)) &&
               (zf == 0 || (zf < 0 && st.tr[i].crop==false) || (zf > 0 && st.tr[i].crop==true))){
            anz=anz+st.tr[i].fac;
        }}
        return anz;
    }
//  
    public double getG(Stand st,int acode, int taken, int ou, int zf){
        double g=0.0;
        for (int i=0;i<st.ntrees;i++){
            if (st.tr[i].d >= 7.0 && (st.tr[i].code==acode || acode==0) &&
               (taken==0 || (taken > -90 && taken<0 && st.tr[i].out<0) || (taken>0 && taken==st.tr[i].out)
               || ( taken==-99 && st.tr[i].remarks.indexOf("Einw")>-1))&&
               (ou == 0 || (ou < 0 && st.tr[i].ou==2) || (ou > 0 && st.tr[i].ou==1)) &&
               (zf == 0 || (zf < 0 && st.tr[i].crop==false) || (zf > 0 && st.tr[i].crop==true))){
            g=g+Math.PI*Math.pow((st.tr[i].d/200.0),2.0)*st.tr[i].fac;
        }}
        return g;
    }
//    
//  
    public double getV(Stand st,int acode, int taken, int ou, int zf){
        double v=0.0;
        for (int i=0;i<st.ntrees;i++){
            if (st.tr[i].d >= 7.0 && (st.tr[i].code==acode || acode==0) &&
               (taken==0 || (taken > -90 && taken<0 && st.tr[i].out<0) || (taken>0 && taken==st.tr[i].out)
               || ( taken==-99 && st.tr[i].remarks.indexOf("Einw")>-1))&&
               (ou == 0 || (ou < 0 && st.tr[i].ou==2) || (ou > 0 && st.tr[i].ou==1)) &&
               (zf == 0 || (zf < 0 && st.tr[i].crop==false) || (zf > 0 && st.tr[i].crop==true))){
            v=v+st.tr[i].v*st.tr[i].fac;
        }}
        return v;
    }
//
    // ! Rechnet nicht korrekt, fac = 0 werden eingerechnet
    public double getA(Stand st,int acode, int taken, int ou, int zf){
        double a=0.0;
        double n=0.0;
        for (int i=0;i<st.ntrees;i++){
            if (st.tr[i].d >= 7.0 && (st.tr[i].code==acode || acode==0) &&
               (taken==0 || (taken > -90 && taken<0 && st.tr[i].out<0) || (taken>0 && taken==st.tr[i].out)
               || ( taken==-99 && st.tr[i].remarks.indexOf("Einw")>-1))&&
               (ou == 0 || (ou < 0 && st.tr[i].ou==2) || (ou > 0 && st.tr[i].ou==1)) &&
               (zf == 0 || (zf < 0 && st.tr[i].crop==false) || (zf > 0 && st.tr[i].crop==true))){
            a=a+st.tr[i].age*st.tr[i].fac;
            n=n+st.tr[i].fac;
        }}
        if (n>0) a=a/n;
        return a;
    }

    // Alle Bäume inkl. < 7 cm
    public void minMaxAlterRechnen(Stand st, int acode, int taken, int ou, int zf){
        for (int i = 0; i < st.ntrees; i ++){
            if(st.tr[i].fac > 0.0
                    && (st.tr[i].code==acode) 
                    && (taken==0 || (taken > -90 && taken<0 && st.tr[i].out<0) || (taken>0 && taken==st.tr[i].out)
                        || ( taken==-99 && st.tr[i].remarks.indexOf("Einw")>-1))
                    && (ou == 0 || (ou < 0 && st.tr[i].ou==2) || (ou > 0 && st.tr[i].ou==1)) 
                    && (zf == 0 || (zf < 0 && st.tr[i].crop==false) || (zf > 0 && st.tr[i].crop==true))){
                if(st.tr[i].age > maxAlter) maxAlter = st.tr[i].age * 1.0;
                if(st.tr[i].age < minAlter) minAlter = st.tr[i].age * 1.0;
            }
            
        }
        
    }

    public double getMinAlter(){
        return minAlter;
    }

     public double getMaxAlter(){
        return maxAlter;
    }
}
