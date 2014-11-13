/** TreeGrOSS : class Tree defines the trees for class stand
    http://treegross.sourceforge.net
    Version 05-Apr-2005 */
/*   (c) 2002-2008 Juergen Nagel, Northwest German Forest Research Station , 
       Grätzelstr.2, 37079 Göttingen, Germany
       E-Mail: Juergen.Nagel@nw-fva.de
 
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
 */
package treegross.base;

/** TreeGrOSS : class tree defines the trees for class stand*/
public class Tree implements Cloneable{
    /** species Code according to Lower Saxony */ public int code; 
    /** tree number */  public String no; 
    /** age */ public int age; 
    /** if living -1, else the year when died or taken out */ public int out; 
    /** 0=standing, 1= fallen, 2=thinned, 3=harvested */ public int outtype; 
    /** diameter, height, volume */ public double d, h, v; 
    /** crown base and crown width */ public double cb, cw ; 
    /** relative tree position coordinates , z=vertical height of ground */ public double x,y,z ; 
    /** crop tree */ public boolean crop; 
    /** temporary crop tree*/ public boolean tempcrop;
    /** habitat tree*/ public boolean habitat;
    /** competition index c66 c66xy c66c and c66cxy */ public double c66, c66c, c66xy, c66cxy; 
    /** weight factor for concentric sample plots */ public double fac; 
    /** site index */ public double si;
    /** reference to species */ public Species sp;
    /** reference to stand */ public Stand st;
    /** tree layer 1=upper, 2=middel, 3=lower*/ public int layer;
    /** last diameter amd height increment */ public double bhdinc, hinc;
    /** year, tree grew into stand*/ public int year;
    /** 0= unknown, 1=planting, 2=natural regeneration*/ public int origin;
    /** remarks */ public String remarks;
    public double hMeasuredValue;
    /** Erlös pro Baum in Euro: getProceeds (get from auxilliaries.SingletreeAsset*/public double proceeds;
    /** erntekostenfreier Erlös pro Baum in Euro: getPwoh (get from auxilliaries.TreeValudation*/public double pwohc;
    /** Stammholzanteil in EFm: getSharelog (get from auxilliaries.TreeValudation*/ public double sharelog;
    /** Kosten pro Baum in Euro: getCosts (get from auxilliaries.TreeValudation*/public double costs;
    /** X-Holzanteil: getShareXtimber (get from auxilliaries.TreeValudation*/public double shareXtimber;
    /** Totholz Zersetzungsgrad */ public int zGrad = 0;
    /** Volume of deadwood */ public double volumeDeadwood =0.0;
    /** Volume of deadwood nature conservation */ public double volumeDeadwoodConservation =0.0;
    /** Volume of harvested */ public double volumeHarvested =0.0;
    /** Degree of Decay */ public double degreeOfDecay =0;
    /** maximum number of neighbor trees */ public int maxNeighbor = 15;
    /** neighbor tree indices in radius of 2*crowthwidth */ public int[] neighbor = new int[maxNeighbor];
    /** number of neighbor trees */ public int nNeighbor;
    /** group */ public int group;
    public int ihpot;
// für experimetal version
    /** width and height of light crown*/ public double cbLightCrown =-9; 
    /** width and height of light crown*/ public double cwLightCrown =-9; 
    /** year of removal in reality */ int yearOfRemovalinReality=0;
// for Viswin 
    /** Ober- u. Unterstand */ public int ou = 0;
    /** mortality reason 1= thinned or harvested, 2= dry and standing, 3= wind throw, 4= other*/ public int mortalityReason = 0;
    

/** empty tree contructor  */  
    public Tree() { }
    
    

/** Fill tree with data, fille missing information with negative value    */
    public Tree(int codex, String nox, int agex, int outx, int outtypex, double dx, double hx, double cbx, double cwx,
            double six, double facx, double xx, double yx, double zx, boolean cropTreex, boolean tempCropTreex,
            boolean habitatTreex, int treeLayerx, double volumeDeadwoodx, String remarksx) {
            code = codex;
            no = nox;
            age= agex;
            out = outx;
            outtype = outtypex;
            d = dx;
            h = hx;
            age = agex;
            cb = cbx;
            cw = cwx;
            si= six;
            fac = facx;
            x=xx;
            y=yx;
            z=zx;
            habitat = habitatTreex;
            crop = cropTreex;
            tempcrop = tempCropTreex;
            layer = treeLayerx;
            volumeDeadwood = volumeDeadwoodx;
            remarks = remarksx;
         
    };
    
    
    public double ci;

