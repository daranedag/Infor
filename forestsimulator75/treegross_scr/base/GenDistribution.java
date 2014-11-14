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
/** TreeGrOSS : class gendistribution generates a diameter distribution from stand values
*  This is the major class for several other classes of this package
*  http://treegross.sourceforge.net
*   
*  @version 	30-NOV-2004
*  @author	Juergen Nagel  
*/

package treegross.base;
public class GenDistribution{

    /**  generates a diameter distribution from stand values   */
    public void weibull ( Stand st, int art, int alter, double dg, double hg, double dmax, double gfl) throws Exception{
// Verteilung nach XML File        
           double dgen,gsum;
           String nr="";
           Species spx = new Species();
           
           //spx.spDef.loadSpeciesDefXML(art,st.programDir,st.FileXMLSettings);
           spx.spDef=st.getSDM().getByCode(art);
           
           spx.dg=dg;
           spx.dmax=dmax;
           gsum=0.0;
        // 1.c Bäume erzeugen         
           do{
              FunctionInterpreter fi = new FunctionInterpreter();
              dgen=fi.getValueForSpecies(spx,spx.spDef.diameterDistributionXML);
              gsum=gsum+Math.PI*(dgen/200)*(dgen/200);
              Integer nox = st.ntrees+2;
              nr = nox.toString();
              st.addtree(art,nr,alter,-1,dgen,0.0,0.0,0.0,-9.0,0.0,0.0,0.0,0,0,0);
           }
           while (gsum<gfl); 
    }

    /**  generates a diameter distribution from stand values, if dg-(dmax-dg) < 7.0
    else the diameters are created using a normal distribution, where dmax-dg) is 3 sigma*/
    public void weibullEven (Stand st, int art, int alter, double dg, double hg, double dmax, double gfl) throws Exception{
        double dgen=7.0;
        double gsum=0.0;
        String nr="";
        int ngen=0;

        if ( dg-(dmax-dg) > 7.0) { 
            nr="";	   
            Species spx = new Species();
            
            //spx.spDef.loadSpeciesDefXML(art,st.programDir,st.FileXMLSettings);
            spx.spDef=st.getSDM().getByCode(art);
            
            spx.dg=dg;
            spx.dmax=dmax;
            gsum=0.0;
            double ggenerated;
            int ncycle=0;
            int anzahl=0;
            // 
            do {        
                // calculate number of trees 
                ncycle=ncycle+1;
                anzahl=(int)(((gfl-gsum)*0.8)/(Math.PI*Math.pow((dg/200.0),2.0)));
                //         System.out.println(" generate Anzahl der gewünschten Bäume"+anzahl);
                // Intervalllänge delta
                if (anzahl>0){
                    double delta=1.0/(anzahl);
                    //            System.out.println(" generate Intervalllänge"+delta);
                    // 1.c Bäume erzeugen         
                        for (int i=0;i<anzahl;i++){
                        FunctionInterpreter fi = new FunctionInterpreter();
                        dgen=fi.getValueForSpecies(spx,spx.spDef.diameterDistributionXML);
                        gsum=gsum+Math.PI*(dgen/200)*(dgen/200);
                        Integer nox = st.ntrees+2;
                        nr = nox.toString();
                        st.addtree(art,nr,alter,-1,dgen,0.0,0.0,0.0,-9.0,0.0,0.0,0.0,0,0,0);
                        ngen=ngen+1;
                    }
                    //            System.out.println(" generate gfl und gsum "+ncycle+" "+gfl+"  "+gsum);
                } 
            } while ((Math.abs(gfl-gsum)>0.05)&&(ncycle<20)&&(anzahl>0));

        }
        else {  //generate from normal distribution
            NormalDistributed ndis = new NormalDistributed();  
            do {
                dgen=dg+((dmax-dg)*ndis.value(3.0));
                    if (dgen >= 1.0){
                    gsum=gsum+Math.PI*(dgen/200)*(dgen/200);
                    Integer nox = st.ntrees+2;
                    nr = nox.toString();
                    st.addtree(art,nr,alter,-1,dgen,0.0,0.0,0.0,-9.0,0.0,0.0,0.0,0,0,0);
                    ngen=ngen+1;
                }
            } while (gfl > gsum) ; 

        }
        // generate at least one tree             
        if (ngen == 0) st.addtree(art,nr,alter,-1,dg,hg,0.0,0.0,-9.0,0.0,0.0,0.0,0,0,0);
    }


}
	
