/*
 * Hoehenverzeichnis.java
 *
 * Created on 12. September 2005, 12:48
 */

package vis2006;
import DatabaseManagement.*;
import Hilfsklassen.HeuteString;
import java.text.*;
import java.util.*;
import java.io.*; 
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.html.HtmlWriter;
import java.awt.Color;
import java.sql.*;


/**
 *
 * @author  admin
 */
public class Hoehenverzeichnis {
    DataExchange ex =new DataExchange();
    Cell cell= new Cell();
    /** Creates a new instance of Hoehenverzeichnis */
    public Hoehenverzeichnis() {
    }

    public boolean createHoehenverzeichnis(DBConnection dbconn, String id, String filename){
// Datenbank ist bereits geöffnet aus Vis2006
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
              document.addKeywords("Forst, Versuchwesen");
              document.addCreator("Vis2006 Version 2/2006");
//
              document.open();
//  Überschrift des Ausdrucks als Tabelle mit 2 Spalten
              //System.out.println("Überschrift...");
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
              cellBild.setRowspan(3);
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
              Cell cell41 = new Cell(new Phrase("Höhenverzeichnis ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
              cell41.setBorder(Rectangle.NO_BORDER);
              cell41.setHorizontalAlignment(Element.ALIGN_CENTER);
              datatable2.addCell(cell41);

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
              }	catch (Exception e)  {	System.out.println(e); }	
//
              document.add(new Paragraph(""));
              document.add(new Paragraph("Versuchsfläche: "+flaechenName+"   "+abtName+"      ID:"+id));
              
              //System.out.println("Datentabelle...");
              int dhSpalten=9; 
              int nZeilen=1;
              Table datatable = new Table(4*dhSpalten+2);
              
              for (int k=0;k<5; k++ ){     // Maximal 45 Aufnahmen (5*9)
              datatable.setBorderWidth(0);
              datatable.setPadding(1);
              datatable.setSpacing(0);
              int headerwidths[] = {5, 3, 3, 3, 3, 3 , 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
               3, 3, 3, 3, 3, 3,3, 3, 3,3, 3, 3,3, 3, 3,3, 3, 3,3, 3, 3         };
              datatable.setWidths(headerwidths);
              datatable.setWidth(100);
              datatable.addCell(getCell("Nr"));
              datatable.addCell(getCell("Art"));
              for (int i=0;i<dhSpalten;i++){
                 Integer ix=(k*dhSpalten)+i+1; 
                 datatable.addCell(getCell("D"+ix.toString()));
                 datatable.addCell(getCell("H"+ix.toString()));
                 datatable.addCell(getCell("K"+ix.toString()));
                 datatable.addCell(getCell("A"+ix.toString()));
              }
              nZeilen=1;
              Cell c = new Cell();
              
// Abfrage der Höhen Aufnahme 1 - 9
              //System.out.println("Höhen der nummerierten Bäume");
              String nrxalt =null;
              int nauf=k*dhSpalten;
              Integer kleiner = (k+1)*dhSpalten+1;
              Integer groesser = k*dhSpalten;
              try{          
                  Statement stmt = dbconn.Connections[0].createStatement(); 
                  ResultSet rs = stmt.executeQuery("select Baum.edvid, Baum.nr, Baum.auf, Baum.art, Baum.d, Baum.h, Baum.k, Baum.a from Baum " +
                          "where (edvid = \'"+id+"\' AND (Baum.h > 1.3) AND (Baum.auf < "+kleiner.toString()+" ) AND (Baum.auf > "+groesser.toString()+" )) ORDER BY Baum.art, Baum.nr, Baum.auf" );
                  while (rs.next()) {
                     String edv = rs.getObject("edvid").toString();
//                     if (edv==null || edv.length()<1) break;
                     String nrx = rs.getObject("nr").toString();
                     int aufx = rs.getInt("auf");
                     Integer artx = rs.getInt("art");
                     double dx = rs.getDouble("d");
                     double hx = rs.getDouble("h");
                     double kx = rs.getDouble("k");
                     String ax=" ";
                     Object atest = rs.getObject("a");
                     if(atest != null)  ax = atest.toString().trim();
//                  System.out.println(nrx+" "+aufx+" "+artx+" "+dx+" "+hx+" "+kx);
//                  int res = nrx.compareTo(nrxalt);
                     if (nrx.indexOf("nur")< 0 && nrx.indexOf("cm")< 0 && nrx.indexOf("onum")< 0 && nrx.indexOf("HG")<0 && nrx.indexOf("HB")< 0){
                       if (nrxalt == null || nrx.compareTo(nrxalt)!=0){
                          if (nrxalt!=null){
                           for (int i=nauf; i < (k+1)*dhSpalten; i++){
                              datatable.addCell(getCell(""));
                              datatable.addCell(getCell(""));
                              datatable.addCell(getCell(""));
                              datatable.addCell(getCell(""));
                           }
                         if ((nZeilen > 27) || (nZeilen >22 && nPage==1)){  
                           if (nPage> 1 &&  (filename.indexOf(".pdf") >-1)) document.add(new Paragraph("Seite:"+nPage.toString()+"    Höhenverzeichnis ID:"+id+"     Die Verwendung der Daten ohne Genehmigung der NW-FVA ist untersagt!       "+datumStr ));
                           if (filename.indexOf(".pdf") >-1) document.add(new Paragraph("Höhen der nummerierten Bäume Aufnahmen "+(k*dhSpalten+1)+" bis "+((k+1)*dhSpalten)));
                           document.add(datatable);
                           if (nPage== 1 &&  (filename.indexOf(".pdf") >-1) ) document.add(new Paragraph("Seite:"+nPage.toString()+"    Höhenverzeichnis ID:"+id+"     Die Verwendung der Daten ohne Genehmigung der NW-FVA ist untersagt!       "+datumStr ));
                           for (int kk=0;kk<nZeilen-1;kk++) datatable.deleteLastRow();
                           nZeilen=1;
                           document.newPage();
                           nPage=nPage+1;
                        }
                       }
                       nrxalt=nrx; 
                       datatable.addCell(getCell(nrx));
                       datatable.addCell(getCell(artx.toString()));
                       nauf=k*dhSpalten;
                       nZeilen=nZeilen+1;
                    }
                    nauf=nauf+1;
                    for (int i=nauf; i < aufx; i++){
                          datatable.addCell(getCell("0"));
                          datatable.addCell(getCell("0"));
                          datatable.addCell(getCell("0"));
                          datatable.addCell(getCell(""));
                       }
                    nauf=aufx;
                    datatable.addCell(getCell(df0.format(dx)));
                    datatable.addCell(getCell(df0.format(hx)));
                    datatable.addCell(getCell(df0.format(kx)));
                    datatable.addCell(getCell(ax));
                    }
                      
                 }
              
              if (nrxalt!=null)
                  for (int i=nauf; i < (k+1)*dhSpalten; i++){
                              datatable.addCell(getCell(""));
                              datatable.addCell(getCell(""));
                              datatable.addCell(getCell(""));
                              datatable.addCell(getCell(""));
                       }
              }	catch (Exception e)  {System.out.println("Consulta Altura: "+e); }
           if (datatable.getNextRow()> 1){
                 if (nPage> 1 && (filename.indexOf(".pdf") >-1)) document.add(new Paragraph("Seite:"+nPage.toString()+"    Höhenverzeichnis ID:"+id+"     Die Verwendung der Daten ohne Genehmigung der NW-FVA ist untersagt!       "+datumStr ));
                 if (filename.indexOf(".pdf") >-1) document.add(new Paragraph("Höhen der nummerierten Bäume Aufnahmen "+(k*dhSpalten+1)+" bis "+((k+1)*dhSpalten)));
                 document.add(datatable);
                 if (nPage== 1 && (filename.indexOf(".pdf") >-1)) document.add(new Paragraph("Seite:"+nPage.toString()+"    Höhenverzeichnis ID:"+id+"     Die Verwendung der Daten ohne Genehmigung der NW-FVA ist untersagt!       "+datumStr ));
                 nZeilen=1;
                 document.newPage();
                 nPage=nPage+1;
              } 
              datatable.deleteAllRows();
              }
// Anfügen der unnummierten Höhen
              //System.out.println("Unnummerierte Höhen");
              Table datatableun = new Table(36);
              datatableun.setBorderWidth(0);
              datatableun.setPadding(1);
              datatableun.setSpacing(0);
//              int headerwidthsun[] = {3, 3, 3, 3, 3, 3 , 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4,
//               3, 3, 3, 4, 3, 3, 3, 4, 3, 3, 3, 4, 3, 3, 3,4, 3, 3,3,         };
//             datatableun.setWidths(headerwidthsun);
              datatableun.setWidth(100);
              for (int i=0;i<9;i++){
                 datatableun.addCell(getCell("Auf"));
                 datatableun.addCell(getCell("Art"));
                 datatableun.addCell(getCell("D"));
                 datatableun.addCell(getCell("H"));
              }
              try{          
                  Statement stmt = dbconn.Connections[0].createStatement(); 
                  ResultSet rs = stmt.executeQuery("select Baum.edvid, Baum.nr, Baum.auf, Baum.art, Baum.d, Baum.h, Baum.a " +
                          "FROM Baum WHERE (edvid = \'"+id+"\' AND (Baum.h > 1.3)  ) ORDER BY  Baum.auf, Baum.art" );
                  int vorauf = 0;
                  while (rs.next()) {
                     String edv = rs.getObject("edvid").toString();
                     if (edv==null || edv.length()<1) break;
                     String nrx = rs.getObject("nr").toString();
                     int aufx = rs.getInt("auf");
                     Integer artx = rs.getInt("art");
                     double dx = rs.getDouble("d");
                     double hx = rs.getDouble("h");
                     String ax = rs.getString("a");
                     ax = ax.trim();
//                  System.out.println(nrx+" "+aufx+" "+artx+" "+dx+" "+hx);
//                  int res = nrx.compareTo(nrxalt);
                     if (nrx.indexOf("nurH")> -1 || nrx.indexOf("cm")> -1 || nrx.length() < 1 || 
                             nrx.indexOf("onum")> -1 || nrx.indexOf("HG")> -1 || nrx.indexOf("HB")> -1 ){
                         if(vorauf!= 0 && vorauf != aufx){
                             datatableun.columns();
                             Cell trennZelle = new Cell();
                             trennZelle.setColspan(4);
                             trennZelle.setBackgroundColor(new Color(230,230,230));
                             datatableun.addCell(trennZelle);
                         }
                         vorauf = aufx;
                        datatableun.addCell(getCell(((Integer)aufx).toString()));
                        datatableun.addCell(getCell(artx.toString()));
                        datatableun.addCell(getCell(df0.format(dx)));
                        datatableun.addCell(getCell(df0.format(hx)));
                        
                     }
                     
                }
              }	catch (Exception e)  {System.out.println("Error al consultar altura sin numerar: "+e); }	
              
             if (datatableun.size()>1){
                 if (nPage> 1) document.add(new Paragraph("Seite:"+nPage.toString()+"    Höhenverzeichnis ID:"+id+"     Die Verwendung der Daten ohne Genehmigung der NW-FVA ist untersagt!       "+datumStr ));
                 document.add(new Paragraph("Höhen der unnummerierten Bäume "));
                 document.add(datatableun);
             }    
              
              document.close();
              ok = true;

        } catch ( Exception e){System.out.println("Error creando pdf");}
        return (ok);
    }

    public Cell getCell(String txt){
        Cell cell=null;
        try { 
            cell = new Cell(new Phrase(txt , FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            
        } catch ( Exception e){System.out.println("Error creando celda");};        
        return cell;
    } 
    
}
