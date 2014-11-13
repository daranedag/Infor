/*
 * CopyExperimentData.java
 *
 * Created on 7. Februar 2006, 08:49
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package vis2006;
import DatabaseManagement.*;
import java.io.*; 
import java.sql.*;
import java.util.*;

/**
 *
 * @author nagel
 */
public class CopyExperimentData {
    String zieldatei = null;
    
    /** Creates a new instance of CopyExperimentData */
    public CopyExperimentData() {
    }
    public String checkOutExperimentData(String id){
    // Versuch mit allen Parzellen in lokale DB kopieren
        int kontrollZeilen=0;
        kontrollZeilen=AnzahlZeilenWaldwachstum(id, false); //false -> alle Parzellen

        String meldung="";
// Kopieren eines leeren *.mdb Containers
// Prüfen, ob Container vorhanden
        String s ="tempdaten//"+id.substring(0, 6)+".mdb";
        String s2 ="tempdaten//visleer.mdb";
        File f = new File(s);
        File fl = new File(s2);
        String pf=""; String pfl="";
        try{ pf= f.getCanonicalPath(); pfl= fl.getCanonicalPath();} catch (Exception e){};
 
        if (f.exists()==false) {
           VisCopyFile copyFile = new VisCopyFile();
           copyFile.copy(pfl,pf); // s2 (visleer.mdb) nach s (<edvid>.mdb) 
           String idx = id.substring(0, 6);
           String idl = "'"+id.substring(0, 6)+"%' ";
//        
           DBConnection dbconn= new DBConnection(2);     // a class to manage the conection to a database
           dbconn.openDBConnection(DBConnection.MYSQL, 0, "db/waldwachstum", "wwuser", "ww2005", true, true);
           dbconn.openDBConnection(DBConnection.ACCESS, 1, s, "", "", false, true);
           BasicQueries basicQueries= new BasicQueries(dbconn.Connections[0]);
           basicQueries.copyLargeTable(dbconn.Connections[0], "Parz", dbconn.Connections[1], "Parz",
                "Select * FROM Parz where edvid = '"+idx+"'");
           basicQueries.copyLargeTable(dbconn.Connections[0], "Auf", dbconn.Connections[1], "Auf",
                "Select * FROM Auf where edvid LIKE "+idl);
           basicQueries.copyLargeTable(dbconn.Connections[0], "Baum", dbconn.Connections[1], "Baum",
                "Select * FROM Baum where edvid LIKE "+idl);
           basicQueries.copyLargeTable(dbconn.Connections[0], "Stammv", dbconn.Connections[1], "Stammv",
                "Select * FROM Stammv where edvid LIKE "+idl);
           basicQueries.copyLargeTable(dbconn.Connections[0], "Qualit", dbconn.Connections[1], "Qualit",
                "Select * FROM Qualit where edvid LIKE "+idl);
           basicQueries.copyLargeTable(dbconn.Connections[0], "ParzInfo", dbconn.Connections[1], "ParzInfo",
                "Select * FROM ParzInfo where edvid = '"+idx+"' " );
           basicQueries.copyLargeTable(dbconn.Connections[0], "Versfl", dbconn.Connections[1], "Versfl",
                "Select forstamt,abt,vers_zweck,baumart,edv_id FROM Versfl where edv_id = '"+idx+"' " );
           basicQueries.copyLargeTable(dbconn.Connections[0], "Land", dbconn.Connections[1], "Land",
                "Select * FROM Land  ");
           basicQueries.copyLargeTable(dbconn.Connections[0], "Forstaemter", dbconn.Connections[1], "Forstaemter",
                "Select * FROM Forstaemter  ");
           basicQueries.copyLargeTable(dbconn.Connections[0], "PI_Durchforstungsart", dbconn.Connections[1], "PI_Durchforstungsart",
                "Select * FROM PI_Durchforstungsart  ");
           basicQueries.copyLargeTable(dbconn.Connections[0], "PI_Durchforstungsstaerke", dbconn.Connections[1], "PI_Durchforstungsstaerke",
                "Select * FROM PI_Durchforstungsstaerke  ");
           dbconn.closeAll();
           
// Prüfen, ob Anzahl der Zeilen gleich ist
           if (kontrollZeilen==AnzahlZeilenTempdaten(s, id, false)){ //für alle Parzellen (parzOnly = false)
                meldung ="Daten kopiert.";
                zieldatei = f.toString(); 
            }
           else meldung = "FEHLER: Daten nicht vollständig kopiert!" ;
        }
        else 
            meldung = "Datei vorhanden.";
        return meldung;
     }

