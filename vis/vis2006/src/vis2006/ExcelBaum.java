/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vis2006;  
import java.util.*;
import org.apache.poi.ss.usermodel.*;

/**
 * 
 * @author sprauer
 */
public class ExcelBaum {
    public String edvid = ""; 
    public String nr =""; 
    public int art=0;
    public String zf=" "; 
    public String ou=" ";            
    public int d1=0; 
    public String a1=" "; 
    public double alt2 = 0.0; 
    public int dk21 = 0;
    public int dk22 = 0;
    public int d2=0;
    public String a2=" ";  
    public int h2=0; 
    public int k2=0; 
    public String r2=" "; 
    public int mh=0;
    public int dn=0; 
    public int dnk=0;
    public String an=" "; 
    public String en = " "; 
    public double alt_en = 0.0; 
    public int hn=0; 
    public int kn=0;
    public String zfn=" "; 
    public String oun=" "; 
    public String bem=""; 
    public String qual="";
    
    public Map<String, String> zusatzWerte = new HashMap<String,String>();

    private int nmax = 48;   // Anzahl möglicher Spalten
    
    private List<Integer> stringcols = new ArrayList<Integer>(3);
    private Set<String> spalten;
    private Map<String, Integer> spaltenordnung;

    private String hinweis = "";
    
    
    
    public ExcelBaum(){
    // Zulässige Spaltenüberschriften
        String spalt[] = {"EDVID","NR","ART","ZF","OU","MH","D1","A1","H1","ALT2","D2_1","D2_2","D2","DDN","SN","A2","H2","K2",
        "B2","MHN","DN","DNK","AN","EN","ALT_EN","HN","KN","KMAXN","HMAXKBN","ZFN","OUN","R","BEMERK","QUA",
        //"BEZNR","BEZART","DIST","GON","G0","G50","G100","G150","G200","G250","G300","G350","KMP_DIST","KMP_GON",
        "VDFV","KRAFT","STA","KRO","SEIG"};
        spalten = new HashSet<String>(Arrays.asList(spalt));
        spaltenordnung = new HashMap<String, Integer>();
      
    }

    public String getHinweis(){
        return hinweis;
    }

    public int getMaxAnzahlSpalten(){
        return nmax;
    }
    
