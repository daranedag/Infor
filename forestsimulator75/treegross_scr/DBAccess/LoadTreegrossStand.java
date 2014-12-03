/*
 * LoadTreegrossStand.java
 *
 * Created on 15. Februar 2006, 14:12
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package treegross.DBAccess;
import java.sql.*;
import treegross.base.*;
import treegross.treatment.*;
import javax.swing.*;

/**
 *
 * @author nagel
 */
public class LoadTreegrossStand {
    
    int schritte =0;
    int ebaum = 0;
    int bestand =0;
    int baumart =0;
    int durchforstung_an =0;
    /** Creates a new instance of LoadTreegrossStand */
    public LoadTreegrossStand() {
    }

    public Stand loadFromDB(DBConnection dbconn,Stand stl, String idx,int selectedAufn, boolean missingDataAutomatisch,
                            boolean missingDataReplace){
       

        String flaechenName="";
        String abtName="";
        try{          
           Statement stmt = dbconn.Connections[0].createStatement(); 
           ResultSet rs = stmt.executeQuery("select * from Versfl where edv_id = \'"+idx.substring(0, 6)+"\'  ");
           if (rs.next()) {
              flaechenName= rs.getObject("oficina forestal").toString();
              abtName=rs.getObject("abt").toString();
           }
       	}	catch (Exception e)  {	System.out.println(e); }	
        java.io.File f= new java.io.File(" ");
        String localPath="";
        try { localPath= f.getCanonicalPath(); } catch ( Exception e){};
//        System.out.println("Load "+idx+" "+selectedAufn);
        String SAuf = new Integer(selectedAufn).toString();
        stl.addName(flaechenName+" "+abtName+" Auf: "+SAuf);
//                   (String)(jComboBox5.getSelectedItem()));
        try{          
           Statement stmt = dbconn.Connections[0].createStatement(); 
           ResultSet rs = stmt.executeQuery("select * from Auf where edvid = \'"+idx+"\' And auf = " + selectedAufn  );
           if (rs.next()) {
              stl.year = rs.getInt("Año");
              stl.addsize(rs.getDouble("flha"));
           }
       	}	catch (Exception e)  {	System.out.println(e); }	
        stl.ntrees=0;
        stl.nspecies=0;
        stl.ncpnt=0;
        int ndh=0;
 // Bäume hinzufügen       
        try{          
           Statement stmt = dbconn.Connections[0].createStatement(); 
           ResultSet rs = stmt.executeQuery("select * from Baum where edvid = \'"+idx+"\' And auf = " + selectedAufn   );
           while (rs.next()) {
             String edv = rs.getObject("edvid").toString();
             edv=edv.trim();
             if (edv==null || edv.length()<1) break;
             int art = rs.getInt("art");
             String nr = rs.getObject("nr").toString();
             int anzahl = rs.getInt("anzahl");
             int age =(int)Math.round((rs.getDouble("alt")));
             String aus ="";
             Object auso=rs.getObject("a");
             if (auso != null) aus=auso.toString().trim();
//             System.out.println(nr);
             String ein ="";
             Object eino=rs.getObject("e");
             if (eino != null) ein=eino.toString().trim();
             int g0 = rs.getInt("g0");
             int g5 = rs.getInt("g5");
             int g10 = rs.getInt("g10");
             int g15 = rs.getInt("g15");
             int g20 = rs.getInt("g20");
             int g25 = rs.getInt("g25");
             int g30 = rs.getInt("g30");
             int g35 = rs.getInt("g35");
             double kb=(g0+g5+g10+g15+g20+g25+g30+g35)/40.0;
             String zfx ="";
             Object zfxo=rs.getObject("zf");
             if (zfxo != null) zfx=zfxo.toString().trim();
             int zf=0;
             if (zfx.compareTo("z")==0 || zfx.compareTo("Z")==0) zf=1;
             String oux ="";
             Object ouxo=rs.getObject("ou");
             if (ouxo != null) oux=ouxo.toString().trim();
             int ou=0;
             if (oux.indexOf("o")>-1 || oux.indexOf("O")>-1) ou=1;
             if (oux.compareTo("u")>-1 || oux.compareTo("U")>-1) ou=2;
             if (aus != null) aus=aus.trim() ;else aus="";
             String rx ="";
             Object rxo=rs.getObject("r");
             if (rxo != null) rx=rxo.toString().trim();
             int r=0; 
             if (rx.compareTo("r")==0 || rx.compareTo("R")==0) r=1;
             if (ein != null) ein=ein.trim(); else ein="";
             aus=aus.trim();
             int out=-1;
// Der einfache Einwachser wurde mit -99 kodiert, jetzt als Einw
// Der einfache ausscheidende mit der Jahreszahl             
             String rm="";
             if (aus.trim().length()>0) 
                              out=stl.year;
             if (ein.trim().length()>0 ) rm="Einw";
//           double d = ex.getDouble("dmess")/10.0;
             double d = rs.getDouble("d")/10.0;
             double h = rs.getDouble("h")/10.0;
             double ka = rs.getDouble("k")/10.0;
             double fac = rs.getDouble("repfl");
              if (anzahl==0) {
                 anzahl=1; fac=0.0;}
// Bäume mit höherem Repräsentationsfaktor als 1 klonen
//
             if (fac >= 2.0){
                 int az = (int) fac;
                 fac = fac /az;
                 anzahl = anzahl*az;
             }
             if (r==1) 
             fac=0;
             int nx=0;
             if (h > 0) ndh=ndh+1;
             if (d > 0){
               for( int i=0;i<anzahl;i++) {
                  String nrx = nr;
                  if (i > 0) nrx=nr+"_"+i;
                  stl.addtreeNFV(art,nrx,age,out, d,h,ka, kb,-9.0,-9.0,-9.0,-9.0,zf,nx,nx,ou, fac, rm); }
                  if (out > 0) stl.tr[stl.ntrees-1].outtype=2;
             } 
          }
       	}	catch (Exception e)  {	System.out.println(e); }	
             System.out.println("fertig");
  
//
//  koordinaten hinzufügen
//        
           try{          
              Statement stmt = dbconn.Connections[0].createStatement(); 
              ResultSet rs = stmt.executeQuery("select * from Stammv where edvid = \'"+idx+"\' ORDER BY nr");
              while (rs.next()) {
                double xp = rs.getDouble("x");
                double yp = rs.getDouble("y");
                String nox = rs.getObject("nr").toString();
                nox=nox.trim();
                int artx = rs.getInt("tipo");
                for (int i=0; i<stl.ntrees;i++) {
                   if ( (nox.compareTo(stl.tr[i].no.trim())==0) && (artx==stl.tr[i].code)){
                       stl.tr[i].x=xp;
                       stl.tr[i].y=yp;
                   }
                }
                if (nox.indexOf("ECK") > -1) {
                       stl.addcornerpoint(nox, xp, yp, 0.0);
                       stl.center.no="polygon";
                } 
             }
          }	catch (Exception e)  {	System.out.println(e); }	
        
//data quality 
/*            for (int i=0; i<stl.ntrees;i++) {
                if (stl.tr[i].d > 0) stl.tr[i].remarks=stl.tr[i].remarks+"D;";
                else stl.tr[i].remarks=stl.tr[i].remarks+"d";
                if (stl.tr[i].h > 0) stl.tr[i].remarks=stl.tr[i].remarks+"H;";
                else stl.tr[i].remarks=stl.tr[i].remarks+"h;";
                if (stl.tr[i].cb > 0) stl.tr[i].remarks=stl.tr[i].remarks+"A;";
                else stl.tr[i].remarks=stl.tr[i].remarks+"a;";
                if (stl.tr[i].cw > 0) stl.tr[i].remarks=stl.tr[i].remarks+"B;";
                else stl.tr[i].remarks=stl.tr[i].remarks+"b;";
                if (stl.tr[i].x > -9.0 && stl.tr[i].y > -9.0 ) stl.tr[i].remarks=stl.tr[i].remarks+"X";
                else stl.tr[i].remarks=stl.tr[i].remarks+"x";
            }
 */
// an dieser Stelle werden alle gemessenen Höhen nach hmeasured in Tree gespeichert.
// Dadurch wird es möglich später verschiedene Höhenkurven zu erzeugen           
/*        for (int i=0;i<stl.ntrees;i++) if(stl.tr[i].h > 0) 
            stl.tr[i].hMeasuredValue=stl.tr[i].h; else stl.tr[i].hMeasuredValue=0.0;
       if (missingDataReplace)
             for (int i=0;i<stl.ntrees;i++) if(stl.tr[i].h > 0) stl.tr[i].h=0.0;
 */
//
// replace missing data at all             
//       if (missingDataReplace){      
// update missing data automatically
/*        if (missingDataAutomatisch!= true) {
            MissingDataDialog mdDialog = new MissingDataDialog(jFrame,true,stl);
            mdDialog.setVisible(true);
        }
        else {  
            GenerateMissingHeights gmh = new GenerateMissingHeights();
            gmh.replaceMissingHeights(stl,true);
        }
*/
/*        stl.sortbyd();
        stl.missingData();
        stl.descspecies();
        int kk=0;
        GenerateXY genxy = new GenerateXY();
        genxy.zufall(stl);
        stl.descspecies();
       }
 */
// Ersatz fehlender Daten Ende             
        int nxx=0;
        for (int i=1;i<stl.ntrees;i++) if(stl.tr[i].ou==2) nxx=nxx+1;
        return stl;
    }
    