    public String getZielDatei(){
        return zieldatei;
    }

    public String checkOutParzData(String id, String parz){
    // Einzelne Parzelle in lokale DB kopieren
        int kontrollZeilen=0;
        kontrollZeilen=AnzahlZeilenWaldwachstum(id, true);

        String meldung="";
    // Kopieren eines leeren *.mdb Containers
    // Prüfen, ob Container vorhanden
        String s ="tempdaten//"+id+".mdb";
        String s2 ="tempdaten//visleer.mdb";
        File f = new File(s);
        File fl = new File(s2);
        String pf=""; String pfl="";
        try{ pf= f.getCanonicalPath(); pfl= fl.getCanonicalPath();} catch (Exception e){};
        if (f.exists()==false) {
           VisCopyFile copyFile = new VisCopyFile();
           copyFile.copy(pfl,pf);  
     
           DBConnection dbconn= new DBConnection(2);     // a class to manage the conection to a database
           dbconn.openDBConnection(DBConnection.MYSQL, 0, "db/waldwachstum", "wwuser", "ww2005", true, true);
           dbconn.openDBConnection(DBConnection.ACCESS, 1, s, "", "", false, true);
           BasicQueries basicQueries= new BasicQueries(dbconn.Connections[0]);
           basicQueries.copyLargeTable(dbconn.Connections[0], "Parz", dbconn.Connections[1], "Parz",
                "Select * FROM Parz where id = '"+id+"'");
           basicQueries.copyLargeTable(dbconn.Connections[0], "Auf", dbconn.Connections[1], "Auf",
                "Select * FROM Auf where edvid = '"+id+"'");
           basicQueries.copyLargeTable(dbconn.Connections[0], "Baum", dbconn.Connections[1], "Baum",
                "Select * FROM Baum where edvid = '"+id+"'");
           basicQueries.copyLargeTable(dbconn.Connections[0], "Stammv", dbconn.Connections[1], "Stammv",
                "Select * FROM Stammv where edvid = '"+id+"'");
           basicQueries.copyLargeTable(dbconn.Connections[0], "Qualit", dbconn.Connections[1], "Qualit",
                "Select * FROM Qualit where edvid = '"+id+"'");
           basicQueries.copyLargeTable(dbconn.Connections[0], "ParzInfo", dbconn.Connections[1], "ParzInfo",
                "Select * FROM ParzInfo where id = '"+id+"' " );
           basicQueries.copyLargeTable(dbconn.Connections[0], "Versfl", dbconn.Connections[1], "Versfl",
                "Select forstamt,abt,vers_zweck,baumart,edv_id FROM Versfl where edv_id = '"+id.substring(0,6)+"' " );
           basicQueries.copyLargeTable(dbconn.Connections[0], "Land", dbconn.Connections[1], "Land",
                "Select * FROM Land  ");
           basicQueries.copyLargeTable(dbconn.Connections[0], "Forstaemter", dbconn.Connections[1], "Forstaemter",
                "Select * FROM Forstaemter  ");
           basicQueries.copyLargeTable(dbconn.Connections[0], "PI_Durchforstungsart", dbconn.Connections[1], "PI_Durchforstungsart",
                "Select * FROM PI_Durchforstungsart  ");
           basicQueries.copyLargeTable(dbconn.Connections[0], "PI_Durchforstungsstaerke", dbconn.Connections[1], "PI_Durchforstungsstaerke",
                "Select * FROM PI_Durchforstungsstaerke  ");
           dbconn.closeAll();
// Prüfen, ob Anzahl der Zeilen gleich ist
           if (kontrollZeilen==AnzahlZeilenTempdaten(s, id, true)){ //Pfad, edvid, parzOnly
                meldung ="Daten kopiert";
                zieldatei = f.toString();
           }
           
           else meldung = "FEHLER: Daten nicht vollständig kopiert!" ; 
        }
        else meldung ="Datei vorhanden";    
        return meldung;
     }
    
    
    public String addTempDaten(String ids, String quellPfad, boolean parzOnly){
// Daten aus der lokalen DB in die WW-DB übertragen
        // Datum
        Calendar kal = Calendar.getInstance();
        Integer month = kal.get(Calendar.MONTH)+1;  
        String monthStr = month.toString();
        if (monthStr.length()<2) monthStr="0"+monthStr;
        Integer day = kal.get(Calendar.DAY_OF_MONTH);
        String dayStr = day.toString();
        if (dayStr.length()<2) dayStr="0"+dayStr;
        Integer year = kal.get(Calendar.YEAR);
        String datumStr=year.toString()+"-"+monthStr+"-"+dayStr;
        int kontrollZeilen=AnzahlZeilenTempdaten(quellPfad, ids, parzOnly);        
        DBConnection dbconn= new DBConnection(2);     // a class to manage the conection to a database
        
        
        dbconn.openDBConnection(DBConnection.MYSQL, 0, "db/waldwachstum", "waldwachstum", "ww2011", true, false);
        dbconn.openDBConnection(DBConnection.ACCESS, 1, quellPfad, "", "", false, true);
        try {
           Statement stmt = dbconn.Connections[0].createStatement(); 
           dbconn.Connections[0].setReadOnly(false);

        stmt.execute("INSERT INTO waldwachstum.altParz SELECT * FROM waldwachstum.Parz WHERE id= \'"+ids+"\'"  );
        stmt.execute("INSERT INTO waldwachstum.altAuf SELECT * FROM waldwachstum.Auf WHERE edvid= \'"+ids+"\'"  );
        stmt.execute("INSERT INTO waldwachstum.altBaum SELECT * FROM waldwachstum.Baum WHERE edvid= \'"+ids+"\'"  );
        stmt.execute("INSERT INTO waldwachstum.altStammv SELECT * FROM waldwachstum.Stammv WHERE edvid= \'"+ids+"\'" );
        stmt.execute("INSERT INTO waldwachstum.altQualit SELECT * FROM waldwachstum.Qualit WHERE edvid= \'"+ids+"\'" );

// Löschen der verschobenen Datensätze

        stmt.execute("DELETE FROM waldwachstum.Parz WHERE id= \'"+ids+"\'"  );
        stmt.execute("DELETE FROM waldwachstum.Auf WHERE edvid= \'"+ids+"\'"  );
        stmt.execute("DELETE FROM waldwachstum.Baum WHERE edvid= \'"+ids+"\'"  );
        stmt.execute("DELETE FROM waldwachstum.Stammv WHERE edvid= \'"+ids+"\'"  );
        stmt.execute("DELETE FROM waldwachstum.Qualit WHERE edvid= \'"+ids+"\'"  );

// Einfügen der Parzellendaten
        Statement stmt2 = dbconn.Connections[1].createStatement(); 
        ResultSet rs = stmt2.executeQuery("SELECT * FROM Parz WHERE edvid= '"+ids.substring(0,6)+"' " +
                "   AND parzelle = '"+ids.substring(6,8)+"'");
        ResultSetMetaData meta = rs.getMetaData();
        Vector tableHeader = new Vector(); 
        Vector tableTypesParz = new Vector();
        int maxCols= meta.getColumnCount();
        for(int i=1; i<=maxCols; i++){
                tableHeader.addElement(meta.getColumnLabel(i));}
        String insertData ="";
        String insertNames ="";
        if (rs.next()){
            Vector tableRow = new Vector(); 
            for (int i=1;i<=maxCols; i++){
                tableRow.addElement(rs.getObject(i));
            }
            for(int i=1; i<=maxCols; i++){
                String name=meta.getColumnTypeName(i);
                tableTypesParz.addElement(name);
            }
            for (int i=0;i<maxCols-2; i++){
                String typeName=(String)tableTypesParz.get(i);
                if (typeName.equals("INTEGER") || typeName.equals("DOUBLE")  ){
                    if (typeName.equals("INTEGER")){
                        if ((Integer)tableRow.get(i)!=null){
                           Integer sxi =(Integer)tableRow.get(i);
                           insertData=insertData+""+sxi.toString()+"";
                        }
                        else insertData=insertData+"0";
                    } 
                    if (typeName.equals("DOUBLE")){
                       if ((Double)tableRow.get(i)!=null){
                          Double sxf =(Double)tableRow.get(i);
                           insertData=insertData+""+sxf.toString()+"";
                       } 
                        else insertData=insertData+"0.0";
                    }
                 }
                 else {
                     if ((String)tableRow.get(i)!=null){
                         String sx =(String)tableRow.get(i);
                         insertData=insertData+"'"+sx.trim()+"'";
                     }
                     else insertData=insertData+"' '";
                  }
                  insertNames= insertNames+(String)tableHeader.get(i);
                  if (i<maxCols-3) insertData=insertData+" ,";
                  if (i<maxCols-3) insertNames=insertNames+" ,";
            }

           insertData=insertData+" ,'"+datumStr+"' "; 
           insertNames=insertNames+" , Datum";
           stmt.execute("INSERT INTO Parz ( "+insertNames+" ) VALUES ("+insertData+" )");
           stmt.execute("UPDATE waldwachstum.Parz SET Stempel = now(), id = '"+ids+"' WHERE edvid= '"+ids.substring(0,6)+"' " +
                "   AND parzelle = '"+ids.substring(6,8)+"'");
           // Mit dem Update gab es mal Probleme (duplicate entry für id-Feld). Lösung: alle Daten zu der betreffenden
           //   Parzelle aus Parz, Auf, Baum und evtl altParz usw. lösche und nochmal übertragen
        }



// Einfügen der Aufnahmedaten
        System.out.println("Einfügen der Aufnahmedaten");
        stmt2 = dbconn.Connections[1].createStatement(); 
        rs = stmt2.executeQuery("SELECT * FROM Auf WHERE edvid= '"+ids+"'");
        meta = rs.getMetaData();
        Vector tableHeaderAuf = new Vector();
        Vector tableTypesAuf = new Vector();
        maxCols= meta.getColumnCount();
        for(int i=1; i<=maxCols; i++){
                tableHeaderAuf.addElement(meta.getColumnLabel(i));}
        for(int i=1; i<=maxCols; i++){
                String name=meta.getColumnTypeName(i);
                tableTypesAuf.addElement(name);
        }
       while (rs.next()){
            insertData ="";
            insertNames ="";
            Vector tableRow = new Vector(); 
            for (int i=1;i<=maxCols; i++){
                tableRow.addElement(rs.getObject(i));
            }
            for (int i=0;i<maxCols-2; i++){
                String typeName=(String)tableTypesAuf.get(i);
                if (typeName.equals("INTEGER") || typeName.equals("DOUBLE")){
                    if (typeName.equals("INTEGER")){
                        if ((Integer)tableRow.get(i)!=null){
                           Integer sxi =(Integer)tableRow.get(i);
                           insertData=insertData+""+sxi.toString()+"";
                        }
                        else insertData=insertData+"0";
                    } 
                    if (typeName.equals("DOUBLE")){
                       if ((Double)tableRow.get(i)!=null){
                          Double sxf =(Double)tableRow.get(i);
                           insertData=insertData+""+sxf.toString()+"";
                       } 
                        else insertData=insertData+"0.0";
                    }
                 }
                 else {
                     if ((String)tableRow.get(i)!=null){
                         String sx =(String)tableRow.get(i);
                         insertData=insertData+"'"+sx.trim()+"'";
                     }
                     else insertData=insertData+"' '";
                  }
                  insertNames= insertNames+(String)tableHeaderAuf.get(i);
                  if (i<maxCols-3) insertData=insertData+" ,";
                  if (i<maxCols-3) insertNames=insertNames+" ,";
            }
           insertData=insertData+" ,'"+datumStr+"' "; 
           insertNames=insertNames+" , datum";
           System.out.println(insertData +" "+ insertNames);

           stmt.execute("INSERT INTO Auf ( "+insertNames+" ) VALUES ("+insertData+" )");
           stmt.execute("UPDATE waldwachstum.Auf SET Stempel = now() WHERE edvid= \'"+ids+"\'" ); //Stempel aktualisiert sich eigentlich selbst (warum ist das hier drin?)
        } 
// Einfügen der Baumdaten
        System.out.println("Einfügen der Baumdaten");
        stmt2 = dbconn.Connections[1].createStatement(); 
        rs = stmt2.executeQuery("SELECT * FROM Baum WHERE edvid= '"+ids+"'");
        meta = rs.getMetaData();
        Vector tableHeaderBaum = new Vector();
        Vector tableTypesBaum = new Vector();
        maxCols= meta.getColumnCount();
        for(int i=1; i<=maxCols; i++){
                tableHeaderBaum.addElement(meta.getColumnLabel(i));}
        for(int i=1; i<=maxCols; i++){
                String name=meta.getColumnTypeName(i);
                tableTypesBaum.addElement(name);
        }
        int nrSpalte = tableHeaderBaum.indexOf("nr");         

       while (rs.next()){
            insertData ="";
            insertNames ="";
            Vector tableRow = new Vector(); 
            for (int i=1;i<=maxCols; i++){
                tableRow.addElement(rs.getObject(i));
            }
            for (int i=1;i<maxCols-2; i++){
                String typeName=(String)tableTypesBaum.get(i);
                if (typeName.equals("INTEGER") || typeName.equals("DOUBLE") || typeName.equals("SMALLINT")){  // smallint neu 3.11.2010
                    if (typeName.equals("INTEGER") || typeName.equals("SMALLINT")){
                        if ((Integer)tableRow.get(i)!=null){
                           Integer sxi =(Integer)tableRow.get(i);
                           insertData=insertData+""+sxi.toString()+"";
                        }
                        else insertData=insertData+"0";
                    } 
                    if (typeName.equals("DOUBLE")){
                       if ((Double)tableRow.get(i)!=null){
                          Double sxf =(Double)tableRow.get(i);
                          String sxfstr = sxf.toString();
                          sxfstr.replaceAll(",",".");
                          insertData=insertData+""+sxfstr+"";
                       } 
                        else insertData=insertData+"0.0";
                    }
                 }
                 else {
                     if ((String)tableRow.get(i)!=null){
                         String sx =(String)tableRow.get(i);
                         sx.replaceAll(",",".");
                         sx.replaceAll("'","");             // 28.03.2011
                         if(i != nrSpalte) sx = sx.trim();  //Positionierung der Baumnummern erhalten
                         insertData=insertData+"'"+sx+"'";
                     }
                     else insertData=insertData+"' '";
                  }
                  insertNames= insertNames+(String)tableHeaderBaum.get(i);
                  if (i<maxCols-3) insertData=insertData+" ,";
                  if (i<maxCols-3) insertNames=insertNames+" ,";
            }
           insertData=insertData+" ,'"+datumStr+"' "; 
           insertNames=insertNames+" , datum";
           System.out.println(insertData +" "+ insertNames);
           stmt.execute("INSERT INTO Baum ( "+insertNames+" ) VALUES ("+insertData+" )");
           stmt.execute("update waldwachstum.Baum set Stempel = now() WHERE edvid= \'"+ids+"\'" );//Stempel aktualisiert sich eigentlich selbst (warum ist das hier drin?)
        } 
// Einfügen der Stammvdaten
        System.out.println("Einfügen der Stammvdaten");
        stmt2 = dbconn.Connections[1].createStatement(); 
        rs = stmt2.executeQuery("SELECT * FROM Stammv WHERE edvid= '"+ids+"'");
        meta = rs.getMetaData();
        Vector tableHeaderStammv = new Vector();
        Vector tableTypesStammv = new Vector();
        maxCols= meta.getColumnCount();
        for(int i=2; i<=maxCols; i++){
                tableHeaderStammv.addElement(meta.getColumnLabel(i));}
        for(int i=2; i<=maxCols; i++){
                String name=meta.getColumnTypeName(i);
//                if(name.equals("YEAR")) name="INTEGER";
//                if(name.equals("DECIMAL")) name="FLOAT";
                System.out.println(i+"  "+name);
                tableTypesStammv.addElement(name);
        }
       while (rs.next()){
            insertData ="";
            insertNames ="";
            Vector tableRow = new Vector(); 
            for (int i=2;i<=maxCols; i++){
                tableRow.addElement(rs.getObject(i));
            }
            for (int i=0;i<maxCols-3; i++){
                String typeName=(String)tableTypesStammv.get(i);
                if (typeName.equals("INTEGER") || typeName.equals("DOUBLE")){
                    if (typeName.equals("INTEGER")){
                        if ((Integer)tableRow.get(i)!=null){
                           Integer sxi =(Integer)tableRow.get(i);
                           insertData=insertData+""+sxi.toString()+"";
                        }
                        else insertData=insertData+"0";
                    } 
                    if (typeName.equals("DOUBLE")){
                       if ((Double)tableRow.get(i)!=null){
                          Double sxf =(Double)tableRow.get(i);
                           insertData=insertData+""+sxf.toString()+"";
                       } 
                        else insertData=insertData+"0.0";
                    }
                 }
                 else {
                     if ((String)tableRow.get(i)!=null){
                         String sx =(String)tableRow.get(i);
                         insertData=insertData+"'"+sx.trim()+"'";
                     }
                     else insertData=insertData+"' '";
                  }
                  insertNames= insertNames+(String)tableHeaderStammv.get(i);
                  if (i<maxCols-4) insertData=insertData+" ,";
                  if (i<maxCols-4) insertNames=insertNames+" ,";
            }
           insertData=insertData+" ,'"+datumStr+"' "; 
           insertNames=insertNames+" , datum";
           stmt.execute("INSERT INTO Stammv ( "+insertNames+" ) VALUES ("+insertData+" )");
           stmt.execute("UPDATE waldwachstum.Stammv set Stempel = now() WHERE id= \'"+ids+"\'" );
        } 
       
  // Einfügen der Qualitdaten
        System.out.println("Einfügen der Qualitdaten");
        stmt2 = dbconn.Connections[1].createStatement(); 
        rs = stmt2.executeQuery("SELECT * FROM Qualit WHERE edvid= '"+ids+"'");
        meta = rs.getMetaData();
        Vector tableHeaderQualit = new Vector();
        Vector tableTypesQualit = new Vector();
        maxCols= meta.getColumnCount();
        for(int i=2; i<=maxCols; i++){
                tableHeaderQualit.addElement(meta.getColumnLabel(i));}
        for(int i=2; i<=maxCols; i++){
                String name=meta.getColumnTypeName(i);
//                if(name.equals("YEAR")) name="INTEGER";
//                if(name.equals("DECIMAL")) name="FLOAT";
                System.out.println(i+"  "+name);
                tableTypesQualit.addElement(name);
        }
       while (rs.next()){
            insertData ="";
            insertNames ="";
            Vector tableRow = new Vector(); 
            for (int i=2;i<=maxCols; i++){
                tableRow.addElement(rs.getObject(i));
            }
            for (int i=0;i<maxCols-2; i++){
                String typeName=(String)tableTypesQualit.get(i);
                if (typeName.equals("INTEGER") || typeName.equals("DOUBLE")){
                    if (typeName.equals("INTEGER")){
                        if ((Integer)tableRow.get(i)!=null){
                           Integer sxi =(Integer)tableRow.get(i);
                           insertData=insertData+""+sxi.toString()+"";
                        }
                        else insertData=insertData+"0";
                    } 
                    if (typeName.equals("DOUBLE")){
                       if ((Double)tableRow.get(i)!=null){
                          Double sxf =(Double)tableRow.get(i);
                           insertData=insertData+""+sxf.toString()+"";
                       } 
                        else insertData=insertData+"0.0";
                    }
                 }
                 else {
                     if ((String)tableRow.get(i)!=null){
                         String sx =(String)tableRow.get(i);
                         if(!((String)tableHeaderQualit.get(i)).equalsIgnoreCase("nr")) sx = sx.trim();
                         insertData=insertData+"'"+sx+"'";
                     }
                     else insertData=insertData+"' '";
                  }
                  insertNames= insertNames+(String)tableHeaderQualit.get(i);
                  if (i<maxCols-3) insertData=insertData+" ,";
                  if (i<maxCols-3) insertNames=insertNames+" ,";
            }

           stmt.execute("INSERT INTO Qualit ( "+insertNames+" ) VALUES ("+insertData+" )");
           stmt.execute("UPDATE waldwachstum.Qualit set Stempel = now() WHERE id= \'"+ids+"\'" );
        } 
       
       
                  
        }
        
       catch (Exception e){  
           System.out.println("Datenbank Fehler :"+e);
           e.printStackTrace(); }
       dbconn.closeAll();
 //      
       String status = "FEHLER: beim Einfügen";
       if (kontrollZeilen==AnzahlZeilenWaldwachstum(ids, parzOnly)) status = "erfolgreich eingefügt !" ;
       return status ;
        
    }
    public String cleanDoppelte(String ids, String datenPfad, boolean parzOnly){
        int doppel[]=new int[20000] ;
        String doppelnr[]=new String[20000] ;
        int ndoppel=0;
        String nrAlt="";
        int aufAlt=0;
        int artAlt=0;
        int zeileAlt=0 ;
        DBConnection dbconn= new DBConnection(1);     // a class to manage the conection to a database
        dbconn.openDBConnection(DBConnection.ACCESS, 0, datenPfad, "", "", false, true);
        try {
           Statement stmt = dbconn.Connections[0].createStatement(); 
           dbconn.Connections[0].setReadOnly(false);
           ResultSet rs = stmt.executeQuery("SELECT * FROM Baum WHERE edvid= '"+ids+"' ORDER BY art,nr,auf");
           while (rs.next()){
                int zeile = rs.getInt("id");
                String nrx = rs.getObject("nr").toString().trim();
                int aufx = rs.getInt("auf");
                int artx = rs.getInt("art");
                if (nrx.matches("\\d+\\w*")&& aufx == aufAlt && nrx.equals(nrAlt) && artx == artAlt
                        && nrx.length()>0){
                    doppel[ndoppel]= zeileAlt;
                    doppelnr[ndoppel]= nrAlt;
                    ndoppel=ndoppel+1;
                 
                }
                zeileAlt=zeile;
                nrAlt=nrx;
                aufAlt=aufx;
                artAlt=artx;
               
           }
          try {
             stmt.execute("DROP INDEX id on Baum");
          }
          catch (Exception e){  System.out.println("Datenbank kein index :"+e); }
          stmt.execute("CREATE INDEX id ON Baum (id) ");
          
           for (int i=0;i<ndoppel;i++){
               stmt.execute("DELETE FROM Baum WHERE (id = "+doppel[i]+" )");
                    System.out.println(i+" Doppelt: "+doppelnr[i]+" (id "+doppel[i]+")");
           }    
        
       }
        
       catch (Exception e){  System.out.println("Datenbank Fehler Clean :"+e); }
       dbconn.closeAll();
        
        return "doppelte Einträge entfernt";
    }
    
