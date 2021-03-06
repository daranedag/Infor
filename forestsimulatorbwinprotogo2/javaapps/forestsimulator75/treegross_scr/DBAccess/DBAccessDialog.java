/*
* @(#) DBAccessDialog.java
*  (c) 2002-2010 Juergen Nagel, Northwest German Research Station,
*      Gr�tzelstr.2, 37079 G�ttingen, Germany
*      E-Mail: Juergen.Nagel@nw-fva.de
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU General Public License
*  as published by the Free Software Foundation.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*/
package treegross.DBAccess;
import treegross.base.*;
import treegross.treatment.*;
import java.sql.*;


/** TreeGrOSS : DBAccessDialog.java
 *  version 	7.5 18-Mar-2010
 *  author	Juergen Nagel
 *
 * This dialog class is used only special data management of the NW-FVA and it is not documented.
 * It reads a forest stand from the data base structure of the NW-FVA
 */
public class DBAccessDialog extends javax.swing.JDialog {
    DBConnection dbconnAC = null;
    String aktivesDatenfile=null;
    Stand st= null;
    int  growthCycles = 0;
    
    /** Creates new form DBAccessDialog */
    public DBAccessDialog(java.awt.Frame parent, boolean modal,Stand stand, String dir) {
        super(parent, modal);
        initComponents();
        st = stand;
        jComboBox1.removeAllItems();
        jTextField1.setText(dir+System.getProperty("file.separator")+"localdata.mdb");
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("suchen");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setText("jTextField1");

        jTextField2.setText("test0001");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton2.setText("Bestand laden");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Bestand rechnen");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("alle rechnen");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("save Stand to db");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
                        .addGap(26, 26, 26)
                        .addComponent(jButton1)))
                .addContainerGap(78, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(461, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addGap(115, 115, 115))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addGap(37, 37, 37))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        aktivesDatenfile = jTextField1.getText();
        dbconnAC= new DBConnection(1);     // a class to manage the conection to a database
        dbconnAC.openDBConnection(dbconnAC.ACCESS, 0, aktivesDatenfile, "", "", false, true);
          try {
               Statement stmt = dbconnAC.Connections[0].createStatement(); 
               ResultSet rs = stmt.executeQuery("SELECT * FROM Auf WHERE (edvid = '"+jTextField2.getText()+"') " );
               while (rs.next()){
                     jComboBox1.addItem(rs.getInt("auf"));
               }
              }
           catch (Exception e){  System.out.println("Problem: "+" "+e); }
       dbconnAC.closeAll();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        aktivesDatenfile = jTextField1.getText();
        dbconnAC= new DBConnection(1);     // a class to manage the conection to a database
        dbconnAC.openDBConnection(dbconnAC.ACCESS, 0, aktivesDatenfile, "", "", false, true);
        LoadTreegrossStand lts = new  LoadTreegrossStand(); 
        
        String ids = jTextField2.getText();
        Object txt = jComboBox1.getSelectedItem();
        
        int aufs = Integer.parseInt(txt.toString());
        
        st=lts.loadFromDB( dbconnAC, st, ids, aufs , true, true);
         st.sortbyd();
        st.missingData();
        GenerateXY gxy =new GenerateXY();
        gxy.zufall(st);
        // Test if all trees are in area           
           for (int k=0; k < st.ntrees; k++){
               if (pnpoly(st.tr[k].x, st.tr[k].y, st)==0){
                   st.tr[k].out=1900;
                   st.tr[k].outtype=1;
               }
           }

            st.descspecies();
