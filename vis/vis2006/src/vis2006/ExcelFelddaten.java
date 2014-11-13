/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vis2006;
import java.io.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
//import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.*;
import java.lang.Object.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;



/**
 * ExcelFelddaten:
 *  - Felddatendatei erzeugen
 *  - Plausibilitäts-Check für Felddatendatei 
 *  - Felddaten in Eingabetabelle übernehmen
 * 
 * @author sprauer
 */
public class ExcelFelddaten {
    JFrame frame;
    String id8; 
    int auf2; /* letzte bereits eingegebene Aufnahme*/
    int auf1; /* vorletzte eingegebene Aufnahme*/
    
    /**
     * Creates new form ExcelFelddaten
     */
    public ExcelFelddaten(JFrame frame, String edvid) {
        this.id8 = edvid;
        this.frame = frame;

    }
    
    /**
     * Eine Excel-Felddatendatei je Parzelle erzeugen
     * @param con Datenbankverbindung zum auslesen der Daten (Connection)
     * @param inOrdner Ordner, in dem die Felddaten abgelegt werden (String)
     * @param letzteAuf Nummer der letzten bereits eingegebenen Aufnahme (int)
     * @param vorletzteAuf Nummer der vorletzten eingegebenen Aufnahme (int)
     * @param uAufn Felddatenversion für die Ablesung von Dauerumfangmessungen (boolean)
     */   
    public boolean erzeugen(Connection con, String inOrdner, int letzteAuf, int vorletzteAuf, boolean uAufn){
        auf2 = letzteAuf;
        auf1 = vorletzteAuf;
        boolean uAuf = uAufn;
        int nspalten = 0;
        boolean ok = false;
        Connection dbconnect = con; 
   
        // Kreuzkluppung in der letzten Aufnahme?
        boolean kreuzklupp = false;
        Statement stmt3 = null;
        ResultSet rs3 = null;
        try{
            stmt3 = dbconnect.createStatement();
            rs3 = stmt3. executeQuery("SELECT sum(dkrz1+dkrz2) as sum FROM Baum WHERE edvid = '"+id8+"' " +
                    "AND auf = "+letzteAuf+";");
            int sum = 0;
            if(rs3.next()) sum = rs3.getInt("sum");
            if(sum > 0) kreuzklupp = true;
        }catch(Exception e){e.printStackTrace();
        }finally{
            try{
                if(rs3!=null) rs3.close();
                if(stmt3!=null) stmt3.close();
            }catch(Exception e){e.printStackTrace();}
        }
        
        // Exceldatei anlegen
        try{
            OutputStream os=new FileOutputStream(inOrdner+"/"+id8+".xls");
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(id8);
            HSSFRow row = null;  // Create a row and put some cells in it. Rows are 0 based.
            int zeile=0;
            row = sheet.createRow(zeile);
            row.createCell(0).setCellValue("EDVID");
            row.createCell(1).setCellValue("Nr");
            row.createCell(2).setCellValue("Art");
            row.createCell(3).setCellValue("ZF"); 
            row.createCell(4).setCellValue("OU");
            row.createCell(5).setCellValue("MH");
            row.createCell(6).setCellValue("D1");
            row.createCell(7).setCellValue("A1");
            row.createCell(8).setCellValue("H1");
            row.createCell(9).setCellValue("Alt2");
            row.createCell(10).setCellValue("D2_1");
            row.createCell(11).setCellValue("D2_2");
            row.createCell(12).setCellValue("D2");
            row.createCell(13).setCellValue("A2");
            row.createCell(14).setCellValue("H2");
            row.createCell(15).setCellValue("K2");
            row.createCell(16).setCellValue("B2");
            row.createCell(17).setCellValue("MHN");
            if(uAuf) row.createCell(18).setCellValue("DDN");
            else row.createCell(18).setCellValue("DN");
            row.createCell(19).setCellValue("DNK");
            row.createCell(20).setCellValue("AN");
            row.createCell(21).setCellValue("EN");
            row.createCell(22).setCellValue("ALT_EN");
            row.createCell(23).setCellValue("HN");
            row.createCell(24).setCellValue("KN");
            row.createCell(25).setCellValue("R");
            row.createCell(26).setCellValue("Bemerk");
            row.createCell(27).setCellValue("ZFN");
            row.createCell(28).setCellValue("OUN");
            row.createCell(29).setCellValue("Qua");
            nspalten = 30;
            zeile = zeile + 1;
            
// Nebenaufnahme?
            Statement stmt2 = dbconnect.createStatement();
            ResultSet rs2 = stmt2.executeQuery("SELECT * FROM Auf WHERE edvid =  \'"+id8+"\'  AND auf > "+(auf1-1)+
                    " ORDER BY auf");
            int nebenauf = 0;
            int nnebenauf = 0;
            String typAuf1 = "H";
            String typAuf2 = "H";
            while (rs2.next()){
                int aufx = rs2.getInt("auf");
                String typx = "";
                Object typtest = rs2.getObject("typ");
                if(typtest != null) typx = typtest.toString().trim();          
                
                if (typx.equals("N")){
                    nnebenauf = nnebenauf+1;
                    if(aufx == auf2) {
                        nebenauf = auf2;
                        typAuf2 = "N";
                    } else{
                        nebenauf = auf1;
                        typAuf1 = "N";
                    }
                }
                
                if(aufx == auf1 && typx.equals("U")) typAuf1 = "U";
                if(aufx == auf2 && typx.equals("U")) typAuf2 = "U";
                
            }
            rs2.close();    
            stmt2.close();
      
// Variablen definieren
             int art1=0; double d1=0.0; int h1=0; String a1=""; String nr1="";
             int art2=0; double d2=0.0; int h2=0; int k2=0; String nr2=""; String zf=""; String ou=""; int mh=0;
             String a2=""; int alt2=0; String bemerk2=""; String rand=""; int dk21=0; int dk22=0;
             
             // Vordurchmesser aus welcher Spalte?
             String d1AusSpalte = "dmess";
             String d2AusSpalte = "dmess";
             if(typAuf1.equals("U") && uAuf) d1AusSpalte = "ddauer";
             if(typAuf2.equals("U") && uAuf) d2AusSpalte = "ddauer";
             
// 2 Hauptaufnahmen         
             if (nnebenauf == 0) {
                 
                 Statement stmt = dbconnect.createStatement();
                 String zusatz = "";
                 if(!uAuf) zusatz = "AND r <> 'P' ";                        // keine Probenahmebäume für ertragskundl. Aufnahmen
                 String sqltext = "SELECT * FROM Baum WHERE edvid = \'" + id8 +
                         "\'  AND auf > " + (auf1 - 1) + 
                         " AND trim(nr) NOT IN (\'nurH\', \'cm\', \'onum\', \'HG\', \'HB\') " + zusatz
                         + " ORDER BY nr,art, auf ";                // Ergänzung am 12.01.2011: art
                 ResultSet rs = stmt.executeQuery(sqltext);  
                 while (rs.next()) {
                     int aufx = rs.getInt("auf");
                     String nrx = rs.getObject("nr").toString().trim();
                     // bei der ersten Aufnahme gemessen                
                     if (aufx == auf1) {
                         art1 = rs.getInt("art");
                         d1 = rs.getInt(d1AusSpalte);
                         if(d1AusSpalte.equals("ddauer")) d1 = d1/10;
                         h1 = rs.getInt("h");
                         a1 = "";                                  // 2.2.2011
                         Object a1obj = rs.getObject("a");
                         if(a1obj != null) a1 = a1obj.toString().trim();
                         nr1 = nrx;
                     }
                     if (aufx == auf2) {
                         art2 = rs.getInt("art");
                         alt2 = rs.getInt("alt");
                         dk21 = rs.getInt("dkrz1");
                         dk22 = rs.getInt("dkrz2");
                         d2 = rs.getInt(d2AusSpalte);
                         if(d2AusSpalte.equals("ddauer")) d2 = d2/10;
                         h2 = rs.getInt("h");
                         k2 = rs.getInt("k");
                         a2="";
                         Object a2obj = rs.getObject("a");
                         if(a2obj != null) a2 = a2obj.toString().trim();
                         rand = "";
                         Object randobj = rs.getObject("r");
                         if(randobj != null) rand = randobj.toString().trim();
                         nr2 = nrx;
                         zf = "";
                         Object zfobj = rs.getObject("zf");
                         if(zfobj != null) zf = zfobj.toString().trim();
                         ou = "";
                         Object ouobj = rs.getObject("ou");
                         if(ouobj != null) ou = ouobj.toString().trim();
                         mh = rs.getInt("mh");
                         bemerk2 = "";
                         Object bemo = rs.getObject("bemerk");
                         if(bemo != null) bemerk2 = bemo.toString().trim();
                         
                         if ((art1 == art2) && nr1.compareTo(nr2) != 0) {
                             d1 = 0;
                             h1 = 0;
                             a1 = "";
                         }
                         
                         if (d2 > 0) {
                             row = sheet.createRow((short) zeile);
                             row.createCell(0).setCellValue(id8);
                             row.createCell(1).setCellValue(nr2);
                             row.createCell(2).setCellValue(art2);
                             row.createCell(3).setCellValue(zf);
                             row.createCell(4).setCellValue(ou);
                             row.createCell(5).setCellValue(mh);
                             row.createCell(6).setCellValue(d1);
                             row.createCell(7).setCellValue(a1);
                             row.createCell(8).setCellValue(h1);
                             row.createCell(9).setCellValue(alt2);
                             row.createCell(10).setCellValue(dk21);
                             row.createCell(11).setCellValue(dk22);
                             row.createCell(12).setCellValue(d2);
                             row.createCell(13).setCellValue(a2);
                             row.createCell(14).setCellValue(h2);
                             row.createCell(15).setCellValue(k2);
                             row.createCell(16).setCellValue(bemerk2);
                             row.createCell(25).setCellValue(rand);

                             zeile = zeile + 1;
                         //out.println(idSelected+","+nr2+","+art2+","+zf+","+ou+","+d1+","+a1+","+alt2+","+d2+","+a2+","+h2+","+
                         //         k2+","+rand+",,,,,,,,,,");    

                         }
                         // Werte der ersten Aufnahme zurücksetzen, falls keine Angaben für nächsten Baum(12.01.2011)
                         d1 = 0;
                         art1 = 0;
                         h1 = 0;
                         a1 = "";
                     }
                    } // Ende While
             }             
// Letzte Aufnahme ist Nebenaufnahme
             if(nnebenauf == 1 && nebenauf == auf2){
                    Statement stmt = dbconnect.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM Baum WHERE edvid = \'" + id8 +
                            "\'  AND auf > " + (auf1 - 1) + 
                            " AND trim(nr) NOT IN (\'nurH\', \'cm\', \'onum\', \'HG\', \'HB\') " + " ORDER BY nr ASC, auf DESC ");
                    while (rs.next()) {
                        int aufx = rs.getInt("auf");
                        String nrx = rs.getObject("nr").toString();
                        if (aufx == auf2) {
                            art2 = rs.getInt("art");
                            alt2 = rs.getInt("alt");
                            d2 = rs.getInt("dmess");
                            dk21 = rs.getInt("dkrz1");
                            dk22 = rs.getInt("dkrz2");
                            h2 = rs.getInt("h");
                            k2 = rs.getInt("k");
                            a2 = rs.getObject("a").toString().trim();
                            rand = rs.getObject("r").toString().trim();
                            nr2 = nrx;
                            zf = rs.getObject("zf").toString().trim();
                            ou = rs.getObject("ou").toString().trim();
                            mh = rs.getInt("mh");
                            bemerk2 = "";
                            Object bemo = rs.getObject("bemerk");
                            if(bemo != null) bemerk2 = bemo.toString().trim();
                        }
                        if (aufx == auf1) {
                            art1 = rs.getInt("art");
                            d1 = rs.getInt(d1AusSpalte);
                            if(d1AusSpalte.equals("ddauer")) d1 = d1/10;
                            a1 = rs.getObject("a").toString().trim();
                            nr1 = nrx;

                            if (art1 != art2 || nr1.compareTo(nr2) != 0) {
                                d2 = 0; dk21 = 0; dk22 = 0; alt2 = 0; h2 = 0; k2 = 0; a2="";
                                rand = rs.getObject("r").toString().trim(); 
                                zf = rs.getObject("zf").toString().trim();
                                ou = rs.getObject("ou").toString().trim();
                                mh = rs.getInt("mh");
                            }

                            row = sheet.createRow(zeile);
                            row.createCell(0).setCellValue(id8);
                            row.createCell(1).setCellValue(nr1);
                            row.createCell(2).setCellValue(art1);
                            row.createCell(3).setCellValue(zf);
                            row.createCell(4).setCellValue(ou);
                            row.createCell(5).setCellValue(mh);
                            row.createCell(6).setCellValue(d1);
                            row.createCell(7).setCellValue(a1);
                            row.createCell(8).setCellValue(h1);
                            row.createCell(9).setCellValue(alt2);
                            row.createCell(10).setCellValue(dk21);
                            row.createCell(11).setCellValue(dk22);
                            row.createCell(12).setCellValue(d2);
                            row.createCell(13).setCellValue(a2);
                            row.createCell(14).setCellValue(h2);
                            row.createCell(15).setCellValue(k2);
                            row.createCell(16).setCellValue(bemerk2);
                            row.createCell(25).setCellValue(rand);

                            zeile = zeile + 1;
                        }
                    }
                }
//Vorletzte Aufnahme ist Nebenaufnahme
             if(nnebenauf == 1 && nebenauf == auf1){ 
                 Statement stmt = dbconnect.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM Baum WHERE edvid = \'" + id8 +
                         "\'  AND auf = " + auf2 +
                         " AND trim(nr) NOT IN (\'nurH\', \'cm\', \'onum\', \'HG\', \'HB\') " + " ORDER BY nr");
                 while (rs.next()) {
                     nr2 = rs.getObject("nr").toString();
                     art2 = rs.getInt("art");
                     alt2 = rs.getInt("alt");
                     d2 = rs.getInt(d2AusSpalte);
                     if(d2AusSpalte.equals("ddauer")) d2 = d2/10;
                     dk21 = rs.getInt("dkrz1");
                     dk22 = rs.getInt("dkrz2");
                     h2 = rs.getInt("h");
                     k2 = rs.getInt("k");
                     a2 = rs.getObject("a").toString().trim();
                     rand = rs.getObject("r").toString().trim();
                     zf = rs.getObject("zf").toString().trim();
                     ou = rs.getObject("ou").toString().trim();
                     mh = rs.getInt("mh");
                     bemerk2 = "";
                     Object bemo = rs.getObject("bemerk");
                     if(bemo != null) bemerk2 = bemo.toString().trim();

                     if (d2 > 0) {
                         row = sheet.createRow(zeile);
                         row.createCell(0).setCellValue(id8);
                         row.createCell(1).setCellValue(nr2);
                         row.createCell(2).setCellValue(art2);
                         row.createCell(3).setCellValue(zf);
                         row.createCell(4).setCellValue(ou);
                         row.createCell(5).setCellValue(mh);
                         row.createCell(6).setCellValue("");
                         row.createCell(7).setCellValue("");
                         row.createCell(8).setCellValue("");
                         row.createCell(9).setCellValue(alt2);
                         row.createCell(10).setCellValue(dk21);
                         row.createCell(11).setCellValue(dk22);
                         row.createCell(12).setCellValue(d2);
                         row.createCell(13).setCellValue(a2);
                         row.createCell(14).setCellValue(h2);
                         row.createCell(15).setCellValue(k2);
                         row.createCell(16).setCellValue(bemerk2);
                         row.createCell(25).setCellValue(rand);
                         
                         zeile = zeile + 1;
                     }               
                 } // Ende While
             }
             if (nnebenauf == 2) {
                 System.out.println("Keine Werte, die letzten beiden Aufnahmen sind Nebenaufnahmen!");
             }
             if(!kreuzklupp){  // Spalten für Kreuzkluppung löschen
                 Iterator rowIter = sheet.rowIterator();
                 while (rowIter.hasNext()) {
                     HSSFRow rowx = (HSSFRow)rowIter.next();
                     HSSFCell cell1 = rowx.getCell(10);
                     HSSFCell cell2 = rowx.getCell(11);
                     rowx.removeCell(cell1);
                     rowx.removeCell(cell2);
                     System.out.println("***** Zeile *****");
                     for(int i = 12; i<nspalten; i++){
                         Integer zieltemp = i-2;
                         short ziel =  zieltemp.shortValue();
                         System.out.println("Verschieben von Spalte "+i+" auf "+ziel);
                         if(rowx.getCell(i) != null) rowx.moveCell(rowx.getCell(i), ziel);
                     }
                     
                 }
                 nspalten = nspalten -2;
             }
             
