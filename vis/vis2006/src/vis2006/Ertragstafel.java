/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vis2006;
import treegross.base.*;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import java.util.Iterator;
import java.io.File;
/**
 *
 * 
 */
public class Ertragstafel {
    String pfad;
    String datei; 
    boolean oberhoehe;
    int alter;
    double suchH;
    double ekl;
    double h;
    double gha;
    double vha;
    double ivha;
    
    /** Creates a new instance of Ertragstafel */
    public Ertragstafel(String Pfad) {
        pfad = Pfad;          
    }


    public Ertragstafelzeile werteBerechnen(Species Sp, int Alter) {
        alter = Alter;
        Species sp = Sp;
// Ertragstafel suchen
        setET(sp);
        if(sp.h100<=1.3) oberhoehe = false;
        else oberhoehe = true;

// Ertragstafelwerte lesen        
        double eklalt =-99.0;
        double eklET = 0.0;
        int aET = 0;
        double hET = 0.0;
        double gET = 0.0;
        double gausET = 0.0; //ausscheidend
        double vET = 0.0;
        double ivET = 0.0;

        Ertragstafelzeile etz = new Ertragstafelzeile(0,0.0,0.0,0.0,0.0,0.0);
        Ertragstafelzeile et[] = new Ertragstafelzeile[1000]; 
        Ertragstafelzeile etx[] = new Ertragstafelzeile[20];         
        int nEt=0;

        try {
            SAXBuilder builder = new SAXBuilder(); 
            Document doc = builder.build(datei);
            Element ET = doc.getRootElement();
            Iterator it = ET.getChildren("Zeile").iterator();
            while (it.hasNext()) {
                Element zeile = (Element) it.next();
                eklET = 0.0;
                eklET = Double.parseDouble(zeile.getChild("Ertragsklasse").getText().trim());
                aET = 0;
                aET = Integer.parseInt(zeile.getChild("Alter").getText().trim());
                hET = 0.0;
                String htxt = "";
                if (oberhoehe) {
                    htxt = zeile.getChild("H100").getText().trim();
                    if (htxt.length() > 0)  hET = Double.parseDouble(htxt);
                    if (hET <= 0.0) oberhoehe = false;      // falls die Ertragstafel keine Oberhöhen hat
                }
                if(!oberhoehe){
                    htxt = zeile.getChild("Hg").getText().trim();
                    if (htxt.length() > 0)  hET = Double.parseDouble(htxt);
                }  
                gET = 0.0;
                gausET = 0.0;
                String gtxt = zeile.getChild("G").getText().trim();
                String gaustxt = zeile.getChild("G_ausscheidend").getText().trim();
                if (gtxt.length() > 0) {
                    gET = Double.parseDouble(gtxt);
                }
                if (gaustxt.length() > 0) {
                    gausET = Double.parseDouble(gaustxt);
                }

                vET = 0.0;
                String vtxt = zeile.getChild("V").getText().trim();
                if (vtxt.length() > 0) {
                    vET = Double.parseDouble(vtxt);
                }
                ivET = 0.0;
                String ivtxt = zeile.getChild("iV").getText().trim();
                if (ivtxt.length() > 0) {
                    ivET = Double.parseDouble(ivtxt);
                }
                et[nEt] = new Ertragstafelzeile(aET, eklET, hET, gET+gausET, vET, ivET); //! Gesamtgrundfläche
                nEt = nEt + 1;
                
            }
        } catch (Exception e) { System.out.println("Problem mit: "+datei+" : "+e); }

// Suchhöhe
        if(oberhoehe)
            suchH = sp.h100;
        else suchH = sp.hg;

        if (suchH > 1.3) {
// Anzahl der Ertragsklassen
            double klassen[] = new double[15];
            int nKlassen = 0;
            for (int i = 0; i < nEt; i++) {
                if (eklalt != et[i].ekl) {
                    klassen[nKlassen] = et[i].ekl;
                    eklalt = et[i].ekl;
                    nKlassen = nKlassen + 1;
                }
            }
// Höhen in der Tafel vorhanden
            int hoeheVorhanden = 0;
            for (int i = 0; i < nEt; i++) {
                if (et[i].h > 0.0) {
                    hoeheVorhanden = hoeheVorhanden + 1;
                }
            }
// weitere Bonitierung nur, wenn Hoehen und mindestens 2 Ekl
            if (nKlassen > 1 && hoeheVorhanden > 1) {
// Zeilen so sortieren, dass jeweils die ersten beiden die gesuchten sein werden
                for (int k = 0; k < nEt - 1; k++) {
                    for (int m = k + 1; m < nEt; m++) {
                        if (Math.abs(et[k].alter + 0.001 - alter) > Math.abs(et[m].alter + 0.001 - alter)) {
                            Ertragstafelzeile tempzeile = new Ertragstafelzeile(0, 0, 10.0, 0.0, 0.0, 0.0);
                            tempzeile = et[k];
                            et[k] = et[m];
                            et[m] = tempzeile;
                        }
                    }
                }
                // Raussuchen und inpolieren der Zeile für das jeweile Alter
                for (int i = 0; i < nKlassen; i++) {
                    int m = 0;
                    int merk1 = 0;
                    int merk2 = 0;
                    for (int j = 0; j < nEt; j++) {
                        if (et[j].ekl == klassen[i]) {
                            if (m == 0) {
                                merk1 = j;
                            }
                            if (m == 1) {
                                merk2 = j;
                            }
                            m = m + 1;
                        }
                    }
                    // Interpolieren der beiden gemerkten Zeilen
                    etx[i] = new Ertragstafelzeile(0, 0.0, 0.0, 0.0, 0.0, 0.0);
                    etx[i] = interpolationByAge(et[merk1], et[merk2], alter);
                }
// Nächsten beiden Zeilen für die Höhe heraussuchen
                for (int k = 0; k < nKlassen - 1; k++) {
                    for (int m = k + 1; m < nKlassen; m++) {
                        if (Math.abs(etx[k].h - suchH) > Math.abs(etx[m].h - suchH)) {
                            Ertragstafelzeile tempzeile = new Ertragstafelzeile(0, 0, 10.0, 0.0, 0.0, 0.0);
                            tempzeile = etx[k];
                            etx[k] = etx[m];
                            etx[m] = tempzeile;
                        }
                    }
                }
                etz = interpolationByH(etx[0], etx[1], suchH);
            } //Klassen und Hoehen vorhanden
            else {
                etz.ekl = -9999.0;
            }
        }
        else{
            etz.ekl = -9999.0;
            System.out.println("Keine Höhe verfügbar!");
        }
        // Check: Extrapolation mit negativer Grundfläche
        if(etz.gha < 0){
            etz.gha = 0.0;
            etz.ivha = 0.0;
            etz.vha = 0.0;

        }
        return etz;
    }

   
    private void setET(Species sp){
        // Ertragstafel festlegen
        datei = pfad+"\\Ertragstafeln\\ET"+sp.code+".xml";
        File test = new File(datei);
        if(!test.exists()){
            datei = pfad+"\\Ertragstafeln\\ET"+sp.spDef.handledLikeCode+".xml";
            if(sp.code>=410 && sp.code<420) datei = pfad+"\\Ertragstafeln\\ET411.xml";
        }
        test = new File(datei);
        if(!test.exists()){
            System.out.println("Keine Ertragstafel für Art "+sp.code+" verfügbar.");
        }
    }

