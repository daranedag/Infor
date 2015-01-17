/*
 * CheckVisData.java
 *
 * Created on 2. Mai 2006, 11:07
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
public class CheckVisData {
    PrintWriter out=null;    
    /** Creates a new instance of CheckVisData */
    public CheckVisData() {
    }

    public PrintWriter createCheckReport(String file, String edvid){
      try {
       OutputStream os=new FileOutputStream(file);
       out=new PrintWriter(new OutputStreamWriter(os));
       out.println("<style type=\"text/css\"> span.titel{color:#137B21; font:185% bold;} " +
               "table{border-collapse: collapse; }td{border:1px solid black; padding:5px;}</style>");
       out.println("<span class=\"titel\">Hinweise</span>");
       if(edvid != null){ 
           out.println("<br><strong> - para "+edvid.substring(0,6)+", Parcela "+edvid.substring(6,8)+" - </strong>");
           out.println("<br><strong>Los siguientes registros pueden estar defectuosos. Comprueba, por favor.</strong><br>");
       }
       
       out.println("<br><TABLE><TR bgcolor=\"#7CB083\"><TD><strong>Fallas</strong></TD><TD><strong>Registro</strong></TR>");
      }
      catch (Exception e) {System.out.println(e+ " Ningún archivo escrito");}
      return(out);
    }  
    
    public void closeCheckReport(){
        out.println("</table>");
        out.close();
    }
    
    public int doppelteDatensaetze(String ids, DBConnection dbconn){
        int ndoppelRueck = -1;
        int ndoppel=0;
        String nrAlt="";
        int aufAlt=0;
        int artAlt=0;
        int zeileAlt=0 ;

        Statement stmt = null;
        ResultSet rs = null;
        try {
           stmt = dbconn.Connections[0].createStatement(); 
           rs = stmt.executeQuery("SELECT * FROM Baum WHERE edvid= '"+ids+"' ORDER BY art,nr,auf");
           while (rs.next()){
                int zeile = rs.getInt("id");
                String nrx = rs.getObject("nr").toString().trim();
                int aufx = rs.getInt("auf");
                int artx = rs.getInt("art");
                if (aufx == aufAlt && nrx.equals(nrAlt) && artx == artAlt && nrx.length()>0
                        && nrx.indexOf("nurH")<0 && nrx.indexOf("cm")<0 && ! nrx.trim().equals("onum")
                        && ! nrx.trim().equals("HB")&& ! nrx.trim().equals("HG")) {
                    ndoppel=ndoppel+1;
                    out.println("<TR><TD>Registro duplicado</TD><TD>"+ids+" "+aufx+" "+artx+" "+nrx+" </TD></TR>");
                }
                zeileAlt=zeile;
                nrAlt=nrx;
                aufAlt=aufx;
                artAlt=artx;
           }
           ndoppelRueck = ndoppel;
        } catch (Exception e){
            System.out.println("Duplicados de base de datos :"+e); }
        finally {
            try{
                if(rs!=null) rs.close();
                if(stmt!=null) stmt.close();   
            }
            catch(Exception ex){ex.printStackTrace();}
        }

        Statement stmt2 = null;
        ResultSet rs2 = null;
        try {
           stmt2 = dbconn.Connections[0].createStatement();
           rs2 = stmt2.executeQuery("SELECT trim(Auf.id) as id FROM Auf WHERE Auf.edvid = '"
                   + ids + "' GROUP BY Auf.id HAVING Count(trim(Auf.id))>1;");
           while (rs2.next()){
               String id = "";
               Object idobj = rs2.getObject("id");
               if(idobj != null) id = idobj.toString();
               ndoppel=ndoppel+1;
               out.println("<TR><TD>Id duplicada en tabla de grabación</TD><TD> "+id+" </TD></TR>");
            }
        } catch (Exception e){
            System.out.println("Duplicados de base de datos  :"+e); }
        finally {
            try{
                if(rs2!=null) rs2.close();
                if(stmt2!=null) stmt2.close();
            }
            catch(Exception ex){ex.printStackTrace();}
        }

// Stammverteilungstabelle prüfen
        int stammFehler = checkStammvDoppel(ids, dbconn);
        if (stammFehler < 0) ndoppelRueck = -1;
        else ndoppelRueck = ndoppelRueck + stammFehler;        
        
       return ndoppelRueck;
    }
    
    private int checkStammvDoppel(String edvid, DBConnection dbconn) {
        int nFehlerRueck = -1;
        int nFehler = 0;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = dbconn.Connections[0].createStatement();
            rs = stmt.executeQuery("SELECT auf, art, nr FROM Stammv WHERE edvid = '" + edvid + "' GROUP BY auf,art,nr HAVING count(nr )>1 ;");
            int aufx = 0;
            int artx = 0;
            String nrx = "";
            while (rs.next()) {
                nFehler = nFehler + 1;
                aufx = rs.getInt("auf");
                artx = rs.getInt("art");
                nrx = "";
                Object nrtest = rs.getObject("nr");
                if (nrtest != null) {
                    nrx = nrtest.toString().trim();
                }
                out.println("<TR><TD>Entradas duplicadas en tabla Stammv</TD><TD>" + aufx + " " + artx + " " + nrx + " </TD></TR>");
            }
            nFehlerRueck = nFehler;

        } catch (Exception e) {
            System.out.println("Comprobar distribución de la deformación: " + e);
        }
        finally{
            try{
                if(rs != null) rs.close();
                if(stmt != null) stmt.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return nFehlerRueck;
    }

      
    public int plausiCheck(String ids, DBConnection dbconn) {
        int nFehlerRueck = -1;
        int nFehler = 0;

        int aufindex = - 1;
        int aufnahmen[] = new int[100];
        String auftyp[] = new String[100];
        Set<Integer> qualAuf = new HashSet<Integer>();  // Aufnahmen mit Qualitätsansprache
        int auf1 = 0;
        int jahr1 = 0;
        int monat1 = 0;
        double alt1 = 0.0;
        int d1 = 0;
        int h1 = 0;
        String aus1 = "";
        String nr1 = "";
        int art1 = 0;
        double fac1 = 0.0;
        boolean hinweisNurHAlt0 = true;   // Hinweise nur für den ersten Fall
        boolean hinweisAus1 = true;
        boolean hinweisAlterdiff = true;
        boolean hinweisRepfacAussch = true;

        Set<String> aInhalt = new HashSet<String>();
        aInhalt.add("");aInhalt.add("D");aInhalt.add("T");aInhalt.add("W");aInhalt.add("F");aInhalt.add("S");
            aInhalt.add("K");

        Statement stmt = null;
        ResultSet rs = null;

// Daten lesen
        try {
            stmt = dbconn.Connections[0].createStatement();
            ResultSet rsAuf = stmt.executeQuery("SELECT Auf.typ, Auf.auf, Auf.qualitat FROM Auf "
                    + " WHERE Auf.edvid= '" + ids + "' ORDER BY Auf.auf;");
            while (rsAuf.next()){
                // Liste mit Aufnahmenummer und Typ und Aufnahme mit Qualitätsansprache
                aufindex = aufindex + 1;
                aufnahmen[aufindex] = rsAuf.getInt("auf");
                String typ = "";
                Object typtest = rsAuf.getObject("typ");
                if(typtest != null) typ = typtest.toString().trim();
                auftyp[aufindex] = typ;
                int qualAufx = rsAuf.getInt("qualitat");
                if(!qualAuf.contains(qualAufx) && qualAufx > 0) qualAuf.add(qualAufx);
            }
            rsAuf.close();


            // Nebenaufnahme markiert?
            Set<Integer> nebenaufs = new HashSet<Integer>();
            //System.out.println("SELECT DISTINCT auf FROM Baum WHERE edvid = '"+ids+"' "
            //        + "AND auf NOT IN (SELECT DISTINCT auf FROM Baum WHERE edvid = '"+ids+"' AND trim( a ) = '')");
            ResultSet rsNAuf = stmt.executeQuery("SELECT DISTINCT auf FROM Baum WHERE edvid = '"+ids+"' "  // diese Abfrage kann bei vielen Bäumen sehr langsam sein!
                    + "AND auf NOT IN (SELECT DISTINCT auf FROM Baum WHERE edvid = '"+ids+"' AND trim( a ) = '')");
           while(rsNAuf.next()){
              nebenaufs.add(rsNAuf.getInt("auf")); 
           }
            rsNAuf.close();

            String sql = "SELECT Baum.*, Auf.jahr, Auf.monat, Auf.typ FROM Baum " +
                    "LEFT JOIN Auf ON Baum.edvid = Auf.edvid AND Baum.auf = Auf.auf " +
                    "WHERE Baum.edvid= '"+ ids +"' ORDER BY Baum.art,Baum.nr,Baum.auf;";
            //System.out.println(sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String nr2 = rs.getObject("nr").toString().trim();
                int auf2 = rs.getInt("auf");
                //System.out.println("Aufnahme: "+auf2+" Baumnummer: "+nr2);
                int jahr2 = rs.getInt("jahr");
                int monat2 = rs.getInt("monat");
                String typ2 = rs.getString("typ");
                int art2 = rs.getInt("art");
                double alt2 = rs.getDouble("alt");
                int d2 = rs.getInt("d");
                int dmess2 = rs.getInt("dmess");
                int ddauer2 = rs.getInt("ddauer");
                int mh2 = rs.getInt("mh");
                int anz2 = rs.getInt("anzahl");
                double fac2 = rs.getDouble("repfl");
                int h2 = rs.getInt("h");
                int k2 = rs.getInt("k");
                String zf2 = "";
                Object zftest = rs.getObject("ZF");
                if(zftest != null) zf2 = zftest.toString().trim();
                String ou2 = "";
                Object outest = rs.getObject("OU");
                if(outest != null) ou2 = outest.toString().trim();
                String aus2 = "";
                Object atest = rs.getObject("a");
                if(atest != null) aus2 = atest.toString().trim();
                String ein2 = "";
                Object etest = rs.getObject("e");
                if(etest != null) ein2 = etest.toString().trim();
                String rand = "";
                Object rtest = rs.getObject("r");
                if(rtest != null) rand = rtest.toString().trim();
                String bemerk = "";
                Object bemtest = rs.getObject("bemerk");
                if(bemtest != null) bemerk = bemtest.toString().trim();
                //System.out.println("Baumnummer: "+nr2);

// Aufnahme 0 (führt zum Abbruch, wenn ein Feld in der Auf-Spalte der Baumtabelle nicht ausgefüllt ist) 2.2.2011
                if(auf2 == 0){
                     System.out.println("Falta el número de admisión o número de admisión es 0 en tabla Auf o en tabla Baum");
                }
                
// Ungekennzeichnete Nebenaufnahme
                if(nebenaufs.contains(auf2) & !typ2.equals("N")){
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Desmarcar al grabar?</TD><TD>" + auf2 + "("+jahr2+") </TD></TR>");
                }

// Höhe über 60 m
                if (h2 > 600) {
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Altura sobre 60 m</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " Altura = " + h2 + " </TD></TR>");
                }

// Kronenansatz über der Höhe
                if (h2 > 0 && k2 > h2) {
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Base de la copa más grande que la altura del árbol</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " h = " + h2 + ", k = " + k2 + " </TD></TR>");
                }


// BHD = 0      bei Probenahmebäume in U-Aufnahmen kann der d-Wert fehlen
                if (d2 < 1 && !(rand.equals("P") && typ2.equals("U"))) {
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Diámetro 0</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " d = " + d2 + " </TD></TR>");
                }                
                

