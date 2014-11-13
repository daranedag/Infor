/*
 * Eingabetabelle.java
 *
 * Created on 20. März 2006, 11:40
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package vis2006;
import DatabaseManagement.*;
import java.awt.Component;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 *
 * @author nagel
 */
public class Eingabetabelle {
    Component parent = null;
    String lokaleDB = "";
    
    /** Creates a new instance of Eingabetabelle */
    public Eingabetabelle(Component parent, String lokaleDB) {
        this.parent = parent;
        this.lokaleDB = lokaleDB;
    }
    
    public int getAnzZeilen(String edvid){
        int zeilen = -1;   // -1 wenn die Tabelle nicht existiert
        
        // Datenbankverbindung       
        DBConnection dbconn= new DBConnection(2);     // a class to manage the conection to a database
        dbconn.openDBConnection(DBConnection.ACCESS, 1, lokaleDB, "", "", false, true);
        
        // Tabelle vorhanden?
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = dbconn.Connections[1].createStatement();
            rs = stmt.executeQuery("SELECT count(*) AS zeilen FROM Eingabe WHERE edvid = '"+edvid+"';");
            while(rs.next()){
                zeilen = rs.getInt("zeilen");
            }
        } catch (Exception e) {
            zeilen = -1;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (Exception e) {e.printStackTrace();}
        }
        dbconn.closeAll();
        return zeilen;
    }
    
    public void parzelleLoeschen(String edvid){
        // Datenbankverbindung       
        DBConnection dbconn= new DBConnection(2);     // a class to manage the conection to a database
        dbconn.openDBConnection(DBConnection.ACCESS, 1, lokaleDB, "", "", false, true);
        
        // Tabelle leeren?
        Statement stmt = null;
        try {
            stmt = dbconn.Connections[1].createStatement();
            stmt.execute("DELETE FROM Eingabe WHERE edvid = '"+edvid+"';");
        } catch (Exception e) {e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception e) {e.printStackTrace();}
        }
        dbconn.closeAll();

    }

    
    public boolean eingabetabelleAnlegen(String edvid6, boolean ausfuellen){
        boolean ok = false;
// Datenbankverbindung       
        DBConnection dbconn= new DBConnection(2);     // a class to manage the conection to a database
        dbconn.openDBConnection(DBConnection.ACCESS, 1, lokaleDB, "", "", false, true);
        BasicQueries basicQueries= new BasicQueries(dbconn.Connections[1]);
// Tabelle anlegen
        String tabellenKopf="id INT Primary key, edvid CHAR(8), nr CHAR(6), nralt CHAR(6), "
                + "art INT, ZF CHAR(1), OU CHAR(1), d1 INT, a1 Char(1), alt2 FLOAT, "
                + "d2 INT, a2 CHAR(1), h2 INT, k2 INT, mh INT, dn INT, dkn INT, an CHAR(1), "
                + "en CHAR(1), alt_en FLOAT, hn INT, kn INT, ZFN CHAR(1), OUN CHAR(1), "
                + "R CHAR(1), MHN INT, Bemerk Char(25), Qualitaet Char(20), vdfv INT, kraft INT, sta INT, kro INT, seig CHAR(20) ";
        boolean tabelleErzeugt=basicQueries.makeTable("Eingabe", tabellenKopf);
        
// Parzellenliste erstellen
        
        if (tabelleErzeugt && ausfuellen){
            // ohne aufgegebene Parzellen
            ArrayList<String> parzellen = new ArrayList<String>();  
            Statement stmt = null;
            ResultSet rs = null;
            try {
                stmt = dbconn.Connections[1].createStatement();
                rs = stmt.executeQuery("SELECT Parz.parzelle FROM Parz LEFT JOIN ParzInfo "
                        + "ON Parz.edvid = ParzInfo.edvid AND Parz.parzelle = ParzInfo.PARZ "
                        + "WHERE Parz.edvid = '" + edvid6 + "' AND ParzInfo.status > 0");
                while(rs.next()){
                    parzellen.add(rs.getString("parzelle"));
                }
                
            } catch (Exception e) {e.printStackTrace();
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                } catch (Exception e) {e.printStackTrace();}
            }
            String[] edvids = new String[parzellen.size()];
            for(int i = 0; i < parzellen.size(); i ++) edvids[i] = edvid6 + parzellen.get(i);
   
// Daten für alle nicht aufgegebenen Parzellen einfügen            
            try{
                int lastAuf = 0; int vorLastAuf =0;
                int art1=0; int d1=0; String a1=" "; String nr1 ="";
                int art2=0; int d2=0; String a2=" "; String nr2 =""; int h2=0; int k2=0;String zf=" "; String ou=" "; String r2=" ";
                int mh2=0; double alt2=0;String nralt2="";
                int nDatensatz=0;
                for(int i = 0; i < edvids.length; i ++){
                    // letzte Aufnahme feststellen
                    Statement stmt2 = dbconn.Connections[1].createStatement();
                    ResultSet rs2 = stmt2.executeQuery("SELECT * FROM Auf WHERE edvid = \'"+edvids[i]+"\' ORDER BY auf DESC");
                    if (rs2.next()) lastAuf=rs2.getInt("auf");
                    if (rs2.next()) vorLastAuf=rs2.getInt("auf");
                    
                    // Daten aus Baum zusammensuchen
                    stmt2 = dbconn.Connections[1].createStatement(); 
                    rs2 = stmt2.executeQuery("SELECT * FROM Baum WHERE edvid = \'"+edvids[i]+"\'  And auf >= "+(vorLastAuf)
                            + " AND trim(nr) NOT IN (\'nurH\', \'cm\', \'onum\', \'HG\', \'HB\')  ORDER BY nr, auf ");
                    while (rs2.next()){
                        int aufx = rs2.getInt("auf");
                        String nrx = rs2.getObject("nr").toString();
                        // Nummer positionieren -> 6stellig
                        while(nrx.length()< 5) nrx = " " + nrx;
                        if(isInteger(nrx.substring(nrx.length()-1)) && nrx.length()<6) nrx = nrx + " ";
                        else if(nrx.length()<6) nrx = " " + nrx;
                        
                        // bei der ersten Aufnahme gemessen                
                        if (aufx== vorLastAuf) {
                            art1=rs2.getInt("art");
                            d1 = rs2.getInt("dmess");
                            Object a1obj = rs2.getObject("a");
                            a1 = "";
                            if(a1obj != null) a1 = a1obj.toString().trim();
                            nr1 = nrx;
                        }
                        if (aufx== lastAuf) {
                            art2=rs2.getInt("art");
                            d2 = rs2.getInt("dmess");
                            h2 = rs2.getInt("h");
                            k2 = rs2.getInt("k");
                            alt2 = rs2.getDouble("alt");
                            mh2 = rs2.getInt("mh");
                            nralt2="";
                            Object nraltobj = rs2.getObject("nralt");
                            if (nraltobj != null) {
                                nralt2 = nraltobj.toString().trim();
                            }
                            a2 = "";
                            Object a2obj = rs2.getObject("a");
                            if (a2obj != null) {
                                a2 = a2obj.toString().trim();
                            }
                            nr2 = nrx;
                            zf = "";
                            Object zfobj = rs2.getObject("zf");
                            if (zfobj != null) zf = zfobj.toString().trim();
                            ou = "";
                            Object ouobj = rs2.getObject("ou");
                            if (ouobj != null) ou = ouobj.toString().trim();
                            r2 = "";
                            Object r2obj = rs2.getObject("r");
                            if (r2obj != null)r2 = r2obj.toString().trim();
                            if ((art1 == art2) && nr1.compareTo(nr2) != 0) {
                                d1 = 0;
                                a1 = "";
                            }
                            // Baum in Eingabetabelle schreiben
                            if (d2 > 0) {
                                nDatensatz = nDatensatz + 1;
                                String vs = "(" + nDatensatz + ", '" + edvids[i] + "', '" + nr2 + "', '" + nralt2 + "', "
                                        + "" + art2 + ", '" + zf + "', '" + ou + "', " + d1 + ", '" + a1 + "', "
                                        + "" + alt2 + " ," + d2 + ", '" + a2 + "', " + h2 + ", " + k2 + ", " + mh2
                                        + ", 0, 0, ' ', ' ',0.0, 0, 0,'" + zf + "', '" + ou + "', '" + r2 + "', ' ', ' ')";
                                Statement stmt3 = basicQueries.dbcon.createStatement();
                                stmt3.execute("INSERT INTO Eingabe  "
                                        + "(id, edvid , nr, nralt, art, ZF, OU, d1, a1, alt2, d2, a2, h2, k2, mh, dn, dkn, "
                                        + "an, en, alt_en, hn, kn, ZFN, OUN , r, Bemerk, Qualitaet ) "
                                        + "VALUES " + vs);
                                stmt3.close();
                            }
                        }
                    } // Ende While
                    try{
                        if(rs2 != null) rs2.close();
                        if(stmt2 != null) stmt2.close();  
                    }catch (Exception ex){ex.printStackTrace();}
                } // Ende Schleife über alle Parzellen
                ok = true;
            }
            catch (Exception e){  e.printStackTrace(); }
        } // Tabelle erzeugt 
        if(tabelleErzeugt && !ausfuellen) ok = true;
        
        dbconn.closeAll();
        return ok;
    }
    