// Define all trees with fac = 0.0 as dead zu that there is no growth          
           for (int k=0; k < st.ntrees; k++){
               if (st.tr[k].fac==0.0){
                   st.tr[k].out=1900;
                   st.tr[k].outtype=1;
               }
           }
       st.descspecies();
       
        dbconnAC.closeAll();

        dispose();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        aktivesDatenfile = jTextField1.getText();
        dbconnAC= new DBConnection(1);     // a class to manage the conection to a database
        dbconnAC.openDBConnection(dbconnAC.ACCESS, 0, aktivesDatenfile, "", "", false, true);
        LoadTreegrossStand lts = new  LoadTreegrossStand(); 
        
        String ids = jTextField2.getText();
        Object txt = jComboBox1.getSelectedItem();
        
        int aufs = Integer.parseInt(txt.toString());
        
        st=lts.loadFromDB( dbconnAC, st, ids, aufs , true, true);
        st.sortbyd();
        st.missingData();
        GenerateXY gxy =new GenerateXY();
        gxy.zufall(st);
        st.descspecies();
        st=lts.loadRules( dbconnAC, st, ids, aufs );
        lts.saveBaum(dbconnAC, st, ids, aufs, 0, 0);
        for (int i=0;i<st.temp_Integer;i++){
            st.grow(5, false);
            st.sortbyd();
            st.missingData();
            st.descspecies();
            lts.saveBaum(dbconnAC, st, ids, aufs, i+1,0);
        }
        
        
        
        dbconnAC.closeAll();

        dispose();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        aktivesDatenfile = jTextField1.getText();
        dbconnAC= new DBConnection(1);     // a class to manage the conection to a database
        dbconnAC.openDBConnection(dbconnAC.ACCESS, 0, aktivesDatenfile, "", "", false, true);
        LoadTreegrossStand lts = new  LoadTreegrossStand(); 
        
        String ida[] = new String[50000];
        int aufa[]= new  int[50000];
        int nauf = 0;
          try {
               Statement stmt = dbconnAC.Connections[0].createStatement(); 
               ResultSet rs = stmt.executeQuery("SELECT * FROM Vorschrift  " );
               while (rs.next()){
                     ida[nauf]= rs.getObject("edvid").toString();
                     aufa[nauf]=rs.getInt("auf");
                     nauf = nauf +1;
               }
              }
           catch (Exception e){  System.out.println("Problem: "+" "+e); }
        
        for (int ii=0; ii < nauf; ii++){
           String ids = ida[ii];
           int aufs = aufa[ii];
           int nwiederh=0;
//
           try {
               Statement stmt = dbconnAC.Connections[0].createStatement();
               ResultSet rs = stmt.executeQuery("SELECT * FROM Vorschrift WHERE edvid='"+ids+"' AND auf = "+aufs+"  " );
               while (rs.next()){
                     nwiederh=rs.getInt("wiederholung");
               }
              }
           catch (Exception e){  System.out.println("Problem: "+" "+e); }
//
          for (int iw=0; iw <nwiederh;iw++){
           st=lts.loadFromDB( dbconnAC, st, ids, aufs , true, true);
           st.sortbyd();
           st.missingData();
           GenerateXY gxy =new GenerateXY();
           gxy.zufall(st);
// Test if all trees are in area           
           for (int k=0; k < st.ntrees; k++){
               if (pnpoly(st.tr[k].x, st.tr[k].y, st)==0){
                   st.tr[k].out=1900;
                   st.tr[k].outtype=1;
               }
           }
           st.descspecies();
// Define all trees with fac = 0.0 as dead zu that there is no growth          
           for (int k=0; k < st.ntrees; k++){
               if (st.tr[k].fac==0.0){
                   st.tr[k].out=1900;
                   st.tr[k].outtype=1;
               }
           }
           st.descspecies();
           st=lts.loadRules( dbconnAC, st, ids, aufs );
           int ebaum = lts.getEBaum();
           int baumart = lts.getBaumart();
           int bestand = lts.getBestand();
           int durchf = lts.getDurchf();
           if (ebaum ==1) lts.saveBaum(dbconnAC, st, ids, aufs, 0,iw+1);
           if (baumart ==1) lts.saveSpecies(dbconnAC, st, ids, aufs, 0);
           if (bestand ==1) lts.saveStand(dbconnAC, st, ids, aufs, 0,iw+1);
           for (int i=0;i<st.temp_Integer;i++){
               if (durchf == 1) {
                   st.descspecies();
                   st.sortbyd();
                   Treatment2 tl = new Treatment2();
                   tl.executeManager2(st);
                   st.descspecies();

               }
               if (bestand ==1) lts.saveStand(dbconnAC, st, ids, aufs, i+1,iw+1);
               if (ebaum == 1) lts.saveBaum(dbconnAC, st, ids, aufs, i+1,iw+1);
               if (baumart==1) lts.saveSpecies(dbconnAC, st, ids, aufs, i+1);
               st.grow(5, st.ingrowthActive);
               st.sortbyd();
               st.missingData();
               st.descspecies();
            }
            if (ebaum == 2)
                lts.saveBaum(dbconnAC, st, ids, aufs, st.temp_Integer, iw+1);
          }
        }
        
        
        dbconnAC.closeAll();

        dispose();
        // TODO add your handling code here:

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        aktivesDatenfile = jTextField1.getText();
        dbconnAC= new DBConnection(1);     // a class to manage the conection to a database
        dbconnAC.openDBConnection(dbconnAC.ACCESS, 0, aktivesDatenfile, "", "", false, true);
        LoadTreegrossStand lts = new  LoadTreegrossStand(); 
        lts.saveXMLToDB(dbconnAC, st);
        dbconnAC.closeAll();
        dispose();
           
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed
 
    /** check if a point is in polygon , if return is 0 then outside*/
    private int pnpoly(double x, double y, Stand st){
        int i,j,c,m ;
        i=0;j=0;c=0;
        m=st.ncpnt;
        //      System.out.println("pnpoly "+m+" "+x+" y "+y);
        j=m-1;
        for (i=0;i< m;i++){
            if ((((st.cpnt[i].y<=y)&&(y<st.cpnt[j].y)) ||
            ((st.cpnt[j].y<=y)&&(y<st.cpnt[i].y))) &&
            (x<(st.cpnt[j].x-st.cpnt[i].x)*(y-st.cpnt[i].y)/
            (st.cpnt[j].y-st.cpnt[i].y)+st.cpnt[i].x)) {
                if (c==0) {c=1;} 
                else {c=0;}
            }

            j=i;
        }
        return c;
    }  

    
    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
    
}
