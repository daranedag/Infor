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
public class StandDiameters {
    /** Creates a new instance of StandDiameters */
    public StandDiameters() {
    }
// for the following get methods code means tree code, if code==0 then take whole stand
// taken controls, which tree are taken 0=all, -1= all alive, 1990 all removed that year    
    public double getN(Stand st,int acode, int taken){
        double anz=0.0;
        for (int i=0;i<st.ntrees;i++){
            if (st.tr[i].fac>0.0 &&  (st.tr[i].code==acode || acode==0) &&
               (taken==0 || (taken<0 && st.tr[i].out<0) || (taken>0 && taken==st.tr[i].out))){
            anz=anz+st.tr[i].fac;
        }}
        return anz;
    }
//    
    public double getDg(Stand st,int acode, int taken){
        double dg=0.0;
        double anz=0.0;
        for (int i=0;i<st.ntrees;i++){
           if (st.tr[i].fac>0.0 && st.tr[i].d >= 7.0 &&(st.tr[i].code==acode || acode==0) &&
               (taken==0 || (taken<0 && st.tr[i].out<0) || (taken>0 && taken==st.tr[i].out))){
              dg=dg+Math.PI*Math.pow((st.tr[i].d/2.0),2.0)*st.tr[i].fac;
              anz=anz+st.tr[i].fac;
        }}
        if (anz > 0.0) dg=2.0*Math.sqrt(dg/(Math.PI*anz));
        return dg;
    }
//    
    public double getDar(Stand st,int acode, int taken){
        double dar=0.0;
        double anz=0.0;
        for (int i=0;i<st.ntrees;i++){
           if (st.tr[i].fac>0.0 && st.tr[i].d >= 7.0 &&(st.tr[i].code==acode || acode==0) &&
               (taken==0 || (taken<0 && st.tr[i].out<0) || (taken>0 && taken==st.tr[i].out))){
              dar=dar+st.tr[i].d*st.tr[i].fac;
              anz=anz+st.tr[i].fac;
        }}
        if (anz > 0.0) dar=dar/anz;
        return dar;
    }
//     
    public double getDow(Stand st,int acode, int taken){
        double dow=0.0;
        double anz=0.0;
        for (int i=0;i<st.ntrees;i++){
            if (st.tr[i].fac>0.0 && st.tr[i].d >= 7.0 && (st.tr[i].code==acode || acode==0) &&
               (taken==0 || (taken<0 && st.tr[i].out<0) || (taken>0 && taken==st.tr[i].out))){
            anz=anz+st.tr[i].fac;
            }
        }
        double n20=anz*0.2;
        anz=0.0;
        for (int i=0;i<st.ntrees;i++){
           if (st.tr[i].fac>0.0 && st.tr[i].d >= 7.0 &&(st.tr[i].code==acode || acode==0) && (anz< n20) &&
               (taken==0 || (taken<0 && st.tr[i].out<0) || (taken>0 && taken==st.tr[i].out))){
              if (anz+st.tr[i].fac < n20) {
                  dow=dow+Math.PI*Math.pow((st.tr[i].d/2.0),2.0)*st.tr[i].fac;  
                  anz=anz+st.tr[i].fac;
              }
              else {
                  dow=dow+Math.PI*Math.pow((st.tr[i].d/2.0),2.0)*(n20-anz);
                  anz=anz+st.tr[i].fac;
              }
           }
        }
        if (n20 > 0.0) dow=2.0*Math.sqrt(dow/(Math.PI*n20));
        return dow;
    }
//     
    public double getDx(Stand st,int acode, int taken, int x){
        double d100=0.0;
        double anz=0.0;
        for (int i=0;i<st.ntrees;i++){
            if (st.tr[i].fac>0.0 && st.tr[i].d >= 7.0 &&(st.tr[i].code==acode || acode==0) &&
               (taken==0 || (taken<0 && st.tr[i].out<0) || (taken>0 && taken==st.tr[i].out))){
            anz=anz+st.tr[i].fac;
        }}
        double n20=st.size*x;
        if (n20 >= 1.0 && n20 <= anz){
          anz=0.0;
          for (int i=0;i<st.ntrees;i++){
           if (st.tr[i].fac>0.0 && st.tr[i].d >= 7.0 &&(st.tr[i].code==acode || acode==0) && (anz< n20) &&
               (taken==0 || (taken<0 && st.tr[i].out<0) || (taken>0 && taken==st.tr[i].out))){
              if (anz+st.tr[i].fac < n20) {
                  d100=d100+Math.PI*Math.pow((st.tr[i].d/2.0),2.0)*st.tr[i].fac;  
                  anz=anz+st.tr[i].fac;
              }
              else {
                  d100=d100+Math.PI*Math.pow((st.tr[i].d/2.0),2.0)*(n20-anz);
                  anz=anz+st.tr[i].fac;
              }
          }}
          if (n20 > 0.0) d100=2.0*Math.sqrt(d100/(Math.PI*n20));
        }
        return d100;
      }
//    
    public double getDmin(Stand st,int acode, int taken){
        double dmin=0.0;
        double dx=9999.0;
        for (int i=0;i<st.ntrees;i++){
           if (st.tr[i].fac>0.0 && st.tr[i].d >= 7.0 &&(st.tr[i].code==acode || acode==0) && (dx>st.tr[i].d) &&
               (taken==0 || (taken<0 && st.tr[i].out<0) || (taken>0 && taken==st.tr[i].out))){
               dx=st.tr[i].d;
           }}
        if (dx < 9999.0) dmin=dx;
        return dmin;
    }
//    
    public double getDmax(Stand st,int acode, int taken){
        double dmax=0.0;
        double dx=-9999.0;
        for (int i=0;i<st.ntrees;i++){
           if (st.tr[i].fac>0.0 && st.tr[i].d >= 7.0 &&(st.tr[i].code==acode || acode==0) && (dx<st.tr[i].d) &&
               (taken==0 || (taken<0 && st.tr[i].out<0) || (taken>0 && taken==st.tr[i].out))){
               dx=st.tr[i].d;
           }}
        if (dx > 0.0) dmax=dx;
        return dmax;
    }
    
//    
}