    /**
     * Erkennen und setzten der Spaltenreihenfolge einer Excel-Felddatendatei
     * @param sheet Tabellenblatt der Excel-Felddatendatei (org.apache.poi.ss.usermodel.Sheet)
     */ 
    public boolean setSpaltenordnung(Sheet sheet){
        boolean ok = true;
        try{
            Row kopf = sheet.getRow(0);
            int letzterSpaltenIndex = -1;
            for (Cell cell : kopf){
                int spaltenIndex = cell.getColumnIndex();
                if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                    String spaltenTitel = cell.getStringCellValue().trim().toUpperCase();                    
                    // Leerspalte zwischen den Datenspalten?
                    if(spaltenIndex != letzterSpaltenIndex+1){
                        ok = false;
                        hinweis = "Espacios vacíos entre las columnas de datos";
                        System.out.println(hinweis);
                        break;
                    }
                    letzterSpaltenIndex ++;
                    // zulässiger Spaltenkopf?
                    if(spalten.contains(spaltenTitel)){
                        // Spaltenkopf nicht doppelt?
                        if(!spaltenordnung.containsKey(spaltenTitel)){    
                            spaltenordnung.put(spaltenTitel, spaltenIndex);
                        }else{
                            ok = false;
                            System.out.println("Doble encabezado de columna: " + spaltenTitel);
                            hinweis = "Encabezado " + spaltenTitel + " dos veces";
                        }
                    }else {
                        if (!cell.getStringCellValue().trim().isEmpty()){
                            ok = false;
                            System.out.println("Error: Cabecera de columna no válida ("+cell.getStringCellValue()+")");
                            hinweis = "Encabezado de columna no válida ("+cell.getStringCellValue()+")";
                        }
                    }
    
                } else if(cell.getCellType()==Cell.CELL_TYPE_BLANK){  // das ist ok
                } else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC && cell.getNumericCellValue()==0){ // das auch
                } else if(cell.getCellType()==Cell.CELL_TYPE_FORMULA && cell.getCellFormula().equals("")){ // das auch
                } else {
                    ok = false;
                    hinweis = "Título de la columna debe estar presente en forma de texto. Cambiar a otro tamaño en la columna. " + (spaltenIndex+1) +".";
                    System.out.println(hinweis);
                }     
            }
            stringcols.add(spaltenordnung.get("EDVID"));
            stringcols.add(spaltenordnung.get("NR"));
            stringcols.add(spaltenordnung.get("QUA"));
            if(spaltenordnung.containsKey("BEZNR")) stringcols.add(spaltenordnung.get("BEZNR"));
            //System.out.println("Stringspalten: "+stringcols);
        }catch(Exception e)
                {System.out.println("Error al crear el orden de las columnas: "+e);}
        
        return(ok);
    }
    
    /**
     * Zeile einer Excel-Felddatendatei einlesen
     * @param zeile Einzulesende Zeile einer Excel-Felddatendatei (org.apache.poi.ss.usermodel.Row)
     */
    public String zeileLesen(Row zeile){
        String ergebnis = "Error";
        Object werte[] = new Object[spalten.size()];
        zusatzWerte.clear();
        for(Cell cell : zeile){
            int colind = cell.getColumnIndex();
            //System.out.println("Spalte " + cell.getColumnIndex());
            switch(cell.getCellType()) {
                case Cell.CELL_TYPE_STRING: 
                    String temps = cell.getStringCellValue().trim();
                    if (! stringcols.contains(colind) && temps.equals(""))
                        werte[colind] = null;
                    else werte[colind] = temps;
                     
                   
                    /* 
                    RichTextString rts = cell.getRichStringCellValue();
                    System.out.println(rts);
                    System.out.println(rts.numFormattingRuns());

                    for (int i=0; i<rts.numFormattingRuns(); i++) { 
                        System.out.println("index = "+rts.getIndexOfFormattingRun(i)); 
                     
                        System.out.println("font = "+rts.getFontOfFormattingRun(i));  
                        Font font =  wb.getFontAt(rts.getFontOfFormattingRun(i));  
                        System.out.println("bold = "+font.getBoldweight());  
                        System.out.println("italic = "+font.getItalic());  
                        System.out.println("underline = "+font.getUnderline());  
                        System.out.println("strikeout = "+font.getStrikeout());  
                        System.out.println();  
                    }     
                     */
                    //System.out.println("Spalte " + colind + ": " + werte[colind]);
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    //System.out.println("Numfeld: "+cell.getNumericCellValue());
                    if (! stringcols.contains(colind)){
                        werte[colind] = cell.getNumericCellValue();
                    } else{
                        int zahl = ((Double) cell.getNumericCellValue()).intValue();
                        werte[colind] = String.valueOf(zahl);
                        //System.out.println("Stringwert: "+alsString);
                    }
                    //System.out.println("Spalte " + colind + ": " + werte[colind]);
                    break;
                case Cell.CELL_TYPE_FORMULA: 
                    int typ = cell.getCachedFormulaResultType();
                    //System.out.println("Numfeld: "+cell.getNumericCellValue());
                    //System.out.println("Spalte " + colind + ": ");
                    switch(typ){
                        case Cell.CELL_TYPE_NUMERIC:
                            werte[colind] = cell.getNumericCellValue();                        
                            break;
                        case Cell.CELL_TYPE_STRING:
                            Object tempo = cell.getStringCellValue().trim();
                            if(!stringcols.contains(colind) && tempo.toString().trim().equals("")) tempo = 0;
                            werte[colind] = tempo;
                            break;
                        default:
                            if(cell.getCellType() != Cell.CELL_TYPE_BLANK){
                                ergebnis = "Formato de celda erroneo en la columna "+(colind-1)+":\n Solo se pueden leer Números, texto y fórmulas.";
                                String dattyp = String.valueOf(cell.getCellType());
                                System.out.println("Otro formato de celda: "+dattyp);
                            }
                    }
                    if (typ==Cell.CELL_TYPE_NUMERIC && stringcols.contains(colind)){
                        int zahl = ((Double) cell.getNumericCellValue()).intValue();
                        werte[colind] = String.valueOf(zahl);
                    }
                    break;
                case Cell.CELL_TYPE_BLANK:
                    werte[colind] = null;
                    if(stringcols.contains(colind)) werte[colind] = "";
                    //System.out.println("Spalte " + colind + ": " + werte[colind]);
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    boolean temp = cell.getBooleanCellValue();
                    if(temp) werte[colind] = "wahr";
                    else werte[colind] = "falsch";
                    //System.out.println("Spalte " + colind + ": " + werte[colind]);
                    break;
                default:
                    System.out.println("raus");
                        ergebnis = "Formato de celda de columna incorrecto "+(colind-1)+":\n Solo se pueden leer Números, texto y fórmulas.";
                        String dattyp = String.valueOf(cell.getCellType());
                        System.out.println("Otro formato de celda: "+dattyp);

            }
            if(ergebnis.equals("Error")) ergebnis = "";
            
        } // Ende der Zeile
        String fehlerSpalte = "";
        try{
        edvid = "";
        if(spaltenordnung.get("EDVID") != null && werte[spaltenordnung.get("EDVID")] != null){
            fehlerSpalte = "EDVID";
            edvid = werte[spaltenordnung.get("EDVID")].toString();
        //System.out.println("edvid: "+edvid);
        }
        nr = "";
        if(spaltenordnung.get("NR") != null && werte[spaltenordnung.get("NR")] != null){
            fehlerSpalte = "NR";
            nr = werte[spaltenordnung.get("NR")].toString();
            System.out.print(nr+", ");
        }
        art = 0;
        if(spaltenordnung.get("ART") != null && werte[spaltenordnung.get("ART")] != null){
            fehlerSpalte = "ART";
            art = ((Double)werte[spaltenordnung.get("ART")]).intValue();
        //System.out.println("Art: "+art);
        }
        zf =" ";
        if(spaltenordnung.get("ZF") != null && werte[spaltenordnung.get("ZF")] != null) {
            fehlerSpalte = "ZF";
            zf = werte[spaltenordnung.get("ZF")].toString().trim();
        //System.out.println("zf: "+zf);
        }
        ou = " ";
        if (spaltenordnung.get("OU") != null && werte[spaltenordnung.get("OU")] != null){
            fehlerSpalte = "OU";
            ou = werte[spaltenordnung.get("OU")].toString().trim();
        //System.out.println("ou: "+ou);
        }
        d1 = 0;
        if(spaltenordnung.get("D1") != null && werte[spaltenordnung.get("D1")] != null){
            fehlerSpalte = "D1";
            d1 = ((Double)werte[spaltenordnung.get("D1")]).intValue();
        //System.out.println("D1: "+d1);
        }
        a1 = " ";
        if(spaltenordnung.get("A1") != null && werte[spaltenordnung.get("A1")] != null){
            fehlerSpalte = "A1";
            a1 = werte[spaltenordnung.get("A1")].toString().trim();
        //System.out.println("A1: "+a1);
        }
        alt2 = 0.0;
        if(spaltenordnung.get("ALT2") != null && werte[spaltenordnung.get("ALT2")] != null){
            fehlerSpalte = "ALT2";
            alt2 = (Double)werte[spaltenordnung.get("ALT2")];
        }
        dk21 = 0;
        if(spaltenordnung.get("D2_1") != null && werte[spaltenordnung.get("D2_1")] != null){
            fehlerSpalte = "D2_1";
            dk21 = ((Double)werte[spaltenordnung.get("D2_1")]).intValue();
        }
        dk22 = 0;
        if(spaltenordnung.get("D2_2") != null && werte[spaltenordnung.get("D2_2")] != null){
            fehlerSpalte = "D2_2";
            dk22 = ((Double)werte[spaltenordnung.get("D2_2")]).intValue();
        }
        d2 = 0;
        if(spaltenordnung.get("D2") != null && werte[spaltenordnung.get("D2")] != null){
            fehlerSpalte = "D2";
            d2 = ((Double)werte[spaltenordnung.get("D2")]).intValue();
        //System.out.println("D2: "+d2);
        }
        a2 = " ";
        if(spaltenordnung.get("A2") != null && werte[spaltenordnung.get("A2")] != null){
            fehlerSpalte = "A2";
            a2 = werte[spaltenordnung.get("A2")].toString();
        }
        h2 = 0;
        if(spaltenordnung.get("H2") != null && werte[spaltenordnung.get("H2")] != null){
            fehlerSpalte = "H2";
            h2 = ((Double)werte[spaltenordnung.get("H2")]).intValue();
        }
        k2 = 0;
        if(spaltenordnung.get("K2") != null && werte[spaltenordnung.get("K2")] != null){
            fehlerSpalte = "K2";
            k2 = ((Double)werte[spaltenordnung.get("K2")]).intValue();
        }
        // Messhöhe aus Voraufnahme es sei denn neue Angabe unter MHN
        mh = 13;
        if(spaltenordnung.get("MH")!=null && werte[spaltenordnung.get("MH")] != null){
            fehlerSpalte = "MH";
            mh = ((Double)werte[spaltenordnung.get("MH")]).intValue();
        }
        if(spaltenordnung.get("MHN")!=null && werte[spaltenordnung.get("MHN")] != null){
            fehlerSpalte = "MHN";
            mh = ((Double)werte[spaltenordnung.get("MHN")]).intValue();
        }
        dn = 0;
        if(spaltenordnung.get("DN") != null && werte[spaltenordnung.get("DN")] != null){
            fehlerSpalte = "DN";
            dn = ((Double)werte[spaltenordnung.get("DN")]).intValue();
        //System.out.println("DN: "+dn);
        }
        dnk = 0;
        if(spaltenordnung.get("DNK")!=null && werte[spaltenordnung.get("DNK")] != null){
            fehlerSpalte = "DNK";
            dnk = ((Double)werte[spaltenordnung.get("DNK")]).intValue();
        //System.out.println("DNK");
        }
        an = " ";
        if(spaltenordnung.get("AN") != null && werte[spaltenordnung.get("AN")] != null){
            fehlerSpalte = "AN";
            an = werte[spaltenordnung.get("AN")].toString().trim();
        //System.out.println("AN");
        }
        en = " ";
        if(spaltenordnung.get("EN") != null && werte[spaltenordnung.get("EN")] != null){
            fehlerSpalte = "EN";
            en = werte[spaltenordnung.get("EN")].toString().trim();
        //System.out.println("EN");
        }
        alt_en= 0.0;
        if(spaltenordnung.get("ALT_EN") != null && werte[spaltenordnung.get("ALT_EN")] != null){
            fehlerSpalte = "ALT_EN";
            alt_en = (Double)werte[spaltenordnung.get("ALT_EN")];
        }
        hn = 0;
        if(spaltenordnung.get("HN") != null && werte[spaltenordnung.get("HN")] != null){
            fehlerSpalte = "HN";
            hn = ((Double)werte[spaltenordnung.get("HN")]).intValue();
        //System.out.println("HN");
        }
        kn = 0;
        if(spaltenordnung.get("KN") != null && werte[spaltenordnung.get("KN")] != null){
            fehlerSpalte = "KN";
            kn = ((Double)werte[spaltenordnung.get("KN")]).intValue();
        //System.out.println("KN");
        }zfn =" ";
        if(spaltenordnung.get("ZFN") != null && werte[spaltenordnung.get("ZFN")] != null){
            fehlerSpalte = "ZFN";
            zfn = werte[spaltenordnung.get("ZFN")].toString().trim();
        //System.out.println("ZFN");
        }
        oun = " ";
        if(spaltenordnung.get("OUN") != null && werte[spaltenordnung.get("OUN")] != null){
            fehlerSpalte = "OUN";
            oun = werte[spaltenordnung.get("OUN")].toString().trim();
        //System.out.println("OUN");
        }
        r2 = " ";
        if(spaltenordnung.get("R") != null && werte[spaltenordnung.get("R")] != null){
            fehlerSpalte = "R";
            r2 = werte[spaltenordnung.get("R")].toString().trim();
        //System.out.println("R");
        }
        bem = " ";
        if(spaltenordnung.get("BEMERK") != null && werte[spaltenordnung.get("BEMERK")] != null){
            fehlerSpalte = "BEMERK";
            bem = werte[spaltenordnung.get("BEMERK")].toString();
        //System.out.println("Bemerk");
        }
        qual = " ";
        if(spaltenordnung.get("QUA") != null && werte[spaltenordnung.get("QUA")] != null){
            fehlerSpalte = "QUA";
            qual = werte[spaltenordnung.get("QUA")].toString().trim();
        }

        
// Zusätzliche Spalten (nicht standardmäßig in der Eingabetabelle vorhanden)       
        if(spaltenordnung.get("DDN")!=null && werte[spaltenordnung.get("DDN")] != null){
            fehlerSpalte = "DDN";
            zusatzWerte.put("DDN", String.valueOf(((Double)werte[spaltenordnung.get("DDN")]).intValue()));
        //System.out.println("DDN");
        }
        if(spaltenordnung.get("SN") != null && werte[spaltenordnung.get("SN")] != null){
            fehlerSpalte = "SN";
            zusatzWerte.put("SN", String.valueOf(((Double)werte[spaltenordnung.get("SN")]).intValue()));
        }
        if(spaltenordnung.get("KMAXN") != null && werte[spaltenordnung.get("KMAXN")] != null){
            fehlerSpalte = "KMAXN";
            zusatzWerte.put("KMAXN", String.valueOf(((Double)werte[spaltenordnung.get("KMAXN")]).intValue()));
        }
        if(spaltenordnung.get("HMAXKBN") != null && werte[spaltenordnung.get("HMAXKBN")] != null){
            fehlerSpalte = "HMAXKBN";
            zusatzWerte.put("HMAXKBN", String.valueOf(((Double)werte[spaltenordnung.get("HMAXKBN")]).intValue()));
        } 
/*  Möglichkeit angedacht, neu eingemessene Koordinaten in den Stammverteilungsplan zu bringen
        if(spaltenordnung.get("BEZNR") != null && werte[spaltenordnung.get("BEZNR")] != null){
            fehlerSpalte = "BEZNR";
            zusatzWerte.put("BEZNR",werte[spaltenordnung.get("BEZNR")].toString().trim());
        }
        if(spaltenordnung.get("BEZART") != null && werte[spaltenordnung.get("BEZART")] != null){
            fehlerSpalte = "BEZART";
            zusatzWerte.put("BEZART", String.valueOf(((Double)werte[spaltenordnung.get("BEZART")]).intValue()));
        }
        if(spaltenordnung.get("DIST") != null && werte[spaltenordnung.get("DIST")] != null){
            fehlerSpalte = "DIST";
            zusatzWerte.put("DIST", String.valueOf(((Double)werte[spaltenordnung.get("DIST")]).intValue()));
        }
        if(spaltenordnung.get("GON") != null && werte[spaltenordnung.get("GON")] != null){
            fehlerSpalte = "GON";
            zusatzWerte.put("GON", String.valueOf(((Double)werte[spaltenordnung.get("GON")]).intValue()));
        }
        if(spaltenordnung.get("G0") != null && werte[spaltenordnung.get("G0")] != null){
            fehlerSpalte = "G0";
            zusatzWerte.put("G0", String.valueOf(((Double)werte[spaltenordnung.get("G0")]).intValue()));
        }
        if(spaltenordnung.get("G50") != null && werte[spaltenordnung.get("G50")] != null){
            fehlerSpalte = "G50";
            zusatzWerte.put("G50", String.valueOf(((Double)werte[spaltenordnung.get("G50")]).intValue()));
        }
        if(spaltenordnung.get("G100") != null && werte[spaltenordnung.get("G100")] != null){
            fehlerSpalte = "G100";
            zusatzWerte.put("G100", String.valueOf(((Double)werte[spaltenordnung.get("G100")]).intValue()));
        }
        if(spaltenordnung.get("G150") != null && werte[spaltenordnung.get("G150")] != null){
            fehlerSpalte = "G150";
            zusatzWerte.put("G150", String.valueOf(((Double)werte[spaltenordnung.get("G150")]).intValue()));
        }
        if(spaltenordnung.get("G200") != null && werte[spaltenordnung.get("G200")] != null){
            fehlerSpalte = "G200";
            zusatzWerte.put("G200", String.valueOf(((Double)werte[spaltenordnung.get("G200")]).intValue()));
        }
        if(spaltenordnung.get("G250") != null && werte[spaltenordnung.get("G250")] != null){
            fehlerSpalte = "G250";
            zusatzWerte.put("G250", String.valueOf(((Double)werte[spaltenordnung.get("G250")]).intValue()));
        }
        if(spaltenordnung.get("G300") != null && werte[spaltenordnung.get("G300")] != null){
            fehlerSpalte = "G300";
            zusatzWerte.put("G300", String.valueOf(((Double)werte[spaltenordnung.get("G300")]).intValue()));
        }
        if(spaltenordnung.get("G350") != null && werte[spaltenordnung.get("G350")] != null){
            fehlerSpalte = "G350";
            zusatzWerte.put("G350", String.valueOf(((Double)werte[spaltenordnung.get("G350")]).intValue()));
        }
        if(spaltenordnung.get("KMP_DIST") != null && werte[spaltenordnung.get("KMP_DIST")] != null){
            fehlerSpalte = "KMP_DIST";
            zusatzWerte.put("KMP_DIST", String.valueOf(((Double)werte[spaltenordnung.get("KMP_DIST")]).intValue()));
        }
        if(spaltenordnung.get("KMP_GON") != null && werte[spaltenordnung.get("KMP_GON")] != null){
            fehlerSpalte = "KMP_GON";
            zusatzWerte.put("KMP_GON", String.valueOf(((Double)werte[spaltenordnung.get("KMP_GON")]).intValue()));
        }
        */
        
        if(spaltenordnung.get("VDFV") != null && werte[spaltenordnung.get("VDFV")] != null){
            fehlerSpalte = "VDFV";
            zusatzWerte.put("VDFV",String.valueOf(((Double)werte[spaltenordnung.get("VDFV")]).intValue()));
        }
        if(spaltenordnung.get("KRAFT") != null && werte[spaltenordnung.get("KRAFT")] != null){
            fehlerSpalte = "KRAFT";
            zusatzWerte.put("KRAFT",String.valueOf(((Double)werte[spaltenordnung.get("KRAFT")]).intValue()));
        }
        if(spaltenordnung.get("STA") != null && werte[spaltenordnung.get("STA")] != null){
            fehlerSpalte = "STA";
            zusatzWerte.put("STA",String.valueOf(((Double)werte[spaltenordnung.get("STA")]).intValue()));
        }
        if(spaltenordnung.get("KRO") != null && werte[spaltenordnung.get("KRO")] != null){
            fehlerSpalte = "KRO";
            zusatzWerte.put("KRO",String.valueOf(((Double)werte[spaltenordnung.get("KRO")]).intValue()));
        }
        if(spaltenordnung.get("SEIG") != null && werte[spaltenordnung.get("SEIG")] != null){
            fehlerSpalte = "SEIG";
            zusatzWerte.put("SEIG", werte[spaltenordnung.get("SEIG")].toString().trim());
        }

        } catch (ClassCastException e) {
            //e.printStackTrace();
            ergebnis = "Tipos de datos incorrectos en la columna " + fehlerSpalte + " por Número de Árbol "+nr+" (por Ej. Texto en lugar de número)";
        }

        return ergebnis;
        
    }
    
    public Map getSpaltenordnung(){
            return spaltenordnung;
    }
    
}
