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
package treegross.standsimulation;
import treegross.base.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.ProcessingInstruction;
import org.jdom.output.XMLOutputter;
import org.jdom.input.*;
import org.jdom.DocType;
import java.net.*;
import java.text.*;
import java.io.*;
import javax.swing.table.*;
import java.util.*;
import java.awt.Dimension.*;


/**
 *
 * @author  nagel
 */
public class TgSpeciesManXML extends javax.swing.JDialog {
    
    SpeciesDef spd[] = new SpeciesDef[100];
    int nspd = 0;
    static Element rootElt;
    javax.swing.DefaultListModel listModel = new javax.swing.DefaultListModel();
    String urlname = null;

    
    /** Creates new form TgSpeciesManXML */
    public TgSpeciesManXML(java.awt.Frame parent, boolean modal, String workdir, String fn) {
        super(parent, modal);
        initComponents();
//        java.awt.Dimension scr = parent.getSize();
//        setPreferredSize(scr);
//        setSize(scr);
        urlname=workdir+System.getProperty("file.separator")+fn;
        loadXMLFile();
//        int m = 1+ (int)(scr.width*0.1);
//        TableColumn col = jTable1.getColumnModel().getColumn( 0 );
//        col.setPreferredWidth(m );
//        TableColumn col1 = jTable1.getColumnModel().getColumn( 1 );
//        m = 1+ (int)(scr.width*0.8);
//        col1.setPreferredWidth(m);
        if (nspd > 0 ){
          loadTable(0);
          jList1.setSelectedIndex(0);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Species Manager XML");
        setBackground(java.awt.Color.white);

        jLabel3.setText("Configuracion General");

        jLabel4.setText("Modelo de region: ");

        jTextField1.setText("jTextField1");

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Componente aleatorio ");

        jCheckBox2.setText("Einwuchsmodell");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox3.setText("Modulo Madera Muerta");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(225, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel1.setPreferredSize(new java.awt.Dimension(10, 10));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel2.setText("Tipo escogido");
        jPanel1.add(jLabel2, java.awt.BorderLayout.PAGE_START);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jTable1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Variable", "XML Code"
            }
        ));
        jTable1.setPreferredSize(new java.awt.Dimension(800, 800));
        jScrollPane2.setViewportView(jTable1);

        jPanel5.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        jButton1.setText("save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Save as new species");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("save settings to file");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Borrar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jButton1)
                .addGap(15, 15, 15)
                .addComponent(jButton2)
                .addGap(21, 21, 21)
                .addComponent(jButton4)
                .addGap(16, 16, 16)
                .addComponent(jButton3)
                .addContainerGap(523, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton4))
                .addGap(37, 37, 37))
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.SOUTH);

        jScrollPane3.setViewportView(jPanel1);

        getContentPane().add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(100, 600));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabel1.setText("Tipos de arboles");
        jPanel2.add(jLabel1, java.awt.BorderLayout.NORTH);

        jList1.setModel(listModel);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.WEST);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-1097)/2, (screenSize.height-758)/2, 1097, 758);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
// TODO add your handling code here:
        int m = jList1.getSelectedIndex();
       
        for (int i=m+1;i< nspd;i++){
            spd[i-1]=spd[i];
        }
        nspd = nspd -1;
       

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
       int m = jList1.getSelectedIndex(); //get SelectedIndex from List
       saveTable(m);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
// TODO add your handling code here:
       int m = jList1.getSelectedIndex(); //get SelectedIndex from List
       loadTable(m);

    }//GEN-LAST:event_jList1ValueChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
// TODO add your handling code here:
        saveXMLFile();
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
// TODO add your handling code here:
        spd[nspd] = new SpeciesDef();
        saveTable(nspd);
        nspd = nspd +1;
       
        renewList();
    }//GEN-LAST:event_jButton2ActionPerformed

