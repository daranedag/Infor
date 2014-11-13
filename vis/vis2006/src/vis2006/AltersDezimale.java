/*
 * AltersDezimale.java
 *
 * Created on 23. März 2006, 11:43
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package vis2006;
/**
 *
 * @author nagel
 */
public class AltersDezimale {
    
    /** Creates a new instance of AltersDezimale */
    public AltersDezimale() {
    }
    public double getAltersdezimale(int monat){
            double dezimal=0.0;
            if (monat == 5) dezimal=0.25;
            if (monat == 6) dezimal=0.5;
            if (monat == 7) dezimal=0.75;
            if (monat >= 8) dezimal=1.0;
            return dezimal;

    }

}