// Fehlstamm mit h, k
                if (aus2.equals("F") && (h2!=0 || k2 != 0)) {
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Fracaso en lecturas de deformación?</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " a2 = "+aus2+" h = " + h2 + ", k = " + k2 + " </TD></TR>");
                }
                
// Verschiedene Durchmesser in d und dmess, obwohl Messhöhe 13 (und umgekehrt)
                if (mh2 == 13 && dmess2 > 0 && d2 != dmess2 && ! typ2.equals("U")) {
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Diferentes diametros a través de mh 13</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " d= " + d2 + " y diametro= " + dmess2 + " </TD></TR>");
                }
                if (mh2 != 13 && dmess2 > 0 && d2 == dmess2 && ! typ2.equals("U") ) {
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Diametro mal calculado?(mh fuera de 1,3m!)</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " mh="+mh2+" d= " + d2 + " y diametro= " + dmess2 + " </TD></TR>");
                }

// Mittlerer jährlicher Durchmesserzuwachs größer 2 cm
                if(alt2 > 0 && d2 > 0 && d2/10/alt2 > 2){
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Media de diametro anual mayor a 2 cm</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " d= " + d2 + ", Edad= " + alt2 + " </TD></TR>");
                }

// Mittlerer jährlicher Höhenzuwachs größer 2 m
                if(alt2 > 0 && h2 > 0 && h2/10/alt2 > 2){
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Incremento de altura media anual mayor a 2 m</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " h= " + h2 + ", Edad= " + alt2 + " </TD></TR>");
                }