private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jCheckBox2ActionPerformed

    private void renewList(){
        listModel.removeAllElements();
        listModel.clear();
        for (int i=0;i<nspd;i++) {listModel.addElement((String)spd[i].shortName);}
    }
    
    private void saveTable(int m){
        
        spd[m].code = Integer.parseInt((String) jTable1.getValueAt(0,1));
        spd[m].shortName=(String) jTable1.getValueAt(1,1);
        spd[m].longName=(String) jTable1.getValueAt(2,1);
        spd[m].latinName=(String) jTable1.getValueAt(3,1);
        spd[m].internalCode = Integer.parseInt((String) jTable1.getValueAt(4,1));
        spd[m].codeGroup = Integer.parseInt((String) jTable1.getValueAt(5,1));
        spd[m].handledLikeCode = Integer.parseInt((String) jTable1.getValueAt(6,1));
        spd[m].heightCurve = getInt((String) jTable1.getValueAt(7,1));
        spd[m].uniformHeightCurveXML=(String) jTable1.getValueAt(8,1);
        spd[m].heightVariationXML=(String) jTable1.getValueAt(9,1);
        spd[m].diameterDistributionXML=(String) jTable1.getValueAt(10,1);
        spd[m].volumeFunctionXML=(String) jTable1.getValueAt(11,1);
        spd[m].stemVolumeFunctionXML=(String) jTable1.getValueAt(12,1);
        spd[m].crownwidthXML=(String) jTable1.getValueAt(13,1);
        spd[m].crownbaseXML=(String) jTable1.getValueAt(14,1);
        spd[m].crownType = getInt((String) jTable1.getValueAt(15,1));
        spd[m].siteindexXML=(String) jTable1.getValueAt(16,1);
        spd[m].siteindexHeightXML=(String) jTable1.getValueAt(17,1);
        spd[m].potentialHeightIncrementXML=(String) jTable1.getValueAt(18,1);
        spd[m].heightIncrementXML=(String) jTable1.getValueAt(19,1);
        spd[m].heightIncrementError = getDouble((String) jTable1.getValueAt(20,1));
        spd[m].diameterIncrementXML=(String) jTable1.getValueAt(21,1);
        spd[m].diameterIncrementError = getDouble((String) jTable1.getValueAt(22,1));
        spd[m].maximumDensityXML = (String) jTable1.getValueAt(23,1);
        spd[m].maximumAge = getInt((String) jTable1.getValueAt(24,1));
        spd[m].ingrowthXML = (String) jTable1.getValueAt(25,1);
        spd[m].decayXML = (String) jTable1.getValueAt(26,1);
        spd[m].targetDiameter = getDouble((String) jTable1.getValueAt(27,1));
        spd[m].heightOfThinningStart = getDouble((String) jTable1.getValueAt(28,1));
        spd[m].moderateThinning = (String) jTable1.getValueAt(29,1);
        spd[m].colorXML = (String) jTable1.getValueAt(30,1);
        spd[m].competitionXML = (String) jTable1.getValueAt(31,1);
        spd[m].taperFunctionXML = (String) jTable1.getValueAt(32,1);
        spd[m].coarseRootBiomass = (String) jTable1.getValueAt(33,1);
        spd[m].smallRootBiomass = (String) jTable1.getValueAt(34,1);
        spd[m].fineRootBiomass = (String) jTable1.getValueAt(35,1);
        spd[m].totalRootBiomass = (String) jTable1.getValueAt(36,1);
        
    }
    
    private int getInt(String s){
        int i=-9;
        try {
          i = Integer.parseInt(s); 
        }
        catch (Exception e)  {	System.out.println(e); }
        return i;
    }
    private double getDouble(String s){
        double d=-9;
        try {
          d = Double.parseDouble(s); 
        }
        catch (Exception e)  {	System.out.println(e); }
        return d;
    }
    
    private void loadXMLFile(){
       
        nspd=0;
        try {
          String fname="";
          
          int m = urlname.toUpperCase().indexOf("FILE");
          int m2 = urlname.toUpperCase().indexOf("HTTP");
          if ( m < 0 && m2 <0 ) fname="file:///"+urlname;
          else fname=urlname;
          URL url = new URL(fname);
         SAXBuilder builder = new SAXBuilder();
         URLConnection urlcon = url.openConnection();

         Document doc = builder.build(urlcon.getInputStream());
         
         DocType docType = doc.getDocType();
//
         
         Element sortimente =  doc.getRootElement();  
         List Sortiment = sortimente.getChildren("SpeciesDefinition");
         Iterator i = Sortiment.iterator();
         
         while (i.hasNext()) {
            Element sortiment = (Element) i.next();
            spd[nspd] = new SpeciesDef();
            spd[nspd].code =Integer.parseInt(sortiment.getChild("Code").getText());
            spd[nspd].internalCode =Integer.parseInt(sortiment.getChild("InternalCode").getText());
            spd[nspd].shortName = sortiment.getChild("ShortName").getText();
            spd[nspd].longName = sortiment.getChild("LongName").getText();
            spd[nspd].latinName = sortiment.getChild("LatinName").getText();
            spd[nspd].codeGroup =Integer.parseInt(sortiment.getChild("CodeGroup").getText());
            spd[nspd].handledLikeCode =Integer.parseInt(sortiment.getChild("HandledLikeCode").getText());
            String temp = sortiment.getChild("HeightCurve").getText();
            int ix = Integer.parseInt(temp);
            spd[nspd].heightCurve = ix;
            spd[nspd].uniformHeightCurveXML = sortiment.getChild("UniformHeightCurveXML").getText();
            spd[nspd].heightVariationXML = sortiment.getChild("HeightVariation").getText();
            spd[nspd].diameterDistributionXML = sortiment.getChild("DiameterDistributionXML").getText();
            spd[nspd].volumeFunctionXML = sortiment.getChild("VolumeFunctionXML").getText();
            spd[nspd].crownwidthXML = sortiment.getChild("Crownwidth").getText();
            spd[nspd].crownbaseXML = sortiment.getChild("Crownbase").getText();
            spd[nspd].crownType =Integer.parseInt(sortiment.getChild("CrownType").getText());
            spd[nspd].siteindexXML = sortiment.getChild("SiteIndex").getText();
            spd[nspd].siteindexHeightXML = sortiment.getChild("SiteIndexHeight").getText();
            spd[nspd].potentialHeightIncrementXML = sortiment.getChild("PotentialHeightIncrement").getText();
            spd[nspd].heightIncrementXML = sortiment.getChild("HeightIncrement").getText();
            spd[nspd].heightIncrementError =Double.parseDouble(sortiment.getChild("HeightIncrementError").getText());
            spd[nspd].diameterIncrementXML = sortiment.getChild("DiameterIncrement").getText();
            spd[nspd].diameterIncrementError =Double.parseDouble(sortiment.getChild("DiameterIncrementError").getText());
            spd[nspd].maximumDensityXML = sortiment.getChild("MaximumDensity").getText();
            spd[nspd].maximumAge =Integer.parseInt(sortiment.getChild("MaximumAge").getText());
            spd[nspd].ingrowthXML = sortiment.getChild("Ingrowth").getText();
            spd[nspd].decayXML = sortiment.getChild("Decay").getText();
            spd[nspd].targetDiameter =Double.parseDouble(sortiment.getChild("TargetDiameter").getText());
            spd[nspd].heightOfThinningStart =Double.parseDouble(sortiment.getChild("HeightOfThinningStart").getText());
            spd[nspd].moderateThinning = sortiment.getChild("ModerateThinning").getText();
            spd[nspd].colorXML = sortiment.getChild("Color").getText();
            spd[nspd].competitionXML = sortiment.getChild("Competition").getText();
            spd[nspd].taperFunctionXML = sortiment.getChild("TaperFunction").getText();
            try {  spd[nspd].stemVolumeFunctionXML = sortiment.getChild("StemVolumeFunction").getText();
                 } catch (Exception e){ System.out.println("Schaftholz ist nicht definiert: ");}

            spd[nspd].coarseRootBiomass = sortiment.getChild("CoarseRootBiomass").getText();
            spd[nspd].smallRootBiomass = sortiment.getChild("SmallRootBiomass").getText();
            spd[nspd].fineRootBiomass = sortiment.getChild("FineRootBiomass").getText();
            spd[nspd].totalRootBiomass = sortiment.getChild("TotalRootBiomass").getText();
          
            nspd = nspd +1;
         } 
         
         Element einstellung =  doc.getRootElement(); 
         List einstellungen = einstellung.getChildren("GeneralSettings");
         Iterator k = einstellungen.iterator();
         
         while (k.hasNext()) {
            Element eingestellt = (Element) k.next();
            jTextField1.setText(eingestellt.getChild("ModelRegion").getText());
            jCheckBox1.setSelected(Boolean.parseBoolean(eingestellt.getChild("ErrorComponent").getText()));
            jCheckBox2.setSelected(Boolean.parseBoolean(eingestellt.getChild("IngrowthModul").getText()));
            jCheckBox3.setSelected(Boolean.parseBoolean(eingestellt.getChild("DeadwoodModul").getText()));
            break;
         }


       } catch (Exception e) {e.printStackTrace();}
      
       renewList();
        
    }
    
    private void saveXMLFile(){
       String fn = urlname; 
       NumberFormat f=NumberFormat.getInstance();
       f=NumberFormat.getInstance(new Locale("en","US"));
       f.setMaximumFractionDigits(2);
       f.setMinimumFractionDigits(2);
       Element elt;
       Element elt2;
       Document doc = new Document();
       rootElt = new Element("ForestSimulatorSettings");
       ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet",
                 "type=\"text/xsl\" href=\"ForestSimulatorSettings.xsl\"");
       doc.addContent(pi);
       doc.setRootElement(rootElt);