   /** this function clones the Tree
    *  the field Stand will not be cloned!!!
    *  The Stand must be added after cloning a Tree.
    *  if Tree.clone() is used in Stand.clone() the new cloned Stand
    *  must be added to the new cloned tree by writing adiditonal code!
    */ 
    public Tree clone(){
        Tree clone= new Tree();
        clone.age= new Integer(this.age);
        clone.bhdinc= new Double(this.bhdinc);
        clone.c66= new Double(this.c66);
        clone.c66c= new Double(this.c66c);
        clone.c66cxy= new Double(this.c66cxy);
        clone.c66xy= new Double(this.c66xy);
        clone.cb= new Double(this.cb);
        clone.cbLightCrown= new Double(this.cbLightCrown);
        clone.ci= new Double(this.ci);
        clone.code= new Integer(this.code);
        clone.costs= new Double(this.costs);
        clone.crop= new Boolean(this.crop);
        clone.cw= new Double(this.cw);
        clone.cwLightCrown= new Double(this.cwLightCrown);
        clone.d= new Double(this.d);
        clone.fac= new Double(this.fac);
        clone.h= new Double(this.h);
        clone.hMeasuredValue= new Double(this.hMeasuredValue);
        clone.habitat= new Boolean(this.habitat);
        clone.hinc= new Double(this.hinc);
        clone.layer= new Integer(this.layer);
        clone.no= new String(this.no);
        clone.origin= new Integer(this.origin);
        clone.ou= new Integer(this.ou);
        clone.out= new Integer(this.out);
        clone.outtype= new Integer(this.outtype);
        clone.proceeds= new Double(this.proceeds);
        clone.pwohc= new Double(this.pwohc);
        if(remarks!=null) clone.remarks= new String(this.remarks);
        else clone.remarks="";
        clone.shareXtimber= new Double(this.shareXtimber);
        clone.sharelog= new Double(this.sharelog);
        clone.si= new Double(this.si);
        clone.sp= this.sp.clone();
        //clone.st has to be added in the super clone call
        clone.v= new Double(this.v);
        clone.x= new Double(this.x);
        clone.y= new Double(this.y);
        clone.year= new Integer(this.year);
        clone.yearOfRemovalinReality=new Integer(this.yearOfRemovalinReality);
        clone.z= new Double(this.z);
        clone.zGrad= new Integer(this.zGrad);
        clone.volumeDeadwood= this.volumeDeadwood;
        clone.degreeOfDecay= this.degreeOfDecay;
        return clone;        
    }
    public double calculateCw() {
        double cwerg =0.0;
        if (sp.spDef.crownwidthXML.length()>2 && d >= 7.0){
                   FunctionInterpreter fi = new FunctionInterpreter();
                   cwerg=fi.getValueForTree(this,sp.spDef.crownwidthXML);
            }
            else cwerg =0.01;
        if (d < 7.0) cwerg = 2.52;  // 5qm
        return cwerg;
    }

    public double calculateCwAtHeight(double hx) {
        double erg =0.0;
        if (d >= 7.0){
            double cwAtHx=0.0;
            double h66=h-2.0*(h-cb)/3.0;
            if (hx<h && hx>h66) cwAtHx=Math.sqrt(1.5*Math.pow((cw/2.0),2.0)*(h-hx)/(h-cb));
            if (hx<=h66 && hx>cb) cwAtHx=cw/2.0;
            erg = 2.0*cwAtHx;
        }
// else we are looking at the small trees representing a layer so they get crown width
        else erg = cw/2.0;
        return erg;
    }
    public double calculateCb() {
        double cberg=0.0;
        if (sp.spDef.crownbaseXML.length()>4 && d >=7){
                   FunctionInterpreter fi = new FunctionInterpreter();
                   if (sp.h100 < 1.3 ) sp.h100=h;
                   cberg=fi.getValueForTree(this,sp.spDef.crownbaseXML);
            }

        if ( d < 7.0) cberg = h/2.0;
        if (cberg < 0.1) cberg=0.1;
        return cberg;
    }
    public double calculateVolume() {
        double erg =0.0;
        if (d > 3.0 && h > 3.0){
           FunctionInterpreter fi = new FunctionInterpreter();
           erg=fi.getValueForTree(this,sp.spDef.volumeFunctionXML);
           if (erg < 0.0) erg =0.0;
           if (new Double(erg).isNaN() ) erg = 0.0;
        }
        return erg;
    }