             if(uAuf){    // für Ablesung von Dauerumfangmessbändern nicht benötigte Spalten löschen
                 // Spalte für Alter nicht löschen -> nötig für das Alter in der nächsten Aufnahme
                 int[] zellnums;
                 zellnums = new int[] {3,4,5,8,10,11,14,15,17,19,23,24,27,28,29}; //{3,4,5,8,10,11,14,15,16,18,22,23,26,27,28};
                 if(!kreuzklupp) zellnums = new int[] {3,4,5,8,12,13,15,17,21,22,25,26,27}; //{3,4,5,8,12,13,14,16,20,21,24,25,26};
                 
                 for(int i = 0; i < zellnums.length; i++){
                     SheetUtility.deleteColumn(sheet, zellnums[i]);

                     
                     // Spaltenindizes aktualisieren
                     if(i <  zellnums.length-1){
                         for(int n = (i+1); n < zellnums.length; n ++) zellnums[n] = zellnums[n]-1;
                     }
                     nspalten = nspalten - 1;
                 }
                 // Status-Spalte einfügen
                 HSSFRow row0 = (HSSFRow)sheet.getRow(0);
                 row0.createCell((int)row0.getLastCellNum()).setCellValue("SN");

                 /*
                  * Funktioniert nicht beim Verschieben verschwindet AN oder andere Felder
                 int i = 0;
                 boolean gefunden = false;
                 while(i < nspalten && !gefunden){
                     HSSFRow row0 = (HSSFRow)sheet.getRow(0);
                     if(row0.getCell(i).getStringCellValue().equals("DDN")){
                         gefunden = true;
                         for(int j = (int) row0.getLastCellNum()-1; j > i; j--){  // LastCellNum 1-basiert
                             System.out.println(row0.getCell(j).getStringCellValue() + " nach Spalte " + (j+1));
                             row0.moveCell(row0.getCell(j), (short)(j+1));
                         }
                         row0.createCell(i+1).setCellValue("SN");
                     }
                     i++;
                     // Wieso verschwindet "AN"? wird nicht ausgegeben, obwohl es während des Debugging angezeigt wird!
                     if(row0.getCell(9) != null)   row0.getCell(9).setCellValue("AN");

                 }
                 */

                 // Keine Ahnung warum, aber ohne diese Zeilen werde die Spalten OUN und Qua einfach nicht gelöscht
                 sheet.getRow(0).createCell(29).setCellType(HSSFCell.CELL_TYPE_BLANK);
                 SheetUtility.deleteColumn(sheet, 27);
             }

