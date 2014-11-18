/* http://www.nw-fva.de
   Version 25-02-2009

   (c) 2002 Juergen Nagel, Northwest German Forest Research Station, 
       Gr�tzelstr.2, 37079 G�ttingen, Germany
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
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.ProcessingInstruction;
import org.jdom.output.XMLOutputter;
import org.jdom.input.*;
import org.jdom.DocType;
import java.util.*;
import java.text.*;
import javax.swing.*;
import java.net.*;
import treegross.base.*;
import java.io.*;


/**
 *
 * @author  nagel
 */
public class DeadwoodPanel extends javax.swing.JPanel {
    
    LoggingSortiment ls[] = new LoggingSortiment[500];  
    int nls=0;
    static Document doc;
    static Element rootElt;
//    String urlString ="file:///C://Dokumente und Einstellungen//nagel//Eigene Dateien//jnProgramme//TreeGrOSSLogging//";
    String urlString ="";
    boolean combo5Active = true;
    Stand st = null;
    String urlcodebase = "";
    javax.swing.DefaultListModel listModel = new javax.swing.DefaultListModel();
    int nlist = 0;
    boolean neuspeichern = false;
    String fname="";
    String proDir;
    boolean dialogActive = true;
    String workDir = "";
    int timeframe = 0;
    double fellingHeight=0.0;
    
    /** Creates new form LoggingPanel */
    public DeadwoodPanel(Stand stand, String programDir, boolean interActive, String workingDir) {
        initComponents();
        dialogActive = interActive;
        jPanel8.setVisible(interActive);
        proDir=programDir;
        workDir = workingDir;
        int m = programDir.toUpperCase().indexOf("FILE");
        int m2 = programDir.toUpperCase().indexOf("HTTP");
        String fname=programDir+System.getProperty("file.separator")+"DeadwoodSortiments.xml";
        if ( m < 0 && m2 <0 ) fname="file:"+System.getProperty("file.separator")+System.getProperty("file.separator")+System.getProperty("file.separator")+fname;

        try{
          URL url = new URL(fname);
          loadls(url);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        st = stand;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jCheckBox3 = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jComboBox4 = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel15 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(608, 221));
        setRequestFocusEnabled(false);
        setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(255, 255, 204));
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel13.setText("TreeGrOSS xml Datei :");
        jPanel8.add(jLabel13);

        jTextField13.setPreferredSize(new java.awt.Dimension(511, 19));
        jPanel8.add(jTextField13);