    int AnzahlZeilenWaldwachstum(String id, boolean parzOnly){
      int zeilen =0; 
      DBConnection dbconn= new DBConnection(1);     // a class to manage the conection to a database
      dbconn.openDBConnection(dbconn.MYSQL, 0, "db/waldwachstum", "wwuser", "ww2005", true, true);
      
      if(parzOnly){
          try {
              Statement stmt = dbconn.Connections[0].createStatement();
              ResultSet rs = stmt.executeQuery("SELECT count(id) as anzahl FROM Parz WHERE id = '"+id+"'");
              while(rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              }
              rs = stmt.executeQuery("SELECT count(edvid) as anzahl FROM Auf WHERE edvid = '" + id + "'");
              while (rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              }
              rs = stmt.executeQuery("SELECT count(edvid) as anzahl FROM Baum WHERE edvid = '" + id + "'");
              while (rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              }
              rs = stmt.executeQuery("SELECT count(edvid) as anzahl FROM Stammv WHERE edvid = '" + id + "'");
              while (rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              }
              rs = stmt.executeQuery("SELECT count(edvid) as anzahl FROM Qualit WHERE edvid = '" + id + "'");
              while (rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              }
        } catch (Exception e){System.out.println("Datenbank Fehler :"+e); }            
      }
      else{
        String idl = "'"+id.substring(0, 6)+"%' ";
          try {
              Statement stmt = dbconn.Connections[0].createStatement();
              ResultSet  rs = stmt.executeQuery("SELECT count(id) as anzahl FROM Parz WHERE id LIKE " + idl);
              while (rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              } 
              rs = stmt.executeQuery("SELECT count(edvid) as anzahl FROM Auf WHERE edvid LIKE " + idl);
              while (rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              }
              rs = stmt.executeQuery("SELECT count(edvid) as anzahl FROM Baum WHERE edvid LIKE " + idl);
              while (rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              }
              rs = stmt.executeQuery("SELECT count(edvid) as anzahl FROM Stammv WHERE edvid LIKE " + idl);
              while (rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              }
          } catch (Exception e) {System.out.println("Datenbank Fehler :" + e);}         
      }

      dbconn.closeAll();
      System.out.println("Anzahl der Zeilen: "+ zeilen);
      return zeilen;
    }