             wb.write(os);
             os.close();
             
             //        out.close();
             ok = true;
        } catch (Exception e)  {e.printStackTrace();}
        
        return ok;
    }

  
    
    /**
     * Daten aus Excel-Felddatendatei in Eingabetabelle übernehmen
     * 
     *  @param  pfad  Angabe der einzulesenden Exceldatei (AbsolutePath)
     *  @param  localcon  Datenbank in der die Eingabetabelle erstellt werden soll (Connection)
     */   
    public String uebernehmen(String pfad, Connection localcon, int letztAuf){
        Connection localconnect = localcon;
        String erg = "Fehler!";
        InputStream input=null;
        Statement stmt = null;
        ResultSet rs = null;
        
// Vorbereitung: schreiben in lokale DB
        int nid = 0; // Spalte "id" in der Eingabetabelle
        try{
            stmt = localconnect.createStatement(); 
            rs = stmt.executeQuery("SELECT max(id) as maxi FROM Eingabe;");
            if(rs.next()) nid = rs.getInt("maxi");
            rs.close();
            
            input = new FileInputStream(pfad);
            HSSFWorkbook wb = new HSSFWorkbook(input);
            input.close();
            HSSFSheet sheet = wb.getSheetAt(0); // nur das erste Arbeitsblatt
            ExcelBaum eBaum = new ExcelBaum();
            boolean ok = eBaum.setSpaltenordnung(sheet);
            
                 
            
            if(ok){
                Set<String> spalten = new HashSet<String>(Arrays.asList("EDVID","NR","ART","ZF","OU",
                                "MH","MHN","D1","A1","H1","ALT2","D2_1","D2_2","D2","A2","H2","K2","B2","DN","DNK",
                                "AN","EN","ALT_EN","HN","KN","ZFN","OUN","R","BEMERK","QUA")); // immer vorhandene/nicht einzufügende Spalten
                // zusätzliche Spalten und Werte
                Set<String> zusatzString = new HashSet<String>(Arrays.asList("BEZNR","SEIG")); // Stringtyp-Zusatzspalten
                Map<String, Integer> spaltord = eBaum.getSpaltenordnung();
                Iterator<String> iter = spaltord.keySet().iterator();
                List<String> zusatzSpalten = new ArrayList<String>();
                String mehrSpalten = "";
                while(iter.hasNext()){
                    String spalx = iter.next();
                    if(!spalten.contains(spalx)){
                        zusatzSpalten.add(spalx);
                        mehrSpalten = mehrSpalten + ", " + spalx;
                        if(zusatzString.contains(spalx))  {
                            if(spalx.equals("BEZNR")) stmt.executeUpdate("ALTER TABLE Eingabe ADD COLUMN " + spalx + " STRING(6) NOT NULL");
                            else if(spalx.equals("SEIG")) stmt.executeUpdate("ALTER TABLE Eingabe ADD COLUMN " + spalx + " STRING(20)");
                            else stmt.executeUpdate("ALTER TABLE Eingabe ADD COLUMN " + spalx + " STRING NOT NULL");
                        }
                        else stmt.executeUpdate("ALTER TABLE Eingabe ADD COLUMN " + spalx + " INTEGER");
                    }
                }
              
                
                String zeilenErgebnis = "";
                boolean einlesenAbbrechen = false;
                Iterator it = sheet.rowIterator();
                while(it.hasNext()){
                    HSSFRow row = (HSSFRow)it.next();
                    if(row.getRowNum() > 0){ //nur für Datenzeilen (nicht Spaltenüberschriften)
                        nid = nid+1;
                        if(einlesenAbbrechen) break;
                        
                        zeilenErgebnis = eBaum.zeileLesen(row);
                        if(zeilenErgebnis.length()>0) break;

                        // nicht vollständig leere Zeilen nach Ende der Daten  (20.06.2011)
                        // keine edvid, keine Nr, keine Art, kein Durchmesser -> Zeile wird als leer betrachtet
                        //    und darauffolgende Zeilen werden ignoriert
                        // (s.auch ExcelFelddatenCheck.java Methode jButton2ActionPerformed())
                        if(eBaum.edvid.equals("") && eBaum.nr.equals("") && eBaum.art<1 && eBaum.dn<1){
                            einlesenAbbrechen = true;
                            String text = "Zeile " + (row.getRowNum()+1) + " scheint leer zu sein.\n"
                                    + "Eventuell vorhandene Daten ab dieser Zeile werden ignoriert.";
                            JOptionPane.showMessageDialog(frame, text);
                            break;
                        }

                        String zfNeu = eBaum.zf;
                        if(!eBaum.zfn.trim().isEmpty()){
                            zfNeu = eBaum.zfn;
                            if(!eBaum.zf.equals(eBaum.zfn) && !eBaum.zf.trim().equals(""))System.out.println("\nUmsetzer, Baum " + eBaum.nr
                                    + "(" + eBaum.art + "): " + eBaum.zf + " -> " + eBaum.zfn);
                        }
                        String ouNeu = eBaum.ou;
                        if(!eBaum.oun.trim().isEmpty()){
                            ouNeu = eBaum.oun;
                            if(!eBaum.ou.equals(eBaum.oun) && !eBaum.ou.trim().equals(""))System.out.println("\nUmsetzer, Baum " + eBaum.nr
                                    + "(" + eBaum.art + "): " + eBaum.ou + " -> " + eBaum.oun);
                        }
                        String mehrWerte = "";
                        Iterator itzs = zusatzSpalten.iterator();
                        while(itzs.hasNext()){
                            String spalx = itzs.next().toString();
                            if(zusatzString.contains(spalx)){
                                String stringwert = "";
                                if(eBaum.zusatzWerte.get(spalx) != null) stringwert = eBaum.zusatzWerte.get(spalx);
                                mehrWerte = mehrWerte + ",'" + stringwert + "'";
                            }
                            else mehrWerte = mehrWerte + "," + eBaum.zusatzWerte.get(spalx);

                        }
                        
                        // Werte in Eingabetabelle schreiben (! bei Änderungen, auch Variable "spalten" ändern?)
                        String sqlstr = "INSERT INTO Eingabe "+
                                "(id, edvid , nr , nralt , art , ZF , OU , d1 , a1 , alt2 , "+
                                "d2 , a2 , h2 , k2 , mh , dn , dkn , an , en , alt_en, hn , kn , ZFN , OUN , "+
                                "R , Bemerk , Qualitaet" + mehrSpalten + " ) "+
                                "VALUES  ("+nid+", '"+eBaum.edvid+"', '"+eBaum.nr+"', ' ', "+eBaum.art
                                +", '"+eBaum.zf+"', '"+eBaum.ou+"', "+eBaum.d1+", '"+eBaum.a1+"',"+eBaum.alt2+", "
                                +eBaum.d2+",'"+eBaum.a2+"',"+eBaum.h2+","+eBaum.k2+","+eBaum.mh+","
                                +eBaum.dn+","+eBaum.dnk+",'"+eBaum.an+"','"+eBaum.en+"',"+eBaum.alt_en+","+eBaum.hn+","+eBaum.kn+",'"
                                +zfNeu+"','"+ouNeu+"','"+eBaum.r2+"','"+eBaum.bem+"','"+eBaum.qual+"'" + mehrWerte + ")";
                        // Nicht übernommen: H1, B2
                        //System.out.println(sqlstr);
                        stmt.execute(sqlstr);
                        
                        
                        
                        
                    }  
                }
                System.out.println("\n");
                
                //Daten aus Datenbank ergänzen
                if(letztAuf>0){
                    int zeilen = stmt.executeUpdate("INSERT INTO Eingabe (id, edvid, nr, nralt, art, ZF, OU, alt2, d2, a2, h2, k2, mh ,dn, dkn, alt_en, hn, kn, ZFN, OUN, R) "+
                        "SELECT Baum.id, Baum.edvid, Baum.nr, Baum.nralt, Baum.art, Baum.ZF, Baum.OU, Baum.alt, Baum.d, Baum.a, Baum.h, Baum.k, Baum.mh, 0, 0, 0, 0, 0, Baum.ZF, Baum.OU, Baum.r "+
                        "FROM Baum LEFT JOIN Eingabe ON Baum.art = Eingabe.art AND TRIM(Baum.nr) = TRIM(Eingabe.nr) "+ 
                        "WHERE Baum.edvid = '"+eBaum.edvid+"' AND Baum.auf ="+letztAuf+" AND " +
                        "(TRIM(Baum.a)='') " +
                        "AND TRIM(Baum.nr) NOT IN (\'nurH\', \'cm\', \'onum\', \'HG\', \'HB\') " +
                        "AND Eingabe.nr IS NULL");
                    System.out.println(zeilen+" Bäume aus Baum ergänzt");
                }
     
                erg = "Daten übertragen."; 
            } else erg = "Fehler beim Einlesen: " +eBaum.getHinweis();
            
            System.out.print("fertig!");
            stmt.close();
            localconnect.close();               
        } catch(Exception e){
            System.out.println("\nFehler beim Einlesen: ");e.printStackTrace();
        } finally {
            try{
            if(input != null) input.close();
            stmt.setFetchDirection(ResultSet.FETCH_REVERSE);
            if(!stmt.isClosed()) stmt.close();
            } catch (Exception e){} // Keine Meldung ausgeben, passiert, wenn Zellen ohne Inhalt nicht ganz leer
        }

        return(erg);

    }
    
        
}