//
// add Eingabetabelle Auf und Baum
//
    public String addEingabeTabelle(String selectedID, double altersDifferenz, int auf){
        String ergebnis = "Fehler";
        int zeilen = 0;
        int rundung=0;
        DBConnection dbconn= new DBConnection(2);     // a class to manage the conection to a database
        dbconn.openDBConnection(DBConnection.ACCESS, 1, lokaleDB, "", "", false, true);
// Check, ob Aufnahme schon übernommen wurde
        int lastAuf = 0;
        int maxId=0;
        Statement stmt = null;
        ResultSet rs = null;
        try {
           stmt = dbconn.Connections[1].createStatement();
           rs = stmt.executeQuery("SELECT * FROM Baum WHERE edvid = \'"+selectedID+"\' ORDER BY auf DESC");
           if (rs.next())  lastAuf=rs.getInt("auf");
           rs = stmt.executeQuery("SELECT max(id) AS maxid FROM Baum WHERE edvid = \'"+selectedID+"\' ");
           if (rs.next())  maxId=rs.getInt("maxid");

        }
        catch (Exception e){  e.printStackTrace(); 
        } finally {
            try{
                if(rs!=null) rs.close();
                if(stmt!=null) stmt.close();
            }
            catch(Exception ex){ex.printStackTrace();}
        }
//  
        if (lastAuf >= auf) {
            ergebnis="Daten bereits vorhanden";
        }
        else {
        Statement stmt2 = null;
        ResultSet rs2 = null;
        try {
           stmt2 = dbconn.Connections[1].createStatement();
           rs2 = stmt2.executeQuery("SELECT * FROM Eingabe WHERE edvid = \'"+selectedID+"\' ");
           int ndfehl = 0;
           boolean qualAnsprache = false;
           while (rs2.next()){
               String edvidx = rs2.getObject("edvid").toString().trim();
               String nrx = "";
               Object nrObj = rs2.getObject("nr");
               if(nrObj != null) nrx = nrObj.toString().trim();
               String nraltx = "";
               Object nraltObj = rs2.getObject("nralt");
               if(nraltObj != null) nraltx = nraltObj.toString().trim(); 
               int artx=rs2.getInt("art");
               Double altx = 0.0;
               Double alt2 = rs2.getDouble("alt2");
               if(alt2 > 0) altx = alt2+altersDifferenz;
               else{
                   Double alt_ex=rs2.getDouble("alt_en");
                   if (alt_ex > 0) altx = alt_ex;
               }
               String a2="";
               Object check = rs2.getObject("a2");
               if(check != null) a2=check.toString().trim();
               int mhx=rs2.getInt("mh");
               int eingId=rs2.getInt("id");
               int dx=rs2.getInt("dn");
               int dkx=rs2.getInt("dkn");
               check = rs2.getObject("an");
               String ax = " ";
               if (check != null) ax=check.toString();    
               if (ax.indexOf("F")>-1) dkx=-999;
               String ex = " ";
               check = rs2.getObject("en");
               if (check != null) ex=check.toString(); 
               int hx=rs2.getInt("hn");
               int kx=rs2.getInt("kn");
               String zfx=" ";
               Object txt1=rs2.getObject("ZFN");
               if (txt1 != null) zfx=txt1.toString().trim();
               String oux=" ";
               Object txt2=rs2.getObject("OUN");
               if (txt2 != null) oux=txt2.toString().trim();
               String rx=" ";
               Object txt3=rs2.getObject("r");
               if (txt3 != null) rx=txt3.toString().trim();
               String bemerkx=" ";
               Object txt4=rs2.getObject("Bemerk");
               if (txt4 != null) bemerkx=txt4.toString().trim();
               String qualitaetx=" ";
               Object txt5=rs2.getObject("Qualitaet");
               if (txt5 != null) qualitaetx=txt5.toString().trim();

               // Spalten die nicht vorhanden sein müssen
               int ddn = 0;
               int sn = 0;
               /*
               int kmaxn = 0;
               int hmaxkb = 0;
               String beznr = "";
               int bezart = 0;
               int dist = 0;
               int gon = 0;
               int[] g = {0,0,0,0,0,0,0,0};  // g0, g50, ... g350
               int kmp_dist = 0;
               int kmp_gon = 0;
                * 
                */
               try{
 //                  So geht es für die anderen nicht -> Abbruch beim ersten, andere werden nicht geprüft
                   ddn = rs2.getInt("ddn");
                   sn = rs2.getInt("sn");
               }catch(java.sql.SQLException e){}   // wenn die Spalten nicht existieren

//         Qualitätsansprache
               int vdfv = 0;
               int kraft = 0;
               int sta = 0;
               int kro = 0;
               String seig = ""; int schief = 0; int zwies = 0; int dreh = 0; int beu = 0; 
                    int ast = 0; int wr = 0; int klast = 0; int trast = 0; int rosen = 0;
               int ncol = rs2.getMetaData().getColumnCount();
               for(int i = 0; i < ncol; i ++){
                   String colname = rs2.getMetaData().getColumnName(i+1);
                   if(colname.equalsIgnoreCase("VDFV")){
                       vdfv = rs2.getInt("VDFV");
                   } else if(colname.equalsIgnoreCase("KRAFT")){
                       kraft = rs2.getInt("KRAFT");
                   } else if(colname.equalsIgnoreCase("STA")){
                       sta = rs2.getInt("STA");
                   } else if(colname.equalsIgnoreCase("KRO")){
                       kro = rs2.getInt("KRO");
                   } else if(colname.equalsIgnoreCase("SEIG")){
                       seig = "";
                       Object seigobj = rs2.getObject("SEIG"); 
                       if(seigobj != null) seig = seigobj.toString().trim();
                       Set<Integer> merkmale = new HashSet<Integer>();
                       if(seig.length()%2!=0){          // wenn ungerade -> Fehler
                           String text = "Ungerade Anzahl Zeichen in Feld SEIG "
                                   + "bei Baum " + nrx + "(" + artx + "). Feldinhalt wird ignoriert!";
                           JOptionPane.showMessageDialog(parent, text, "Fehler", JOptionPane.ERROR_MESSAGE);
                           System.out.println(text);
                       } else{    
                           for(int j = 0; j < seig.length()/2; j ++){
                               String test = seig.substring(j*2, j*2+2);
                               if(isInteger(test)) merkmale.add(Integer.parseInt(test));
                           }
                           if(merkmale.contains(10)){schief = 1; merkmale.remove(10);}
                           if(merkmale.contains(11)){zwies = 1; merkmale.remove(11);}
                           if(merkmale.contains(12)){zwies = 2; merkmale.remove(12);}
                           if(merkmale.contains(13)){zwies = 3; merkmale.remove(13);}
                           if(merkmale.contains(14)){dreh = 1; merkmale.remove(14);}
                           if(merkmale.contains(15)){beu = 1; merkmale.remove(15);}
                           if(merkmale.contains(16)){ast = 1; merkmale.remove(16);}
                           if(merkmale.contains(17)){wr = 1; merkmale.remove(17);}
                           if(merkmale.contains(18)){klast = 1; merkmale.remove(18);}
                           if(merkmale.contains(19)){trast = 1; merkmale.remove(19);}
                           if(merkmale.contains(20)){rosen = 1; merkmale.remove(20);}
                           
                           if(merkmale.size()>0){
                               String text = "Nicht vergebene Schlüsselzahl in Feld SEIG "
                                       + "bei Baum " + nrx + " (" + artx + "). Zahl wird ignoriert!";
                               JOptionPane.showMessageDialog(parent, text, "Fehler", JOptionPane.ERROR_MESSAGE);
                               System.out.println(text);
                           }
                           
                       }
                       
                   }                   
               }
               
               if(vdfv > 0 || kraft > 0 || sta > 0 || kro > 0 || seig.length() > 0){
                   qualAnsprache = true;
                   String vs = "'" + edvidx + "'," + auf + ",'" + nrx + "'," + artx + "," + sta + "," + kro + "," 
                           + schief + "," + zwies + "," + dreh + "," + beu + "," + ast + "," + wr + ","
                           + klast + "," + trast + "," + rosen ;
                   
                   Statement stmt4 = dbconn.Connections[1].createStatement();
                   stmt4.execute("INSERT INTO Qualit(edvid, auf, nr, art, stamm, krone, "
                               + "schief, zwies, dre, beu, ast, wr, "
                               + "klast, trast, rosen) VALUES( " + vs + ");");
          
               }
               
              
// Kreuzkluppung bearbeiten
               int dk1x=0;
               int dk2x=0;
               double dges = 0.0;
               if (dkx > 0 | dkx == -999) { 
                   dk1x=dx;
                   dk2x=dkx;
                   dges = ((double)dk1x + (double)dk2x)/2;
                   if(dges - Math.floor(dges) > 0){
                       dx = rundung + (int)dges;
                       if (rundung==0) rundung=1; else rundung=0;
                   } else dx = (int)dges;
                   if (dkx == -999) dx = -999;
                   
               }
// Datensatz in Baum einfügen
               if(!(dx == 0 && !a2.isEmpty())){ // ohne in der letzten Aufnahme ausgeschiedene
                   Statement stmt3 = dbconn.Connections[1].createStatement();
                   String vs = "(" + (maxId + eingId) + ", '" + edvidx + "', " + auf + ", '" + nrx + "', '" + nraltx + "', " + artx + ", 1, 1," +
                           altx.toString() + ", '" + rx + "', '" + zfx + "', '" + oux + "',-1, '" + qualitaetx + "'," + kraft +"," + vdfv + "," +
                           dk1x + ", " + dk2x + ", " + dx + ", " + mhx + ", 0, '" + ax + "', '" + ex + "', " + hx + ", " + kx + ", " +
                           " '0' ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0  ,  '" + bemerkx + "', " + ddn + ", " + sn + ")";
                   stmt3.execute("INSERT INTO Baum  " +
                           "(id, edvid , auf, nr, nralt, art, anzahl, repfl, alt, r, zf, ou, schicht, sonder, kraft, vdfv," +
                           "dkrz1,dkrz2,dmess,mh,d, a, e, h, k, hbr, g0, g5, g10, g15, g20, g25, g30, g35, hmaxkb," +
                           "hastfrei, sfg, sfe,  bemerk, ddauer, status) " +
                           "VALUES " + vs);
                   stmt3.close();
                   zeilen = zeilen + 1;
               }
           } // Ende: Schleife über alle Datensätze
           
           // Aufnahmenummer für Qualitätsansprache in Aufnahmetabelle aktualisieren
           if(qualAnsprache){
               Statement stmt5 = dbconn.Connections[1].createStatement();
               stmt5.execute("UPDATE Auf SET qualitat = " + auf + " WHERE edvid = '" + selectedID + "' AND auf = " + auf + ";");   
           }           
           
           
           ergebnis = "fertig";
        } catch (Exception e){  e.printStackTrace();
        }
        finally {
            try{
                if(rs2!=null) rs2.close();
                if(stmt2!=null) stmt2.close();
            }
            catch(Exception ex){ex.printStackTrace();}
        }
        
        }
        dbconn.closeAll();
        return ergebnis;
    }
//
// Eingabetabelle löschen
//
    public String deleteTable(){
        String ergebnis = "Tabelle Eingabe erfolgreich gelöscht";
// Feststellen welche Tabelle angelegt werden soll. Pro Parzelle wird jeweils eine eigene Liste angelegt       
        DBConnection dbconn= new DBConnection(2);     // a class to manage the conection to a database
        dbconn.openDBConnection(DBConnection.ACCESS, 1, lokaleDB, "", "", false, true);
        try{ 
// letzte Aufnahme feststellen
             Statement stmt = dbconn.Connections[1].createStatement(); 
             
             stmt.execute("DROP TABLE Eingabe ");
           }
          catch (Exception e){  System.out.println(e); ergebnis="Tabelle konnte nicht gelöscht werden, Tabelle nicht vorhanden !";}
          

        dbconn.closeAll();
        return ergebnis;
    }
    
    
    public boolean isInteger( String input ){  
        try{  
            Integer.parseInt( input );  
            return true;  
        }  
        catch( Exception e){  
            return false;  
        }  
    }
    

    
    
}
