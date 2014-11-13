/* http://www.nw-fva.de
   Version 07-11-2008

   (c) 2002 Juergen Nagel, Northwest German Forest Research Station, 
       Grätzelstr.2, 37079 Göttingen, Germany
       E-Mail: Juergen.Nagel@nw-fva.de
 
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT  WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
 */
package treegross.base;
import org.nfunk.jep.*;

/**
 *
 * @author nagel
 */
public class FunctionInterpreter {
    
    /** Creates a new instance of FunctionInterpreter */
    public FunctionInterpreter() {
    }
    
    public double getValueForTree(Tree t, String function){
        double value = 0.0;
        JEP jep = new JEP();
        String realFun = function;
        int m = function.indexOf("/*");
        if (m > 0) function.substring(0,m-1);
        if (function.indexOf("t.d")>-1) jep.addVariable("t.d",t.d);
        if (function.indexOf("t.h")>-1) jep.addVariable("t.h",t.h);
        if (function.indexOf("t.age")>-1) jep.addVariable("t.age",t.age);
        if (function.indexOf("t.cb")>-1) jep.addVariable("t.cb",t.cb);
        if (function.indexOf("t.cw")>-1) jep.addVariable("t.cw",t.cw);
        if (function.indexOf("t.c66c")>-1) jep.addVariable("t.c66c",t.c66c);
        if (function.indexOf("t.c66")>-1) jep.addVariable("t.c66",t.c66);
        if (function.indexOf("t.c66cxy")>-1) jep.addVariable("t.c66cxy",t.c66cxy);
        if (function.indexOf("t.c66xy")>-1) jep.addVariable("t.c66xy",t.c66xy);
        if (function.indexOf("t.out")>-1) 
            jep.addVariable("t.out",t.out);
        if (function.indexOf("t.si")>-1) jep.addVariable("t.si",t.si);
        if (function.indexOf("t.ihpot")>-1) jep.addVariable("t.ihpot",t.ihpot);
        if (function.indexOf("t.hinc")>-1) jep.addVariable("t.hinc",t.hinc);
        if (function.indexOf("sp.hg")>-1) jep.addVariable("sp.hg",t.sp.hg);
        if (function.indexOf("sp.dg")>-1) jep.addVariable("sp.dg",t.sp.dg);
        if (function.indexOf("sp.h100")>-1) jep.addVariable("sp.h100",t.sp.h100);
        if (function.indexOf("sp.year")>-1) jep.addVariable("sp.year",t.st.year);
        if (function.indexOf("sp.BHD_STD")>-1) jep.addVariable("sp.BHD_STD",getBHD_STD(t));
        if (function.indexOf("sp.Cw_dg")>-1) jep.addVariable("sp.Cw_dg",getCw_dg(t));

        jep.addStandardFunctions();
        jep.parseExpression(realFun);
        value = jep.getValue();

        return value;
    }
    public double getValueForSpecies(Species sp,  String function){
        double value = 0.0;
        RandomNumber zz = new RandomNumber();   
        JEP jep = new JEP();
        if (function.indexOf("sp.dg")>-1) jep.addVariable("sp.dg",sp.dg);
        if (function.indexOf("sp.h100")>-1) jep.addVariable("sp.h100",sp.h100);
        if (function.indexOf("dmax")>-1) 
            jep.addVariable("dmax",sp.dmax);
        if (function.indexOf("random")>-1) 
            jep.addVariable("random",zz.random());
        jep.addStandardFunctions();
        jep.parseExpression(function);
        value = jep.getValue();
        return value;
    } 
// calculates the Standard Error of the DBH of a species    
    private double getBHD_STD(Tree t){
         double sError=0.0;
         double dm=0.0; int ndg=0;
         for (int i=0;i<t.st.ntrees;i++)
             if (t.code==t.st.tr[i].code && t.st.tr[i].out < 0){
                 dm=dm+t.st.tr[i].d;
                 ndg=ndg+1;
           }
           if (ndg>0) {
               dm=dm/ndg; 
               double sum=0.0;
               for (int i=0;i<t.st.ntrees;i++)
                   if (t.code==t.st.tr[i].code && t.st.tr[i].out < 0)
                       sum=sum+Math.pow((t.st.tr[i].d-dm),2.0);
               if (ndg>1) { sError=Math.sqrt(sum/(ndg-1))/Math.sqrt(ndg);}
               else sError=1;
           }
       return sError;
  }
    // calculates the Standard Error of the DBH of a species    
    private double getCw_dg(Tree t){
        Tree atree = new Tree();
        atree.sp = t.sp;
        atree.code = t.code;
        atree.d  = t.sp.dg;
        atree.h  = t.sp.hg;
        atree.cb=atree.calculateCb();
        atree.cw=atree.calculateCw();
        return atree.cw;
   }
    
    
}