        jButton5.setText("laden");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton5);

        add(jPanel8, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jPanel10.add(jLabel16);

        jPanel1.add(jPanel10, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.GridLayout(4, 0));

        jPanel4.setLayout(new java.awt.GridLayout(2, 1));

        jPanel9.setLayout(new java.awt.GridLayout(1, 8));

        jLabel1.setText("Name");
        jPanel9.add(jLabel1);

        jTextField1.setText("Name");
        jPanel9.add(jTextField1);

        jLabel2.setText("Art von");
        jPanel9.add(jLabel2);

        jTextField2.setText("211");
        jPanel9.add(jTextField2);

        jLabel3.setText("Art bis");
        jPanel9.add(jLabel3);

        jTextField3.setText("499");
        jPanel9.add(jTextField3);

        jPanel4.add(jPanel9);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Entnahme, wenn nein dann Totholz");
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel12.add(jCheckBox1);

        jPanel4.add(jPanel12);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.GridLayout(2, 8));

        jLabel4.setText("min D");
        jPanel5.add(jLabel4);

        jLabel6.setText("max D");
        jPanel5.add(jLabel6);

        jLabel7.setText("min Zopf");
        jPanel5.add(jLabel7);

        jLabel8.setText("max Zopf");
        jPanel5.add(jLabel8);

        jLabel9.setText("min L");
        jPanel5.add(jLabel9);

        jLabel5.setText("max L");
        jPanel5.add(jLabel5);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Zugabe %", "Zugabe cm" }));
        jPanel5.add(jComboBox1);
        jPanel5.add(jPanel7);

        jTextField4.setText("7.0");
        jPanel5.add(jTextField4);

        jTextField5.setText("199.9");
        jPanel5.add(jTextField5);

        jTextField6.setText("7.0");
        jPanel5.add(jTextField6);

        jTextField7.setText("199.9");
        jPanel5.add(jTextField7);

        jTextField8.setText("3.0");
        jPanel5.add(jTextField8);

        jTextField9.setText("18.0");
        jPanel5.add(jTextField9);

        jTextField10.setText("0.0");
        jPanel5.add(jTextField10);

        jCheckBox3.setText("bisKA");
        jCheckBox3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel5.add(jCheckBox3);

        jPanel2.add(jPanel5);

        jPanel6.setLayout(new java.awt.GridLayout(2, 4));

        jLabel10.setText("Wertigkeit");
        jPanel6.add(jLabel10);

        jLabel11.setText("Preis");
        jPanel6.add(jLabel11);

        jLabel12.setText("Prozent der B�ume");
        jPanel6.add(jLabel12);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "alle B�ume", "Z- B�ume" }));
        jPanel6.add(jComboBox3);

        jTextField11.setText("10.0");
        jPanel6.add(jTextField11);

        jTextField12.setText("50.0");
        jPanel6.add(jTextField12);

        jTextField14.setText("100.0");
        jPanel6.add(jTextField14);

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "mehrfach", "einfach" }));
        jPanel6.add(jComboBox4);

        jPanel2.add(jPanel6);

        jButton3.setText("neu ");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3);

        jButton2.setText("speichern");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2);

        jButton4.setText("l�schen");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton4);

        jPanel2.add(jPanel3);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel11.setPreferredSize(new java.awt.Dimension(140, 144));
        jPanel11.setLayout(new java.awt.BorderLayout());

        jList1.setModel(listModel);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        jPanel11.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jLabel15.setText("Sortimente");
        jPanel11.add(jLabel15, java.awt.BorderLayout.NORTH);

        jPanel1.add(jPanel11, java.awt.BorderLayout.WEST);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
       
// TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
// TODO add your handling code here:
       int m = jList1.getSelectedIndex(); //get SelectedIndex from List
       if (m > -1) {
           jTextField1.setText(ls[m].name);
           jTextField2.setText(new Integer(ls[m].artvon).toString());
           jTextField3.setText(new Integer(ls[m].artbis).toString());
           jTextField4.setText(new Double(ls[m].minD).toString());
           jTextField5.setText(new Double(ls[m].maxD).toString());
           jTextField6.setText(new Double(ls[m].minTop).toString());
           jTextField7.setText(new Double(ls[m].maxTop).toString());
           jTextField8.setText(new Double(ls[m].minH).toString());
           jTextField9.setText(new Double(ls[m].maxH).toString());
           if (ls[m].zugabeProzent > 0) jComboBox1.setSelectedIndex(0);
           else jComboBox1.setSelectedIndex(1);
           if (jComboBox1.getSelectedIndex()==0) jTextField10.setText(new Double(ls[m].zugabeProzent).toString());
           else jTextField10.setText(new Double(ls[m].zugabeCm).toString());
           jTextField11.setText(new Double(ls[m].gewicht).toString());
           jTextField12.setText(new Double(ls[m].preis).toString());
           jTextField14.setText(new Double(ls[m].wahrscheinlich).toString());
           if (ls[m].nurZBaum) jComboBox3.setSelectedIndex(1);
           else jComboBox3.setSelectedIndex(0);
           if (ls[m].mehrfach) jComboBox4.setSelectedIndex(0);
           else jComboBox4.setSelectedIndex(1);
           jCheckBox1.setSelected(ls[m].entnahme);
           jCheckBox3.setSelected(ls[m].bisKronenansatz);
       }
    }//GEN-LAST:event_jList1ValueChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int m = jList1.getSelectedIndex();
        for (int i=m+1;i< nls;i++){
            ls[i-1]=ls[i];
        }
        nls = nls -1;
        nlist = nlist - 1;
        
        savels(proDir+"//loggingSortiment.xml");
        nlist = 0;
        
        int m1 = proDir.toUpperCase().indexOf("FILE");
        int m2 = proDir.toUpperCase().indexOf("HTTP");
        String fname=proDir+System.getProperty("file.separator")+"loggingSortiment.xml";
        if ( m1 < 0 && m2 <0 ) fname="file:"+System.getProperty("file.separator")+System.getProperty("file.separator")+System.getProperty("file.separator")+fname;

        listModel.removeAllElements();
        try{
          URL url = new URL(fname);
          loadls(url);
        }
        catch (IOException e){
            e.printStackTrace();
        }



// TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
           jTextField1.setText(" ");
           jTextField2.setText("100");
           jTextField3.setText("999");
           jTextField4.setText("7.0");
           jTextField5.setText("99.9");
           jTextField6.setText("7.0");
           jTextField7.setText("99.9");
           jTextField8.setText("1.0");
           jTextField9.setText("18.0");
           jComboBox1.setSelectedIndex(0);
           jTextField10.setText("0.0");
           jTextField11.setText("50");
           jTextField12.setText("50.0");
           jTextField14.setText("100.0");
           jComboBox3.setSelectedIndex(0);
           jComboBox4.setSelectedIndex(1);
           jCheckBox1.setSelected(true);
           neuspeichern = true;
        
/*        double zugabepro = 0.0;
        double zugabeabs = 0.0;
        if (jComboBox1.getSelectedIndex()==0) zugabepro=Double.parseDouble(jTextField10.getText());
         else zugabeabs=Double.parseDouble(jTextField10.getText());
        boolean zb = true;
        if (jComboBox3.getSelectedIndex()==0) zb=false;
        boolean mehrf = true;
        if (jComboBox4.getSelectedIndex() > 0) mehrf=false;
        
        ls[nls]= new LoggingSortiment(jTextField1.getText(),
                Integer.parseInt(jTextField2.getText()),Integer.parseInt(jTextField3.getText()),
                    Double.parseDouble(jTextField4.getText()),Double.parseDouble(jTextField5.getText()),
                    Double.parseDouble(jTextField6.getText()),Double.parseDouble(jTextField7.getText()),
                    Double.parseDouble(jTextField8.getText()),Double.parseDouble(jTextField9.getText()),
                    zugabepro, zugabeabs,Double.parseDouble(jTextField12.getText()),
                    Double.parseDouble(jTextField11.getText()),Double.parseDouble(jTextField14.getText()),
                    zb, mehrf , jCheckBox1.isSelected(), jCheckBox3.isSelected(), true,
                    nls  );
        nls = nls+1;
        System.out.println("Array L�nge "+ls.length);
        savels("C://Dokumente und Einstellungen//nagel//Eigene Dateien//jnProgramme//TreeGrOSSLogging//loggingSortiment.xml");
        try{
          URL url = new URL(urlString+"loggingSortiment.xml");
          loadls(url);
        }
        catch (IOException e){
            e.printStackTrace();
        }
  */   
        

// TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        double zugabepro = 0.0;
        double zugabeabs = 0.0;
        if (jComboBox1.getSelectedIndex()==0) zugabepro=Double.parseDouble(jTextField10.getText());
        else zugabeabs=Double.parseDouble(jTextField10.getText());
        boolean zb = true;
        if (jComboBox3.getSelectedIndex()==0) zb=false;
        boolean mehrf = true;
        if (jComboBox4.getSelectedIndex() > 0) mehrf=false;
        int m = nlist;
        if (neuspeichern == false) m = jList1.getSelectedIndex();
        ls[m]= new LoggingSortiment(jTextField1.getText(),
                Integer.parseInt(jTextField2.getText()),Integer.parseInt(jTextField3.getText()),
                    Double.parseDouble(jTextField4.getText()),Double.parseDouble(jTextField5.getText()),
                    Double.parseDouble(jTextField6.getText()),Double.parseDouble(jTextField7.getText()),
                    Double.parseDouble(jTextField8.getText()),Double.parseDouble(jTextField9.getText()),
                    zugabepro, zugabeabs,Double.parseDouble(jTextField12.getText()),
                    Double.parseDouble(jTextField11.getText()),Double.parseDouble(jTextField14.getText()),
                    zb, mehrf , jCheckBox1.isSelected(), jCheckBox3.isSelected(), true,
                    nls,0  );
        if (neuspeichern) {
            listModel.addElement((String) ls[nlist].name);
            nlist = nlist + 1;
        }
        else { listModel.setElementAt((String) ls[m].name,m);}
        neuspeichern = false;
        nls = nlist;
        System.out.println("Array L�nge "+ls.length);
        savels(proDir+System.getProperty("file.separator")+"DeadwoodSortiments.xml");
        
    }//GEN-LAST:event_jButton2ActionPerformed
    
    public void savels(String fn){
/*       JFileChooser fc = new JFileChooser();
       TxtFileFilter txtFilter = new TxtFileFilter();
       txtFilter.setExtension("xml");
       fc.addChoosableFileFilter(txtFilter);
       int auswahl = fc.showOpenDialog(this);
       String pa= fc.getSelectedFile().getPath();
       String dn= fc.getSelectedFile().getName();
*/
       NumberFormat f=NumberFormat.getInstance();
       f=NumberFormat.getInstance(new Locale("en","US"));
       f.setMaximumFractionDigits(2);
       f.setMinimumFractionDigits(2);
       f.setGroupingUsed(false);
       Element elt;
       Element elt2;
/** Creates an Treegross xml */
       Document doc = new Document();
       rootElt = new Element("Sortimente");
       ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet",
                 "type=\"text/xsl\" href=\"loggingSortimente.xsl\"");
       doc.addContent(pi);
       doc.setRootElement(rootElt);
         