// hd-Wert
                if(h2 > 0 && d2 > 0){
                    System.out.println("h2: "+h2 + " d2:"+d2);
                    double hd = (h2/10)/((double)d2/1000);
                    if(hd < 20 || hd > 300){
                        nFehler = nFehler + 1;
                        out.println("<TR><TD>Altura / ratio de diámetro poco probable</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " h= " + h2 + ", d= " + d2 + " </TD></TR>");
                    }
                }

// Bei U-Aufnahme nur Eintrag in ddauer (nicht dmess)
                if(typ2.equals("U") && dmess2>0){
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Entrada de diámetro en grabación U</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " diámetro="+dmess2+" </TD></TR>");
                }
// Bei H/N-Aufnahme nur Eintrag in dmess (nicht ddauer)
                if((typ2.equals("N")||typ2.equals("H")) && ddauer2>0){
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Entrada en ddauer para no realizar Grabación U</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " ddauer="+ddauer2+" </TD></TR>");
                }
                
// Art 0                     
                if (art2 == 0) {
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Indicación del tipo que falta</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " y tipo= " + art2 + " </TD></TR>");
                }
// Alter 0
                if (nr2.matches("\\d+\\w*") && alt2 < 1.0) {
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Edad 0</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " y edad= " + alt2 + " </TD></TR>");
                }
// Alter 0 für HG, nurH, HB
                if((hinweisNurHAlt0 && nr2.equals("nurH") || nr2.equals("HG") || nr2.equals("HB")) && alt2 < 1.0){
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Indicación de edad para nurH, HG, HB que falta</TD><TD>z.B.: " + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " </TD></TR>");
                    hinweisNurHAlt0 = false;
                }
                
// Anzahl = 0 für nummerierte Nicht-Rand-Bäume
                if (nr2.matches("\\d+\\w*") && rand.equals("") && anz2 == 0) {
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Arbol sin borde numerado = 0</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " </TD></TR>");
                }
                
// Anzahl = 1 für HG, nurH, HB
                if((nr2.equals("nurH") || nr2.equals("HG") || nr2.equals("HB")) && anz2 == 1){
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>nurH o HG numerado = 1</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " </TD></TR>");
                }
// Faktor 0
//                if(anz2 > 0 && fac < 0.0){
//                    nFehler = nFehler + 1;
//                    out.println("<TR><TD>repfl kleiner als 1,0</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " </TD></TR>");
//                }

// Faktor > 1 für ausscheidenden Bestand
                if(hinweisRepfacAussch && fac2>1 && aus2.length() > 0){
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Factor de representación para inventario de corte correcto?</TD>"
                            + "<TD>z.B." + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " repfl="+ fac2 +" </TD></TR>");
                    hinweisRepfacAussch = false;
                }
           
// Messhöhe                    
                if (mh2 < 10 || mh2 > 30) {
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Altura de medición correcta?</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " </TD></TR>");
                }
                                
// Ausscheidender Bestand
                if (aus2.length() > 0 && !aInhalt.contains(aus2)) {
                    nFehler = nFehler + 1;
                    //Hier gehts nicht, test mit 89721001
                    if(hinweisAus1 && aus2.equals("1")){
                        out.println("<TR><TD>Razón de rechazo 1</TD><TD>z.B.: " + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " a= "+aus2+" </TD></TR>");
                        hinweisAus1 = false;
                    }else if(!aus2.equals("1")){
                        out.println("<TR><TD>Razón de rechazo?</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " a= "+aus2+" </TD></TR>");
                    }
                }

// ZF
                if (zf2.length() > 0 && !"F".equals(zf2) && ! "Z".equals(zf2)) {
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Entrada en ZF?</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " ZF= "+zf2+" </TD></TR>");
                }

