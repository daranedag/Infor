/*
 * HessenArten.java
 *
 * Created on 4. April 2006, 10:33
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package vis2006;
import java.io.*;
import java.util.*;

/**
 *
 * @author nagel
 */
public class HessenArten {
    
    int nArten=0;
    HessenArt hArt[] = new HessenArt[100] ;
    
    /** Creates a new instance of HessenArten */
    public HessenArten() {
       try{
         BufferedReader in=
         new BufferedReader(
         new InputStreamReader(
         new FileInputStream("HessenArten.txt")));
         StringTokenizer stx;
         String delim=";";
         nArten=0;
         while (true){
            String s=in.readLine();
            if (s == null || s.length() < 5) break;
            stx = new StringTokenizer(s,delim);
            hArt[nArten] = new HessenArt();
            hArt[nArten].art=stx.nextToken();
            hArt[nArten].code=Integer.parseInt(stx.nextToken());
            nArten=nArten+1;
         }

         in.close();
        }
        catch (Exception e) {System.out.println("Error en Tipos Hesse"+e); }   
    }
    
    public int getCode(String baumArt){
        int ndsCode=0;
        for (int i=0;i<nArten;i++)
            if (hArt[i].art.compareTo(baumArt)==0) ndsCode=hArt[i].code;
        return ndsCode;
    }
    
    
}