    public Ertragstafelzeile interpolationByAge(Ertragstafelzeile z1, Ertragstafelzeile z2, int age){
        Ertragstafelzeile interpol = new Ertragstafelzeile(age,0.0,0.0,0.0,0.0,0.0);
        interpol.h= z1.h-(z1.h-z2.h)*((1.0*(z1.alter-age))/(1.0*(z1.alter-z2.alter)));
        interpol.gha= z1.gha-(z1.gha-z2.gha)*((1.0*(z1.alter-age))/(1.0*(z1.alter-z2.alter)));
        interpol.vha= z1.vha-(z1.vha-z2.vha)*((1.0*(z1.alter-age))/(1.0*(z1.alter-z2.alter)));
        interpol.ivha= z1.ivha-(z1.ivha-z2.ivha)*((1.0*(z1.alter-age))/(1.0*(z1.alter-z2.alter)));
        interpol.ekl=z1.ekl;

        return interpol;
    }
//
    public Ertragstafelzeile interpolationByH(Ertragstafelzeile z1, Ertragstafelzeile z2, double hoehe){
        Ertragstafelzeile interpol = new Ertragstafelzeile(0,0.0,hoehe,0.0,0.0,0.0);
        interpol.ekl= z1.ekl-(z1.ekl-z2.ekl)*((1.0*(z1.h-hoehe))/(1.0*(z1.h-z2.h)));
        interpol.gha= z1.gha-(z1.gha-z2.gha)*((1.0*(z1.h-hoehe))/(1.0*(z1.h-z2.h)));
        interpol.vha= z1.vha-(z1.vha-z2.vha)*((1.0*(z1.h-hoehe))/(1.0*(z1.h-z2.h)));
        interpol.ivha= z1.ivha-(z1.ivha-z2.ivha)*((1.0*(z1.h-hoehe))/(1.0*(z1.h-z2.h)));
        interpol.alter=z1.alter;

        return interpol;
    }
    
   

    
    

}
