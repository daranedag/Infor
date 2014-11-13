/* http://www.nw-fva.de
   Version 20-2-2009

   (c) 2002 Juergen Nagel, Northwest German Forest Research Station, 
       Grätzelstr.2, 37079 Göttingen, Germany
       E-Mail: Juergen.Nagel@nw-fva.de
 
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT  WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
 */
package treegross.harvesting;
import java.io.*;
import java.io.IOException.*;

/**
 * Class for an easy browser start in Windows and Ubuntu Linux
 * @author J. Nagel
 */
public class StartBrowser {
   static String url;
//start local HTML Page
/** Starts the browser and displays the file given in urlx
 *
 * @param urlx = url of the file to display
 */
public StartBrowser( String urlx ) {
   url = urlx;
   start();

}
/** Starts your browser and displays the file in url
 */
public void start() {
    try {
       String trenn =System.getProperty("file.separator"); 
       if (trenn.indexOf("/") < 0 ) {
          Runtime.getRuntime().exec( "rundll32 url.dll,FileProtocolHandler "+ url );
       } else {
          Runtime.getRuntime().exec("firefox " + url);
       }
    } catch (IOException ioe) {
    ioe.printStackTrace();
     }
 System.out.println("StartBrowser");
}
}
