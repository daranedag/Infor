/* http://www.nw-fva.de
   Version 07-11-2008

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
package treegross.roots;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.ProcessingInstruction;
import org.jdom.Comment;
import org.jdom.output.XMLOutputter;
import org.jdom.input.*;
import org.jdom.DocType;
import java.net.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
public class RootsPanel extends javax.swing.JPanel {
    
    RootsSpeciesSetting rss[] = new RootsSpeciesSetting[500]; 
    int nrss=-9;
    static Document doc;
    static Element rootElt;
    

    javax.swing.DefaultListModel listModel = new javax.swing.DefaultListModel();
    int nlist = 0;
    boolean neuspeichern = false;
    String fname="";
    String proDir;
    boolean dialogActive = true;
    String urlcodebase = "";
    String urlString ="";
    String workDir = "";
    Stand st = null;
    URL urlRootFun = null;
    
    /** Creates new form LoggingPanel */
    public RootsPanel(Stand stand, String programDir, boolean interActive, String workingDir) {
        initComponents();
        dialogActive = interActive;
        jPanel2.setVisible(false);
        if (dialogActive = true) jPanel8.setVisible(false);
        st = stand;
                proDir=programDir;
        workDir = workingDir;
        int m = programDir.toUpperCase().indexOf("FILE");
        int m2 = programDir.toUpperCase().indexOf("HTTP");
        String fname=programDir+System.getProperty("file.separator")+"RootBiomassFunctions.xml";
        if ( m < 0 && m2 <0 ) fname="file:"+System.getProperty("file.separator")+System.getProperty("file.separator")+System.getProperty("file.separator")+fname;

        try{
          URL url = new URL(fname);
          urlRootFun =url;
          loadSpecies();
          loadSpeciesFunctions(url);
        }
        catch (IOException e){
            e.printStackTrace();
        }

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
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel15 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(608, 221));
        setRequestFocusEnabled(false);
        setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(255, 255, 204));
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel13.setText("Datos TreeGrOSS xml :");
        jPanel8.add(jLabel13);

        jTextField13.setPreferredSize(new java.awt.Dimension(511, 19));
        jPanel8.add(jTextField13);

        jButton5.setText("load");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton5);

        add(jPanel8, java.awt.BorderLayout.NORTH);

        jButton1.setText("Calculate root biomass");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1, java.awt.BorderLayout.SOUTH);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel14.setText("Choose species functions ");
        jPanel10.add(jLabel14);

        jLabel1.setText("Coarse Root Function");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("Fine Root Function");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Small Root Function");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setText("Total Root Function");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton2.setText("load Functions");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel5.setText("use  functions of other species with code:");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(19, 19, 19)
                .add(jLabel5)
                .add(55, 55, 55)
                .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(29, 29, 29)
                .add(jButton2)
                .add(153, 153, 153))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(19, 19, 19)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(jButton2)
                    .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(67, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                            .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 194, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jComboBox4, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                            .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 194, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 364, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 194, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 194, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jComboBox3, 0, 350, Short.MAX_VALUE)
                                .add(jComboBox2, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 593, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(375, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jComboBox3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jComboBox4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(40, 40, 40))
        );

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

        jLabel15.setText("Species");
        jPanel11.add(jLabel15, java.awt.BorderLayout.NORTH);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 722, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 279, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
       
// TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
// Chane of species List
       showFunctions();
    }//GEN-LAST:event_jList1ValueChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
  // Calculate and write xml
       String pa="";
       String dn="";
       pa = workDir+System.getProperty("file.separator")+"rootbiomass.xml";
//
       NumberFormat f=NumberFormat.getInstance();
       f=NumberFormat.getInstance(new Locale("en","US"));
       f.setMaximumFractionDigits(2);
       f.setMinimumFractionDigits(2);
       f.setGroupingUsed(false);
       Element elt;
       Element elt2;
       Element elt3;
