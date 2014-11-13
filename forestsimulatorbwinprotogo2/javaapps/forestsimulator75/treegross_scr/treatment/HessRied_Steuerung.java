/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package treegross.treatment;

import treegross.base.Stand;
import treegross.base.Tree;

/**
 *
 * @author nagel
 */
public class HessRied_Steuerung {


    public int setTreatment(Stand st, int naehrstoffe, int feuchte){
        Treatment2 tr = new Treatment2();
// In der Variable Error wird die Anzahl der Fehler gezählt
        int error = -1;
// Festlegen des aktuellen WET
        HessRied_BZT_Regel bztRegel = new HessRied_BZT_Regel();
        int wetIst=bztRegel.getWET(st);
        if (wetIst==-1) error=error +1;

        // Festlegen des gewünschten WET
        int wetSoll = bztRegel.selectWETbySite(naehrstoffe, feuchte, wetIst);
        if (wetIst==-1) error=error +1;

        System.out.println("wetsoll: "+wetSoll+"  wetIst: "+wetIst);

//
//  Einstellen der Trules, dieses muss erfolgen, wenn der Bestand voellig aufgebaut ist
//
//  Festlegen der Waldbauregel
//
        HessRied_Waldbau hrWaldbau = new HessRied_Waldbau();
        int art1 = bztRegel.getArt1(st);
        hrWaldbau.setHauptart(art1, st.h100);
//
//      Nicht vorhandene Baumarten einrichten
//
        String pflStr= bztRegel.getMixturebyWET(wetSoll);
        int art=211;
        while (pflStr.indexOf(";") > 0){
            int m = pflStr.indexOf("[");
            art = Integer.parseInt(pflStr.substring(0, m).trim());
            pflStr = pflStr.substring(m+1, pflStr.length());
            m = pflStr.indexOf("]");
            double ha = Double.parseDouble(pflStr.substring(0, m).trim());
            pflStr = pflStr.substring(m+1, pflStr.length());
            m = pflStr.indexOf(";");
            pflStr = pflStr.substring(m+1, pflStr.length());
            boolean artDa = true;
            for (int j=0;j < st.nspecies;j++)
                if (art==st.sp[j].code) artDa=false;
            Tree atree = new Tree();
            atree.code=art;
            try { st.addspecies(atree);
            } catch(Exception e){ System.out.println(e);}

          }
//
//   Baumartensettings überschreiben
//
        String mixstr = bztRegel.getMixturebyWET(wetSoll);
        String zsstr = bztRegel.getZielstbyWET(wetIst);
        for (int i=0; i < st.nspecies;i++){
            st.sp[i].spDef.moderateThinning = hrWaldbau.getThinning(st.sp[i].code);
            String spstr = new Integer(st.sp[i].code).toString();
            int m = mixstr.indexOf(spstr);
            if (m > -1 ) {
                String hstr = mixstr.substring(m+4);
                m = hstr.indexOf("]");
                if (m > -1) {
                    st.sp[i].trule.targetCrownPercent=100.0*Double.parseDouble(hstr.substring(0, m));
                }
//                st.sp[i].hbon=40.0;
            }
            m = zsstr.indexOf(spstr);
            if (m > -1 ) {
                String hstr = zsstr.substring(m+4);
                m = hstr.indexOf("]");
                if (m > -1) {
                    st.sp[i].trule.targetDiameter=Double.parseDouble(hstr.substring(0, m));
                }
            }
            st.sp[i].trule.numberCropTreesWanted=(int) Math.round(hrWaldbau.getZBaeumeHa()*st.sp[i].trule.targetCrownPercent/100.0);
            st.sp[i].spDef.moderateThinning = hrWaldbau.getThinning(st.sp[i].code);
//            System.out.println("Z-Bäume: "+st.sp[i].code+"   "+st.sp[i].trule.numberCropTreesWanted);

        }



//  a ) Festlegen der Rückgassen, im Regelset aus der Hauptart und Höhe des aktuellen WET
        if (art1 <500 ) tr.setSkidTrails(st, hrWaldbau.getRueckegasse(), 40.0, 4.0);
        else tr.setSkidTrails(st, hrWaldbau.getRueckegasse(), 20.0, 4.0);
//
//  b ) Festlegen der Durchforstung;  moderateThinningfactors s. oben
        tr.setThinningRegime(st,0,1.0,hrWaldbau.getMinThinningV(), hrWaldbau.getMaxThinningV(), false);
//
// Ernteeinstellungen vornehmen: min und max ist vorgegeben.
//
        HessRied_Nutzung hrNutzung = new HessRied_Nutzung();
        int nutzungstyp = hrNutzung.getNutzungstyp(st, wetSoll);
        tr.setHarvestRegime(st, nutzungstyp, hrNutzung.getMinHarvest(), hrNutzung.getMaxHarvest(), 0.3, hrNutzung.getVerjuengungsgang());
//        System.out.println("Nutzungstyp vjg: "+nutzungstyp+"   "+hrNutzung.getVerjuengungsgang());
//
//      Pflanzung festlegen, Kritischer Wert und Pflanzstring
//
        double nx = hrWaldbau.kritKronenschluss;
        tr.setAutoPlanting(st, true, true,hrWaldbau.kritKronenschluss , bztRegel.getMixturebyWET(wetSoll));
//        System.out.println("Pflanzung: "+bztRegel.getMixturebyWET(wetSoll));
      
        return error ;
    }

}