    public double calculateDecay() {
        double erg =0.0;
        FunctionInterpreter fi = new FunctionInterpreter();
        erg=fi.getValueForTree(this,sp.spDef.decayXML);
        if (erg < 0.0 ) erg=0.0;
        if (erg < 1.0 ) volumeDeadwood=v*erg;
        return erg;
    }

    
    public double calculateSiteIndex() {
        double erg =0.0;
        FunctionInterpreter fi = new FunctionInterpreter();
        erg=fi.getValueForTree(this,sp.spDef.siteindexXML);
        return erg;
    }
    public double calculateMaxBasalArea() {
        double erg =0.0;
        FunctionInterpreter fi = new FunctionInterpreter();
        erg=fi.getValueForTree(this,sp.spDef.maximumDensityXML);
        return erg;
    }
    public void ageBasedMortality() {
        double ageindex = (1.0*age / (1.0*sp.spDef.maximumAge)) - 1.0;
//        System.out.println("Naturliche Mortalität  "+ageindex);
        if (ageindex >  Math.random() ){
            out = st.year;
            outtype = 1;
//            System.out.println("Naturliche Mortalität");
        }
    }

/** get moderate thinning factor for maximum basal area from setting */
     public double getModerateThinningFactor(){
        double fac = 1.0;
        if (sp.spDef.moderateThinning.length()>4){
             String zeile=sp.spDef.moderateThinning;
             String[] tokens;
             tokens = zeile.split(";");
             for (int i=0;i < 3; i++){
                 double hu = Double.parseDouble(tokens[i*3]);
                 double f = Double.parseDouble(tokens[i*3+1]);
                 double ho = Double.parseDouble(tokens[i*3+2]);
                 if (h >= hu && h < ho) fac=f;
             }
         }
         return fac;
     }


    public void setMissingData() {
        //      if the first tree of a species is added there is no site index
        if (si <= -9.0 && sp.hbon<=0.0){
            if (sp.h100 <= 1.3) sp.h100 = st.h100;
            si = calculateSiteIndex();
        }
        if (si <= -9.0) si=sp.hbon;
        if (cb < 0.01) {
            cb = calculateCb();
        }
        if (cw < 0.01) {
            cw =calculateCw();
        }
        if (v <=0.0 ) {
            v= calculateVolume();
        }
    }
    
    /** grows a single tree for the length of up to 5 years, with and without random
    *  effects. If the years is < 5 then the increment is set to increment*years/5
    */        
    void grow(int years, boolean randomEffects){
         FunctionInterpreter fi = new FunctionInterpreter();
         bhdinc=fi.getValueForTree(this,sp.spDef.diameterIncrementXML);
         NormalDistributed ndis = new NormalDistributed();
         double effect = 0.0;
         if (randomEffects) {
              effect = sp.spDef.diameterIncrementError*ndis.value(1.0);
              bhdinc = Math.exp(Math.log(bhdinc)+effect);
         }
         if (bhdinc < 0.0) bhdinc =0.0;
         bhdinc = 2.0*Math.sqrt((Math.PI*Math.pow((d/2.0),2.0)+bhdinc*10000)/Math.PI) - d;
// grow Height
         double ihpot=fi.getValueForTree(this,sp.spDef.potentialHeightIncrementXML);
         if (h/sp.h100 >= 1.0) hinc=1.0; else hinc=sp.h100/h;
//         hinc = fi.getValueForTree(this,sp.spDef.heightIncrementXML);
//         if (randomEffects) {
//             effect= ihpot/50.0;
//         }
//         hinc=ihpot+hinc+effect*ndis.value(1.0);
//         if (hinc < 0)  hinc=1.0;           //no negative height growth allowed
//         hinc = h*hinc;
         hinc = fi.getValueForTree(this,sp.spDef.heightIncrementXML);
         if (randomEffects) {
             effect= sp.spDef.heightIncrementError;
         }
         hinc=hinc+effect*ndis.value(1.0);
         if (hinc > ihpot*1.2)  hinc=ihpot*1.2;   //ihpot*1.2 is max
         if (hinc < 0)  hinc=0.0;    //no negative height growth allowed

// Jugenwachstum, solange der BHD < 7.0
         if (d < 7 ){
              double jh1=0.0;double jh2=0.0;
              Tree atree = new Tree();
              atree.sp=sp;
              atree.code=sp.code;
              atree.si=si;
              atree.age = 30;
              jh1=fi.getValueForTree(atree,sp.spDef.siteindexHeightXML);
              jh2=jh1;
              jh1=jh1*((Math.exp(age/30.0)-1.0)/(Math.exp(1.0)-1.0));
              jh2=jh2*((Math.exp((age+5)/30.0)-1.0)/(Math.exp(1.0)-1.0));
              hinc=1.0*(jh2-jh1);
              bhdinc=0.0;
              double hd = 1.0;
              if (sp.code < 200) hd = 128.0;
              if (sp.code >= 200 && sp.code < 300 ) hd = 1.40;
              if (sp.code >= 300 && sp.code < 400 ) hd = 1.80;
              if (sp.code >= 400 && sp.code < 500 ) hd = 1.20;
              if (sp.code >= 500 && sp.code < 600 ) hd = 0.95;
              if (sp.code >= 600 && sp.code < 700 ) hd = 0.85;
              if (sp.code >= 700 && sp.code < 800 ) hd = 1.1;
              if (sp.code >= 800 ) hd = 95.0;
              d = h/hd;
         }


        d=d+years*bhdinc/5.0;
        h=h+years*hinc/5.0;
        age=age+years;
        v = calculateVolume();
         
    }

