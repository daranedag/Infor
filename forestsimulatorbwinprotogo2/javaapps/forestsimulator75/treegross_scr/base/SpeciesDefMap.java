/*
 * this class stores all species definitions
 * read from species.txt oder species.xml
 *
 * remember change SpecedDef.java too!!
 *  -> remove load methods
 *
 * change stand.java too
 *  -> remove field speciesfile and add pointer to this class
 *  -> if a new species appears do not load from file but from this class
 */

package treegross.base;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.*;
import org.jdom.DocType;


import java.text.*;
import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

/**
 *
 * @author jhansen
 */
public class SpeciesDefMap {
    public HashMap<Integer,SpeciesDef> spcdef;
    private URL actualurl;
    private boolean loaded=false;

    public SpeciesDefMap(){
        spcdef = null;
        loaded=false;
        actualurl=null;
    }
    
    public void reload(){
        if(loaded && actualurl!=null){
            readFromURL(actualurl);
        }
    }
    
    public void readFromPath(String path){
        try{
            URL url = new File(path).toURI().toURL();
            readFromURL(url);            
        }
        catch(Exception e){
            e.printStackTrace();
        }        
    }

    public void readFromURL(URL url){
        try{
           readXML(url);          
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void readXML(URL url) throws IOException, org.jdom.JDOMException{
        //todo paste code for rading xml species definition
        spcdef = new HashMap<Integer,SpeciesDef>();
        actualurl=url;
        
        URLConnection urlcon = url.openConnection();
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(urlcon.getInputStream());
         
        DocType docType = doc.getDocType();
   
        Element rm =  doc.getRootElement();  
        List  list= rm.getChildren("SpeciesDefinition");
        Iterator i = list.iterator();
        
        Element def;
        int code, m, handledLikeCode;
        
        while (i.hasNext()) {            
            def = (Element)i.next();
            code =Integer.parseInt(def.getChild("Code").getText());
            
            handledLikeCode = Integer.parseInt(def.getChild("HandledLikeCode").getText());
            
            spcdef.put(code,new SpeciesDef());
            SpeciesDef actual=spcdef.get(code);
            define(code, actual, def, handledLikeCode); 
            
            if(code!=handledLikeCode){  
                boolean found=false;
                Iterator j = list.iterator();       
                while(j.hasNext() && !found){
                    Element parent_def = (Element)j.next();
                    int code_parent = Integer.parseInt(parent_def.getChild("Code").getText());
                    if (handledLikeCode == code_parent){
                        overload(actual, parent_def);
                        found=true;
                    }
                }
            }
            m = actual.colorXML.indexOf(";");
            actual.colorRed=Integer.parseInt(actual.colorXML.substring(0,m));
            actual.colorXML=actual.colorXML.substring(m+1);
            m = actual.colorXML.indexOf(";");
            actual.colorGreen=Integer.parseInt(actual.colorXML.substring(0,m));
            actual.colorXML=actual.colorXML.substring(m+1);
            actual.colorBlue=Integer.parseInt(actual.colorXML);
            actual.setDefined(true);
        }
        loaded=true;
    } 
    
    private void overload(SpeciesDef actual, Element with){
         if (actual.heightCurve < 0) actual.heightCurve =Integer.parseInt(with.getChild("HeightCurve").getText());
         if (actual.uniformHeightCurveXML.trim().length() < 1) actual.uniformHeightCurveXML = with.getChild("UniformHeightCurveXML").getText();
         if (actual.heightVariationXML.trim().length() < 1) actual.heightVariationXML = with.getChild("HeightVariation").getText();
         if (actual.diameterDistributionXML.trim().length() < 1) actual.diameterDistributionXML = with.getChild("DiameterDistributionXML").getText();
         if (actual.volumeFunctionXML.trim().length() < 1) actual.volumeFunctionXML = with.getChild("VolumeFunctionXML").getText();
         if (actual.crownwidthXML.trim().length() < 1) actual.crownwidthXML = with.getChild("Crownwidth").getText();
         if (actual.crownbaseXML.trim().length() < 1) actual.crownbaseXML = with.getChild("Crownbase").getText();
         if (actual.crownType < 0) actual.crownType =Integer.parseInt(with.getChild("CrownType").getText());
         if (actual.siteindexXML.trim().length() < 1) actual.siteindexXML = with.getChild("SiteIndex").getText();
         if (actual.siteindexHeightXML.trim().length() < 1) actual.siteindexHeightXML = with.getChild("SiteIndexHeight").getText();
         if (actual.potentialHeightIncrementXML.trim().length() < 1) actual.potentialHeightIncrementXML = with.getChild("PotentialHeightIncrement").getText();
         if (actual.heightIncrementXML.trim().length() < 1) actual.heightIncrementXML = with.getChild("HeightIncrement").getText();
         if (actual.heightIncrementError < 0) actual.heightIncrementError = Double.parseDouble(with.getChild("HeightIncrementError").getText());
         if (actual.diameterIncrementXML.trim().length() < 1) actual.diameterIncrementXML = with.getChild("DiameterIncrement").getText();
         if (actual.diameterIncrementError < 0) actual.diameterIncrementError = Double.parseDouble(with.getChild("DiameterIncrementError").getText());
         if (actual.maximumDensityXML.trim().length() < 1) actual.maximumDensityXML =with.getChild("MaximumDensity").getText();
         if (actual.maximumAge < 0) actual.maximumAge =Integer.parseInt(with.getChild("MaximumAge").getText());
         if (actual.ingrowthXML.trim().length() < 1) actual.ingrowthXML = with.getChild("Ingrowth").getText();
         if (actual.decayXML.trim().length() < 1) actual.decayXML = with.getChild("Decay").getText();
         if (actual.targetDiameter < 0) actual.targetDiameter =Double.parseDouble(with.getChild("TargetDiameter").getText());
         if (actual.heightOfThinningStart < 0) actual.heightOfThinningStart =Double.parseDouble(with.getChild("HeightOfThinningStart").getText());
         if (actual.moderateThinning.trim().length() < 1) actual.moderateThinning = with.getChild("ModerateThinning").getText();
         if (actual.colorXML.trim().length() < 1) actual.colorXML = with.getChild("Color").getText();
         if (actual.competitionXML.trim().length() < 1) actual.competitionXML = with.getChild("Competition").getText();
         if (actual.taperFunctionXML.trim().length() < 1) actual.taperFunctionXML = with.getChild("TaperFunction").getText();
         if (actual.stemVolumeFunctionXML.trim().length() < 1){
            try{
                actual.stemVolumeFunctionXML = with.getChild("StemVolumeFunction").getText();
            }catch (Exception e){
                System.out.println("Schaftholz ist: "+actual.stemVolumeFunctionXML);
            }
         }
         if (actual.coarseRootBiomass.trim().length() < 1) actual.coarseRootBiomass = with.getChild("CoarseRootBiomass").getText();
         if (actual.smallRootBiomass.trim().length() < 1) actual.smallRootBiomass = with.getChild("SmallRootBiomass").getText();
         if (actual.fineRootBiomass.trim().length() < 1) actual.fineRootBiomass = with.getChild("FineRootBiomass").getText();
         if (actual.totalRootBiomass.trim().length() < 1) actual.totalRootBiomass = with.getChild("TotalRootBiomass").getText();                   
    }
    
    private void define(int code, SpeciesDef actual, Element def, int hlc){
        actual.code=code;
        actual.handledLikeCode=hlc;
        actual.shortName = def.getChild("ShortName").getText();
        actual.longName = def.getChild("LongName").getText();
        actual.latinName = def.getChild("LatinName").getText();
        actual.internalCode =Integer.parseInt(def.getChild("InternalCode").getText());
        actual.codeGroup =Integer.parseInt(def.getChild("CodeGroup").getText());
        actual.heightCurve =Integer.parseInt(def.getChild("HeightCurve").getText());
        actual.uniformHeightCurveXML = def.getChild("UniformHeightCurveXML").getText();
        actual.heightVariationXML = def.getChild("HeightVariation").getText();
        actual.diameterDistributionXML = def.getChild("DiameterDistributionXML").getText();
        actual.volumeFunctionXML = def.getChild("VolumeFunctionXML").getText();
        actual.crownwidthXML = def.getChild("Crownwidth").getText();
        actual.crownbaseXML = def.getChild("Crownbase").getText();
        actual.crownType =Integer.parseInt(def.getChild("CrownType").getText());
        actual.siteindexXML = def.getChild("SiteIndex").getText();
        actual.siteindexHeightXML = def.getChild("SiteIndexHeight").getText();
        actual.potentialHeightIncrementXML = def.getChild("PotentialHeightIncrement").getText();
        actual.heightIncrementXML = def.getChild("HeightIncrement").getText();
        actual.heightIncrementError = Double.parseDouble(def.getChild("HeightIncrementError").getText());
        actual.diameterIncrementXML = def.getChild("DiameterIncrement").getText();
        actual.diameterIncrementError = Double.parseDouble(def.getChild("DiameterIncrementError").getText());
        actual.maximumDensityXML =def.getChild("MaximumDensity").getText();
        actual.maximumAge =Integer.parseInt(def.getChild("MaximumAge").getText());
        actual.ingrowthXML = def.getChild("Ingrowth").getText();
        actual.decayXML = def.getChild("Decay").getText();
        actual.targetDiameter =Double.parseDouble(def.getChild("TargetDiameter").getText());
        actual.heightOfThinningStart =Double.parseDouble(def.getChild("HeightOfThinningStart").getText());
        actual.moderateThinning = def.getChild("ModerateThinning").getText();
        actual.colorXML = def.getChild("Color").getText();
        actual.competitionXML = def.getChild("Competition").getText();
        actual.taperFunctionXML = def.getChild("TaperFunction").getText();
        // warum wird hier gecatcht??
        try {
            actual.stemVolumeFunctionXML = def.getChild("StemVolumeFunction").getText();
        }catch (Exception e){
             System.out.println("Schaftholz ist: "+actual.stemVolumeFunctionXML);
        }
        actual.coarseRootBiomass = def.getChild("CoarseRootBiomass").getText();
        actual.smallRootBiomass = def.getChild("SmallRootBiomass").getText();
        actual.fineRootBiomass = def.getChild("FineRootBiomass").getText();
        actual.totalRootBiomass = def.getChild("TotalRootBiomass").getText();
    }
    
    public boolean isLoaded(){
        return loaded;
    }

    public URL getActualURL(){
        return actualurl;
    }

    public int getSize(){
        return spcdef.size();
    }

    public SpeciesDef getByCode(int code){
        return (SpeciesDef)spcdef.get(code);
    }

    @Override
    public String toString(){
        return "SpeciedDefMap [size: "+getSize()+"; URL:"+getActualURL().toString()+"]";
    }
    
    /* writes species information for all species of one stand to a html file in
     * specified path with specified filename
     * and returns the complete cannonical path of the output file.
    */
    public String listAllSpeciesDefinition(Stand st, String path, String fname) throws IOException{
        DecimalFormat f  = new DecimalFormat("0.00");
        File file= new File(path, fname);
        String filename=file.getCanonicalPath();        
        OutputStream os=new FileOutputStream(filename); 
	PrintWriter out= new PrintWriter(new OutputStreamWriter(os)); 
	out.println("<HTML>"); 
	out.println("<H2><P align=center>"+"Simulator Species Definition"+"</H2> ");

    Iterator<Integer> iterator= spcdef.keySet().iterator();

       int min =99999;
       int max =-99999;
        while(iterator.hasNext()){
           SpeciesDef sd=this.getByCode(iterator.next());
           if (sd.code > max )max = sd.code;
           if (sd.code < min )min = sd.code;
        }
   for (int i=min ; i <= max; i++){
            SpeciesDef sd=this.getByCode(i);
            if (sd != null){
            out.println("<P>"); 
            int m = -9;
            if(sd.latinName.indexOf("http") > -1) m = sd.latinName.indexOf("http")-1;
            String txt= sd.latinName;
            if (m > 1) txt = "<a href="+sd.latinName.substring(m+1,sd.latinName.length())+">"+sd.latinName.substring(0,m)+"</a>";
            out.println("<P><B>Baumart: "+sd.code+" "+sd.longName+"  "+txt+"</B>");
            }
        }
	out.println("</TABLE>"); 
	out.println("<br>"+"created by ForestSimulatorBWINPro "+st.modelRegion+"</br></HTML>"); 
	out.close();	
	return filename;    
    }

   /* writes species information for active species of one stand to a html file in
     * specified path with specified filename
     * and returns the complete cannonical path of the output file.
    */
    public String listCurrentSpeciesDefinition(Stand st, String path, String fname) throws IOException{
        DecimalFormat f  = new DecimalFormat("0.00");
        File file= new File(path, fname);
        String filename=file.getCanonicalPath();
        OutputStream os=new FileOutputStream(filename);
	PrintWriter out= new PrintWriter(new OutputStreamWriter(os));
	out.println("<HTML>");
	out.println("<H2><P align=center>"+"Simulator Species Definition"+"</H2> ");
        if (st.nspecies>0 && st.ingrowthActive==true){
            try {
               String modelPlugIn="treegross.base."+st.sp[0].spDef.ingrowthXML;
               PlugInIngrowth ig = (PlugInIngrowth)Class.forName(modelPlugIn).newInstance();
               out.println("<P><B>Aktivieres Einwuchsmodell : "+ig.getModelName()+"</B>");
            }
            catch(Exception e){
                System.out.println(e);
                System.out.println("ERROR in Class Ingrowth2 ");
            }
        }
        for (int i=0;i<st.nspecies;i++){
            out.println("<P>");
            int m = -9;
            if(st.sp[i].spDef.latinName.indexOf("http") > -1) m = st.sp[i].spDef.latinName.indexOf("http")-1;
            String txt= st.sp[i].spDef.latinName;
            if (m > 1) txt = "<a href="+st.sp[i].spDef.latinName.substring(m+1,st.sp[i].spDef.latinName.length())+">"+st.sp[i].spDef.latinName.substring(0,m)+"</a>";
            out.println("<P><B>Baumart: "+st.sp[i].code+" "+st.sp[i].spDef.longName+"  "+txt+"</B>");
            out.println("<BR>   Kronenbreite [m] = "+st.sp[i].spDef.crownwidthXML);
            out.println("<BR>   Kronenansatz [m] = "+st.sp[i].spDef.crownbaseXML);
            out.println("<BR>   Bonität      [m] = "+st.sp[i].spDef.siteindexXML);
            out.println("<BR>   Potentielle Höhenzuwachs [%] = "+st.sp[i].spDef.potentialHeightIncrementXML);
            out.println("<BR>   Höhenzuwachsmodulation [%] = "+st.sp[i].spDef.heightIncrementXML);
            out.println("<BR>   Standardabweichung Höhenzuwachs [m] = "+(new Double(st.sp[i].spDef.heightIncrementError)).toString());
            out.println("<BR>   Grundflächenzuwachs [cm²] = "+st.sp[i].spDef.diameterIncrementXML);
            out.println("<BR>   Standardabweichung Grundflächenzuwachs [m²] = "+(new Double(st.sp[i].spDef.diameterIncrementError)).toString());
            out.println("<BR>   Maximale Dichte [m²/ha] = "+st.sp[i].spDef.maximumDensityXML);
            out.println("<BR>   Volumenfunktion [m³] = "+st.sp[i].spDef.volumeFunctionXML);
            out.println("<BR>   Durchmesserverteilung : "+st.sp[i].spDef.diameterDistributionXML);
            out.println("<BR>   Höhenkurvenfunktion = "+st.sp[i].spDef.heightCurve);
            out.println("<BR>   Einheitshöhenkurve [m] = "+st.sp[i].spDef.uniformHeightCurveXML);
            out.println("<BR>   Höhenkurvenvariation [m] = "+st.sp[i].spDef.heightVariationXML);
            out.println("<BR>   Totholzzerfall [%] = "+st.sp[i].spDef.decayXML);
            out.println("<BR>   Kronendarstellung = "+st.sp[i].spDef.crownType);
            out.println("<BR>   Baumartenfarbe [RGB] = "+st.sp[i].spDef.colorXML);
        }
	out.println("</TABLE>");
	out.println("<br>"+"created by ForestSimulatorBWINPro "+st.modelRegion+"</br></HTML>");
	out.close();
	return filename;
    }

    public String listSpeciesCode(int code, String path, String fname2) throws Exception{
	DecimalFormat f  = new DecimalFormat("0.00");
        File file= new File(path, fname2);
        String filename=file.getCanonicalPath();
        OutputStream os=new FileOutputStream(filename); 
	PrintWriter out= new PrintWriter(
	new OutputStreamWriter(os)); 
	out.println("<HTML>"); 
	out.println("<H2><P align=center>"+"Species Code"+"</P align=center></H2><P> ");
        SpeciesDef sd=this.getByCode(code);

        int mm = -9;  
        if (sd.latinName.indexOf("http") > -1) mm = sd.latinName.indexOf("http")-1;
        String txt= sd.latinName;
        if(mm > 1) txt = "<a href="+sd.latinName.substring(mm+1,sd.latinName.length())+">"+sd.latinName.substring(0,mm)+"</a>";
        out.println("<BR>Baumart: "+sd.code+" "+sd.shortName+" "+sd.longName+"  "+txt+"");         
        out.println("</P></TABLE>"); 
	out.println("<br>"+"created by ForestSimulatorBWINPro </br></HTML>"); 
	out.close();    
        return filename;            
    }
}