    public Stand loadRules(DBConnection dbconn,Stand stl, String idx,int auf){
        durchforstung_an =0;
        try{          
           Statement stmt = dbconn.Connections[0].createStatement(); 
           ResultSet rs = stmt.executeQuery("select * from Vorschrift where (edvid = \'"+idx+" \' AND auf = "+auf+"  )");
           if (rs.next()) {
              int jj = rs.getInt("Accidemte");
              if (jj == 0) stl.randomGrowthEffects=false;else stl.randomGrowthEffects=true;
                  jj = rs.getInt("Crecimiento");
              if (jj == 0) stl.ingrowthActive=false; else stl.ingrowthActive=true;
                  stl.temp_Integer = rs.getInt("Pasos");
              ebaum = rs.getInt("Arbol E");
              bestand = rs.getInt("Valores");
              baumart = rs.getInt("Especie arbol");
              durchforstung_an = rs.getInt("Adelgazamiento");

           }
       	 }   catch (Exception e)  {	System.out.println(e); }
        
        if (durchforstung_an == 1){ 
              stl.distanceDependent=true;
              stl.trule.typeOfThinning=0;
              stl.trule.thinArea=false;
              stl.trule.selectCropTrees=true;
              stl.trule.reselectCropTrees=true;
              stl.trule.selectCropTreesOfAllSpecies=false;
              stl.trule.releaseCropTrees=true;
              stl.trule.cutCompetingCropTrees=false;
              stl.trule.releaseCropTreesSpeciesDependent =true;
              stl.trule.minThinningVolume=0;
              stl.trule.maxThinningVolume=80;
              stl.trule.thinAreaSpeciesDependent =true;
              stl.trule.thinningIntensityArea=0.0;
              stl.trule.minHarvestVolume=0.0;
              stl.trule.maxHarvestVolume=250.0;
              stl.trule.typeOfHarvest=0;
              stl.trule.harvestLayerFromBelow =false;
              stl.trule.maxHarvestingPeriode=6; 
              stl.trule.lastTreatment =0;
              stl.trule.protectMinorities=false;
              stl.trule.nHabitat=0;
              stl.trule.thinningIntensity = 1.0;
              TreatmentElements2 tl = new TreatmentElements2();
              for (int j=0;j< stl.nspecies;j++){
                  stl.sp[j].trule.numberCropTreesWanted=tl.numberOfCropTrees(stl.sp[j], stl.sp[j].spDef.targetDiameter, stl.sp[j].percCSA);
              }
        }
        
        
        return stl;
 
    }
    