    int AnzahlZeilenTempdaten(String file, String id, boolean parzOnly){
      int zeilen =0; 
      DBConnection dbconn= new DBConnection(1);     // a class to manage the conection to a database
      dbconn.openDBConnection(dbconn.ACCESS, 0, file, "", "", false, true);
      
      if(parzOnly){
          try {
              Statement stmt = dbconn.Connections[0].createStatement();
              ResultSet rs = stmt.executeQuery("SELECT count(id) as anzahl FROM Parz WHERE id = '"+id+"'");
              while(rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              }  
              rs = stmt.executeQuery("SELECT count(edvid) as anzahl FROM Auf WHERE edvid = '" + id + "'");
              while (rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              }
              rs = stmt.executeQuery("SELECT count(edvid) as anzahl FROM Baum WHERE edvid = '" + id + "'");
              while (rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              }
              rs = stmt.executeQuery("SELECT count(edvid) as anzahl FROM Stammv WHERE edvid = '" + id + "'");
              while (rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              }
              rs = stmt.executeQuery("SELECT count(edvid) as anzahl FROM Qualit WHERE edvid = '" + id + "'");
              while (rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              }

          } catch (Exception e){System.out.println("Datenbank Fehler :"+e); }
      dbconn.closeAll();
      }
      else{
        String idl = "'"+id.substring(0, 6)+"%' ";

          try {
              Statement stmt = dbconn.Connections[0].createStatement();
              ResultSet rs = stmt.executeQuery("SELECT count(id) as anzahl FROM Parz WHERE id LIKE " + idl);
              while (rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              } 
                    
              rs = stmt.executeQuery("SELECT count(edvid) as anzahl FROM Auf WHERE edvid LIKE " + idl);
              while (rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              }
              rs = stmt.executeQuery("SELECT count(edvid) as anzahl FROM Baum WHERE edvid LIKE " + idl);
              while (rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              }
              rs = stmt.executeQuery("SELECT count(edvid) as anzahl FROM Stammv WHERE edvid LIKE " + idl);
              while (rs.next()) {
                  int zeile = rs.getInt("anzahl");
                  zeilen = zeilen + zeile;
              }
        } catch (Exception e){  System.out.println("Datenbank Fehler :"+e); }
      dbconn.closeAll();          
      }

      System.out.println("Anzahl der Zeilen: "+ zeilen);
      return zeilen;
    }

    
}
