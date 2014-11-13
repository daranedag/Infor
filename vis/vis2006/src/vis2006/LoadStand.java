/*
 * LoadStand.java
 *
 * Created on 15. Februar 2006, 14:12
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package vis2006;
import java.sql.*;
import DatabaseManagement.*;
import treegross.base.*;
import javax.swing.*;
import java.text.*;
import java.util.HashSet;

/**
 *
 * @author nagel
 */
public class LoadStand {
    boolean abbrechen = false;
    
    /** Creates a new instance of LoadStand */
    public LoadStand() {
    }
    
    public Stand loadFromDB(JFrame jFrame,
                DBConnection dbconn,
                Stand stl,
                String idx,
                int selectedAufn,
                boolean missingDataAutomatisch,
                boolean missingDataReplace,
                boolean includeRand){

        String flaechenName="";
        String abtName="";
        Statement stVersfl = null;
        ResultSet rsVersfl = null;
        int vorAuf = 0;
        int nachAuf = 0;

        try{
           stVersfl = dbconn.Connections[0].createStatement();
           rsVersfl = stVersfl.executeQuery("SELECT * from Versfl where edv_id = \'"+idx.substring(0, 6)+"\'  ");
           if (rsVersfl.next()) {
              flaechenName= rsVersfl.getObject("forstamt").toString().trim();
              abtName=rsVersfl.getObject("abt").toString().trim();
           }
       	} catch (Exception ex){
            ex.printStackTrace();
        } finally{
            try{
                if(rsVersfl != null) rsVersfl.close();
                if(stVersfl != null) stVersfl.close();
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        java.io.File f= new java.io.File(" ");
        String localPath="";
        try {
            localPath= f.getCanonicalPath();
        } catch ( Exception e){
            e.printStackTrace();
        }
        stl.setProgramDir(localPath);
//        System.out.println(localPath);        
        System.out.println("Load "+idx+" "+selectedAufn);
        stl.addName(flaechenName+" "+abtName+" Auf: "+(new Integer(selectedAufn)).toString());
//                   (String)(jComboBox5.getSelectedItem()));
        Statement stmt = null;
        ResultSet rs = null;
        try{
           stmt = dbconn.Connections[0].createStatement();
           rs = stmt.executeQuery("SELECT auf,Jahr,flha FROM Auf WHERE edvid = \'"+idx+"\' ORDER BY auf;");
           int vorAufTemp = 0;
           while (rs.next()) {
               int aufx = rs.getInt("auf");
               if(aufx == selectedAufn){
                   stl.year = rs.getInt("Jahr");
                   stl.addsize(rs.getDouble("flha"));
                   vorAuf = vorAufTemp;
               }
               else if(aufx > selectedAufn){
                   nachAuf = aufx;
                   break;
               }
               vorAufTemp = aufx;
           }
       	} catch (Exception e)  {System.out.println(e); }
        finally{
            try{
                if(rs != null) rs.close();
                if(stmt != null) stmt.close();
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
        stl.ntrees=0;
        stl.nspecies=0;
        stl.ncpnt=0;
        int ndh=0;
 // Bäume hinzufügen
        Statement stmt2 = null;
        ResultSet rs2 = null;
        try{          
           stmt2= dbconn.Connections[0].createStatement();
           String sql ="SELECT * FROM Baum WHERE edvid = \'"+idx+   //Sortieren -> gleiche Reihenfolge der Arten z.B. im Ergebnisbogen
                   "\' AND auf = " + selectedAufn + " ORDER BY art, nr";
           rs2 = stmt2.executeQuery(sql);
           while (rs2.next()) {
             String edv = rs2.getObject("edvid").toString();
             edv=edv.trim();
             if (edv==null || edv.length()<1) break;
             int art = rs2.getInt("art");
             String nr = rs2.getObject("nr").toString();
             int anzahl = rs2.getInt("anzahl");
             int age =(int)Math.round((rs2.getDouble("alt")));
             String aus ="";
             Object auso=rs2.getObject("a");
             if (auso != null) aus=auso.toString().trim();
//             System.out.println(nr);
             String ein ="";
             Object eino=rs2.getObject("e");
             if (eino != null) ein=eino.toString().trim();
             int g0 = rs2.getInt("g0");
             int g5 = rs2.getInt("g5");
             int g10 = rs2.getInt("g10");
             int g15 = rs2.getInt("g15");
             int g20 = rs2.getInt("g20");
             int g25 = rs2.getInt("g25");
             int g30 = rs2.getInt("g30");
             int g35 = rs2.getInt("g35");
             double kb=(g0+g5+g10+g15+g20+g25+g30+g35)/40.0;
             String zfx ="";
             Object zfxo=rs2.getObject("zf");
             if (zfxo != null) zfx=zfxo.toString().trim();
             int zf=0;
             if (zfx.compareTo("z")==0 || zfx.compareTo("Z")==0) zf=1;
             String oux ="";
             Object ouxo=rs2.getObject("ou");
             if (ouxo != null) oux=ouxo.toString().trim();
             int ou=0;
             if (oux.indexOf("o")>-1 || oux.indexOf("O")>-1) ou=1;
             if (oux.compareTo("u")>-1 || oux.compareTo("U")>-1) ou=2;
             String rx ="";
             Object rxo=rs2.getObject("r");
             if (rxo != null) rx=rxo.toString().trim();
             int r=0; 
             if (rx.length() > 0) r=1;
             if (ein != null) ein=ein.trim(); else ein="";
             int out=-1;
// Der einfache Einwachser wurde mit -99 kodiert, jetzt als Einw
// Der einfache ausscheidende mit der Jahreszahl             
             String rm="";
             if (aus.length()>0) out=stl.year;
             int outr = 0; 
             if (aus.length()> 0){
                 if (aus.equalsIgnoreCase("D")) outr = 1;
                 else if (aus.equalsIgnoreCase("T")) outr = 2;
                 else if (aus.equalsIgnoreCase("W")) outr = 3;
                 else outr = 4;
             }
             if (ein.trim().length()>0 ) rm="Einw";
//           double d = ex.getDouble("dmess")/10.0;
             double d = rs2.getDouble("d")/10.0;
             double h = rs2.getDouble("h")/10.0;
             double ka = rs2.getDouble("k")/10.0;
             double fac = rs2.getDouble("repfl");
             if (anzahl==0) {
                 anzahl=1; fac=0.0;}
             if (r==1) {
                 fac=0;
                 rm = "Randb;"+rm;
             }
             int nx=0;
             if (h > 0) ndh=ndh+1;
             if (d > 0 && (r < 1 || includeRand)){
                 //System.out.println(nr + " " + art);
               for( int i=0;i<anzahl;i++) { // cm-Listen auflösen
                  stl.addtreeNFV(art,nr,age,out, d,h,ka, kb,-9.0,-9.0,-9.0,-9.0,zf,nx,nx,ou, fac, rm); }
                  if (out > 0) stl.tr[stl.ntrees-1].outtype=2;
                  if (outr > 0) stl.tr[stl.ntrees-1].mortalityReason = outr;
             } 
          }         
        System.out.println("fertig");
       	}catch (Exception ex){
            ex.printStackTrace();
        }finally{
            try{
                if(rs2 != null) rs2.close();
                if(stmt2 != null) stmt2.close();
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        // Vorhandene Arten <- keine zusätzlichen Arten durch Verwendung anderer Höhen (14.10.2010)
        HashSet arten = new HashSet<Integer>();
        for(int n = 0; n < stl.nspecies; n ++) arten.add(stl.sp[n].code);
        
             
//
// keine Höhen vorhanden, dann Voraufnahme
//        
        if (ndh == 0 && selectedAufn > 0 && vorAuf > 0) {
           Statement stmt3 = null;
           ResultSet rs3 = null;
            try{
              stmt3 = dbconn.Connections[0].createStatement();
              rs3 = stmt3.executeQuery("SELECT * FROM Baum WHERE edvid = \'"+idx+"\' "
                      + "AND auf = " + vorAuf +" And h > 0 "
                      + " AND (trim(r)='' OR r is null)" );    //   15.11.2010, 7.7.2011, 9.8.2011
              while (rs3.next()) {
                String edv = rs3.getObject("edvid").toString();
                edv=edv.trim();
                if (edv==null || edv.length()<1) break;
                int art = rs3.getInt("art");
                String nr = rs3.getObject("nr").toString();
                int anzahl = rs3.getInt("anzahl");
                int age =(int)(rs3.getDouble("alt"));
                String aus = "";
                Object ausobj = rs3.getObject("a");
                if(ausobj != null) aus = ausobj.toString().trim();  // 2.2.2011
                String ein = "";
                Object einobj = rs3.getObject("e");                 // 9.8.2011
                if(einobj != null) ein = einobj.toString().trim();
                String zfx ="";
                Object zfxo=rs3.getObject("zf");
                if (zfxo != null) zfx=zfxo.toString().trim();
                int zf=0;
                if (zfx.compareTo("z")==0 || zfx.compareTo("Z")==0) zf=1;
                String oux ="";
                Object ouxo=rs3.getObject("ou");
                if (ouxo != null) oux=ouxo.toString().trim();
                int ou=0;
                if (oux.indexOf("o")>-1 || oux.indexOf("O")>-1) ou=1;
                if (oux.indexOf("u")>-1 || oux.indexOf("U")>-1) ou=2;
                //String rx ="";                     15.11.2010
                //Object rxo=rs3.getObject("r");
                //if (rxo != null) rx=rxo.toString().trim();
                //int r=0;
                //if (rx.compareTo("r")==0 || rx.compareTo("R")==0) r=1;
                int out=-1;
                if (aus.length()>0) out=stl.year;
                if (ein.length()>0 ) out=-99;
                double d = rs3.getDouble("d")/10.0;
                double h = rs3.getDouble("h")/10.0;
                double ka = rs3.getDouble("k")/10.0;
                double fac = rs3.getDouble("repfl");
                String rm="vorherige HK;";
                if (anzahl==0) {anzahl=1; fac=0.0;}
                //if (r==1) anzahl=0;             15.11.2010
                if (h > 0) ndh=ndh+1;
                if (d > 0 && anzahl > 0.0 && arten.contains(art)){
                     stl.addtreeNFV(art,nr,age,out, d,h,ka, -9.0, -9.0,-9.0,-9.0,-9.0,zf,0,0,ou,  0.0, rm);
                } 
             }
          }	catch (Exception ex){
              ex.printStackTrace();
              String text = "Fehler beim Zusammenstellen der verwendbaren Höhen aus der Voraufnahme!";
              javax.swing.JOptionPane.showMessageDialog (jFrame, text, "Fehler", JOptionPane.ERROR_MESSAGE);
          } finally{
            try{
                if(rs3 != null) rs3.close();
                if(stmt3 != null) stmt3.close();
            } catch(Exception ex){
                ex.printStackTrace();
            }
            }

        } 
        
//
// keine Höhen vorhanden, dann Folgeaufnahme
//        
        if (ndh == 0 && selectedAufn > 0 && nachAuf > 0) {
           Statement stmt4 = null;
           ResultSet rs4 = null;

            try{
              stmt4 = dbconn.Connections[0].createStatement();
              rs4 = stmt4.executeQuery("select * from Baum where edvid = \'"+idx+"\' "
                      + " AND auf = " + nachAuf +" And h > 0 "
                      + " AND (trim(r)='' OR r is null)" );    //   15.11.2010, 7.7.2011, 9.8.2011
              while (rs4.next()) {
                String edv = rs4.getObject("edvid").toString();
                edv=edv.trim();
                if (edv==null || edv.length()<1) break;
                int art = rs4.getInt("art");
                String nr = rs4.getObject("nr").toString();
                int anzahl = rs4.getInt("anzahl");
                int age =(int)(rs4.getDouble("alt"));
                String aus = "";
                Object ausobj = rs4.getObject("a");
                if(ausobj != null) aus = ausobj.toString().trim();   // 9.8.2011
                String ein = "";
                Object einobj = rs4.getObject("e");                 // 9.8.2011
                if(einobj != null) ein = einobj.toString().trim();
                String zfx = rs4.getObject("zf").toString().trim();
                int zf=0;
                if (zfx.compareTo("z")==0 || zfx.compareTo("Z")==0) zf=1;
                String oux = rs4.getObject("ou").toString().trim();
                int ou=0;
                if (oux.indexOf("o")>-1 || oux.indexOf("O")>-1) ou=1;
                if (oux.indexOf("u")>-1 || oux.indexOf("U")>-1) ou=2;
                //String rx = rs4.getObject("r").toString().trim();    15.11.2010
                //int r=0;
                //if (rx.compareTo("r")==0 || rx.compareTo("R")==0) r=1;
                int out=-1;
                if (aus.length()>0) out=stl.year;
                if (ein.length()>0 ) out=-99;
                double d = rs4.getDouble("d")/10.0;
                double h = rs4.getDouble("h")/10.0;
                double ka = rs4.getDouble("k")/10.0;
                double fac = rs4.getDouble("repfl");
                String rm="nächste HK;";
                if (anzahl==0) {anzahl=1; fac=0.0;}
                // if (r==1) anzahl=0;  15.11.2010
                if (h > 0) ndh=ndh+1;
                if (d > 0 && anzahl > 0.0 && arten.contains(art)){
                   stl.addtreeNFV(art,nr,age,out, d,h,ka, -9.0, -9.0,-9.0,-9.0,-9.0,zf,0,0,ou,0.0,rm); 
                } 
             }
          }	catch (Exception e){
              e.printStackTrace();
              String text = "Fehler beim Zusammenstellen der verwendbaren Höhen aus der Folgeaufnahme!";
              javax.swing.JOptionPane.showMessageDialog (jFrame, text, "Fehler", JOptionPane.ERROR_MESSAGE);

          } finally{
            try{
                if(rs4 != null) rs4.close();
                if(stmt4 != null) stmt4.close();
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
        }    
//
//  koordinaten hinzufügen
//
        Statement stmt5 = null;
        Statement stmt51 = null;
        Statement stmt52 = null;
        ResultSet rs5 = null;
        ResultSet rs51 = null;
        ResultSet rs52 = null;
        try{
            stmt5 = dbconn.Connections[0].createStatement();
            rs5 = stmt5.executeQuery("SELECT * FROM Stammv WHERE edvid = \'"+idx+"\' " +
                    " AND LEFT(trim(nr),3) <> 'ECK' ORDER BY nr");
            while (rs5.next()) {
                double xp = rs5.getDouble("x");
                double yp = rs5.getDouble("y");
                String nox = rs5.getObject("nr").toString();
                nox=nox.trim();
                int artx = rs5.getInt("art");
                for (int i=0; i<stl.ntrees;i++) {
                    if ( (nox.compareTo(stl.tr[i].no.trim())==0) && (artx==stl.tr[i].code) 
                            && xp > -999 && yp > -999){ // 16.10.2010
                        stl.tr[i].x=xp;
                        stl.tr[i].y=yp;
                    }
                }
            }

            // Eckpunkte hinzufügen
            int necksets = 0;
            int[] aufs = new int[50];
            stmt51 = dbconn.Connections[0].createStatement();
            rs51 = stmt51.executeQuery("SELECT DISTINCT auf FROM Stammv WHERE LEFT(trim(nr),3)='ECK'" +
                    " AND edvid = '"+idx+"' ORDER BY auf;");
            while(rs51.next()){
               aufs[necksets] = rs51.getInt("auf");
               necksets++;
            }
            int eckauf = aufs[0];
            if (necksets > 1) for(int i = 0; i < necksets; i++){
                if(aufs[i] <= selectedAufn) eckauf = aufs[i];
            }
            stmt52 = dbconn.Connections[0].createStatement();
            rs52 = stmt52.executeQuery("SELECT * FROM Stammv WHERE LEFT(trim(nr),3)='ECK'" +
                    " AND edvid = '"+idx+"' AND auf = "+eckauf+" ORDER BY nr;");
            while(rs52.next()){
                Object test = rs52.getObject("nr");
                String nox = "";
                if(test != null) nox = test.toString().trim();
                double xp = rs52.getDouble("x");
                double yp = rs52.getDouble("y");
                stl.addcornerpoint(nox, xp, yp, 0.0);
                stl.center.no="polygon";
            }

          } catch (Exception ex){ex.printStackTrace();
          } finally{
            try{
                if(rs5 != null) rs5.close();
                if(rs51 != null) rs51.close();
                if(rs52 != null) rs52.close();
                if(stmt5 != null) stmt5.close();
                if(stmt51 != null) stmt51.close();
                if(stmt52 != null) stmt52.close();
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
//data quality
            for (int i=0; i<stl.ntrees;i++) {
                if (stl.tr[i].d > 0) stl.tr[i].remarks=stl.tr[i].remarks+"D;";
                else stl.tr[i].remarks=stl.tr[i].remarks+"d";
                if (stl.tr[i].h > 0) stl.tr[i].remarks=stl.tr[i].remarks+"H;";
                else stl.tr[i].remarks=stl.tr[i].remarks+"h;";
                if (stl.tr[i].cb > 0) stl.tr[i].remarks=stl.tr[i].remarks+"A;";
                else stl.tr[i].remarks=stl.tr[i].remarks+"a;";
                if (stl.tr[i].cw > 0) stl.tr[i].remarks=stl.tr[i].remarks+"B;";
                else stl.tr[i].remarks=stl.tr[i].remarks+"b;";
                if (stl.tr[i].x != -9.0 && stl.tr[i].y != -9.0 ) stl.tr[i].remarks=stl.tr[i].remarks+"X";
                else stl.tr[i].remarks=stl.tr[i].remarks+"x";
            }
// an dieser Stelle werden alle gemessenen Höhen nach hmeasured in Tree gespeichert.
// Dadurch wird es möglich später verschiedene Höhenkurven zu erzeugen           
        for (int i=0;i<stl.ntrees;i++){
            if(stl.tr[i].h > 0){
                stl.tr[i].hMeasuredValue=stl.tr[i].h; 
            } else stl.tr[i].hMeasuredValue=0.0;
        } 
            
       if (missingDataReplace)
             for (int i=0;i<stl.ntrees;i++) if(stl.tr[i].h > 0) stl.tr[i].h=0.0;
//
// replace missing data at all             
       if (missingDataReplace){      
// update missing data automatically
        if (missingDataAutomatisch!= true) {
            MissingDataDialog mdDialog = new MissingDataDialog(jFrame,true,stl);
            mdDialog.setVisible(true);
            if(!mdDialog.weiter) abbrechen = true;
        }
        else {  
            GenerateMissingHeights gmh = new GenerateMissingHeights();
            gmh.replaceMissingHeights(stl,true);
        }
        if (!abbrechen){
            stl.sortbyd();
            stl.missingData();
            stl.descspecies();
            int kk=0;
            GenerateXY genxy = new GenerateXY();
            genxy.zufall(stl);
            stl.descspecies();
        } 
       }
        if(!abbrechen){
// Ersatz fehlender Daten Ende             
        int nxx=0;
        for (int i=1;i<stl.ntrees;i++) if(stl.tr[i].ou==2) nxx=nxx+1;            
        }
        else stl=stl.clone(); // stl leeren

        return stl;
    }
    
    
    public Stand loadBZE(JFrame jFrame,
                DBConnection dbconn,
                Stand stl,
                String idx,
                boolean missingDataAutomatisch,
                boolean missingDataReplace){

        java.io.File f= new java.io.File(" ");
        String localPath="";
        try {
            localPath= f.getCanonicalPath();
        } catch ( Exception e){
            e.printStackTrace();
        }
        stl.setProgramDir(localPath);
//        System.out.println(localPath);        
        System.out.println("Load "+idx);
        stl.addName(idx);
        
        Double flaeche = 0.1; // Kreis mit Radius 17.84 m
        stl.addsize(flaeche); 
          

// Daten lesen und in st1 speichern       
        stl.ntrees=0;
        stl.nspecies=0;
        stl.ncpnt=0;

        double xp=0.0; double yp=0.0; double radius=0.0;
        radius=Math.sqrt(10000.0*stl.size/Math.PI);
        stl.center.no="circle";
        stl.center.x =radius;
        stl.center.y =radius;
        stl.center.z =0.0;
        for (int i=0;i<18;i++){
            xp=radius+Math.sin(Math.PI*i*20.0/180.0)*radius;
            yp=radius+Math.cos(Math.PI*i*20.0/180.0)*radius;
            //System.out.println(xp+" "+yp);
            stl.addcornerpoint("ECKP",xp,yp,0.0);
        }

        int ndh=0;
        double fl1 = 0.01; // Kreis mit Radius 5.64 m
        double fl2 = 0.05; // Kreis mit Radius 12.62 m
        Statement stmt = null;
        ResultSet rs = null;
        try{          
           stmt= dbconn.Connections[0].createStatement();
           String sql ="SELECT * FROM BaumBZE WHERE plot = \'"+idx+   //Sortieren -> gleiche Reihenfolge der Arten z.B. im Ergebnisbogen
                   "\' ORDER BY art, nr";
           rs = stmt.executeQuery(sql);
           SimpleDateFormat jahrform = new SimpleDateFormat("yyyy");
           SimpleDateFormat monatsform = new SimpleDateFormat("MM");
           while (rs.next()) {
             int art = rs.getInt("art");
             String nr = rs.getObject("nr").toString().trim();
             int pk = rs.getInt("pk");          
             java.util.Date datum = rs.getDate("aufdatum");
             stl.year = Integer.parseInt(jahrform.format(datum).toString());
             int monat = Integer.parseInt(monatsform.format(datum).toString());
             
             // Alter
             AltersDezimale aDez = new AltersDezimale();
             int age = (int)Math.round(stl.year + aDez.getAltersdezimale(monat) - rs.getInt("begrjahr"));
             String oux ="";
             Object ouxo=rs.getObject("schicht");
             if (ouxo != null) oux=ouxo.toString().trim();
             int schicht = 0;
             if(oux.equals("Oberschicht")) schicht = 1;
             if(oux.equals("Mittelschicht")) schicht = 2;
             if(oux.equals("Unterschicht")) schicht = 3;
             if(oux.equals("Überhälter")) schicht = 4;
             int ou=0;
             if (oux.equalsIgnoreCase("Oberschicht") | oux.equalsIgnoreCase("Überhälter")) ou=1;
             if (oux.equalsIgnoreCase("Unterschicht") | oux.equalsIgnoreCase("Mittelschicht")) ou=2;
             int out=-1;        
             String rm="";
             double d = rs.getDouble("d")/10.0;
             double h = rs.getDouble("h")/10.0;
             double ka = rs.getDouble("k")/10.0;
             double fac = 1;
             // Umrechnung auf die Fläche des äußersten Probekreises
             if(pk == 1) fac = 1/fl1*flaeche;   // fac muss >= 1 sein (grundsätzlich)
             if(pk == 2) fac = 1/fl2*flaeche;   // und eine ganze Zahl (hier, s.u)
             if(pk < 0) fac = 0;    // Bäume außerhalb des Probekreises, deren Höhe berücksichtigt werden kann
             // Koordinaten
             double dist = rs.getDouble("entf");
             int winkel = rs.getInt("winkel");
             double x=dist*Math.sin(winkel*Math.PI/200) +radius; // keine negativen Koordinaten
             double y=dist*Math.cos(winkel*Math.PI/200) +radius;

             int nx=0;
             if (h > 0) ndh=ndh+1;
             if (d > 0){
                 if(pk == 3 || pk < 0){
                     if(pk < 0) nr = "nurH";
                     stl.addtreeNFV(art,nr,age,out,d,h,ka,-9,-9.0,x,y,-9.0,0,nx,nx,ou,fac,rm);
                     stl.tr[stl.ntrees-1].layer=schicht;
                 }
                 else{
                     stl.addtreeNFV(art,nr,age,out,d,h,ka,-9,-9.0,x,y,-9.0,0,nx,nx,ou,1,rm);
                     stl.tr[stl.ntrees-1].layer=schicht;
                     for(int i = 0; i < (fac-1); i++){
                         stl.addtreeNFV(art,nr,age,out,d,h,ka,-9,-9.0,-9.0,-9.0,-9.0,0,nx,nx,ou,1,rm);
                         stl.tr[stl.ntrees-1].layer=schicht;
                     }

                 }
             }
          }
        System.out.println("fertig");
       	}catch (Exception ex){
            ex.printStackTrace();
        }finally{
            try{
                if(rs != null) rs.close();
                if(stmt != null) stmt.close();
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }


// Keine Höhenkurve aus anderen Aufnahmen möglich              
     
//data quality
            for (int i=0; i<stl.ntrees;i++) {
                if (stl.tr[i].d > 0) stl.tr[i].remarks=stl.tr[i].remarks+"D;";
                else stl.tr[i].remarks=stl.tr[i].remarks+"d";
                if (stl.tr[i].h > 0) stl.tr[i].remarks=stl.tr[i].remarks+"H;";
                else stl.tr[i].remarks=stl.tr[i].remarks+"h;";
                if (stl.tr[i].cb > 0) stl.tr[i].remarks=stl.tr[i].remarks+"A;";
                else stl.tr[i].remarks=stl.tr[i].remarks+"a;";
                if (stl.tr[i].cw > 0) stl.tr[i].remarks=stl.tr[i].remarks+"B;";
                else stl.tr[i].remarks=stl.tr[i].remarks+"b;";
                if (stl.tr[i].x != -9.0 && stl.tr[i].y != -9.0 ) stl.tr[i].remarks=stl.tr[i].remarks+"X";
                else stl.tr[i].remarks=stl.tr[i].remarks+"x";
            }
// an dieser Stelle werden alle gemessenen Höhen nach hmeasured in Tree gespeichert.
// Dadurch wird es möglich später verschiedene Höhenkurven zu erzeugen           
        for (int i=0;i<stl.ntrees;i++){
            if(stl.tr[i].h > 0){
                stl.tr[i].hMeasuredValue=stl.tr[i].h; 
            } else stl.tr[i].hMeasuredValue=0.0;
        } 
            
       if (missingDataReplace)
             for (int i=0;i<stl.ntrees;i++) if(stl.tr[i].h > 0) stl.tr[i].h=0.0;
//
// replace missing data at all             
       if (missingDataReplace){      
// update missing data automatically
        if (missingDataAutomatisch!= true) {
            MissingDataDialog mdDialog = new MissingDataDialog(jFrame,true,stl);
            mdDialog.setVisible(true);
            if(!mdDialog.weiter) abbrechen = true;
        }
        else {  
            GenerateMissingHeights gmh = new GenerateMissingHeights();
            gmh.replaceMissingHeights(stl,true);
        }
        if (!abbrechen){
            stl.sortbyd();
            stl.missingData();
            stl.descspecies();
            GenerateXY genxy = new GenerateXY();
            genxy.zufall(stl);
            stl.descspecies();
        } 
       }

        //for (int i = 0; i < stl.ntrees; i++){
        //    System.out.println("x="+stl.tr[i].x+" y="+stl.tr[i].y);
        //}
        if (!abbrechen) {
// Ersatz fehlender Daten Ende             
            int nxx = 0;
            for (int i = 1; i < stl.ntrees; i++) if (stl.tr[i].ou == 2) nxx = nxx + 1;
        } else stl = stl.clone(); // stl leeren

        return stl;
    }
     
    public void saveXYToDB(DBConnection dbconn,Stand st, String idSelected, int auf){
       for (int i=0;i<st.ntrees;i++) 
           if (st.tr[i].remarks.equals((String)"xychanged") ){
                try{                    
                    boolean neuerEintrag=true;
                    Statement stmt = dbconn.Connections[0].createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM Stammv WHERE (trim(nr) = '"+st.tr[i].no.trim()+"' " +
                            "AND art = "+st.tr[i].code+
                           " AND edvid = '"+idSelected+"' )");
                    while (rs.next()) { neuerEintrag=false; }
                    if (neuerEintrag) {
                        int maxId = 0;
                        rs = stmt.executeQuery("SELECT max(id) AS maxid FROM Stammv WHERE edvid = \'"+idSelected+"\' ");
                        if (rs.next())  maxId=rs.getInt("maxid");
                        maxId=maxId+1;
                        Double xx = new Double(st.tr[i].x);
                        Double yy = new Double(st.tr[i].y);
                        stmt.execute("INSERT INTO Stammv ( id, edvid, nr, art, auf, x, y, z) "+
                              "values ( "+maxId+", '"+idSelected+"','"+st.tr[i].no+"',"+st.tr[i].code+",1, "+
                              xx.toString()+" , "+yy.toString()+", 0.0)"); 
                    }
                    else {
                       stmt.execute("UPDATE Stammv SET x = "+st.tr[i].x+" , y = "+st.tr[i].y+
                               " WHERE (trim(nr) = '"+st.tr[i].no.trim()+"'  AND " +
                               " art = "+st.tr[i].code+" AND"+
                               " edvid = '"+idSelected+"' )");
                    }
              stmt.close();

                }
                catch (Exception e){  System.out.println("Datenbank Stammv :"+e); }

           }
       for (int i=0;i<st.ncpnt;i++){
           if(st.cpnt[i].remarks!= null && st.cpnt[i].remarks.equals("xychanged")){
               try{
                   Statement stmt = dbconn.Connections[0].createStatement();
                   stmt.execute("UPDATE Stammv SET x = "+st.cpnt[i].x+" , y = "+st.cpnt[i].y+
                           " WHERE (trim(nr) = '"+st.cpnt[i].no.trim()+"'  AND " +
                           "edvid = '"+idSelected+"' )");
                   stmt.close();    
               }
               catch (Exception e){  System.out.println("Datenbank Stammv :"+e); }
           // Neue Punkte, die nicht in st.tr vorhanden sind (Kennzeichen: art in remarks)
           } else if(st.cpnt[i].remarks != null){                      // 29.10.2010 
               Statement stmt = null;
               try{
                   stmt = dbconn.Connections[0].createStatement();
                   stmt.execute("INSERT INTO Stammv(edvid, nr, art, auf, x, y, z) "
                           + "VALUES ('"+idSelected+"','"+st.cpnt[i].no+"',"+ st.cpnt[i].remarks +"," + auf + ", "+
                              st.cpnt[i].x+" , " + st.cpnt[i].y + "," + st.cpnt[i].z + ")"); 
                   
               }catch(Exception e){
                   e.printStackTrace();
               } finally{
                   try{
                       if(stmt != null) stmt.close();
                   }catch(Exception e){}
               }
           }
        }
       
    }
    
}