    public int getEBaum(){return ebaum;}
    public int getBestand(){return bestand;}
    public int getBaumart(){return baumart;}
    public int getDurchf(){return durchforstung_an;}
    
    public void saveBaum(DBConnection dbconn,Stand st, String ids, int aufs, int sims, int nwieder){
        try{
           Statement stmt = dbconn.Connections[0].createStatement();
           for (int i=0;i <st.ntrees; i++){
               Double dd= st.tr[i].d;
               Double hh= st.tr[i].h;
               Double ka= st.tr[i].cb;
               Double kb= st.tr[i].cw;
               Double vv= st.tr[i].v;
               Double cc66= st.tr[i].c66;
               Double cc66c= st.tr[i].c66c;
               Double cc66xy= st.tr[i].c66xy;
               Double cc66cxy= st.tr[i].c66cxy;
               Double ssi= st.tr[i].si;
               Double xx = st.tr[i].x;
               Double yy = st.tr[i].y;
               int zbx = 0;
               if (st.tr[i].crop==true)zbx=1;
               
               
           
               stmt.execute("INSERT INTO ProgBaum (  edvid, auf, simschritt, wiederholung,nr, art, alt, aus, d, h, ka, kb, v, c66,c66c, c66xy, c66cxy, si,x,y, zb) "+
                     "values (  '"+ids+"', "+aufs+", "+sims+", "+nwieder+",'"+st.tr[i].no+"',"+st.tr[i].code+", "+
                      st.tr[i].age+" , "+st.tr[i].out+" , "+dd.toString()+" , "+hh.toString()+
                      " , "+ka.toString()+" , "+kb.toString()+" , "+vv.toString()+
                      " , "+cc66.toString()+" , "+cc66c.toString()+
                      " , "+cc66xy.toString()+" , "+cc66cxy.toString()+" , "+ssi.toString()+" , "+xx.toString()+" , "+yy.toString()+
                      ", "+zbx+" )");
           }
        
           
           stmt.close();

           }
        catch (Exception e){  System.out.println("Datenbank Stammv :"+e); }
        
    }
    public void saveSpecies(DBConnection dbconn,Stand st, String ids, int aufs, int sims){
        try{
           Statement stmt = dbconn.Connections[0].createStatement();
           for (int i=0; i< st.nspecies;i++){
               Double ggha= st.sp[i].gha;
               Double vvha= st.sp[i].vol;
               Double ddg= st.sp[i].dg;
               Double hhg= st.sp[i].hg;
               Double dd100= st.sp[i].d100;
               Double hh100= st.sp[i].h100;
               Double nnha= st.sp[i].nha;
               Double nnhaa= st.sp[i].nhaout;
               Double gghaa= st.sp[i].ghaout;
               Double aalt= st.sp[i].h100age;
               Double vvhaa = st.sp[i].vhaout;
               Double gpro = 100.0*st.sp[i].gha/st.bha;
               int art = st.sp[i].code;
               
           
               stmt.execute("INSERT INTO ProgArt (  edvid, auf, art, gpro, simschritt, alt, nha, gha, vha,"+
                       "dg,hg,d100,h100,nhaa,ghaa,vhaa) "+
                     "values (  '"+ids+"', "+aufs+", "+art+", "+gpro+", "+sims+","+aalt+","+nnha+", "+
                      ggha+" , "+vvha+" , "+ddg+
                      " , "+hhg+" , "+dd100+" , "+hh100+
                      " , "+nnhaa+" , "+gghaa+" ,"+vvhaa+" )"); 
        
           }
           stmt.close();

           }
        catch (Exception e){  System.out.println("Datenbank Stammv :"+e); }
        
    }
    