//         
/* Alle Sortimente */;
        for (int i=0;i< nlist;i++){
            elt = new Element("Sortiment");
            elt = addString(elt, "Id", new Integer(i).toString());
            elt = addString(elt, "Name",ls[i].name);
            elt = addString(elt, "Art_von", new Integer(ls[i].artvon).toString());
            elt = addString(elt, "Art_bis", new Integer(ls[i].artbis).toString());
            elt = addString(elt, "minD",f.format(ls[i].minD));
            elt = addString(elt, "maxD",f.format(ls[i].maxD));
            elt = addString(elt, "minTop",f.format(ls[i].minTop));
            elt = addString(elt, "maxTop",f.format(ls[i].maxTop));
            elt = addString(elt, "minH",f.format(ls[i].minH));
            elt = addString(elt, "maxH",f.format(ls[i].maxH));
            elt = addString(elt, "ZugabeProzent",f.format(ls[i].zugabeProzent));
            elt = addString(elt, "ZugabeCm",f.format(ls[i].zugabeCm));
            elt = addString(elt, "Preis",f.format(ls[i].preis));
            elt = addString(elt, "Gewicht",f.format(ls[i].gewicht));
            elt = addString(elt, "Wahrscheinlichkeit",f.format(ls[i].wahrscheinlich));
            elt = addString(elt, "nurZBaum",new Boolean(ls[i].nurZBaum).toString());
            elt = addString(elt, "mehrfach",new Boolean(ls[i].mehrfach).toString());
            elt = addString(elt, "Entnahme",new Boolean(ls[i].entnahme).toString());
            elt = addString(elt, "bisKA",new Boolean(ls[i].bisKronenansatz).toString());
            elt = addString(elt, "ausgewaehlt",new Boolean(ls[i].ausgewaehlt).toString());
            rootElt.addContent(elt);
        }