//
       elt = new Element("GeneralSettings");
       elt = addString(elt, "ModelRegion", jTextField1.getText());
       elt = addString(elt, "ErrorComponent",new Boolean(jCheckBox1.isSelected()).toString());
       elt = addString(elt, "IngrowthModul",new Boolean(jCheckBox2.isSelected()).toString());
       elt = addString(elt, "DeadwoodModul",new Boolean(jCheckBox3.isSelected()).toString());
       rootElt.addContent(elt);
 
//         
       for (int i=0;i< nspd;i++){
            elt = new Element("SpeciesDefinition");
            elt = addString(elt, "Code", new Integer(spd[i].code).toString());
            elt = addString(elt, "ShortName",spd[i].shortName);
            elt = addString(elt, "LongName",spd[i].longName);
            elt = addString(elt, "LatinName",spd[i].latinName);
            elt = addString(elt, "InternalCode", new Integer(spd[i].internalCode).toString());
            elt = addString(elt, "CodeGroup", new Integer(spd[i].codeGroup).toString());
            elt = addString(elt, "HandledLikeCode", new Integer(spd[i].handledLikeCode).toString());
            elt = addString(elt, "HeightCurve", new Integer(spd[i].heightCurve).toString());
            elt = addString(elt, "UniformHeightCurveXML",spd[i].uniformHeightCurveXML);
            elt = addString(elt, "HeightVariation",spd[i].heightVariationXML);
            elt = addString(elt, "DiameterDistributionXML",spd[i].diameterDistributionXML);
            elt = addString(elt, "VolumeFunctionXML",spd[i].volumeFunctionXML);
            elt = addString(elt, "StemVolumeFunction",spd[i].stemVolumeFunctionXML);
            elt = addString(elt, "Crownwidth",spd[i].crownwidthXML);
            elt = addString(elt, "Crownbase",spd[i].crownbaseXML);
            elt = addString(elt, "CrownType", new Integer(spd[i].crownType).toString());
            elt = addString(elt, "SiteIndex",spd[i].siteindexXML);
            elt = addString(elt, "SiteIndexHeight",spd[i].siteindexHeightXML);
            elt = addString(elt, "PotentialHeightIncrement",spd[i].potentialHeightIncrementXML);
            elt = addString(elt, "HeightIncrement",spd[i].heightIncrementXML);
            elt = addString(elt, "HeightIncrementError", new Double(spd[i].heightIncrementError).toString());
            elt = addString(elt, "DiameterIncrement",spd[i].diameterIncrementXML);
            elt = addString(elt, "DiameterIncrementError", new Double(spd[i].diameterIncrementError).toString());
            elt = addString(elt, "MaximumDensity", spd[i].maximumDensityXML.toString());
            elt = addString(elt, "MaximumAge", new Integer(spd[i].maximumAge).toString());
            elt = addString(elt, "Ingrowth", spd[i].ingrowthXML);
            elt = addString(elt, "Decay",spd[i].decayXML);
            elt = addString(elt, "TargetDiameter", new Double(spd[i].targetDiameter).toString());
            elt = addString(elt, "HeightOfThinningStart", new Double(spd[i].heightOfThinningStart).toString());
            elt = addString(elt, "ModerateThinning", spd[i].moderateThinning.toString());
            elt = addString(elt, "Color",spd[i].colorXML);
            elt = addString(elt, "Competition",spd[i].competitionXML);
            elt = addString(elt, "TaperFunction",spd[i].taperFunctionXML);
            elt = addString(elt, "CoarseRootBiomass",spd[i].coarseRootBiomass);
            elt = addString(elt, "SmallRootBiomass",spd[i].smallRootBiomass);
            elt = addString(elt, "FineRootBiomass",spd[i].fineRootBiomass);
            elt = addString(elt, "TotalRootBiomass",spd[i].totalRootBiomass);
            rootElt.addContent(elt);
        }
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

    private void loadTable(int m){
        jTable1.setValueAt("Baumarten Code (I) ",0,0);
        jTable1.setValueAt("Kurzname ",1,0);
        jTable1.setValueAt("Name ",2,0);
        jTable1.setValueAt("lateinisch ",3,0);
        jTable1.setValueAt("Interner Code (I) ",4,0);
        jTable1.setValueAt("Gruppen Code (I) ",5,0);
        jTable1.setValueAt("Einstellungen wie Code (I) ",6,0);
        jTable1.setValueAt("Height Curve (I)",7,0);
        jTable1.setValueAt("Uniform Height Curve ",8,0);
        jTable1.setValueAt("Height Variation",9,0);
        jTable1.setValueAt("Diameter distribution",10,0);
        jTable1.setValueAt("Volume Function o.B.",11,0);
        jTable1.setValueAt("Stem Volume Function o.B.",12,0);
        jTable1.setValueAt("Crown width",13,0);
        jTable1.setValueAt("Crown base",14,0);
        jTable1.setValueAt("Crown type",15,0);
        jTable1.setValueAt("Site index",16,0);
        jTable1.setValueAt("Index height",17,0);
        jTable1.setValueAt("Potential height increment",18,0);
        jTable1.setValueAt("Height Increment",19,0);
        jTable1.setValueAt("Height Increment Error",20,0);
        jTable1.setValueAt("Quadratic Diameter Increment",21,0);
        jTable1.setValueAt("Diameter Increment Error",22,0);
        jTable1.setValueAt("Maximum Density",23,0);
        jTable1.setValueAt("Maximum Age",24,0);
        jTable1.setValueAt("Plugin Ingrowth",25,0);
        jTable1.setValueAt("Decay",26,0);
        jTable1.setValueAt("Target Diameter",27,0);
        jTable1.setValueAt("Height of first Thinning",28,0);
        jTable1.setValueAt("Moderate Thinning Factor",29,0);
        jTable1.setValueAt("Color",30,0);
        jTable1.setValueAt("Plugin Competition",31,0);
        jTable1.setValueAt("Plugin Taper Fuction",32,0);
        jTable1.setValueAt("Grobwurzelbiomasse Funktion",33,0);
        jTable1.setValueAt("Kleinwurzelbiomasse Funktion",34,0);
        jTable1.setValueAt("Feinwurzelbiomasse Funktion",35,0);
        jTable1.setValueAt("Gesamtwurzelbiomasse Funktion",36,0);
        jTable1.setValueAt(new Integer(spd[m].code).toString(),0,1);
        jTable1.setValueAt(spd[m].shortName,1,1);
        jTable1.setValueAt(spd[m].longName,2,1);
        jTable1.setValueAt(spd[m].latinName,3,1);
        jTable1.setValueAt(new Integer(spd[m].internalCode).toString(),4,1);
        jTable1.setValueAt(new Integer(spd[m].codeGroup).toString(),5,1);
        jTable1.setValueAt(new Integer(spd[m].handledLikeCode).toString(),6,1);
        jTable1.setValueAt(new Integer(spd[m].heightCurve).toString(),7,1);
        jTable1.setValueAt(spd[m].uniformHeightCurveXML,8,1);
        jTable1.setValueAt(spd[m].heightVariationXML,9,1);
        jTable1.setValueAt(spd[m].diameterDistributionXML,10,1);
        jTable1.setValueAt(spd[m].volumeFunctionXML,11,1);
        jTable1.setValueAt(spd[m].stemVolumeFunctionXML,12,1);
        jTable1.setValueAt(spd[m].crownwidthXML,13,1);
        jTable1.setValueAt(spd[m].crownbaseXML,14,1);
        jTable1.setValueAt(new Integer(spd[m].crownType).toString(),15,1);
        jTable1.setValueAt(spd[m].siteindexXML,16,1);
        jTable1.setValueAt(spd[m].siteindexHeightXML,17,1);
        jTable1.setValueAt(spd[m].potentialHeightIncrementXML,18,1);
        jTable1.setValueAt(spd[m].heightIncrementXML,19,1);
        jTable1.setValueAt(new Double(spd[m].heightIncrementError).toString(),20,1);
        jTable1.setValueAt(spd[m].diameterIncrementXML,21,1);
        jTable1.setValueAt(new Double(spd[m].diameterIncrementError).toString(),22,1);
        jTable1.setValueAt(spd[m].maximumDensityXML,23,1);
        jTable1.setValueAt(new Integer(spd[m].maximumAge).toString(),24,1);
        jTable1.setValueAt(spd[m].ingrowthXML,25,1);
        jTable1.setValueAt(spd[m].decayXML,26,1);
        jTable1.setValueAt(new Double(spd[m].targetDiameter).toString(),27,1);
        jTable1.setValueAt(new Double(spd[m].heightOfThinningStart).toString(),28,1);
        jTable1.setValueAt(spd[m].moderateThinning,29,1);
        jTable1.setValueAt(spd[m].colorXML,30,1);
        jTable1.setValueAt(spd[m].competitionXML,31,1);
        jTable1.setValueAt(spd[m].taperFunctionXML,32,1);
        jTable1.setValueAt(spd[m].coarseRootBiomass,33,1);
        jTable1.setValueAt(spd[m].smallRootBiomass,34,1);
        jTable1.setValueAt(spd[m].fineRootBiomass,35,1);
        jTable1.setValueAt(spd[m].totalRootBiomass,36,1);
        
    }    
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
    
}
