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
import java.text.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

class TgStandInfo extends JPanel // implements ActionListener
{     
      int nrow=0; 
      javax.swing.JTable jTable1 = new javax.swing.JTable();
      javax.swing.table.DefaultTableModel yieldTable;
      Object[] rowData={" "," "," "," "," "," "," "," "," "," "," "," "," "," "};
 
   public TgStandInfo(String preferredLanguage) {
     Locale currentLocale;
     ResourceBundle messages;
     currentLocale = new Locale(preferredLanguage, "");
     messages = ResourceBundle.getBundle("treegross.standsimulation.TgStandInfo",currentLocale);
    
     yieldTable= new javax.swing.table.DefaultTableModel(
            new Object [][] {  },
            new String [] {
                messages.getString("sp"),messages.getString("Age"),messages.getString("Dg"),
                messages.getString("Hg"),messages.getString("D100"),messages.getString("H100"),
                messages.getString("nha"),messages.getString("gha"),messages.getString("vha"),
                messages.getString("noutha"),messages.getString("goutha"),
                messages.getString("voutha"),messages.getString("mix")
            }
        );
     
      JScrollPane spane = new JScrollPane();
      jTable1.setModel(yieldTable);
      
      Dimension scr= Toolkit.getDefaultToolkit().getScreenSize();             
      spane.setPreferredSize(new Dimension((scr.width-scr.width/50), (scr.height/4)));       
      spane.getViewport().add(jTable1);      
      
      setLayout(new BorderLayout());
      add(spane, BorderLayout.CENTER);
//
//      yieldTable.
      
      }
   void formUpdate(Stand st) {
           
           
	   NumberFormat f=NumberFormat.getInstance();
	   f=NumberFormat.getInstance(new Locale("en","US"));
	   f.setMaximumFractionDigits(1);
	   f.setMinimumFractionDigits(1);
           f.setGroupingUsed(false);
           
           double sum_nha=0.0;
           double sum_gha=0.0;
           double sum_vol=0.0;
           double sum_nhaout=0.0;
           double sum_ghaout=0.0;
           double sum_vhaout=0.0;
           for (int j=nrow; j>0; j--) yieldTable.removeRow(j-1);
           nrow=0;
           for (int i=0;i<st.nspecies;i++){
               yieldTable.addRow(rowData);
               jTable1.setValueAt(st.sp[i].spDef.shortName,nrow,0);
               jTable1.setValueAt(f.format(st.sp[i].dg),nrow,2);
               jTable1.setValueAt(f.format(st.sp[i].h100age),nrow,1);
               jTable1.setValueAt(f.format(st.sp[i].hg),nrow,3);
               jTable1.setValueAt(f.format(st.sp[i].d100),nrow,4);
               jTable1.setValueAt(f.format(st.sp[i].h100),nrow,5);
               jTable1.setValueAt(f.format(st.sp[i].nha),nrow,6);
               jTable1.setValueAt(f.format(st.sp[i].gha),nrow,7);
               jTable1.setValueAt(f.format(st.sp[i].vol),nrow,8);
               jTable1.setValueAt(f.format(st.sp[i].nhaout),nrow,9);
               jTable1.setValueAt(f.format(st.sp[i].ghaout),nrow,10);
               jTable1.setValueAt(f.format(st.sp[i].vhaout),nrow,11);
               jTable1.setValueAt(f.format(st.sp[i].percBA),nrow,12);
               sum_nha=sum_nha+st.sp[i].nha;
               sum_gha=sum_gha+st.sp[i].gha;
               sum_vol=sum_vol+st.sp[i].vol;
               sum_nhaout=sum_nhaout+st.sp[i].nhaout;
               sum_ghaout=sum_ghaout+st.sp[i].ghaout;
               sum_vhaout=sum_vhaout+st.sp[i].vhaout;
               nrow=nrow+1;
               
 
           }
           
           yieldTable.addRow(rowData);
           jTable1.setValueAt("Sum",nrow,0);          
           jTable1.setValueAt(f.format(sum_nha),nrow,6);
           jTable1.setValueAt(f.format(sum_gha),nrow,7);
           jTable1.setValueAt(f.format(sum_vol),nrow,8);
           jTable1.setValueAt(f.format(sum_nhaout),nrow,9);
           jTable1.setValueAt(f.format(sum_ghaout),nrow,10);
           jTable1.setValueAt(f.format(sum_vhaout),nrow,11);
           jTable1.setValueAt(f.format(st.degreeOfDensity),nrow,12);
           
    } 
          
      
}      