/** Creates an Treegross xml */
       Document doc = new Document();
       rootElt = new Element("Rootbiomass");
       ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet",
                 "type=\"text/xsl\" href=\"rootbiomass.xsl\"");
       doc.addContent(pi);
       doc.setRootElement(rootElt);
       try {
// File 
        rootElt= addString(rootElt, "Id","1");
        rootElt= addString(rootElt, "Identificacion",st.standname);
        rootElt= addString(rootElt, "General"," ");
        rootElt= addString(rootElt, "Tama�oZona_m2",new Double(st.size*10000).toString());
        rootElt= addString(rootElt, "CodigoStdTipoArbolPrincipal",new Integer(st.sp[0].code).toString());
        rootElt= addString(rootElt, "CodigoLokalTipoArbolPrincipal",new Integer(st.sp[0].code).toString());
        rootElt= addString(rootElt, "A�oGrabacion",new Integer(st.year).toString());
        rootElt= addString(rootElt, "MesGrabacion",new Integer(st.monat).toString());
        rootElt= addString(rootElt, "AscendenciaDatos",st.datenHerkunft);
        rootElt= addString(rootElt, "Ubicacion",st.standort);
        rootElt= addString(rootElt, "ValorAltura_m",new Double(st.hochwert_m).toString());
        rootElt= addString(rootElt, "ValorDerecho_m",new Double(st.rechtswert_m).toString());
        rootElt= addString(rootElt, "Altura_uNN_m",new Double(st.hoehe_uNN_m).toString());
        rootElt= addString(rootElt, "Exposition_Gon",new Integer(st.exposition_Gon).toString());
        rootElt= addString(rootElt, "Porcentaje_Pendiente",new Double(st.hangneigungProzent).toString());
        rootElt= addString(rootElt, "AreaVegetacion",st.wuchsgebiet);
        rootElt= addString(rootElt, "DistritoCrecimiento",st.wuchsbezirk);
        rootElt= addString(rootElt, "IdentificadorUbicacion",st.standortsKennziffer);
    
// Table of functions
           for (int jj=0; jj< nrss; jj++){
              elt = new Element("Functions");
              elt = addString(elt, "Code", new Integer(rss[jj].code).toString());
              elt = addString(elt, "Shortname", rss[jj].shortname);
              elt = addString(elt, "Component", "CoarseRoot");
              elt = addString(elt, "Function", rss[jj].coarseRootFun);
              rootElt.addContent(elt);
              elt = new Element("Functions");
              elt = addString(elt, "Code", new Integer(rss[jj].code).toString());
              elt = addString(elt, "Shortname", rss[jj].shortname);
              elt = addString(elt, "Component", "FineRoot");
              elt = addString(elt, "Function", rss[jj].fineRootFun);
              rootElt.addContent(elt);
              elt = new Element("Functions");
              elt = addString(elt, "Code", new Integer(rss[jj].code).toString());
              elt = addString(elt, "Shortname", rss[jj].shortname);
              elt = addString(elt, "Component", "SmallRoot");
              elt = addString(elt, "Function", rss[jj].smallRootFun);
              rootElt.addContent(elt);
              elt = new Element("Functions");
              elt = addString(elt, "Code", new Integer(rss[jj].code).toString());
              elt = addString(elt, "Shortname", rss[jj].shortname);
              elt = addString(elt, "Component", "TotalRoot");
              elt = addString(elt, "Function", rss[jj].totalRootFun);
              rootElt.addContent(elt);
               
           }
           
// Calculate stand total root biomass
           FunctionInterpreter fi = new FunctionInterpreter();
           for (int jj=0; jj< nrss; jj++){
              double sumCR = 0.0;
              double sumFR = 0.0;
              double sumSR = 0.0;
              double sumTR = 0.0;
              for (int i=0;i < st.ntrees;i++)
                  if (st.tr[i].out < 0 && st.tr[i].code == rss[jj].code) {
                      sumCR = sumCR + st.tr[i].fac*fi.getValueForTree(st.tr[i], rss[jj].coarseRootFun);
                      sumFR = sumFR + st.tr[i].fac*fi.getValueForTree(st.tr[i], rss[jj].fineRootFun);
                      sumSR = sumSR + st.tr[i].fac*fi.getValueForTree(st.tr[i], rss[jj].smallRootFun);
                      sumTR = sumTR + st.tr[i].fac*fi.getValueForTree(st.tr[i], rss[jj].totalRootFun);
              }
              sumCR = sumCR/st.size;
              sumFR = sumFR/st.size;
              sumSR = sumSR/st.size;
              sumTR = sumTR/st.size;
              elt = new Element("Species");
              elt = addString(elt, "Code", new Integer(rss[jj].code).toString());
              elt = addString(elt, "Shortname", rss[jj].shortname);
              elt = addString(elt, "SumCoarseRoots", f.format(sumCR));
              elt = addString(elt, "SumFineRoots", f.format(sumFR));
              elt = addString(elt, "SumSmallRoots", f.format(sumSR));
              elt = addString(elt, "SumTotalRoots", f.format(sumTR));
              rootElt.addContent(elt);
           }
       
       for (int jj=0; jj< nrss; jj++){
              for (int i=0;i < st.ntrees;i++)
                  if (st.tr[i].out < 0 && st.tr[i].code == rss[jj].code) {
               elt = new Element("Tree");
               elt = addString(elt, "Code", new Integer(st.tr[i].code).toString());
               elt = addString(elt, "Shortname", st.tr[i].sp.spDef.shortName);
               elt = addString(elt, "Shortname", st.tr[i].no);
               elt = addString(elt, "Age", new Integer(st.tr[i].age).toString());
               elt = addString(elt, "DBH", f.format(st.tr[i].d));
               elt = addString(elt, "Height", f.format(st.tr[i].h));
               elt = addString(elt, "CoarseRoots", f.format(fi.getValueForTree(st.tr[i], rss[jj].coarseRootFun)));
               elt = addString(elt, "FineRoots", f.format(fi.getValueForTree(st.tr[i], rss[jj].fineRootFun)));
               elt = addString(elt, "SmallRoots", f.format(fi.getValueForTree(st.tr[i], rss[jj].smallRootFun)));
               elt = addString(elt, "TotalRoots", f.format(fi.getValueForTree(st.tr[i], rss[jj].totalRootFun)));
               rootElt.addContent(elt);
           }
       }

       }
       catch (Exception e){
		System.out.println(e); 
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
            if (dialogActive){
                String seite="file:"+System.getProperty("file.separator")+System.getProperty("file.separator")+System.getProperty("file.separator")+pa;
                try  {
                    Runtime.getRuntime().exec( " rundll32 url.dll,FileProtocolHandler "+seite);
                   }
		   catch ( Exception e2)  {
                    System.out.println(e2); 
                   }	         

            }    
            
                        
        }
        catch (IOException e){
            e.printStackTrace();
        }



  

// TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
// Load alternative functions
    int codex = Integer.parseInt(jTextField1.getText());
    int m = jList1.getSelectedIndex();
    loadAlternativeCodeFunctions(urlRootFun, m, codex);
    showFunctions();
    
}//GEN-LAST:event_jButton2ActionPerformed
    
    Element addString(Element elt, String variable, String text){
            Element var = new Element(variable);
            var.addContent(text);  
            elt.addContent(var);
            return elt;
    }
   
       
    public void loadSpecies(){
        nrss=0;
        for (int i=0; i < st.nspecies; i++){
            rss[nrss] = new RootsSpeciesSetting(nrss,st.sp[i].spDef.shortName,st.sp[i].code,
                    "","","",""
                    );
            listModel.addElement((String) rss[nrss].shortname);

            nrss=nrss+1;
         } 


    }    
   public void loadSpeciesFunctions(URL url){
      try {
         SAXBuilder builder = new SAXBuilder();
         URLConnection urlcon = url.openConnection();

         Document doc = builder.build(urlcon.getInputStream());
         
         DocType docType = doc.getDocType();
//        
         Element rootelemente =  doc.getRootElement();  
         List liste = rootelemente.getChildren("Function");
         Iterator i = liste.iterator();
         
         while (i.hasNext()) {
            Element rootelement = (Element) i.next();
            int co = Integer.parseInt(rootelement.getChild("Code").getText());
            for (int j=0; j< nrss; j++)
                if (co == rss[j].code){
                    if (rootelement.getChild("Component").getText().indexOf("biomassOfCoarseRoots")>-1)
                        rss[j].coarseRootFun=rootelement.getChild("Function").getText();
                    if (rootelement.getChild("Component").getText().indexOf("biomassOfFineRoots")>-1)
                        rss[j].fineRootFun=rootelement.getChild("Function").getText();
                    if (rootelement.getChild("Component").getText().indexOf("biomassOfSmallRoots")>-1)
                        rss[j].smallRootFun=rootelement.getChild("Function").getText();
                    if (rootelement.getChild("Component").getText().indexOf("biomassOfTotalRoots")>-1)
                        rss[j].totalRootFun=rootelement.getChild("Function").getText();
                }
         } 

       } catch (Exception e) {e.printStackTrace();}
      
   }; 
   public void loadAlternativeCodeFunctions(URL url, int nrss, int codex){
      try {
         SAXBuilder builder = new SAXBuilder();
         URLConnection urlcon = url.openConnection();

         Document doc = builder.build(urlcon.getInputStream());
         
         DocType docType = doc.getDocType();
//        
         Element rootelemente =  doc.getRootElement();  
         List liste = rootelemente.getChildren("Function");
         Iterator i = liste.iterator();
         
         while (i.hasNext()) {
            Element rootelement = (Element) i.next();
            int co = Integer.parseInt(rootelement.getChild("Code").getText());
                if (co == codex){
                    if (rootelement.getChild("Component").getText().indexOf("biomassOfCoarseRoots")>-1)
                        rss[nrss].coarseRootFun=rootelement.getChild("Function").getText();
                    if (rootelement.getChild("Component").getText().indexOf("biomassOfFineRoots")>-1)
                        rss[nrss].fineRootFun=rootelement.getChild("Function").getText();
                    if (rootelement.getChild("Component").getText().indexOf("biomassOfSmallRoots")>-1)
                        rss[nrss].smallRootFun=rootelement.getChild("Function").getText();
                    if (rootelement.getChild("Component").getText().indexOf("biomassOfTotalRoots")>-1)
                        rss[nrss].totalRootFun=rootelement.getChild("Function").getText();
                }
         } 

       } catch (Exception e) {e.printStackTrace();}
      
   }; 

   private void showFunctions(){
        jPanel2.setVisible(true);
        int m = jList1.getSelectedIndex(); 
        if (m > -1) {
            jComboBox1.removeAllItems();  
            jComboBox1.addItem((String) rss[m].coarseRootFun);
            jComboBox2.removeAllItems();
            jComboBox2.addItem((String) rss[m].fineRootFun);
            jComboBox3.removeAllItems();
            jComboBox3.addItem((String) rss[m].smallRootFun);
            jComboBox4.removeAllItems();
            jComboBox4.addItem((String) rss[m].totalRootFun);
            boolean allOk = true;
            if (jComboBox1.getSelectedItem().toString().length()< 1) allOk=false;
            if (jComboBox2.getSelectedItem().toString().length()< 1) allOk=false;
            if (jComboBox3.getSelectedItem().toString().length()< 1) allOk=false;
            if (jComboBox4.getSelectedItem().toString().length()< 1) allOk=false;
            if (allOk == false) jPanel3.setVisible(true);
            else jPanel3.setVisible(false);
        }

   }   
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField13;
    // End of variables declaration//GEN-END:variables
    
}