/* Abspeichern des doc */
        try {
            File file = new File(fn);
            FileOutputStream result = new FileOutputStream(file);
            XMLOutputter outputter = new XMLOutputter();
//            outputter.setNewlines(true);
//            outputter.setIndent("  ");
            outputter.output(doc,result);
                        
        }
        catch (IOException e){
            e.printStackTrace();
        }
   

    }
     Element addString(Element elt, String variable, String text){
            Element var = new Element(variable);
            var.addContent(text);  
            elt.addContent(var);
            return elt;
    }

    public void calculate(){
               String pa="";
       String dn="";
       if (dialogActive){
           JFileChooser fc = new JFileChooser();
//       TxtFileFilter txtFilter = new TxtFileFilter();
//       txtFilter.setExtension("xml");
//     fc.addChoosableFileFilter(txtFilter);

           int auswahl = fc.showOpenDialog(this);
           pa= fc.getSelectedFile().getPath();
           dn= fc.getSelectedFile().getName();
       }
        else
            pa = workDir+System.getProperty("file.separator")+"deadwoodreport.xml";
// ausgew�hlte markieren
       for(int i=0; i<nlist; i++) ls[i].ausgewaehlt=false;
       int[] indices = jList1.getSelectedIndices(); //get Selected Assortments from list
       for(int i=0; i<indices.length; i++)ls[indices[i]].ausgewaehlt=true;


//
       NumberFormat f=NumberFormat.getInstance();
       f=NumberFormat.getInstance(new Locale("en","US"));
       f.setMaximumFractionDigits(2);
       f.setMinimumFractionDigits(2);
       f.setGroupingUsed(false);
       Element elt;
       Element elt2;
       Element elt3;
/** Find starting year for report */
       int startyear=st.year;
       for (int i=0;i<st.ntrees;i++)
           if (st.tr[i].out > -1 && st.tr[i].out < startyear) startyear=st.tr[i].out;
/** Creates an Treegross xml */
       Document doc = new Document();
       rootElt = new Element("Totholz");
       ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet",
                 "type=\"text/xsl\" href=\"deadwoodreport.xsl\"");
       doc.addContent(pi);
       doc.setRootElement(rootElt);