    public void saveStand(DBConnection dbconn,Stand st, String ids, int aufs, int sims,int nwieder){
        try{
           Statement stmt = dbconn.Connections[0].createStatement();
               Double ggha= st.bha;
               Double vvha= st.getVhaResidual(0);
               Double ddg= st.dg;
               Double hhg= st.hg;
               Double dd100= st.d100;
               Double hh100= st.h100;
               Double nnha= st.nha;
               Double nnhaa= st.nhaout;
               Double gghaa= st.bhaout;
               Double aalt= 1.0*st.year;
               Double vvhaa = st.getVhaTargetDiameter(0)+st.getVhaThinning(0);
               double vvhaaz = st.getVhaTargetDiameter(0);
           
               stmt.execute("INSERT INTO ProgBestand (  edvid, auf, simschritt, wiederholung, alt, nha, gha, vha,"+
                       "dg,hg,d100,h100,nhaa,ghaa,vhaa, vhaazst) "+
                     "values (  '"+ids+"', "+aufs+", "+sims+", "+nwieder+","+aalt+","+nnha+", "+
                      ggha+" , "+vvha+" , "+ddg+
                      " , "+hhg+" , "+dd100+" , "+hh100+
                      " , "+nnhaa+" , "+gghaa+" ,"+vvhaa+" ,"+vvhaaz+" )");
        
           
           stmt.close();

           }
        catch (Exception e){  System.out.println("Datenbank Stammv :"+e); }
        
    }

