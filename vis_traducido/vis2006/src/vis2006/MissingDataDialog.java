/*
 * MissingDataDialog.java
 *
 * Created on 17. August 2005, 12:44
 */

package vis2006;
import treegross.base.*;
import java.text.*;

/**
 *
 * @author  admin
 */
public class MissingDataDialog extends javax.swing.JDialog {
    
    Stand st=null;
    int speciesInFocus=0;
    int heightCurveInFocus=0;
    boolean weiter = false; 
    /** Creates new form MissingDataDialog */
    public MissingDataDialog(java.awt.Frame parent, boolean modal,Stand stl) {
        super(parent, modal);
        st = stl;
        initComponents();
        
        jLabel1.setText("Tipo de �rbol");
        jLabel2.setText("Curva de altura");
        jLabel3.setText("Capa");
        jLabel4.setText("Reemplazar");
        jLabel5.setText("Variaci�n de altura");
        for (int i=0;i<st.nspecies;i++){
           jComboBox1.addItem(st.sp[i].spDef.longName);
        }
        jComboBox3.addItem("todo");
        jComboBox4.addItem("todos los valores Werte");
        jComboBox4.addItem("valores faltantesWerte");
        jComboBox5.addItem("no");
//        jComboBox5.addItem("hinzuf�gen");
        
        speciesChanged();
//        jPanel1.add(grafik);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Reemplace los valores de altura faltantes");
        setModal(true);

        jPanel1.setPreferredSize(new java.awt.Dimension(300, 400));
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridLayout(2, 5));

        jLabel1.setText("jLabel1");
        jPanel2.add(jLabel1);

        jLabel2.setText("jLabel2");
        jPanel2.add(jLabel2);

        jLabel3.setText("jLabel3");
        jPanel2.add(jLabel3);

        jLabel4.setText("jLabel4");
        jPanel2.add(jLabel4);

        jLabel5.setText("jLabel5");
        jPanel2.add(jLabel5);

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jPanel2.add(jComboBox1);

        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        jPanel2.add(jComboBox2);
        jPanel2.add(jComboBox3);
        jPanel2.add(jComboBox4);
        jPanel2.add(jComboBox5);

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.GridLayout(2, 0));

        jPanel4.setLayout(new java.awt.GridLayout(1, 4));

        jLabel6.setText("jLabel6");
        jPanel4.add(jLabel6);

        jLabel7.setText("jLabel7");
        jPanel4.add(jLabel7);

        jLabel8.setText("jLabel8");
        jPanel4.add(jLabel8);

        jLabel9.setText("jLabel9");
        jPanel4.add(jLabel9);

        jPanel3.add(jPanel4);

        jPanel5.setLayout(new java.awt.GridLayout(1, 2));

        jButton1.setText("Renovar curva de altura ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1);

        jButton2.setText("Calcular y generar hoja de resultados");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton2);

        jPanel3.add(jPanel5);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-754)/2, (screenSize.height-578)/2, 754, 578);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        weiter = true;
        dispose();
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        grafikNeu();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
//        speciesChanged();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
    }//GEN-LAST:event_jComboBox2ActionPerformed
    
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    // End of variables declaration//GEN-END:variables

    public void speciesChanged(){
        speciesInFocus = 0;
        for (int i=0; i< st.nspecies; i++)
            if (st.sp[i].spDef.longName.compareTo((String)(jComboBox1.getSelectedItem()))==0)
                speciesInFocus=i;
// check for type of height curve
        int ndh = 0;
        for (int i=0; i< st.ntrees; i++)
            if (st.tr[i].code==st.sp[speciesInFocus].code && st.tr[i].hMeasuredValue > 1.3 && st.tr[i].d > 0) ndh=ndh+1;
        jComboBox2.removeAllItems();
        if (ndh > 4){
           HeightCurve hc = new HeightCurve();
           for (int i=0;i<hc.getNumberOfFunctions();i++){
              jComboBox2.addItem(hc.getHeightCurveName(i));
           }
           jComboBox2.setSelectedIndex(st.sp[speciesInFocus].spDef.heightCurve);   
           heightCurveInFocus=st.sp[speciesInFocus].spDef.heightCurve;
        }
        if (ndh <= 4){
              jComboBox2.addItem("Curva de altura unida");
        }

        if (ndh == 0){
           for (int i=0;i<st.nspecies;i++){
              if ( i != speciesInFocus) jComboBox2.addItem("como tipo "+st.sp[i].code);
        }}
//
        heightCurveRenew();
        grafikErsetzen(st.sp[speciesInFocus].code);
	DecimalFormat f  = new DecimalFormat("0.0000");

        jLabel6.setText("Curva de altura : "+ st.sp[speciesInFocus].heightcurveUsed);
        jLabel7.setText("const : "+ f.format(st.sp[speciesInFocus].heightcurveUsedP0));
        jLabel8.setText("b1 : "+ f.format(st.sp[speciesInFocus].heightcurveUsedP1));
        jLabel9.setText("b2 : "+ f.format(st.sp[speciesInFocus].heightcurveUsedP2));
        
        
    }
    
    public void grafikErsetzen(int code){
        jPanel1.removeAll();
        VisGrafik visgrafik = new VisGrafik(st);
        visgrafik.setGraphType(3,code);visgrafik.neuzeichnen();
//        visgrafik.starten();
        jPanel1.add(visgrafik);
        jPanel1.setVisible(true);

    }
    
    public void heightCurveRenew(){
        for (int i=0;i< st.ntrees;i++){
            st.tr[i].h =0.0;
        }
        GenerateMissingHeights gmh = new GenerateMissingHeights();
        boolean alleWerte = true;
        if (jComboBox4.getSelectedIndex()>0) alleWerte=false;
        gmh.replaceMissingHeights(st,alleWerte);
        
    }
    
    public void grafikNeu(){
        boolean changeHeightCurve=false;
        if (speciesInFocus==jComboBox1.getSelectedIndex() || st.sp[speciesInFocus].spDef.heightCurve!=jComboBox2.getSelectedIndex() ) 
            changeHeightCurve=true;
        speciesInFocus=jComboBox1.getSelectedIndex();
        if (changeHeightCurve==true) st.sp[speciesInFocus].spDef.heightCurve=jComboBox2.getSelectedIndex();
        heightCurveRenew();
        speciesChanged();
       
    }
}
