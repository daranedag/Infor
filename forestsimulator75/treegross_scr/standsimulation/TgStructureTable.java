/* http://www.nw-fva.de
   Version 07-11-2008

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
package treegross.standsimulation;
import treegross.base.*;
import java.io.*; 
import java.text.*;
import java.util.*;

class TgStructureTable {
   String filename ="structuretable.html";
   ResourceBundle messages;

   void TgStructureTable() {
   }   
   void writeTable(Stand st,String path, String fname, String preferredLanguage) {
         Locale currentLocale;
         StandStructure struc = new StandStructure();
         currentLocale = new Locale(preferredLanguage, "");
         messages = ResourceBundle.getBundle("treegross.standsimulation.TgStructureTable",currentLocale);
	  try {
		File file= new File(path, fname);
                filename=file.getCanonicalPath();
                OutputStream os=new FileOutputStream(filename); 
		PrintWriter out= new PrintWriter(
			new OutputStreamWriter(os)); 
		out.println("<HTML>"); 
		out.println("<H2><P align=center>"+messages.getString("stand_structure_table")+"</P align=center></H2> "); 
		out.println("<P><B>"+messages.getString("stand")+st.standname); 
		out.println("<BR>"+messages.getString("stand_size")+st.size); 
		out.println("<BR>"+messages.getString("year")+st.year+"</B></P>"); 
		String ss;
		char c=34;
         NumberFormat f=NumberFormat.getInstance();
	 f=NumberFormat.getInstance(new Locale("en","US"));
	 f.setMaximumFractionDigits(1);
	 f.setMinimumFractionDigits(1);
	 ss=String.valueOf(c);
	 out.println("<HR>"); 
         out.println("<TABLE BORDER>");
// Species Richness         
         out.println("<TR><TD><FONT SIZE=2>"+messages.getString("numberOfSpecies")+
                     "<TD><FONT SIZE=2>"+st.nspecies+ "</TR>");
// Shannon Index         
         out.println("<TR><TD><FONT SIZE=2>"+messages.getString("shannon")+
                     "<TD><FONT SIZE=2>"+f.format(struc.shannon(st))+ "</TR>");
// Species Profil         
         out.println("<TR><TD><FONT SIZE=2>"+messages.getString("profil")+
                     "<TD><FONT SIZE=2>"+f.format(struc.a_index(st))+ "</TR>");
// Shannon Index         
         out.println("<TR><TD><FONT SIZE=2>"+messages.getString("th")+
                     "<TD><FONT SIZE=2>"+f.format(struc.th(st))+ "</TR>");
// Shannon Index         
         out.println("<TR><TD><FONT SIZE=2>"+messages.getString("td")+
                     "<TD><FONT SIZE=2>"+f.format(struc.td(st))+ "</TR>");
// Shannon Index         
         out.println("<TR><TD><FONT SIZE=2>"+messages.getString("tart")+
                     "<TD><FONT SIZE=2>"+f.format(struc.tart(st))+ "</TR>");
      out.println("</HR></TABLE>"); 
      out.println("<BR>"+messages.getString("created")+st.modelRegion+"</BR></HTML>"); 
	   out.close();
	   }
	   catch (Exception e) {	System.out.println(e); 	}   
 
     }
   public String getFilename() {
       return filename;
   }

}