    public void saveXMLToDB(DBConnection dbconn,Stand st){
       try{
           Statement stmt = dbconn.Connections[0].createStatement();
                    
          for (int i=0;i<st.ncpnt;i++) {
                  Double xx = new Double(st.cpnt[i].x);
                  Double yy = new Double(st.cpnt[i].y);
                  String nrx = "ECK"+i;
                  stmt.execute("INSERT INTO Stammv ( edvid, nr, art, auf, x, y, z) "+
                              "values (  '"+st.standname+"','"+nrx+"', -99, 1, "+
                              xx.toString()+" , "+yy.toString()+", 0.0)"); 

            }
           for (int i=0;i<st.ntrees;i++) {
                  Double xx = new Double(st.tr[i].x);
                  Double yy = new Double(st.tr[i].y);
                  stmt.execute("INSERT INTO Stammv ( edvid, nr, art, auf, x, y, z) "+
                              "values (  '"+st.standname+"','"+st.tr[i].no+"',"+st.tr[i].code+",1, "+
                              xx.toString()+" , "+yy.toString()+", 0.0)"); 
                 }
           for (int i=0;i<st.ntrees;i++) {
                  int dd = (int)(Math.round(st.tr[i].d*10));
                  int hh = (int)(Math.round(st.tr[i].h*10));
                  int ka = (int)(Math.round(st.tr[i].cb*10));
                  int kb = (int)(Math.round(st.tr[i].cw*5));
                          stmt.execute("INSERT INTO Baum ( edvid, nr, art, auf, anzahl, repfl, mh, alt, dmess,d,"+
                               " h, k, g0, g5, g10, g15, g20, g25, g30, g35 ) "+
                              "values (  '"+st.standname+"','"+st.tr[i].no+"',"+st.tr[i].code+",1,1,1,13, "+
                              st.tr[i].age+","+dd+","+dd+","+hh+", "+ka+", "+kb+","+kb+","+kb+","+kb+","+
                              kb+","+kb+","+kb+","+kb+" )");
                 }

                }
                catch (Exception e){  System.out.println("Datenbank Stammv :"+e); }

    }
    
}
