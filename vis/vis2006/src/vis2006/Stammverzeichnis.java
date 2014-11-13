/*
 * Stammverzeichnis.java
 *
 * Created on 12. September 2005, 12:48
 */

package vis2006;
import Hilfsklassen.HeuteString;
import java.text.*;
import java.util.*;
import java.io.*; 
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.html.HtmlWriter;
import java.awt.Color;
import java.sql.*;
import DatabaseManagement.*;
import javax.swing.JOptionPane;

/**
 *
 * @author  admin
 */
public class Stammverzeichnis {
    DataExchange ex =new DataExchange();
    Cell cell= new Cell();
    /** Creates a new instance of Stammverzeichnis */
    public Stammverzeichnis() {
    }

    public boolean createStammverzeichnis(DBConnection dbconn,String id, int maxauf, String filename, 
            boolean inklNralt,
            boolean nachNrSortieren){
        boolean ok = false;
        NumberFormat df=NumberFormat.getInstance();
        df=NumberFormat.getInstance(new Locale("en","US"));
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        df.setGroupingUsed(false);
        NumberFormat df0=NumberFormat.getInstance();
        df0=NumberFormat.getInstance(new Locale("en","US"));
        df0.setMaximumFractionDigits(0);
        df0.setMinimumFractionDigits(0);
        df0.setGroupingUsed(false);
        HeuteString datum = new HeuteString();
        String datumStr = datum.get();

        Integer nPage=1;
        try { 
              Document document = new Document(PageSize.A4.rotate());
              PdfWriter writerPDF = null;
              HtmlWriter writerHTML =null;
        
              if (filename.indexOf(".pdf") >-1) writerPDF  = PdfWriter.getInstance(document, new FileOutputStream(filename));
              if (filename.indexOf(".htm") >-1) writerHTML = HtmlWriter.getInstance(document, new FileOutputStream(filename));
//              PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
//
              document.addTitle("NW-FVA Ergebnisbogen für Versuchsparzelle");
              document.addAuthor("NW-FVA Abt. Waldwachstum, Grätzelstr.2, 37079 Göttingen");
              document.addSubject("Versuchsflächenauswertung");
              document.addKeywords("Forst, Versuchswesen");
              document.addCreator("Vis2006 Version 2/2007");
//
              document.open();
//  Überschrift des Ausdrucks als Tabelle mit 2 Spalten
              Image jpeg = Image.getInstance("ns-ross.jpg");
//        document.add(jpeg);
              Table datatable2 = new Table(2);
              datatable2.setPadding(1);
              datatable2.setSpacing(0);
              datatable2.setBorder(0);
              int headerwidths2[] = {15,85};
              datatable2.setWidths(headerwidths2);
              datatable2.setWidth(100);
              Cell cellBild = new Cell(jpeg);
              cellBild.setBorder(Rectangle.NO_BORDER);
              cellBild.setHorizontalAlignment(Element.ALIGN_CENTER);
              cellBild.setRowspan(2);
              datatable2.addCell(cellBild);
//datatable2.setDefaultRowspan(1);
              Cell cell1 = new Cell(new Phrase("Nordwestdeutsche Forstliche Versuchsanstalt - Abt. Waldwachstum ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
              cell1.setBorder(Rectangle.NO_BORDER);
              cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
              datatable2.addCell(cell1);
              Cell cell32 = new Cell(new Phrase("Grätzelstr.2,  37079 Göttingen; Tel. 0551-69401121, http://www.nw-fva.de ", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
              cell32.setBorder(Rectangle.NO_BORDER);
              cell32.setHorizontalAlignment(Element.ALIGN_CENTER);
              datatable2.addCell(cell32);
              document.add(datatable2);
              String flaechenName = "";
              String abtName = "";
              String artUndVerszweck="";
              try{          
                  Statement stmt = dbconn.Connections[0].createStatement(); 
                  ResultSet rs = stmt.executeQuery("SELECT * FROM Versfl WHERE edv_id = \'"+id.substring(0,6)+"\'  "  );
                  if (rs.next()){
                     flaechenName = rs.getObject("forstamt").toString();
                     abtName = rs.getObject("abt").toString();
                     artUndVerszweck = rs.getObject("baumart").toString()+"  "+rs.getObject("vers_zweck").toString();
                 }
              }	catch (Exception e)  {System.out.println(e); }	
              
              document.add(new Paragraph(""));
              document.add(new Paragraph("Versuchsfläche: "+flaechenName+"   "+abtName+"      ID:"+id)); 
              
// BHD-Messhöhe und Aufnahmetyp
              Integer[] messH = new Integer[maxauf+1];
              String[] aufTyp = new String[maxauf+1];

              Statement stmtMessh = dbconn.Connections[0].createStatement();
              ResultSet rsMessh = stmtMessh.executeQuery("SELECT Baum.auf, Baum.mh, Auf.typ FROM Baum "
                      + "LEFT JOIN Auf ON Auf.auf = Baum.auf AND Auf.edvid = Baum.edvid "
                      + "WHERE Baum.edvid = \'" +id+ "\' AND TRIM(Baum.nr) <> 'nurH' "
                      + "GROUP BY Baum.auf, Baum.mh, Auf.typ "
                      + "ORDER BY Baum.auf");
              
              int aufalt = 0;
              int nmessh = 0;
              int auf = 0;
              while (rsMessh.next()) {
                  auf = rsMessh.getInt("auf");
                  if(auf==0){     // 2.2.2011
                      System.out.println("Fehlende Aufnahmenummer oder Aufnahmenummer ist 0 in Auf oder Baum");
                  }
                  if (auf == aufalt || aufalt == 0){
                      if (rsMessh.getInt("mh") == 13) nmessh = nmessh + 1;
                      else nmessh = nmessh + 2;
                      if(aufalt == 0) aufTyp[auf] = rsMessh.getString("typ").trim();
                  } 
                  else {
                      messH[aufalt] = nmessh;
                      aufTyp[auf] = rsMessh.getString("typ").trim();
                      if (rsMessh.getInt("mh") == 13) nmessh = 1;
                      else nmessh = 2; 
                  }
                  aufalt = auf;
              }
              if (aufalt > messH.length-1) 
                  System.out.println("Fehler: Ungleiche Anzahl Aufnahmen in Tabellen Baum und auf!");
              messH[aufalt] = nmessh; //letzten Wert eintragen
              rsMessh.close();
          
// Vorbereiten der Tabelle
              int dhSpalten=15; 
              int nZeilen=1;
              Table datatable = new Table(dhSpalten+2);
              
              for (int k=0;k<3; k++ ){ //  15 * 3 = 45 Aufnahme sind möglich
              datatable.setBorderWidth(0);
              datatable.setPadding(0);
              datatable.setSpacing(0);
              datatable.setAlignment("ALIGN_MIDDLE");
              int headerwidths[] = {4, 3, 3, 3, 3, 3 , 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,};
//               3, 3, 3,3, 3, 3,3, 3, 3,3        };
              datatable.setWidths(headerwidths);
              datatable.setWidth(100);
              if(inklNralt) datatable.addCell(getCell("Nr (Nralt)"));
              else datatable.addCell(getCell("Nr"));
              datatable.addCell(getCell("Art"));
              boolean legende = false;
              boolean legendeErweitern = false;
              for (int i=0;i<dhSpalten;i++){
                 Integer ix=(k*dhSpalten)+i+1;
                 String spaltit = "D"+ix.toString();
                 /*if (ix > messH.length-1 || messH[ix]==null || messH[ix] == 1) datatable.addCell(getCell("D"+ix.toString()));
                 else {
                     legende = true;
                     if (messH[ix] == 2) datatable.addCell(getCell("D"+ix.toString()+"*"));
                     if (messH[ix] > 2) datatable.addCell(getCell("D"+ix.toString()+"**"));
                 } */
                 if( ix < maxauf+1 && messH[ix] != null && (aufTyp[ix].equals("U") || messH[ix] > 1)){
                     legende = true;
                     if (messH[ix] == 2) spaltit = spaltit+"*";
                     if (messH[ix] > 2) spaltit = spaltit+"**";
                     if(aufTyp[ix].equals("U")) spaltit = spaltit+" U";{
                         legendeErweitern = true;
                     }
                 }
                 datatable.addCell(getCell(spaltit));

              }
              nZeilen=1;
              Cell c = new Cell();          
                    
            
// Abfrage Aufnahme 1 - 12
              String nrxalt =null;
              int artxalt = 0;
              int nauf=k*dhSpalten; 
              Integer kleiner = (k+1)*dhSpalten+1;
              Integer groesser = k*dhSpalten;
              try{
                  // Sortierung
                  String order = "ORDER BY Baum.art, Baum.nr, Baum.auf";
                  if(nachNrSortieren) order = "ORDER BY Baum.nr, Baum.art, Baum.auf";

                  // Datenbankabfrage
                  Statement stmt = dbconn.Connections[0].createStatement(); 
                  ResultSet rs = stmt.executeQuery("SELECT Baum.edvid, Baum.nr, Baum.nralt, Baum.auf, Baum.art, Baum.anzahl, Baum.dmess, Baum.d, Baum.ddauer, Baum.ou, Baum.a, Baum.zf, Baum.e, Baum.r " +
                          "FROM Baum WHERE (edvid = \'"+id+"\' AND (Baum.anzahl > 0) " +    //vorher: dmess>0
                          "AND (Baum.auf < "+kleiner.toString()+" ) AND (Baum.anzahl > 0) " +
                          "AND (Baum.auf > "+groesser.toString()+" )) "
                          + order );
                  while (rs.next()) {
                  // Werte aus der Abfrage holen und in dStr zusammenfassen
                     String edv = rs.getObject("edvid").toString();
                     if (edv==null || edv.length()<1) break;
                     String nrx = rs.getObject("nr").toString();
                     String nrxAusgabe = nrx;
                     if(inklNralt){
                         Object nralto = rs.getObject("nralt");
                         String nralt = "";
                         if(nralto != null) nralt = nralto.toString().trim();
                         nrxAusgabe = nrxAusgabe+" ("+nralt+")";
                     }
                     int aufx = rs.getInt("auf");
                     Integer artx = rs.getInt("art");
                     double dx = rs.getDouble("dmess");
                     double ddauer = rs.getDouble("ddauer");
                     double dwert = rs.getDouble("d");
                     String dStr1= df0.format(dx);
                     if(dx < 1){
                         if(ddauer > 0) dStr1 = df0.format(ddauer/10.0);             // für U-Aufnahmen
                         else if (dx < -998) dStr1 = "~"+df0.format(dwert);          // für Fehlstämme 
                     }
                     String rx = "";
                     Object txt1 = rs.getObject("r");
                     if(txt1 != null) rx = txt1.toString().trim();
                     if(rx.length()>0) dStr1 = "("+dStr1+")";
                     String txt=" ";
                     txt1=rs.getObject("ou");
                     if (txt1 != null) txt=txt1.toString().trim();
                     if (txt.length()==0) txt=" ";
                     String dStr = txt;
                     txt=" ";
                     txt1=rs.getObject("zf");
                     if (txt1 != null) txt=txt1.toString().trim(); 
                     if (txt.length()==0) txt=" ";
                     dStr = dStr+txt;
                     dStr = dStr+" "+dStr1;
                     txt="";
                     txt1=rs.getObject("e");
                     if (txt1 != null) txt=txt1.toString().trim();
                     if (txt.length()==0) txt=" ";
                     dStr = dStr+txt;
                     txt=" ";
                     txt1=rs.getObject("a");
                     if (txt1 != null) txt=txt1.toString().trim();
                     if (txt.length()==0) txt=" ";
                     dStr = dStr+txt;
                     
                     if (nrx.indexOf("cm")< 0 && nrx.indexOf("onum")< 0 && nrx.indexOf("nur")< 0 && nrx.indexOf("HG")< 0){// nur nummerierte
                       if (nrxalt == null || nrx.compareTo(nrxalt)!=0 || artx != artxalt){ // noch kein Baum geschrieben || nrx ungleich nrxalt
                         if (nrxalt!=null){ //nrx ungleich nrxalt
                           for (int i=nauf; i < (k+1)*dhSpalten; i++){ //Zeile anlegen
                               datatable.addCell(getCell(""));
                           }
                           if ((nZeilen > 33) || (nZeilen >28 && nPage==1)){ // Seite voll  
                             if (nPage> 1) document.add(new Paragraph("Seite:"+nPage.toString()+"    Stammverzeichnis ID:"+id+"     Die Verwendung der Daten ohne Genehmigung der NW-FVA ist untersagt!       "+datumStr ));
                             if(legende){
                                Table datatable3 = new Table(2);
                                datatable3.setPadding(1); datatable3.setSpacing(0);
                                datatable3.setBorder(0);
                                datatable3.setOffset(10);
                                int widths3[] = {80,20}; datatable3.setWidths(widths3);
                                datatable3.setWidth(100);
                                Cell cellleft = new Cell(new Phrase(10,"Durchmesser der nummerierten Bäume Aufnahmen "+(k*dhSpalten+1)+" bis "+((k+1)*dhSpalten)));
                                cellleft.setBorder(Rectangle.NO_BORDER); 
                                cellleft.setHorizontalAlignment(Element.ALIGN_LEFT);
                                datatable3.addCell(cellleft);
                                String text = " *  von 1.3 m abweichende Messhöhe \n ** verschiedene Messhöhen";
                                if(legendeErweitern) text = text + "\n U Ablesung Dauerumfangmessband";
                                Cell cellrigth = new Cell(new Phrase(7 , text, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                                cellrigth.setBorder(Rectangle.NO_BORDER); 
                                cellrigth.setHorizontalAlignment(Element.ALIGN_LEFT);
                                datatable3.addCell(cellrigth); 
                                document.add(datatable3);
                             }
                             else document.add(new Paragraph("Durchmesser der nummerierten Bäume Aufnahmen "+
                                     (k*dhSpalten+1)+" bis "+((k+1)*dhSpalten)));                                          
                            
                             document.add(datatable);
                             if (nPage== 1) document.add(new Paragraph("Seite:"+nPage.toString()+"    Stammverzeichnis ID:"+id+"     Die Verwendung der Daten ohne Genehmigung der NW-FVA ist untersagt!       "+datumStr ));
                             for (int kk=0;kk<nZeilen-1;kk++) datatable.deleteLastRow();
                             nZeilen=1;
                             document.newPage();
                             nPage=nPage+1;
                           }                         
                         
                         }
                         nrxalt = nrx; // schon geschriebene Nummer
                         artxalt = artx;
                         datatable.addCell(getCell(nrxAusgabe));
                         datatable.addCell(getCell(artx.toString()));
                         nauf=k*dhSpalten;
                         nZeilen=nZeilen+1;
                      }
                      nauf=nauf+1;
                      for (int i=nauf; i < aufx; i++){
                            datatable.addCell(getCell(" "));
                         }
                      nauf=aufx;
                      datatable.addCell(getCell(dStr));
                     
                      }         
                }
              }	catch (Exception e)  // Ende der Aufnahmen 1-12
                {	System.out.println(e); } 	
                  
              if (nrxalt!=null)
                  for (int i=nauf; i < (k+1)*dhSpalten; i++){
                              datatable.addCell(getCell(""));
                       }
              if (datatable.getNextRow()> 1){
                 if (nPage> 1) document.add(new Paragraph("Seite:"+nPage.toString()+"    Stammverzeichnis ID:"+id+"     Die Verwendung der Daten ohne Genehmigung der NW-FVA ist untersagt!       "+datumStr ));
                 if (legende){
                    Table datatable3 = new Table(2);
                    datatable3.setPadding(1); datatable3.setSpacing(0);
                    datatable3.setBorder(0);
                    datatable3.setOffset(10);
                    int widths3[] = {80,20}; datatable3.setWidths(widths3);
                    datatable3.setWidth(100);
                    Cell cellleft = new Cell(new Phrase(10,"Durchmesser der nummerierten Bäume Aufnahmen "+(k*dhSpalten+1)+" bis "+((k+1)*dhSpalten)));
                    cellleft.setBorder(Rectangle.NO_BORDER); 
                    cellleft.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datatable3.addCell(cellleft);
                    Cell cellrigth = new Cell(new Phrase(7 , "*  von 1.3 m abweichende Messhöhe" +
                          "         ** verschiedene Messhöhen", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                    cellrigth.setBorder(Rectangle.NO_BORDER); 
                    cellrigth.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datatable3.addCell(cellrigth); 
                    document.add(datatable3);    
                 }
                 else document.add(new Paragraph("Durchmesser der nummerierten Bäume Aufnahmen "+
                         (k*dhSpalten+1)+" bis "+((k+1)*dhSpalten)));
                 
                 document.add(datatable);
                 if (nPage== 1) document.add(new Paragraph("Seite:"+nPage.toString()+"    Stammverzeichnis ID:"+id+"     Die Verwendung der Daten ohne Genehmigung der NW-FVA ist untersagt!       "+datumStr ));
                 nZeilen=1;
                 document.newPage();
                 nPage=nPage+1;
              } 
              datatable.deleteAllRows();
              }
// Anfügen der unnummierten cm              
            Table datatableun = new Table(40);
              datatableun.setBorderWidth(0);
              datatableun.setPadding(1);
              datatableun.setSpacing(0);
              int headerwidthsun[] = {2, 3, 3, 3, 1, 2, 3, 3, 3, 1, 2, 3, 3, 3, 1,
                   2, 3, 3, 3, 1, 2, 3, 3, 3, 1,2, 3, 3, 3, 1,2, 3, 3, 3, 1,2, 3, 3, 3, 1};
             datatableun.setWidths(headerwidthsun);
              datatableun.setWidth(100);
              for (int i=0;i<8;i++){
                 datatableun.addCell(getCell("Auf"));
                 datatableun.addCell(getCell("Art"));
                 datatableun.addCell(getCell("Anz"));
                 datatableun.addCell(getCell("DSt"));
                 datatableun.addCell(getCell("A"));
              }
              try{          
                  Statement stmt = dbconn.Connections[0].createStatement(); 
                  ResultSet rs = stmt.executeQuery("select Baum.edvid, Baum.nr, Baum.auf, Baum.art, Baum.mh, Baum.dmess, Baum.anzahl, Baum.a from Baum where (edvid = \'"
                         +id+"\' AND (Baum.anzahl > 0)  ) ORDER BY  Baum.auf, Baum.art, Baum.d" );
                  boolean abwMessH = false;
                  int vorauf = 0;
                  while (rs.next()) {
                      String edv = rs.getObject("edvid").toString();
                      if (edv == null || edv.length() < 1) {
                          break;
                      }
                      String nrx = rs.getObject("nr").toString();
                      int aufx = rs.getInt("auf");
                      Integer artx = rs.getInt("art");
                      int mhx = rs.getInt("mh");
                      double dx = rs.getDouble("dmess");
                      Integer anzx = rs.getInt("anzahl");
                      String ax = "";
                      Object aObj = rs.getObject("a");
                      if(aObj != null) ax = aObj.toString().trim();
                      Integer aufxx = aufx;
//                    System.out.println(nrx+" "+aufx+" "+artx+" "+dx+" "+hx);
//                    int res = nrx.compareTo(nrxalt);
                      if (nrx.indexOf("cm") > -1 || nrx.indexOf("onum") > -1 || nrx.length() < 1) {
                          if (vorauf != 0 && vorauf != aufx) {
                              datatableun.columns();
                              Cell trennZelle = new Cell();
                              trennZelle.setColspan(5);
                              trennZelle.setBackgroundColor(new Color(230, 230, 230));
                              datatableun.addCell(trennZelle);
                          }
                          vorauf = aufx;
                          datatableun.addCell(getCell(aufxx.toString()));
                          datatableun.addCell(getCell(artx.toString()));
                          datatableun.addCell(getCell(anzx.toString()));
                          datatableun.addCell(getCell(df0.format(dx)));
                          datatableun.addCell(getCell(ax));
                          if (mhx != 13) {
                              abwMessH = true;
                          }
                      }
                  }
              
                  if (datatableun.size()>1){
                    if (nPage> 1) document.add(new Paragraph("Seite:"+nPage.toString()+"    Stammverzeichnis ID:"+id+"     Die Verwendung der Daten ohne Genehmigung der NW-FVA ist untersagt!       "+datumStr ));
                      if (abwMessH){
                      Table datatable3 = new Table(2);
                      datatable3.setPadding(1); datatable3.setSpacing(0);
                      datatable3.setBorder(0);
                      datatable3.setOffset(10);
                      int widths3[] = {85,15}; datatable3.setWidths(widths3);
                      datatable3.setWidth(100);
                      Cell cellleft = new Cell(new Phrase(10,"Durchmesser der unnummerierten Bäume "));
                      cellleft.setBorder(Rectangle.NO_BORDER); 
                      cellleft.setHorizontalAlignment(Element.ALIGN_LEFT);
                      datatable3.addCell(cellleft);
                      Cell cellrigth = new Cell(new Phrase(7 , "Von 1.3 m abweichende Messhöhen kommen vor.", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                      cellrigth.setBorder(Rectangle.NO_BORDER); 
                      cellrigth.setHorizontalAlignment(Element.ALIGN_LEFT);
                      datatable3.addCell(cellrigth); 
                      document.add(datatable3);    
                   }
                   else document.add(new Paragraph("Durchmesser der unnummerierten Bäume "));
                   
                   document.add(datatableun);
                 } 
              
                  ok = true;       // verschoben von unten 29.10.2010
              }	catch (Exception e){e.printStackTrace();}
              //{	System.out.println(e); }
              
                document.close();
                //ok = true;   verschoben nach oben 29.10.2010
        } catch ( Exception e){System.out.println("Fehler creating pdf:"+e); e.printStackTrace();};
        return(ok);
    }

    public Cell getCell(String txt){
        Cell cell=null;
        try { 
            cell = new Cell(new Phrase(txt , FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            
        } catch ( Exception e){System.out.println("Fehler creating Cell");};        
        return cell;
    } 
    
}
