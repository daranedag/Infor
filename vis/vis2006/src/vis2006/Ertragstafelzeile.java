/*
 * Zeile.java
 *
 * Created on 12. Dezember 2006, 11:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 *
 * @author J.Nagel
 */
package vis2006;


public class Ertragstafelzeile {
    int alter;
    double ekl;
    double h;
    double gha;
    double vha;
    double ivha;
    
    /** Creates a new instance of Zeile */
    public Ertragstafelzeile(int Alter, double Ekl, double H,double G, double V, double IV) {
        alter =Alter;
        ekl =Ekl;
        h = H;
        gha = G;
        vha = V;
        ivha = IV;
    }

    public Integer getAlter(){
        Integer w=alter;
        return w;
    }
    public Double getEkl(){
        Double w=Math.round(ekl*10.0)/10.0;
        return w;
    }
    public Double getH(){
        if (h < 0.0) h=0.0;
        Double w=Math.round(h*10.0)/10.0;
        return w;
    }
    public Double getGha(){
        if (gha < 0.0) gha = 0.0;
        Double w=Math.round(gha*10.0)/10.0;
        return w;
    }
    public Double getVha(){
        if (vha < 0.0) vha = 0.0;
        Double w=Math.round(vha*10.0)/10.0;
        return w;
    }
    public Double getIvha(){
        if (ivha < 0.0) ivha = 0.0;
        Double w=Math.round(ivha*10.0)/10.0;
        return w;
    }
    
}