// OU
                if (ou2.length() > 0 && !"O".equals(ou2) && ! "U".equals(ou2)) {
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Entrada en OU?</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " OU= "+ou2+" </TD></TR>");
                }

// R            erlaubt: R und für U-Aufnahmen auch P
                if (rand.length() > 0 && !("R".equals(rand) || "P".equals(rand))) {
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Entrada en r?</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " r= "+rand+" </TD></TR>");
                }
// e
                if (ein2.length() > 0 && !"e".equals(ein2)) {
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Entrada en e?</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " e= "+ein2+" </TD></TR>");
                }
// Einwuchs im Oberstand
                if (ein2.length() > 0 && "O".equals(ou2)) {
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Crecimiento interno en estado superior?</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " </TD></TR>");
                }

                
// Folgemesswert, der prüfbar ist
// für 'reguläre' Bäume, die zum mindestens zweiten Mal aufgenommen sind:
                if (art1 == art2 && nr1.equals(nr2) && nr2.length() > 0 &&
                        nr2.matches("\\d+\\w*") && rand.equals("") && auf1!=auf2) {
                        // Ziffer (mindestens einmal) + Buchstabe, Ziffer oder Unterstrich (null,ein-oder mehrmals)
                    AltersDezimale aDez = new AltersDezimale();
                    double zeit= jahr2+aDez.getAltersdezimale(monat2)-(jahr1+aDez.getAltersdezimale(monat1));
                    if(zeit == 0.0 && monat2==monat1 && !typ2.equals("U")){
                        nFehler = nFehler + 1;
                        out.println("<TR><TD>Dos disparos simultáneos</TD><TD>" + auf2 + "("+jahr2+")" + " " + auf1 + "("+jahr1+")</TD></TR>");
                    }

    // 1. Höhenwert, kleiner -10%, größer 30%/50% (Fünfjahreszuwachs)                  
                    if(zeit > 0.0 && h1 > 0 && h2 > 0 && !bemerk.contains("H!") && !bemerk.contains("H*")){
                        int hdiff = (int) Math.round(h1 * 0.10);
                        if (((h2 - h1)/zeit*5) < -hdiff) {
                            nFehler = nFehler + 1;
                            out.println("<TR><TD>Valor altura menor</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " h1= " + h1 + " h2= " + h2 + " </TD></TR>");
                        }
                        hdiff = (int) Math.round(h1*0.3); // für Bäume über 20 m
                        if (h1 > 200 && (((h2 - h1)/zeit*5) > hdiff)){
                            nFehler = nFehler + 1;
                            out.println("<TR><TD>Aumento de la altura sobre 30%</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " h1= " + h1 + " h2= " + h2 + " </TD></TR>");
                        }
                        hdiff = (int) Math.round(h1*0.5); // für Bäume unter 20 m
                        if (h1 <= 200 && (((h2 - h1)/zeit*5) > hdiff)){
                            nFehler = nFehler + 1;
                            out.println("<TR><TD>Aumento de la altura sobre 50%</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " h1= " + h1 + " h2= " + h2 + " </TD></TR>");
                        }
                    }
                  
    // 2. BHDwert, kleiner -5%, größer 30%/50% (Fünfjahreszuwachs)
                    if (zeit > 0.0 && d1 > 0 && d2 > 0 && !bemerk.contains("BHD!") && !bemerk.contains("BHD*")){
                        int ddiff = (int) Math.round(d1 * 0.05);
                        if (ddiff < 8) ddiff = 8;
                        if (((d2 - d1)/zeit*5) < -ddiff && aus2.equalsIgnoreCase("T") == false) {
                            nFehler = nFehler + 1;
                            out.println("<TR><TD>Valor BHD menor</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " d1= " + d1 + " d2= " + d2 + " </TD></TR>");
                        }
                        // T-Bäume dürfen höchsten 2 cm pro 5 Jahre kleiner werden (22.06.2011)
                        if(aus2.equalsIgnoreCase("T") && d2 < d1 && (d1-d2)/zeit*5 > 20) {
                            nFehler = nFehler + 1;
                            out.println("<TR><TD>Valor BHD para árbol(T) retirado más de 2 cm más pequeño</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " d1= " + d1 + " d2= " + d2 + " </TD></TR>");
                        }                        
                        ddiff = (int) Math.round(d1 * 0.3); // für Bäume über 30 cm
                        if (d1 > 300 && (((d2 - d1)/zeit*5) > ddiff)) {
                            nFehler = nFehler + 1;
                            out.println("<TR><TD>Crecimiento-BHD sobre 30%</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " d1= " + d1 + " d2= " + d2 + " </TD></TR>");
                        }
                        ddiff = (int) Math.round(d1 * 0.5); // für Bäume unter 30 cm
                        if (d1 <= 300 && (((d2 - d1)/zeit*5) > ddiff)) {
                            nFehler = nFehler + 1;
                            out.println("<TR><TD>Crecimiento-BHD sobre 50%</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " d1= " + d1 + " d2= " + d2 + " </TD></TR>");
                        }
                    }
                    
    // 3. Baum bereits ausgeschieden
                    if (aus1.equals("") == false && d2 > 0) {
                        nFehler = nFehler + 1;
                        out.println("<TR><TD>Árbol ya eliminado</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " aus1= " + aus1 + " aus2= " + aus2 + " </TD></TR>");
                    }
   // 4. Alter darf nicht kleiner werden
                    if(alt2 < alt1){
                        nFehler = nFehler + 1;
                        out.println("<TR><TD>Edad menor</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " alt1= " + alt1 + " alt2= " + alt2 + " </TD></TR>");
                    }
   // 5. Übereinstimmung Altersdifferenz und Abstand zwischen den Aufnahmen                                       
                    if(hinweisAlterdiff && (Math.abs((Math.round((alt2-alt1)*10)/10.0) - zeit) > 0.1)){ //kein exakter Vergleich möglich <- Rundungsfehler
                        nFehler = nFehler + 1;                                    // zeit nicht runden! sonst Probleme zB mit 56151100
                        hinweisAlterdiff = false;
                        out.println("<TR><TD>Edad de falla?</TD><TD>z.B. " + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " alt1= " + alt1 + " alt2= " + alt2 + " (Tiempo entre muestras: "+ zeit + " Años) </TD></TR>");
                    }
   // 6. Einwachser schon vorhanden (funktioniert nicht, wenn eine Nebenaufnahme dazwischen ist)
                    if(auf1 > 0 && ein2.equals("e")){
                        nFehler = nFehler + 1;
                        out.println("<TR><TD>Einwachser ya disponible en pre-grabacion</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 +"</TD></TR>");

                    }
                    
                } /* else {
// nummerierter Einwuchs ohne Eintrag in Spalte e
                    // raus, zu viele Fehlmeldungen, wenn z.B. cm-Daten in der nächsten Aufnahme nummeriert sind
                    // Fehler sollten bei der Zusammenstellung auffallen

                    if (auf2 > aufnahmen[0] && nr2.matches("\\d+\\w*") && rand.equals("")) {
                        int k = 0;
                        while (aufnahmen[k] < auf1) k = k + 1;
                        String typ1 = auftyp[k];
                        if ("H".equals(typ1) && ein2.equals("") && d2 > 0) {
                            nFehler = nFehler + 1;
                            out.println("<TR><TD>Einwuchsmarkierung fehlt</TD><TD>" + auf2 + "("+jahr2+")"  + " " + art2 + " " + nr2 + " ein2= " + ein2 + " </TD></TR>");
                        }
                    }
                } */
// Verbleibender Baum ohne Eintrag in der Folgeaufnahme (nur nicht-repräsentative Bäume)
                // wenn auf1 nicht die letzte Aufnahme und > 0 ist und...
                if (auf1 != aufnahmen[aufindex] && auf1 > 0 && nr1.matches("\\d+\\w*") && aus1.equals("")
                        && fac1 <= 1) {
                    int k = 0;
                    while (aufnahmen[k] < auf1) k = k + 1;
                    int folgeauf = aufnahmen[k + 1];
                    // Wenn die folgende Aufnahme Nebenaufnahme ist und der Bezugsbaum dort nicht auftaucht -> nächste Aufnahme zum Vergleich
                    while(auftyp[k+1].equals("N") && folgeauf != auf2){
                        k = k + 1;
                        folgeauf = aufnahmen[k + 1];
                    }
                    //wenn auf2 Hauptaufnahme und entweder auf2 nicht die folgende Aufnahme oder Bäume nicht identisch
                    // 2 mögliche Fälle: Ausscheidegrund fehlt (-> Baum taucht nicht wieder auf)
                    //   oder Baum wurde in einer Aufnahme vergessen (-> Baum taucht wieder auf, nr und art identisch, aber nicht in der richtigen Aufnahme)
                    if ("H".equals(auftyp[k+1]) && (folgeauf != auf2 || !(nr1.equals(nr2) && art1==art2))) {
                        nFehler = nFehler + 1;
                        out.println("<TR><TD>Árbol restante</TD><TD>" + auf1 + "("+jahr1+")"  + " " + art1 + " " + nr1 + " falta en grabación " + folgeauf + " </TD></TR>");
                    }
                }

                auf1 = auf2;
                jahr1 = jahr2;
                monat1 = monat2;
                alt1 = alt2;
                nr1 = nr2;
                art1 = art2;
                d1 = d2;
                h1 = h2;
                aus1 = aus2;
                fac1 = fac2;
            }
            // Letzter Baum fehlt in Folgeaufnahme? 26.10.2010
            if (auf1 != aufnahmen[aufindex] && auf1 > 0 && nr1.matches("\\d+\\w*") && aus1.equals("")
                    && fac1 <= 1) {
                int k = 0;
                while (aufnahmen[k] < auf1) k = k + 1;
                int folgeauf = aufnahmen[k + 1];
                // Wenn die folgende Aufnahme Nebenaufnahme ist und der Bezugsbaum dort nicht auftaucht -> nächste Aufnahme zum Vergleich
                while(auftyp[k+1].equals("N")){
                    k = k + 1;
                    folgeauf = aufnahmen[k + 1];
                }
                // Wenn keine Nebenaufnahme
                if (!auftyp[k].equals("N")) {
                    nFehler = nFehler + 1;
                    out.println("<TR><TD>Árbol restante</TD><TD>" + auf1 + " " + art1 + " " + nr1 + " falta en grabación " + folgeauf + " </TD></TR>");
                }
            }

            nFehlerRueck = nFehler;
        } catch (Exception e) {
            System.out.println("Vis Plausichek:" + e);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException ex) {ex.printStackTrace();}
        }

// Plausibilität aufnahmenweise prüfen

// Kronenablotungen prüfen        
        int kronenFehler = checkKronenabl(ids, dbconn);
        if (kronenFehler < 0 || nFehlerRueck < 0) nFehlerRueck = -1;
        else nFehlerRueck = nFehlerRueck + kronenFehler; 
        System.out.println("PlausiCheck: "+nFehlerRueck+" Información para tabla Baum");
        
// Stammverteilungstabelle prüfen        
        int stammFehler = checkStammv(ids, dbconn);
        if (stammFehler < 0 || nFehlerRueck < 0) nFehlerRueck = -1;
        else nFehlerRueck = nFehlerRueck + stammFehler; 
        System.out.println("             "+stammFehler+" Información para tabla Stammv");
        
// Qualitätstabelle prüfen        
        int qualitFehler = checkQualit(ids, dbconn, qualAuf);
        if (qualitFehler < 0 || nFehlerRueck < 0) nFehlerRueck = -1;
        else nFehlerRueck = nFehlerRueck + qualitFehler; 
        System.out.println("             "+qualitFehler+" Información para tabla Qualit");        
        return nFehlerRueck;
        
    }
    
    private int checkStammv(String edvid, DBConnection dbconn){
        int stammFehlerRueck = -1;
        int stammFehler = 0;
        Statement stmt = null;
        ResultSet rs = null;
        int nStamm = 0;
        int aufStamm = 100;   // Aufnahme der Erstellung des Stammverteilungsplans

        try{
            stmt = dbconn.Connections[0].createStatement();
            rs = stmt.executeQuery("SELECT auf, art, nr FROM Stammv WHERE edvid = '"+edvid+"';");
            int artx = 0;
            String nrx = "";
            while (rs.next()){
                nStamm++;
                int aufStammtest = rs.getInt("auf");   
                if(aufStammtest < aufStamm) aufStamm = aufStammtest;
                artx = rs.getInt("art");
// Art = 0
                if (artx == 0){
                    nrx = "";
                    Object nrtest = rs.getObject("nr");
                    if(nrtest != null) nrx = nrtest.toString().trim();
                    if(nrx.matches("\\d+\\w+")){
                        stammFehler = stammFehler + 1;
                        out.println("<TR><TD>Falta Artangabe en tabla Stammv</TD><TD>" +artx + " " + nrx +"</TD></TR>");
                    } 
                }
            }
            if(nStamm<5) stammFehlerRueck = 0;
        } catch(Exception e){
            System.out.println("Revisar tabla Stammv: "+e);
        }
        finally{
            try{
                if(rs != null) rs.close();
                if(stmt != null) stmt.close();    
            } catch(Exception e) {
                e.printStackTrace();
            }
        }        
// Übereinstimmung mit Baumtabelle
        if(nStamm>4){   // nur bei mehr als 4 Einträgen in Stammv = Stammverteilungsplan vorhanden
            
            Statement stmt2 = null;
            ResultSet rs2 = null;
            try{
                stmt2 = dbconn.Connections[0].createStatement();
                rs2 = stmt2.executeQuery("SELECT Baum.nr AS bnr, Baum.art AS bart, Baum.auf AS bauf, " +
                        " Stammv.nr AS snr, Stammv.art AS sart, Auf.jahr AS jahr" +
                        " FROM Auf RIGHT JOIN (Baum LEFT JOIN Stammv ON(trim(Baum.nr) = trim(Stammv.nr))" +
                        " AND (Baum.edvid = Stammv.edvid)) " +
                        " ON Baum.edvid = Auf.edvid AND Baum.auf = Auf.auf " +
                        " WHERE trim(Baum.nr) NOT IN ('HG', 'nurH','HB','onum','cm')  AND " +
                        " (Baum.art <> Stammv.art OR Stammv.art IS NULL) AND Baum.edvid = '"+edvid+"'" +
                        " AND Baum.anzahl > 0 " +
                        " ORDER BY trim(Baum.nr), Baum.auf;");  
                String fehlnr = "";
                Map<String,String> falschArt = new HashMap();   // enthält Nr und Art als String zusammen! und die evtl. auszugebende Meldung
                Set<String> vorhandenInStammv = new HashSet();
                while (rs2.next()){
                    String nrBaum = "";
                    Object nrBobj = rs2.getObject("bnr");
                    if(nrBobj != null) nrBaum = nrBobj.toString().trim();
                    Object nrSobj = rs2.getObject("snr");
                    String nrStammv = "";
                    if(nrSobj != null) nrStammv = nrSobj.toString().trim();
                    int artBaum = rs2.getInt("bart");
                    int artStammv = rs2.getInt("sart");
                    int aufBaum = rs2.getInt("bauf");
                    int jahr = rs2.getInt("jahr");

                    // Fehlender Eintrag in Stammv
                    if(nrStammv.equals("") && aufBaum >= aufStamm && !fehlnr.equals(nrBaum)){
                        stammFehler = stammFehler + 1;
                        out.println("<TR><TD>Entrada no encontrada en tabla Stammv</TD><TD>"+ aufBaum + "("+jahr+") " + artBaum + " " + nrBaum +"</TD></TR>");
                        fehlnr = nrBaum;
                    }
                    // Verschiedene Arten Baum - Stammv (zwischenspeichern)
                    if(nrBaum.equals(nrStammv) && aufBaum >= aufStamm && artBaum != artStammv){
                        falschArt.put(nrBaum + "-" + artBaum, "<TR><TD>Tipo no coincide</TD><TD>"+ aufBaum + "("+jahr+") " + " Baum: " +artBaum+ " " + nrBaum +","
                                    + " Stammv: " + artStammv + " " + nrStammv +"</TD></TR>");
                        vorhandenInStammv.add(nrStammv + "-" + artStammv);                       
                    }
                }
                
                // Verschieden Arten Baum - Stammv (Fehlerausgabe)
                Iterator iterator = falschArt.keySet().iterator();
                while(iterator.hasNext()){
                    String fehl = iterator.next().toString();
                    if(!vorhandenInStammv.contains(fehl)){
                        out.println(falschArt.get(fehl));
                        stammFehler = stammFehler + 1;
                    }
                    
                }

                
                stammFehlerRueck = stammFehler;
            }catch(Exception e){
                System.out.println("Revisar tabla Stammv: "+e);
            }
            finally{
                try{
                    if(rs2 != null) rs2.close();
                    if(stmt2 != null) stmt2.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }

        
  
        return stammFehlerRueck;
    }
    
    private int checkQualit(String edvid, DBConnection dbconn, Set<Integer> qualAuf){
        int qualitFehler = 0;
        int qualitFehlerRueck = 0;
        
        Set<Integer> zusAufsinQual = new HashSet<Integer>();  // Aufnahmen die in Qualit aber nicht in qualitat von Auf

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = dbconn.Connections[0].createStatement();
            String sql = "SELECT Baum.nr AS bnr, Baum.art AS bart, Baum.auf AS bauf, "
                    + " Qualit.nr AS qnr, Qualit.art AS qart, Auf.jahr AS jahr"
                    + " FROM Auf RIGHT JOIN (Baum LEFT JOIN Qualit ON(trim(Baum.nr) = trim(Qualit.nr))"
                    + " AND (Baum.edvid = Qualit.edvid) AND (Baum.auf = Qualit.auf)) "
                    + " ON Baum.edvid = Auf.edvid AND Baum.auf = Auf.auf "
                    + " WHERE trim(Baum.nr) NOT IN ('HG', 'nurH','HB','onum','cm')  AND "
                    + " (Baum.art <> Qualit.art OR Qualit.art IS NULL) AND Baum.edvid = '" + edvid + "'"
                    + " AND Baum.Auf IN (SELECT DISTINCT Auf FROM Qualit WHERE edvid = '" + edvid + "') AND Baum.anzahl > 0 "
                    + " ORDER BY Baum.auf, trim(Baum.nr);";
            //System.out.println(sql);
            rs = stmt.executeQuery(sql);
            String fehlnr = "";
            Map<String, String> falschArt = new HashMap();   // enthält Nr und Art als String zusammen! und die evtl. auszugebende Meldung
            Set<String> vorhandenInQualit = new HashSet();
            int vorauf = -1; 
            while (rs.next()) {
                String nrBaum = "";
                Object nrBobj = rs.getObject("bnr");
                if (nrBobj != null) {
                    nrBaum = nrBobj.toString().trim();
                }
                Object nrQobj = rs.getObject("qnr");
                String nrQual = "";
                if (nrQobj != null) {
                    nrQual = nrQobj.toString().trim();
                }
                int artBaum = rs.getInt("bart");
                int artQual = rs.getInt("qart");
                int aufBaum = rs.getInt("bauf");
                int jahr = rs.getInt("jahr");

                // Fehlender Eintrag in Qualit
                if (nrQual.equals("") && !fehlnr.equals(nrBaum)) {   ///aufBaum >= aufQual && 
                    qualitFehler = qualitFehler + 1;
                    out.println("<TR><TD>Entrada no encontrada en tabla Qualit</TD><TD>" + aufBaum + "(" + jahr + ") " + artBaum + " " + nrBaum + "</TD></TR>");
                    fehlnr = nrBaum;
                }
                
                // Nach jeder Aufnahme: Verschieden Arten Baum - Qualit (Fehlerausgabe)
                if (vorauf > 0 && vorauf != aufBaum) {
                    Iterator iterator = falschArt.keySet().iterator();
                    while (iterator.hasNext()) {
                        String fehl = iterator.next().toString();
                        if (!vorhandenInQualit.contains(fehl)) {
                            out.println(falschArt.get(fehl));
                            qualitFehler = qualitFehler + 1;
                        }
                    }
                    vorhandenInQualit.clear();
                    falschArt.clear();
                    
                    if(qualAuf.contains(vorauf)){      // Übereinstimmung zw. Auf und Qualit
                        qualAuf.remove(vorauf);
                    } else zusAufsinQual.add(vorauf);
                }
                
                // Verschiedene Arten Baum - Stammv (zwischenspeichern)
                if (nrBaum.equals(nrQual) &&  artBaum != artQual) { // aufBaum >= aufQual &&
                    falschArt.put(nrBaum + "-" + artBaum, "<TR><TD>Tipo no coincide</TD><TD>" + aufBaum + "(" + jahr + ") " + " Baum: " + artBaum + " " + nrBaum + ","
                            + " Qualit: " + artQual + " " + nrQual + "</TD></TR>");
                    vorhandenInQualit.add(nrQual + "-" + artQual);
                }
                
                

                            
                vorauf = aufBaum;
            }
            
            if(qualAuf.contains(vorauf)){      // Übereinstimmung zw. Auf und Qualit letzte Auf + Fehler
                  qualAuf.remove(vorauf);
            } else if(vorauf > 0) zusAufsinQual.add(vorauf);
            Iterator ite = qualAuf.iterator();
            while(ite.hasNext()){
                out.println("<TR><TD>Grabación (Gráfico en columna qualitat) <br> sin entrada en tabla Qualit</TD><TD>Grabación " + ite.next() + "</TD></TR>");
            }
            Iterator iter = zusAufsinQual.iterator();
            while(iter.hasNext()){
                out.println("<TR><TD>Grabación (Tabla Qualit) <br> sin utilizar en la tabla Auf</TD><TD>Grabación " + iter.next() + "</TD></TR>");
                
            }

            // Fehler verschiedene Arten für letzte Aufnahme
            Iterator iterator = falschArt.keySet().iterator();
                    while (iterator.hasNext()) {
                        String fehl = iterator.next().toString();
                        if (!vorhandenInQualit.contains(fehl)) {
                            out.println(falschArt.get(fehl));
                            qualitFehler = qualitFehler + 1;
                        }
                    }
            


            qualitFehlerRueck = qualitFehler;
        } catch (Exception e) {
            System.out.println("checkQualit: " + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        return qualitFehlerRueck;
    }
    
     private int checkKronenabl(String edvid, DBConnection dbconn) {
        int kronenFehlerRueck = -1;
        int kronenFehler = 0;

        int ablot[] = new int[8];
        int sfe = 0;
        int sfg = 0;
        int aufx = 0;
        int artx = 0;
        String nrx = "";

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = dbconn.Connections[0].createStatement();
            rs = stmt.executeQuery("SELECT auf, art, nr, g0, g5, g10, g15, g20, g25, g30, g35, sfe, sfg " +
                    "FROM Baum WHERE edvid = '" + edvid + "' AND g0+g5+g10+g15 > 0 ORDER BY auf, art, nr;");
            int ablfehler = 0;
            while (rs.next()) {
                ablot[0] = rs.getInt("g0");
                ablot[1] = rs.getInt("g5");
                ablot[2] = rs.getInt("g10");
                ablot[3] = rs.getInt("g15");
                ablot[4] = rs.getInt("g20");
                ablot[5] = rs.getInt("g25");
                ablot[6] = rs.getInt("g30");
                ablot[7] = rs.getInt("g35");
// Kronenradius negativ oder größer 7 m
                for (int i = 0; i < 8; i++) {
                    if (ablot[i] < 0 || ablot[i] > 700) {
                        ablfehler = ablfehler + 1;
                    }
                }
                if (ablfehler > 0) {
                    aufx = rs.getInt("auf");
                    artx = rs.getInt("art");
                    Object nrtest = rs.getObject("nr");
                    if (nrtest != null) {
                        nrx = nrtest.toString().trim();
                    }
                    kronenFehler = kronenFehler + 1;
                    String ablotStr = ablot[0] + "," + ablot[1] + "," + ablot[2] + "," + ablot[3] + "," + ablot[4] + "," + ablot[5] + "," + ablot[6] + "," + ablot[7];
                    out.println("<TR><TD>g0-g35. Fallas?</TD><TD>" + aufx + " " + artx + " " + nrx + " g0-g35=" + ablotStr + "</TD></TR>");
                    ablfehler = 0;
                }
// sfe = sfg
                sfe = rs.getInt("sfe");
                sfg = rs.getInt("sfg");
                if (sfe > 0 && sfe == sfg) {
                    if(aufx == 0) aufx = rs.getInt("auf");
                    if(artx == 0) artx = rs.getInt("art");
                    if(nrx.equals("")){
                        Object nrtest = rs.getObject("nr");
                        if (nrtest != null) {                            
                            nrx = nrtest.toString().trim();
                        }
                    }
                    
                    kronenFehler = kronenFehler + 1;
                    out.println("<TR><TD>sfg = sfe. Fehler?</TD><TD>" + aufx + " " + artx + " " + nrx + " sfe=sfg=" + sfe + "</TD></TR>");
                }
// sfg zwischen 0 und 400
                if(sfg > 400 || sfg < 0){
                    if(aufx == 0) aufx = rs.getInt("auf");
                    if(artx == 0) artx = rs.getInt("art");
                    if(nrx.equals("")) {
                        Object nrtest = rs.getObject("nr");
                        if (nrtest != null) {
                            nrx = nrtest.toString().trim();
                        }
                    }
                    
                    kronenFehler = kronenFehler + 1;
                    out.println("<TR><TD>Entrada en sfg correcta?</TD><TD>" + aufx + " " + artx + " " + nrx + " sfg=" + sfg + "</TD></TR>");
                }
// sfe
                if(sfe < 0 || sfe > 120){
                    if(aufx == 0) aufx = rs.getInt("auf");
                    if(artx == 0) artx = rs.getInt("art");
                    if(nrx.equals("")) {
                        Object nrtest = rs.getObject("nr");
                        if (nrtest != null) {
                            nrx = nrtest.toString().trim();
                        }
                    }
                    
                    kronenFehler = kronenFehler + 1;
                    out.println("<TR><TD>Entrada en sfe correcta?</TD><TD>" + aufx + " " + artx + " " + nrx + " sfe=" + sfe + "</TD></TR>");

                }

            }
            kronenFehlerRueck = kronenFehler;
        } catch (Exception e) {
            System.out.println("checkKronenablot: " + e);
            //e.printStackTrace();
        } finally{
            try{
                if(rs != null) rs.close();
                if(stmt != null) stmt.close();
            } catch(Exception e) {
            }
        }
        return kronenFehlerRueck;
    }
    
  
}
