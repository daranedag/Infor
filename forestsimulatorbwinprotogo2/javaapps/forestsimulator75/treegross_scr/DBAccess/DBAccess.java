/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package treegross.DBAccess;
import treegross.standsimulation.*;
import treegross.base.*;
import java.awt.*;

/**
 *
 * @author nagel
 */
public class DBAccess implements PlugInDBAccess {
    
public void startDialog(java.awt.Frame frame, Stand st, String dirx){
    String dir =dirx;
    System.out.println("starte Dialog");
    DBAccessDialog dialog = new DBAccessDialog(frame,true,st, dir);
    dialog.setVisible(true);
    
}    
    

}