    void growBack(int years){
//        System.out.println("grow back Tree");
       if (out == st.year) { out=-1; outtype=0;}
       if (out < 0){
          Tree xtree = new Tree();
          xtree=this.clone();
          xtree.grow(years,false);
          double dzvor = xtree.bhdinc;
          double hzvor = xtree.hinc;
//          System.out.println("grow back: d,h "+dzvor+"  "+hzvor);
          xtree=this.clone();
          xtree.h=xtree.h-years*hzvor/5.0;
          xtree.d=xtree.d-years*dzvor/5.0;
          xtree.age=xtree.age-years;
          FunctionInterpreter fi = new FunctionInterpreter();
          xtree.sp.h100=xtree.sp.h100-fi.getValueForTree(xtree,sp.spDef.potentialHeightIncrementXML);
          if (xtree.h < 1.3) h=4;
          if (xtree.d < 1.0) d=1.0;
          xtree.cw=xtree.calculateCw();
          xtree.cb=xtree.calculateCb();
          
          xtree.grow(years,false);
//          System.out.println("grow back: d,h "+xtree.bhdinc+"  "+xtree.hinc);
          h=h-years*xtree.hinc/5.0;
          d=d-years*xtree.bhdinc/5.0;
          if (d < 7.0 || h < 5.0) {
              d=3.0;h=4.0;cw=2.0;cb=2.0;
          }
          if (cb >= h ) cb =h - 0.2;
          
//          System.out.println("grow back: cb,h "+cb+"  "+h);

          age=age-years;
          v = calculateVolume();
       }

    }


    /** update the crown base and width i.e. after growing of the tree*/
    void updateCrown(){
         if (d >= 7.0){
           double cbnew=0.0;
           cbnew = calculateCb();
           if (cbnew > cb) cb=cbnew;
        // Crown base is not allowed to get a lower value
           cw = calculateCw();
         } //the little trees
         else {
           cb = h/2.0;

         }
    }
    
    /** update the competition indices, should be called after growing, thinning, and
    *  mortality 
    */
    public void updateCompetition(){
        if (sp.spDef.competitionXML.length()>1){
        try {
            String modelPlugIn="treegross.base."+sp.spDef.competitionXML;  
            PlugInCompetition comp = (PlugInCompetition)Class.forName(modelPlugIn).newInstance();  
            if (fac > 0.0 && out < 1 ){
              c66=comp.getc66(this);
              c66c = comp.getc66c(this);
              comp.replaceC66xyAndC66cxy(this,2.0*cw);
            }
            else{
              c66=-99;
              c66c=-99.0;
              c66xy=-99;
              c66cxy=-99;
           }
            
        }
        catch (Exception e){
            System.out.println(e);
            System.out.println("ERROR in Class tree  updateCompetition "); }
        }
    }
/** setGroup to assign the tree to an individual group */
    public void setGroup(int group){
        this.group=group;
    }

}