//
//	    System.out.println("Neuen Bericht erzeugen nach try");
/** all data is writen in File info/treelist.html */
       try {
// TimeFarme

// Sortimente nach xml
       for (int j=0; j< nls; j++)
           if (ls[j].ausgewaehlt) {
            elt = new Element("Sortiment_gesucht");
            elt = addString(elt, "Code", ls[j].name);
            elt = addString(elt, "Art_von", new Integer(ls[j].artvon).toString());
            elt = addString(elt, "Art_bis", new Integer(ls[j].artbis).toString());
            elt = addString(elt, "L_min", f.format(ls[j].minH));
            elt = addString(elt, "L_min", f.format(ls[j].minH));
            elt = addString(elt, "L_max", f.format(ls[j].maxH));
            elt = addString(elt, "D_min", f.format(ls[j].minD));
            elt = addString(elt, "D_max", f.format(ls[j].maxD));
            elt = addString(elt, "T_min", f.format(ls[j].minTop));
            elt = addString(elt, "T_max", f.format(ls[j].maxTop));
            String entn = "false";
            if(ls[j].entnahme) entn="true";
            elt = addString(elt, "removed", entn );
            rootElt.addContent(elt);

           }

// Sortierung
// Logging Sortimente nach ausgewaehlt bzw. nicht sortieren
        LoggingSortiment temp = new LoggingSortiment();
        for (int i=0;i< nls-1;i++)
           for (int j=i+1;j< nls;j++)
               if (ls[j].ausgewaehlt) {
                   temp=ls[i];
                   ls[i]=ls[j];
                   ls[j]=temp;
               }
// Festellen der Anzahl ausgewaehlter ls
         int nausgewaehlt = 0;
         for (int i=0;i< nls;i++) if (ls[i].ausgewaehlt) nausgewaehlt = nausgewaehlt +1;
//         System.out.println("ausgew�hlt: "+nausgewaehlt);
// ausgewaehlte Logging Sortimente nach Gewichtung sortieren
        for (int i=0;i< nausgewaehlt-1;i++)
           for (int j=i+1;j< nausgewaehlt;j++)
               if (ls[j].gewicht > ls[i].gewicht) {
                   temp=ls[i];
                   ls[i]=ls[j];
                   ls[j]=temp;
               }
// Sortierung
        JSortiererNFV sortierer = new JSortiererNFV(st.sp[0].spDef.taperFunctionXML);
// Taperfunction set
        for (int k=0;k<st.nspecies;k++) {
            st.sp[k].spDef.taperFunction=sortierer.getFunNumber(st.sp[k].code);
        }

            double sumv=0.0;
            double sumvt =0.0;
            double sumvn =0.0;

      while (startyear <= st.year ){
       for (int k=0;k<st.nspecies;k++) {
            double volumenStubben=0.0;
            double volumenTotholz = 0.0;
            double volumenNaturschutz = 0.0;
            double volumenStehend =0.0;
            double volumenGesamt = 0.0;


            for (int i=0;i<st.ntrees;i++)
                if (st.sp[k].code == st.tr[i].code && st.tr[i].out <= startyear && st.tr[i].out >=0  ) {
// use stem volume function
               double vol =0.0;
               FunctionInterpreter fi = new FunctionInterpreter();
               if (st.tr[i].sp.spDef.stemVolumeFunctionXML.length()>5)
                   vol=fi.getValueForTree(st.tr[i], st.tr[i].sp.spDef.stemVolumeFunctionXML);
                 else vol = st.tr[i].v;

//               double zerfall = (1.0-(Math.pow((1.0-Math.exp(-0.0658*(startyear-st.tr[i].out))),2.2529 )));
// Get zerfall or decay from Function
               Tree atree = new Tree();
               atree = st.tr[i].clone();
               atree.sp = st.tr[i].sp;
               atree.age = startyear-st.tr[i].out;
               atree.st = st;
               double zerfall=fi.getValueForTree(atree, st.tr[i].sp.spDef.decayXML);

               volumenGesamt = volumenGesamt+vol*zerfall*st.tr[i].fac/st.size;

// Tree of mortality are treated as standing volume until a certain age which is
// determined by the possibility that they might have fallen
               if (startyear <= st.tr[i].out && st.tr[i].outtype ==1 ) st.tr[i].outtype =0;
               double bruchwahrscheinlichkeit = 0.0;
               if (startyear-st.tr[i].out > 1 && st.tr[i].outtype == 0 ) bruchwahrscheinlichkeit= 1.0-Math.exp(-Math.pow(((startyear-st.tr[i].out-1.0)/19.654),2.875));
               if (bruchwahrscheinlichkeit > Math.random() && st.tr[i].outtype == 0) st.tr[i].outtype =1;



               if ( st.tr[i].d > 20 && st.tr[i].outtype < 2 && st.tr[i].out>0 ){
                   if (st.tr[i].outtype ==0) volumenStehend=volumenStehend+vol*st.tr[i].fac/st.size;
                   else volumenNaturschutz = volumenNaturschutz + vol*zerfall*st.tr[i].fac/st.size;
               }

               if ( st.tr[i].d > 20 && st.tr[i].outtype >1 && st.tr[i].out>0 ) {
// Sotierung
// alle ausgew�hlten Sortimente durchlaufen
                       double startHeight=fellingHeight;
                       for (int j=0;j<nausgewaehlt;j++) {
                          double ran = Math.random() * 100.0;
                          if (st.tr[i].code >= ls[j].artvon && st.tr[i].code <= ls[j].artbis && ran <= ls[j].wahrscheinlich){
                            boolean logFound = true;
                            double endHeight = st.tr[i].h;
                            if (ls[j].bisKronenansatz) endHeight = st.tr[i].cb;
                            boolean logOk = true;
                            if (ls[j].nurZBaum && st.tr[i].crop == false) logOk = false;
                            while(logFound && logOk) {
                               logFound = sortierer.getAssortment(st.tr[i].sp.spDef.taperFunction , st.tr[i].d, 1.3, 0.0, 0.0, st.tr[i].h,
                                    startHeight, endHeight, ls[j].minD, ls[j].maxD, ls[j].minTop, ls[j].maxTop,
                                    ls[j].minH, ls[j].maxH, 0.0, ls[j].zugabeProzent, ls[j].zugabeCm);
                                if(logFound) {
// calculate volume by Huber
                                double dmR_cm= sortierer.getADmB_cm();
                                double doR_cm= sortierer.getADm_cm();
                                double l_m = startHeight;
                                l_m = sortierer.getALae_m();
                                Double vo1HmR = 0.0;
                                vo1HmR = sortierer.getAVolmR_m3() ;
                                if (vo1HmR.isNaN() )  vo1HmR=0.0;
// Pr�fen , ob das St�ck aus dem Wald genommen wird oder nicht
                                if (ls[j].entnahme == false) {
                                     if (startHeight + l_m <= 0.3) volumenStubben = volumenStubben + vo1HmR*zerfall*st.tr[i].fac/st.size;
                                     if ( dmR_cm > 20 && startHeight+l_m > 0.3 )volumenNaturschutz = volumenNaturschutz + vo1HmR*zerfall*st.tr[i].fac/st.size;
                                     volumenTotholz = volumenTotholz+vo1HmR*zerfall*st.tr[i].fac/st.size;
                                }
                                else volumenGesamt=volumenGesamt - vo1HmR*zerfall*st.tr[i].fac/st.size;
// Sortimentsst�cke nach xml
//

                                   startHeight=startHeight + l_m;

                                }
                               if (ls[j].mehrfach == false) logFound = false; //Abbruch


                            }// while log found

                           } // Baumart


                        }   // for j
                 }
            }


        elt3 = new Element("Year");
        elt3 = addString(elt3, "Code", new Integer(st.sp[k].code).toString());
        elt3 = addString(elt3, "Jahr",new Integer(startyear).toString());
        elt3 = addString(elt3, "Stubben",f.format(volumenStubben));
        elt3 = addString(elt3, "Naturschutz",f.format(volumenNaturschutz));
        elt3 = addString(elt3, "Totholz",f.format(volumenTotholz));
        elt3 = addString(elt3, "Stehend",f.format(volumenStehend));
        elt3 = addString(elt3, "Gesamt",f.format(volumenGesamt));
        rootElt.addContent(elt3);

       } // for k species
       startyear = startyear+5;
       } //while

       }
		catch (Exception e)
		{	System.out.println(e);
		}


        try {
            File file = new File(pa);
            FileOutputStream result = new FileOutputStream(file);
            XMLOutputter outputter = new XMLOutputter();
//            outputter.setNewlines(true);
//            outputter.setIndent("  ");
            outputter.output(doc,result);
//
//
            if (dialogActive == false){
              String seite="file:"+System.getProperty("file.separator")+System.getProperty("file.separator")
                              +System.getProperty("file.separator")+pa;
              StartBrowser startBrowser = new StartBrowser(seite);
//              startBrowser.start();
            }



        }
        catch (IOException e){
            e.printStackTrace();
        }



    }

    public void loadls(URL url){
        nls=0;
        combo5Active=false;
        combo5Active=true;
        try {
         SAXBuilder builder = new SAXBuilder();
         URLConnection urlcon = url.openConnection();

         Document doc = builder.build(urlcon.getInputStream());
         
         DocType docType = doc.getDocType();
//        
         Element sortimente =  doc.getRootElement();  
         List Sortiment = sortimente.getChildren("Sortiment");
         Iterator i = Sortiment.iterator();
         
         while (i.hasNext()) {
            Element sortiment = (Element) i.next();
            ls[nls] = new LoggingSortiment(sortiment.getChild("Name").getText(),
                    Integer.parseInt(sortiment.getChild("Art_von").getText()),Integer.parseInt(sortiment.getChild("Art_bis").getText()),
                    Double.parseDouble(sortiment.getChild("minD").getText()),Double.parseDouble(sortiment.getChild("maxD").getText()),
                    Double.parseDouble(sortiment.getChild("minTop").getText()),Double.parseDouble(sortiment.getChild("maxTop").getText()),
                    Double.parseDouble(sortiment.getChild("minH").getText()),Double.parseDouble(sortiment.getChild("maxH").getText()),
                    Double.parseDouble(sortiment.getChild("ZugabeProzent").getText()),Double.parseDouble(sortiment.getChild("ZugabeCm").getText()),
                    Double.parseDouble(sortiment.getChild("Preis").getText()),Double.parseDouble(sortiment.getChild("Gewicht").getText()),
                    Double.parseDouble(sortiment.getChild("Wahrscheinlichkeit").getText()),
                    Boolean.parseBoolean(sortiment.getChild("nurZBaum").getText()),
                    Boolean.parseBoolean(sortiment.getChild("mehrfach").getText()),
                    Boolean.parseBoolean(sortiment.getChild("Entnahme").getText()),
                    Boolean.parseBoolean(sortiment.getChild("bisKA").getText()),
                    Boolean.parseBoolean(sortiment.getChild("ausgewaehlt").getText()),
                    nls,0
                    );
            listModel.addElement((String) ls[nls].name);
            nlist = nlist + 1;
            nls = nls +1;
         } 

       } catch (Exception e) {e.printStackTrace();}
       
}     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
    
}